import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.CpioConstants;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.utils.CpioUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CpioArchiveOutputStreamTest {

    private CpioArchiveOutputStream outputStream;
    private ByteArrayOutputStream filterOutputStream;

    @Before
    public void setUp() throws IOException {
        filterOutputStream = new ByteArrayOutputStream();
        outputStream = new CpioArchiveOutputStream(filterOutputStream, CpioConstants.FORMAT_NEW);
    }

    @After
    public void tearDown() throws IOException {
        outputStream.close();
    }

    @Test
    public void testPutNextEntryWithValidData() throws IOException {
        String testFileName = "testfile";
        String testFileContents = "12345";
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(testFileName);
        entry.setFileSize(testFileContents.length());

        outputStream.putNextEntry(entry);
        outputStream.write(testFileContents.getBytes());
        outputStream.closeArchiveEntry();

        byte[] expectedCpioHeader = new byte[entry.getHeaderSize() + entry.getName().length() + 1];
        writeCpioHeader(expectedCpioHeader, entry);

        byte[] actualCpioHeader = Arrays.copyOfRange(filterOutputStream.toByteArray(), 0, entry.getHeaderSize() + entry.getName().length() + 1);
        assertEquals("CPIO header mismatch", expectedCpioHeader, actualCpioHeader);

        assertEquals(testFileName, outputStream.getNextEntry().getName());
    }

    @Test(expected = IOException.class)
    public void testPutNextEntryWithDuplicateName() throws IOException {
        String testFileName = "testfile";
        String testFileContents = "12345";
        CpioArchiveEntry entry1 = new CpioArchiveEntry();
        entry1.setName(testFileName);
        entry1.setFileSize(testFileContents.length());

        outputStream.putNextEntry(entry1);
        outputStream.write(testFileContents.getBytes());
        outputStream.closeArchiveEntry();

        CpioArchiveEntry entry2 = new CpioArchiveEntry();
        entry2.setName(testFileName);

        outputStream.putNextEntry(entry2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutNextEntryWithNegativeFormat() throws IOException {
        CpioArchiveOutputStream outputStream = new CpioArchiveOutputStream(new ByteArrayOutputStream(), (short) -1);
    }

    @Test(expected = IOException.class)
    public void testWriteWithClosedStream() throws IOException {
        outputStream.close();
        outputStream.write(1);
    }

    @Test(expected = IOException.class)
    public void testWriteWithInvalidData() throws IOException {
        outputStream.putNextEntry(new CpioArchiveEntry());
        outputStream.write(-1);
    }

    @Test
    public void testWriteWithValidData() throws IOException {
        String testFileContents = "12345";
        outputStream.putNextEntry(new CpioArchiveEntry());
        outputStream.write(testFileContents.getBytes());
        outputStream.closeArchiveEntry();

        byte[] actualCpioData = Arrays.copyOfRange(filterOutputStream.toByteArray(), 0, outputStream.size());
        byte[] expectedCpioData = CpioUtil.bytesFromHex("60000001600000014b6574656c696e6520746869737065203132333435");
        assertEquals("CPIO data mismatch", expectedCpioData, actualCpioData);
    }

    @Test
    public void testFinish() throws IOException {
        outputStream.finish();

        byte[] expectedTrailer = new byte[512];
        Arrays.fill(expectedTrailer, (byte) 0xFF);
        byte[] actualTrailer = Arrays.copyOfRange(filterOutputStream.toByteArray(), filterOutputStream.size() - 512, filterOutputStream.size());
        assertTrue("Trailer mismatch", Arrays.equals(expectedTrailer, actualTrailer));
    }

    private void writeCpioHeader(byte[] buffer, CpioArchiveEntry entry) throws IOException {
        writeAsciiLong(buffer, entry.getInode(), 8);
        writeAsciiLong(buffer, entry.getMode(), 8);
        writeAsciiLong(buffer, entry.getUID(), 8);
        writeAsciiLong(buffer, entry.getGID(), 8);
        writeAsciiLong(buffer, entry.getNumberOfLinks(), 8);
        writeAsciiLong(buffer, entry.getTime(), 8);
        writeAsciiLong(buffer, entry.getSize(), 8);
        writeAsciiLong(buffer, entry.getDeviceMaj(), 8);
        writeAsciiLong(buffer, entry.getDeviceMin(), 8);
        writeAsciiLong(buffer, entry.getRemoteDeviceMaj(), 8);
        writeAsciiLong(buffer, entry.getRemoteDeviceMin(), 8);
        writeAsciiLong(buffer, entry.getName().length() + 1, 8);
        writeAsciiLong(buffer, entry.getChksum(), 8);
        writeCString(buffer, entry.getName());
        pad(buffer, 512);
    }

    private void writeCString(byte[] buffer, String name) {
        for (int i = 0; i < name.length(); i++) {
            buffer[i] = (byte) name.charAt(i);
        }
        buffer[name.length()] = 0;
    }

    private void pad(byte[] buffer, int length) {
        for (int i = 0; i < length % 4; i++) {
            buffer[buffer.length - 1] = 0;
        }
    }

    private void writeAsciiLong(byte[] buffer, long number, int length) {
        String numberString = Long.toString(number, length > 16 ? 16 : 8);
        int index = buffer.length - (numberString.length() + 1);
        for (int i = 0; i < numberString.length(); i++) {
            buffer[index - i] = (byte) numberString.charAt(i);
        }
    }
}