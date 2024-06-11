import static org.junit.Assert.*;
import org.junit.Test;

public class TarUtilsTest {
  
  // Test cases for parseOctal method
  @Test
  public void testParseOctal_withValidOctalString_ReturnsLongValue() {
    byte[] buffer = "123\0".getBytes();
    int offset = 0;
    int length = 4;
    
    long result = TarUtils.parseOctal(buffer, offset, length);
    
    assertEquals(83, result);
  }
  
  @Test(expected=IllegalArgumentException.class)
  public void testParseOctal_withInvalidOctalString_ThrowsIllegalArgumentException() {
    byte[] buffer = "789\0".getBytes();
    int offset = 0;
    int length = 4;
    
    TarUtils.parseOctal(buffer, offset, length);
  }
  
  @Test
  public void testParseOctal_withAllNULBytes_ReturnsZero() {
    byte[] buffer = "\0\0\0\0".getBytes();
    int offset = 0;
    int length = 4;
    
    long result = TarUtils.parseOctal(buffer, offset, length);
    
    assertEquals(0, result);
  }
  
  // Test cases for parseName method
  @Test
  public void testParseName_withValidName_ReturnsString() {
    byte[] buffer = "filename\0".getBytes();
    int offset = 0;
    int length = 9;
    
    String result = TarUtils.parseName(buffer, offset, length);
    
    assertEquals("filename", result);
  }
  
  // Test cases for formatNameBytes method
  @Test
  public void testFormatNameBytes_withValidName_CopiesCharactersIntoBuffer() {
    String name = "filename";
    byte[] buf = new byte[10];
    int offset = 0;
    int length = 10;
    
    int result = TarUtils.formatNameBytes(name, buf, offset, length);
    
    assertArrayEquals("filename\0".getBytes(), buf);
    assertEquals(10, result);
  }
  
  @Test
  public void testFormatNameBytes_withNameLongerThanBuffer_TruncatesOutput() {
    String name = "longfilename";
    byte[] buf = new byte[5];
    int offset = 0;
    int length = 5;
    
    int result = TarUtils.formatNameBytes(name, buf, offset, length);
    
    assertArrayEquals("longf".getBytes(), buf);
    assertEquals(5, result);
  }
  
  @Test
  public void testFormatNameBytes_withBufferLongerThanName_FillsWithNULs() {
    String name = "name";
    byte[] buf = new byte[10];
    int offset = 0;
    int length = 10;
    
    int result = TarUtils.formatNameBytes(name, buf, offset, length);
    
    byte[] expected = "name\0\0\0\0\0\0".getBytes();
    assertArrayEquals(expected, buf);
    assertEquals(10, result);
  }
  
  // Test cases for formatUnsignedOctalString method
  @Test
  public void testFormatUnsignedOctalString_withZeroValue_FillsBufferWithZeroValue() {
    long value = 0;
    byte[] buffer = new byte[5];
    int offset = 0;
    int length = 5;
    
    TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
    
    assertArrayEquals("00000".getBytes(), buffer);
  }
  
  @Test
  public void testFormatUnsignedOctalString_withNonZeroValue_FillsBufferWithOctalValue() {
    long value = 123;
    byte[] buffer = new byte[5];
    int offset = 0;
    int length = 5;
    
    TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
    
    assertArrayEquals("00173".getBytes(), buffer);
  }
  
  @Test(expected=IllegalArgumentException.class)
  public void testFormatUnsignedOctalString_withBufferTooSmallForValue_ThrowsIllegalArgumentException() {
    long value = 123456;
    byte[] buffer = new byte[4];
    int offset = 0;
    int length = 4;
    
    TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
  }
  
  // Test cases for formatOctalBytes method
  @Test
  public void testFormatOctalBytes_withValidValue_CopiesOctalStringIntoBuffer() {
    long value = 123;
    byte[] buf = new byte[5];
    int offset = 0;
    int length = 5;
    
    int result = TarUtils.formatOctalBytes(value, buf, offset, length);
    
    assertArrayEquals("00173 ".getBytes(), buf);
    assertEquals(5, result);
  }
  
  @Test(expected=IllegalArgumentException.class)
  public void testFormatOctalBytes_withBufferTooSmallForTrailer_ThrowsIllegalArgumentException() {
    long value = 123;
    byte[] buf = new byte[5];
    int offset = 0;
    int length = 4;
    
    TarUtils.formatOctalBytes(value, buf, offset, length);
  }
  
  // Test cases for formatLongOctalBytes method
  @Test
  public void testFormatLongOctalBytes_withValidValue_CopiesOctalStringIntoBuffer() {
    long value = 123;
    byte[] buf = new byte[4];
    int offset = 0;
    int length = 4;
    
    int result = TarUtils.formatLongOctalBytes(value, buf, offset, length);
    
    assertArrayEquals("00173".getBytes(), buf);
    assertEquals(4, result);
  }
  
  // Test cases for formatCheckSumOctalBytes method
  @Test
  public void testFormatCheckSumOctalBytes_withValidValue_CopiesOctalStringIntoBuffer() {
    long value = 123;
    byte[] buf = new byte[5];
    int offset = 0;
    int length = 5;
    
    int result = TarUtils.formatCheckSumOctalBytes(value, buf, offset, length);
    
    assertArrayEquals("00173\0".getBytes(), buf);
    assertEquals(5, result);
  }
  
  @Test(expected=IllegalArgumentException.class)
  public void testFormatCheckSumOctalBytes_withBufferTooSmallForTrailer_ThrowsIllegalArgumentException() {
    long value = 123;
    byte[] buf = new byte[5];
    int offset = 0;
    int length = 4;
    
    TarUtils.formatCheckSumOctalBytes(value, buf, offset, length);
  }
  
  // Test case for computeCheckSum method
  @Test
  public void testComputeCheckSum_withValidBuffer_ReturnsCheckSum() {
    byte[] buffer = "123".getBytes();
    
    long result = TarUtils.computeCheckSum(buffer);
    
    assertEquals(49 + 50 + 51, result);
  }
}