import org.junit.Before;
import org.junit.Test;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.IOException;
import org.apache.commons.compress.archivers.ArchiveEntry;

public class TarArchiveOutputStreamTest {
    private TarArchiveOutputStream tarOutput;

    @Before
    public void setup() throws IOException {
        OutputStream os = new FileOutputStream("test.tar");
        tarOutput = new TarArchiveOutputStream(os);
    }

    @After
    public void teardown() throws IOException {
        tarOutput.close();
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        ArchiveEntry entry = new TarArchiveEntry(new File("test.txt"), "test.txt");
        tarOutput.putArchiveEntry(entry);
        tarOutput.closeArchiveEntry();
    }

    @Test
    public void testWrite() throws IOException {
        ArchiveEntry entry = new TarArchiveEntry(new File("test.txt"), "test.txt");
        tarOutput.putArchiveEntry(entry);
        tarOutput.write("test data".getBytes());
        tarOutput.closeArchiveEntry();
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        ArchiveEntry entry = new TarArchiveEntry(new File("test.txt"), "test.txt");
        tarOutput.putArchiveEntry(entry);
        tarOutput.closeArchiveEntry();
    }
}