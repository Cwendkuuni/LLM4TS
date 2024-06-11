package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.junit.Test;

public class TarArchiveOutputStreamTest {

    @Test
    public void testFinishAndCloseWithNoEntries() throws Exception {
        try (TarArchiveOutputStream tarOut = new TarArchiveOutputStream(new MockOutputStream())) {
            tarOut.finish();
            tarOut.close();
        }
        // no exception expected
    }

    @Test
    public void testFinishAndCloseAfterPuttingAndClosingSingleEntry() throws Exception {
        try (TarArchiveOutputStream tarOut = new TarArchiveOutputStream(new MockOutputStream())) {
            TarArchiveEntry entry = new TarArchiveEntry("test.txt");
            tarOut.putArchiveEntry(entry);
            tarOut.closeArchiveEntry();
            tarOut.finish();
            tarOut.close();
        }
        // no exception expected
    }

    @Test
    public void testFinishAndCloseAfterPuttingManyEntries() throws Exception {
        try (TarArchiveOutputStream tarOut = new TarArchiveOutputStream(new MockOutputStream())) {
            for (int i = 0; i < 10; i++) {
                TarArchiveEntry entry = new TarArchiveEntry("test_" + i + ".txt");
                tarOut.putArchiveEntry(entry);
                tarOut.closeArchiveEntry();
            }
            tarOut.finish();
            tarOut.close();
        }
        // no exception expected
    }

    @Test
    public void testSetLongFileMode() throws Exception {
        try (TarArchiveOutputStream tarOut = new TarArchiveOutputStream(new MockOutputStream())) {
            TarArchiveEntry entry = new TarArchiveEntry("test.txt");
            entry.setSize(TarConstants.NAMELEN + 1);

            tarOut.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
            tarOut.putArchiveEntry(entry);
            tarOut.closeArchiveEntry();
            tarOut.finish();
            tarOut.close();

            TarArchiveOutputStream tarOut2 = new TarArchiveOutputStream(new MockOutputStream());
            tarOut2.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
            tarOut2.putArchiveEntry(entry);
            tarOut2.closeArchiveEntry();
            tarOut2.finish();
            tarOut2.close();
        }
        // no exception expected
    }

    @Test
    public void testWriteWithEmptyBuffer() throws Exception {
        try (TarArchiveOutputStream tarOut = new TarArchiveOutputStream(new MockOutputStream())) {
            byte[] testData = new byte[100];
            tarOut.write(testData, 0, 0);
        }
        // no exception expected
    }

    @Test
    public void testWriteWithPartiallyFilledBuffer() throws Exception {
        try (TarArchiveOutputStream tarOut = new TarArchiveOutputStream(new MockOutputStream())) {
            byte[] testData = new byte[200];
            tarOut.write(testData, 0, 100);
            tarOut.write(testData, 0, 100);
        }
        // no exception expected
    }

    @Test
    public void testWriteWithFullBuffer() throws Exception {
        try (TarArchiveOutputStream tarOut = new TarArchiveOutputStream(new MockOutputStream())) {
            byte[] testData = new byte[512];
            tarOut.write(testData, 0, testData.length);
        }
        // no exception expected
    }

    @Test
    public void testWriteWithWrongSize() throws Exception {
        try (TarArchiveOutputStream tarOut = new TarArchiveOutputStream(new MockOutputStream())) {
            byte[] testData = new byte[512];
            tarOut.write(testData, 0, testData.length + 1);
            assertThrows(IOException.class, () -> tarOut.write(testData, 0, testData.length + 1));
        }
        // no exception expected for first write
        // IOException expected for second write
    }

    @Test
    public void testWriteWithLongFileName() throws Exception {
        try (TarArchiveOutputStream tarOut = new TarArchiveOutputStream(new MockOutputStream())) {
            TarArchiveEntry entry = new TarArchiveEntry("test_" + TarConstants.NAMELEN + "_long_name.txt");
            tarOut.putArchiveEntry(entry);
            byte[] testData = new byte[512];
            tarOut.write(testData, 0, testData.length);
            tarOut.closeArchiveEntry();
            tarOut.finish();
            tarOut.close();
        }
        // no exception expected
    }

    @Test
    public void testCloseArchiveEntryWithoutFinish() throws Exception {
        try (TarArchiveOutputStream tarOut = new TarArchiveOutputStream(new MockOutputStream())) {
            TarArchiveEntry entry = new TarArchiveEntry("test.txt");
            tarOut.putArchiveEntry(entry);
            tarOut.close(); // should throw an exception
            tarOut.closeArchiveEntry();
        }
        assertThrows(IOException.class, () -> tarOut.close());
    }

    @Test
    public void testPutArchiveEntryWithNullEntry() throws Exception {
        try (TarArchiveOutputStream tarOut = new TarArchiveOutputStream(new MockOutputStream())) {
            assertThrows(NullPointerException.class, () -> tarOut.putArchiveEntry(null));
        }
        // NullPointerException expected
    }

    @Test
    public void testPutArchiveEntryWithNonTarEntry() throws Exception {
        try (TarArchiveOutputStream tarOut = new TarArchiveOutputStream(new MockOutputStream())) {
            ArchiveEntry nonTarEntry = new File("test.txt").toPath().toFile();
            assertThrows(ClassCastException.class, () -> tarOut.putArchiveEntry(nonTarEntry));
        }
        // ClassCastException expected
    }

    @Test
    public void testPutArchiveEntryWithLongFileName() throws Exception {
        try (TarArchiveOutputStream tarOut = new TarArchiveOutputStream(new MockOutputStream())) {
            String longFileName = new String(new char[TarConstants.NAMELEN + 1]);
            TarArchiveEntry entry = new TarArchiveEntry(longFileName);
            tarOut.putArchiveEntry(entry);
            tarOut.closeArchiveEntry();
            tarOut.finish();
            tarOut.close();
        }
        // no exception expected
    }

    @Test
    public void testPutArchiveEntryWithLongFileNameAndLongFileMode() throws Exception {
        try (TarArchiveOutputStream tarOut = new TarArchiveOutputStream(new MockOutputStream())) {
            String longFileName = new String(new char[TarConstants.NAMELEN + 1]);
            TarArchiveEntry entry = new TarArchiveEntry(longFileName);
            tarOut.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
            tarOut.putArchiveEntry(entry);
            tarOut.closeArchiveEntry();
            tarOut.finish();
            tarOut.close();
        }
        // no exception expected
    }

    @Test
    public void testPutArchiveEntryWithLongFileNameAndLongFileModeTruncate() throws Exception {
        try (TarArchiveOutputStream tarOut = new TarArchiveOutputStream(new MockOutputStream())) {
            String longFileName = new String(new char[TarConstants.NAMELEN + 1]);
            TarArchiveEntry entry = new TarArchiveEntry(longFileName);
            tarOut.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
            tarOut.putArchiveEntry(entry);
            tarOut.closeArchiveEntry();
            tarOut.finish();
            tarOut.close();
        }
        // no exception expected
    }

    @Test
    public void testPutArchiveEntryWithLongFileNameAndLongFileModeError() throws Exception {
        try (TarArchiveOutputStream tarOut = new TarArchiveOutputStream(new MockOutputStream())) {
            String longFileName = new String(new char[TarConstants.NAMELEN + 1]);
            TarArchiveEntry entry = new TarArchiveEntry(longFileName);
            tarOut.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
            tarOut.putArchiveEntry(entry);
            assertThrows(IOException.class, () -> tarOut.putArchiveEntry(entry));
        }
        // IOException expected
    }

    @Test
    public void testGetRecordSize() throws Exception {
        try (TarArchiveOutputStream tarOut = new TarArchiveOutputStream(new MockOutputStream())) {
            assertEquals(512, tarOut.getRecordSize());
        }
        // no exception expected
    }

    @Test
    public void testClose() throws Exception {
        try (TarArchiveOutputStream tarOut = new TarArchiveOutputStream(new MockOutputStream())) {
            assertFalse(tarOut.isClosed());
            tarOut.close();
            assertTrue(tarOut.isClosed());
        }
        // no exception expected
    }

    @Test
    public void testHasUnclosedEntry() throws Exception {
        try (TarArchiveOutputStream tarOut = new TarArchiveOutputStream(new MockOutputStream())) {
            TarArchiveEntry entry = new TarArchiveEntry("test.txt");
            tarOut.putArchiveEntry(entry);
            assertFalse(tarOut.hasUnclosedEntry());
            tarOut.closeArchiveEntry();
            assertFalse(tarOut.hasUnclosedEntry());
            tarOut.close();
            assertFalse(tarOut.hasUnclosedEntry());
        }
        // no exception expected
    }

    @Test
    public void testHasUnclosedEntryAfterPuttingAndClosingMultipleEntries() throws Exception {
        try (TarArchiveOutputStream tarOut = new TarArchiveOutputStream(new MockOutputStream())) {
            for (int i = 0; i < 10; i++) {
                TarArchiveEntry entry = new TarArchiveEntry("test_" + i + ".txt");
                tarOut.putArchiveEntry(entry);
                tarOut.closeArchiveEntry();
            }
            assertFalse(tarOut.hasUnclosedEntry());
            tarOut.close();
            assertFalse(tarOut.hasUnclosedEntry());
        }
        // no exception expected
    }

    @Test
    public void testHasUnclosedEntryAfterPuttingAndNotClosingSingleEntry() throws Exception {
        try (TarArchiveOutputStream tarOut = new TarArchiveOutputStream(new MockOutputStream())) {
            TarArchiveEntry entry = new TarArchiveEntry("test.txt");
            tarOut.putArchiveEntry(entry);
            assertTrue(tarOut.hasUnclosedEntry());
            tarOut.close();
            assertFalse(tarOut.hasUnclosedEntry());
        }
        // no exception expected
    }

    @Test
    public void testHasUnclosedEntryAfterPuttingAndNotClosingMultipleEntries() throws Exception {
        try (TarArchiveOutputStream tarOut = new TarArchiveOutputStream(new MockOutputStream())) {
            for (int i = 0; i < 10; i++) {
                TarArchiveEntry entry = new TarArchiveEntry("test_" + i + ".txt");
                tarOut.putArchiveEntry(entry);
            }
            assertTrue(tarOut.hasUnclosedEntry());
            tarOut.close();
            assertFalse(tarOut.hasUnclosedEntry());
        }
        // no exception expected
    }

    private static class MockOutputStream extends OutputStream {
        @Override
        public void write(int b) throws IOException {
            // no-op
        }

        @Override
        public void write(byte[] b, int off, int len) throws IOException {
            // no-op
        }

        @Override
        public void close() throws IOException {
            // no-op
        }

        @Override
        public void flush() throws IOException {
            // no-op
        }
    }
}