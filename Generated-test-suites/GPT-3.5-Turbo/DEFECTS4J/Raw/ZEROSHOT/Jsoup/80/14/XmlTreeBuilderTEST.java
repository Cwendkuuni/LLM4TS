import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.*;

import java.io.Reader;
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
        ParseSettings settings = xmlTreeBuilder.defaultSettings();
        assertNotNull(settings);
        assertEquals(ParseSettings.preserveCase, settings);
    }

    @Test
    public void testParseWithReaderAndBaseUri() {
        String testHtml = "<html><head></head><body>Test</body></html>";
        Document document = xmlTreeBuilder.parse(new StringReader(testHtml), "http://example.com");
        assertNotNull(document);
        assertEquals("<html>\n <head></head>\n <body>Test</body>\n</html>", document.toString());
    }

    @Test
    public void testParseWithStringAndBaseUri() {
        String testHtml = "<html><head></head><body>Test</body></html>";
        Document document = xmlTreeBuilder.parse(testHtml, "http://example.com");
        assertNotNull(document);
        assertEquals("<html>\n <head></head>\n <body>Test</body>\n</html>", document.toString());
    }

    @Test
    public void testInitialiseParse() {
        StringReader input = new StringReader("<html><head></head><body>Test</body></html>");
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        ParseSettings settings = new ParseSettings(true, true);

        xmlTreeBuilder.initialiseParse(input, baseUri, errors, settings);

        // Check that the document is placed onto the stack
        List<Node> stack = xmlTreeBuilder.getStack();
        assertEquals(1, stack.size());
        assertTrue(stack.get(0) instanceof Document);

        // Check that the document's output settings are set to xml
        Document doc = (Document) stack.get(0);
        assertEquals(Document.OutputSettings.Syntax.xml, doc.outputSettings().syntax());
    }

    @Test
    public void testProcessStartTag() {
        Token.StartTag startTag = new Token.StartTag("div");
        startTag.attributes().put("id", "test");

        xmlTreeBuilder.process(startTag);

        List<Node> stack = xmlTreeBuilder.getStack();
        assertEquals(2, stack.size());

        assertTrue(stack.get(1) instanceof Element);
        Element element = (Element) stack.get(1);
        assertEquals("div", element.nodeName());
        assertEquals("test", element.id());
    }

    @Test
    public void testProcessEndTag() {
        Token.EndTag endTag = new Token.EndTag("div");

        // Add a div to the stack
        List<Node> stack = xmlTreeBuilder.getStack();
        stack.add(new Element(Tag.valueOf("div"), "http://example.com"));

        xmlTreeBuilder.process(endTag);

        assertEquals(1, stack.size());
    }

    @Test
    public void testProcessComment() {
        Token.Comment comment = new Token.Comment("Test comment");

        xmlTreeBuilder.process(comment);

        List<Node> stack = xmlTreeBuilder.getStack();
        assertEquals(2, stack.size());

        assertTrue(stack.get(1) instanceof Comment);
        Comment insertedComment = (Comment) stack.get(1);
        assertEquals("Test comment", insertedComment.getData());
    }

    @Test
    public void testProcessCharacter() {
        Token.Character character = new Token.Character("Test text");

        xmlTreeBuilder.process(character);

        List<Node> stack = xmlTreeBuilder.getStack();
        assertEquals(2, stack.size());

        assertTrue(stack.get(1) instanceof TextNode);
        TextNode insertedTextNode = (TextNode) stack.get(1);
        assertEquals("Test text", insertedTextNode.getWholeText());
    }

    @Test
    public void testProcessDoctype() {
        Token.Doctype doctype = new Token.Doctype("html", "-//W3C//DTD HTML 4.01 Transitional//EN", "http://www.w3.org/TR/html4/loose.dtd");

        xmlTreeBuilder.process(doctype);

        List<Node> stack = xmlTreeBuilder.getStack();
        assertEquals(2, stack.size());

        assertTrue(stack.get(1) instanceof DocumentType);
        DocumentType insertedDoctype = (DocumentType) stack.get(1);
        assertEquals("html", insertedDoctype.nodeName());
        assertEquals("-//W3C//DTD HTML 4.01 Transitional//EN", insertedDoctype.attr("publicId"));
        assertEquals("http://www.w3.org/TR/html4/loose.dtd", insertedDoctype.attr("systemId"));
    }

    @Test
    public void testInsertNode() {
        Element element = new Element(Tag.valueOf("div"), "http://example.com");
        xmlTreeBuilder.insertNode(element);

        List<Node> stack = xmlTreeBuilder.getStack();
        assertEquals(2, stack.size());
        assertSame(element, stack.get(1));
    }

    @Test
    public void testInsertStartTag() {
        Token.StartTag startTag = new Token.StartTag("div");

        Element element = xmlTreeBuilder.insert(startTag);

        List<Node> stack = xmlTreeBuilder.getStack();
        assertEquals(2, stack.size());
        assertSame(element, stack.get(1));
        assertEquals("div", element.nodeName());
    }

    @Test
    public void testInsertComment() {
        Token.Comment commentToken = new Token.Comment("Test comment");

        xmlTreeBuilder.insert(commentToken);

        List<Node> stack = xmlTreeBuilder.getStack();
        assertEquals(2, stack.size());

        assertTrue(stack.get(1) instanceof Comment);
        Comment insertedComment = (Comment) stack.get(1);
        assertEquals("Test comment", insertedComment.getData());
    }

    @Test
    public void testInsertCharacter() {
        Token.Character character = new Token.Character("Test text");

        xmlTreeBuilder.insert(character);

        List<Node> stack = xmlTreeBuilder.getStack();
        assertEquals(2, stack.size());

        assertTrue(stack.get(1) instanceof TextNode);
        TextNode insertedTextNode = (TextNode) stack.get(1);
        assertEquals("Test text", insertedTextNode.getWholeText());
    }

    @Test
    public void testInsertDoctype() {
        Token.Doctype doctype = new Token.Doctype("html", "-//W3C//DTD HTML 4.01 Transitional//EN", "http://www.w3.org/TR/html4/loose.dtd");

        xmlTreeBuilder.insert(doctype);

        List<Node> stack = xmlTreeBuilder.getStack();
        assertEquals(2, stack.size());

        assertTrue(stack.get(1) instanceof DocumentType);
        DocumentType insertedDoctype = (DocumentType) stack.get(1);
        assertEquals("html", insertedDoctype.nodeName());
        assertEquals("-//W3C//DTD HTML 4.01 Transitional//EN", insertedDoctype.attr("publicId"));
        assertEquals("http://www.w3.org/TR/html4/loose.dtd", insertedDoctype.attr("systemId"));
    }

    @Test
    public void testPopStackToClose() {
        List<Node> stack = xmlTreeBuilder.getStack();

        Element div1 = new Element(Tag.valueOf("div"), "http://example.com");
        Element div2 = new Element(Tag.valueOf("div"), "http://example.com");

        stack.add(div2);
        stack.add(div1);

        assertEquals(3, stack.size());

        xmlTreeBuilder.popStackToClose(new Token.EndTag("div"));

        assertEquals(2, stack.size());
        assertSame(div2, stack.get(1));
    }

    @Test
    public void testParseFragment() {
        String inputFragment = "<div>Test fragment</div>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        ParseSettings settings = new ParseSettings(true, true);

        List<Node> fragmentNodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors, settings);

        assertNotNull(fragmentNodes);
        assertEquals(1, fragmentNodes.size());

        Node node = fragmentNodes.get(0);
        assertTrue(node instanceof Element);
        Element element = (Element) node;
        assertEquals("div", element.nodeName());
        assertEquals("Test fragment", element.text());
    }
}