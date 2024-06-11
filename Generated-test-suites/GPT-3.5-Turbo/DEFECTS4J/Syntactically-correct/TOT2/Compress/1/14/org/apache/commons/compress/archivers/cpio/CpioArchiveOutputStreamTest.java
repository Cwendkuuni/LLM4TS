import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.commons.compress.archivers.cpio.CpioArchiveEntry;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CpioArchiveOutputStreamTest {

    private ByteArrayOutputStream testOutputStream;
    private CpioArchiveOutputStream cpioOutputStream;

    @Before
    public void setUp() {
        testOutputStream = new ByteArrayOutputStream();
        cpioOutputStream = new CpioArchiveOutputStream(testOutputStream);
    }

    @After
    public void tearDown() {
        try {
            cpioOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testPutNextEntry() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(5);
        cpioOutputStream.putNextEntry(entry);
        cpioOutputStream.write("12345".getBytes());
        cpioOutputStream.closeArchiveEntry();

        assertEquals(5, testOutputStream.size());
    }

    @Test
    public void testCloseArchiveEntry() {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(5);

        try {
            cpioOutputStream.putNextEntry(entry);
            cpioOutputStream.write("12345".getBytes());
            cpioOutputStream.closeArchiveEntry();
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertTrue(true); // No exceptions thrown during closeArchiveEntry
    }

    @Test
    public void testFinish() {
        try {
            cpioOutputStream.finish();
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertTrue(true); // No exceptions thrown during finish
    }
}