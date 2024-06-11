
import static org.junit.Assert.*;
import org.junit.Test;

public class CpioArchiveOutputStreamTest {

    @Test
    public void testPutNextEntry() {
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(new ByteArrayOutputStream());
        CpioArchiveEntry cpioEntry = new CpioArchiveEntry();
        cpioEntry.setName("testfile");
        String contents = "12345";
        cpioEntry.setFileSize(contents.length());

        try {
            cpioOutputStream.putNextEntry(cpioEntry);
            cpioOutputStream.write(contents.getBytes());
            cpioOutputStream.closeArchiveEntry();
            cpioOutputStream.finish();
            cpioOutputStream.close();
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test(expected = IOException.class)
    public void testInvalidEntrySize() throws IOException {
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(new ByteArrayOutputStream());
        CpioArchiveEntry cpioEntry = new CpioArchiveEntry();
        cpioEntry.setName("testfile");
        cpioEntry.setFileSize(5);

        cpioOutputStream.putNextEntry(cpioEntry);
        cpioOutputStream.write("123456".getBytes());
        cpioOutputStream.closeArchiveEntry();
        cpioOutputStream.finish();
        cpioOutputStream.close();
    }

    @Test(expected = IOException.class)
    public void testNoCurrentEntry() throws IOException {
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(new ByteArrayOutputStream());
        cpioOutputStream.write("123456".getBytes());
        cpioOutputStream.closeArchiveEntry();
    }

    @Test(expected = IOException.class)
    public void testWritePastEndOfStoredEntry() throws IOException {
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(new ByteArrayOutputStream());
        CpioArchiveEntry cpioEntry = new CpioArchiveEntry();
        cpioEntry.setName("testfile");
        cpioEntry.setFileSize(5);

        cpioOutputStream.putNextEntry(cpioEntry);
        cpioOutputStream.write("123456".getBytes());
        cpioOutputStream.closeArchiveEntry();
    }

    @Test
    public void testFinish() {
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(new ByteArrayOutputStream());
        CpioArchiveEntry cpioEntry = new CpioArchiveEntry();
        cpioEntry.setName("testfile");
        String contents = "12345";
        cpioEntry.setFileSize(contents.length());

        try {
            cpioOutputStream.putNextEntry(cpioEntry);
            cpioOutputStream.write(contents.getBytes());
            cpioOutputStream.closeArchiveEntry();
            cpioOutputStream.finish();
            cpioOutputStream.close();
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
}

