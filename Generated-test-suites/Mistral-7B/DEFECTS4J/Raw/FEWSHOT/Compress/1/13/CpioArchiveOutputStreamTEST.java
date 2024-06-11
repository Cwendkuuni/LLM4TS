package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.CpioConstants;
import org.apache.commons.compress.utils.CpioUtil;

public class CpioArchiveOutputStreamTest {

    private static final byte[] MAGIC_NEW = CpioUtil.stringToBytes("!<new>\0");
    private static final byte[] MAGIC_NEW_CRC = CpioUtil.stringToBytes("!<new>!\0");
    private static final byte[] MAGIC_OLD_ASCII = CpioUtil.stringToBytes("07074100");
    private static final byte[] MAGIC_OLD_BINARY = CpioUtil.long2byteArray(070741L, 3);

    @Test
    public void testNewInstance() {
        final OutputStream out = new ByteArrayOutputStream();
        final CpioArchiveOutputStream cpioStream = new CpioArchiveOutputStream(out);
        assertEquals(FORMAT_NEW, cpioStream.getFormat());
        assertNull(cpioStream.getCpioEntry());
        assertFalse(cpioStream.isFinished());
        assertFalse(cpioStream.isClosed());
    }

    @Test
    public void testNewInstanceWithFormat() {
        final OutputStream out = new ByteArrayOutputStream();
        final CpioArchiveOutputStream cpioStream = new CpioArchiveOutputStream(out, FORMAT_NEW);
        assertEquals(FORMAT_NEW, cpioStream.getFormat());
    }

    @Test
    public void testWriteMultipleEntries() throws IOException {
        final OutputStream out = new ByteArrayOutputStream();
        final CpioArchiveOutputStream cpioStream = new CpioArchiveOutputStream(out);

        final CpioArchiveEntry entry1 = new CpioArchiveEntry();
        entry1.setName("entry1");
        cpioStream.putNextEntry(entry1);
        assertArrayEquals(MAGIC_NEW, cpioStream.out.getBuffer(), 0, MAGIC_NEW.length);
        writeNewEntry(cpioStream, entry1);
        assertEquals(8, cpioStream.getWritten());

        final CpioArchiveEntry entry2 = new CpioArchiveEntry();
        entry2.setName("entry2");
        cpioStream.putNextEntry(entry2);
        assertArrayEquals(MAGIC_NEW, cpioStream.out.getBuffer(), 0, MAGIC_NEW.length);
        writeNewEntry(cpioStream, entry2);
        assertEquals(18, cpioStream.getWritten());

        cpioStream.close();
        assertEquals(MAGIC_NEW, cpioStream.out.toByteArray(), 0, MAGIC_NEW.length);
    }

    @Test
    public void testWriteBinaryEntry() throws IOException {
        final OutputStream out = new ByteArrayOutputStream();
        final CpioArchiveOutputStream cpioStream = new CpioArchiveOutputStream(out);

        final CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("entry");
        entry.setFormat(FORMAT_OLD_BINARY);
        cpioStream.putNextEntry(entry);
        writeOldBinaryEntry(cpioStream, entry, true);
        assertEquals(32, cpioStream.getWritten());

        cpioStream.close();
        assertEquals(32, cpioStream.out.size());
    }

    @Test
    public void testWriteAsciiEntry() throws IOException {
        final OutputStream out = new ByteArrayOutputStream();
        final CpioArchiveOutputStream cpioStream = new CpioArchiveOutputStream(out);

        final CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("entry");
        cpioStream.putNextEntry(entry);
        writeOldAsciiEntry(cpioStream, entry);
        assertEquals(34, cpioStream.getWritten());

        cpioStream.close();
        assertEquals(34, cpioStream.out.size());
    }

    @Test
    public void testWriteNewEntryWithCRC() throws IOException {
        final OutputStream out = new ByteArrayOutputStream();
        final CpioArchiveOutputStream cpioStream = new CpioArchiveOutputStream(out);

        final CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("entry");
        cpioStream.putNextEntry(entry);
        writeNewEntry(cpioStream, entry);
        assertEquals(52, cpioStream.getWritten());

        cpioStream.finish();
        assertEquals(60, cpioStream.getWritten());

        byte[] actual = cpioStream.out.toByteArray();
        byte[] expected = new byte[60];
        System.arraycopy(MAGIC_NEW_CRC, 0, expected, 0, MAGIC_NEW_CRC.length);
        writeNewEntry(cpioStream, entry);
        assertEquals(expected, actual, 0, expected.length);

        cpioStream.close();
    }

    @Test
    public void testWriteTrailer() throws IOException {
        final OutputStream out = new ByteArrayOutputStream();
        final CpioArchiveOutputStream cpioStream = new CpioArchiveOutputStream(out);

        cpioStream.finish();
        assertEquals(MAGIC_NEW, cpioStream.out.getBuffer(), 0, MAGIC_NEW.length);
        writeNewEntry(cpioStream, new CpioArchiveEntry("TRAILER!!!", FORMAT_NEW));
        assertEquals(52, cpioStream.getWritten());

        cpioStream.close();
        assertEquals(52, cpioStream.out.size());
    }

    @Test
    public void testWriteWithUnknownFormat() throws IOException {
        final OutputStream out = new ByteArrayOutputStream();
        final CpioArchiveOutputStream cpioStream = new CpioArchiveOutputStream(out);

        final CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("entry");
        entry.setFormat((short) 0xFFFF);
        cpioStream.putNextEntry(entry);
        assertThrows(IllegalArgumentException.class, () -> writeNewEntry(cpioStream, entry));

        cpioStream.close();
    }

    @Test
    public void testWriteWithDuplicateName() throws IOException {
        final OutputStream out = new ByteArrayOutputStream();
        final CpioArchiveOutputStream cpioStream = new CpioArchiveOutputStream(out);

        final CpioArchiveEntry entry1 = new CpioArchiveEntry();
        entry1.setName("entry1");
        cpioStream.putNextEntry(entry1);

        final CpioArchiveEntry entry2 = new CpioArchiveEntry();
        entry2.setName("entry1");
        assertThrows(IOException.class, () -> cpioStream.putNextEntry(entry2));

        cpioStream.close();
    }

    private void writeNewEntry(CpioArchiveOutputStream cpioStream,
            CpioArchiveEntry entry) throws IOException {
        byte[] header = new byte[entry.getName().length() + 1];
        writeAsciiString(header, entry.getName());
        cpioStream.out.write(header);

        cpioStream.write(CpioConstants.MagicNumber, 0, MAGIC_NEW.length);
        long size = entry.getSize();
        writeAsciiLong(cpioStream.out, size, 8);
        cpioStream.write(header, 0, header.length);
    }

    private void writeOldEntry(CpioArchiveOutputStream cpioStream,
            CpioArchiveEntry entry, boolean swapHalfWord) throws IOException {
        byte[] header = new byte[entry.getName().length() + 1];
        writeAsciiString(header, entry.getName());
        cpioStream.out.write(header);

        cpioStream.write(MAGIC_OLD_BINARY, 0, MAGIC_OLD_BINARY.length);
        writeBinaryLong(cpioStream.out, entry.getDevice(), 2, swapHalfWord);
        writeBinaryLong(cpioStream.out, entry.getInode(), 2, swapHalfWord);
        writeBinaryLong(cpioStream.out, entry.getMode(), 2, swapHalfWord);
        writeBinaryLong(cpioStream.out, entry.getUID(), 2, swapHalfWord);
        writeBinaryLong(cpioStream.out, entry.getGID(), 2, swapHalfWord);
        writeBinaryLong(cpioStream.out, entry.getNumberOfLinks(), 2, swapHalfWord);
        writeBinaryLong(cpioStream.out, entry.getRemoteDevice(), 2, swapHalfWord);
        writeAsciiLong(cpioStream.out, entry.getTime(), 4, swapHalfWord);
        writeAsciiLong(cpioStream.out, entry.getName().length() + 1, 2, swapHalfWord);
        writeAsciiLong(cpioStream.out, entry.getSize(), 4, swapHalfWord);
        cpioStream.write(header, 0, header.length);

        cpioStream.write(entry.getName().getBytes());
        cpioStream.write('\0');
    }

    private void writeAsciiString(byte[] buffer, String string) {
        for (int i = 0; i < string.length(); i++) {
            buffer[i] = (byte) string.charAt(i);
        }
    }

    private void writeAsciiLong(OutputStream out, long number, int length)
            throws IOException {
        String string = Long.toString(number, length > 8 ? 16 : 8);
        byte[] bytes = string.getBytes();
        out.write(bytes, 0, bytes.length);
    }

    private void writeBinaryLong(OutputStream out, long number, int length,
            boolean swapHalfWord) throws IOException {
        byte[] bytes = CpioUtil.long2byteArray(number, length, swapHalfWord);
        out.write(bytes);
    }
}