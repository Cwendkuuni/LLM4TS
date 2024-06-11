import org.apache.commons.compress.archivers.*;
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
 final byte[] arSignature = { '!', 'f', 'u', 'n', 'c', '.\n', 'A', 'R', ' ', '1', '.', '0' };
 final byte[] zipSignature = { 0x50, 0x4b, 0x05, 0x06, 0x00, 0x02, 0x1c, 0x01, 0x0b, 0x05, 0x00, 0x04, 0x14, 0x00, 0x00, 0x00, 0x08, 0x6b, 0x75, 0x72, 0x6c, 0x65, 0x61, 0x74, 0x65 };
 final byte[] cpioSignature = { '070701' }; // CPIO signature is just 4 bytes long
 final byte[] tarSignature = { 0x00, 0x00, 0x14, 0x00, 'T', 'A', 'R', ' ', 'h', 'd', 'r', 'w', '.', 't', 'a', 'r' };
 final byte[] dumpSignature = { 0x07, 0x00, 0x01, 0x4d, 0x4d, 0x00, 0x02, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x37, 0x62, 0x69, 0x61, 0x74, 0x79, 0x00 };

 // Test AR Archive
 InputStream arInputStream = new ByteArrayInputStream(arSignature);
 ArchiveInputStream arInputStreamExpected = new ArArchiveInputStream(arInputStream);
 ArchiveInputStream arInputStreamActual = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.AR, arInputStream);
 assertEquals(arInputStreamExpected.getClass(), arInputStreamActual.getClass());
 arInputStream.close();

 // Test ZIP Archive
 InputStream zipInputStream = new ByteArrayInputStream(zipSignature);
 ArchiveInputStream zipInputStreamExpected = new ZipArchiveInputStream(zipInputStream);
 ArchiveInputStream zipInputStreamActual = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.ZIP, zipInputStream);
 assertEquals(zipInputStreamExpected.getClass(), zipInputStreamActual.getClass());
 zipInputStream.close();

 // Test CPIO Archive
 InputStream cpioInputStream = new ByteArrayInputStream(cpioSignature);
 ArchiveInputStream cpioInputStreamExpected = new CpioArchiveInputStream(cpioInputStream);
 ArchiveInputStream cpioInputStreamActual = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.CPIO, cpioInputStream);
 assertEquals(cpioInputStreamExpected.getClass(), cpioInputStreamActual.getClass());
 cpioInputStream.close();

 // Test TAR Archive
 InputStream tarInputStream = new ByteArrayInputStream(tarSignature);
 ArchiveInputStream tarInputStreamExpected = new TarArchiveInputStream(tarInputStream);
 ArchiveInputStream tarInputStreamActual = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.TAR, tarInputStream);
 assertEquals(tarInputStreamExpected.getClass(), tarInputStreamActual.getClass());
 tarInputStream.close();

 // Test Dump Archive
 InputStream dumpInputStream = new ByteArrayInputStream(dumpSignature);
 ArchiveInputStream dumpInputStreamExpected = new DumpArchiveInputStream(dumpInputStream);
 ArchiveInputStream dumpInputStreamActual = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.DUMP, dumpInputStream);
 assertEquals(dumpInputStreamExpected.getClass(), dumpInputStreamActual.getClass());
 dumpInputStream.close();
 }

 @Test
 public void testCreateArchiveOutputStream() throws Exception {
 OutputStream outputStream = new ByteArrayOutputStream();

 // Test AR Archive
 ArchiveOutputStream arOutputStreamExpected = new ArArchiveOutputStream(outputStream);
 ArchiveOutputStream arOutputStreamActual = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.AR, outputStream);
 assertEquals(arOutputStreamExpected.getClass(), arOutputStreamActual.getClass());
 arOutputStreamActual.close();
 outputStream.close();

 // Test ZIP Archive
 ArchiveOutputStream zipOutputStreamExpected = new ZipArchiveOutputStream(outputStream);
 ArchiveOutputStream zipOutputStreamActual = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.ZIP, outputStream);
 assertEquals(zipOutputStreamExpected.getClass(), zipOutputStreamActual.getClass());
 zipOutputStreamActual.close();
 outputStream.close();

 // Test TAR Archive
 ArchiveOutputStream tarOutputStreamExpected = new TarArchiveOutputStream(outputStream);
 ArchiveOutputStream tarOutputStreamActual = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.TAR, outputStream);
 assertEquals(tarOutputStreamExpected.getClass(), tarOutputStreamActual.getClass());
 tarOutputStreamActual.close();
 outputStream.close();

 // Test CPIO Archive
 ArchiveOutputStream cpioOutputStreamExpected = new CpioArchiveOutputStream(outputStream);
 ArchiveOutputStream cpioOutputStreamActual = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.CPIO, outputStream);
 assertEquals(cpioOutputStreamExpected.getClass(), cpioOutputStreamActual.getClass());
 cpioOutputStreamActual.close();
 outputStream.close();

 // Test JAR Archive
 ArchiveOutputStream jarOutputStreamExpected = new JarArchiveOutputStream(outputStream);
 ArchiveOutputStream jarOutputStreamActual = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.JAR, outputStream);
 assertEquals(jarOutputStreamExpected.getClass(), jarOutputStreamActual.getClass());
 jarOutputStreamActual.close();
 outputStream.close();
 }

 @Test
 public void testCreateArchiveInputStreamWithUnknownSignature() throws Exception {
 InputStream unknownInputStream = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F });
 try {
 ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.AR, unknownInputStream);
 fail("Expected ArchiveException");
 } catch (ArchiveException e) {
 // Expected
 }
 unknownInputStream.close();
 }
}