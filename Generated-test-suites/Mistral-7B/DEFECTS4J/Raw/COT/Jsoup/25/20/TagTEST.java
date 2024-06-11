package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class TagTest {

 private Tag tag;

 @Before
 public void setUp() {
 tag = Tag.valueOf("div");
 }

 @Test
 public void testTagName() {
 String tagName = tag.getName();
 assertEquals("div", tagName);
 }

 @Test
 public void testIsBlock() {
 boolean isBlock = tag.isBlock();
 assertTrue(isBlock);
 }

 @Test
 public void testFormatAsBlock() {
 boolean formatAsBlock = tag.formatAsBlock();
 assertTrue(formatAsBlock);
 }

 @Test
 public void testCanContainBlock() {
 boolean canContainBlock = tag.canContainBlock();
 assertTrue(canContainBlock);
 }

 @Test
 public void testIsData() {
 assertFalse(tag.isData());
 }

 @Test
 public void testIsEmpty() {
 assertFalse(tag.isEmpty());
 }

 @Test
 public void testIsSelfClosing() {
 assertFalse(tag.isSelfClosing());
 }

 @Test
 public void testIsKnownTag() {
 assertTrue(Tag.isKnownTag("div"));
 }

 @Test(expected = NullPointerException.class)
 public void testNullTagName() {
 Tag tag = null;
 try {
 Tag.valueOf(null);
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test
 public void testEmptyTag() {
 Tag emptyTag = Tag.valueOf("img");
 emptyTag.canContainBlock = false;
 emptyTag.canContainInline = false;
 emptyTag.empty = true;
 assertTrue(emptyTag.isData());
 assertTrue(emptyTag.isEmpty());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidTagName() {
 Tag tag = null;
 try {
 tag = Tag.valueOf(" ");
 } catch (IllegalArgumentException e) {
 // expected
 }
 assertNull(tag);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTagNameWithSlash() {
 Tag tag = null;
 try {
 tag = Tag.valueOf("/html");
 } catch (IllegalArgumentException e) {
 // expected
 }
 assertNull(tag);
 }

 @Test
 public void testTagRegistering() {
 Map<String, Tag> tags = new HashMap<>();
 Tag htmlTag = new Tag("html");
 Tag divTag = new Tag("div");
 Tag tag = null;
 synchronized (tags) {
 tag = tags.get("div");
 tags.put("html", htmlTag);
 tags.put("div", divTag);
 }
 assertSame(divTag, tag);
 }
}