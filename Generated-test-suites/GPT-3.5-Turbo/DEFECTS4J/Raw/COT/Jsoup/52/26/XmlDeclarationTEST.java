
import static org.junit.Assert.*;
import org.junit.Test;

public class XmlDeclarationTest {
  
  @Test
  public void testNodeName() {
    XmlDeclaration declaration = new XmlDeclaration("xml", "http://example.com", false);
    assertEquals("#declaration", declaration.nodeName());
  }
  
  @Test
  public void testName() {
    XmlDeclaration declaration = new XmlDeclaration("xml", "http://example.com", false);
    assertEquals("xml", declaration.name());
  }
  
  @Test
  public void testGetWholeDeclaration() {
    XmlDeclaration declaration = new XmlDeclaration("xml", "http://example.com", false);
    assertEquals("xml", declaration.getWholeDeclaration());
  }
  
  @Test
  public void testGetWholeDeclarationWithAttributes() {
    XmlDeclaration declaration = new XmlDeclaration("xml", "http://example.com", false);
    declaration.attributes.put("version", "1.0");
    declaration.attributes.put("encoding", "utf-8");
    assertEquals("xml version=\"1.0\" encoding=\"utf-8\"", declaration.getWholeDeclaration());
  }
  
  // Edge Cases and Exception Handling
  @Test
  public void testGetWholeDeclarationWithEmptyName() {
    XmlDeclaration declaration = new XmlDeclaration("", "http://example.com", false);
    assertEquals("", declaration.getWholeDeclaration());
  }
  
  @Test(expected = NullPointerException.class)
  public void testConstructorWithNullName() {
    XmlDeclaration declaration = new XmlDeclaration(null, "http://example.com", false);
  }
  
  @Test
  public void testToString() {
    XmlDeclaration declaration = new XmlDeclaration("xml", "http://example.com", false);
    assertEquals("<!xml>", declaration.toString());
  }

}
