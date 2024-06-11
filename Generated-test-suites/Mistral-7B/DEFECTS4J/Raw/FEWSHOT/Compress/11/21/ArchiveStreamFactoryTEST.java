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
        byte[] arSignature = new byte[12];
        byte[] tarHeader = new byte[512];

        // Test AR archive
        ArchiveInputStream in = ArchiveStreamFactory.newInstance().createArchiveInputStream(ArchiveStreamFactory.AR, new ByteArrayInputStream(arSignature));
        assertInstanceOf(ArArchiveInputStream.class, in);

        // Test TAR archive
        in = ArchiveStreamFactory.newInstance().createArchiveInputStream(ArchiveStreamFactory.TAR, new ByteArrayInputStream(tarHeader));
        assertInstanceOf(TarArchiveInputStream.class, in);

        // Test ZIP archive
        InputStream is = new ByteArrayInputStream(TestUtils.getZipSignature());
        in = ArchiveStreamFactory.newInstance().createArchiveInputStream(ArchiveStreamFactory.ZIP, is);
        assertInstanceOf(ZipArchiveInputStream.class, in);

        // Test JAR archive
        in = ArchiveStreamFactory.newInstance().createArchiveInputStream(ArchiveStreamFactory.JAR, is);
        assertInstanceOf(JarArchiveInputStream.class, in);

        // Test CPIO archive
        in = ArchiveStreamFactory.newInstance().createArchiveInputStream(ArchiveStreamFactory.CPIO, is);
        assertInstanceOf(CpioArchiveInputStream.class, in);

        // Test DUMP archive
        in = ArchiveStreamFactory.newInstance().createArchiveInputStream(ArchiveStreamFactory.DUMP, is);
        assertInstanceOf(DumpArchiveInputStream.class, in);

        // Test unknown archive
        in = ArchiveStreamFactory.newInstance().createArchiveInputStream("unknown", is);
        assertNull(in);

        // Test null archiver name
        try {
            ArchiveStreamFactory.newInstance().createArchiveInputStream(null, is);
        } catch (ArchiveException e) {
            // expected
        }

        // Test null input stream
        try {
            ArchiveStreamFactory.newInstance().createArchiveInputStream(ArchiveStreamFactory.AR, null);
        } catch (ArchiveException e) {
            // expected
        }
    }

    @Test
    public void testCreateArchiveOutputStream() throws Exception {
        // Test AR archive
        OutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream os = ArchiveStreamFactory.newInstance()
                .createArchiveOutputStream(ArchiveStreamFactory.AR, out);
        assertInstanceOf(ArArchiveOutputStream.class, os);

        // Test TAR archive
        out = new ByteArrayOutputStream();
        os = ArchiveStreamFactory.newInstance()
                .createArchiveOutputStream(ArchiveStreamFactory.TAR, out);
        assertInstanceOf(TarArchiveOutputStream.class, os);

        // Test ZIP archive
        out = new ByteArrayOutputStream();
        os = ArchiveStreamFactory.newInstance()
                .createArchiveOutputStream(ArchiveStreamFactory.ZIP, out);
        assertInstanceOf(ZipArchiveOutputStream.class, os);

        // Test JAR archive
        out = new ByteArrayOutputStream();
        os = ArchiveStreamFactory.newInstance()
                .createArchiveOutputStream(ArchiveStreamFactory.JAR, out);
        assertInstanceOf(JarArchiveOutputStream.class, os);

        // Test CPIO archive
        out = new ByteArrayOutputStream();
        os = ArchiveStreamFactory.newInstance()
                .createArchiveOutputStream(ArchiveStreamFactory.CPIO, out);
        assertInstanceOf(CpioArchiveOutputStream.class, os);

        // Test DUMP archive
        out = new ByteArrayOutputStream();
        os = ArchiveStreamFactory.newInstance()
                .createArchiveOutputStream(ArchiveStreamFactory.DUMP, out);
        assertInstanceOf(DumpArchiveOutputStream.class, os);

        // Test unknown archive
        try {
            ArchiveStreamFactory.newInstance()
                    .createArchiveOutputStream("unknown", new ByteArrayOutputStream());
        } catch (ArchiveException e) {
            // expected
        }

        // Test null archiver name
        try {
            ArchiveStreamFactory.newInstance()
                    .createArchiveOutputStream(null, new ByteArrayOutputStream());
        } catch (ArchiveException e) {
            // expected
        }

        // Test null output stream
        try {
            ArchiveStreamFactory.newInstance()
                    .createArchiveOutputStream(ArchiveStreamFactory.AR, null);
        } catch (ArchiveException e) {
            // expected
        }
    }
}