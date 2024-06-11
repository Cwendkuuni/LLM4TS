
package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert;

public class TokenTest {

    @Test
    public void testTokenType() {
        Token.Doctype doctype = new Token.Doctype();
        Assert.assertEquals(Token.TokenType.Doctype, doctype.type);

        Token.StartTag startTag = new Token.StartTag();
        Assert.assertEquals(Token.TokenType.StartTag, startTag.type);

        Token.EndTag endTag = new Token.EndTag();
        Assert.assertEquals(Token.TokenType.EndTag, endTag.type);

        Token.Comment comment = new Token.Comment();
        Assert.assertEquals(Token.TokenType.Comment, comment.type);

        Token.Character character = new Token.Character();
        Assert.assertEquals(Token.TokenType.Character, character.type);

        Token.EOF eof = new Token.EOF();
        Assert.assertEquals(Token.TokenType.EOF, eof.type);
    }

    @Test
    public void testDoctypeReset() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("html");
        doctype.pubSysKey = "pub";
        doctype.publicIdentifier.append("pubid");
        doctype.systemIdentifier.append("sysid");
        doctype.forceQuirks = true;
        
        doctype.reset();
        
        Assert.assertEquals("", doctype.name.toString());
        Assert.assertNull(doctype.pubSysKey);
        Assert.assertEquals("", doctype.publicIdentifier.toString());
        Assert.assertEquals("", doctype.systemIdentifier.toString());
        Assert.assertFalse(doctype.forceQuirks);
    }

    @Test
    public void testDoctypeGetterMethods() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("html");
        doctype.pubSysKey = "pub";
        doctype.publicIdentifier.append("pubid");
        doctype.systemIdentifier.append("sysid");
        doctype.forceQuirks = true;
        
        Assert.assertEquals("html", doctype.getName());
        Assert.assertEquals("pub", doctype.getPubSysKey());
        Assert.assertEquals("pubid", doctype.getPublicIdentifier());
        Assert.assertEquals("sysid", doctype.getSystemIdentifier());
        Assert.assertTrue(doctype.isForceQuirks());
    }

    @Test
    public void testStartTagReset() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "tag";
        startTag.normalName = "TAG";
        startTag.pendingAttributeName = "attr";
        startTag.pendingAttributeValue.append("value");
        startTag.pendingAttributeValueS = "value";
        startTag.hasEmptyAttributeValue = true;
        startTag.hasPendingAttributeValue = true;
        startTag.attributes = new Attributes();
        startTag.selfClosing = true;
        
        startTag.reset();
        
        Assert.assertNull(startTag.tagName);
        Assert.assertNull(startTag.normalName);
        Assert.assertNull(startTag.pendingAttributeName);
        Assert.assertEquals("", startTag.pendingAttributeValue.toString());
        Assert.assertNull(startTag.pendingAttributeValueS);
        Assert.assertFalse(startTag.hasEmptyAttributeValue);
        Assert.assertFalse(startTag.hasPendingAttributeValue);
        Assert.assertNull(startTag.attributes);
        Assert.assertFalse(startTag.selfClosing);
    }

    @Test
    public void testStartTagNameAttr() {
        Token.StartTag startTag = new Token.StartTag();
        Attributes attributes = new Attributes();
        attributes.put("attr", "value");
        
        startTag.nameAttr("tag", attributes);
        
        Assert.assertEquals("tag", startTag.tagName);
        Assert.assertEquals("tag", startTag.normalName);
        Assert.assertEquals("attr", startTag.pendingAttributeName);
        Assert.assertEquals("value", startTag.pendingAttributeValue.toString());
        Assert.assertNull(startTag.pendingAttributeValueS);
        Assert.assertFalse(startTag.hasEmptyAttributeValue);
        Assert.assertTrue(startTag.hasPendingAttributeValue);
        Assert.assertEquals(attributes, startTag.attributes);
    }

    @Test
    public void testEndTagReset() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "tag";
        endTag.normalName = "TAG";
        
        endTag.reset();
        
        Assert.assertNull(endTag.tagName);
        Assert.assertNull(endTag.normalName);
    }

    @Test
    public void testCommentReset() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("data");
        comment.bogus = true;
        
        comment.reset();
        
        Assert.assertEquals("", comment.data.toString());
        Assert.assertFalse(comment.bogus);
    }

    @Test
    public void testCharacterReset() {
        Token.Character character = new Token.Character();
        character.data = "data";
        
        character.reset();
        
        Assert.assertNull(character.data);
    }

    @Test
    public void testCData() {
        Token.CData cdata = new Token.CData("data");
        Assert.assertTrue(cdata.isCData());
        Assert.assertEquals("data", cdata.getData());
        Assert.assertEquals("<![CDATA[data]]>", cdata.toString());
    }

    @Test
    public void testEOFReset() {
        Token.EOF eof = new Token.EOF();
        eof.reset();
        Assert.assertTrue(eof.isEOF());
    }

    @Test
    public void testClassMethods() {
        Token.Doctype doctype = new Token.Doctype();
        Assert.assertTrue(doctype.isDoctype());
        Assert.assertTrue(doctype.asDoctype() instanceof Token.Doctype);
        
        Token.StartTag startTag = new Token.StartTag();
        Assert.assertTrue(startTag.isStartTag());
        Assert.assertTrue(startTag.asStartTag() instanceof Token.StartTag);
        
        Token.EndTag endTag = new Token.EndTag();
        Assert.assertTrue(endTag.isEndTag());
        Assert.assertTrue(endTag.asEndTag() instanceof Token.EndTag);
        
        Token.Comment comment = new Token.Comment();
        Assert.assertTrue(comment.isComment());
        Assert.assertTrue(comment.asComment() instanceof Token.Comment);
        
        Token.Character character = new Token.Character();
        Assert.assertTrue(character.isCharacter());
        Assert.assertTrue(character.asCharacter() instanceof Token.Character);
        
        Token.EOF eof = new Token.EOF();
        Assert.assertTrue(eof.isEOF());
    }
}
