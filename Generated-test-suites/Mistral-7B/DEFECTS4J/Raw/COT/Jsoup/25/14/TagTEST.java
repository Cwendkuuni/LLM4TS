package org.jsoup.parser;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RunWith(Parameterized.class)
public class TagTest {

 private String tagName;
 private Tag tag;

 public TagTest(String tagName) {
 this.tagName = tagName;
 }

 @Parameterized.Parameters(name = "{index}: tagName={0}")
 public static Collection<Object[]> data() {
 return Arrays.asList(new Object[][]{
 // Add all possible test cases here
 });
 }

 @Test
 public void testTagName() {
 tag = Tag.valueOf(tagName);
 Assertions.assertEquals(tagName, tag.getName());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullTagName() {
 Assertions.assertThrows(IllegalArgumentException.class, () -> Tag.valueOf(null));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyTagName() {
 Assertions.assertThrows(IllegalArgumentException.class, () -> Tag.valueOf(""));
 }

 @Test
 public void testIsBlock() {
 tag = Tag.valueOf("div");
 Assertions.assertTrue(tag.isBlock());

 tag = Tag.valueOf("span");
 Assertions.assertFalse(tag.isBlock());
 }

 @Test
 public void testFormatAsBlock() {
 tag = Tag.valueOf("pre");
 Assertions.assertTrue(tag.formatAsBlock());

 tag = Tag.valueOf("span");
 Assertions.assertFalse(tag.formatAsBlock());
 }

 @Test
 public void testCanContainBlock() {
 tag = Tag.valueOf("div");
 Assertions.assertTrue(tag.canContainBlock());

 tag = Tag.valueOf("span");
 Assertions.assertFalse(tag.canContainBlock());
 }

 @Test
 public void testIsInline() {
 tag = Tag.valueOf("div");
 Assertions.assertFalse(tag.isInline());

 tag = Tag.valueOf("span");
 Assertions.assertTrue(tag.isInline());
 }

 @Test
 public void testIsData() {
 tag = Tag.valueOf("script");
 Assertions.assertTrue(tag.isData());

 tag = Tag.valueOf("p");
 Assertions.assertFalse(tag.isData());
 }

 @Test
 public void testIsEmpty() {
 tag = Tag.valueOf("img");
 Assertions.assertFalse(tag.isEmpty());

 tag = Tag.valueOf("br");
 Assertions.assertTrue(tag.isEmpty());
 }

 @Test
 public void testIsSelfClosing() {
 tag = Tag.valueOf("img");
 Assertions.assertFalse(tag.isSelfClosing());

 tag = Tag.valueOf("img");
 tag.setSelfClosing();
 Assertions.assertTrue(tag.isSelfClosing());
 }

 @Test
 public void testIsKnownTag() {
 tag = Tag.valueOf("div");
 Assertions.assertTrue(Tag.isKnownTag("div"));

 tag = Tag.valueOf("unknown");
 Assertions.assertFalse(Tag.isKnownTag("unknown"));
 }

 @Test
 public void testPreserveWhitespace() {
 tag = Tag.valueOf("pre");
 Assertions.assertTrue(tag.preserveWhitespace());

 tag = Tag.valueOf("span");
 Assertions.assertFalse(tag.preserveWhitespace());
 }

 @Test
 public void testEquals() {
 Tag tag1 = Tag.valueOf("div");
 Tag tag2 = Tag.valueOf("div");
 Tag tag3 = Tag.valueOf("span");
 Tag tag4 = tag1;

 Assertions.assertTrue(tag1.equals(tag2));
 Assertions.assertFalse(tag1.equals(tag3));
 Assertions.assertTrue(tag1.equals(tag4));
 }

 @Test
 public void testHashCode() {
 Tag tag1 = Tag.valueOf("div");
 Tag tag2 = Tag.valueOf("div");
 Tag tag3 = Tag.valueOf("span");

 Assertions.assertEquals(tag1.hashCode(), tag2.hashCode());
 Assertions.assertNotEquals(tag1.hashCode(), tag3.hashCode());
 }

 @Test
 public void testToString() {
 tag = Tag.valueOf("div");
 Assertions.assertEquals("div", tag.toString());
 }
}