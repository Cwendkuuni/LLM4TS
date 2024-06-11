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
        for (String archiverName : ArchiveStreamFactory.values()) {
            InputStream inputStream = new ByteArrayInputStream(getBytesForArchive(archiverName));
            ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(archiverName, inputStream);
            assertInstanceOf(getArchiveClassForName(archiverName), archiveInputStream.getClass());
            archiveInputStream.close();
        }
    }

    @Test
    public void testCreateArchiveOutputStream() throws Exception {
        for (String archiverName : ArchiveStreamFactory.values()) {
            OutputStream outputStream = new ByteArrayOutputStream();
            ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(archiverName, outputStream);
            assertInstanceOf(getArchiveClassForName("output." + archiverName), archiveOutputStream.getClass());
            archiveOutputStream.finish();
            assertNull(archiveOutputStream.getNextEntry());
            archiveOutputStream.close();
        }
    }

    @Test
    public void testCreateArchiveInputStreamFromUnknownName() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(getBytesForArchive("unknown"));
        try {
            ArchiveStreamFactory.getInstance().createArchiveInputStream("unknown", inputStream);
            fail("Expected ArchiveException");
        } catch (ArchiveException e) {
            // Expected
        }
    }

    @Test
    public void testCreateArchiveOutputStreamFromNullOutput() {
        try {
            ArchiveStreamFactory.getInstance().createArchiveOutputStream(null, new ByteArrayOutputStream());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testCreateArchiveOutputStreamFromUnknownOutput() {
        try {
            ArchiveStreamFactory.getInstance().createArchiveOutputStream("unknown", new ByteArrayOutputStream());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    private byte[] getBytesForArchive(String archiverName) {
        switch (archiverName) {
            case ArchiveStreamFactory.AR:
                return ArArchiveTest.AR_TEST_DATA;
            case ArchiveStreamFactory.CPIO:
                return CpioArchiveTest.CPIO_TEST_DATA;
            case ArchiveStreamFactory.DUMP:
                return DumpArchiveTest.DUMP_TEST_DATA;
            case ArchiveStreamFactory.JAR:
                return JarArchiveTest.JAR_TEST_DATA;
            case ArchiveStreamFactory.TAR:
                return TarArchiveTest.TAR_TEST_DATA;
            case ArchiveStreamFactory.ZIP:
                return ZipArchiveTest.ZIP_TEST_DATA;
            default:
                throw new IllegalArgumentException("Unexpected archiver name: " + archiverName);
        }
    }

    private Class<?> getArchiveClassForName(String name) {
        try {
            return Class.forName(name.startsWith("output.") ? "org.apache.commons.compress.archivers.output." + name.substring(6)
                    : name);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Unexpected archive class name: " + name, e);
        }
    }
}