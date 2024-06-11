import org.apache.commons.compress.archivers.*;
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
import org.junit.Test;
import static org.junit.Assert.*;

public class ArchiveStreamFactoryTest {

 @Test
 public void testCreateArchiveInputStreamWithValidArguments() throws ArchiveException {
 ArchiveStreamFactory factory = new ArchiveStreamFactory();
 InputStream testInputStream = new ByteArrayInputStream(new byte[1024]); // valid input stream
 ArchiveInputStream output = factory.createArchiveInputStream("ZIP", testInputStream); // valid archiver name
 assertNotNull(output);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateArchiveInputStreamWithNullArchiverName() {
 ArchiveStreamFactory factory = new ArchiveStreamFactory();
 InputStream testInputStream = new ByteArrayInputStream(new byte[1024]); // valid input stream
 try {
 factory.createArchiveInputStream(null, testInputStream);
 fail("Expected an IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // pass
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateArchiveInputStreamWithNullInputStream() {
 ArchiveStreamFactory factory = new ArchiveStreamFactory();
 try {
 factory.createArchiveInputStream("ZIP", null);
 fail("Expected an IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // pass
 }
 }

 @Test(expected = ArchiveException.class)
 public void testCreateArchiveInputStreamWithInvalidArchiverName() throws ArchiveException {
 ArchiveStreamFactory factory = new ArchiveStreamFactory();
 InputStream testInputStream = new ByteArrayInputStream(new byte[1024]); // valid input stream
 try {
 factory.createArchiveInputStream("INVALID", testInputStream);
 fail("Expected an ArchiveException");
 } catch (ArchiveException e) {
 // pass
 }
 }

 @Test
 public void testCreateArchiveOutputStreamWithValidArguments() throws ArchiveException {
 ArchiveStreamFactory factory = new ArchiveStreamFactory();
 OutputStream testOutputStream = new ByteArrayOutputStream(); // valid output stream
 ArchiveOutputStream output = factory.createArchiveOutputStream("ZIP", testOutputStream); // valid archiver name
 assertNotNull(output);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateArchiveOutputStreamWithNullArchiverName() {
 ArchiveStreamFactory factory = new ArchiveStreamFactory();
 OutputStream testOutputStream = new ByteArrayOutputStream(); // valid output stream
 try {
 factory.createArchiveOutputStream(null, testOutputStream);
 fail("Expected an IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // pass
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateArchiveOutputStreamWithNullOutputStream() {
 ArchiveStreamFactory factory = new ArchiveStreamFactory();
 try {
 factory.createArchiveOutputStream("ZIP", null);
 fail("Expected an IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // pass
 }
 }

 @Test(expected = ArchiveException.class)
 public void testCreateArchiveOutputStreamWithInvalidArchiverName() throws ArchiveException {
 ArchiveStreamFactory factory = new ArchiveStreamFactory();
 OutputStream testOutputStream = new ByteArrayOutputStream(); // valid output stream
 try {
 factory.createArchiveOutputStream("INVALID", testOutputStream);
 fail("Expected an ArchiveException");
 } catch (ArchiveException e) {
 // pass
 }
 }

 @Test
 public void testCreateArchiveOutputStreamFromInputStreamWithValidArguments() throws ArchiveException {
 ArchiveStreamFactory factory = new ArchiveStreamFactory();
 InputStream testInputStream = new ByteArrayInputStream(new byte[1024]); // valid input stream
 OutputStream testOutputStream = new ByteArrayOutputStream(); // valid output stream
 ArchiveOutputStream output = factory.createArchiveOutputStream("ZIP", testOutputStream); // valid archiver name
 assertNotNull(output);
 output.putNextEntry(new JarArchiveEntry("test.txt"));
 output.write("Hello World!".getBytes());
 output.closeArchiveEntry();
 output.finallyClose();
 assertTrue(testOutputStream.toString().contains("Hello World!"));
 }

 @Test(expected = ArchiveException.class)
 public void testCreateArchiveInputStreamWithInvalidSignature() throws ArchiveException {
 ArchiveStreamFactory factory = new ArchiveStreamFactory();
 InputStream testInputStream = new ByteArrayInputStream(new byte[10]); // invalid signature length
 try {
 factory.createArchiveInputStream("ZIP", testInputStream);
 fail("Expected an ArchiveException");
 } catch (ArchiveException e) {
 // pass
 }
 }

 @Test
 public void testCreateArchiveInputStreamWithValidSignature() throws ArchiveException {
 ArchiveStreamFactory factory = new ArchiveStreamFactory();
 InputStream testInputStream = new ByteArrayInputStream(new byte[1024]); // valid input stream with valid signature
 ArchiveInputStream output = factory.createArchiveInputStream("ZIP", testInputStream); // valid archiver name
 assertNotNull(output);
 output.close();
 }

 @Test(expected = ArchiveException.class)
 public void testCreateArchiveInputStreamWithInvalidSignatureAndInvalidArchiverName() throws ArchiveException {
 ArchiveStreamFactory factory = new ArchiveStreamFactory();
 InputStream testInputStream = new ByteArrayInputStream(new byte[10]); // invalid signature length
 try {
 factory.createArchiveInputStream("INVALID", testInputStream);
 fail("Expected an ArchiveException");
 } catch (ArchiveException e) {
 // pass
 }
 }

}