package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class TagTest {

 private Tag pTag;
 private Tag divTag;
 private Tag imgTag;
 private Tag unknownTag;

 @Before
 public void setup() {
 pTag = Tag.valueOf("p");
 divTag = Tag.valueOf("div");
 imgTag = Tag.valueOf("img");
 unknownTag = new Tag("UnknownTag");
 }

 @Test
 public void testTagValueOf() {
 // Test getting a Tag by name
 Assert.assertEquals("p", pTag.getName());
 Assert.assertEquals("div", divTag.getName());

 // Test getting an unknown tag
 Tag unknown = Tag.valueOf("UnknownTag");
 Assert.assertNotEquals("p", unknown.getName());
 Assert.assertFalse(unknown.isKnownTag());
 }

 @Test
 public void testIsBlock() {
 // Test isBlock method for known tags
 Assert.assertTrue(pTag.isBlock());
 Assert.assertTrue(divTag.isBlock());

 // Test isBlock method for unknown tag
 Assert.assertFalse(unknownTag.isBlock());
 }

 @Test
 public void testFormatAsBlock() {
 // Test formatAsBlock method for known tags
 Assert.assertTrue(pTag.formatAsBlock());
 Assert.assertTrue(divTag.formatAsBlock());

 // Test formatAsBlock method for empty tag
 Assert.assertFalse(imgTag.formatAsBlock());
 }

 @Test
 public void testCanContainBlock() {
 // Test canContainBlock method for known tags
 Assert.assertTrue(pTag.canContainBlock());
 Assert.assertTrue(divTag.canContainBlock());

 // Test canContainBlock method for empty tag
 Assert.assertFalse(imgTag.canContainBlock());
 }

 @Test
 public void testIsInline() {
 // Test isInline method for known tags
 Assert.assertFalse(pTag.isInline());
 Assert.assertFalse(divTag.isInline());

 // Test isInline method for empty tag
 Assert.assertTrue(imgTag.isInline());
 }

 @Test
 public void testIsData() {
 // Test isData method for known tags
 Assert.assertFalse(pTag.isData());
 Assert.assertFalse(divTag.isData());

 // Test isData method for empty tag
 Assert.assertTrue(imgTag.isData());
 }

 @Test
 public void testIsEmpty() {
 // Test isEmpty method for known tags
 Assert.assertFalse(pTag.isEmpty());
 Assert.assertFalse(divTag.isEmpty());

 // Test isEmpty method for empty tag
 Assert.assertTrue(imgTag.isEmpty());
 }

 @Test
 public void testIsSelfClosing() {
 // Test isSelfClosing method for known tags
 Assert.assertFalse(pTag.isSelfClosing());
 Assert.assertFalse(divTag.isSelfClosing());

 // Test isSelfClosing method for empty tag
 Assert.assertTrue(imgTag.isSelfClosing());
 }

 @Test
 public void testIsKnownTag() {
 // Test isKnownTag method for known tags
 Assert.assertTrue(pTag.isKnownTag());
 Assert.assertTrue(divTag.isKnownTag());

 // Test isKnownTag method for unknown tag
 Assert.assertFalse(unknownTag.isKnownTag());
 }

 @Test(expected = NullPointerException.class)
 public void testNullTagName() {
 Tag tag = null;
 tag.setValueOf(null);
 }

 @Test
 public void testEquals() {
 Tag tag1 = Tag.valueOf("p");
 Tag tag2 = Tag.valueOf("p");
 Tag tag3 = Tag.valueOf("div");

 Assert.assertEquals(tag1, tag1);
 Assert.assertEquals(tag1, tag2);
 Assert.assertNotEquals(tag1, tag3);
 }

 @Test
 public void testHashCode() {
 Tag tag1 = Tag.valueOf("p");
 Tag tag2 = Tag.valueOf("p");
 Tag tag3 = Tag.valueOf("div");

 Assert.assertEquals(tag1.hashCode(), tag1.hashCode());
 Assert.assertEquals(tag1.hashCode(), tag2.hashCode());
 Assert.assertNotEquals(tag1.hashCode(), tag3.hashCode());
 }

 @Test
 public void testToString() {
 Assert.assertEquals("p", pTag.toString());
 Assert.assertEquals("div", divTag.toString());
 }

 @Test
 public void testMapSizeAfterInitialization() {
 Map<String, Tag> tagMap = new HashMap<>();
 for (String tagName : blockTags) {
 tagMap.put(tagName, Tag.valueOf(tagName));
 }
 Assert.assertEquals(blockTags.length, tagMap.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyTagName() {
 Tag tag = new Tag("");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyTrimmedTagName() {
 Tag tag = new Tag(" ");
 }
}