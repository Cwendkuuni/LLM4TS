import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.io.FileOutputStream;

public class CpioArchiveOutputStreamTest {

    @Test
    public void testPutArchiveEntry() {
        try {
            CpioArchiveOutputStream out = new CpioArchiveOutputStream(new FileOutputStream(new File("test.cpio")));
            CpioArchiveEntry entry = new CpioArchiveEntry();
            entry.setName("testfile");
            String contents = "12345";
            entry.setFileSize(contents.length());
            entry.setMode(CpioConstants.C_ISREG);
            out.putArchiveEntry(entry);
            out.write(contents.getBytes());
            out.close();
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testPutArchiveEntryWithInvalidFormat() {
        try {
            CpioArchiveOutputStream out = new CpioArchiveOutputStream(new FileOutputStream(new File("test.cpio")), 5);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testPutArchiveEntryWithDuplicateName() {
        try {
            CpioArchiveOutputStream out = new CpioArchiveOutputStream(new FileOutputStream(new File("test.cpio")));
            CpioArchiveEntry entry1 = new CpioArchiveEntry();
            entry1.setName("testfile");
            CpioArchiveEntry entry2 = new CpioArchiveEntry();
            entry2.setName("testfile");
            out.putArchiveEntry(entry1);
            out.putArchiveEntry(entry2);
            fail("Expected IOException for duplicate entry name");
        } catch (IOException e) {
            // Expected exception
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testWriteToEntry() {
        try {
            CpioArchiveOutputStream out = new CpioArchiveOutputStream(new FileOutputStream(new File("test.cpio")));
            CpioArchiveEntry entry = new CpioArchiveEntry();
            entry.setName("testfile");
            String contents = "12345";
            entry.setFileSize(contents.length());
            entry.setMode(CpioConstants.C_ISREG);
            out.putArchiveEntry(entry);
            out.write(contents.getBytes());
            out.close();
            // Add assertion here based on specific requirements for the write operation
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testCloseArchiveEntryWithInvalidSize() {
        try {
            CpioArchiveOutputStream out = new CpioArchiveOutputStream(new FileOutputStream(new File("test.cpio")));
            CpioArchiveEntry entry = new CpioArchiveEntry();
            entry.setName("testfile");
            String contents = "12345";
            entry.setFileSize(contents.length() + 1); // Invalid size
            entry.setMode(CpioConstants.C_ISREG);
            out.putArchiveEntry(entry);
            out.write(contents.getBytes());
            out.closeArchiveEntry();
            fail("Expected IOException for invalid entry size");
        } catch (IOException e) {
            // Expected exception
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testFinishWithUnclosedEntries() {
        try {
            CpioArchiveOutputStream out = new CpioArchiveOutputStream(new FileOutputStream(new File("test.cpio")));
            CpioArchiveEntry entry = new CpioArchiveEntry();
            entry.setName("testfile");
            String contents = "12345";
            entry.setFileSize(contents.length());
            entry.setMode(CpioConstants.C_ISREG);
            out.putArchiveEntry(entry);
            out.finish();
            fail("Expected IOException for unclosed entries");
        } catch (IOException e) {
            // Expected exception
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    // Add more test cases for other methods in CpioArchiveOutputStream

}