import static org.junit.Assert.*;
import org.junit.Test;

public class QuotedPrintableCodecTest {
  
  @Test
  public void testEncode() {
    QuotedPrintableCodec codec = new QuotedPrintableCodec();
    byte[] input = "Hello World".getBytes();
    byte[] expected = "Hello World".getBytes();
    byte[] result = codec.encode(input);
    
    assertArrayEquals(expected, result);
  }
  
  @Test
  public void testDecode() {
    QuotedPrintableCodec codec = new QuotedPrintableCodec();
    byte[] input = "Hello=20World".getBytes();
    byte[] expected = "Hello World".getBytes();
    byte[] result = codec.decode(input);
    
    assertArrayEquals(expected, result);
  }
  
  @Test
  public void testEncodeWithCharset() {
    QuotedPrintableCodec codec = new QuotedPrintableCodec("UTF-8");
    String input = "Hello World";
    String expected = "Hello World";
    String result = codec.encode(input);
    
    assertEquals(expected, result);
  }
  
  @Test
  public void testDecodeWithCharset() {
    QuotedPrintableCodec codec = new QuotedPrintableCodec("UTF-8");
    String input = "Hello=20World";
    String expected = "Hello World";
    String result = codec.decode(input);
    
    assertEquals(expected, result);
  }
  
  @Test
  public void testEncodeObject() {
    QuotedPrintableCodec codec = new QuotedPrintableCodec();
    String input = "Hello World";
    byte[] expected = "Hello World".getBytes();
    Object result = codec.encode(input);
    
    assertArrayEquals(expected, (byte[]) result);
  }
  
  @Test
  public void testDecodeObject() {
    QuotedPrintableCodec codec = new QuotedPrintableCodec();
    byte[] input = "Hello=20World".getBytes();
    String expected = "Hello World";
    Object result = codec.decode(input);
    
    assertEquals(expected, (String) result);
  }
}