import org.junit.*;
import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String input = "<html><body><h1>Title</h1></body></html>";
        String baseUri = "http://www.example.com";
        ParseErrorList errors = new ParseErrorList();
        builder.initialiseParse(input, baseUri, errors);

        assertEquals(input, builder.getInput());
        assertEquals(baseUri, builder.getBaseUri());
        assertEquals(errors, builder.getErrors());
    }

    @Test
    public void testProcess_StartTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token token = new Token.StartTag("h1");
        builder.process(token);

        Element element = builder.getCurrentElement();
        assertNotNull(element);
        assertEquals("h1", element.tagName());

        List<Node> children = element.childNodes();
        assertEquals(0, children.size());
    }

    @Test
    public void testProcess_EndTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element element = new Element("h1");
        builder.setCurrentElement(element);

        Token token = new Token.EndTag("h1");
        builder.process(token);

        Element currentElement = builder.getCurrentElement();
        assertNull(currentElement);
    }

    @Test
    public void testProcess_Comment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token token = new Token.Comment("This is a comment");
        builder.process(token);

        Element element = builder.getCurrentElement();
        assertNull(element);

        List<Node> children = builder.getDocument().childNodes();
        assertEquals(1, children.size());

        Node commentNode = children.get(0);
        assertTrue(commentNode instanceof Comment);
        assertEquals("This is a comment", ((Comment) commentNode).getData());
    }

    @Test
    public void testProcess_Character() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token token = new Token.Character("Text");
        builder.process(token);

        Element element = builder.getCurrentElement();
        assertNull(element);

        List<Node> children = builder.getDocument().childNodes();
        assertEquals(1, children.size());

        Node textNode = children.get(0);
        assertTrue(textNode instanceof TextNode);
        assertEquals("Text", ((TextNode) textNode).getWholeText());
    }

    @Test
    public void testProcess_Doctype() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token token = new Token.Doctype("html", "-//W3C//DTD HTML 4.0//EN", "http://www.w3.org/TR/REC-html40/strict.dtd");
        builder.process(token);

        Element element = builder.getCurrentElement();
        assertNull(element);

        List<Node> children = builder.getDocument().childNodes();
        assertEquals(1, children.size());

        Node doctypeNode = children.get(0);
        assertTrue(doctypeNode instanceof DocumentType);
        DocumentType doctype = (DocumentType) doctypeNode;
        assertEquals("html", doctype.getName());
        assertEquals("-//W3C//DTD HTML 4.0//EN", doctype.getPublicIdentifier());
        assertEquals("http://www.w3.org/TR/REC-html40/strict.dtd", doctype.getSystemIdentifier());
    }

    @Test
    public void testInsertNode() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element parentElement = new Element("body");
        builder.setCurrentElement(parentElement);

        Element childElement = new Element("h1");
        builder.insertNode(childElement);

        List<Node> children = parentElement.childNodes();
        assertEquals(1, children.size());
        assertSame(childElement, children.get(0));
    }

    @Test
    public void testInsert_StartTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.StartTag tag = new Token.StartTag("h1");
        Element element = builder.insert(tag);

        assertEquals("h1", element.tagName());
        assertEquals("", element.baseUri());
        assertEquals(tag.attributes, element.attributes());
    }

    @Test
    public void testInsert_Comment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Comment comment = new Token.Comment("This is a comment");
        builder.insert(comment);

        Element element = builder.getCurrentElement();
        assertNull(element);

        List<Node> children = builder.getDocument().childNodes();
        assertEquals(1, children.size());

        Node commentNode = children.get(0);
        assertTrue(commentNode instanceof Comment);
        assertEquals("This is a comment", ((Comment) commentNode).getData());
    }

    @Test
    public void testInsert_Character() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Character character = new Token.Character("Text");
        builder.insert(character);

        Element element = builder.getCurrentElement();
        assertNull(element);

        List<Node> children = builder.getDocument().childNodes();
        assertEquals(1, children.size());

        Node textNode = children.get(0);
        assertTrue(textNode instanceof TextNode);
        assertEquals("Text", ((TextNode) textNode).getWholeText());
    }

    @Test
    public void testInsert_Doctype() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Doctype doctype = new Token.Doctype("html", "-//W3C//DTD HTML 4.0//EN", "http://www.w3.org/TR/REC-html40/strict.dtd");
        builder.insert(doctype);

        Element element = builder.getCurrentElement();
        assertNull(element);

        List<Node> children = builder.getDocument().childNodes();
        assertEquals(1, children.size());

        Node doctypeNode = children.get(0);
        assertTrue(doctypeNode instanceof DocumentType);
        DocumentType doctype = (DocumentType) doctypeNode;
        assertEquals("html", doctype.getName());
        assertEquals("-//W3C//DTD HTML 4.0//EN", doctype.getPublicIdentifier());
        assertEquals("http://www.w3.org/TR/REC-html40/strict.dtd", doctype.getSystemIdentifier());
    }

    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element element1 = new Element("h1");
        Element element2 = new Element("h2");
        Element element3 = new Element("h1");
        builder.getStack().add(element1);
        builder.getStack().add(element2);
        builder.getStack().add(element3);

        Token.EndTag endTag = new Token.EndTag("h1");
        builder.popStackToClose(endTag);

        List<Element> stack = builder.getStack();
        assertEquals(1, stack.size());
        assertSame(element2, stack.get(0));
    }

    @Test
    public void testParseFragment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String input = "<h1>Title</h1>";
        String baseUri = "http://www.example.com";
        ParseErrorList errors = new ParseErrorList();
        List<Node> nodes = builder.parseFragment(input, baseUri, errors);

        assertEquals(1, nodes.size());

        Node node = nodes.get(0);
        assertTrue(node instanceof Element);
        Element element = (Element) node;
        assertEquals("h1", element.tagName());
        assertEquals("Title", element.text());
        assertEquals(baseUri, element.baseUri());
        assertNull(element.parent());
    }
}