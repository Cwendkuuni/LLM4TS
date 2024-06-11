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
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.utils.ArchiveUtils;
import org.apache.commons.compress.utils.CountingOutputStream;
import org.apache.commons.compress.utils.TarConstants;
import org.apache.commons.compress.utils.TarEntry;
import org.junit.Test;

public class TarArchiveOutputStreamTest {

    @Test
    public void testFinish() throws IOException {
        // Test finish without any data
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
        tarOutputStream.finish();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        TarArchiveInputStream tarInputStream = new TarArchiveInputStream(inputStream);
        ArchiveEntry entry = tarInputStream.getNextEntry();
        assertEquals(TarConstants.MAGIC, entry.getMagic());
        assertNull(entry);

        // Test finish with data
        outputStream = new ByteArrayOutputStream();
        tarOutputStream = new TarArchiveOutputStream(outputStream);
        byte[] data = new byte[]{(byte) TarConstants.MAGIC, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        tarOutputStream.write(data, 0, data.length);
        tarOutputStream.finish();
        inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        tarInputStream = new TarArchiveInputStream(inputStream);
        entry = tarInputStream.getNextEntry();
        assertEquals(TarConstants.MAGIC, entry.getMagic());
        assertNotNull(entry);
        ByteArrayOutputStream outputStream2 = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = new TarArchiveOutputStream(outputStream2, TarConstants.DEFAULT_BLKSIZE, TarConstants.DEFAULT_RCDSIZE);
        archiveOutputStream.putNextEntry(new TarArchiveEntry("test.txt"));
        archiveOutputStream.write(data, 0, data.length);
        archiveOutputStream.finish();
        ByteArrayInputStream inputStream2 = new ByteArrayInputStream(outputStream2.toByteArray());
        TarInputStream tarInputStream2 = new TarInputStream(inputStream2);
        TarEntry tarEntry = tarInputStream2.getNextTarEntry();
        assertEquals(entry.getName(), tarEntry.getName());
        assertEquals(entry.getSize(), tarEntry.getSize());
        assertEquals(entry.isDirectory(), tarEntry.isDirectory());
        assertTrue(Arrays.equals(entry.getHeaderRecord(), tarEntry.getHeaderRecord()));
        assertArrayEquals(data, tarInputStream2.readAllBytes());
    }

    @Test
    public void testClose() throws IOException {
        // Test close without finish
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
        assertFalse(tarOutputStream.isFinished());
        tarOutputStream.close();
        assertTrue(tarOutputStream.isFinished());

        // Test close after finish
        outputStream = new ByteArrayOutputStream();
        tarOutputStream = new TarArchiveOutputStream(outputStream);
        byte[] data = new byte[]{(byte) TarConstants.MAGIC, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        tarOutputStream.write(data, 0, data.length);
        tarOutputStream.finish();
        tarOutputStream.close();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        TarArchiveInputStream tarInputStream = new TarArchiveInputStream(inputStream);
        ArchiveEntry entry = tarInputStream.getNextEntry();
        assertEquals(TarConstants.MAGIC, entry.getMagic());
        assertNotNull(entry);
        ByteArrayOutputStream outputStream2 = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = new TarArchiveOutputStream(outputStream2, TarConstants.DEFAULT_BLKSIZE, TarConstants.DEFAULT_RCDSIZE);
        archiveOutputStream.putNextEntry(new TarArchiveEntry("test.txt"));
        archiveOutputStream.write(data, 0, data.length);
        archiveOutputStream.finish();
        archiveOutputStream.close();
        ByteArrayInputStream inputStream2 = new ByteArrayInputStream(outputStream2.toByteArray());
        TarInputStream tarInputStream2 = new TarInputStream(inputStream2);
        TarEntry tarEntry = tarInputStream2.getNextTarEntry();
        assertEquals(entry.getName(), tarEntry.getName());
        assertEquals(entry.getSize(), tarEntry.getSize());
        assertEquals(entry.isDirectory(), tarEntry.isDirectory());
        assertTrue(Arrays.equals(entry.getHeaderRecord(), tarEntry.getHeaderRecord()));
        assertArrayEquals(data, tarInputStream2.readAllBytes());
    }

    @Test
    public void testSetLongFileMode() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
        String longFileName = "longFileName" + new String(new char[TarConstants.NAMELEN + 1])[0];
        TarArchiveEntry entry = new TarArchiveEntry(new File("test.txt"), longFileName);
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.write("Test data".getBytes());
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        tarOutputStream.finish();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        TarArchiveInputStream tarInputStream = new TarArchiveInputStream(inputStream);
        assertEquals(0, tarInputStream.read());
        assertEquals(TarConstants.MAGIC, tarInputStream.read());
        assertEquals(TarConstants.VERSION_UNIX, tarInputStream.read());
        assertEquals(1, tarInputStream.read());
        assertEquals(TarConstants.TYPEFLAG_REG, tarInputStream.read());
        assertEquals(TarConstants.FLAG_PAX_REC, tarInputStream.read());
        assertEquals(10, tarInputStream.read());
        assertEquals(TarConstants.NAMELEN + 11, tarInputStream.read());
        assertEquals(longFileName.length(), tarInputStream.readLong());
        assertEquals(10, tarInputStream.read());
        assertEquals(0, tarInputStream.read());
        assertEquals('T', tarInputStream.read());
        assertEquals('e', tarInputStream.read());
        assertEquals('s', tarInputStream.read());
        assertEquals('t', tarInputStream.read());
        assertEquals(' ', tarInputStream.read());
        assertEquals('d', tarInputStream.read());
        assertEquals('a', tarInputStream.read());
        assertEquals('t', tarInputStream.read());
        assertEquals(' ', tarInputStream.read());
        assertEquals('T', tarInputStream.read());
        assertEquals('e', tarInputStream.read());
        assertEquals('s', tarInputStream.read());
        assertEquals('t', tarInputStream.read());
        assertEquals(' ', tarInputStream.read());
        assertNull(tarInputStream.readEntry());
        assertNull(tarInputStream.getNextEntry());
    }

    @Test (expected = RuntimeException.class)
    public void testSetLongFileModeWithLongFileName() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
        String longFileName = "longFileName" + new String(new char[TarConstants.NAMELEN + 1])[0];
        TarArchiveEntry entry = new TarArchiveEntry(new File("test.txt"), longFileName);
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.write("Test data".getBytes());
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
    }

    @Test
    public void testWrite() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
        byte[] data = new byte[]{(byte) TarConstants.MAGIC, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        tarOutputStream.write(data, 0, data.length);
        tarOutputStream.finish();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        TarArchiveInputStream tarInputStream = new TarArchiveInputStream(inputStream);
        byte[] expectedData = new byte[]{(byte) TarConstants.MAGIC, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        assertArrayEquals(expectedData, tarInputStream.readAllBytes());

        // Test small write
        outputStream = new ByteArrayOutputStream();
        tarOutputStream = new TarArchiveOutputStream(outputStream);
        data = new byte[]{1, 2, 3};
        tarOutputStream.write(data, 0, data.length);
        tarOutputStream.finish();
        inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        tarInputStream = new TarArchiveInputStream(inputStream);
        expectedData = new byte[]{TarConstants.MAGIC, TarConstants.VERSION_UNIX, TarConstants.TYPEFLAG_REG, 3,
                TarConstants.NAMELEN.byteValue(), (byte) (TarConstants.NAMELEN + data.length + 1), 0, 0, 0,
                0, 0, 1, 2, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        assertArrayEquals(expectedData, tarInputStream.readAllBytes());

        // Test large write
        outputStream = new ByteArrayOutputStream();
        tarOutputStream = new TarArchiveOutputStream(outputStream);
        data = new byte[1024 * 1024];
        tarOutputStream.write(data, 0, data.length);
        tarOutputStream.finish();
        inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        tarInputStream = new TarArchiveInputStream(inputStream);
        expectedData = new byte[1024 * 1024 + 512];
        Arrays.fill(expectedData, (byte) 0);
        assertArrayEquals(expectedData, tarInputStream.readAllBytes());
    }
}