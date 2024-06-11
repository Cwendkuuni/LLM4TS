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
import static org.junit.Assert.assertNotNull;

public class ArchiveStreamFactoryTest {

 @Test
 public void testCreateArchiveInputStream() throws Exception {
 ArchiveStreamFactory factory = new ArchiveStreamFactory();

 // Test with valid archiver names and InputStreams
 InputStream arInputStream = new ByteArrayInputStream(new byte[10]);
 ArchiveInputStream arResult = factory.createArchiveInputStream(ArchiveStreamFactory.AR, arInputStream);
 assertNotNull(arResult);
 assertNotNull(arResult.getInputStream());

 InputStream tarInputStream = new ByteArrayInputStream(new byte[10]);
 ArchiveInputStream tarResult = factory.createArchiveInputStream(ArchiveStreamFactory.TAR, tarInputStream);
 assertNotNull(tarResult);
 assertNotNull(tarResult.getInputStream());

 InputStream zipInputStream = new ByteArrayInputStream(new byte[10]);
 ArchiveInputStream zipResult = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, zipInputStream);
 assertNotNull(zipResult);
 assertNotNull(zipResult.getInputStream());

 InputStream jarInputStream = new ByteArrayInputStream(new byte[10]);
 ArchiveInputStream jarResult = factory.createArchiveInputStream(ArchiveStreamFactory.JAR, jarInputStream);
 assertNotNull(jarResult);
 assertNotNull(jarResult.getInputStream());

 InputStream cpioInputStream = new ByteArrayInputStream(new byte[10]);
 ArchiveInputStream cpioResult = factory.createArchiveInputStream(ArchiveStreamFactory.CPIO, cpioInputStream);
 assertNotNull(cpioResult);
 assertNotNull(cpioResult.getInputStream());

 InputStream dumpInputStream = new ByteArrayInputStream(new byte[10]);
 ArchiveInputStream dumpResult = factory.createArchiveInputStream(ArchiveStreamFactory.DUMP, dumpInputStream);
 assertNotNull(dumpResult);
 assertNotNull(dumpResult.getInputStream());

 // Test with invalid archiver name
 InputStream invalidArchiverNameInputStream = new ByteArrayInputStream(new byte[10]);
 ArchiveInputStream invalidArchiverNameResult = null;
 try {
 invalidArchiverNameResult = factory.createArchiveInputStream("invalidArchiverName", invalidArchiverNameInputStream);
 } catch (ArchiveException e) {
 // Expected
 }
 assertNull(invalidArchiverNameResult);
 }

 @Test
 public void testCreateArchiveOutputStream() throws Exception {
 ArchiveStreamFactory factory = new ArchiveStreamFactory();

 // Test with valid archiver names and OutputStreams
 OutputStream arOutputStream = new ByteArrayOutputStream();
 ArchiveOutputStream arResult = factory.createArchiveOutputStream(ArchiveStreamFactory.AR, arOutputStream);
 assertNotNull(arResult);
 assertNotNull(arResult.getArchiveEntry());

 OutputStream tarOutputStream = new ByteArrayOutputStream();
 ArchiveOutputStream tarResult = factory.createArchiveOutputStream(ArchiveStreamFactory.TAR, tarOutputStream);
 assertNotNull(tarResult);
 assertNotNull(tarResult.getArchiveEntry());

 OutputStream zipOutputStream = new ByteArrayOutputStream();
 ArchiveOutputStream zipResult = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, zipOutputStream);
 assertNotNull(zipResult);
 assertNotNull(zipResult.getArchiveEntry());

 OutputStream jarOutputStream = new ByteArrayOutputStream();
 ArchiveOutputStream jarResult = factory.createArchiveOutputStream(ArchiveStreamFactory.JAR, jarOutputStream);
 assertNotNull(jarResult);
 assertNotNull(jarResult.getArchiveEntry());

 OutputStream cpioOutputStream = new ByteArrayOutputStream();
 ArchiveOutputStream cpioResult = factory.createArchiveOutputStream(ArchiveStreamFactory.CPIO, cpioOutputStream);
 assertNotNull(cpioResult);
 assertNotNull(cpioResult.getArchiveEntry());

 OutputStream dumpOutputStream = new ByteArrayOutputStream();
 ArchiveOutputStream dumpResult = factory.createArchiveOutputStream(ArchiveStreamFactory.DUMP, dumpOutputStream);
 assertNotNull(dumpResult);
 assertNotNull(dumpResult.getArchiveEntry());

 // Test with null OutputStream
 OutputStream nullOutputStream = null;
 ArchiveOutputStream nullResult = null;
 try {
 nullResult = factory.createArchiveOutputStream(ArchiveStreamFactory.AR, nullOutputStream);
 } catch (IllegalArgumentException e) {
 // Expected
 }
 assertNull(nullResult);
 }

 @Test
 public void testCreateArchiveInputStreamFromInputStream() throws Exception {
 ArchiveStreamFactory factory = new ArchiveStreamFactory();

 InputStream testInputStream = new ByteArrayInputStream(new byte[10]);
 ArchiveInputStream result = factory.createArchiveInputStream(testInputStream);
 assertNotNull(result);
 }
}