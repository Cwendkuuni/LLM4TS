
import org.junit.*;
import static org.junit.Assert.*;

public class TagTest {
  
  @Test
  public void testGetName() {
    Tag tag = new Tag("div");
    assertEquals("div", tag.getName());
  }
  
  @Test
  public void testValueOfWithParseSettings() {
    Tag tag = Tag.valueOf("div", ParseSettings.preserveCase);
    assertEquals("div", tag.getName());
  }
  
  @Test
  public void testValueOfWithoutParseSettings() {
    Tag tag = Tag.valueOf("div");
    assertEquals("div", tag.getName());
  }
  
  @Test
  public void testIsBlock() {
    Tag tag = new Tag("div");
    assertTrue(tag.isBlock());
  }
  
  @Test
  public void testFormatAsBlock() {
    Tag tag = new Tag("div");
    assertTrue(tag.formatAsBlock());
  }
  
  @Test
  public void testCanContainBlock() {
    Tag tag = new Tag("div");
    assertTrue(tag.canContainBlock());
  }
  
  @Test
  public void testIsInline() {
    Tag tag = new Tag("div");
    assertFalse(tag.isInline());
  }
  
  @Test
  public void testIsData() {
    Tag tag1 = new Tag("div");
    assertFalse(tag1.isData());
    
    Tag tag2 = new Tag("div");
    tag2.canContainInline = false;
    assertTrue(tag2.isData());
  }
  
  @Test
  public void testIsEmpty() {
    Tag tag = new Tag("div");
    assertFalse(tag.isEmpty());
  }
  
  @Test
  public void testIsSelfClosing() {
    Tag tag1 = new Tag("div");
    assertFalse(tag1.isSelfClosing());
    
    Tag tag2 = new Tag("div");
    tag2.empty = true;
    assertTrue(tag2.isSelfClosing());
  }
  
  @Test
  public void testIsKnownTag() {
    assertFalse(Tag.isKnownTag("div"));
    assertFalse(Tag.isKnownTag("span"));
    assertFalse(Tag.isKnownTag("customTag"));
  }
  
  @Test
  public void testPreserveWhitespace() {
    Tag tag1 = new Tag("div");
    assertFalse(tag1.preserveWhitespace());
    
    Tag tag2 = new Tag("div");
    tag2.tagName = "pre";
    assertTrue(tag2.preserveWhitespace());
  }
  
  @Test
  public void testIsFormListed() {
    Tag tag1 = new Tag("div");
    assertFalse(tag1.isFormListed());
    
    Tag tag2 = new Tag("div");
    tag2.formList = true;
    assertTrue(tag2.isFormListed());
  }
  
  @Test
  public void testIsFormSubmittable() {
    Tag tag1 = new Tag("div");
    assertFalse(tag1.isFormSubmittable());
    
    Tag tag2 = new Tag("div");
    tag2.formSubmit = true;
    assertTrue(tag2.isFormSubmittable());
  }
  
  @Test
  public void testEquals() {
    Tag tag1 = new Tag("div");
    Tag tag2 = new Tag("div");
    assertTrue(tag1.equals(tag2));
  }
  
  @Test
  public void testHashCode() {
    Tag tag1 = new Tag("div");
    Tag tag2 = new Tag("div");
    assertEquals(tag1.hashCode(), tag2.hashCode());
  }
  
  @Test
  public void testToString() {
    Tag tag = new Tag("div");
    assertEquals("div", tag.toString());
  }
}
