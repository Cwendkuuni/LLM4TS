import org.apache.commons.compress.archivers.*;
import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ar.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.arj.ArjArchiveInputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveEntry;
import org.apache.commons.compress.archivers.jar.JarArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArchiveStreamFactoryTest {

 private final static String AR = ArchiveStreamFactory.AR;
 private final static String ARJ = ArchiveStreamFactory.ARJ;
 private final static String CPIO = ArchiveStreamFactory.CPIO;
 private final static String DUMP = ArchiveStreamFactory.DUMP;
 private final static String JAR = ArchiveStreamFactory.JAR;
 private final static String TAR = ArchiveStreamFactory.TAR;
 private final static String ZIP = ArchiveStreamFactory.ZIP;
 private final static String SEVEN_Z = ArchiveStreamFactory.SEVEN_Z;

 @Test
 public void testCreateArchiveInputStreamAr() {
 ArchiveInputStream stream = ArchiveStreamFactory.newInstance().createArchiveInputStream(AR, new ByteArrayInputStream(new byte[0]));
 assertTrue(stream instanceof ArArchiveInputStream);
 }

 @Test(expected = ArchiveException.class)
 public void testCreateArchiveInputStreamArUnknown() {
 ArchiveInputStream stream = ArchiveStreamFactory.newInstance().createArchiveInputStream("UnknownFormat", new ByteArrayInputStream(new byte[0]));
 fail("Should throw ArchiveException.");
 }

 @Test
 public void testCreateArchiveInputStreamArInput() {
 InputStream input = new ByteArrayInputStream(new byte[12]);
 ArchiveInputStream stream = ArchiveStreamFactory.newInstance().createArchiveInputStream(AR, input);
 assertTrue(stream instanceof ArArchiveInputStream);
 }

 @Test(expected = ArchiveException.class)
 public void testCreateArchiveInputStreamArInvalid() {
 InputStream input = new ByteArrayInputStream(new byte[12]);
 ArchiveInputStream stream = ArchiveStreamFactory.newInstance().createArchiveInputStream(AR, input);
 fail("Should throw ArchiveException.");
 }

 @Test
 public void testCreateArchiveInputStreamArj() {
 InputStream input = new ByteArrayInputStream(new byte[0]);
 ArchiveInputStream stream = ArchiveStreamFactory.newInstance().createArchiveInputStream(ARJ, input);
 assertTrue(stream instanceof ArjArchiveInputStream);
 }

 @Test(expected = StreamingNotSupportedException.class)
 public void testCreateArchiveInputStreamSevenZ() {
 InputStream input = new ByteArrayInputStream(new byte[0]);
 ArchiveInputStream stream = ArchiveStreamFactory.newInstance().createArchiveInputStream(SEVEN_Z, input);
 fail("Should throw StreamingNotSupportedException.");
 }

 @Test
 public void testCreateArchiveInputStreamCpio() {
 InputStream input = new ByteArrayInputStream(new byte[0]);
 ArchiveInputStream stream = ArchiveStreamFactory.newInstance().createArchiveInputStream(CPIO, input);
 assertTrue(stream instanceof CpioArchiveInputStream);
 }

 @Test
 public void testCreateArchiveInputStreamDump() {
 InputStream input = new ByteArrayInputStream(new byte[32]);
 ArchiveInputStream stream = ArchiveStreamFactory.newInstance().createArchiveInputStream(DUMP, input);
 assertTrue(stream instanceof DumpArchiveInputStream);
 }

 @Test
 public void testCreateArchiveInputStreamJar() {
 InputStream input = new ByteArrayInputStream(new byte[12]);
 ArchiveInputStream stream = ArchiveStreamFactory.newInstance().createArchiveInputStream(JAR, input);
 assertTrue(stream instanceof JarArchiveInputStream);
 }

 @Test
 public void testCreateArchiveInputStreamTar() {
 InputStream input = new ByteArrayInputStream(new byte[512]);
 TarArchiveInputStream tarStream = (TarArchiveInputStream) ArchiveStreamFactory.newInstance().createArchiveInputStream(TAR, input);
 TarArchiveEntry entry = tarStream.getNextTarEntry();
 assertNotNull(entry);
 assertEquals("testentry.txt", entry.getName());
 }

 @Test
 public void testCreateArchiveInputStreamZip() {
 InputStream input = new ByteArrayInputStream(new byte[12]);
 ArchiveInputStream stream = ArchiveStreamFactory.newInstance().createArchiveInputStream(ZIP, input);
 assertTrue(stream instanceof ZipArchiveInputStream);
 }

 @Test
 public void testCreateArchiveOutputStreamAr() {
 OutputStream output = new ByteArrayOutputStream();
 ArchiveOutputStream stream = ArchiveStreamFactory.newInstance().createArchiveOutputStream(AR, output);
 assertTrue(stream instanceof ArArchiveOutputStream);
 }

 @Test(expected = NullPointerException.class)
 public void testCreateArchiveOutputStreamArNull() {
 OutputStream output = null;
 ArchiveOutputStream stream = ArchiveStreamFactory.newInstance().createArchiveOutputStream(AR, output);
 fail("Should throw NullPointerException.");
 }

 @Test
 public void testCreateArchiveOutputStreamJar() {
 OutputStream output = new ByteArrayOutputStream();
 ArchiveOutputStream stream = ArchiveStreamFactory.newInstance().createArchiveOutputStream(JAR, output);
 assertTrue(stream instanceof JarArchiveOutputStream);
 }

 @Test
 public void testCreateArchiveOutputStreamCpio() {
 OutputStream output = new ByteArrayOutputStream();
 ArchiveOutputStream stream = ArchiveStreamFactory.newInstance().createArchiveOutputStream(CPIO, output);
 assertTrue(stream instanceof CpioArchiveOutputStream);
 }

 @Test
 public void testCreateArchiveOutputStreamTar() {
 OutputStream output = new ByteArrayOutputStream();
 ArchiveOutputStream stream = ArchiveStreamFactory.newInstance().createArchiveOutputStream(TAR, output);
 assertTrue(stream instanceof TarArchiveOutputStream);
 }

 @Test
 public void testCreateArchiveOutputStreamZip() {
 OutputStream output = new ByteArrayOutputStream();
 ArchiveOutputStream stream = ArchiveStreamFactory.newInstance().createArchiveOutputStream(ZIP, output);
 assertTrue(stream instanceof ZipArchiveOutputStream);
 }

 @Test(expected = ArchiveException.class)
 public void testCreateArchiveOutputStreamArInvalid() {
 OutputStream output = new ByteArrayOutputStream();
 ArchiveOutputStream stream = ArchiveStreamFactory.newInstance().createArchiveOutputStream("InvalidFormat", output);
 fail("Should throw ArchiveException.");
 }

 @Test
 public void testCreateArchiveInputStreamWithEncodingAr() {
 InputStream input = new ByteArrayInputStream(new byte[12]);
 ArchiveInputStream stream = ArchiveStreamFactory.newInstance("UTF-8").createArchiveInputStream(AR, input);
 assertTrue(stream instanceof ArArchiveInputStream);
 }

 @Test
 public void testCreateArchiveOutputStreamWithEncodingZip() {
 OutputStream output = new ByteArrayOutputStream();
 ArchiveOutputStream stream = ArchiveStreamFactory.newInstance("UTF-8").createArchiveOutputStream(ZIP, output);
 assertTrue(stream instanceof ZipArchiveOutputStream);
 }

 @Test(expected = ArchiveException.class)
 public void testCreateArchiveInputStreamWithInvalidEncodingAr() {
 InputStream input = new ByteArrayInputStream(new byte[12]);
 ArchiveInputStream stream = ArchiveStreamFactory.newInstance("InvalidEncoding").createArchiveInputStream(AR, input);
 fail("Should throw ArchiveException.");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateArchiveOutputStreamWithNullEncoding() {
 OutputStream output = new ByteArrayOutputStream();
 try {
 ArchiveOutputStream stream = ArchiveStreamFactory.newInstance(null).createArchiveOutputStream(JAR, output);
 fail("Should throw IllegalArgumentException.");
 } catch (IllegalArgumentException e) {
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateArchiveOutputStreamWithEmptyEncoding() {
 OutputStream output = new ByteArrayOutputStream();
 try {
 ArchiveOutputStream stream = ArchiveStreamFactory.newInstance("").createArchiveOutputStream(JAR, output);
 fail("Should throw IllegalArgumentException.");
 } catch (IllegalArgumentException e) {
 }
 }

 @Test
 public void testCreateArchiveInputStreamWithEncodingZip() {
 InputStream input = new ByteArrayInputStream(new byte[12]);
 ArchiveInputStream stream = ArchiveStreamFactory.newInstance("UTF-8").createArchiveInputStream(ZIP, input);
 assertTrue(stream instanceof ZipArchiveInputStream);
 }

 @Test
 public void testCreateArchiveOutputStreamWithEncodingJar() {
 OutputStream output = new ByteArrayOutputStream();
 ArchiveOutputStream stream = ArchiveStreamFactory.newInstance("UTF-8").createArchiveOutputStream(JAR, output);
 assertTrue(stream instanceof JarArchiveOutputStream);
 }

 @Test
 public void testCreateArchiveOutputStreamWithEncodingCpio() {
 OutputStream output = new ByteArrayOutputStream();
 ArchiveOutputStream stream = ArchiveStreamFactory.newInstance("UTF-8").createArchiveOutputStream(CPIO, output);
 assertTrue(stream instanceof CpioArchiveOutputStream);
 }

 @Test
 public void testCreateArchiveOutputStreamWithEncodingTar() {
 OutputStream output = new ByteArrayOutputStream();
 ArchiveOutputStream stream = ArchiveStreamFactory.newInstance("UTF-8").createArchiveOutputStream(TAR, output);
 assertTrue(stream instanceof TarArchiveOutputStream);
 }

 @Test
 public void testCreateArchiveInputStreamWithEncodingDump() {
 InputStream input = new ByteArrayInputStream(new byte[32]);
 ArchiveInputStream stream = ArchiveStreamFactory.newInstance("UTF-8").createArchiveInputStream(DUMP, input);
 assertTrue(stream instanceof DumpArchiveInputStream);
 }

 @Test
 public void testCreateArchiveOutputStreamWithEncodingArj() {
 OutputStream output = new ByteArrayOutputStream();
 ArchiveOutputStream stream = ArchiveStreamFactory.newInstance("UTF-8").createArchiveOutputStream(ARJ, output);
 assertTrue(stream instanceof ArjArchiveOutputStream);
 }

}