package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.BooleanAttribute;
import org.jsoup.nodes.Attribute;
import org.junit.Test;

public class TokenTest {
    @Test
    public void testDoctypeReset() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("test");
        doctype.pubSysKey = "key";
        doctype.publicIdentifier.append("public");
        doctype.systemIdentifier.append("system");
        doctype.forceQuirks = true;

        doctype.reset();

        assertEquals("", doctype.name.toString());
        assertNull(doctype.pubSysKey);
        assertEquals("", doctype.publicIdentifier.toString());
        assertEquals("", doctype.systemIdentifier.toString());
        assertFalse(doctype.forceQuirks);
    }

    @Test
    public void testDoctypeGetters() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("test");
        doctype.pubSysKey = "key";
        doctype.publicIdentifier.append("public");
        doctype.systemIdentifier.append("system");
        doctype.forceQuirks = true;

        assertEquals("test", doctype.getName());
        assertEquals("key", doctype.getPubSysKey());
        assertEquals("public", doctype.getPublicIdentifier());
        assertEquals("system", doctype.getSystemIdentifier());
        assertTrue(doctype.isForceQuirks());
    }

    @Test
    public void testTagReset() {
        Token.Tag tag = new Token.StartTag();
        tag.tagName = "test";
        tag.normalName = "test";
        tag.pendingAttributeName = "attr";
        tag.pendingAttributeValue.append("value");
        tag.pendingAttributeValueS = "valueS";
        tag.hasEmptyAttributeValue = true;
        tag.hasPendingAttributeValue = true;
        tag.selfClosing = true;
        tag.attributes = new Attributes();

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
    public void testTagNewAttribute() {
        Token.Tag tag = new Token.StartTag();
        tag.newAttribute();

        assertNull(tag.attributes);

        tag.pendingAttributeName = "attr";
        tag.newAttribute();

        assertTrue(tag.attributes != null);
        assertEquals(1, tag.attributes.size());

        tag.pendingAttributeName = null;
        tag.newAttribute();

        assertEquals(1, tag.attributes.size());
    }

    @Test
    public void testTagFinaliseTag() {
        Token.Tag tag = new Token.StartTag();
        tag.tagName = "test";
        tag.attributes = new Attributes();
        tag.pendingAttributeName = "attr";
        tag.finaliseTag();

        assertTrue(tag.attributes.size() > 0);
        assertNull(tag.pendingAttributeName);
    }

    @Test
    public void testTagName() {
        Token.StartTag tag = new Token.StartTag();
        tag.tagName = "Test";

        assertEquals("Test", tag.name());
    }

    @Test
    public void testTagNormalName() {
        Token.StartTag tag = new Token.StartTag();
        tag.normalName = "test";

        assertEquals("test", tag.normalName());
    }

    @Test
    public void testTagAttributeNameAndValueAppenders() {
        Token.Tag tag = new Token.StartTag();
        tag.appendTagName("test");
        tag.appendAttributeName("attr");
        tag.appendAttributeValue("value");

        assertEquals("test", tag.tagName);
        assertEquals("attr", tag.pendingAttributeName);
        assertEquals("value", tag.pendingAttributeValue.toString());
    }

    @Test
    public void testTagAttributeValueAppender() {
        Token.Tag tag = new Token.StartTag();
        tag.appendAttributeValue("value");

        assertEquals("value", tag.pendingAttributeValue.toString());
    }

    @Test
    public void testTagAttributeValueAppenderWithArray() {
        Token.Tag tag = new Token.StartTag();
        char[] append = {'v', 'a', 'l', 'u', 'e'};
        tag.appendAttributeValue(append);

        assertEquals("value", tag.pendingAttributeValue.toString());
    }

    @Test
    public void testTagAttributeValueAppenderWithCodepoints() {
        Token.Tag tag = new Token.StartTag();
        int[] append = {'v', 'a', 'l', 'u', 'e'};
        tag.appendAttributeValue(append);

        assertEquals("value", tag.pendingAttributeValue.toString());
    }

    @Test
    public void testTagEmptyAttributeValue() {
        Token.Tag tag = new Token.StartTag();
        tag.setEmptyAttributeValue();

        assertTrue(tag.hasEmptyAttributeValue);
    }

    @Test
    public void testTagEnsureAttributeValue() {
        Token.Tag tag = new Token.StartTag();
        tag.appendAttributeValue("value");
        tag.ensureAttributeValue();

        assertEquals("value", tag.pendingAttributeValue.toString());
    }

    @Test
    public void testTagAppendTagNameWithChar() {
        Token.Tag tag = new Token.StartTag();
        tag.appendTagName('t');

        assertEquals("t", tag.tagName);
    }

    @Test
    public void testTagAppendAttributeNameWithChar() {
        Token.Tag tag = new Token.StartTag();
        tag.appendAttributeName('a');

        assertEquals("a", tag.pendingAttributeName);
    }

    @Test
    public void testTagAppendAttributeValueWithChar() {
        Token.Tag tag = new Token.StartTag();
        tag.appendAttributeValue('v');

        assertEquals("v", tag.pendingAttributeValue.toString());
    }

    @Test
    public void testCommentReset() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("test");
        comment.bogus = true;

        comment.reset();

        assertEquals("", comment.data.toString());
        assertFalse(comment.bogus);
    }

    @Test
    public void testCommentGetData() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("test");

        assertEquals("test", comment.getData());
    }

    @Test
    public void testCharacterReset() {
        Token.Character character = new Token.Character();
        character.data = "test";

        character.reset();

        assertNull(character.data);
    }

    @Test
    public void testCharacterGetData() {
        Token.Character character = new Token.Character();
        character.data = "test";

        assertEquals("test", character.getData());
    }
}