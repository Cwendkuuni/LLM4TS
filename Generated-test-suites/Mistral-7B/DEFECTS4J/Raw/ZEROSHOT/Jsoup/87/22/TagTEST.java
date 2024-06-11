package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jsoup.parser.Tag;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class TagTest {

 private static final Map<String, Tag> tags = new HashMap<>();

 @Test
 public void testTagCreation() {
 Tag tag = Tag.valueOf("p");
 assertEquals("p", tag.getName());
 }

 @Test
 public void testTagValueOfKnownTag() {
 Tag tag = Tag.valueOf("p");
 Tag tagFromMap = Tag.valueOf("p");
 assertEquals(tag, tagFromMap);
 }

 @Test
 public void testTagValueOfUnknownTag() {
 Tag tag = Tag.valueOf("unknownTag");
 assertFalse(tag.isBlock());
 }

 @Test
 public void testTagIsBlock() {
 Tag pTag = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");
 assertTrue(pTag.isBlock());
 assertTrue(divTag.isBlock());
 }

 @Test
 public void testTagFormatAsBlock() {
 Tag pTag = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");
 assertTrue(pTag.formatAsBlock());
 assertTrue(divTag.formatAsBlock());
 }

 @Test
 public void testTagCanContainBlock() {
 Tag pTag = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");
 assertTrue(pTag.canContainBlock());
 assertTrue(divTag.canContainBlock());
 }

 @Test
 public void testTagIsEmpty() {
 Tag imgTag = Tag.valueOf("img");
 assertFalse(imgTag.isEmpty());
 }

 @Test
 public void testTagIsSelfClosing() {
 Tag imgTag = Tag.valueOf("img");
 assertFalse(imgTag.isSelfClosing());
 }

 @Test
 public void testTagIsSelfClosingWithSelfClosingTag() {
 Tag imgTag = Tag.valueOf("img").setSelfClosing();
 assertTrue(imgTag.isSelfClosing());
 }

 @Test
 public void testTagIsKnownTag() {
 assertTrue(Tag.isKnownTag("p"));
 }

 @Test
 public void testTagIsData() {
 Tag scriptTag = Tag.valueOf("script");
 assertFalse(scriptTag.isData());
 }

 @Test
 public void testTagPreserveWhitespace() {
 Tag preTag = Tag.valueOf("pre");
 assertTrue(preTag.preserveWhitespace());
 }

 @Test
 public void testTagIsFormListed() {
 Tag inputTag = Tag.valueOf("input");
 assertTrue(inputTag.isFormListed());
 }

 @Test
 public void testTagIsFormSubmittable() {
 Tag textareaTag = Tag.valueOf("textarea");
 assertTrue(textareaTag.isFormSubmittable());
 }
}