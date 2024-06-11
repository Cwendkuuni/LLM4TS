import org.apache.commons.compress.archivers.ArchiveException;
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
 public void testCreateArchiveInputStream() throws Exception {
 InputStream input = new ByteArrayInputStream("test".getBytes());
 ArchiveInputStream archiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream(ArchiveStreamFactory.ZIP, input);
 assertNotNull(archiveInputStream);
 assertEquals(ZipArchiveInputStream.class, archiveInputStream.getClass());

 archiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream(ArchiveStreamFactory.AR, input);
 assertNotNull(archiveInputStream);
 assertEquals(ArArchiveInputStream.class, archiveInputStream.getClass());

 archiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream(ArchiveStreamFactory.CPIO, input);
 assertNotNull(archiveInputStream);
 assertEquals(CpioArchiveInputStream.class, archiveInputStream.getClass());

 archiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream(ArchiveStreamFactory.DUMP, input);
 assertNotNull(archiveInputStream);
 assertEquals(DumpArchiveInputStream.class, archiveInputStream.getClass());

 archiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream(ArchiveStreamFactory.TAR, input);
 assertNotNull(archiveInputStream);
 assertEquals(TarArchiveInputStream.class, archiveInputStream.getClass());

 archiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream(ArchiveStreamFactory.JAR, input);
 assertNotNull(archiveInputStream);
 assertEquals(JarArchiveInputStream.class, archiveInputStream.getClass());

 archiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream("unknown", input);
 assertNull(archiveInputStream);

 archiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream(null, input);
 assertNull(archiveInputStream);

 archiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream(ArchiveStreamFactory.ZIP, null);
 assertNull(archiveInputStream);
 }

 @Test
 public void testCreateArchiveOutputStream() throws Exception {
 OutputStream output = new ByteArrayOutputStream();
 ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.newInstance().createArchiveOutputStream(ArchiveStreamFactory.ZIP, output);
 assertNotNull(archiveOutputStream);
 assertEquals(ZipArchiveOutputStream.class, archiveOutputStream.getClass());

 archiveOutputStream = ArchiveStreamFactory.newInstance().createArchiveOutputStream(ArchiveStreamFactory.AR, output);
 assertNotNull(archiveOutputStream);
 assertEquals(ArArchiveOutputStream.class, archiveOutputStream.getClass());

 archiveOutputStream = ArchiveStreamFactory.newInstance().createArchiveOutputStream(ArchiveStreamFactory.CPIO, output);
 assertNotNull(archiveOutputStream);
 assertEquals(CpioArchiveOutputStream.class, archiveOutputStream.getClass());

 archiveOutputStream = ArchiveStreamFactory.newInstance().createArchiveOutputStream(ArchiveStreamFactory.DUMP, output);
 assertNotNull(archiveOutputStream);
 assertEquals(DumpArchiveOutputStream.class, archiveOutputStream.getClass());

 archiveOutputStream = ArchiveStreamFactory.newInstance().createArchiveOutputStream(ArchiveStreamFactory.TAR, output);
 assertNotNull(archiveOutputStream);
 assertEquals(TarArchiveOutputStream.class, archiveOutputStream.getClass());

 archiveOutputStream = ArchiveStreamFactory.newInstance().createArchiveOutputStream(ArchiveStreamFactory.JAR, output);
 assertNotNull(archiveOutputStream);
 assertEquals(JarArchiveOutputStream.class, archiveOutputStream.getClass());

 archiveOutputStream = ArchiveStreamFactory.newInstance().createArchiveOutputStream("unknown", output);
 assertNull(archiveOutputStream);

 archiveOutputStream = ArchiveStreamFactory.newInstance().createArchiveOutputStream(null, output);
 assertNull(archiveOutputStream);
 }

 @Test
 public void testCreateArchiveInputStreamFromBytes() throws Exception {
 byte[] bytes = "test".getBytes();
 InputStream input = new ByteArrayInputStream(bytes);
 ArchiveInputStream archiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream(ArchiveStreamFactory.ZIP, input);
 assertNotNull(archiveInputStream);
 assertEquals(ZipArchiveInputStream.class, archiveInputStream.getClass());

 archiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream(ArchiveStreamFactory.AR, input);
 assertNotNull(archiveInputStream);
 assertEquals(ArArchiveInputStream.class, archiveInputStream.getClass());

 archiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream(ArchiveStreamFactory.CPIO, input);
 assertNotNull(archiveInputStream);
 assertEquals(CpioArchiveInputStream.class, archiveInputStream.getClass());

 archiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream(ArchiveStreamFactory.DUMP, input);
 assertNotNull(archiveInputStream);
 assertEquals(DumpArchiveInputStream.class, archiveInputStream.getClass());

 archiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream(ArchiveStreamFactory.TAR, input);
 assertNotNull(archiveInputStream);
 assertEquals(TarArchiveInputStream.class, archiveInputStream.getClass());

 archiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream(ArchiveStreamFactory.JAR, input);
 assertNotNull(archiveInputStream);
 assertEquals(JarArchiveInputStream.class, archiveInputStream.getClass());

 archiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream("unknown", input);
 assertNull(archiveInputStream);

 archiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream(null, input);
 assertNull(archiveInputStream);

 archiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream(ArchiveStreamFactory.ZIP, null);
 assertNull(archiveInputStream);
 }

 @Test(expected = ArchiveException.class)
 public void testUnknownArchiverNameException() throws Exception {
 InputStream input = new ByteArrayInputStream("test".getBytes());
 ArchiveStreamFactory factory = new ArchiveStreamFactory();
 factory.createArchiveInputStream("unknown", input);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullArchiverNameException() throws Exception {
 InputStream input = new ByteArrayInputStream("test".getBytes());
 ArchiveStreamFactory factory = new ArchiveStreamFactory();
 factory.createArchiveInputStream(null, input);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullInputStreamException() throws Exception {
 ArchiveStreamFactory factory = new ArchiveStreamFactory();
 factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, null);
 }

 @Test(expected = ArchiveException.class)
 public void testNullInputStreamWithMarkException() throws Exception {
 InputStream input = new ByteArrayInputStream("test".getBytes());
 ArchiveStreamFactory factory = new ArchiveStreamFactory();
 assertFalse(input.markSupported());
 factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, input);
 }
}