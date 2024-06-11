package org.apache.commons.compress.archivers.dump;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipEncoding;
import org.apache.commons.compress.archivers.zip.ZipEncodingHelper;
import org.apache.commons.compress.utils.TestUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.apache.commons.compress.archivers.dump.DumpArchiveConstants.*;
import static org.apache.commons.compress.archivers.dump.DumpArchiveUtil.*;

public class DumpArchiveInputStreamTest {
    

    private static final String TEST_FILE = "test.dump";
    private static final ZipEncoding zipEncoding = ZipEncodingHelper.getDefaultEncoding();
    private ArchiveInputStream input;
    private DumpArchiveInputStream dumpArchiveInputStream;
    private ByteArrayOutputStream output;
    private byte[] testData;

    @Before
    public void setUp() throws IOException {
        // Test data creation
        byte[] header = new byte[TP_SIZE];
        System.arraycopy(new byte[]{(byte) 0x64, (byte) 0x64, (byte) 0x64, (byte) 0x64}, 0, header, 0, 4);
        System.arraycopy(new byte[]{CLRI_MAGIC, 5, 0, 5, 0, 0, 0, 0}, 4, header, 4, 8);

        testData = new byte[TP_SIZE * 6];
        System.arraycopy(header, 0, testData, 0, TP_SIZE);

        // Write test data to output stream
        output = new ByteArrayOutputStream();
        output.write(testData);
        output.close();
        testData = output.toByteArray();

        // Create test InputStream
        InputStream inputStream = new ByteArrayInputStream(testData);
        this.input = new ArchiveInputStream(inputStream) {
            @Override
            public String getFormatName() {
                return "test-format";
            }
        };

        this.dumpArchiveInputStream = new DumpArchiveInputStream(inputStream, zipEncoding);
    }

    @Test
    public void testMatches() {
        byte[] testData = new byte[]{NFS_MAGIC, 0, 0, 0, 0};
        Assert.assertTrue(matches(testData, 5));
        Assert.assertFalse(matches(new byte[TP_SIZE], TP_SIZE));
    }

    @Test
    public void testConstructorWithDefaultEncoding() throws IOException {
        dumpArchiveInputStream = new DumpArchiveInputStream(new ByteArrayInputStream(testData));
        DumpArchiveEntry entry = dumpArchiveInputStream.getNextEntry();
        Assert.assertNotNull(entry);
        Assert.assertEquals(CLRI_MAGIC, entry.getHeaderType());
    }

    @Test
    public void testConstructorWithProvidedEncoding() throws IOException {
        dumpArchiveInputStream = new DumpArchiveInputStream(new ByteArrayInputStream(testData), zipEncoding);
        DumpArchiveEntry entry = dumpArchiveInputStream.getNextEntry();
        Assert.assertNotNull(entry);
        Assert.assertEquals(CLRI_MAGIC, entry.getHeaderType());
    }

    @Test(expected = ArchiveException.class)
    public void testInvalidFormatException() throws IOException {
        InputStream invalidFormatInputStream = new ByteArrayInputStream(new byte[]{INVALID_FORMAT_MAGIC});
        new DumpArchiveInputStream(invalidFormatInputStream);
    }

    @Test(expected = ArchiveException.class)
    public void testUnrecognizedFormatException() throws IOException {
        InputStream unrecognizedFormatInputStream = new ByteArrayInputStream(new byte[]{UNRECOGNIZED_FORMAT_MAGIC});
        new DumpArchiveInputStream(unrecognizedFormatInputStream);
    }

    @Test
    public void testGetNextEntry() throws IOException {
        DumpArchiveEntry entry = dumpArchiveInputStream.getNextEntry();
        Assert.assertNotNull(entry);
        Assert.assertEquals(CLRI_MAGIC, entry.getHeaderType());

        entry = dumpArchiveInputStream.getNextEntry();
        Assert.assertNotNull(entry);
        Assert.assertEquals(BITS_MAGIC, entry.getHeaderType());

        entry = dumpArchiveInputStream.getNextEntry();
        Assert.assertNull(entry);
    }

    @Test(expected = IOException.class)
    public void testEOFException() throws IOException {
        dumpArchiveInputStream.getNextEntry();
        dumpArchiveInputStream.getNextEntry();
        dumpArchiveInputStream.getNextEntry();

        dumpArchiveInputStream.read(new byte[TP_SIZE], 0, TP_SIZE);
    }

    @Test
    public void testRead() throws IOException {
        byte[] buffer = new byte[TP_SIZE];
        int bytesRead = dumpArchiveInputStream.read(buffer, 0, TP_SIZE);
        Assert.assertEquals(TP_SIZE, bytesRead);
        Assert.assertArrayEquals(testData, buffer);

        bytesRead = dumpArchiveInputStream.read(buffer, 0, TP_SIZE);
        Assert.assertEquals(TP_SIZE, bytesRead);
        Assert.assertArrayEquals(Arrays.copyOfRange(testData, TP_SIZE, testData.length), buffer);

        bytesRead = dumpArchiveInputStream.read(buffer, 0, TP_SIZE);
        Assert.assertEquals(-1, bytesRead);
    }

    @Test
    public void testClose() throws IOException {
        dumpArchiveInputStream.close();
        Assert.assertFalse(input.isOpen());
    }

    @Test
    public void testVerify() {
        Assert.assertTrue(DumpArchiveUtil.verify(testData));
    }

    
}