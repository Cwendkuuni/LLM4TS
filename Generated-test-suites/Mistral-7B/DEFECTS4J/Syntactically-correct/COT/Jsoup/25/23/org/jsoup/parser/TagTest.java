package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

public class TagTest {

 private Tag tag;

 @Before
 public void setUp() {
 tag = Tag.valueOf("div");
 }

 @Test
 public void testGetterName() {
 String expected = "div";
 String result = tag.getName();
 assertEquals(expected, result);
 }

 @Test
 public void testUnknownTagReturnNewInstance() {
 String unknownTagName = "unknownTag";
 Tag unknownTag = Tag.valueOf(unknownTagName);
 String expectedUnknownTagName = unknownTagName;
 String resultUnknownTagName = unknownTag.getName();
 assertEquals(expectedUnknownTagName, resultUnknownTagName);
 // check that it's not the same instance as the known tag "div"
 Assert.assertNotSame(tag, unknownTag);
 }

 @Test
 public void testIsBlock() {
 boolean expected = true;
 boolean result = tag.isBlock();
 assertEquals(expected, result);
 }

 @Test
 public void testFormatAsBlock() {
 boolean expected = true;
 boolean result = tag.formatAsBlock();
 assertEquals(expected, result);
 }

 @Test
 public void testCanContainBlock() {
 boolean expected = true;
 boolean result = tag.canContainBlock();
 assertEquals(expected, result);
 }

 @Test
 public void testIsEmpty() {
 boolean expected = false;
 boolean result = tag.isEmpty();
 assertEquals(expected, result);
 }

 @Test
 public void testIsSelfClosing() {
 boolean expected = false;
 boolean result = tag.isSelfClosing();
 assertEquals(expected, result);
 }

 @Test
 public void testIsKnownTag() {
 boolean expected = true;
 boolean result = Tag.isKnownTag(tag.getName());
 assertEquals(expected, result);
 }

 @Test
 public void testPreserveWhitespace() {
 boolean expected = false;
 boolean result = tag.preserveWhitespace();
 assertEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullTagNameThrowsIllegalArgumentException() {
 Tag tagWithNullName = new Tag(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyTagNameThrowsIllegalArgumentException() {
 Tag tagWithEmptyName = new Tag("");
 }

 @Test
 public void testGetKnownTagByName() {
 String knownTagName = "div";
 Tag knownTag = Tag.valueOf(knownTagName);
 Tag knownTagRetrieved = Tag.valueOf(knownTagName);
 assertEquals(knownTag, knownTagRetrieved);
 }

 @Test
 public void testGetUnknownTagByName() {
 String unknownTagName = "unknownTag";
 Tag unknownTag = Tag.valueOf(unknownTagName);
 Tag unknownTagRetrieved = Tag.valueOf(unknownTagName);
 Assert.assertNotSame(Tag.valueOf("div"), unknownTag);
 assertEquals(unknownTag, unknownTagRetrieved);
 }

 @Test
 public void testIsBlockForUnknownTag() {
 String unknownTagName = "unknownTag";
 Tag unknownTag = Tag.valueOf(unknownTagName);
 boolean expected = false;
 boolean result = unknownTag.isBlock();
 assertEquals(expected, result);
 }

 @Test
 public void testFormatAsBlockForUnknownTag() {
 String unknownTagName = "unknownTag";
 Tag unknownTag = Tag.valueOf(unknownTagName);
 boolean expected = false;
 boolean result = unknownTag.formatAsBlock();
 assertEquals(expected, result);
 }

 @Test
 public void testCanContainBlockForUnknownTag() {
 String unknownTagName = "unknownTag";
 Tag unknownTag = Tag.valueOf(unknownTagName);
 boolean expected = false;
 boolean result = unknownTag.canContainBlock();
 assertEquals(expected, result);
 }

 @Test
 public void testIsDataForBlockTag() {
 tag = Tag.valueOf("script");
 boolean expected = true;
 boolean result = tag.isData();
 assertEquals(expected, result);
 }

 @Test
 public void testIsDataForInlineTag() {
 tag = Tag.valueOf("span");
 boolean expected = false;
 boolean result = tag.isData();
 assertEquals(expected, result);
 }

 @Test
 public void testIsDataForEmptyTag() {
 tag = Tag.valueOf("img");
 boolean expected = true;
 boolean result = tag.isData();
 assertEquals(expected, result);
 }

 @Test
 public void testIsDataForPredefinedTag() {
 tag = Tag.valueOf("p");
 boolean expected = false;
 boolean result = tag.isData();
 assertEquals(expected, result);
 }

 @Test
 public void testIsEmptyForEmptyTag() {
 tag = Tag.valueOf("img");
 boolean expected = true;
 boolean result = tag.isEmpty();
 assertEquals(expected, result);
 }

 @Test
 public void testIsEmptyForNonEmptyTag() {
 tag = Tag.valueOf("p");
 boolean expected = false;
 boolean result = tag.isEmpty();
 assertEquals(expected, result);
 }

 @Test
 public void testIsSelfClosingForSelfClosingTag() {
 tag = Tag.valueOf("img");
 tag.setSelfClosing();
 boolean expected = true;
 boolean result = tag.isSelfClosing();
 assertEquals(expected, result);
 }

 @Test
 public void testIsSelfClosingForNonSelfClosingTag() {
 tag = Tag.valueOf("p");
 boolean expected = false;
 boolean result = tag.isSelfClosing();
 assertEquals(expected, result);
 }

 @Test
 public void testIsKnownTagForKnownTag() {
 String knownTagName = "div";
 boolean expected = true;
 boolean result = Tag.isKnownTag(knownTagName);
 assertEquals(expected, result);
 }

 @Test
 public void testIsKnownTagForUnknownTag() {
 String unknownTagName = "unknownTag";
 boolean expected = false;
 boolean result = Tag.isKnownTag(unknownTagName);
 assertEquals(expected, result);
 }

 @Test
 public void testPreserveWhitespaceForPredefinedTag() {
 tag = Tag.valueOf("pre");
 boolean expected = true;
 boolean result = tag.preserveWhitespace();
 assertEquals(expected, result);
 }

 @Test
 public void testPreserveWhitespaceForNonPredefinedTag() {
 tag = Tag.valueOf("span");
 boolean expected = false;
 boolean result = tag.preserveWhitespace();
 assertEquals(expected, result);
 }

 @Test
 public void testEqualsForSameInstance() {
 Tag tag1 = Tag.valueOf("div");
 Tag tag2 = tag1;
 boolean expected = true;
 boolean result = tag1.equals(tag2);
 assertEquals(expected, result);
 }

 @Test
 public void testEqualsForSameClassDifferentInstances() {
 Tag tag1 = Tag.valueOf("div");
 Tag tag2 = Tag.valueOf("div");
 boolean expected = true;
 boolean result = tag1.equals(tag2);
 assertEquals(expected, result);
 }

 @Test
 public void testEqualsForDifferentClasses() {
 Tag tag1 = Tag.valueOf("div");
 Tag tag2 = Tag.valueOf("span");
 boolean expected = false;
 boolean result = tag1.equals(tag2);
 assertEquals(expected, result);
 }

 @Test
 public void testHashcodeForSameInstance() {
 Tag tag1 = Tag.valueOf("div");
 Tag tag2 = tag1;
 int expected = tag1.hashCode();
 int result = tag2.hashCode();
 assertEquals(expected, result);
 }

 @Test
 public void testHashcodeForDifferentInstances() {
 Tag tag1 = Tag.valueOf("div");
 Tag tag2 = Tag.valueOf("div");
 int expected = tag1.hashCode();
 int result = tag2.hashCode();
 assertNotSame(expected, result);
 }

 @Test
 public void testToString() {
 String expected = "div";
 String result = tag.toString();
 assertEquals(expected, result);
 }

 @Test
 public void testIsKnownTagForUnknownTagCaseInsensitive() {
 String unknownTagName = "UnknownTag";
 boolean expected = false;
 boolean result = Tag.isKnownTag(unknownTagName);
 assertEquals(expected, result);
 }

 @Test
 public void testIsKnownTagForPredefinedTagCaseInsensitive() {
 String knownTagName = "Div";
 boolean expected = true;
 boolean result = Tag.isKnownTag(knownTagName);
 assertEquals(expected, result);
 }

}