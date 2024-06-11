import org.junit.Test;
import org.znerd.xmlenc.XMLEncoder;

public class XMLEncoderTest {

  // Test declaration method
  @Test
  public void testDeclaration() {
    // Test declaration method by providing a mock writer object and verifying the output matches the expected declaration string
    Writer out = new StringWriter();
    XMLEncoder encoder = new XMLEncoder("UTF-8");
    encoder.declaration(out);
    String expectedDeclaration = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
    assertEquals(expectedDeclaration, out.toString());
  }

  // Test text method with escapeAmpersands=false
  @Test
  public void testTextWithoutAmpersandEscape() {
    // Test text method by providing a mock writer object and verifying the output matches the expected text string
    Writer out = new StringWriter();
    XMLEncoder encoder = new XMLEncoder("UTF-8");
    encoder.text(out, "Hello World", false);
    assertEquals("Hello World", out.toString());
  }

  // Test text method with escapeAmpersands=true
  @Test
  public void testTextWithAmpersandEscape() {
    // Test text method by providing a mock writer object and verifying the output matches the expected text string with escaped ampersands
    Writer out = new StringWriter();
    XMLEncoder encoder = new XMLEncoder("UTF-8");
    encoder.text(out, "Hello & World", true);
    assertEquals("Hello &amp; World", out.toString());
  }

  // Test text method with single character input
  @Test
  public void testTextWithSingleCharacter() {
    // Test text method by providing a mock writer object and verifying the output matches the expected text string with escaped characters
    Writer out = new StringWriter();
    XMLEncoder encoder = new XMLEncoder("UTF-8");
    encoder.text(out, '<');
    assertEquals("&lt;", out.toString());
  }

  // Test whitespace method
  @Test
  public void testWhitespace() {
    // Test whitespace method by providing a mock writer object and verifying the output matches the expected whitespace string
    Writer out = new StringWriter();
    XMLEncoder encoder = new XMLEncoder("UTF-8");
    encoder.whitespace(out, "  ");
    assertEquals("  ", out.toString());
  }

  // Test attribute method
  @Test
  public void testAttribute() {
    // Test attribute method by providing a mock writer object and verifying the output matches the expected attribute string
    Writer out = new StringWriter();
    XMLEncoder encoder = new XMLEncoder("UTF-8");
    encoder.attribute(out, "name", "value", '\"', true);
    assertEquals(" name=\"value\"", out.toString());
  }

}