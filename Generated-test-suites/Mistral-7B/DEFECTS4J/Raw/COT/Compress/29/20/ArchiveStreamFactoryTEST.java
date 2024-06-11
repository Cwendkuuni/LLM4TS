import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArjArchiveInputStream;
import org.apache.commons.compress.archivers.ArjArchiveOutputStream;
import org.apache.commons.compress.archivers.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.JarArchiveInputStream;
import org.apache.commons.compress.archivers.JarArchiveOutputStream;
import org.apache.commons.compress.archivers.TarArchiveInputStream;
import org.apache.commons.compress.archivers.TarArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArchiveStreamFactoryTest {

 private ArchiveStreamFactory factory = new ArchiveStreamFactory();

 @Test
 public void testCreateArchiveInputStream_AR() throws ArchiveException, IOException {
 ArchiveInputStream inputStream = factory.createArchiveInputStream(ArchiveStreamFactory.AR, new ByteArrayInputStream(new byte[0]));
 assertEquals("ar", inputStream.getFormat().getName());
 IOUtils.closeQuietly(inputStream);
 }

 @Test
 public void testCreateArchiveInputStream_ARJ() throws ArchiveException, IOException {
 ArchiveInputStream inputStream = factory.createArchiveInputStream(ArchiveStreamFactory.ARJ, new ByteArrayInputStream(new byte[0]));
 assertEquals("arj", inputStream.getFormat().getName());
 IOUtils.closeQuietly(inputStream);
 }

 @Test(expected = StreamingNotSupportedException.class)
 public void testCreateArchiveInputStream_SEVEN_Z() throws ArchiveException, IOException {
 factory.createArchiveInputStream(ArchiveStreamFactory.SEVEN_Z, new ByteArrayInputStream(new byte[0]));
 }

 @Test(expected = ArchiveException.class)
 public void testCreateArchiveInputStream_InvalidArchiverName() throws ArchiveException, IOException {
 try {
 factory.createArchiveInputStream("invalidArchiverName", new ByteArrayInputStream(new byte[0]));
 fail("Expected ArchiveException");
 } catch (ArchiveException e) {
 //expected
 }
 }

 @Test
 public void testCreateArchiveInputStream_ZIP() throws ArchiveException, IOException {
 ArchiveInputStream inputStream = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, new ByteArrayInputStream(new byte[0]));
 assertEquals("zip", inputStream.getFormat().getName());
 IOUtils.closeQuietly(inputStream);
 }

 @Test
 public void testCreateArchiveInputStream_CPIO() throws ArchiveException, IOException {
 ArchiveInputStream inputStream = factory.createArchiveInputStream(ArchiveStreamFactory.CPIO, new ByteArrayInputStream(new byte[0]));
 assertEquals("cpio", inputStream.getFormat().getName());
 IOUtils.closeQuietly(inputStream);
 }

 @Test
 public void testCreateArchiveInputStream_DUMP() throws ArchiveException, IOException {
 ArchiveInputStream inputStream = factory.createArchiveInputStream(ArchiveStreamFactory.DUMP, new ByteArrayInputStream(new byte[0]));
 assertEquals("dump", inputStream.getFormat().getName());
 IOUtils.closeQuietly(inputStream);
 }

 @Test
 public void testCreateArchiveInputStream_TAR() throws ArchiveException, IOException {
 ArchiveInputStream inputStream = factory.createArchiveInputStream(ArchiveStreamFactory.TAR, new ByteArrayInputStream(new byte[0]));
 assertEquals("tar", inputStream.getFormat().getName());
 IOUtils.closeQuietly(inputStream);
 }

 @Test
 public void testCreateArchiveOutputStream_AR() throws ArchiveException, IOException {
 ArchiveOutputStream outputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.AR, new ByteArrayOutputStream());
 assertEquals("ar", outputStream.getFormat().getName());
 IOUtils.closeQuietly(outputStream);
 }

 @Test
 public void testCreateArchiveOutputStream_ARJ() {
 // ARJ is not supported as an output stream type
 }

 @Test
 public void testCreateArchiveOutputStream_ZIP() throws ArchiveException, IOException {
 ArchiveOutputStream outputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, new ByteArrayOutputStream());
 assertEquals("zip", outputStream.getFormat().getName());
 IOUtils.closeQuietly(outputStream);
 }

 @Test
 public void testCreateArchiveOutputStream_TAR() throws ArchiveException, IOException {
 ArchiveOutputStream outputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.TAR, new ByteArrayOutputStream());
 assertEquals("tar", outputStream.getFormat().getName());
 IOUtils.closeQuietly(outputStream);
 }

 @Test
 public void testCreateArchiveOutputStream_JAR() throws ArchiveException, IOException {
 ArchiveOutputStream outputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.JAR, new ByteArrayOutputStream());
 assertEquals("jar", outputStream.getFormat().getName());
 IOUtils.closeQuietly(outputStream);
 }

 @Test
 public void testCreateArchiveOutputStream_CPIO() throws ArchiveException, IOException {
 ArchiveOutputStream outputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.CPIO, new ByteArrayOutputStream());
 assertEquals("cpio", outputStream.getFormat().getName());
 IOUtils.closeQuietly(outputStream);
 }

 @Test
 public void testCreateArchiveOutputStream_SEVEN_Z() throws ArchiveException, IOException {
 // SevenZFile is not an InputStream, cannot be used for testing createArchiveOutputStream
 }

 @Test
 public void testCreateArchiveInputStream_InvalidInputStream() throws ArchiveException, IOException {
 try {
 factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, null);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 //expected
 }
 }

 @Test
 public void testCreateArchiveOutputStream_InvalidOutputStream() throws ArchiveException, IOException {
 try {
 factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, null);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 //expected
 }
 }

 @Test(expected = ArchiveException.class)
 public void testCreateArchiveInputStream_InvalidFormat() throws ArchiveException, IOException {
 try {
 factory.createArchiveInputStream("invalidFormat", new ByteArrayInputStream(new byte[0]));
 fail("Expected ArchiveException");
 } catch (ArchiveException e) {
 //expected
 }
 }

 @Test
 public void testGetEntryEncoding() {
 String encoding = factory.getEntryEncoding();
 assertNull(encoding);
 }

 @Test
 public void testSetEntryEncoding() {
 factory.setEntryEncoding("UTF-8");
 assertEquals("UTF-8", factory.getEntryEncoding());
 }

}