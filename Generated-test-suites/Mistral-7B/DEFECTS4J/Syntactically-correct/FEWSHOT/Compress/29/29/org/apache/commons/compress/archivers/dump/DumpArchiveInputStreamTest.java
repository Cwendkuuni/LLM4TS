package org.apache.commons.compress.archivers.dump;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipEncoding;
import org.apache.commons.compress.archivers.zip.ZipEncodingHelper;
import org.apache.commons.compress.utils.ByteStreams;
import org.apache.commons.compress.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class DumpArchiveInputStreamTest {
    private static final int BUFFER_SIZE = 256;
    private static final int DUMP_ARCHIVE_HEADER_SIZE = 512;
    private static final int TEST_ENTRY_COUNT = 3;
    private static final long TEST_ENTRY_SIZE = 1024;
    private static final String TEST_FILE_NAME = "test.dump";

    private DumpArchiveInputStream inputStream;
    private ArchiveInputStream expectedInputStream;
    private DumpArchiveSummary expectedSummary;

    @Before
    public void setUp() throws IOException {
        byte[] headerBytes = generateDumpArchiveHeader();
        expectedSummary = new DumpArchiveSummary(headerBytes, ZipEncodingHelper.UTF8);
        expectedInputStream = new MockArchiveInputStream(generateTestEntries(TEST_ENTRY_COUNT, TEST_ENTRY_SIZE));
    }

    @Test
    public void testMatches() {
        byte[] headerBytes = generateDumpArchiveHeader();
        assertTrue(DumpArchiveInputStream.matches(headerBytes, DumpArchiveConstants.TP_SIZE));
        assertFalse(DumpArchiveInputStream.matches(headerBytes, DUMP_ARCHIVE_HEADER_SIZE - 1));
        assertFalse(DumpArchiveInputStream.matches(new byte[DUMP_ARCHIVE_HEADER_SIZE], DUMP_ARCHIVE_HEADER_SIZE));
    }

    @Test
    public void testConstructor() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(generateDumpArchiveHeader());
        DumpArchiveInputStream actualInputStream = new DumpArchiveInputStream(inputStream);
        assertEquals(expectedSummary, actualInputStream.getSummary());

        inputStream = new ByteArrayInputStream(generateDumpArchiveHeader());
        actualInputStream = new DumpArchiveInputStream(inputStream, "UTF-8");
        assertEquals(expectedSummary, actualInputStream.getSummary());
    }

    @Test(expected = ArchiveException.class)
    public void testConstructorWithInvalidHeader() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[DUMP_ARCHIVE_HEADER_SIZE - 1]);
        new DumpArchiveInputStream(inputStream);
    }

    @Test(expected = ArchiveException.class)
    public void testConstructorWithInvalidEncoding() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(generateDumpArchiveHeader());
        new DumpArchiveInputStream(inputStream, "InvalidEncoding");
    }

    @Test
    public void testGetBytesRead() throws IOException {
        long expectedBytesRead = TEST_ENTRY_COUNT * TEST_ENTRY_SIZE + DUMP_ARCHIVE_HEADER_SIZE;
        assertEquals(expectedBytesRead, inputStream.getBytesRead());
        assertEquals(expectedBytesRead, expectedInputStream.getBytesRead());
    }

    @Test
    public void testGetCount() throws IOException {
        assertEquals(expectedBytesRead(), getCount(expectedInputStream));
        assertEquals(expectedBytesRead(), inputStream.getCount());
    }

    @Test
    public void testGetNextEntry() throws IOException {
        DumpArchiveEntry actualEntry = inputStream.getNextEntry();
        assertEquals(expectedInputStream.getNextEntry(), actualEntry);
    }

    @Test
    public void testGetNextEntryNoMoreEntries() throws IOException {
        inputStream.getNextEntry();
        inputStream.getNextEntry();
        assertNull(inputStream.getNextEntry());
    }

    @Test
    public void testGetNextEntryInvalidHeader() throws IOException {
        inputStream.reset();
        assertNull(inputStream.getNextEntry());
    }

    @Test
    public void testGetNextEntryWithEof() throws IOException {
        inputStream.getNextEntry();
        inputStream.getNextEntry();
        inputStream.getNextEntry();
        inputStream.close();
        inputStream.getNextEntry();
        assertNull(inputStream.getNextEntry());
    }

    @Test
    public void testGetNextEntryWithSparseRecord() throws IOException {
        inputStream.getNextEntry();
        inputStream.getNextEntry();
        DumpArchiveEntry actualEntry = inputStream.getNextEntry();
        assertEquals(expectedInputStream.getNextEntry(), actualEntry);
        assertEquals(expectedSummary.getFileDescriptors().get(2).getOffset(), actualEntry.getOffset());
        assertEquals(expectedSummary.getFileDescriptors().get(2).getSize(), actualEntry.getSize());
        assertEquals(expectedSummary.getFileDescriptors().get(2).getName(), actualEntry.getName());
    }

    @Test
    public void testGetNextEntryWithEndOfVolumeMarker() throws IOException {
        inputStream.getNextEntry();
        inputStream.getNextEntry();
        inputStream.getNextEntry();
        inputStream.getNextEntry();
        inputStream.getNextEntry();
        assertNull(inputStream.getNextEntry());
    }

    @Test
    public void testClose() throws IOException {
        inputStream.getNextEntry();
        inputStream.getNextEntry();
        inputStream.getNextEntry();
        inputStream.close();
        assertNull(inputStream.getNextEntry());
        expectedInputStream.close();
        assertFalse(expectedInputStream.isOpen());
        assertFalse(inputStream.isOpen());
    }

    @Test
    public void testRead() throws IOException {
        byte[] buffer = new byte[BUFFER_SIZE];
        int bytesRead = inputStream.read(buffer);
        int expectedBytesRead = (int) Math.min(BUFFER_SIZE, TEST_ENTRY_SIZE);
        assertArrayEquals(expectedInputStream.read(new byte[expectedBytesRead]), buffer, 0, expectedBytesRead);
        assertEquals(expectedBytesRead, bytesRead);
    }

    @Test
    public void testReadWithEof() throws IOException {
        byte[] buffer = new byte[BUFFER_SIZE];
        int bytesRead = inputStream.read(buffer);
        assertEquals(-1, bytesRead);
    }

    @Test
    public void testReadWithReadBufSizeBiggerThanEntrySize() throws IOException {
        byte[] buffer = new byte[TEST_ENTRY_SIZE * 2];
        int bytesRead = inputStream.read(buffer);
        int expectedBytesRead = TEST_ENTRY_SIZE;
        assertArrayEquals(expectedInputStream.read(new byte[expectedBytesRead]), buffer, 0, expectedBytesRead);
        assertEquals(expectedBytesRead, bytesRead);
    }

    @Test
    public void testReadWithReadBufSizeSmallerThanEntrySize() throws IOException {
        byte[] buffer = new byte[TEST_ENTRY_SIZE / 2];
        int bytesRead = inputStream.read(buffer);
        int expectedBytesRead = TEST_ENTRY_SIZE / 2;
        assertArrayEquals(expectedInputStream.read(new byte[expectedBytesRead]), buffer, 0, expectedBytesRead);
        assertEquals(expectedBytesRead, bytesRead);
    }

    @Test
    public void testReadWithReadBufSizeEqualsEntrySize() throws IOException {
        byte[] buffer = new byte[TEST_ENTRY_SIZE];
        int bytesRead = inputStream.read(buffer);
        int expectedBytesRead = TEST_ENTRY_SIZE;
        assertArrayEquals(expectedInputStream.read(new byte[expectedBytesRead]), buffer, 0, expectedBytesRead);
        assertEquals(expectedBytesRead, bytesRead);
    }

    @Test
    public void testReadWithReadBufSizeEqualsEntrySizePlusOne() throws IOException {
        byte[] buffer = new byte[TEST_ENTRY_SIZE + 1];
        int bytesRead = inputStream.read(buffer);
        int expectedBytesRead = TEST_ENTRY_SIZE;
        assertArrayEquals(expectedInputStream.read(new byte[expectedBytesRead]), buffer, 0, expectedBytesRead);
        assertEquals(expectedBytesRead, bytesRead);
    }

    @Test
    public void testReadWithReadBufSizeBiggerThanEntrySizePlusOne() throws IOException {
        byte[] buffer = new byte[TEST_ENTRY_SIZE * 2];
        int bytesRead = inputStream.read(buffer);
        int expectedBytesRead = TEST_ENTRY_SIZE;
        assertArrayEquals(expectedInputStream.read(new byte[expectedBytesRead]), buffer, 0, expectedBytesRead);
        assertEquals(expectedBytesRead, bytesRead);
    }

    @Test
    public void testReadWithReadBufSizeEqualsEntrySizeMinusOne() throws IOException {
        byte[] buffer = new byte[TEST_ENTRY_SIZE - 1];
        int bytesRead = inputStream.read(buffer);
        int expectedBytesRead = TEST_ENTRY_SIZE - 1;
        assertArrayEquals(expectedInputStream.read(new byte[expectedBytesRead]), buffer, 0, expectedBytesRead);
        assertEquals(expectedBytesRead, bytesRead);
    }

    @Test
    public void testReadWithSparseRecord() throws IOException {
        byte[] buffer = new byte[TEST_ENTRY_SIZE];
        int bytesRead = inputStream.read(buffer);
        int expectedBytesRead = TEST_ENTRY_SIZE;
        assertArrayEquals(expectedInputStream.read(new byte[expectedBytesRead]), buffer, 0, expectedBytesRead);
        assertEquals(expectedBytesRead, bytesRead);

        inputStream.getNextEntry();
        buffer = new byte[TEST_ENTRY_SIZE];
        bytesRead = inputStream.read(buffer);
        assertEquals(-1, bytesRead);
    }

    @Test
    public void testReadWithEndOfVolumeMarker() throws IOException {
        byte[] buffer = new byte[TEST_ENTRY_SIZE];
        int bytesRead = inputStream.read(buffer);
        int expectedBytesRead = TEST_ENTRY_SIZE;
        assertArrayEquals(expectedInputStream.read(new byte[expectedBytesRead]), buffer, 0, expectedBytesRead);
        assertEquals(expectedBytesRead, bytesRead);

        inputStream.getNextEntry();
        inputStream.getNextEntry();
        inputStream.getNextEntry();
        inputStream.getNextEntry();
        inputStream.getNextEntry();
        buffer = new byte[TEST_ENTRY_SIZE];
        bytesRead = inputStream.read(buffer);
        assertEquals(-1, bytesRead);
    }

    @Test
    public void testIsOpen() throws IOException {
        assertTrue(inputStream.isOpen());
        inputStream.close();
        assertFalse(inputStream.isOpen());
    }

    @Test
    public void testIsClosed() throws IOException {
        assertFalse(inputStream.isClosed());
        inputStream.close();
        assertTrue(inputStream.isClosed());
    }

    @Test
    public void testToString() throws IOException {
        assertEquals("DumpArchiveInputStream[enenries=" + TEST_ENTRY_COUNT +
                ", entrySize=" + TEST_ENTRY_SIZE +
                ", position=" + 0 +
                ", size=" + (TEST_ENTRY_COUNT * TEST_ENTRY_SIZE + DUMP_ARCHIVE_HEADER_SIZE) + "]",
                inputStream.toString());
    }

    @Test
    public void testVerify() throws IOException {
        byte[] headerBytes = generateDumpArchiveHeader();
        assertTrue(DumpArchiveInputStream.matches(headerBytes, DumpArchiveConstants.TP_SIZE));
    }

    private byte[] generateDumpArchiveHeader() {
        byte[] headerBytes = new byte[DUMP_ARCHIVE_HEADER_SIZE];
        DumpArchiveUtil.write(headerBytes, DumpArchiveConstants.MAGIC_NUMBER);
        DumpArchiveUtil.write(headerBytes, TEST_ENTRY_COUNT);
        DumpArchiveUtil.write(headerBytes, TEST_ENTRY_SIZE);
        return headerBytes;
    }

    private ArchiveInputStream generateTestEntries(int entryCount, long entrySize) throws IOException {
        byte[] buffer = new byte[TEST_ENTRY_SIZE];
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(TEST_ENTRY_SIZE);
        List<DumpArchiveEntry> entries = new ArrayList<>();
        for (int i = 0; i < entryCount; i++) {
            byte[] entryBytes = TestUtils.createTestFile(buffer, "TestEntry" + i);
            byte[] lengthBytes = DumpArchiveUtil.writeInt(TEST_ENTRY_SIZE, buffer, 8);
            byte[] inoBytes = DumpArchiveUtil.writeInt((int) (i + 1), buffer, 12);
            byte[] nameSizeBytes = DumpArchiveUtil.writeInt(nameLength(i), buffer, 16);
            byte[] nameBytes = DumpArchiveUtil.writeString(TestUtils.getString(i), buffer, 16 + nameLength(i), ZipEncodingHelper.UTF8);
            byte[] typeBytes = new byte[]{0x4};
            DumpArchiveEntry entry = new DumpArchiveEntry(inoBytes, nameBytes, typeBytes, lengthBytes, nameSizeBytes);
            entries.add(entry);
            outputStream.write(buffer, 0, TEST_ENTRY_SIZE);
            buffer = new byte[TEST_ENTRY_SIZE];
        }
        return new MockArchiveInputStream(outputStream.toByteArray());
    }

    private int nameLength(int index) {
        String name = TestUtils.getString(index);
        return name.getBytes(ZipEncodingHelper.UTF8).length;
    }

    private static class MockArchiveInputStream extends ArchiveInputStream {
        private final InputStream inputStream;

        public MockArchiveInputStream(InputStream inputStream) {
            super(inputStream);
            this.inputStream = inputStream;
        }

        public MockArchiveInputStream(byte[] bytes) throws IOException {
            this(new ByteArrayInputStream(bytes));
        }

        @Override
        public DumpArchiveEntry getNextEntry() throws IOException {
            return getNextEntry(inputStream);
        }

        @Override
        public DumpArchiveEntry getNextEntry(InputStream testData) throws IOException {
            if (hasNextEntry()) {
                return getNextEntry();
            }
            return null;
        }
    }
}