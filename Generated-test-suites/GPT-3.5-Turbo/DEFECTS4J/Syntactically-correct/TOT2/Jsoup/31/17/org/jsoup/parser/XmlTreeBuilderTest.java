import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.DocumentType;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.parser.Token;
import org.jsoup.helper.Validate;
import org.jsoup.parser.ParseErrorList;
import org.junit.Test;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {
    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.initialiseParse("input", "baseUri", new ParseErrorList());
        assertEquals(xmlTreeBuilder.stack.size(), 1);
    }

    @Test
    public void testInsert() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("example");
        Element element = xmlTreeBuilder.insert(startTag);
        assertNotNull(element);
        assertEquals(element.tagName(), "example");
    }

    @Test
    public void testInsertComment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Comment commentToken = new Token.Comment("example comment");
        xmlTreeBuilder.insert(commentToken);
        assertEquals(xmlTreeBuilder.currentElement().childNodeSize(), 1);
        assertTrue(xmlTreeBuilder.currentElement().childNode(0) instanceof Comment);
    }

    @Test
    public void testInsertCharacter() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Character characterToken = new Token.Character("example character");
        xmlTreeBuilder.insert(characterToken);
        assertEquals(xmlTreeBuilder.currentElement().childNodeSize(), 1);
        assertTrue(xmlTreeBuilder.currentElement().childNode(0) instanceof TextNode);
    }

    @Test
    public void testInsertDoctype() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Doctype doctype = new Token.Doctype("html", "pubId", "sysId");
        xmlTreeBuilder.insert(doctype);
        assertEquals(xmlTreeBuilder.currentElement().childNodeSize(), 1);
        assertTrue(xmlTreeBuilder.currentElement().childNode(0) instanceof DocumentType);
    }

    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.EndTag endTag = new Token.EndTag("example");
        xmlTreeBuilder.stack.push(xmlTreeBuilder.currentElement());
        xmlTreeBuilder.popStackToClose(endTag);
        assertEquals(xmlTreeBuilder.stack.size(), 0);
    }
}