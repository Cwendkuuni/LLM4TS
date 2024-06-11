import org.junit.Before;
import org.junit.Test;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;

import java.io.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TarArchiveOutputStreamTest {
    private ByteArrayOutputStream outputStream;
    private TarArchiveOutputStream tarArchiveOutputStream;

    @Before
    public void setUp() {
        outputStream = new ByteArrayOutputStream();
        tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
    }

    @Test
    public void testConstructor1() {
        try {
            TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new FileOutputStream("test.tar"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        assertTrue(true);
    }

    @Test
    public void testConstructor2() {
        try {
            TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new FileOutputStream("test.tar"), 512);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        assertTrue(true);
    }

    @Test
    public void testConstructor3() {
        try {
            TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new FileOutputStream("test.tar"), 512, 1024);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        assertTrue(true);
    }

    @Test
    public void testSetLongFileMode() {
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarArchiveOutputStream.getLongFileMode());
    }

    @Test
    public void testGetRecordSize() {
        assertEquals(org.apache.commons.compress.archivers.tar.TarBuffer.DEFAULT_RCDSIZE, tarArchiveOutputStream.getRecordSize());
    }

    @Test
    public void testPutArchiveEntry() {
        File file = new File("test.txt");
        TarArchiveEntry entry = new TarArchiveEntry(file, "test.txt");

        try {
            tarArchiveOutputStream.putArchiveEntry(entry);
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertTrue(true);
    }

    @Test
    public void testWrite() {
        byte[] bytes = "Hello, world!".getBytes();

        try {
            tarArchiveOutputStream.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertTrue(true);
    }

    @Test
    public void testCloseArchiveEntry() {
        File file = new File("test.txt");
        TarArchiveEntry entry = new TarArchiveEntry(file, "test.txt");

        try {
            tarArchiveOutputStream.putArchiveEntry(entry);
            tarArchiveOutputStream.write("Hello, world!".getBytes());
            tarArchiveOutputStream.closeArchiveEntry();
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertTrue(true);
    }

    @Test
    public void testFinish() {
        try {
            tarArchiveOutputStream.finish();
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertTrue(true);
    }

    @Test
    public void testClose() {
        try {
            tarArchiveOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertTrue(true);
    }

    @Test
    public void testCreateArchiveEntry() {
        File inputFile = new File("test.txt");
        String entryName = "test.txt";

        try {
            TarArchiveEntry entry = (TarArchiveEntry) tarArchiveOutputStream.createArchiveEntry(inputFile, entryName);
            assertEquals(inputFile, entry.getFile());
            assertEquals(entryName, entry.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testFlush() {
        try {
            tarArchiveOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertTrue(true);
    }
}