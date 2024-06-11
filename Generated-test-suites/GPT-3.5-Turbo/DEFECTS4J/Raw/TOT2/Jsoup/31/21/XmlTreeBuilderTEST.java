import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.DocumentType;
import org.jsoup.parser.Token;
import org.jsoup.helper.Validate;

import static org.junit.Assert.*;
import org.junit.Test;

public class XmlTreeBuilderTest {
    
    @Test
    public void testInsertNode() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Element element = new Element("div");
        xmlTreeBuilder.insertNode(element);
        assertEquals(element, xmlTreeBuilder.currentElement().child(0));
    }
    
    @Test
    public void testInsertStartTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("div");
        Element element = xmlTreeBuilder.insert(startTag);
        assertEquals("div", element.tagName());
    }
    
    @Test
    public void testInsertComment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Comment commentToken = new Token.Comment("test comment");
        xmlTreeBuilder.insert(commentToken);
        assertTrue(xmlTreeBuilder.currentElement().child(0) instanceof Comment);
    }
    
    @Test
    public void testInsertCharacter() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Character characterToken = new Token.Character("test character");
        xmlTreeBuilder.insert(characterToken);
        assertTrue(xmlTreeBuilder.currentElement().child(0) instanceof TextNode);
    }
    
    @Test
    public void testInsertDoctype() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Doctype doctype = new Token.Doctype("html", "", "", "");
        xmlTreeBuilder.insert(doctype);
        assertTrue(xmlTreeBuilder.currentElement().child(0) instanceof DocumentType);
    }
    
    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("div");
        xmlTreeBuilder.insert(startTag);
        Token.EndTag endTag = new Token.EndTag("div");
        xmlTreeBuilder.popStackToClose(endTag);
        assertTrue(xmlTreeBuilder.stack.isEmpty());
    }
}