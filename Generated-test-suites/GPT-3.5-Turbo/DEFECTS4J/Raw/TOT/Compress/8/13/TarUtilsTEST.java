import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

  @Test
  public void testParseOctal_validInput() {
    byte[] buffer = "  777  ".getBytes();
    long expected = 777;
    
    long result = TarUtils.parseOctal(buffer, 0, buffer.length);
    
    assertEquals(expected, result);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testParseOctal_invalidInput() {
    byte[] buffer = "  78z  ".getBytes();
    
    TarUtils.parseOctal(buffer, 0, buffer.length);
  }

  @Test
  public void testParseName_validInput() {
    byte[] buffer = "hello world\0".getBytes();
    String expected = "hello world";
    
    String result = TarUtils.parseName(buffer, 0, buffer.length);
    
    assertEquals(expected, result);
  }

  @Test
  public void testParseName_nullTerminationMissing() {
    byte[] buffer = "hello world".getBytes();
    String expected = "hello world";
    
    String result = TarUtils.parseName(buffer, 0, buffer.length);
    
    assertEquals(expected, result);
  }

  @Test
  public void testFormatNameBytes_exactFit() {
    String name = "hello world";
    byte[] expected = "hello world\0\0\0".getBytes();
    byte[] buffer = new byte[expected.length];
    
    int result = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
    
    assertEquals(expected.length, result);
    assertArrayEquals(expected, buffer);
  }

  @Test
  public void testFormatNameBytes_nameTooLong() {
    String name = "hello world";
    byte[] expected = "hello\0\0\0\0\0".getBytes();
    byte[] buffer = new byte[expected.length];
    
    int result = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
    
    assertEquals(expected.length, result);
    assertArrayEquals(expected, buffer);
  }

  @Test
  public void testFormatUnsignedOctalString_exactFit() {
    long value = 123456;
    byte[] expected = "1170340 ".getBytes();
    byte[] buffer = new byte[expected.length];
    
    TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
    
    assertArrayEquals(expected, buffer);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFormatUnsignedOctalString_overflow() {
    long value = 1234567890123456789L;
    byte[] buffer = new byte[20];
    
    TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
  }

  @Test
  public void testFormatOctalBytes_exactFit() {
    long value = 7654321;
    byte[] expected = "036110461 \0".getBytes();
    byte[] buffer = new byte[expected.length];
    
    int result = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
    
    assertEquals(expected.length, result);
    assertArrayEquals(expected, buffer);
  }

  @Test
  public void testFormatLongOctalBytes_exactFit() {
    long value = 9876543;
    byte[] expected = "00003616457 ".getBytes();
    byte[] buffer = new byte[expected.length];
    
    int result = TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
    
    assertEquals(expected.length, result);
    assertArrayEquals(expected, buffer);
  }

  @Test
  public void testFormatCheckSumOctalBytes_exactFit() {
    long value = 654321;
    byte[] expected = "036573 }\0".getBytes();
    byte[] buffer = new byte[expected.length];
    
    int result = TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);
    
    assertEquals(expected.length, result);
    assertArrayEquals(expected, buffer);
  }
  
  @Test
  public void testComputeCheckSum_validInput() {
    byte[] buffer = "hello world".getBytes();
    long expected = 1116;
    
    long result = TarUtils.computeCheckSum(buffer);
    
    assertEquals(expected, result);
  }

}