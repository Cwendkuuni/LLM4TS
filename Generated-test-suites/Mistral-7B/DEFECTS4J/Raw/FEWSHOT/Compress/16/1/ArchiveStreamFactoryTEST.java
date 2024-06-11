package org.apache.commons.compress.archivers;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;

import org.apache.commons.compress.archivers.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.JarArchiveInputStream;
import org.apache.commons.compress.archivers.JarArchiveOutputStream;
import org.apache.commons.compress.archivers.TarArchiveInputStream;
import org.apache.commons.compress.archivers.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.ZipArchiveOutputStream;
import org.apache.commons.compress.archivers.util.TestUtils;
import org.junit.Test;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() throws IOException {
        final String[] archivers = new String[]{"ar", "zip", "tar", "jar", "dump", "cpio"};
        final InputStream[] inputs = new InputStream[]{
                new ByteArrayInputStream(TestUtils.getArTestData()),
                new ByteArrayInputStream(TestUtils.getZipTestData()),
                new ByteArrayInputStream(TestUtils.getTarTestData()),
                new ByteArrayInputStream(TestUtils.getJarTestData()),
                new ByteArrayInputStream(TestUtils.getCpioTestData()),
                new ByteArrayInputStream(TestUtils.getDumpTestData())
        };

        for (String archiver : archivers) {
            for (InputStream input : inputs) {
                ArchiveInputStream result = ArchiveStreamFactory.getInstance().createArchiveInputStream(archiver, input);
                assertInstanceOf(archiver.equalsIgnoreCase("cpio") ? CpioArchiveInputStream.class :
                        archiver.equalsIgnoreCase("dump") ? DumpArchiveInputStream.class :
                        archiver.equalsIgnoreCase("jar") ? JarArchiveInputStream.class :
                        archiver.equalsIgnoreCase("tar") ? TarArchiveInputStream.class :
                        archiver.equalsIgnoreCase("ar") ? ArArchiveInputStream.class :
                        archiver.equalsIgnoreCase("zip") ? ZipArchiveInputStream.class : null, result);
            }
        }
    }

    @Test
    public void testCreateArchiveInputStreamNullArchiver() {
        assertNull(ArchiveStreamFactory.getInstance().createArchiveInputStream(null, new ByteArrayInputStream(new byte[0])));
    }

    @Test
    public void testCreateArchiveInputStreamNullInputStream() {
        assertNull(ArchiveStreamFactory.getInstance().createArchiveInputStream("zip", null));
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamUnknownArchiver() throws IOException {
        ArchiveStreamFactory.getInstance().createArchiveInputStream("unknown", new ByteArrayInputStream(new byte[0]));
    }

    @Test
    public void testCreateArchiveOutputStream() throws IOException {
        final String[] archivers = new String[]{"ar", "zip", "tar", "jar", "cpio"};
        final OutputStream[] outputs = new OutputStream[]{
                new ByteArrayOutputStream(),
                new ByteArrayOutputStream(),
                new ByteArrayOutputStream(),
                new ByteArrayOutputStream(),
                new ByteArrayOutputStream()
        };

        for (String archiver : archivers) {
            for (OutputStream output : outputs) {
                ArchiveOutputStream result = ArchiveStreamFactory.getInstance().createArchiveOutputStream(archiver, output);
                assertInstanceOf(archiver.equalsIgnoreCase("cpio") ? CpioArchiveOutputStream.class :
                        archiver.equalsIgnoreCase("dump") ? DumpArchiveOutputStream.class :
                        archiver.equalsIgnoreCase("jar") ? JarArchiveOutputStream.class :
                        archiver.equalsIgnoreCase("tar") ? TarArchiveOutputStream.class :
                        archiver.equalsIgnoreCase("ar") ? ArArchiveOutputStream.class :
                        archiver.equalsIgnoreCase("zip") ? ZipArchiveOutputStream.class : null, result);
            }
        }
    }

    @Test
    public void testCreateArchiveOutputStreamNullArchiver() {
        assertNull(ArchiveStreamFactory.getInstance().createArchiveOutputStream(null, new ByteArrayOutputStream()));
    }

    @Test
    public void testCreateArchiveOutputStreamNullOutputStream() {
        assertNull(ArchiveStreamFactory.getInstance().createArchiveOutputStream("zip", null));
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStreamUnknownArchiver() throws IOException {
        ArchiveStreamFactory.getInstance().createArchiveOutputStream("unknown", new ByteArrayOutputStream());
    }
}