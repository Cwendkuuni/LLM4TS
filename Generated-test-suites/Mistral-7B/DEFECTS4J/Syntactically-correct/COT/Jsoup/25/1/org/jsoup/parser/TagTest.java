package org.jsoup.parser.test;

import org.junit.Test;
import org.jsoup.parser.Tag;
import static org.junit.Assert.*;

public class TagTest {

 private Tag pTag;
 private Tag divTag;
 private Tag imgTag;
 private Tag scriptTag;
 private Tag unknownTag;

 @Before
 public void setup() {
 pTag = Tag.valueOf("p");
 divTag = Tag.valueOf("div");
 imgTag = Tag.valueOf("img");
 scriptTag = Tag.valueOf("script");
 unknownTag = new Tag("UnknownTag");
 }

 @Test
 public void testTagName() {
 String pTagName = pTag.getName();
 assertEquals("p", pTagName);
 }

 @Test
 public void testUnknownTagValueOf() {
 String unknownTagName = unknownTag.getName();
 assertEquals("UnknownTag", unknownTagName);
 }

 @Test
 public void testIsBlock() {
 assertTrue(pTag.isBlock());
 assertTrue(divTag.isBlock());
 assertFalse(imgTag.isBlock());
 assertFalse(scriptTag.isBlock());
 }

 @Test
 public void testFormatAsBlock() {
 assertTrue(pTag.formatAsBlock());
 assertTrue(divTag.formatAsBlock());
 assertFalse(imgTag.formatAsBlock());
 assertFalse(scriptTag.formatAsBlock());
 }

 @Test
 public void testCanContainBlock() {
 assertTrue(pTag.canContainBlock());
 assertTrue(divTag.canContainBlock());
 assertFalse(imgTag.canContainBlock());
 assertFalse(scriptTag.canContainBlock());
 }

 @Test
 public void testIsData() {
 assertFalse(pTag.isData());
 assertFalse(divTag.isData());
 assertTrue(imgTag.isData());
 assertTrue(scriptTag.isData());
 }

 @Test
 public void testIsEmpty() {
 assertFalse(pTag.isEmpty());
 assertFalse(divTag.isEmpty());
 assertTrue(imgTag.isEmpty());
 assertTrue(scriptTag.isEmpty());
 }

 @Test
 public void testIsSelfClosing() {
 assertFalse(pTag.isSelfClosing());
 assertFalse(divTag.isSelfClosing());
 assertFalse(imgTag.isSelfClosing());
 assertTrue(scriptTag.isSelfClosing());
 }

 @Test
 public void testIsKnownTag() {
 assertTrue(Tag.isKnownTag("p"));
 assertTrue(Tag.isKnownTag("div"));
 assertFalse(Tag.isKnownTag("UnknownTag"));
 }

 @Test
 public void testPreserveWhitespace() {
 assertFalse(pTag.preserveWhitespace());
 assertFalse(divTag.preserveWhitespace());
 assertTrue(scriptTag.preserveWhitespace());
 assertFalse(imgTag.preserveWhitespace());
 }

 @Test
 public void testEquals() {
 Tag sameTag = Tag.valueOf("p");
 Tag differentTag = Tag.valueOf("div");
 Tag sameTag2 = pTag;
 Tag differentTag2 = new Tag("p");

 assertEquals(pTag, sameTag);
 assertEquals(sameTag, pTag);
 assertEquals(sameTag, sameTag2);
 assertNotEquals(pTag, differentTag);
 assertNotEquals(pTag, differentTag2);
 }

 @Test
 public void testHashCode() {
 Tag tag1 = Tag.valueOf("p");
 Tag tag2 = Tag.valueOf("p");
 Tag tag3 = Tag.valueOf("div");

 int hashCode1 = tag1.hashCode();
 int hashCode2 = tag2.hashCode();
 int hashCode3 = tag3.hashCode();

 assertEquals(hashCode1, hashCode2);
 assertNotEquals(hashCode1, hashCode3);
 }

 @Test
 public void testToString() {
 assertEquals("p", pTag.toString());
 assertEquals("div", divTag.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullTagName() {
 Tag nullTag = new Tag(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyTagName() {
 Tag emptyTag = new Tag("");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLowercaseTagName() {
 Tag lowerTag = new Tag("p");
 Tag mixedTag = new Tag("P");
 Tag upperTag = new Tag("P");
 Tag emptyUpperTag = new Tag("UPPER");

 Tag expectedTag = Tag.valueOf("p");
 Tag expectedMixedTag = Tag.valueOf("P");
 Tag expectedUpperTag = Tag.valueOf("P");
 Tag expectedEmptyUpperTag = new Tag("UPPER");

 assertEquals(expectedTag, lowerTag);
 assertEquals(expectedMixedTag, mixedTag);
 assertEquals(expectedUpperTag, upperTag);
 assertEquals(expectedEmptyUpperTag, emptyUpperTag);
 }

 @Test
 public void testUnknownTagValueOf() {
 Tag unknownTag = Tag.valueOf("UnknownTag");
 Tag expectedUnknownTag = new Tag("UnknownTag");
 assertEquals(expectedUnknownTag, unknownTag);
 }

 @Test
 public void testValueOfRegisteredTag() {
 Tag pTagFromMap = Tag.valueOf("p");
 assertEquals(pTag, pTagFromMap);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUnknownTagValueOfWithSameNameButDifferentTag() {
 Tag pTag = Tag.valueOf("p");
 Tag unknownTag = new Tag("UnknownTag");
 Tag unknownTag2 = new Tag("p");

 assertEquals(pTag, pTag);
 assertNotEquals(pTag, unknownTag);
 assertNotEquals(pTag, unknownTag2);
 }
}