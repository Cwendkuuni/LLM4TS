import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.StringReader;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class XmlTreeBuilderTest {

    @Mock
    private Parser parser;

    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testDefaultSettings() {
        ParseSettings defaultSettings = xmlTreeBuilder.defaultSettings();
        assertNotNull(defaultSettings);
        assertEquals(ParseSettings.preserveCase, defaultSettings);
    }

    @Test
    public void testInitialiseParse() {
        StringReader input = new StringReader("");
        String baseUri = "http://example.com";

        xmlTreeBuilder.initialiseParse(input, baseUri, parser);

        // Verify that the document is added to the stack
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertEquals(xmlTreeBuilder.doc, xmlTreeBuilder.stack.get(0));

        // Verify that the output settings syntax is set to xml
        assertEquals(Document.OutputSettings.Syntax.xml, xmlTreeBuilder.doc.outputSettings().syntax());
    }

    @Test
    public void testParseReader() {
        StringReader input = new StringReader("<html><head><title>Test</title></head><body><p>Test</p></body></html>");
        String baseUri = "http://example.com";

        Document document = xmlTreeBuilder.parse(input, baseUri);

        assertNotNull(document);
        assertEquals("html", document.nodeName());
        assertEquals("<html>\n" +
                " <head>\n" +
                "  <title>Test</title>\n" +
                " </head>\n" +
                " <body>\n" +
                "  <p>Test</p>\n" +
                " </body>\n" +
                "</html>", document.toString());
    }

    @Test
    public void testParseString() {
        String input = "<html><head><title>Test</title></head><body><p>Test</p></body></html>";
        String baseUri = "http://example.com";

        Document document = xmlTreeBuilder.parse(input, baseUri);

        assertNotNull(document);
        assertEquals("html", document.nodeName());
        assertEquals("<html>\n" +
                " <head>\n" +
                "  <title>Test</title>\n" +
                " </head>\n" +
                " <body>\n" +
                "  <p>Test</p>\n" +
                " </body>\n" +
                "</html>", document.toString());
    }

    @Test
    public void testProcessWithStartTag() {
        Token token = new Token.StartTag("p");
        XmlTreeBuilder spyXmlTreeBuilder = Mockito.spy(xmlTreeBuilder);

        spyXmlTreeBuilder.process(token);

        // Verify that insert method is called with the start tag token
        Mockito.verify(spyXmlTreeBuilder).insert(token.asStartTag());
    }

    @Test
    public void testProcessWithEndTag() {
        Token token = new Token.EndTag("p");
        XmlTreeBuilder spyXmlTreeBuilder = Mockito.spy(xmlTreeBuilder);

        spyXmlTreeBuilder.process(token);

        // Verify that popStackToClose method is called with the end tag token
        Mockito.verify(spyXmlTreeBuilder).popStackToClose(token.asEndTag());
    }

    @Test
    public void testProcessWithComment() {
        Token token = new Token.Comment("Test");
        XmlTreeBuilder spyXmlTreeBuilder = Mockito.spy(xmlTreeBuilder);

        spyXmlTreeBuilder.process(token);

        // Verify that insert method is called with the comment token
        Mockito.verify(spyXmlTreeBuilder).insert(token.asComment());
    }

    @Test
    public void testProcessWithCharacter() {
        Token token = new Token.Character("Test");
        XmlTreeBuilder spyXmlTreeBuilder = Mockito.spy(xmlTreeBuilder);

        spyXmlTreeBuilder.process(token);

        // Verify that insert method is called with the character token
        Mockito.verify(spyXmlTreeBuilder).insert(token.asCharacter());
    }

    @Test
    public void testProcessWithDoctype() {
        Token token = new Token.Doctype("html");
        XmlTreeBuilder spyXmlTreeBuilder = Mockito.spy(xmlTreeBuilder);

        spyXmlTreeBuilder.process(token);

        // Verify that insert method is called with the doctype token
        Mockito.verify(spyXmlTreeBuilder).insert(token.asDoctype());
    }

    @Test
    public void testInsertNode() {
        Node node = new TextNode("Test");
        Element currentElement = Mockito.mock(Element.class);
        Mockito.when(xmlTreeBuilder.currentElement()).thenReturn(currentElement);

        xmlTreeBuilder.insertNode(node);

        // Verify that appendChild method is called with the node
        Mockito.verify(currentElement).appendChild(node);
    }

    @Test
    public void testInsertWithStartTag() {
        Token.StartTag startTag = new Token.StartTag("p");
        Element element = new Element(Tag.valueOf("p"), "http://example.com", null);
        Mockito.when(xmlTreeBuilder.insertNode(element)).thenReturn(element);

        Element inserted = xmlTreeBuilder.insert(startTag);

        assertNotNull(inserted);
        assertEquals(element, inserted);
        assertFalse(startTag.isSelfClosing());
    }

    @Test
    public void testInsertWithStartTagSelfClosing() {
        Token.StartTag startTag = new Token.StartTag("br");
        Element element = new Element(Tag.valueOf("br"), "http://example.com", null);
        Mockito.when(xmlTreeBuilder.insertNode(element)).thenReturn(element);

        Element inserted = xmlTreeBuilder.insert(startTag);

        assertNotNull(inserted);
        assertEquals(element, inserted);
        assertTrue(startTag.isSelfClosing());
    }

    @Test
    public void testInsertWithCommentTokenBogus() {
        Token.Comment commentToken = new Token.Comment("<!-- Test -->");
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "version=\"1.0\" encoding=\"UTF-8\"");
        Mockito.when(xmlTreeBuilder.insertNode(xmlDeclaration)).thenReturn(xmlDeclaration);

        xmlTreeBuilder.insert(commentToken);

        // Verify that asXmlDeclaration method is called if token is a bogus comment
        Mockito.verify(commentToken).asXmlDeclaration();
    }

    @Test
    public void testInsertWithCommentToken() {
        Token.Comment commentToken = new Token.Comment("<!-- Test -->");
        Comment comment = new Comment("<!-- Test -->");
        Mockito.when(xmlTreeBuilder.insertNode(comment)).thenReturn(comment);

        xmlTreeBuilder.insert(commentToken);

        // Verify that insertNode method is called with the comment
        Mockito.verify(xmlTreeBuilder).insertNode(comment);
    }

    @Test
    public void testInsertWithCharacterTokenCData() {
        Token.Character token = new Token.Character("<![CDATA[Test]]>");
        CDataNode cDataNode = new CDataNode("<![CDATA[Test]]>");
        Mockito.when(xmlTreeBuilder.insertNode(cDataNode)).thenReturn(cDataNode);

        xmlTreeBuilder.insert(token);

        // Verify that insertNode method is called with the cDataNode
        Mockito.verify(xmlTreeBuilder).insertNode(cDataNode);
    }

    @Test
    public void testInsertWithCharacterToken() {
        Token.Character token = new Token.Character("Test");
        TextNode textNode = new TextNode("Test");
        Mockito.when(xmlTreeBuilder.insertNode(textNode)).thenReturn(textNode);

        xmlTreeBuilder.insert(token);

        // Verify that insertNode method is called with the textNode
        Mockito.verify(xmlTreeBuilder).insertNode(textNode);
    }

    @Test
    public void testInsertWithDoctype() {
        Token.Doctype doctype = new Token.Doctype("html");
        DocumentType documentType = new DocumentType("html", "", "");
        Mockito.when(xmlTreeBuilder.insertNode(documentType)).thenReturn(documentType);

        xmlTreeBuilder.insert(doctype);

        // Verify that insertNode method is called with the documentType
        Mockito.verify(xmlTreeBuilder).insertNode(documentType);
    }

    @Test
    public void testPopStackToCloseNotFound() {
        Token.EndTag endTag = new Token.EndTag("p");

        xmlTreeBuilder.popStackToClose(endTag);

        // Verify that no elements are removed from the stack as the end tag is not found
        assertEquals(0, xmlTreeBuilder.stack.size());
    }

    @Test
    public void testPopStackToClose() {
        Token.EndTag endTag = new Token.EndTag("p");
        Element element = new Element(Tag.valueOf("p"), "http://example.com", null);
        xmlTreeBuilder.stack.add(element);

        xmlTreeBuilder.popStackToClose(endTag);

        // Verify that the element is removed from the stack as the end tag is found
        assertEquals(0, xmlTreeBuilder.stack.size());
    }

    @Test
    public void testParseFragmentString() {
        String inputFragment = "<p>Test</p>";
        String baseUri = "http://example.com";
        Parser parser = new Parser(xmlTreeBuilder);

        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, parser);

        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        assertEquals("p", nodes.get(0).nodeName());
        assertEquals("<p>Test</p>", nodes.get(0).toString());
    }

    @Test
    public void testParseFragmentStringWithContext() {
        String inputFragment = "<p>Test</p>";
        Element context = new Element(Tag.valueOf("div"), "http://example.com", null);
        String baseUri = "http://example.com";
        Parser parser = new Parser(xmlTreeBuilder);

        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, context, baseUri, parser);

        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        assertEquals("p", nodes.get(0).nodeName());
        assertEquals("<p>Test</p>", nodes.get(0).toString());
    }
}