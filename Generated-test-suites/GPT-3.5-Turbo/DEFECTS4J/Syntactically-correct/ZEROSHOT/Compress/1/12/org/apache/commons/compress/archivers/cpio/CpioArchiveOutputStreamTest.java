import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class CpioArchiveOutputStreamTest {
    
    private CpioArchiveOutputStream cpioOutputStream;
    private ByteArrayOutputStream byteArrayOutputStream;

    @Before
    public void setUp() {
        byteArrayOutputStream = new ByteArrayOutputStream();
        cpioOutputStream = new CpioArchiveOutputStream(byteArrayOutputStream);
    }

    @After
    public void cleanUp() throws IOException {
        cpioOutputStream.close();
        byteArrayOutputStream.close();
    }

    @Test
    public void testPutNextEntry() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(5);

        cpioOutputStream.putNextEntry(entry);

        Assert.assertEquals("ASCII string", entry.getName(), "testfile");
    }

    @Test
    public void testWrite() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(5);

        cpioOutputStream.putNextEntry(entry);
        cpioOutputStream.write("12345".getBytes());

        cpioOutputStream.closeArchiveEntry();
        cpioOutputStream.finish();

        Assert.assertEquals("Byte array", byteArrayOutputStream.toByteArray(), "12345".getBytes());
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(5);

        cpioOutputStream.putNextEntry(entry);
        cpioOutputStream.write("12345".getBytes());

        cpioOutputStream.closeArchiveEntry();

        Assert.assertNull("Cpio entry", cpioOutputStream.cpioEntry);
    }

    @Test
    public void testFinish() throws IOException {
        cpioOutputStream.finish();

        Assert.assertNotNull("Cpio entry", cpioOutputStream.cpioEntry);
    }

    @Test
    public void testClose() throws IOException {
        cpioOutputStream.close();

        Assert.assertTrue("Stream closed", cpioOutputStream.closed);
    }
}