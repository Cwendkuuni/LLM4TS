import static org.junit.Assert.*;
import org.junit.Test;

public class XMLEncoderTest {
  
  @Test
  public void testGetEncoder() throws Exception {
    XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
    assertNotNull(encoder);
  }
  
  @Test(expected=IllegalArgumentException.class)
  public void testGetEncoderNullEncoding() throws Exception {
    XMLEncoder encoder = XMLEncoder.getEncoder(null);
  }
  
  @Test(expected=UnsupportedEncodingException.class)
  public void testGetEncoderUnsupportedEncoding() throws Exception {
    XMLEncoder encoder = XMLEncoder.getEncoder("UTF-16LE");
  }
  
  @Test
  public void testConstructor() throws Exception {
    XMLEncoder encoder = new XMLEncoder("UTF-8");
    assertEquals("UTF-8", encoder.getEncoding());
  }
  
  @Test(expected=UnsupportedEncodingException.class)
  public void testConstructorUnsupportedEncoding() throws Exception {
    XMLEncoder encoder = new XMLEncoder("US-ASCII");
  }
  
  @Test(expected=IllegalArgumentException.class)
  public void testConstructorNullEncoding() throws Exception {
    XMLEncoder encoder = new XMLEncoder(null);
  }
  
  @Test
  public void testDeclaration() throws Exception {
    StringWriter writer = new StringWriter();
    XMLEncoder encoder = new XMLEncoder("UTF-8");
    encoder.declaration(writer);
    assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", writer.toString());
  }
  
  @Test
  public void testText() throws Exception {
    StringWriter writer = new StringWriter();
    XMLEncoder encoder = new XMLEncoder("UTF-8");
    encoder.text(writer, "hello", true);
    assertEquals("hello", writer.toString());
  }
  
  @Test
  public void testTextWithEscape() throws Exception {
    StringWriter writer = new StringWriter();
    XMLEncoder encoder = new XMLEncoder("UTF-8");
    encoder.text(writer, "<hello>", true);
    assertEquals("&lt;hello&gt;", writer.toString());
  }
  
  @Test
  public void testTextWithInvalidChar() throws Exception {
    StringWriter writer = new StringWriter();
    XMLEncoder encoder = new XMLEncoder("UTF-8");
    try {
      encoder.text(writer, '\u0001');
      fail("InvalidXMLException not thrown");
    } catch (InvalidXMLException e) {
      // expected
    }
  }
  
  @Test
  public void testWhitespace() throws Exception {
    StringWriter writer = new StringWriter();
    XMLEncoder encoder = new XMLEncoder("UTF-8");
    encoder.whitespace(writer, "     ");
    assertEquals("     ", writer.toString());
  }
  
  @Test
  public void testAttribute() throws Exception {
    StringWriter writer = new StringWriter();
    XMLEncoder encoder = new XMLEncoder("UTF-8");
    encoder.attribute(writer, "name", "value", '"', true);
    assertEquals(" name=\"value\"", writer.toString());
  }
  
  @Test(expected=IllegalArgumentException.class)
  public void testAttributeInvalidQuotationMark() throws Exception {
    StringWriter writer = new StringWriter();
    XMLEncoder encoder = new XMLEncoder("UTF-8");
    encoder.attribute(writer, "name", "value", '#', true);
  }
}