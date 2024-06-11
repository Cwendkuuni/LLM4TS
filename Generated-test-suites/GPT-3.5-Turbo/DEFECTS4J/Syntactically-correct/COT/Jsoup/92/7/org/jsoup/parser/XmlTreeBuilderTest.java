import org.jsoup.nodes.*;
import org.jsoup.parser.*;
import org.junit.Before;
import org.junit.Test;

import java.io.Reader;
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
    public void testParseWithReaderAndBaseUri() {
        String input = "<xml><tag1>value1</tag1><tag2>value2</tag2></xml>";
        String baseUri = "http://example.com";
        
        try (Reader reader = new StringReader(input)) {
            Document document = xmlTreeBuilder.parse(reader, baseUri);
            
            assertNotNull(document);
            assertEquals("xml", document.nodeName());
            assertEquals("http://example.com", document.baseUri());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testParseWithStringAndBaseUri() {
        String input = "<xml><tag1>value1</tag1><tag2>value2</tag2></xml>";
        String baseUri = "http://example.com";
        
        Document document = xmlTreeBuilder.parse(input, baseUri);
        
        assertNotNull(document);
        assertEquals("xml", document.nodeName());
        assertEquals("http://example.com", document.baseUri());
    }

    @Test
    public void testProcessWithStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.name("tag1");
        startTag.attributes(new Attributes());

        xmlTreeBuilder.process(startTag);

        Element element = xmlTreeBuilder.currentElement();
        assertNotNull(element);
        assertEquals("tag1", element.nodeName());
        assertNull(element.parent());
    }

    @Test
    public void testProcessWithEndTag() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName("tag1");

        Element element = new Element(Tag.valueOf("tag1"), "");
        xmlTreeBuilder.stack.add(element);

        xmlTreeBuilder.process(endTag);

        assertTrue(xmlTreeBuilder.stack.isEmpty());
    }

    @Test
    public void testProcessWithComment() {
        Token.Comment comment = new Token.Comment();
        comment.data("This is a comment");

        xmlTreeBuilder.process(comment);

        List<Node> childNodes = xmlTreeBuilder.currentElement().childNodes();
        assertEquals(1, childNodes.size());
        Comment insertedComment = (Comment) childNodes.get(0);
        assertEquals("This is a comment", insertedComment.getData());
        assertNull(insertedComment.parent());
    }

    @Test
    public void testProcessWithCharacter() {
        Token.Character character = new Token.Character();
        character.data("Some text");

        xmlTreeBuilder.process(character);

        List<Node> childNodes = xmlTreeBuilder.currentElement().childNodes();
        assertEquals(1, childNodes.size());
        TextNode insertedTextNode = (TextNode) childNodes.get(0);
        assertEquals("Some text", insertedTextNode.getWholeText());
        assertNull(insertedTextNode.parent());
    }

    @Test
    public void testProcessWithDoctype() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.setName("html");

        xmlTreeBuilder.process(doctype);

        List<Node> childNodes = xmlTreeBuilder.currentElement().childNodes();
        assertEquals(1, childNodes.size());
        DocumentType insertedDoctype = (DocumentType) childNodes.get(0);
        assertEquals("html", insertedDoctype.name());
        assertNull(insertedDoctype.parent());
    }

    @Test
    public void testInsertNode() {
        Element element = new Element(Tag.valueOf("tag1"), "");
        Element parentElement = new Element(Tag.valueOf("parent"), "");
        parentElement.appendChild(element);
        xmlTreeBuilder.stack.add(parentElement);

        Token.Character character = new Token.Character();
        character.data("Some text");

        xmlTreeBuilder.insertNode(character);

        List<Node> childNodes = parentElement.childNodes();
        assertEquals(2, childNodes.size());
        assertEquals(character, childNodes.get(1));
        assertEquals(parentElement, character.parent());
    }

    @Test
    public void testInsertWithStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.name("tag1");
        startTag.attributes(new Attributes());

        xmlTreeBuilder.insert(startTag);

        Element element = xmlTreeBuilder.currentElement();
        assertNotNull(element);
        assertEquals("tag1", element.nodeName());
        assertNull(element.parent());
    }

    @Test
    public void testInsertWithCommentToken() {
        Token.Comment commentToken = new Token.Comment();
        commentToken.data("This is a comment");

        xmlTreeBuilder.insert(commentToken);

        List<Node> childNodes = xmlTreeBuilder.currentElement().childNodes();
        assertEquals(1, childNodes.size());
        Comment insertedComment = (Comment) childNodes.get(0);
        assertEquals("This is a comment", insertedComment.getData());
        assertNull(insertedComment.parent());
    }

    @Test
    public void testInsertWithCharacterToken() {
        Token.Character characterToken = new Token.Character();
        characterToken.data("Some text");

        xmlTreeBuilder.insert(characterToken);

        List<Node> childNodes = xmlTreeBuilder.currentElement().childNodes();
        assertEquals(1, childNodes.size());
        TextNode insertedTextNode = (TextNode) childNodes.get(0);
        assertEquals("Some text", insertedTextNode.getWholeText());
        assertNull(insertedTextNode.parent());
    }

    @Test
    public void testInsertWithDoctypeToken() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.setName("html");

        xmlTreeBuilder.insert(doctype);

        List<Node> childNodes = xmlTreeBuilder.currentElement().childNodes();
        assertEquals(1, childNodes.size());
        DocumentType insertedDoctype = (DocumentType) childNodes.get(0);
        assertEquals("html", insertedDoctype.name());
        assertNull(insertedDoctype.parent());
    }

    @Test
    public void testPopStackToClose() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName("tag1");

        Element element1 = new Element(Tag.valueOf("tag1"), "");
        Element element2 = new Element(Tag.valueOf("tag2"), "");

        xmlTreeBuilder.stack.add(element1);
        xmlTreeBuilder.stack.add(element2);
        xmlTreeBuilder.stack.add(element1);

        xmlTreeBuilder.popStackToClose(endTag);
        
        assertEquals(2, xmlTreeBuilder.stack.size());
        assertEquals(element2, xmlTreeBuilder.stack.get(0));
        assertEquals(element1, xmlTreeBuilder.stack.get(1));
    }

    @Test
    public void testParseFragmentWithStringAndBaseUri() {
        String inputFragment = "<tag1>value1</tag1><tag2>value2</tag2>";
        String baseUri = "http://example.com";

        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, new Parser(xmlTreeBuilder));

        assertEquals(2, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        assertEquals("tag1", ((Element) nodes.get(0)).nodeName());
        assertTrue(nodes.get(1) instanceof Element);
        assertEquals("tag2", ((Element) nodes.get(1)).nodeName());
    }

    @Test
    public void testParseFragmentWithStringAndElementAndBaseUri() {
        String inputFragment = "<tag1>value1</tag1><tag2>value2</tag2>";
        Element context = new Element(Tag.valueOf("wrapper"), "");
        String baseUri = "http://example.com";

        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, context, baseUri, new Parser(xmlTreeBuilder));

        assertEquals(2, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        assertEquals("tag1", ((Element) nodes.get(0)).nodeName());
        assertTrue(nodes.get(1) instanceof Element);
        assertEquals("tag2", ((Element) nodes.get(1)).nodeName());
    }

}