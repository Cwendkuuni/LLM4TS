import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Token;
import org.junit.Test;

public class XmlTreeBuilderTest {
    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.initialiseParse("<root></root>", "", new ParseErrorList());
        Element doc = builder.getDocument();
        
        assertEquals("root", doc.nodeName());
        assertTrue(builder.getStack().contains(doc));
    }

    @Test
    public void testProcess() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        
        // Test StartTag
        Token.StartTag startTag = new Token.StartTag("tag", "attr1=\"value1\" attr2=\"value2\"");
        builder.process(startTag);
        Element el1 = builder.getCurrentElement();
        
        assertEquals("tag", el1.nodeName());
        assertEquals("value1", el1.attr("attr1"));
        assertEquals("value2", el1.attr("attr2"));
        
        // Test EndTag
        Token.EndTag endTag = new Token.EndTag("tag");
        builder.process(endTag);
        Element el2 = builder.getCurrentElement();
        
        assertEquals("tag", el2.nodeName());
        assertNull(el2.attr("attr1"));
        assertNull(el2.attr("attr2"));
        
        // Test Comment
        Token.Comment commentToken = new Token.Comment("test comment");
        builder.process(commentToken);
        Node comment = builder.getCurrentElement().childNode(0);
        
        assertTrue(comment instanceof Comment);
        assertEquals("test comment", ((Comment) comment).getData());
        
        // Test Character
        Token.Character characterToken = new Token.Character("test character");
        builder.process(characterToken);
        Node character = builder.getCurrentElement().childNode(0);
        
        assertTrue(character instanceof TextNode);
        assertEquals("test character", ((TextNode) character).getWholeText());
        
        // Test Doctype
        Token.Doctype doctype = new Token.Doctype("html", "", "", "");
        builder.process(doctype);
        Node doctypeNode = builder.getCurrentElement().childNode(0);
        
        assertTrue(doctypeNode instanceof DocumentType);
        assertEquals("html", ((DocumentType) doctypeNode).getName());
    }

    @Test
    public void testInsert() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        
        // Test insert StartTag
        Token.StartTag startTag = new Token.StartTag("tag", "attr1=\"value1\" attr2=\"value2\"");
        Element el1 = builder.insert(startTag);
        Element doc = builder.getDocument();
        
        assertEquals("tag", el1.nodeName());
        assertEquals("value1", el1.attr("attr1"));
        assertEquals("value2", el1.attr("attr2"));
        assertFalse(el1.hasParent());
        assertTrue(builder.getStack().contains(el1));
        
        // Test insert Comment
        Token.Comment commentToken = new Token.Comment("test comment");
        builder.insert(commentToken);
        Node comment = builder.getCurrentElement().childNode(0);
        
        assertTrue(comment instanceof Comment);
        assertEquals("test comment", ((Comment) comment).getData());
        
        // Test insert Character
        Token.Character characterToken = new Token.Character("test character");
        builder.insert(characterToken);
        Node character = builder.getCurrentElement().childNode(1);
        
        assertTrue(character instanceof TextNode);
        assertEquals("test character", ((TextNode) character).getWholeText());
        
        // Test insert Doctype
        Token.Doctype doctype = new Token.Doctype("html", "", "", "");
        builder.insert(doctype);
        Node doctypeNode = builder.getCurrentElement().childNode(2);
        
        assertTrue(doctypeNode instanceof DocumentType);
        assertEquals("html", ((DocumentType) doctypeNode).getName());
    }

    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        
        Token.StartTag startTag1 = new Token.StartTag("tag1");
        builder.process(startTag1);
        
        Token.StartTag startTag2 = new Token.StartTag("tag2");
        builder.process(startTag2);
        
        builder.popStackToClose(new Token.EndTag("tag1"));
        
        Element currentElement = builder.getCurrentElement();
        assertEquals("tag2", currentElement.nodeName());
    }
}