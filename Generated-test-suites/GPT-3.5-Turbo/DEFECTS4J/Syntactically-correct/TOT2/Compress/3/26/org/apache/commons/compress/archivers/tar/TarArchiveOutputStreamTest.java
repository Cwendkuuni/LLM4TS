import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class TarArchiveOutputStreamTest {

    @Test
    public void testPutArchiveEntry() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);

        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("testFile.txt");
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        tarArchiveOutputStream.closeArchiveEntry();

        tarArchiveOutputStream.close();

        // Check if output stream contains the expected data
        byte[] expectedBytes = outputStream.toByteArray();
        // perform assertions
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);

        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("testFile.txt");
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        tarArchiveOutputStream.write("test data".getBytes());
        tarArchiveOutputStream.closeArchiveEntry();

        tarArchiveOutputStream.close();

        // Check if output stream contains the expected data
        byte[] expectedBytes = outputStream.toByteArray();
        // perform assertions
    }

    @Test
    public void testWrite() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);

        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("testFile.txt");
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        tarArchiveOutputStream.write("test data".getBytes());
        tarArchiveOutputStream.closeArchiveEntry();

        tarArchiveOutputStream.close();

        // Check if output stream contains the expected data
        byte[] expectedBytes = outputStream.toByteArray();
        // perform assertions
    }

    // Include additional test methods for other public methods in TarArchiveOutputStream class

}