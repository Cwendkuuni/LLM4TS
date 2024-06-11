import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.JarArchiveInputStream;
import org.apache.commons.compress.archivers.JarArchiveOutputStream;
import org.apache.commons.compress.archivers.TarArchiveInputStream;
import org.apache.commons.compress.archivers.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(org.junit.runners.JUnit4.class)
public class ArchiveStreamFactoryTest {

 private ArchiveStreamFactory factory;

 @Test
 public void testCreateArchiveInputStream_AR() throws Exception {
 InputStream inputStream = Mockito.mock(InputStream.class);
 ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.AR, inputStream);
 assertTrue(archiveInputStream instanceof ArArchiveInputStream);
 }

 @Test
 public void testCreateArchiveInputStream_ZIP() throws Exception {
 InputStream inputStream = new ByteArrayInputStream("ZIP signature".getBytes());
 ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, inputStream);
 assertTrue(archiveInputStream instanceof ZipArchiveInputStream);
 }

 @Test(expected = ArchiveException.class)
 public void testCreateArchiveInputStream_InvalidFormat() throws Exception {
 InputStream inputStream = new ByteArrayInputStream("Invalid format".getBytes());
 factory = new ArchiveStreamFactory();
 ArchiveInputStream archiveInputStream = factory.createArchiveInputStream("invalidFormat", inputStream);
 fail("Expected ArchiveException but got: " + archiveInputStream.getClass().getName());
 }

 @Test
 public void testCreateArchiveOutputStream_AR() throws Exception {
 OutputStream outputStream = Mockito.mock(OutputStream.class);
 ArchiveOutputStream archiveOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.AR, outputStream);
 assertTrue(archiveOutputStream instanceof ArArchiveOutputStream);
 }

 @Test
 public void testCreateArchiveOutputStream_ZIP() throws Exception {
 OutputStream outputStream = Mockito.mock(OutputStream.class);
 ArchiveOutputStream archiveOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, outputStream);
 assertTrue(archiveOutputStream instanceof ZipArchiveOutputStream);
 }

 @Test(expected = ArchiveException.class)
 public void testCreateArchiveOutputStream_InvalidFormat() throws Exception {
 OutputStream outputStream = Mockito.mock(OutputStream.class);
 factory = new ArchiveStreamFactory();
 ArchiveOutputStream archiveOutputStream = factory.createArchiveOutputStream("invalidFormat", outputStream);
 fail("Expected ArchiveException but got: " + archiveOutputStream.getClass().getName());
 }

 @Test
 public void testCreateArchiveInputStream_Autodetect_TAR() throws Exception {
 InputStream inputStream = new ByteArrayInputStream(new byte[512]);
 ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(inputStream);
 assertTrue(archiveInputStream instanceof TarArchiveInputStream);
 }

 @Test
 public void testCreateArchiveOutputStream_Autodetect_ZIP() throws Exception {
 InputStream inputStream = new ByteArrayInputStream("ZIP signature".getBytes());
 ArchiveOutputStream archiveOutputStream = factory.createArchiveOutputStream(inputStream);
 assertTrue(archiveOutputStream instanceof ZipArchiveOutputStream);
 }

 @Test(expected = ArchiveException.class)
 public void testCreateArchiveOutputStream_Autodetect_Unknown() throws Exception {
 InputStream inputStream = new ByteArrayInputStream("Unknown signature".getBytes());
 factory = new ArchiveStreamFactory();
 ArchiveOutputStream archiveOutputStream = factory.createArchiveOutputStream(inputStream);
 fail("Expected ArchiveException but got: " + archiveOutputStream.getClass().getName());
 }

 @Test
 public void testCreateArchiveInputStream_Autodetect_EmptyStream() throws Exception {
 InputStream inputStream = Mockito.mock(InputStream.class);
 Mockito.doThrow(new IOException("Stream empty")).when(inputStream).read();
 ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(inputStream);
 assertNull(archiveInputStream);
 }
}