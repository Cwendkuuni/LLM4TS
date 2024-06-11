package org.jsoup.parser;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
 
import static org.junit.Assert.assertEquals;

// Test Doctype class
public class TokenTest {
    
    @Test
    public void testDoctype() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("html");
        doctype.publicIdentifier.append("-//W3C//DTD HTML 4.01//EN");
        doctype.systemIdentifier.append("http://www.w3.org/TR/html4/strict.dtd");
        doctype.forceQuirks = true;

        assertEquals("html", doctype.getName());
        assertEquals("-//W3C//DTD HTML 4.01//EN", doctype.getPublicIdentifier());
        assertEquals("http://www.w3.org/TR/html4/strict.dtd", doctype.getSystemIdentifier());
        assertEquals(true, doctype.isForceQuirks());
    }
}

// Test Tag class
public class TokenTest {
    @Test
    public void testTag() {
        Token.Tag tag = new Token.Tag();
        tag.tagName = "div";
        tag.selfClosing = true;
        
        Attributes attributes = new Attributes();
        Attribute attribute1 = new Attribute("class", "container");
        Attribute attribute2 = new Attribute("id", "main");
        attributes.put(attribute1);
        attributes.put(attribute2);
        tag.attributes = attributes;
        
        assertEquals("div", tag.name());
        assertEquals(true, tag.isSelfClosing());
        assertEquals(attributes, tag.getAttributes());
    }
}

// Test StartTag class
public class TokenTest {
    @Test
    public void testStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "div";
        startTag.selfClosing = false;
        
        Attributes attributes = new Attributes();
        Attribute attribute1 = new Attribute("class", "container");
        Attribute attribute2 = new Attribute("id", "main");
        attributes.put(attribute1);
        attributes.put(attribute2);
        startTag.attributes = attributes;
        
        assertEquals("div", startTag.name());
        assertEquals(false, startTag.isSelfClosing());
        assertEquals(attributes, startTag.getAttributes());
        assertEquals("<div class=\"container\" id=\"main\">", startTag.toString());
    }
}

// Test EndTag class
public class TokenTest {
    @Test
    public void testEndTag() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "div";
        
        assertEquals("div", endTag.name());
        assertEquals("</div>", endTag.toString());
    }
}

// Test Comment class
public class TokenTest {
    @Test
    public void testComment() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("This is a comment.");
        
        assertEquals("This is a comment.", comment.getData());
        assertEquals("<!--This is a comment.-->", comment.toString());
    }
}

// Test Character class
public class TokenTest {
    @Test
    public void testCharacter() {
        Token.Character character = new Token.Character("text");
        
        assertEquals("text", character.getData());
        assertEquals("text", character.toString());
    }
}

// Test EOF class
public class TokenTest {
    @Test
    public void testEOF() {
        Token.EOF eof = new Token.EOF();
        
        assertEquals(Token.TokenType.EOF, eof.type);
    }
}