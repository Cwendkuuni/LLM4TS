package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TagTest {

 @Test
 public void testTagName() {
 Tag tag = new Tag("p");
 assertEquals("p", tag.getName());
 }

 @Test
 public void testTagCaseInsensitive() {
 Tag tag = Tag.valueOf("p");
 assertEquals("p", tag.getName());

 Tag tagUpper = Tag.valueOf("P");
 assertEquals("p", tagUpper.getName());
 }

 @Test
 public void testTagValueOfUnknown() {
 Tag tag = Tag.valueOf("foo");
 assertFalse(tag.isKnownTag());
 assertEquals("foo", tag.getName());
 }

 @Test
 public void testTagIsBlock() {
 Tag pTag = new Tag("p");
 assertTrue(pTag.isBlock());

 Tag imgTag = new Tag("img");
 assertFalse(imgTag.isBlock());
 }

 @Test
 public void testTagFormatAsBlock() {
 Tag pTag = new Tag("p");
 assertTrue(pTag.formatAsBlock());

 Tag imgTag = new Tag("img");
 assertFalse(imgTag.formatAsBlock());
 }

 @Test
 public void testTagCanContainBlock() {
 Tag pTag = new Tag("p");
 assertTrue(pTag.canContainBlock()); // alias for isBlock()

 Tag imgTag = new Tag("img");
 assertFalse(imgTag.canContainBlock());
 }

 @Test
 public void testTagIsInline() {
 Tag pTag = new Tag("p");
 assertFalse(pTag.isInline());

 Tag imgTag = new Tag("img");
 assertTrue(imgTag.isInline());
 }

 @Test
 public void testTagIsData() {
 Tag pTag = new Tag("p");
 assertFalse(pTag.isData());

 Tag scriptTag = new Tag("script");
 assertTrue(scriptTag.isData());
 }

 @Test
 public void testTagIsEmpty() {
 Tag imgTag = new Tag("img");
 assertFalse(imgTag.isEmpty());

 Tag imgTagEmpty = new Tag("img").setSelfClosing();
 assertTrue(imgTagEmpty.isEmpty());
 }

 @Test
 public void testTagIsSelfClosing() {
 Tag imgTag = new Tag("img");
 assertFalse(imgTag.isSelfClosing());

 Tag imgTagEmpty = new Tag("img").setSelfClosing();
 assertTrue(imgTagEmpty.isSelfClosing());
 }

 @Test
 public void testTagIsKnownTag() {
 Tag pTag = new Tag("p");
 assertTrue(pTag.isKnownTag());

 Tag fooTag = new Tag("foo");
 assertFalse(fooTag.isKnownTag());
 }

 @Test
 public void testTagPreserveWhitespace() {
 Tag preTag = new Tag("pre");
 assertTrue(preTag.preserveWhitespace());

 Tag pTag = new Tag("p");
 assertFalse(pTag.preserveWhitespace());
 }

 @Test
 public void testTagIsFormListed() {
 Tag inputTag = new Tag("input");
 assertTrue(inputTag.isFormListed());

 Tag hrTag = new Tag("hr");
 assertFalse(hrTag.isFormListed());
 }

 @Test
 public void testTagIsFormSubmittable() {
 Tag inputTag = new Tag("input");
 assertTrue(inputTag.isFormSubmittable());

 Tag hrTag = new Tag("hr");
 assertFalse(hrTag.isFormSubmittable());
 }

 @Test
 public void testTagValueOfNull() {
 assertNull(Tag.valueOf(null));
 }

 @Test
 public void testTagValueOfEmpty() {
 Tag tag = Tag.valueOf("");
 assertFalse(tag.isKnownTag());
 assertNull(tag.getName());
 }
}