
package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.BooleanAttribute;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokenTest {

    private static Token.Doctype doctype;
    private static Token.EndTag endTag;
    private static Token.StartTag startTag;
    private static Token.Comment comment;
    private static Token.Character character;
    private static Token.EOF eof;

    @BeforeClass
    public static void setUp() {
        doctype = new Token.Doctype();
        endTag = new Token.EndTag();
        startTag = new Token.StartTag();
        comment = new Token.Comment();
        character = new Token.Character();
        eof = new Token.EOF();
    }

    // Doctype tests
    
    @Test
    public void testDoctypeReset() {
        doctype.name.append("html");
        doctype.pubSysKey = "html";
        doctype.publicIdentifier.append("-//W3C//DTD XHTML 1.0 Transitional//EN");
        doctype.systemIdentifier.append("http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd");
        doctype.forceQuirks = true;
        
        doctype.reset();
        
        assertEquals("", doctype.getName());
        assertNull(doctype.getPubSysKey());
        assertEquals("", doctype.getPublicIdentifier());
        assertEquals("", doctype.getSystemIdentifier());
        assertFalse(doctype.isForceQuirks());
    }
    
    @Test
    public void testDoctypeGetName() {
        doctype.name.append("html");
        
        assertEquals("html", doctype.getName());
    }
    
    @Test
    public void testDoctypeGetPubSysKey() {
        doctype.pubSysKey = "html";
        
        assertEquals("html", doctype.getPubSysKey());
    }
    
    @Test
    public void testDoctypeGetPublicIdentifier() {
        doctype.publicIdentifier.append("-//W3C//DTD XHTML 1.0 Transitional//EN");
        
        assertEquals("-//W3C//DTD XHTML 1.0 Transitional//EN", doctype.getPublicIdentifier());
    }
    
    @Test
    public void testDoctypeGetSystemIdentifier() {
        doctype.systemIdentifier.append("http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd");
        
        assertEquals("http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", doctype.getSystemIdentifier());
    }
    
    @Test
    public void testDoctypeIsForceQuirks() {
        doctype.forceQuirks = true;
        
        assertTrue(doctype.isForceQuirks());
    }
    
    // StartTag tests
    
    @Test
    public void testStartTagReset() {
        startTag.tagName = "div";
        startTag.normalName = "div";
        startTag.pendingAttributeName = "class";
        startTag.pendingAttributeValue.append("container");
        startTag.selfClosing = true;
        startTag.attributes = new Attributes();
        
        startTag.reset();
        
        assertNull(startTag.tagName);
        assertNull(startTag.normalName);
        assertNull(startTag.pendingAttributeName);
        assertEquals("", startTag.pendingAttributeValue.toString());
        assertNull(startTag.pendingAttributeValueS);
        assertFalse(startTag.hasEmptyAttributeValue);
        assertFalse(startTag.hasPendingAttributeValue);
        assertFalse(startTag.selfClosing);
        assertNull(startTag.attributes);
    }
    
    @Test
    public void testStartTagName() {
        startTag.tagName = "div";
        
        assertEquals("div", startTag.name());
    }
    
    @Test
    public void testStartTagNormalName() {
        startTag.normalName = "div";
        
        assertEquals("div", startTag.normalName());
    }
    
    @Test
    public void testStartTagIsSelfClosing() {
        startTag.selfClosing = true;
        
        assertTrue(startTag.isSelfClosing());
    }
    
    // EndTag tests
    
    @Test
    public void testEndTagReset() {
        endTag.tagName = "div";
        endTag.normalName = "div";
        
        endTag.reset();
        
        assertNull(endTag.tagName);
        assertNull(endTag.normalName);
    }
    
    // Comment tests
    
    @Test
    public void testCommentReset() {
        comment.data.append("This is a comment");
        comment.bogus = true;
        
        comment.reset();
        
        assertEquals("", comment.getData());
        assertFalse(comment.bogus);
    }
    
    // Character tests
    
    @Test
    public void testCharacterReset() {
        character.data = "This is a character";
        
        character.reset();
        
        assertNull(character.getData());
    }
    
    // EOF tests
    
    @Test
    public void testEOFReset() {
        Token.EOF result = eof.reset();
        
        assertSame(eof, result);
    }
    
    // TokenType tests
    
    @Test
    public void testIsDoctype() {
        assertFalse(doctype.isDoctype());
    }
    
    @Test
    public void testIsStartTag() {
        assertFalse(startTag.isStartTag());
    }
    
    @Test
    public void testIsEndTag() {
        assertFalse(endTag.isEndTag());
    }
    
    @Test
    public void testIsComment() {
        assertFalse(comment.isComment());
    }
    
    @Test
    public void testIsCharacter() {
        assertFalse(character.isCharacter());
    }
    
    @Test
    public void testIsEOF() {
        assertFalse(eof.isEOF());
    }
}
