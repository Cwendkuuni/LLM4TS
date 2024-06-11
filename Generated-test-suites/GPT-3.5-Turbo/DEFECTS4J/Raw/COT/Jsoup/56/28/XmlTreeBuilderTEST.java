
import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;
    private static final String BASE_URI = "http://www.example.com/";

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testDefaultSettings() {
        ParseSettings settings = xmlTreeBuilder.defaultSettings();
        assertEquals(ParseSettings.preserveCase, settings);
    }

    @Test
    public void testParse() {
        String input = "<xml><element attribute=\"value\">Text</element></xml>";
        Document document = xmlTreeBuilder.parse(input, BASE_URI);
        Element rootElement = document.child(0);
        assertEquals("xml", rootElement.tagName());
        Element element = rootElement.child(0);
        assertEquals("element", element.tagName());
        assertEquals("value", element.attr("attribute"));
        assertEquals("Text", element.text());
    }

    @Test
    public void testInitialiseParse() {
        String input = "<xml><element attribute=\"value\">Text</element></xml>";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        xmlTreeBuilder.initialiseParse(input, BASE_URI, errors, settings);
        List<Node> stack = xmlTreeBuilder.getStack();
        Document doc = xmlTreeBuilder.getDoc();
        assertEquals(doc, stack.get(0));
        assertEquals(Document.OutputSettings.Syntax.xml, doc.outputSettings().syntax());
    }

    @Test
    public void testProcessWithStartTagToken() {
        Token token = new Token.StartTag("element", "attribute=\"value\"", true);
        assertTrue(xmlTreeBuilder.process(token));
        List<Node> stack = xmlTreeBuilder.getStack();
        Element element = (Element) stack.get(stack.size() - 1);
        assertEquals("element", element.tagName());
        assertEquals("value", element.attr("attribute"));
    }

    @Test
    public void testProcessWithEndTagToken() {
        xmlTreeBuilder.initialiseParse("<xml><element></element></xml>", BASE_URI, ParseErrorList.noTracking(), ParseSettings.preserveCase);
        Token startTagToken = new Token.StartTag("element", "", false);
        xmlTreeBuilder.process(startTagToken);
        Token endTagToken = new Token.EndTag("element");
        assertTrue(xmlTreeBuilder.process(endTagToken));
        List<Node> stack = xmlTreeBuilder.getStack();
        assertTrue(stack.isEmpty());
    }

    @Test
    public void testProcessWithCommentToken() {
        Token token = new Token.Comment("This is a comment");
        assertTrue(xmlTreeBuilder.process(token));
        List<Node> stack = xmlTreeBuilder.getStack();
        Comment comment = (Comment) stack.get(stack.size() - 1);
        assertEquals("This is a comment", comment.getData());
    }

    @Test
    public void testProcessWithCharacterToken() {
        Token token = new Token.Character("Text");
        assertTrue(xmlTreeBuilder.process(token));
        List<Node> stack = xmlTreeBuilder.getStack();
        TextNode textNode = (TextNode) stack.get(stack.size() - 1);
        assertEquals("Text", textNode.text());
    }

    @Test
    public void testProcessWithDoctypeToken() {
        Token token = new Token.Doctype("", "", "");
        assertTrue(xmlTreeBuilder.process(token));
        List<Node> stack = xmlTreeBuilder.getStack();
        DocumentType documentType = (DocumentType) stack.get(stack.size() - 1);
        assertEquals("", documentType.getName());
        assertEquals("", documentType.getPublicIdentifier());
        assertEquals("", documentType.getSystemIdentifier());
    }

    @Test
    public void testInsertNode() {
        Element element = new Element(Tag.valueOf("element"), BASE_URI);
        xmlTreeBuilder.insertNode(element);
        List<Node> stack = xmlTreeBuilder.getStack();
        Node lastNode = stack.get(stack.size() - 1);
        assertEquals(element, lastNode);
    }

    @Test
    public void testInsertWithStartTagToken() {
        Token.StartTag startTag = new Token.StartTag("element", "attribute=\"value\"", true);
        Element element = xmlTreeBuilder.insert(startTag);
        List<Node> stack = xmlTreeBuilder.getStack();
        assertEquals(element, stack.get(stack.size() - 1));
        assertTrue(startTag.isSelfClosing());
        assertTrue(element.tag().isKnownTag());
        assertTrue(element.tag().selfClosing());
    }

    @Test
    public void testInsertWithCommentToken() {
        Token.Comment commentToken = new Token.Comment("This is a comment");
        xmlTreeBuilder.insert(commentToken);
        List<Node> stack = xmlTreeBuilder.getStack();
        Comment comment = (Comment) stack.get(stack.size() - 1);
        assertEquals("This is a comment", comment.getData());
    }

    @Test
    public void testInsertWithCharacterToken() {
        Token.Character characterToken = new Token.Character("Text");
        xmlTreeBuilder.insert(characterToken);
        List<Node> stack = xmlTreeBuilder.getStack();
        TextNode textNode = (TextNode) stack.get(stack.size() - 1);
        assertEquals("Text", textNode.text());
    }

    @Test
    public void testInsertWithDoctypeToken() {
        Token.Doctype doctypeToken = new Token.Doctype("name", "publicId", "systemId");
        xmlTreeBuilder.insert(doctypeToken);
        List<Node> stack = xmlTreeBuilder.getStack();
        DocumentType doctype = (DocumentType) stack.get(stack.size() - 1);
        assertEquals("name", doctype.getName());
        assertEquals("publicId", doctype.getPublicIdentifier());
        assertEquals("systemId", doctype.getSystemIdentifier());
    }

    @Test
    public void testPopStackToClose() {
        xmlTreeBuilder.insert(new Token.StartTag("element", "", false));
        xmlTreeBuilder.insert(new Token.StartTag("element", "", false));
        xmlTreeBuilder.insert(new Token.StartTag("element", "", false));
        Token.EndTag endTag = new Token.EndTag("element");
        xmlTreeBuilder.popStackToClose(endTag);
        List<Node> stack = xmlTreeBuilder.getStack();
        assertEquals(0, stack.size());
    }

    @Test
    public void testParseFragment() {
        String inputFragment = "<element>Text</element>";
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, BASE_URI, ParseErrorList.noTracking(), ParseSettings.preserveCase);
        Element element = (Element) nodes.get(0);
        assertEquals("element", element.tagName());
        assertEquals("Text", element.text());
    }

}
