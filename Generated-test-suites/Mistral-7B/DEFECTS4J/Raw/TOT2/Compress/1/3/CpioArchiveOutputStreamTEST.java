import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.CpioConstants;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.utils.CpioUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class CpioArchiveOutputStreamTest {

    private CpioArchiveOutputStream out;
    private OutputStream underlingStream;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUp() throws IOException {
        underlingStream = new ByteArrayOutputStream();
        out = new CpioArchiveOutputStream(underlingStream);
    }

    @Test
    public void testPutNextEntryWithValidArguments() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setSize(5);
        out.putNextEntry(entry);

        byte[] expectedHeader = new byte[entry.getHeaderSize() + entry.getName().length() + 1];
        writeCpioHeader(expectedHeader, entry);

        byte[] testData = "12345".getBytes(StandardCharsets.UTF_8);
        out.write(testData);
        out.closeArchiveEntry();

        Assert.assertEquals(expectedHeader, underlingStream.toByteArray());
        Assert.assertEquals(5, underlingStream.size());
    }

    @Test
    public void testPutNextEntryWithNullEntry() {
        expectedException.expect(NullPointerException.class);
        out.putNextEntry(null);
    }

    @Test
    public void testPutNextEntryWithDuplicateEntry() throws IOException {
        CpioArchiveEntry entry1 = new CpioArchiveEntry();
        entry1.setName("testfile");
        entry1.setSize(5);
        out.putNextEntry(entry1);

        expectedException.expect(IOException.class);
        out.putNextEntry(entry1);
    }

    @Test
    public void testPutNextEntryWithInvalidFormat() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setSize(5);
        entry.setFormat(CpioConstants.FORMAT_OLD_ASCII + 1); //invalid format

        expectedException.expect(IllegalArgumentException.class);
        out.putNextEntry(entry);
    }

    @Test
    public void testWriteWithValidArguments() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setSize(5);
        out.putNextEntry(entry);

        byte[] testData = "12345".getBytes(StandardCharsets.UTF_8);
        out.write(testData);
        out.closeArchiveEntry();

        Assert.assertEquals(testData, underlingStream.toByteArray());
    }

    @Test
    public void testWriteWithNullData() {
        expectedException.expect(NullPointerException.class);
        out.write(null);
    }

    @Test
    public void testWriteWithOffsetAndLengthGreaterThanAvailableData() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setSize(5);
        out.putNextEntry(entry);

        byte[] testData = "1234567890".getBytes(StandardCharsets.UTF_8);
        out.write(testData, 5, 5);
        out.closeArchiveEntry();

        expectedException.expect(IOException.class);
        out.write(testData, 5, 6);
    }

    @Test
    public void testCloseArchiveEntryWithValidArguments() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setSize(5);
        out.putNextEntry(entry);

        byte[] testData = "12345".getBytes(StandardCharsets.UTF_8);
        out.write(testData);
        out.closeArchiveEntry();

        Assert.assertEquals(5, underlingStream.size());
    }

    @Test
    public void testCloseArchiveEntryWithInvalidArguments() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setSize(5);
        out.putNextEntry(entry);

        byte[] testData = "12345".getBytes(StandardCharsets.UTF_8);
        out.write(testData);

        expectedException.expect(IllegalStateException.class);
        out.closeArchiveEntry();
        out.closeArchiveEntry();
    }

    @Test
    public void testFinish() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setSize(5);
        out.putNextEntry(entry);

        byte[] testData = "12345".getBytes(StandardCharsets.UTF_8);
        out.write(testData);
        out.closeArchiveEntry();

        out.finish();

        byte[] expectedTrailer = new byte[13];
        CpioUtil.writeCpioTrailer(expectedTrailer);

        Assert.assertEquals(expectedTrailer, underlingStream.toByteArray());
    }

    @Test
    public void testClose() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setSize(5);
        out.putNextEntry(entry);

        byte[] testData = "12345".getBytes(StandardCharsets.UTF_8);
        out.write(testData);
        out.closeArchiveEntry();

        out.close();

        Assert.assertEquals(5, underlingStream.size());
    }

    private void writeCpioHeader(byte[] expectedHeader, CpioArchiveEntry entry) throws IOException {
        writeAsciiLong(expectedHeader, entry.getDevice(), 2, false);
        writeAsciiLong(expectedHeader, entry.getInode(), 2, false);
        writeAsciiLong(expectedHeader, entry.getMode(), 2, false);
        writeAsciiLong(expectedHeader, entry.getUID(), 2, false);
        writeAsciiLong(expectedHeader, entry.getGID(), 2, false);
        writeAsciiLong(expectedHeader, entry.getNumberOfLinks(), 2, false);
        writeAsciiLong(expectedHeader, entry.getRemoteDevice(), 2, false);
        writeAsciiLong(expectedHeader, entry.getTime(), 4, false);
        writeAsciiLong(expectedHeader, entry.getName().length() + 1, 2, false);
        writeAsciiLong(expectedHeader, entry.getSize(), 4, false);
        writeCString(expectedHeader, entry.getName());

        if (entry.getFormat() == CpioConstants.FORMAT_OLD_BINARY) {
            pad(expectedHeader, 2);
        } else if (entry.getFormat() == CpioConstants.FORMAT_NEW || entry.getFormat() == CpioConstants.FORMAT_NEW_CRC) {
            pad(expectedHeader, 4);
        }
    }

    private void writeAsciiLong(byte[] expectedHeader, long number, int length, boolean swapHalfWord) throws IOException {
        String tmpStr = Long.toString(number);
        int index = expectedHeader.length - tmpStr.length() - 1;

        if (length == 1) {
            for (int i = 0; i < tmpStr.length(); i++) {
                expectedHeader[index--] = (byte) tmpStr.charAt(i);
            }
        } else {
            for (int i = 0; i < tmpStr.length(); i++) {
                if (i % 2 == 0) {
                    expectedHeader[index--] = (byte) Character.forDigit(tmpStr.charAt(i), 16);
                } else {
                    expectedHeader[index--] = (byte) (0xFF & ((int) Character.digit(tmpStr.charAt(i), 16) << 4 | Character.digit(tmpStr.charAt(i - 1), 16)));
                }
            }

            if (swapHalfWord) {
                byte temp = expectedHeader[index];
                expectedHeader[index] = expectedHeader[index + 1];
                expectedHeader[index + 1] = temp;
            }
        }
    }

    private void writeCString(byte[] expectedHeader, String name) throws IOException {
        byte[] nameBytes = name.getBytes(StandardCharsets.UTF_8);

        for (int i = 0; i < nameBytes.length; i++) {
            expectedHeader[expectedHeader.length - nameBytes.length - i - 1] = nameBytes[i];
        }

        expectedHeader[expectedHeader.length - nameBytes.length - 1] = 0;
    }

    private void pad(byte[] expectedHeader, long count) throws IOException {
        long skip = count % 4;

        for (int i = 0; i < skip; i++) {
            expectedHeader[expectedHeader.length - 1] = (byte) 0;
        }
    }
}