package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

public class TagTest {

 private static final Tag HTML = Tag.valueOf("html");
 private static final Tag DIV = Tag.valueOf("div");
 private static final Tag P = Tag.valueOf("p");
 private static final Tag BR = Tag.valueOf("br");
 private static final Tag IMG = Tag.valueOf("img");

 @Test
 public void testTagValueOf() {
 Tag tag = Tag.valueOf("html");
 assertEquals("html", tag.getName());
 tag = Tag.valueOf("div");
 assertEquals("div", tag.getName());
 }

 @Test
 public void testTagIsBlock() {
 assertTrue(HTML.isBlock());
 assertTrue(P.isBlock());
 assertFalse(BR.isBlock());
 }

 @Test
 public void testTagFormatAsBlock() {
 assertTrue(HTML.formatAsBlock());
 assertTrue(P.formatAsBlock());
 assertFalse(BR.formatAsBlock());
 }

 @Test
 public void testTagCanContainBlock() {
 assertTrue(HTML.canContainBlock());
 assertTrue(P.canContainBlock());
 assertFalse(BR.canContainBlock());
 }

 @Test
 public void testTagCanContainInline() {
 assertTrue(HTML.canContainInline());
 assertTrue(P.canContainInline());
 assertFalse(BR.canContainInline());
 }

 @Test
 public void testTagEmpty() {
 assertFalse(HTML.isEmpty());
 assertFalse(P.isEmpty());
 assertTrue(BR.isEmpty());
 assertTrue(IMG.isEmpty());
 }

 @Test
 public void testTagSelfClosing() {
 assertFalse(HTML.isSelfClosing());
 assertFalse(P.isSelfClosing());
 assertTrue(BR.isSelfClosing());
 assertTrue(IMG.isSelfClosing());
 }

 @Test
 public void testTagIsKnownTag() {
 assertTrue(Tag.isKnownTag("html"));
 assertTrue(Tag.isKnownTag("div"));
 assertFalse(Tag.isKnownTag("unknown"));
 }

 @Test
 public void testTagPreserveWhitespace() {
 assertFalse(HTML.preserveWhitespace());
 assertFalse(P.preserveWhitespace());
 assertTrue(Tag.valueOf("pre").preserveWhitespace());
 }

 @Test(expected = NullPointerException.class)
 public void testTagNullName() {
 Tag tag = new Tag(null);
 fail("Should not be able to instantiate Tag with null name");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTagEmptyTrimmedName() {
 Tag tag = new Tag("");
 fail("Should not be able to instantiate Tag with empty or trimmed name");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTagUpperCaseName() {
 Tag tag = new Tag("DIV");
 assertEquals("div", tag.getName());
 }

 @Test
 public void testTagValueOfUnknown() {
 Tag tag = Tag.valueOf("unknown");
 assertFalse(tag.isKnownTag());
 }

 @Test
 public void testTagEquals() {
 Tag tag1 = Tag.valueOf("p");
 Tag tag2 = Tag.valueOf("p");
 Tag tag3 = Tag.valueOf("div");

 assertEquals(tag1, tag1);
 assertEquals(tag1, tag2);
 assertNotEquals(tag1, tag3);
 }

 @Test
 public void testTagHashCode() {
 Tag tag1 = Tag.valueOf("p");
 Tag tag2 = Tag.valueOf("p");
 Tag tag3 = Tag.valueOf("div");

 int hashCode1 = tag1.hashCode();
 int hashCode2 = tag2.hashCode();
 int hashCode3 = tag3.hashCode();

 assertTrue(hashCode1 == hashCode2);
 assertNotEquals(hashCode1, hashCode3);
 }

 @Test
 public void testTagToString() {
 Tag tag = Tag.valueOf("p");
 assertEquals("p", tag.toString());
 }

 @Test
 public void testTagRegisterAllTags() {
 Map<String, Tag> map = new HashMap<>();
 Tag[] tags = new Tag[]{HTML, DIV, P, BR, IMG};

 for (Tag tag : tags) {
 Tag tagFromMap = map.get(tag.getName());
 if (tagFromMap != null) {
 fail("Multiple instance of tag found: " + tag.getName());
 }
 map.put(tag.getName(), tag);
 }
 }
}