import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.Token;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
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
    public void testParseReaderAndBaseUri() {
        String xml = "<root><child1>data1</child1><child2>data2</child2></root>";
        Reader reader = new StringReader(xml);
        String baseUri = "http://example.com/";

        Document document = xmlTreeBuilder.parse(reader, baseUri);

        assertNotNull(document);
        assertEquals(xml, document.outerHtml());
    }

    @Test
    public void testParseStringAndBaseUri() {
        String xml = "<root><child1>data1</child1><child2>data2</child2></root>";
        String baseUri = "http://example.com/";

        Document document = xmlTreeBuilder.parse(xml, baseUri);

        assertNotNull(document);
        assertEquals(xml, document.outerHtml());
    }

    @Test
    public void testInitialiseParse() {
        Reader reader = new StringReader("<root>data</root>");
        String baseUri = "http://example.com/";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;

        xmlTreeBuilder.initialiseParse(reader, baseUri, errors, settings);

        assertEquals(reader, xmlTreeBuilder.reader);
        assertEquals(baseUri, xmlTreeBuilder.baseUri);
        assertEquals(errors, xmlTreeBuilder.errors);
        assertEquals(settings, xmlTreeBuilder.settings);

        Element docElement = xmlTreeBuilder.doc.child(0);
        assertNotNull(docElement);
        assertEquals("root", docElement.tagName());
        assertEquals("data", docElement.ownText());

        Document.OutputSettings.Syntax syntax = xmlTreeBuilder.doc.outputSettings().syntax();
        assertNotNull(syntax);
        assertEquals(Document.OutputSettings.Syntax.xml, syntax);
    }

    @Test
    public void testProcessStartTag() {
        Element element = xmlTreeBuilder.doc.createElement("tag");
        xmlTreeBuilder.stack.add(element);

        Token.StartTag startTag = new Token.StartTag();
        startTag.name = "child";

        xmlTreeBuilder.insert(startTag);

        List<Node> childNodes = xmlTreeBuilder.currentElement().childNodes();
        assertNotNull(childNodes);
        assertEquals(1, childNodes.size());

        Node childNode = childNodes.get(0);
        assertTrue(childNode instanceof Element);
        Element childElement = (Element) childNode;
        assertEquals("child", childElement.tagName());
    }

    @Test
    public void testProcessEndTag() {
        Element element = xmlTreeBuilder.doc.createElement("tag");
        xmlTreeBuilder.stack.add(element);

        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "tag";

        xmlTreeBuilder.popStackToClose(endTag);

        List<Node> childNodes = xmlTreeBuilder.currentElement().childNodes();
        assertTrue(childNodes.isEmpty());
    }

    @Test
    public void testProcessComment() {
        Token.Comment commentToken = new Token.Comment();
        commentToken.data = "Test Comment";

        xmlTreeBuilder.insert(commentToken);

        List<Node> childNodes = xmlTreeBuilder.currentElement().childNodes();
        assertNotNull(childNodes);
        assertEquals(1, childNodes.size());

        Node childNode = childNodes.get(0);
        assertTrue(childNode instanceof Comment);
        Comment comment = (Comment) childNode;
        assertEquals("Test Comment", comment.getData());
    }

    @Test
    public void testProcessCharacter() {
        Token.Character characterToken = new Token.Character();
        characterToken.data = "Test Character";

        xmlTreeBuilder.insert(characterToken);

        List<Node> childNodes = xmlTreeBuilder.currentElement().childNodes();
        assertNotNull(childNodes);
        assertEquals(1, childNodes.size());

        Node childNode = childNodes.get(0);
        assertTrue(childNode instanceof TextNode);
        TextNode textNode = (TextNode) childNode;
        assertEquals("Test Character", textNode.getWholeText());
    }

    @Test
    public void testProcessDoctype() {
        Token.Doctype doctypeToken = new Token.Doctype();
        doctypeToken.name = "html";

        xmlTreeBuilder.insert(doctypeToken);

        List<Node> childNodes = xmlTreeBuilder.currentElement().childNodes();
        assertNotNull(childNodes);
        assertEquals(1, childNodes.size());

        Node childNode = childNodes.get(0);
        assertTrue(childNode instanceof DocumentType);
        DocumentType doctype = (DocumentType) childNode;
        assertEquals("HTML", doctype.getName());
    }

    @Test
    public void testInsertNode() {
        Element element = xmlTreeBuilder.doc.createElement("parent");
        xmlTreeBuilder.stack.add(element);

        Element childElement = xmlTreeBuilder.doc.createElement("child");
        xmlTreeBuilder.insertNode(childElement);

        List<Node> childNodes = xmlTreeBuilder.currentElement().childNodes();
        assertNotNull(childNodes);
        assertEquals(1, childNodes.size());
        assertEquals(childElement, childNodes.get(0));
    }

    @Test
    public void testInsertSelfClosingTag() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.name = "self-closing";
        startTag.selfClosing = true;

        xmlTreeBuilder.insert(startTag);

        List<Node> childNodes = xmlTreeBuilder.currentElement().childNodes();
        assertNotNull(childNodes);
        assertEquals(1, childNodes.size());

        Node childNode = childNodes.get(0);
        assertTrue(childNode instanceof Element);
        Element element = (Element) childNode;

        assertTrue(element.tag().isSelfClosing());
        assertTrue(element.tag().isKnownTag());
    }

    @Test
    public void testInsertBogusComment() {
        Token.Comment commentToken = new Token.Comment();
        commentToken.data = "!<xml attribute=\"value\">";

        xmlTreeBuilder.insert(commentToken);

        List<Node> childNodes = xmlTreeBuilder.currentElement().childNodes();
        assertNotNull(childNodes);
        assertEquals(1, childNodes.size());

        Node childNode = childNodes.get(0);
        assertTrue(childNode instanceof XmlDeclaration);
        XmlDeclaration declaration = (XmlDeclaration) childNode;

        assertEquals("xml", declaration.tagName());
        assertTrue(declaration.isProcessingInstruction());
        assertEquals("attribute=\"value\"", declaration.attributes().html());
    }

    @Test
    public void testInsertCharacterCDATA() {
        Token.Character characterToken = new Token.Character();
        characterToken.isCData = true;
        characterToken.data = "<![CDATA[Test Data]]>";

        xmlTreeBuilder.insert(characterToken);

        List<Node> childNodes = xmlTreeBuilder.currentElement().childNodes();
        assertNotNull(childNodes);
        assertEquals(1, childNodes.size());

        Node childNode = childNodes.get(0);
        assertTrue(childNode instanceof CDataNode);
        CDataNode cDataNode = (CDataNode) childNode;

        assertEquals("Test Data", cDataNode.getWholeText());
    }

    @Test
    public void testPopStackToCloseNotFound() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "tag";

        List<Element> stack = new ArrayList<>();
        stack.add(xmlTreeBuilder.doc.createElement("other-tag"));
        xmlTreeBuilder.stack.addAll(stack);

        xmlTreeBuilder.popStackToClose(endTag);

        assertEquals(stack, xmlTreeBuilder.stack);
    }

    @Test
    public void testPopStackToClose() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "tag";

        List<Element> stack = new ArrayList<>();
        stack.add(xmlTreeBuilder.doc.createElement("other-tag"));
        stack.add(xmlTreeBuilder.doc.createElement("tag"));
        stack.add(xmlTreeBuilder.doc.createElement("tag"));
        stack.add(xmlTreeBuilder.doc.createElement("other-tag"));
        xmlTreeBuilder.stack.addAll(stack);

        xmlTreeBuilder.popStackToClose(endTag);

        assertEquals(1, xmlTreeBuilder.stack.size());
        assertEquals(stack.get(0), xmlTreeBuilder.stack.get(0));
    }

    @Test
    public void testParseFragment() {
        String inputFragment = "<child1>data1</child1><child2>data2</child2>";
        String baseUri = "http://example.com/";

        List<Node> childNodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, ParseErrorList.noTracking(), ParseSettings.preserveCase);

        assertNotNull(childNodes);
        assertEquals(2, childNodes.size());

        assertTrue(childNodes.get(0) instanceof Element);
        Element child1 = (Element) childNodes.get(0);
        assertEquals("child1", child1.tagName());
        assertEquals("data1", child1.ownText());

        assertTrue(childNodes.get(1) instanceof Element);
        Element child2 = (Element) childNodes.get(1);
        assertEquals("child2", child2.tagName());
        assertEquals("data2", child2.ownText());
    }

}