package org.jsoup.parser; // Import necessary packages

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;

import org.junit.Test;

public class TokenTest {
    // Test case for reset method of Doctype class
    @Test
    public void testDoctypeReset() {
        Token doctype = new Token.Doctype();
        String expectedName = "";
        doctype.asDoctype().getName(); // Initialize doctype object

        Token resetDoctype = doctype.reset();
        assertSame(doctype, resetDoctype); // Check if reset method returns the same object
        assertEquals(expectedName, doctype.asDoctype().getName()); // Check if name is reset
        assertNull(doctype.asDoctype().getPubSysKey()); // Check if pubSysKey is reset
        assertNull(doctype.asDoctype().getPublicIdentifier()); // Check if publicIdentifier is reset
        assertNull(doctype.asDoctype().getSystemIdentifier()); // Check if systemIdentifier is reset
        assertFalse(doctype.asDoctype().isForceQuirks()); // Check if isForceQuirks is reset
    }

    // Test case for reset method of Tag class
    @Test
    public void testTagReset() {
        Token tag = new Token.Tag();
        String expectedTagName = "";
        tag.asTag().name(); // Initialize tag object

        Token resetTag = tag.reset();
        assertSame(tag, resetTag); // Check if reset method returns the same object
        assertNull(tag.asTag().tagName); // Check if tagName is reset
        assertNull(tag.asTag().normalName); // Check if normalName is reset
        assertNull(tag.asTag().pendingAttributeName); // Check if pendingAttributeName is reset
        assertNull(tag.asTag().pendingAttributeValue); // Check if pendingAttributeValue is reset
        assertFalse(tag.asTag().hasPendingAttributeValue); // Check if hasPendingAttributeValue is reset
        assertFalse(tag.asTag().selfClosing); // Check if selfClosing is reset
        assertNull(tag.asTag().attributes); // Check if attributes is reset
    }

    // Test case for newAttribute method of Tag class
    @Test
    public void testTagNewAttribute() {
        Token tag = new Token.Tag();
        String attributeName = "test";
        tag.asTag().appendAttributeName("test");
        tag.asTag().newAttribute();

        Attribute attribute = tag.asTag().getAttributes().getValue("test");
        assertNotNull(attribute);
        assertEquals("test", attribute.getKey());
        assertNull(tag.asTag().pendingAttributeName);
        assertFalse(tag.asTag().hasPendingAttributeValue);
    }

    // Test case for finaliseTag method of Tag class
    @Test
    public void testTagFinaliseTag() {
        Token tag = new Token.Tag();
        String attributeName = "test";
        tag.asTag().appendAttributeName("test");
        tag.asTag().newAttribute();
        tag.asTag().finaliseTag();

        Attribute attribute = tag.asTag().getAttributes().getValue("test");
        assertNotNull(attribute);
        assertEquals("test", attribute.getKey());
        assertNull(tag.asTag().pendingAttributeName);
        assertFalse(tag.asTag().hasPendingAttributeValue);
    }

    // Test case for getAttributes method of Tag class
    @Test
    public void testTagGetAttributes() {
        Token tag = new Token.Tag();
        tag.asTag().appendAttributeName("test");
        tag.asTag().newAttribute();
        tag.asTag().getAttributes().put(new Attribute("test", "testValue"));

        Attribute attribute = tag.asTag().getAttributes().getValue("test");
        assertNotNull(attribute);
        assertEquals("test", attribute.getKey());
        assertEquals("testValue", attribute.getValue());
    }

    // Test case for TokenType enum
    @Test
    public void testTokenTypeEnum() {
        TokenType doctypeTokenType = TokenType.Doctype;
        TokenType endTagTokenType = TokenType.EndTag;

        assertTrue(doctypeTokenType == TokenType.Doctype);
        assertTrue(endTagTokenType == TokenType.EndTag);

        String expectedString = "Doctype";
        assertTrue(doctypeTokenType.toString().equals(expectedString));
        expectedString = "EndTag";
        assertTrue(endTagTokenType.toString().equals(expectedString));
    }
}