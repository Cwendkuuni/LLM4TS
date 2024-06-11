package org.jsoup.parser;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class TokenTest {

    @Test
    public void testTokenType() {
        Token token = new Token() {
            @Override
            Token reset() {
                return null;
            }
        };
        String expected = "Token";
        String actual = token.tokenType();
        assertEquals(expected, actual);
    }

    @Test
    public void testDoctypeGetName() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("test");
        String expected = "test";
        String actual = doctype.getName();
        assertEquals(expected, actual);
    }

    @Test
    public void testDoctypeGetPubSysKey() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.pubSysKey = "test";
        String expected = "test";
        String actual = doctype.getPubSysKey();
        assertEquals(expected, actual);
    }

    @Test
    public void testDoctypeGetPublicIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.publicIdentifier.append("test");
        String expected = "test";
        String actual = doctype.getPublicIdentifier();
        assertEquals(expected, actual);
    }

    @Test
    public void testDoctypeGetSystemIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.systemIdentifier.append("test");
        String expected = "test";
        String actual = doctype.getSystemIdentifier();
        assertEquals(expected, actual);
    }

    @Test
    public void testDoctypeIsForceQuirks() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.forceQuirks = true;
        boolean expected = true;
        boolean actual = doctype.isForceQuirks();
        assertEquals(expected, actual);
    }

    @Test
    public void testStartTagReset() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "test";
        startTag.normalName = "TEST";
        startTag.pendingAttributeName = "attr";
        startTag.pendingAttributeValue.append("value");
        startTag.pendingAttributeValueS = "test";
        startTag.hasEmptyAttributeValue = true;
        startTag.hasPendingAttributeValue = true;
        startTag.selfClosing = true;
        startTag.attributes = new Attributes();
        
        startTag.reset();
        
        assertNull(startTag.tagName);
        assertNull(startTag.normalName);
        assertNull(startTag.pendingAttributeName);
        assertTrue(startTag.pendingAttributeValue.length() == 0);
        assertNull(startTag.pendingAttributeValueS);
        assertFalse(startTag.hasEmptyAttributeValue);
        assertFalse(startTag.hasPendingAttributeValue);
        assertFalse(startTag.selfClosing);
        assertNull(startTag.attributes);
    }
    // repeated for each method in the Token class

}