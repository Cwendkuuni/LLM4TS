package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.*;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.utils.CountingOutputStream;
import org.apache.commons.compress.utils.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class TarArchiveOutputStreamTest {
    private File outputFile;
    private TarArchiveOutputStream tarStream;

    @Before
    public void setUp() throws Exception {
        outputFile = File.createTempFile("test", ".tar");
        FileOutputStream out = new FileOutputStream(outputFile);
        tarStream = new TarArchiveOutputStream(out) {
            @Override
            protected void writeEOFRecord() throws IOException {
                // Do nothing
            }
        };
        tarStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
    }

    @After
    public void tearDown() throws Exception {
        tarStream.close();
        outputFile.delete();
    }

    @Test
    public void testPutArchiveEntryAndClose() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        entry.setSize(10);
        tarStream.putArchiveEntry(entry);
        byte[] data = "Test data".getBytes(StandardCharsets.UTF_8);
        tarStream.write(data, 0, data.length);
        tarStream.closeArchiveEntry();

        ArchiveOutputStream testStream = new TarArchiveOutputStream(new FileOutputStream(outputFile), TarBuffer.DEFAULT_BLKSIZE, TarBuffer.DEFAULT_RCDSIZE);
        testStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        ArchiveEntry testEntry = testStream.getInputArchive().getEntry("test.txt");
        byte[] expectedData = new byte[data.length];
        System.arraycopy(data, 0, expectedData, 0, data.length);
        assertEquals(expectedData, testEntry.getInputStream().readAllBytes());
        testStream.close();
    }

    @Test
    public void testPutArchiveEntryWithoutClose() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarStream.putArchiveEntry(entry);
        assertFalse(tarStream.haveUnclosedEntry);
        tarStream.close();
        assertTrue(tarStream.haveUnclosedEntry);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithLongFileName() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test/" + TarConstants.NAMELEN + "long_name.txt");
        tarStream.putArchiveEntry(entry);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithLongFileNameAndTruncate() throws IOException {
        TarArchiveOutputStream os = new TarArchiveOutputStream(new FileOutputStream(outputFile), TarBuffer.DEFAULT_BLKSIZE, TarBuffer.DEFAULT_RCDSIZE) {
            @Override
            protected void writeEOFRecord() throws IOException {
                // Do nothing
            }
        };
        os.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        TarArchiveEntry entry = new TarArchiveEntry("test/" + TarConstants.NAMELEN + "long_name.txt");
        os.putArchiveEntry(entry);
        os.close();
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithLongFileNameAndGNU() throws IOException {
        TarArchiveOutputStream os = new TarArchiveOutputStream(new FileOutputStream(outputFile), TarBuffer.DEFAULT_BLKSIZE, TarBuffer.DEFAULT_RCDSIZE) {
            @Override
            protected void writeEOFRecord() throws IOException {
                // Do nothing
            }
        };
        os.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        TarArchiveEntry entry = new TarArchiveEntry("test/" + TarConstants.NAMELEN + "long_name.txt");
        os.putArchiveEntry(entry);
        os.close();
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithoutCloseAndFinish() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarStream.putArchiveEntry(entry);
        tarStream.finish();
        assertTrue(tarStream.finished);
        assertFalse(tarStream.haveUnclosedEntry);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithoutCloseAndClose() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarStream.putArchiveEntry(entry);
        tarStream.close();
        assertTrue(tarStream.finished);
        assertFalse(tarStream.haveUnclosedEntry);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithLongFileNameWithoutClose() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test/" + TarConstants.NAMELEN + "long_name.txt");
        tarStream.putArchiveEntry(entry);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithLongFileNameAndClose() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test/" + TarConstants.NAMELEN + "long_name.txt");
        tarStream.putArchiveEntry(entry);
        tarStream.close();
    }
}