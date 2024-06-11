
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class TarArchiveOutputStreamTest {

    @Test
    public void testPutArchiveEntry() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);

        ArchiveEntry entry = tarArchiveOutputStream.createArchiveEntry(new File("test.txt"), "test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);

        tarArchiveOutputStream.closeArchiveEntry();

        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.close();

        byte[] tarFile = outputStream.toByteArray();
        // Add assertions to check the bytes of the tar file here
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);

        ArchiveEntry entry = tarArchiveOutputStream.createArchiveEntry(new File("test.txt"), "test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);

        tarArchiveOutputStream.write("This is a test".getBytes());
        tarArchiveOutputStream.closeArchiveEntry();

        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.close();

        byte[] tarFile = outputStream.toByteArray();
        // Add assertions to check the bytes of the tar file here
    }

    @Test
    public void testFinish() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);

        ArchiveEntry entry = tarArchiveOutputStream.createArchiveEntry(new File("test.txt"), "test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);

        tarArchiveOutputStream.closeArchiveEntry();

        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.close();

        byte[] tarFile = outputStream.toByteArray();
        // Add assertions to check the bytes of the tar file here
    }

    @Test
    public void testClose() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);

        ArchiveEntry entry = tarArchiveOutputStream.createArchiveEntry(new File("test.txt"), "test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);

        tarArchiveOutputStream.write("This is a test".getBytes());
        tarArchiveOutputStream.closeArchiveEntry();

        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.close();

        byte[] tarFile = outputStream.toByteArray();
        // Add assertions to check the bytes of the tar file here
    }
}
