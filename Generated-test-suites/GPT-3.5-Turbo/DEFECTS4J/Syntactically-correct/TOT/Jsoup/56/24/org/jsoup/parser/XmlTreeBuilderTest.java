import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setup() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    // Test for default settings
    @Test
    public void testDefaultSettings() {
        ParseSettings settings = xmlTreeBuilder.defaultSettings();
        assertNotNull(settings);
        // Assert that default settings are preserved
        assertNull(settings.parser());
        assertTrue(settings.preserveCase());
    }

    // Test for parsing XML with base URI
    @Test
    public void testParseWithBaseUri() {
        String input = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><root/>";
        String baseUri = "http://example.com";
        Document document = xmlTreeBuilder.parse(input, baseUri);

        assertNotNull(document);
        assertEquals(input, document.outerHtml());
        assertEquals(baseUri, document.baseUri());
    }

    // Test for parsing XML without base URI
    @Test
    public void testParseWithoutBaseUri() {
        String input = "<root/>";
        Document document = xmlTreeBuilder.parse(input, "");

        assertNotNull(document);
        assertEquals(input, document.outerHtml());
        assertEquals("", document.baseUri());
    }

    // Test for initializing XML parsing
    @Test
    public void testInitialiseParse() {
        String input = "<root/>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        ParseSettings settings = new ParseSettings(true, null);

        xmlTreeBuilder.initialiseParse(input, baseUri, errors, settings);

        // Assert that parser is initialized correctly
        assertEquals(input, xmlTreeBuilder.tokeniser.tokeniserState.getReader().toString());
        assertEquals(baseUri, xmlTreeBuilder.tokeniser.baseUri);
        assertEquals(errors, xmlTreeBuilder.tokeniser.errors);
        assertEquals(settings, xmlTreeBuilder.settings);

        // Assert that document is placed on stack
        assertEquals(xmlTreeBuilder.doc, xmlTreeBuilder.stack.get(0));
    }

    // Test for processing XML tokens
    @Test
    public void testProcessToken() {
        // Test StartTag token
        Token.StartTag startTag = new Token.StartTag(Tag.valueOf("root"), null);
        assertTrue(xmlTreeBuilder.process(startTag));
        assertEquals("root", xmlTreeBuilder.currentElement().nodeName());

        // Test EndTag token
        Token.EndTag endTag = new Token.EndTag(Tag.valueOf("root"));
        assertTrue(xmlTreeBuilder.process(endTag));
        assertEquals(0, xmlTreeBuilder.stack.size());

        // Test Comment token
        Token.Comment comment = new Token.Comment("test comment");
        assertTrue(xmlTreeBuilder.process(comment));
        assertEquals("test comment", xmlTreeBuilder.currentElement().childNodes().get(0).outerHtml());

        // Test Character token
        Token.Character character = new Token.Character("test character");
        assertTrue(xmlTreeBuilder.process(character));
        assertEquals("test character", xmlTreeBuilder.currentElement().childNodes().get(0).outerHtml());

        // Test Doctype token
        Token.Doctype doctype = new Token.Doctype("test doctype", "", "", "");
        assertTrue(xmlTreeBuilder.process(doctype));
        assertEquals("test doctype", xmlTreeBuilder.currentElement().childNodes().get(0).outerHtml());

        // Test default token
        Token token = new Token(TokenType.EOF);
        try {
            xmlTreeBuilder.process(token);
            fail("Expected Validate.fail exception");
        } catch (Exception e) {
            assertEquals("Unexpected token type: EOF", e.getMessage());
        }
    }

    // Test for inserting XML node
    @Test
    public void testInsertNode() {
        Element parentElement = new Element(Tag.valueOf("root"), "");
        Node node = new TextNode("test node");

        xmlTreeBuilder.stack.add(parentElement);
        xmlTreeBuilder.insertNode(node);

        assertEquals(node, parentElement.childNodes().get(0));
    }

    // Test for inserting StartTag token
    @Test
    public void testInsertStartTag() {
        Token.StartTag startTag = new Token.StartTag(Tag.valueOf("root"), null);
        Element element = xmlTreeBuilder.insert(startTag);

        assertNotNull(element);
        assertEquals("root", element.nodeName());
        assertEquals(element, xmlTreeBuilder.currentElement());
        assertEquals(element, xmlTreeBuilder.stack.get(0));

        startTag.setSelfClosing(true);
        element = xmlTreeBuilder.insert(startTag);
        assertTrue(element.tag().isKnownTag());
        assertTrue(element.tag().isSelfClosing());
    }

    // Test for inserting Comment token
    @Test
    public void testInsertComment() {
        Token.Comment comment = new Token.Comment("test comment");
        xmlTreeBuilder.insert(comment);

        assertEquals(comment.getData(), xmlTreeBuilder.currentElement().childNodes().get(0).outerHtml());

        // Test for bogus comment
        Token.Comment bogusComment = new Token.Comment("<!DOCTYPE html>");
        xmlTreeBuilder.insert(bogusComment);

        assertEquals("<!DOCTYPE html>", xmlTreeBuilder.currentElement().childNodes().get(1).outerHtml());
    }

    // Test for inserting Character token
    @Test
    public void testInsertCharacter() {
        Token.Character character = new Token.Character("test character");
        xmlTreeBuilder.insert(character);

        assertEquals(character.getData(), xmlTreeBuilder.currentElement().childNodes().get(0).outerHtml());
    }

    // Test for inserting Doctype token
    @Test
    public void testInsertDoctype() {
        Token.Doctype doctype = new Token.Doctype("test doctype", "publicId", "systemId", "baseUri");
        xmlTreeBuilder.insert(doctype);

        assertEquals(doctype.getName(), xmlTreeBuilder.currentElement().childNodes().get(0).outerHtml());
    }

    // Test for popping stack to close EndTag
    @Test
    public void testPopStackToClose() {
        Element parentElement = new Element(Tag.valueOf("root"), "");
        Element childElement = new Element(Tag.valueOf("child"), "");

        xmlTreeBuilder.stack.add(parentElement);
        xmlTreeBuilder.stack.add(childElement);

        Token.EndTag endTag = new Token.EndTag(Tag.valueOf("child"));
        xmlTreeBuilder.popStackToClose(endTag);

        assertTrue(xmlTreeBuilder.stack.isEmpty());
    }

    // Test for parsing XML fragment
    @Test
    public void testParseFragment() {
        String inputFragment = "<root>test</root>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        ParseSettings settings = new ParseSettings(true, null);

        xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors, settings);

        assertNotNull(xmlTreeBuilder.doc);
        assertEquals(inputFragment, xmlTreeBuilder.doc.outerHtml());
    }

}