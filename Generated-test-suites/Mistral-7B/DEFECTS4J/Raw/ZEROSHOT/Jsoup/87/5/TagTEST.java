package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class TagTest {

 private static final Tag P = Tag.valueOf("p");
 private static final Tag DIV = Tag.valueOf("div");
 private static final Tag IMG = Tag.valueOf("img");
 private static final Tag SCRIPT = Tag.valueOf("script");
 private static final Tag FORM = Tag.valueOf("form");
 private static final Tag INPUT = Tag.valueOf("input");

 @Test
 public void testTagName() {
 Tag tag = new Tag("testTag");
 Assert.assertEquals("testTag", tag.getName());
 }

 @Test
 public void testValueOf() {
 Assert.assertEquals(P, Tag.valueOf("p"));
 Assert.assertEquals(DIV, Tag.valueOf("div"));
 Assert.assertEquals(IMG, Tag.getValueOf("img"));
 Assert.assertEquals(SCRIPT, Tag.getValueOf("script"));
 Assert.assertEquals(FORM, Tag.getValueOf("form"));
 }

 @Test
 public void testValueOfCaseInsensitive() {
 Assert.assertEquals(P, Tag.valueOf("P"));
 }

 @Test
 public void testIsBlock() {
 Assert.assertTrue(P.isBlock());
 Assert.assertFalse(INPUT.isBlock());
 }

 @Test
 public void testFormatAsBlock() {
 Assert.assertTrue(P.formatAsBlock());
 Assert.assertFalse(INPUT.formatAsBlock());
 }

 @Test
 public void testCanContainBlock() {
 Assert.assertTrue(P.canContainBlock());
 }

 @Test
 public void testIsInline() {
 Assert.assertFalse(P.isInline());
 Assert.assertTrue(INPUT.isInline());
 }

 @Test
 public void testIsData() {
 Assert.assertFalse(P.isData());
 Assert.assertTrue(SCRIPT.isData());
 }

 @Test
 public void testIsEmpty() {
 Assert.assertFalse(P.isEmpty());
 Assert.assertTrue(IMG.isEmpty());
 }

 @Test
 public void testIsSelfClosing() {
 Assert.assertFalse(P.isSelfClosing());
 Assert.assertTrue(IMG.isSelfClosing());
 }

 @Test
 public void testIsKnownTag() {
 Assert.assertTrue(Tag.isKnownTag("p"));
 Assert.assertFalse(Tag.isKnownTag("testTag"));
 }

 @Test
 public void testPreserveWhitespace() {
 Assert.assertFalse(P.preserveWhitespace());
 Assert.assertTrue(SCRIPT.preserveWhitespace());
 }

 @Test
 public void testIsFormListed() {
 Assert.assertFalse(P.isFormListed());
 Assert.assertTrue(INPUT.isFormListed());
 }

 @Test
 public void testIsFormSubmittable() {
 Assert.assertFalse(INPUT.isFormSubmittable());
 Assert.assertTrue(INPUT.setSelfClosing().isFormSubmittable());
 }

 @Test
 public void testHashCodeAndEquals() {
 Tag tag1 = new Tag("p");
 Tag tag2 = new Tag("p");
 Tag tag3 = new Tag("div");

 Assert.assertEquals(tag1, tag1);
 Assert.assertEquals(tag1.hashCode(), tag1.hashCode());
 Assert.assertEquals(tag1, tag2);
 Assert.assertEquals(tag1.hashCode(), tag2.hashCode());
 Assert.assertNotEquals(tag1, tag3);
 Assert.assertNotEquals(tag1.hashCode(), tag3.hashCode());
 }
}