package org.jsoup.parser.test;

import org.apache.commons.lang3.Validate;
import org.jsoup.parser.Tag;
import org.junit.Test;

import static org.junit.Assert.*;

public class TagTest {

 @Test
 public void testTagValues() {
 Tag tagEmpty = Tag.valueOf("html");
 Tag tagInline = Tag.valueOf("a");
 Tag tagBlock = Tag.valueOf("div");
 Tag tagData = Tag.valueOf("script");

 assertEquals("html", tagEmpty.getName());
 assertEquals("a", tagInline.getName());
 assertEquals("div", tagBlock.getName());
 assertEquals("script", tagData.getName());
 }

 @Test
 public void testDefaultAncestor() {
 Tag defaultAncestor = Tag.valueOf("html");
 Tag body = Tag.valueOf("body");

 assertTrue(defaultAncestor.canContain(body));
 }

 @Test
 public void testInvalidTagName() {
 try {
 Tag tagInvalid = Tag.valueOf(" ");
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testUnknownTag() {
 Tag tagUnknown = Tag.valueOf("unknown");
 Tag tagEmpty = Tag.valueOf("html");

 assertNotSame(tagEmpty, tagUnknown);
 assertTrue(tagUnknown.isData());
 }

 @Test
 public void testCanContainBlock() {
 Tag div = Tag.valueOf("div");
 Tag p = Tag.valueOf("p");

 assertTrue(div.canContain(p));
 }

 @Test
 public void testCannotContainBlock() {
 Tag p = Tag.valueOf("p");
 Tag div = Tag.valueOf("div");

 assertFalse(p.canContain(div));
 }

 @Test
 public void testCannotContainInline() {
 Tag div = Tag.valueOf("div");
 Tag span = Tag.valueOf("span");

 assertFalse(div.canContain(span));
 }

 @Test
 public void testCanContainInline() {
 Tag p = Tag.valueOf("p");
 Tag span = Tag.valueOf("span");

 assertTrue(p.canContain(span));
 }

 @Test
 public void testOptionalClosing() {
 Tag div = Tag.valueOf("div");
 Tag div2 = Tag.valueOf("div");

 assertFalse(div.canContain(div2));
 }

 @Test
 public void testCanContainSelf() {
 Tag p = Tag.valueOf("p");

 assertFalse(p.canContain(p));
 }

 @Test
 public void testEmptyTag() {
 Tag img = Tag.valueOf("img");

 assertTrue(img.isEmpty());
 }

 @Test
 public void testSetAncestor() {
 Tag body = Tag.valueOf("body");
 Tag div = Tag.valueOf("div");
 Tag p = Tag.valueOf("p");

 body.setAncestor("html");
 div.setAncestor(body);
 p.setAncestor(div);

 assertTrue(p.isValidParent(body));
 assertTrue(p.isValidParent(div));
 assertFalse(p.isValidParent(p));
 }

 @Test
 public void testSetAncestorNull() {
 Tag tag = Tag.valueOf("tag");
 tag.setAncestor(null);

 assertTrue(tag.ancestors.isEmpty());
 }

 @Test
 public void testSetAncestorEmptyList() {
 Tag tag = Tag.valueOf("tag");
 tag.setAncestor(new String[0]);

 assertTrue(tag.ancestors.isEmpty());
 }

 @Test
 public void testSetAncestorList() {
 Tag head = Tag.valueOf("head");
 Tag body = Tag.valueOf("body");
 Tag html = Tag.valueOf("html");

 html.setAncestor("html");
 head.setAncestor(html);
 body.setAncestor(html);

 assertTrue(head.isValidParent(body));
 }

 @Test
 public void testEquals() {
 Tag tag1 = Tag.valueOf("div");
 Tag tag2 = Tag.valueOf("div");
 Tag tag3 = Tag.valueOf("p");

 assertTrue(tag1.equals(tag1));
 assertTrue(tag1.equals(tag2));
 assertFalse(tag1.equals(tag3));
 }

 @Test
 public void testHashCode() {
 Tag tag1 = Tag.valueOf("div");
 Tag tag2 = Tag.valueOf("div");
 Tag tag3 = Tag.valueOf("p");

 int hashCode1 = tag1.hashCode();
 int hashCode2 = tag1.hashCode();
 int hashCode3 = tag3.hashCode();

 assertEquals(hashCode1, hashCode2);
 assertNotEquals(hashCode1, hashCode3);
 }

 @Test
 public void testToString() {
 Tag div = Tag.valueOf("div");

 assertEquals("div", div.toString());
 }

}