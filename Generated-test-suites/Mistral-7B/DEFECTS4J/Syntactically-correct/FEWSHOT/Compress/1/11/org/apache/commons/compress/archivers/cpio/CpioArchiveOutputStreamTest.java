package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.CpioConstants;
import org.apache.commons.compress.archivers.CpioUtil;
import org.apache.commons.compress.archivers.test.CpioTestUtils;
import org.junit.Test;

public class CpioArchiveOutputStreamTest {

    @Test
    public void testPutNextEntry() throws IOException {
        final OutputStream out = new ByteArrayOutputStream();
        final CpioArchiveOutputStream cpioOut = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);

        // test putNextEntry with null CpioArchiveEntry
        try {
            cpioOut.putNextEntry(null);
            fail("Expected IOException: null CpioArchiveEntry");
        } catch (final IOException e) {
            // expected
        }

        final CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("test.txt");
        entry.setSize(5);
        entry.setFormat(CpioConstants.FORMAT_NEW);

        cpioOut.putNextEntry(entry);

        // verify that header has been written correctly
        final byte[] headerBytes = cpioOut.getHeaderBytes();
        final byte[] expectedHeader = CpioTestUtils.createTestNewFormatHeader(entry.getName(), entry.getSize(), entry.getFormat());
        assertArrayEquals(expectedHeader, headerBytes);

        // test putNextEntry with existing entry name
        try {
            final CpioArchiveEntry duplicateEntry = new CpioArchiveEntry();
            duplicateEntry.setName("test.txt");
            cpioOut.putNextEntry(duplicateEntry);
            fail("Expected IOException: duplicate entry: test.txt");
        } catch (final IOException e) {
            // expected
        }

        // test putNextEntry with non-new format
        final CpioArchiveEntry newFormatEntry = new CpioArchiveEntry();
        newFormatEntry.setName("test.bin");
        newFormatEntry.setSize(10);
        newFormatEntry.setFormat(CpioConstants.FORMAT_OLD_BINARY);

        try {
            cpioOut.putNextEntry(newFormatEntry);
            fail("Expected IllegalArgumentException: Unknown header type");
        } catch (final IllegalArgumentException e) {
            // expected
        }

        // test putNextEntry with null entry name
        final CpioArchiveEntry nullNameEntry = new CpioArchiveEntry();
        nullNameEntry.setName(null);
        nullNameEntry.setSize(10);
        nullNameEntry.setFormat(CpioConstants.FORMAT_NEW);

        try {
            cpioOut.putNextEntry(nullNameEntry);
            fail("Expected IllegalArgumentException: null entry name");
        } catch (final IllegalArgumentException e) {
            // expected
        }

        // test putNextEntry with null entry
        try {
            cpioOut.putNextEntry(null);
            fail("Expected IllegalArgumentException: null entry");
        } catch (final IllegalArgumentException e) {
            // expected
        }

        // test putNextEntry with existing entry format
        final CpioArchiveEntry existingFormatEntry = new CpioArchiveEntry();
        existingFormatEntry.setName("test.txt");
        existingFormatEntry.setSize(5);
        existingFormatEntry.setFormat(CpioConstants.FORMAT_NEW);
        cpioOut.putNextEntry(existingFormatEntry);

        try {
            cpioOut.putNextEntry(entry);
            fail("Expected IOException: duplicate entry: test.txt");
        } catch (final IOException e) {
            // expected
        }

        // test putNextEntry with existing entry name and different format
        final CpioArchiveEntry differentFormatEntry = new CpioArchiveEntry();
        differentFormatEntry.setName("test.txt");
        differentFormatEntry.setSize(5);
        differentFormatEntry.setFormat(CpioConstants.FORMAT_OLD_ASCII);

        cpioOut.putNextEntry(differentFormatEntry);

        // verify that headers have been written correctly
        final byte[] headersBytes = cpioOut.getHeadersBytes();
        final byte[] expectedHeaders = CpioTestUtils.createTestHeaders(entry, differentFormatEntry);
        assertArrayEquals(expectedHeaders, headersBytes);

        // test putting multiple entries
        final String[] entryNames = {"test1.txt", "test2.txt"};
        final int[] entrySizes = {5, 10};

        for (int i = 0; i < entryNames.length; i++) {
            final CpioArchiveEntry e = new CpioArchiveEntry();
            e.setName(entryNames[i]);
            e.setSize(entrySizes[i]);
            e.setFormat(CpioConstants.FORMAT_NEW);
            cpioOut.putNextEntry(e);
        }

        // verify that all headers have been written correctly
        final InputStream inputStream = new ByteArrayInputStream(out.toByteArray());
        final byte[] allHeadersBytes = CpioTestUtils.readAllHeaders(inputStream);
        final byte[] expectedAllHeaders = CpioTestUtils.createTestAllHeaders(entryNames, entrySizes, CpioConstants.FORMAT_NEW);
        assertArrayEquals(expectedAllHeaders, allHeadersBytes);

        // test closeArchiveEntry
        cpioOut.closeArchiveEntry();
        assertNull(cpioOut.getCpioEntry());

        // test finish
        cpioOut.finish();
        assertTrue(cpioOut.isFinished());

        // test close
        cpioOut.close();
        assertTrue(cpioOut.isClosed());

        // test putNextEntry after finish
        try {
            cpioOut.putNextEntry(entry);
            fail("Expected IOException: stream is already finished");
        } catch (final IOException e) {
            // expected
        }
    }

    @Test
    public void testWrite() throws IOException {
        final OutputStream out = new ByteArrayOutputStream();
        final CpioArchiveOutputStream cpioOut = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);

        final CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("test.txt");
        entry.setSize(5);
        entry.setFormat(CpioConstants.FORMAT_NEW);

        cpioOut.putNextEntry(entry);

        final byte[] data = "abcde".getBytes();

        cpioOut.write(data, 0, data.length);

        final InputStream inputStream = new ByteArrayInputStream(out.toByteArray());
        final byte[] expectedData = new byte[data.length];
        inputStream.read(expectedData);

        assertArrayEquals(data, expectedData);

        cpioOut.closeArchiveEntry();

        // test write after closeArchiveEntry and before finish
        cpioOut.write(data, 0, data.length);

        final byte[] allBytes = cpioOut.getAllBytes();
        final byte[] expectedAllBytes = CpioTestUtils.createTestAllBytes(data, entry.getName(), entry.getSize(), CpioConstants.FORMAT_NEW);
        assertArrayEquals(expectedAllBytes, allBytes);

        cpioOut.finish();

        // test write after finish
        try {
            cpioOut.write(data, 0, data.length);
            fail("Expected IOException: stream is already finished");
        } catch (final IOException e) {
            // expected
        }

        cpioOut.close();
    }

    @Test
    public void testWriteDifferentSizedEntries() throws IOException {
        final OutputStream out = new ByteArrayOutputStream();
        final CpioArchiveOutputStream cpioOut = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);

        final CpioArchiveEntry entry1 = new CpioArchiveEntry();
        entry1.setName("test1.txt");
        entry1.setSize(5);
        entry1.setFormat(CpioConstants.FORMAT_NEW);

        cpioOut.putNextEntry(entry1);

        final byte[] data1 = "abcde".getBytes();

        cpioOut.write(data1, 0, data1.length);

        final CpioArchiveEntry entry2 = new CpioArchiveEntry();
        entry2.setName("test2.txt");
        entry2.setSize(10);
        entry2.setFormat(CpioConstants.FORMAT_NEW);

        cpioOut.putNextEntry(entry2);

        final byte[] data2 = "fghijklmnopqrstuvwxyz".getBytes();

        cpioOut.write(data2, 0, data2.length);

        final InputStream inputStream = new ByteArrayInputStream(out.toByteArray());
        final byte[] expectedAllBytes = CpioTestUtils.createTestAllBytes(data1, entry1.getName(), entry1.getSize(), CpioConstants.FORMAT_NEW, data2, entry2.getName(), entry2.getSize(), CpioConstants.FORMAT_NEW);
        final byte[] allBytes = cpioOut.getAllBytes();
        assertArrayEquals(expectedAllBytes, allBytes);

        cpioOut.closeArchiveEntry();
        cpioOut.closeArchiveEntry();
        cpioOut.finish();
        cpioOut.close();
    }

    @Test
    public void testWriteSwappedHalfWords() throws IOException {
        final OutputStream out = new ByteArrayOutputStream();
        final CpioArchiveOutputStream cpioOut = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_OLD_BINARY);

        final CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("test.txt");
        entry.setSize(5);
        entry.setFormat(CpioConstants.FORMAT_OLD_BINARY);

        cpioOut.putNextEntry(entry);

        final byte[] data = "abcde".getBytes();

        cpioOut.write(data, 0, data.length);

        final InputStream inputStream = new ByteArrayInputStream(out.toByteArray());
        final byte[] expectedSwappedData = new byte[data.length];
        for (int i = 0; i < data.length; i += 2) {
            final int index = i / 2;
            final int byteOrder = i % 2;
            expectedSwappedData[i] = data[data.length - 1 - index * 2 - byteOrder];
            expectedSwappedData[i + 1] = data[data.length - 1 - index * 2 - !byteOrder];
        }
        inputStream.read(expectedSwappedData);

        assertArrayEquals(data, expectedSwappedData);

        cpioOut.closeArchiveEntry();

        // test write after closeArchiveEntry and before finish
        cpioOut.write(data, 0, data.length);

        final byte[] allBytes = cpioOut.getAllBytes();
        final byte[] expectedAllBytes = CpioTestUtils.createTestAllBytes(data, entry.getName(), entry.getSize(), CpioConstants.FORMAT_OLD_BINARY, null, null, 0, CpioConstants.FORMAT_OLD_BINARY);
        assertArrayEquals(expectedAllBytes, allBytes);

        cpioOut.finish();

        // test write after finish
        try {
            cpioOut.write(data, 0, data.length);
            fail("Expected IOException: stream is already finished");
        } catch (final IOException e) {
            // expected
        }

        cpioOut.close();
    }

    @Test
    public void testWriteWithMultipleFilters() throws IOException {
        final OutputStream out = new ByteArrayOutputStream();
        final CpioArchiveOutputStream cpioOut = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);

        final CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("test.txt");
        entry.setSize(5);
        entry.setFormat(CpioConstants.FORMAT_NEW);

        cpioOut.putNextEntry(entry);

        final byte[] data = "abcde".getBytes();

        cpioOut.finish();

        final ByteArrayOutputStream byteArrayOutputStream1 = new ByteArrayOutputStream();
        final FilterOutputStream filterOutputStream1 = new FilterOutputStream(byteArrayOutputStream1);
        final OutputStream outputStream1 = new FilterOutputStream(filterOutputStream1);
        final OutputStream outputStream2 = new FilterOutputStream(outputStream1);

        cpioOut.put(outputStream2);
        cpioOut.write(data, 0, data.length);

        final InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream1.toByteArray());
        final byte[] expectedData = new byte[data.length];
        inputStream.read(expectedData);

        assertArrayEquals(data, expectedData);

        cpioOut.closeArchiveEntry();
        cpioOut.close();
    }
}