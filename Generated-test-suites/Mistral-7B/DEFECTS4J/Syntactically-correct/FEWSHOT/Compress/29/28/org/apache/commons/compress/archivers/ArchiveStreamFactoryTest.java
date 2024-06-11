package org.apache.commons.compress.archivers;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;

import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ar.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.arj.ArjArchiveInputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveEntry;
import org.apache.commons.compress.archivers.jar.JarArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() throws Exception {
        // Test AR archive
        InputStream arInputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory()
                .createArchiveInputStream(ArchiveStreamFactory.AR, arInputStream);
        assertInstanceOf(ArArchiveInputStream.class, archiveInputStream);

        // Test ARJ archive
        InputStream arjInputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream arjArchiveInputStream = new ArchiveStreamFactory()
                .createArchiveInputStream(ArchiveStreamFactory.ARJ, arjInputStream);
        assertInstanceOf(ArjArchiveInputStream.class, arjArchiveInputStream);

        // Test CPIO archive
        InputStream cpioInputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream cpioArchiveInputStream = new ArchiveStreamFactory()
                .createArchiveInputStream(ArchiveStreamFactory.CPIO, cpioInputStream);
        assertInstanceOf(CpioArchiveInputStream.class, cpioArchiveInputStream);

        // Test Dump archive
        InputStream dumpInputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream dumpArchiveInputStream = new ArchiveStreamFactory()
                .createArchiveInputStream(ArchiveStreamFactory.DUMP, dumpInputStream);
        assertInstanceOf(DumpArchiveInputStream.class, dumpArchiveInputStream);

        // Test JAR archive
        InputStream jarInputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream jarArchiveInputStream = new ArchiveStreamFactory()
                .createArchiveInputStream(ArchiveStreamFactory.JAR, jarInputStream);
        assertInstanceOf(JarArchiveInputStream.class, jarArchiveInputStream);

        JarArchiveEntry jarEntry = jarArchiveInputStream.getNextJarEntry();
        assertNull(jarEntry);

        // Test TAR archive
        InputStream tarInputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream tarArchiveInputStream = new ArchiveStreamFactory()
                .createArchiveInputStream(ArchiveStreamFactory.TAR, tarInputStream);
        assertInstanceOf(TarArchiveInputStream.class, tarArchiveInputStream);

        TarArchiveEntry tarEntry = tarArchiveInputStream.getNextEntry();
        assertNull(tarEntry);

        // Test ZIP archive
        InputStream zipInputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream zipArchiveInputStream = new ArchiveStreamFactory()
                .createArchiveInputStream(ArchiveStreamFactory.ZIP, zipInputStream);
        assertInstanceOf(ZipArchiveInputStream.class, zipArchiveInputStream);

        ZipArchiveEntry zipEntry = (ZipArchiveEntry) zipArchiveInputStream.getNextEntry();
        assertNull(zipEntry);

        // Test SEVEN_Z archive
        InputStream sevenZInputStream = new ByteArrayInputStream(new byte[0]);
        try {
            new ArchiveStreamFactory().createArchiveInputStream(
                    ArchiveStreamFactory.SEVEN_Z, sevenZInputStream);
            // This should not reach here
            assertFalse(true);
        } catch (StreamingNotSupportedException e) {
            // expected
        }
    }

    @Test
    public void testCreateArchiveOutputStream() throws Exception {
        // Test AR archive
        OutputStream arOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory()
                .createArchiveOutputStream(ArchiveStreamFactory.AR, arOutputStream);
        assertInstanceOf(ArArchiveOutputStream.class, archiveOutputStream);

        // Test ARJ archive
        OutputStream arjOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream arjArchiveOutputStream = new ArchiveStreamFactory()
                .createArchiveOutputStream(ArchiveStreamFactory.ARJ, arjOutputStream);
        assertInstanceOf(ArjArchiveOutputStream.class, arjArchiveOutputStream);

        // Test CPIO archive
        OutputStream cpioOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream cpioArchiveOutputStream = new ArchiveStreamFactory()
                .createArchiveOutputStream(ArchiveStreamFactory.CPIO, cpioOutputStream);
        assertInstanceOf(CpioArchiveOutputStream.class, cpioArchiveOutputStream);

        // Test JAR archive
        OutputStream jarOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream jarArchiveOutputStream = new ArchiveStreamFactory()
                .createArchiveOutputStream(ArchiveStreamFactory.JAR, jarOutputStream);
        assertInstanceOf(JarArchiveOutputStream.class, jarArchiveOutputStream);

        // Test TAR archive
        OutputStream tarOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream tarArchiveOutputStream = new ArchiveStreamFactory()
                .createArchiveOutputStream(ArchiveStreamFactory.TAR, tarOutputStream);
        assertInstanceOf(TarArchiveOutputStream.class, tarArchiveOutputStream);

        // Test ZIP archive
        OutputStream zipOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream zipArchiveOutputStream = new ArchiveStreamFactory()
                .createArchiveOutputStream(ArchiveStreamFactory.ZIP, zipOutputStream);
        assertInstanceOf(ZipArchiveOutputStream.class, zipArchiveOutputStream);

        // Test SEVEN_Z archive
        OutputStream sevenZOutputStream = new ByteArrayOutputStream();
        try {
            new ArchiveStreamFactory().createArchiveOutputStream(
                    ArchiveStreamFactory.SEVEN_Z, sevenZOutputStream);
            // This should not reach here
            assertFalse(true);
        } catch (StreamingNotSupportedException e) {
            // expected
        }
    }
}