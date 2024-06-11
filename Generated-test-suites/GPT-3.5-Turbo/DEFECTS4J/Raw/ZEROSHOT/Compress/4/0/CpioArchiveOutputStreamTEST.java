import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static org.junit.Assert.assertEquals;

public class CpioArchiveOutputStreamTest {

    private CpioArchiveOutputStream cpioOutputStream;
    private OutputStream outputStream;

    @Before
    public void setup() {
        outputStream = new ByteArrayOutputStream();
        cpioOutputStream = new CpioArchiveOutputStream(outputStream);
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        // create a CpioArchiveEntry
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testFile");
        entry.setMode(CpioConstants.C_ISREG);
        entry.setFileSize(5);

        // put the entry in the CpioArchiveOutputStream
        cpioOutputStream.putArchiveEntry(entry);

        // check if the entry is written correctly
        assertEquals(7, outputStream.toByteArray().length); // check if the correct number of bytes is written
        assertEquals(040755, outputStream.toByteArray()[0]); // check the first byte of the entry
        assertEquals(0, outputStream.toByteArray()[6]); // check the last byte of the entry

        // close the CpioArchiveEntry
        cpioOutputStream.closeArchiveEntry();

        // check if the entry is closed correctly
        assertEquals(0, outputStream.toByteArray()[7]); // check if the next entry indicator is written correctly
    }

    @Test
    public void testWrite() throws IOException {
        // create a CpioArchiveEntry
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testFile");
        entry.setMode(CpioConstants.C_ISREG);
        entry.setFileSize(5);

        // put the entry in the CpioArchiveOutputStream
        cpioOutputStream.putArchiveEntry(entry);

        // write some data to the entry
        byte[] data = "12345".getBytes();
        cpioOutputStream.write(data, 0, data.length);

        // close the CpioArchiveEntry
        cpioOutputStream.closeArchiveEntry();

        // check if the data is written correctly
        assertEquals(12, outputStream.toByteArray().length); // check if the correct number of bytes is written
        assertEquals(49, outputStream.toByteArray()[7]); // check the first byte of the data
        assertEquals(53, outputStream.toByteArray()[11]); // check the last byte of the data
    }

    @Test
    public void testFinish() throws IOException {
        // create a CpioArchiveEntry
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testFile");
        entry.setMode(CpioConstants.C_ISREG);
        entry.setFileSize(5);

        // put the entry in the CpioArchiveOutputStream
        cpioOutputStream.putArchiveEntry(entry);
        cpioOutputStream.write("12345".getBytes());
        cpioOutputStream.closeArchiveEntry();

        // finish the CpioArchiveOutputStream
        cpioOutputStream.finish();

        // check if the trailer entry is written correctly
        assertEquals(105, outputStream.toByteArray()[12]); // check the first byte of the trailer entry
        assertEquals(0, outputStream.toByteArray()[18]); // check the last byte of the trailer entry
    }

    @Test
    public void testClose() throws IOException {
        // create a CpioArchiveEntry
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testFile");
        entry.setMode(CpioConstants.C_ISREG);
        entry.setFileSize(5);

        // put the entry in the CpioArchiveOutputStream
        cpioOutputStream.putArchiveEntry(entry);
        cpioOutputStream.write("12345".getBytes());
        cpioOutputStream.closeArchiveEntry();

        // close the CpioArchiveOutputStream
        cpioOutputStream.close();

        // check if the stream is closed correctly
        assertEquals(-1, outputStream.write(49)); // try to write to the closed stream
    }
}