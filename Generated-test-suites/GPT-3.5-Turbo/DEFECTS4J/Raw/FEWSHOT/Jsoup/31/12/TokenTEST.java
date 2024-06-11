package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokenTest {
    
    @Test
    public void testTokenType() {
        Token token1 = new Token.Doctype();
        assertEquals("Doctype", token1.tokenType());
        
        Token token2 = new Token.StartTag();
        assertEquals("StartTag", token2.tokenType());
        
        Token token3 = new Token.EndTag();
        assertEquals("EndTag", token3.tokenType());
        
        Token token4 = new Token.Comment();
        assertEquals("Comment", token4.tokenType());
        
        Token token5 = new Token.Character("");
        assertEquals("Character", token5.tokenType());
        
        Token token6 = new Token.EOF();
        assertEquals("EOF", token6.tokenType());
    }
    
    @Test
    public void testDoctypeGettersAndSetters() {
        Token.Doctype doctype = new Token.Doctype();
        
        doctype.name.append("name");
        assertEquals("name", doctype.getName());
        
        doctype.publicIdentifier.append("publicIdentifier");
        assertEquals("publicIdentifier", doctype.getPublicIdentifier());
        
        doctype.systemIdentifier.append("systemIdentifier");
        assertEquals("systemIdentifier", doctype.getSystemIdentifier());
        
        doctype.forceQuirks = true;
        assertTrue(doctype.isForceQuirks());
    }
    
    @Test
    public void testTag() {
        Token.Tag tag = new Token.StartTag();
        
        tag.name = "name";
        assertEquals("name", tag.name());
        
        tag.selfClosing = true;
        assertTrue(tag.isSelfClosing());
        
        tag.attributes = new Attributes();
        Attribute attribute = new Attribute("attrName", "attrValue");
        tag.attributes.put(attribute);
        assertEquals(1, tag.getAttributes().size());
        
        tag.newAttribute();
        assertNull(tag.pendingAttributeName);
        
        tag.appendTagName("append");
        assertEquals("append", tag.tagName);
        
        tag.appendTagName('a');
        assertEquals("appenda", tag.tagName);
        
        tag.appendAttributeName("attr");
        assertEquals("attr", tag.pendingAttributeName);
        
        tag.appendAttributeValue("value");
        assertEquals("value", tag.pendingAttributeValue.toString());
        
        tag.finaliseTag();
        assertEquals(2, tag.getAttributes().size());
    }
    
    @Test
    public void testStartTagConstructor() {
        Token.StartTag startTag = new Token.StartTag("name");
        assertEquals("name", startTag.name());
        
        Attributes attributes = new Attributes();
        Attribute attribute = new Attribute("attrName", "attrValue");
        attributes.put(attribute);
        Token.StartTag startTagWithAttributes = new Token.StartTag("name", attributes);
        assertEquals("name", startTagWithAttributes.name());
        assertEquals(1, startTagWithAttributes.getAttributes().size());
    }
    
    @Test
    public void testTagToString() {
        Token.StartTag startTagWithAttributes = new Token.StartTag("name");
        assertEquals("<name>", startTagWithAttributes.toString());
        
        Attributes attributes = new Attributes();
        Attribute attribute = new Attribute("attrName", "attrValue");
        attributes.put(attribute);
        Token.StartTag startTag = new Token.StartTag("name", attributes);
        assertEquals("<name attrName=\"attrValue\">", startTag.toString());
        
        Token.EndTag endTag = new Token.EndTag("name");
        assertEquals("</name>", endTag.toString());
        
        Token.Comment comment = new Token.Comment();
        comment.data.append("data");
        assertEquals("<!--data-->", comment.toString());
        
        Token.Character character = new Token.Character("data");
        assertEquals("data", character.toString());
    }
    
    @Test
    public void testDoctypeType() {
        Token token = new Token.Doctype();
        assertTrue(token.isDoctype());
        
        assertFalse(token.isStartTag());
        assertFalse(token.isEndTag());
        assertFalse(token.isComment());
        assertFalse(token.isCharacter());
        assertFalse(token.isEOF());
    }
    
    @Test
    public void testDoctypeAsDoctype() {
        Token.Doctype doctype = new Token.Doctype();
        Token token = doctype;
        
        assertEquals(doctype, token.asDoctype());
    }
    
    @Test
    public void testStartTagType() {
        Token token = new Token.StartTag();
        assertTrue(token.isStartTag());
        
        assertFalse(token.isDoctype());
        assertFalse(token.isEndTag());
        assertFalse(token.isComment());
        assertFalse(token.isCharacter());
        assertFalse(token.isEOF());
    }
    
    @Test
    public void testStartTagAsStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        Token token = startTag;
        
        assertEquals(startTag, token.asStartTag());
    }
    
    @Test
    public void testEndTagType() {
        Token token = new Token.EndTag();
        assertTrue(token.isEndTag());
        
        assertFalse(token.isDoctype());
        assertFalse(token.isStartTag());
        assertFalse(token.isComment());
        assertFalse(token.isCharacter());
        assertFalse(token.isEOF());
    }
    
    @Test
    public void testEndTagAsEndTag() {
        Token.EndTag endTag = new Token.EndTag();
        Token token = endTag;
        
        assertEquals(endTag, token.asEndTag());
    }
    
    @Test
    public void testCommentType() {
        Token token = new Token.Comment();
        assertTrue(token.isComment());
        
        assertFalse(token.isDoctype());
        assertFalse(token.isStartTag());
        assertFalse(token.isEndTag());
        assertFalse(token.isCharacter());
        assertFalse(token.isEOF());
    }
    
    @Test
    public void testCommentAsComment() {
        Token.Comment comment = new Token.Comment();
        Token token = comment;
        
        assertEquals(comment, token.asComment());
    }
    
    @Test
    public void testCharacterType() {
        Token token = new Token.Character("data");
        assertTrue(token.isCharacter());
        
        assertFalse(token.isDoctype());
        assertFalse(token.isStartTag());
        assertFalse(token.isEndTag());
        assertFalse(token.isComment());
        assertFalse(token.isEOF());
    }
    
    @Test
    public void testCharacterAsCharacter() {
        Token.Character character = new Token.Character("data");
        Token token = character;
        
        assertEquals(character, token.asCharacter());
    }
    
    @Test
    public void testEOFType() {
        Token token = new Token.EOF();
        assertTrue(token.isEOF());
        
        assertFalse(token.isDoctype());
        assertFalse(token.isStartTag());
        assertFalse(token.isEndTag());
        assertFalse(token.isComment());
        assertFalse(token.isCharacter());
    }
    
    @Test
    public void testGettersAndSetters() {
        Token token = new Token.Doctype();
        
        token.type = Token.TokenType.StartTag;
        Token.TokenType type = token.type;
        assertEquals(Token.TokenType.StartTag, type);
    }
}