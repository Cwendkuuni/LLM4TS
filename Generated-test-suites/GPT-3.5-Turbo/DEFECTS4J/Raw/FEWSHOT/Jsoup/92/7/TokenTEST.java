package org.jsoup.parser;

import static org.junit.Assert.*;
import org.junit.Test;

public class TokenTest {
    // Test Doctype class
    @Test
    public void testDoctypeReset() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("html");
        doctype.pubSysKey = "pub";
        doctype.publicIdentifier.append("//W3C//DTD HTML 4.01 Transitional//EN");
        doctype.systemIdentifier.append("http://www.w3.org/TR/html4/loose.dtd");
        doctype.forceQuirks = true;
        
        Token.Doctype resetDoctype = doctype.reset();
        
        assertEquals("", resetDoctype.name.toString());
        assertNull(resetDoctype.pubSysKey);
        assertEquals("", resetDoctype.publicIdentifier.toString());
        assertEquals("", resetDoctype.systemIdentifier.toString());
        assertFalse(resetDoctype.forceQuirks);
    }
    
    @Test
    public void testDoctypeGetName() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("html");
        
        assertEquals("html", doctype.getName());
    }
    
    @Test
    public void testDoctypeGetPubSysKey() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.pubSysKey = "pub";
        
        assertEquals("pub", doctype.getPubSysKey());
    }
    
    @Test
    public void testDoctypeGetPublicIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.publicIdentifier.append("//W3C//DTD HTML 4.01 Transitional//EN");
        
        assertEquals("//W3C//DTD HTML 4.01 Transitional//EN", doctype.getPublicIdentifier());
    }
    
    @Test
    public void testDoctypeGetSystemIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.systemIdentifier.append("http://www.w3.org/TR/html4/loose.dtd");
        
        assertEquals("http://www.w3.org/TR/html4/loose.dtd", doctype.getSystemIdentifier());
    }
    
    @Test
    public void testDoctypeIsForceQuirks() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.forceQuirks = true;
        
        assertTrue(doctype.isForceQuirks());
    }
    
    // Test StartTag class
    @Test
    public void testStartTagReset() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "div";
        startTag.normalName = "div";
        startTag.pendingAttributeName = "class";
        startTag.pendingAttributeValue.append("container");
        startTag.selfClosing = false;
        startTag.attributes = new Attributes();
        
        Token.StartTag resetStartTag = startTag.reset();
        
        assertNull(resetStartTag.tagName);
        assertNull(resetStartTag.normalName);
        assertNull(resetStartTag.pendingAttributeName);
        assertEquals("", resetStartTag.pendingAttributeValue.toString());
        assertFalse(resetStartTag.selfClosing);
        assertNull(resetStartTag.attributes);
    }
    
    @Test
    public void testStartTagNameAttr() {
        Token.StartTag startTag = new Token.StartTag();
        Attributes attributes = new Attributes();
        
        Token.StartTag nameAttrStartTag = startTag.nameAttr("div", attributes);
        
        assertEquals("div", nameAttrStartTag.tagName);
        assertEquals("div", nameAttrStartTag.normalName);
        assertEquals(attributes, nameAttrStartTag.attributes);
    }
    
    // Test EndTag class
    @Test
    public void testEndTagReset() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "div";
        endTag.normalName = "div";
        
        Token.EndTag resetEndTag = endTag.reset();
        
        assertNull(resetEndTag.tagName);
        assertNull(resetEndTag.normalName);
    }
    
    // Test Comment class
    @Test
    public void testCommentReset() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("Test Comment");
        comment.bogus = false;
        
        Token.Comment resetComment = comment.reset();
        
        assertEquals("", resetComment.data.toString());
        assertFalse(resetComment.bogus);
    }
    
    @Test
    public void testCommentGetData() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("Test Comment");
        
        assertEquals("Test Comment", comment.getData());
    }
    
    // Test Character class
    @Test
    public void testCharacterReset() {
        Token.Character character = new Token.Character();
        character.data = "Test Character";
        
        Token.Character resetCharacter = character.reset();
        
        assertNull(resetCharacter.data);
    }
    
    @Test
    public void testCharacterData() {
        Token.Character character = new Token.Character();
        character.data("Test Character");
        
        assertEquals("Test Character", character.getData());
    }
    
    // Test CData class
    @Test
    public void testCDataToString() {
        Token.CData cdata = new Token.CData("Test CData");
        
        assertEquals("<![CDATA[Test CData]]>", cdata.toString());
    }
    
    // Test EOF class
    @Test
    public void testEOFReset() {
        Token.EOF eof = new Token.EOF();
        
        Token.EOF resetEOF = eof.reset();
        
        assertEquals(resetEOF, eof);
    }
    
    // Test tokenType method
    @Test
    public void testTokenType() {
        Token token = new Token.Doctype();
        
        assertEquals("Doctype", token.tokenType());
    }
}