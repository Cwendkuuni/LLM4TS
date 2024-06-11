import org.junit.Test;

public class TagTest {
  
  @Test
  public void testGetName() {
    Tag tag = Tag.valueOf("p");
    assert(tag.getName().equals("p"));
  }
  
  @Test
  public void testIsBlock() {
    Tag tag = Tag.valueOf("p");
    assert(tag.isBlock() == true);
  }
  
  @Test
  public void testFormatAsBlock() {
    Tag tag = Tag.valueOf("p");
    assert(tag.formatAsBlock() == true);
  }
  
  @Test
  public void testCanContainBlock() {
    Tag tag = Tag.valueOf("p");
    assert(tag.canContainBlock() == true);
  }
  
  @Test
  public void testIsInline() {
    Tag tag = Tag.valueOf("p");
    assert(tag.isInline() == false);
  }
  
  @Test
  public void testIsData() {
    Tag tag = Tag.valueOf("p");
    assert(tag.isData() == false);
  }
  
  @Test
  public void testIsEmpty() {
    Tag tag = Tag.valueOf("p");
    assert(tag.isEmpty() == false);
  }
  
  @Test
  public void testIsSelfClosing() {
    Tag tag = Tag.valueOf("p");
    assert(tag.isSelfClosing() == false);
  }
  
  @Test
  public void testIsKnownTag() {
    assert(Tag.isKnownTag("p") == true);
  }
  
  @Test
  public void testPreserveWhitespace() {
    Tag tag = Tag.valueOf("p");
    assert(tag.preserveWhitespace() == false);
  }
}