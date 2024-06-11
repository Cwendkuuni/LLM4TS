package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.parser.Tag;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

/**
 * Tag Test Cases
 *
 * @author XYZ
 */
public class TagTest {

 private static final Tag HTML = Tag.valueOf("html");
 private static final Tag HEAD = Tag.valueOf("head");
 private static final Tag BODY = Tag.valueOf("body");
 private static final Tag P = Tag.valueOf("p");
 private static final Tag DIV = Tag.valueOf("div");

 @Test
 public void testValues() {
 Tag html = Tag.valueOf("HTML");
 Tag head = Tag.valueOf("HEAD");
 Tag body = Tag.valueOf("BODY");
 Tag p = Tag.valueOf("p");
 Tag div = Tag.valueOf("DIV");

 assertEquals(HTML, html);
 assertEquals(HEAD, head);
 assertEquals(BODY, body);
 assertEquals(P, p);
 assertEquals(DIV, div);
 }

 @Test
 public void testValueOfWithNormalizeTagAndEmptyTag() {
 Tag pTag = Tag.valueOf("p");
 Tag emptyPTag = Tag.valueOf("p", ParseSettings.emptyElementTagMode);

 assertEquals(P, pTag);
 assertEquals(Tag.valueOf("p"), emptyPTag);
 }

 @Test
 public void testValueOfWithUnknownTag() {
 Tag unknownTag = Tag.valueOf("UNKNOWN");

 assertNotEquals(HTML, unknownTag);
 assertEquals(unknownTag.tagName, "UNKNOWN");
 }

 @Test
 public void testIsBlockAndFormatAsBlock() {
 assertTrue(HTML.isBlock());
 assertTrue(HTML.formatAsBlock());

 assertFalse(P.isBlock());
 assertFalse(P.formatAsBlock());

 assertTrue(DIV.isBlock());
 assertTrue(DIV.formatAsBlock());
 }

 @Test
 public void testIsDataTag() {
 assertFalse(P.isData());
 assertFalse(DIV.isData());

 assertTrue(Tag.valueOf("script").isData());
 assertTrue(Tag.valueOf("style").isData());
 }

 @Test
 public void testIsEmptyTag() {
 assertFalse(P.isEmpty());
 assertFalse(DIV.isEmpty());

 assertTrue(Tag.valueOf("img").isEmpty());
 assertTrue(Tag.valueOf("br").isEmpty());
 }

 @Test
 public void testIsSelfClosing() {
 assertFalse(P.isSelfClosing());
 assertFalse(DIV.isSelfClosing());

 assertTrue(Tag.valueOf("img").isSelfClosing());
 assertTrue(Tag.valueOf("br").isSelfClosing());
 }

 @Test
 public void testIsKnownTag() {
 assertTrue(Tag.isKnownTag("html"));
 assertFalse(Tag.isKnownTag("UNKNOWN"));
 }

 @Test
 public void testPreserveWhitespace() {
 assertFalse(P.preserveWhitespace());
 assertFalse(DIV.preserveWhitespace());

 assertTrue(Tag.valueOf("pre").preserveWhitespace());
 assertTrue(Tag.valueOf("textarea").preserveWhitespace());
 }

 @Test
 public void testIsFormListed() {
 assertFalse(P.isFormListed());
 assertFalse(DIV.isFormListed());

 assertTrue(Tag.valueOf("button").isFormListed());
 assertTrue(Tag.valueOf("textarea").isFormListed());
 }

 @Test
 public void testIsFormSubmittable() {
 assertFalse(Tag.valueOf("button").isFormSubmittable());
 assertFalse(Tag.valueOf("textarea").isFormSubmittable());

 assertTrue(Tag.valueOf("input").isFormSubmittable());
 assertTrue(Tag.valueOf("select").isFormSubmittable());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidTagName() {
 Tag tag = Tag.valueOf("", ParseSettings.preserveCase);
 fail("IllegalArgumentException expected");
 }

 @Test
 public void testEquals() {
 Tag p1 = Tag.valueOf("p");
 Tag p2 = Tag.valueOf("p");
 Tag div1 = Tag.valueOf("div");
 Tag div2 = Tag.valueOf("div");
 Tag sameTag = p1;

 assertEquals(p1, p2);
 assertEquals(p1.hashCode(), p2.hashCode());

 assertNotEquals(p1, div1);
 assertNotEquals(p1.hashCode(), div1.hashCode());

 assertEquals(sameTag, p1);
 assertEquals(p1.hashCode(), sameTag.hashCode());
 }

 @Test
 public void testToString() {
 assertEquals(HTML.toString(), "html");
 assertEquals(P.toString(), "p");
 assertEquals(DIV.toString(), "div");
 }

 private static Map<String, Tag> tagsCopy = new HashMap<>(Tag.tags);

 @Test
 public void testStaticTags() {
 validateStaticBlockTags();
 validateStaticInlineTags();
 validateStaticEmptyTags();
 validateStaticFormatAsInlineTags();
 validateStaticPreserveWhitespaceTags();
 validateStaticFormListedTags();
 validateStaticFormSubmitTags();
 }

 private void validateStaticBlockTags() {
 for (String tagName : Tag.blockTags) {
 Tag tag = Tag.valueOf(tagName);
 assertTrue(tag.isBlock());
 assertTrue(tag.formatAsBlock());
 }
 }

 private void validateStaticInlineTags() {
 for (String tagName : Tag.inlineTags) {
 Tag tag = Tag.valueOf(tagName);
 assertFalse(tag.isBlock());
 assertFalse(tag.formatAsBlock());
 }
 }

 private void validateStaticEmptyTags() {
 for (String tagName : Tag.emptyTags) {
 Tag tag = Tag.valueOf(tagName);
 assertFalse(tag.canContainInline);
 assertTrue(tag.isEmpty());
 }
 }

 private void validateStaticFormatAsInlineTags() {
 for (String tagName : Tag.formatAsInlineTags) {
 Tag tag = Tag.valueOf(tagName);
 assertFalse(tag.formatAsBlock);
 }
 }

 private void validateStaticPreserveWhitespaceTags() {
 for (String tagName : Tag.preserveWhitespaceTags) {
 Tag tag = Tag.valueOf(tagName);
 assertTrue(tag.preserveWhitespace);
 }
 }

 private void validateStaticFormListedTags() {
 for (String tagName : Tag.formListedTags) {
 Tag tag = Tag.valueOf(tagName);
 assertTrue(tag.formList);
 }
 }

 private void validateStaticFormSubmitTags() {
 for (String tagName : Tag.formSubmitTags) {
 Tag tag = Tag.valueOf(tagName);
 assertTrue(tag.formSubmit);
 }
 }
}