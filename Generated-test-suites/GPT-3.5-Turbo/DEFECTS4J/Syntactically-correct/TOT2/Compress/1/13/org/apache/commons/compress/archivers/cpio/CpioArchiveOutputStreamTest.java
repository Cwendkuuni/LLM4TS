import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CpioArchiveOutputStreamTest {
    
    private ByteArrayOutputStream byteOut;
    private CpioArchiveOutputStream cpioOut;

    @Before
    public void setUp() throws Exception {
        byteOut = new ByteArrayOutputStream();
        cpioOut = new CpioArchiveOutputStream(byteOut);
    }

    @After
    public void tearDown() throws Exception {
        cpioOut.close();
        byteOut.close();
    }

    @Test
    public void testPutNextEntry() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFormat(CpioConstants.FORMAT_NEW);
        entry.setSize(5);

        cpioOut.putNextEntry(entry);
        cpioOut.write("12345".getBytes());
        cpioOut.closeArchiveEntry();

        byte[] output = byteOut.toByteArray();
        
        assertTrue(output.length != 0);
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        cpioOut.putNextEntry(entry);
        cpioOut.closeArchiveEntry();

        assertTrue(cpioOut.getCurrentEntry() == null);
    }

    @Test(expected = IOException.class)
    public void testWritePastEndOfEntry() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setSize(5);
        cpioOut.putNextEntry(entry);

        cpioOut.write("123456".getBytes());
    }

    @Test
    public void testPutNextEntryWithInvalidTime() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setTime(-1);

        cpioOut.putNextEntry(entry);

        assertTrue(entry.getTime() != -1);
    }

    @Test
    public void testFinish() throws IOException {
        cpioOut.finish();

        assertTrue(cpioOut.getCurrentEntry() != null);
    }

    @Test
    public void testClose() throws IOException {
        cpioOut.close();

        assertTrue(cpioOut.isClosed());
    }

}