import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Test;

public class ArchiveUtilsTest {

  @Test
  public void testToString() {
    ArchiveEntry entry = new ArchiveEntry("testfile.txt", 100, false);
    String expectedOutput = "-     100 testfile.txt";
    String actualOutput = ArchiveUtils.toString(entry);
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testMatchAsciiBuffer() {
    String expected = "Hello World";
    byte[] buffer = expected.getBytes();
    assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer));
  }

  @Test
  public void testToAsciiBytes() {
    String inputString = "Hello World";
    byte[] expectedBytes = {72, 101, 108, 108, 111, 32, 87, 111, 114, 108, 100};
    byte[] actualBytes = ArchiveUtils.toAsciiBytes(inputString);
    assertArrayEquals(expectedBytes, actualBytes);
  }

  @Test
  public void testToAsciiString() {
    byte[] inputBytes = {72, 101, 108, 108, 111, 32, 87, 111, 114, 108, 100};
    String expectedString = "Hello World";
    String actualString = ArchiveUtils.toAsciiString(inputBytes);
    assertEquals(expectedString, actualString);
  }

  @Test
  public void testIsEqual() {
    byte[] buffer1 = "Hello".getBytes();
    byte[] buffer2 = "Hello".getBytes();
    assertTrue(ArchiveUtils.isEqual(buffer1, buffer2));
    buffer2[0] = 72;
    assertFalse(ArchiveUtils.isEqual(buffer1, buffer2));
  }

  @Test
  public void testIsEqualWithNull() {
    byte[] buffer1 = "Hello".getBytes();
    byte[] buffer2 = "Hello\0\0".getBytes();
    assertTrue(ArchiveUtils.isEqualWithNull(buffer1, 0, 5, buffer2, 0, 7));
  }

  @Test
  public void testIsArrayZero() {
    byte[] array = new byte[10];
    assertTrue(ArchiveUtils.isArrayZero(array, 10));
    array[0] = 1;
    assertFalse(ArchiveUtils.isArrayZero(array, 10));
  }

  @Test
  public void testSanitize() {
    String input = "Hello\tWorld\n";
    String expectedOutput = "Hello?World?";
    String actualOutput = ArchiveUtils.sanitize(input);
    assertEquals(expectedOutput, actualOutput);
  }

}