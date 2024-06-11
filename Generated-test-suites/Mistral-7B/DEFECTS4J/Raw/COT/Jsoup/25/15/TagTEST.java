package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.parser.Tag;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class TagTest {

 private Tag pTag;
 private Tag divTag;
 private Tag imgTag;
 private Tag unknownTag;

 @Before
 public void setUp() {
 pTag = Tag.valueOf("p");
 divTag = Tag.valueOf("div");
 imgTag = Tag.valueOf("img");
 unknownTag = new Tag("UnknownTag");
 }

 @Test
 public void testValueOf() {
 // Test getting a known tag by name
 Tag tag = Tag.valueOf("p");
 assertEquals("p", tag.getName());

 // Test getting an unknown tag, which should be registered as a generic tag
 tag = Tag.valueOf("UnknownTag");
 assertEquals("UnknownTag", tag.getName());
 assertFalse(tag.isBlock());
 }

 @Test
 public void testIsBlock() {
 // Test block tag
 assertTrue(pTag.isBlock());
 assertTrue(divTag.isBlock());

 // Test inline tag
 assertFalse(imgTag.isBlock());
 }

 @Test
 public void testFormatAsBlock() {
 // Test block tag formatted as block
 assertTrue(pTag.formatAsBlock());
 assertTrue(divTag.formatAsBlock());

 // Test inline tag formatted as inline
 assertFalse(imgTag.formatAsBlock());
 }

 @Test
 public void testCanContainBlock() {
 // Test block tag that can contain block level tags
 assertTrue(pTag.canContainBlock());
 assertTrue(divTag.canContainBlock());

 // Test inline tag that cannot contain block level tags
 assertFalse(imgTag.canContainBlock());
 }

 @Test
 public void testCanContainInline() {
 // Test block tag that can contain inline elements
 assertTrue(pTag.canContainInline());
 assertTrue(divTag.canContainInline());

 // Test empty tag that can only contain pcdata
 assertTrue(imgTag.canContainInline());
 assertFalse(imgTag.canContainBlock());
 }

 @Test
 public void testIsEmpty() {
 // Test empty tag
 assertTrue(imgTag.isEmpty());

 // Test non empty tag
 assertFalse(pTag.isEmpty());
 }

 @Test
 public void testIsSelfClosing() {
 // Test self closing tag
 assertTrue(imgTag.isSelfClosing());

 // Test non self closing tag
 assertFalse(pTag.isSelfClosing());
 }

 @Test
 public void testIsKnownTag() {
 // Test known tag
 assertTrue(Tag.isKnownTag("p"));

 // Test unknown tag
 assertFalse(Tag.isKnownTag("UnknownTag"));
 }

 @Test
 public void testPreserveWhitespace() {
 // Test tag that preserves whitespace
 assertTrue(pTag.preserveWhitespace());

 // Test tag that does not preserve whitespace
 assertFalse(imgTag.preserveWhitespace());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullTagName() {
 Tag tag = null;
 tag = new Tag(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyTagName() {
 Tag tag = new Tag("");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBlankTagName() {
 Tag tag = new Tag(" ");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCaseInsensitiveTagName() {
 Tag tag = new Tag("P");
 assertFalse(tag.isBlock());
 tag = new Tag("p");
 assertTrue(tag.isBlock());
 }

 @Test(expected = IllegalStateException.class)
 public void testTagRegistrationFailed() {
 Tag tag = new Tag("UnknownTag");
 tag.canContainBlock = true;
 tag.register(tag);
 }

 @Test
 public void testRegisterAllTags() {
 // Ensure all tags are registered
 for (String tagName : Tag.blockTags) {
 Tag tag = Tag.valueOf(tagName);
 assertTrue(Tag.isKnownTag(tagName));
 }
 for (String tagName : Tag.inlineTags) {
 Tag tag = Tag.valueOf(tagName);
 assertTrue(Tag.isKnownTag(tagName));
 }
 for (String tagName : Tag.emptyTags) {
 Tag tag = Tag.valueOf(tagName);
 assertTrue(Tag.isKnownTag(tagName));
 }
 for (String tagName : Tag.formatAsInlineTags) {
 Tag tag = Tag.valueOf(tagName);
 assertTrue(Tag.isKnownTag(tagName));
 }
 for (String tagName : Tag.preserveWhitespaceTags) {
 Tag tag = Tag.valueOf(tagName);
 assertTrue(Tag.isKnownTag(tagName));
 }
 }

 @Test
 public void testEquals() {
 Tag tag1 = new Tag("p");
 Tag tag2 = new Tag("p");
 Tag tag3 = new Tag("div");
 assertEquals(tag1, tag1);
 assertEquals(tag1, tag2);
 assertNotEquals(tag1, tag3);
 }

 @Test
 public void testHashCode() {
 Tag tag1 = new Tag("p");
 Tag tag2 = new Tag("p");
 Tag tag3 = new Tag("div");
 int hashCode1 = tag1.hashCode();
 int hashCode2 = tag2.hashCode();
 int hashCode3 = tag3.hashCode();
 assertEquals(hashCode1, hashCode2);
 assertNotEquals(hashCode1, hashCode3);
 }
}