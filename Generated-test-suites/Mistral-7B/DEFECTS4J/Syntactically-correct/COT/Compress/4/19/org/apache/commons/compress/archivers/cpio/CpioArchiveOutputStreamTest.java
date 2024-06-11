import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.CpioConstants;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.utils.ArchiveUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Test class for CpioArchiveOutputStream
 */
public class CpioArchiveOutputStreamTest {

    private OutputStream outputStream;
    private CpioArchiveOutputStream cpioArchiveOutputStream;

    @Before
    public void setUp() throws Exception {
        outputStream = new ByteArrayOutputStream();
        cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);
    }

    @Test
    public void testPutArchiveEntry_ValidEntry() throws IOException {
        String entryName = "testFile.txt";
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(entryName);
        entry.setSize(10);

        cpioArchiveOutputStream.putArchiveEntry(entry);

        byte[] expectedHeader = ArchiveUtils.toByteArray(CpioConstants.MAGIC_NEW);
        expectedHeader = ArrayUtils.addAll(expectedHeader, writeEntryHeader(entry));
        expectedHeader = ArrayUtils.addAll(expectedHeader, new byte[entry.getName().length() + 1]);

        Assert.assertArrayEquals(expectedHeader, outputStream.toByteArray());
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntry_DuplicateEntry() throws IOException {
        String entryName = "testFile.txt";
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(entryName);

        cpioArchiveOutputStream.putArchiveEntry(entry);
        cpioArchiveOutputStream.putArchiveEntry(entry);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutArchiveEntry_InvalidFormat() throws IOException {
        cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, 123);
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testFile.txt");

        cpioArchiveOutputStream.putArchiveEntry(entry);
    }

    @Test
    public void testPutArchiveEntry_WriteFileContent() throws IOException {
        String entryName = "testFile.txt";
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(entryName);
        entry.setSize(10);

        cpioArchiveOutputStream.putArchiveEntry(entry);

        byte[] testContent = new byte[]{'T', 'e', 's', 't', 'F', 'i', 'l', 'e'};
        cpioArchiveOutputStream.write(testContent, 0, testContent.length);

        byte[] expectedHeader = ArchiveUtils.toByteArray(CpioConstants.MAGIC_NEW);
        expectedHeader = ArrayUtils.addAll(expectedHeader, writeEntryHeader(entry));
        expectedHeader = ArrayUtils.addAll(expectedHeader, new byte[entry.getName().length() + 1]);
        expectedHeader = ArrayUtils.addAll(expectedHeader, testContent);

        Assert.assertArrayEquals(expectedHeader, outputStream.toByteArray());
    }

    @Test
    public void testWrite_WriteValidData() throws IOException {
        String entryName = "testFile.txt";
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(entryName);
        entry.setSize(10);

        cpioArchiveOutputStream.putArchiveEntry(entry);
        byte[] testContent = new byte[]{'T', 'e', 's', 't', 'F', 'i', 'l', 'e'};
        cpioArchiveOutputStream.write(testContent, 0, testContent.length);

        byte[] expectedHeader = ArchiveUtils.toByteArray(CpioConstants.MAGIC_NEW);
        expectedHeader = ArrayUtils.addAll(expectedHeader, writeEntryHeader(entry));
        expectedHeader = ArrayUtils.addAll(expectedHeader, new byte[entry.getName().length() + 1]);
        expectedHeader = ArrayUtils.addAll(expectedHeader, testContent);

        Assert.assertArrayEquals(expectedHeader, outputStream.toByteArray());
    }

    @Test(expected = IOException.class)
    public void testWrite_WriteBeyondEntrySize() throws IOException {
        String entryName = "testFile.txt";
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(entryName);
        entry.setSize(10);

        cpioArchiveOutputStream.putArchiveEntry(entry);

        byte[] testContent = new byte[]{'T', 'e', 's', 't', 'F', 'i', 'l', 'e'};
        cpioArchiveOutputStream.write(testContent, 0, testContent.length + 1);
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        String entryName = "testFile.txt";
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(entryName);
        entry.setSize(10);

        cpioArchiveOutputStream.putArchiveEntry(entry);
        byte[] testContent = new byte[]{'T', 'e', 's', 't', 'F', 'i', 'l', 'e'};
        cpioArchiveOutputStream.write(testContent, 0, testContent.length);
        cpioArchiveOutputStream.closeArchiveEntry();

        byte[] expectedHeader = ArchiveUtils.toByteArray(CpioConstants.MAGIC_NEW);
        expectedHeader = ArrayUtils.addAll(expectedHeader, writeEntryHeader(entry));
        expectedHeader = ArrayUtils.addAll(expectedHeader, new byte[entry.getName().length() + 1]);
        expectedHeader = ArrayUtils.addAll(expectedHeader, testContent);
        expectedHeader = ArrayUtils.addAll(expectedHeader, entry.getDataPadCount() > 0 ? new byte[entry.getDataPadCount()] : new byte[0]);

        Assert.assertArrayEquals(expectedHeader, outputStream.toByteArray());
    }

    @Test
    public void testFinish() throws IOException {
        String entryName = "testFile.txt";
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(entryName);
        entry.setSize(10);

        cpioArchiveOutputStream.putArchiveEntry(entry);
        byte[] testContent = new byte[]{'T', 'e', 's', 't', 'F', 'i', 'l', 'e'};
        cpioArchiveOutputStream.write(testContent, 0, testContent.length);

        cpioArchiveOutputStream.finish();

        byte[] expectedHeader = ArchiveUtils.toByteArray(CpioConstants.MAGIC_NEW);
        expectedHeader = ArrayUtils.addAll(expectedHeader, writeEntryHeader(entry));
        expectedHeader = ArrayUtils.addAll(expectedHeader, new byte[entry.getName().length() + 1]);
        expectedHeader = ArrayUtils.addAll(expectedHeader, testContent);
        expectedHeader = ArrayUtils.addAll(expectedHeader, entry.getDataPadCount() > 0 ? new byte[entry.getDataPadCount()] : new byte[0]);
        expectedHeader = ArrayUtils.addAll(expectedHeader, CpioConstants.CPIO_TRAILER.getBytes());

        Assert.assertArrayEquals(expectedHeader, outputStream.toByteArray());
    }

    private byte[] writeEntryHeader(CpioArchiveEntry entry) {
        Map<String, CpioArchiveEntry> entries = new HashMap<>();
        entries.put(entry.getName(), entry);

        byte[] formatSpecificData = new byte[0];

        switch (entry.getFormat()) {
            case FORMAT_NEW:
                formatSpecificData = writeNewEntryFormatSpecificData(entry);
                break;
            case FORMAT_NEW_CRC:
                formatSpecificData = writeNewCrcEntryFormatSpecificData(entry);
                break;
            case FORMAT_OLD_ASCII:
                formatSpecificData = writeOldAsciiEntryFormatSpecificData(entry);
                break;
            case FORMAT_OLD_BINARY:
                formatSpecificData = writeOldBinaryEntryFormatSpecificData(entry);
                break;
            default:
                throw new RuntimeException("Unsupported format: " + entry.getFormat());
        }

        byte[] nameBytes = entry.getName().getBytes();
        int nameLength = nameBytes.length;
        int padCount = entry.getHeaderPadCount();

        return ArrayUtils.addAll(
                new byte[]{INODE},
                new byte[]{entry.getMode().getValue() & 0xFF},
                new byte[]{entry.getUID().getValue() & 0xFF},
                new byte[]{entry.getGID().getValue() & 0xFF},
                new byte[]{entry.getNumberOfLinks()},
                new byte[]{entry.getTime().getTime() & 0xFF},
                new byte[]{entry.getTime().getTime() >> 8 & 0xFF},
                new byte[]{entry.getTime().getTime() >> 16 & 0xFF},
                new byte[]{entry.getTime().getTime() >> 24 & 0xFF},
                new byte[]{entry.getSize() & 0xFF},
                new byte[]{entry.getSize() >> 8 & 0xFF},
                new byte[]{entry.getSize() >> 16 & 0xFF},
                new byte[]{entry.getSize() >> 24 & 0xFF},
                new byte[]{entry.getDeviceMaj()},
                new byte[]{entry.getDeviceMin()},
                new byte[]{entry.getRemoteDeviceMaj()},
                new byte[]{entry.getRemoteDeviceMin()},
                new byte[]{nameLength},
                new byte[]{nameLength + 1},
                formatSpecificData,
                nameBytes
        );
    }

    private byte[] writeNewEntryFormatSpecificData(CpioArchiveEntry entry) {
        int chksum = 0;
        for (byte b : entry.getName().getBytes()) {
            chksum += b;
        }
        return new byte[]{chksum & 0xFF, (chksum >> 8) & 0xFF};
    }

    private byte[] writeNewCrcEntryFormatSpecificData(CpioArchiveEntry entry) {
        byte[] entryBytes = writeEntryHeader(entry);
        byte[] contentBytes = entry.getContent().getBytes();

        int crc = 0;
        for (byte b : entryBytes) {
            crc ^= b;
        }
        for (byte b : contentBytes) {
            crc = (crc >> 8) ^ b;
            crc = crc << 8 ^ b;
        }

        byte[] crcBytes = new byte[2];
        crcBytes[0] = (crc & 0xFF);
        crcBytes[1] = (crc >> 8) & 0xFF;

        return ArrayUtils.addAll(entryBytes, crcBytes);
    }

    private byte[] writeOldAsciiEntryFormatSpecificData(CpioArchiveEntry entry) {
        byte[] nameBytes = entry.getName().getBytes();

        return ArrayUtils.concat(
                new byte[]{entry.getDevice() & 0xFF, entry.getDevice() >> 8 & 0xFF},
                new byte[]{entry.getInode() & 0xFF, entry.getInode() >> 8 & 0xFF},
                new byte[]{entry.getMode()},
                new byte[]{entry.getUID() & 0xFF, entry.getUID() >> 8 & 0xFF},
                new byte[]{entry.getGID() & 0xFF, entry.getGID() >> 8 & 0xFF},
                new byte[]{entry.getNumberOfLinks()},
                new byte[]{entry.getRemoteDevice() & 0xFF, entry.getRemoteDevice() >> 8 & 0xFF},
                new byte[]{entry.getTime().getTime() & 0xFF},
                new byte[]{entry.getTime().getTime() >> 8 & 0xFF},
                new byte[]{entry.getTime().getTime() >> 16 & 0xFF},
                new byte[]{entry.getTime().getTime() >> 20 & 0xFF},
                new byte[]{entry.getSize() & 0xFF},
                new byte[]{entry.getSize() >> 8 & 0xFF},
                new byte[]{entry.getSize() >> 16 & 0xFF},
                new byte[]{entry.getSize() >> 20 & 0xFF},
                new byte[]{nameBytes[0]},
                new byte[]{nameBytes[1]},
                new byte[]{nameBytes[2]},
                new byte[]{nameBytes[3]},
                new byte[]{nameBytes[4]},
                new byte[]{nameBytes[5]},
                new byte[]{nameBytes[6]},
                new byte[]{nameBytes[7]},
                new byte[]{nameBytes[8]},
                new byte[]{nameBytes[9]},
                new byte[]{nameBytes[10]},
                new byte[]{nameBytes[11]},
                new byte[]{nameBytes[12]},
                new byte[]{nameBytes[13]},
                new byte[]{nameBytes[14]},
                new byte[]{nameBytes[15]},
                new byte[]{(byte) 0x00}
        );
    }

    private byte[] writeOldBinaryEntryFormatSpecificData(CpioArchiveEntry entry) {
        byte[] nameBytes = entry.getName().getBytes();

        return ArrayUtils.concat(
                new byte[]{entry.getDevice() & 0xFF, entry.getDevice() >> 8 & 0xFF},
                new byte[]{entry.getInode() & 0xFF, entry.getInode() >> 8 & 0xFF},
                new byte[]{entry.getMode()},
                new byte[]{entry.getUID() & 0xFF, entry.getUID() >> 8 & 0xFF},
                new byte[]{entry.getGID() & 0xFF, entry.getGID() >> 8 & 0xFF},
                new byte[]{entry.getNumberOfLinks()},
                new byte[]{entry.getRemoteDevice() & 0xFF, entry.getRemoteDevice() >> 8 & 0xFF},
                new byte[]{entry.getTime().getTime() & 0xFF, entry.getTime().getTime() >> 8 & 0xFF},
                new byte[]{entry.getTime().getTime() >> 16 & 0xFF, entry.getTime().getTime() >> 20 & 0xFF},
                new byte[]{entry.getSize() & 0xFF, entry.getSize() >> 8 & 0xFF},
                new byte[]{entry.getSize() >> 16 & 0xFF, entry.getSize() >> 20 & 0xFF},
                nameBytes
        );
    }
}