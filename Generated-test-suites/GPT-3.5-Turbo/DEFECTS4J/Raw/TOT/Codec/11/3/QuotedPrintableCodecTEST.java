import org.junit.*;
import static org.junit.Assert.*;

public class QuotedPrintablecodecTest {

  private QuotedPrintableCodec codec;

  @Before
  public void setUp() {
    codec = new QuotedPrintableCodec();
  }

  @Test
  public void testEncode() {
    byte[] input = "Hello World".getBytes();
    byte[] expected = "Hello=20World".getBytes();
    assertArrayEquals(expected, codec.encode(input));
  }

  @Test
  public void testEncodeNullInput() {
    byte[] input = null;
    byte[] expected = null;
    assertArrayEquals(expected, codec.encode(input));
  }

  @Test
  public void testDecode() throws DecoderException {
    byte[] input = "Hello=20World".getBytes();
    byte[] expected = "Hello World".getBytes();
    assertArrayEquals(expected, codec.decode(input));
  }

  @Test
  public void testDecodeNullInput() throws DecoderException {
    byte[] input = null;
    byte[] expected = null;
    assertArrayEquals(expected, codec.decode(input));
  }

  @Test
  public void testEncodeString() throws EncoderException {
    String input = "Hello World";
    String expected = "Hello=20World";
    assertEquals(expected, codec.encode(input));
  }

  @Test
  public void testEncodeStringNullInput() throws EncoderException {
    String input = null;
    String expected = null;
    assertEquals(expected, codec.encode(input));
  }

  @Test
  public void testDecodeString() throws DecoderException {
    String input = "Hello=20World";
    String expected = "Hello World";
    assertEquals(expected, codec.decode(input));
  }

  @Test
  public void testDecodeStringNullInput() throws DecoderException {
    String input = null;
    String expected = null;
    assertEquals(expected, codec.decode(input));
  }

}