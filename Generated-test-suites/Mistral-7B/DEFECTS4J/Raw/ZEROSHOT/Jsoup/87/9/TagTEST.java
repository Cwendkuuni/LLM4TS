package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

public class TagTest {

 private static final Tag HTML = Tag.valueOf("html");
 private static final Tag HEAD = Tag.valueOf("head");
 private static final Tag BODY = Tag.valueOf("body");
 private static final Tag P = Tag.valueOf("p");
 private static final Tag DIV = Tag.valueOf("div");
 private static final Tag FORM = Tag.valueOf("form");
 private static final Tag INPUT = Tag.valueOf("input");
 private static final Tag TEXTAREA = Tag.valueOf("textarea");

 @Test
 public void testTagName() {
 HTML.assertEquals("html");
 }

 @Test
 public void testTagNameNormalized() {
 HTML.setValue(ParseSettings.preserveCase).assertEquals("HTML");
 }

 @Test
 public void testIsBlockTag() {
 HTML.assertFalse("html is not a block tag");
 P.assertTrue("p is a block tag");
 }

 @Test
 public void testFormatAsBlock() {
 HTML.assertFalse("html does not format as a block");
 P.assertTrue("p formats as a block by default");
 P.setFormatAsBlock(false).assertFalse("p no longer formats as a block");
 }

 @Test
 public void testCanContainBlock() {
 HTML.assertFalse("html can't contain block tags");
 DIV.assertTrue("div can contain block tags");
 }

 @Test
 public void testIsDataTag() {
 DIV.assertFalse("div is not a data only tag");
 INPUT.assertTrue("input is a data only tag");
 }

 @Test
 public void testIsEmptyTag() {
 INPUT.assertFalse("input is not an empty tag");
 IMG.assertTrue("img is an empty tag");
 }

 @Test
 public void testIsSelfClosingTag() {
 IMG.assertTrue("img is a self-closing tag");
 DIV.assertFalse("div is not a self-closing tag");
 }

 @Test
 public void testIsKnownTag() {
 Tag unknownTag = Tag.valueOf("unknown");
 assertFalse("unknown tag is not known", Tag.isKnownTag("unknown"));
 assertTrue("html tag is known", Tag.isKnownTag("html"));
 }

 @Test
 public void testPreserveWhitespace() {
 P.assertFalse("p does not preserve whitespace by default");
 Pre.assertTrue("pre preserves whitespace");
 }

 @Test
 public void testIsFormListedTag() {
 Input.assertFalse("input is not form listed by default");
 Output.assertTrue("output is form listed");
 }

 @Test
 public void testIsFormSubmittableTag() {
 Button.assertFalse("button is not form submittable by default");
 Input.assertTrue("input is form submittable");
 }

 @Test
 public void testEquals() {
 Tag tag1 = new Tag("p");
 Tag tag2 = new Tag("p");
 assertTrue("Tags with the same name are equal", tag1.equals(tag2));

 Tag tag3 = new Tag("div");
 assertFalse("Tags with different names are not equal", tag1.equals(tag3));
 }

 @Test
 public void testHashCode() {
 Tag tag1 = new Tag("p");
 Tag tag2 = new Tag("p");
 int hash1 = tag1.hashCode();
 int hash2 = tag2.hashCode();
 assertEquals("Tags with the same name have the same hash code", hash1, hash2);

 Tag tag3 = new Tag("div");
 int hash3 = tag3.hashCode();
 assertNotEquals("Tags with different names have different hash codes", hash1, hash3);
 }
}