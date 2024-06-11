import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    @Test
    public void testDefaultSettings() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        ParseSettings settings = builder.defaultSettings();
        assertNotNull(settings);
    }

    @Test
    public void testParse() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document doc = builder.parse("<root></root>", "http://example.com");
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
    }

    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.initialiseParse("<root></root>", "http://example.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        // Check if stack is initialized correctly
        assertEquals(1, builder.stack.size());
        assertEquals(builder.doc, builder.stack.get(0));

        // Check if output settings syntax is set to xml
        assertEquals(Document.OutputSettings.Syntax.xml, builder.doc.outputSettings().syntax());
    }

    @Test
    public void testProcess() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token startTag = new Token(Token.TokenType.StartTag, "tag");
        builder.process(startTag);
        // Check if the start tag is correctly inserted
        assertEquals(1, builder.currentElement().childNodes().size());
        assertTrue(builder.currentElement().childNode(0) instanceof Element);
        assertEquals("tag", builder.currentElement().childNode(0).nodeName());
    }

    @Test
    public void testInsertNode() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element element = new Element("tag");
        builder.insertNode(element);
        // Check if the element is correctly inserted into the current element
        assertEquals(1, builder.currentElement().childNodes().size());
        assertEquals(element, builder.currentElement().childNode(0));
    }

    @Test
    public void testInsertStartTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("tag");
        Element element = builder.insert(startTag);
        // Check if the start tag is correctly inserted and returned
        assertEquals(1, builder.currentElement().childNodes().size());
        assertEquals(element, builder.currentElement().childNode(0));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testInsertSelfClosingStartTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("tag");
        startTag.selfClosing = true;
        Element element = builder.insert(startTag);
        // Check if the self-closing start tag is correctly inserted and returned
        assertEquals(0, builder.stack.size());
        assertEquals(element, builder.currentElement());
    }

    @Test
    public void testInsertComment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Comment comment = new Token.Comment("comment");
        builder.insert(comment);
        // Check if the comment is correctly inserted
        assertEquals(1, builder.currentElement().childNodes().size());
        assertTrue(builder.currentElement().childNode(0) instanceof Comment);
        assertEquals("comment", ((Comment) builder.currentElement().childNode(0)).getData());
    }

    @Test
    public void testInsertCharacter() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Character character = new Token.Character("data");
        builder.insert(character);
        // Check if the character data is correctly inserted
        assertEquals(1, builder.currentElement().childNodes().size());
        assertTrue(builder.currentElement().childNode(0) instanceof TextNode);
        assertEquals("data", ((TextNode) builder.currentElement().childNode(0)).getWholeText());
    }

    @Test
    public void testInsertDoctype() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Doctype doctype = new Token.Doctype("doctype", "public", "system");
        builder.insert(doctype);
        // Check if the doctype is correctly inserted
        assertEquals(1, builder.currentElement().childNodes().size());
        assertTrue(builder.currentElement().childNode(0) instanceof DocumentType);
        DocumentType doctypeNode = (DocumentType) builder.currentElement().childNode(0);
        assertEquals("doctype", doctypeNode.nodeName());
        assertEquals("public", doctypeNode.getPublicIdentifier());
        assertEquals("system", doctypeNode.getSystemIdentifier());
    }

    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.stack.add(new Element("tag"));
        builder.stack.add(builder.doc);
        Token.EndTag endTag = new Token.EndTag("tag");
        builder.popStackToClose(endTag);
        // Check if the stack is correctly popped until the matching end tag is found
        assertEquals(1, builder.stack.size());
        assertEquals(builder.doc, builder.stack.get(0));
    }

    @Test
    public void testParseFragment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        List<Node> nodes = builder.parseFragment("<root></root>", "http://example.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        assertEquals("root", ((Element) nodes.get(0)).nodeName());
    }

}