package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class TagTest {

 private Tag pTag;
 private Tag imgTag;
 private Tag divTag;
 private Tag scriptTag;

 @Before
 public void setUp() {
 pTag = Tag.valueOf("p");
 imgTag = Tag.valueOf("img");
 divTag = Tag.valueOf("div");
 scriptTag = Tag.valueOf("script");
 }

 @Test
 public void testgetName() {
 assertEquals("p", pTag.getName());
 assertEquals("img", imgTag.getName());
 assertEquals("div", divTag.getName());
 assertEquals("script", scriptTag.getName());
 }

 @Test
 public void testIsBlock() {
 assertTrue(pTag.isBlock());
 assertFalse(imgTag.isBlock());
 assertTrue(divTag.isBlock());
 assertTrue(scriptTag.isBlock());
 }

 @Test
 public void testFormatAsBlock() {
 assertTrue(pTag.formatAsBlock());
 assertFalse(imgTag.formatAsBlock());
 assertTrue(divTag.formatAsBlock());
 assertTrue(scriptTag.formatAsBlock());
 }

 @Test
 public void testCanContainBlock() {
 assertTrue(pTag.canContainBlock());
 assertTrue(divTag.canContainBlock());
 assertFalse(imgTag.canContainBlock());
 assertFalse(scriptTag.canContainBlock());
 }

 @Test
 public void testIsInline() {
 assertFalse(pTag.isInline());
 assertTrue(imgTag.isInline());
 assertFalse(divTag.isInline());
 assertFalse(scriptTag.isInline());
 }

 @Test
 public void testIsData() {
 assertFalse(pTag.isData());
 assertFalse(imgTag.isData());
 assertFalse(divTag.isData());
 assertFalse(scriptTag.isData());
 }

 @Test
 public void testIsEmpty() {
 assertFalse(pTag.isEmpty());
 assertFalse(imgTag.isEmpty());
 assertFalse(divTag.isEmpty());
 assertFalse(scriptTag.isEmpty());
 }

 @Test
 public void testIsSelfClosing() {
 assertFalse(pTag.isSelfClosing());
 assertFalse(imgTag.isSelfClosing());
 assertFalse(divTag.isSelfClosing());
 assertFalse(scriptTag.isSelfClosing());
 }

 @Test
 public void testIsKnownTag() {
 assertTrue(Tag.isKnownTag("p"));
 assertTrue(Tag.isKnownTag("img"));
 assertTrue(Tag.isKnownTag("div"));
 assertTrue(Tag.isKnownTag("script"));
 }

 @Test
 public void testPreserveWhitespace() {
 assertFalse(pTag.preserveWhitespace());
 assertFalse(imgTag.preserveWhitespace());
 assertFalse(divTag.preserveWhitespace());
 assertFalse(scriptTag.preserveWhitespace());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidTagName() {
 Tag tag = null;
 try {
 tag = Tag.valueOf(null);
 } catch (IllegalArgumentException e) {
 tag = e.getCause().getCause();
 }
 assertNotNull(tag);
 assertEquals("", tag.tagName);
 }

 @Test
 public void testValueOfUnknownTag() {
 Tag tag = Tag.valueOf("unknownTag");
 assertFalse(tag.isKnownTag());
 assertTrue(tag.isEmpty());
 assertFalse(tag.canContainBlock());
 assertFalse(tag.canContainInline());
 assertFalse(tag.isData());
 assertFalse(tag.isBlock());
 assertFalse(tag.formatAsBlock());
 assertFalse(tag.preserveWhitespace());
 }

 @Test
 public void testEquals() {
 Tag tag1 = new Tag("p");
 Tag tag2 = new Tag("p");
 Tag tag3 = new Tag("div");

 assertEquals(tag1, tag1);
 assertEquals(tag1.hashCode(), tag1.hashCode());

 assertEquals(tag1, tag2);
 assertEquals(tag1.hashCode(), tag2.hashCode());

 assertNotEquals(tag1, tag3);
 assertNotEquals(tag1.hashCode(), tag3.hashCode());
 }

}