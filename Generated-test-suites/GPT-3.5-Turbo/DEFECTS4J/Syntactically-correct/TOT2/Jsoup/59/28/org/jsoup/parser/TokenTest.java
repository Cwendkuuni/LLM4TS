package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testTokenType() {
        Token token = new Token.Doctype();
        assertEquals("Doctype", token.tokenType());
    }

    @Test
    public void testDoctypeReset() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("Test");
        doctype.pubSysKey = "KEY";
        doctype.publicIdentifier.append("Public");
        doctype.systemIdentifier.append("System");
        doctype.forceQuirks = true;

        doctype.reset();

        assertEquals("", doctype.name.toString());
        assertNull(doctype.pubSysKey);
        assertEquals("", doctype.publicIdentifier.toString());
        assertEquals("", doctype.systemIdentifier.toString());
        assertFalse(doctype.forceQuirks);
    }

    @Test
    public void testTagReset() {
        Token.Tag tag = new Token.StartTag();
        tag.tagName = "test";
        tag.pendingAttributeName = "attr";
        tag.pendingAttributeValue.append("value");
        tag.selfClosing = true;

        tag.reset();

        assertNull(tag.tagName);
        assertNull(tag.pendingAttributeName);
        assertEquals("", tag.pendingAttributeValue.toString());
        assertFalse(tag.selfClosing);
        assertNull(tag.attributes);
    }

    // Add more test cases for other methods and edge cases

}