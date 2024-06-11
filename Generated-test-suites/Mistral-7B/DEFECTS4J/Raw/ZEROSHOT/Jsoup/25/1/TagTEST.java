package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.parser.Tag;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

/**
 * JUnit 4 test file for Tag class.
 *
 * @author Johnson
 */
public class TagTest {

    @Test
    public void testTagValues() {
        // Test getting the tag value by name
        Tag pTag = Tag.valueOf("p");
        String actualPTagName = pTag.getName();
        String expectedPTagName = "p";
        assertEquals(expectedPTagName, actualPTagName);

        // Test getting known tag
        boolean isHtmlTagKnown = Tag.isKnownTag("html");
        assertTrue(isHtmlTagKnown);

        // Test getting unknown tag
        Tag unknownTag = Tag.valueOf("unknown");
        assertNotEquals("p", unknownTag.getName());
    }

    @Test
    public void testTagProperties() {
        // Test isBlock property
        Tag pTag = Tag.valueOf("p");
        boolean actualPIsBlock = pTag.isBlock();
        boolean expectedPIsBlock = true;
        assertEquals(expectedPIsBlock, actualPIsBlock);

        // Test formatAsBlock property
        Tag preTag = Tag.valueOf("pre");
        boolean actualPreFormatAsBlock = preTag.formatAsBlock();
        boolean expectedPreFormatAsBlock = true;
        assertEquals(expectedPreFormatAsBlock, actualPreFormatAsBlock);

        // Test canContainBlock property
        Tag spanTag = Tag.valueOf("span");
        boolean actualSpanCanContainBlock = spanTag.canContainBlock();
        boolean expectedSpanCanContainBlock = true;
        assertEquals(expectedSpanCanContainBlock, actualSpanCanContainBlock);

        // Test isEmpty property
        Tag imgTag = Tag.valueOf("img");
        boolean actualImgIsEmpty = imgTag.isEmpty();
        boolean expectedImgIsEmpty = false;
        assertEquals(expectedImgIsEmpty, actualImgIsEmpty);

        // Test isSelfClosing property
        Tag inputTag = Tag.valueOf("input");
        boolean actualInputIsSelfClosing = inputTag.isSelfClosing();
        boolean expectedInputIsSelfClosing = false;
        assertEquals(expectedInputIsSelfClosing, actualInputIsSelfClosing);

        Tag imgTagSelfClosing = Tag.valueOf("img").setSelfClosing();
        boolean actualImgSelfClosingIsSelfClosing = imgTagSelfClosing.isSelfClosing();
        boolean expectedImgSelfClosingIsSelfClosing = true;
        assertEquals(expectedImgSelfClosingIsSelfClosing, actualImgSelfClosingIsSelfClosing);

        // Test isKnownTag property
        Tag unknownTag = Tag.valueOf("unknown");
        boolean actualUnknownIsKnownTag = unknownTag.isKnownTag();
        boolean expectedUnknownIsKnownTag = false;
        assertEquals(expectedUnknownIsKnownTag, actualUnknownIsKnownTag);

        // Test preserveWhitespace property
        Tag preTagWithPreserveWhitespace = Tag.valueOf("pre").setPreserveWhitespace(true);
        boolean actualPreWithPreserveWhitespacePreserveWhitespace = preTagWithPreserveWhitespace.preserveWhitespace();
        boolean expectedPreWithPreserveWhitespacePreserveWhitespace = true;
        assertEquals(expectedPreWithPreserveWhitespacePreserveWhitespace, actualPreWithPreserveWhitespacePreserveWhitespace);
    }

    @Test
    public void testTagHashCodeAndEquals() {
        // Test equals method
        Tag pTag1 = Tag.valueOf("p");
        Tag pTag2 = Tag.valueOf("p");
        boolean actualPTagsEqual = pTag1.equals(pTag2);
        boolean expectedPTagsEqual = true;
        assertEquals(expectedPTagsEqual, actualPTagsEqual);

        Tag spanTag1 = Tag.valueOf("span");
        Tag spanTag2 = Tag.valueOf("span");
        boolean actualSpanTagsEqual = spanTag1.equals(spanTag2);
        boolean expectedSpanTagsEqual = true;
        assertEquals(expectedSpanTagsEqual, actualSpanTagsEqual);

        Tag pTag3 = new Tag("p"); // different object
        boolean actualPTag1EqualsPTag3 = pTag1.equals(pTag3);
        boolean expectedPTag1EqualsPTag3 = false;
        assertEquals(expectedPTag1EqualsPTag3, actualPTag1EqualsPTag3);

        // Test hashCode method
        Tag spanTag4 = Tag.valueOf("span");
        int actualSpanTag4HashCode = spanTag4.hashCode();
        int expectedSpanTag4HashCode = 285556201; // tested value, may change
        assertEquals(expectedSpanTag4HashCode, actualSpanTag4HashCode);
    }

    @Test
    public void testTagRegisteredValues() {
        // Test known tags are registered
        boolean isHtmlRegistered = Tag.isKnownTag("html");
        assertTrue(isHtmlRegistered);

        // Test unknown tag is not registered
        Tag unknownTag = Tag.valueOf("unknown");
        boolean isUnknownRegistered = Tag.isKnownTag("unknown");
        assertFalse(isUnknownRegistered);

        // Test tag registration
        Tag registeredTag = new Tag("registeredTag");
        Tag registeredTagValue = Tag.valueOf("registeredTag");
        assertTrue(registeredTag.equals(registeredTagValue));
        assertTrue(Tag.isKnownTag("registeredTag"));
    }
}