import org.junit.Before;
import org.junit.Test;
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
    Tag testTag = Tag.valueOf("testTag");
    assertNotNull(testTag);
  }

  @Test
  public void testIsBlock() {
    assertFalse(tag.isBlock());
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
    assertTrue(tag.isInline());
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
    assertTrue(tag.isKnownTag());
  }

  @Test
  public void testIsKnownSpecificTag() {
    assertTrue(Tag.isKnownTag("testTag"));
  }

  @Test
  public void testPreserveWhitespace() {
    assertFalse(tag.preserveWhitespace());
  }

  @Test
  public void testSetSelfClosing() {
    tag.setSelfClosing();
    assertTrue(tag.isSelfClosing());
  }

  @Test
  public void testEquals() {
    Tag testTag = new Tag("testTag");
    assertTrue(tag.equals(testTag));
  }
  
  @Test
  public void testHashCode() {
    Tag testTag = new Tag("testTag");
    assertEquals(tag.hashCode(), testTag.hashCode());
  }
  
  @Test
  public void testToString() {
    assertEquals("testTag", tag.toString());
  }

}