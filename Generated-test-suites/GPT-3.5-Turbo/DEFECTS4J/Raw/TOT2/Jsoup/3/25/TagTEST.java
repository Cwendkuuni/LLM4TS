import org.junit.Test;
import static org.junit.Assert.*;

public class TagTest {

  @Test
  public void testName() {
    Tag tag = Tag.valueOf("div");
    assertEquals("div", tag.getName());
  }

  @Test
  public void testCanContain() {
    Tag parentTag = Tag.valueOf("div");
    Tag childTag = Tag.valueOf("span");
    assertTrue(parentTag.canContain(childTag));
  }

  @Test
  public void testIsBlock() {
    Tag tag = Tag.valueOf("div");
    assertTrue(tag.isBlock());
  }

  @Test
  public void testCanContainBlock() {
    Tag tag = Tag.valueOf("div");
    assertTrue(tag.canContainBlock());
  }

  @Test
  public void testIsInline() {
    Tag tag = Tag.valueOf("span");
    assertTrue(tag.isInline());
  }

  @Test
  public void testIsData() {
    Tag tag = Tag.valueOf("span");
    assertFalse(tag.isData());
  }

  @Test
  public void testIsEmpty() {
    Tag tag = Tag.valueOf("img");
    assertTrue(tag.isEmpty());
  }

  @Test
  public void testPreserveWhitespace() {
    Tag tag = Tag.valueOf("pre");
    assertTrue(tag.preserveWhitespace());
  }

  @Test
  public void testGetImplicitParent() {
    Tag tag = Tag.valueOf("div");
    assertNull(tag.getImplicitParent());
  }

  @Test
  public void testIsValidParent() {
    Tag parentTag = Tag.valueOf("div");
    Tag childTag = Tag.valueOf("p");
    assertTrue(parentTag.isValidParent(childTag));
  }

}