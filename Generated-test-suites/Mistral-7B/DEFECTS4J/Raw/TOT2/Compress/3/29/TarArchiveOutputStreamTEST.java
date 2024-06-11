import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TarArchiveOutputStreamTest {

    private ByteArrayOutputStream outputStream;
    private TarArchiveOutputStream tarArchiveOutputStream;

    @Before
    public void setUp() throws Exception {
        outputStream = new ByteArrayOutputStream();
        tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
    }

    @After
    public void tearDown() throws Exception {
        if (tarArchiveOutputStream != null) {
            tarArchiveOutputStream.finish();
            IOUtils.closeQuietly(tarArchiveOutputStream);
        }
        outputStream = null;
    }

    @Test
    public void testPutEmptyArchiveEntry() throws Exception {
        // Put an empty archive entry
        TarArchiveEntry emptyEntry = new TarArchiveEntry("testEmptyEntry.txt");
        tarArchiveOutputStream.putArchiveEntry(emptyEntry);
        tarArchiveOutputStream.closeArchiveEntry();

        // Verify the header record is written correctly
        byte[] headerRecord = tarArchiveOutputStream.recordBuf;
        assertEquals(TarConstants.MAGIC_NUMBER, headerRecord[0], 0);
        assertEquals(TarConstants.HEADER_TYPE, headerRecord[1], 0);
        assertEquals(TarConstants.NAME_LENGTH + 1, headerRecord[2], 0);
        assertEquals(0, headerRecord[3], 0);
        assertEquals(0, headerRecord[4], 0);
        assertEquals(0, headerRecord[5], 0);
        assertEquals(0, headerRecord[6], 0);
        assertEquals(0, headerRecord[7], 0);

        // Verify the null terminated name is written correctly
        final String emptyEntryName = "testEmptyEntry.txt";
        byte[] emptyEntryNameBytes = emptyEntryName.getBytes(StandardCharsets.UTF_8);
        assertEquals(emptyEntryNameBytes.length, headerRecord[8], 0);
        for (int i = 0; i < emptyEntryNameBytes.length; i++) {
            assertEquals(emptyEntryNameBytes[i], headerRecord[9 + i], 0);
        }

        assertEquals(0, headerRecord[emptyEntryNameBytes.length + 8], 0);

        // Verify the file size is written correctly as 0
        assertEquals(0, headerRecord[emptyEntryNameBytes.length + 9], 0);
        assertEquals(0, headerRecord[emptyEntryNameBytes.length + 10], 0);
    }

    @Test
    public void testPutArchiveEntryWithNameLongerThanNAMELEN() throws Exception {
        // Set the long file mode to LONGFILE_GNU
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);

        // Create a long entry name
        String longEntryName = new String(new char[TarConstants.NAMELEN + 1])
                .replace('\0', 'a');

        // Put an archive entry with a long name
        TarArchiveEntry longEntry = new TarArchiveEntry("testLongEntry.txt", longEntryName.getBytes(StandardCharsets.UTF_8));
        tarArchiveOutputStream.putArchiveEntry(longEntry);
        tarArchiveOutputStream.closeArchiveEntry();

        // Verify the long name is written as a LongLink entry
        assertNotNull(findLongLinkEntry(longEntryName));
    }

    @Test
    public void testPutArchiveEntryWithNameLongerThanNAMELENAndLargeFileSize() throws Exception {
        // Set the long file mode to LONGFILE_GNU
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);

        // Create a long entry name and a large file size
        String longEntryName = new String(new char[TarConstants.NAMELEN + 1])
                .replace('\0', 'a');
        long fileSize = 1024 * 1024; // 1 MB

        // Put an archive entry with a long name and a large file size
        ByteArrayOutputStream largeFileOutputStream = new ByteArrayOutputStream((int) fileSize);
        tarArchiveOutputStream.putArchiveEntry(new TarArchiveEntry("testLargeEntry.txt", longEntryName.getBytes(StandardCharsets.UTF_8)), largeFileOutputStream);
        tarArchiveOutputStream.write(new byte[512]); // write the first record
        byte[] data = new byte[fileSize];
        new File("testData.txt").getBytes(StandardCharsets.UTF_8, data);
        tarArchiveOutputStream.write(data); // write the data
        tarArchiveOutputStream.closeArchiveEntry();

        // Verify the long name is written as a LongLink entry
        assertNotNull(findLongLinkEntry(longEntryName));

        // Verify the data is written correctly
        assertEquals(fileSize, tarArchiveOutputStream.currBytes);
    }

    @Test(expected = RuntimeException.class)
    public void testPutArchiveEntryWithNameLongerThanNAMELENAndLongFileModeIsTruncate() throws Exception {
        // Set the long file mode to LONGFILE_TRUNCATE
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);

        // Create a long entry name
        String longEntryName = new String(new char[TarConstants.NAMELEN + 1])
                .replace('\0', 'a');

        // Try to put an archive entry with a long name
        TarArchiveEntry longEntry = new TarArchiveEntry("testLongEntry.txt", longEntryName.getBytes(StandardCharsets.UTF_8));
        tarArchiveOutputStream.putArchiveEntry(longEntry);

        // This should throw a RuntimeException
    }

    private TarArchiveEntry findLongLinkEntry(String name) {
        byte[] headerRecord = null;
        for (int i = 0; i < outputStream.size(); i += TarConstants.RECORD_SIZE) {
            byte[] record = new byte[TarConstants.RECORD_SIZE];
            System.arraycopy(outputStream.toByteArray(), i, record, 0, TarConstants.RECORD_SIZE);

            if (record[0] == TarConstants.MAGIC_NUMBER &&
                    record[1] == TarConstants.LF_GNUTYPE_LONGNAME &&
                    name.length() == new String(new byte[TarConstants.NAMELEN + 1][], 0, TarConstants.NAMELEN, StandardCharsets.UTF_8).equals(new String(record, 8, TarConstants.NAMELEN, StandardCharsets.UTF_8))) {
                headerRecord = record;
                break;
            }
        }

        if (headerRecord != null) {
            return new TarArchiveEntry(new ByteArrayInputStream(headerRecord), TarConstants.GNU_LONGLINK);
        }

        return null;
    }
}