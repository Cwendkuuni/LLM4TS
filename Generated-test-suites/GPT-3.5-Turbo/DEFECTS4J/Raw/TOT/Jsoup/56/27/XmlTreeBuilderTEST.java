import org.jsoup.nodes.*;
import org.jsoup.parser.*;
import org.junit.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class XmlTreeBuilderTest {

    @Test
    public void testDefaultSettings() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        ParseSettings settings = builder.defaultSettings();

        assertTrue(settings.preserveCase());
    }

    @Test
    public void testParseWithoutErrors() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document document = builder.parse("<root><child1>Value1</child1><child2>Value2</child2></root>", "");

        assertNotNull(document);
        assertEquals("<root>\n <child1>Value1</child1>\n <child2>Value2</child2>\n</root>", document.html());
    }

    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element root = new Element(Tag.valueOf("root"), "");
        builder.stack.add(root);

        builder.initialiseParse("", "", new ParseErrorList(), ParseSettings.preserveCase);

        assertTrue(builder.stack.size() == 1);
        assertEquals(Document.OutputSettings.Syntax.xml, builder.doc.outputSettings().syntax());
    }

    @Test
    public void testProcessStartTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("child");

        Element element = builder.insert(startTag);

        assertEquals("child", element.tagName());
        assertFalse(startTag.isSelfClosing());
        assertEquals(1, builder.stack.size());
    }

    @Test
    public void testProcessEndTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.EndTag endTag = new Token.EndTag("child");

        Element root = new Element(Tag.valueOf("root"), "");
        builder.stack.add(root);
        builder.stack.add(new Element(Tag.valueOf("child"), ""));

        builder.popStackToClose(endTag);

        assertTrue(builder.stack.isEmpty());
    }

    @Test
    public void testInsertComment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Comment commentToken = new Token.Comment("This is a comment");

        builder.insert(commentToken);

        assertEquals("<!--This is a comment-->", builder.currentElement().toString());
    }

    @Test
    public void testInsertCharacter() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Character characterToken = new Token.Character("Data");

        builder.insert(characterToken);

        assertEquals("Data", builder.currentElement().text());
    }

    @Test
    public void testInsertDoctype() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Doctype doctype = new Token.Doctype("html", "", "", "");

        builder.insert(doctype);

        assertEquals("<!doctype html>", builder.currentElement().toString());
    }

    @Test
    public void testParseFragment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        List<Node> nodes = builder.parseFragment("<child1>Value1</child1><child2>Value2</child2>", "", new ParseErrorList(), ParseSettings.preserveCase);

        assertEquals(2, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        assertEquals("child1", ((Element) nodes.get(0)).tagName());
        assertEquals("Value1", ((Element) nodes.get(0)).text());
        assertTrue(nodes.get(1) instanceof Element);
        assertEquals("child2", ((Element) nodes.get(1)).tagName());
        assertEquals("Value2", ((Element) nodes.get(1)).text());
    }

}