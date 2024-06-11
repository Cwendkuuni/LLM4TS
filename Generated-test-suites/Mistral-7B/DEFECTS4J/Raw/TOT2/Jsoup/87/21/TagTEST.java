package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class TagTest {

 @Test
 public void testValueOfWithPredefinedTag() {
 Tag tag = Tag.valueOf("p");
 assertTrue(tag.isKnownTag());
 assertEquals("p", tag.getName());
 }

 @Test
 public void testValueOfWithUnknownTag() {
 Tag tag = Tag.getValueOf("mytag");
 assertFalse(tag.isKnownTag());
 assertEquals("mytag", tag.getName());
 }

 @Test
 public void testValueOfWithCaseSensitiveTag() {
 Tag tag = Tag.valueOf("myTag");
 assertFalse(tag.isKnownTag());
 assertEquals("myTag", tag.getName());
 }

 @Test
 public void testIsBlockForBlockTag() {
 Tag tag = Tag.valueOf("p");
 assertTrue(tag.isBlock());
 }

 @Test
 public void testIsBlockForInlineTag() {
 Tag tag = Tag.valueOf("span");
 assertFalse(tag.isBlock());
 }

 @Test
 public void testIsDataForDataOnlyTag() {
 Tag tag = Tag.valueOf("script");
 assertTrue(tag.isData());
 }

 @Test
 public void testIsEmptyForEmptyTag() {
 Tag tag = Tag.valueOf("img");
 assertTrue(tag.isEmpty());
 }

 @Test
 public void testIsSelfClosingForEmptySelfClosingTag() {
 Tag tag = Tag.valueOf("img");
 assertTrue(tag.isSelfClosing());
 }

 @Test
 public void testIsSelfClosingForNonEmptySelfClosingTag() {
 Tag tag = Tag.valueOf("input");
 assertFalse(tag.isSelfClosing());
 }

 @Test
 public void testIsFormListedForFormListedTag() {
 Tag tag = Tag.valueOf("input");
 assertTrue(tag.isFormListed());
 }

 @Test
 public void testIsFormSubmittableForFormSubmittableTag() {
 Tag tag = Tag.valueOf("input");
 assertTrue(tag.isFormSubmittable());
 }

 @Test(expected = NullPointerException.class)
 public void testValueOfWithNullTagName() {
 Tag tag = Tag.valueOf(null);
 fail("Expected NullPointerException, but got: " + tag);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testValueOfWithEmptyTagName() {
 Tag tag = Tag.valueOf("");
 fail("Expected IllegalArgumentException, but got: " + tag);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testValueOfWithInvalidTagName() {
 Tag tag = Tag.valueOf("&tag;");
 fail("Expected IllegalArgumentException, but got: " + tag);
 }
}