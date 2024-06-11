package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

public class TagTest {

 @Test
 public void testTagValues() {
 Tag pTag = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");

 // Test known tags
 Assert.assertEquals("p", pTag.getName());
 Assert.assertEquals("div", divTag.getName());
 Assert.assertTrue(Tag.isKnownTag("p"));
 Assert.assertTrue(Tag.isKnownTag("div"));

 // Test unknown tags
 Tag unknownTag = Tag.valueOf("unknownTag");
 Assert.assertNotEquals("unknownTag", unknownTag.getName());
 Assert.assertFalse(Tag.isKnownTag("unknownTag"));
 Assert.assertEquals(unknownTag, Tag.valueOf("unknownTag", ParseSettings.preserveCase));

 // Test case sensitivity
 Assert.assertNotEquals("P", pTag);
 Assert.assertNotEquals("p", Tag.valueOf("P"));

 // Test generic tag
 Tag genericTag = Tag.valueOf("genericTag");
 Assert.assertFalse(genericTag.isBlock());
 Assert.assertFalse(genericTag.formatAsBlock());
 Assert.assertTrue(genericTag.canContainInline());
 Assert.assertFalse(genericTag.empty());
 Assert.assertFalse(genericTag.selfClosing());
 Assert.assertFalse(genericTag.preserveWhitespace());
 Assert.assertFalse(genericTag.formList());
 Assert.assertFalse(genericTag.formSubmit());
 }

 @Test
 public void testTagProperties() {
 Tag pTag = Tag.valueOf("p");

 // Test isBlock()
 Assert.assertTrue(pTag.isBlock());
 // Test formatAsBlock()
 Assert.assertTrue(pTag.formatAsBlock());
 // Test canContainBlock()
 Assert.assertTrue(pTag.canContainBlock());
 // Test isInline()
 Assert.assertFalse(pTag.isInline());
 // Test isData()
 Assert.assertFalse(pTag.isData());
 // Test isEmpty()
 Assert.assertFalse(pTag.isEmpty());
 // Test isSelfClosing()
 Assert.assertFalse(pTag.isSelfClosing());
 // Test isKnownTag()
 Assert.assertTrue(pTag.isKnownTag());
 // Test preserveWhitespace()
 Assert.assertFalse(pTag.preserveWhitespace());
 // Test isFormListed()
 Assert.assertFalse(pTag.isFormListed());
 // Test isFormSubmittable()
 Assert.assertFalse(pTag.isFormSubmittable());

 // Test setSelfClosing()
 pTag = pTag.setSelfClosing();
 Assert.assertTrue(pTag.isSelfClosing());
 }

 @Test
 public void testTagMethods() {
 Tag pTag = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");

 // Test equals()
 Assert.assertNotEquals(pTag, divTag);
 Assert.assertEquals(pTag, pTag);
 // Test hashCode()
 int hashCodeP = pTag.hashCode();
 int hashCodeDiv = divTag.hashCode();
 Assert.assertNotEquals(hashCodeP, hashCodeDiv);
 // Test toString()
 Assert.assertEquals("p", pTag.toString());

 // Test static methods
 Tag unknownTag = Tag.valueOf("unknownTag");
 Tag emptyTag = Tag.valueOf("meta");
 Tag formTag = Tag.valueOf("input");

 Assert.assertEquals(unknownTag, Tag.valueOf("unknownTag", ParseSettings.preserveCase));
 Assert.assertTrue(Tag.isEmptyTag("meta"));
 Assert.assertTrue(Tag.isFormControl(formTag));
 Assert.assertFalse(Tag.isFormControl(emptyTag));
 }
}