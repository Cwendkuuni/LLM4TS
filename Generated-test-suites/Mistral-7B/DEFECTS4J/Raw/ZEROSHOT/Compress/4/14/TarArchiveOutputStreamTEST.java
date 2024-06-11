import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.archivers.tar.TarOutputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TarArchiveOutputStreamTest {

    private File outputFile;
    private TarOutputStream tarOutputStream;

    @Before
    public void setUp() throws Exception {
        outputFile = new File("target/test-output.tar");
        tarOutputStream = new TarArchiveOutputStream(new FileOutputStream(outputFile));
    }

    @After
    public void tearDown() throws Exception {
        if (tarOutputStream != null) {
            tarOutputStream.close();
        }
        if (outputFile.exists()) {
            outputFile.delete();
        }
    }

    @Test
    public void testConstructor() {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        assertNull(tarArchiveOutputStream.getRecordSize());

        TarArchiveOutputStream tarArchiveOutputStream1 = new TarArchiveOutputStream(new ByteArrayOutputStream(), 1024);
        assertEquals(1024, tarArchiveOutputStream1.getRecordSize());

        TarArchiveOutputStream tarArchiveOutputStream2 = new TarArchiveOutputStream(new ByteArrayOutputStream(), 1024, 512);
        assertEquals(512, tarArchiveOutputStream2.getRecordSize());
    }

    @Test
    public void testSetLongFileMode() {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());

        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarArchiveOutputStream.getLongFileMode());

        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        assertEquals(TarArchiveOutputStream.LONGFILE_GNU, tarArchiveOutputStream.getLongFileMode());

        tarArchiveOutputStream.setLongFileMode(-1);
        assertEquals(TarArchiveOutputStream.LONGFILE_ERROR, tarArchiveOutputStream.getLongFileMode());
    }

    @Test
    public void testFinish() throws Exception {
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test.txt");
        tarOutputStream.putNextEntry(tarArchiveEntry);
        tarOutputStream.write("Hello World!".getBytes());
        tarOutputStream.finish();

        // Check that the output file is valid
        TarArchiveInputStream tarArchiveInputStream = new TarArchiveInputStream(new FileInputStream(outputFile));
        TarArchiveEntry entry = tarArchiveInputStream.getNextEntry();
        assertNotNull(entry);
        assertEquals("test.txt", entry.getName());
        assertEquals("test.txt\0", entry.getNameInFile());
        assertTrue(entry.isDirectory());
        assertFalse(entry.isRandomAccessFile());
        assertEquals(12, entry.getSize());

        byte[] bytes = new byte[12];
        tarArchiveInputStream.read(bytes, 0, bytes.length);
        assertEquals("Hello World!\0".getBytes(), bytes);
        tarArchiveInputStream.close();

        // Check that the output file is empty
        assertFalse(tarOutputStream.hasNextEntry());
        tarOutputStream.close();
    }

    @Test(expected = IOException.class)
    public void testFinishWithOpenEntry() throws Exception {
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test.txt");
        tarOutputStream.putNextEntry(tarArchiveEntry);
        tarOutputStream.finish();
        tarOutputStream.write("Hello World!".getBytes());
    }

    @Test
    public void testClose() throws Exception {
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test.txt");
        tarOutputStream.putNextEntry(tarArchiveEntry);
        tarOutputStream.write("Hello World!".getBytes());
        tarOutputStream.close();

        // Check that the output file is valid
        TarArchiveInputStream tarArchiveInputStream = new TarArchiveInputStream(new FileInputStream(outputFile));
        TarArchiveEntry entry = tarArchiveInputStream.getNextEntry();
        assertNotNull(entry);
        assertEquals("test.txt", entry.getName());
        assertEquals("test.txt\0", entry.getNameInFile());
        assertTrue(entry.isDirectory());
        assertFalse(entry.isRandomAccessFile());
        assertEquals(12, entry.getSize());

        byte[] bytes = new byte[12];
        tarArchiveInputStream.read(bytes, 0, bytes.length);
        assertEquals("Hello World!\0".getBytes(), bytes);
        tarArchiveInputStream.close();

        // Check that the output file is empty
        assertFalse(tarOutputStream.hasNextEntry());
    }

    @Test(expected = IOException.class)
    public void testCloseWithOpenEntry() throws Exception {
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test.txt");
        tarOutputStream.putNextEntry(tarArchiveEntry);
        tarOutputStream.close();
        tarOutputStream.write("Hello World!".getBytes());
    }

    @Test
    public void testPutArchiveEntry() throws Exception {
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(tarArchiveEntry);
        tarOutputStream.write("Hello World!".getBytes());
        tarOutputStream.close();

        // Check that the output file is valid
        TarArchiveInputStream tarArchiveInputStream = new TarArchiveInputStream(new FileInputStream(outputFile));
        TarArchiveEntry entry = tarArchiveInputStream.getNextEntry();
        assertNotNull(entry);
        assertEquals("test.txt", entry.getName());
        assertEquals("test.txt\0", entry.getNameInFile());
        assertFalse(entry.isDirectory());
        assertFalse(entry.isRandomAccessFile());
        assertEquals(12, entry.getSize());

        byte[] bytes = new byte[12];
        tarArchiveInputStream.read(bytes, 0, bytes.length);
        assertEquals("Hello World!\0".getBytes(), bytes);
        tarArchiveInputStream.close();
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithOpenEntry() throws Exception {
        TarArchiveEntry tarArchiveEntry1 = new TarArchiveEntry("test1.txt");
        tarOutputStream.putNextEntry(tarArchiveEntry1);
        tarOutputStream.putArchiveEntry(tarArchiveEntry1);
    }

    @Test
    public void testCloseArchiveEntry() throws Exception {
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test.txt");
        tarOutputStream.putNextEntry(tarArchiveEntry);
        tarOutputStream.write("Hello World!".getBytes());
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.write("Goodbye World!".getBytes());
        tarOutputStream.finish();
        tarOutputStream.close();

        // Check that the output file is valid
        TarArchiveInputStream tarArchiveInputStream = new TarArchiveInputStream(new FileInputStream(outputFile));
        TarArchiveEntry entry1 = tarArchiveInputStream.getNextEntry();
        TarArchiveEntry entry2 = tarArchiveInputStream.getNextEntry();
        assertNotNull(entry1);
        assertNull(entry2);

        assertEquals("test.txt", entry1.getName());
        assertEquals("test.txt\0", entry1.getNameInFile());
        assertFalse(entry1.isDirectory());
        assertFalse(entry1.isRandomAccessFile());
        assertEquals(12, entry1.getSize());

        byte[] bytes = new byte[12];
        tarArchiveInputStream.read(bytes, 0, bytes.length);
        assertEquals("Hello World!\0".getBytes(), bytes);

        // Check that the second entry is empty
        assertFalse(entry2.isDirectory());
        assertFalse(entry2.isRandomAccessFile());
        assertEquals(0, entry2.getSize());

        tarArchiveInputStream.close();
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntryWithOpenEntry() throws Exception {
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test.txt");
        tarOutputStream.putNextEntry(tarArchiveEntry);
        tarOutputStream.write("Hello World!".getBytes());
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.write("Goodbye World!".getBytes());
    }

    @Test
    public void testWrite() throws Exception {
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test.txt");
        tarOutputStream.putNextEntry(tarArchiveEntry);
        tarOutputStream.write("Hello World!".getBytes());
        tarOutputStream.close();

        // Check that the output file is valid
        TarArchiveInputStream tarArchiveInputStream = new TarArchiveInputStream(new FileInputStream(outputFile));
        TarArchiveEntry entry = tarArchiveInputStream.getNextEntry();
        assertNotNull(entry);
        assertEquals("test.txt", entry.getName());
        assertEquals("test.txt\0", entry.getNameInFile());
        assertFalse(entry.isDirectory());
        assertFalse(entry.isRandomAccessFile());
        assertEquals(12, entry.getSize());

        byte[] bytes = new byte[12];
        tarArchiveInputStream.read(bytes, 0, bytes.length);
        assertEquals("Hello World!\0".getBytes(), bytes);
        tarArchiveInputStream.close();
    }

    @Test(expected = IOException.class)
    public void testWriteWithOpenEntry() throws Exception {
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test.txt");
        tarOutputStream.putNextEntry(tarArchiveEntry);
        tarOutputStream.write("Hello World!".getBytes());
        tarOutputStream.write("Goodbye World!".getBytes());
    }
}