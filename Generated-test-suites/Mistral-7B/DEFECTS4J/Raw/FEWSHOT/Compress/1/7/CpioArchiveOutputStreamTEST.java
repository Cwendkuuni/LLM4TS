package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.CpioConstants;
import org.apache.commons.compress.utils.CpioUtil;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class CpioArchiveOutputStreamTest {

    @Test
    public void testCpioArchiveOutputStreamConstruction() {
        OutputStream outputStream = new ByteArrayOutputStream(1024);
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream);
        assertEquals(CpioConstants.FORMAT_NEW, cpioArchiveOutputStream.getFormat());

        OutputStream outputStream2 = new ByteArrayOutputStream(1024);
        CpioArchiveOutputStream cpioArchiveOutputStream2 = new CpioArchiveOutputStream(outputStream2, CpioConstants.FORMAT_OLD_BINARY);
        assertEquals(CpioConstants.FORMAT_OLD_BINARY, cpioArchiveOutputStream2.getFormat());
    }

    @Test
    public void testPutNextEntryWithNullEntry() {
        OutputStream outputStream = new ByteArrayOutputStream(1024);
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream);
        assertThrows(NullPointerException.class, () -> cpioArchiveOutputStream.putNextEntry(null));
    }

    @Test
    public void testPutNextEntryWithEmptyName() {
        OutputStream outputStream = new ByteArrayOutputStream(1024);
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream);

        CpioArchiveEntry cpioArchiveEntry = new CpioArchiveEntry();
        cpioArchiveEntry.setName("");

        assertThrows(IllegalArgumentException.class, () -> cpioArchiveOutputStream.putNextEntry(cpioArchiveEntry));
    }

    @Test
    public void testPutNextEntryWithDuplicateName() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream(1024);
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream);

        CpioArchiveEntry cpioArchiveEntry1 = new CpioArchiveEntry();
        cpioArchiveEntry1.setName("test.txt");
        cpioArchiveOutputStream.putNextEntry(cpioArchiveEntry1);

        CpioArchiveEntry cpioArchiveEntry2 = new CpioArchiveEntry();
        cpioArchiveEntry2.setName("test.txt");

        assertThrows(IllegalStateException.class, () -> cpioArchiveOutputStream.putNextEntry(cpioArchiveEntry2));
    }

    @Test
    public void testPutNextEntryWithValidEntry() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream(1024);
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream);

        CpioArchiveEntry cpioArchiveEntry = new CpioArchiveEntry();
        cpioArchiveEntry.setName("test.txt");
        cpioArchiveEntry.setMode(0755);
        cpioArchiveEntry.setSize(10);

        cpioArchiveOutputStream.putNextEntry(cpioArchiveEntry);

        assertEquals(1, cpioArchiveOutputStream.getHeaderSize());
        assertEquals(17, cpioArchiveOutputStream.getEntryHeaderSize(cpioArchiveEntry));
    }

    @Test
    public void testWriteNewEntry() throws IOException {
        byte[] magicNew = CpioConstants.MAGIC_NEW.getBytes();
        byte[] testData = "test data".getBytes();

        OutputStream outputStream = new ByteArrayOutputStream(1024);
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream);

        CpioArchiveEntry cpioArchiveEntry = new CpioArchiveEntry();
        cpioArchiveEntry.setName("test.txt");
        cpioArchiveEntry.setMode(0755);
        cpioArchiveEntry.setSize(testData.length);

        cpioArchiveOutputStream.putNextEntry(cpioArchiveEntry);

        cpioArchiveOutputStream.write(testData);
        cpioArchiveOutputStream.closeArchiveEntry();
        cpioArchiveOutputStream.finish();

        byte[] output = outputStream.toByteArray();

        assertEquals(magicNew.length + 17 + testData.length, output.length);
        assertEquals(magicNew, output, 0, 17);
        assertArrayEquals(testData, output, 17, testData.length);
    }

    @Test
    public void testWriteOldAsciiEntry() throws IOException {
        byte[] magicOldAscii = CpioConstants.MAGIC_OLD_ASCII.getBytes();

        OutputStream outputStream = new ByteArrayOutputStream(1024);
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream);

        CpioArchiveEntry cpioArchiveEntry = new CpioArchiveEntry();
        cpioArchiveEntry.setName("test.txt");
        cpioArchiveEntry.setMode(0755);
        cpioArchiveEntry.setSize(10);

        cpioArchiveOutputStream.putNextEntry(cpioArchiveEntry);
        cpioArchiveOutputStream.writeHeader(cpioArchiveEntry);
        cpioArchiveOutputStream.closeArchiveEntry();
        cpioArchiveOutputStream.finish();

        byte[] output = outputStream.toByteArray();

        assertEquals(magicOldAscii.length + 10 + 17 + 10, output.length);
        assertEquals(magicOldAscii, output, 0, magicOldAscii.length);
    }

    @Test
    public void testWriteOldBinaryEntry() throws IOException {
        byte[] magicOldBinary = CpioConstants.MAGIC_OLD_BINARY;

        OutputStream outputStream = new ByteArrayOutputStream(1024);
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream);

        CpioArchiveEntry cpioArchiveEntry = new CpioArchiveEntry();
        cpioArchiveEntry.setName("test.txt");
        cpioArchiveEntry.setMode(0755);
        cpioArchiveEntry.setSize(10);

        cpioArchiveOutputStream.putNextEntry(cpioArchiveEntry);
        cpioArchiveOutputStream.writeOldBinaryEntry(cpioArchiveEntry, false);
        cpioArchiveOutputStream.closeArchiveEntry();
        cpioArchiveOutputStream.finish();

        byte[] output = outputStream.toByteArray();

        assertEquals(magicOldBinary.length + 10 + 18 + 10, output.length);
        assertEquals(magicOldBinary, output, 0, magicOldBinary.length);
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream(1024);
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream);

        CpioArchiveEntry cpioArchiveEntry = new CpioArchiveEntry();
        cpioArchiveEntry.setName("test.txt");
        cpioArchiveEntry.setMode(0755);
        cpioArchiveEntry.setSize(10);

        cpioArchiveOutputStream.putNextEntry(cpioArchiveEntry);
        cpioArchiveOutputStream.write(new byte[10]);

        cpioArchiveOutputStream.closeArchiveEntry();

        assertEquals(0, cpioArchiveOutputStream.getWritten());
        assertEquals(cpioArchiveEntry, cpioArchiveOutputStream.getCpioEntry());
        assertNull(cpioArchiveOutputStream.getNames().get("test.txt"));
    }

    @Test
    public void testFinish() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream(1024);
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream);

        CpioArchiveEntry cpioArchiveEntry1 = new CpioArchiveEntry();
        cpioArchiveEntry1.setName("test1.txt");
        cpioArchiveEntry1.setMode(0755);
        cpioArchiveEntry1.setSize(10);

        cpioArchiveOutputStream.putNextEntry(cpioArchiveEntry1);
        cpioArchiveOutputStream.write(new byte[10]);

        CpioArchiveEntry cpioArchiveEntry2 = new CpioArchiveEntry();
        cpioArchiveEntry2.setName("test2.txt");
        cpioArchiveEntry2.setMode(0755);
        cpioArchiveEntry2.setSize(20);

        cpioArchiveOutputStream.putNextEntry(cpioArchiveEntry2);
        cpioArchiveOutputStream.write(new byte[20]);

        cpioArchiveOutputStream.finish();

        byte[] output = outputStream.toByteArray();

        assertEquals(2 * (CpioConstants.MAGIC_NEW.length + 17 + 10), output.length);
        CpioUtil.assertBytesEqual(output, 0, 17, CpioConstants.MAGIC_NEW);
        CpioUtil.assertBytesEqual(output, 17, 17, CpioConstants.MAGIC_NEW);

        assertEquals(0, cpioArchiveOutputStream.getWritten());
        assertFalse(cpioArchiveOutputStream.isFinished());
        assertNull(cpioArchiveOutputStream.getCpioEntry());
        assertEquals(2, cpioArchiveOutputStream.getNames().size());
    }

    @Test
    public void testClose() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream(1024);
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream);

        CpioArchiveEntry cpioArchiveEntry1 = new CpioArchiveEntry();
        cpioArchiveEntry1.setName("test1.txt");
        cpioArchiveEntry1.setMode(0755);
        cpioArchiveEntry1.setSize(10);

        cpioArchiveOutputStream.putNextEntry(cpioArchiveEntry1);
        cpioArchiveOutputStream.write(new byte[10]);

        CpioArchiveEntry cpioArchiveEntry2 = new CpioArchiveEntry();
        cpioArchiveEntry2.setName("test2.txt");
        cpioArchiveEntry2.setMode(0755);
        cpioArchiveEntry2.setSize(20);

        cpioArchiveOutputStream.putNextEntry(cpioArchiveEntry2);
        cpioArchiveOutputStream.write(new byte[20]);

        cpioArchiveOutputStream.close();

        assertEquals(0, cpioArchiveOutputStream.getWritten());
        assertEquals(2, cpioArchiveOutputStream.getNames().size());
    }
}