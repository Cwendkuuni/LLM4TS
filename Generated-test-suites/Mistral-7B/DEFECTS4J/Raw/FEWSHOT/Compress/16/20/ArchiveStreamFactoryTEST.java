package org.apache.commons.compress.archivers;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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

public class ArchiveStreamFactoryTest {
    @Test
    public void testCreateArchiveInputStream() {
        try {
            InputStream in = new ByteArrayInputStream("test".getBytes()); // invalid archive
            ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.ZIP, in);
            assertNull(archiveInputStream);

            // Testing for each supported format
            InputStream arInputStream = new ByteArrayInputStream(new byte[]{'!'}); // AR archive signature
            ArchiveInputStream arArchiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.AR, arInputStream);
            assertNotNull(arArchiveInputStream);
            assertTrue(arArchiveInputStream instanceof ArArchiveInputStream);
            ((ArArchiveInputStream) arArchiveInputStream).close();

            InputStream cpioInputStream = new ByteArrayInputStream(new byte[]{'0004030201'}); // CPIO archive signature
            ArchiveInputStream cpioArchiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.CPIO, cpioInputStream);
            assertNotNull(cpioArchiveInputStream);
            assertTrue(cpioArchiveInputStream instanceof CpioArchiveInputStream);
            ((CpioArchiveInputStream) cpioArchiveInputStream).close();

            InputStream dumpInputStream = new ByteArrayInputStream(new byte[32]); // Dump archive signature
            ArchiveInputStream dumpArchiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.DUMP, dumpInputStream);
            assertNotNull(dumpArchiveInputStream);
            assertTrue(dumpArchiveInputStream instanceof DumpArchiveInputStream);
            ((DumpArchiveInputStream) dumpArchiveInputStream).close();

            InputStream jarInputStream = new ByteArrayInputStream(new byte[2]); // JAR archive signature (magic number '0x00 0x02')
            ArchiveInputStream jarArchiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.JAR, jarInputStream);
            assertNotNull(jarArchiveInputStream);
            assertTrue(jarArchiveInputStream instanceof JarArchiveInputStream);
            ((JarArchiveInputStream) jarArchiveInputStream).close();

            InputStream tarInputStream = new ByteArrayInputStream(new byte[512]); // Tar archive header
            ArchiveInputStream tarArchiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.TAR, tarInputStream);
            assertNotNull(tarArchiveInputStream);
            assertTrue(tarArchiveInputStream instanceof TarArchiveInputStream);
            ((TarArchiveInputStream) tarArchiveInputStream).close();

            InputStream zipInputStream = new ByteArrayInputStream(new byte[2]); // ZIP archive signature (magic number '0x02 0x01')
            ArchiveInputStream zipArchiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.ZIP, zipInputStream);
            assertNotNull(zipArchiveInputStream);
            assertTrue(zipArchiveInputStream instanceof ZipArchiveInputStream);
            ((ZipArchiveInputStream) zipArchiveInputStream).close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCreateArchiveOutputStream() {
        try {
            OutputStream out = new ByteArrayOutputStream();
            ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.ZIP, out);
            assertNotNull(archiveOutputStream);
            assertTrue(archiveOutputStream instanceof ZipArchiveOutputStream);
            archiveOutputStream.finish();

            // Testing for each supported format
            OutputStream arOutputStream = new ByteArrayOutputStream();
            ArchiveOutputStream arArchiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.AR, arOutputStream);
            assertNotNull(arArchiveOutputStream);
            assertTrue(arArchiveOutputStream instanceof ArArchiveOutputStream);
            ((ArArchiveOutputStream) arArchiveOutputStream).close();

            OutputStream cpioOutputStream = new ByteArrayOutputStream();
            ArchiveOutputStream cpioArchiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.CPIO, cpioOutputStream);
            assertNotNull(cpioArchiveOutputStream);
            assertTrue(cpioArchiveOutputStream instanceof CpioArchiveOutputStream);
            ((CpioArchiveOutputStream) cpioArchiveOutputStream).close();

            OutputStream dumpOutputStream = new ByteArrayOutputStream();
            ArchiveOutputStream dumpArchiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.DUMP, dumpOutputStream);
            assertNotNull(dumpArchiveOutputStream);
            assertTrue(dumpArchiveOutputStream instanceof DumpArchiveOutputStream);
            ((DumpArchiveOutputStream) dumpArchiveOutputStream).close();

            OutputStream tarOutputStream = new ByteArrayOutputStream();
            ArchiveOutputStream tarArchiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.TAR, tarOutputStream);
            assertNotNull(tarArchiveOutputStream);
            assertTrue(tarArchiveOutputStream instanceof TarArchiveOutputStream);
            ((TarArchiveOutputStream) tarArchiveOutputStream).close();

            OutputStream zipOutputStream = new ByteArrayOutputStream();
            ArchiveOutputStream zipArchiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.ZIP, zipOutputStream);
            assertNotNull(zipArchiveOutputStream);
            assertTrue(zipArchiveOutputStream instanceof ZipArchiveOutputStream);
            ((ZipArchiveOutputStream) zipArchiveOutputStream).close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}