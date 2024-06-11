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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() throws Exception {
        for (String archiverName : ArchiveStreamFactory.values()) {
            InputStream inputStream = null;
            if (archiverName.equalsIgnoreCase(ArchiveStreamFactory.AR.name())) {
                inputStream = new ByteArrayInputStream(new byte[0]);
            } else if (archiverName.equalsIgnoreCase(ArchiveStreamFactory.CPIO.name())) {
                inputStream = new ByteArrayInputStream(new byte[0]);
            } else if (archiverName.equalsIgnoreCase(ArchiveStreamFactory.DUMP.name())) {
                inputStream = new ByteArrayInputStream(new byte[0]);
            } else if (archiverName.equalsIgnoreCase(ArchiveStreamFactory.JAR.name())) {
                inputStream = new ByteArrayInputStream(new byte[0]);
            } else if (archiverName.equalsIgnoreCase(ArchiveStreamFactory.TAR.name())) {
                inputStream = new ByteArrayInputStream(new byte[0]);
            } else if (archiverName.equalsIgnoreCase(ArchiveStreamFactory.ZIP.name())) {
                inputStream = new ByteArrayInputStream(new byte[0]);
            }

            ArchiveInputStream archiveInputStream = ArchiveStreamFactory.createArchiveInputStream(archiverName, inputStream);
            if (archiverName.equalsIgnoreCase(ArchiveStreamFactory.AR.name())) {
                assertInstanceOf(ArArchiveInputStream.class, archiveInputStream);
            } else if (archiverName.equalsIgnoreCase(ArchiveStreamFactory.CPIO.name())) {
                assertInstanceOf(CpioArchiveInputStream.class, archiveInputStream);
            } else if (archiverName.equalsIgnoreCase(ArchiveStreamFactory.DUMP.name())) {
                assertInstanceOf(DumpArchiveInputStream.class, archiveInputStream);
            } else if (archiverName.equalsIgnoreCase(ArchiveStreamFactory.JAR.name())) {
                assertInstanceOf(JarArchiveInputStream.class, archiveInputStream);
            } else if (archiverName.equalsIgnoreCase(ArchiveStreamFactory.TAR.name())) {
                assertInstanceOf(TarArchiveInputStream.class, archiveInputStream);
            } else if (archiverName.equalsIgnoreCase(ArchiveStreamFactory.ZIP.name())) {
                assertInstanceOf(ZipArchiveInputStream.class, archiveInputStream);
            } else {
                assertNull(archiveInputStream);
            }
        }
    }

    @Test
    public void testCreateArchiveOutputStream() throws Exception {
        for (String archiverName : ArchiveStreamFactory.values()) {
            OutputStream outputStream = new ByteArrayOutputStream();
            ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.createArchiveOutputStream(archiverName, outputStream);
            if (archiverName.equalsIgnoreCase(ArchiveStreamFactory.AR.name())) {
                assertInstanceOf(ArArchiveOutputStream.class, archiveOutputStream);
            } else if (archiverName.equalsIgnoreCase(ArchiveStreamFactory.CPIO.name())) {
                assertInstanceOf(CpioArchiveOutputStream.class, archiveOutputStream);
            } else if (archiverName.equalsIgnoreCase(ArchiveStreamFactory.DUMP.name())) {
                assertInstanceOf(DumpArchiveOutputStream.class, archiveOutputStream);
            } else if (archiverName.equalsIgnoreCase(ArchiveStreamFactory.JAR.name())) {
                assertInstanceOf(JarArchiveOutputStream.class, archiveOutputStream);
            } else if (archiverName.equalsIgnoreCase(ArchiveStreamFactory.TAR.name())) {
                assertInstanceOf(TarArchiveOutputStream.class, archiveOutputStream);
            } else if (archiverName.equalsIgnoreCase(ArchiveStreamFactory.ZIP.name())) {
                assertInstanceOf(ZipArchiveOutputStream.class, archiveOutputStream);
            } else {
                assertNull(archiveOutputStream);
            }
        }
    }

    @Test
    public void testCreateArchiveInputStreamFromBytes() throws IOException, ArchiveException {
        for (String archiverName : ArchiveStreamFactory.values()) {
            byte[] inputBytes = new byte[0];
            ArchiveInputStream archiveInputStream = ArchiveStreamFactory.createArchiveInputStream(archiverName, new ByteArrayInputStream(inputBytes));
            if (archiverName.equalsIgnoreCase(ArchiveStreamFactory.AR.name())) {
                assertInstanceOf(ArArchiveInputStream.class, archiveInputStream);
            } else if (archiverName.equalsIgnoreCase(ArchiveStreamFactory.CPIO.name())) {
                assertInstanceOf(CpioArchiveInputStream.class, archiveInputStream);
            } else if (archiverName.equalsIgnoreCase(ArchiveStreamFactory.DUMP.name())) {
                assertInstanceOf(DumpArchiveInputStream.class, archiveInputStream);
            } else if (archiverName.equalsIgnoreCase(ArchiveStreamFactory.JAR.name())) {
                assertInstanceOf(JarArchiveInputStream.class, archiveInputStream);
            } else if (archiverName.equalsIgnoreCase(ArchiveStreamFactory.TAR.name())) {
                assertInstanceOf(TarArchiveInputStream.class, archiveInputStream);
            } else if (archiverName.equalsIgnoreCase(ArchiveStreamFactory.ZIP.name())) {
                assertInstanceOf(ZipArchiveInputStream.class, archiveInputStream);
            } else {
                assertNull(archiveInputStream);
            }
        }
    }
}