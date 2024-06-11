
package org.jsoup.parser;

import static org.junit.Assert.*;
import org.junit.Test;

public class TagTest {

  @Test
  public void testGetName() {
    Tag tag = new Tag("testTag");
    assertEquals("testTag", tag.getName());
  }
  
  @Test
  public void testValueOfWithSettings() {
    Tag tag = Tag.valueOf("p", ParseSettings.preserveCase);
    assertEquals("p", tag.getName());
    assertFalse(tag.isBlock());
  }
  
  @Test
  public void testValueOfWithoutSettings() {
    Tag tag = Tag.valueOf("p");
    assertEquals("p", tag.getName());
    assertFalse(tag.isBlock());
  }
  
  @Test
  public void testIsBlock() {
    Tag tag = new Tag("testTag");
    assertTrue(tag.isBlock());
    tag.setSelfClosing();
    assertFalse(tag.isBlock());
  }
  
  @Test
  public void testFormatAsBlock() {
    Tag tag = new Tag("testTag");
    assertTrue(tag.formatAsBlock());
    tag.setSelfClosing();
    assertTrue(tag.formatAsBlock());
  }
  
  @Test
  public void testCanContainBlock() {
    Tag tag = new Tag("testTag");
    assertTrue(tag.canContainBlock());
    tag.setSelfClosing();
    assertTrue(tag.canContainBlock());
  }
  
  @Test
  public void testIsInline() {
    Tag tag = new Tag("testTag");
    assertFalse(tag.isInline());
    tag.setSelfClosing();
    assertTrue(tag.isInline());
  }
  
  @Test
  public void testIsData() {
    Tag tag1 = new Tag("testTag");
    assertFalse(tag1.isData());
    Tag tag2 = new Tag("testTag2");
    assertFalse(tag2.isData());
    tag2.setSelfClosing();
    assertFalse(tag2.isData());
    Tag tag3 = new Tag("testTag3");
    tag3.empty = true;
    assertTrue(tag3.isData());
  }
  
  @Test
  public void testIsEmpty() {
    Tag tag = new Tag("testTag");
    assertFalse(tag.isEmpty());
    tag.empty = true;
    assertTrue(tag.isEmpty());
  }
  
  @Test
  public void testIsSelfClosing() {
    Tag tag1 = new Tag("testTag");
    assertFalse(tag1.isSelfClosing());
    Tag tag2 = new Tag("testTag2");
    tag2.empty = true;
    assertFalse(tag2.isSelfClosing());
    Tag tag3 = new Tag("testTag3");
    tag3.setSelfClosing();
    assertTrue(tag3.isSelfClosing());
  }
  
  @Test
  public void testIsKnownTag() {
    assertFalse(Tag.isKnownTag("unknownTag"));
    assertTrue(Tag.isKnownTag("p"));
  }
  
  @Test
  public void testPreserveWhitespace() {
    Tag tag1 = new Tag("testTag");
    assertFalse(tag1.preserveWhitespace());
    Tag tag2 = new Tag("testTag2");
    tag2.preserveWhitespace = true;
    assertTrue(tag2.preserveWhitespace());
  }
  
  @Test
  public void testIsFormListed() {
    Tag tag1 = new Tag("testTag");
    assertFalse(tag1.isFormListed());
    Tag tag2 = new Tag("testTag2");
    tag2.formList = true;
    assertTrue(tag2.isFormListed());
  }
  
  @Test
  public void testIsFormSubmittable() {
    Tag tag1 = new Tag("testTag");
    assertFalse(tag1.isFormSubmittable());
    Tag tag2 = new Tag("testTag2");
    tag2.formSubmit = true;
    assertTrue(tag2.isFormSubmittable());
  }
  
  @Test
  public void testEquals() {
    Tag tag1 = new Tag("testTag");
    Tag tag2 = new Tag("testTag");
    assertTrue(tag1.equals(tag2));
  }
  
  @Test
  public void testHashCode() {
    Tag tag1 = new Tag("testTag");
    Tag tag2 = new Tag("testTag");
    assertEquals(tag1.hashCode(), tag2.hashCode());
  }
  
  @Test
  public void testToString() {
    Tag tag = new Tag("testTag");
    assertEquals("testTag", tag.toString());
  }
  
}

