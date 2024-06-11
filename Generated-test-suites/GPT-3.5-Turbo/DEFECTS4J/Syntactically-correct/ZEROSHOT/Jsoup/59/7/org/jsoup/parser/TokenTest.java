package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testTokenType() {
        Token token = new Token() {
            @Override
            Token reset() {
                return null;
            }
        };
        TokenType expected = Token.TokenType.EOF;
        TokenType actual = token.type;
        assertEquals(expected, actual);
    }

    @Test
    public void testReset() {
        Token token = new Token() {
            @Override
            Token reset() {
                return null;
            }
        };

        assertNull(token.reset());
    }

    @Test
    public void testDoctypeReset() {
        String expectedName = "";
        String expectedPubSysKey = null;
        String expectedPublicIdentifier = "";
        String expectedSystemIdentifier = "";
        boolean expectedForceQuirks = false;

        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("Test");
        doctype.pubSysKey = "Test PubSysKey";
        doctype.publicIdentifier.append("Test PublicIdentifier");
        doctype.systemIdentifier.append("Test SystemIdentifier");
        doctype.forceQuirks = true;

        Token.Doctype resetDoctype = doctype.reset();

        assertEquals(expectedName, resetDoctype.getName());
        assertEquals(expectedPubSysKey, resetDoctype.getPubSysKey());
        assertEquals(expectedPublicIdentifier, resetDoctype.getPublicIdentifier());
        assertEquals(expectedSystemIdentifier, resetDoctype.getSystemIdentifier());
        assertEquals(expectedForceQuirks, resetDoctype.isForceQuirks());
    }

    @Test
    public void testTagReset() {
        String expectedTagName = null;
        String expectedNormalName = null;
        String expectedPendingAttributeName = null;
        StringBuilder expectedPendingAttributeValue = new StringBuilder();
        String expectedPendingAttributeValueS = null;
        boolean expectedHasEmptyAttributeValue = false;
        boolean expectedHasPendingAttributeValue = false;
        boolean expectedSelfClosing = false;
        Attributes expectedAttributes = null;

        Token.Tag tag = new Token.Tag() {
            @Override
            Tag reset() {
                return null;
            }
        };
        tag.tagName = "Test Tag";
        tag.normalName = "test tag";
        tag.pendingAttributeName = "Test PendingAttributeName";
        tag.pendingAttributeValue.append("Test PendingAttributeValue");
        tag.pendingAttributeValueS = "Test PendingAttributeValueS";
        tag.hasEmptyAttributeValue = true;
        tag.hasPendingAttributeValue = true;
        tag.selfClosing = true;
        tag.attributes = new Attributes();

        Token.Tag resetTag = tag.reset();

        assertEquals(expectedTagName, resetTag.tagName);
        assertEquals(expectedNormalName, resetTag.normalName);
        assertEquals(expectedPendingAttributeName, resetTag.pendingAttributeName);
        assertEquals(expectedPendingAttributeValue.toString(), resetTag.pendingAttributeValue.toString());
        assertEquals(expectedPendingAttributeValueS, resetTag.pendingAttributeValueS);
        assertEquals(expectedHasEmptyAttributeValue, resetTag.hasEmptyAttributeValue);
        assertEquals(expectedHasPendingAttributeValue, resetTag.hasPendingAttributeValue);
        assertEquals(expectedSelfClosing, resetTag.selfClosing);
        assertEquals(expectedAttributes, resetTag.attributes);
    }

    @Test
    public void testTagNewAttribute() {
        String expectedAttributeName = "Test NewAttribute";
        String expectedAttributeValue = "Test AttributeValue";

        Token.Tag tag = new Token.Tag() {
            @Override
            Tag reset() {
                return null;
            }
        };
        tag.pendingAttributeName = "Test AttributeName";
        tag.pendingAttributeValue.append("Test AttributeValue");

        tag.newAttribute();

        assertEquals(expectedAttributeName, tag.pendingAttributeName);
        assertEquals(expectedAttributeValue, tag.pendingAttributeValue.toString());
    }

    @Test
    public void testTagFinaliseTag() {
        Token.Tag tag = new Token.Tag() {
            @Override
            Tag reset() {
                return null;
            }
        };
        tag.pendingAttributeValueS = "Test PendingAttributeValueS";

        tag.finaliseTag();

        assertNull(tag.pendingAttributeName);
        assertNull(tag.pendingAttributeValueS);
    }

    // TODO: Add more test cases to comprehensively test all methods in the Token class

}