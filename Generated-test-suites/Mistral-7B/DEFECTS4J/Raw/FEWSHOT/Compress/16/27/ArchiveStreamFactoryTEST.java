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
        String[] testCases = {
                //AR
                "ar", new ByteArrayInputStream(ArchiveStreamFactory.AR_SIGNATURE),
                //CPIO
                "cpio", new ByteArrayInputStream(ArchiveStreamFactory.CPIO_SIGNATURE),
                //Dump
                "dump", new ByteArrayInputStream(ArchiveStreamFactory.DUMP_SIGNATURE),
                //JAR
                "jar", new ByteArrayInputStream(ArchiveStreamFactory.JAR_SIGNATURE),
                //TAR
                "tar", new ByteArrayInputStream(ArchiveStreamFactory.TAR_SIGNATURE),
                //ZIP
                "zip", new ByteArrayInputStream(ArchiveStreamFactory.ZIP_SIGNATURE),
                //Invalid Archiver
                "invalid", new ByteArrayInputStream(new byte[10])
        };

        for (String[] testCase : testCases) {
            String archiverName = testCase[0];
            InputStream inputStream = testCase[1];
            ArchiveInputStream archiveInputStream = ArchiveStreamFactory.INSTANCE.createArchiveInputStream(archiverName, inputStream);
            if (archiverName.equalsIgnoreCase("ar")) {
                assertInstanceOf(ArArchiveInputStream.class, archiveInputStream);
            } else if (archiverName.equalsIgnoreCase("cpio")) {
                assertInstanceOf(CpioArchiveInputStream.class, archiveInputStream);
            } else if (archiverName.equalsIgnoreCase("dump")) {
                assertInstanceOf(DumpArchiveInputStream.class, archiveInputStream);
            } else if (archiverName.equalsIgnoreCase("jar")) {
                assertInstanceOf(JarArchiveInputStream.class, archiveInputStream);
            } else if (archiverName.equalsIgnoreCase("tar")) {
                assertInstanceOf(TarArchiveInputStream.class, archiveInputStream);
            } else if (archiverName.equalsIgnoreCase("zip")) {
                assertInstanceOf(ZipArchiveInputStream.class, archiveInputStream);
            } else {
                assertNull(archiveInputStream);
            }
        }
    }

    @Test
    public void testCreateArchiveOutputStream() throws Exception {
        String[] testCases = {
                //AR
                "ar", new ByteArrayInputStream(ArchiveStreamFactory.AR_SIGNATURE),
                //CPIO
                "cpio", new ByteArrayInputStream(ArchiveStreamFactory.CPIO_SIGNATURE),
                //Dump
                "dump", new ByteArrayInputStream(ArchiveStreamFactory.DUMP_SIGNATURE),
                //JAR
                "jar", new ByteArrayInputStream(ArchiveStreamFactory.JAR_SIGNATURE),
                //TAR
                "tar", new ByteArrayInputStream(ArchiveStreamFactory.TAR_SIGNATURE),
                //ZIP
                "zip", new ByteArrayInputStream(ArchiveStreamFactory.ZIP_SIGNATURE),
                //Invalid Archiver
                "invalid", new ByteArrayInputStream(new byte[10])
        };

        for (String[] testCase : testCases) {
            String archiverName = testCase[0];
            InputStream inputStream = testCase[1];
            OutputStream outputStream = new ByteArrayOutputStream();
            ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.INSTANCE.createArchiveOutputStream(archiverName, outputStream);
            if (archiverName.equalsIgnoreCase("ar")) {
                assertInstanceOf(ArArchiveOutputStream.class, archiveOutputStream);
            } else if (archiverName.equalsIgnoreCase("cpio")) {
                assertInstanceOf(CpioArchiveOutputStream.class, archiveOutputStream);
            } else if (archiverName.equalsIgnoreCase("dump")) {
                assertInstanceOf(DumpArchiveOutputStream.class, archiveOutputStream);
            } else if (archiverName.equalsIgnoreCase("jar")) {
                assertInstanceOf(JarArchiveOutputStream.class, archiveOutputStream);
            } else if (archiverName.equalsIgnoreCase("tar")) {
                assertInstanceOf(TarArchiveOutputStream.class, archiveOutputStream);
            } else if (archiverName.equalsIgnoreCase("zip")) {
                assertInstanceOf(ZipArchiveOutputStream.class, archiveOutputStream);
            } else {
                assertNull(archiveOutputStream);
            }
        }
    }
}