package org.apache.commons.compress.archivers;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ar.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.arj.ArjArchiveInputStream;
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

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() throws Exception {
        for (String archiverName : new String[]{"AR", "ARJ", "CPIO", "DUMP", "JAR", "TAR", "ZIP", "SEVEN_Z"}) {
           try (InputStream in = new ByteArrayInputStream(createTestData(archiverName).getBytes());
                ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(archiverName, in)) {
                assertNotNull(archiveInputStream);
                archiveInputStream.close();
            }
        }

        for (String archiverName : new String[]{"AR", "ARJ", "CPIO", "DUMP", "JAR", "TAR", "ZIP", "SEVEN_Z"}) {
            try (InputStream in = new ByteArrayInputStream(createTestData("unknown").getBytes());
                 ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(archiverName, in)) {
                assertThrows(ArchiveException.class, () -> archiveInputStream.getNextEntry());
                archiveInputStream.close();
            }
        }

        try (InputStream in = new ByteArrayInputStream(new byte[0]);
             ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream("AR", in)) {
            assertThrows(IOException.class, () -> archiveInputStream.getNextEntry());
            archiveInputStream.close();
        }

        try (InputStream in = new ByteArrayInputStream(new byte[0]);
             ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream("unknown", in)) {
            assertThrows(ArchiveException.class, () -> archiveInputStream.getNextEntry());
            archiveInputStream.close();
        }
    }

    @Test
    public void testCreateArchiveOutputStream() throws Exception {
        for (String archiverName : new String[]{"AR", "ZIP", "TAR", "JAR", "CPIO"}) {
            try (OutputStream out = new ByteArrayOutputStream();
                 ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(archiverName, out)) {
                assertNotNull(archiveOutputStream);
                archiveOutputStream.finish();
                out.close();
            }
        }

        try (OutputStream out = new ByteArrayOutputStream();
             ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream("ARJ", out)) {
            assertThrows(StreamingNotSupportedException.class, () -> archiveOutputStream.finish());
            out.close();
        }

        try (OutputStream out = new ByteArrayOutputStream();
             ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream("SEVEN_Z", out)) {
            assertThrows(StreamingNotSupportedException.class, () -> archiveOutputStream.finish());
            out.close();
        }

        try (OutputStream out = new ByteArrayOutputStream();
             ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream("unknown", out)) {
            assertThrows(ArchiveException.class, () -> archiveOutputStream.finish());
            out.close();
        }
    }

    @Test
    public void testCreateArchiveInputStreamWithUnknownFormat() throws Exception {
        try (InputStream in = new ByteArrayInputStream(new byte[0]);
             ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream("unknown", in)) {
            assertThrows(ArchiveException.class, () -> archiveInputStream.getNextEntry());
            archiveInputStream.close();
        }
    }

    @Test
    public void testCreateArchiveOutputStreamWithNullOutputStream() {
        try {
            new ArchiveStreamFactory().createArchiveOutputStream("AR", null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    private static byte[] createTestData(String archiverName) {
        switch (archiverName) {
            case "AR":
                return AR_TEST_DATA;
            case "ARJ":
                return ARJ_TEST_DATA;
            case "CPIO":
                return CPIO_TEST_DATA;
            case "DUMP":
                return DUMP_TEST_DATA;
            case "JAR":
                return JAR_TEST_DATA;
            case "TAR":
                return TAR_TEST_DATA;
            case "ZIP":
                return ZIP_TEST_DATA;
            case "SEVEN_Z":
                return new byte[0]; // SevenZFile is not supported as an input stream
            default:
                throw new IllegalArgumentException("Unsupported archiverName: " + archiverName);
        }
    }

    private static final byte[] AR_TEST_DATA = "!<arch>\n" +
            "  Type: unknown\n" +
            "  Size: 0\n" +
            "Name: test.txt\n" +
            "  Type: unknown\n" +
            "  Size: 10\n" +
            "  Data:\n" +
            "Hallo\n" +
            "!<\n";

    private static final byte[] ARJ_TEST_DATA = new byte[0]; // Not supported as an input stream

    private static final byte[] CPIO_TEST_DATA = "0700100401\n" +
            "1 test.txt\n" +
            "0600100401\n" +
            "Hallo\n";

    private static final byte[] DUMP_TEST_DATA = new byte[0]; // Not supported as an input stream

    private static final byte[] JAR_TEST_DATA = "  META-INF/\n" +
            "  META-INF/MANIFEST.MF\n" +
            "Manifest-Version: 1.0\n" +
            "Created-By: Apache Commons Compress (Fabrice Daugan)\n" +
            "Build-Jdk: 1.8.0_242\n" +
            "Main-Class: org.apache.commons.compress.utils.TestFiles\n" +
            "\n" +
            "test.txt\n" +
            "Hallo\n";

    private static final byte[] TAR_TEST_DATA = "0  1 test.txt\n" +
            "0000000000 1970-01-01 00:00:00 test.txt\n" +
            "19700101000001 1970-01-01 00:00:00 Hallo\n";

    private static final byte[] ZIP_TEST_DATA = "pk\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0" +
            "\0\0\0\0test.txt\0\0\0\0Hallo\0\0\0\0test.txt\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0" +
            "\0\0\0\0Hallo\0\0\0\0";
}