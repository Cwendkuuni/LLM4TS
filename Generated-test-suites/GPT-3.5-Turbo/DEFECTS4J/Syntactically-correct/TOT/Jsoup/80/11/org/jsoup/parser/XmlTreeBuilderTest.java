import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;

    @Test
    public void testDefaultSettings() {
        xmlTreeBuilder = new XmlTreeBuilder();

        ParseSettings settings = xmlTreeBuilder.defaultSettings();

        assertSame(ParseSettings.preserveCase, settings);
    }

    @Test
    public void testParseReaderString() {
        xmlTreeBuilder = new XmlTreeBuilder();
        String exampleXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><note><to>Tove</to><from>Jani</from><heading>Reminder</heading><body>Don't forget me this weekend!</body></note>";
        Reader reader = new StringReader(exampleXML);
        String baseUri = "http://www.example.com";

        Document document = xmlTreeBuilder.parse(reader, baseUri);

        assertNotNull(document);
        assertEquals(exampleXML, document.toString());
    }

    @Test
    public void testParseStringString() {
        xmlTreeBuilder = new XmlTreeBuilder();
        String exampleXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><note><to>Tove</to><from>Jani</from><heading>Reminder</heading><body>Don't forget me this weekend!</body></note>";
        String baseUri = "http://www.example.com";

        Document document = xmlTreeBuilder.parse(exampleXML, baseUri);

        assertNotNull(document);
        assertEquals(exampleXML, document.toString());
    }

    @Test
    public void testInitialiseParse() {
        xmlTreeBuilder = new XmlTreeBuilder();

        // Prepare test data
        String exampleXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><note><to>Tove</to><from>Jani</from><heading>Reminder</heading><body>Don't forget me this weekend!</body></note>";
        Reader reader = new StringReader(exampleXML);
        String baseUri = "http://www.example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;

        // Perform action
        xmlTreeBuilder.initialiseParse(reader, baseUri, errors, settings);

        // Verify
        assertSame(settings, xmlTreeBuilder.settings);
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertEquals(xmlTreeBuilder.doc, xmlTreeBuilder.stack.get(0));
        assertEquals(Document.OutputSettings.Syntax.xml, xmlTreeBuilder.doc.outputSettings().syntax());
    }

    @Test
    public void testProcessStartTag() {
        xmlTreeBuilder = new XmlTreeBuilder();

        // Prepare test data
        Token token = new Token.StartTag("to");

        // Perform action
        boolean result = xmlTreeBuilder.process(token);

        // Verify
        assertTrue(result);
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertEquals("to", xmlTreeBuilder.currentElement().nodeName());
        assertEquals(xmlTreeBuilder.doc, xmlTreeBuilder.stack.get(0).parent());
    }

    @Test
    public void testProcessEndTag() {
        xmlTreeBuilder = new XmlTreeBuilder();

        // Prepare test data
        xmlTreeBuilder.stack.push(new Element("to"));

        Token token = new Token.EndTag("to");

        // Perform action
        boolean result = xmlTreeBuilder.process(token);

        // Verify
        assertTrue(result);
        assertEquals(0, xmlTreeBuilder.stack.size());
    }

    @Test
    public void testProcessComment() {
        xmlTreeBuilder = new XmlTreeBuilder();

        // Prepare test data
        Token token = new Token.Comment("This is a comment");

        // Perform action
        boolean result = xmlTreeBuilder.process(token);

        // Verify
        assertTrue(result);
        assertEquals(0, xmlTreeBuilder.stack.size());
        assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
        assertEquals("<!--This is a comment-->", xmlTreeBuilder.currentElement().childNodes().get(0).toString());
    }

    @Test
    public void testProcessCharacter() {
        xmlTreeBuilder = new XmlTreeBuilder();

        // Prepare test data
        Token token = new Token.Character("Hello world");

        // Perform action
        boolean result = xmlTreeBuilder.process(token);

        // Verify
        assertTrue(result);
        assertEquals(0, xmlTreeBuilder.stack.size());
        assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
        assertEquals("Hello world", xmlTreeBuilder.currentElement().childNodes().get(0).toString());
    }

    @Test
    public void testProcessDoctype() {
        xmlTreeBuilder = new XmlTreeBuilder();

        // Prepare test data
        Token.Doctype token = new Token.Doctype("html");

        // Perform action
        boolean result = xmlTreeBuilder.process(token);

        // Verify
        assertTrue(result);
        assertEquals(0, xmlTreeBuilder.stack.size());
        assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
        assertEquals("<!DOCTYPE html>", xmlTreeBuilder.currentElement().childNodes().get(0).toString());
    }

    @Test
    public void testInsertNode() {
        xmlTreeBuilder = new XmlTreeBuilder();

        // Prepare test data
        xmlTreeBuilder.stack.push(new Element("root"));

        // Perform action
        xmlTreeBuilder.insertNode(new Element("child"));

        // Verify
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
        assertEquals("child", xmlTreeBuilder.currentElement().childNodes().get(0).nodeName());
    }

    @Test
    public void testInsertStartTag() {
        xmlTreeBuilder = new XmlTreeBuilder();

        // Prepare test data
        Token.StartTag startTag = new Token.StartTag("element");
        startTag.attributes.put("attr", "value");

        // Perform action
        Element element = xmlTreeBuilder.insert(startTag);

        // Verify
        assertNotNull(element);
        assertEquals("element", element.nodeName());
        assertEquals(1, element.attributes().size());
        assertEquals("value", element.attributes().get("attr"));
        assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
        assertEquals(element, xmlTreeBuilder.currentElement().childNodes().get(0));
    }

    @Test
    public void testInsertComment() {
        xmlTreeBuilder = new XmlTreeBuilder();

        // Prepare test data
        Token.Comment commentToken = new Token.Comment("This is a comment");

        // Perform action
        xmlTreeBuilder.insert(commentToken);

        // Verify
        assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
        assertEquals("<!--This is a comment-->", xmlTreeBuilder.currentElement().childNodes().get(0).toString());
    }

    @Test
    public void testInsertCharacter() {
        xmlTreeBuilder = new XmlTreeBuilder();

        // Prepare test data
        Token.Character characterToken = new Token.Character("Hello world");

        // Perform action
        xmlTreeBuilder.insert(characterToken);

        // Verify
        assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
        assertEquals("Hello world", xmlTreeBuilder.currentElement().childNodes().get(0).toString());
    }

    @Test
    public void testInsertDoctype() {
        xmlTreeBuilder = new XmlTreeBuilder();

        // Prepare test data
        Token.Doctype doctypeToken = new Token.Doctype("html");

        // Perform action
        xmlTreeBuilder.insert(doctypeToken);

        // Verify
        assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
        assertEquals("<!DOCTYPE html>", xmlTreeBuilder.currentElement().childNodes().get(0).toString());
    }

    @Test
    public void testPopStackToClose() {
        xmlTreeBuilder = new XmlTreeBuilder();

        // Prepare test data
        xmlTreeBuilder.stack.push(new Element("element1"));
        xmlTreeBuilder.stack.push(new Element("element2"));
        xmlTreeBuilder.stack.push(new Element("element3"));

        Token.EndTag endTag = new Token.EndTag("element2");

        // Perform action
        xmlTreeBuilder.popStackToClose(endTag);

        // Verify
        assertEquals(2, xmlTreeBuilder.stack.size());
        assertEquals("element1", xmlTreeBuilder.stack.get(0).nodeName());
        assertEquals("element3", xmlTreeBuilder.stack.get(1).nodeName());
    }

    @Test
    public void testParseFragment() {
        xmlTreeBuilder = new XmlTreeBuilder();

        // Prepare test data
        String inputFragment = "<element>Hello <b>world</b>!</element>";
        String baseUri = "http://www.example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;

        // Perform action
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors, settings);

        // Verify
        assertEquals(1, nodes.size());
        assertEquals("<element>Hello <b>world</b>!</element>", nodes.get(0).toString());
    }
}