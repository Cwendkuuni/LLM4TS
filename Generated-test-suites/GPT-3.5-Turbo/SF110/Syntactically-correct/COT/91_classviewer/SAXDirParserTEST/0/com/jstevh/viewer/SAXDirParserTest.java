
import org.junit.*;
import static org.junit.Assert.*;

public class SAXDirParserTest {
  @Test
  public void testGetWebData() {
    SAXDirParser parser = new SAXDirParser();
    assertEquals("", parser.getWebData());
  }

  @Test
  public void testGetDirectory() {
    SAXDirParser parser = new SAXDirParser();
    assertNotNull(parser.getDirectory());
  }

  @Test
  public void testGetLocalPackages() {
    SAXDirParser parser = new SAXDirParser();
    assertNull(parser.getLocalPackages());
  }

  @Test
  public void testGetLocalBrowser() {
    SAXDirParser parser = new SAXDirParser();
    assertEquals("", parser.getLocalBrowser());
  }

  @Test
  public void testGetEditor() {
    SAXDirParser parser = new SAXDirParser();
    assertEquals("", parser.getEditor());
  }

  @Test
  public void testAcceptsLineNumber() {
    SAXDirParser parser = new SAXDirParser();
    assertFalse(parser.acceptsLineNumber());
  }

  @Test
  public void testLineNumberParameter() {
    SAXDirParser parser = new SAXDirParser();
    assertEquals("", parser.lineNumberParameter());
  }
}
