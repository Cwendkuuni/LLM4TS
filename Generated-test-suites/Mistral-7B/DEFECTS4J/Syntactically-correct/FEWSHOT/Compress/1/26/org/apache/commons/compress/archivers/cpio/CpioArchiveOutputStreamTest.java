package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.CpioConstants;
import org.apache.commons.compress.archivers.CpioUtil;
import org.apache.commons.compress.utils.ByteStreams;

/**
 * Test cases for CpioArchiveOutputStream class.
 */
public class CpioArchiveOutputStreamTest {

    @Test
    public void testNewOutputStream() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioStream = new CpioArchiveOutputStream(out);
        assertNull(cpioStream.getCpioEntry());
        assertFalse(cpioStream.isFinished());
        assertTrue(cpioStream.isOpen());
        assertEquals(CpioConstants.FORMAT_NEW, cpioStream.getFormat());
        cpioStream.close();
    }

    @Test
    public void testPutNextEntryOldAsciiFormat() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioStream = new CpioArchiveOutputStream(out,
                CpioConstants.FORMAT_OLD_ASCII);

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("test.txt");
        entry.setSize(5);
        entry.setFormat(CpioConstants.FORMAT_OLD_ASCII);
        cpioStream.putNextEntry(entry);

        byte[] magicOldAscii = CpioUtil.MAGIC_OLD_ASCII;
        byte[] header = cpioStream.getHeader(entry);
        assertArrayEquals(magicOldAscii, header);

        cpioStream.write("Hello World!".getBytes());
        cpioStream.closeArchiveEntry();
        byte[] expectedData = "Hello World!".getBytes();
        byte[] actualData = ByteStreams.toByteArray(out);
        assertArrayEquals(expectedData, actualData);

        cpioStream.close();
    }

    @Test
    public void testPutNextEntryNewFormat() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioStream = new CpioArchiveOutputStream(out);

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("test.txt");
        entry.setSize(5);
        entry.setFormat(CpioConstants.FORMAT_NEW);
        cpioStream.putNextEntry(entry);

        byte[] magicNew = CpioUtil.MAGIC_NEW.getBytes();
        byte[] header = cpioStream.getHeader(entry);
        assertArrayEquals(magicNew, header);

        cpioStream.write("Hello World!".getBytes());
        cpioStream.closeArchiveEntry();
        byte[] expectedData = "Hello World!".getBytes();
        byte[] actualData = ByteStreams.toByteArray(out);
        assertArrayEquals(expectedData, actualData);

        cpioStream.finish();
        byte[] finishedData = ByteStreams.toByteArray(out);
        byte[] expectedFinishedData = CpioUtil.MAGIC_NEW.getBytes();
        assertArrayEquals(expectedFinishedData, finishedData, 0, 8);

        cpioStream.close();
    }

    @Test
    public void testPutNextEntryNewFormatWithCrc() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioStream = new CpioArchiveOutputStream(out,
                CpioConstants.FORMAT_NEW_CRC);

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("test.txt");
        entry.setSize(5);
        entry.setFormat(CpioConstants.FORMAT_NEW_CRC);
        cpioStream.putNextEntry(entry);

        byte[] magicNewCrc = CpioUtil.MAGIC_NEW_CRC.getBytes();
        byte[] header = cpioStream.getHeader(entry);
        assertArrayEquals(magicNewCrc, header);

        cpioStream.write("Hello World!".getBytes());
        cpioStream.closeArchiveEntry();
        byte[] expectedData = "Hello World!".getBytes();
        byte[] actualData = ByteStreams.toByteArray(out);
        assertArrayEquals(expectedData, actualData);

        byte[] crc = new byte[4];
        for (int i = 0; i < "Hello World!".length(); ++i) {
            cpioStream.crc(expectedData[i]);
        }
        byte[] expectedCrc = cpioStream.getCrc();
        byte[] actualCrc = ByteStreams.toByteArray(new ByteArrayOutputStream()
                .write(expectedData));
        assertArrayEquals(expectedCrc, actualCrc);

        cpioStream.finish();
        byte[] finishedData = ByteStreams.toByteArray(out);
        byte[] expectedFinishedData = CpioUtil.MAGIC_NEW_CRC.getBytes();
        assertArrayEquals(expectedFinishedData, finishedData, 0, 8);

        byte[] expectedFinishedWithCrc = new byte[finishedData.length + 4];
        System.arraycopy(finishedData, 0, expectedFinishedWithCrc, 0,
                finishedData.length);
        System.arraycopy(expectedCrc, 0, expectedFinishedWithCrc,
                finishedData.length, 4);
        assertArrayEquals(expectedFinishedWithCrc, finishedData);

        cpioStream.close();
    }

    @Test
    public void testPutNextEntryOldBinaryFormat() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioStream = new CpioArchiveOutputStream(out,
                CpioConstants.FORMAT_OLD_BINARY);

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("test.txt");
        entry.setSize(5);
        entry.setFormat(CpioConstants.FORMAT_OLD_BINARY);
        cpioStream.putNextEntry(entry);

        byte[] magicOldBinary = CpioUtil.MAGIC_OLD_BINARY;
        byte[] header = cpioStream.getHeader(entry);
        assertArrayEquals(magicOldBinary, header, 0, 4);
        assertArrayEquals(new byte[] { 0, 0 }, header, 4, 2);

        cpioStream.write("Hello World!".getBytes());
        cpioStream.closeArchiveEntry();
        byte[] expectedData = "Hello World!".getBytes();
        byte[] actualData = ByteStreams.toByteArray(out);
        assertArrayEquals(expectedData, actualData);

        cpioStream.finish();
        byte[] finishedData = ByteStreams.toByteArray(out);
        byte[] expectedFinishedData = CpioUtil.MAGIC_OLD_BINARY;
        assertArrayEquals(expectedFinishedData, finishedData, 0, 4);
        assertArrayEquals(new byte[] { 0, 0 }, finishedData, 4, 2);

        cpioStream.close();
    }

    @Test
    public void testPutNextEntryNewFormatWithSwappedHalfWords()
            throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioStream = new CpioArchiveOutputStream(out,
                CpioConstants.FORMAT_NEW);

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("test.txt");
        entry.setSize(5);
        entry.setFormat(CpioConstants.FORMAT_NEW);
        cpioStream.putNextEntry(entry);

        byte[] magicNew = CpioUtil.MAGIC_NEW.getBytes();
        byte[] header = cpioStream.getHeader(entry);
        assertArrayEquals(magicNew, header);

        cpioStream.setSwapBytes(true);
        cpioStream.write(new byte[] { 0x01, 0x02, 0x03, 0x04 });
        cpioStream.closeArchiveEntry();

        Map<String, CpioArchiveEntry> entries = new HashMap<>();
        entries.put("test.txt", entry);
        byte[] expectedData = "test.txt\0\0\0\0\0\0\0\0".getBytes();
        byte[] actualData = ByteStreams.toByteArray(out);
        assertArrayEquals(expectedData, actualData);

        byte[] expectedName = "test.txt".getBytes();
        byte[] actualName = new byte[entries.get("test.txt").getName().length() + 1];
        System.arraycopy(entries.get("test.txt").getName().getBytes(), 0,
                actualName, 0, actualName.length - 1);
        assertArrayEquals(expectedName, actualName);

        byte[] expectedNameSwapped = new byte[expectedName.length];
        for (int i = 0; i < expectedName.length; ++i) {
            expectedNameSwapped[expectedName.length - 1 - i] = expectedName[i];
        }

        byte[] headerWithSwappedName = cpioStream.getHeader(entry);
        assertArrayEquals(headerWithSwappedName, new byte[][] {
                magicNew,
                new byte[] { (byte) 0x01, (byte) 0x01 }, // name length
                expectedNameSwapped,
                new byte[] { 0, 0 }, // name checksum
                new byte[] { (byte) 0x02, (byte) 0x01, 0, 0, 0, 0, 0, 0 },
                new byte[] { 0, 0, 0, 0 }, // format, version
                new byte[] { 0, 0, 0, 0 }, // device major
                new byte[] { 0, 0, 0, 0 }, // device minor
                new byte[] { 0, 0, 0, 0 }, // remote device major
                new byte[] { 0, 0, 0, 0 }, // remote device minor
                new byte[] { (byte) 0x05, 0 }, // file size
                new byte[] { 0, 0 }, // file checksum
                new byte[] { 0, 0, 0, 0 }, // user ID
                new byte[] { 0, 0, 0, 0 }, // group ID
                new byte[] { 0, 0, 0, 0, 0, 0, 0, 0 }, // file permission
                new byte[] { 0, 0, 0, 0 }, // file modification time
                new byte[] { 0, 0 } // file mode
        });

        cpioStream.setSwapBytes(false);
        cpioStream.finish();
        byte[] finishedData = ByteStreams.toByteArray(out);
        assertArrayEquals(finishedData,
                CpioUtil.getFinishedData(expectedData, CpioConstants.FORMAT_NEW));

        cpioStream.close();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidFormat() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioStream = new CpioArchiveOutputStream(out);
        cpioStream.setFormat((short) 0xFF);
        cpioStream.putNextEntry(null);
    }

    @Test(expected = IllegalStateException.class)
    public void testPutNextEntryTwice() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioStream = new CpioArchiveOutputStream(out);

        CpioArchiveEntry entry1 = new CpioArchiveEntry();
        entry1.setName("test1.txt");
        entry1.setSize(5);
        entry1.setFormat(CpioConstants.FORMAT_NEW);
        cpioStream.putNextEntry(entry1);

        CpioArchiveEntry entry2 = new CpioArchiveEntry();
        entry2.setName("test2.txt");
        entry2.setSize(5);
        entry2.setFormat(CpioConstants.FORMAT_NEW);
        cpioStream.putNextEntry(entry2);
    }

    @Test(expected = IllegalStateException.class)
    public void testWriteAfterClose() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioStream = new CpioArchiveOutputStream(out);
        cpioStream.putNextEntry(new CpioArchiveEntry());
        cpioStream.close();
        cpioStream.write(new byte[] { 0x01 });
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutNextEntryWithNullName() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioStream = new CpioArchiveOutputStream(out);
        cpioStream.putNextEntry(new CpioArchiveEntry(null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutNextEntryWithEmptyName() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioStream = new CpioArchiveOutputStream(out);
        cpioStream.putNextEntry(new CpioArchiveEntry(""));
    }
}