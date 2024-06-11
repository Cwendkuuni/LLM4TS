import org.apache.commons.compress.archivers.*;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;

public class ArchiveStreamFactoryTest {

 private ArchiveStreamFactory factory;
 private InputStream inputStream;
 private OutputStream outputStream;

 @Before
 public void setup() {
 factory = new ArchiveStreamFactory();
 }

 @Test
 public void testCreateArchiveInputStreamAR() throws Exception {
 InputStream in = new ByteArrayInputStream(new byte[1024]);
 ArchiveInputStream aris = factory.createArchiveInputStream(ArchiveStreamFactory.AR, in);
 assertTrue(aris instanceof ArArchiveInputStream);
 aris.close();
 }

 @Test
 public void testCreateArchiveInputStreamCPIO() throws Exception {
 InputStream in = new ByteArrayInputStream(new byte[1024]);
 ArchiveInputStream cpiois = factory.createArchiveInputStream(ArchiveStreamFactory.CPIO, in);
 assertTrue(cpiois instanceof CpioArchiveInputStream);
 cpiois.close();
 }

 @Test
 public void testCreateArchiveInputStreamDUMP() throws Exception {
 InputStream in = new ByteArrayInputStream(new byte[1024]);
 ArchiveInputStream dumpis = factory.createArchiveInputStream(ArchiveStreamFactory.DUMP, in);
 assertTrue(dumpis instanceof DumpArchiveInputStream);
 dumpis.close();
 }

 @Test
 public void testCreateArchiveInputStreamJAR() throws Exception {
 InputStream in = new ByteArrayInputStream(new byte[1024]);
 ArchiveInputStream jaris = factory.createArchiveInputStream(ArchiveStreamFactory.JAR, in);
 assertTrue(jaris instanceof JarArchiveInputStream);
 JarArchiveEntry entry = (JarArchiveEntry) jaris.getNextEntry();
 assertNotNull(entry);
 jaris.close();
 }

 @Test
 public void testCreateArchiveInputStreamTAR() throws Exception {
 InputStream in = new ByteArrayInputStream(new byte[1024]);
 TarArchiveEntry tarEntry = new TarArchiveEntry("test.txt");
 OutputStream out = new ByteArrayOutputStream();
 ArchiveOutputStream taros = factory.createArchiveOutputStream(ArchiveStreamFactory.TAR, out);
 taros.putNextEntry(tarEntry);
 taros.write(new byte[1024], 0, 1024);
 taros.closeArchiveEntry();
 taros.close();
 InputStream taris = factory.createArchiveInputStream(ArchiveStreamFactory.TAR, new ByteArrayInputStream(out.toByteArray()));
 TarArchiveEntry tarisEntry = (TarArchiveEntry) taris.getNextEntry();
 assertNotNull(tarisEntry);
 taris.close();
 }

 @Test
 public void testCreateArchiveInputStreamZIP() throws Exception {
 InputStream in = new ByteArrayInputStream(new byte[1024]);
 ArchiveInputStream zipis = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, in);
 assertTrue(zipis instanceof ZipArchiveInputStream);
 ZipArchiveEntry entry = (ZipArchiveEntry) zipis.getNextEntry();
 assertNotNull(entry);
 zipis.close();
 }

 @Test(expected = ArchiveException.class)
 public void testCreateArchiveInputStreamUnknownFormat() throws Exception {
 InputStream in = new ByteArrayInputStream(new byte[1024]);
 factory.createArchiveInputStream("unknown", in);
 fail("Expected ArchiveException not thrown.");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateArchiveOutputStreamNullArchiverName() {
 try {
 factory.createArchiveOutputStream(null, new ByteArrayOutputStream());
 fail("Expected IllegalArgumentException not thrown.");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateArchiveOutputStreamNullOutputStream() {
 try {
 factory.createArchiveOutputStream("tar", null);
 fail("Expected IllegalArgumentException not thrown.");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testCreateArchiveOutputStreamAR() throws Exception {
 OutputStream out = new ByteArrayOutputStream();
 ArchiveOutputStream aros = factory.createArchiveOutputStream(ArchiveStreamFactory.AR, out);
 ArchiveEntry archiveEntry = new ArArchiveEntry("test.txt", "test data");
 aros.putArchiveEntry(archiveEntry);
 aros.finish();
 byte[] expected = new byte[4096]; // 4 kB buffer
 byte[] result = new byte[4096];
 int bytesRead = IOUtils.read(aros.getOutputStream(), result, 0, result.length);
 IOUtils.closeQuietly(aros);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testCreateArchiveOutputStreamCPIO() throws Exception {
 OutputStream out = new ByteArrayOutputStream();
 ArchiveOutputStream cpioos = factory.createArchiveOutputStream(ArchiveStreamFactory.CPIO, out);
 ArchiveEntry archiveEntry = new CpioArchiveEntry("test.txt", "test data");
 cpioos.putArchiveEntry(archiveEntry);
 cpioos.finish();
 byte[] expected = new byte[4096]; // 4 kB buffer
 byte[] result = new byte[4096];
 int bytesRead = IOUtils.read(cpioos.getOutputStream(), result, 0, result.length);
 IOUtils.closeQuietly(cpioos);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testCreateArchiveOutputStreamDUMP() throws Exception {
 OutputStream out = new ByteArrayOutputStream();
 ArchiveOutputStream dumpos = factory.createArchiveOutputStream(ArchiveStreamFactory.DUMP, out);
 ArchiveEntry archiveEntry = new DumpArchiveEntry("test.txt", "test data");
 dumpos.putArchiveEntry(archiveEntry);
 dumpos.finish();
 byte[] expected = new byte[4096]; // 4 kB buffer
 byte[] result = new byte[4096];
 int bytesRead = IOUtils.read(dumpos.getOutputStream(), result, 0, result.length);
 IOUtils.closeQuietly(dumpos);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testCreateArchiveOutputStreamJAR() throws Exception {
 OutputStream out = new ByteArrayOutputStream();
 ArchiveOutputStream jaros = factory.createArchiveOutputStream(ArchiveStreamFactory.JAR, out);
 JarArchiveEntry archiveEntry = new JarArchiveEntry("test.txt", "test data");
 jaros.putNextEntry(archiveEntry);
 jaros.finish();
 byte[] expected = new byte[4096]; // 4 kB buffer
 byte[] result = new byte[4096];
 int bytesRead = IOUtils.read(jaros.getOutputStream(), result, 0, result.length);
 IOUtils.closeQuietly(jaros);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testCreateArchiveOutputStreamTAR() throws Exception {
 OutputStream out = new ByteArrayOutputStream();
 TarArchiveOutputStream taros = (TarArchiveOutputStream) factory.createArchiveOutputStream(ArchiveStreamFactory.TAR, out);
 TarArchiveEntry archiveEntry = new TarArchiveEntry("test.txt", "test data");
 taros.putNextEntry(archiveEntry);
 taros.finish();
 byte[] expected = new byte[1024]; // 1 kB buffer
 byte[] result = new byte[1024];
 int bytesRead = IOUtils.read(taros.getOutputStream(), result, 0, result.length);
 IOUtils.closeQuietly(taros);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testCreateArchiveOutputStreamZIP() throws Exception {
 OutputStream out = new ByteArrayOutputStream();
 ArchiveOutputStream zipos = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, out);
 ZipArchiveEntry archiveEntry = new ZipArchiveEntry("test.txt", "test data");
 zipos.putNextEntry(archiveEntry);
 zipos.finish();
 byte[] expected = new byte[4096]; // 4 kB buffer
 byte[] result = new byte[4096];
 int bytesRead = IOUtils.read(zipos.getOutputStream(), result, 0, result.length);
 IOUtils.closeQuietly(zipos);
 assertArrayEquals(expected, result);
 }

 @Test(expected = ArchiveException.class)
 public void testCreateArchiveOutputStreamNullArchiverName() {
 try {
 factory.createArchiveOutputStream(null, new ByteArrayOutputStream());
 fail("Expected ArchiveException not thrown.");
 } catch (ArchiveException e) {
 // expected
 }
 }
}