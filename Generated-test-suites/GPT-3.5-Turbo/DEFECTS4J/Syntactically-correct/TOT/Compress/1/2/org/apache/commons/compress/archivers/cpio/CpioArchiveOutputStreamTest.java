import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class CpioArchiveOutputStreamTest {

    private CpioArchiveOutputStream cpioOutputStream;
    private byte[] testContents;

    @Before
    public void setup() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        cpioOutputStream = new CpioArchiveOutputStream(byteArrayOutputStream);
        testContents = "12345".getBytes();
    }

    @After
    public void teardown() throws IOException {
        cpioOutputStream.close();
    }

    @Test
    public void testPutNextEntryWithValidEntry() throws IOException {
        CpioArchiveEntry testEntry = new CpioArchiveEntry();
        testEntry.setName("testfile");
        testEntry.setFileSize(testContents.length);
        cpioOutputStream.putNextEntry(testEntry);
        cpioOutputStream.write(testContents);
        cpioOutputStream.closeArchiveEntry();

        Assert.assertEquals("testfile", testEntry.getName());
        Assert.assertEquals(testContents.length, testEntry.getSize());
        Assert.assertEquals("testfile", cpioOutputStream.getCpioEntry().getName());
        Assert.assertEquals(testContents.length, cpioOutputStream.getWritten());
    }

    @Test(expected = IOException.class)
    public void testPutNextEntryWithoutValidEntry() throws IOException {
        cpioOutputStream.putNextEntry(null);
    }

    @Test(expected = IOException.class)
    public void testWriteWithoutNextEntry() throws IOException {
        cpioOutputStream.write(testContents);
    }

    @Test(expected = IOException.class)
    public void testWritePastEndOfStoredEntry() throws IOException {
        CpioArchiveEntry testEntry = new CpioArchiveEntry();
        testEntry.setName("testfile");
        testEntry.setFileSize(testContents.length);
        cpioOutputStream.putNextEntry(testEntry);

        byte[] largerContents = new byte[testContents.length + 1];
        cpioOutputStream.write(largerContents);
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        CpioArchiveEntry testEntry = new CpioArchiveEntry();
        testEntry.setName("testfile");
        testEntry.setFileSize(testContents.length);
        cpioOutputStream.putNextEntry(testEntry);
        cpioOutputStream.write(testContents);
        cpioOutputStream.closeArchiveEntry();

        Assert.assertNull(cpioOutputStream.getCpioEntry());
        Assert.assertEquals(0, cpioOutputStream.getWritten());
    }

}