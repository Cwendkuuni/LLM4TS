package org.jsoup.parser;

import org.jsoup.nodes.*;

import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

public class XmlTreeBuilderTest {
    @Test
    public void testInitialiseParse() {
        // Test if doc is placed onto the stack and if syntax is set to xml
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        treeBuilder.initialiseParse(null, null, null);
        assertEquals(treeBuilder.stack.get(0), treeBuilder.doc);
        assertEquals(Document.OutputSettings.Syntax.xml, treeBuilder.doc.outputSettings().syntax());
    }

    @Test
    public void testProcess() {
        // Test if the correct methods are called based on the token type
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        
        // Test StartTag
        Token token = new Token(Token.TokenType.StartTag, "tag", null);
        treeBuilder.process(token);
        assertEquals(treeBuilder.doc.childNodes().get(0).nodeName(), "tag");
        
        // Test EndTag
        token = new Token(Token.TokenType.EndTag, "tag", null);
        treeBuilder.process(token);
        assertTrue(treeBuilder.doc.childNodes().isEmpty());
        
        // Test Comment
        token = new Token(Token.TokenType.Comment, "comment", null);
        treeBuilder.process(token);
        assertEquals(treeBuilder.doc.childNodes().get(0).nodeName(), "#comment");
        
        // Test Character
        token = new Token(Token.TokenType.Character, "character", null);
        treeBuilder.process(token);
        assertEquals(treeBuilder.doc.childNodes().get(0).nodeName(), "#text");
        
        // Test Doctype
        token = new Token(Token.TokenType.Doctype, "doctype", null);
        treeBuilder.process(token);
        assertEquals(treeBuilder.doc.childNodes().get(0).nodeName(), "!doctype");
        
        // Test EOF
        token = new Token(Token.TokenType.EOF, null, null);
        treeBuilder.process(token);
        assertTrue(treeBuilder.doc.childNodes().isEmpty());
        
        // Test unexpected token type
        token = new Token(null, null, null);
        try {
            treeBuilder.process(token);
            fail("Expected Validate.fail to throw an exception");
        } catch (Exception e) {
            assertEquals(e.getMessage(), "Unexpected token type: null");
        }
    }

    @Test
    public void testInsertNode() {
        // Test if a node is appended correctly to the current element
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Document doc = new Document("");
        Element element = new Element(Tag.valueOf("div"), "");
        doc.appendChild(element);
        treeBuilder.stack.add(element);
        
        Node node = new Comment("comment");
        treeBuilder.insertNode(node);
        
        assertEquals(treeBuilder.currentElement().childNodes().get(0), node);
    }

    @Test
    public void testInsert() {
        // Test if a StartTag is correctly inserted into the document and stack
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        
        // Test StartTag with self closing flag
        Token.StartTag startTag = new Token.StartTag("tag", null, true);
        Element element = treeBuilder.insert(startTag);
        assertEquals(treeBuilder.doc.childNodes().get(0), element);
        assertTrue(element.tag().selfClosing());
        
        // Test StartTag without self closing flag
        startTag = new Token.StartTag("tag", null, false);
        element = treeBuilder.insert(startTag);
        assertEquals(treeBuilder.stack.get(0), element);
        
        // Test unknown tag
        treeBuilder.stack.clear();
        startTag = new Token.StartTag("unknown", null, true);
        element = treeBuilder.insert(startTag);
        assertTrue(element.tag().isKnownTag());
        assertTrue(element.tag().selfClosing());
    }

    @Test
    public void testInsertComment() {
        // Test if a Comment is correctly inserted into the document
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        
        // Test regular comment
        Token.Comment commentToken = new Token.Comment("comment", null);
        treeBuilder.insert(commentToken);
        assertEquals(treeBuilder.doc.childNodes().get(0).nodeName(), "#comment");
        
        // Test comment with declaration
        commentToken = new Token.Comment("!DOCTYPE", null);
        treeBuilder.insert(commentToken);
        assertEquals(treeBuilder.doc.childNodes().get(1).nodeName(), "!doctype");
    }

    @Test
    public void testInsertCharacter() {
        // Test if a Character is correctly inserted into the document
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        
        Token.Character characterToken = new Token.Character("character");
        treeBuilder.insert(characterToken);
        assertEquals(treeBuilder.doc.childNodes().get(0).nodeName(), "#text");
    }

    @Test
    public void testInsertDoctype() {
        // Test if a Doctype is correctly inserted into the document
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        
        Token.Doctype doctype = new Token.Doctype("name", null, null);
        treeBuilder.insert(doctype);
        assertEquals(treeBuilder.doc.childNodes().get(0).nodeName(), "!doctype");
    }

    @Test
    public void testPopStackToClose() {
        // Test if the stack is correctly popped until the first occurrence of the given EndTag name
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        
        treeBuilder.stack.add(new Element(Tag.valueOf("tag1"), ""));
        treeBuilder.stack.add(new Element(Tag.valueOf("tag2"), ""));
        treeBuilder.stack.add(new Element(Tag.valueOf("tag1"), ""));
        treeBuilder.stack.add(new Element(Tag.valueOf("tag3"), ""));
        
        Token.EndTag endTag = new Token.EndTag("tag1");
        treeBuilder.popStackToClose(endTag);
        assertFalse(treeBuilder.stack.contains(endTag));
        assertEquals(treeBuilder.stack.size(), 2);
    }

    @Test
    public void testParseFragment() {
        // Test if the fragment is correctly parsed and returned as a list of nodes
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        
        String inputFragment = "<div>text</div>";
        List<Node> nodes = treeBuilder.parseFragment(inputFragment, "", null);
        
        assertEquals(nodes.size(), 1);
        assertTrue(nodes.get(0) instanceof Element);
        assertEquals(nodes.get(0).nodeName(), "div");
    }
}