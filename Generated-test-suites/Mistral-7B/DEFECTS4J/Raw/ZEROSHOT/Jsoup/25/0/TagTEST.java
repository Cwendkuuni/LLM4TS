package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class TagTest {

 @Test
 public void testTagName() {
 Tag tag = Tag.valueOf("p");
 assertEquals("p", tag.getName());
 }

 @Test
 public void testIsBlock() {
 Tag tag = Tag.valueOf("p");
 assertTrue(tag.isBlock());
 }

 @Test
 public void testFormatAsBlock() {
 Tag tag = Tag.valueOf("p");
 assertTrue(tag.formatAsBlock());
 }

 @Test
 public void testCanContainBlock() {
 Tag tag = Tag.valueOf("p");
 assertTrue(tag.canContainBlock());
 }

 @Test
 public void testCanContainInline() {
 Tag tag = Tag.valueOf("p");
 assertTrue(tag.canContainInline());
 }

 @Test
 public void testEmpty() {
 Tag tag = Tag.valueOf("img");
 assertFalse(tag.isEmpty());

 Tag imgTag = new Tag("img").setEmpty();
 assertTrue(imgTag.isEmpty());
 }

 @Test
 public void testSelfClosing() {
 Tag tag = Tag.valueOf("img");
 assertFalse(tag.isSelfClosing());

 Tag imgTag = new Tag("img").setSelfClosing();
 assertTrue(imgTag.isSelfClosing());
 }

 @Test
 public void testIsKnownTag() {
 assertTrue(Tag.isKnownTag("p"));
 assertFalse(Tag.isKnownTag("unknownTag"));
 }

 @Test
 public void testPreserveWhitespace() {
 Tag tag = Tag.valueOf("pre");
 assertTrue(tag.preserveWhitespace());
 }

 @Test
 public void testEquals() {
 Tag tag1 = Tag.valueOf("p");
 Tag tag2 = Tag.valueOf("p");
 assertEquals(tag1, tag2);
 }

 @Test
 public void testHashCode() {
 Tag tag1 = Tag.valueOf("p");
 Tag tag2 = Tag.valueOf("p");
 assertEquals(tag1.hashCode(), tag2.hashCode());
 }

 @Test
 public void testToString() {
 Tag tag = Tag.valueOf("p");
 assertEquals("p", tag.toString());
 }

 @Test(expected = NullPointerException.class)
 public void testNullTagName() {
 Tag tag = new Tag(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyTagName() {
 Tag tag = new Tag("");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUnknownTag() {
 Tag tag = null;
 try {
 tag = Tag.valueOf("unknownTag");
 } catch (IllegalArgumentException e) {
 // expected
 }
 assertNotNull(tag);
 assertEquals(Tag.class, tag.getClass());
 }

}