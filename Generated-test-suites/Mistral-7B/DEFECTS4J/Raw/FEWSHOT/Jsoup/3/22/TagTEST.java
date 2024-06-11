package org.jsoup.parser;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import java.util.Arrays;
import org.junit.Test;

public class TagTest {

 Tag pTag = new Tag("P");
 Tag h1Tag = new Tag("H1");
 Tag ulTag = new Tag("UL");
 Tag liTag = new Tag("LI");
 Tag tableTag = new Tag("TABLE");
 Tag trTag = new Tag("TR");
 Tag thTag = new Tag("TH");
 Tag tdTag = new Tag("TD");

 @Test
 public void testCanContainBlock() {
 assertTrue(pTag.canContain(pTag));
 assertTrue(pTag.canContain(h1Tag));
 assertTrue(ulTag.canContain(liTag));
 assertTrue(liTag.canContain(pTag));
 assertTrue(liTag.canContain(h1Tag));
 assertTrue(tableTag.canContain(trTag));
 assertTrue(trTag.canContain(tdTag));
 assertTrue(tdTag.canContain(pTag));
 assertTrue(tdTag.canContain(h1Tag));
 }

 @Test
 public void testCannotContainBlock() {
 assertFalse(h1Tag.canContain(pTag));
 assertFalse(tdTag.canContain(h1Tag));
 assertFalse(tdTag.canContain(pTag));
 }

 @Test
 public void testIsDataOnlyTag() {
 assertTrue(scriptTag.isData());
 assertTrue(styleTag.isData());
 }

 @Test
 public void testIsEmptyTag() {
 assertTrue(imgTag.isEmpty());
 assertTrue(brTag.isEmpty());
 }

 @Test
 public void testIsOptionalClosingTag() {
 assertTrue(pTag.optionalClosing);
 assertFalse(h1Tag.optionalClosing);
 }

 @Test
 public void testIsContainedInAncestors() {
 Tag headTag = new Tag("HEAD");
 Tag bodyTag = new Tag("BODY");
 headTag.ancestors = Arrays.asList(headTag, bodyTag);
 linkTag.ancestors = Arrays.asList(headTag, bodyTag);
 assertTrue(headTag.isValidParent(linkTag));
 }

 @Test
 public void testIsNotContainedInAncestors() {
 Tag bodyTag = new Tag("BODY");
 pTag.ancestors = Arrays.asList(pTag, bodyTag);
 assertFalse(bodyTag.isValidParent(pTag));
 }

 @Test
 public void testDefaultAncestor() {
 Tag bodyTag = new Tag("BODY");
 Tag imgTag = new Tag("IMG");
 Tag htmlTag = new Tag("HTML");
 imgTag.setAncestor(htmlTag.tagName);
 assertTrue(bodyTag.canContain(imgTag));
 }

}