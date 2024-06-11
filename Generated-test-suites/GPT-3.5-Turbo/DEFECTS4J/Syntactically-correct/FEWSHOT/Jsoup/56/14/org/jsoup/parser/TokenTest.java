package org.jsoup.parser;

import org.jsoup.nodes.Attributes;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {
    private Token.Doctype doctype;
    private Token.StartTag startTag;
    private Token.EndTag endTag;
    private Token.Comment comment;
    private Token.Character character;
    private Token.EOF eof;

    @Before
    public void setUp() {
        doctype = new Token.Doctype();
        startTag = new Token.StartTag();
        endTag = new Token.EndTag();
        comment = new Token.Comment();
        character = new Token.Character();
        eof = new Token.EOF();
    }

    @Test
    public void testResetDoctype() {
        doctype.name.append("html");
        doctype.publicIdentifier.append("public id");
        doctype.systemIdentifier.append("system id");
        doctype.forceQuirks = true;

        doctype.reset();

        assertEquals("", doctype.name.toString());
        assertEquals("", doctype.publicIdentifier.toString());
        assertEquals("", doctype.systemIdentifier.toString());
        assertFalse(doctype.forceQuirks);
    }

    @Test
    public void testGetNameDoctype() {
        doctype.name.append("html");

        String name = doctype.getName();

        assertEquals("html", name);
    }

    @Test
    public void testGetPublicIdentifier() {
        doctype.publicIdentifier.append("public id");

        String publicIdentifier = doctype.getPublicIdentifier();

        assertEquals("public id", publicIdentifier);
    }

    @Test
    public void testGetSystemIdentifier() {
        doctype.systemIdentifier.append("system id");

        String systemIdentifier = doctype.getSystemIdentifier();

        assertEquals("system id", systemIdentifier);
    }

    @Test
    public void testIsForceQuirks() {
        doctype.forceQuirks = true;

        boolean forceQuirks = doctype.isForceQuirks();

        assertTrue(forceQuirks);
    }

    @Test
    public void testResetStartTag() {
        startTag.tagName = "p";
        startTag.normalName = "p";
        startTag.pendingAttributeName = "class";
        startTag.pendingAttributeValue.append("paragraph");
        startTag.hasPendingAttributeValue = true;
        startTag.selfClosing = true;
        startTag.attributes = new Attributes();

        startTag.reset();

        assertNull(startTag.tagName);
        assertNull(startTag.normalName);
        assertNull(startTag.pendingAttributeName);
        assertEquals("", startTag.pendingAttributeValue.toString());
        assertFalse(startTag.hasPendingAttributeValue);
        assertFalse(startTag.selfClosing);
        assertNull(startTag.attributes);
    }

    @Test
    public void testNewAttribute() {
        startTag.attributes = new Attributes();
        startTag.pendingAttributeName = "class";
        startTag.pendingAttributeValue.append("paragraph");
        startTag.hasPendingAttributeValue = true;

        startTag.newAttribute();

        assertEquals(1, startTag.attributes.size());
        assertEquals("class", startTag.attributes.get("class").getKey());
        assertEquals("paragraph", startTag.attributes.get("class").getValue());
        assertNull(startTag.pendingAttributeName);
        assertEquals("", startTag.pendingAttributeValue.toString());
        assertFalse(startTag.hasPendingAttributeValue);
    }

    @Test
    public void testFinaliseTag() {
        startTag.pendingAttributeName = "class";

        startTag.finaliseTag();

        assertEquals(1, startTag.attributes.size());
        assertEquals("class", startTag.attributes.get("class").getKey());
        assertNull(startTag.pendingAttributeName);
    }

    @Test
    public void testNameStartTag() {
        startTag.name("p");

        String name = startTag.name();

        assertEquals("p", name);
    }

    @Test
    public void testNormalName() {
        startTag.normalName = "p";

        String normalName = startTag.normalName();

        assertEquals("p", normalName);
    }

    @Test
    public void testIsSelfClosing() {
        startTag.selfClosing = true;

        boolean selfClosing = startTag.isSelfClosing();

        assertTrue(selfClosing);
    }

    @Test
    public void testGetAttributes() {
        startTag.attributes = new Attributes();

        Attributes attributes = startTag.getAttributes();

        assertNotNull(attributes);
    }

    @Test
    public void testAppendTagNameString() {
        startTag.tagName = "p";

        startTag.appendTagName("a");

        assertEquals("pa", startTag.tagName);
        assertEquals("pa", startTag.normalName);
    }

    @Test
    public void testAppendTagNameChar() {
        startTag.tagName = "p";

        startTag.appendTagName('a');

        assertEquals("pa", startTag.tagName);
        assertEquals("pa", startTag.normalName);
    }

    @Test
    public void testAppendAttributeNameString() {
        startTag.pendingAttributeName = "class";

        startTag.appendAttributeName("id");

        assertEquals("classid", startTag.pendingAttributeName);
    }

    @Test
    public void testAppendAttributeNameChar() {
        startTag.pendingAttributeName = "class";

        startTag.appendAttributeName('i');

        assertEquals("classi", startTag.pendingAttributeName);
    }

    @Test
    public void testAppendAttributeValueString() {
        startTag.ensureAttributeValue();

        startTag.appendAttributeValue("paragraph");

        assertEquals("paragraph", startTag.pendingAttributeValue.toString());
        assertNull(startTag.pendingAttributeValueS);
        assertTrue(startTag.hasPendingAttributeValue);
    }

    @Test
    public void testAppendAttributeValueChar() {
        startTag.ensureAttributeValue();

        startTag.appendAttributeValue('p');

        assertEquals("p", startTag.pendingAttributeValue.toString());
        assertNull(startTag.pendingAttributeValueS);
        assertTrue(startTag.hasPendingAttributeValue);
    }

    @Test
    public void testAppendAttributeValueCharArray() {
        startTag.ensureAttributeValue();

        startTag.appendAttributeValue(new char[] {'p', 'a', 'r', 'a', 'g', 'r', 'a', 'p', 'h'});

        assertEquals("paragraph", startTag.pendingAttributeValue.toString());
        assertNull(startTag.pendingAttributeValueS);
        assertTrue(startTag.hasPendingAttributeValue);
    }

    @Test
    public void testAppendAttributeValueIntArray() {
        startTag.ensureAttributeValue();

        startTag.appendAttributeValue(new int[] {112, 97, 114, 97, 103, 114, 97, 112, 104});

        assertEquals("paragraph", startTag.pendingAttributeValue.toString());
        assertNull(startTag.pendingAttributeValueS);
        assertTrue(startTag.hasPendingAttributeValue);
    }

    @Test
    public void testSetEmptyAttributeValue() {
        startTag.setEmptyAttributeValue();

        assertTrue(startTag.hasEmptyAttributeValue);
    }

    @Test
    public void testResetEndTag() {
        endTag.tagName = "p";
        endTag.normalName = "p";

        endTag.reset();

        assertNull(endTag.tagName);
        assertNull(endTag.normalName);
    }

    @Test
    public void testResetComment() {
        comment.data.append("comment");
        comment.bogus = true;

        comment.reset();

        assertEquals("", comment.data.toString());
        assertFalse(comment.bogus);
    }

    @Test
    public void testGetDataComment() {
        comment.data.append("comment");

        String data = comment.getData();

        assertEquals("comment", data);
    }

    @Test
    public void testResetCharacter() {
        character.data = "data";

        character.reset();

        assertNull(character.data);
    }

    @Test
    public void testDataCharacter() {
        character.data("data");

        String data = character.getData();

        assertEquals("data", data);
    }

    @Test
    public void testResetEof() {
        eof.reset();

        assertNotNull(eof);
    }
}