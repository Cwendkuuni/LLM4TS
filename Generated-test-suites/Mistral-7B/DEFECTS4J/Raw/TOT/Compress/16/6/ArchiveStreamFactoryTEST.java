package org.apache.commons.compress.archivers.test;

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
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class ArchiveStreamFactoryTest {

    private final String archiverName;
    private final Class<? extends ArchiveInputStream> expectedInputStreamClass;
    private final Class<? extends ArchiveOutputStream> expectedOutputStreamClass;

    public ArchiveStreamFactoryTest(String archiverName,
                                    Class<? extends ArchiveInputStream> expectedInputStreamClass,
                                    Class<? extends ArchiveOutputStream> expectedOutputStreamClass) {
        this.archiverName = archiverName;
        this.expectedInputStreamClass = expectedInputStreamClass;
        this.expectedOutputStreamClass = expectedOutputStreamClass;
    }

    @Parameterized.Parameters(name = "{0} archive name")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
            {"ar", ArArchiveInputStream.class, ArArchiveOutputStream.class},
            {"cpio", CpioArchiveInputStream.class, CpioArchiveOutputStream.class},
            {"dump", DumpArchiveInputStream.class, null},
            {"jar", JarArchiveInputStream.class, JarArchiveOutputStream.class},
            {"tar", TarArchiveInputStream.class, TarArchiveOutputStream.class},
            {"zip", ZipArchiveInputStream.class, ZipArchiveOutputStream.class},
            {"unknown", null, null}
        });
    }

    @Test
    public void testCreateArchiveInputStream() throws IOException, ArchiveException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory()
                .createArchiveInputStream(this.archiverName, inputStream);
        assertEquals(this.expectedInputStreamClass, archiveInputStream.getClass());
    }

    @Test
    public void testCreateArchiveOutputStream() throws IOException, ArchiveException {
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory()
                .createArchiveOutputStream(this.archiverName, outputStream);
        assertEquals(this.expectedOutputStreamClass, archiveOutputStream.getClass());

        // Test creating an archive entry and writing it to the archive
        if (this.expectedOutputStreamClass != null) {
            archiveOutputStream.putArchiveEntry(new JarArchiveEntry("test.txt"));
            archiveOutputStream.write(new byte[0], 0, 0);
            archiveOutputStream.closeArchiveEntry();
            archiveOutputStream.finish();
            archiveOutputStream.close();

            JarArchiveInputStream archiveInputStream = new JarArchiveInputStream(
                    new ByteArrayInputStream(outputStream.toByteArray()));
            JarArchiveEntry entry = (JarArchiveEntry) archiveInputStream.getNextEntry();
            assertEquals("test.txt", entry.getName());
            assertNull(entry.getSize());
            assertFalse(entry.isDirectory());
            archiveInputStream.closeEntry();
            archiveInputStream.close();
        }
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamUnknownFormat() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        new ArchiveStreamFactory().createArchiveInputStream("unknown", inputStream);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStreamUnsupportedFormat() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        new ArchiveStreamFactory().createArchiveOutputStream("dump", outputStream);
    }
}