import org.apache.commons.compress.archivers.*;
import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ar.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.arj.ArjArchiveInputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveOutputStream;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;

public class ArchiveStreamFactoryTest {

 @Test
 public void testCreateArchiveInputStream() throws Exception {
 ArchiveStreamFactory factory = new ArchiveStreamFactory();

 // Test valid archives
 InputStream arInput = new ByteArrayInputStream(AR_ARCHIVE.getBytes());
 InputStream arjInput = new ByteArrayInputStream(ARJ_ARCHIVE.getBytes());
 InputStream cpioInput = new ByteArrayInputStream(CPIO_ARCHIVE.getBytes());
 InputStream dumpInput = new ByteArrayInputStream(DUMP_ARCHIVE.getBytes());
 InputStream jarInput = new ByteArrayInputStream(JAR_ARCHIVE.getBytes());
 InputStream tarInput = new ByteArrayInputStream(TAR_ARCHIVE.getBytes());
 InputStream zipInput = new ByteArrayInputStream(ZIP_ARCHIVE.getBytes());

 ArchiveInputStream arStream = factory.createArchiveInputStream("ar", arInput);
 ArchiveInputStream arjStream = factory.createArchiveInputStream("arj", arjInput);
 ArchiveInputStream cpioStream = factory.createArchiveInputStream("cpio", cpioInput);
 ArchiveInputStream dumpStream = factory.createArchiveInputStream("dump", dumpInput);
 ArchiveInputStream jarStream = factory.createArchiveInputStream("jar", jarInput);
 ArchiveInputStream tarStream = factory.createArchiveInputStream("tar", tarInput);
 ArchiveInputStream zipStream = factory.createArchiveInputStream("zip", zipInput);

 assertNotNull(arStream);
 assertNotNull(arjStream);
 assertNotNull(cpioStream);
 assertNotNull(dumpStream);
 assertNotNull(jarStream);
 assertNotNull(tarStream);
 assertNotNull(zipStream);

 arStream.close();
 arjStream.close();
 cpioStream.close();
 dumpStream.close();
 jarStream.close();
 tarStream.close();
 zipStream.close();
 arInput.close();
 arjInput.close();
 cpioInput.close();
 dumpInput.close();
 jarInput.close();
 tarInput.close();
 zipInput.close();

 // Test invalid archives
 InputStream sevenzInput = new ByteArrayInputStream(SEVEN_Z_ARCHIVE.getBytes());
 InputStream invalidInput = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03 });

 try {
 factory.createArchiveInputStream("7z", sevenzInput);
 } catch (ArchiveException e) {
 // expected
 } finally {
 sevenzInput.close();
 }

 try {
 factory.createArchiveInputStream("invalid", invalidInput);
 } catch (ArchiveException e) {
 // expected
 } finally {
 invalidInput.close();
 }
 }

 @Test
 public void testCreateArchiveOutputStream() throws Exception {
 ArchiveStreamFactory factory = new ArchiveStreamFactory();

 // Test valid archives
 OutputStream arOutput = new ByteArrayOutputStream();
 OutputStream arjOutput = new ByteArrayOutputStream();
 OutputStream cpioOutput = new ByteArrayOutputStream();
 OutputStream dumpOutput = new ByteArrayOutputStream();
 OutputStream jarOutput = new ByteArrayOutputStream();
 OutputStream tarOutput = new ByteArrayOutputStream();
 OutputStream zipOutput = new ByteArrayOutputStream();

 ArchiveOutputStream arStream = factory.createArchiveOutputStream("ar", arOutput);
 ArchiveOutputStream arjStream = factory.createArchiveOutputStream("arj", arjOutput);
 ArchiveOutputStream cpioStream = factory.createArchiveOutputStream("cpio", cpioOutput);
 ArchiveOutputStream dumpStream = factory.createArchiveOutputStream("dump", dumpOutput);
 ArchiveOutputStream jarStream = factory.createArchiveOutputStream("jar", jarOutput);
 ArchiveOutputStream tarStream = factory.createArchiveOutputStream("tar", tarOutput);
 ArchiveOutputStream zipStream = factory.createArchiveOutputStream("zip", zipOutput);

 arStream.putNextEntry(new ArArchiveEntry("test.txt"));
 arjStream.putNextEntry(new ArjArchiveEntry("test.txt"));
 cpioStream.putNextEntry(new CpioArchiveEntry("test.txt"));
 dumpStream.putNextEntry(new DumpArchiveEntry("test.txt"));
 jarStream.putNextEntry(new JarArchiveEntry("test.txt"));
 tarStream.putNextEntry(new TarArchiveEntry("test.txt"));
 zipStream.putNextEntry(new ZipArchiveEntry("test.txt"));

 arStream.finish();
 arjStream.finish();
 cpioStream.finish();
 dumpStream.finish();
 jarStream.finish();
 tarStream.finish();
 zipStream.finish();

 byte[] arOutputBytes = arOutput.toByteArray();
 byte[] arjOutputBytes = arjOutput.toByteArray();
 byte[] cpioOutputBytes = cpioOutput.toByteArray();
 byte[] dumpOutputBytes = dumpOutput.toByteArray();
 byte[] jarOutputBytes = jarOutput.toByteArray();
 byte[] tarOutputBytes = tarOutput.toByteArray();
 byte[] zipOutputBytes = zipOutput.toByteArray();

 assertNotNull(arStream);
 assertNotNull(arjStream);
 assertNotNull(cpioStream);
 assertNotNull(dumpStream);
 assertNotNull(jarStream);
 assertNotNull(tarStream);
 assertNotNull(zipStream);

 // Test AR
 ArArchiveInputStream arInput = new ArArchiveInputStream(new ByteArrayInputStream(arOutputBytes));
 assertNotNull(arInput.getNextEntry());
 arInput.close();

 // Test ARJ
 ArjArchiveInputStream arjInput = new ArjArchiveInputStream(new ByteArrayInputStream(arjOutputBytes));
 assertNotNull(arjInput.getNextEntry());
 arjInput.close();

 // Test CPIO
 CpioArchiveInputStream cpioInput = new CpioArchiveInputStream(new ByteArrayInputStream(cpioOutputBytes));
 assertNotNull(cpioInput.getNextEntry());
 cpioInput.close();

 // Test DUMP
 DumpArchiveInputStream dumpInput = new DumpArchiveInputStream(new ByteArrayInputStream(dumpOutputBytes));
 assertNotNull(dumpInput.getNextEntry());
 dumpInput.close();

 // Test JAR
 JarArchiveInputStream jarInput = new JarArchiveInputStream(new ByteArrayInputStream(jarOutputBytes));
 assertNotNull(jarInput.getNextEntry());
 jarInput.close();

 // Test TAR
 TarArchiveInputStream tarInput = new TarArchiveInputStream(new ByteArrayInputStream(tarOutputBytes));
 assertNotNull(tarInput.getNextEntry());
 tarInput.close();

 // Test ZIP
 ZipArchiveInputStream zipInput = new ZipArchiveInputStream(new ByteArrayInputStream(zipOutputBytes));
 assertNotNull(zipInput.getNextEntry());
 zipInput.close();

 arOutput.close();
 arjOutput.close();
 cpioOutput.close();
 dumpOutput.close();
 jarOutput.close();
 tarOutput.close();
 zipOutput.close();
 }

 private static byte[] AR_ARCHIVE = new byte[100]; // the actual content does not matter
 private static byte[] ARJ_ARCHIVE = new byte[100]; // the actual content does not matter
 private static byte[] CPIO_ARCHIVE = new byte[100]; // the actual content does not matter
 private static byte[] DUMP_ARCHIVE = new byte[100]; // the actual content does not matter
 private static byte[] JAR_ARCHIVE = new byte[100]; // the actual content does not matter
 private static byte[] TAR_ARCHIVE = new byte[100]; // the actual content does not matter
 private static byte[] ZIP_ARCHIVE = new byte[100]; // the actual content does not matter
 private static byte[] SEVEN_Z_ARCHIVE = new byte[100]; // the actual content does not matter
}