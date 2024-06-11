import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
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
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

@RunWith(Parameterized.class)
public class ArchiveStreamFactoryTest {

    private ArchiveStreamFactory factory;
    private String archiverName;
    private InputStream testStream;
    private OutputStream testOutputStream;
    private ArchiveInputStream testArchiveInputStream;
    private ArchiveOutputStream testArchiveOutputStream;

    public ArchiveStreamFactoryTest(String archiverName) {
        this.archiverName = archiverName;
    }

    @Parameterized.Parameters(name = "Archive type: {0}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {ArchiveStreamFactory.AR},
                {ArchiveStreamFactory.CPIO},
                {ArchiveStreamFactory.DUMP},
                {ArchiveStreamFactory.JAR},
                {ArchiveStreamFactory.TAR},
                {ArchiveStreamFactory.ZIP}
        });
    }

    @Before
    public void setUp() throws IOException {
        factory = new ArchiveStreamFactory();
        testOutputStream = new ByteArrayOutputStream();
        testArchiveOutputStream = null;
    }

    @After
    public void tearDown() throws IOException {
        if (testArchiveOutputStream != null) {
            testArchiveOutputStream.finish();
            testArchiveOutputStream.close();
        }
        IOUtils.closeQuietly(testArchiveInputStream);
        IOUtils.closeQuietly(testOutputStream);
    }

    @Test
    public void testCreateArchiveInputStream() throws IOException {
        testArchiveInputStream = factory.createArchiveInputStream(archiverName, new ByteArrayInputStream(new byte[0]));
        assertNotNull(testArchiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStreamWithValidInput() throws IOException {
        testArchiveInputStream = factory.createArchiveInputStream(archiverName, new ByteArrayInputStream(getTestData()));
        assertInstanceOf(getCorrespondingArchiveInputStreamClass(), testArchiveInputStream);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamWithInvalidName() {
        factory.createArchiveInputStream("invalid", new ByteArrayInputStream(getTestData()));
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamWithNullInput() {
        factory.createArchiveInputStream(archiverName, null);
    }

    @Test
    public void testCreateArchiveOutputStream() throws IOException {
        testArchiveOutputStream = factory.createArchiveOutputStream(archiverName, testOutputStream);
        assertNotNull(testArchiveOutputStream);
    }

    @Test
    public void testCreateArchiveOutputStreamWithValidInputStream() throws IOException {
        testArchiveOutputStream = factory.createArchiveOutputStream(archiverName, testOutputStream);
        assertInstanceOf(getCorrespondingArchiveOutputStreamClass(), testArchiveOutputStream);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStreamWithInvalidName() {
        factory.createArchiveOutputStream("invalid", testOutputStream);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStreamWithNullOutputStream() {
        factory.createArchiveOutputStream(archiverName, null);
    }

    @Test
    public void testCreateArchiveOutputStreamWithTestData() throws IOException {
        testArchiveOutputStream = factory.createArchiveOutputStream(archiverName, testOutputStream);
        testArchiveOutputStream.putArchiveEntry(new JarArchiveEntry("test.txt"));
        testArchiveOutputStream.write(getTestData(), 0, getTestData().length);
        testArchiveOutputStream.closeArchiveEntry();
        testArchiveOutputStream.finish();
        testArchiveOutputStream.close();

        testArchiveInputStream = factory.createArchiveInputStream(archiverName, new ByteArrayInputStream(testOutputStream.toByteArray()));
        assertEquals(getTestData().length, testArchiveInputStream.read(new byte[getTestData().length]));
        testArchiveInputStream.close();
    }

    @Test(expected = IOException.class)
    public void testCreateArchiveInputStreamWithMalformedData() throws IOException {
        testArchiveInputStream = factory.createArchiveInputStream(archiverName, new ByteArrayInputStream(getMalformedData()));
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamWithUnknownFormat() throws IOException {
        testArchiveInputStream = factory.createArchiveInputStream("unknown", new ByteArrayInputStream(getTestData()));
    }

    @Test
    public void testCreateArchiveOutputStreamWithStreamMark() throws IOException {
        testArchiveOutputStream = factory.createArchiveOutputStream(archiverName, testOutputStream);
        testArchiveOutputStream.mark(getTestData().length);
        testArchiveOutputStream.putNextEntry(new JarArchiveEntry("test.txt"));
        testArchiveOutputStream.write(getTestData(), 0, getTestData().length);
        testArchiveOutputStream.closeArchiveEntry();
        testArchiveOutputStream.reset();

        byte[] data = new byte[getTestData().length];
        testArchiveInputStream.read(data, 0, getTestData().length);
        assertEquals(getTestData(), data);
        testArchiveInputStream.close();
    }

    private byte[] getTestData() {
        return "Test Data".getBytes();
    }

    private byte[] getMalformedData() {
        return new byte[0];
    }

    private Class<? extends ArchiveInputStream> getCorrespondingArchiveInputStreamClass() {
        switch (archiverName) {
            case ArchiveStreamFactory.AR:
                return ArArchiveInputStream.class;
            case ArchiveStreamFactory.CPIO:
                return CpioArchiveInputStream.class;
            case ArchiveStreamFactory.DUMP:
                return DumpArchiveInputStream.class;
            case ArchiveStreamFactory.JAR:
                return JarArchiveInputStream.class;
            case ArchiveStreamFactory.TAR:
                return TarArchiveInputStream.class;
            case ArchiveStreamFactory.ZIP:
                return ZipArchiveInputStream.class;
            default:
                throw new IllegalStateException("Unreachable");
        }
    }

    private Class<? extends ArchiveOutputStream> getCorrespondingArchiveOutputStreamClass() {
        switch (archiverName) {
            case ArchiveStreamFactory.AR:
                return ArArchiveOutputStream.class;
            case ArchiveStreamFactory.CPIO:
                return CpioArchiveOutputStream.class;
            case ArchiveStreamFactory.DUMP:
                return DumpArchiveOutputStream.class; // DumpArchiveOutputStream is not part of Commons Compress
            case ArchiveStreamFactory.JAR:
                return JarArchiveOutputStream.class;
            case ArchiveStreamFactory.TAR:
                return TarArchiveOutputStream.class;
            case ArchiveStreamFactory.ZIP:
                return ZipArchiveOutputStream.class;
            default:
                throw new IllegalStateException("Unreachable");
        }
    }
}