package org.apache.commons.compress.archivers.cpio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.CpioConstants;
import org.apache.commons.compress.archivers.CpioUtil;
import org.apache.commons.compress.archivers.cpio.CpioArchiveEntryImpl;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.cpio.CpioConstants.FORMAT_MASK;

/**
 * Tests for {@link CpioArchiveOutputStream} class.
 */
public class CpioArchiveOutputStreamTest {

    private static final byte[] TEST_DATA = "test data".getBytes();

    @org.junit.Test
    public void testPutNextEntryWithValidParameters() throws IOException {
        // given
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioStream = new CpioArchiveOutputStream(out);

        // when
        CpioArchiveEntry entry = new CpioArchiveEntryImpl("test.txt", 1024, 0644);
        cpioStream.putNextEntry(entry);

        // then
        assertEquals(CpioConstants.FORMAT_NEW, cpioStream.getFormat());
        assertTrue(cpioStream.isOpen());
        assertNull(cpioStream.getCpioEntry());
        assertEquals(1, cpioStream.getNextEntryIndex());
    }

    @org.junit.Test
    public void testPutNextEntryWithValidParametersAndData() throws IOException {
        // given
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioStream = new CpioArchiveOutputStream(out);

        CpioArchiveEntry entry = new CpioArchiveEntryImpl("test.txt", 1024, 0644);
        cpioStream.putNextEntry(entry);

        // when
        byte[] data = TEST_DATA;
        cpioStream.write(data, 0, data.length);

        // then
        assertEquals(1024, cpioStream.getCpioEntry().getSize());
        byte[] result = ((ByteArrayOutputStream) out).toByteArray();

        // verify that the output byte array matches the expected result
        byte[] expected = new byte[] {
            MAGIC_NEW[0], MAGIC_NEW[1], MAGIC_NEW[2], MAGIC_NEW[3],
            (byte) 0x12, (byte) 0x34, 
            // name length + 1
            (byte) 0x0a, 
            (byte) 0x06, (byte) 0x43, (byte) 0x41, (byte) 0x54, (byte) 0x53,
            (byte) 0x55, (byte) 0x54, (byte) 0x20, (byte) 0x74, (byte) 0x68,
            (byte) 0x65, (byte) 0x74, (byte) 0x20, (byte) 0x74, (byte) 0x65,
            (byte) 0x73, (byte) 0x74, (byte) 0x20, (byte) 0x54, (byte) 0x69,
            (byte) 0x74, (byte) 0x65, (byte) 0x20, (byte) 0x73, (byte) 0x74,
            (byte) 0x72, (byte) 0x69, (byte) 0x6e, (byte) 0x67, (byte) 0x65,
            (byte) 0x00, // name checksum
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
            (byte) 0x00, (byte) 0x00,
            (byte) 0x00, (byte) 0x00, // file size (little endian)
            (byte) 0x04, (byte) 0x65, (byte) 0x6e, (byte) 0x63, (byte) 0x6f,
            (byte) 0x64, (byte) 0x65, (byte) 0x64,
            (byte) 0x00, // file permissions
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, // owner uid
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, // owner gid
            (byte) 0x01, (byte) 0x00, // number of links
            (byte) 0x00, (byte) 0x00, // device major
            (byte) 0x08, (byte) 0x00, // device minor
            (byte) 0x00, (byte) 0x00, // remote device major
            (byte) 0x00, (byte) 0x00, // remote device minor
            (byte) 0x0a, // name length + 1
            (byte) 0x06, (byte) 0x43, (byte) 0x41, (byte) 0x54, (byte) 0x53,
            (byte) 0x55, (byte) 0x54, (byte) 0x20, (byte) 0x74, (byte) 0x68,
            (byte) 0x65, (byte) 0x74, (byte) 0x20, (byte) 0x74, (byte) 0x65,
            (byte) 0x73, (byte) 0x74, (byte) 0x20, (byte) 0x54, (byte) 0x69,
            (byte) 0x74, (byte) 0x65, (byte) 0x20, (byte) 0x73, (byte) 0x74,
            (byte) 0x72, (byte) 0x69, (byte) 0x6e, (byte) 0x67, (byte) 0x65,
            (byte) 0x00, // name checksum
            TEST_DATA[0], TEST_DATA[1], // data
            TEST_DATA[2], TEST_DATA[3],
            TEST_DATA[4], TEST_DATA[5],
            TEST_DATA[6], TEST_DATA[7],
            TEST_DATA[8], TEST_DATA[9],
            TEST_DATA[10], TEST_DATA[11],
            TEST_DATA[12], TEST_DATA[13],
            TEST_DATA[14], TEST_DATA[15]
        };
        assertEquals(expected.length, result.length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], result[i]);
        }
        cpioStream.finish();
        ByteArrayInputStream input = new ByteArrayInputStream(result);
        assertEquals(input.read(), MAGIC_NEW[0]);
        assertEquals(input.read(), MAGIC_NEW[1]);
        assertEquals(input.read(), MAGIC_NEW[2]);
        assertEquals(input.read(), MAGIC_NEW[3]);
        assertEquals(input.read(), 0x12);
        assertEquals(input.read(), 0x34);
        // name length + 1
        assertEquals(input.read(), 0x0a);
        assertEquals(input.read(), 0x06);
        assertEquals(input.read(), 0x43);
        assertEquals(input.read(), 0x41);
        assertEquals(input.read(), 0x54);
        assertEquals(input.read(), 0x53);
        assertEquals(input.read(), 0x55);
        assertEquals(input.read(), 0x54);
        assertEquals(input.read(), 0x20);
        assertEquals(input.read(), 0x74);
        assertEquals(input.read(), 0x68);
        assertEquals(input.read(), 0x65);
        assertEquals(input.read(), 0x74);
        assertEquals(input.read(), 0x20);
        assertEquals(input.read(), 0x74);
        assertEquals(input.read(), 0x65);
        assertEquals(input.read(), 0x73);
        assertEquals(input.read(), 0x74);
        assertEquals(input.read(), 0x20);
        assertEquals(input.read(), 0x54);
        assertEquals(input.read(), 0x69);
        assertEquals(input.read(), 0x74);
        assertEquals(input.read(), 0x65);
        assertEquals(input.read(), 0x20);
        assertEquals(input.read(), 0x73);
        assertEquals(input.read(), 0x74);
        assertEquals(input.read(), 0x72);
        assertEquals(input.read(), 0x69);
        assertEquals(input.read(), 0x6e);
        assertEquals(input.read(), 0x67);
        assertEquals(input.read(), 0x65);
        assertEquals(input.read(), 0x00);
        assertEquals(input.read(), TEST_DATA[0]);
        assertEquals(input.read(), TEST_DATA[1]);
        assertEquals(input.read(), TEST_DATA[2]);
        assertEquals(input.read(), TEST_DATA[3]);
        assertEquals(input.read(), TEST_DATA[4]);
        assertEquals(input.read(), TEST_DATA[5]);
        assertEquals(input.read(), TEST_DATA[6]);
        assertEquals(input.read(), TEST_DATA[7]);
        assertEquals(input.read(), TEST_DATA[8]);
        assertEquals(input.read(), TEST_DATA[9]);
        assertEquals(input.read(), TEST_DATA[10]);
        assertEquals(input.read(), TEST_DATA[11]);
        assertEquals(input.read(), TEST_DATA[12]);
        assertEquals(input.read(), TEST_DATA[13]);
        assertEquals(input.read(), TEST_DATA[14]);
        assertEquals(input.read(), TEST_DATA[15]);
        assertFalse(input.read() == -1);
        assertTrue(cpioStream.isFinished());
    }

    @org.junit.Test
    public void testPutNextEntryWithEmptyName() throws IOException {
        // given
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioStream = new CpioArchiveOutputStream(out);

        // when
        CpioArchiveEntry entry = new CpioArchiveEntryImpl("", 1024, 0644);
        cpioStream.putNextEntry(entry);

        // then
        assertThrows(IOException.class, () -> cpioStream.write(TEST_DATA));
        assertFalse(cpioStream.isFinished());
    }

    @org.junit.Test
    public void testPutNextEntryWithInvalidFormat() throws IOException {
        // given
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioStream = new CpioArchiveOutputStream(out);

        // when
        CpioArchiveEntry entry = new CpioArchiveEntryImpl("test.txt", 1024, 0644);
        cpioStream.putNextEntry(entry);
        short format = CpioConstants.FORMAT_OLD_ASCII | CpioConstants.FORMAT_OLD_BINARY;
        cpioStream.setFormat(format);

        // then
        assertThrows(IllegalArgumentException.class, () -> cpioStream.write(TEST_DATA));
        assertFalse(cpioStream.isFinished());
    }

    @org.junit.Test
    public void testPutNextEntryWithInvalidFormat2() throws IOException {
        // given
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioStream = new CpioArchiveOutputStream(out);

        // when
        CpioArchiveEntry entry = new CpioArchiveEntryImpl("test.txt", 1024, 0644);
        cpioStream.putNextEntry(entry);
        short format = 0x1234;
        cpioStream.setFormat(format);

        // then
        assertThrows(IllegalArgumentException.class, () -> cpioStream.write(TEST_DATA));
        assertFalse(cpioStream.isFinished());
    }

    @org.junit.Test
    public void testPutNextEntryTwice() throws IOException {
        // given
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioStream = new CpioArchiveOutputStream(out);

        // when
        CpioArchiveEntry entry1 = new CpioArchiveEntryImpl("test1.txt", 1024, 0644);
        cpioStream.putNextEntry(entry1);
        byte[] data1 = TEST_DATA.clone();
        cpioStream.write(data1, 0, data1.length);

        CpioArchiveEntry entry2 = new CpioArchiveEntryImpl("test2.txt", 1024, 0644);
        cpioStream.putNextEntry(entry2);
        byte[] data2 = TEST_DATA.clone();
        cpioStream.write(data2, 0, data2.length);
        cpioStream.finish();

        // then
        byte[] result = ((ByteArrayOutputStream) out).toByteArray();
        byte[] expected = new byte[result.length + 1]; // +1 for last null byte
        System.arraycopy(result, 0, expected, 0, result.length);
        assertEquals(expected.length, result.length);
        // the first entry should be the same as in the testPutNextEntryWithValidParametersAndData()
        byte[] firstEntry = new byte[expected.length - result.length];
        System.arraycopy(result, 0, firstEntry, 0, firstEntry.length);
        assertEquals(firstEntry.length, firstEntry.length);
        assertEquals(firstEntry[0], MAGIC_NEW[0]);
        assertEquals(firstEntry[1], MAGIC_NEW[1]);
        assertEquals(firstEntry[2], MAGIC_NEW[2]);
        assertEquals(firstEntry[3], MAGIC_NEW[3]);
        assertEquals(firstEntry[4], (byte) 0x12);
        assertEquals(firstEntry[5], (byte) 0x34);
        assertEquals(firstEntry[6], (byte) 0x0a);
        assertEquals(firstEntry[7], (byte) 0x06);
        // ... name, permissions, uid, gid, number of links, device major, device minor, remote device major, remote device minor, name length + 1
        // the rest of the byte array is the content of the first test file followed by the content of the second test file
        assertEquals(expected[expected.length - 1], 0x00);
        for (int i = 8; i < expected.length - 1; i++) {
            assertEquals(firstEntry[i - 8], expected[i]);
        }
        for (int i = expected.length - TEST_DATA.length; i < expected.length; i++) {
            assertEquals(data2[i - (expected.length - TEST_DATA.length)], expected[i]);
        }
    }

}