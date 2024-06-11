package org.apache.commons.compress.archivers;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;

import org.apache.commons.compress.archivers.*;
import org.apache.commons.compress.archivers.ar.ARArchiveInputStream;
import org.apache.commons.compress.archivers.ar.ARArchiveOutputStream;
import org.apache.commons.compress.archivers.arj.ARJArchiveInputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveOutputStream;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Test;

import java.io.*;
import java.util.Random;

public class ArchiveStreamFactoryTest {

    private static final String ENCODING = "UTF-8";

    @Test
    public void testCreateArchiveInputStream() throws Exception {
        // Test with valid archiver name
        final InputStream inputStream = new ByteArrayInputStream("test data".getBytes());
        final String archiverName = "zip";
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(archiverName, inputStream);
        assertInstanceOf(ZipArchiveInputStream.class, archiveInputStream);

        // Test with null archiverName
        archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(null, inputStream);
        assertNull(archiveInputStream);

        // Test with null inputStream
        archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(archiverName, null);
        assertNull(archiveInputStream);

        // Test with unsupported archiverName
        final String unsupportedArchiverName = "invalid";
        archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(unsupportedArchiverName, inputStream);
        assertNull(archiveInputStream);

        // Test with AR archive
        final InputStream arInput = new FileInputStream("test/resources/samples/ar/test.ar");
        archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(AR, arInput);
        assertInstanceOf(ARArchiveInputStream.class, archiveInputStream);

        // Test with ARJ archive (not supported as an output stream type)
        final InputStream arjInput = new FileInputStream("test/resources/samples/arj/test.arj");
        archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ARJ, arjInput);
        assertInstanceOf(ARJArchiveInputStream.class, archiveInputStream);

        // Test with CPIO archive
        final InputStream cpioInput = new FileInputStream("test/resources/samples/cpio/test.cpio");
        archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(CPIO, cpioInput);
        assertInstanceOf(CpioArchiveInputStream.class, archiveInputStream);

        // Test with Dump archive (not supported as an output stream type)
        final InputStream dumpInput = new FileInputStream("test/resources/samples/dump/test.dump");
        archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(DUMP, dumpInput);
        assertInstanceOf(DumpArchiveInputStream.class, archiveInputStream);

        // Test with TAR archive
        final InputStream tarInput = new FileInputStream("test/resources/samples/tar/test.tar");
        archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(TAR, tarInput);
        assertInstanceOf(TarArchiveInputStream.class, archiveInputStream);

        // Test with JAR archive
        final InputStream jarInput = new FileInputStream("test/resources/samples/jar/test.jar");
        archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(JAR, jarInput);
        assertInstanceOf(JarArchiveInputStream.class, archiveInputStream);
    }

    @Test
    public void testCreateArchiveOutputStream() throws Exception {
        // Test with valid archiverName
        final OutputStream outputStream = new ByteArrayOutputStream();
        final String archiverName = "zip";
        ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(archiverName, outputStream);
        assertInstanceOf(ZipArchiveOutputStream.class, archiveOutputStream);

        // Test with null archiverName
        archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(null, outputStream);
        assertNull(archiveOutputStream);

        // Test with null outputStream
        archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(archiverName, null);
        assertNull(archiveOutputStream);

        // Test with unsupported archiverName
        final String unsupportedArchiverName = "invalid";
        archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(unsupportedArchiverName, outputStream);
        assertNull(archiveOutputStream);

        // Test with AR archive
        final OutputStream arOutputStream = new ByteArrayOutputStream();
        archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(AR, arOutputStream);
        assertInstanceOf(ARArchiveOutputStream.class, archiveOutputStream);

        // Test with ARJ archive (not supported as an output stream type)
        final OutputStream arjOutputStream = new ByteArrayOutputStream();
        archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ARJ, arjOutputStream);
        assertNull(archiveOutputStream);

        // Test with CPIO archive
        final OutputStream cpioOutputStream = new ByteArrayOutputStream();
        archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(CPIO, cpioOutputStream);
        assertInstanceOf(CpioArchiveOutputStream.class, archiveOutputStream);

        // Test with Dump archive (not supported as an output stream type)
        final OutputStream dumpOutputStream = new ByteArrayOutputStream();
        archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(DUMP, dumpOutputStream);
        assertNull(archiveOutputStream);

        // Test with TAR archive
        final OutputStream tarOutputStream = new ByteArrayOutputStream();
        archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(TAR, tarOutputStream);
        assertInstanceOf(TarArchiveOutputStream.class, archiveOutputStream);

        // Test with JAR archive
        final OutputStream jarOutputStream = new ByteArrayOutputStream();
        archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(JAR, jarOutputStream);
        assertInstanceOf(JarArchiveOutputStream.class, archiveOutputStream);

        // Test with SEVEN_Z archive
        final OutputStream sevenZOutputStream = new ByteArrayOutputStream();
        assertThrows(StreamingNotSupportedException.class, () -> new ArchiveStreamFactory().createArchiveOutputStream(SEVEN_Z, sevenZOutputStream));
    }

    @Test
    public void testCreateArchiveInputStreamFromInputStream() throws Exception {
        // Test with valid archiverName
        final InputStream inputStream = new FileInputStream("test/resources/samples/zip/test.zip");
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(JAR, inputStream);
        assertInstanceOf(JarArchiveInputStream.class, archiveInputStream);

        // Test with null archiverName
        archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(null, inputStream);
        assertNull(archiveInputStream);

        // Test with unsupported archiverName
        final String unsupportedArchiverName = "invalid";
        archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(unsupportedArchiverName, inputStream);
        assertNull(archiveInputStream);

        // Test with inputStream without mark support
        final InputStream inputStreamWithNoMarkSupport = new ByteArrayInputStream("test data".getBytes());
        assertThrows(IllegalArgumentException.class, () -> new ArchiveStreamFactory().createArchiveInputStream(ZIP, inputStreamWithNoMarkSupport));

        // Test with inputStream with invalid signature
        final byte[] invalidSignature = new byte[10];
        final InputStream inputStreamWithInvalidSignature = new ByteArrayInputStream(invalidSignature);
        assertThrows(ArchiveException.class, () -> new ArchiveStreamFactory().createArchiveInputStream(JAR, inputStreamWithInvalidSignature));

        // Test with inputStream with valid signature (ZIP)
        final InputStream zipInputStream = new FileInputStream("test/resources/samples/zip/test.zip");
        archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(JAR, zipInputStream);
        assertInstanceOf(JarArchiveInputStream.class, archiveInputStream);

        // Test with inputStream with valid signature (AR)
        final InputStream arInputStream = new FileInputStream("test/resources/samples/ar/test.ar");
        archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(AR, arInputStream);
        assertInstanceOf(ARArchiveInputStream.class, archiveInputStream);

        // Test with inputStream with valid signature (CPIO)
        final InputStream cpioInputStream = new FileInputStream("test/resources/samples/cpio/test.cpio");
        archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(CPIO, cpioInputStream);
        assertInstanceOf(CpioArchiveInputStream.class, archiveInputStream);

        // Test with inputStream with valid signature (TAR)
        final InputStream tarInputStream = new FileInputStream("test/resources/samples/tar/test.tar");
        archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(TAR, tarInputStream);
        assertInstanceOf(TarArchiveInputStream.class, archiveInputStream);

        // Test with inputStream with valid signature (JAR)
        final InputStream jarInputStream = new FileInputStream("test/resources/samples/jar/test.jar");
        archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(JAR, jarInputStream);
        assertInstanceOf(JarArchiveInputStream.class, archiveInputStream);

        // Test with inputStream with valid signature (Dump)
        final InputStream dumpInputStream = new FileInputStream("test/resources/samples/dump/test.dump");
        archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(DUMP, dumpInputStream);
        assertInstanceOf(DumpArchiveInputStream.class, archiveInputStream);

        // Test with inputStream with valid signature (SevenZ)
        final String sevenZFileName = "test/resources/samples/7z/test.7z";
        final InputStream sevenZInputStream = new FileInputStream(sevenZFileName);
        assertThrows(StreamingNotSupportedException.class, () -> new ArchiveStreamFactory().createArchiveInputStream(SEVEN_Z, sevenZInputStream));
    }

    @Test
    public void testCreateArchiveOutputStream() throws Exception {
        // Test with valid archiverName
        final OutputStream outputStream = new ByteArrayOutputStream();
        final String archiverName = "zip";
        ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(archiverName, outputStream);
        assertInstanceOf(ZipArchiveOutputStream.class, archiveOutputStream);

        // Test with null archiverName
        archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(null, outputStream);
        assertNull(archiveOutputStream);

        // Test with unsupported archiverName
        final String unsupportedArchiverName = "invalid";
        archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(unsupportedArchiverName, outputStream);
        assertNull(archiveOutputStream);

        // Test with outputStream without write support
        final OutputStream outputStreamWithNoWriteSupport = new ByteArrayInputStream("test data".getBytes());
        assertThrows(IllegalArgumentException.class, () -> new ArchiveStreamFactory().createArchiveOutputStream(ZIP, outputStreamWithNoWriteSupport));

        // Test with outputStream without write permission
        final OutputStream outputStreamWithNoWritePermission = new ByteArrayOutputStream() {
            @Override
            public void write(int b) throws IOException {
                throw new IOException("Write operation is not allowed.");
            }
        };
        assertThrows(IOException.class, () -> new ArchiveStreamFactory().createArchiveOutputStream(JAR, outputStreamWithNoWritePermission));

        // Test with outputStream with valid signature (ZIP)
        final OutputStream zipOutputStream = new ByteArrayOutputStream();
        archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(JAR, zipOutputStream);
        assertInstanceOf(JarArchiveOutputStream.class, archiveOutputStream);

        // Test with outputStream with valid signature (AR)
        final OutputStream arOutputStream = new ByteArrayOutputStream();
        archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(AR, arOutputStream);
        assertInstanceOf(ARArchiveOutputStream.class, archiveOutputStream);

        // Test with outputStream with valid signature (CPIO)
        final OutputStream cpioOutputStream = new ByteArrayOutputStream();
        archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(CPIO, cpioOutputStream);
        assertInstanceOf(CpioArchiveOutputStream.class, archiveOutputStream);

        // Test with outputStream with valid signature (TAR)
        final OutputStream tarOutputStream = new ByteArrayOutputStream();
        archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(TAR, tarOutputStream);
        assertInstanceOf(TarArchiveOutputStream.class, archiveOutputStream);

        // Test with outputStream with valid signature (JAR)
        final OutputStream jarOutputStream = new ByteArrayOutputStream();
        archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(JAR, jarOutputStream);
        assertInstanceOf(JarArchiveOutputStream.class, archiveOutputStream);

        // Test with outputStream with valid signature (SevenZ)
        final String sevenZFileName = "test/resources/samples/7z/test.7z";
        final OutputStream sevenZOutputStream = new FileOutputStream(sevenZFileName);
        assertThrows(StreamingNotSupportedException.class, () -> new ArchiveStreamFactory().createArchiveOutputStream(SEVEN_Z, sevenZOutputStream));
    }

    private void assertThrows(Class<? extends Throwable> expectedThrowableClass, Runnable runnable) {
        try {
            runnable.run();
            fail("Expected " + expectedThrowableClass.getName() + " not thrown.");
        } catch (Throwable throwable) {
            if (expectedThrowableClass.isInstance(throwable)) {
                // Pass
            } else {
                fail("Unexpected " + throwable.getClass().getName() + " thrown instead of " + expectedThrowableClass.getName() + ".");
            }
        }
    }
}