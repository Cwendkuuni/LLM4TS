
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class TagTest {

  private Tag tag;

  @Before
  public void setUp() {
    tag = new Tag("test");
  }

  @Test
  public void testGetName() {
    assertEquals("test", tag.getName());
  }

  @Test
  public void testValueOf() {
    Tag tag = Tag.valueOf("p");
    assertEquals("p", tag.getName());
  }

  @Test
  public void testCanContain() {
    Tag child = new Tag("child");
    assertTrue(tag.canContain(child));
  }

  @Test
  public void testIsBlock() {
    assertFalse(tag.isBlock());
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
  public void testPreserveWhitespace() {
    assertFalse(tag.preserveWhitespace());
  }

  @Test
  public void testGetImplicitParent() {
    assertNull(tag.getImplicitParent());
  }

  @Test
  public void testIsValidParent() {
    Tag child = new Tag("child");
    assertTrue(tag.isValidParent(child));
  }

  @Test
  public void testEquals() {
    Tag tag2 = new Tag("test");
    assertTrue(tag.equals(tag2));
    assertFalse(tag.equals(null));
    assertFalse(tag.equals(new Object()));
  }

  @Test
  public void testHashCode() {
    assertNotNull(tag.hashCode());
  }

  @Test
  public void testToString() {
    assertEquals("test", tag.toString());
  }

}
