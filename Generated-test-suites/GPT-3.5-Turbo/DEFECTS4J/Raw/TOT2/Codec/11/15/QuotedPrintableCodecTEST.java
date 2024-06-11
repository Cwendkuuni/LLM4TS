import org.junit.Test;
import static org.junit.Assert.*;

public class QuotedPrintableCodecTest {
  
  @Test
  public void testEncode() {
    QuotedPrintableCodec codec = new QuotedPrintableCodec();
    String input = "Hello World";
    String expected = "=48=65=6C=6C=6F=20=57=6F=72=6C=64";
    assertEquals(expected, codec.encode(input));
  }
  
  @Test
  public void testDecode() {
    QuotedPrintableCodec codec = new QuotedPrintableCodec();
    String input = "=48=65=6C=6C=6F=20=57=6F=72=6C=64";
    String expected = "Hello World";
    assertEquals(expected, codec.decode(input));
  }
  
  @Test
  public void testNullEncoding() {
    QuotedPrintableCodec codec = new QuotedPrintableCodec();
    assertNull(codec.encode(null));
  }
  
  @Test
  public void testNullDecoding() {
    QuotedPrintableCodec codec = new QuotedPrintableCodec();
    assertNull(codec.decode(null));
  }
  
  @Test
  public void testUnsupportedCharset() {
    QuotedPrintableCodec codec = new QuotedPrintableCodec();
    String input = "Hello World";
    assertThrows(UnsupportedEncodingException.class, () -> {
      codec.encode(input, "InvalidCharset");
    });
  }
  
  @Test
  public void testObjectEncoding() {
    QuotedPrintableCodec codec = new QuotedPrintableCodec();
    Object input = "Hello World";
    String expected = "=48=65=6C=6C=6F=20=57=6F=72=6C=64";
    assertEquals(expected, codec.encode(input));
  }
  
  @Test
  public void testObjectDecoding() {
    QuotedPrintableCodec codec = new QuotedPrintableCodec();
    Object input = "=48=65=6C=6C=6F=20=57=6F=72=6C=64";
    String expected = "Hello World";
    assertEquals(expected, codec.decode(input));
  }
  
}