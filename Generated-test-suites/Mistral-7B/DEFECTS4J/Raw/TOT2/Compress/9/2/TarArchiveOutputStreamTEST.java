package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.*;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.utils.CountingOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Test case for {@link TarArchiveOutputStream}.
 */
public class TarArchiveOutputStreamTest {

    private OutputStream outputStream;
    private ArchiveOutputStream tarArchiveOutputStream;
    private CountingOutputStream countingOutputStream;

    @Before
    public void setUp() throws IOException {
        outputStream = new CountingOutputStream(new ByteArrayOutputStream());
        countingOutputStream = new CountingOutputStream(outputStream);
        tarArchiveOutputStream = new TarArchiveOutputStream(countingOutputStream);
    }

    @After
    public void tearDown() throws IOException {
        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.close();
    }

    /**
     * Test that an entry can be added to the archive.
     *
     * @throws IOException if an error occurs
     */
    @Test
    public void testPutArchiveEntry() throws IOException {
        final String entryName = "test.txt";
        final String entryContent = "Hello, World!";
        final byte[] entryContentBytes = entryContent.getBytes(StandardCharsets.UTF_8);

        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry(entryName);
        tarArchiveEntry.setSize(entryContentBytes.length);
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        tarArchiveOutputStream.write(entryContentBytes, 0, entryContentBytes.length);
        tarArchiveOutputStream.closeArchiveEntry();

        InputStream inputStream = tarArchiveOutputStream.getInputStream(new TarArchiveEntry(entryName));
        assertEquals(entryContent, IOUtils.toString(inputStream, StandardCharsets.UTF_8));
    }

    /**
     * Test that an exception is thrown when an entry is added after the archive has been finished.
     *
     * @throws IOException if an error occurs
     */
    @Test(expected = IOException.class)
    public void testPutArchiveEntryAfterFinish() throws IOException {
        tarArchiveOutputStream.finish();
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
    }

    /**
     * Test that an exception is thrown when a null entry is added.
     *
     * @throws IOException if an error occurs
     */
    @Test(expected = NullPointerException.class)
    public void testPutArchiveEntryNullEntry() throws IOException {
        tarArchiveOutputStream.putArchiveEntry(null);
    }

    /**
     * Test that an exception is thrown when a long file name is added and the long file mode is not set to LONGFILE_GNU.
     *
     * @throws IOException if an error occurs
     */
    @Test(expected = RuntimeException.class)
    public void testPutArchiveEntryLongFileName() throws IOException {
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
        final String longFileName = "verylongfilenamethatislongerthanNAMELENbytes";
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry(longFileName);
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
    }

    /**
     * Test that the archive can be read back from the output stream.
     *
     * @throws IOException if an error occurs
     */
    @Test
    public void testReadArchive() throws IOException {
        final String entryName1 = "entry1.txt";
        final String entryContent1 = "First entry.";
        final String entryName2 = "entry2.txt";
        final String entryContent2 = "Second entry.";

        TarArchiveEntry entry1 = new TarArchiveEntry(entryName1);
        TarArchiveEntry entry2 = new TarArchiveEntry(entryName2);

        tarArchiveOutputStream.putArchiveEntry(entry1);
        tarArchiveOutputStream.write(entryContent1.getBytes(StandardCharsets.UTF_8));
        tarArchiveOutputStream.closeArchiveEntry();

        tarArchiveOutputStream.putArchiveEntry(entry2);
        tarArchiveOutputStream.write(entryContent2.getBytes(StandardCharsets.UTF_8));
        tarArchiveOutputStream.closeArchiveEntry();

        tarArchiveOutputStream.finish();

        TarArchiveOutputStream newTarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        newTarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        InputStream inputStream = new ByteArrayInputStream(tarArchiveOutputStream.toStringArray());
        ArchiveInputStream archiveInputStream = new ArchiveInputStream(inputStream);

        TarArchiveEntry readEntry1 = (TarArchiveEntry) archiveInputStream.getNextEntry();
        assertEquals(entryName1, readEntry1.getName());
        assertEquals(entryContent1, IOUtils.toString(archiveInputStream, StandardCharsets.UTF_8));

        TarArchiveEntry readEntry2 = (TarArchiveEntry) archiveInputStream.getNextEntry();
        assertEquals(entryName2, readEntry2.getName());
        assertEquals(entryContent2, IOUtils.toString(archiveInputStream, StandardCharsets.UTF_8));

        archiveInputStream.close();
        newTarArchiveOutputStream.close();
    }
}