package org.apache.commons.compress.archivers.tar;

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
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TarArchiveOutputStreamTest {
    private File testFile;
    private OutputStream outputStream;
    private TarArchiveOutputStream tarOutputStream;

    @Before
    public void setUp() throws Exception {
        testFile = File.createTempFile("test", null);
        outputStream = new CountingOutputStream(new FileOutputStream(testFile));
        tarOutputStream = new TarArchiveOutputStream(outputStream);
    }

    @After
    public void tearDown() throws Exception {
        IOUtils.closeQuietly(tarOutputStream);
        IOUtils.closeQuietly(outputStream);
        testFile.delete();
    }

    @Test
    public void testCreateTarArchiveWithSingleEntry() throws Exception {
        // create a test entry
        ArchiveEntry entry = new TarArchiveEntry("test.txt");
        entry.setSize(10);
        entry.setName("test.txt");

        // write the entry to the archive
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.write("Hello World!".getBytes());
        tarOutputStream.closeArchiveEntry();

        tarOutputStream.finish();
        tarOutputStream.close();

        // read the created archive back
        InputStream inputStream = new FileInputStream(testFile);
        ArchiveOutputStream archiveOutputStream = new TarArchiveOutputStream(new CountingOutputStream(new FileOutputStream("test_output.tar")));
        TarArchiveInputStream tarInputStream = new TarArchiveInputStream(inputStream);
        ArchiveEntry readEntry = tarInputStream.getNextEntry();

        // check the read entry matches the original
        assertNotNull(readEntry);
        assertEquals(entry.getName(), readEntry.getName());
        assertEquals(entry.getSize(), readEntry.getSize());

        // read the content of the entry
        byte[] buffer = new byte[10];
        inputStream = tarInputStream.getInputStream();
        inputStream.read(buffer);
        assertArrayEquals(new String(buffer, StandardCharsets.UTF_8).getBytes(), "Hello World!".getBytes());

        // close the archive stream
        archiveOutputStream.close();
        IOUtils.closeQuietly(tarInputStream);
        IOUtils.closeQuietly(inputStream);

        // check the file size and content of the created archive
        assertEquals(11, testFile.length());
        byte[] fileContent = new byte[testFile.length()];
        InputStream fileInputStream = new FileInputStream(testFile);
        fileInputStream.read(fileContent);

        // check the content of the archive matches the expected content
        byte[] expectedContent = new byte[]{(byte) 0x00, (byte) 0x00, (byte) 0x14, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                (byte) 'T', (byte) 'a', (byte) 'r', (byte) '\u001A', (byte) '!', (byte) 'H', (byte) 'e', (byte) 'l', (byte) 'l',
                (byte) 'o', (byte) ' ', (byte) 'W', (byte) 'o', (byte) 'r', (byte) 'l', (byte) 'd', (byte) '!', (byte) '\u001A',
                (byte) '\u001A', (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x0B, (byte) 't', (byte) 'e', (byte) 's',
                (byte) 't', (byte) '.', (byte) 't', (byte) 'x', (byte) 't', (byte) '\u001A'};
        assertArrayEquals(fileContent, expectedContent);

        // clean up
        IOUtils.closeQuietly(fileInputStream);
        testFile.delete();
    }

    @Test(expected = IOException.class)
    public void testCreateTarArchiveWithLongFileName() throws Exception {
        // create a test entry with a long file name
        ArchiveEntry entry = new TarArchiveEntry("test_long_filename.txt");
        entry.setSize(10);
        entry.setName("test_long_filename.txt");

        // set the long file mode to LONGFILE_TRUNCATE
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);

        // write the entry to the archive
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.write("Hello World!".getBytes());
        tarOutputStream.closeArchiveEntry();

        tarOutputStream.finish();
        tarOutputStream.close();

        // read the created archive back
        InputStream inputStream = new FileInputStream(testFile);
        ArchiveOutputStream archiveOutputStream = new TarArchiveOutputStream(new CountingOutputStream(new FileOutputStream("test_output.tar")));
        TarArchiveInputStream tarInputStream = new TarArchiveInputStream(inputStream);
        ArchiveEntry readEntry = tarInputStream.getNextEntry();

        // the read entry should be null, because the file name was truncated
        assertNull(readEntry);

        // clean up
        IOUtils.closeQuietly(tarInputStream);
        IOUtils.closeQuietly(inputStream);
        testFile.delete();
    }

    @Test(expected = IOException.class)
    public void testCreateTarArchiveWithLongFileNameAndLongFileModeGNU() throws Exception {
        // create a test entry with a long file name
        ArchiveEntry entry = new TarArchiveEntry("test_long_filename.txt");
        entry.setSize(10);
        entry.setName("test_long_filename.txt");

        // set the long file mode to LONGFILE_GNU
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);

        // write the entry to the archive
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.write("Hello World!".getBytes());
        tarOutputStream.closeArchiveEntry();

        tarOutputStream.finish();
        tarOutputStream.close();

        // read the created archive back
        InputStream inputStream = new FileInputStream(testFile);
        ArchiveOutputStream archiveOutputStream = new TarArchiveOutputStream(new CountingOutputStream(new FileOutputStream("test_output.tar")));
        TarArchiveInputStream tarInputStream = new TarArchiveInputStream(inputStream);
        ArchiveEntry readEntry = tarInputStream.getNextEntry();

        // the read entry should not be null, because the file name was stored as a GNU extended header
        assertNotNull(readEntry);
        assertEquals("test_long_filename.txt", readEntry.getName());

        // clean up
        IOUtils.closeQuietly(tarInputStream);
        IOUtils.closeQuietly(inputStream);
        testFile.delete();
    }

    @Test
    public void testCreateTarArchiveWithGzippedEntry() throws Exception {
        // create a test gzipped entry
        ArchiveEntry gzippedEntry = new TarArchiveEntry("test.gz");
        gzippedEntry.setSize(10);
        gzippedEntry.setName("test.gz");

        // write the gzipped entry to the archive
        tarOutputStream.putNextEntry(gzippedEntry);
        byte[] gzippedData = "Hello World!".getBytes();
        GZIPOutputStream gzipOutputStream = new GZIPOutputStream(tarOutputStream);
        gzipOutputStream.write(gzippedData);
        gzipOutputStream.finish();
        tarOutputStream.closeEntry();

        tarOutputStream.finish();
        tarOutputStream.close();

        // read the created archive back
        InputStream inputStream = new FileInputStream(testFile);
        ArchiveInputStream archiveInputStream = new TarArchiveInputStream(inputStream);
        ArchiveEntry readEntry = archiveInputStream.getNextEntry();

        // check the read entry matches the original
        assertNotNull(readEntry);
        assertEquals(gzippedEntry.getName(), readEntry.getName());
        assertEquals(gzippedEntry.getSize(), readEntry.getSize());

        // read the content of the entry as a byte array
        byte[] buffer = new byte[10];
        InputStream entryInputStream = archiveInputStream.getInputStream();
        int bytesRead = entryInputStream.read(buffer);
        assertArrayEquals(new String(Arrays.copyOfRange(buffer, 0, bytesRead), StandardCharsets.UTF_8).getBytes(), gzippedData);

        // clean up
        IOUtils.closeQuietly(archiveInputStream);
        IOUtils.closeQuietly(inputStream);
        testFile.delete();
    }

    @Test
    public void testCreateTarArchiveWithDirectoryEntry() throws Exception {
        // create a test directory entry
        ArchiveEntry directoryEntry = new TarArchiveEntry("test_dir");
        directoryEntry.setSize(0);
        directoryEntry.setName("test_dir/");
        directoryEntry.setDirectory(true);

        // write the directory entry to the archive
        tarOutputStream.putArchiveEntry(directoryEntry);
        tarOutputStream.finish();
        tarOutputStream.close();

        // read the created archive back
        InputStream inputStream = new FileInputStream(testFile);
        ArchiveInputStream archiveInputStream = new TarArchiveInputStream(inputStream);
        ArchiveEntry readEntry = archiveInputStream.getNextEntry();

        // check the read entry matches the original
        assertNotNull(readEntry);
        assertEquals(directoryEntry.getName(), readEntry.getName());
        assertTrue(readEntry.isDirectory());

        // read the next entry, which should be the EOF record
        readEntry = archiveInputStream.getNextEntry();
        assertNull(readEntry);

        // clean up
        IOUtils.closeQuietly(archiveInputStream);
        IOUtils.closeQuietly(inputStream);
        testFile.delete();
    }

    @Test
    public void testCreateTarArchiveWithMultipleEntries() throws Exception {
        // create two test entries
        ArchiveEntry entry1 = new TarArchiveEntry("test1.txt");
        entry1.setSize(5);
        entry1.setName("test1.txt");

        ArchiveEntry entry2 = new TarArchiveEntry("test2.txt");
        entry2.setSize(5);
        entry2.setName("test2.txt");

        // write the entries to the archive
        tarOutputStream.putArchiveEntry(entry1);
        tarOutputStream.write("Hello1".getBytes());
        tarOutputStream.closeArchiveEntry();

        tarOutputStream.putArchiveEntry(entry2);
        tarOutputStream.write("Hello2".getBytes());
        tarOutputStream.closeArchiveEntry();

        tarOutputStream.finish();
        tarOutputStream.close();

        // read the created archive back
        InputStream inputStream = new FileInputStream(testFile);
        ArchiveInputStream archiveInputStream = new TarArchiveInputStream(inputStream);

        ArchiveEntry readEntry1 = archiveInputStream.getNextEntry();
        assertNotNull(readEntry1);
        assertEquals(entry1.getName(), readEntry1.getName());
        assertEquals(entry1.getSize(), readEntry1.getSize());

        byte[] buffer1 = new byte[5];
        InputStream entryInputStream1 = archiveInputStream.getInputStream();
        int bytesRead1 = entryInputStream1.read(buffer1);
        assertArrayEquals(new String(Arrays.copyOfRange(buffer1, 0, bytesRead1), StandardCharsets.UTF_8).getBytes(), "Hello1".getBytes());

        ArchiveEntry readEntry2 = archiveInputStream.getNextEntry();
        assertNotNull(readEntry2);
        assertEquals(entry2.getName(), readEntry2.getName());
        assertEquals(entry2.getSize(), readEntry2.getSize());

        byte[] buffer2 = new byte[5];
        InputStream entryInputStream2 = archiveInputStream.getInputStream();
        int bytesRead2 = entryInputStream2.read(buffer2);
        assertArrayEquals(new String(Arrays.copyOfRange(buffer2, 0, bytesRead2), StandardCharsets.UTF_8).getBytes(), "Hello2".getBytes());

        // clean up
        IOUtils.closeQuietly(archiveInputStream);
        IOUtils.closeQuietly(inputStream);
        testFile.delete();
    }

    @Test
    public void testCreateTarArchiveWithClosedEntry() throws Exception {
        // create a test entry
        ArchiveEntry entry = new TarArchiveEntry("test.txt");
        entry.setSize(10);
        entry.setName("test.txt");

        // write the entry to the archive without closing it
        tarOutputStream.putArchiveEntry(entry);

        // write some data to the archive
        tarOutputStream.write("Hello World!".getBytes());

        // check that the entry is still open and can be written to
        assertFalse(tarOutputStream.hasNextEntry());
        tarOutputStream.write("An error occurred!".getBytes());

        // close the entry and write the EOF record
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.finish();
        tarOutputStream.close();

        // read the created archive back
        InputStream inputStream = new FileInputStream(testFile);
        ArchiveInputStream archiveInputStream = new TarArchiveInputStream(inputStream);
        ArchiveEntry readEntry = archiveInputStream.getNextEntry();

        // check the read entry matches the original
        assertNotNull(readEntry);
        assertEquals(entry.getName(), readEntry.getName());
        assertEquals(entry.getSize(), readEntry.getSize());

        // read the content of the entry
        byte[] buffer = new byte[10];
        InputStream entryInputStream = archiveInputStream.getInputStream();
        int bytesRead = entryInputStream.read(buffer);
        assertArrayEquals(new String(Arrays.copyOfRange(buffer, 0, bytesRead), StandardCharsets.UTF_8).getBytes(), "Hello World!".getBytes());

        // clean up
        IOUtils.closeQuietly(archiveInputStream);
        IOUtils.closeQuietly(inputStream);
        testFile.delete();
    }
}