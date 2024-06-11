import org.jsoup.nodes.CDataNode;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setup() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testDefaultSettings() {
        ParseSettings settings = xmlTreeBuilder.defaultSettings();
        assertNotNull(settings);
    }

    @Test
    public void testInitialiseParse() {
        Reader input = new StringReader("<root></root>");
        String baseUri = "http://example.com";
        Parser parser = new Parser(xmlTreeBuilder);
        xmlTreeBuilder.initialiseParse(input, baseUri, parser);

        assertEquals(xmlTreeBuilder.stack.size(), 1);

        Document document = xmlTreeBuilder.stack.get(0);
        assertEquals(document.baseUri(), baseUri);
    }

    @Test
    public void testParse() {
        Reader input = new StringReader("<root></root>");
        String baseUri = "http://example.com";
        Document document = xmlTreeBuilder.parse(input, baseUri);

        assertNotNull(document);
        assertEquals(document.baseUri(), baseUri);
    }

    @Test
    public void testParseFragment() {
        String inputFragment = "<div>test</div>";
        String baseUri = "http://example.com";
        Parser parser = new Parser(xmlTreeBuilder);
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, parser);

        assertNotNull(nodes);
        assertEquals(nodes.size(), 1);
        assertTrue(nodes.get(0) instanceof Element);
        Element element = (Element) nodes.get(0);
        assertEquals(element.nodeName(), "div");
        assertEquals(element.text(), "test");
    }

    @Test
    public void testInsertNode() {
        Element element = new Element("div");
        xmlTreeBuilder.insertNode(element);

        assertNotNull(xmlTreeBuilder.currentElement());
        assertEquals(xmlTreeBuilder.currentElement(), element);
    }

    @Test
    public void testInsertStartTag() {
        Token.StartTag startTag = new Token.StartTag("div");
        startTag.addAttribute("id", "test");
        Element element = xmlTreeBuilder.insert(startTag);

        assertNotNull(element);
        assertEquals(element.nodeName(), "div");
        assertEquals(element.attr("id"), "test");
    }

    @Test
    public void testInsertComment() {
        Token.Comment commentToken = new Token.Comment("test");
        xmlTreeBuilder.insert(commentToken);

        assertNotNull(xmlTreeBuilder.currentElement());
        assertTrue(xmlTreeBuilder.currentElement() instanceof Comment);
        Comment comment = (Comment) xmlTreeBuilder.currentElement();
        assertEquals(comment.getData(), "test");
    }

    @Test
    public void testInsertCharacter() {
        Token.Character characterToken = new Token.Character("test", false);
        xmlTreeBuilder.insert(characterToken);

        assertNotNull(xmlTreeBuilder.currentElement());
        assertTrue(xmlTreeBuilder.currentElement() instanceof TextNode);
        TextNode textNode = (TextNode) xmlTreeBuilder.currentElement();
        assertEquals(textNode.getWholeText(), "test");
    }

    @Test
    public void testInsertCDataCharacter() {
        Token.Character characterToken = new Token.Character("test", true);
        xmlTreeBuilder.insert(characterToken);

        assertNotNull(xmlTreeBuilder.currentElement());
        assertTrue(xmlTreeBuilder.currentElement() instanceof CDataNode);
        CDataNode cDataNode = (CDataNode) xmlTreeBuilder.currentElement();
        assertEquals(cDataNode.getWholeText(), "test");
    }

    @Test
    public void testInsertDoctype() {
        Token.Doctype doctype = new Token.Doctype("html", "-//W3C//DTD HTML 4.0 Transitional//EN", "http://www.w3.org/TR/REC-html40/loose.dtd");
        xmlTreeBuilder.insert(doctype);

        assertNotNull(xmlTreeBuilder.currentElement());
        assertTrue(xmlTreeBuilder.currentElement() instanceof DocumentType);
        DocumentType documentType = (DocumentType) xmlTreeBuilder.currentElement();
        assertEquals(documentType.nodeName(), "html");
        assertEquals(documentType.publicIdentifier(), "-//W3C//DTD HTML 4.0 Transitional//EN");
        assertEquals(documentType.systemIdentifier(), "http://www.w3.org/TR/REC-html40/loose.dtd");
    }

    @Test
    public void testPopStackToClose() {
        Token.EndTag endTag = new Token.EndTag("div");
        Element element = new Element("div");
        xmlTreeBuilder.stack.add(element);
        xmlTreeBuilder.popStackToClose(endTag);

        assertEquals(xmlTreeBuilder.stack.size(), 0);
    }

}