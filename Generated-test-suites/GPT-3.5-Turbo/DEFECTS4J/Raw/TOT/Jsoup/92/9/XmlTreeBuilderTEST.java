import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Token;
import org.jsoup.parser.XmlTreeBuilder;
import static org.junit.Assert.*;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;
    private static final String BASE_URI = "https://example.com";

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testParse_validXmlInput_returnsDocument() {
        String xmlInput = "<root><child>Text</child></root>";
        Reader reader = new StringReader(xmlInput);

        Document document = xmlTreeBuilder.parse(reader, BASE_URI);

        assertNotNull(document);
        assertEquals("root", document.childNode(0).nodeName());
        assertEquals("child", document.childNode(0).childNode(0).nodeName());
        assertEquals("Text", document.childNode(0).childNode(0).childNode(0).toString());
    }

    @Test
    public void testParse_emptyInput_returnsEmptyDocument() {
        String xmlInput = "";
        Reader reader = new StringReader(xmlInput);

        Document document = xmlTreeBuilder.parse(reader, BASE_URI);

        assertNotNull(document);
        assertEquals(0, document.childNodes().size());
    }

    @Test
    public void testParseFragment_validFragment_returnsListofNodes() {
        String xmlFragment = "<div id=\"test\">Text</div>";
        Reader reader = new StringReader(xmlFragment);

        List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, BASE_URI, new Parser(xmlTreeBuilder));

        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertEquals("div", nodes.get(0).nodeName());
        assertEquals("test", ((Element) nodes.get(0)).id());
        assertEquals("Text", nodes.get(0).childNode(0).toString());
    }

    @Test
    public void testProcess_StartTag_tokenInsertedToCurrentElement() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.name("div");
        startTag.attributes().put("id", "test");

        xmlTreeBuilder.process(startTag);

        Element currentElement = xmlTreeBuilder.currentElement();
        assertNotNull(currentElement);
        assertEquals("div", currentElement.nodeName());
        assertEquals("test", currentElement.id());
    }

    @Test
    public void testProcess_EndTag_matchingTagFoundAndRemovedFromStack() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.name("div");
        startTag.attributes().put("id", "test");
        xmlTreeBuilder.process(startTag);

        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "div";
        xmlTreeBuilder.process(endTag);

        Element currentElement = xmlTreeBuilder.currentElement();
        assertNull(currentElement);
    }

    @Test(expected = IllegalStateException.class)
    public void testProcess_InvalidToken_throwsException() {
        Token invalidToken = new Token();
        invalidToken.type = Token.TokenType.Comment;

        xmlTreeBuilder.process(invalidToken);
    }

    @Test
    public void testInsertNode_givenNode_insertsNodeToCurrentElement() {
        Element element = new Element("p");
        xmlTreeBuilder.insertNode(element);

        Element currentElement = xmlTreeBuilder.currentElement();
        assertNotNull(currentElement);
        assertEquals(element, currentElement.childNode(0));
    }

    @Test
    public void testInsert_givenStartTag_startTagInsertedToStackAndAttributesNormalized() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.name("div");
        startTag.attributes().put("iD", "test");

        Element element = xmlTreeBuilder.insert(startTag);

        assertNotNull(element);
        assertEquals("div", element.nodeName());
        assertEquals("test", element.id());
    }

    @Test
    public void testInsert_givenSelfClosingStartTag_selfClosingTagAddedToKnownTagList() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.name("img");
        startTag.selfClosing(true);

        Element element = xmlTreeBuilder.insert(startTag);

        assertTrue(element.tag().isSelfClosing());
        assertTrue(element.tag().isKnownTag());
    }

    @Test
    public void testInsert_givenCommentToken_insertsCommentOrXmlDeclarationNode() {
        Token.Comment commentToken = new Token.Comment();
        commentToken.data("<!-- comment -->");

        xmlTreeBuilder.insert(commentToken);

        assertEquals(1, xmlTreeBuilder.currentElement().childNodeSize());
        assertEquals("<!-- comment -->", xmlTreeBuilder.currentElement().childNode(0).toString());
    }

    @Test
    public void testInsert_givenCharacterToken_insertsTextNodeOrCDataNode() {
        Token.Character characterToken = new Token.Character();
        characterToken.data("Text");

        xmlTreeBuilder.insert(characterToken);

        assertEquals(1, xmlTreeBuilder.currentElement().childNodeSize());
        assertEquals("Text", xmlTreeBuilder.currentElement().childNode(0).toString());
    }

    @Test
    public void testInsert_givenDoctypeToken_insertsDoctypeNode() {
        Token.Doctype doctypeToken = new Token.Doctype();
        doctypeToken.setName("html");

        xmlTreeBuilder.insert(doctypeToken);

        assertEquals(1, xmlTreeBuilder.currentElement().childNodeSize());
        assertEquals("html", xmlTreeBuilder.currentElement().childNode(0).nodeName());
    }

    @Test
    public void testPopStackToClose_givenEndTag_matchingElementInStack_removedFromStack() {
        Token.StartTag startTag1 = new Token.StartTag();
        startTag1.name("div");
        xmlTreeBuilder.process(startTag1);

        Token.StartTag startTag2 = new Token.StartTag();
        startTag2.name("p");
        xmlTreeBuilder.process(startTag2);

        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "div";
        xmlTreeBuilder.popStackToClose(endTag);

        assertEquals(1, xmlTreeBuilder.stack.size());
        assertEquals("p", xmlTreeBuilder.stack.get(0).nodeName());
    }

    @Test
    public void testPopStackToClose_givenEndTag_noMatchingElementInStack_noChange() {
        Token.StartTag startTag1 = new Token.StartTag();
        startTag1.name("div");
        xmlTreeBuilder.process(startTag1);

        Token.StartTag startTag2 = new Token.StartTag();
        startTag2.name("p");
        xmlTreeBuilder.process(startTag2);

        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "span";
        xmlTreeBuilder.popStackToClose(endTag);

        assertEquals(2, xmlTreeBuilder.stack.size());
    }
}