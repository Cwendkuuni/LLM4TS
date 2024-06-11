
package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class TarArchiveOutputStreamTest {

    @Test
    public void testConstructor() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(out);
        assertEquals(TarBuffer.DEFAULT_BLKSIZE, tarOut.getBuffer().getBlockSize());
        assertEquals(TarBuffer.DEFAULT_RCDSIZE, tarOut.getBuffer().getRecordSize());

        tarOut = new TarArchiveOutputStream(out, 512);
        assertEquals(512, tarOut.getBuffer().getBlockSize());
        assertEquals(TarBuffer.DEFAULT_RCDSIZE, tarOut.getBuffer().getRecordSize());

        tarOut = new TarArchiveOutputStream(out, 512, 128);
        assertEquals(512, tarOut.getBuffer().getBlockSize());
        assertEquals(128, tarOut.getBuffer().getRecordSize());
    }

    @Test
    public void testSetLongFileMode() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(out);
        tarOut.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);

        assertEquals(TarArchiveOutputStream.LONGFILE_GNU, tarOut.getLongFileMode());
    }

    @Test
    public void testFinish() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(out);

        tarOut.finish();

        byte[] expectedBytes = new byte[512];
        assertEquals(expectedBytes.length, out.toByteArray().length);
        assertTrue(compareBytes(expectedBytes, out.toByteArray()));
    }

    @Test
    public void testClose() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(out);

        tarOut.close();
        assertTrue(tarOut.isClosed());
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(out);

        Path inputFile = Files.createTempFile("test", ".txt");
        inputFile.toFile().deleteOnExit();

        int numFiles = 1;
        for (int i = 0; i < numFiles; i++) {
            String entryName = "file" + i + ".txt";
            ArchiveEntry entry = tarOut.createArchiveEntry(inputFile.toFile(), entryName);

            assertEquals(entryName, entry.getName());
            assertFalse(entry.isDirectory());
            assertEquals(0, entry.getSize());
        }

        Files.delete(inputFile);
        tarOut.close();
    }

    private boolean compareBytes(byte[] expected, byte[] actual) {
        if (expected.length != actual.length) {
            return false;
        }

        for (int i = 0; i < expected.length; i++) {
            if (expected[i] != actual[i]) {
                return false;
            }
        }
        return true;
    }
}
