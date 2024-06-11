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
 private static final String TAG_NAME_UL = "ul";
 private static final String TAG_NAME_TR = "tr";
 private static final String TAG_NAME_TD = "td";
 private static final String TAG_NAME_SPAN = "span";

 @Test
 public void testTagValues() {
 Tag tagP = Tag.valueOf(TAG_NAME_P);
 Tag tagDiv = Tag.valueOf(TAG_NAME_DIV);
 Tag tagImg = Tag.valueOf(TAG_NAME_IMG);
 Tag tagUL = Tag.valueOf(TAG_NAME_UL);
 Tag tagTR = Tag.valueOf(TAG_NAME_TR);
 Tag tagTD = Tag.valueOf(TAG_NAME_TD);
 Tag tagSpan = Tag.valueOf(TAG_NAME_SPAN);

 assertEquals("p", tagP.getName());
 assertEquals("div", tagDiv.getName());
 assertEquals("img", tagImg.getName());
 assertEquals("ul", tagUL.getName());
 assertEquals("tr", tagTR.getName());
 assertEquals("td", tagTD.getName());
 assertEquals("span", tagSpan.getName());
 }

 @Test
 public void testTagIsBlock() {
 final boolean EXPECTED_IS_BLOCK_FOR_P = true;
 final boolean EXPECTED_IS_BLOCK_FOR_DIV = true;
 final boolean EXPECTED_IS_BLOCK_FOR_UL = true;
 final boolean EXPECTED_IS_BLOCK_FOR_TR = true;
 final boolean EXPECTED_IS_BLOCK_FOR_IMG = false;
 final boolean EXPECTED_IS_BLOCK_FOR_SPAN = false;

 assertTrue(TAG_NAME_P.equals(tagP.getName()));
 assertTrue(TAG_NAME_P.equals(tagP.tagName));
 assertTrue(TAG_NAME_DIV.equals(tagDiv.getName()));
 assertTrue(TAG_NAME_DIV.equals(tagDiv.tagName));
 assertTrue(tagP.isBlock());
 assertTrue(tagDiv.isBlock());
 assertFalse(tagImg.isBlock());
 assertFalse(tagSpan.isBlock());
 }

 @Test
 public void testTagFormatAsBlock() {
 final boolean EXPECTED_FORMAT_AS_BLOCK_FOR_P = true;
 final boolean EXPECTED_FORMAT_AS_BLOCK_FOR_DIV = true;
 final boolean EXPECTED_FORMAT_AS_BLOCK_FOR_UL = true;
 final boolean EXPECTED_FORMAT_AS_BLOCK_FOR_TR = true;
 final boolean EXPECTED_FORMAT_AS_BLOCK_FOR_IMG = false;
 final boolean EXPECTED_FORMAT_AS_BLOCK_FOR_SPAN = false;

 assertTrue(TAG_NAME_P.equals(tagP.getName()));
 assertTrue(TAG_NAME_P.equals(tagP.tagName));
 assertTrue(TAG_NAME_DIV.equals(tagDiv.getName()));
 assertTrue(TAG_NAME_DIV.equals(tagDiv.tagName));
 assertTrue(tagP.formatAsBlock());
 assertTrue(tagDiv.formatAsBlock());
 assertFalse(tagImg.formatAsBlock());
 assertFalse(tagSpan.formatAsBlock());
 }

 @Test
 public void testTagCanContainBlock() {
 final boolean EXPECTED_CAN_CONTAIN_BLOCK_FOR_P = true;
 final boolean EXPECTED_CAN_CONTAIN_BLOCK_FOR_DIV = true;
 final boolean EXPECTED_CAN_CONTAIN_BLOCK_FOR_UL = true;
 final boolean EXPECTED_CAN_CONTAIN_BLOCK_FOR_TR = true;
 final boolean EXPECTED_CAN_CONTAIN_BLOCK_FOR_IMG = false;
 final boolean EXPECTED_CAN_CONTAIN_BLOCK_FOR_SPAN = false;

 assertTrue(TAG_NAME_P.equals(tagP.getName()));
 assertTrue(TAG_NAME_P.equals(tagP.tagName));
 assertTrue(TAG_NAME_DIV.equals(tagDiv.getName()));
 assertTrue(TAG_NAME_DIV.equals(tagDiv.tagName));
 assertTrue(tagP.canContainBlock());
 assertTrue(tagDiv.canContainBlock());
 assertFalse(tagImg.canContainBlock());
 assertFalse(tagSpan.canContainBlock());
 }

 @Test
 public void testTagIsEmpty() {
 final boolean EXPECTED_IS_EMPTY_FOR_IMG = false;
 final boolean EXPECTED_IS_EMPTY_FOR_SPAN = false;
 final boolean EXPECTED_IS_EMPTY_FOR_META = true;
 final boolean EXPECTED_IS_EMPTY_FOR_AREA = true;
 final boolean EXPECTED_IS_EMPTY_FOR_INPUT = false;
 final boolean EXPECTED_IS_EMPTY_FOR_OPTION = false;

 assertFalse(tagImg.isEmpty());
 assertFalse(tagSpan.isEmpty());
 assertTrue(Tag.valueOf(TAG_NAME_META).isEmpty());
 assertTrue(Tag.valueOf(TAG_NAME_AREA).isEmpty());
 assertFalse(Tag.valueOf(TAG_NAME_INPUT).isEmpty());
 assertFalse(Tag.valueOf(TAG_NAME_OPTION).isEmpty());
 }

 @Test
 public void testTagIsSelfClosing() {
 final boolean EXPECTED_IS_SELF_CLOSING_FOR_IMG = true;
 final boolean EXPECTED_IS_SELF_CLOSING_FOR_SPAN = false;
 final boolean EXPECTED_IS_SELF_CLOSING_FOR_IMG_SELF_CLOSING = true;
 final boolean EXPECTED_IS_SELF_CLOSING_FOR_SPAN_SELF_CLOSING = false;
 final boolean EXPECTED_IS_SELF_CLOSING_FOR_INPUT = false;
 final boolean EXPECTED_IS_SELF_CLOSING_FOR_INPUT_SELF_CLOSING = false;

 assertFalse(tagImg.isSelfClosing());
 assertFalse(tagSpan.isSelfClosing());
 assertTrue(tagImg.setSelfClosing().isSelfClosing());
 assertFalse(tagSpan.setSelfClosing().isSelfClosing());
 assertFalse(Tag.valueOf(TAG_NAME_INPUT).isSelfClosing());
 assertFalse(Tag.valueOf(TAG_NAME_INPUT).setSelfClosing().isSelfClosing());
 }

 @Test
 public void testTagIsKnownTag() {
 final boolean EXPECTED_IS_KNOWN_TAG_FOR_P = true;
 final boolean EXPECTED_IS_KNOWN_TAG_FOR_DIV = true;
 final boolean EXPECTED_IS_KNOWN_TAG_FOR_UL = true;
 final boolean EXPECTED_IS_KNOWN_TAG_FOR_TR = true;
 final boolean EXPECTED_IS_KNOWN_TAG_FOR_IMG = true;
 final boolean EXPECTED_IS_KNOWN_TAG_FOR_SPAN = true;
 final boolean EXPECTED_IS_KNOWN_TAG_FOR_UNKNOWN_TAG = false;

 assertTrue(TAG_NAME_P.equals(tagP.getName()));
 assertTrue(TAG_NAME_P.equals(tagP.tagName));
 assertTrue(TAG_NAME_DIV.equals(tagDiv.getName()));
 assertTrue(TAG_NAME_DIV.equals(tagDiv.tagName));
 assertTrue(Tag.isKnownTag(TAG_NAME_P));
 assertTrue(Tag.isKnownTag(TAG_NAME_DIV));
 assertFalse(Tag.isKnownTag("unknown_tag"));
 }

 @Test
 public void testTagPreserveWhitespace() {
 final boolean EXPECTED_PRESERVE_WHITESPACE_FOR_PRE = true;
 final boolean EXPECTED_PRESERVE_WHITESPACE_FOR_PLAINTEXT = true;
 final boolean EXPECTED_PRESERVE_WHITESPACE_FOR_DIV = false;
 final boolean EXPECTED_PRESERVE_WHITESPACE_FOR_SPAN = false;

 assertTrue(Tag.isKnownTag(TAG_NAME_PRE));
 assertTrue(Tag.isKnownTag(TAG_NAME_PLAINTEXT));
 assertFalse(tagDiv.preserveWhitespace());
 assertFalse(tagSpan.preserveWhitespace());
 assertTrue(tagP.preserveWhitespace());
 assertTrue(tagTD.preserveWhitespace());
 }

 @Test
 public void testTagEquals() {
 final Tag tagP1 = Tag.valueOf(TAG_NAME_P);
 final Tag tagP2 = Tag.valueOf(TAG_NAME_P);
 final Tag tagDiv1 = Tag.valueOf(TAG_NAME_DIV);
 final Tag tagDiv2 = Tag.valueOf(TAG_NAME_DIV);
 final Tag tagSpan1 = Tag.valueOf(TAG_NAME_SPAN);
 final Tag tagSpan2 = Tag.valueOf(TAG_NAME_SPAN);

 assertEquals(tagP1, tagP1);
 assertEquals(tagP1, tagP2);
 assertEquals(tagDiv1, tagDiv1);
 assertEquals(tagDiv1, tagDiv2);
 assertNotEquals(tagP1, tagDiv1);
 assertNotEquals(tagP1, tagSpan1);
 assertNotEquals(tagDiv1, tagSpan1);
 }

 @Test
 public void testTagHashCode() {
 final Tag tagP = Tag.valueOf(TAG_NAME_P);
 final Tag tagDiv = Tag.valueOf(TAG_NAME_DIV);
 final Tag tagImg = Tag.valueOf(TAG_NAME_IMG);
 final Tag tagSpan = Tag.valueOf(TAG_NAME_SPAN);

 int hashCodeTagP = tagP.hashCode();
 int hashCodeTagDiv = tagDiv.hashCode();
 int hashCodeTagImg = tagImg.hashCode();
 int hashCodeTagSpan = tagSpan.hashCode();

 assertEquals(tagP.hashCode(), hashCodeTagP);
 assertEquals(tagDiv.hashCode(), hashCodeTagDiv);
 assertEquals(tagImg.hashCode(), hashCodeTagImg);
 assertEquals(tagSpan.hashCode(), hashCodeTagSpan);

 final String tagNameP = TAG_NAME_P;
 final String tagNameDiv = TAG_NAME_DIV;
 final String tagNameImg = TAG_NAME_IMG;
 final String tagNameSpan = TAG_NAME_SPAN;

 final Tag tagP2 = Tag.valueOf(tagNameP);
 final Tag tagDiv2 = Tag.valueOf(tagNameDiv);
 final Tag tagImg2 = Tag.valueOf(tagNameImg);
 final Tag tagSpan2 = Tag.valueOf(tagNameSpan);

 assertEquals(tagP.hashCode(), tagP2.hashCode());
 assertEquals(tagDiv.hashCode(), tagDiv2.hashCode());
 assertEquals(tagImg.hashCode(), tagImg2.hashCode());
 assertEquals(tagSpan.hashCode(), tagSpan2.hashCode());
 }
}