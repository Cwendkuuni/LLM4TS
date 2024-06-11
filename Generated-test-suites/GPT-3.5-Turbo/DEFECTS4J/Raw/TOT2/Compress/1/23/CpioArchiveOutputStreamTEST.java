import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CpioArchiveOutputStreamTest {

    private CpioArchiveOutputStream cpioOutputStream;
    private ByteArrayOutputStream byteArrayOutputStream;

    @Before
    public void setUp() {
        byteArrayOutputStream = new ByteArrayOutputStream();
        cpioOutputStream = new CpioArchiveOutputStream(byteArrayOutputStream);
    }

    @After
    public void tearDown() throws IOException {
        cpioOutputStream.close();
        byteArrayOutputStream.close();
    }

    @Test
    public void testPutNextEntry() throws IOException {
        CpioArchiveEntry entry1 = new CpioArchiveEntry();
        entry1.setName("testfile1");
        entry1.setFormat(CpioConstants.FORMAT_NEW);

        cpioOutputStream.putNextEntry(entry1);
        cpioOutputStream.closeArchiveEntry();

        assertNotNull(cpioOutputStream);

        CpioArchiveEntry entry2 = new CpioArchiveEntry();
        entry2.setName("testfile2");
        entry2.setFormat(CpioConstants.FORMAT_OLD_ASCII);

        cpioOutputStream.putNextEntry(entry2);
        cpioOutputStream.closeArchiveEntry();

        assertNotNull(cpioOutputStream);
    }

    @Test
    public void testWrite() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFormat(CpioConstants.FORMAT_NEW);
        entry.setSize(5);

        cpioOutputStream.putNextEntry(entry);
        cpioOutputStream.write("12345".getBytes());
        cpioOutputStream.closeArchiveEntry();

        assertNotNull(cpioOutputStream);
    }

    @Test
    public void testFinish() throws IOException {
        cpioOutputStream.finish();
        assertNotNull(cpioOutputStream);
    }

    @Test
    public void testClose() throws IOException {
        cpioOutputStream.close();
        assertTrue(cpioOutputStream.isClosed());
    }
}