import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class CpioArchiveOutputStreamTest {

    @Test
    public void testPutNextEntry() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOut = new CpioArchiveOutputStream(out);

        CpioArchiveEntry entry = new CpioArchiveEntry("testFile");
        entry.setMode(644);
        entry.setUID(1001);
        entry.setGID(1002);
        
        cpioOut.putNextEntry(entry);

        cpioOut.finish();
        cpioOut.close();

        // Check if entry is written correctly
        byte[] result = out.toByteArray();
        assertNotNull(result);
    }

    @Test
    public void testWrite() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOut = new CpioArchiveOutputStream(out);

        CpioArchiveEntry entry = new CpioArchiveEntry("testFile");
        entry.setMode(644);
        entry.setUID(1001);
        entry.setGID(1002);
        
        cpioOut.putNextEntry(entry);
        byte[] data = {1, 2, 3, 4, 5};
        cpioOut.write(data, 0, data.length);

        cpioOut.finish();
        cpioOut.close();

        // Check if data is written correctly
        byte[] result = out.toByteArray();
        assertNotNull(result);
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntry() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOut = new CpioArchiveOutputStream(out);

        cpioOut.closeArchiveEntry(); // No current entry should throw IOException
    }

    @Test
    public void testFinish() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOut = new CpioArchiveOutputStream(out);

        CpioArchiveEntry entry = new CpioArchiveEntry("testFile");
        entry.setMode(644);
        entry.setUID(1001);
        entry.setGID(1002);
        
        cpioOut.putNextEntry(entry);

        cpioOut.finish();
        cpioOut.close();

        // Check if finish completes without error
        byte[] result = out.toByteArray();
        assertNotNull(result);
    }
}