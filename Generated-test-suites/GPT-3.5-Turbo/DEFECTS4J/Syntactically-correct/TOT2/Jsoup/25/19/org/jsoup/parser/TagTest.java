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
      assertNotNull(tag);
  }
  
  @Test
  public void testIsBlock() {
      Tag tag = Tag.valueOf("div");
      assertTrue(tag.isBlock());
  }
  
  @Test
  public void testFormatAsBlock() {
      Tag tag = Tag.valueOf("div");
      assertTrue(tag.formatAsBlock());
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
      Tag tag = Tag.valueOf("div");
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
      assertTrue(Tag.isKnownTag("div"));
  }
  
  @Test
  public void testPreserveWhitespace() {
      Tag tag = Tag.valueOf("pre");
      assertTrue(tag.preserveWhitespace());
  }
  
  @Test
  public void testSetSelfClosing() {
      Tag tag = new Tag("img");
      tag.setSelfClosing();
      assertTrue(tag.isSelfClosing());
  }
  
}