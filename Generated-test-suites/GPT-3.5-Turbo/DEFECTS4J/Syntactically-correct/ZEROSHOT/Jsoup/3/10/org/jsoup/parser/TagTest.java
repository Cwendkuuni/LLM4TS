import org.jsoup.parser.*;
import static org.junit.Assert.*;
import org.junit.Test;

public class TagTest {
  
  @Test
  public void testGetName() {
    Tag tag = Tag.valueOf("p");
    assertEquals("p", tag.getName());
  }
  
  @Test
  public void testValueOf() {
    Tag tag = Tag.valueOf("div");
    assertTrue(tag.isBlock());
    assertTrue(tag.canContainBlock());
    assertFalse(tag.isInline());
    assertFalse(tag.isData());
    assertFalse(tag.isEmpty());
    assertFalse(tag.preserveWhitespace());
  }
  
  @Test
  public void testCanContain() {
    Tag parent = Tag.valueOf("p");
    Tag child = Tag.valueOf("span");
    assertTrue(parent.canContain(child));
    
    Tag parent2 = Tag.valueOf("p");
    Tag child2 = Tag.valueOf("div");
    assertFalse(parent2.canContain(child2));
    
    Tag parent3 = Tag.valueOf("head");
    Tag child3 = Tag.valueOf("script");
    assertTrue(parent3.canContain(child3));
    
    Tag parent4 = Tag.valueOf("head");
    Tag child4 = Tag.valueOf("body");
    assertFalse(parent4.canContain(child4));
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
    Tag tag = Tag.valueOf("script");
    assertTrue(tag.isData());
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
    Tag child = Tag.valueOf("span");
    Tag parent = Tag.valueOf("div");
    child.setAncestor(parent.getName());
    assertEquals(parent, child.getImplicitParent());
  }
  
  @Test
  public void testIsValidParent() {
    Tag parent = Tag.valueOf("div");
    Tag child = Tag.valueOf("span");
    parent.setAncestor("body");
    assertFalse(parent.isValidParent(child));
  }
  
  @Test
  public void testEquals() {
    Tag tag1 = Tag.valueOf("div");
    Tag tag2 = Tag.valueOf("div");
    assertTrue(tag1.equals(tag2));
  }
  
  @Test
  public void testHashCode() {
    Tag tag1 = Tag.valueOf("div");
    Tag tag2 = Tag.valueOf("div");
    assertEquals(tag1.hashCode(), tag2.hashCode());
  }
  
  @Test
  public void testToString() {
    Tag tag = Tag.valueOf("a");
    assertEquals("a", tag.toString());
  }
}