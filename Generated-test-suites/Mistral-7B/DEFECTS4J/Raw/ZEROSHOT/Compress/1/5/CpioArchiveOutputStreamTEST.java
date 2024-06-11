import org.apache.commons.compress.archivers.cpio.CpioArchiveEntry;
import org.apache.commons.compress.archivers.cpio.CpioConstants;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class CpioArchiveOutputStreamTest {

    private CpioArchiveOutputStream outputStream;
    private ByteArrayOutputStream byteArrayOutputStream;

    @Before
    public void setUp() {
        byteArrayOutputStream = new ByteArrayOutputStream();
        outputStream = new CpioArchiveOutputStream(byteArrayOutputStream, CpioConstants.FORMAT_NEW);
    }

    @After
    public void tearDown() throws IOException {
        outputStream.close();
    }

    @Test
    public void testPutNextEntryWithValidArguments() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        outputStream.putNextEntry(entry);
        assertEquals(1, outputStream.getHeaderBytes().length);
        assertNull(outputStream.cpioEntry);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutNextEntryWithNullEntry() throws IOException {
        outputStream.putNextEntry(null);
    }

    @Test(expected = IllegalStateException.class)
    public void testPutNextEntryWithEntryAlreadyAdded() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        outputStream.putNextEntry(entry);
        outputStream.putNextEntry(entry);
    }

    @Test(expected = IllegalStateException.class)
    public void testPutNextEntryWithDuplicateEntryName() throws IOException {
        CpioArchiveEntry entry1 = new CpioArchiveEntry();
        entry1.setName("testfile");
        outputStream.putNextEntry(entry1);
        CpioArchiveEntry entry2 = new CpioArchiveEntry();
        entry2.setName("testfile");
        outputStream.putNextEntry(entry2);
    }

    @Test
    public void testWriteToOutputstream() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        outputStream.putNextEntry(entry);
        byte[] testData = "12345".getBytes();
        outputStream.write(testData, 0, testData.length);
        assertEquals(testData.length + 1, byteArrayOutputStream.size());
        outputStream.closeArchiveEntry();
    }

    @Test(expected = IOException.class)
    public void testWriteToOutputstreamAfterClosed() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        outputStream.putNextEntry(entry);
        outputStream.closeArchiveEntry();
        outputStream.write(1);
    }

    @Test
    public void testFinishMethod() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        outputStream.putNextEntry(entry);
        byte[] testData = "12345".getBytes();
        outputStream.write(testData, 0, testData.length);
        outputStream.finish();
        assertEquals(testData.length + 1 + 11, byteArrayOutputStream.size());
    }

    @Test(expected = IOException.class)
    public void testFinishMethodAfterClosed() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        outputStream.putNextEntry(entry);
        outputStream.closeArchiveEntry();
        outputStream.finish();
    }
}