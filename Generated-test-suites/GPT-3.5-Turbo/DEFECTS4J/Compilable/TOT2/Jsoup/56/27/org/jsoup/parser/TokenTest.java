package org.jsoup.parser;
import static org.junit.Assert.*;
import org.junit.Test;

public class TokenTest {

    @Test
    public void testTokenType() {
        Token token = new Token.Doctype();
        assertEquals("Doctype", token.tokenType());
    }

    @Test
    public void testDoctypeReset() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("testName");
        doctype.reset();
        assertEquals("", doctype.name.toString());
        assertFalse(doctype.forceQuirks);
    }

    @Test
    public void testDoctypeEdgeCases() {
        // Add edge cases test here
    }

    @Test
    public void testTagReset() {
        Token.Tag tag = new Token.StartTag();
        tag.tagName = "testName";
        tag.reset();
        assertNull(tag.tagName);
        assertNull(tag.normalName);
        assertFalse(tag.selfClosing);
    }

    @Test
    public void testTagNewAttribute() {
        Token.Tag tag = new Token.StartTag();
        tag.appendAttributeName("testAttr");
        tag.newAttribute();
        // Add assertion here
    }

    @Test
    public void testTagAttributeValues() {
        Token.Tag tag = new Token.StartTag();
        // Test various attribute value scenarios
    }

    // Add more test methods as needed

}