package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jsoup.parser.Tag;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class TagTest {

 private static final Map<String, Tag> tags = new HashMap<>(); // map of known tags

 @Test
 public void testTagNameGetters() {
 Tag tag = new Tag("div");
 assertEquals("div", tag.getName());
 assertEquals("div", tag.toString());
 }

 @Test
 public void testTagValueOf() {
 Tag tag = Tag.valueOf("p");
 assertEquals("p", tag.getName());

 tag = Tag.valueOf("DIV");
 assertEquals("DIV", tag.getName());
 }

 @Test
 public void testIsBlock() {
 Tag tag = new Tag("p");
 assertTrue(tag.isBlock());

 tag = new Tag("input");
 assertFalse(tag.isBlock());
 }

 @Test
 public void testFormatAsBlock() {
 Tag tag = new Tag("p");
 assertTrue(tag.formatAsBlock());

 tag = new Tag("input");
 assertFalse(tag.formatAsBlock());
 }

 @Test
 public void testCanContainBlock() {
 Tag tag = new Tag("p");
 assertTrue(tag.canContainBlock()); // equivalent to isBlock()

 tag = new Tag("input");
 assertFalse(tag.canContainBlock()); // equivalent to isBlock()
 }

 @Test
 public void testIsInline() {
 Tag tag = new Tag("p");
 assertFalse(tag.isInline());

 tag = new Tag("input");
 assertTrue(tag.isInline());
 }

 @Test
 public void testIsData() {
 Tag tag = new Tag("p");
 assertFalse(tag.isData());

 tag = new Tag("script");
 assertTrue(tag.isData());
 }

 @Test
 public void testIsEmpty() {
 Tag tag = new Tag("img");
 assertFalse(tag.isEmpty());

 tag = new Tag("img").setSelfClosing();
 assertTrue(tag.isEmpty());
 }

 @Test
 public void testIsKnownTag() {
 Tag tag = new Tag("p");
 assertTrue(Tag.isKnownTag("p"));

 tag = new Tag("customTag");
 assertFalse(Tag.isKnownTag("customTag"));
 }

 @Test(expected = NullPointerException.class)
 public void testNullTagName() {
 new Tag(null);
 }

 @Test
 public void testPreserveWhitespace() {
 Tag tag = new Tag("pre");
 assertTrue(tag.preserveWhitespace());

 tag = new Tag("p");
 assertFalse(tag.preserveWhitespace());
 }

 @Test
 public void testIsFormListed() {
 Tag tag = new Tag("input");
 assertTrue(tag.isFormListed());

 tag = new Tag("p");
 assertFalse(tag.isFormListed());
 }

 @Test
 public void testIsFormSubmittable() {
 Tag tag = new Tag("input[type=submit]");
 assertTrue(tag.isFormSubmittable());

 tag = new Tag("p");
 assertFalse(tag.isFormSubmittable());
 }

 @Test
 public void testEquals() {
 Tag tag1 = new Tag("p");
 Tag tag2 = new Tag("p");
 assertEquals(tag1, tag2);

 tag2 = new Tag("div");
 assertFalse(tag1.equals(tag2));
 }

 @Test
 public void testHashCode() {
 Tag tag1 = new Tag("p");
 Tag tag2 = new Tag("p");
 assertEquals(tag1.hashCode(), tag2.hashCode());

 tag2 = new Tag("div");
 assertFalse(tag1.hashCode() == tag2.hashCode());
 }

 // Edge cases and exception handling tests

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyTagName() {
 new Tag(" ");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTagNameWithSpecialChar() {
 new Tag("<tag>");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTagNameWithSpecialCharAndDigit() {
 new Tag("<tag123>");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTagNameWithDigitOnly() {
 new Tag("123");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTagNameWithOnlyDigitAndSpecialChars() {
 new Tag("##@%^&*()_+=-[]{};':\"|,.<>/?");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTagNameWithOnlySpecialChars() {
 new Tag("@#$%^&*()_+=-[]{};':\"|,.<>/?");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTagNameWithWhitespace() {
 new Tag("tag with space");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTagNameWithMultipleWhitespace() {
 new Tag("tag \t with \n space");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTagNameWithLeadingOrTrailingWhitespace() {
 new Tag(" tagName ");
 new Tag(" tagName ");
 }

 @Test(expected =IllegalArgumentException.class)
 public void testTagNameWithNullSetting() {
 new Tag("", null);
 }
}