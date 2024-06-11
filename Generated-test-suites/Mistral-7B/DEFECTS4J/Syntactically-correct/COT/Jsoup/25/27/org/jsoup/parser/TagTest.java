package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class TagTest {

 private Tag tagBlock;
 private Tag tagInline;
 private Tag tagEmpty;
 private Tag tagUnknown;

 @ org.junit.Before
 public void setUp() {
 tagBlock = Tag.valueOf("div");
 tagInline = Tag.valueOf("span");
 tagEmpty = Tag.valueOf("meta");
 tagUnknown = Tag.valueOf("unknownTag");
 }

 @Test
 public void testTagName() {
 // test getName()
 assertEquals("div", tagBlock.getName());
 assertEquals("span", tagInline.getName());
 assertEquals("meta", tagEmpty.getName());
 assertEquals("unknownTag", tagUnknown.getName());
 }

 @Test
 public void testIsBlock() {
 // test isBlock()
 assertEquals(true, tagBlock.isBlock());
 assertEquals(false, tagInline.isBlock());
 }

 @Test
 public void testFormatAsBlock() {
 // test formatAsBlock()
 assertEquals(true, tagBlock.formatAsBlock());
 assertEquals(false, tagInline.formatAsBlock());
 }

 @Test
 public void testCanContainBlock() {
 // test canContainBlock()
 assertEquals(true, tagBlock.canContainBlock());
 assertEquals(false, tagInline.canContainBlock());
 }

 @Test
 public void testIsData() {
 // test isData()
 assertFalse(tagBlock.isData());
 assertFalse(tagInline.isData());
 assertTrue(tagEmpty.isData());
 }

 @Test
 public void testIsEmpty() {
 // test isEmpty()
 assertFalse(tagBlock.isEmpty());
 assertFalse(tagInline.isEmpty());
 assertTrue(tagEmpty.isEmpty());
 }

 @Test
 public void testIsSelfClosing() {
 // test isSelfClosing()
 assertFalse(tagBlock.isSelfClosing());
 assertFalse(tagInline.isSelfClosing());
 assertFalse(tagEmpty.isSelfClosing());
 }

 @Test
 public void testIsKnownTag() {
 // test isKnownTag()
 assertTrue(Tag.isKnownTag("div"));
 assertTrue(Tag.isKnownTag("span"));
 assertTrue(Tag.isKnownTag("meta"));
 assertFalse(Tag.isKnownTag("unknownTag"));
 }

 @Test
 public void testPreserveWhitespace() {
 // test preserveWhitespace()
 assertFalse(tagBlock.preserveWhitespace());
 assertFalse(tagInline.preserveWhitespace());
 assertFalse(tagEmpty.preserveWhitespace());
 }

 @Test
 public void testEqualsAndHashCode() {
 Tag tagSame = Tag.valueOf("div");
 Tag tagDifferent = Tag.valueOf("span");
 Tag tagSameNameDifferentState = new Tag("div");

 // test equals()
 assertTrue(tagBlock.equals(tagBlock));
 assertTrue(tagBlock.equals(tagSame));
 assertFalse(tagBlock.equals(tagDifferent));
 assertFalse(tagBlock.equals(tagSameNameDifferentState));

 // test hashCode()
 assertEquals(tagBlock.hashCode(), tagSame.hashCode());
 assertNotSame(tagBlock.hashCode(), tagDifferent.hashCode());
 assertNotSame(tagBlock.hashCode(), tagSameNameDifferentState.hashCode());
 }

 @Test(expected = NullPointerException.class)
 public void testNullTagName() {
 Tag nullTag = new Tag(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyTagName() {
 Tag emptyTag = new Tag("");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidTagName() {
 Tag invalidTag = new Tag("&tag");
 }

 @Test(expected = IllegalStateException.class)
 public void testDuplicateTagName() {
 Tag duplicateTag = Tag.valueOf("div");
 Tag duplicateTag2 = new Tag("div"); // should fail
 }

 @Test
 public void testTagRegistration() {
 Map<String, Tag> map = new HashMap<String, Tag>();
 Tag tagRegistered = new Tag("registeredTag");
 Tag tagRegisteredAgain = new Tag("registeredTag");

 Tag.register(tagRegistered);
 Tag.register(tagRegisteredAgain);

 assertEquals(tagRegistered, Tag.valueOf("registeredTag"));
 assertEquals(tagRegistered, Tag.valueOf("RegisteredTag"));
 }
}