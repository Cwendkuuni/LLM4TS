import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TarArchiveOutputStreamTest {

    private TarArchiveOutputStream tarOut;
    private ByteArrayOutputStream byteOut;

    @Before
    public void setUp() throws Exception {
        byteOut = new ByteArrayOutputStream();
        tarOut = new TarArchiveOutputStream(byteOut, TarConstants.DEFAULT_BLKSIZE, TarConstants.DEFAULT_RCDSIZE);
    }

    @After
    public void tearDown() throws Exception {
        tarOut.close();
    }

    @Test
    public void testConstructors() {
        TarArchiveOutputStream tarOut1 = new TarArchiveOutputStream(new File("test.tar").getOutputStream());
        TarArchiveOutputStream tarOut2 = new TarArchiveOutputStream(new File("test.tar").getOutputStream(), TarConstants.DEFAULT_BLKSIZE);
        TarArchiveOutputStream tarOut3 = new TarArchiveOutputStream(new File("test.tar").getOutputStream(), TarConstants.DEFAULT_BLKSIZE, TarConstants.DEFAULT_RCDSIZE);

        assertNotNull(tarOut1);
        assertNotNull(tarOut2);
        assertNotNull(tarOut3);
    }

    @Test
    public void testSetLongFileMode() throws Exception {
        tarOut.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertTrue(tarOut.getLongFileMode() == TarArchiveOutputStream.LONGFILE_TRUNCATE);

        tarOut.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        assertTrue(tarOut.getLongFileMode() == TarArchiveOutputStream.LONGFILE_GNU);
    }

    @Test
    public void testFinish() throws Exception {
        tarOut.finish();

        byte[] expectedEOF = new byte[TarConstants.TAR_BLOCKSIZE];
        Arrays.fill(expectedEOF, (byte) 0);

        byte[] actualEOF = new byte[TarConstants.TAR_BLOCKSIZE * 2];
        tarOut.read(actualEF, 0, actualEF.length);

        assertArrayEquals(expectedEOF, actualEF);
    }

    @Test
    public void testClose() throws Exception {
        tarOut.close();

        assertFalse(tarOut.isOpen());
    }

    @Test
    public void testGetRecordSize() throws Exception {
        assertEquals(TarConstants.TAR_RECORDSIZE, tarOut.getRecordSize());
    }

    @Test
    public void testPutArchiveEntry() throws Exception {
        String entryName = "testFile.txt";
        long entrySize = 10;

        TarArchiveEntry entry = new TarArchiveEntry(entryName, entrySize);
        tarOut.putArchiveEntry(entry);

        // check that the entry header was written correctly
        tarOut.read(recordBuf, 0, TarConstants.TAR_BLOCKSIZE);

        assertEquals(TarConstants.TAR_MAGIC, recordBuf[0]);
        assertEquals(TarConstants.TAR_VERSION, recordBuf[1]);
        assertEquals(TarConstants.TAR_MODE_FILE, recordBuf[2]);
        assertEquals(entrySize, readLong(recordBuf, 32));
        assertEquals(entryName.length(), readInt(recordBuf, 40));
        assertEquals(TarConstants.NAMELEN - entryName.length(), readInt(recordBuf, 44));
        assertEquals(0, readLong(recordBuf, 48));
        assertEquals(0, readLong(recordBuf, 56));
        assertEquals(0, readLong(recordBuf, 64));
        assertEquals(TarConstants.NO_NAME, readLong(recordBuf, 72));
        assertEquals(0, readLong(recordBuf, 80));
    }

    @Test
    public void testCloseArchiveEntry() throws Exception {
        String entryName = "testFile.txt";
        long entrySize = 10;

        TarArchiveEntry entry = new TarArchiveEntry(entryName, entrySize);
        tarOut.putArchiveEntry(entry);

        byte[] data = "Hello, World!".getBytes(StandardCharsets.UTF_8);

        tarOut.write(data, 0, data.length);
        tarOut.closeArchiveEntry();

        byte[] expectedContent = new byte[data.length];
        System.arraycopy(data, 0, expectedContent, 0, data.length);

        byte[] actualContent = new byte[TarConstants.TAR_BLOCKSIZE];
        tarOut.read(actualContent, 0, TarConstants.TAR_BLOCKSIZE);

        assertArrayEquals(expectedContent, actualContent);
    }

    @Test
    public void testWrite() throws Exception {
        String entryName = "testFile.txt";
        long entrySize = 10;

        TarArchiveEntry entry = new TarArchiveEntry(entryName, entrySize);
        tarOut.putArchiveEntry(entry);

        byte[] data = "Hello, World!".getBytes(StandardCharsets.UTF_8);

        tarOut.write(data, 0, data.length);
        tarOut.closeArchiveEntry();

        byte[] expectedContent = new byte[data.length];
        System.arraycopy(data, 0, expectedContent, 0, data.length);

        byte[] actualContent = new byte[TarConstants.TAR_BLOCKSIZE];
        tarOut.read(actualContent, 0, TarConstants.TAR_BLOCKSIZE);

        assertArrayEquals(expectedContent, actualContent);
    }

    @Test
    public void testWriteLongFileName() throws Exception {
        String longFileName = "a/long/test/filename.txt";
        long entrySize = 10;

        TarArchiveOutputStream longFileTarOut = new TarArchiveOutputStream(
                new File("longFileName.tar").getOutputStream(),
                TarConstants.DEFAULT_BLKSIZE,
                TarConstants.DEFAULT_RCDSIZE);
        longFileTarOut.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);

        TarArchiveEntry longFileNameEntry = new TarArchiveEntry(longFileName, entrySize);
        longFileTarOut.putArchiveEntry(longFileNameEntry);

        byte[] data = "Hello, World!".getBytes(StandardCharsets.UTF_8);

        longFileTarOut.write(data, 0, data.length);
        longFileTarOut.closeArchiveEntry();

        longFileTarOut.finish();
        longFileTarOut.close();

        List<String> lines = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File("longFileName.tar"))) {
            while (scanner.hasNextLine()) {
                lines.add(scanner.nextLine());
            }
        }

        String expectedContent = new String(data, StandardCharsets.UTF_8);
        String actualContent = lines.get(lines.size() - 1).split(" ")[1];

        assertEquals(expectedContent, actualContent);
    }

    @Test(expected = IOException.class)
    public void testWriteLargeFile() throws Exception {
        byte[] data = new byte[Integer.MAX_VALUE];

        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(byteOut, TarConstants.DEFAULT_BLKSIZE, TarConstants.DEFAULT_RCDSIZE);
        TarArchiveEntry entry = new TarArchiveEntry("testFile.dat", data.length);
        tarOut.putArchiveEntry(entry);

        tarOut.write(data, 0, data.length);
        tarOut.closeArchiveEntry();
        tarOut.finish();
        tarOut.close();

        fail("Should throw an IOException when trying to write a large file");
    }

    @Test(expected = IOException.class)
    public void testWriteLongFileNameWithoutLongFileMode() throws Exception {
        String longFileName = "a/long/test/filename.txt";
        long entrySize = 10;

        TarArchiveOutputStream longFileTarOut = new TarArchiveOutputStream(
                new File("longFileName.tar").getOutputStream(),
                TarConstants.DEFAULT_BLKSIZE,
                TarConstants.DEFAULT_RCDSIZE);

        TarArchiveEntry longFileNameEntry = new TarArchiveEntry(longFileName, entrySize);
        longFileTarOut.putArchiveEntry(longFileNameEntry);

        byte[] data = "Hello, World!".getBytes(StandardCharsets.UTF_8);

        longFileTarOut.write(data, 0, data.length);
        longFileTarOut.closeArchiveEntry();

        longFileTarOut.finish();
        longFileTarOut.close();

        fail("Should throw an IOException when trying to write a long file name without long file mode");
    }
}