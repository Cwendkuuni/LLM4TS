import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.CpioConstants;
import org.apache.commons.compress.archivers.CpioUtil;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.cpio.CpioConstants.FORMAT_NEW;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CpioArchiveOutputStreamTest {

    private CpioArchiveOutputStream outputStream;
    private ByteArrayOutputStream byteArrayOutputStream;

    @Before
    public void setUp() throws Exception {
        byteArrayOutputStream = new ByteArrayOutputStream();
        outputStream = new CpioArchiveOutputStream(byteArrayOutputStream, FORMAT_NEW);
    }

    @After
    public void tearDown() throws Exception {
        if (outputStream != null) {
            outputStream.close();
        }
    }

    @Test
    public void testPutNextEntryWithValidEntry() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        outputStream.putNextEntry(entry);
        assertNotNull(outputStream.getCpioEntry());
    }

    @Test(expected = IOException.class)
    public void testPutNextEntryWithNullEntry() throws IOException {
        outputStream.putNextEntry(null);
    }

    @Test(expected = IOException.class)
    public void testPutNextEntryWithDuplicateEntry() throws IOException {
        outputStream.putNextEntry(new CpioArchiveEntry());
        outputStream.putNextEntry(new CpioArchiveEntry());
    }

    @Test
    public void testWriteWithValidData() throws IOException {
        byte[] data = "test data".getBytes();
        outputStream.putNextEntry(new CpioArchiveEntry());
        outputStream.write(data, 0, data.length);
        assertNotNull(outputStream.getCpioEntry());
        assertEquals(data.length, outputStream.getCpioEntry().getSize());
    }

    @Test(expected = IOException.class)
    public void testWriteWithNegativeOffset() throws IOException {
        outputStream.write(new byte[1], -1, 1);
    }

    @Test(expected = IOException.class)
    public void testWriteWithNegativeLength() throws IOException {
        outputStream.write(new byte[1], 0, -1);
    }

    @Test(expected = IOException.class)
    public void testWriteWithNullData() throws IOException {
        outputStream.write(null, 0, 0);
    }

    @Test
    public void testFinish() throws IOException {
        byte[] data = "test data".getBytes();
        outputStream.putNextEntry(new CpioArchiveEntry());
        outputStream.write(data, 0, data.length);
        outputStream.finish();
        String result = new String(byteArrayOutputStream.toByteArray(), "ASCII");
        assertTrue(result.contains(CpioUtil.getMagicNumber(FORMAT_NEW)));
        assertNull(outputStream.getCpioEntry());
    }

    @Test(expected = IOException.class)
    public void testFinishWithEntries() throws IOException {
        byte[] data1 = "test data 1".getBytes();
        byte[] data2 = "test data 2".getBytes();
        outputStream.putNextEntry(new CpioArchiveEntry());
        outputStream.write(data1, 0, data1.length);
        outputStream.putNextEntry(new CpioArchiveEntry());
        outputStream.write(data2, 0, data2.length);
        outputStream.finish();
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        byte[] data = "test data".getBytes();
        outputStream.putNextEntry(new CpioArchiveEntry());
        outputStream.write(data, 0, data.length);
        outputStream.closeArchiveEntry();
        assertNull(outputStream.getCpioEntry());
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntryWithNoEntry() throws IOException {
        outputStream.closeArchiveEntry();
    }

    @Test
    public void testGetNames() {
        Map<String, CpioArchiveEntry> names = new HashMap<>();
        byte[] data = "test data".getBytes();
        for (int i = 0; i < 10; i++) {
            CpioArchiveEntry entry = new CpioArchiveEntry();
            entry.setName("testfile" + i);
            names.put(entry.getName(), entry);
            outputStream.putNextEntry(entry);
            outputStream.write(data, 0, data.length);
        }
        outputStream.finish();
        assertEquals(names.size(), outputStream.getNames().size());
        for (Map.Entry<String, CpioArchiveEntry> entry : names.entrySet()) {
            assertEquals(entry.getValue().getName(),
                    outputStream.getNames().get(entry.getKey()).getName());
        }
    }

}