package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class TagTest {

 @Test
 public void testTagValueOf() {
 String tagName = "p";
 Tag tag = Tag.valueOf(tagName);
 assertEquals(tagName, tag.getName());
 assertTrue(tag.isBlock());
 assertTrue(tag.formatAsBlock());
 assertTrue(tag.canContainInline());
 assertFalse(tag.isEmpty());
 assertFalse(tag.isSelfClosing());
 assertFalse(tag.preserveWhitespace());
 assertFalse(tag.isFormListed());
 assertFalse(tag.isFormSubmittable());

 tagName = "DIV";
 tag = Tag.valueOf(tagName);
 assertEquals(tagName, tag.getName());
 assertTrue(tag.isBlock());
 assertTrue(tag.formatAsBlock());
 assertTrue(tag.canContainInline());
 assertFalse(tag.isEmpty());
 assertFalse(tag.isSelfClosing());
 assertFalse(tag.preserveWhitespace());
 assertFalse(tag.isFormListed());
 assertFalse(tag.isFormSubmittable());

 tagName = "input";
 tag = Tag.valueOf(tagName);
 assertEquals(tagName, tag.getName());
 assertFalse(tag.isBlock());
 assertFalse(tag.formatAsBlock());
 assertTrue(tag.canContainInline());
 assertFalse(tag.isEmpty());
 assertFalse(tag.isSelfClosing());
 assertFalse(tag.preserveWhitespace());
 assertTrue(tag.isFormListed());
 assertTrue(tag.isFormSubmittable());
 }

 @Test
 public void testTagValueOfUnknownCaseInsensitive() {
 String tagName = "p";
 Tag tag = Tag.valueOf(tagName);
 assertEquals(tagName, tag.getName());
 // Test other methods with known tag

 tagName = "div";
 tag = Tag.valueOf(tagName);
 assertEquals(tagName, tag.getName());
 // Test other methods with unknown tag

 tagName = "UnknownTag";
 tag = Tag.valueOf(tagName);
 assertNotNull(tag);
 assertEquals("UnknownTag", tag.getName());
 assertFalse(tag.isBlock());
 assertFalse(tag.formatAsBlock());
 assertFalse(tag.canContainInline());
 assertFalse(tag.isEmpty());
 assertFalse(tag.isSelfClosing());
 assertFalse(tag.preserveWhitespace());
 assertFalse(tag.isFormListed());
 assertFalse(tag.isFormSubmittable());
 }

 @Test
 public void testTagValueOfUnknown() {
 String tagName = "UnknownTag";
 Tag tag = Tag.valueOf(tagName);
 assertNotNull(tag);
 assertEquals("UnknownTag", tag.getName());
 // Test methods with unknown tag
 }
}