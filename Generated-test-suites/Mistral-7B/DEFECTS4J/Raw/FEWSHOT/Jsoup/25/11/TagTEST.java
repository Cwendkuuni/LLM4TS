package org.jsoup.parser;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class TagTest {

 @Test
 public void testIsBlock() {
 Tag pTag = Tag.valueOf("p");
 assertTrue(pTag.isBlock());

 Tag inputTag = Tag.valueOf("input");
 assertFalse(inputTag.isBlock());
 }

 @Test
 public void testFormatAsBlock() {
 Tag pTag = Tag.valueOf("p");
 assertTrue(pTag.formatAsBlock());

 Tag inputTag = Tag.valueOf("input");
 assertFalse(inputTag.formatAsBlock());
 }

 @Test
 public void testCanContainBlock() {
 Tag divTag = Tag.valueOf("div");
 assertTrue(divTag.canContainBlock());

 Tag imgTag = Tag.valueOf("img");
 assertFalse(imgTag.canContainBlock());
 }

 @Test
 public void testIsEmpty() {
 Tag imgTag = Tag.valueOf("img");
 assertFalse(imgTag.isEmpty());

 Tag metaTag = Tag.valueOf("meta");
 assertTrue(metaTag.isEmpty());
 }

 @Test
 public void testIsSelfClosing() {
 Tag imgTag = Tag.valueOf("img");
 assertFalse(imgTag.isSelfClosing());

 Tag inputTag = Tag.valueOf("input");
 assertTrue(inputTag.isSelfClosing());
 }

 @Test
 public void testIsData() {
 Tag scriptTag = Tag.valueOf("script");
 assertTrue(scriptTag.isData());

 Tag pTag = Tag.valueOf("p");
 assertFalse(pTag.isData());
 }

 @Test
 public void testIsKnownTag() {
 assertTrue(Tag.isKnownTag("p"));
 assertFalse(Tag.isKnownTag("foo"));
 }

 @Test
 public void testPreserveWhitespace() {
 Tag preTag = Tag.valueOf("pre");
 assertTrue(preTag.preserveWhitespace());

 Tag pTag = Tag.valueOf("p");
 assertFalse(pTag.preserveWhitespace());
 }

 @Test
 public void testEquals() {
 Tag pTag1 = Tag.valueOf("p");
 Tag pTag2 = Tag.valueOf("p");
 assertEquals(pTag1, pTag2);

 Tag pTag1_1 = new Tag("p");
 Tag pTag2_1 = new Tag("p");
 assertNotEquals(pTag1_1, pTag2_1);

 Tag pTag1_2 = pTag1;
 assertEquals(pTag1, pTag1_2);
 }

 @Test
 public void testHashCode() {
 Tag pTag1 = Tag.valueOf("p");
 Tag pTag2 = Tag.valueOf("p");
 assertEquals(pTag1.hashCode(), pTag2.hashCode());

 Tag pTag1_1 = new Tag("p");
 Tag pTag2_1 = new Tag("p");
 assertNotEquals(pTag1_1.hashCode(), pTag2_1.hashCode());

 Tag pTag1_3 = pTag1;
 assertEquals(pTag1.hashCode(), pTag1_3.hashCode());
 }
}