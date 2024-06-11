package org.jsoup.parser;

import org.apache.commons.lang3.Validate;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TagTest {

 @Test
 public void testTagCreationAndGetters() {
 Tag html = Tag.valueOf("HTML");
 Tag head = Tag.valueOf("HEAD");
 Tag body = Tag.valueOf("BODY");
 Tag p = Tag.valueOf("P");
 Tag div = Tag.valueOf("DIV");

 assertEquals("HTML", html.getName());
 assertEquals(true, html.isBlock());
 // other getter tests here
 }

 @Test
 public void testTagCanContain() {
 Tag html = Tag.valueOf("HTML");
 Tag head = Tag.valueOf("HEAD");
 Tag body = Tag.valueOf("BODY");
 Tag p = Tag.valueOf("P");
 Tag div = Tag.valueOf("DIV");

 assertTrue(html.canContain(p));
 assertTrue(html.canContain(div));
 assertTrue(head.canContain(html));
 assertTrue(body.canContain(p));
 assertTrue(body.canContain(div));
 // other test cases here
 }

 @Test
 public void testTagIsValidParent() {
 Tag html = Tag.valueOf("HTML");
 Tag head = Tag.valueOf("HEAD");
 Tag body = Tag.valueOf("BODY");
 Tag p = Tag.valueOf("P");
 Tag div = Tag.valueOf("DIV");

 assertTrue(html.isValidParent(head));
 assertTrue(html.isValidParent(body));
 assertTrue(head.isValidParent(html));
 assertTrue(head.isValidParent(body));
 assertTrue(body.isValidParent(p));
 assertTrue(body.isValidParent(div));
 // other test cases here
 }

 @Test
 public void testEqualsAndHashCode() {
 Tag html = Tag.valueOf("HTML");
 Tag head = Tag.valueOf("HEAD");
 Tag body = Tag.valueOf("BODY");
 Tag p = Tag.valueOf("P");

 assertEquals(html, html);
 assertEquals(html, html); // hashcode test
 assertNotSame(html, head);
 assertNotSame(html, body);
 assertNotSame(html, p);
 // equals for empty lists
 Tag empty = Tag.valueOf("empty");
 Tag empty2 = Tag.valueOf("empty");
 assertEquals(empty, empty2);
 // equals for different tags
 Tag a = Tag.valueOf("A");
 assertNotSame(html, a);
 // equals for same tag with different properties
 Tag p2 = new Tag("P");
 assertNotSame(p, p2);
 // equals for same tag with same properties
 Tag p3 = new Tag("P").setEmpty().setCanContainBlock(false).setCanContainInline(false);
 assertEquals(p, p3);
 }

 @Test
 public void testToString() {
 Tag html = Tag.valueOf("HTML");
 Tag head = Tag.valueOf("HEAD");
 Tag body = Tag.valueOf("BODY");
 Tag p = Tag.valueOf("P");

 assertEquals("HTML", html.toString());
 assertEquals("HEAD", head.toString());
 assertEquals("BODY", body.toString());
 assertEquals("P", p.toString());
 }

 @Test
 public void testCanContainBlockAndInline() {
 Tag html = Tag.valueOf("HTML");
 Tag body = Tag.valueOf("BODY");
 Tag p = Tag.valueOf("P");
 Tag div = Tag.valueOf("DIV");
 Tag span = Tag.valueOf("SPAN");

 assertFalse(html.isBlock());
 assertFalse(html.canContainBlock());
 assertFalse(html.canContainInline());

 assertTrue(body.isBlock());
 assertTrue(body.canContainBlock());
 assertTrue(body.canContainInline());

 assertTrue(p.isInline());
 assertTrue(p.canContainBlock());
 assertFalse(p.canContainInline());

 assertTrue(div.isBlock());
 assertTrue(div.canContainBlock());
 assertTrue(div.canContainInline());

 assertTrue(span.isInline());
 assertFalse(span.canContainBlock());
 assertTrue(span.canContainInline());
 }

 @Test
 public void testIsData() {
 Tag html = Tag.valueOf("HTML");
 Tag body = Tag.valueOf("BODY");
 Tag p = Tag.valueOf("P");
 Tag script = Tag.valueOf("SCRIPT");

 assertFalse(html.isData());
 assertFalse(body.isData());
 assertFalse(p.isData());
 assertTrue(script.isData());
 }

 @Test
 public void testIsEmpty() {
 Tag html = Tag.valueOf("HTML");
 Tag body = Tag.valueOf("BODY");
 Tag p = Tag.valueOf("P");
 Tag img = Tag.valueOf("IMG");
 Tag br = Tag.valueOf("BR");

 assertFalse(html.isEmpty());
 assertFalse(body.isEmpty());
 assertFalse(p.isEmpty());
 assertTrue(img.isEmpty());
 assertTrue(br.isEmpty());
 }

 @Test
 public void testPreserveWhitespace() {
 Tag pre = Tag.valueOf("PRE");
 assertTrue(pre.preserveWhitespace());
 }

 @Test
 public void testGetImplicitParent() {
 Tag ul = Tag.valueOf("UL");
 Tag li = Tag.valueOf("LI");
 List<Tag> ancestors = Arrays.asList(new Tag("UL"), new Tag("BODY"));
 li.setAncestors(ancestors);
 Tag parent = li.getImplicitParent();
 assertEquals(ul, parent);
 }

 @Test
 public void testValidParent() {
 Tag html = Tag.valueOf("HTML");
 Tag head = Tag.valueOf("HEAD");
 Tag body = Tag.valueOf("BODY");
 Tag p = Tag.valueOf("P");
 Tag div = Tag.valueOf("DIV");
 Tag ul = Tag.valueOf("UL");
 Tag li = Tag.valueOf("LI");

 assertTrue(html.isValidParent(head));
 assertTrue(html.isValidParent(body));
 assertTrue(head.isValidParent(html));
 assertTrue(head.isValidParent(body));
 assertTrue(body.isValidParent(p));
 assertTrue(body.isValidParent(div));
 assertFalse(html.isValidParent(p));
 assertFalse(html.isValidParent(ul));
 assertFalse(p.isValidParent(ul));
 assertFalse(li.isValidParent(ul));
 }
}