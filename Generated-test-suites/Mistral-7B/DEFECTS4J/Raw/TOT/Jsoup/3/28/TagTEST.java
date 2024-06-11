package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class TagTest {

 @Test
 public void testValueOf() {
 Tag pTag = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");

 assertEquals("p", pTag.getName());
 assertEquals("div", divTag.getName());
 }

 @Test
 public void testCanContain() {
 Tag pTag = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");
 Tag brTag = Tag.valueOf("br");

 assertTrue(pTag.canContain(brTag));
 assertTrue(divTag.canContain(pTag));
 assertTrue(divTag.canContain(brTag));

 assertFalse(pTag.canContain(pTag)); // cannot contain self
 assertFalse(pTag.canContain(divTag)); // p cannot contain div directly
 }

 @Test
 public void testIsBlockAndCanContainBlock() {
 Tag pTag = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");

 assertTrue(pTag.isBlock());
 assertTrue(pTag.canContainBlock());
 assertTrue(divTag.isBlock());
 assertTrue(divTag.canContainBlock());

 Tag h1Tag = Tag.valueOf("h1");
 assertFalse(h1Tag.isBlock()); // h1 is not a block tag
 assertFalse(h1Tag.canContainBlock());
 }

 @Test
 public void testIsData() {
 Tag scriptTag = Tag.valueOf("script");

 assertFalse(scriptTag.isData());

 Tag metaTag = Tag.valueOf("meta");
 metaTag.setEmpty();
 assertTrue(metaTag.isData());
 }

 @Test
 public void testIsEmpty() {
 Tag imgTag = Tag.valueOf("img");
 imgTag.setEmpty();

 assertTrue(imgTag.isEmpty());

 Tag pTag = Tag.valueOf("p");
 assertFalse(pTag.isEmpty());
 }

 @Test
 public void testPreserveWhitespace() {
 Tag preTag = Tag.valueOf("pre");
 preTag.setPreserveWhitespace();

 assertTrue(preTag.preserveWhitespace());

 Tag divTag = Tag.valueOf("div");
 assertFalse(divTag.preserveWhitespace());
 }

 @Test
 public void testGetImplicitParent() {
 Tag pTag = Tag.valueOf("p");
 pTag.setAncestor("body");

 assertEquals("body", pTag.getImplicitParent().getName());
 }

 @Test
 public void testIsValidParent() {
 Tag pTag = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");
 Tag headTag = Tag.valueOf("head");

 assertTrue(pTag.isValidParent(headTag)); // head is an ancestor of body, so p is a valid parent for head
 assertTrue(pTag.isValidParent(divTag)); // valid since we haven't set any explicit children for div

 assertFalse(divTag.isValidParent(pTag)); // div cannot be a parent for p
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidTagName() {
 Tag invalidTag = new Tag("");
 }
}