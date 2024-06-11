package org.jsoup.parser;

import org.junit.Test;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

public class TokenTest {

    @Test
    public void testTokenType() {
        Token.Doctype doctype = new Token.Doctype();
        assertEquals("Doctype", doctype.tokenType());

        Token.StartTag startTag = new Token.StartTag();
        assertEquals("StartTag", startTag.tokenType());

        Token.EndTag endTag = new Token.EndTag();
        assertEquals("EndTag", endTag.tokenType());

        Token.Comment comment = new Token.Comment();
        assertEquals("Comment", comment.tokenType());

        Token.Character character = new Token.Character();
        assertEquals("Character", character.tokenType());

        Token.CData cdata = new Token.CData("");
        assertEquals("CData", cdata.tokenType());

        Token.EOF eof = new Token.EOF();
        assertEquals("EOF", eof.tokenType());
    }

    @Test
    public void testDoctypeReset() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("name");
        doctype.pubSysKey = "pubSysKey";
        doctype.publicIdentifier.append("publicIdentifier");
        doctype.systemIdentifier.append("systemIdentifier");
        doctype.forceQuirks = true;

        Token resetToken = doctype.reset();

        assertEquals("", doctype.name.toString());
        assertNull(doctype.pubSysKey);
        assertEquals("", doctype.publicIdentifier.toString());
        assertEquals("", doctype.systemIdentifier.toString());
        assertFalse(doctype.forceQuirks);
        assertEquals(doctype, resetToken);
    }

    @Test
    public void testDoctypeGetterAndSetter() {
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
    public void testStartTagReset() {
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

        Token resetToken = startTag.reset();

        assertNull(startTag.tagName);
        assertNull(startTag.normalName);
        assertNull(startTag.pendingAttributeName);
        assertEquals("", startTag.pendingAttributeValue.toString());
        assertNull(startTag.pendingAttributeValueS);
        assertFalse(startTag.hasEmptyAttributeValue);
        assertFalse(startTag.hasPendingAttributeValue);
        assertFalse(startTag.selfClosing);
        assertNull(startTag.attributes);
        assertEquals(startTag, resetToken);
    }

    @Test
    public void testStartTagToString() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "tagName";
        startTag.attributes = new Attributes();
        assertEquals("<tagName>", startTag.toString());

        startTag.attributes.put("key", "value");
        assertEquals("<tagName key=\"value\">", startTag.toString());
    }

    @Test
    public void testEndTagReset() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "tagName";
        endTag.normalName = "normalName";

        Token resetToken = endTag.reset();

        assertNull(endTag.tagName);
        assertNull(endTag.normalName);
        assertEquals(endTag, resetToken);
    }

    @Test
    public void testEndTagToString() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "tagName";
        assertEquals("</tagName>", endTag.toString());
    }

    @Test
    public void testCommentReset() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("data");
        comment.bogus = true;

        Token resetToken = comment.reset();

        assertEquals("", comment.data.toString());
        assertFalse(comment.bogus);
        assertEquals(comment, resetToken);
    }

    @Test
    public void testCharacterReset() {
        Token.Character character = new Token.Character();
        character.data = "data";

        Token resetToken = character.reset();

        assertNull(character.data);
        assertEquals(character, resetToken);
    }

    @Test
    public void testCharacterGetterAndSetter() {
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
}