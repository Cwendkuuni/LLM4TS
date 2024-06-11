package org.jsoup.parser;

import org.apache.commons.lang3.Validate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class TagTest {

 private static final Tag HTML = Tag.valueOf("HTML");
 private static final Tag HEAD = Tag.valueOf("HEAD");
 private static final Tag BODY = Tag.valueOf("BODY");
 private static final Tag DIV = Tag.valueOf("DIV");
 private static final Tag P = Tag.valueOf("P");
 private static final Tag SPAN = Tag.valueOf("SPAN");
 private static final Tag A = Tag.valueOf("A");
 private static final Tag UL = Tag.valueOf("UL");
 private static final Tag LI = Tag.valueOf("LI");
 private static final Tag TABLE = Tag.valueOf("TABLE");
 private static final Tag TR = Tag.valueOf("TR");
 private static final Tag TD = Tag.valueOf("TD");

 @Test
 public void testTagCreation() {
 Validate.notNull(HTML);
 Validate.notNull(HEAD);
 Validate.notNull(BODY);
 Validate.notNull(DIV);
 Validate.notNull(P);
 Validate.notNull(SPAN);
 Validate.notNull(A);
 Validate.notNull(UL);
 Validate.notNull(LI);
 Validate.notNull(TABLE);
 Validate.notNull(TR);
 Validate.notNull(TD);
 }

 @Test
 public void testTagCanContain() {
 // HTML can contain all
 assertTrue(HTML.canContain(HTML));
 assertTrue(HTML.canContain(HEAD));
 assertTrue(HTML.canContain(BODY));
 assertTrue(HTML.canContain(DIV));
 // Head can only contain a few
 assertFalse(HEAD.canContain(BODY));
 assertFalse(HEAD.canContain(DIV));
 assertFalse(HEAD.canContain(P));
 // P can contain SPAN
 assertTrue(P.canContain(SPAN));
 // A can't contain itself
 assertFalse(A.canContain(A));
 // A can contain SPAN
 assertTrue(A.canContain(SPAN));
 // Ul can contain Li
 assertTrue(UL.canContain(LI));
 // Li can contain P
 assertTrue(LI.canContain(P));
 // Tr can contain Td
 assertTrue(TR.canContain(TD));
 // Td can contain P
 assertTrue(TD.canContain(P));
 // Table can contain Tr
 assertTrue(TABLE.canContain(TR));
 // Tr can contain Td
 assertTrue(TR.canContain(TD));
 // Td can contain P
 assertTrue(TD.canContain(P));
 }

 @Test
 public void testTagIsBlock() {
 // HTML is block
 assertTrue(HTML.isBlock());
 // Head is not block
 assertFalse(HEAD.isBlock());
 // Body is block
 assertTrue(BODY.isBlock());
 // DIV is block
 assertTrue(DIV.isBlock());
 // P is block
 assertTrue(P.isBlock());
 // Span is inline
 assertFalse(SPAN.isBlock());
 // A is inline
 assertFalse(A.isBlock());
 // Ul is block
 assertTrue(UL.isBlock());
 // Li is block
 assertTrue(LI.isBlock());
 // TR is block
 assertTrue(TR.isBlock());
 // TD is block
 assertTrue(TD.isBlock());
 // Table is block
 assertTrue(TABLE.isBlock());
 }

 @Test
 public void testTagCanContainBlock() {
 // HTML can contain block
 assertTrue(HTML.canContainBlock());
 // Head can contain block
 assertTrue(HEAD.canContainBlock());
 // Body can contain block
 assertTrue(BODY.canContainBlock());
 // DIV can contain block
 assertTrue(DIV.canContainBlock());
 // P can contain block (only inline)
 assertFalse(P.canContainBlock());
 // Span can contain block (only inline)
 assertFalse(SPAN.canContainBlock());
 // Ul can contain block (only inline)
 assertFalse(UL.canContainBlock());
 // Li can contain block (only inline)
 assertFalse(LI.canContainBlock());
 // TR can contain block
 assertTrue(TR.canContainBlock());
 // TD can contain block
 assertTrue(TD.canContainBlock());
 // Table can contain block
 assertTrue(TABLE.canContainBlock());
 }

 @Test
 public void testTagIsEmpty() {
 // HTML is not empty
 assertFalse(HTML.isEmpty());
 // Head is not empty
 assertFalse(HEAD.isEmpty());
 // Body is not empty
 assertFalse(BODY.isEmpty());
 // DIV is not empty
 assertFalse(DIV.isEmpty());
 // P is not empty
 assertFalse(P.isEmpty());
 // Span is not empty
 assertFalse(SPAN.isEmpty());
 // A is not empty
 assertFalse(A.isEmpty());
 // Ul is not empty
 assertFalse(UL.isEmpty());
 // Li is not empty
 assertFalse(LI.isEmpty());
 // TR is not empty
 assertFalse(TR.isEmpty());
 // TD is not empty
 assertFalse(TD.isEmpty());
 // Table is not empty
 assertFalse(TABLE.isEmpty());
 }

 @Test
 public void testTagIsData() {
 // HTML is not data
 assertFalse(HTML.isData());
 // Head is not data
 assertFalse(HEAD.isData());
 // Body is not data
 assertFalse(BODY.isData());
 // DIV is not data
 assertFalse(DIV.isData());
 // P is not data
 assertFalse(P.isData());
 // Span is not data
 assertFalse(SPAN.isData());
 // A is not data
 assertFalse(A.isData());
 // Ul is not data
 assertFalse(UL.isData());
 // Li is not data
 assertFalse(LI.isData());
 // TR is not data
 assertFalse(TR.isData());
 // TD is not data
 assertFalse(TD.isData());
 // Table is not data
 assertFalse(TABLE.isData());
 // Script is data
 assertTrue(Tag.valueOf("SCRIPT").isData());
 }

 @Test
 public void testTagPreserveWhitespace() {
 // HTML does not preserve whitespace
 assertFalse(HTML.preserveWhitespace());
 // Pre does preserve whitespace
 assertTrue(Tag.valueOf("PRE").preserveWhitespace());
 }

 @Test
 public void testTagGetImplicitParent() {
 // HTML has no parent
 assertNull(HTML.getImplicitParent());
 // Head has parent HTML
 assertEquals(HTML, HEAD.getImplicitParent());
 // Body has parent HTML
 assertEquals(HTML, BODY.getImplicitParent());
 }

 @Test
 public void testTagIsValidParent() {
 // P is valid parent for SPAN
 assertTrue(P.isValidParent(SPAN));
 // SPAN is valid parent for P
 assertTrue(SPAN.isValidParent(P));
 // TR is valid parent for TD
 assertTrue(TR.isValidParent(TD));
 // TD is valid parent for TR
 assertTrue(TD.isValidParent(TR));
 // TR is not valid parent for TR
 assertFalse(TR.isValidParent(TR));
 // TD is not valid parent for TD
 assertFalse(TD.isValidParent(TD));
 // TD is not valid parent for TR when TD is ancestor of TR
 assertFalse(TD.isValidParent(TR, Arrays.asList(DIV, TR)));
 }

 @Test
 public void testTagEqualsAndHashCode() {
 Tag tag1 = Tag.valueOf("DIV");
 Tag tag2 = Tag.valueOf("DIV");
 Tag tag3 = Tag.valueOf("P");
 Tag tag4 = new Tag("DIV");

 // Tag is equals itself
 assertEquals(tag1, tag1);
 // Tag is equal to itself, regardless of case
 assertEquals(tag1, Tag.valueOf("div"));
 // Tags with same name are equal
 assertEquals(tag1, tag2);
 // Tags with different names are not equal
 assertFalse(tag1.equals(tag3));
 // New Tag with same name is equal to the registered one
 assertEquals(tag1, tag4);
 // Hashcodes are equal for equal tags
 assertEquals(tag1.hashCode(), tag2.hashCode());
 // Hashcodes are not equal for different tags
 assertFalse(tag1.hashCode() == tag3.hashCode());
 }

 @Test
 public void testToString() {
 assertEquals("DIV", DIV.toString());
 }
}