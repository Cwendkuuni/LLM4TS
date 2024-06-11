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
import org.junit.Test;
import static org.junit.Assert.*;

public class ArchiveStreamFactoryTest {

 // Test createArchiveInputStream method with various archive types
 @Test
 public void testCreateArchiveInputStream() throws ArchiveException, IOException {
 InputStream testStream = new ByteArrayInputStream(new byte[1024]);
 String[] archivers = {ArchiveStreamFactory.AR, ArchiveStreamFactory.CPIO, ArchiveStreamFactory.DUMP, ArchiveStreamFactory.JAR, ArchiveStreamFactory.TAR, ArchiveStreamFactory.ZIP};
 for (String archiver : archivers) {
 ArchiveInputStream inputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(archiver, testStream);
 assertNotNull(inputStream);
 // Add more assertions based on specific archive types and their functionality
 inputStream.close();
 }
 }

 // Test createArchiveOutputStream method with various archive types
 @Test
 public void testCreateArchiveOutputStream() throws ArchiveException, IOException {
 OutputStream testStream = new ByteArrayOutputStream();
 String[] archivers = {ArchiveStreamFactory.AR, ArchiveStreamFactory.CPIO, ArchiveStreamFactory.DUMP, ArchiveStreamFactory.JAR, ArchiveStreamFactory.TAR, ArchiveStreamFactory.ZIP};
 for (String archiver : archivers) {
 ArchiveOutputStream outputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(archiver, testStream);
 assertNotNull(outputStream);
 // Add more assertions based on specific archive types and their functionality
 outputStream.close();
 }
 }

 // Test createArchiveInputStream method with unknown archive type
 @Test(expected = ArchiveException.class)
 public void testUnknownArchiveType() throws ArchiveException, IOException {
 InputStream testStream = new ByteArrayInputStream(new byte[1024]);
 ArchiveStreamFactory factory = ArchiveStreamFactory.getInstance();
 factory.createArchiveInputStream("unknown", testStream);
 }

 // Test createArchiveOutputStream method with null output stream
 @Test(expected = IllegalArgumentException.class)
 public void testNullOutputStream() {
 ArchiveStreamFactory factory = ArchiveStreamFactory.getInstance();
 try {
 factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, null);
 } catch (IllegalArgumentException e) {
 fail("Expected IllegalArgumentException, but got " + e.getMessage());
 }
 }

 // Test createArchiveOutputStream method with null archiver name
 @Test(expected = IllegalArgumentException.class)
 public void testNullArchiverName() {
 ArchiveStreamFactory factory = ArchiveStreamFactory.getInstance();
 try {
 factory.createArchiveOutputStream(null, new ByteArrayOutputStream());
 } catch (IllegalArgumentException e) {
 fail("Expected IllegalArgumentException, but got " + e.getMessage());
 }
 }

 // Test createArchiveInputStream method with null input stream
 @Test(expected = IllegalArgumentException.class)
 public void testNullInputStream() {
 ArchiveStreamFactory factory = ArchiveStreamFactory.getInstance();
 try {
 factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, null);
 } catch (IllegalArgumentException e) {
 fail("Expected IllegalArgumentException, but got " + e.getMessage());
 }
 }

 // Test createArchiveOutputStream method with empty input stream
 @Test
 public void testEmptyInputStream() throws ArchiveException, IOException {
 OutputStream testStream = new ByteArrayOutputStream();
 ArchiveOutputStream outputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.ZIP, testStream);
 assertNotNull(outputStream);
 outputStream.close();
 }

 // Test edge cases and exception handling scenarios
 // Add more test cases for various edge cases and exception handling scenarios
 // based on specific archive types and their functionality
}