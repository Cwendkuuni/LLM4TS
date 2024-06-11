package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

public class TagTest {

 @Test
 public void testTagValues() {
 Tag pTag = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");

 Assert.assertEquals("p", pTag.getName());
 Assert.assertEquals("div", divTag.getName());

 Assert.assertTrue(pTag.isBlock());
 Assert.assertTrue(pTag.formatAsBlock());
 Assert.assertTrue(pTag.canContainInline());
 Assert.assertFalse(pTag.isEmpty());
 Assert.assertFalse(pTag.isSelfClosing());
 Assert.assertFalse(pTag.preserveWhitespace());
 Assert.assertFalse(pTag.isFormListed());
 Assert.assertFalse(pTag.isFormSubmittable());

 Assert.assertTrue(divTag.isBlock());
 Assert.assertTrue(divTag.formatAsBlock());
 Assert.assertTrue(divTag.canContainInline());
 Assert.assertFalse(divTag.isEmpty());
 Assert.assertFalse(divTag.isSelfClosing());
 Assert.assertFalse(divTag.preserveWhitespace());
 Assert.assertFalse(divTag.isFormListed());
 Assert.assertFalse(divTag.isFormSubmittable());
 }

 @Test
 public void testUnknownTagValues() {
 Tag unknownTag = Tag.valueOf("unknown");

 Assert.assertEquals("unknown", unknownTag.getName());

 Assert.assertFalse(unknownTag.isBlock());
 Assert.assertFalse(unknownTag.formatAsBlock());
 Assert.assertFalse(unknownTag.canContainInline());
 Assert.assertFalse(unknownTag.isEmpty());
 Assert.assertFalse(unknownTag.isSelfClosing());
 Assert.assertFalse(unknownTag.preserveWhitespace());
 Assert.assertFalse(unknownTag.isFormListed());
 Assert.assertFalse(unknownTag.isFormSubmittable());
 }

 @Test
 public void testCaseInsensitiveTagValues() {
 Tag pTag = Tag.valueOf("p");
 Tag PTag = Tag.valueOf("P");

 Assert.assertEquals(pTag, PTag);
 }

 @Test
 public void testCaseSensitiveTagValues() {
 Tag pTag = Tag.valueOf("p");
 Tag PTag = Tag.valueOf("P");

 Assert.assertNotEquals(pTag, PTag);
 }

 @Test
 public void testTagProperties() {
 Tag pTag = Tag.valueOf("p");
 pTag.setSelfClosing();

 Assert.assertEquals("p", pTag.getName());
 Assert.assertFalse(pTag.isBlock());
 Assert.assertFalse(pTag.formatAsBlock());
 Assert.assertTrue(pTag.canContainInline());
 Assert.assertFalse(pTag.isEmpty());
 Assert.assertTrue(pTag.isSelfClosing());
 Assert.assertFalse(pTag.preserveWhitespace());
 Assert.assertFalse(pTag.isFormListed());
 Assert.assertFalse(pTag.isFormSubmittable());
 }
}