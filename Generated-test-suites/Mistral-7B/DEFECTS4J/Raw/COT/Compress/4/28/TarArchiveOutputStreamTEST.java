import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.archivers.tar.TarBuffer;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarInputStream;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class TarArchiveOutputStreamTest {
    private TarArchiveOutputStream tarOutputStream;
    private OutputStream outputStream;
    private ByteArrayOutputStream buffer;

    @Before
    public void setUp() throws Exception {
        outputStream = new ByteArrayOutputStream();
        tarOutputStream = new TarArchiveOutputStream(outputStream);
        buffer = new ByteArrayOutputStream();
    }

    @After
    public void tearDown() throws Exception {
        tarOutputStream.close();
        outputStream.close();
    }

    @Test
    public void testConstructor_OutputStream() throws Exception {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        Assert.assertNotNull(tarOutputStream);
    }

    @Test
    public void testConstructor_OutputStream_int() throws Exception {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(), 1024);
        Assert.assertNotNull(tarOutputStream);
    }

    @Test
    public void testConstructor_OutputStream_int_int() throws Exception {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(), 1024, 512);
        Assert.assertNotNull(tarOutputStream);
    }

    @Test
    public void testSetLongFileMode() throws Exception {
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        Assert.assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarOutputStream.getLongFileMode());
    }

    @Test
    public void testFinish() throws Exception {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.finish();
        TarInputStream tarInputStream = new TarInputStream(new ByteArrayInputStream(outputStream.toByteArray()));

        TarArchiveEntry archiveEntry = tarInputStream.getNextEntry();
        Assert.assertEquals("test.txt", archiveEntry.getName());
        Assert.assertNull(tarInputStream.getNextEntry());
    }

    @Test(expected = IOException.class)
    public void testFinish_UnclosedEntry() throws Exception {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.finish();
        tarOutputStream.putArchiveEntry(new TarArchiveEntry("test2.txt"));
    }

    @Test
    public void testClose() throws Exception {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.close();
        TarInputStream tarInputStream = new TarInputStream(new ByteArrayInputStream(outputStream.toByteArray()));

        TarArchiveEntry archiveEntry = tarInputStream.getNextEntry();
        Assert.assertEquals("test.txt", archiveEntry.getName());
        Assert.assertNull(tarInputStream.getNextEntry());
    }

    @Test(expected = IOException.class)
    public void testClose_UnfinishedEntry() throws Exception {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);
    }

    @Test
    public void testGetRecordSize() throws Exception {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream, 1024, 512);
        Assert.assertEquals(512, tarOutputStream.getRecordSize());
    }

    @Test
    public void testPutArchiveEntry() throws Exception {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);
        byte[] expectedHeader = entry.getHeaderBytes();

        byte[] actualHeader = new byte[TarConstants.STANDARD_RECORD_LENGTH];
        tarOutputStream.write(actualHeader, 0, TarConstants.STANDARD_RECORD_LENGTH);

        Assert.assertArrayEquals(expectedHeader, actualHeader);
    }

    @Test
    public void testCloseArchiveEntry() throws Exception {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.write("Hello World!".getBytes(StandardCharsets.UTF_8), 0, "Hello World!".length());
        tarOutputStream.closeArchiveEntry();

        byte[] expectedHeader = entry.getHeaderBytes();
        byte[] expectedData = "Hello World!".getBytes(StandardCharsets.UTF_8);

        byte[] actualHeader = new byte[TarConstants.STANDARD_RECORD_LENGTH];
        tarOutputStream.write(actualHeader, 0, TarConstants.STANDARD_RECORD_LENGTH);
        byte[] actualData = new byte[entry.getSize()];

        tarOutputStream.read(actualData, 0, actualData.length);

        Assert.assertArrayEquals(expectedHeader, actualHeader);
        Assert.assertArrayEquals(expectedData, actualData);
    }

    @Test
    public void testWriteByteArray() throws Exception {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);
        byte[] data = "Hello World!".getBytes(StandardCharsets.UTF_8);

        tarOutputStream.write(data, 0, data.length);
        tarOutputStream.closeArchiveEntry();

        byte[] expectedHeader = entry.getHeaderBytes();
        byte[] expectedData = data;

        byte[] actualHeader = new byte[TarConstants.STANDARD_RECORD_LENGTH];
        tarOutputStream.write(actualHeader, 0, TarConstants.STANDARD_RECORD_LENGTH);
        byte[] actualData = new byte[entry.getSize()];

        tarOutputStream.read(actualData, 0, actualData.length);

        Assert.assertArrayEquals(expectedHeader, actualHeader);
        Assert.assertArrayEquals(expectedData, actualData);
    }

    @Test
    public void testWriteByteArray_PartialWrite() throws Exception {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);
        byte[] data = "Hello World!".getBytes(StandardCharsets.UTF_8);

        int numToWrite = data.length / 2;
        tarOutputStream.write(data, 0, numToWrite);
        tarOutputStream.closeArchiveEntry();

        byte[] expectedHeader = entry.getHeaderBytes();
        byte[] expectedData = new byte[numToWrite];
        System.arraycopy(data, 0, expectedData, 0, numToWrite);

        byte[] actualHeader = new byte[TarConstants.STANDARD_RECORD_LENGTH];
        tarOutputStream.write(actualHeader, 0, TarConstants.STANDARD_RECORD_LENGTH);
        byte[] actualData = new byte[entry.getSize()];

        tarOutputStream.read(actualData, 0, actualData.length);

        Assert.assertArrayEquals(expectedHeader, actualHeader);
        Assert.assertArrayEquals(expectedData, actualData, 0, numToWrite);
    }

    @Test
    public void testWriteByteArray_PartialWrite_LastRecord() throws Exception {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);
        byte[] data = "Hello World!".getBytes(StandardCharsets.UTF_8);

        int numToWrite = data.length % TarConstants.BLOCK_SIZE;
        tarOutputStream.write(data, 0, numToWrite);
        tarOutputStream.write(new byte[TarConstants.BLOCK_SIZE - numToWrite], 0, TarConstants.BLOCK_SIZE - numToWrite);
        tarOutputStream.closeArchiveEntry();

        byte[] expectedHeader = entry.getHeaderBytes();
        byte[] expectedData = new byte[data.length];
        System.arraycopy(data, 0, expectedData, 0, data.length);

        byte[] actualHeader = new byte[TarConstants.STANDARD_RECORD_LENGTH];
        tarOutputStream.write(actualHeader, 0, TarConstants.STANDARD_RECORD_LENGTH);
        byte[] actualData = new byte[entry.getSize()];

        tarOutputStream.read(actualData, 0, actualData.length);

        Assert.assertArrayEquals(expectedHeader, actualHeader);
        Assert.assertArrayEquals(expectedData, actualData);
    }
}