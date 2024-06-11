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
    public void testCreateArchiveInputStream() throws Exception {
        // Test with known archive types
        InputStream testInputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.ZIP, testInputStream);
        assertInstanceOf(ZipArchiveInputStream.class, archiveInputStream);

        archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.JAR, testInputStream);
        assertInstanceOf(JarArchiveInputStream.class, archiveInputStream);

        archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.AR, testInputStream);
        assertInstanceOf(ArArchiveInputStream.class, archiveInputStream);

        archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.TAR, testInputStream);
        assertInstanceOf(TarArchiveInputStream.class, archiveInputStream);

        archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.CPIO, testInputStream);
        assertInstanceOf(CpioArchiveInputStream.class, archiveInputStream);

        archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.DUMP, testInputStream);
        assertInstanceOf(DumpArchiveInputStream.class, archiveInputStream);

        // Test with null archiver name
        testInputStream = new ByteArrayInputStream(new byte[0]);
        assertNull(ArchiveStreamFactory.getInstance().createArchiveInputStream(null, testInputStream));

        // Test with null input stream
        assertNull(ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.ZIP, null));

        // Test with unknown archiver name
        assertNull(ArchiveStreamFactory.getInstance().createArchiveInputStream("unknown", testInputStream));
    }

    @Test
    public void testCreateArchiveOutputStream() throws Exception {
        // Test with known archive types
        OutputStream testOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.ZIP, testOutputStream);
        assertInstanceOf(ZipArchiveOutputStream.class, archiveOutputStream);

        archiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.JAR, testOutputStream);
        assertInstanceOf(JarArchiveOutputStream.class, archiveOutputStream);

        archiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.AR, testOutputStream);
        assertInstanceOf(ArArchiveOutputStream.class, archiveOutputStream);

        archiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.TAR, testOutputStream);
        assertInstanceOf(TarArchiveOutputStream.class, archiveOutputStream);

        archiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.CPIO, testOutputStream);
        assertInstanceOf(CpioArchiveOutputStream.class, archiveOutputStream);

        archiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.DUMP, testOutputStream);
        assertInstanceOf(DumpArchiveOutputStream.class, archiveOutputStream);

        // Test with null archiver name
        assertNull(ArchiveStreamFactory.getInstance().createArchiveOutputStream(null, testOutputStream));

        // Test with null output stream
        assertNull(ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.ZIP, null));

        // Test with unknown archiver name
        assertNull(ArchiveStreamFactory.getInstance().createArchiveOutputStream("unknown", testOutputStream));
    }

    @Test
    public void testCreateArchiveInputStreamWithSignature() throws Exception {
        // Test with known archive types
        InputStream testInputStream = new ByteArrayInputStream(getZipSignature());
        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(testInputStream);
        assertInstanceOf(ZipArchiveInputStream.class, archiveInputStream);

        testInputStream = new ByteArrayInputStream(getJarSignature());
        archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(testInputStream);
        assertInstanceOf(JarArchiveInputStream.class, archiveInputStream);

        testInputStream = new ByteArrayInputStream(getArSignature());
        archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(testInputStream);
        assertInstanceOf(ArArchiveInputStream.class, archiveInputStream);

        testInputStream = new ByteArrayInputStream(getTarSignature());
        archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(testInputStream);
        assertInstanceOf(TarArchiveInputStream.class, archiveInputStream);

        testInputStream = new ByteArrayInputStream(getCpioSignature());
        archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(testInputStream);
        assertInstanceOf(CpioArchiveInputStream.class, archiveInputStream);

        testInputStream = new ByteArrayInputStream(getDumpSignature());
        archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(testInputStream);
        assertInstanceOf(DumpArchiveInputStream.class, archiveInputStream);

        // Test with null input stream
        assertNull(ArchiveStreamFactory.getInstance().createArchiveInputStream(null));

        // Test with unknown archive type
        InputStream unknownArchiveInputStream = new ByteArrayInputStream(new byte[0]);
        assertNull(ArchiveStreamFactory.getInstance().createArchiveInputStream(unknownArchiveInputStream));
    }

    private byte[] getZipSignature() {
        return new byte[]{0x50, 0x4B, 0x03, 0x59, 0x50, 0x45, 0x44, 0x54, 0x20, 0x14, 0x00, 0x00};
    }

    private byte[] getJarSignature() {
        return new byte[]{0x00, 0x02, 0x01, 0x4C, 0x69, 0x6E, 0x65, 0x78, 0x70, 0x00, 0x02, 0x01, 0x10, 0x00};
    }

    private byte[] getArSignature() {
        return new byte[]{0x61, 0x73, 0x74, 0x52, 0x61, 0x73, 0x74, 0x41, 0x72, 0x72, 0x65, 0x74, 0x53, 0x6F, 0x66, 0x74};
    }

    private byte[] getTarSignature() {
        return new byte[]{0x00, 0x00, 0x14, 0x00, 0x00, 0x1A, 0x50, 0x4B, 0x03, 0x00, 0x14, 0x00, 0x00, 0x00, 0x18, 0x00, 0x00};
    }

    private byte[] getCpioSignature() {
        return new byte[]{0x07, 0x00, 0x01, 0x43, 0x41, 0x4D, 0x50, 0x4C, 0x45, 0x01, 0x00, 0x01};
    }

    private byte[] getDumpSignature() {
        return new byte[]{0x64, 0x75, 0x6E, 0x6B, 0x62, 0x6F, 0x6F, 0x6D, 0x00, 0x00, 0x0B, 0x62, 0x61, 0x73, 0x74, 0x72, 0x6F, 0x6F, 0x74};
    }
}