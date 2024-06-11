import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Before;
import org.junit.Test;
import java.util.List;
import static org.junit.Assert.*;

public class XmlTreeBuilderTest {
    
    private XmlTreeBuilder xmlTreeBuilder;
    private Document doc;
    
    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
        doc = new Document("");
    }
    
    @Test
    public void testDefaultSettings() {
        ParseSettings settings = xmlTreeBuilder.defaultSettings();
        assertNotNull(settings);
    }
    
    @Test
    public void testParse() {
        String input = "<example>Test</example>";
        String baseUri = "https://www.example.com";
        Document document = xmlTreeBuilder.parse(input, baseUri);
        assertNotNull(document);
    }
    
    @Test
    public void testInitialiseParse() {
        String input = "<example>Test</example>";
        String baseUri = "https://www.example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        xmlTreeBuilder.initialiseParse(input, baseUri, errors, settings);
        Element currentElement = xmlTreeBuilder.currentElement();
        assertNotNull(currentElement);
    }
    
    @Test
    public void testProcessStartTag() {
        Token.StartTag startTag = new Token.StartTag("example");
        xmlTreeBuilder.insert(startTag);
        Element currentElement = xmlTreeBuilder.currentElement();
        assertNotNull(currentElement);
        assertEquals("example", currentElement.nodeName());
    }
    
    @Test
    public void testProcessEndTag() {
        Token.EndTag endTag = new Token.EndTag("example");
        xmlTreeBuilder.popStackToClose(endTag);
        Element currentElement = xmlTreeBuilder.currentElement();
        assertNull(currentElement);
    }
    
    @Test
    public void testProcessComment() {
        Token.Comment commentToken = new Token.Comment("Test comment");
        xmlTreeBuilder.insert(commentToken);
        Element currentElement = xmlTreeBuilder.currentElement();
        assertNotNull(currentElement);
        assertEquals("<!--Test comment-->", currentElement.toString());
    }
    
    @Test
    public void testProcessCharacter() {
        Token.Character characterToken = new Token.Character("Test character");
        xmlTreeBuilder.insert(characterToken);
        Element currentElement = xmlTreeBuilder.currentElement();
        assertNotNull(currentElement);
        assertEquals("Test character", currentElement.text());
    }
    
    @Test
    public void testProcessDoctype() {
        Token.Doctype doctype = new Token.Doctype("example", "public", "system");
        xmlTreeBuilder.insert(doctype);
        Element currentElement = xmlTreeBuilder.currentElement();
        assertNotNull(currentElement);
        assertEquals("<!DOCTYPE example PUBLIC \"public\" \"system\">", currentElement.toString());
    }
    
    @Test
    public void testPopStackToCloseNotFound() {
        Token.EndTag endTag = new Token.EndTag("example");
        xmlTreeBuilder.popStackToClose(endTag);
        Element currentElement = xmlTreeBuilder.currentElement();
        assertNull(currentElement);
    }
    
    @Test
    public void testPopStackToClose() {
        Token.StartTag startTag = new Token.StartTag("example");
        xmlTreeBuilder.insert(startTag);
        Token.EndTag endTag = new Token.EndTag("example");
        xmlTreeBuilder.popStackToClose(endTag);
        Element currentElement = xmlTreeBuilder.currentElement();
        assertNull(currentElement);
    }
    
    @Test
    public void testParseFragment() {
        String inputFragment = "<example>Test</example>";
        String baseUri = "https://www.example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors, settings);
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        Element element = (Element) nodes.get(0);
        assertEquals("example", element.nodeName());
    }
}