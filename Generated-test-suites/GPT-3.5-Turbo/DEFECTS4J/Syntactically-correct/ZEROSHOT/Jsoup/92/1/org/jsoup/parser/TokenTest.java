package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testTokenTypeMethod() {
        Token token = new Token() {
            @Override
            Token reset() {
                return null;
            }
        };
        
        assertEquals("Token", token.tokenType());
    }

    @Test
    public void testDoctypeTokenResetMethod() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("name");
        doctype.pubSysKey = "pubSysKey";
        doctype.publicIdentifier.append("publicIdentifier");
        doctype.systemIdentifier.append("systemIdentifier");
        doctype.forceQuirks = true;
        
        Token.Doctype resetDoctype = doctype.reset();
        
        assertEquals("", resetDoctype.name.toString());
        assertNull(resetDoctype.pubSysKey);
        assertEquals("", resetDoctype.publicIdentifier.toString());
        assertEquals("", resetDoctype.systemIdentifier.toString());
        assertFalse(resetDoctype.forceQuirks);
    }

    @Test
    public void testDoctypeTokenGetters() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("name");
        doctype.pubSysKey = "pubSysKey";
        doctype.publicIdentifier.append("publicIdentifier");
        doctype.systemIdentifier.append("systemIdentifier");
        doctype.forceQuirks = true;
        
        assertEquals("name", doctype.getName());
        assertEquals("pubSysKey", doctype.getPubSysKey());
        assertEquals("publicIdentifier", doctype.getPublicIdentifier());
        assertEquals("systemIdentifier", doctype.getSystemIdentifier());
        assertTrue(doctype.isForceQuirks());
    }

    @Test
    public void testStartTagTokenResetMethod() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "tagName";
        startTag.normalName = "normalName";
        startTag.pendingAttributeName = "pendingAttributeName";
        startTag.pendingAttributeValue.append("pendingAttributeValue");
        startTag.pendingAttributeValueS = "pendingAttributeValueS";
        startTag.hasEmptyAttributeValue = true;
        startTag.hasPendingAttributeValue = true;
        startTag.selfClosing = true;
        startTag.attributes = new Attributes();
        
        Token.StartTag resetStartTag = startTag.reset();
        
        assertNull(resetStartTag.tagName);
        assertNull(resetStartTag.normalName);
        assertNull(resetStartTag.pendingAttributeName);
        assertEquals("", resetStartTag.pendingAttributeValue.toString());
        assertNull(resetStartTag.pendingAttributeValueS);
        assertFalse(resetStartTag.hasEmptyAttributeValue);
        assertFalse(resetStartTag.hasPendingAttributeValue);
        assertFalse(resetStartTag.selfClosing);
        assertNull(resetStartTag.attributes);
    }

    @Test
    public void testStartTagTokenNewAttributeMethod() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.newAttribute();
        
        assertNotNull(startTag.attributes);
        assertNull(startTag.pendingAttributeName);
        assertEquals("", startTag.pendingAttributeValue.toString());
        assertFalse(startTag.hasEmptyAttributeValue);
        assertFalse(startTag.hasPendingAttributeValue);
    }

    @Test
    public void testEndTagTokenResetMethod() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "tagName";
        endTag.normalName = "normalName";
        endTag.pendingAttributeName = "pendingAttributeName";
        endTag.pendingAttributeValue.append("pendingAttributeValue");
        endTag.pendingAttributeValueS = "pendingAttributeValueS";
        endTag.hasEmptyAttributeValue = true;
        endTag.hasPendingAttributeValue = true;
        endTag.selfClosing = true;
        endTag.attributes = new Attributes();
        
        Token.EndTag resetEndTag = endTag.reset();
        
        assertNull(resetEndTag.tagName);
        assertNull(resetEndTag.normalName);
        assertNull(resetEndTag.pendingAttributeName);
        assertEquals("", resetEndTag.pendingAttributeValue.toString());
        assertNull(resetEndTag.pendingAttributeValueS);
        assertFalse(resetEndTag.hasEmptyAttributeValue);
        assertFalse(resetEndTag.hasPendingAttributeValue);
        assertFalse(resetEndTag.selfClosing);
        assertNull(resetEndTag.attributes);
    }

    @Test
    public void testCommentTokenResetMethod() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("data");
        comment.bogus = true;
        
        Token.Comment resetComment = comment.reset();
        
        assertEquals("", resetComment.data.toString());
        assertFalse(resetComment.bogus);
    }

    @Test
    public void testCharacterTokenResetMethod() {
        Token.Character character = new Token.Character();
        character.data = "data";
        
        Token.Character resetCharacter = character.reset();
        
        assertNull(resetCharacter.data);
    }

    @Test
    public void testCDataTokenToStringMethod() {
        Token.CData cdata = new Token.CData("cdata");
        
        assertEquals("<![CDATA[cdata]]>", cdata.toString());
    }

    @Test
    public void testEOFTokenResetMethod() {
        Token.EOF eof = new Token.EOF();
        
        Token.EOF resetEof = eof.reset();
        
        assertNotNull(resetEof);
    }

    @Test
    public void testIsDoctypeMethod() {
        Token.Doctype doctype = new Token.Doctype();
        
        assertTrue(doctype.isDoctype());
    }

    @Test
    public void testAsDoctypeMethod() {
        Token.Doctype doctype = new Token.Doctype();
        
        Token.Doctype asDoctype = doctype.asDoctype();
        
        assertNotNull(asDoctype);
    }

    @Test
    public void testIsStartTagMethod() {
        Token.StartTag startTag = new Token.StartTag();
        
        assertTrue(startTag.isStartTag());
    }

    @Test
    public void testAsStartTagMethod() {
        Token.StartTag startTag = new Token.StartTag();
        
        Token.StartTag asStartTag = startTag.asStartTag();
        
        assertNotNull(asStartTag);
    }

    @Test
    public void testIsEndTagMethod() {
        Token.EndTag endTag = new Token.EndTag();
        
        assertTrue(endTag.isEndTag());
    }

    @Test
    public void testAsEndTagMethod() {
        Token.EndTag endTag = new Token.EndTag();
        
        Token.EndTag asEndTag = endTag.asEndTag();
        
        assertNotNull(asEndTag);
    }

    @Test
    public void testIsCommentMethod() {
        Token.Comment comment = new Token.Comment();
        
        assertTrue(comment.isComment());
    }

    @Test
    public void testAsCommentMethod() {
        Token.Comment comment = new Token.Comment();
        
        Token.Comment asComment = comment.asComment();
        
        assertNotNull(asComment);
    }

    @Test
    public void testIsCharacterMethod() {
        Token.Character character = new Token.Character();
        
        assertTrue(character.isCharacter());
    }

    @Test
    public void testIsCDataMethod() {
        Token.CData cdata = new Token.CData("cdata");
        
        assertTrue(cdata.isCData());
    }

    @Test
    public void testAsCharacterMethod() {
        Token.Character character = new Token.Character();
        
        Token.Character asCharacter = character.asCharacter();
        
        assertNotNull(asCharacter);
    }

    @Test
    public void testIsEOFMethod() {
        Token.EOF eof = new Token.EOF();
        
        assertTrue(eof.isEOF());
    }
}