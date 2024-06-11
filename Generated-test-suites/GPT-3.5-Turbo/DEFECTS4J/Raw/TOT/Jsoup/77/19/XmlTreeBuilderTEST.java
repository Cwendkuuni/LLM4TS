import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testDefaultSettings() {
        ParseSettings settings = xmlTreeBuilder.defaultSettings();

        assertNotNull(settings);
        assertEquals(ParseSettings.preserveCase, settings);
    }

    @Test
    public void testParseWithReaderAndBaseUri() {
        Reader input = new StringReader("<xml><tag>example</tag></xml>");
        String baseUri = "https://www.example.com";

        Document document = xmlTreeBuilder.parse(input, baseUri);

        assertNotNull(document);
        assertEquals("https://www.example.com", document.baseUri());
    }

    @Test
    public void testParseWithStringAndBaseUri() {
        String input = "<xml><tag>example</tag></xml>";
        String baseUri = "https://www.example.com";

        Document document = xmlTreeBuilder.parse(input, baseUri);

        assertNotNull(document);
        assertEquals("https://www.example.com", document.baseUri());
    }

    @Test
    public void testInitialiseParse() {
        Reader input = new StringReader("<xml><tag>example</tag></xml>");
        String baseUri = "https://www.example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;

        xmlTreeBuilder.initialiseParse(input, baseUri, errors, settings);

        // Add assertions here to test the logic in initialiseParse method
    }

    @Test
    public void testProcessStartTag() {
        Token.StartTag startTag = new Token.StartTag("tag");
        startTag.attributes().put("attr1", "value1");
        startTag.attributes().put("attr2", "value2");
        
        Element element = xmlTreeBuilder.insert(startTag);

        assertNotNull(element);
        assertEquals("tag", element.tagName());
        assertEquals(2, element.attributes().size()); // Check if attributes are properly added
        assertEquals("value1", element.attr("attr1"));
        assertEquals("value2", element.attr("attr2"));
    }

    @Test
    public void testProcessEndTag() {
        Token.EndTag endTag = new Token.EndTag("tag");

        xmlTreeBuilder.popStackToClose(endTag);

        // Add assertions here to test the logic in popStackToClose method
    }

    @Test
    public void testProcessComment() {
        Token.Comment commentToken = new Token.Comment("Example comment");

        xmlTreeBuilder.insert(commentToken);

        // Add assertions here to test the logic in insert method for comment token
    }

    @Test
    public void testProcessCharacter() {
        Token.Character characterToken = new Token.Character("Example text data");

        xmlTreeBuilder.insert(characterToken);

        // Add assertions here to test the logic in insert method for character token
    }

    @Test
    public void testProcessDoctype() {
        Token.Doctype doctype = new Token.Doctype("html", "publicId", "systemId", "pubSysKey");

        xmlTreeBuilder.insert(doctype);

        // Add assertions here to test the logic in insert method for doctype token
    }

    @Test
    public void testParseFragment() {
        String inputFragment = "<tag>example</tag>";
        String baseUri = "https://www.example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;

        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors, settings);

        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        assertEquals("tag", ((Element) nodes.get(0)).tagName());
        assertEquals("example", ((Element) nodes.get(0)).text());
    }

}