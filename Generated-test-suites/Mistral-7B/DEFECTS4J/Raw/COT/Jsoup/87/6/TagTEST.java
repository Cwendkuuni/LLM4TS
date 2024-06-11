package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.Map;

public class TagTest {

 private static final Tag HTML = Tag.valueOf("html");
 private static final Tag HEAD = Tag.valueOf("head");
 private static final Tag BODY = Tag.valueOf("body");
 private static final Tag P = Tag.valueOf("p");
 private static final Tag IMG = Tag.valueOf("img");
 private static final Tag IFRAME = Tag.valueOf("iframe");

 @Test
 public void testTagName() {
 Tag tag = new Tag("testTag");
 assertEquals("testTag", tag.getName());
 }

 @Test
 public void testTagNormalization() {
 Tag tag = Tag.valueOf("p");
 assertEquals("p", tag.getName());
 }

 @Test
 public void testTagValueOfWithNull() {
 assertNull(Tag.valueOf(null));
 }

 @Test
 public void testTagValueOfWithEmpty() {
 assertNull(Tag.valueOf(""));
 }

 @Test
 public void testTagValueOfUnknown() {
 Tag tag = Tag.valueOf("unknownTag");
 assertEquals("unknownTag", tag.getName());
 }

 @Test
 public void testTagIsBlock() {
 assertEquals(true, HTML.isBlock());
 assertEquals(true, HEAD.isBlock());
 assertEquals(true, BODY.isBlock());
 assertEquals(true, P.isBlock());
 assertFalse(IMG.isBlock());
 assertFalse(IFRAME.isBlock());
 }

 @Test
 public void testTagFormatAsBlock() {
 assertEquals(true, HTML.formatAsBlock());
 assertEquals(true, HEAD.formatAsBlock());
 assertEquals(true, BODY.formatAsBlock());
 assertEquals(true, P.formatAsBlock());
 assertFalse(IMG.formatAsBlock());
 }

 @Test
 public void testTagCanContainBlock() {
 assertEquals(true, HTML.canContainBlock());
 assertEquals(true, HEAD.canContainBlock());
 assertEquals(true, BODY.canContainBlock());
 assertEquals(true, P.canContainBlock());
 }

 @Test
 public void testTagIsInline() {
 assertFalse(HTML.isInline());
 assertFalse(HEAD.isInline());
 assertFalse(BODY.isInline());
 assertFalse(P.isInline());
 assertTrue(IMG.isInline());
 assertTrue(IFRAME.isInline());
 }

 @Test
 public void testTagIsData() {
 assertFalse(HTML.isData());
 assertFalse(HEAD.isData());
 assertFalse(BODY.isData());
 assertFalse(P.isData());
 assertTrue(IMG.isData());
 }

 @Test
 public void testTagIsEmpty() {
 assertFalse(HTML.isEmpty());
 assertFalse(HEAD.isEmpty());
 assertFalse(BODY.isEmpty());
 assertFalse(P.isEmpty());
 assertTrue(IMG.isEmpty());
 }

 @Test
 public void testTagIsSelfClosing() {
 assertFalse(HTML.isSelfClosing());
 assertFalse(HEAD.isSelfClosing());
 assertFalse(BODY.isSelfClosing());
 assertFalse(P.isSelfClosing());
 assertTrue(IMG.isSelfClosing());
 }

 @Test
 public void testTagIsKnownTag() {
 assertTrue(Tag.isKnownTag("html"));
 }

 @Test
 public void testTagIsNotKnownTag() {
 assertFalse(Tag.isKnownTag(""));
 assertFalse(Tag.isKnownTag("unknownTag"));
 }

 @Test
 public void testTagPreserveWhitespace() {
 assertFalse(HTML.preserveWhitespace());
 assertFalse(HEAD.preserveWhitespace());
 assertFalse(BODY.preserveWhitespace());
 assertFalse(P.preserveWhitespace());
 assertTrue(Tag.valueOf("pre").preserveWhitespace());
 }

 @Test
 public void testTagIsFormListed() {
 assertFalse(HTML.isFormListed());
 assertFalse(HEAD.isFormListed());
 assertFalse(BODY.isFormListed());
 assertFalse(P.isFormListed());
 assertTrue(Tag.valueOf("button").isFormListed());
 }

 @Test
 public void testTagIsFormSubmittable() {
 assertFalse(HTML.isFormSubmittable());
 assertFalse(HEAD.isFormSubmittable());
 assertFalse(BODY.isFormSubmittable());
 assertFalse(P.isFormSubmittable());
 assertTrue(Tag.valueOf("input").isFormSubmittable());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTagValueOfWithInvalidTagName() {
 Tag.valueOf("<html>");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTagValueOfWithEmptyTagName() {
 Tag.valueOf(" ");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTagValueOfWithInvalidSettings() {
 Tag.valueOf("p", null);
 }

 @Test
 public void testTagValueOfWithNullSettings() {
 Tag tag = Tag.valueOf("p", null);
 assertEquals("p", tag.getName());
 assertFalse(tag.isBlock());
 assertFalse(tag.formatAsBlock());
 assertFalse(tag.canContainInline());
 assertFalse(tag.isEmpty());
 assertFalse(tag.selfClosing);
 assertFalse(tag.preserveWhitespace());
 assertFalse(tag.formList);
 assertFalse(tag.formSubmit);
 }
}