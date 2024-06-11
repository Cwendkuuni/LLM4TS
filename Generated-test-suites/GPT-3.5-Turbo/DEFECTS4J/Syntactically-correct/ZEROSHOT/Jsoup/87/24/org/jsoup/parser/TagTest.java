package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class TagTest {
  
  private Tag tag;

  @Before
  public void setUp() {
      tag = new Tag("testTag");
  }

  @Test
  public void testGetName() {
      assertEquals("testTag", tag.getName());
  }

  @Test
  public void testValueOf() {
      assertEquals(tag, Tag.valueOf("testTag"));
  }

  @Test
  public void testValueOfWithSettings() {
      ParseSettings settings = ParseSettings.preserveCase;
      assertEquals(tag, Tag.valueOf("testTag", settings));
  }

  @Test
  public void testIsBlock() {
      assertTrue(tag.isBlock());
  }

  @Test
  public void testFormatAsBlock() {
      assertTrue(tag.formatAsBlock());
  }

  @Test
  public void testCanContainBlock() {
      assertTrue(tag.canContainBlock());
  }

  @Test
  public void testIsInline() {
      assertFalse(tag.isInline());
  }

  @Test
  public void testIsData() {
      assertFalse(tag.isData());
  }

  @Test
  public void testIsEmpty() {
      assertFalse(tag.isEmpty());
  }

  @Test
  public void testIsSelfClosing() {
      assertFalse(tag.isSelfClosing());
  }

  @Test
  public void testIsKnownTag() {
      assertFalse(tag.isKnownTag());
  }

  @Test
  public void testIsKnownTagWithName() {
      assertFalse(Tag.isKnownTag("testTag"));
  }

  @Test
  public void testPreserveWhitespace() {
      assertFalse(tag.preserveWhitespace());
  }

  @Test
  public void testIsFormListed() {
      assertFalse(tag.isFormListed());
  }

  @Test
  public void testIsFormSubmittable() {
      assertFalse(tag.isFormSubmittable());
  }

  @Test
  public void testSetSelfClosing() {
      tag.setSelfClosing();
      assertTrue(tag.isSelfClosing());
  }

  @Test
  public void testEquals() {
      assertTrue(tag.equals(tag));
      assertFalse(tag.equals(null));
      assertFalse(tag.equals(new Object()));

      Tag equalTag = new Tag("testTag");
      assertTrue(tag.equals(equalTag));

      Tag differentTag = new Tag("differentTag");
      assertFalse(tag.equals(differentTag));
  }

  @Test
  public void testHashCode() {
      Tag equalTag = new Tag("testTag");
      assertEquals(tag.hashCode(), equalTag.hashCode());
  }

  @Test
  public void testToString() {
      assertEquals("testTag", tag.toString());
  }

  @Test
  public void testTagInitialization() {
      assertTrue(Tag.valueOf("html").isBlock());
      assertFalse(Tag.valueOf("object").isBlock());
      assertTrue(Tag.valueOf("br").isEmpty());
      assertTrue(Tag.valueOf("a").formatAsBlock());
      assertTrue(Tag.valueOf("pre").preserveWhitespace());
      assertTrue(Tag.valueOf("button").isFormListed());
      assertTrue(Tag.valueOf("input").isFormSubmittable());
  }

}