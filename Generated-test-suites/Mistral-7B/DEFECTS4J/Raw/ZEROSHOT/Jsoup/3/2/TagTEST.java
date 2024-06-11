package org.jsoup.parser;

import org.apache.commons.lang3.Validate;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TagTest {

 @Test
 public void testTagCreationAndEquals() {
 Tag tag1 = new Tag("tag1");
 Tag tag2 = new Tag("tag1");
 Tag tag3 = new Tag("tag2");
 Tag tag4 = new Tag("Tag1");

 assertTrue(tag1.equals(tag1));
 assertTrue(tag1.equals(tag2));
 assertFalse(tag1.equals(tag3));
 assertFalse(tag1.equals(tag4));

 assertFalse(tag3.equals(tag1));
 assertFalse(tag3.equals(tag2));
 assertFalse(tag3.equals(tag4));

 assertTrue(tag2.equals(tag1));
 assertTrue(tag2.equals(tag2));
 assertFalse(tag2.equals(tag3));
 assertFalse(tag2.equals(tag4));

 assertFalse(tag4.equals(tag1));
 assertFalse(tag4.equals(tag2));
 assertFalse(tag4.equals(tag3));
 assertTrue(tag4.equals(tag4));
 }

 @Test
 public void testTagCanContain() {
 Tag body = new Tag("BODY");
 Tag html = new Tag("HTML");
 Tag head = new Tag("HEAD");
 Tag title = new Tag("TITLE");
 Tag p = new Tag("P");
 Tag div = new Tag("DIV");
 Tag ul = new Tag("UL");
 Tag li = new Tag("LI");
 Tag h1 = new Tag("H1");
 Tag span = new Tag("SPAN");
 Tag script = new Tag("SCRIPT");
 Tag object = new Tag("OBJECT");
 Tag base = new Tag("BASE");
 Tag link = new Tag("LINK");
 Tag meta = new Tag("META");

 assertTrue(body.canContain(html));
 assertTrue(html.canContain(body));
 assertTrue(body.canContain(head));
 assertTrue(head.canContain(body));
 assertTrue(head.canContain(title));
 assertTrue(title.canContain(head));
 assertTrue(head.canContain(script));
 assertTrue(script.canContain(head));

 assertTrue(body.canContain(p));
 assertTrue(p.canContain(body));

 assertTrue(body.canContain(div));
 assertTrue(div.canContain(body));

 assertTrue(ul.canContain(li));
 assertTrue(li.canContain(ul));

 assertTrue(body.canContain(span));
 assertTrue(span.canContain(body));

 assertTrue(p.canContain(h1));
 assertTrue(h1.canContain(p));

 assertFalse(script.canContain(object));
 assertFalse(object.canContain(script));

 assertFalse(base.canContain(link));
 assertFalse(link.canContain(base));

 assertFalse(meta.canContain(link));
 assertFalse(link.canContain(meta));
 }

 @Test
 public void testTagIsBlockAndInline() {
 Tag body = new Tag("BODY");
 Tag html = new Tag("HTML");
 Tag head = new Tag("HEAD");
 Tag title = new Tag("TITLE");
 Tag p = new Tag("P");
 Tag div = new Tag("DIV");
 Tag ul = new Tag("UL");
 Tag li = new Tag("LI");
 Tag h1 = new Tag("H1");
 Tag span = new Tag("SPAN");
 Tag script = new Tag("SCRIPT");

 assertTrue(body.isBlock());
 assertTrue(html.isBlock());
 assertTrue(head.isBlock());
 assertTrue(title.isBlock());
 assertTrue(p.isBlock());
 assertTrue(div.isBlock());
 assertTrue(ul.isBlock());
 assertTrue(li.isInline());
 assertTrue(li.isBlock()); // li is block in this context
 assertTrue(h1.isBlock());
 assertTrue(span.isInline());

 assertFalse(script.isBlock());
 assertFalse(script.isInline());
 }

 @Test
 public void testTagCanContainBlockAndInline() {
 Tag body = new Tag("BODY");
 Tag html = new Tag("HTML");
 Tag head = new Tag("HEAD");
 Tag title = new Tag("TITLE");
 Tag p = new Tag("P");
 Tag div = new Tag("DIV");
 Tag ul = new Tag("UL");
 Tag li = new Tag("LI");
 Tag h1 = new Tag("H1");
 Tag span = new Tag("SPAN");
 Tag script = new Tag("SCRIPT");
 Tag object = new Tag("OBJECT");
 Tag base = new Tag("BASE");
 Tag link = new Tag("LINK");
 Tag meta = new Tag("META");

 assertTrue(body.canContainBlock());
 assertTrue(html.canContainBlock());
 assertTrue(head.canContainBlock());
 assertTrue(title.canContainBlock());
 assertTrue(p.canContainBlock());
 assertTrue(div.canContainBlock());
 assertTrue(ul.canContainBlock());
 assertTrue(li.canContainInline());
 assertTrue(li.canContainBlock()); // li is block in this context
 assertTrue(h1.canContainBlock());
 assertTrue(span.canContainInline());

 assertFalse(script.canContainBlock());
 assertFalse(script.canContainInline());

 assertFalse(object.canContainBlock());
 assertFalse(object.canContainInline());

 assertTrue(base.canContainInline());
 assertFalse(base.canContainBlock());

 assertTrue(link.canContainInline());
 assertFalse(link.canContainBlock());

 assertTrue(meta.canContainInline());
 assertFalse(meta.canContainBlock());
 }

 @Test
 public void testTagIsEmpty() {
 Tag body = new Tag("BODY");
 Tag html = new Tag("HTML");
 Tag head = new Tag("HEAD");
 Tag title = new Tag("TITLE");
 Tag p = new Tag("P");
 Tag div = new Tag("DIV");
 Tag ul = new Tag("UL");
 Tag li = new Tag("LI");
 Tag h1 = new Tag("H1");
 Tag span = new Tag("SPAN");
 Tag img = new Tag("IMG");
 Tag br = new Tag("BR");
 Tag map = new Tag("MAP");

 assertTrue(img.isEmpty());
 assertTrue(img.isBlock());
 assertFalse(img.canContainBlock());
 assertFalse(img.canContainInline());

 assertTrue(br.isEmpty());
 assertFalse(br.isBlock());
 assertFalse(br.canContainBlock());
 assertFalse(br.canContainInline());

 assertTrue(p.isEmpty());
 assertFalse(p.isBlock());
 assertFalse(p.canContainBlock());
 assertFalse(p.canContainInline());

 assertTrue(title.isEmpty());
 assertFalse(title.isBlock());
 assertFalse(title.canContainBlock());
 assertFalse(title.canContainInline());

 assertTrue(h1.isEmpty());
 assertFalse(h1.isBlock());
 assertFalse(h1.canContainBlock());
 assertFalse(h1.canContainInline());

 assertTrue(span.isEmpty());
 assertFalse(span.isBlock());
 assertFalse(span.canContainBlock());
 assertFalse(span.canContainInline());

 assertFalse(div.isEmpty());
 assertTrue(div.isBlock());
 assertFalse(div.canContainBlock());
 assertFalse(div.canContainInline());

 assertFalse(ul.isEmpty());
 assertTrue(ul.isBlock());
 assertFalse(ul.canContainBlock());
 assertTrue(ul.canContainInline());

 assertFalse(li.isEmpty());
 assertFalse(li.isBlock());
 assertFalse(li.canContainBlock());
 assertTrue(li.canContainInline());

 assertFalse(html.isEmpty());
 assertTrue(html.isBlock());
 assertFalse(html.canContainBlock());
 assertFalse(html.canContainInline());

 assertFalse(body.isEmpty());
 assertTrue(body.isBlock());
 assertFalse(body.canContainBlock());
 assertFalse(body.canContainInline());
 }

 @Test
 public void testTagPreserveWhitespace() {
 Tag p = new Tag("P");
 Tag pre = new Tag("PRE");

 assertFalse(p.preserveWhitespace());
 assertFalse(p.isBlock());
 assertFalse(p.canContainBlock());
 assertFalse(p.canContainInline());

 assertTrue(pre.preserveWhitespace());
 assertTrue(pre.isBlock());
 assertFalse(pre.canContainBlock());
 assertFalse(pre.canContainInline());
 }

 @Test
 public void testTagGetAncestors() {
 Tag body = new Tag("BODY");
 Tag html = new Tag("HTML");
 Tag head = new Tag("HEAD");
 Tag title = new Tag("TITLE");
 Tag p = new Tag("P");
 Tag div = new Tag("DIV");
 Tag ul = new Tag("UL");
 Tag li = new Tag("LI");
 Tag h1 = new Tag("H1");

 body.setAncestors(Arrays.asList(html, head));
 html.setAncestors(Collections.singletonList(null));
 head.setAncestors(Collections.singletonList(html));
 title.setAncestors(Collections.singletonList(head));
 p.setAncestors(Arrays.asList(body));
 div.setAncestors(Arrays.asList(body));
 ul.setAncestors(Arrays.asList(body));
 li.setAncestors(Arrays.asList(ul));
 h1.setAncestors(Arrays.asList(p));

 List<Tag> bodyAncestors = body.getImplicitParents();
 List<Tag> htmlAncestors = html.getImplicitParents();
 List<Tag> headAncestors = head.getImplicitParents();
 List<Tag> titleAncestors = title.getImplicitParents();
 List<Tag> pAncestors = p.getImplicitParents();
 List<Tag> divAncestors = div.getImplicitParents();
 List<Tag> ulAncestors = ul.getImplicitParents();
 List<Tag> liAncestors = li.getImplicitParents();
 List<Tag> h1Ancestors = h1.getImplicitParents();

 assertNotNull(bodyAncestors);
 assertNotNull(htmlAncestors);
 assertNotNull(headAncestors);
 assertNotNull(titleAncestors);
 assertNotNull(pAncestors);
 assertNotNull(divAncestors);
 assertNotNull(ulAncestors);
 assertNotNull(liAncestors);
 assertNotNull(h1Ancestors);

 assertTrue(bodyAncestors.contains(html));
 assertTrue(bodyAncestors.contains(head));
 assertTrue(headAncestors.contains(html));
 assertTrue(titleAncestors.contains(head));
 assertTrue(pAncestors.contains(body));
 assertTrue(divAncestors.contains(body));
 assertTrue(ulAncestors.contains(body));
 assertTrue(liAncestors.contains(ul));
 assertTrue(h1Ancestors.contains(p));
 }

 @Test
 public void testTagIsData() {
 Tag body = new Tag("BODY");
 Tag script = new Tag("SCRIPT");

 assertFalse(body.isData());
 assertFalse(body.isBlock());
 assertFalse(body.canContainBlock());
 assertFalse(body.canContainInline());

 assertTrue(script.isData());
 assertFalse(script.isBlock());
 assertFalse(script.canContainBlock());
 assertFalse(script.canContainInline());
 }

 @Test
 public void testTagIsValidParent() {
 Tag body = new Tag("BODY");
 Tag p = new Tag("P");
 Tag div = new Tag("DIV");

 assertTrue(body.isValidParent(p));
 assertTrue(body.isValidParent(div));
 assertFalse(p.isValidParent(p));
 assertFalse(p.isValidParent(div));
 }

 @Test
 public void testTagGetName() {
 Tag body = new Tag("BODY");
 Tag html = new Tag("HTML");
 Tag head = new Tag("HEAD");
 Tag title = new Tag("TITLE");
 Tag p = new Tag("P");
 Tag div = new Tag("DIV");
 Tag ul = new Tag("UL");
 Tag li = new Tag("LI");
 Tag h1 = new Tag("H1");

 assertEquals("BODY", body.getName());
 assertEquals("HTML", html.getName());
 assertEquals("HEAD", head.getName());
 assertEquals("TITLE", title.getName());
 assertEquals("P", p.getName());
 assertEquals("DIV", div.getName());
 assertEquals("UL", ul.getName());
 assertEquals("LI", li.getName());
 assertEquals("H1", h1.getName());
 }

 @Test
 public void testTagValueOf() {
 Tag tag1 = Tag.valueOf("tag1");
 Tag tag2 = Tag.valueOf("TAG1");
 Tag tag3 = Tag.valueOf("TAG2");
 Tag tag4 = Tag.valueOf("tag4");

 assertEquals("tag1", tag1.getName());
 assertEquals("TAG1", tag2.getName());
 assertEquals("TAG2", tag3.getName());
 assertEquals("tag4", tag4.getName());
 }
}