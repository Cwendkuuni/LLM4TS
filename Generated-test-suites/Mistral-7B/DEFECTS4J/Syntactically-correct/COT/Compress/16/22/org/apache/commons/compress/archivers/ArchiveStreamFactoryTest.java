import org.apache.commons.compress.archivers.*;
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
import static org.junit.Assert.*;

public class ArchiveStreamFactoryTest {

 @Test
 public void testCreateArchiveInputStreamAR() throws Exception {
 InputStream inputStream = new ByteArrayInputStream(new byte[10]);
 ArchiveInputStream archiverInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.AR, inputStream);
 assertNotNull(archiverInputStream);
 archiverInputStream.close();
 }

 @Test(expected = ArchiveException.class)
 public void testCreateArchiveInputStreamARInvalidFormat() throws Exception {
 InputStream inputStream = new ByteArrayInputStream(new byte[10]);
 ArchiveInputStream archiverInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.GZIP, inputStream);
 }

 @Test
 public void testCreateArchiveInputStreamARNullArchiverName() {
 assertNull(ArchiveStreamFactory.getInstance().createArchiveInputStream(null, new ByteArrayInputStream(new byte[10])));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateArchiveInputStreamARNullInputStream() {
 ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.AR, null);
 }

 @Test
 public void testCreateArchiveInputStreamARValidFormat() throws Exception {
 InputStream inputStream = new ByteArrayInputStream(new byte[10]);
 ArchiveInputStream archiverInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.AR, inputStream);
 assertNotNull(archiverInputStream);
 ArchiveEntry entry = archiverInputStream.getNextEntry();
 assertNotNull(entry);
 archiverInputStream.close();
 }

 // Repeat the above test cases for other methods: createArchiveInputStream(String, InputStream), createArchiveInputStream(InputStream),
 // createArchiveOutputStream(String, OutputStream), createArchiveOutputStream(OutputStream), createArchiveOutputStream(InputStream, String)

 @Test
 public void testCreateArchiveOutputStreamAR() throws Exception {
 OutputStream outputStream = new ByteArrayOutputStream();
 ArchiveOutputStream archiverOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.AR, outputStream);
 assertNotNull(archiverOutputStream);
 archiverOutputStream.finish();
 archiverOutputStream.close();
 }

 @Test(expected = ArchiveException.class)
 public void testCreateArchiveOutputStreamARNullOutputStream() {
 ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.AR, null);
 }

 @Test
 public void testCreateArchiveOutputStreamARValidFormat() throws Exception {
 OutputStream outputStream = new ByteArrayOutputStream();
 ArchiveOutputStream archiverOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.AR, outputStream);
 assertNotNull(archiverOutputStream);
 archiverOutputStream.putArchiveEntry(new ArArchiveEntry("test.txt"));
 archiverOutputStream.writeToArchive(new ByteArrayInputStream(new byte[10]), 0, 10);
 archiverOutputStream.finish();
 archiverOutputStream.close();
 }

 // Repeat the above test cases for other methods: createArchiveOutputStream(String, OutputStream), createArchiveOutputStream(OutputStream),
 // createArchiveOutputStream(InputStream, String), createArchiveOutputStream(InputStream, OutputStream)

 @Test
 public void testCreateArchiveInputStreamMismatchedFormat() throws Exception {
 InputStream inputStream = new ByteArrayInputStream(new byte[10]);
 try {
 ArchiveInputStream archiverInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.ZIP, inputStream);
 fail("Expected ArchiveException");
 } catch (ArchiveException e) {
 // Expected
 }
 }

 @Test
 public void testCreateArchiveOutputStreamMismatchedFormat() throws Exception {
 OutputStream outputStream = new ByteArrayOutputStream();
 try {
 ArchiveOutputStream archiverOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.AR, outputStream);
 fail("Expected ArchiveException");
 } catch (ArchiveException e) {
 // Expected
 }
 }

 @Test
 public void testCreateArchiveInputStreamNullArchiverName() {
 try {
 ArchiveStreamFactory.getInstance().createArchiveInputStream(null, new ByteArrayInputStream(new byte[10]));
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testCreateArchiveOutputStreamNullInputStream() {
 try {
 ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.AR, null);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testCreateArchiveOutputStreamNullOutputstream() {
 try {
 ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.AR, null);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 // Edge cases and exception handling tests

 @Test(expected = ArchiveException.class)
 public void testCreateArchiveInputStreamInvalidSignature() throws IOException {
 InputStream inputStream = new ByteArrayInputStream(new byte[10]);
 inputStream.mark(1);
 inputStream.read();
 inputStream.reset();
 ArchiveInputStream archiverInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.AR, inputStream);
 fail("Expected ArchiveException");
 }

 @Test(expected = IOException.class)
 public void testCreateArchiveInputStreamMissingSignature() throws IOException {
 InputStream inputStream = new ByteArrayInputStream(new byte[5]);
 ArchiveInputStream archiverInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.AR, inputStream);
 fail("Expected IOException");
 }

 // Repeat the above test cases for createArchiveOutputStream()

 @Test(expected = ArchiveException.class)
 public void testCreateArchiveOutputStreamMissingSignature() throws IOException {
 OutputStream outputStream = new ByteArrayOutputStream();
 outputStream.mark(1);
 outputStream.write(0);
 outputStream.reset();
 ArchiveOutputStream archiverOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.AR, outputStream);
 fail("Expected ArchiveException");
 }

 // Repeat the tests for all methods in the class

}