import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.parser.Token;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.DocumentType;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testInitialiseParse() {
        // Test initialization of parse
        xmlTreeBuilder.initialiseParse("input", "baseUri", new ParseErrorList());
        assertEquals(xmlTreeBuilder.stack.size(), 1);
        assertEquals(xmlTreeBuilder.stack.peek(), xmlTreeBuilder.doc);
    }

    @Test
    public void testInsertNode() {
        Element element = new Element();
        xmlTreeBuilder.insertNode(element);
        assertEquals(xmlTreeBuilder.currentElement().childNode(0), element);
    }

    @Test
    public void testProcessStartTag() {
        Token.StartTag startTag = new Token.StartTag("tag", null);
        Element element = xmlTreeBuilder.insert(startTag);
        assertEquals(element.tagName(), "tag");
    }

    @Test
    public void testProcessComment() {
        Token.Comment comment = new Token.Comment("comment");
        xmlTreeBuilder.insert(comment);
        assertEquals(xmlTreeBuilder.currentElement().childNode(0), new Comment("comment", ""));
    }

    @Test
    public void testProcessCharacter() {
        Token.Character character = new Token.Character("text");
        xmlTreeBuilder.insert(character);
        assertEquals(xmlTreeBuilder.currentElement().childNode(0), new TextNode("text", ""));
    }

    @Test
    public void testProcessDoctype() {
        Token.Doctype doctype = new Token.Doctype("html", "", "", "");
        xmlTreeBuilder.insert(doctype);
        assertEquals(xmlTreeBuilder.currentElement().childNode(0), new DocumentType("html", "", "", ""));
    }

    @Test
    public void testPopStackToClose() {
        Element element1 = new Element("tag1");
        Element element2 = new Element("tag2");
        Element element3 = new Element("tag3");
        xmlTreeBuilder.stack.push(element1);
        xmlTreeBuilder.stack.push(element2);
        xmlTreeBuilder.stack.push(element3);
        Token.EndTag endTag = new Token.EndTag("tag2");
        xmlTreeBuilder.popStackToClose(endTag);
        assertEquals(xmlTreeBuilder.stack.size(), 2);
        assertEquals(xmlTreeBuilder.stack.peek(), element1);
    }
}