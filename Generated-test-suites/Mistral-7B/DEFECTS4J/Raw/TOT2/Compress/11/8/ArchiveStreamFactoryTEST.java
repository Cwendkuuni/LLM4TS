import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.JarArchiveInputStream;
import org.apache.commons.compress.archivers.JarArchiveOutputStream;
import org.apache.commons.compress.archivers.TarArchiveInputStream;
import org.apache.commons.compress.archivers.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.ZipArchiveOutputStream;
import org.junit.Test;
import org.junit.Assert.*;

public class ArchiveStreamFactoryTest {

 // Test method for createArchiveInputStream(String, InputStream)
 @Test
 public void testCreateArchiveInputStream() throws Exception {
 String archiverName = "zip";
 InputStream inputStream = new ByteArrayInputStream(new byte[1024]);
 ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(archiverName, inputStream);
 assertTrue(archiveInputStream instanceof ZipArchiveInputStream);
 archiveInputStream.close();
 }

 // Test method for createArchiveInputStream(InputStream)
 @Test
 public void testCreateArchiveInputStreamWithAutoDetection() throws Exception {
 InputStream inputStream = new ByteArrayInputStream(new byte[1024]);
 ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(inputStream);
 assertTrue(archiveInputStream instanceof ZipArchiveInputStream);
 archiveInputStream.close();
 }

 // Test method for createArchiveOutputStream(String, OutputStream)
 @Test
 public void testCreateArchiveOutputStream() throws Exception {
 String archiverName = "tar";
 OutputStream outputStream = new ByteArrayOutputStream();
 ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(archiverName, outputStream);
 assertTrue(archiveOutputStream instanceof TarArchiveOutputStream);
 archiveOutputStream.close();
 }

 // Test method for createArchiveOutputStream(String, byte[])
 @Test
 public void testCreateArchiveOutputStreamWithSignature() throws Exception {
 byte[] signature = new byte[12];
 OutputStream outputStream = new ByteArrayOutputStream();
 ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream("zip", outputStream, signature);
 assertTrue(archiveOutputStream instanceof ZipArchiveOutputStream);
 archiveOutputStream.close();
 }

 // Test edge case: createArchiveInputStream(null)
 @Test(expected = IllegalArgumentException.class)
 public void testCreateArchiveInputStreamWithNullInputStream() {
 String archiverName = "tar";
 InputStream inputStream = null;
 new ArchiveStreamFactory().createArchiveInputStream(archiverName, inputStream);
 }

 // Test edge case: createArchiveOutputStream(null)
 @Test(expected = IllegalArgumentException.class)
 public void testCreateArchiveOutputStreamWithNullArchiverName() {
 OutputStream outputStream = new ByteArrayOutputStream();
 new ArchiveStreamFactory().createArchiveOutputStream(null, outputStream);
 }

 // Test exception handling: createArchiveInputStream(String, InputStream) with unknown archiver
 @Test(expected = ArchiveException.class)
 public void testCreateArchiveInputStreamWithUnknownArchiver() throws Exception {
 String archiverName = "foo";
 InputStream inputStream = new ByteArrayInputStream(new byte[1024]);
 new ArchiveStreamFactory().createArchiveInputStream(archiverName, inputStream);
 }

 // Test exception handling: createArchiveOutputStream(String, OutputStream) with unknown archiver
 @Test(expected = ArchiveException.class)
 public void testCreateArchiveOutputStreamWithUnknownArchiver() throws Exception {
 String archiverName = "foo";
 OutputStream outputStream = new ByteArrayOutputStream();
 new ArchiveStreamFactory().createArchiveOutputStream(archiverName, outputStream);
 }

 // Test exception handling: createArchiveInputStream(InputStream) with null input stream
 @Test(expected = IllegalArgumentException.class)
 public void testCreateArchiveInputStreamWithNullInputStreamSignal() throws Exception {
 ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(null);
 }

 // Test exception handling: createArchiveOutputStream(InputStream) with unsupported input stream
 @Test(expected = ArchiveException.class)
 public void testCreateArchiveOutputStreamWithUnsupportedInputStream() throws Exception {
 InputStream inputStream = new ByteArrayInputStream(new byte[1024]);
 inputStream.mark(1);
 inputStream.close(); // close the stream
 ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream("tar", inputStream);
 }

 // Test exception handling: createArchiveOutputStream(String, OutputStream) with null output stream
 @Test(expected = IllegalArgumentException.class)
 public void testCreateArchiveOutputStreamWithNullOutputStreamSignal() throws Exception {
 ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream("zip", null);
 }

 // Test edge case: createArchiveOutputStream(String, OutputStream) with empty output stream
 @Test
 public void testCreateArchiveOutputStreamWithEmptyOutputStream() throws Exception {
 String archiverName = "zip";
 OutputStream outputStream = new ByteArrayOutputStream();
 ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(archiverName, outputStream);
 archiveOutputStream.close();
 }
}