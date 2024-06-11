
import org.junit.Test;
import static org.junit.Assert.*;

public class TagTest {
  
  @Test
  public void testGetName() {
    Tag tag = Tag.valueOf("div");
    assertEquals("div", tag.getName());
  }
  
  @Test
  public void testValueOf() {
    Tag tag = Tag.valueOf("p");
    assertEquals("p", tag.getName());
    assertTrue(tag.isInline());
    assertTrue(tag.canContainInline());
  }
  
  @Test
  public void testCanContain() {
    Tag parent = Tag.valueOf("div");
    Tag child1 = Tag.valueOf("p");
    Tag child2 = Tag.valueOf("span");
    assertTrue(parent.canContain(child1));
    assertTrue(parent.canContain(child2));
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
    Tag child = Tag.valueOf("a");
    assertNull(child.getImplicitParent());
    
    Tag parent = Tag.valueOf("div");
    parent.setAncestor("body");
    child.setAncestor("div");
    assertEquals(parent, child.getImplicitParent());
  }
  
  @Test
  public void testIsValidParent() {
    Tag parent = Tag.valueOf("div");
    Tag child = Tag.valueOf("span");
    assertTrue(parent.isValidParent(child));
  }
  
  @Test
  public void testEquals() {
    Tag tag1 = Tag.valueOf("a");
    Tag tag2 = Tag.valueOf("a");
    assertTrue(tag1.equals(tag2));
    assertFalse(tag1.equals(null));
    assertFalse(tag1.equals(new Object()));
  }
  
  @Test
  public void testHashCode() {
    Tag tag1 = Tag.valueOf("div");
    Tag tag2 = Tag.valueOf("div");
    assertEquals(tag1.hashCode(), tag2.hashCode());
  }
  
  @Test
  public void testToString() {
    Tag tag = Tag.valueOf("div");
    assertEquals("div", tag.toString());
  }
}
