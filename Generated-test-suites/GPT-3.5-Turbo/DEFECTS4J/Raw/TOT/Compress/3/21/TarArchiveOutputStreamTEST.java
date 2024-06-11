import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TarArchiveOutputStreamTest {

   private TarArchiveOutputStream tarOutputStream;
   private ByteArrayOutputStream outputStream;

   @Before
   public void setUp() {
      outputStream = new ByteArrayOutputStream();
      tarOutputStream = new TarArchiveOutputStream(outputStream);
   }

   @After
   public void tearDown() throws IOException {
      tarOutputStream.close();
   }

   @Test
   public void testSetLongFileModeValidValue() {
      tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
      assertEquals(TarArchiveOutputStream.LONGFILE_GNU, tarOutputStream.getLongFileMode());
   }

   @Test
   public void testSetLongFileModeInvalidValue() {
      try {
         tarOutputStream.setLongFileMode(10);
         fail("Should throw IllegalArgumentException");
      } catch (IllegalArgumentException e) {
         assertEquals("Invalid longFileMode: 10, please choose from LONGFILE_ERROR(0), LONGFILE_TRUNCATE(1), LONGFILE_GNU(2)",
               e.getMessage());
      }
   }

   @Test
   public void testFinish() throws IOException {
      tarOutputStream.finish();
      byte[] expected = new byte[1024];
      Arrays.fill(expected, (byte) 0);
      assertArrayEquals(expected, outputStream.toByteArray());
   }

   @Test
   public void testClose() throws IOException {
      tarOutputStream.close();
      assertTrue(tarOutputStream.isClosed());
      tarOutputStream.putArchiveEntry(new TarArchiveEntry(new File("test.txt")));
      tarOutputStream.write("test".getBytes());
      assertEquals(0, tarOutputStream.getBytesWritten());
   }

   @Test
   public void testGetRecordSize() {
      assertEquals(TarBuffer.DEFAULT_RCDSIZE, tarOutputStream.getRecordSize());
   }

   @Test
   public void testPutArchiveEntryValidEntry() throws IOException {
      TarArchiveEntry entry = new TarArchiveEntry(new File("test.txt"));

      tarOutputStream.putArchiveEntry(entry);

      assertEquals(entry, tarOutputStream.getCurrentEntry());
   }

   @Test(expected = ClassCastException.class)
   public void testPutArchiveEntryInvalidEntry() throws IOException {
      ArchiveEntry entry = new ArchiveEntry() {
         // Test entry without TarArchiveEntry implementation
      };

      tarOutputStream.putArchiveEntry(entry);

      assertNull(tarOutputStream.getCurrentEntry());
   }

   @Test
   public void testCloseArchiveEntryCompleteEntry() throws IOException {
      TarArchiveEntry entry = new TarArchiveEntry(new File("test.txt"));
      entry.setSize(4);

      tarOutputStream.putArchiveEntry(entry);
      tarOutputStream.write("test".getBytes());
      tarOutputStream.closeArchiveEntry();

      assertNull(tarOutputStream.getCurrentEntry());
   }

   @Test(expected = IOException.class)
   public void testCloseArchiveEntryIncompleteEntry() throws IOException {
      TarArchiveEntry entry = new TarArchiveEntry(new File("test.txt"));
      entry.setSize(10);

      tarOutputStream.putArchiveEntry(entry);
      tarOutputStream.write("test".getBytes());
      tarOutputStream.closeArchiveEntry();
   }

   @Test
   public void testWriteCompleteBytes() throws IOException {
      tarOutputStream.putArchiveEntry(new TarArchiveEntry(new File("test.txt")));
      tarOutputStream.write("test".getBytes());
      tarOutputStream.closeArchiveEntry();

      assertArrayEquals("test".getBytes(), outputStream.toByteArray());
   }

   @Test
   public void testWriteIncompleteBytes() throws IOException {
      tarOutputStream.putArchiveEntry(new TarArchiveEntry(new File("test.txt")));
      tarOutputStream.write("tes".getBytes());
      tarOutputStream.closeArchiveEntry();

      assertArrayEquals("tes".getBytes(), outputStream.toByteArray());
   }

   @Test(expected = IOException.class)
   public void testWriteExceedingBytes() throws IOException {
      TarArchiveEntry entry = new TarArchiveEntry(new File("test.txt"));
      entry.setSize(3);

      tarOutputStream.putArchiveEntry(entry);
      tarOutputStream.write("testing".getBytes());
   }

   @Test
   public void testCreateArchiveEntry() throws IOException {
      File inputFile = new File("test.txt");
      String entryName = "test-entry";
      ArchiveEntry entry = tarOutputStream.createArchiveEntry(inputFile, entryName);

      assertNotNull(entry);
      assertEquals(inputFile, ((TarArchiveEntry) entry).getFile());
      assertEquals(entryName, ((TarArchiveEntry) entry).getName());
   }

}