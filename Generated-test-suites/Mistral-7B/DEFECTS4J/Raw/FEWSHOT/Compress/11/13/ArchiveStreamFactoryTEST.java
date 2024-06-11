package org.apache.commons.compress.archivers;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;

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
        String[] archivers = new String[]{"ar", "zip", "tar", "jar", "cpio", "dump"};
        InputStream input;
        for (String archiver : archivers) {
            input = new ByteArrayInputStream(ArchiveStreamFactoryTest.getSignature(archiver));
            ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(archiver, input);
            assertInstanceOf(archiver.charAt(0) == 'z' ? ZipArchiveInputStream.class :
                    archiver.equalsIgnoreCase("ar") ? ArArchiveInputStream.class :
                    archiver.equalsIgnoreCase("tar") ? TarArchiveInputStream.class :
                    archiver.equalsIgnoreCase("jar") ? JarArchiveInputStream.class :
                    archiver.equalsIgnoreCase("cpio") ? CpioArchiveInputStream.class :
                    DumpArchiveInputStream.class, archiveInputStream.getClass());
            ArchiveStreamFactory.getInstance().releaseArchiveInputStream(archiveInputStream);
        }
        input = new ByteArrayInputStream(new byte[0]);
        assertNull(ArchiveStreamFactory.getInstance().createArchiveInputStream(null, input));
        assertNull(ArchiveStreamFactory.getInstance().createArchiveInputStream("", input));
        assertNull(ArchiveStreamFactory.getInstance().createArchiveInputStream("invalid", input));
    }

    @Test
    public void testCreateArchiveOutputStream() {
        String[] archivers = new String[]{"ar", "zip", "tar", "jar", "cpio"};
        OutputStream output;
        for (String archiver : archivers) {
            output = new ByteArrayOutputStream();
            ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(archiver, output);
            assertInstanceOf(archiver.charAt(0) == 'z' ? ZipArchiveOutputStream.class :
                    archiver.equalsIgnoreCase("ar") ? ArArchiveOutputStream.class :
                    archiver.equalsIgnoreCase("tar") ? TarArchiveOutputStream.class :
                    archiver.equalsIgnoreCase("jar") ? JarArchiveOutputStream.class :
                    CpioArchiveOutputStream.class, archiveOutputStream.getClass());
            ArchiveStreamFactory.getInstance().releaseArchiveOutputStream(archiveOutputStream);
        }
        output = new ByteArrayOutputStream();
        assertNull(ArchiveStreamFactory.getInstance().createArchiveOutputStream(null, output));
        assertNull(ArchiveStreamFactory.getInstance().createArchiveOutputStream("", output));
        assertNull(ArchiveStreamFactory.getInstance().createArchiveOutputStream("invalid", output));
    }

    @Test
    public void testCreateArchiveInputStreamFromStream() {
        InputStream input;
        ArchiveInputStream archiveInputStream;
        String[] archivers = new String[]{"zip", "jar", "ar", "tar", "cpio", "dump"};
        for (String archiver : archivers) {
            input = new ByteArrayInputStream(ArchiveStreamFactoryTest.getSignature(archiver));
            archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(archiver, input);
            assertInstanceOf(archiver.charAt(0) == 'z' ? ZipArchiveInputStream.class :
                    archiver.equalsIgnoreCase("ar") ? ArArchiveInputStream.class :
                    archiver.equalsIgnoreCase("tar") ? TarArchiveInputStream.class :
                    archiver.equalsIgnoreCase("jar") ? JarArchiveInputStream.class :
                    archiver.equalsIgnoreCase("cpio") ? CpioArchiveInputStream.class :
                    DumpArchiveInputStream.class, archiveInputStream.getClass());
            ArchiveStreamFactory.getInstance().releaseArchiveInputStream(archiveInputStream);
        }
        input = new ByteArrayInputStream(new byte[0]);
        assertNull(ArchiveStreamFactory.getInstance().createArchiveInputStream(null, input));
        assertNull(ArchiveStreamFactory.getInstance().createArchiveInputStream("", input));
        assertNull(ArchiveStreamFactory.getInstance().createArchiveInputStream("invalid", input));
    }

    private static byte[] getSignature(String archiver) {
        switch (archiver) {
            case "ar":
                return ArchiveStreamFactory.AR_SIGNATURE;
            case "zip":
            case "jar":
                return new byte[0]; // Zip does not have a fixed signature
            case "tar":
                return ArchiveStreamFactory.TAR_SIGNATURE;
            case "cpio":
                return ArchiveStreamFactory.CPIO_SIGNATURE;
            case "dump":
                return ArchiveStreamFactory.DUMP_SIGNATURE;
            default:
                throw new IllegalArgumentException("Invalid archiver: " + archiver);
        }
    }
}