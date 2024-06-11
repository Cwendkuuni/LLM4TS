import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
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
 ArchiveStreamFactory factory = new ArchiveStreamFactory();

 // Test with known archive types
 InputStream arInput = new ByteArrayInputStream(new byte[10]);
 ArchiveInputStream arResult = factory.createArchiveInputStream("ar", arInput);
 assertInstanceOf(ArArchiveInputStream.class, arResult);
 arResult.close();

 InputStream tarInput = new ByteArrayInputStream(new byte[512]);
 ArchiveInputStream tarResult = factory.createArchiveInputStream("tar", tarInput);
 assertInstanceOf(TarArchiveInputStream.class, tarResult);
 tarResult.close();

 InputStream zipInput = new ByteArrayInputStream(new byte[22]);
 ArchiveInputStream zipResult = factory.createArchiveInputStream("zip", zipInput);
 assertInstanceOf(ZipArchiveInputStream.class, zipResult);
 zipResult.close();

 InputStream jarInput = new ByteArrayInputStream(new byte[12]);
 ArchiveInputStream jarResult = factory.createArchiveInputStream("jar", jarInput);
 assertInstanceOf(JarArchiveInputStream.class, jarResult);
 jarResult.close();

 InputStream cpioInput = new ByteArrayInputStream(new byte[16]);
 ArchiveInputStream cpioResult = factory.createArchiveInputStream("cpio", cpioInput);
 assertInstanceOf(CpioArchiveInputStream.class, cpioResult);
 cpioResult.close();

 InputStream dumpInput = new ByteArrayInputStream(new byte[32]);
 ArchiveInputStream dumpResult = factory.createArchiveInputStream("dump", dumpInput);
 assertInstanceOf(DumpArchiveInputStream.class, dumpResult);
 dumpResult.close();

 // Test with unknown archive type
 InputStream unknownInput = new ByteArrayInputStream(new byte[1]);
 ArchiveInputStream unknownResult = factory.createArchiveInputStream("unknown", unknownInput);
 fail("Expected ArchiveException but got " + unknownResult.getClass().getName());
 unknownResult.close();
 }

 @Test
 public void testCreateArchiveOutputStream() throws Exception {
 ArchiveStreamFactory factory = new ArchiveStreamFactory();

 // Test with known archive types
 OutputStream arOutput = new ByteArrayOutputStream();
 ArchiveOutputStream arResult = factory.createArchiveOutputStream("ar", arOutput);
 arResult.close();
 assertNull(arOutput.toString());

 OutputStream tarOutput = new ByteArrayOutputStream();
 ArchiveOutputStream tarResult = factory.createArchiveOutputStream("tar", tarOutput);
 tarResult.close();
 assertNull(tarOutput.toString());

 OutputStream zipOutput = new ByteArrayOutputStream();
 ArchiveOutputStream zipResult = factory.createArchiveOutputStream("zip", zipOutput);
 zipResult.close();
 assertNull(zipOutput.toString());

 OutputStream jarOutput = new ByteArrayOutputStream();
 ArchiveOutputStream jarResult = factory.createArchiveOutputStream("jar", jarOutput);
 jarResult.close();
 assertNull(jarOutput.toString());

 OutputStream cpioOutput = new ByteArrayOutputStream();
 ArchiveOutputStream cpioResult = factory.createArchiveOutputStream("cpio", cpioOutput);
 cpioResult.close();
 assertNull(cpioOutput.toString());

 // Test with unknown archive type
 OutputStream unknownOutput = new ByteArrayOutputStream();
 ArchiveOutputStream unknownResult = factory.createArchiveOutputStream("unknown", unknownOutput);
 fail("Expected ArchiveException but got " + unknownResult.getClass().getName());
 unknownResult.close();
 }

 @Test
 public void testCreateArchiveInputStreamFromInputStream() throws Exception {
 ArchiveStreamFactory factory = new ArchiveStreamFactory();

 // Test with known archive types
 InputStream arInput = new ByteArrayInputStream(new byte[10]);
 ArchiveInputStream arResult = factory.createArchiveInputStream(new String[]{"ar"}, arInput);
 assertInstanceOf(ArArchiveInputStream.class, arResult);
 arResult.close();

 InputStream tarInput = new ByteArrayInputStream(new byte[512]);
 ArchiveInputStream tarResult = factory.createArchiveInputStream(new String[]{"tar"}, tarInput);
 assertInstanceOf(TarArchiveInputStream.class, tarResult);
 tarResult.close();

 InputStream zipInput = new ByteArrayInputStream(new byte[22]);
 ArchiveInputStream zipResult = factory.createArchiveInputStream(new String[]{"zip"}, zipInput);
 assertInstanceOf(ZipArchiveInputStream.class, zipResult);
 zipResult.close();

 InputStream jarInput = new ByteArrayInputStream(new byte[12]);
 ArchiveInputStream jarResult = factory.createArchiveInputStream(new String[]{"jar"}, jarInput);
 assertInstanceOf(JarArchiveInputStream.class, jarResult);
 jarResult.close();

 InputStream cpioInput = new ByteArrayInputStream(new byte[16]);
 ArchiveInputStream cpioResult = factory.createArchiveInputStream(new String[]{"cpio"}, cpioInput);
 assertInstanceOf(CpioArchiveInputStream.class, cpioResult);
 cpioResult.close();

 InputStream dumpInput = new ByteArrayInputStream(new byte[32]);
 ArchiveInputStream dumpResult = factory.createArchiveInputStream(new String[]{"dump"}, dumpInput);
 assertInstanceOf(DumpArchiveInputStream.class, dumpResult);
 dumpResult.close();

 // Test with unknown archive type
 InputStream unknownInput = new ByteArrayInputStream(new byte[1]);
 ArchiveInputStream unknownResult = factory.createArchiveInputStream(new String[]{"unknown"}, unknownInput);
 fail("Expected ArchiveException but got " + unknownResult.getClass().getName());
 unknownResult.close();
 }

 @Test
 public void testCreateArchiveOutputStreamFromOutputStream() throws Exception {
 ArchiveStreamFactory factory = new ArchiveStreamFactory();

 // Test with known archive types
 OutputStream arOutput = new ByteArrayOutputStream();
 ArchiveOutputStream arResult = factory.createArchiveOutputStream(new String[]{"ar"}, arOutput);
 arResult.finish();
 arResult.close();
 assertNotNull(arOutput.toString());

 OutputStream tarOutput = new ByteArrayOutputStream();
 ArchiveOutputStream tarResult = factory.createArchiveOutputStream(new String[]{"tar"}, tarOutput);
 tarResult.finish();
 tarResult.close();
 assertNotNull(tarOutput.toString());

 OutputStream zipOutput = new ByteArrayOutputStream();
 ArchiveOutputStream zipResult = factory.createArchiveOutputStream(new String[]{"zip"}, zipOutput);
 zipResult.finish();
 zipResult.close();
 assertNotNull(zipOutput.toString());

 OutputStream jarOutput = new ByteArrayOutputStream();
 ArchiveOutputStream jarResult = factory.createArchiveOutputStream(new String[]{"jar"}, jarOutput);
 jarResult.finish();
 jarResult.close();
 assertNotNull(jarOutput.toString());

 OutputStream cpioOutput = new ByteArrayOutputStream();
 ArchiveOutputStream cpioResult = factory.createArchiveOutputStream(new String[]{"cpio"}, cpioOutput);
 cpioResult.finish();
 cpioResult.close();
 assertNotNull(cpioOutput.toString());

 // Test with unknown archive type
 OutputStream unknownOutput = new ByteArrayOutputStream();
 ArchiveOutputStream unknownResult = factory.createArchiveOutputStream(new String[]{"unknown"}, unknownOutput);
 fail("Expected ArchiveException but got " + unknownResult.getClass().getName());
 unknownResult.close();
 }
}