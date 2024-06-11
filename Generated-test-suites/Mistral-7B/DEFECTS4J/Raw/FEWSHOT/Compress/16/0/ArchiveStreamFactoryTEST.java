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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() throws IOException {
        // Test AR archive
        InputStream is = new ByteArrayInputStream(TestUtils.getArHeaderBytes());
        ArchiveInputStream ais = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.AR, is);
        assertInstanceOf(ArArchiveInputStream.class, ais);

        // Test ZIP archive
        is = new ByteArrayInputStream(TestUtils.getZipHeaderBytes());
        ais = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.ZIP, is);
        assertInstanceOf(ZipArchiveInputStream.class, ais);

        // Test JAR archive
        is = new ByteArrayInputStream(TestUtils.getJarHeaderBytes());
        ais = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.JAR, is);
        assertInstanceOf(JarArchiveInputStream.class, ais);

        // Test TAR archive
        is = new ByteArrayInputStream(TestUtils.getTarHeaderBytes());
        ais = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.TAR, is);
        assertInstanceOf(TarArchiveInputStream.class, ais);

        // Test CPIO archive
        is = new ByteArrayInputStream(TestUtils.getCpioHeaderBytes());
        ais = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.CPIO, is);
        assertInstanceOf(CpioArchiveInputStream.class, ais);

        // Test DUMP archive
        is = new ByteArrayInputStream(TestUtils.getDumpHeaderBytes());
        ais = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.DUMP, is);
        assertInstanceOf(DumpArchiveInputStream.class, ais);

        // Test unknown archive
        InputStream unknown = new ByteArrayInputStream("unknown archive".getBytes());
        assertNull(ArchiveStreamFactory.getInstance().createArchiveInputStream("unknown", unknown));
    }

    @Test
    public void testCreateArchiveOutputStream() throws IOException {
        // Test AR archive
        OutputStream os = new ByteArrayOutputStream();
        ArchiveOutputStream aos = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.AR, os);
        assertInstanceOf(ArArchiveOutputStream.class, aos);

        // Test ZIP archive
        os = new ByteArrayOutputStream();
        aos = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.ZIP, os);
        assertInstanceOf(ZipArchiveOutputStream.class, aos);

        // Test JAR archive
        os = new ByteArrayOutputStream();
        aos = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.JAR, os);
        assertInstanceOf(JarArchiveOutputStream.class, aos);

        // Test TAR archive
        os = new ByteArrayOutputStream();
        aos = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.TAR, os);
        assertInstanceOf(TarArchiveOutputStream.class, aos);

        // Test CPIO archive
        os = new ByteArrayOutputStream();
        aos = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.CPIO, os);
        assertInstanceOf(CpioArchiveOutputStream.class, aos);

        // Test unknown archive
        OutputStream unknown = new ByteArrayOutputStream();
        assertNull(ArchiveStreamFactory.getInstance().createArchiveOutputStream("unknown", unknown));
    }
}