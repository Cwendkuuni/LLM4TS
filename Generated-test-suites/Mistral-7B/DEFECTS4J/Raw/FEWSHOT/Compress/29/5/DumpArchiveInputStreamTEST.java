package org.apache.commons.compress.archivers.dump;

import static org.junit.Assert.*;

import org.junit.Test;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipEncoding;
import org.apache.commons.compress.archivers.zip.ZipEncodingHelper;
import org.apache.commons.compress.utils.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Test class for the DumpArchiveInputStream class.
 */
public class DumpArchiveInputStreamTest {
    private static final String TEST_DATA = "testData.dump";

    /**
     * Test the constructor when given an InputStream and no encoding.
     *
     * @throws Exception on failure
     */
    @Test
    public void testDefaultEncoding() throws Exception {
        try (ArchiveInputStream ais = new DumpArchiveInputStream(this.getClass().getResourceAsStream(TEST_DATA));) {
            // perform some basic checks
            assertEquals(DumpArchiveConstants.MAGIC, ais.read(new byte[DumpArchiveConstants.TP_SIZE], 0));
            assertEquals(DumpArchiveConstants.TP_SIZE, ais.getBytesRead());

            // read all entries
            DumpArchiveEntry entry;
            while ((entry = ais.getNextEntry()) != null) {
                assertTrue(entry.isDirectory() || entry.getSize() > 0);
            }

            // verify EOF
            assertNull(ais.getNextEntry());
            assertTrue(ais.isClosed());
        }
    }

    /**
     * Test the constructor when given an InputStream and a custom encoding.
     *
     * @throws Exception on failure
     */
    @Test
    public void testCustomEncoding() throws Exception {
        ZipEncoding encoding = ZipEncodingHelper.getZipEncoding("UTF-8");

        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ByteArrayInputStream bis = new ByteArrayInputStream(DumpArchiveTestData.getDumpArchiveBytes())) {
            // write the test data to a byte array
            byte[] buffer = bos.toByteArray();

            try (ArchiveInputStream ais = new DumpArchiveInputStream(bis, encoding)) {
                // perform some basic checks
                assertEquals(DumpArchiveConstants.MAGIC, ais.read(new byte[DumpArchiveConstants.TP_SIZE], 0));
                assertEquals(DumpArchiveConstants.TP_SIZE, ais.getBytesRead());

                // read all entries
                DumpArchiveEntry entry;
                while ((entry = ais.getNextEntry()) != null) {
                    assertTrue(entry.isDirectory() || entry.getSize() > 0);
                }

                // verify EOF
                assertNull(ais.getNextEntry());
                assertTrue(ais.isClosed());
            }
        }
    }

    /**
     * Test the read method with a valid entry.
     *
     * @throws Exception on failure
     */
    @Test
    public void testReadValidEntry() throws Exception {
        try (ArchiveInputStream ais = new DumpArchiveInputStream(this.getClass().getResourceAsStream(TEST_DATA))) {
            DumpArchiveEntry entry = ais.getNextEntry();

            // read the data
            byte[] buf = new byte[1024];
            int len = ais.read(buf);

            // verify the read
            assertEquals(entry.getSize(), len);
            assertArrayEquals(entry.getData(), Arrays.copyOfRange(buf, 0, len));
        }
    }

    /**
     * Test the read method with an empty entry.
     *
     * @throws Exception on failure
     */
    @Test
    public void testReadEmptyEntry() throws Exception {
        try (ArchiveInputStream ais = new DumpArchiveInputStream(this.getClass().getResourceAsStream(TEST_DATA))) {
            DumpArchiveEntry entry = ais.getNextEntry();
            assertTrue(entry.isDirectory());

            int len = ais.read(new byte[1024]);
            assertEquals(0, len);
        }
    }

    /**
     * Test the close method.
     *
     * @throws Exception on failure
     */
    @Test
    public void testClose() throws Exception {
        try (ArchiveInputStream ais = new DumpArchiveInputStream(this.getClass().getResourceAsStream(TEST_DATA))) {
            // read all entries
            DumpArchiveEntry entry;
            while ((entry = ais.getNextEntry()) != null) {
                // do nothing
            }

            // verify EOF
            assertNull(ais.getNextEntry());
            assertTrue(ais.isClosed());
            ais.close();

            // read all entries again (should throw an IOException)
            assertThrows(IOException.class, () -> ais.getNextEntry());
        }
    }
}