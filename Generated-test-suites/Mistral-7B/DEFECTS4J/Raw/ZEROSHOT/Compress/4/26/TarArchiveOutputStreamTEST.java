import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class TarArchiveOutputStreamTest {

    private ByteArrayOutputStream outputStream;
    private TarArchiveOutputStream tarArchiveOutputStream;

    @Before
    public void setUp() throws Exception {
        outputStream = new ByteArrayOutputStream();
        tarArchiveOutputStream = new TarArchiveOutputStream(outputStream, 1024, 512);
    }

    @After
    public void cleanUp() throws Exception {
        tarArchiveOutputStream.close();
    }

    @Test
    public void testConstructor() throws Exception {
        // Test default constructor
        TarArchiveOutputStream tarArchiveOutputStream1 = new TarArchiveOutputStream(Mockito.mock(OutputStream.class));

        // Test constructor with record size and block size
        TarArchiveOutputStream tarArchiveOutputStream2 = new TarArchiveOutputStream(Mockito.mock(OutputStream.class), 1024, 512);
    }

    @Test
    public void testSetLongFileMode() throws Exception {
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarArchiveOutputStream.longFileMode);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetLongFileModeInvalidMode() throws Exception {
        tarArchiveOutputStream.setLongFileMode(4);
    }

    @Test
    public void testFinish() throws Exception {
        // Write an entry
        TarArchiveEntry entry = new TarArchiveEntry("test.txt", "test content".getBytes(StandardCharsets.UTF_8));
        tarArchiveOutputStream.putArchiveEntry(entry);

        // Write entry content
        byte[] content = "test content".getBytes(StandardCharsets.UTF_8);
        tarArchiveOutputStream.write(content, 0, content.length);

        // Close entry and finish archive
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.finish();

        byte[] bytes = outputStream.toByteArray();

        // Check that EOF records have been written
        assertEquals(2, CountBytes(bytes, (byte) 0));
    }

    @Test
    public void testClose() throws Exception {
        // Write an entry
        TarArchiveEntry entry = new TarArchiveEntry("test.txt", "test content".getBytes(StandardCharsets.UTF_8));
        tarArchiveOutputStream.putArchiveEntry(entry);

        // Write entry content
        byte[] content = "test content".getBytes(StandardCharsets.UTF_8);
        tarArchiveOutputStream.write(content, 0, content.length);

        // Close entry and archive
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.close();

        byte[] bytes = outputStream.toByteArray();

        // Check that EOF records have been written
        assertEquals(2, CountBytes(bytes, (byte) 0));
    }

    @Test
    public void testGetRecordSize() throws Exception {
        assertEquals(512, tarArchiveOutputStream.getRecordSize());
    }

    @Test
    public void testPutArchiveEntry() throws Exception {
        // Write an entry
        TarArchiveEntry entry = new TarArchiveEntry("test.txt", "test content".getBytes(StandardCharsets.UTF_8));
        tarArchiveOutputStream.putArchiveEntry(entry);

        // Write entry content
        byte[] content = "test content".getBytes(StandardCharsets.UTF_8);
        tarArchiveOutputStream.write(content, 0, content.length);

        // Close entry
        tarArchiveOutputStream.closeArchiveEntry();

        byte[] bytes = outputStream.toByteArray();

        // Check that header record has been written
        byte[] expectedHeader = entry.getHeaderBytes();
        assertEquals(expectedHeader.length, CountBytes(bytes, expectedHeader[0]));
    }

    @Test
    public void testCloseArchiveEntry() throws Exception {
        // Write an entry
        TarArchiveEntry entry = new TarArchiveEntry("test.txt", "test content".getBytes(StandardCharsets.UTF_8));
        tarArchiveOutputStream.putArchiveEntry(entry);

        // Write entry content
        byte[] content = "test content".getBytes(StandardCharsets.UTF_8);
        tarArchiveOutputStream.write(content, 0, content.length);

        // Close entry
        tarArchiveOutputStream.closeArchiveEntry();

        byte[] bytes = outputStream.toByteArray();

        // Check that header and data records have been written
        byte[] expectedHeader = entry.getHeaderBytes();
        assertEquals(expectedHeader.length, CountBytes(bytes, expectedHeader[0]));
        assertEquals(content.length, CountBytes(bytes, (byte) 0, content.length));
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryInvalidArchiveEntry() throws Exception {
        tarArchiveOutputStream.putArchiveEntry(new Object());
    }

    @Test
    public void testWrite() throws Exception {
        // Write an entry
        TarArchiveEntry entry = new TarArchiveEntry("test.txt", "test content".getBytes(StandardCharsets.UTF_8));
        tarArchiveOutputStream.putArchiveEntry(entry);

        // Write entry content
        byte[] content = "test content".getBytes(StandardCharsets.UTF_8);
        tarArchiveOutputStream.write(content, 0, content.length);

        byte[] bytes = outputStream.toByteArray();

        // Check that header and data records have been written
        byte[] expectedHeader = entry.getHeaderBytes();
        assertEquals(expectedHeader.length, CountBytes(bytes, expectedHeader[0]));
        assertEquals(content.length, CountBytes(bytes, (byte) 0, content.length));
    }

    @Test
    public void testWriteWithAssembling() throws Exception {
        // Write an entry
        TarArchiveEntry entry = new TarArchiveEntry("test.txt", "test content".getBytes(StandardCharsets.UTF_8));
        tarArchiveOutputStream.putArchiveEntry(entry);

        byte[] content = "test content".getBytes(StandardCharsets.UTF_8);

        // Write smaller chunks of data
        for (int i = 0; i < content.length; i += 128) {
            int length = Math.min(content.length - i, 128);
            tarArchiveOutputStream.write(content, i, length);
        }

        byte[] bytes = outputStream.toByteArray();

        // Check that header and data records have been written
        byte[] expectedHeader = entry.getHeaderBytes();
        assertEquals(expectedHeader.length, CountBytes(bytes, expectedHeader[0]));
        assertEquals(content.length, CountBytes(bytes, (byte) 0, content.length));
    }

    @Test
    public void testWriteWithLongFileName() throws Exception {
        String longFileName = "test/directory/longFileNameWithMoreThanNameLengthBytes.txt";
        TarArchiveEntry entry = new TarArchiveEntry(longFileName, "test content".getBytes(StandardCharsets.UTF_8));
        tarArchiveOutputStream.putArchiveEntry(entry);

        byte[] content = "test content".getBytes(StandardCharsets.UTF_8);
        tarArchiveOutputStream.write(content, 0, content.length);

        byte[] bytes = outputStream.toByteArray();

        // Check that long file name has been written using the specified longFileMode
        if (tarArchiveOutputStream.longFileMode == TarArchiveOutputStream.LONGFILE_TRUNCATE) {
            assertEquals(longFileName.substring(0, TarConstants.NAMELEN).getBytes(StandardCharsets.UTF_8).length, CountBytes(bytes, (byte) 0, TarConstants.NAMELEN));
        } else if (tarArchiveOutputStream.longFileMode == TarArchiveOutputStream.LONGFILE_GNU) {
            TarArchiveEntry longLinkEntry = new TarArchiveEntry(TarConstants.GNU_LONGLINK, TarConstants.LF_GNUTYPE_LONGNAME);
            longLinkEntry.setName(longFileName);
            byte[] longLinkNameBytes = longLinkEntry.getName().getBytes(StandardCharsets.UTF_8);
            assertEquals(longLinkNameBytes.length + 1, CountBytes(bytes, (byte) 0, longLinkNameBytes.length + 1));
        } else {
            throw new RuntimeException("Invalid longFileMode: " + tarArchiveOutputStream.longFileMode);
        }

        assertEquals(content.length, CountBytes(bytes, (byte) 0, content.length));
    }

    @Test
    public void testCreateArchiveEntry() throws Exception {
        File file = new File("test.txt");
        TarArchiveEntry entry = tarArchiveOutputStream.createArchiveEntry(file, "test.txt");
        assertNotNull(entry);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveEntryNullFile() throws Exception {
        TarArchiveOutputStream tarArchiveOutputStream1 = new TarArchiveOutputStream(Mockito.mock(OutputStream.class));
        tarArchiveOutputStream1.createArchiveEntry(null, "test.txt");
    }

    private int CountBytes(byte[] bytes, byte value) {
        int count = 0;
        for (byte b : bytes) {
            if (b == value) {
                count++;
            }
        }
        return count;
    }

    private int CountBytes(byte[] bytes, byte value, int offset, int length) {
        int count = 0;
        for (int i = offset; i < offset + length; i++) {
            if (bytes[i] == value) {
                count++;
            }
        }
        return count;
    }
}