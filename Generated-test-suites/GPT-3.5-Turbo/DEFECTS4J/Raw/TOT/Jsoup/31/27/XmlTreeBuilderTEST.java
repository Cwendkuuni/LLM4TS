package org.jsoup.parser;

import org.junit.Test;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.Doctype;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String input = "<root></root>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        
        builder.initialiseParse(input, baseUri, errors);
        
        Element root = builder.getRoot();
        assertNotNull(root);
        assertTrue(root.tagName().equals("root"));
        assertEquals(baseUri, root.baseUri());
        assertFalse(errors.hasErrors());
    }
    
    @Test
    public void testProcess_StartTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element root = new Element("root");
        builder.setStack(Arrays.asList(root));
        StartTag startTag = new StartTag("child", new Attributes());
        
        boolean result = builder.process(new Token(startTag));
        assertTrue(result);
        
        assertEquals(1, root.children().size());
        Element child = root.children().get(0);
        assertNotNull(child);
        assertTrue(child.tagName().equals("child"));
    }
    
    @Test
    public void testProcess_EndTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element root = new Element("root");
        Element child = new Element("child");
        root.appendChild(child);
        builder.setStack(Arrays.asList(root, child));
        EndTag endTag = new EndTag("child");
        
        boolean result = builder.process(new Token(endTag));
        assertTrue(result);
        
        assertEquals(0, child.children().size());
    }
    
    @Test
    public void testProcess_Comment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element root = new Element("root");
        builder.setStack(Arrays.asList(root));
        Comment commentToken = new Comment("Comment");
        
        boolean result = builder.process(new Token(commentToken));
        assertTrue(result);
        
        assertEquals(1, root.children().size());
        Comment comment = (Comment) root.children().get(0);
        assertNotNull(comment);
        assertEquals("Comment", comment.getData());
    }
    
    @Test
    public void testProcess_Character() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element root = new Element("root");
        builder.setStack(Arrays.asList(root));
        Character characterToken = new Character("Text");
        
        boolean result = builder.process(new Token(characterToken));
        assertTrue(result);
        
        assertEquals(1, root.children().size());
        TextNode textNode = (TextNode) root.children().get(0);
        assertNotNull(textNode);
        assertEquals("Text", textNode.getWholeText());
    }
    
    @Test
    public void testProcess_Doctype() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element root = new Element("root");
        builder.setStack(Arrays.asList(root));
        Doctype doctype = new Doctype("html", "", "", "");
        
        boolean result = builder.process(new Token(doctype));
        assertTrue(result);
        
        assertEquals(1, root.children().size());
        DocumentType documentType = (DocumentType) root.children().get(0);
        assertNotNull(documentType);
        assertEquals("html", documentType.getName());
    }
    
    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element root = new Element("root");
        Element child1 = new Element("child");
        Element child2 = new Element("child");
        Element child3 = new Element("child");
        root.appendChild(child1);
        root.appendChild(child2);
        root.appendChild(child3);
        builder.setStack(Arrays.asList(root, child1, child2, child3));
        EndTag endTag = new EndTag("child");
        
        builder.popStackToClose(new Token(endTag));
        
        assertEquals(1, root.children().size());
        assertEquals(child3, root.children().get(0));
    }

}