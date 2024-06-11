import org.jsoup.nodes.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class XmlDeclarationTest {
  
  private XmlDeclaration xmlDeclaration;
  
  @Before
  public void setUp() {
    xmlDeclaration = new XmlDeclaration("name", "baseUri", true);
  }
  
  @Test
  public void testNodeName() {
    assertEquals("#declaration", xmlDeclaration.nodeName());
  }
  
  @Test
  public void testName() {
    assertEquals("name", xmlDeclaration.name());
  }
  
  @Test
  public void testGetWholeDeclaration() {
    assertEquals("name", xmlDeclaration.getWholeDeclaration());
  }
  
  @Test
  public void testOuterHtmlHead() {
    StringBuilder sb = new StringBuilder();
    try {
      xmlDeclaration.outerHtmlHead(sb, 0, new Document.OutputSettings());
    } catch (Exception e) {
      fail("Exception occurred: " + e.getMessage());
    }
    
    assertEquals("<?name>", sb.toString());
  }
  
  @Test
  public void testOuterHtmlTail() {
    StringBuilder sb = new StringBuilder();
    try {
      xmlDeclaration.outerHtmlTail(sb, 0, new Document.OutputSettings());
    } catch (Exception e) {
      fail("Exception occurred: " + e.getMessage());
    }
    
    assertEquals("", sb.toString());
  }
  
  @Test
  public void testToString() {
    assertEquals("<?name>", xmlDeclaration.toString());
  }
}