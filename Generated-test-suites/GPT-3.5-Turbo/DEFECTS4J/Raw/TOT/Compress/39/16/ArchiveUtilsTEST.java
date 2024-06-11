
import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.compress.archivers.ArchiveEntry;

public class ArchiveUtilsTest {

  // test toString method
  @Test
  public void testToString() {
    ArchiveEntry entry1 = new ArchiveEntry();
    entry1.setName("main.c");
    entry1.setSize(2000);
    Assert.assertEquals("-    2000 main.c", ArchiveUtils.toString(entry1));

    ArchiveEntry entry2 = new ArchiveEntry();
    entry2.setName("testfiles");
    entry2.setSize(100);
    entry2.setIsDirectory(true);
    Assert.assertEquals("d     100 testfiles", ArchiveUtils.toString(entry2));
  }

  // test matchAsciiBuffer method
  @Test
  public void testMatchAsciiBuffer() {
    byte[] buffer = new byte[6];
    buffer[0] = 67; // C
    buffer[1] = 111; // o
    buffer[2] = 109; // m
    buffer[3] = 112; // p
    buffer[4] = 114; // r
    buffer[5] = 115; // s
    Assert.assertTrue(ArchiveUtils.matchAsciiBuffer("Comprs", buffer));

    byte[] buffer2 = new byte[5];
    buffer2[0] = 84; // T
    buffer2[1] = 101; // e
    buffer2[2] = 115; // s
    buffer2[3] = 116; // t
    buffer2[4] = 49; // 1
    Assert.assertFalse(ArchiveUtils.matchAsciiBuffer("Test", buffer2));

    byte[] buffer3 = new byte[6];
    buffer3[0] = 97; // a
    buffer3[1] = 98; // b
    buffer3[2] = 99; // c
    buffer3[3] = 0; // null
    buffer3[4] = 0; // null
    buffer3[5] = 97; // a
    Assert.assertTrue(ArchiveUtils.matchAsciiBuffer("abc", buffer3, 0, 3));
  }

  // test toAsciiBytes method
  @Test
  public void testToAsciiBytes() {
    String inputString = "Apache";
    byte[] expectedBytes = {65, 112, 97, 99, 104, 101};
    Assert.assertArrayEquals(expectedBytes, ArchiveUtils.toAsciiBytes(inputString));
  }

  // test toAsciiString method
  @Test
  public void testToAsciiString() {
    byte[] inputBytes = {65, 112, 97, 99, 104, 101};
    String expectedString = "Apache";
    Assert.assertEquals(expectedString, ArchiveUtils.toAsciiString(inputBytes));
  }

  // test isEqual method
  @Test
  public void testIsEqual() {
    byte[] buffer1 = {84, 101, 115, 116, 32, 83, 116, 114, 105, 110, 103}; // Test String
    byte[] buffer2 = {84, 101, 115, 116, 32, 83, 116, 114, 105, 110, 103}; // Test String
    Assert.assertTrue(ArchiveUtils.isEqual(buffer1, buffer2));

    byte[] buffer3 = {84, 101, 115, 116, 32, 83, 116, 114, 105, 110}; // Test Strin
    byte[] buffer4 = {84, 101, 115, 116, 32, 83, 116, 114, 105, 110, 103}; // Test String
    Assert.assertFalse(ArchiveUtils.isEqual(buffer3, buffer4));

    byte[] buffer5 = {84, 101, 115, 116, 32, 83, 116, 114, 105, 110, 103}; // Test String
    byte[] buffer6 = {84, 101, 115, 116, 32, 83, 116, 114, 105, 110, 103}; // Test String
    Assert.assertTrue(ArchiveUtils.isEqual(buffer5, 0, 10, buffer6, 0, 10));

  }

  // test isArrayZero method
  @Test
  public void testIsArrayZero() {
    byte[] a = {0, 0, 0, 0, 0, 0};
    Assert.assertTrue(ArchiveUtils.isArrayZero(a, 6));

    byte[] b = {1, 0, 0, 0, 0, 0};
    Assert.assertFalse(ArchiveUtils.isArrayZero(b, 6));
  }

  // test sanitize method
  @Test
  public void testSanitize() {
    String s = "This is a test string!";
    String expected = "This is a test string!";
    Assert.assertEquals(expected, ArchiveUtils.sanitize(s));

    String s2 = "This is a \n test \r string!";
    String expected2 = "This is a ? test ? string!";
    Assert.assertEquals(expected2, ArchiveUtils.sanitize(s2));
  }
  
}
