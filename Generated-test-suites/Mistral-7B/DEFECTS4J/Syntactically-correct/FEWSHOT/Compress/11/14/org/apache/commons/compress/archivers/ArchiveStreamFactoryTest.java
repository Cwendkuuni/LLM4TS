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
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.junit.Test;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() {
        // Test AR
        ArchiveInputStream testStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.AR, new ByteArrayInputStream(new byte[0]));
        assertInstanceOf(ArArchiveInputStream.class, testStream);

        // Test CPIO
        testStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.CPIO, new ByteArrayInputStream(new byte[0]));
        assertInstanceOf(CpioArchiveInputStream.class, testStream);

        // Test Dump
        testStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.DUMP, new ByteArrayInputStream(new byte[0]));
        assertInstanceOf(DumpArchiveInputStream.class, testStream);

        // Test JAR
        testStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.JAR, new ByteArrayInputStream(new byte[0]));
        assertInstanceOf(JarArchiveInputStream.class, testStream);

        // Test TAR
        testStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.TAR, new ByteArrayInputStream(new byte[0]));
        assertInstanceOf(TarArchiveInputStream.class, testStream);

        // Test ZIP
        testStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.ZIP, new ByteArrayInputStream(new byte[0]));
        assertInstanceOf(ZipArchiveInputStream.class, testStream);

        // Test unknown
        testStream = ArchiveStreamFactory.getInstance().createArchiveInputStream("unknown", new ByteArrayInputStream(new byte[0]));
        assertNull(testStream);

        // Test null input stream
        try {
            ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.AR, null);
        } catch (ArchiveException e) {
            // Expected
        }
    }

    @Test
    public void testCreateArchiveOutputStream() {
        // Test AR
        ArchiveOutputStream testStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.AR, new ByteArrayOutputStream());
        assertInstanceOf(ArArchiveOutputStream.class, testStream);

        // Test CPIO
        testStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.CPIO, new ByteArrayOutputStream());
        assertInstanceOf(CpioArchiveOutputStream.class, testStream);

        // Test Dump
        testStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.DUMP, new ByteArrayOutputStream());
        assertInstanceOf(DumpArchiveOutputStream.class, testStream);

        // Test JAR
        testStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.JAR, new ByteArrayOutputStream());
        assertInstanceOf(JarArchiveOutputStream.class, testStream);

        // Test TAR
        testStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.TAR, new ByteArrayOutputStream());
        assertInstanceOf(TarArchiveOutputStream.class, testStream);

        // Test ZIP
        testStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.ZIP, new ByteArrayOutputStream());
        assertInstanceOf(ZipArchiveOutputStream.class, testStream);

        // Test unknown
        testStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream("unknown", new ByteArrayOutputStream());
        assertNull(testStream);

        // Test null output stream
        try {
            ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.AR, null);
        } catch (ArchiveException e) {
            // Expected
        }
    }

    @Test
    public void testCreateArchiveInputStreamFromBytes() {
        // Test AR
        ArchiveInputStream testStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.AR, new byte[0]);
        assertNull(testStream);

        testStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.AR, new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06});
        assertInstanceOf(ArArchiveInputStream.class, testStream);

        // Test CPIO
        testStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.CPIO, new byte[0]);
        assertNull(testStream);

        testStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.CPIO, new byte[]{0x01, 0x02, 0x03});
        assertInstanceOf(CpioArchiveInputStream.class, testStream);

        // Test Dump
        testStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.DUMP, new byte[0]);
        assertNull(testStream);

        testStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.DUMP, new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07});
        assertInstanceOf(DumpArchiveInputStream.class, testStream);

        // Test JAR
        testStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.JAR, new byte[0]);
        assertNull(testStream);

        testStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.JAR, new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09});
        assertInstanceOf(JarArchiveInputStream.class, testStream);

        // Test TAR
        testStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.TAR, new byte[0]);
        assertNull(testStream);

        testStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.TAR, new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07});
        assertInstanceOf(TarArchiveInputStream.class, testStream);

        // Test ZIP
        testStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.ZIP, new byte[0]);
        assertNull(testStream);

        testStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.ZIP, new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09});
        assertInstanceOf(ZipArchiveInputStream.class, testStream);

        // Test unknown
        testStream = ArchiveStreamFactory.getInstance().createArchiveInputStream("unknown", new byte[0]);
        assertNull(testStream);

        // Test empty byte array
        testStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.AR, new byte[0]);
        assertNull(testStream);
    }

    @Test
    public void testCreateArchiveOutputStreamFromBytes() {
        // Test AR
        ArchiveOutputStream testStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.AR, new byte[0]);
        assertNull(testStream);

        testStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.AR, new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07});
        assertInstanceOf(ArArchiveOutputStream.class, testStream);

        // Test CPIO
        testStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.CPIO, new byte[0]);
        assertNull(testStream);

        testStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.CPIO, new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07});
        assertInstanceOf(CpioArchiveOutputStream.class, testStream);

        // Test Dump
        testStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.DUMP, new byte[0]);
        assertNull(testStream);

        testStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.DUMP, new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08});
        assertInstanceOf(DumpArchiveOutputStream.class, testStream);

        // Test JAR
        testStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.JAR, new byte[0]);
        assertNull(testStream);

        testStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.JAR, new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09});
        assertInstanceOf(JarArchiveOutputStream.class, testStream);

        // Test TAR
        testStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.TAR, new byte[0]);
        assertNull(testStream);

        testStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.TAR, new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07});
        assertInstanceOf(TarArchiveOutputStream.class, testStream);

        // Test ZIP
        testStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.ZIP, new byte[0]);
        assertNull(testStream);

        testStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.ZIP, new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09});
        assertInstanceOf(ZipArchiveOutputStream.class, testStream);

        // Test unknown
        testStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream("unknown", new byte[0]);
        assertNull(testStream);

        // Test empty byte array
        testStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.AR, new byte[0]);
        assertNull(testStream);
    }

    private static ArchiveStreamFactory getInstance() {
        return ArchiveStreamFactory.class.newInstance();
    }
}