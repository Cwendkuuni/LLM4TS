package org.apache.commons.cli;

import static org.junit.Assert.*;

import org.apache.commons.compress.archivers.cpio.CpioArchiveEntry;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.util.CpioConstants;
import org.apache.commons.compress.util.CpioUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

public class CpioArchiveOutputStreamTest {

    @Test
    public void testCpioArchiveOutputStream() {
        OutputStream outputStream = new ByteArrayOutputStream();
        short format = CpioConstants.FORMAT_NEW;
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, format);
        assertEquals(format, cpioArchiveOutputStream.getFormat());
    }

    @Test
    public void testPutNextEntry() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);

        String entryName = "testEntry.txt";
        long entrySize = 10;
        CpioArchiveEntry cpioArchiveEntry = new CpioArchiveEntry();
        cpioArchiveEntry.setName(entryName);
        cpioArchiveEntry.setSize(entrySize);

        cpioArchiveOutputStream.putNextEntry(cpioArchiveEntry);

        byte[] expectedMagicNew = CpioConstants.MAGIC_NEW;
        byte[] expectedEntryHeader = cpioArchiveEntry.getHeaderBytes();
        byte[] expectedFileContent = new byte[entrySize];

        assertEquals(expectedMagicNew.length, outputStream.size());
        assertEquals(expectedMagicNew, outputStream.toByteArray().subArray(0, expectedMagicNew.length));
        assertEquals(expectedEntryHeader.length, outputStream.size() - expectedMagicNew.length);
        assertEquals(expectedEntryHeader, outputStream.toByteArray().subArray(expectedMagicNew.length, expectedMagicNew.length + expectedEntryHeader.length));

        cpioArchiveOutputStream.close();
    }

    @Test
    public void testWrite() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);

        String entryName = "testEntry.txt";
        long entrySize = 10;
        CpioArchiveEntry cpioArchiveEntry = new CpioArchiveEntry();
        cpioArchiveEntry.setName(entryName);
        cpioArchiveEntry.setSize(entrySize);
        cpioArchiveOutputStream.putNextEntry(cpioArchiveEntry);

        byte[] fileContent = "Hello World!".getBytes();
        cpioArchiveOutputStream.write(fileContent, 0, fileContent.length);

        byte[] expectedFileContent = fileContent;
        byte[] actualFileContent = outputStream.toByteArray().subArray(outputStream.size() - entrySize, outputStream.size());
        assertArrayEquals(expectedFileContent, actualFileContent);

        cpioArchiveOutputStream.close();
    }

    @Test
    public void testFinish() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);

        String entryName = "testEntry.txt";
        long entrySize = 10;
        CpioArchiveEntry cpioArchiveEntry = new CpioArchiveEntry();
        cpioArchiveEntry.setName(entryName);
        cpioArchiveEntry.setSize(entrySize);
        cpioArchiveOutputStream.putNextEntry(cpioArchiveEntry);

        byte[] fileContent = "Hello World!".getBytes();
        cpioArchiveOutputStream.write(fileContent, 0, fileContent.length);

        cpioArchiveOutputStream.finish();

        byte[] expectedTrailer = CpioConstants.TRAILER_MAGIC.getBytes();
        byte[] actualTrailer = outputStream.toByteArray().subArray(outputStream.size() - expectedTrailer.length, outputStream.size());
        assertArrayEquals(expectedTrailer, actualTrailer);

        cpioArchiveOutputStream.close();
    }

    @Test
    public void testClose() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);

        String entryName = "testEntry.txt";
        long entrySize = 10;
        CpioArchiveEntry cpioArchiveEntry = new CpioArchiveEntry();
        cpioArchiveEntry.setName(entryName);
        cpioArchiveEntry.setSize(entrySize);
        cpioArchiveOutputStream.putNextEntry(cpioArchiveEntry);

        byte[] fileContent = "Hello World!".getBytes();
        cpioArchiveOutputStream.write(fileContent, 0, fileContent.length);

        cpioArchiveOutputStream.finish();
        cpioArchiveOutputStream.close();

        HashMap<String, CpioArchiveEntry> entries = cpioArchiveOutputStream.getNames();
        assertEquals(1, entries.size());
        CpioArchiveEntry actualEntry = entries.values().iterator().next();
        assertEquals(entryName, actualEntry.getName());
        assertEquals(entrySize, actualEntry.getSize());

        byte[] actualContent = outputStream.toByteArray();
        byte[] expectedMagicNew = CpioConstants.MAGIC_NEW.getBytes();
        byte[] expectedEntryHeader = cpioArchiveEntry.getHeaderBytes();
        byte[] expectedFileContent = "Hello World!".getBytes();
        byte[] expectedTrailer = CpioConstants.TRAILER_MAGIC.getBytes();

        assertEquals(expectedMagicNew.length + expectedEntryHeader.length + expectedFileContent.length + expectedTrailer.length, actualContent.length);
        assertArrayEquals(expectedMagicNew, actualContent.subArray(0, expectedMagicNew.length));
        assertArrayEquals(expectedEntryHeader, actualContent.subArray(expectedMagicNew.length, expectedMagicNew.length + expectedEntryHeader.length));
        assertArrayEquals(expectedFileContent, actualContent.subArray(expectedMagicNew.length + expectedEntryHeader.length, expectedMagicNew.length + expectedEntryHeader.length + expectedFileContent.length));
        assertArrayEquals(expectedTrailer, actualContent.subArray(expectedMagicNew.length + expectedEntryHeader.length + expectedFileContent.length, actualContent.length));
    }

    @Test
    public void testSetFormat() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream);

        short format = CpioConstants.FORMAT_OLD_BINARY;
        cpioArchiveOutputStream.setFormat(format);
        assertEquals(format, cpioArchiveOutputStream.getFormat());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetFormatInvalidFormat() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream);

        short format = (short) 0xFFFF;
        cpioArchiveOutputStream.setFormat(format);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutNextEntryNullEntry() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream);
        cpioArchiveOutputStream.putNextEntry(null);
    }

    @Test(expected = IOException.class)
    public void testPutNextEntryDuplicateEntry() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream);

        String entryName = "testEntry.txt";
        CpioArchiveEntry cpioArchiveEntry = new CpioArchiveEntry();
        cpioArchiveEntry.setName(entryName);
        cpioArchiveOutputStream.putNextEntry(cpioArchiveEntry);
        cpioArchiveOutputStream.putNextEntry(cpioArchiveEntry);
    }

    @Test
    public void testGetNames() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream);

        String entryName1 = "testEntry1.txt";
        long entrySize1 = 10;
        CpioArchiveEntry cpioArchiveEntry1 = new CpioArchiveEntry();
        cpioArchiveEntry1.setName(entryName1);
        cpioArchiveEntry1.setSize(entrySize1);
        cpioArchiveOutputStream.putNextEntry(cpioArchiveEntry1);

        String entryName2 = "testEntry2.txt";
        long entrySize2 = 20;
        CpioArchiveEntry cpioArchiveEntry2 = new CpioArchiveEntry();
        cpioArchiveEntry2.setName(entryName2);
        cpioArchiveEntry2.setSize(entrySize2);
        cpioArchiveOutputStream.putNextEntry(cpioArchiveEntry2);

        HashMap<String, CpioArchiveEntry> actualEntries = cpioArchiveOutputStream.getNames();
        assertEquals(2, actualEntries.size());
        CpioArchiveEntry actualEntry1 = actualEntries.values().iterator().next();
        assertEquals(entryName1, actualEntry1.getName());
        assertEquals(entrySize1, actualEntry1.getSize());

        CpioArchiveEntry actualEntry2 = actualEntries.values().iterator().next();
        assertEquals(entryName2, actualEntry2.getName());
        assertEquals(entrySize2, actualEntry2.getSize());

        cpioArchiveOutputStream.close();
    }
}