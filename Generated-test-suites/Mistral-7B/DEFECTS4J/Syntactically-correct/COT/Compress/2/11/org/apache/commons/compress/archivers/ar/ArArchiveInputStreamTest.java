import org.apache.commons.compress.archivers.ArArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ArArchiveInputStreamTest {

 private ArArchiveInputStream inputStream;
 private InputStream input;
 private ByteArrayOutputStream outputStream;

 @Before
 public void setUp() throws IOException {
 input = new ByteArrayInputStream(new byte[]{"\u001fAR\u0002\u0004\u0010test.txt\u0000\u0000\u0000\u0000\u0000\u0000" +
 "\u0000\u0000\u0000\u0000\u0001\u0012\u0004test2.txt\u0000\u0000\u0000\u0000\u0000\u0000" +
 "\u0000\u0000\u0000\u0000\u0002\u0012\u0004test3.txt\u0000\u0000\u0000\u0000\u0000\u0000" +
 "\u0000\u0000\u0000\u0000\u0003\u0012\u0004test4.txt\u0000\u0000\u0000\u0000\u0000\u0000" +
 "\u0000\u0000\u0000\u0000\u0005\u0012\u0004test5.txt\u0000\u0000\u0000\u0000\u0000\u0000" +
 "\u0000\u0000\u0000\u0000\u0006\u0012\u0004test6.txt\u0000\u0000\u0000\u0000\u0000\u0000" +
 "\u0000\u0000\u0000\u0000\u0007\u0012\u0004test7.txt\u0000\u0000\u0000\u0000\u0000\u0000" +
 "\u0000\u0000\u0000\u0000\b\u0012\u0004test8.txt\u0000\u0000\u0000\u0000\u0000\u0000" +
 "\u0000\u0000\u0000\u0000\t\u0012\u0004test9.txt\u0000\u0000\u0000\u0000\u0000\u0000" +
 "\u0000\u0000\u0000\u0000\n\u0012\u0004test10.txt\u0000\u0000\u0000\u0000\u0000\u0000" +
 "\u0000\u0000\u0000\u0000\f\u0012\u0004test11.txt\u0000\u0000\u0000\u0000\u0000\u0000" +
 "\u0000\u0000\u0000\u0000\r\u0012\u0004test12.txt\u0000\u0000\u0000\u0000\u0000\u0000" +
 "\u0000\u0000\u0000\u0000\u0016"}.trim().getBytes());
 outputStream = new ByteArrayOutputStream();
 inputStream = new ArArchiveInputStream(input);
 }

 @Test
 public void testGetNextArEntry() throws IOException {
 ArArchiveEntry entry;
 for (int i = 0; i < 12; i++) {
 entry = inputStream.getNextArEntry();
 Assert.assertNotNull(entry);
 }
 // check for eof
 entry = inputStream.getNextArEntry();
 Assert.assertNull(entry);
 }

 @Test
 public void testGetNextEntry() throws IOException {
 ArchiveEntry entry;
 for (int i = 0; i < 12; i++) {
 entry = inputStream.getNextEntry();
 Assert.assertInstanceOf(ArArchiveEntry.class, entry);
 }
 // check for eof
 entry = inputStream.getNextEntry();
 Assert.assertNull(entry);
 }

 @Test
 public void testClose() throws IOException {
 inputStream.close();
 Assert.assertTrue(inputStream.isClosed());
 }

 @Test(expected = IOException.class)
 public void testGetNextEntryAfterClose() throws IOException {
 inputStream.close();
 inputStream.getNextEntry();
 }

 @Test
 public void testMatches() throws IOException {
 byte[] signature = new byte[16];
 ArArchiveInputStream.matches(signature, 0);
 Assert.assertFalse(ArArchiveInputStream.matches(signature, 15));
 ArArchiveInputStream.matches(signature, 16);
 Assert.assertTrue(ArArchiveInputStream.matches(signature, 17));
 }

 @Test
 public void testRead() throws IOException {
 byte b;
 for (int i = 0; i < 12; i++) {
 b = (byte) inputStream.read();
 Assert.assertNotEquals(-1, b);
 }
 // check for eof
 b = (byte) inputStream.read();
 Assert.assertEquals(-1, b);
 }

 @Test
 public void testReadByteArray() throws IOException {
 byte[] buffer = new byte[10];
 int read = inputStream.read(buffer);
 Assert.assertEquals(10, read);
 }

 @Test(expected = IOException.class)
 public void testReadByteArrayWithNegativeLength() throws IOException {
 byte[] buffer = new byte[10];
 inputStream.read(buffer, -10);
 }

 @Test
 public void testReadByteArrayWithZeroLength() throws IOException {
 byte[] buffer = new byte[0];
 int read = inputStream.read(buffer);
 Assert.assertEquals(0, read);
 }
}