import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ar.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArchiveStreamFactoryTest {

 // Test createArchiveInputStream(String, InputStream)
 @Test
 public void testCreateArchiveInputStreamStringInputStream() throws ArchiveException {
 InputStream input = new ByteArrayInputStream("ZipSignature".getBytes());
 ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, input);
 assertTrue(archiveInputStream instanceof ZipArchiveInputStream);
 archiveInputStream.close();
 }

 @Test(expected = ArchiveException.class)
 public void testCreateArchiveInputStreamUnknownFormat() throws ArchiveException {
 InputStream input = new ByteArrayInputStream("UnknownFormatSignature".getBytes());
 new ArchiveStreamFactory().createArchiveInputStream("unknown", input);
 }

 // Test createArchiveInputStream(InputStream)
 @Test
 public void testCreateArchiveInputStream() throws ArchiveException, IOException {
 InputStream input = new FileInputStream("test.zip");
 ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(input);
 assertTrue(archiveInputStream instanceof ZipArchiveInputStream);
 archiveInputStream.close();
 IOUtils.closeQuietly(input);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateArchiveInputStreamNullInputStream() {
 InputStream input = null;
 try {
 new ArchiveStreamFactory().createArchiveInputStream("zip", input);
 } catch (IllegalArgumentException e) {
 assertEquals("InputStream must not be null.", e.getMessage());
 }
 }

 // Test createArchiveOutputStream(String, OutputStream)
 @Test
 public void testCreateArchiveOutputStreamStringOutputStream() throws ArchiveException {
 OutputStream output = new ByteArrayOutputStream();
 ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.AR, output);
 assertTrue(archiveOutputStream instanceof ArArchiveOutputStream);
 archiveOutputStream.close();
 }

 @Test(expected = ArchiveException.class)
 public void testCreateArchiveOutputStreamUnknownFormat() throws ArchiveException {
 OutputStream output = new ByteArrayOutputStream();
 new ArchiveStreamFactory().createArchiveOutputStream("unknown", output);
 }

 // Test createArchiveOutputStream(OutputStream)
 @Test
 public void testCreateArchiveOutputStream() throws ArchiveException, IOException {
 OutputStream output = new FileOutputStream("test.tar");
 ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.TAR, output);
 assertTrue(archiveOutputStream instanceof TarArchiveOutputStream);
 archiveOutputStream.close();
 IOUtils.closeQuietly(output);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateArchiveOutputStreamNullOutputStream() {
 OutputStream output = null;
 try {
 new ArchiveStreamFactory().createArchiveOutputStream("tar", output);
 } catch (IllegalArgumentException e) {
 assertEquals("OutputStream must not be null.", e.getMessage());
 }
 }

 // Test createArchiveInputStream(InputStream) with invalid mark support
 @Test(expected = IllegalArgumentException.class)
 public void testCreateArchiveInputStreamNoMarkSupport() throws ArchiveException, IOException {
 ByteArrayInputStream input = new ByteArrayInputStream("ZipSignature".getBytes());
 assertFalse(input.markSupported());
 try {
 new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, input);
 } catch (IllegalArgumentException e) {
 assertEquals("Stream must support mark and reset.", e.getMessage());
 }
 input.close();
 }

 // Test createArchiveInputStream(InputStream) with empty signature
 @Test(expected = ArchiveException.class)
 public void testCreateArchiveInputStreamEmptySignature() throws ArchiveException, IOException {
 InputStream input = new ByteArrayInputStream(new byte[0]);
 try {
 new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.JAR, input);
 } catch (ArchiveException e) {
 assertEquals("No Archiver found for the stream signature", e.getMessage());
 }
 input.close();
 }

 // Test createArchiveInputStream(InputStream) with wrong signature
 @Test(expected = ArchiveException.class)
 public void testCreateArchiveInputStreamWrongSignature() throws ArchiveException, IOException {
 InputStream input = new ByteArrayInputStream("InvalidSignature".getBytes());
 try {
 new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.TAR, input);
 } catch (ArchiveException e) {
 assertEquals("No Archiver found for the stream signature", e.getMessage());
 }
 input.close();
 }

 // Test createArchiveInputStream(InputStream) with large signature
 @Test
 public void testCreateArchiveInputStreamLargeSignature() throws ArchiveException, IOException {
 InputStream input = new FileInputStream("test.zip");
 ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(archiveInputStream);
 assertTrue(archiveInputStream instanceof ZipArchiveInputStream);
 archiveInputStream.close();
 IOUtils.closeQuietly(input);
 }

 // Test createArchiveInputStream(InputStream) with incomplete signature
 @Test(expected = ArchiveException.class)
 public void testCreateArchiveInputStreamIncompleteSignature() throws ArchiveException, IOException {
 InputStream input = new ByteArrayInputStream("ZipSignature".getBytes(), 0, 5);
 try {
 new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, input);
 } catch (ArchiveException e) {
 assertEquals("No Archiver found for the stream signature", e.getMessage());
 }
 input.close();
 }
}