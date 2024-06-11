import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CpioArchiveOutputStreamTest {

    private CpioArchiveOutputStream cpioOutputStream;

    @Before
    public void setup() {
        // Initialize the CpioArchiveOutputStream
        cpioOutputStream = new CpioArchiveOutputStream(null);
    }

    @Test
    public void testPutNextEntry() {
        // Test the putNextEntry method
        CpioArchiveEntry entry = new CpioArchiveEntry();
        cpioOutputStream.putNextEntry(entry);
        assertEquals(entry, cpioOutputStream.getCurrentEntry());
    }

    @Test(expected = IOException.class)
    public void testPutNextEntryWithClosedStream() throws IOException {
        // Test putNextEntry method with a closed stream
        cpioOutputStream.close();
        CpioArchiveEntry entry = new CpioArchiveEntry();
        cpioOutputStream.putNextEntry(entry);
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        // Test the closeArchiveEntry method
        CpioArchiveEntry entry = new CpioArchiveEntry();
        cpioOutputStream.putNextEntry(entry);
        cpioOutputStream.closeArchiveEntry();
        assertNull(cpioOutputStream.getCurrentEntry());
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntryWithInvalidSize() throws IOException {
        // Test closeArchiveEntry method with invalid size
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setSize(10);
        cpioOutputStream.putNextEntry(entry);
        cpioOutputStream.write(new byte[5]);
        cpioOutputStream.closeArchiveEntry();
    }

    @Test
    public void testWrite() throws IOException {
        // Test the write method
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setSize(5);
        cpioOutputStream.putNextEntry(entry);
        cpioOutputStream.write("12345".getBytes());
        cpioOutputStream.closeArchiveEntry();
    }

    @Test(expected = IOException.class)
    public void testWritePastEndOfEntry() throws IOException {
        // Test write method with data exceeding the entry size
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setSize(5);
        cpioOutputStream.putNextEntry(entry);
        cpioOutputStream.write("123456".getBytes());
        cpioOutputStream.closeArchiveEntry();
    }

    @Test(expected = IOException.class)
    public void testWriteWithNoCurrentEntry() throws IOException {
        // Test write method with no current entry
        cpioOutputStream.write("12345".getBytes());
    }

    @Test
    public void testFinish() throws IOException {
        // Test the finish method
        CpioArchiveEntry entry = new CpioArchiveEntry();
        cpioOutputStream.putNextEntry(entry);
        cpioOutputStream.finish();
    }

    @Test
    public void testClose() throws IOException {
        // Test the close method
        cpioOutputStream.close();
    }

}