package org.apache.commons.compress.archivers.tar;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static org.junit.Assert.*;

public class TarArchiveOutputStreamTest {

    @Test
    public void testSetLongFileMode() {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
        assertEquals(TarArchiveOutputStream.LONGFILE_ERROR, tarArchiveOutputStream.longFileMode);
    }

    @Test
    public void testFinish() {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream)) {
            tarArchiveOutputStream.finish();
            byte[] expectedBytes = new byte[TarConstants.DEFAULT_RCDSIZE * 2];
            byte[] actualBytes = outputStream.toByteArray();
            assertArrayEquals(expectedBytes, actualBytes);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testClose() {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream)) {
            tarArchiveOutputStream.close();
            assertTrue(tarArchiveOutputStream.closed);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testGetRecordSize() {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        assertEquals(TarBuffer.DEFAULT_RCDSIZE, tarArchiveOutputStream.getRecordSize());
    }

    @Test
    public void testPutArchiveEntry() {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream)) {
            TarArchiveEntry entry = new TarArchiveEntry("testEntry");
            tarArchiveOutputStream.putArchiveEntry(entry);
            assertTrue(tarArchiveOutputStream.haveUnclosedEntry);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testCloseArchiveEntry() {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream)) {
            TarArchiveEntry entry = new TarArchiveEntry("testEntry");
            tarArchiveOutputStream.putArchiveEntry(entry);
            tarArchiveOutputStream.closeArchiveEntry();
            assertFalse(tarArchiveOutputStream.haveUnclosedEntry);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testWrite_byteArray_int_int() {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream)) {
            byte[] data = "Hello, World!".getBytes();
            TarArchiveEntry entry = new TarArchiveEntry("testEntry");
            tarArchiveOutputStream.putArchiveEntry(entry);
            tarArchiveOutputStream.write(data, 0, data.length);
            tarArchiveOutputStream.closeArchiveEntry();
            tarArchiveOutputStream.close();
            byte[] actualBytes = outputStream.toByteArray();
            byte[] expectedBytes = new byte[TarConstants.DEFAULT_RCDSIZE * 2];
            System.arraycopy(data, 0, expectedBytes, 0, data.length);
            assertArrayEquals(expectedBytes, actualBytes);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testWrite_byteArray() {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream)) {
            byte[] data = "Hello, World!".getBytes();
            TarArchiveEntry entry = new TarArchiveEntry("testEntry");
            tarArchiveOutputStream.putArchiveEntry(entry);
            tarArchiveOutputStream.write(data);
            tarArchiveOutputStream.closeArchiveEntry();
            tarArchiveOutputStream.close();
            byte[] actualBytes = outputStream.toByteArray();
            byte[] expectedBytes = new byte[TarConstants.DEFAULT_RCDSIZE * 2];
            System.arraycopy(data, 0, expectedBytes, 0, data.length);
            assertArrayEquals(expectedBytes, actualBytes);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testFlush() {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream)) {
            tarArchiveOutputStream.flush();
            // no exceptions if successfully flushed
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveEntry() {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        File inputFile = new File("testFile");
        String entryName = "testEntry";
        try {
            ArchiveEntry archiveEntry = tarArchiveOutputStream.createArchiveEntry(inputFile, entryName);
            assertNotNull(archiveEntry);
            assertTrue(archiveEntry instanceof TarArchiveEntry);
            assertEquals(inputFile, ((TarArchiveEntry) archiveEntry).getFile());
            assertEquals(entryName, archiveEntry.getName());
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }
}