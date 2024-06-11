package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

public class TagTest {

 private static final String TAG_NAME_A = "a";
 private static final String TAG_NAME_P = "p";
 private static final String TAG_NAME_DIV = "div";
 private static final String TAG_NAME_SPAN = "span";
 private static final String TAG_NAME_IMG = "img";
 private static final String TAG_NAME_FORM = "form";
 private static final String TAG_NAME_INPUT = "input";
 private static final String TAG_NAME_BUTTON = "button";

 @Test
 public void testTagName() {
 Tag tagA = new Tag(TAG_NAME_A);
 Tag tagP = new Tag(TAG_NAME_P);
 Tag tagDiv = new Tag(TAG_NAME_DIV);
 Tag tagSpan = new Tag(TAG_NAME_SPAN);
 Tag tagImg = new Tag(TAG_NAME_IMG);
 Tag tagForm = new Tag(TAG_NAME_FORM);
 Tag tagInput = new Tag(TAG_NAME_INPUT);
 Tag tagButton = new Tag(TAG_NAME_BUTTON);

 assertEquals(TAG_NAME_A, tagA.getName());
 assertEquals(TAG_NAME_P, tagP.getName());
 assertEquals(TAG_NAME_DIV, tagDiv.getName());
 assertEquals(TAG_NAME_SPAN, tagSpan.getName());
 assertEquals(TAG_NAME_IMG, tagImg.getName());
 assertEquals(TAG_NAME_FORM, tagForm.getName());
 assertEquals(TAG_NAME_INPUT, tagInput.getName());
 assertEquals(TAG_NAME_BUTTON, tagButton.getName());
 }

 @Test
 public void testTagIsBlock() {
 Tag tagP = new Tag(TAG_NAME_P);
 Tag tagA = new Tag(TAG_NAME_A);
 Tag tagSpan = new Tag(TAG_NAME_SPAN);

 assertTrue(tagP.isBlock());
 assertFalse(tagA.isBlock());
 assertFalse(tagSpan.isBlock());
 }

 @Test
 public void testTagFormatAsBlock() {
 Tag tagP = new Tag(TAG_NAME_P);
 Tag tagA = new Tag(TAG_NAME_A);
 Tag tagSpan = new Tag(TAG_NAME_SPAN);

 assertTrue(tagP.formatAsBlock());
 assertFalse(tagA.formatAsBlock());
 assertFalse(tagSpan.formatAsBlock());
 }

 @Test
 public void testTagIsData() {
 Tag tagSpan = new Tag(TAG_NAME_SPAN);
 Tag tagP = new Tag(TAG_NAME_P);

 assertFalse(tagSpan.isData());
 assertTrue(tagP.isData());
 }

 @Test
 public void testTagIsEmpty() {
 Tag tagImg = new Tag(TAG_NAME_IMG);
 Tag tagInput = new Tag(TAG_NAME_INPUT);
 Tag tagButton = new Tag(TAG_NAME_BUTTON);

 assertFalse(tagImg.isEmpty());
 assertFalse(tagInput.isEmpty());
 assertTrue(tagButton.isEmpty());
 }

 @Test
 public void testTagIsSelfClosing() {
 Tag tagImg = new Tag(TAG_NAME_IMG);
 Tag tagInput = new Tag(TAG_NAME_INPUT);
 Tag tagButton = new Tag(TAG_NAME_BUTTON);

 assertFalse(tagImg.isSelfClosing());
 assertFalse(tagInput.isSelfClosing());
 assertFalse(tagButton.isSelfClosing());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testThrowIllegalArgumentExceptionOnUnknownTag() {
 Tag tagUnknown = Tag.valueOf("unknown");
 }

 @Test
 public void testTagIsKnownTag() {
 Tag tagP = new Tag(TAG_NAME_P);
 Tag tagA = new Tag(TAG_NAME_A);

 assertTrue(Tag.isKnownTag(TAG_NAME_P));
 assertTrue(Tag.isKnownTag(TAG_NAME_A));
 }

 @Test
 public void testTagPreserveWhitespace() {
 Tag tagPre = new Tag(Tag.Tags.PRE.name());
 Tag tagTextarea = new Tag(Tag.Tags.TEXTAREA.name());

 assertTrue(tagPre.preserveWhitespace());
 assertTrue(tagTextarea.preserveWhitespace());
 }

 @Test
 public void testTagIsFormListed() {
 Tag tagButton = new Tag(TAG_NAME_BUTTON);
 Tag tagInput = new Tag(TAG_NAME_INPUT);

 assertTrue(tagInput.isFormListed());
 assertTrue(tagButton.isFormListed());
 }

 @Test
 public void testTagIsFormSubmittable() {
 Tag tagButton = new Tag(TAG_NAME_BUTTON);
 Tag tagInput = new Tag(TAG_NAME_INPUT);

 assertTrue(tagButton.isFormSubmittable());
 assertTrue(tagInput.isFormSubmittable());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testThrowIllegalArgumentExceptionOnNullTagName() {
 Tag tagNull = Tag.valueOf(null);
 }

 @Test
 public void testTagEquals() {
 Tag tagP1 = new Tag(TAG_NAME_P);
 Tag tagP2 = new Tag(TAG_NAME_P);
 Tag tagA = new Tag(TAG_NAME_A);

 assertEquals(tagP1, tagP2);
 assertNotEquals(tagP1, tagA);
 }

 @Test
 public void testTagHashCode() {
 Tag tagP1 = new Tag(TAG_NAME_P);
 Tag tagP2 = new Tag(TAG_NAME_P);
 Tag tagA = new Tag(TAG_NAME_A);

 int hashCodeP1 = tagP1.hashCode();
 int hashCodeP2 = tagP2.hashCode();
 int hashCodeA = tagA.hashCode();

 assertEquals(hashCodeP1, hashCodeP2);
 assertNotEquals(hashCodeP1, hashCodeA);
 }

 @Test
 public void testToString() {
 Tag tagP = new Tag(TAG_NAME_P);
 Tag tagA = new Tag(TAG_NAME_A);

 assertEquals("p", tagP.toString());
 assertEquals("a", tagA.toString());
 }
}