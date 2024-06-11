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
import org.apache.commons.compress.archivers.test.TestUtilities;
import org.junit.Test;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() {
        // Test with valid archiver names
        testCreateArchiveInputStream("ar", new byte[0][], null);
        testCreateArchiveInputStream("zip", new byte[0][], null);
        testCreateArchiveInputStream("tar", new byte[0][], null);
        testCreateArchiveInputStream("jar", new byte[0][], null);
        testCreateArchiveInputStream("cpio", new byte[0][], null);
        testCreateArchiveInputStream("dump", new byte[0][], null);

        // Test with null input stream
        testCreateArchiveInputStreamNull("ar");
        testCreateArchiveInputStreamNull("zip");
        testCreateArchiveInputStreamNull("tar");
        testCreateArchiveInputStreamNull("jar");
        testCreateArchiveInputStreamNull("cpio");
        testCreateArchiveInputStreamNull("dump");

        // Test with empty byte array input stream
        testCreateArchiveInputStreamEmpty("ar");
        testCreateArchiveInputStreamEmpty("zip");
        testCreateArchiveInputStreamEmpty("tar");
        testCreateArchiveInputStreamEmpty("jar");
        testCreateArchiveInputStreamEmpty("cpio");
        testCreateArchiveInputStreamEmpty("dump");

        // Test with invalid archiver name
        testCreateArchiveInputStreamInvalid("x-archiver");
    }

    @Test
    public void testCreateArchiveOutputStream() {
        // Test with valid archiver names
        testCreateArchiveOutputStream("ar", TestUtilities.createOutputByteArrayStream());
        testCreateArchiveOutputStream("zip", TestUtilities.createOutputByteArrayStream());
        testCreateArchiveOutputStream("tar", TestUtilities.createOutputByteArrayStream());
        testCreateArchiveOutputStream("jar", TestUtilities.createOutputByteArrayStream());
        testCreateArchiveOutputStream("cpio", TestUtilities.createOutputByteArrayStream());
        testCreateArchiveOutputStream("dump", TestUtilities.createOutputByteArrayStream());

        // Test with null output stream
        testCreateArchiveOutputStreamNull("ar");
        testCreateArchiveOutputStreamNull("zip");
        testCreateArchiveOutputStreamNull("tar");
        testCreateArchiveOutputStreamNull("jar");
        testCreateArchiveOutputStreamNull("cpio");
        testCreateArchiveOutputStreamNull("dump");
    }

    @Test
    public void testCreateArchiveInputStreamFromStream() {
        // Test with valid archiver names
        testCreateArchiveInputStreamFromStream("ar", TestUtilities.createTestFile("test-archive.ar"));
        testCreateArchiveInputStreamFromStream("zip", TestUtilities.createTestFile("test-archive.zip"));
        testCreateArchiveInputStreamFromStream("tar", TestUtilities.createTestFile("test-archive.tar"));
        testCreateArchiveInputStreamFromStream("jar", TestUtilities.createTestFile("test-archive.jar"));
        testCreateArchiveInputStreamFromStream("cpio", TestUtilities.createTestFile("test-archive.cpio"));
        testCreateArchiveInputStreamFromStream("dump", TestUtilities.createTestFile("test-archive.dump"));

        // Test with null input stream
        testCreateArchiveInputStreamFromStreamNull();
    }

    private void testCreateArchiveInputStream(String archiverName, byte[] signature) {
        try {
            ArchiveInputStream in = new ArchiveStreamFactory().createArchiveInputStream(archiverName, new ByteArrayInputStream(signature));
            assertInstanceOf(getArchiveInputStreamClass(archiverName), in);
        } catch (ArchiveException e) {
            e.printStackTrace();
            throw new RuntimeException("Test failed: " + e.getMessage());
        }
    }

    private void testCreateArchiveInputStreamNull(String archiverName) {
        try {
            new ArchiveStreamFactory().createArchiveInputStream(archiverName, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    private void testCreateArchiveInputStreamEmpty(String archiverName) {
        try {
            new ArchiveStreamFactory().createArchiveInputStream(archiverName, new byte[0][]);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    private void testCreateArchiveInputStreamInvalid(String archiverName) {
        try {
            new ArchiveStreamFactory().createArchiveInputStream(archiverName, new byte[0][]);
            fail("Expected ArchiveException");
        } catch (ArchiveException e) {
            // Expected
        }
    }

    private void testCreateArchiveOutputStream(String archiverName, OutputStream out) {
        try {
            ArchiveOutputStream outStream = new ArchiveStreamFactory().createArchiveOutputStream(archiverName, out);
            assertInstanceOf(getArchiveOutputStreamClass(archiverName), outStream);
        } catch (ArchiveException e) {
            e.printStackTrace();
            throw new RuntimeException("Test failed: " + e.getMessage());
        }
    }

    private void testCreateArchiveOutputStreamNull(String archiverName) {
        try {
            new ArchiveStreamFactory().createArchiveOutputStream(archiverName, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    private void testCreateArchiveInputStreamFromStream(String archiverName) {
        try {
            InputStream in = TestUtilities.createTestFile(archiverName + "-test.archive");
            ArchiveInputStream inStream = new ArchiveStreamFactory().createArchiveInputStream(archiverName, in);
            assertInstanceOf(getArchiveInputStreamClass(archiverName), inStream);
            in.close();
        } catch (IOException | ArchiveException e) {
            e.printStackTrace();
            throw new RuntimeException("Test failed: " + e.getMessage());
        }
    }

    private void testCreateArchiveInputStreamFromStreamNull() {
        try {
            InputStream in = null;
            new ArchiveStreamFactory().createArchiveInputStream("ar", in);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    private Class<?> getArchiveInputStreamClass(String archiverName) {
        switch (archiverName) {
            case "ar":
                return ArArchiveInputStream.class;
            case "zip":
                return ZipArchiveInputStream.class;
            case "tar":
                return TarArchiveInputStream.class;
            case "jar":
                return JarArchiveInputStream.class;
            case "cpio":
                return CpioArchiveInputStream.class;
            case "dump":
                return DumpArchiveInputStream.class;
            default:
                throw new RuntimeException("Unexpected archiver name: " + archiverName);
        }
    }

    private Class<?> getArchiveOutputStreamClass(String archiverName) {
        switch (archiverName) {
            case "ar":
                return ArArchiveOutputStream.class;
            case "zip":
                return ZipArchiveOutputStream.class;
            case "tar":
                return TarArchiveOutputStream.class;
            case "jar":
                return JarArchiveOutputStream.class;
            case "cpio":
                return CpioArchiveOutputStream.class;
            case "dump":
                return DumpArchiveOutputStream.class;
            default:
                throw new RuntimeException("Unexpected archiver name: " + archiverName);
        }
    }
}