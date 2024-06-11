import static org.junit.Assert.*;
import org.junit.Test;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;

public class XmlTreeBuilderTest {

    @Test
    public void testParse() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Reader input = new StringReader("<root><child>Test</child></root>");
        Document doc = builder.parse(input, "http://example.com");

        assertEquals("http://example.com", doc.baseUri());
        assertEquals("<root><child>Test</child></root>", doc.html());
    }

    @Test
    public void testParseFragment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();

        List<Node> nodes = builder.parseFragment("<child>Test</child>", "http://example.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);

        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        Element element = (Element) nodes.get(0);
        assertEquals("child", element.tagName());
        assertEquals("Test", element.text());
    }

    @Test
    public void testInsert() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("root", new Attributes());
        builder.insert(startTag);

        assertEquals("root", builder.currentElement().tagName());
    }

    @Test
    public void testInsertNode() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element element = new Element("child");
        builder.insertNode(element);

        assertEquals(element, builder.currentElement().child(0));
    }

    @Test
    public void testProcess() {
        XmlTreeBuilder builder = new XmlTreeBuilder();

        Token.StartTag startTag = new Token.StartTag("root", new Attributes());
        builder.process(startTag);

        assertEquals("root", builder.currentElement().tagName());

        Token.EndTag endTag = new Token.EndTag("root");
        builder.process(endTag);

        assertNull(builder.currentElement());
    }

    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder builder = new XmlTreeBuilder();

        Token.StartTag startTag1 = new Token.StartTag("div", new Attributes());
        Token.StartTag startTag2 = new Token.StartTag("span", new Attributes());
        Token.StartTag startTag3 = new Token.StartTag("p", new Attributes());
        builder.insert(startTag1);
        builder.insert(startTag2);
        builder.insert(startTag3);

        Token.EndTag endTag = new Token.EndTag("span");
        builder.popStackToClose(endTag);

        assertEquals("div", builder.currentElement().tagName());
        assertEquals(2, builder.stack.size());
        assertEquals("div", builder.stack.get(0).tagName());
        assertEquals("p", builder.stack.get(1).tagName());
    }

    @Test
    public void testInsertCharacter() {
        XmlTreeBuilder builder = new XmlTreeBuilder();

        Token.Character character = new Token.Character("Test");
        builder.insert(character);

        assertTrue(builder.currentElement().childNode(0) instanceof TextNode);
        TextNode textNode = (TextNode) builder.currentElement().childNode(0);
        assertEquals("Test", textNode.getWholeText());
    }

    @Test
    public void testInsertComment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();

        Token.Comment comment = new Token.Comment("Test");
        builder.insert(comment);

        assertTrue(builder.currentElement().childNode(0) instanceof Comment);
        Comment commentNode = (Comment) builder.currentElement().childNode(0);
        assertEquals("Test", commentNode.getData());
    }
}