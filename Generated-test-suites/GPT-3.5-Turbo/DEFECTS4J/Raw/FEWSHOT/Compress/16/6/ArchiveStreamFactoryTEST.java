import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
import org.junit.Before;
import org.junit.Test;

public class ArchiveStreamFactoryTest {

    private ArchiveStreamFactory factory;

    @Before
    public void setUp() {
        factory = new ArchiveStreamFactory();
    }

    @Test
    public void testCreateArchiveInputStream_withArchiverNameAndInputStream() throws ArchiveException {
        ArchiveInputStream arIn = factory.createArchiveInputStream(ArchiveStreamFactory.AR, System.in);
        assertNotNull(arIn);
        assertEquals(ArArchiveInputStream.class, arIn.getClass());

        ArchiveInputStream zipIn = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, System.in);
        assertNotNull(zipIn);
        assertEquals(ZipArchiveInputStream.class, zipIn.getClass());

        ArchiveInputStream tarIn = factory.createArchiveInputStream(ArchiveStreamFactory.TAR, System.in);
        assertNotNull(tarIn);
        assertEquals(TarArchiveInputStream.class, tarIn.getClass());

        ArchiveInputStream jarIn = factory.createArchiveInputStream(ArchiveStreamFactory.JAR, System.in);
        assertNotNull(jarIn);
        assertEquals(JarArchiveInputStream.class, jarIn.getClass());

        ArchiveInputStream cpioIn = factory.createArchiveInputStream(ArchiveStreamFactory.CPIO, System.in);
        assertNotNull(cpioIn);
        assertEquals(CpioArchiveInputStream.class, cpioIn.getClass());

        ArchiveInputStream dumpIn = factory.createArchiveInputStream(ArchiveStreamFactory.DUMP, System.in);
        assertNotNull(dumpIn);
        assertEquals(DumpArchiveInputStream.class, dumpIn.getClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_withNullArchiverName() throws ArchiveException {
        factory.createArchiveInputStream(null, System.in);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_withNullInputStream() throws ArchiveException {
        factory.createArchiveInputStream(ArchiveStreamFactory.AR, null);
    }
  
    @Test
    public void testCreateArchiveOutputStream_withArchiverNameAndOutputStream() throws ArchiveException {
        ArchiveOutputStream arOut = factory.createArchiveOutputStream(ArchiveStreamFactory.AR, System.out);
        assertNotNull(arOut);
        assertEquals(ArArchiveOutputStream.class, arOut.getClass());

        ArchiveOutputStream zipOut = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, System.out);
        assertNotNull(zipOut);
        assertEquals(ZipArchiveOutputStream.class, zipOut.getClass());

        ArchiveOutputStream tarOut = factory.createArchiveOutputStream(ArchiveStreamFactory.TAR, System.out);
        assertNotNull(tarOut);
        assertEquals(TarArchiveOutputStream.class, tarOut.getClass());

        ArchiveOutputStream jarOut = factory.createArchiveOutputStream(ArchiveStreamFactory.JAR, System.out);
        assertNotNull(jarOut);
        assertEquals(JarArchiveOutputStream.class, jarOut.getClass());

        ArchiveOutputStream cpioOut = factory.createArchiveOutputStream(ArchiveStreamFactory.CPIO, System.out);
        assertNotNull(cpioOut);
        assertEquals(CpioArchiveOutputStream.class, cpioOut.getClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStream_withNullArchiverName() throws ArchiveException {
        factory.createArchiveOutputStream(null, System.out);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStream_withNullOutputStream() throws ArchiveException {
        factory.createArchiveOutputStream(ArchiveStreamFactory.AR, null);
    }
  
    @Test
    public void testCreateArchiveInputStream_autoDetect() throws ArchiveException {
        ArchiveInputStream zipIn = factory.createArchiveInputStream(System.in);
        assertNotNull(zipIn);
        assertEquals(ZipArchiveInputStream.class, zipIn.getClass());

        ArchiveInputStream jarIn = factory.createArchiveInputStream(System.in);
        assertNotNull(jarIn);
        assertEquals(JarArchiveInputStream.class, jarIn.getClass());

        ArchiveInputStream arIn = factory.createArchiveInputStream(System.in);
        assertNotNull(arIn);
        assertEquals(ArArchiveInputStream.class, arIn.getClass());

        ArchiveInputStream cpioIn = factory.createArchiveInputStream(System.in);
        assertNotNull(cpioIn);
        assertEquals(CpioArchiveInputStream.class, cpioIn.getClass());

        ArchiveInputStream dumpIn = factory.createArchiveInputStream(System.in);
        assertNotNull(dumpIn);
        assertEquals(DumpArchiveInputStream.class, dumpIn.getClass());

        ArchiveInputStream tarIn = factory.createArchiveInputStream(System.in);
        assertNotNull(tarIn);
        assertEquals(TarArchiveInputStream.class, tarIn.getClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_autoDetect_withNullInputStream() throws ArchiveException {
        factory.createArchiveInputStream(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_autoDetect_withUnsupportedMark() throws ArchiveException {
        InputStream in = System.in;
        factory.createArchiveInputStream(in);
    }

}