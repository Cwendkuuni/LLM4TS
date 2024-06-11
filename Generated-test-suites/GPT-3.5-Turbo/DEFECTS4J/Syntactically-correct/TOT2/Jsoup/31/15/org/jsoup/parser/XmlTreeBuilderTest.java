import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.parser.Token;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.DocumentType;
import org.junit.Test;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.initialiseParse("<xml></xml>", "", null);
        assertEquals("Initialise parse failed", "<xml></xml>", xmlTreeBuilder.currentElement().html());
    }

    @Test
    public void testProcess() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token startTagToken = new Token(Token.TokenType.StartTag, "<tag>");
        assertTrue("Process failed for start tag", xmlTreeBuilder.process(startTagToken));
    }

    @Test
    public void testInsertNode() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Element element = new Element("test");
        xmlTreeBuilder.insertNode(element);
        assertEquals("Insert node failed", element, xmlTreeBuilder.currentElement().childNode(0));
    }

    @Test
    public void testInsertStartTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("div");
        Element element = xmlTreeBuilder.insert(startTag);
        assertEquals("Insert start tag failed", "<div></div>", element.outerHtml());
    }

    @Test
    public void testInsertComment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Comment commentToken = new Token.Comment("This is a comment");
        xmlTreeBuilder.insert(commentToken);
        assertEquals("Insert comment failed", "<!--This is a comment-->", xmlTreeBuilder.currentElement().html());
    }

    @Test
    public void testInsertCharacter() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Character characterToken = new Token.Character("Test text");
        xmlTreeBuilder.insert(characterToken);
        assertEquals("Insert character failed", "Test text", ((TextNode) xmlTreeBuilder.currentElement().childNode(0)).text());
    }

    @Test
    public void testInsertDoctype() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Doctype doctypeToken = new Token.Doctype("html", "", "", "");
        xmlTreeBuilder.insert(doctypeToken);
        assertEquals("Insert doctype failed", "<!DOCTYPE html>", xmlTreeBuilder.currentElement().html());
    }

    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.EndTag endTag = new Token.EndTag("div");
        xmlTreeBuilder.insert(new Token.StartTag("div"));
        xmlTreeBuilder.insert(new Token.StartTag("span"));
        xmlTreeBuilder.insert(new Token.StartTag("p"));
        xmlTreeBuilder.insert(new Token.EndTag("span"));
        xmlTreeBuilder.popStackToClose(endTag);
        assertEquals("Pop stack to close failed", "<div></div><p></p>", xmlTreeBuilder.currentElement().html());
    }

}