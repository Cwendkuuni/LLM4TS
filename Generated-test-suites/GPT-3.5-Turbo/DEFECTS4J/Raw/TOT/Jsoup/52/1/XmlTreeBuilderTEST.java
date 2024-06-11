import org.junit.Before;
import org.junit.Test;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.nodes.*;
import org.jsoup.parser.Token.*;

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
    public void testInitialiseParse() {
        String input = "<root></root>";
        String baseUri = "https://example.com";
        ParseErrorList errors = new ParseErrorList();

        xmlTreeBuilder.initialiseParse(input, baseUri, errors);

        // Verify that doc is placed onto the stack
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertEquals(xmlTreeBuilder.doc, xmlTreeBuilder.stack.get(0));

        // Verify that syntax is set to xml
        assertEquals(Document.OutputSettings.Syntax.xml, xmlTreeBuilder.doc.outputSettings().syntax());
    }

    @Test
    public void testProcess_StartTag() {
        Token token = new Token(StartTag, "div");

        xmlTreeBuilder.process(token);

        // Verify that a new element is inserted and added to the stack
        assertEquals(1, xmlTreeBuilder.stack.size());
        Element element = xmlTreeBuilder.stack.get(0);
        assertEquals("div", element.tagName());
    }

    @Test
    public void testProcess_EndTag() {
        Element element = new Element("div");
        xmlTreeBuilder.stack.add(element);
        Token token = new Token(EndTag, "div");

        xmlTreeBuilder.process(token);

        // Verify that the element is removed from the stack
        assertEquals(0, xmlTreeBuilder.stack.size());
    }

    @Test
    public void testProcess_Comment() {
        Token token = new Token(Comment, "This is a comment");

        xmlTreeBuilder.process(token);

        // Verify that a new comment node is inserted
        assertEquals(1, xmlTreeBuilder.currentElement().childNodeSize());
        Comment comment = (Comment) xmlTreeBuilder.currentElement().childNode(0);
        assertEquals("This is a comment", comment.getData());
    }

    @Test
    public void testProcess_Character() {
        Token token = new Token(Character, "Some text");

        xmlTreeBuilder.process(token);

        // Verify that a new text node is inserted
        assertEquals(1, xmlTreeBuilder.currentElement().childNodeSize());
        TextNode textNode = (TextNode) xmlTreeBuilder.currentElement().childNode(0);
        assertEquals("Some text", textNode.getWholeText());
    }

    @Test
    public void testProcess_Doctype() {
        Token token = new Token(Doctype, "<!DOCTYPE html>");

        xmlTreeBuilder.process(token);

        // Verify that a new doctype node is inserted
        assertEquals(1, xmlTreeBuilder.currentElement().childNodeSize());
        DocumentType docType = (DocumentType) xmlTreeBuilder.currentElement().childNode(0);
        assertEquals("<!DOCTYPE html>", docType.getOuterHtml());
    }

    @Test
    public void testInsertNode() {
        Element element = new Element("div");
        xmlTreeBuilder.stack.add(element);
        Node node = new Element("span");

        xmlTreeBuilder.insertNode(node);

        // Verify that the node is appended to the current element
        assertEquals(1, xmlTreeBuilder.currentElement().childNodeSize());
        assertEquals(node, xmlTreeBuilder.currentElement().childNode(0));
    }

    @Test
    public void testInsert_StartTag_SelfClosing() {
        Token.StartTag startTag = new Token.StartTag("img", true);
        Tag tag = Tag.valueOf("img");
        Element expectedElement = new Element(tag, xmlTreeBuilder.baseUri, startTag.attributes);

        Element element = xmlTreeBuilder.insert(startTag);

        // Verify that a new element is inserted and added to the stack
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertEquals(expectedElement, element);

        // Verify that the self-closing flag is acknowledged
        assertTrue(xmlTreeBuilder.tokeniser.selfClosingFlagAcknowledged);

        // Verify that the tag is marked as self-closing if it's unknown
        if (!tag.isKnownTag()) {
            assertTrue(tag.isSelfClosing());
        }
    }

    @Test
    public void testInsert_StartTag_NotSelfClosing_KnownTag() {
        Token.StartTag startTag = new Token.StartTag("div", false);
        Tag tag = Tag.valueOf("div");
        Element expectedElement = new Element(tag, xmlTreeBuilder.baseUri, startTag.attributes);

        Element element = xmlTreeBuilder.insert(startTag);

        // Verify that a new element is inserted and added to the stack
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertEquals(expectedElement, element);

        // Verify that the self-closing flag is not acknowledged
        assertFalse(xmlTreeBuilder.tokeniser.selfClosingFlagAcknowledged);
    }

    @Test
    public void testInsert_StartTag_NotSelfClosing_UnknownTag() {
        Token.StartTag startTag = new Token.StartTag("xyz", false);
        Tag tag = Tag.valueOf("xyz");
        Element expectedElement = new Element(tag, xmlTreeBuilder.baseUri, startTag.attributes);

        Element element = xmlTreeBuilder.insert(startTag);

        // Verify that a new element is inserted and added to the stack
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertEquals(expectedElement, element);

        // Verify that the self-closing flag is not acknowledged
        assertFalse(xmlTreeBuilder.tokeniser.selfClosingFlagAcknowledged);

        // Verify that the tag is marked as self-closing
        assertTrue(tag.isSelfClosing());
    }

    @Test
    public void testInsert_Comment() {
        Token.Comment commentToken = new Token.Comment("This is a comment");
        Comment expectedComment = new Comment(commentToken.getData(), xmlTreeBuilder.baseUri);

        xmlTreeBuilder.insert(commentToken);

        // Verify that a new comment node is inserted
        assertEquals(1, xmlTreeBuilder.currentElement().childNodeSize());
        Comment comment = (Comment) xmlTreeBuilder.currentElement().childNode(0);
        assertEquals(expectedComment, comment);
    }

    @Test
    public void testInsert_Comment_Bogus() {
        Token.Comment commentToken = new Token.Comment("<!DOCTYPE html>");
        Comment expectedComment = new Comment(commentToken.getData(), xmlTreeBuilder.baseUri);

        xmlTreeBuilder.insert(commentToken);

        // Verify that a new comment node is inserted
        assertEquals(1, xmlTreeBuilder.currentElement().childNodeSize());
        Comment comment = (Comment) xmlTreeBuilder.currentElement().childNode(0);
        assertEquals(expectedComment, comment);
    }

    @Test
    public void testInsert_Character() {
        Token.Character characterToken = new Token.Character("Some text");
        TextNode expectedTextNode = new TextNode(characterToken.getData(), xmlTreeBuilder.baseUri);

        xmlTreeBuilder.insert(characterToken);

        // Verify that a new text node is inserted
        assertEquals(1, xmlTreeBuilder.currentElement().childNodeSize());
        TextNode textNode = (TextNode) xmlTreeBuilder.currentElement().childNode(0);
        assertEquals(expectedTextNode, textNode);
    }

    @Test
    public void testInsert_Doctype() {
        Token.Doctype doctypeToken = new Token.Doctype("<!DOCTYPE html>");
        DocumentType expectedDoctype = new DocumentType(doctypeToken.getName(), doctypeToken.getPublicIdentifier(), doctypeToken.getSystemIdentifier(), xmlTreeBuilder.baseUri);

        xmlTreeBuilder.insert(doctypeToken);

        // Verify that a new doctype node is inserted
        assertEquals(1, xmlTreeBuilder.currentElement().childNodeSize());
        DocumentType doctypeNode = (DocumentType) xmlTreeBuilder.currentElement().childNode(0);
        assertEquals(expectedDoctype, doctypeNode);
    }

    @Test
    public void testPopStackToClose_ElementFound() {
        Element div1 = new Element("div");
        Element div2 = new Element("div");
        xmlTreeBuilder.stack.add(div1);
        xmlTreeBuilder.stack.add(div2);
        Token.EndTag endTag = new Token.EndTag("div");

        xmlTreeBuilder.popStackToClose(endTag);

        // Verify that the first occurrence of the element is removed from the stack
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertEquals(div1, xmlTreeBuilder.stack.get(0));
    }

    @Test
    public void testPopStackToClose_ElementNotFound() {
        Element div = new Element("div");
        xmlTreeBuilder.stack.add(div);
        Token.EndTag endTag = new Token.EndTag("span");

        xmlTreeBuilder.popStackToClose(endTag);

        // Verify that the stack remains unchanged
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertEquals(div, xmlTreeBuilder.stack.get(0));
    }

    @Test
    public void testParseFragment() {
        String inputFragment = "<div>This is a div</div><span>This is a span</span>";
        String baseUri = "https://example.com";
        ParseErrorList errors = new ParseErrorList();
        Element div = new Element("div");
        div.appendChild(new TextNode("This is a div"));
        Element span = new Element("span");
        span.appendChild(new TextNode("This is a span"));
        List<Node> expectedNodes = new ArrayList<>();
        expectedNodes.add(div);
        expectedNodes.add(span);

        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors);

        // Verify that the expected nodes are parsed from the fragment
        assertEquals(expectedNodes, nodes);
    }

}