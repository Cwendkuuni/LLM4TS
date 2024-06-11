import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.archivers.tar.TarOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests for {@link TarArchiveOutputStream}.
 */
public class TarArchiveOutputStreamTest {

    private ByteArrayOutputStream outputStream;
    private TarArchiveOutputStream tarOutputStream;

    @Before
    public void setUp() throws Exception {
        outputStream = new ByteArrayOutputStream();
        tarOutputStream = new TarArchiveOutputStream(outputStream);
    }

    @After
    public void tearDown() throws Exception {
        tarOutputStream.close();
    }

    @Test
    public void testPutArchiveEntryWithSmallFileName() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        byte[] data = "Hello World".getBytes(StandardCharsets.UTF_8);

        tarOutputStream.putNextEntry(entry);
        tarOutputStream.write(data, 0, data.length);
        tarOutputStream.closeArchiveEntry();

        byte[] expected = createExpectedTarRecord("test.txt", data.length);

        byte[] actual = outputStream.toByteArray();
        assertArrayEquals("Unexpected tar record", expected, actual);
    }

    @Test
    public void testPutArchiveEntryWithLongFileName() throws IOException {
        String longFileName = "longLongFileNameWithMoreThanNameLengthCharacters.txt";

        TarArchiveEntry entry = new TarArchiveEntry(longFileName);
        byte[] data = "Long file name test".getBytes(StandardCharsets.UTF_8);

        if (tarOutputStream.getLongFileMode() == TarArchiveOutputStream.LONGFILE_TRUNCATE) {
            // Truncating is not supported for long file names, so we can't test it
            throw new IOException("Long file names can't be truncated");
        }

        tarOutputStream.putNextEntry(entry);
        tarOutputStream.write(data, 0, data.length);
        tarOutputStream.closeArchiveEntry();

        byte[] expected = createExpectedTarRecord(longFileName, data.length);

        byte[] actual = outputStream.toByteArray();
        assertArrayEquals("Unexpected tar record", expected, actual);
    }

    @Test
    public void testPutArchiveEntryWithLongFileNameAndLongFileModeGNU() throws IOException {
        String longFileName = "longLongFileNameWithMoreThanNameLengthCharacters.txt";

        TarArchiveEntry entry = new TarArchiveEntry(longFileName);
        byte[] data = "Long file name test".getBytes(StandardCharsets.UTF_8);

        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);

        tarOutputStream.putNextEntry(entry);
        tarOutputStream.write(data, 0, data.length);
        tarOutputStream.closeArchiveEntry();

        byte[] expected = createExpectedTarRecordWithLongLink(longFileName, data.length);

        byte[] actual = outputStream.toByteArray();
        assertArrayEquals("Unexpected tar record", expected, actual);
    }

    @Test
    public void testWriteDataBeyondSizeSpecifiedInHeader() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        byte[] data = "Hello World".getBytes(StandardCharsets.UTF_8);

        tarOutputStream.putNextEntry(entry);
        tarOutputStream.write(data, 0, data.length);

        byte[] extraData = "Extra data".getBytes(StandardCharsets.UTF_8);
        tarOutputStream.write(extraData, 0, extraData.length);

        tarOutputStream.closeArchiveEntry();

        byte[] expected = createExpectedTarRecord("test.txt", data.length + extraData.length);

        byte[] actual = outputStream.toByteArray();
        assertArrayEquals("Unexpected tar record", expected, actual);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithTooLongFileName() throws IOException {
        String longFileName = "longLongFileNameWithMoreThanNameLengthPlusOneCharacter.txt";

        TarArchiveEntry entry = new TarArchiveEntry(longFileName);
        byte[] data = "Long file name test".getBytes(StandardCharsets.UTF_8);

        tarOutputStream.putNextEntry(entry);
    }

    @Test
    public void testCloseAfterPutNextEntryWithNoData() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");

        tarOutputStream.putNextEntry(entry);
        tarOutputStream.closeArchiveEntry();

        byte[] expected = createExpectedTarRecord("test.txt", 0);

        byte[] actual = outputStream.toByteArray();
        assertArrayEquals("Unexpected tar record", expected, actual);
    }

    @Test
    public void testFinish() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        byte[] data = "Hello World".getBytes(StandardCharsets.UTF_8);

        tarOutputStream.putNextEntry(entry);
        tarOutputStream.write(data, 0, data.length);
        tarOutputStream.closeArchiveEntry();

        tarOutputStream.finish();

        byte[] actual = outputStream.toByteArray();

        byte[] expectedEOF1 = new byte[TarConstants.TAR_REC_SIZE];
        byte[] expectedEOF2 = new byte[TarConstants.TAR_REC_SIZE];
        for (int i = 0; i < expectedEOF1.length; ++i) {
            expectedEOF1[i] = 0;
            expectedEOF2[i] = 0;
        }

        byte[] expected = new byte[outputStream.size() + 2 * TarConstants.TAR_REC_SIZE];
        System.arraycopy(expectedEOF1, 0, expected, 0, TarConstants.TAR_REC_SIZE);
        System.arraycopy(expectedEOF2, 0, expected, TarConstants.TAR_REC_SIZE, TarConstants.TAR_REC_SIZE);
        System.arraycopy(outputStream.toByteArray(), 0, expected, TarConstants.TAR_REC_SIZE * 2, outputStream.size());

        assertArrayEquals("Unexpected tar archive", expected, actual);
    }

    @Test
    public void testCloseAfterAllEntriesAreWritten() throws IOException {
        TarArchiveEntry entry1 = new TarArchiveEntry("test1.txt");
        byte[] data1 = "Hello World1".getBytes(StandardCharsets.UTF_8);

        TarArchiveEntry entry2 = new TarArchiveEntry("test2.txt");
        byte[] data2 = "Hello World2".getBytes(StandardCharsets.UTF_8);

        tarOutputStream.putNextEntry(entry1);
        tarOutputStream.write(data1, 0, data1.length);
        tarOutputStream.closeArchiveEntry();

        tarOutputStream.putNextEntry(entry2);
        tarOutputStream.write(data2, 0, data2.length);
        tarOutputStream.closeArchiveEntry();

        tarOutputStream.finish();

        byte[] actual = outputStream.toByteArray();

        byte[] expectedEOF1 = new byte[TarConstants.TAR_REC_SIZE];
        byte[] expectedEOF2 = new byte[TarConstants.TAR_REC_SIZE];
        for (int i = 0; i < expectedEOF1.length; ++i) {
            expectedEOF1[i] = 0;
            expectedEOF2[i] = 0;
        }

        byte[] test1 = createExpectedTarRecord("test1.txt", data1.length);
        byte[] test2 = createExpectedTarRecord("test2.txt", data2.length);

        byte[] expected = new byte[outputStream.size() + 3 * TarConstants.TAR_REC_SIZE];
        System.arraycopy(expectedEOF1, 0, expected, 0, TarConstants.TAR_REC_SIZE);
        System.arraycopy(expectedEOF1, 0, expected, TarConstants.TAR_REC_SIZE, TarConstants.TAR_REC_SIZE);
        System.arraycopy(expectedEOF2, 0, expected, 2 * TarConstants.TAR_REC_SIZE, TarConstants.TAR_REC_SIZE);
        System.arraycopy(test1, 0, expected, 3 * TarConstants.TAR_REC_SIZE, test1.length);
        System.arraycopy(test2, 0, expected, 3 * TarConstants.TAR_REC_SIZE + test1.length, test2.length);
        System.arraycopy(outputStream.toByteArray(), 0, expected, 3 * TarConstants.TAR_REC_SIZE + test1.length + test2.length, outputStream.size() - test1.length - test2.length);

        assertArrayEquals("Unexpected tar archive", expected, actual);
    }

    @Test
    public void testWriteToFile() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        byte[] data = "Hello World".getBytes(StandardCharsets.UTF_8);

        File outputFile = new File("output.tar");
        tarOutputStream = new TarArchiveOutputStream(outputFile);

        tarOutputStream.putNextEntry(entry);
        tarOutputStream.write(data, 0, data.length);
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.finish();

        byte[] actual = IOUtils.toByteArray(new FileInputStream(outputFile));

        byte[] expected = createExpectedTarRecord("test.txt", data.length);

        assertArrayEquals("Unexpected tar record", expected, actual);
    }

    private byte[] createExpectedTarRecord(String fileName, int fileSize) {
        byte[] nameBytes = fileName.getBytes(StandardCharsets.UTF_8);

        byte[] header = new byte[1024];
        int namePos = 0;
        System.arraycopy(header, TarConstants.MAGIC, header, 0, TarConstants.MAGIC.length);

        header[TarConstants.NAME_LENGTH] = (byte) nameBytes.length;
        header[TarConstants.FILE_SIZE] = (byte) (fileSize >> 24);
        header[TarConstants.FILE_SIZE + 1] = (byte) (fileSize >> 16);
        header[TarConstants.FILE_SIZE + 2] = (byte) (fileSize >> 8);
        header[TarConstants.FILE_SIZE + 3] = (byte) fileSize;
        System.arraycopy(nameBytes, 0, header, namePos, nameBytes.length);
        namePos += nameBytes.length;

        header[TarConstants.MODE] = (byte) 0644;
        header[TarConstants.UID] = (byte) 0;
        header[TarConstants.GID] = (byte) 0;

        byte[] record = new byte[TarConstants.TAR_REC_SIZE + fileSize];
        System.arraycopy(header, 0, record, 0, TarConstants.TAR_REC_SIZE);
        System.arraycopy(nameBytes, 0, record, TarConstants.NAME_LENGTH, nameBytes.length);

        return record;
    }

    private byte[] createExpectedTarRecordWithLongLink(String fileName, int fileSize) {
        byte[] nameBytes = fileName.getBytes(StandardCharsets.UTF_8);
        byte[] longLinkNameBytes = fileName.getBytes(StandardCharsets.UTF_8);

        byte[] header = new byte[2048];
        int namePos = 0;
        int linkNamePos = TarConstants.NAME_LENGTH + TarConstants.HEADER_SIZE;

        System.arraycopy(header, TarConstants.MAGIC, header, 0, TarConstants.MAGIC.length);
        System.arraycopy(header, TarConstants.MAGIC, header, TarConstants.TAR_GNU_LONGLINK_MAGIC, TarConstants.TAR_GNU_LONGLINK_MAGIC.length);

        header[TarConstants.NAME_LENGTH] = (byte) (nameBytes.length + TarConstants.NAME_LENGTH + Long.BYTES * 2);
        header[TarConstants.FILE_SIZE] = (byte) (fileSize >> 24);
        header[TarConstants.FILE_SIZE + 1] = (byte) (fileSize >> 16);
        header[TarConstants.FILE_SIZE + 2] = (byte) (fileSize >> 8);
        header[TarConstants.FILE_SIZE + 3] = (byte) fileSize;
        System.arraycopy(nameBytes, 0, header, namePos, nameBytes.length);
        namePos += nameBytes.length;

        long linkNameSize = Long.BYTES * 2 + nameBytes.length;
        header[linkNamePos] = (byte) (linkNameSize >> 24);
        header[linkNamePos + 1] = (byte) (linkNameSize >> 16);
        header[linkNamePos + 2] = (byte) (linkNameSize >> 8);
        header[linkNamePos + 3] = (byte) linkNameSize;
        System.arraycopy(longLinkNameBytes, 0, header, linkNamePos + TarConstants.NAME_LENGTH, longLinkNameBytes.length);
        System.arraycopy(longLinkNameBytes, 0, header, linkNamePos + TarConstants.NAME_LENGTH + Long.BYTES, Long.BYTES);

        header[TarConstants.MODE] = (byte) 0644;
        header[TarConstants.UID] = (byte) 0;
        header[TarConstants.GID] = (byte) 0;

        byte[] record = new byte[header.length + fileSize];
        System.arraycopy(header, 0, record, 0, header.length);

        return record;
    }

    private void assertArrayEquals(String msg, byte[] expected, byte[] actual) {
        assertEquals("Unexpected byte array: " + msg, expected, actual);
    }
}