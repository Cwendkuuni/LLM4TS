import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.parser.ParseSettings;

public class TagTest {

  @Test
  public void testGetName() {
    Tag tag = new Tag("test");
    assertEquals("test", tag.getName());
  }

  @Test
  public void testValueOfWithParseSettings() {
    Tag tag = Tag.valueOf("div", ParseSettings.preserveCase);
    assertNotNull(tag);
    assertEquals("div", tag.getName());
  }

  @Test
  public void testValueOfWithoutParseSettings() {
    Tag tag = Tag.valueOf("input");
    assertNotNull(tag);
    assertEquals("input", tag.getName());
  }

  @Test
  public void testIsBlock() {
    Tag tag = new Tag("test");
    assertTrue(tag.isBlock());
  }

  @Test
  public void testFormatAsBlock() {
    Tag tag1 = new Tag("test");
    assertTrue(tag1.formatAsBlock());

    Tag tag2 = new Tag("div");
    assertTrue(tag2.formatAsBlock());
  }

  @Test
  public void testCanContainBlock() {
    Tag tag1 = new Tag("test");
    assertTrue(tag1.canContainBlock());

    Tag tag2 = new Tag("div");
    assertTrue(tag2.canContainBlock());
  }

  @Test
  public void testIsInline() {
    Tag tag = new Tag("test");
    assertFalse(tag.isInline());
  }

  @Test
  public void testIsData() {
    Tag tag = new Tag("test");
    assertFalse(tag.isData());
  }

  @Test
  public void testIsEmpty() {
    Tag tag = new Tag("test");
    assertFalse(tag.isEmpty());
  }

  @Test
  public void testIsSelfClosing() {
    Tag tag = new Tag("test");
    assertFalse(tag.isSelfClosing());
  }

  @Test
  public void testIsKnownTag() {
    Tag tag1 = new Tag("test");
    assertFalse(tag1.isKnownTag());

    Tag tag2 = new Tag("div");
    assertTrue(tag2.isKnownTag());
  }

  @Test
  public void testIsKnownTagWithString() {
    assertTrue(Tag.isKnownTag("div"));
    assertFalse(Tag.isKnownTag("test"));
  }

  @Test
  public void testPreserveWhitespace() {
    Tag tag = new Tag("test");
    assertFalse(tag.preserveWhitespace());
  }

  @Test
  public void testIsFormListed() {
    Tag tag = new Tag("test");
    assertFalse(tag.isFormListed());
  }

  @Test
  public void testIsFormSubmittable() {
    Tag tag = new Tag("test");
    assertFalse(tag.isFormSubmittable());
  }

  @Test
  public void testSetSelfClosing() {
    Tag tag = new Tag("test");
    tag.setSelfClosing();
    assertTrue(tag.isSelfClosing());
  }

  @Test
  public void testEquals() {
    Tag tag1 = new Tag("test");
    Tag tag2 = new Tag("test");
    assertTrue(tag1.equals(tag2));
  }

  @Test
  public void testHashCode() {
    Tag tag = new Tag("test");
    assertEquals(3528017, tag.hashCode());
  }

  @Test
  public void testToString() {
    Tag tag = new Tag("test");
    assertEquals("test", tag.toString());
  }

  // Additional test cases for edge cases and exceptions
  @Test
  public void testValueOfNullTagName() {
    try {
      Tag.valueOf(null, ParseSettings.preserveCase);
      fail("No exception thrown");
    } catch (IllegalArgumentException e) {
      assertEquals("tagName must not be null", e.getMessage());
    }
  }

  @Test
  public void testValueOfEmptyTagName() {
    try {
      Tag.valueOf("", ParseSettings.preserveCase);
      fail("No exception thrown");
    } catch (IllegalArgumentException e) {
      assertEquals("tagName must not be empty", e.getMessage());
    }
  }

  @Test
  public void testValueOfUnknownTagName() {
    Tag tag = Tag.valueOf("test");
    assertNotNull(tag);
    assertEquals("test", tag.getName());
  }

  @Test
  public void testEqualsDifferentTagName() {
    Tag tag1 = new Tag("test1");
    Tag tag2 = new Tag("test2");
    assertFalse(tag1.equals(tag2));
  }

  @Test
  public void testHashCodeDifferentTagName() {
    Tag tag1 = new Tag("test1");
    Tag tag2 = new Tag("test2");
    assertNotEquals(tag1.hashCode(), tag2.hashCode());
  }

}