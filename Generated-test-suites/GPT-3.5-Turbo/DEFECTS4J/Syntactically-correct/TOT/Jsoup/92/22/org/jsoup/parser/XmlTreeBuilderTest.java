import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.junit.Before;
import org.junit.Test;

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
    public void testParse_Reader_BaseUri_NormalInput() {
        String input = "<root><element>content</element></root>";
        String baseUri = "http://example.com";
        Document document = xmlTreeBuilder.parse(new StringReader(input), baseUri);
        assertNotNull(document);
        assertEquals("http://example.com", document.baseUri());
        Element root = document.getElementsByTag("root").first();
        assertNotNull(root);
        Element element = root.getElementsByTag("element").first();
        assertNotNull(element);
        assertEquals("content", element.text());
    }

    @Test
    public void testParse_Reader_BaseUri_EmptyInput() {
        String input = "";
        String baseUri = "http://example.com";
        Document document = xmlTreeBuilder.parse(new StringReader(input), baseUri);
        assertNotNull(document);
        assertEquals("http://example.com", document.baseUri());
        Element root = document.getElementsByTag("root").first();
        assertNull(root);
    }

    @Test
    public void testInsertNode() {
        Document document = new Document("");
        Element root = new Element("root");
        document.appendChild(root);
        Node childNode = new Comment("comment");
        xmlTreeBuilder.insertNode(childNode);
        assertEquals(childNode, root.childNode(0));
    }

    @Test
    public void testInsert_StartTag_SelfClosing() {
        Token.StartTag startTag = new Token.StartTag("empty");
        startTag.setSelfClosing();
        Element element = xmlTreeBuilder.insert(startTag);
        assertNotNull(element);
        assertEquals("empty", element.tagName());
        assertTrue(element.isBlock());
        assertTrue(element.isKnownTag());
        assertTrue(element.isSelfClosing());
    }

    @Test
    public void testInsert_StartTag_NotSelfClosing() {
        Token.StartTag startTag = new Token.StartTag("div");
        Element element = xmlTreeBuilder.insert(startTag);
        assertNotNull(element);
        assertEquals("div", element.tagName());
        assertTrue(element.isBlock());
        assertTrue(element.isKnownTag());
        assertFalse(element.isSelfClosing());
    }

    @Test
    public void testInsert_Comment_Normal() {
        Token.Comment commentToken = new Token.Comment("comment");
        xmlTreeBuilder.insert(commentToken);
        Element root = xmlTreeBuilder.currentElement();
        assertEquals(1, root.childNodes().size());
        assertEquals(commentToken, root.childNode(0));
    }

    @Test
    public void testInsert_Comment_XmlDeclaration() {
        String data = "<!DOCTYPE html>";
        Token.Comment commentToken = new Token.Comment(data);
        xmlTreeBuilder.insert(commentToken);
        Element root = xmlTreeBuilder.currentElement();
        assertEquals(1, root.childNodes().size());
        assertEquals(data, root.childNode(0).toString());
    }

    @Test
    public void testInsert_Character_NormalTextNode() {
        Token.Character token = new Token.Character("text");
        xmlTreeBuilder.insert(token);
        Element root = xmlTreeBuilder.currentElement();
        assertEquals(1, root.childNodes().size());
        assertEquals("text", root.childNode(0).toString());
    }

    @Test
    public void testInsert_Character_CDataNode() {
        Token.Character token = new Token.Character("data");
        token.setCData();
        xmlTreeBuilder.insert(token);
        Element root = xmlTreeBuilder.currentElement();
        assertEquals(1, root.childNodes().size());
        assertEquals("data", root.childNode(0).toString());
    }

    @Test
    public void testInsert_Doctype_Normal() {
        Token.Doctype doctype = new Token.Doctype("html", "-//W3C//DTD HTML 4.01 Transitional//EN", "http://www.w3.org/TR/html4/loose.dtd");
        xmlTreeBuilder.insert(doctype);
        Element root = xmlTreeBuilder.currentElement();
        assertEquals(1, root.childNodes().size());
        assertEquals(doctype.toString(), root.childNode(0).toString());
    }

    @Test
    public void testPopStackToClose_TagExistsInStack() {
        Token.EndTag endTag = new Token.EndTag("element");
        Element root = new Element("root");
        Element element = new Element("element");
        root.appendChild(element);
        xmlTreeBuilder.stack.add(root);
        xmlTreeBuilder.stack.add(element);
        xmlTreeBuilder.popStackToClose(endTag);
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertEquals(root, xmlTreeBuilder.stack.get(0));
    }

    @Test
    public void testPopStackToClose_TagNotExistsInStack() {
        Token.EndTag endTag = new Token.EndTag("element");
        Element root = new Element("root");
        xmlTreeBuilder.stack.add(root);
        xmlTreeBuilder.popStackToClose(endTag);
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertEquals(root, xmlTreeBuilder.stack.get(0));
    }

    @Test
    public void testParseFragment_InputFragmentNormal() {
        String inputFragment = "<element>content</element>";
        String baseUri = "http://example.com";
        Parser parser = new Parser(xmlTreeBuilder);
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, parser);
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        Node node = nodes.get(0);
        assertTrue(node instanceof Element);
        Element element = (Element) node;
        assertEquals("element", element.tagName());
        assertEquals("content", element.text());
    }

    @Test
    public void testParseFragment_InputFragmentEmpty() {
        String inputFragment = "";
        String baseUri = "http://example.com";
        Parser parser = new Parser(xmlTreeBuilder);
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, parser);
        assertNotNull(nodes);
        assertEquals(0, nodes.size());
    }

    @Test
    public void testParseFragment_InputFragmentWithContext() {
        String inputFragment = "<div>content</div>";
        Element context = new Element("context");
        String baseUri = "http://example.com";
        Parser parser = new Parser(xmlTreeBuilder);
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, context, baseUri, parser);
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        Node node = nodes.get(0);
        assertTrue(node instanceof Element);
        Element element = (Element) node;
        assertEquals("div", element.tagName());
        assertEquals("content", element.text());
        assertEquals(context, element.parent());
    }
}