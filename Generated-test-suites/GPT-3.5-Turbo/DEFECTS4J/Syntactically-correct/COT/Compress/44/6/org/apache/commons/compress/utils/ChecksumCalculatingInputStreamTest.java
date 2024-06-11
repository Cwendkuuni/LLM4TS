import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.Checksum;

public class ChecksumCalculatingInputStreamTest {
   private ChecksumCalculatingInputStream inputstream;

   @Before
   public void setUp() {
      Checksum checksum = new CRC32();
      InputStream in = new FileInputStream("testfile.txt");
      inputstream = new ChecksumCalculatingInputStream(checksum, in);
   }

   @Test
   public void testRead() throws IOException {
      int expected = 65; // ASCII value of 'A'
      int actual = inputstream.read();
      assertEquals(expected, actual);
   }

   @Test
   public void testReadArray() throws IOException {
      byte[] expected = {65, 66, 67}; // ASCII values of 'A', 'B', 'C'
      byte[] actual = new byte[3];
      int bytesRead = inputstream.read(actual);
      assertArrayEquals(expected, actual);
      assertEquals(3, bytesRead);
   }

   @Test
   public void testReadOffsetLength() throws IOException {
      byte[] expected = {65, 66, 67}; // ASCII values of 'A', 'B', 'C'
      byte[] actual = new byte[3];
      int bytesRead = inputstream.read(actual, 0, 3);
      assertArrayEquals(expected, actual);
      assertEquals(3, bytesRead);
   }

   @Test
   public void testSkip() throws IOException {
      long expected = 1; // Skipped 1 byte
      long actual = inputstream.skip(1);
      assertEquals(expected, actual);
   }

   @Test
   public void testGetValue() {
      long expected = 1234; // Example value
      long actual = inputstream.getValue();
      assertEquals(expected, actual);
   }
}