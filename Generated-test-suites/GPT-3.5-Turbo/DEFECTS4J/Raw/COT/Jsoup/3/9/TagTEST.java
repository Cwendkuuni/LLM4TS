import static org.junit.Assert.*;
import org.junit.Test;

public class TagTest {
  
  @Test
  public void testGetName() {
    Tag tag = Tag.valueOf("p");
    assertEquals("p", tag.getName());
  }
  
  @Test
  public void testCanContain() {
    Tag parent = Tag.valueOf("div");
    Tag child = Tag.valueOf("p");
    assertTrue(parent.canContain(child));
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
    Tag tag = Tag.valueOf("p");
    assertTrue(tag.isInline());
  }
  
  @Test
  public void testIsData() {
    Tag tag = Tag.valueOf("p");
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
    Tag tag = Tag.valueOf("p");
    assertNull(tag.getImplicitParent());
  }
  
  @Test
  public void testIsValidParent() {
    Tag parent = Tag.valueOf("div");
    Tag child = Tag.valueOf("p");
    assertTrue(parent.isValidParent(child));
  }
  
  @Test
  public void testEquals() {
    Tag tag1 = Tag.valueOf("p");
    Tag tag2 = Tag.valueOf("p");
    assertTrue(tag1.equals(tag2));
  }
  
  @Test
  public void testHashCode() {
    Tag tag = Tag.valueOf("p");
    assertNotNull(tag.hashCode());
  }
  
  @Test
  public void testToString() {
    Tag tag = Tag.valueOf("p");
    assertNotNull(tag.toString());
  }
  
}