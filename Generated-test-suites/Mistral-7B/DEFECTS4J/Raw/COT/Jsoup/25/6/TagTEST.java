package org.jsoup.parser.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.Tag;

import java.util.HashMap;
import java.util.Map;

public class TagTest {

 private Tag pTag;
 private Tag imgTag;
 private Tag unknownTag;

 @Before
 public void setUp() {
 pTag = Tag.valueOf("p");
 imgTag = Tag.valueOf("img");
 unknownTag = Tag.valueOf("unknown");
 }

 @Test
 public void testTagName() {
 Tag tag = new Tag("testTag");
 assertEquals("testTag", tag.getName());
 }

 @Test
 public void testTagValueOf() {
 assertEquals(pTag.getName(), "p");
 assertEquals(imgTag.getName(), "img");
 assertEquals(unknownTag.getName(), "unknown");
 }

 @Test
 public void testIsBlock() {
 assertTrue(pTag.isBlock());
 assertFalse(imgTag.isBlock());
 }

 @Test
 public void testFormatAsBlock() {
 assertTrue(pTag.formatAsBlock());
 assertFalse(imgTag.formatAsBlock());
 }

 @Test
 public void testCanContainBlock() {
 assertTrue(pTag.canContainBlock());
 assertTrue(imgTag.canContainBlock());
 }

 @Test
 public void testIsInline() {
 assertFalse(pTag.isInline());
 assertTrue(imgTag.isInline());
 }

 @Test
 public void testIsData() {
 assertFalse(pTag.isData());
 assertTrue(imgTag.isData());
 }

 @Test
 public void testIsEmpty() {
 assertFalse(pTag.isEmpty());
 assertFalse(imgTag.isEmpty());
 }

 @Test
 public void testIsSelfClosing() {
 assertFalse(pTag.isSelfClosing());
 assertFalse(imgTag.isSelfClosing());
 }

 @Test
 public void testIsKnownTag() {
 assertTrue(Tag.isKnownTag("p"));
 assertFalse(Tag.isKnownTag("unknown"));
 }

 @Test(expected = NullPointerException.class)
 public void testNullTagNameThrowsException() {
 Tag tag = new Tag(null);
 // test will fail with NullPointerException
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyTagNameThrowsException() {
 Tag tag = new Tag("");
 // test will fail with IllegalArgumentException
 }

 @Test
 public void testGetTagsMapIsNotEmpty() {
 Map<String, Tag> tags = Tag.tags;
 assertFalse(tags.isEmpty());
 }

 @Test
 public void testTagRegistration() {
 Tag p = new Tag("p");
 Tag q = new Tag("q");
 Tag r = new Tag("r");
 Tag.register(p);
 Tag.register(q);
 Tag.register(r);
 assertEquals(3, Tag.tags.size());
 }

 @Test
 public void testRegisterDuplicateTagNameThrowsException() {
 Tag p = new Tag("p");
 Tag p2 = new Tag("p");
 Tag.register(p);
 try {
 Tag.register(p2);
 fail("Should have thrown an exception");
 } catch (IllegalStateException e) {
 // pass
 }
 }

 @Test
 public void testValueOfUnknownTag() {
 Tag tag = Tag.valueOf("unknown");
 assertFalse(tag.isBlock());
 assertFalse(tag.formatAsBlock());
 assertFalse(tag.canContainBlock());
 assertFalse(tag.canContainInline());
 assertFalse(tag.isEmpty());
 assertFalse(tag.isSelfClosing());
 assertFalse(tag.preserveWhitespace());
 }
}