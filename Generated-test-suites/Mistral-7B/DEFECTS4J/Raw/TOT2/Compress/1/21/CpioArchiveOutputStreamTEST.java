import org.apache.commons.compress.archivers.cpio.CpioArchiveEntry;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.cpio.CpioConstants;
import org.apache.commons.compress.utils.CpioUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

public class CpioArchiveOutputStreamTest {

    private static final String TEST_ENTRY_NAME = "testEntry.txt";
    private static final String TEST_CONTENTS = "Hello World!";
    private CpioArchiveOutputStream outputStream;
    private ByteArrayOutputStream byteArrayOutputStream;
    private Map<String, CpioArchiveEntry> entries;

    @Before
    public void setUp() throws IOException {
        byteArrayOutputStream = new ByteArrayOutputStream();
        outputStream = new CpioArchiveOutputStream(byteArrayOutputStream);
        entries = new HashMap<>();
    }

    @Test
    public void testPutNextEntry() throws IOException {
        // test putting a new entry with default values
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(TEST_ENTRY_NAME);
        outputStream.putNextEntry(entry);
        Assert.assertEquals(TEST_ENTRY_NAME, outputStream.getCpioEntry().getName());
        Assert.assertEquals(CpioConstants.FORMAT_NEW, outputStream.getCpioEntry().getFormat());

        // test putting a new entry with specified format
        CpioArchiveEntry newEntry = new CpioArchiveEntry();
        newEntry.setName(TEST_ENTRY_NAME + "_new_format");
        newEntry.setFormat(CpioConstants.FORMAT_OLD_BINARY);
        outputStream.putNextEntry(newEntry);
        Assert.assertEquals(TEST_ENTRY_NAME + "_new_format", outputStream.getCpioEntry().getName());
        Assert.assertEquals(CpioConstants.FORMAT_OLD_BINARY, outputStream.getCpioEntry().getFormat());

        // test putting multiple entries
        CpioArchiveEntry secondEntry = new CpioArchiveEntry();
        secondEntry.setName("secondEntry.txt");
        outputStream.putNextEntry(secondEntry);
        Assert.assertEquals(2, entries.size());
    }

    @Test
    public void testWrite() throws IOException {
        // test writing to an existing entry
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(TEST_ENTRY_NAME);
        outputStream.putNextEntry(entry);
        outputStream.write(TEST_CONTENTS.getBytes());
        byte[] actualBytes = byteArrayOutputStream.toByteArray();
        byte[] expectedBytes = new byte[]{(byte) 0x16, (byte) 'C', (byte) 'P', (byte) 'I', (byte) 'O', (byte) ' ', (byte) 0x20, (byte) 0x10, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x07, (byte) 'H', (byte) 'e', (byte) 'l', (byte) 'l', (byte) 'o', (byte) ' ', (byte) 'W', (byte) 'o', (byte) 'r', (byte) 'l', (byte) 'd', (byte) '!', (byte) '\0', (byte) 0x00};
        Assert.assertArrayEquals(expectedBytes, actualBytes);

        // test writing to a new entry
        outputStream.putNextEntry(new CpioArchiveEntry());
        outputStream.write(TEST_CONTENTS.getBytes());
        actualBytes = byteArrayOutputStream.toByteArray();
        expectedBytes = new byte[]{(byte) 0x16, (byte) 'C', (byte) 'P', (byte) 'I', (byte) 'O', (byte) ' ', (byte) 0x20, (byte) 0x11, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x07, (byte) 'T', (byte) 'e', (byte) 's', (byte) 't', (byte) '_', (byte) 'E', (byte) 'n', (byte) 't', (byte) 'r', (byte) 'y', (byte) '_', (byte) 'n', (byte) 'e', (byte) 'w', (byte) '_', (byte) 'f', (byte) 'o', (byte) 'r', (byte) 'm', (byte) 'a', (byte) 't', (byte) ' ', (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x07, (byte) 'H', (byte) 'e', (byte) 'l', (byte) 'l', (byte) 'o', (byte) ' ', (byte) 'W', (byte) 'o', (byte) 'r', (byte) 'l', (byte) 'd', (byte) '!', (byte) '\0', (byte) 0x00};
        Assert.assertArrayEquals(expectedBytes, actualBytes);
    }

    @Test(expected = IOException.class)
    public void testPutNextEntryAfterClose() throws IOException {
        outputStream.close();
        outputStream.putNextEntry(new CpioArchiveEntry());
    }

    @Test(expected = IOException.class)
    public void testPutNextEntryWithDuplicateName() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(TEST_ENTRY_NAME);
        outputStream.putNextEntry(entry);
        outputStream.putNextEntry(entry);
    }

    @Test(expected = IOException.class)
    public void testWriteAfterClose() throws IOException {
        outputStream.close();
        outputStream.write(TEST_CONTENTS.getBytes());
    }

    @Test(expected = IOException.class)
    public void testWritePastEndOfEntry() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(TEST_ENTRY_NAME);
        outputStream.putNextEntry(entry);
        outputStream.write(TEST_CONTENTS.getBytes());
        outputStream.write(TEST_CONTENTS.getBytes());
    }

    @Test(expected = IOException.class)
    public void testFinishWithoutPuttingAnEntry() throws IOException {
        outputStream.finish();
    }

    @Test(expected = IOException.class)
    public void testFinishWithDuplicateName() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(TEST_ENTRY_NAME);
        outputStream.putNextEntry(entry);
        outputStream.finish();
        outputStream.putNextEntry(entry);
    }

    @Test(expected = IOException.class)
    public void testWriteWithInvalidFormat() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{0x16, (byte) 0x01, (byte) 'C', (byte) 'P', (byte) 'I', (byte) 'O', (byte) ' ', (byte) 0x20, (byte) 0x10, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x07, (byte) 'H', (byte) 'e', (byte) 'l', (byte) 'l', (byte) 'o', (byte) ' ', (byte) 'W', (byte) 'o', (byte) 'r', (byte) 'l', (byte) 'd', (byte) '!', (byte) '\0', (byte) 0x01, (byte) 'T', (byte) 'e', (byte) 's', (byte) 't', (byte) '_', (byte) 'E', (byte) 'n', (byte) 't', (byte) 'r', (byte) 'y', (byte) '_', (byte) 'n', (byte) 'e', (byte) 'w', (byte) '_', (byte) 'f', (byte) 'o', (byte) 'r', (byte) 'm', (byte) 'a', (byte) 't', (byte) ' ', (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x07, (byte) 0x01, (byte) 'H', (byte) 'e', (byte) 'l', (byte) 'l', (byte) 'o', (byte) ' ', (byte) 'W', (byte) 'o', (byte) 'r', (byte) 'l', (byte) 'd', (byte) '!', (byte) '\0', (byte) 0x00});
        CpioArchiveOutputStream archiveOutputStream = new CpioArchiveOutputStream(inputStream);
        archiveOutputStream.finish();
    }
}