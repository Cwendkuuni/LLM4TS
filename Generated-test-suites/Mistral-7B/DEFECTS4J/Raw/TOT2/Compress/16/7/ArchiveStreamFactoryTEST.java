import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.JarArchiveInputStream;
import org.apache.commons.compress.archivers.JarArchiveOutputStream;
import org.apache.commons.compress.archivers.TarArchiveInputStream;
import org.apache.commons.compress.archivers.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArchiveStreamFactoryTest {

 // Test createArchiveInputStream() with each archiver
 @Test
 public void testCreateArchiveInputStreamAR() throws Exception {
 InputStream input = new ByteArrayInputStream(new byte[1024]);
 ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance()
 .createArchiveInputStream(ArchiveStreamFactory.AR, input);
 assertTrue(archiveInputStream instanceof ArArchiveInputStream);
 }

 @Test
 public void testCreateArchiveInputStreamCpio() throws Exception {
 InputStream input = new ByteArrayInputStream(new byte[1024]);
 ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance()
 .createArchiveInputStream(ArchiveStreamFactory.CPIO, input);
 assertTrue(archiveInputStream instanceof CpioArchiveInputStream);
 }

 @Test
 public void testCreateArchiveInputStreamDump() throws Exception {
 InputStream input = new ByteArrayInputStream(new byte[1024]);
 ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance()
 .createArchiveInputStream(ArchiveStreamFactory.DUMP, input);
 assertTrue(archiveInputStream instanceof DumpArchiveInputStream);
 }

 @Test
 public void testCreateArchiveInputStreamJar() throws Exception {
 InputStream input = new ByteArrayInputStream(new byte[1024]);
 ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance()
 .createArchiveInputStream(ArchiveStreamFactory.JAR, input);
 assertTrue(archiveInputStream instanceof JarArchiveInputStream);
 }

 @Test
 public void testCreateArchiveInputStreamTar() throws Exception {
 InputStream input = new ByteArrayInputStream(new byte[1024]);
 ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance()
 .createArchiveInputStream(ArchiveStreamFactory.TAR, input);
 assertTrue(archiveInputStream instanceof TarArchiveInputStream);
 }

 @Test
 public void testCreateArchiveInputStreamZip() throws Exception {
 InputStream input = new ByteArrayInputStream(new byte[1024]);
 ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance()
 .createArchiveInputStream(ArchiveStreamFactory.ZIP, input);
 assertTrue(archiveInputStream instanceof ZipArchiveInputStream);
 }

 // Test createArchiveOutputStream() with each archiver
 @Test
 public void testCreateArchiveOutputStreamAR() throws Exception {
 OutputStream output = new ByteArrayOutputStream();
 ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.getInstance()
 .createArchiveOutputStream(ArchiveStreamFactory.AR, output);
 assertTrue(archiveOutputStream instanceof ArArchiveOutputStream);
 }

 @Test
 public void testCreateArchiveOutputStreamCpio() throws Exception {
 OutputStream output = new ByteArrayOutputStream();
 ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.getInstance()
 .createArchiveOutputStream(ArchiveStreamFactory.CPIO, output);
 assertTrue(archiveOutputStream instanceof CpioArchiveOutputStream);
 }

 @Test
 public void testCreateArchiveOutputStreamDump() throws Exception {
 OutputStream output = new ByteArrayOutputStream();
 ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.getInstance()
 .createArchiveOutputStream(ArchiveStreamFactory.DUMP, output);
 assertTrue(archiveOutputStream instanceof DumpArchiveOutputStream);
 }

 @Test
 public void testCreateArchiveOutputStreamJar() throws Exception {
 OutputStream output = new ByteArrayOutputStream();
 ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.getInstance()
 .createArchiveOutputStream(ArchiveStreamFactory.JAR, output);
 assertTrue(archiveOutputStream instanceof JarArchiveOutputStream);
 }

 @Test
 public void testCreateArchiveOutputStreamTar() throws Exception {
 OutputStream output = new ByteArrayOutputStream();
 ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.getInstance()
 .createArchiveOutputStream(ArchiveStreamFactory.TAR, output);
 assertTrue(archiveOutputStream instanceof TarArchiveOutputStream);
 }

 @Test
 public void testCreateArchiveOutputStreamZip() throws Exception {
 OutputStream output = new ByteArrayOutputStream();
 ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.getInstance()
 .createArchiveOutputStream(ArchiveStreamFactory.ZIP, output);
 assertTrue(archiveOutputStream instanceof ZipArchiveOutputStream);
 }

 // Test createArchiveInputStream() with invalid archivers
 @Test(expected = ArchiveException.class)
 public void testCreateArchiveInputStreamInvalidArchiver() throws Exception {
 InputStream input = new ByteArrayInputStream(new byte[1024]);
 ArchiveStreamFactory.getInstance()
 .createArchiveInputStream("invalid", input);
 fail("Expected ArchiveException");
 }

 // Test createArchiveOutputStream() with null InputStream
 @Test(expected = IllegalArgumentException.class)
 public void testCreateArchiveOutputStreamNullInputStream() throws Exception {
 ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance()
 .createArchiveInputStream(ArchiveStreamFactory.ZIP, null);
 fail("Expected IllegalArgumentException");
 }

 // Test createArchiveOutputStream() with null OutputStream
 @Test(expected = IllegalArgumentException.class)
 public void testCreateArchiveOutputStreamNullOutputStream() throws Exception {
 ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.getInstance()
 .createArchiveOutputStream(ArchiveStreamFactory.ZIP, null);
 fail("Expected IllegalArgumentException");
 }

 // Test createArchiveInputStream() with invalid signature
 @Test
 public void testCreateArchiveInputStreamInvalidSignature() throws Exception {
 InputStream input = new ByteArrayInputStream(new byte[1024]);
 try {
 ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance()
 .createArchiveInputStream("AR", input);
 fail("Expected ArchiveException");
 } catch (ArchiveException e) {
 // expected
 }
 }

 // Test createArchiveOutputStream() with empty InputStream
 @Test
 public void testCreateArchiveOutputStreamEmptyInputStream() throws Exception {
 OutputStream output = new ByteArrayOutputStream();
 try {
 ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.getInstance()
 .createArchiveOutputStream(ArchiveStreamFactory.ZIP, output);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 // Test createArchiveOutputStream() with empty OutputStream
 @Test
 public void testCreateArchiveOutputStreamEmptyOutputStream() throws Exception {
 OutputStream output = new ByteArrayOutputStream();
 try {
 ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.getInstance()
 .createArchiveOutputStream(ArchiveStreamFactory.ZIP, output);
 archiveOutputStream.close();
 } catch (IllegalArgumentException e) {
 fail("Expected ArchiveOutputStream");
 } finally {
 assertEquals(0, output.size());
 }
 }
}