package org.jsoup.parser; // import necessary classes here if not already imported

import org.junit.Test;
import org.junit.Assert.*;

public class TokenTest {
    

    @Test
    public void testTokenType() {
        Token token = new Token().tokenType();
        assertEquals("Token", token);

        // Test other specific token types
        Token doctypeToken = new Token().new Doctype().tokenType();
        assertEquals("Doctype", doctypeToken);

        Token startTagToken = new Token().new StartTag().tokenType();
        assertEquals("StartTag", startTagToken);

        Token endTagToken = new Token().new EndTag().tokenType();
        assertEquals("EndTag", endTagToken);

        Token commentToken = new Token().new Comment().tokenType();
        assertEquals("Comment", commentToken);

        Token characterToken = new Token().new Character().tokenType();
        assertEquals("Character", characterToken);

        Token eofToken = new Token().new EOF().tokenType();
        assertEquals("EOF", eofToken);
    }

    @Test
    public void testDoctypeReset() {
        Token doctypeToken = new Token().new Doctype();
        doctypeToken.getName().concat("test");
        doctypeToken.getPublicIdentifier().concat("test");
        doctypeToken.getSystemIdentifier().concat("test");
        doctypeToken.isForceQuirks = true;

        Doctype resetDoctype = doctypeToken.reset();

        assertFalse(doctypeToken.isForceQuirks());
        assertEquals("", doctypeToken.getName());
        assertEquals(null, doctypeToken.getPublicIdentifier());
        assertEquals(null, doctypeToken.getSystemIdentifier());
        assertEquals(doctypeToken, resetDoctype);
    }

    @Test
    public void testTagReset() {
        Token tagToken = new Token().new StartTag();
        tagToken.tagName = "testTag";
        tagToken.newAttribute().put("testAttr", "testValue");

        Tag resetTag = tagToken.reset();

        assertEquals(false, tagToken.isSelfClosing());
        assertEquals(null, tagToken.tagName);
        assertEquals(null, tagToken.getAttributes());

        assertEquals(tagToken, resetTag);
    }

    @Test(expected = AssertionError.class)
    public void testTagNewAttributeWithEmptyAttributeValue() {
        Token tagToken = new Token().new StartTag();
        tagToken.tagName = "testTag";
        tagToken.newAttribute().put("testAttr", "");
        tagToken.newAttribute(); // trying to set another attribute with empty string value
    }

    @Test(expected = AssertionError.class)
    public void testTagNewAttributeWithPendingAttributeValue() {
        Token tagToken = new Token().new StartTag();
        tagToken.tagName = "testTag";
        tagToken.newAttribute().put("testAttr", "testValue");
        tagToken.appendAttributeValue("anotherValue");
        tagToken.newAttribute();
    }

    @Test
    public void testTagFinaliseTag() {
        Token tagToken = new Token().new StartTag().name("testTag");
        tagToken.newAttribute().put("testAttr", "testValue");
        tagToken.finaliseTag();

        assertEquals("testTag", tagToken.name());
        assertEquals("testValue", tagToken.getAttributes().get("testAttr").getValue());
        assertEquals(false, tagToken.isSelfClosing());
        assertEquals(null, tagToken.getAttributes());
    }

    // ... Add other test cases for other methods and edge cases here ...

    
}