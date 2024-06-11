import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class TarArchiveOutputStreamTest {
    
    private TarArchiveOutputStream tarOutputStream;
    private ByteArrayOutputStream outputStream;

    @Before
    public void setup() {
        outputStream = new ByteArrayOutputStream();
        tarOutputStream = new TarArchiveOutputStream(outputStream);
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        // Test putting a basic archive entry
        TarArchiveEntry entry = new TarArchiveEntry("testfile.txt");
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.closeArchiveEntry();
        assertEquals(512, outputStream.toByteArray().length);
    }

    @Test
    public void testSetLongFileMode() {
        // Test setting long file mode to GNU
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        assertEquals(TarArchiveOutputStream.LONGFILE_GNU, tarOutputStream.longFileMode);
    }

    @Test(expected = IOException.class)
    public void testFinishThrowsExceptionIfFinishedTwice() throws IOException {
        tarOutputStream.finish();
        tarOutputStream.finish();
    }

    @Test(expected = IOException.class)
    public void testCloseThrowsExceptionIfFinished() throws IOException {
        tarOutputStream.finish();
        tarOutputStream.close();
    }

    // Add more test cases to cover edge cases and error scenarios

}