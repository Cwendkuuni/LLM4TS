package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Test;

public class TarArchiveOutputStreamTest {

    @Test
    public void testEmptyOutputStream() {
        try (TarArchiveOutputStream tar = new TarArchiveOutputStream(new ByteArrayOutputStream(0))) {
            tar.finish();
            assertArrayEquals(new byte[0][], tar.getBuffer().getBytes());
        }
    }

    @Test
    public void testLongFileNames() throws IOException, ArchiveException {
        final String longFileName = "a" + TarConstants.NAMELEN + "longFileName";
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             TarArchiveOutputStream tar = new TarArchiveOutputStream(out)) {
            tar.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
            ArchiveEntry entry = new TarArchiveEntry(new File("testFilePath"), longFileName);
            tar.putArchiveEntry(entry);
            tar.write("Long file name test".getBytes());
            tar.closeArchiveEntry();
            tar.finish();
            byte[] buffer = out.toByteArray();

            TarArchiveInputStream tarIn = new TarArchiveInputStream(new ByteArrayInputStream(buffer));
            assertEquals("Long file name test", tarIn.getInputChannel().size());
            TarArchiveEntry entryFromStream = tarIn.getNextTarEntry();
            assertEquals(longFileName, entryFromStream.getName());
            tarIn.close();
        }
    }

    @Test(expected = RuntimeException.class)
    public void testTooLongFileNames() throws IOException {
        final String longFileName = "a" + (TarConstants.NAMELEN + 1) + "longFileName";
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             TarArchiveOutputStream tar = new TarArchiveOutputStream(out)) {
            TarArchiveEntry entry = new TarArchiveEntry(new File("testFilePath"), longFileName);
            tar.putArchiveEntry(entry);
            tar.close();
        }
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntryBeforePutArchiveEntry() throws IOException {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             TarArchiveOutputStream tar = new TarArchiveOutputStream(out)) {
            TarArchiveEntry entry = new TarArchiveEntry(new File("testFilePath"), "testFileName");
            tar.closeArchiveEntry();
            tar.putArchiveEntry(entry);
        }
    }

    @Test(expected = IOException.class)
    public void testWriteBeyondSize() throws IOException {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             TarArchiveOutputStream tar = new TarArchiveOutputStream(out)) {
            ArchiveEntry entry = new TarArchiveEntry(new File("testFilePath"), "testFileName");
            tar.putArchiveEntry(entry);
            tar.write("Test".getBytes(), 1, entry.getSize() - 1);
            tar.closeArchiveEntry();
        }
    }

    @Test
    public void testWrite() throws IOException {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             TarArchiveOutputStream tar = new TarArchiveOutputStream(out)) {
            ArchiveEntry entry = new TarArchiveEntry(new File("testFilePath"), "testFileName");
            tar.putArchiveEntry(entry);
            byte[] data = new byte[]{'T', 'e', 's', 't'};
            tar.write(data);
            tar.closeArchiveEntry();
            byte[] buffer = out.toByteArray();
            byte[] expected = new byte[]{'0', '0', '00', 0x14, 0, 0, 0, 0, '\u0000',
                                         'T', 'e', 's', 't', 0};
            assertArrayEquals(expected, new byte[13][0].concat(new byte[]{0}[1]).concat(data));
        }
    }

    @Test
    public void testWriteTwoEntries() throws IOException {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             TarArchiveOutputStream tar = new TarArchiveOutputStream(out)) {
            ArchiveEntry entry1 = new TarArchiveEntry(new File("testFilePath1"), "testFileName1");
            TarArchiveEntry entry2 = new TarArchiveEntry(new File("testFilePath2"), "testFileName2");
            tar.putArchiveEntry(entry1);
            byte[] data1 = new byte[]{'T', 'e', 's', 't'};
            tar.write(data1);
            tar.putArchiveEntry(entry2);
            byte[] data2 = new byte[]{'A', 'p', 'a', 'c'};
            tar.write(data2);
            tar.closeArchiveEntry();
            tar.closeArchiveEntry();
            tar.finish();
            byte[] buffer = out.toByteArray();
            int pos = 0;
            byte[] record1 = new byte[TarConstants.HEADER_LENGTH + 2 + data1.length][];
            System.arraycopy(buffer, pos, record1[0], 0, TarConstants.HEADER_LENGTH);
            pos += TarConstants.HEADER_LENGTH;
            record1[0][pos++] = 0x14;
            record1[0][pos++] = 0;
            record1[0][pos++] = 0;
            record1[0][pos++] = 0;
            record1[0][pos++] = 0;
            record1[0][pos++] = 0;
            record1[0][pos++] = 0;
            record1[0][pos++] = (byte) data1.length;
            System.arraycopy(data1, 0, record1[0], pos, data1.length);
            pos += data1.length;
            byte[] record2 = new byte[TarConstants.HEADER_LENGTH + 2 + data2.length][];
            System.arraycopy(buffer, pos, record2[0], 0, TarConstants.HEADER_LENGTH);
            pos += TarConstants.HEADER_LENGTH;
            record2[0][pos++] = 0x14;
            record2[0][pos++] = 0;
            record2[0][pos++] = 0;
            record2[0][pos++] = 0;
            record2[0][pos++] = 0;
            record2[0][pos++] = 0;
            record2[0][pos++] = 0;
            record2[0][pos++] = (byte) data2.length;
            System.arraycopy(data2, 0, record2[0], pos, data2.length);
            pos += data2.length;
            assertArrayEquals(new byte[][]{record1, record2}, getRecordBytes(buffer));
        }
    }

    @Test
    public void testFinish() throws IOException {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             TarArchiveOutputStream tar = new TarArchiveOutputStream(out)) {
            ArchiveEntry entry = new TarArchiveEntry(new File("testFilePath"), "testFileName");
            tar.putArchiveEntry(entry);
            tar.closeArchiveEntry();
            tar.finish();
            byte[] buffer = out.toByteArray();
            byte[] record = new byte[TarConstants.HEADER_LENGTH * 2 + 2 * 512][];
            int pos = 0;
            System.arraycopy(buffer, pos, record[0], 0, TarConstants.HEADER_LENGTH);
            pos += TarConstants.HEADER_LENGTH;
            System.arraycopy(buffer, pos, record[1], 0, TarConstants.HEADER_LENGTH);
            pos += TarConstants.HEADER_LENGTH;
            int eof1 = TarConstants.HEADER_LENGTH + 512;
            int eof2 = buffer.length - eof1;
            System.arraycopy(buffer, eof1, record[0], pos, 512);
            pos += 512;
            System.arraycopy(buffer, eof2, record[1], pos, 512);
            pos += 512;
            assertArrayEquals(new byte[][]{record[0], record[1]}, getRecordBytes(buffer));
        }
    }

    @Test
    public void testClose() throws IOException {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             TarArchiveOutputStream tar = new TarArchiveOutputStream(out)) {
            ArchiveEntry entry = new TarArchiveEntry(new File("testFilePath"), "testFileName");
            tar.putArchiveEntry(entry);
            tar.close();
            byte[] buffer = out.toByteArray();
            byte[] record = new byte[TarConstants.HEADER_LENGTH + 2][];
            int pos = 0;
            System.arraycopy(buffer, pos, record[0], 0, TarConstants.HEADER_LENGTH);
            pos += TarConstants.HEADER_LENGTH;
            byte[] data = new byte[]{'T', 'e', 's', 't'};
            int recordLength = TarConstants.HEADER_LENGTH + data.length;
            System.arraycopy(buffer, pos, record[1], 0, recordLength);
            pos += recordLength;
            assertTrue(pos < buffer.length);
            assertNull(tar.getNextTarEntry());
        }
    }

    @Test
    public void testWriteWithClosedStream() throws IOException {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             TarArchiveOutputStream tar = new TarArchiveOutputStream(out)) {
            ArchiveEntry entry = new TarArchiveEntry(new File("testFilePath"), "testFileName");
            tar.putArchiveEntry(entry);
            tar.close();
            tar.write("This should fail".getBytes());
        }
        // Test should fail silently
    }

    @Test(expected = IOException.class)
    public void testWriteWithoutPutArchiveEntry() throws IOException {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             TarArchiveOutputStream tar = new TarArchiveOutputStream(out)) {
            tar.write("This should fail".getBytes());
        }
    }

    @Test
    public void testFlush() throws IOException {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             TarArchiveOutputStream tar = new TarArchiveOutputStream(out)) {
            ArchiveEntry entry = new TarArchiveEntry(new File("testFilePath"), "testFileName");
            tar.putArchiveEntry(entry);
            tar.write("Test".getBytes());
            tar.flush();
            byte[] buffer = out.toByteArray();
            byte[] expected = new byte[]{'0', '0', '00', 0x14, 0, 0, 0, 0, '\u0000',
                                         'T', 'e', 's', 't', 0};
            assertArrayEquals(expected, new byte[13][0].concat(new byte[]{0}[1]).concat(new byte[]{'T', 'e', 's', 't'}[0]));
        }
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             TarArchiveOutputStream tar = new TarArchiveOutputStream(out)) {
            File file = new File("testFilePath");
            ArchiveEntry entry = tar.createArchiveEntry(file, "testFileName");
            assertEquals("testFileName", entry.getName());
            assertEquals(file.length(), entry.getSize());
        }
    }

    private byte[][] getRecordBytes(byte[] buffer) {
        int pos = 0;
        int recordIndex = 0;
        int totalRecords = 0;
        int recordLength = 0;
        byte[] record = null;
        while (pos < buffer.length) {
            record = new byte[TarConstants.HEADER_LENGTH];
            System.arraycopy(buffer, pos, record, 0, TarConstants.HEADER_LENGTH);
            recordLength = TarConstants.HEADER_LENGTH;
            if (buffer[pos + TarConstants.MAGIC] == 0x14) {
                totalRecords++;
                if (recordIndex < 1) {
                    recordLength += TarConstants.NAME_LENGTH + 2 + 8 + 2;
                }
                if (totalRecords == 2) {
                    break;
                }
            }
            pos += recordLength;
        }
        return new byte[][]{record, new byte[TarConstants.HEADER_LENGTH][0].concat(new byte[recordLength - TarConstants.HEADER_LENGTH][0]).toArray(new byte[0][])};
    }
}