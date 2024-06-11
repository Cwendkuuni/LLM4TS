import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CpioArchiveOutputStreamTest {

    private CpioArchiveOutputStream cpioOutputStream;
    private ByteArrayOutputStream output;

    @Before
    public void setUp() {
        output = new ByteArrayOutputStream();
        cpioOutputStream = new CpioArchiveOutputStream(output);
    }

    @After
    public void tearDown() throws IOException {
        cpioOutputStream.close();
        output.close();
    }

    @Test
    public void testPutNextEntry() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(5);
        cpioOutputStream.putNextEntry(entry);
        assertEquals("testfile", entry.getName());
        assertEquals(5, entry.getFileSize());
    }

    @Test
    public void testWrite() throws IOException {
        byte[] data = "12345".getBytes();
        cpioOutputStream.write(data, 0, data.length);
        assertEquals("Data length should match", data.length, output.size());
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setFormat(CpioArchiveOutputStream.FORMAT_NEW);
        cpioOutputStream.putNextEntry(entry);
        cpioOutputStream.write("test".getBytes());
        cpioOutputStream.closeArchiveEntry();
        assertEquals("Data size should match", 16, output.size());
    }

    @Test(expected = IOException.class)
    public void testEnsureOpen() throws IOException {
        cpioOutputStream.close();
        cpioOutputStream.ensureOpen();
    }

    @Test
    public void testFinish() throws IOException {
        cpioOutputStream.finish();
        assertEquals("Trailer name should exist", "TRAILER!!!", cpioOutputStream.getCurrentEntry().getName());
    }

}