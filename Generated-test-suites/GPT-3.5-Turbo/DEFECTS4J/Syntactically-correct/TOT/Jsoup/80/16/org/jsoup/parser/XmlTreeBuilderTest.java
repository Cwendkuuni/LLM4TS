import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.jsoup.parser.*;

import java.io.StringReader;
import java.util.List;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setup() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testDefaultSettings() {
        ParseSettings defaultSettings = xmlTreeBuilder.defaultSettings();
        
        // Check if default settings are preserveCase
        Assert.assertEquals(ParseSettings.preserveCase, defaultSettings);
    }
    
    @Test
    public void testParseReaderAndBaseUri() {
        String input = "<tag>content</tag>";
        String baseUri = "http://example.com";
        
        Document document = xmlTreeBuilder.parse(new StringReader(input), baseUri);
        
        // Check if the parsed document is correct
        Assert.assertEquals("<tag>content</tag>", document.outerHtml());
    }
    
    @Test
    public void testParseStringAndBaseUri() {
        String input = "<tag>content</tag>";
        String baseUri = "http://example.com";
        
        Document document = xmlTreeBuilder.parse(input, baseUri);
        
        // Check if the parsed document is correct
        Assert.assertEquals("<tag>content</tag>", document.outerHtml());
    }
    
    @Test
    public void testInitialiseParse() {
        String input = "<tag>content</tag>";
        String baseUri = "http://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        
        xmlTreeBuilder.initialiseParse(new StringReader(input), baseUri, errors, settings);
        
        // Check if the initial parsing is done correctly
        Assert.assertEquals(ParseSettings.preserveCase, xmlTreeBuilder.settings);
        Assert.assertEquals(Document.OutputSettings.Syntax.xml, xmlTreeBuilder.doc.outputSettings().syntax());
    }
    
    @Test
    public void testProcessTokenForStartTag() {
        Token token = new Token.StartTag("tag");
        Token.StartTag startTag = token.asStartTag();
        startTag.attributes.put("attr", "value");
        
        Element element = xmlTreeBuilder.insert(startTag);
        
        // Check if the start tag is inserted correctly and attributes are preserved
        Assert.assertEquals("tag", element.tagName());
        Assert.assertEquals("value", element.attr("attr"));
    }

    @Test
    public void testProcessTokenForEndTag() {
        Token token = new Token.EndTag("tag");
        Token.EndTag endTag = token.asEndTag();

        xmlTreeBuilder
          .stack.add(new Element(Tag.valueOf("tag"), "http://example.com", ParseSettings.preserveCase));
        
        xmlTreeBuilder.popStackToClose(endTag);
        
        // Check if the end tag is correctly removed from the stack
        Assert.assertEquals(0, xmlTreeBuilder.stack.size());
    }

    @Test
    public void testProcessTokenForComment() {
        Token token = new Token.Comment("comment");
        Token.Comment commentToken = token.asComment();
        
        xmlTreeBuilder.insert(commentToken);
        
        // Check if the comment is correctly inserted
        Assert.assertEquals(1, xmlTreeBuilder.doc.childNodes().size());
        Assert.assertEquals("#comment", xmlTreeBuilder.doc.childNode(0).nodeName());
        Assert.assertEquals("comment", xmlTreeBuilder.doc.childNode(0).toString());
    }

    @Test
    public void testProcessTokenForCharacter() {
        Token token = new Token.Character("data");
        Token.Character characterToken = token.asCharacter();
        
        xmlTreeBuilder.insert(characterToken);
        
        // Check if the character token is correctly inserted
        Assert.assertEquals(1, xmlTreeBuilder.doc.childNodes().size());
        Assert.assertEquals("#text", xmlTreeBuilder.doc.childNode(0).nodeName());
        Assert.assertEquals("data", xmlTreeBuilder.doc.childNode(0).toString());
    }

    @Test
    public void testProcessTokenForDoctype() {
        Token token = new Token.Doctype("name", "public", "system", "pubsyskey");
        Token.Doctype doctypeToken = token.asDoctype();
        
        xmlTreeBuilder.insert(doctypeToken);
        
        // Check if the doctype token is correctly inserted
        Assert.assertEquals(1, xmlTreeBuilder.doc.childNodes().size());
        Assert.assertEquals("!DOCTYPE", xmlTreeBuilder.doc.childNode(0).nodeName());
        Assert.assertEquals("name", xmlTreeBuilder.doc.childNode(0).toString());
    }

    @Test
    public void testParseFragment() {
        String inputFragment = "<tag>content</tag>";
        String baseUri = "http://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors, settings);
        
        // Check if the fragment is correctly parsed and returned as a list of nodes
        Assert.assertEquals(1, nodes.size());
        Assert.assertEquals("tag", nodes.get(0).nodeName());
        Assert.assertEquals("content", nodes.get(0).childNode(0).toString());
    }
    
}