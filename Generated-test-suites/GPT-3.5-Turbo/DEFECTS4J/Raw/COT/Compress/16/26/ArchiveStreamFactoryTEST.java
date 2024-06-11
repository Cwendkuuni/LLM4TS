
import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.compress.archivers.ArchiveEntry;
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

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() {
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();

            InputStream in = new FileInputStream("test.tar");
            ArchiveInputStream tarInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.TAR, in);
            assertNotNull(tarInputStream);
            tarInputStream.close();

            in = new FileInputStream("test.zip");
            ArchiveInputStream zipInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, in);
            assertNotNull(zipInputStream);
            zipInputStream.close();

            in = new FileInputStream("test.ar");
            ArchiveInputStream arInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.AR, in);
            assertNotNull(arInputStream);
            arInputStream.close();

            in = new FileInputStream("test.cpio");
            ArchiveInputStream cpioInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.CPIO, in);
            assertNotNull(cpioInputStream);
            cpioInputStream.close();

            in = new FileInputStream("test.dump");
            ArchiveInputStream dumpInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.DUMP, in);
            assertNotNull(dumpInputStream);
            dumpInputStream.close();

            in = new FileInputStream("test.jar");
            ArchiveInputStream jarInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.JAR, in);
            assertNotNull(jarInputStream);
            jarInputStream.close();

        } catch (ArchiveException e) {
            fail("Exception should not be thrown");
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testCreateArchiveOutputStream() {
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            
            OutputStream out = new FileOutputStream("test.tar");
            ArchiveOutputStream tarOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.TAR, out);
            assertNotNull(tarOutputStream);
            tarOutputStream.close();

            out = new FileOutputStream("test.zip");
            ArchiveOutputStream zipOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, out);
            assertNotNull(zipOutputStream);
            zipOutputStream.close();

            out = new FileOutputStream("test.ar");
            ArchiveOutputStream arOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.AR, out);
            assertNotNull(arOutputStream);
            arOutputStream.close();

            out = new FileOutputStream("test.cpio");
            ArchiveOutputStream cpioOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.CPIO, out);
            assertNotNull(cpioOutputStream);
            cpioOutputStream.close();

            out = new FileOutputStream("test.dump");
            ArchiveOutputStream dumpOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.DUMP, out);
            assertNotNull(dumpOutputStream);
            dumpOutputStream.close();

            out = new FileOutputStream("test.jar");
            ArchiveOutputStream jarOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.JAR, out);
            assertNotNull(jarOutputStream);
            jarOutputStream.close();
            
        } catch (ArchiveException e) {
            fail("Exception should not be thrown");
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }
    
    @Test
    public void testCreateArchiveInputStream_autoDetection() {
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
                        
            InputStream in = new FileInputStream("test.auto");
            
            ArchiveInputStream inputStream = factory.createArchiveInputStream(in);
            assertNotNull(inputStream);
            inputStream.close();
            
        } catch (ArchiveException e) {
            fail("Exception should not be thrown");
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_nullArchiverName() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new FileInputStream("test.tar");
        factory.createArchiveInputStream(null, in);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_nullInputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        factory.createArchiveInputStream(ArchiveStreamFactory.TAR, null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStream_nullArchiverName() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream out = new FileOutputStream("test.tar");
        factory.createArchiveOutputStream(null, out);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStream_nullOutputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        factory.createArchiveOutputStream(ArchiveStreamFactory.TAR, null);
    }
    
    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_unknownArchiverName() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new FileInputStream("test.unknown");
        factory.createArchiveInputStream("unknown", in);
    }
    
    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStream_unknownArchiverName() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream out = new FileOutputStream("test.unknown");
        factory.createArchiveOutputStream("unknown", out);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_nullInputStream_autoDetection() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        factory.createArchiveInputStream(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_noMarkSupported() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream(new byte[0]);
        factory.createArchiveInputStream(in);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_noArchiverFound() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new FileInputStream("test.invalid");
        factory.createArchiveInputStream(in);
    }

}
