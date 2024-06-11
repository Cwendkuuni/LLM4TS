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
        // Testing AR format
        InputStream arInputStream = new ByteArrayInputStream(new byte[10]);
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.AR, arInputStream);
        assertTrue(archiveInputStream instanceof ArArchiveInputStream);
        archiveInputStream.close();

        // Testing Cpio format
        InputStream cpioInputStream = new ByteArrayInputStream(new byte[10]);
        archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.CPIO, cpioInputStream);
        assertTrue(archiveInputStream instanceof CpioArchiveInputStream);
        archiveInputStream.close();

        // Testing Dump format
        InputStream dumpInputStream = new ByteArrayInputStream(new byte[10]);
        archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.DUMP, dumpInputStream);
        assertTrue(archiveInputStream instanceof DumpArchiveInputStream);
        archiveInputStream.close();

        // Testing JAR format
        InputStream jarInputStream = new ByteArrayInputStream(new byte[10]);
        archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.JAR, jarInputStream);
        assertTrue(archiveInputStream instanceof JarArchiveInputStream);
        archiveInputStream.close();

        // Testing Tar format
        InputStream tarInputStream = new ByteArrayInputStream(new byte[10]);
        archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.TAR, tarInputStream);
        assertTrue(archiveInputStream instanceof TarArchiveInputStream);
        archiveInputStream.close();

        // Testing ZIP format
        InputStream zipInputStream = new ByteArrayInputStream(new byte[10]);
        archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, zipInputStream);
        assertTrue(archiveInputStream instanceof ZipArchiveInputStream);
        archiveInputStream.close();

        // Testing unknown format
        InputStream unknownInputStream = new ByteArrayInputStream(new byte[10]);
        try {
            archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream("unknown", unknownInputStream);
            fail();
        } catch (ArchiveException e) {
            // expected
        } finally {
            unknownInputStream.close();
        }
    }

    @Test
    public void testCreateArchiveOutputStream() throws Exception {
        // Testing AR format
        OutputStream arOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.AR, arOutputStream);
        assertTrue(archiveOutputStream instanceof ArArchiveOutputStream);
        archiveOutputStream.close();

        // Testing Cpio format
        OutputStream cpioOutputStream = new ByteArrayOutputStream();
        archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.CPIO, cpioOutputStream);
        assertTrue(archiveOutputStream instanceof CpioArchiveOutputStream);
        archiveOutputStream.close();

        // Testing Dump format
        OutputStream dumpOutputStream = new ByteArrayOutputStream();
        archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.DUMP, dumpOutputStream);
        assertTrue(archiveOutputStream instanceof DumpArchiveOutputStream);
        archiveOutputStream.close();

        // Testing JAR format
        OutputStream jarOutputStream = new ByteArrayOutputStream();
        archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.JAR, jarOutputStream);
        assertTrue(archiveOutputStream instanceof JarArchiveOutputStream);
        archiveOutputStream.close();

        // Testing Tar format
        OutputStream tarOutputStream = new ByteArrayOutputStream();
        archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.TAR, tarOutputStream);
        assertTrue(archiveOutputStream instanceof TarArchiveOutputStream);
        archiveOutputStream.close();

        // Testing ZIP format
        OutputStream zipOutputStream = new ByteArrayOutputStream();
        archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP, zipOutputStream);
        assertTrue(archiveOutputStream instanceof ZipArchiveOutputStream);
        archiveOutputStream.close();

        // Testing unknown format
        OutputStream unknownOutputStream = new ByteArrayOutputStream();
        try {
            archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream("unknown", unknownOutputStream);
            fail();
        } catch (ArchiveException e) {
            // expected
        } finally {
            unknownOutputStream.close();
        }
    }

    @Test
    public void testCreateArchiveInputStreamFromStream() throws Exception {
        // Testing AR format
        InputStream arInputStream = new ByteArrayInputStream(new byte[10]);
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(arInputStream);
        assertTrue(archiveInputStream instanceof ArArchiveInputStream);
        archiveInputStream.close();

        // Testing Cpio format
        InputStream cpioInputStream = new ByteArrayInputStream(new byte[10]);
        archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(cpioInputStream);
        assertTrue(archiveInputStream instanceof CpioArchiveInputStream);
        archiveInputStream.close();

        // Testing Dump format
        InputStream dumpInputStream = new ByteArrayInputStream(new byte[10]);
        archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(dumpInputStream);
        assertTrue(archiveInputStream instanceof DumpArchiveInputStream);
        archiveInputStream.close();

        // Testing JAR format
        InputStream jarInputStream = new ByteArrayInputStream(new byte[10]);
        archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(jarInputStream);
        assertTrue(archiveInputStream instanceof JarArchiveInputStream);
        archiveInputStream.close();

        // Testing Tar format
        InputStream tarInputStream = new ByteArrayInputStream(new byte[10]);
        archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(tarInputStream);
        assertTrue(archiveInputStream instanceof TarArchiveInputStream);
        archiveInputStream.close();

        // Testing ZIP format
        InputStream zipInputStream = new ByteArrayInputStream(new byte[10]);
        archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(zipInputStream);
        assertTrue(archiveInputStream instanceof ZipArchiveInputStream);
        archiveInputStream.close();

        // Testing empty stream
        InputStream emptyInputStream = new ByteArrayInputStream(new byte[0]);
        archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(emptyInputStream);
        assertTrue(archiveInputStream instanceof ArchiveInputStream);
        archiveInputStream.close();

        // Testing null stream
        InputStream nullInputStream = null;
        try {
            archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(nullInputStream);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        } finally {
            // nothing to close
        }
    }

    @Test
    public void testCreateArchiveOutputStreamFromStream() throws Exception {
        // Testing AR format
        OutputStream arOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.AR, arOutputStream);
        archiveOutputStream.putNextEntry(new org.apache.commons.compress.archivers.ar.ArArchiveEntry("test.txt"));
        archiveOutputStream.write(new byte[10]);
        archiveOutputStream.finish();
        archiveOutputStream.close();
        assertTrue(arOutputStream.size() > 0);

        // Testing Cpio format
        OutputStream cpioOutputStream = new ByteArrayOutputStream();
        archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.CPIO, cpioOutputStream);
        archiveOutputStream.putNextEntry(new org.apache.commons.compress.archivers.cpio.CpioArchiveEntry("test.txt"));
        archiveOutputStream.write(new byte[10]);
        archiveOutputStream.finish();
        archiveOutputStream.close();
        assertTrue(cpioOutputStream.size() > 0);

        // Testing Dump format
        OutputStream dumpOutputStream = new ByteArrayOutputStream();
        archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.DUMP, dumpOutputStream);
        archiveOutputStream.putNextEntry(new org.apache.commons.compress.archivers.dump.DumpArchiveEntry("test.txt"));
        archiveOutputStream.write(new byte[10]);
        archiveOutputStream.finish();
        archiveOutputStream.close();
        assertTrue(dumpOutputStream.size() > 0);

        // Testing JAR format
        OutputStream jarOutputStream = new ByteArrayOutputStream();
        archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.JAR, jarOutputStream);
        archiveOutputStream.putNextEntry(new org.apache.commons.compress.archivers.jar.JarArchiveEntry("test.txt"));
        archiveOutputStream.write(new byte[10]);
        archiveOutputStream.finish();
        archiveOutputStream.close();
        assertTrue(jarOutputStream.size() > 0);

        // Testing Tar format
        OutputStream tarOutputStream = new ByteArrayOutputStream();
        archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.TAR, tarOutputStream);
        archiveOutputStream.putNextEntry(new org.apache.commons.compress.archivers.tar.TarArchiveEntry("test.txt"));
        archiveOutputStream.write(new byte[10]);
        archiveOutputStream.finish();
        archiveOutputStream.close();
        assertTrue(tarOutputStream.size() > 0);

        // Testing ZIP format
        OutputStream zipOutputStream = new ByteArrayOutputStream();
        archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP, zipOutputStream);
        archiveOutputStream.putNextEntry(new org.apache.commons.compress.archivers.zip.ZipArchiveEntry("test.txt"));
        archiveOutputStream.write(new byte[10]);
        archiveOutputStream.finish();
        archiveOutputStream.close();
        assertTrue(zipOutputStream.size() > 0);

        // Testing null output stream
        OutputStream nullOutputStream = null;
        try {
            archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.AR, nullOutputStream);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        } finally {
            // nothing to close
        }
    }
}