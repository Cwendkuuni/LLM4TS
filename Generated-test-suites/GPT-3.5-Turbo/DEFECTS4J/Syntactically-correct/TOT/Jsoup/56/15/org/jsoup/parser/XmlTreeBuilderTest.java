import static org.junit.Assert.*;
import org.junit.Test;
import java.util.List;
import org.jsoup.helper.Validate;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Parser;
import org.jsoup.parser.XmlTreeBuilder;

public class XmlTreeBuilderTest {

    @Test
    public void testDefaultSettings() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        ParseSettings settings = treeBuilder.defaultSettings();
      
        assertNotNull(settings);
        assertEquals(ParseSettings.preserveCase, settings);
    }
  
    @Test
    public void testParse() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        String input = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><root></root>";
        String baseUri = "http://example.com";
        Document doc = treeBuilder.parse(input, baseUri);
      
        assertNotNull(doc);
        assertEquals("<root></root>", doc.outerHtml());
    }
  
    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        String input = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><root></root>";
        String baseUri = "http://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
      
        treeBuilder.initialiseParse(input, baseUri, errors, settings);
        Document doc = treeBuilder.getDocument();
      
        assertNotNull(doc);
        assertEquals("<root></root>", doc.outerHtml());
        assertEquals(Document.OutputSettings.Syntax.xml, doc.outputSettings().syntax());
    }
  
    @Test
    public void testProcessWithStartTag() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Token token = new Token.StartTag("element", null, false);
      
        assertTrue(treeBuilder.process(token));
        assertEquals("<element></element>", treeBuilder.getDocument().outerHtml());
    }
  
    @Test
    public void testProcessWithEndTag() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Token token = new Token.EndTag("element");
      
        assertTrue(treeBuilder.process(token));
        assertNull(treeBuilder.getDocument().children());
    }
  
    @Test
    public void testProcessWithComment() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Token token = new Token.Comment("This is a comment");
      
        assertTrue(treeBuilder.process(token));
        assertEquals("<!--This is a comment-->", treeBuilder.getDocument().outerHtml());
    }
  
    @Test
    public void testProcessWithCharacter() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Token token = new Token.Character("Hello World");
      
        assertTrue(treeBuilder.process(token));
        assertEquals("Hello World", treeBuilder.getDocument().text());
    }
  
    @Test
    public void testProcessWithDoctype() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Token token = new Token.Doctype("html", "", "", "");
      
        assertTrue(treeBuilder.process(token));
        assertEquals("<!DOCTYPE html >", treeBuilder.getDocument().outerHtml());
    }
  
    @Test(expected = IllegalArgumentException.class)
    public void testProcessWithUnexpectedToken() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Token token = new Token.Data("");
      
        treeBuilder.process(token);
    }
  
    @Test
    public void testInsertNode() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Document doc = new Document("");
        Element element = new Element("element");
      
        treeBuilder.setDocument(doc);
        treeBuilder.insertNode(element);
      
        assertEquals("<element></element>", doc.outerHtml());
    }
  
    @Test
    public void testInsertStartTag() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("element", null, false);
      
        Element element = treeBuilder.insert(startTag);
      
        assertNotNull(element);
        assertEquals("<element></element>", treeBuilder.getDocument().outerHtml());
    }
  
    @Test
    public void testInsertSelfClosingStartTag() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("element", null, true);
      
        Element element = treeBuilder.insert(startTag);
      
        assertNotNull(element);
        assertEquals("<element/>", treeBuilder.getDocument().outerHtml());
    }
  
    @Test
    public void testInsertComment() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Token.Comment comment = new Token.Comment("This is a comment");
      
        treeBuilder.insert(comment);
      
        assertEquals("<!--This is a comment-->", treeBuilder.getDocument().outerHtml());
    }
  
    @Test
    public void testInsertCharacter() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Token.Character character = new Token.Character("Hello World");
      
        treeBuilder.insert(character);
      
        assertEquals("Hello World", treeBuilder.getDocument().text());
    }
  
    @Test
    public void testInsertDoctype() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Token.Doctype doctype = new Token.Doctype("html", "", "", "");
      
        treeBuilder.insert(doctype);
      
        assertEquals("<!DOCTYPE html >", treeBuilder.getDocument().outerHtml());
    }
  
    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Token.EndTag endTag = new Token.EndTag("element");
      
        treeBuilder.insert(new Token.StartTag("element", null, false));
        treeBuilder.insert(new Token.StartTag("element", null, false));
        treeBuilder.insert(new Token.StartTag("element", null, false));
        treeBuilder.popStackToClose(endTag);
      
        assertNull(treeBuilder.getDocument().children());
    }
  
    @Test
    public void testParseFragment() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        String inputFragment = "<element></element>";
        String baseUri = "http://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
      
        List<Node> nodes = treeBuilder.parseFragment(inputFragment, baseUri, errors, settings);
      
        assertNotNull(nodes);
        assertEquals("<element></element>", nodes.get(0).outerHtml());
    }
}