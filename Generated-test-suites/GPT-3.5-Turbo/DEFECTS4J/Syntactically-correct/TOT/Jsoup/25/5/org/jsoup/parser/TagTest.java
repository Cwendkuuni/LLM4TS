package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class TagTest {

  @Test
  public void testGetName() {
    Tag tag = new Tag("div");
    assertEquals("div", tag.getName());
  }

  @Test
  public void testValueOf() {
    Tag tag = Tag.valueOf("p");
    assertFalse(tag.isBlock());
    assertTrue(tag.canContainBlock());
  }

  @Test
  public void testIsBlock() {
    Tag tag = new Tag("p");
    assertFalse(tag.isBlock());
  }

  @Test
  public void testFormatAsBlock() {
    Tag tag = new Tag("p");
    assertTrue(tag.formatAsBlock());
  }

  @Test
  public void testCanContainBlock() {
    Tag tag = new Tag("p");
    assertTrue(tag.canContainBlock());
  }

  @Test
  public void testIsInline() {
    Tag tag = new Tag("div");
    assertTrue(tag.isInline());
  }

  @Test
  public void testIsData() {
    Tag tag = new Tag("div");
    assertFalse(tag.isData());
  }

  @Test
  public void testIsEmpty() {
    Tag tag = new Tag("img");
    assertTrue(tag.isEmpty());
  }

  @Test
  public void testIsSelfClosing() {
    Tag tag = new Tag("img");
    assertTrue(tag.isSelfClosing());
  }

  @Test
  public void testIsKnownTag() {
    assertTrue(Tag.isKnownTag("p"));
  }

  @Test
  public void testPreserveWhitespace() {
    Tag tag = new Tag("pre");
    assertTrue(tag.preserveWhitespace());
  }

  // Additional test cases
  @Test
  public void testEquals() {
    Tag tag1 = new Tag("div");
    Tag tag2 = new Tag("div");
    assertTrue(tag1.equals(tag2));
  }

  @Test
  public void testHashCode() {
    Tag tag1 = new Tag("div");
    Tag tag2 = new Tag("div");
    assertEquals(tag1.hashCode(), tag2.hashCode());
  }

}