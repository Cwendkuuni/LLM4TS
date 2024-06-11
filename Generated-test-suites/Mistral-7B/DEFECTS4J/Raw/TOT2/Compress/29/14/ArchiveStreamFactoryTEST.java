import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.FormatTests;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

public class ArchiveStreamFactoryTest {

    private ArchiveStreamFactory factory;

    @Before
    public void setup() {
        factory = new ArchiveStreamFactory();
    }

    /**
     * Test creation of ArchiveInputStream for known formats.
     */
    @Test
    public void testCreateArchiveInputStream() throws Exception {
        for (String format : FormatTests.FORMATS) {
            InputStream inputStream = new ByteArrayInputStream(FormatTests.getTestFileBytes(format));
            ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(format, inputStream);
            assertNotNull(archiveInputStream);
            IOUtils.closeQuietly(archiveInputStream);
            IOUtils.closeQuietly(inputStream);
        }
    }

    /**
     * Test creation of ArchiveInputStream for unknown format.
     */
    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamUnknownFormat() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        factory.createArchiveInputStream("unknown", inputStream);
    }

    /**
     * Test creation of ArchiveOutputStream for known formats.
     */
    @Test
    public void testCreateArchiveOutputStream() throws Exception {
        for (String format : FormatTests.FORMATS) {
            OutputStream outputStream = new ByteArrayOutputStream();
            ArchiveOutputStream archiveOutputStream = factory.createArchiveOutputStream(format, outputStream);
            assertNotNull(archiveOutputStream);
            IOUtils.closeQuietly(archiveOutputStream);
            IOUtils.closeQuietly(outputStream);
        }
    }

    /**
     * Test creation of ArchiveOutputStream for unknown format.
     */
    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStreamUnknownFormat() throws Exception {
        OutputStream outputStream = new ByteArrayOutputStream();
        factory.createArchiveOutputStream("unknown", outputStream);
    }

    /**
     * Test creation of ArchiveInputStream from an InputStream without mark support.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamNoMarkSupport() throws Exception {
        InputStream inputStream = new InputStream() {
            @Override
            public int read() throws IOException {
                return 0;
            }

            @Override
            public boolean markSupported() {
                return false;
            }
        };
        factory.createArchiveInputStream("zip", inputStream);
    }

    /**
     * Test creation of ArchiveOutputStream for ZIP format with null encoding.
     */
    @Test
    public void testCreateArchiveOutputStreamZipNullEncoding() throws Exception {
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, outputStream);
        assertNotNull(archiveOutputStream);
        IOUtils.closeQuietly(archiveOutputStream);
        IOUtils.closeQuietly(outputStream);
    }

    /**
     * Test creation of ArchiveOutputStream for ZIP format with specified encoding.
     */
    @Test
    public void testCreateArchiveOutputStreamZipSpecifiedEncoding() throws Exception {
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, outputStream, "UTF-8");
        assertNotNull(archiveOutputStream);
        IOUtils.closeQuietly(archiveOutputStream);
        IOUtils.closeQuietly(outputStream);
    }

    /**
     * Test creation of ArchiveOutputStream for unsupported format.
     */
    @Test(expected = StreamingNotSupportedException.class)
    public void testCreateArchiveOutputStreamSevenZ() throws Exception {
        OutputStream outputStream = new ByteArrayOutputStream();
        factory.createArchiveOutputStream(ArchiveStreamFactory.SEVEN_Z, outputStream);
    }
}