import org.apache.commons.compress.archivers.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ArjArchiveInputStream;
import org.apache.commons.compress.archivers.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.JarArchiveInputStream;
import org.apache.commons.compress.archivers.TarArchiveInputStream;
import org.apache.commons.compress.archivers.ZipArchiveInputStream;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArchiveStreamFactoryTest {

 private ArchiveStreamFactory factory = new ArchiveStreamFactory();

 @Test
 public void testCreateArchiveInputStreamAR() {
 ArchiveInputStream inputStream = null;
 try {
 inputStream = factory.createArchiveInputStream(ArchiveStreamFactory.AR, new ByteArrayInputStream(new byte[0]));
 assertTrue(inputStream instanceof ArArchiveInputStream);
 } catch (ArchiveException e) {
 e.printStackTrace();
 fail("Failed to create ArArchiveInputStream.");
 } finally {
 IOUtils.closeQuietly(inputStream);
 }
 }

 @Test
 public void testCreateArchiveInputStreamARJ() {
 ArchiveInputStream inputStream = null;
 try {
 inputStream = factory.createArchiveInputStream(ArchiveStreamFactory.ARJ, new ByteArrayInputStream(new byte[0]));
 fail("Expected StreamingNotSupportedException, got " + inputStream.getClass().getName());
 } catch (StreamingNotSupportedException e) {
 // Expected
 }
 }

 @Test
 public void testCreateArchiveInputStreamCPIO() {
 ArchiveInputStream inputStream = null;
 try {
 inputStream = factory.createArchiveInputStream(ArchiveStreamFactory.CPIO, new ByteArrayInputStream(new byte[0]));
 assertTrue(inputStream instanceof CpioArchiveInputStream);
 } catch (ArchiveException e) {
 e.printStackTrace();
 fail("Failed to create CpioArchiveInputStream.");
 } finally {
 IOUtils.closeQuietly(inputStream);
 }
 }

 @Test
 public void testCreateArchiveInputStreamDump() {
 ArchiveInputStream inputStream = null;
 try {
 inputStream = factory.createArchiveInputStream(ArchiveStreamFactory.DUMP, new ByteArrayInputStream(new byte[0]));
 assertTrue(inputStream instanceof DumpArchiveInputStream);
 } catch (ArchiveException e) {
 e.printStackTrace();
 fail("Failed to create DumpArchiveInputStream.");
 } finally {
 IOUtils.closeQuietly(inputStream);
 }
 }

 @Test
 public void testCreateArchiveInputStreamJAR() {
 ArchiveInputStream inputStream = null;
 try {
 inputStream = factory.createArchiveInputStream(ArchiveStreamFactory.JAR, new ByteArrayInputStream(new byte[0]));
 assertTrue(inputStream instanceof JarArchiveInputStream);
 } catch (ArchiveException e) {
 e.printStackTrace();
 fail("Failed to create JarArchiveInputStream.");
 } finally {
 IOUtils.closeQuietly(inputStream);
 }
 }

 @Test
 public void testCreateArchiveInputStreamTAR() {
 ArchiveInputStream inputStream = null;
 try {
 inputStream = factory.createArchiveInputStream(ArchiveStreamFactory.TAR, new ByteArrayInputStream(new byte[0]));
 assertTrue(inputStream instanceof TarArchiveInputStream);
 } catch (ArchiveException e) {
 e.printStackTrace();
 fail("Failed to create TarArchiveInputStream.");
 } finally {
 IOUtils.closeQuietly(inputStream);
 }
 }

 @Test
 public void testCreateArchiveInputStreamZIP() {
 ArchiveInputStream inputStream = null;
 try {
 inputStream = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, new ByteArrayInputStream(new byte[0]));
 assertTrue(inputStream instanceof ZipArchiveInputStream);
 } catch (ArchiveException e) {
 e.printStackTrace();
 fail("Failed to create ZipArchiveInputStream.");
 } finally {
 IOUtils.closeQuietly(inputStream);
 }
 }

 @Test(expected = ArchiveException.class)
 public void testCreateArchiveInputStreamUnknownFormat() {
 ArchiveInputStream inputStream = null;
 try {
 inputStream = factory.createArchiveInputStream("unknown", new ByteArrayInputStream(new byte[0]));
 fail("Expected ArchiveException, got " + inputStream.getClass().getName());
 } catch (ArchiveException e) {
 // Expected
 } finally {
 IOUtils.closeQuietly(inputStream);
 }
 }

 @Test
 public void testCreateArchiveOutputStreamAR() {
 ArchiveOutputStream outputStream = null;
 try {
 outputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.AR, new ByteArrayOutputStream());
 assertTrue(outputStream instanceof ArArchiveOutputStream);
 } catch (ArchiveException e) {
 e.printStackTrace();
 fail("Failed to create ArArchiveOutputStream.");
 } finally {
 IOUtils.closeQuietly(outputStream);
 }
 }

 @Test
 public void testCreateArchiveOutputStreamARJ() {
 ArchiveOutputStream outputStream = null;
 try {
 outputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.ARJ, new ByteArrayOutputStream());
 fail("Expected StreamingNotSupportedException, got " + outputStream.getClass().getName());
 } catch (StreamingNotSupportedException e) {
 // Expected
 }
 }

 @Test
 public void testCreateArchiveOutputStreamCPIO() {
 ArchiveOutputStream outputStream = null;
 try {
 outputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.CPIO, new ByteArrayOutputStream());
 assertTrue(outputStream instanceof CpioArchiveOutputStream);
 } catch (ArchiveException e) {
 e.printStackTrace();
 fail("Failed to create CpioArchiveOutputStream.");
 } finally {
 IOUtils.closeQuietly(outputStream);
 }
 }

 @Test
 public void testCreateArchiveOutputStreamJAR() {
 ArchiveOutputStream outputStream = null;
 try {
 outputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.JAR, new ByteArrayOutputStream());
 assertTrue(outputStream instanceof JarArchiveOutputStream);
 } catch (ArchiveException e) {
 e.printStackTrace();
 fail("Failed to create JarArchiveOutputStream.");
 } finally {
 IOUtils.closeQuietly(outputStream);
 }
 }

 @Test
 public void testCreateArchiveOutputStreamTAR() {
 ArchiveOutputStream outputStream = null;
 try {
 outputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.TAR, new ByteArrayOutputStream());
 assertTrue(outputStream instanceof TarArchiveOutputStream);
 } catch (ArchiveException e) {
 e.printStackTrace();
 fail("Failed to create TarArchiveOutputStream.");
 } finally {
 IOUtils.closeQuietly(outputStream);
 }
 }

 @Test
 public void testCreateArchiveOutputStreamZIP() {
 ArchiveOutputStream outputStream = null;
 try {
 outputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, new ByteArrayOutputStream());
 assertTrue(outputStream instanceof ZipArchiveOutputStream);
 } catch (ArchiveException e) {
 e.printStackTrace();
 fail("Failed to create ZipArchiveOutputStream.");
 } finally {
 IOUtils.closeQuietly(outputStream);
 }
 }

 @Test(expected = ArchiveException.class)
 public void testCreateArchiveOutputStreamUnknownFormat() {
 ArchiveOutputStream outputStream = null;
 try {
 outputStream = factory.createArchiveOutputStream("unknown", new ByteArrayOutputStream());
 fail("Expected ArchiveException, got " + outputStream.getClass().getName());
 } catch (ArchiveException e) {
 // Expected
 } finally {
 IOUtils.closeQuietly(outputStream);
 }
 }

 @Test
 public void testCreateArchiveInputStreamWithEntryEncoding() {
 ArchiveInputStream inputStream = null;
 String encoding = "UTF-8";
 try {
 inputStream = factory.createArchiveInputStream(ArchiveStreamFactory.JAR, new ByteArrayInputStream(new byte[0]), encoding);
 assertTrue(inputStream instanceof JarArchiveInputStream);
 } catch (ArchiveException e) {
 e.printStackTrace();
 fail("Failed to create JarArchiveInputStream with encoding.");
 } finally {
 IOUtils.closeQuietly(inputStream);
 }
 }

 @Test
 public void testCreateArchiveOutputStreamWithEntryEncodingAR() {
 ArchiveOutputStream outputStream = null;
 String encoding = "UTF-8";
 try {
 outputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.AR, new ByteArrayOutputStream(), encoding);
 fail("Expected StreamingNotSupportedException, got " + outputStream.getClass().getName());
 } catch (StreamingNotSupportedException e) {
 // Expected
 } finally {
 IOUtils.closeQuietly(outputStream);
 }
 }

 @Test
 public void testCreateArchiveOutputStreamWithEntryEncodingCPIO() {
 ArchiveOutputStream outputStream = null;
 String encoding = "UTF-8";
 try {
 outputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.CPIO, new ByteArrayOutputStream(), encoding);
 assertTrue(outputStream instanceof CpioArchiveOutputStream);
 } catch (ArchiveException e) {
 e.printStackTrace();
 fail("Failed to create CpioArchiveOutputStream with encoding.");
 } finally {
 IOUtils.closeQuietly(outputStream);
 }
 }

 @Test
 public void testCreateArchiveOutputStreamWithEntryEncodingJAR() {
 ArchiveOutputStream outputStream = null;
 String encoding = "UTF-8";
 try {
 outputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.JAR, new ByteArrayOutputStream(), encoding);
 assertTrue(outputStream instanceof JarArchiveOutputStream);
 } catch (ArchiveException e) {
 e.printStackTrace();
 fail("Failed to create JarArchiveOutputStream with encoding.");
 } finally {
 IOUtils.closeQuietly(outputStream);
 }
 }

 @Test
 public void testCreateArchiveOutputStreamWithEntryEncodingTAR() {
 ArchiveOutputStream outputStream = null;
 String encoding = "UTF-8";
 try {
 outputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.TAR, new ByteArrayOutputStream(), encoding);
 assertTrue(outputStream instanceof TarArchiveOutputStream);
 } catch (ArchiveException e) {
 e.printStackTrace();
 fail("Failed to create TarArchiveOutputStream with encoding.");
 } finally {
 IOUtils.closeQuietly(outputStream);
 }
 }

 @Test
 public void testCreateArchiveOutputStreamWithEntryEncodingZIP() {
 ArchiveOutputStream outputStream = null;
 String encoding = "UTF-8";
 try {
 outputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, new ByteArrayOutputStream(), encoding);
 assertTrue(outputStream instanceof ZipArchiveOutputStream);
 } catch (ArchiveException e) {
 e.printStackTrace();
 fail("Failed to create ZipArchiveOutputStream with encoding.");
 } finally {
 IOUtils.closeQuietly(outputStream);
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateArchiveOutputStreamWithNullEntryEncoding() {
 ArchiveOutputStream outputStream = null;
 String encoding = null;
 try {
 outputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.JAR, new ByteArrayOutputStream(), encoding);
 fail("Expected IllegalArgumentException, got " + outputStream.getClass().getName());
 } catch (IllegalArgumentException e) {
 // Expected
 } finally {
 IOUtils.closeQuietly(outputStream);
 }
 }

}