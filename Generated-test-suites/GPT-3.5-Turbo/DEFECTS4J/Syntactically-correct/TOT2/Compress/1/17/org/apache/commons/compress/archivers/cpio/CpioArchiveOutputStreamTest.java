import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.commons.compress.archivers.cpio.CpioArchiveEntry;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CpioArchiveOutputStreamTest {

    private ByteArrayOutputStream outStream;
    private CpioArchiveOutputStream cpioOutputStream;

    @Before
    public void setUp() {
        outStream = new ByteArrayOutputStream();
        cpioOutputStream = new CpioArchiveOutputStream(outStream);
    }

    @After
    public void tearDown() throws IOException {
        cpioOutputStream.close();
    }

    @Test
    public void testPutNextEntry() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(5);
        
        cpioOutputStream.putNextEntry(entry);
        
        assertEquals(entry, cpioOutputStream.getCurrentEntry());
    }

    @Test
    public void testWrite() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(5);
        
        cpioOutputStream.putNextEntry(entry);
        cpioOutputStream.write("12345".getBytes());
        cpioOutputStream.closeArchiveEntry();
        
        byte[] expected = {49, 50, 51, 52, 53}; // ASCII values for "12345"
        assertArrayEquals(expected, outStream.toByteArray());
    }

    @Test
    public void testFinish() throws IOException {
        cpioOutputStream.finish();

        assertNotNull(cpioOutputStream.getCurrentEntry());
        assertEquals("TRAILER!!!", cpioOutputStream.getCurrentEntry().getName());
    }

}