import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.archivers.tar.TarOutputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TarArchiveOutputStreamTest {
    private ByteArrayOutputStream outputStream;
    private TarOutputStream tarOutputStream;

    @Before
    public void setUp() {
        outputStream = new ByteArrayOutputStream();
        tarOutputStream = new TarArchiveOutputStream(outputStream);
    }

    @After
    public void tearDown() throws IOException {
        tarOutputStream.close();
    }

    @Test
    public void testPutArchiveEntryWithSmallFileName() throws IOException {
        String fileName = "testFile.txt";
        byte[] fileContent = "Hello, World!".getBytes();
        TarArchiveEntry entry = new TarArchiveEntry(fileName);
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.write(fileContent, 0, fileContent.length);
        tarOutputStream.closeArchiveEntry();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        TarArchiveEntry readEntry = new TarArchiveEntry(inputStream);
        byte[] readContent = new byte[fileContent.length];
        inputStream.read(readContent);

        assertEquals(fileName, readEntry.getName());
        assertEquals(fileContent.length, readEntry.getSize());
        assertArrayEquals(fileContent, readContent);
    }

    @Test(expected = RuntimeException.class)
    public void testPutArchiveEntryWithLongFileName() throws IOException {
        String fileName = "a/long/file/name.txt";
        TarArchiveEntry entry = new TarArchiveEntry(fileName);
        tarOutputStream.putArchiveEntry(entry);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithFileNotFound() throws IOException {
        String fileName = "non_existent_file.txt";
        TarArchiveEntry entry = new TarArchiveEntry(fileName);
        tarOutputStream.putArchiveEntry(entry);
    }

    @Test
    public void testSetLongFileMode() throws IOException {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(), 512, 512);
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        String longFileName = "a/long/file/name.txt";
        TarArchiveEntry entry = new TarArchiveEntry(longFileName);
        tarOutputStream.putArchiveEntry(entry);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(tarOutputStream.toByteArray());
        TarArchiveEntry readEntry = new TarArchiveEntry(inputStream);
        assertNull(readEntry.getLongLinkName());

        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        longFileName = "a/long/file/name.txt";
        entry = new TarArchiveEntry(longFileName, TarConstants.GNU_LONGLINK, TarConstants.LF_GNUTYPE_LONGNAME);
        tarOutputStream.putArchiveEntry(entry);

        inputStream = new ByteArrayInputStream(tarOutputStream.toByteArray());
        readEntry = new TarArchiveEntry(inputStream);
        assertEquals(longFileName, readEntry.getLongLinkName());
    }

    @Test
    public void testFinish() throws IOException {
        String fileName1 = "testFile1.txt";
        String fileName2 = "testFile2.txt";
        byte[] fileContent1 = "Hello, World!".getBytes();
        byte[] fileContent2 = "Hello, JUnit!".getBytes();
        TarArchiveEntry entry1 = new TarArchiveEntry(fileName1);
        TarArchiveEntry entry2 = new TarArchiveEntry(fileName2);

        tarOutputStream.putArchiveEntry(entry1);
        tarOutputStream.write(fileContent1, 0, fileContent1.length);
        tarOutputStream.closeArchiveEntry();

        tarOutputStream.putArchiveEntry(entry2);
        tarOutputStream.write(fileContent2, 0, fileContent2.length);
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.finish();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        TarArchiveEntry readEntry1 = new TarArchiveEntry(inputStream);
        assertEquals(fileName1, readEntry1.getName());
        assertEquals(fileContent1.length, readEntry1.getSize());

        TarArchiveEntry readEntry2 = new TarArchiveEntry(inputStream);
        assertEquals(fileName2, readEntry2.getName());
        assertEquals(fileContent2.length, readEntry2.getSize());

        // Check that there are two EOF records
        byte[] eofRecord = new byte[TarConstants.STANDARD_BLOCKSIZE];
        inputStream.read(eofRecord);
        assertTrue(Arrays.equals(eofRecord, new byte[TarConstants.STANDARD_BLOCKSIZE]));
        inputStream.read(eofRecord);
        assertTrue(Arrays.equals(eofRecord, new byte[TarConstants.STANDARD_BLOCKSIZE]));
    }

    @Test
    public void testClose() throws IOException {
        String fileName = "testFile.txt";
        byte[] fileContent = "Hello, World!".getBytes();
        TarArchiveEntry entry = new TarArchiveEntry(fileName);
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.write(fileContent, 0, fileContent.length);

        tarOutputStream.close();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        TarArchiveEntry readEntry = new TarArchiveEntry(inputStream);
        byte[] readContent = new byte[fileContent.length];
        inputStream.read(readContent);

        assertEquals(fileName, readEntry.getName());
        assertEquals(fileContent.length, readEntry.getSize());
        assertArrayEquals(fileContent, readContent);

        assertFalse(outputStream.hasRemaining());
    }

    @Test(expected = IOException.class)
    public void testCloseTwice() throws IOException {
        String fileName = "testFile.txt";
        byte[] fileContent = "Hello, World!".getBytes();
        TarArchiveEntry entry = new TarArchiveEntry(fileName);
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.write(fileContent, 0, fileContent.length);
        tarOutputStream.close();
        tarOutputStream.close();
    }
}