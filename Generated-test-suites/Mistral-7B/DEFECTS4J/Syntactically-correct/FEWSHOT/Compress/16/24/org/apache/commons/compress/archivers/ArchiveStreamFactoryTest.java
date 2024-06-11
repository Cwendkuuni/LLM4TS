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
import org.apache.commons.compress.archivers.test.testclasses.TestInputStream;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Test cases for ArchiveStreamFactory class
 */
public class ArchiveStreamFactoryTest {

    /**
     * Test createArchiveInputStream method with valid arguments
     */
    @Test
    public void testCreateArchiveInputStreamWithValidArguments() {
        String[] archiverNames = {ArchiveStreamFactory.AR, ArchiveStreamFactory.CPIO,
                ArchiveStreamFactory.DUMP, ArchiveStreamFactory.JAR, ArchiveStreamFactory.TAR,
                ArchiveStreamFactory.ZIP};

        for (String archiverName : archiverNames) {
            InputStream testStream = new ByteArrayInputStream(getTestData(archiverName));
            ArchiveInputStream expectedInputStream = null;

            switch (archiverName) {
                case ArchiveStreamFactory.AR:
                    expectedInputStream = new ArArchiveInputStream(testStream);
                    break;
                case ArchiveStreamFactory.CPIO:
                    expectedInputStream = new CpioArchiveInputStream(testStream);
                    break;
                case ArchiveStreamFactory.DUMP:
                    expectedInputStream = new DumpArchiveInputStream(testStream);
                    break;
                case ArchiveStreamFactory.JAR:
                    expectedInputStream = new JarArchiveInputStream(testStream);
                    break;
                case ArchiveStreamFactory.TAR:
                    expectedInputStream = new TarArchiveInputStream(testStream);
                    break;
                case ArchiveStreamFactory.ZIP:
                    expectedInputStream = new ZipArchiveInputStream(testStream);
                    break;
                default:
                    break;
            }

            ArchiveInputStream actualInputStream = ArchiveStreamFactory.getInstance()
                    .createArchiveInputStream(archiverName, testStream);
            assertInstanceOf(expectedInputStream.getClass(), actualInputStream.getClass());
        }
    }

    /**
     * Test createArchiveOutputStream method with valid arguments
     */
    @Test
    public void testCreateArchiveOutputStreamWithValidArguments() {
        String[] archiverNames = {ArchiveStreamFactory.AR, ArchiveStreamFactory.CPIO,
                ArchiveStreamFactory.DUMP, ArchiveStreamFactory.JAR, ArchiveStreamFactory.TAR,
                ArchiveStreamFactory.ZIP};

        for (String archiverName : archiverNames) {
            OutputStream testOutputStream = new ByteArrayOutputStream();
            ArchiveOutputStream expectedOutputStream = null;

            switch (archiverName) {
                case ArchiveStreamFactory.AR:
                    expectedOutputStream = new ArArchiveOutputStream(testOutputStream);
                    break;
                case ArchiveStreamFactory.CPIO:
                    expectedOutputStream = new CpioArchiveOutputStream(testOutputStream);
                    break;
                case ArchiveStreamFactory.DUMP:
                    expectedOutputStream = new DumpArchiveOutputStream(testOutputStream);
                    break;
                case ArchiveStreamFactory.JAR:
                    expectedOutputStream = new JarArchiveOutputStream(testOutputStream);
                    break;
                case ArchiveStreamFactory.TAR:
                    expectedOutputStream = new TarArchiveOutputStream(testOutputStream);
                    break;
                case ArchiveStreamFactory.ZIP:
                    expectedOutputStream = new ZipArchiveOutputStream(testOutputStream);
                    break;
                default:
                    break;
            }

            ArchiveOutputStream actualOutputStream = ArchiveStreamFactory.getInstance()
                    .createArchiveOutputStream(archiverName, testOutputStream);
            assertInstanceOf(expectedOutputStream.getClass(), actualOutputStream.getClass());
        }
    }

    /**
     * Test createArchiveInputStream method with null archiverName or inputStream
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamWithNullArchiverNameOrInputStream() {
        ArchiveStreamFactory factory = ArchiveStreamFactory.getInstance();
        factory.createArchiveInputStream(null, new ByteArrayInputStream(new byte[0]));
    }

    /**
     * Test createArchiveOutputStream method with null archiverName or outputStream
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStreamWithNullArchiverNameOrOutputStream() {
        ArchiveStreamFactory factory = ArchiveStreamFactory.getInstance();
        factory.createArchiveOutputStream(null, new ByteArrayOutputStream());
    }

    /**
     * Test createArchiveInputStream method with unknown archiverName
     */
    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamWithUnknownArchiverName() {
        ArchiveStreamFactory factory = ArchiveStreamFactory.getInstance();
        InputStream testStream = new ByteArrayInputStream(getTestData("unknown"));
        factory.createArchiveInputStream("unknown", testStream);
    }

    /**
     * Test createArchiveOutputStream method with unknown archiverName
     */
    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStreamWithUnknownArchiverName() {
        ArchiveStreamFactory factory = ArchiveStreamFactory.getInstance();
        OutputStream testOutputStream = new ByteArrayOutputStream();
        factory.createArchiveOutputStream("unknown", testOutputStream);
    }

    /**
     * Test createArchiveInputStream method with ArArchiveInputStream
     */
    @Test
    public void testCreateArchiveInputStreamWithArArchiveInputStream() {
        InputStream testStream = new ArArchiveInputStream(new ByteArrayInputStream(getTestData(ArchiveStreamFactory.AR)));
        ArchiveInputStream expectedInputStream = testStream;
        ArchiveInputStream actualInputStream = ArchiveStreamFactory.getInstance()
                .createArchiveInputStream(ArchiveStreamFactory.AR, testStream);
        assertInstanceOf(expectedInputStream.getClass(), actualInputStream.getClass());
    }

    /**
     * Test createArchiveOutputStream method with JarArchiveOutputStream
     */
    @Test
    public void testCreateArchiveOutputStreamWithJarArchiveOutputStream() {
        OutputStream testOutputStream = new ByteArrayOutputStream();
        JarArchiveOutputStream expectedOutputStream = (JarArchiveOutputStream) testOutputStream;
        ArchiveOutputStream actualOutputStream = ArchiveStreamFactory.getInstance()
                .createArchiveOutputStream(ArchiveStreamFactory.JAR, testOutputStream);
        assertInstanceOf(expectedOutputStream.getClass(), actualOutputStream.getClass());
    }

    /**
     * Test createArchiveInputStream method with null inputStream
     */
    @Test
    public void testCreateArchiveInputStreamWithNullInputStream() {
        ArchiveStreamFactory factory = ArchiveStreamFactory.getInstance();
        assertNull(factory.createArchiveInputStream(ArchiveStreamFactory.AR, null));
    }

    /**
     * Test createArchiveOutputStream method with null outputStream
     */
    @Test
    public void testCreateArchiveOutputStreamWithNullOutputStream() {
        ArchiveStreamFactory factory = ArchiveStreamFactory.getInstance();
        assertNull(factory.createArchiveOutputStream(ArchiveStreamFactory.AR, null));
    }

    /**
     * Helper method to get test data for each archiver
     *
     * @param archiverName the name of the archiver
     * @return the test data as a byte array
     */
    private byte[] getTestData(String archiverName) {
        switch (archiverName) {
            case ArchiveStreamFactory.AR:
                return getArTestData();
            case ArchiveStreamFactory.CPIO:
                return getCpioTestData();
            case ArchiveStreamFactory.DUMP:
                return getDumpTestData();
            case ArchiveStreamFactory.JAR:
                return getJarTestData();
            case ArchiveStreamFactory.TAR:
                return getTarTestData();
            case ArchiveStreamFactory.ZIP:
                return getZipTestData();
            default:
                return new byte[0];
        }
    }

    /**
     * Helper method to get test data for AR archive
     *
     * @return the test data as a byte array
     */
    private byte[] getArTestData() {
        // Your test data for AR archive goes here
        return new byte[0];
    }

    /**
     * Helper method to get test data for CPIO archive
     *
     * @return the test data as a byte array
     */
    private byte[] getCpioTestData() {
        // Your test data for CPIO archive goes here
        return new byte[0];
    }

    /**
     * Helper method to get test data for DUMP archive
     *
     * @return the test data as a byte array
     */
    private byte[] getDumpTestData() {
        // Your test data for DUMP archive goes here
        return new byte[0];
    }

    /**
     * Helper method to get test data for JAR archive
     *
     * @return the test data as a byte array
     */
    private byte[] getJarTestData() {
        // Your test data for JAR archive goes here
        return new byte[0];
    }

    /**
     * Helper method to get test data for TAR archive
     *
     * @return the test data as a byte array
     */
    private byte[] getTarTestData() {
        // Your test data for TAR archive goes here
        return new byte[0];
    }

    /**
     * Helper method to get test data for ZIP archive
     *
     * @return the test data as a byte array
     */
    private byte[] getZipTestData() {
        // Your test data for ZIP archive goes here
        return new byte[0];
    }
}

class TestInputStream extends InputStream {
    private byte[] data;
    private int index;

    TestInputStream(byte[] data) {
        this.data = data;
        this.index = 0;
    }

    @Override
    public int read() throws IOException {
        if (index >= data.length) {
            return -1;
        }
        return data[index++];
    }

    @Override
    public int available() throws IOException {
        return data.length - index;
    }

    @Override
    public long skip(long n) throws IOException {
        long skipped = 0;
        while (skipped < n && index < data.length) {
            int read = data[index] == (byte) -1 ? -1 : read();
            if (read != -1) {
                index++;
                skipped++;
            }
        }
        return skipped;
    }

    @Override
    public void mark(int readlimit) {
        // No-op
    }

    @Override
    public void reset() throws IOException {
        // No-op
    }

    @Override
    public boolean markSupported() {
        return false;
    }

    @Override
    public void close() throws IOException {
        // No-op
    }
}