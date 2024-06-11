package org.apache.commons.compress.archivers.ar;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.compress.archivers.ArArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipUtils;
import org.junit.Test;

/**
 * Test for ArArchiveInputStream class.
 */
public class ArArchiveInputStreamTest {

    @Test
    public void testArArchiveInputStream() throws IOException {
        final String testData = "testData";
        final byte[] uncompressedData = testData.getBytes();
        final byte[] compressedData = GzipUtils.compress(uncompressedData);
        final byte[] arHeader = new byte[] {0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x14, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x16, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00, 0x00, 0x10, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00, 0x00, 0x04, 0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00, 0x00};
        final byte[] arTrailer = new byte[] {0x3e, 0x0a, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x14, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x16, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00, 0x00, 0x10, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x04, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};

        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write(arHeader);
        baos.write(uncompressedData);
        baos.write(arTrailer);

        final byte[] testDataWithArHeaders = baos.toByteArray();
        final InputStream input = new ByteArrayInputStream(testDataWithArHeaders);

        ArArchiveInputStream arInputStream = new ArArchiveInputStream(input);
        ArArchiveEntry arEntry = arInputStream.getNextArEntry();
        assertTrue(arEntry instanceof ArArchiveEntry);
        assertEquals("testData", arEntry.getName());
        assertEquals(uncompressedData.length, arEntry.getSize());
        arEntry = arInputStream.getNextArEntry();
        assertNull(arEntry);
        arInputStream.close();
    }
}