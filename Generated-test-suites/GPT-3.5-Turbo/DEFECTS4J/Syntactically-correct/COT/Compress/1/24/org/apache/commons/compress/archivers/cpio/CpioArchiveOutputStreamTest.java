import org.junit.Test;
import java.io.FileOutputStream;
import java.io.IOException;

public class CpioArchiveOutputStreamTest {

    @Test
    public void testPutNextEntry() throws IOException {
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(new FileOutputStream("test.cpio"));
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        String contents = "12345";
        entry.setFileSize(contents.length());
        out.putNextEntry(entry);
        out.write(contents.getBytes());
        out.close();

        // Add assertion here to check if the test file was created correctly, or if any exceptions were thrown during the process
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(new FileOutputStream("test.cpio"));
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        String contents = "12345";
        entry.setFileSize(contents.length());
        out.putNextEntry(entry);
        out.write(contents.getBytes());
        out.closeArchiveEntry();
        out.close();

        // Add assertion here to check if the entry was closed correctly, or if any exceptions were thrown during the process
    }

    // Continue adding test methods for other public methods in the class...

}