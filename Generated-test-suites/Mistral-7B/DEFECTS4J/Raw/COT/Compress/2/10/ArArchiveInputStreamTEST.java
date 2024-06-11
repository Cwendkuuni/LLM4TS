import org.apache.commons.compress.archivers.ar.ArArchiveEntry;
import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ArArchiveInputStreamTest {

 private InputStream inputStream;
 private ArArchiveInputStream archiveInputStream;

 @Before
 public void setup() {
 inputStream = new ByteArrayInputStream(new byte[]{(byte) 0x21, (byte) 0x3c, (byte) 0x61, (byte) 0x72, (byte) 0x63, (byte) 0x68, (byte) 0x3e, (byte) 0x0a, (byte) 0x21, (byte) 0x3c, (byte) 0x72, (byte) 0x61, (byte) 0x68, (byte) 0x73, (byte) 0x63, (byte) 0x72, (byte) 0x65, (byte) 0x64, (byte) 0x3e, (byte) 0x0a, (byte) 0x61, (byte) 0x72, (byte) 0x63, (byte) 0x68, (byte) 0x65, (byte) 0x6d, (byte) 0x65, (byte) 0x64, (byte) 0x69, (byte) 0x6, (byte) 0x0, (byte) 0x3e, (byte) 0x0a, (byte) 0x61, (byte) 0x72, (byte) 0x63, (byte) 0x68, (byte) 0x65, (byte) 0x6d, (byte) 0x65, (byte) 0x64, (byte) 0x69, (byte) 0x10, (byte) 0x0, (byte) 0x3e, (byte) 0x0a, (byte) 0x61, (byte) 0x72, (byte) 0x63, (byte) 0x68, (byte) 0x65, (byte) 0x6d, (byte) 0x65, (byte) 0x64, (byte) 0x69, (byte) 0x16, (byte) 0x0, (byte) 0x3e});
 archiveInputStream = new ArArchiveInputStream(inputStream);
 }

 @Test
 public void testGetNextArEntry() throws IOException {
 ArchiveEntry entry = archiveInputStream.getNextArEntry();
 Assert.assertNotNull(entry);
 Assert.assertTrue(entry instanceof ArArchiveEntry);
 }

 @Test(expected = IOException.class)
 public void testGetNextArEntryWhenNoMoreEntries() throws IOException {
 ArchiveEntry entry = archiveInputStream.getNextArEntry();
 Assert.assertNotNull(entry);
 archiveInputStream.getNextArEntry(); // consume next entry
 Assert.assertNull(archiveInputStream.getNextArEntry());
 }

 @Test(expected = IOException.class)
 public void testGetNextArEntryWithInvalidHeader() throws IOException {
 ByteArrayOutputStream output = new ByteArrayOutputStream();
 output.write(new byte[]{0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, (byte) 0x0a, 0x21, 0x3c, 0x72, 0x61, 0x68, 0x73, 0x63, 0x72, 0x65, (byte) 0x64, 0x3e, 0x0a, 0x61, 0x72, 0x63, 0x68, 0x65, 0x6d, (byte) 0x65, 0x64, 0x69, 0x6, 0x0, 0x3e}); // missing last byte
 InputStream input = new ByteArrayInputStream(output.toByteArray());
 ArArchiveInputStream archiveInputStream = new ArArchiveInputStream(input);
 Assert.assertNull(archiveInputStream.getNextArEntry());
 }

 @Test(expected = IOException.class)
 public void testGetNextArEntryWithInvalidLength() throws IOException {
 ByteArrayOutputStream output = new ByteArrayOutputStream();
 output.write(new byte[]{0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, (byte) 0x0a, 0x21, 0x3c, 0x72, 0x61, 0x68, 0x73, 0x63, 0x72, 0x65, 0x64, 0x3e, 0x0a, 0x61, 0x72, 0x63, 0x68, 0x65, 0x6d, 0x65, 0x64, 0x69, 0x10, 0x0, 0x3e, 0x0a, 0x61, 0x72, 0x63, 0x68, 0x65, 0x6d, 0x65, 0x64, 0x69, 0x16, 0x1, (byte) 0x0}); // invalid length
 InputStream input = new ByteArrayInputStream(output.toByteArray());
 ArArchiveInputStream archiveInputStream = new ArArchiveInputStream(input);
 Assert.assertNull(archiveInputStream.getNextArEntry());
 }

 @Test
 public void testGetNextEntry() throws IOException {
 ArchiveEntry entry = archiveInputStream.getNextEntry();
 Assert.assertTrue(entry instanceof ArArchiveEntry);
 }

 @Test
 public void testClose() throws IOException {
 archiveInputStream.close();
 }

 @Test(expected = IOException.class)
 public void testGetNextEntryAfterClose() throws IOException {
 archiveInputStream.close();
 Assert.assertNull(archiveInputStream.getNextArEntry());
 }

 @Test
 public void testRead() throws IOException {
 byte[] buffer = new byte[10];
 int read = archiveInputStream.read(buffer, 0, buffer.length);
 Assert.assertEquals(5, read);
 }

 @Test
 public void testReadWithOffset() throws IOException {
 byte[] buffer = new byte[10];
 int read = archiveInputStream.read(buffer, 5, buffer.length - 5);
 Assert.assertEquals(5, read);
 }

 @Test
 public void testReadWithLength() throws IOException {
 byte[] buffer = new byte[10];
 int read = archiveInputStream.read(buffer, 0, 5);
 Assert.assertEquals(5, read);
 }

 @Test
 public void testReadWithOffsetAndLength() throws IOException {
 byte[] buffer = new byte[10];
 int read = archiveInputStream.read(buffer, 5, 5);
 Assert.assertEquals(0, read);
 }

 @Test(expected = IOException.class)
 public void testReadWhenStreamIsClosed() throws IOException {
 archiveInputStream.close();
 int read = archiveInputStream.read();
 }
}