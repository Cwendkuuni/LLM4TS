import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.JarArchiveInputStream;
import org.apache.commons.compress.archivers.JarArchiveOutputStream;
import org.apache.commons.compress.archivers.TarArchiveInputStream;
import org.apache.commons.compress.archivers.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;

public class ArchiveStreamFactoryTest {

 @Test
 public void testCreateArchiveInputStream() throws Exception {
 ArchiveStreamFactory factory = new ArchiveStreamFactory();

 // Test with valid archiver names
 InputStream arInputStream = new ByteArrayInputStream(new byte[1024]);
 ArchiveInputStream arArchiveInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.AR, arInputStream);
 assertNotNull(arArchiveInputStream);
 arArchiveInputStream.close();

 InputStream zipInputStream = new ByteArrayInputStream(new byte[1024]);
 ArchiveInputStream zipArchiveInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, zipInputStream);
 assertNotNull(zipArchiveInputStream);
 zipArchiveInputStream.close();

 InputStream jarInputStream = new ByteArrayInputStream(new byte[1024]);
 ArchiveInputStream jarArchiveInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.JAR, jarInputStream);
 assertNotNull(jarArchiveInputStream);
 jarArchiveInputStream.close();

 InputStream tarInputStream = new ByteArrayInputStream(new byte[1024]);
 ArchiveInputStream tarArchiveInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.TAR, tarInputStream);
 assertNotNull(tarArchiveInputStream);
 tarArchiveInputStream.close();

 InputStream cpioInputStream = new ByteArrayInputStream(new byte[1024]);
 ArchiveInputStream cpioArchiveInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.CPIO, cpioInputStream);
 assertNotNull(cpioArchiveInputStream);
 cpioArchiveInputStream.close();

 InputStream dumpInputStream = new ByteArrayInputStream(new byte[1024]);
 ArchiveInputStream dumpArchiveInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.DUMP, dumpInputStream);
 assertNotNull(dumpArchiveInputStream);
 dumpArchiveInputStream.close();

 // Test with invalid archiver name
 InputStream invalidArchiverInputStream = new ByteArrayInputStream(new byte[1024]);
 try {
 factory.createArchiveInputStream("invalid", invalidArchiveInputStream);
 } catch (ArchiveException e) {
 assertNotNull(e.getMessage());
 }
 }

 @Test
 public void testCreateArchiveOutputStream() throws Exception {
 ArchiveStreamFactory factory = new ArchiveStreamFactory();

 // Test with valid archiver names
 OutputStream outputStream = new ByteArrayOutputStream();
 ArchiveOutputStream arArchiveOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.AR, outputStream);
 arArchiveOutputStream.finish();
 assertNotNull(arArchiveOutputStream);
 arArchiveOutputStream.close();

 OutputStream zipOutputStream = new ByteArrayOutputStream();
 ArchiveOutputStream zipArchiveOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, zipOutputStream);
 zipArchiveOutputStream.finish();
 assertNotNull(zipArchiveOutputStream);
 zipArchiveOutputStream.close();

 OutputStream jarOutputStream = new ByteArrayOutputStream();
 ArchiveOutputStream jarArchiveOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.JAR, jarOutputStream);
 jarArchiveOutputStream.finish();
 assertNotNull(jarArchiveOutputStream);
 jarArchiveOutputStream.close();

 OutputStream tarOutputStream = new ByteArrayOutputStream();
 ArchiveOutputStream tarArchiveOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.TAR, tarOutputStream);
 tarArchiveOutputStream.finish();
 assertNotNull(tarArchiveOutputStream);
 tarArchiveOutputStream.close();

 OutputStream cpioOutputStream = new ByteArrayOutputStream();
 ArchiveOutputStream cpioArchiveOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.CPIO, cpioOutputStream);
 cpioArchiveOutputStream.finish();
 assertNotNull(cpioArchiveOutputStream);
 cpioArchiveOutputStream.close();

 OutputStream dumpOutputStream = new ByteArrayOutputStream();
 ArchiveOutputStream dumpArchiveOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.DUMP, dumpOutputStream);
 dumpArchiveOutputStream.finish();
 assertNotNull(dumpArchiveOutputStream);
 dumpArchiveOutputStream.close();

 // Test with invalid archiver name
 OutputStream invalidArchiverOutputStream = new ByteArrayOutputStream();
 try {
 factory.createArchiveOutputStream("invalid", invalidArchiverOutputStream);
 } catch (ArchiveException e) {
 assertNotNull(e.getMessage());
 }
 }

 @Test
 public void testCreateArchiveInputStreamFromSignature() throws Exception {
 ArchiveStreamFactory factory = new ArchiveStreamFactory();

 byte[] arSignature = new byte[12];
 byte[] zipSignature = new byte[12];
 byte[] jarSignature = new byte[12];
 byte[] tarSignature = new byte[512];
 byte[] cpioSignature = new byte[32];
 byte[] dumpSignature = new byte[32];

 // AR signature
 System.arraycopy(ArArchiveInputStream.MAGIC, 0, arSignature, 0, ArArchiveInputStream.MAGIC.length);
 InputStream arInputStream = new ByteArrayInputStream(arSignature);
 ArchiveInputStream arArchiveInputStream = factory.createArchiveInputStream(arInputStream);
 assertNotNull(arArchiveInputStream);
 arArchiveInputStream.close();

 // ZIP signature
 System.arraycopy(ZipArchiveInputStream.SIGNATURE, 0, zipSignature, 0, ZipArchiveInputStream.SIGNATURE.length);
 InputStream zipInputStream = new ByteArrayInputStream(zipSignature);
 ArchiveInputStream zipArchiveInputStream = factory.createArchiveInputStream(zipInputStream);
 assertNotNull(zipArchiveInputStream);
 zipArchiveInputStream.close();

 // JAR signature
 System.arraycopy(JarArchiveInputStream.SIGNATURE, 0, jarSignature, 0, JarArchiveInputStream.SIGNATURE.length);
 InputStream jarInputStream = new ByteArrayInputStream(jarSignature);
 ArchiveInputStream jarArchiveInputStream = factory.createArchiveInputStream(jarInputStream);
 assertNotNull(jarArchiveInputStream);
 jarArchiveInputStream.close();

 // TAR signature
 System.arraycopy(TarArchiveInputStream.MAGIC, 0, tarSignature, 0, TarArchiveInputStream.MAGIC.length);
 InputStream tarInputStream = new ByteArrayInputStream(tarSignature);
 ArchiveInputStream tarArchiveInputStream = factory.createArchiveInputStream(tarInputStream);
 assertNotNull(tarArchiveInputStream);
 tarArchiveInputStream.close();

 // CPIO signature
 System.arraycopy(CpioArchiveInputStream.SIG, 0, cpioSignature, 0, CpioArchiveInputStream.SIG.length);
 InputStream cpioInputStream = new ByteArrayInputStream(cpioSignature);
 ArchiveInputStream cpioArchiveInputStream = factory.createArchiveInputStream(cpioInputStream);
 assertNotNull(cpioArchiveInputStream);
 cpioArchiveInputStream.close();

 // Dump signature
 System.arraycopy(DumpArchiveInputStream.SIG, 0, dumpSignature, 0, DumpArchiveInputStream.SIG.length);
 InputStream dumpInputStream = new ByteArrayInputStream(dumpSignature);
 ArchiveInputStream dumpArchiveInputStream = factory.createArchiveInputStream(dumpInputStream);
 assertNotNull(dumpArchiveInputStream);
 dumpArchiveInputStream.close();
 }
}