
import org.junit.Test;

import static org.junit.Assert.*;

public class TokenTest {
    
    // Doctype tests
    
    @Test
    public void testDoctypeReset() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("html");
        doctype.pubSysKey = "pubSysKey";
        doctype.publicIdentifier.append("//public-identifier");
        doctype.systemIdentifier.append("//system-identifier");
        doctype.forceQuirks = true;
        
        doctype.reset();
        
        assertEquals("", doctype.name.toString());
        assertNull(doctype.pubSysKey);
        assertEquals("", doctype.publicIdentifier.toString());
        assertEquals("", doctype.systemIdentifier.toString());
        assertFalse(doctype.forceQuirks);
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
        doctype.pubSysKey = "pubSysKey";
        
        assertEquals("pubSysKey", doctype.getPubSysKey());
    }
    
    @Test
    public void testDoctypeGetPublicIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.publicIdentifier.append("//public-identifier");
        
        assertEquals("//public-identifier", doctype.getPublicIdentifier());
    }
    
    @Test
    public void testDoctypeGetSystemIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.systemIdentifier.append("//system-identifier");
        
        assertEquals("//system-identifier", doctype.getSystemIdentifier());
    }
    
    @Test
    public void testDoctypeIsForceQuirks() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.forceQuirks = true;
        
        assertTrue(doctype.isForceQuirks());
    }
    
    // Tag tests
    
    @Test
    public void testTagReset() {
        Token.Tag tag = new Token.StartTag();
        tag.tagName = "div";
        tag.normalName = "div";
        tag.pendingAttributeName = "class";
        tag.pendingAttributeValue.append("container");
        tag.pendingAttributeValueS = null;
        tag.hasEmptyAttributeValue = true;
        tag.hasPendingAttributeValue = true;
        tag.selfClosing = true;
        tag.attributes = new org.jsoup.nodes.Attributes();
        tag.attributes.put("class", "container");
        
        tag.reset();
        
        assertNull(tag.tagName);
        assertNull(tag.normalName);
        assertNull(tag.pendingAttributeName);
        assertEquals("", tag.pendingAttributeValue.toString());
        assertNull(tag.pendingAttributeValueS);
        assertFalse(tag.hasEmptyAttributeValue);
        assertFalse(tag.hasPendingAttributeValue);
        assertFalse(tag.selfClosing);
        assertNull(tag.attributes);
    }
    
    @Test
    public void testStartTagNameAttr() {
        Token.StartTag startTag = new Token.StartTag();
        org.jsoup.nodes.Attributes attributes = new org.jsoup.nodes.Attributes();
        attributes.put("class", "container");
        
        startTag.nameAttr("div", attributes);
        
        assertEquals("div", startTag.tagName);
        assertEquals("div", startTag.normalName);
        assertEquals(attributes, startTag.attributes);
    }
    
    @Test
    public void testStartTagToStringWithAttributes() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "div";
        startTag.attributes = new org.jsoup.nodes.Attributes();
        startTag.attributes.put("class", "container");
        
        assertEquals("<div class=\"container\">", startTag.toString());
    }
    
    @Test
    public void testStartTagToStringWithoutAttributes() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "div";
        
        assertEquals("<div>", startTag.toString());
    }
    
    @Test
    public void testEndTagToString() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "div";
        
        assertEquals("</div>", endTag.toString());
    }
    
    // Comment tests
    
    @Test
    public void testCommentReset() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("sample comment");
        comment.bogus = true;
        
        comment.reset();
        
        assertEquals("", comment.data.toString());
        assertFalse(comment.bogus);
    }
    
    @Test
    public void testCommentGetData() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("sample comment");
        
        assertEquals("sample comment", comment.getData());
    }
    
    @Test
    public void testCommentToString() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("sample comment");
        
        assertEquals("<!--sample comment-->", comment.toString());
    }
    
    // Character tests
    
    @Test
    public void testCharacterReset() {
        Token.Character character = new Token.Character();
        character.data = "sample character";
        
        character.reset();
        
        assertNull(character.data);
    }
    
    @Test
    public void testCharacterData() {
        Token.Character character = new Token.Character();
        character.data("sample character");
        
        assertEquals("sample character", character.getData());
    }
    
    @Test
    public void testCharacterToString() {
        Token.Character character = new Token.Character();
        character.data = "sample character";
        
        assertEquals("sample character", character.toString());
    }
    
    // CData tests
    
    @Test
    public void testCDataToString() {
        Token.CData cdata = new Token.CData("sample cdata");
        
        assertEquals("<![CDATA[sample cdata]]>", cdata.toString());
    }
    
    // EOF tests
    
    @Test
    public void testEOFReset() {
        Token.EOF eof = new Token.EOF();
        
        eof.reset();
        
        assertNotNull(eof);
    }
    
    // Other tests
    
    @Test
    public void testIsDoctype() {
        Token token = new Token.Doctype();
        
        assertTrue(token.isDoctype());
        assertFalse(token.isStartTag());
        assertFalse(token.isEndTag());
        assertFalse(token.isComment());
        assertFalse(token.isCharacter());
        assertFalse(token.isCData());
        assertFalse(token.isEOF());
    }
    
    @Test
    public void testIsStartTag() {
        Token token = new Token.StartTag();
        
        assertFalse(token.isDoctype());
        assertTrue(token.isStartTag());
        assertFalse(token.isEndTag());
        assertFalse(token.isComment());
        assertFalse(token.isCharacter());
        assertFalse(token.isCData());
        assertFalse(token.isEOF());
    }
    
    @Test
    public void testIsEndTag() {
        Token token = new Token.EndTag();
        
        assertFalse(token.isDoctype());
        assertFalse(token.isStartTag());
        assertTrue(token.isEndTag());
        assertFalse(token.isComment());
        assertFalse(token.isCharacter());
        assertFalse(token.isCData());
        assertFalse(token.isEOF());
    }
    
    @Test
    public void testIsComment() {
        Token token = new Token.Comment();
        
        assertFalse(token.isDoctype());
        assertFalse(token.isStartTag());
        assertFalse(token.isEndTag());
        assertTrue(token.isComment());
        assertFalse(token.isCharacter());
        assertFalse(token.isCData());
        assertFalse(token.isEOF());
    }
    
    @Test
    public void testIsCharacter() {
        Token token = new Token.Character();
        
        assertFalse(token.isDoctype());
        assertFalse(token.isStartTag());
        assertFalse(token.isEndTag());
        assertFalse(token.isComment());
        assertTrue(token.isCharacter());
        assertFalse(token.isCData());
        assertFalse(token.isEOF());
    }
    
    @Test
    public void testIsCData() {
        Token token = new Token.CData("sample cdata");
        
        assertFalse(token.isDoctype());
        assertFalse(token.isStartTag());
        assertFalse(token.isEndTag());
        assertFalse(token.isComment());
        assertFalse(token.isCharacter());
        assertTrue(token.isCData());
        assertFalse(token.isEOF());
    }
    
    @Test
    public void testIsEOF() {
        Token token = new Token.EOF();
        
        assertFalse(token.isDoctype());
        assertFalse(token.isStartTag());
        assertFalse(token.isEndTag());
        assertFalse(token.isComment());
        assertFalse(token.isCharacter());
        assertFalse(token.isCData());
        assertTrue(token.isEOF());
    }
}
