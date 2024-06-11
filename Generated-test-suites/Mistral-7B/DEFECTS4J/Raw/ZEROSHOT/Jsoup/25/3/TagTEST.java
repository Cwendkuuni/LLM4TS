package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.parser.Tag;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class TagTest {

 private static final String TAG_NAME_P = "p";
 private static final String TAG_NAME_DIV = "div";
 private static final String TAG_NAME_IMG = "img";
 private static final String TAG_NAME_SPAN = "span";

 @Test
 public void testTagValues() {
 Tag pTag = Tag.valueOf(TAG_NAME_P);
 Tag divTag = Tag.valueOf(TAG_NAME_DIV);
 Tag imgTag = Tag.valueOf(TAG_NAME_IMG);
 Tag spanTag = Tag.valueOf(TAG_NAME_SPAN);

 assertEquals("p", pTag.getName());
 assertEquals("div", divTag.getName());
 assertEquals("img", imgTag.getName());
 assertEquals("span", spanTag.getName());
 }

 @Test
 public void testIsBlockTag() {
 assertEquals(true, pTag.isBlock());
 assertEquals(true, divTag.isBlock());
 assertEquals(false, imgTag.isBlock());
 assertEquals(false, spanTag.isBlock());
 }

 @Test
 public void testFormatAsBlock() {
 assertEquals(true, pTag.formatAsBlock());
 assertEquals(false, imgTag.formatAsBlock());
 assertEquals(true, divTag.formatAsBlock());
 assertEquals(false, spanTag.formatAsBlock());
 }

 @Test
 public void testCanContainBlock() {
 assertEquals(true, pTag.canContainBlock());
 assertEquals(true, divTag.canContainBlock());
 assertEquals(false, imgTag.canContainBlock());
 assertEquals(false, spanTag.canContainBlock());
 }

 @Test
 public void testIsEmptyTag() {
 assertEquals(false, pTag.isEmpty());
 assertEquals(false, divTag.isEmpty());
 assertEquals(true, imgTag.isEmpty());
 assertEquals(false, spanTag.isEmpty());
 }

 @Test
 public void testIsSelfClosingTag() {
 assertEquals(false, pTag.isSelfClosing());
 assertEquals(false, divTag.isSelfClosing());
 assertEquals(true, imgTag.isSelfClosing());
 assertEquals(false, spanTag.isSelfClosing());
 }

 @Test
 public void testIsKnownTag() {
 assertTrue(Tag.isKnownTag(TAG_NAME_P));
 assertTrue(Tag.isKnownTag(TAG_NAME_DIV));
 assertTrue(Tag.isKnownTag(TAG_NAME_IMG));
 assertTrue(Tag.isKnownTag(TAG_NAME_SPAN));
 }

 @Test
 public void testPreserveWhitespaceTag() {
 Tag preTag = Tag.valueOf("pre");
 Tag plaintextTag = Tag.valueOf("plaintext");

 assertEquals(true, preTag.preserveWhitespace());
 assertEquals(false, plaintextTag.preserveWhitespace());
 }
}