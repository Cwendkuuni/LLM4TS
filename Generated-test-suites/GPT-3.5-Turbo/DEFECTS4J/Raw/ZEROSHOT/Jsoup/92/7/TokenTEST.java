package org.jsoup.parser;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class TokenTest {
    
    @Test
    public void testTokenType() {
        Token token = new Token.TokenType();
        assertEquals("TokenType", token.tokenType());
    }
    
    @Test
    public void testDoctypeReset() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.reset();
        assertEquals("", doctype.getName());
        assertEquals(null, doctype.getPubSysKey());
        assertEquals("", doctype.getPublicIdentifier());
        assertEquals("", doctype.getSystemIdentifier());
        assertEquals(false, doctype.isForceQuirks());
    }
    
    @Test
    public void testDoctypeGetName() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("doctype");
        assertEquals("doctype", doctype.getName());
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
        doctype.publicIdentifier.append("publicIdentifier");
        assertEquals("publicIdentifier", doctype.getPublicIdentifier());
    }
    
    @Test
    public void testDoctypeGetSystemIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.systemIdentifier.append("systemIdentifier");
        assertEquals("systemIdentifier", doctype.getSystemIdentifier());
    }
    
    @Test
    public void testDoctypeIsForceQuirks() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.forceQuirks = true;
        assertEquals(true, doctype.isForceQuirks());
    }
    
    @Test
    public void testTagReset() {
        Token.Tag tag = new Token.Tag();
        tag.reset();
        assertEquals(null, tag.tagName);
        assertEquals(null, tag.normalName);
        assertEquals(null, tag.pendingAttributeName);
        assertEquals("", tag.pendingAttributeValue.toString());
        assertEquals(null, tag.pendingAttributeValueS);
        assertEquals(false, tag.hasEmptyAttributeValue);
        assertEquals(false, tag.hasPendingAttributeValue);
        assertEquals(false, tag.selfClosing);
        assertEquals(null, tag.attributes);
    }
    
    @Test
    public void testTagName() {
        Token.Tag tag = new Token.Tag();
        tag.tagName = "tag";
        tag.normalName = "tag";
        assertEquals("tag", tag.name());
        assertEquals("tag", tag.normalName());
    }
    
    @Test
    public void testTagIsSelfClosing() {
        Token.Tag tag = new Token.Tag();
        tag.selfClosing = true;
        assertEquals(true, tag.isSelfClosing());
    }
    
    @Test
    public void testTagGetName() {
        Token.Tag tag = new Token.Tag();
        tag.tagName = "tag";
        assertEquals("tag", tag.name());
    }
    
    @Test
    public void testTagNormalName() {
        Token.Tag tag = new Token.Tag();
        tag.tagName = "tag";
        tag.normalName = "tag";
        assertEquals("tag", tag.normalName());
    }
    
    @Test
    public void testTagSetEmptyAttributeValue() {
        Token.Tag tag = new Token.Tag();
        tag.setEmptyAttributeValue();
        assertEquals(true, tag.hasEmptyAttributeValue);
    }
    
    @Test
    public void testTagAppendTagName() {
        Token.Tag tag = new Token.Tag();
        tag.appendTagName("append");
        assertEquals("append", tag.tagName);
        assertEquals("append", tag.normalName);
    }
    
    @Test
    public void testTagAppendTagNameChar() {
        Token.Tag tag = new Token.Tag();
        tag.appendTagName('a');
        assertEquals("a", tag.tagName);
        assertEquals("a", tag.normalName);
    }
    
    @Test
    public void testTagAppendAttributeName() {
        Token.Tag tag = new Token.Tag();
        tag.appendAttributeName("attribute");
        assertEquals("attribute", tag.pendingAttributeName);
    }
    
    @Test
    public void testTagAppendAttributeNameChar() {
        Token.Tag tag = new Token.Tag();
        tag.appendAttributeName('a');
        assertEquals("a", tag.pendingAttributeName);
    }
    
    @Test
    public void testTagAppendAttributeValue() {
        Token.Tag tag = new Token.Tag();
        tag.appendAttributeValue("value");
        assertEquals("value", tag.pendingAttributeValue.toString());
    }
    
    @Test
    public void testTagAppendAttributeValueChar() {
        Token.Tag tag = new Token.Tag();
        tag.appendAttributeValue('a');
        assertEquals("a", tag.pendingAttributeValue.toString());
    }
    
    @Test
    public void testTagAppendAttributeValueCharArray() {
        Token.Tag tag = new Token.Tag();
        tag.appendAttributeValue(new char[]{'a', 'b', 'c'});
        assertEquals("abc", tag.pendingAttributeValue.toString());
    }
    
    @Test
    public void testTagAppendAttributeValueIntArray() {
        Token.Tag tag = new Token.Tag();
        tag.appendAttributeValue(new int[]{97, 98, 99});
        assertEquals("abc", tag.pendingAttributeValue.toString());
    }
    
    @Test
    public void testTagNewAttribute() {
        Token.Tag tag = new Token.Tag();
        tag.newAttribute();
        assertEquals(null, tag.attributes);
        assertEquals(null, tag.pendingAttributeName);
        assertEquals("", tag.pendingAttributeValue.toString());
        assertEquals(null, tag.pendingAttributeValueS);
        assertEquals(false, tag.hasEmptyAttributeValue);
        assertEquals(false, tag.hasPendingAttributeValue);
    }
    
    @Test
    public void testTagFinaliseTag() {
        Token.Tag tag = new Token.Tag();
        tag.finaliseTag();
        assertEquals(null, tag.pendingAttributeName);
        assertEquals("", tag.pendingAttributeValue.toString());
        assertEquals(null, tag.pendingAttributeValueS);
        assertEquals(false, tag.hasEmptyAttributeValue);
        assertEquals(false, tag.hasPendingAttributeValue);
    }
    
    @Test
    public void testStartTagReset() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.reset();
        assertEquals(null, startTag.tagName);
        assertEquals(null, startTag.normalName);
        assertEquals(null, startTag.pendingAttributeName);
        assertEquals("", startTag.pendingAttributeValue.toString());
        assertEquals(null, startTag.pendingAttributeValueS);
        assertEquals(false, startTag.hasEmptyAttributeValue);
        assertEquals(false, startTag.hasPendingAttributeValue);
        assertEquals(false, startTag.selfClosing);
        assertEquals(null, startTag.attributes);
    }
    
    @Test
    public void testStartTagToStringWithAttributes() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "tag";
        startTag.attributes = new Attributes();
        startTag.attributes.put("attr1", "value1");
        startTag.attributes.put("attr2", "value2");
        assertEquals("<tag attr1=\"value1\" attr2=\"value2\">", startTag.toString());
    }
    
    @Test
    public void testStartTagToStringWithoutAttributes() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "tag";
        assertEquals("<tag>", startTag.toString());
    }
    
    @Test
    public void testEndTagToString() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "tag";
        assertEquals("</tag>", endTag.toString());
    }
    
    @Test
    public void testCommentReset() {
        Token.Comment comment = new Token.Comment();
        comment.reset();
        assertEquals("", comment.data.toString());
        assertEquals(false, comment.bogus);
    }
    
    @Test
    public void testCommentGetData() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("comment");
        assertEquals("comment", comment.getData());
    }
    
    @Test
    public void testCommentToString() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("comment");
        assertEquals("<!--comment-->", comment.toString());
    }
    
    @Test
    public void testCharacterReset() {
        Token.Character character = new Token.Character();
        character.reset();
        assertEquals(null, character.data);
    }
    
    @Test
    public void testCharacterGetData() {
        Token.Character character = new Token.Character();
        character.data = "data";
        assertEquals("data", character.getData());
    }
    
    @Test
    public void testCharacterToString() {
        Token.Character character = new Token.Character();
        character.data = "data";
        assertEquals("data", character.toString());
    }
    
    @Test
    public void testCDataToString() {
        Token.CData cdata = new Token.CData("data");
        assertEquals("<![CDATA[data]]>", cdata.toString());
    }
    
    @Test
    public void testEOFReset() {
        Token.EOF eof = new Token.EOF();
        eof.reset();
        assertEquals(eof, eof.reset());
    }
    
}