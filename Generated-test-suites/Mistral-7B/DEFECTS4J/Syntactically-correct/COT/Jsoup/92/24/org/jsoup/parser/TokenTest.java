package org.jsoup.parser; // adjust the package if needed

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

/**
 * Test class for Token abstract class.
 */
public class TokenTest {

    /**
     * Test method for {@link Token#reset()}
     */
    @Test
    public void testReset() {
        Token token = new Token.Doctype();
        token.reset();
        assertNull(token.type);
        assertNull(token.getName());
        assertNull(token.getPubSysKey());
        assertNull(token.getPublicIdentifier());
        assertNull(token.getSystemIdentifier());
        assertNull(token.isForceQuirks());
    }

    /**
     * Test method for {@link Token.Doctype#getName()}
     */
    @Test
    public void testDoctype_getName() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.getName("html");
        assertEquals("Doctype", tokenType(doctype));
        assertEquals("html", doctype.getName());
    }

    /**
     * Test method for {@link Token.Doctype#getPubSysKey()}
     */
    @Test
    public void testDoctype_getPubSysKey() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.getPubSysKey("pubSysKey");
        assertEquals("Doctype", tokenType(doctype));
        assertEquals("pubSysKey", doctype.getPubSysKey());
    }

    /**
     * Test method for {@link Token.Doctype#getPublicIdentifier()}
     */
    @Test
    public void testDoctype_getPublicIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.getPublicIdentifier("publicIdentifier");
        assertEquals("Doctype", tokenType(doctype));
        assertEquals("publicIdentifier", doctype.getPublicIdentifier());
    }

    /**
     * Test method for {@link Token.Doctype#getSystemIdentifier()}
     */
    @Test
    public void testDoctype_getSystemIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.getSystemIdentifier("systemIdentifier");
        assertEquals("Doctype", tokenType(doctype));
        assertEquals("systemIdentifier", doctype.getSystemIdentifier());
    }

    /**
     * Test method for {@link Token.Doctype#isForceQuirks()}
     */
    @Test
    public void testDoctype_isForceQuirks() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.isForceQuirks(true);
        assertEquals("Doctype", tokenType(doctype));
        assertTrue(doctype.isForceQuirks());
    }

    /**
     * Test method for {@link Token#tokenType()}
     */
    private String tokenType(Token token) {
        return token.tokenType();
    }

    /**
     * Test method for {@link Token.Tag#reset()}
     */
    @Test
    public void testTag_reset() {
        Token.Tag tag = new Token.StartTag();
        tag.reset();
        assertNull(tag.tagName);
        assertNull(tag.normalName);
        assertNull(tag.pendingAttributeName);
        assertNull(tag.attributes);
        assertNull(tag.hasEmptyAttributeValue);
        assertNull(tag.hasPendingAttributeValue);
        assertNull(tag.pendingAttributeValue);
        assertNull(tag.pendingAttributeValueS);
        assertFalse(tag.selfClosing);
    }

    /**
     * Test method for {@link Token.Tag#name()}
     */
    @Test
    public void testTag_name() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.name("div");
        assertEquals("StartTag", tokenType(startTag));
        assertEquals("div", startTag.name());
    }

    /**
     * Test method for {@link Token.Tag#normalName()}
     */
    @Test
    public void testTag_normalName() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.name("DIV");
        assertEquals("StartTag", tokenType(startTag));
        assertEquals("div", startTag.normalName());
    }

    /**
     * Test method for {@link Token.Tag#finaliseTag()}
     */
    @Test
    public void testTag_finaliseTag() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.name("div");
        startTag.newAttribute();
        startTag.newAttribute();
        startTag.finaliseTag();
        assertNotNull(startTag.attributes);
        assertEquals(2, startTag.attributes.size());
    }

    /**
     * Test method for {@link Token.Tag#toString()}
     */
    @Test
    public void testTag_toString() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.name("div");
        startTag.attributes = new Attributes();
        startTag.attributes.put("id", "test");
        startTag.attributes.put("class", "test");
        assertEquals("<div id=\"test\" class=\"test\">", startTag.toString());
    }

    /**
     * Test method for {@link Token.Tag#appendTagName(String)}
     */
    @Test
    public void testTag_appendTagName() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.appendTagName("div");
        assertEquals("div", startTag.tagName);
        startTag.appendTagName("class");
        assertEquals("divclass", startTag.tagName);
    }

    /**
     * Test method for {@link Token.Tag#appendTagName(char)}
     */
    @Test
    public void testTag_appendTagName_char() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.appendTagName('d');
        startTag.appendTagName('i');
        startTag.appendTagName('v');
        assertEquals("div", startTag.tagName);
    }

    /**
     * Test method for {@link Token.Tag#appendAttributeName(String)}
     */
    @Test
    public void testTag_appendAttributeName() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.appendAttributeName("id");
        assertEquals("id", startTag.pendingAttributeName);
    }

    /**
     * Test method for {@link Token.Tag#appendAttributeName(char)}
     */
    @Test
    public void testTag_appendAttributeName_char() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.appendAttributeName('i');
        startTag.appendAttributeName('d');
        assertEquals("id", startTag.pendingAttributeName);
    }

    /**
     * Test method for {@link Token.Tag#appendAttributeValue(String)}
     */
    @Test
    public void testTag_appendAttributeValue() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.appendAttributeName("id");
        startTag.appendAttributeValue("test");
        assertEquals("test", startTag.attributes.get("id"));
    }

    /**
     * Test method for {@link Token.Tag#appendAttributeValue(char)}
     */
    @Test
    public void testTag_appendAttributeValue_char() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.appendAttributeName("id");
        startTag.appendAttributeValue('t');
        startTag.appendAttributeValue('e');
        startTag.appendAttributeValue('s');
        assertEquals("test", startTag.attributes.get("id"));
    }

    /**
     * Test method for {@link Token.Tag#appendAttributeValue(char[])}
     */
    @Test
    public void testTag_appendAttributeValue_charArray() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.appendAttributeName("id");
        char[] attributeValue = {'t', 'e', 's', 't'};
        startTag.appendAttributeValue(attributeValue);
        assertEquals("test", startTag.attributes.get("id"));
    }

    /**
     * Test method for {@link Token.Tag#appendAttributeValue(int[])}
     */
    @Test
    public void testTag_appendAttributeValue_intArray() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.appendAttributeName("id");
        int[] attributeValue = {68, 101, 115, 116};
        startTag.appendAttributeValue(attributeValue);
        assertEquals("test", startTag.attributes.get("id"));
    }

    /**
     * Test method for {@link Token.Tag#setEmptyAttributeValue()}
     */
    @Test
    public void testTag_setEmptyAttributeValue() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.appendAttributeName("id");
        startTag.setEmptyAttributeValue();
        assertNull(startTag.attributes.get("id"));
    }
}