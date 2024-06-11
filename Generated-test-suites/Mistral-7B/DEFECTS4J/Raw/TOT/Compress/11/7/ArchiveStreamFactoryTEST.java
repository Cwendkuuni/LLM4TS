import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ar.ARArchiveInputStream;
import org.apache.commons.compress.archivers.ar.ARArchiveOutputStream;
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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

public class ArchiveStreamFactoryTest {

 @Test
 public void testCreateArchiveInputStreamFromNameAndInputStream() throws Exception {
 ArchiveInputStream inputStream = new ArchiveStreamFactory()
 .createArchiveInputStream(ArchiveStreamFactory.ZIP, new ByteArrayInputStream(new byte[0]));
 assertNotNull(inputStream);
 assertFalse(inputStream.hasNextEntry());
 }

 @Test
 public void testCreateArchiveInputStreamFromNameAndNullInputStream() {
 assertThrows(IllegalArgumentException.class, () -> new ArchiveStreamFactory()
 .createArchiveInputStream(ArchiveStreamFactory.ZIP, null));
 }

 @Test
 public void testCreateArchiveInputStreamFromNameAndInvalidArchive() throws Exception {
 InputStream invalidArchive = new ByteArrayInputStream(new byte[0]);
 ArchiveInputStream inputStream = new ArchiveStreamFactory()
 .createArchiveInputStream("invalid", invalidArchive);
 assertThrows(ArchiveException.class, () -> inputStream.getNextEntry());
 }

 @Test
 public void testCreateArchiveOutputStreamFromNameAndOutputStream() throws Exception {
 OutputStream outputStream = new ByteArrayOutputStream();
 ArchiveOutputStream output = new ArchiveStreamFactory()
 .createArchiveOutputStream(ArchiveStreamFactory.JAR, outputStream);
 assertNotNull(output);
 assertFalse(output.isClosed());
 }

 @Test
 public void testCreateArchiveOutputStreamFromNameAndNullOutputStream() {
 assertThrows(IllegalArgumentException.class, () -> new ArchiveStreamFactory()
 .createArchiveOutputStream(ArchiveStreamFactory.JAR, null));
 }

 @Test
 public void testCreateArchiveInputStreamFromInputStream() throws Exception {
 InputStream archive = new ByteArrayInputStream(new byte[0]);
 ArchiveInputStream input = new ArchiveStreamFactory()
 .createArchiveInputStream(archive);
 assertNotNull(input);
 assertFalse(input.hasNextEntry());
 }

 @Test
 public void testCreateArchiveInputStreamFromNullInputStream() {
 assertThrows(IllegalArgumentException.class, () -> new ArchiveStreamFactory()
 .createArchiveInputStream(null));
 }

 @Test
 public void testCreateArchiveOutputStreamFromInputStreamAndDetectArchiveType() throws Exception {
 InputStream archive = new ByteArrayInputStream(new byte[0]);
 ArchiveInputStream input = new ArchiveStreamFactory()
 .createArchiveInputStream(archive);
 ArchiveOutputStream output = new ArchiveStreamFactory()
 .createArchiveOutputStream(archive);
 assertNotNull(input);
 assertNotNull(output);
 assertFalse(input.hasNextEntry());
 assertFalse(output.isClosed());
 }

 @Test
 public void testCreateIncorrectArchiveInputStreamFromInputStream() throws Exception {
 InputStream archive = new ByteArrayInputStream(new byte[0]);
 ArchiveInputStream input = new ArchiveStreamFactory()
 .createArchiveInputStream(archive);
 assertThrows(ArchiveException.class, () -> input.getNextEntry());
 }

 @Test
 public void testCreateArchiveOutputStreamFromInputStreamAndDetectWrongArchiveType() throws Exception {
 InputStream archive = new ByteArrayInputStream(new byte[0]);
 ArchiveOutputStream output = new ArchiveStreamFactory()
 .createArchiveOutputStream(ArchiveStreamFactory.AR, archive);
 assertThrows(ArchiveException.class, () -> output.putNextEntry(new JarArchiveEntry("test.jar")));
 }
}