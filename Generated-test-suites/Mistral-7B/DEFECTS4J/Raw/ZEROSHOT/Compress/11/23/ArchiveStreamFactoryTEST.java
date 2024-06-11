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
import static org.junit.Assert.assertNotNull;

public class ArchiveStreamFactoryTest {

 @Test
 public void testCreateArchiveInputStream() throws ArchiveException, IOException {
 // Test createArchiveInputStream with known archives
 ArchiveInputStream archiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream(ArchiveStreamFactory.AR, new ByteArrayInputStream(new byte[0]));
 assertNotNull(archiveInputStream);
 archiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream(ArchiveStreamFactory.CPIO, new ByteArrayInputStream(new byte[0]));
 assertNotNull(archiveInputStream);
 archiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream(ArchiveStreamFactory.DUMP, new ByteArrayInputStream(new byte[0]));
 assertNotNull(archiveInputStream);
 archiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream(ArchiveStreamFactory.JAR, new ByteArrayInputStream(new byte[0]));
 assertNotNull(archiveInputStream);
 archiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream(ArchiveStreamFactory.TAR, new ByteArrayInputStream(new byte[0]));
 assertNotNull(archiveInputStream);
 archiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream(ArchiveStreamFactory.ZIP, new ByteArrayInputStream(new byte[0]));
 assertNotNull(archiveInputStream);

 // Test createArchiveInputStream with unknown archives
 try {
 ArchiveInputStream unknownArchiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream("unknown", new ByteArrayInputStream(new byte[0]));
 fail("Unexpectedly created ArchiveInputStream for unknown archive format.");
 } catch (ArchiveException e) {
 // Expected
 }

 // Test createArchiveInputStream with invalid input stream
 try {
 ArchiveInputStream invalidArchiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream(ArchiveStreamFactory.ZIP, new InputStream() {
 @Override
 public int read() throws IOException {
 return -1;
 }

 @Override
 public long skip(long n) throws IOException {
 return 0;
 }

 @Override
 public boolean markSupported() {
 return false;
 }

 @Override
 public void mark(int readlimit) {
 // NOP
 }

 @Override
 public void reset() throws IOException {
 throw new IOException("Mark not supported.");
 }

 @Override
 public void close() throws IOException {
 // NOP
 }
 });
 fail("Unexpectedly created ArchiveInputStream for invalid input stream.");
 } catch (ArchiveException e) {
 // Expected
 }
 }

 @Test
 public void testCreateArchiveOutputStream() throws ArchiveException, IOException {
 // Test createArchiveOutputStream with known archives
 OutputStream outputStream = new ByteArrayOutputStream();
 ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.newInstance().createArchiveOutputStream(ArchiveStreamFactory.AR, outputStream);
 assertNotNull(archiveOutputStream);
 archiveOutputStream = ArchiveStreamFactory.newInstance().createArchiveOutputStream(ArchiveStreamFactory.CPIO, outputStream);
 assertNotNull(archiveOutputStream);
 archiveOutputStream = ArchiveStreamFactory.newInstance().createArchiveOutputStream(ArchiveStreamFactory.DUMP, outputStream);
 assertNotNull(archiveOutputStream);
 archiveOutputStream = ArchiveStreamFactory.newInstance().createArchiveOutputStream(ArchiveStreamFactory.JAR, outputStream);
 assertNotNull(archiveOutputStream);
 archiveOutputStream = ArchiveStreamFactory.newInstance().createArchiveOutputStream(ArchiveStreamFactory.TAR, outputStream);
 assertNotNull(archiveOutputStream);
 archiveOutputStream = ArchiveStreamFactory.newInstance().createArchiveOutputStream(ArchiveStreamFactory.ZIP, outputStream);
 assertNotNull(archiveOutputStream);

 // Test createArchiveOutputStream with unknown archives
 try {
 ArchiveOutputStream unknownArchiveOutputStream = ArchiveStreamFactory.newInstance().createArchiveOutputStream("unknown", outputStream);
 fail("Unexpectedly created ArchiveOutputStream for unknown archive format.");
 } catch (ArchiveException e) {
 // Expected
 }

 // Test createArchiveOutputStream with invalid output stream
 try {
 OutputStream invalidOutputStream = new OutputStream() {
 @Override
 public void write(int b) throws IOException {
 // NOP
 }

 @Override
 public void close() throws IOException {
 // NOP
 }
 };
 ArchiveOutputStream invalidArchiveOutputStream = ArchiveStreamFactory.newInstance().createArchiveOutputStream(ArchiveStreamFactory.ZIP, invalidOutputStream);
 fail("Unexpectedly created ArchiveOutputStream for invalid output stream.");
 } catch (ArchiveException e) {
 // Expected
 }

 // Test createArchiveOutputStream using the autodetection feature
 OutputStream autodetectOutputStream = new ByteArrayOutputStream();
 ArchiveOutputStream autodetectArchiveOutputStream = ArchiveStreamFactory.newInstance().createArchiveOutputStream(autodetectOutputStream);
 assertNotNull(autodetectArchiveOutputStream);
 // Add known archive entries to the output stream
 autodetectArchiveOutputStream.putArchiveEntry(new ArArchiveEntry("test.txt"));
 autodetectArchiveOutputStream.writeToArchive("test.txt".getBytes());
 autodetectArchiveOutputStream.finish();
 ArchiveInputStream autodetectArchiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream(ArchiveStreamFactory.AR, new ByteArrayInputStream(autodetectOutputStream.toByteArray()));
 assertNotNull(autodetectArchiveInputStream);
 autodetectArchiveInputStream.close();
 }
}