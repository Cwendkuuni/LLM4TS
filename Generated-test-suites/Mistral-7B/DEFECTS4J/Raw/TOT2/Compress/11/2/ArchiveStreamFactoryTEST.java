import org.apache.commons.compress.archivers.*;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.format.TarHeader;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArchiveStreamFactoryTest {

 // Test createArchiveInputStream() with valid arguments
 @Test
 public void testCreateArchiveInputStream_validArguments() throws ArchiveException, IOException {
 InputStream input = new ByteArrayInputStream("valid archive".getBytes());
 String archiverName = "zip";
 ArchiveInputStream archiveInput = new ArchiveStreamFactory().createArchiveInputStream(archiverName, input);
 assertNotNull(archiveInput);
 // Add more tests for other archiver types
 }

 // Test createArchiveInputStream() with null input stream
 @Test(expected = IllegalArgumentException.class)
 public void testCreateArchiveInputStream_nullInputStream() {
 String archiverName = "tar";
 new ArchiveStreamFactory().createArchiveInputStream(archiverName, null);
 }

 // Test createArchiveInputStream() with archiverName not found
 @Test(expected = ArchiveException.class)
 public void testCreateArchiveInputStream_unknownArchiver() {
 InputStream input = new ByteArrayInputStream("valid archive".getBytes());
 String archiverName = "unknown";
 new ArchiveStreamFactory().createArchiveInputStream(archiverName, input);
 }

 // Test createArchiveOutputStream() with valid arguments
 @Test
 public void testCreateArchiveOutputStream_validArguments() throws ArchiveException, IOException {
 OutputStream output = new ByteArrayOutputStream();
 String archiverName = "tar";
 ArchiveOutputStream archiveOutput = new ArchiveStreamFactory().createArchiveOutputStream(archiverName, output);
 archiveOutput.putNextEntry(new TarHeader("test.txt", (short) 0, (long) 10));
 archiveOutput.write(new byte[10], 0, 10);
 archiveOutput.closeArchiveEntry();
 archiveOutput.finish();
 assertNotNull(archiveOutput);
 // Add more tests for other archiver types
 }

 // Test createArchiveOutputStream() with null output stream
 @Test(expected = IllegalArgumentException.class)
 public void testCreateArchiveOutputStream_nullOutputStream() {
 String archiverName = "zip";
 new ArchiveStreamFactory().createArchiveOutputStream(archiverName, null);
 }

 // Test createArchiveOutputStream() with archiverName not found
 @Test(expected = ArchiveException.class)
 public void testCreateArchiveOutputStream_unknownArchiver() {
 OutputStream output = new ByteArrayOutputStream();
 String archiverName = "unknown";
 new ArchiveStreamFactory().createArchiveOutputStream(archiverName, output);
 }

 // Test createArchiveInputStream() with autodetection
 @Test
 public void testCreateArchiveInputStream_autodetection() throws ArchiveException, IOException {
 InputStream input = new ByteArrayInputStream("valid archive".getBytes());
 ArchiveInputStream archiveInput = new ArchiveStreamFactory().createArchiveInputStream(input);
 assertNotNull(archiveInput);
 // Add more tests for different archive types
 }

 // Test createArchiveInputStream() with broken signature
 @Test(expected = ArchiveException.class)
 public void testCreateArchiveInputStream_brokenSignature() throws ArchiveException, IOException {
 InputStream input = new ByteArrayInputStream("broken signature".getBytes());
 new ArchiveStreamFactory().createArchiveInputStream("unknown", input);
 }
}