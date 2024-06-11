import static org.junit.Assert.*;
import org.junit.Test;

public class XmlTreeBuilderTest {

    @Test
    public void testDefaultSettings() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        ParseSettings settings = builder.defaultSettings();

        assertNotNull(settings);
        assertFalse(settings.htmlParser);
        assertTrue(settings.preserveCase);
        assertFalse(settings.inheritCharset);
        assertFalse(settings.xmlParser);
    }

    @Test
    public void testInitialiseParse() {
        // Test with valid input
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Reader input = new StringReader("<xml><tag></tag></xml>");
        String baseUri = "http://example.com";
        Parser parser = new Parser(XmlTreeBuilder);
        builder.initialiseParse(input, baseUri, parser);

        // Assert that document is on the stack
        assertEquals(1, builder.stack.size());
        assertTrue(builder.stack.contains(builder.doc));

        // Assert that output settings syntax is set to xml
        assertEquals(Document.OutputSettings.Syntax.xml, builder.doc.outputSettings().syntax());

        // Test with null input
        try {
            builder.initialiseParse(null, baseUri, parser);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Test passed
        }
    }

    @Test
    public void testParse() {
        // Test with valid input
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Reader input = new StringReader("<xml><tag></tag></xml>");
        String baseUri = "http://example.com";
        Document result = builder.parse(input, baseUri);

        assertNotNull(result);
        assertEquals(baseUri, result.baseUri());

        // Test with invalid input
        try {
            builder.parse("", baseUri);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Test passed
        }
    }

    @Test
    public void testProcess() {
        // Test with StartTag token
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token token = new Token.StartTag("tag");
        assertTrue(builder.process(token));
        // Assert that element was inserted
        assertEquals("tag", builder.currentElement().nodeName());

        // Test with EndTag token
        token = new Token.EndTag("tag");
        assertTrue(builder.process(token));
        // Assert that element was popped from stack
        assertEquals(0, builder.stack.size());

        // Test with Comment token
        token = new Token.Comment("comment");
        assertTrue(builder.process(token));
        // Assert that comment was inserted
        assertEquals("comment", builder.currentElement().childNodes().get(0).outerHtml());

        // Test with Character token
        token = new Token.Character("text");
        assertTrue(builder.process(token));
        // Assert that text node was inserted
        assertEquals("text", builder.currentElement().childNodes().get(0).outerHtml());

        // Test with Doctype token
        token = new Token.Doctype("doctype");
        assertTrue(builder.process(token));
        // Assert that doctype node was inserted
        assertEquals("doctype", builder.currentElement().childNodes().get(0).outerHtml());

        // Test with invalid token
        token = new Token.EOF();
        try {
            builder.process(token);
            fail("Expected ValidateException");
        } catch (ValidateException e) {
            // Test passed
        }
    }

    @Test
    public void testInsertNode() {
        // Test with valid node
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element element = new Element(Tag.valueOf("tag"), "http://example.com", false);
        builder.insertNode(element);
        // Assert that element was inserted
        assertEquals(element, builder.currentElement().childNodes().get(0));

        // Test with null node
        try {
            builder.insertNode(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Test passed
        }
    }

    @Test
    public void testInsertStartTag() {
        // Test with valid start tag
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("tag");
        Element result = builder.insert(startTag);

        assertNotNull(result);
        assertEquals("tag", result.nodeName());
        // Assert that element was inserted
        assertEquals(result, builder.currentElement().childNodes().get(0));

        // Test with self-closing start tag
        startTag = new Token.StartTag("selfClosingTag", true);
        result = builder.insert(startTag);
        // Assert that element was inserted and set as self-closing
        assertEquals(result, builder.currentElement().childNodes().get(1));
        assertTrue(result.tag().isSelfClosing());

        // Test with unknown start tag
        startTag = new Token.StartTag("unknownTag");
        result = builder.insert(startTag);
        // Assert that element was inserted and set as unknown and self-closing
        assertEquals(result, builder.currentElement().childNodes().get(2));
        assertFalse(result.tag().isKnownTag());
        assertTrue(result.tag().isSelfClosing());
    }

    @Test
    public void testInsertComment() {
        // Test with valid comment
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Comment comment = new Token.Comment("comment");
        builder.insert(comment);
        // Assert that comment was inserted
        assertEquals("comment", builder.currentElement().childNodes().get(0).outerHtml());

        // Test with bogus comment representing an xml declaration
        comment = new Token.Comment("!<xml version=\"1.0\">");
        builder.insert(comment);
        // Assert that xml declaration was inserted
        assertEquals("xml version=\"1.0\"", builder.currentElement().childNodes().get(1).outerHtml());

        // Test with invalid bogus comment
        comment = new Token.Comment("!<invalid>");
        builder.insert(comment);
        // Assert that comment was inserted
        assertEquals(2, builder.currentElement().childNodes().size());
    }

    @Test
    public void testInsertCharacter() {
        // Test with valid character
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Character character = new Token.Character("text");
        builder.insert(character);
        // Assert that text node was inserted
        assertEquals("text", builder.currentElement().childNodes().get(0).outerHtml());

        // Test with valid CDATA character
        character = new Token.Character("cdata");
        character.setCData(true);
        builder.insert(character);
        // Assert that CDATA node was inserted
        assertEquals("cdata", builder.currentElement().childNodes().get(1).outerHtml());
        
        // Test with invalid character
        character = new Token.Character("<invalid>");
        try {
            builder.insert(character);
            fail("Expected ValidateException");
        } catch (ValidateException e) {
            // Test passed
        }
    }

    @Test
    public void testInsertDoctype() {
        // Test with valid doctype
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Doctype doctype = new Token.Doctype("doctype");
        builder.insert(doctype);
        // Assert that doctype node was inserted
        assertEquals("doctype", builder.currentElement().childNodes().get(0).outerHtml());

        // Test with invalid doctype
        doctype = new Token.Doctype("<invalid>");
        builder.insert(doctype);
        // Assert that doctype node was inserted
        assertEquals(2, builder.currentElement().childNodes().size());
    }

    @Test
    public void testPopStackToClose() {
        // Test with found element
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.stack.add(new Element(Tag.valueOf("tag"), "http://example.com", false));
        builder.stack.add(new Element(Tag.valueOf("tag"), "http://example.com", false));

        Token.EndTag endTag = new Token.EndTag("tag");
        builder.popStackToClose(endTag);
        // Assert that first occurrence of element was removed from stack
        assertEquals(1, builder.stack.size());

        // Test with not found element
        builder.popStackToClose(endTag);
        // Assert that stack remains the same
        assertEquals(1, builder.stack.size());
    }

    @Test
    public void testParseFragment() {
        // Test with valid input fragment
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String inputFragment = "<tag></tag>";
        String baseUri = "http://example.com";
        Parser parser = new Parser(XmlTreeBuilder);
        List<Node> result = builder.parseFragment(inputFragment, baseUri, parser);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0) instanceof Element);

        // Test with invalid input fragment
        try {
            builder.parseFragment("", baseUri, parser);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Test passed
        }
    }

    @Test
    public void testParseFragmentWithElementContext() {
        // Test with valid input fragment and element context
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element context = new Element(Tag.valueOf("context"), "http://example.com", false);
        String inputFragment = "<tag></tag>";
        String baseUri = "http://example.com";
        Parser parser = new Parser(XmlTreeBuilder);
        List<Node> result = builder.parseFragment(inputFragment, context, baseUri, parser);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0) instanceof Element);
    }

}