import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class CpioArchiveOutputStreamTest {
    private CpioArchiveOutputStream cpioOutput;

    @Before
    public void setup() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        cpioOutput = new CpioArchiveOutputStream(out);
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        // Create a test entry
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(5);
        entry.setMode(CpioConstants.C_ISREG);

        // Put the entry into the CPIO stream
        cpioOutput.putArchiveEntry(entry);
        cpioOutput.closeArchiveEntry();
        cpioOutput.finish();

        // Verify that the entry was written correctly
        byte[] output = cpioOutput.toByteArray();
        assertEquals("07070100000000000000000000000000000000000000000500000000" +
                "000000007465737466696C6500", toHexString(output));
    }

    @Test
    public void testWrite() throws IOException {
        // Create a test entry
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(10);
        entry.setMode(CpioConstants.C_ISREG);

        // Put the entry into the CPIO stream
        cpioOutput.putArchiveEntry(entry);
        cpioOutput.write("1234567890".getBytes());
        cpioOutput.closeArchiveEntry();
        cpioOutput.finish();

        // Verify that the entry was written correctly
        byte[] output = cpioOutput.toByteArray();
        assertEquals("07070100000000000000000000000000000000000000000A00000000" +
                "000000007465737466696C653132333435363738393000", toHexString(output));
    }

    // Helper method to convert byte array to hex string
    private static String toHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }
}