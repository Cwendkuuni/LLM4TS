package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class TagTest {

 @Test
 public void testValueOf() {
 Tag tagP = Tag.valueOf("p");
 Tag tagDiv = Tag.valueOf("div");
 Tag tagUnknown = Tag.valueOf("unknown");

 Assert.assertEquals("p", tagP.getName());
 Assert.assertEquals("div", tagDiv.getName());
 Assert.assertNotEquals("p", tagUnknown.getName());
 }

 @Test
 public void testCanContain() {
 Tag tagP = Tag.valueOf("p");
 Tag tagDiv = Tag.valueOf("div");
 Tag tagP2 = Tag.valueOf("p");

 Assert.assertTrue(tagP.canContain(tagDiv)); // p can contain div (as a child)
 Assert.assertTrue(tagP.canContain(tagP2)); // p can contain p (as a child)
 Assert.assertFalse(tagDiv.canContain(tagP)); // div cannot contain p (as a child)
 }

 @Test
 public void testIsValidParent() {
 Tag tagP = Tag.valueOf("p");
 Tag tagDiv = Tag.valueOf("div");
 Tag tagP2 = Tag.valueOf("p");
 Tag tagHead = Tag.valueOf("head");

 // p is a valid parent for p
 Assert.assertTrue(tagP.isValidParent(tagP2));

 // p is not a valid parent for div
 Assert.assertFalse(tagP.isValidParent(tagDiv));

 // p is not a valid parent for head
 Assert.assertFalse(tagP.isValidParent(tagHead));
 }

 @Test
 public void testIsData() {
 Tag tagScript = Tag.valueOf("script");
 Tag tagLink = Tag.valueOf("link");

 // script is a data-only tag
 Assert.assertTrue(tagScript.isData());

 // link is not a data-only tag
 Assert.assertFalse(tagLink.isData());
 }

 @Test
 public void testIsEmpty() {
 Tag tagImg = Tag.valueOf("img");
 Tag tagP = Tag.valueOf("p");

 // img is an empty tag
 Assert.assertTrue(tagImg.isEmpty());

 // p is not an empty tag
 Assert.assertFalse(tagP.isEmpty());
 }

 @Test
 public void testPreserveWhitespace() {
 Tag tagPre = Tag.valueOf("pre");

 // pre tag preserves whitespace
 Assert.assertTrue(tagPre.preserveWhitespace());
 }

 @Test
 public void testGetImplicitParent() {
 Tag tagP = Tag.valueOf("p");
 Tag tagDiv = Tag.valueOf("div").setAncestor("p");

 // implicit parent of p is div
 Assert.assertEquals("div", tagP.getImplicitParent().getName());
 }

 @Test
 public void testCanContainBlockAndInline() {
 Tag tagDiv = Tag.valueOf("div");
 Tag tagP = Tag.valueOf("p");
 Tag tagBr = Tag.valueOf("br");

 // div can contain both block (p) and inline (br) elements
 Assert.assertTrue(tagDiv.canContain(tagP));
 Assert.assertTrue(tagDiv.canContain(tagBr));
 }

 @Test
 public void testCanContainSameTag() {
 Tag tagP = Tag.valueOf("p");

 // p cannot contain itself
 Assert.assertFalse(tagP.canContain(tagP));
 }

 @Test
 public void testCanContainHeadTags() {
 Tag tagHead = Tag.valueOf("head");
 Tag tagScript = Tag.valueOf("script");
 Tag tagLink = Tag.valueOf("link");

 // head can contain script and link tags
 Assert.assertTrue(tagHead.canContain(tagScript));
 Assert.assertTrue(tagHead.canContain(tagLink));
 }

 @Test
 public void testCanContainDTDDAssociation() {
 Tag tagDT = Tag.valueOf("dt");
 Tag tagDD = Tag.valueOf("dd");

 // dt cannot contain dd, but dd can contain dt
 Assert.assertFalse(tagDT.canContain(tagDD));
 Assert.assertTrue(tagDD.canContain(tagDT));
 }

 @Test
 public void testEqualsAndHashCode() {
 Tag tagP1 = Tag.valueOf("p");
 Tag tagP2 = Tag.valueOf("p");
 Tag tagDiv = Tag.valueOf("div");

 // two identical p tags are equal
 Assert.assertEquals(tagP1, tagP2);

 // different tag names are not equal
 Assert.assertNotEquals(tagP1, tagDiv);

 // two identical tags have the same hashcode
 Assert.assertEquals(tagP1.hashCode(), tagP2.hashCode());
 }

 @Test
 public void testToString() {
 Tag tagP = Tag.valueOf("p");

 // toString returns tag name
 Assert.assertEquals("p", tagP.toString());
 }

 @Test
 public void testCreateBlocksAndInlines() {
 List<String> tagNames = Arrays.asList("HTML", "HEAD", "BODY", "FRAMESET", "SCRIPT", "NOSCRIPT", "STYLE", "META", "LINK", "OBJECT",
         "TITLE", "FRAME", "NOFRAMES", "FONT", "TT", "I", "B", "BIG", "SMALL", "EM", "STRONG", "DFN", "CODE", "SAMP", "KBD", "VAR",
         "CITE", "ABBR", "ACRONYM", "A", "IMG", "BR", "MAP", "Q", "SUB", "SUP", "SPAN", "BDO", "P", "H1", "H2", "H3", "H4", "H5", "H6",
         "UL", "OL", "PRE", "DIV", "BLOCKQUOTE", "HR", "ADDRESS", "INS", "DEL", "DL", "DT", "DD", "LI");

 for (String tagName : tagNames) {
 Tag tag = null;
 if (tagName.startsWith("HTML")) { // special case for HTML tag
 tag = Tag.valueOf(tagName);
 } else {
 tag = (tagName.charAt(0) == '<' && tagName.charAt(1) == '/') ? Tag.createInline(tagName.substring(1, tagName.length() - 1)) : Tag.createBlock(tagName);
 }
 System.out.println(tag.getName());
 }
 }
}