import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipEncoding;
import org.apache.commons.compress.utils.ZipLong;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.*;
import java.util.zip.DataFormatException;
import java.util.zip.ZipException;

public class ZipArchiveInputStreamTest {

 private InputStream inputStream;
 private ZipArchiveInputStream zipArchiveInputStream;
 private ZipArchiveEntry zipArchiveEntry;

 @Before
 public void setUp() throws IOException {
 inputStream = new ByteArrayInputStream(
 new byte[]{0x04, 0x03, 0x4b, 0x50, // signature
 (byte) 0x00, (byte) 0x02, (byte) 0x08, (byte) 0x06, // version made by
 (byte) 0x00, (byte) 0x02, (byte) 0x00, (byte) 0x01, // version needed to extract
 (byte) 0x08, 0x07, 0x5c, 0x69, 0x62, 0x72, 0x65, 0x73, // general purpose bit flag
 (byte) 0x08, 0x00, (byte) 0x08, 0x00, // compression method
 (byte) 0x42, (byte) 0x41, 0x53, 0x53, 0x45, 0x54, // last mod file time
 (byte) 0x00, (byte) 0x00, (byte) 0x00, 0x01, // last mod file date
 (byte) 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, // crc-32
 0x00, 0x00, 0x00, 0x00, // compressed size
 0x00, 0x00, 0x00, 0x01, // uncompressed size
 (byte) 0x06, 0x00, // file name length
 (byte) 0x02, (byte) 0x03, // extra field length
 0x2e, 0x64, 0x6f, 0x63, 0x73, 0x61, 0x6d, 0x70, 0x6c, 0x65, 0x2e, // fileName
 0x00, // extra field
 0x04, 0x03, 0x4B, 0x50, 0x02, 0x01, // end of central directory signature
 0x00, 0x02 // number of this disk
 });
 zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
 }

 @Test
 public void testGetNextZipEntry() throws IOException {
 zipArchiveEntry = zipArchiveInputStream.getNextZipEntry();
 assertEquals("myfile.txt", zipArchiveEntry.getName());
 zipArchiveEntry = zipArchiveInputStream.getNextZipEntry();
 assertEquals(null, zipArchiveEntry);
 }

 @Test
 public void testGetNextEntry() throws IOException {
 zipArchiveEntry = zipArchiveInputStream.getNextEntry();
 assertEquals("myfile.txt", zipArchiveEntry.getName());
 zipArchiveEntry = zipArchiveInputStream.getNextEntry();
 assertEquals(null, zipArchiveEntry);
 }

 @Test
 public void testRead() throws IOException {
 byte[] buffer = new byte[10];
 int length = zipArchiveInputStream.read(buffer);
 assertEquals(5, length);
 assertEquals((byte) 0x2e, buffer[0]); // '.'
 assertEquals((byte) 0x64, buffer[1]); // 'd'
 assertEquals((byte) 0x6f, buffer[2]); // 'o'
 assertEquals((byte) 0x63, buffer[3]); // 'c'
 assertEquals((byte) 0x73, buffer[4]); // 's'
 }

 @Test(expected = IOException.class)
 public void testReadClosedStream() throws IOException {
 byte[] buffer = new byte[1];
 zipArchiveInputStream.close();
 zipArchiveInputStream.read(buffer);
 }

 @Test
 public void testClose() throws IOException {
 zipArchiveInputStream.close();
 zipArchiveEntry = zipArchiveInputStream.getNextEntry();
 assertEquals(null, zipArchiveEntry);
 }

 @Test
 public void testSkip() throws IOException {
 zipArchiveInputStream.skip(5);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSkipNegativeValue() throws IOException {
 zipArchiveInputStream.skip(-1);
 }

 @Test(expected = ZipException.class)
 public void testGetNextEntryWithCentralDirectory() throws IOException {
 zipArchiveInputStream.getNextEntry();
 zipArchiveInputStream.getNextEntry();
 zipArchiveInputStream.getNextEntry();
 zipArchiveInputStream.getNextEntry();
 zipArchiveInputStream.getNextEntry();
 zipArchiveInputStream.getNextEntry();
 zipArchiveInputStream.getNextEntry();
 zipArchiveInputStream.getNextEntry();
 zipArchiveInputStream.getNextEntry();
 zipArchiveInputStream.getNextEntry();
 zipArchiveInputStream.getNextEntry();
 zipArchiveInputStream.getNextEntry();
 zipArchiveInputStream.getNextEntry();
 zipArchiveInputStream.getNextEntry();
 zipArchiveInputStream.getNextEntry();
 zipArchiveInputStream.getNextEntry();
 zipArchiveInputStream.getNextEntry();
 zipArchiveInputStream.getNextEntry();
 zipArchiveInputStream.getNextEntry();
 zipArchiveInputStream.getNextEntry();
 zipArchiveInputStream.getNextEntry();
 zipArchiveInputStream.getNextEntry();
 zipArchiveInputStream.getNextEntry();
 zipArchiveInputStream.getNextEntry();
 zipArchiveInputStream.getNextEntry();
 zipArchiveInputStream.getNextEntry();
 zipArchiveInputStream.getNextEntry(); // throws ZipException
 }

 @Test
 public void testMatchesSignature() {
 byte[] signature = new byte[ZipArchiveOutputStream.LFH_SIG.length];
 assertTrue(ZipArchiveInputStream.matches(signature, signature.length));
 }
}