import org.jsoup.parser.*;
import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Before;
import org.junit.Test;
import java.io.StringReader;
import java.util.List;
import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;
    
    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }
    
    @Test
    public void testDefaultSettings() {
        ParseSettings expectedSettings = ParseSettings.preserveCase;
        ParseSettings actualSettings = xmlTreeBuilder.defaultSettings();
        assertEquals(expectedSettings, actualSettings);
    }
    
    @Test
    public void testParseReaderString() {
        String input = "<html></html>";
        String baseUri = "";
        Document expected = Jsoup.parse(input, baseUri, Parser.xmlParser());
        Document actual = xmlTreeBuilder.parse(new StringReader(input), baseUri);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testParseStringString() {
        String input = "<html></html>";
        String baseUri = "";
        Document expected = Jsoup.parse(input, baseUri, Parser.xmlParser());
        Document actual = xmlTreeBuilder.parse(input, baseUri);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testInitialiseParse() {
        Reader input = new StringReader("<html></html>");
        String baseUri = "";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        
        xmlTreeBuilder.initialiseParse(input, baseUri, errors, settings);
        
        List<Node> stack = xmlTreeBuilder.getStack();
        Document doc = xmlTreeBuilder.getDoc();
        
        assertTrue(stack.contains(doc));
        assertEquals(Document.OutputSettings.Syntax.xml, doc.outputSettings().syntax());
    }
    
    @Test
    public void testProcessStartTag() {
        Token.StartTag startTag = new Token.StartTag("div");
        startTag.attributes().put("class", "test");
        
        Element expected = doc.createElement("div");
        expected.attributes().put("class", "test");
        
        Element actual = xmlTreeBuilder.insert(startTag);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testProcessEndTag() {
        Token.EndTag endTag = new Token.EndTag("div");
        
        Element div = doc.createElement("div");
        xmlTreeBuilder.getStack().add(div);
        
        xmlTreeBuilder.process(endTag);
        
        assertFalse(xmlTreeBuilder.getStack().contains(div));
    }
    
    @Test
    public void testProcessComment() {
        Token.Comment commentToken = new Token.Comment("This is a comment");
        
        Comment expected = new Comment("This is a comment");
        Comment actual = xmlTreeBuilder.insert(commentToken);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testProcessCharacter() {
        Token.Character charToken = new Token.Character("This is a character");
        
        TextNode expected = new TextNode("This is a character");
        TextNode actual = xmlTreeBuilder.insert(charToken);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testProcessDoctype() {
        Token.Doctype doctype = new Token.Doctype("html");
        doctype.setPublicIdentifier("-//W3C//DTD HTML 4.01//EN");
        doctype.setSystemIdentifier("http://www.w3.org/TR/html4/strict.dtd");
        
        DocumentType expected = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd");
        DocumentType actual = xmlTreeBuilder.insert(doctype);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testPopStackToClose() {
        Token.EndTag endTag = new Token.EndTag("div");
        
        Element div1 = doc.createElement("div");
        Element div2 = doc.createElement("div");
        Element div3 = doc.createElement("div");
        
        xmlTreeBuilder.getStack().add(div1);
        xmlTreeBuilder.getStack().add(div2);
        xmlTreeBuilder.getStack().add(div3);
        
        xmlTreeBuilder.popStackToClose(endTag);
        
        assertFalse(xmlTreeBuilder.getStack().contains(div1));
        assertFalse(xmlTreeBuilder.getStack().contains(div2));
        assertFalse(xmlTreeBuilder.getStack().contains(div3));
    }
    
    @Test
    public void testParseFragment() {
        String inputFragment = "<div class=\"container\"><p>This is a paragraph.</p></div>";
        String baseUri = "";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        
        List<Node> expected = Jsoup.parse(inputFragment, baseUri, Parser.xmlParser()).childNodes();
        List<Node> actual = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors, settings);
        
        assertEquals(expected, actual);
    }
}