package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class TagTest {

 private static final String TAG_NAME_P = "p";
 private static final String TAG_NAME_DIV = "div";
 private static final String TAG_NAME_IMG = "img";
 private static final String TAG_NAME_TABLE = "table";
 private static final String TAG_NAME_BR = "br";
 private static final String TAG_NAME_SCRIPT = "script";
 private static final String TAG_NAME_UNKNOWN = "unknown";

 @Test
 public void testTag_isBlock() {
 Tag tagP = Tag.valueOf(TAG_NAME_P);
 Tag tagDiv = Tag.valueOf(TAG_NAME_DIV);
 Assert.assertTrue(tagP.isBlock());
 Assert.assertTrue(tagDiv.isBlock());
 }

 @Test
 public void testTag_formatAsBlock() {
 Tag tagP = Tag.valueOf(TAG_NAME_P);
 Tag tagBr = Tag.valueOf(TAG_NAME_BR);
 Assert.assertTrue(tagP.formatAsBlock());
 Assert.assertFalse(tagBr.formatAsBlock());
 }

 @Test
 public void testTag_canContainBlock() {
 Tag tagP = Tag.valueOf(TAG_NAME_P);
 Tag tagDiv = Tag.valueOf(TAG_NAME_DIV);
 Tag tagImg = Tag.valueOf(TAG_NAME_IMG);
 Tag tagScript = Tag.valueOf(TAG_NAME_SCRIPT);
 Assert.assertTrue(tagP.canContainBlock());
 Assert.assertTrue(tagDiv.canContainBlock());
 Assert.assertFalse(tagImg.canContainBlock());
 Assert.assertFalse(tagScript.canContainBlock());
 }

 @Test
 public void testTag_isInline() {
 Tag tagP = Tag.valueOf(TAG_NAME_P);
 Tag tagBr = Tag.valueOf(TAG_NAME_BR);
 Tag tagImg = Tag.valueOf(TAG_NAME_IMG);
 Tag tagScript = Tag.valueOf(TAG_NAME_SCRIPT);
 Assert.assertFalse(tagBr.isInline());
 Assert.assertFalse(tagP.isInline());
 Assert.assertTrue(tagImg.isInline());
 Assert.assertTrue(tagScript.isInline());
 }

 @Test
 public void testTag_isData() {
 Tag tagDiv = Tag.valueOf(TAG_NAME_DIV);
 Tag tagP = Tag.valueOf(TAG_NAME_P);
 Tag tagImg = Tag.valueOf(TAG_NAME_IMG);
 Tag tagScript = Tag.valueOf(TAG_NAME_SCRIPT);
 Assert.assertFalse(tagDiv.isData());
 Assert.assertFalse(tagP.isData());
 Assert.assertFalse(tagImg.isData());
 Assert.assertTrue(tagScript.isData());
 }

 @Test
 public void testTag_isKnownTag() {
 Tag tagP = Tag.valueOf(TAG_NAME_P);
 Tag tagDiv = Tag.valueOf(TAG_NAME_DIV);
 Tag tagUnknown = Tag.valueOf(TAG_NAME_UNKNOWN);
 Assert.assertTrue(Tag.isKnownTag(TAG_NAME_P));
 Assert.assertTrue(Tag.isKnownTag(TAG_NAME_DIV));
 Assert.assertFalse(Tag.isKnownTag(TAG_NAME_UNKNOWN));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTag_valueOfWithNullName() {
 Tag tag = Tag.valueOf(null);
 }

 @Test
 public void testTag_preserveWhitespace() {
 Tag tagP = Tag.valueOf(TAG_NAME_P);
 Tag tagPre = Tag.valueOf("pre");
 Assert.assertFalse(tagP.preserveWhitespace());
 Assert.assertTrue(tagPre.preserveWhitespace());
 }

 @Test
 public void testTag_toString() {
 Tag tagP = Tag.valueOf(TAG_NAME_P);
 Tag tagDiv = Tag.valueOf(TAG_NAME_DIV);
 Assert.assertEquals("p", tagP.toString());
 Assert.assertEquals("div", tagDiv.toString());
 }

 @Test
 public void testTag_isSelfClosing() {
 Tag tagImg = Tag.valueOf(TAG_NAME_IMG);
 Tag tagP = Tag.valueOf(TAG_NAME_P);
 Tag tagInput = Tag.valueOf("input");
 Tag tagBr = Tag.valueOf(TAG_NAME_BR);
 Assert.assertFalse(tagP.isSelfClosing());
 Assert.assertFalse(tagImg.isSelfClosing());
 Assert.assertFalse(tagInput.isSelfClosing());
 Assert.assertFalse(tagBr.isSelfClosing());
 }

 @Test
 public void testTag_setValueToSelfClosing() {
 Tag tagUnknown = Tag.valueOf(TAG_NAME_UNKNOWN);
 Tag tagUnknownSelfClosing = tagUnknown.setSelfClosing();
 Assert.assertTrue(tagUnknownSelfClosing.isSelfClosing());
 }

 @Test
 public void testTag_hashCode() {
 Tag tagP = Tag.valueOf(TAG_NAME_P);
 Tag tagDiv = Tag.valueOf(TAG_NAME_DIV);
 Tag tagP2 = Tag.valueOf(TAG_NAME_P);
 Tag tagDiv2 = Tag.valueOf(TAG_NAME_DIV);
 Assert.assertEquals(tagP.hashCode(), tagP2.hashCode());
 Assert.assertEquals(tagDiv.hashCode(), tagDiv2.hashCode());
 }

 @Test
 public void testTag_equals() {
 Tag tagP = Tag.valueOf(TAG_NAME_P);
 Tag tagDiv = Tag.valueOf(TAG_NAME_DIV);
 Tag tagP2 = Tag.valueOf(TAG_NAME_P);
 Tag tagDiv2 = Tag.valueOf(TAG_NAME_DIV);
 Tag tagUnknown = Tag.valueOf(TAG_NAME_UNKNOWN);
 Assert.assertTrue(tagP.equals(tagP2));
 Assert.assertTrue(tagDiv.equals(tagDiv2));
 Assert.assertFalse(tagP.equals(tagDiv));
 Assert.assertFalse(tagP.equals(tagUnknown));
 }
}