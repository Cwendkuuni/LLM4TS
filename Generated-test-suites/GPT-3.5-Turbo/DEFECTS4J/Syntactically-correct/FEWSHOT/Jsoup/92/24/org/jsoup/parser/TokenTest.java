package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TokenTest {
    @Test
    public void testTokenType() {
        StartTag startTag = new StartTag();
        assertEquals("StartTag", startTag.tokenType());
        
        EndTag endTag = new EndTag();
        assertEquals("EndTag", endTag.tokenType());
        
        Doctype doctype = new Doctype();
        assertEquals("Doctype", doctype.tokenType());
        
        Comment comment = new Comment();
        assertEquals("Comment", comment.tokenType());
        
        Character character = new Character();
        assertEquals("Character", character.tokenType());
        
        EOF eof = new EOF();
        assertEquals("EOF", eof.tokenType());
    }
    
    @Test
    public void testDoctypeReset() {
        Doctype doctype = new Doctype();
        doctype.name.append("name");
        doctype.pubSysKey = "key";
        doctype.publicIdentifier.append("public");
        doctype.systemIdentifier.append("system");
        doctype.forceQuirks = true;
        
        doctype.reset();
        
        assertEquals("", doctype.name.toString());
        assertNull(doctype.pubSysKey);
        assertEquals("", doctype.publicIdentifier.toString());
        assertEquals("", doctype.systemIdentifier.toString());
        assertEquals(false, doctype.forceQuirks);
    }
    
    @Test
    public void testStartTagReset() {
        StartTag startTag = new StartTag();
        startTag.tagName = "tag";
        startTag.normalName = "normal";
        startTag.pendingAttributeName = "attr";
        startTag.pendingAttributeValue.append("value");
        startTag.pendingAttributeValueS = "valueS";
        startTag.hasEmptyAttributeValue = true;
        startTag.hasPendingAttributeValue = true;
        startTag.selfClosing = true;
        startTag.attributes = new Attributes();
        
        startTag.reset();
        
        assertNull(startTag.tagName);
        assertNull(startTag.normalName);
        assertNull(startTag.pendingAttributeName);
        assertEquals("", startTag.pendingAttributeValue.toString());
        assertNull(startTag.pendingAttributeValueS);
        assertEquals(false, startTag.hasEmptyAttributeValue);
        assertEquals(false, startTag.hasPendingAttributeValue);
        assertEquals(false, startTag.selfClosing);
        assertNull(startTag.attributes);
    }
    
    @Test
    public void testEndTagReset() {
        EndTag endTag = new EndTag();
        endTag.tagName = "tag";
        endTag.normalName = "normal";
        endTag.pendingAttributeName = "attr";
        endTag.pendingAttributeValue.append("value");
        endTag.pendingAttributeValueS = "valueS";
        endTag.hasEmptyAttributeValue = true;
        endTag.hasPendingAttributeValue = true;
        endTag.selfClosing = true;
        endTag.attributes = new Attributes();
        
        endTag.reset();
        
        assertNull(endTag.tagName);
        assertNull(endTag.normalName);
        assertNull(endTag.pendingAttributeName);
        assertEquals("", endTag.pendingAttributeValue.toString());
        assertNull(endTag.pendingAttributeValueS);
        assertEquals(false, endTag.hasEmptyAttributeValue);
        assertEquals(false, endTag.hasPendingAttributeValue);
        assertEquals(false, endTag.selfClosing);
        assertNull(endTag.attributes);
    }
    
    @Test
    public void testCommentReset() {
        Comment comment = new Comment();
        comment.data.append("data");
        comment.bogus = true;
        
        comment.reset();
        
        assertEquals("", comment.data.toString());
        assertEquals(false, comment.bogus);
    }
    
    @Test
    public void testCharacterReset() {
        Character character = new Character();
        character.data = "data";
        
        character.reset();
        
        assertNull(character.data);
    }
    
    @Test
    public void testStartTagToString() {
        StartTag startTag = new StartTag();
        startTag.tagName = "tag";
        startTag.attributes = new Attributes();
        startTag.attributes.put("attr1", "value1");
        startTag.attributes.put("attr2", "value2");
        
        String expected = "<tag attr1=\"value1\" attr2=\"value2\">";
        assertEquals(expected, startTag.toString());
    }
    
    @Test
    public void testEndTagToString() {
        EndTag endTag = new EndTag();
        endTag.tagName = "tag";
        
        String expected = "</tag>";
        assertEquals(expected, endTag.toString());
    }
    
    @Test
    public void testCommentToString() {
        Comment comment = new Comment();
        comment.data.append("data");
        comment.bogus = true;
        
        String expected = "<!--data-->";
        assertEquals(expected, comment.toString());
    }
    
    @Test
    public void testCDataToString() {
        CData cdata = new CData("data");
        
        String expected = "<![CDATA[data]]>";
        assertEquals(expected, cdata.toString());
    }
    
    @Test
    public void testEOFReset() {
        EOF eof = new EOF();
        
        eof.reset();
        
        assertEquals(eof, eof.tokenType());
    }
}