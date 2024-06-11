import org.junit.Test;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.CDataNode;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.DocumentType;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.ParseSettings;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    @Test
    public void testDefaultSettings() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        ParseSettings settings = builder.defaultSettings();
        
        assertNotNull(settings);
        // Check if the default settings preserve case
        assertTrue(settings.preserveCase());
    }

    @Test
    public void testParseReaderString1() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Reader input = new StringReader("<root><element>Text</element></root>");
        String baseUri = "http://example.com";
        Document document = builder.parse(input, baseUri);

        assertNotNull(document);
        assertEquals("root", document.nodeName());

        Element rootElement = document.child(0);
        assertNotNull(rootElement);
        assertEquals("element", rootElement.child(0).nodeName());

        TextNode textNode = (TextNode) rootElement.child(0).childNode(0);
        assertNotNull(textNode);
        assertEquals("Text", textNode.getWholeText());
    }

    @Test
    public void testParseReaderString2() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Reader input = new StringReader("<root><element>Text1</element><element>Text2</element></root>");
        String baseUri = "http://example.com";
        Document document = builder.parse(input, baseUri);

        assertNotNull(document);
        assertEquals("root", document.nodeName());

        List<Element> elementList = document.getElementsByTag("element");
        assertEquals(2, elementList.size());

        TextNode textNode1 = (TextNode) elementList.get(0).childNode(0);
        assertNotNull(textNode1);
        assertEquals("Text1", textNode1.getWholeText());

        TextNode textNode2 = (TextNode) elementList.get(1).childNode(0);
        assertNotNull(textNode2);
        assertEquals("Text2", textNode2.getWholeText());
    }

    @Test
    public void testParseString1() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String input = "<root><element>Text</element></root>";
        String baseUri = "http://example.com";
        Document document = builder.parse(input, baseUri);

        assertNotNull(document);
        assertEquals("root", document.nodeName());

        Element rootElement = document.child(0);
        assertNotNull(rootElement);
        assertEquals("element", rootElement.child(0).nodeName());

        TextNode textNode = (TextNode) rootElement.child(0).childNode(0);
        assertNotNull(textNode);
        assertEquals("Text", textNode.getWholeText());
    }

    @Test
    public void testParseString2() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String input = "<root><element>Text1</element><element>Text2</element></root>";
        String baseUri = "http://example.com";
        Document document = builder.parse(input, baseUri);

        assertNotNull(document);
        assertEquals("root", document.nodeName());

        List<Element> elementList = document.getElementsByTag("element");
        assertEquals(2, elementList.size());

        TextNode textNode1 = (TextNode) elementList.get(0).childNode(0);
        assertNotNull(textNode1);
        assertEquals("Text1", textNode1.getWholeText());

        TextNode textNode2 = (TextNode) elementList.get(1).childNode(0);
        assertNotNull(textNode2);
        assertEquals("Text2", textNode2.getWholeText());
    }

    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Reader input = new StringReader("<root><element>Text</element></root>");
        String baseUri = "http://example.com";

        builder.initialiseParse(input, baseUri, ParseErrorList.noTracking(), ParseSettings.preserveCase);
        assertNotNull(builder.stack);
        assertEquals(1, builder.stack.size());

        Document document = builder.document();
        assertNotNull(document);
        assertEquals("root", document.nodeName());

        Element rootElement = document.child(0);
        assertNotNull(rootElement);
        assertEquals("element", rootElement.child(0).nodeName());

        TextNode textNode = (TextNode) rootElement.child(0).childNode(0);
        assertNotNull(textNode);
        assertEquals("Text", textNode.getWholeText());
    }

    @Test
    public void testProcessStartTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        XmlTreeBuilder.Token.StartTag startTag = new XmlTreeBuilder.Token.StartTag("element");

        Element element = builder.insert(startTag);

        assertNotNull(element);
        assertEquals("element", element.nodeName());
        assertEquals(0, element.childNodeSize());
    }

    @Test
    public void testProcessEndTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        XmlTreeBuilder.Token.EndTag endTag = new XmlTreeBuilder.Token.EndTag("element");

        Element parentElement = new Element("parent");
        Element element = new Element("element");
        parentElement.appendChild(element);

        builder.stack.add(parentElement);
        builder.stack.add(element);

        builder.popStackToClose(endTag);

        assertEquals(1, builder.stack.size());
        assertEquals(parentElement, builder.stack.get(0));
    }

    @Test
    public void testProcessComment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        XmlTreeBuilder.Token.Comment commentToken = new XmlTreeBuilder.Token.Comment("Test Comment");

        builder.insert(commentToken);

        List<Comment> comments = builder.document().getAllElements().get(0).childNodes().get(0).commmentNodes();

        assertEquals(1, comments.size());

        Comment comment = comments.get(0);
        assertNotNull(comment);
        assertEquals("Test Comment", comment.getData());
    }

    @Test
    public void testProcessCharacter() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        XmlTreeBuilder.Token.Character characterToken = new XmlTreeBuilder.Token.Character("Test Character");

        builder.insert(characterToken);

        List<TextNode> textNodes = builder.document().getAllElements().get(0).childNodes().get(0).textNodes();

        assertEquals(1, textNodes.size());

        TextNode textNode = textNodes.get(0);
        assertNotNull(textNode);
        assertEquals("Test Character", textNode.getWholeText());
    }

    @Test
    public void testProcessDoctype() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        XmlTreeBuilder.Token.Doctype doctypeToken = new XmlTreeBuilder.Token.Doctype("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd");

        builder.insert(doctypeToken);

        List<DocumentType> doctypes = builder.document().getAllElements().get(0).childNodes().get(0).documentTypeNodes();

        assertEquals(1, doctypes.size());

        DocumentType doctype = doctypes.get(0);
        assertNotNull(doctype);
        assertEquals("html", doctype.nodeName());
        assertEquals("-//W3C//DTD HTML 4.01//EN", doctype.attr("publicId"));
        assertEquals("http://www.w3.org/TR/html4/strict.dtd", doctype.attr("systemId"));
    }

    @Test
    public void testInsertNode() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element rootElement = new Element("root");
        Element element = new Element("element");

        builder.stack.add(rootElement);
        builder.insertNode(element);

        assertEquals(2, rootElement.childNodeSize());
        assertEquals(element, rootElement.childNode(1));
    }

    @Test
    public void testParseFragment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String inputFragment = "<element>Text</element>";
        String baseUri = "http://example.com";
        List<Node> nodes = builder.parseFragment(inputFragment, baseUri, ParseErrorList.noTracking(), ParseSettings.preserveCase);

        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        Element element = (Element) nodes.get(0);
        assertEquals("element", element.nodeName());

        TextNode textNode = (TextNode) element.childNode(0);
        assertNotNull(textNode);
        assertEquals("Text", textNode.getWholeText());
    }

}