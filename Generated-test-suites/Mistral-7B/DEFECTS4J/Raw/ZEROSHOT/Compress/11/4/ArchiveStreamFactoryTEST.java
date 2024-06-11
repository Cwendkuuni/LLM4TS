package org.apache.commons.compress.archivers.test;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test case for {@link ArchiveStreamFactory}.
 */
public class ArchiveStreamFactoryTest {

    /**
     * Test creating an archive input stream from an archive name and an input stream.
     * @throws Exception in case of test failures
     */
    @Test
    public void testCreateArchiveInputStreamFromNameAndInputStream() throws Exception {
        // Test AR
        InputStream in = new ByteArrayInputStream(new byte[10]);
        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance()
                .createArchiveInputStream(ArchiveStreamFactory.AR, in);
        assertNotNull(archiveInputStream);
        assertEquals(ArchiveInputStream.class, archiveInputStream.getClass());
        ArchiveInputStream arArchiveInputStream = (ArchiveInputStream) archiveInputStream;
        assertEquals(ArArchiveInputStream.class, arArchiveInputStream.getClass());

        // Test ZIP
        in = new ByteArrayInputStream(new byte[10]);
        archiveInputStream = ArchiveStreamFactory.getInstance()
                .createArchiveInputStream(ArchiveStreamFactory.ZIP, in);
        assertNotNull(archiveInputStream);
        assertEquals(ArchiveInputStream.class, archiveInputStream.getClass());
        ZipArchiveInputStream zipArchiveInputStream = (ZipArchiveInputStream) archiveInputStream;
        assertEquals(ZipArchiveInputStream.class, zipArchiveInputStream.getClass());

        // Test TAR
        in = new ByteArrayInputStream(new byte[10]);
        archiveInputStream = ArchiveStreamFactory.getInstance()
                .createArchiveInputStream(ArchiveStreamFactory.TAR, in);
        assertNotNull(archiveInputStream);
        assertEquals(ArchiveInputStream.class, archiveInputStream.getClass());
        TarArchiveInputStream tarArchiveInputStream = (TarArchiveInputStream) archiveInputStream;
        assertEquals(TarArchiveInputStream.class, tarArchiveInputStream.getClass());

        // Test JAR
        in = new ByteArrayInputStream(new byte[10]);
        archiveInputStream = ArchiveStreamFactory.getInstance()
                .createArchiveInputStream(ArchiveStreamFactory.JAR, in);
        assertNotNull(archiveInputStream);
        assertEquals(ArchiveInputStream.class, archiveInputStream.getClass());
        JarArchiveInputStream jarArchiveInputStream = (JarArchiveInputStream) archiveInputStream;
        assertEquals(JarArchiveInputStream.class, jarArchiveInputStream.getClass());

        // Test CPIO
        in = new ByteArrayInputStream(new byte[10]);
        archiveInputStream = ArchiveStreamFactory.getInstance()
                .createArchiveInputStream(ArchiveStreamFactory.CPIO, in);
        assertNotNull(archiveInputStream);
        assertEquals(ArchiveInputStream.class, archiveInputStream.getClass());
        CpioArchiveInputStream cpioArchiveInputStream = (CpioArchiveInputStream) archiveInputStream;
        assertEquals(CpioArchiveInputStream.class, cpioArchiveInputStream.getClass());

        // Test DUMP
        in = new ByteArrayInputStream(new byte[10]);
        archiveInputStream = ArchiveStreamFactory.getInstance()
                .createArchiveInputStream(ArchiveStreamFactory.DUMP, in);
        assertNotNull(archiveInputStream);
        assertEquals(ArchiveInputStream.class, archiveInputStream.getClass());
        DumpArchiveInputStream dumpArchiveInputStream = (DumpArchiveInputStream) archiveInputStream;
        assertEquals(DumpArchiveInputStream.class, dumpArchiveInputStream.getClass());

        // Test unknown format
        in = new ByteArrayInputStream(new byte[10]);
        try {
            archiveInputStream = ArchiveStreamFactory.getInstance()
                    .createArchiveInputStream("unknown", in);
            fail("Expected ArchiveException");
        } catch (ArchiveException e) {
            // Expected
        }
    }

    /**
     * Test creating an archive output stream from an archive name and an output stream.
     * @throws Exception in case of test failures
     */
    @Test
    public void testCreateArchiveOutputStreamFromNameAndOutputStream() throws Exception {
        // Test AR
        OutputStream out = new ByteArrayOutputStream(10);
        ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.getInstance()
                .createArchiveOutputStream(ArchiveStreamFactory.AR, out);
        assertNotNull(archiveOutputStream);
        assertEquals(ArchiveOutputStream.class, archiveOutputStream.getClass());
        ArchiveOutputStream arArchiveOutputStream = (ArchiveOutputStream) archiveOutputStream;
        assertEquals(ArArchiveOutputStream.class, arArchiveOutputStream.getClass());

        // Test ZIP
        out = new ByteArrayOutputStream(10);
        archiveOutputStream = ArchiveStreamFactory.getInstance()
                .createArchiveOutputStream(ArchiveStreamFactory.ZIP, out);
        assertNotNull(archiveOutputStream);
        assertEquals(ArchiveOutputStream.class, archiveOutputStream.getClass());
        ZipArchiveOutputStream zipArchiveOutputStream = (ZipArchiveOutputStream) archiveOutputStream;
        assertEquals(ZipArchiveOutputStream.class, zipArchiveOutputStream.getClass());

        // Test TAR
        out = new ByteArrayOutputStream(10);
        archiveOutputStream = ArchiveStreamFactory.getInstance()
                .createArchiveOutputStream(ArchiveStreamFactory.TAR, out);
        assertNotNull(archiveOutputStream);
        assertEquals(ArchiveOutputStream.class, archiveOutputStream.getClass());
        TarArchiveOutputStream tarArchiveOutputStream = (TarArchiveOutputStream) archiveOutputStream;
        assertEquals(TarArchiveOutputStream.class, tarArchiveOutputStream.getClass());

        // Test JAR
        out = new ByteArrayOutputStream(10);
        archiveOutputStream = ArchiveStreamFactory.getInstance()
                .createArchiveOutputStream(ArchiveStreamFactory.JAR, out);
        assertNotNull(archiveOutputStream);
        assertEquals(ArchiveOutputStream.class, archiveOutputStream.getClass());
        JarArchiveOutputStream jarArchiveOutputStream = (JarArchiveOutputStream) archiveOutputStream;
        assertEquals(JarArchiveOutputStream.class, jarArchiveOutputStream.getClass());

        // Test CPIO
        out = new ByteArrayOutputStream(10);
        archiveOutputStream = ArchiveStreamFactory.getInstance()
                .createArchiveOutputStream(ArchiveStreamFactory.CPIO, out);
        assertNotNull(archiveOutputStream);
        assertEquals(ArchiveOutputStream.class, archiveOutputStream.getClass());
        CpioArchiveOutputStream cpioArchiveOutputStream = (CpioArchiveOutputStream) archiveOutputStream;
        assertEquals(CpioArchiveOutputStream.class, cpioArchiveOutputStream.getClass());

        // Test DUMP
        out = new ByteArrayOutputStream(10);
        archiveOutputStream = ArchiveStreamFactory.getInstance()
                .createArchiveOutputStream(ArchiveStreamFactory.DUMP, out);
        assertNotNull(archiveOutputStream);
        assertEquals(ArchiveOutputStream.class, archiveOutputStream.getClass());
        DumpArchiveOutputStream dumpArchiveOutputStream = (DumpArchiveOutputStream) archiveOutputStream;
        assertEquals(DumpArchiveOutputStream.class, dumpArchiveOutputStream.getClass());

        // Test unknown format
        out = new ByteArrayOutputStream(10);
        try {
            archiveOutputStream = ArchiveStreamFactory.getInstance()
                    .createArchiveOutputStream("unknown", out);
            fail("Expected ArchiveException");
        } catch (ArchiveException e) {
            // Expected
        }
    }

    /**
     * Test creating an archive input stream from an input stream by autodetection.
     * @throws Exception in case of test failures
     */
    @Test
    public void testCreateArchiveInputStreamFromInputStream() throws Exception {
        // Test ZIP
        ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
        OutputStream os = out;
        ArchiveOutputStream zipArchiveOutputStream = ArchiveStreamFactory.getInstance()
                .createArchiveOutputStream(ArchiveStreamFactory.ZIP, os);
        zipArchiveOutputStream.putArchiveEntry(new JarArchiveEntry("test.txt"));
        zipArchiveOutputStream.write(new byte[1], 0, 1); // write metadata
        zipArchiveOutputStream.closeArchiveEntry();
        zipArchiveOutputStream.finish();
        os.flush();
        InputStream in = new ByteArrayInputStream(out.toByteArray());
        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance()
                .createArchiveInputStream(in);
        assertEquals(JarArchiveInputStream.class, archiveInputStream.getClass());
        JarArchiveEntry jarArchiveEntry = (JarArchiveEntry) archiveInputStream.getNextEntry();
        assertEquals("test.txt", jarArchiveEntry.getName());

        // Test AR
        out = new ByteArrayOutputStream(1024);
        os = out;
        ArchiveOutputStream arArchiveOutputStream = ArchiveStreamFactory.getInstance()
                .createArchiveOutputStream(ArchiveStreamFactory.AR, os);
        arArchiveOutputStream.putArchiveEntry(new ArArchiveEntry("test.txt"));
        arArchiveOutputStream.writeToArchive(new byte[256]);
        arArchiveOutputStream.closeArchiveEntry();
        arArchiveOutputStream.finish();
        os.flush();
        in = new ByteArrayInputStream(out.toByteArray());
        archiveInputStream = ArchiveStreamFactory.getInstance()
                .createArchiveInputStream(in);
        assertEquals(ArArchiveInputStream.class, archiveInputStream.getClass());
        ArArchiveEntry arArchiveEntry = (ArArchiveEntry) archiveInputStream.getNextEntry();
        assertEquals("test.txt", arArchiveEntry.getName());

        // Test TAR
        out = new ByteArrayOutputStream(1024);
        os = out;
        ArchiveOutputStream tarArchiveOutputStream = ArchiveStreamFactory.getInstance()
                .createArchiveOutputStream(ArchiveStreamFactory.TAR, os);
        tarArchiveOutputStream.putNextEntry(new TarEntry("test.txt"));
        tarArchiveOutputStream.write(new byte[256]);
        tarArchiveOutputStream.closeEntry();
        tarArchiveOutputStream.finish();
        os.flush();
        in = new ByteArrayInputStream(out.toByteArray());
        archiveInputStream = ArchiveStreamFactory.getInstance()
                .createArchiveInputStream(in);
        assertEquals(TarArchiveInputStream.class, archiveInputStream.getClass());
        TarArchiveEntry tarArchiveEntry = (TarArchiveEntry) archiveInputStream.getNextEntry();
        assertEquals("test.txt", tarArchiveEntry.getName());

        // Test JAR
        out = new ByteArrayOutputStream(1024);
        os = out;
        ArchiveOutputStream jarArchiveOutputStream = ArchiveStreamFactory.getInstance()
                .createArchiveOutputStream(ArchiveStreamFactory.JAR, os);
        jarArchiveOutputStream.putNextEntry(new JarEntry("test.txt"));
        jarArchiveOutputStream.write(new byte[256]);
        jarArchiveOutputStream.closeEntry();
        jarArchiveOutputStream.finish();
        os.flush();
        in = new ByteArrayInputStream(out.toByteArray());
        archiveInputStream = ArchiveStreamFactory.getInstance()
                .createArchiveInputStream(in);
        assertEquals(JarArchiveInputStream.class, archiveInputStream.getClass());
        JarArchiveEntry jarArchiveEntry = (JarArchiveEntry) archiveInputStream.getNextEntry();
        assertEquals("test.txt", jarArchiveEntry.getName());

        // Test CPIO
        out = new ByteArrayOutputStream(1024);
        os = out;
        ArchiveOutputStream cpioArchiveOutputStream = ArchiveStreamFactory.getInstance()
                .createArchiveOutputStream(ArchiveStreamFactory.CPIO, os);
        cpioArchiveOutputStream.putNextEntry(new CpioEntry("test.txt"));
        cpioArchiveOutputStream.write(new byte[256]);
        cpioArchiveOutputStream.closeEntry();
        cpioArchiveOutputStream.finish();
        os.flush();
        in = new ByteArrayInputStream(out.toByteArray());
        archiveInputStream = ArchiveStreamFactory.getInstance()
                .createArchiveInputStream(in);
        assertEquals(CpioArchiveInputStream.class, archiveInputStream.getClass());
        CpioArchiveEntry cpioArchiveEntry = (CpioArchiveEntry) archiveInputStream.getNextEntry();
        assertEquals("test.txt", cpioArchiveEntry.getName());

        // Test DUMP
        out = new ByteArrayOutputStream(1024);
        os = out;
        ArchiveOutputStream dumpArchiveOutputStream = ArchiveStreamFactory.getInstance()
                .createArchiveOutputStream(ArchiveStreamFactory.DUMP, os);
        dumpArchiveOutputStream.putNextEntry(new DumpEntry("test.txt"));
        dumpArchiveOutputStream.write(new byte[256]);
        dumpArchiveOutputStream.closeEntry();
        dumpArchiveOutputStream.finish();
        os.flush();
        in = new ByteArrayInputStream(out.toByteArray());
        archiveInputStream = ArchiveStreamFactory.getInstance()
                .createArchiveInputStream(in);
        assertEquals(DumpArchiveInputStream.class, archiveInputStream.getClass());
        DumpArchiveEntry dumpArchiveEntry = (DumpArchiveEntry) archiveInputStream.getNextEntry();
        assertEquals("test.txt", dumpArchiveEntry.getName());

        // Test unknown format
        out = new ByteArrayOutputStream(1024);
        os = out;
        ArchiveOutputStream unknownArchiveOutputStream = ArchiveStreamFactory.getInstance()
                .createArchiveOutputStream("unknown", os);
        unknownArchiveOutputStream.finish();
        os.flush();
        in = new ByteArrayInputStream(out.toByteArray());
        try {
            archiveInputStream = ArchiveStreamFactory.getInstance()
                    .createArchiveInputStream(in);
            fail("Expected ArchiveException");
        } catch (ArchiveException e) {
            // Expected
        }
    }

}