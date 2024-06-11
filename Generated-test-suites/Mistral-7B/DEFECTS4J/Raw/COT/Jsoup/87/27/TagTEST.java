package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

public class TagTest {

 private static final Tag HTML = Tag.valueOf("html");
 private static final Tag DIV = Tag.valueOf("div");
 private static final Tag P = Tag.valueOf("p");
 private static final Tag INPUT = Tag.valueOf("input");

 @Test
 public void testTagName() {
 HTML.assertEquals("html");
 DIV.assertEquals("div");
 P.assertEquals("p");
 INPUT.assertEquals("input");
 }

 @Test
 public void testGetName() {
 HTML.getName().assertEquals("html");
 DIV.getName().assertEquals("div");
 P.getName().assertEquals("p");
 INPUT.getName().assertEquals("input");
 }

 @Test
 public void testValueOfWithNormalizedTagName() {
 Tag tag = Tag.valueOf("P", ParseSettings.preserveCase);
 tag.assertEquals(P);
 }

 @Test
 public void testValueOfWithUnknownTagName() {
 Tag tag = Tag.valueOf("UNKNOWN_TAG", ParseSettings.preserveCase);
 tag.assertNotEquals(P);
 }

 @Test
 public void testIsBlock() {
 assertTrue(HTML.isBlock());
 assertTrue(P.isBlock());
 assertFalse(INPUT.isBlock());
 }

 @Test
 public void testFormatAsBlock() {
 assertTrue(HTML.formatAsBlock());
 assertTrue(P.formatAsBlock());
 assertFalse(INPUT.formatAsBlock());
 }

 @Test
 public void testCanContainBlock() {
 // deprecated and no different result than isBlock()
 assertTrue(P.isBlock());
 }

 @Test
 public void testIsInline() {
 assertFalse(HTML.isInline());
 assertFalse(P.isInline());
 assertTrue(INPUT.isInline());
 }

 @Test
 public void testIsData() {
 assertFalse(P.isData());
 assertFalse(INPUT.isData());
 assertTrue(Tag.valueOf("script").isData());
 }

 @Test
 public void testIsEmpty() {
 assertFalse(P.isEmpty());
 assertFalse(INPUT.isEmpty());
 assertTrue(Tag.valueOf("img").isEmpty());
 }

 @Test
 public void testIsSelfClosing() {
 assertFalse(P.isSelfClosing());
 assertTrue(Tag.valueOf("img").isSelfClosing());
 }

 @Test
 public void testIsKnownTag() {
 assertTrue(Tag.isKnownTag("html"));
 assertTrue(Tag.isKnownTag("p"));
 assertFalse(Tag.isKnownTag("UNKNOWN_TAG"));
 }

 @Test
 public void testPreserveWhitespace() {
 assertFalse(P.preserveWhitespace());
 assertTrue(Tag.valueOf("pre").preserveWhitespace());
 }

 @Test
 public void testIsFormListed() {
 assertFalse(P.isFormListed());
 assertTrue(INPUT.isFormListed());
 }

 @Test
 public void testIsFormSubmittable() {
 assertFalse(P.isFormSubmittable());
 assertTrue(INPUT.isFormSubmittable());
 }

 @Test
 public void testEquals() {
 Tag tag1 = new Tag("p");
 Tag tag2 = new Tag("p");
 Tag tag3 = new Tag("div");
 assertTrue(tag1.equals(tag1));
 assertTrue(tag1.equals(tag2));
 assertFalse(tag1.equals(tag3));
 }

 @Test
 public void testSetSelfClosing() {
 Tag tag = new Tag("img");
 tag.setSelfClosing();
 assertTrue(tag.isSelfClosing());
 }

 @Test(expected = NullPointerException.class)
 public void testNullObjectEquals() {
 Tag tag = new Tag("p");
 assertFalse(tag.equals(null));
 }

 @Test
 public void testHashCode() {
 Tag tag1 = new Tag("p");
 Tag tag2 = new Tag("p");
 Tag tag3 = new Tag("div");
 int hashCode1 = tag1.hashCode();
 int hashCode2 = tag2.hashCode();
 int hashCode3 = tag3.hashCode();
 assertEquals(hashCode1, hashCode2);
 assertNotEquals(hashCode1, hashCode3);
 }

 @Test
 public void testToString() {
 assertEquals("p", HTML.toString());
 assertEquals("div", DIV.toString());
 }

 @Test
 public void testBlockTags() {
 for (String tagName : Tag.blockTags) {
 Tag tag = Tag.valueOf(tagName);
 assertTrue(tag.isBlock());
 }
 }

 @Test
 public void testInlineTags() {
 for (String tagName : Tag.inlineTags) {
 Tag tag = Tag.valueOf(tagName);
 assertFalse(tag.isBlock());
 }
 }

 @Test
 public void testEmptyTags() {
 for (String tagName : Tag.emptyTags) {
 Tag tag = Tag.valueOf(tagName);
 assertTrue(tag.isEmpty());
 }
 }

 @Test
 public void testFormatAsInlineTags() {
 for (String tagName : Tag.formatAsInlineTags) {
 Tag tag = Tag.valueOf(tagName);
 assertFalse(tag.formatAsBlock());
 }
 }

 @Test
 public void testPreserveWhitespaceTags() {
 for (String tagName : Tag.preserveWhitespaceTags) {
 Tag tag = Tag.valueOf(tagName);
 assertTrue(tag.preserveWhitespace());
 }
 }

 @Test
 public void testFormListedTags() {
 for (String tagName : Tag.formListedTags) {
 Tag tag = Tag.valueOf(tagName);
 assertTrue(tag.isFormListed());
 }
 }

 @Test
 public void testFormSubmitTags() {
 for (String tagName : Tag.formSubmitTags) {
 Tag tag = Tag.valueOf(tagName);
 assertTrue(tag.isFormSubmittable());
 }
 }
}