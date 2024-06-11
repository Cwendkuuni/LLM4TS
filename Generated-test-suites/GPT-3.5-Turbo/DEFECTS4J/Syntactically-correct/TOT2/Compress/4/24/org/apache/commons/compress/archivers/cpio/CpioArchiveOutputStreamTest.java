import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CpioArchiveOutputStreamTest {

    private CpioArchiveOutputStream cpioArchiveOutputStream;
    private ByteArrayOutputStream outputStream;

    @Before
    public void setUp() {
        outputStream = new ByteArrayOutputStream();
        cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream);
    }

    @After
    public void tearDown() {
        try {
            cpioArchiveOutputStream.close();
        } catch (IOException e) {
            fail("Exception caught while closing the output stream.");
        }
    }

    @Test
    public void testPutArchiveEntry() {
        try {
            CpioArchiveEntry entry = new CpioArchiveEntry();
      
            entry.setName("testfile");
            entry.setFileSize(10);
            entry.setMode(CpioConstants.C_ISREG);
      
            cpioArchiveOutputStream.putArchiveEntry(entry);
      
            cpioArchiveOutputStream.closeArchiveEntry();
        } catch (IOException e) {
            fail("Exception caught during putArchiveEntry test.");
        }
    }

    @Test
    public void testWriteAndCloseArchiveEntry() {
        try {
            CpioArchiveEntry entry = new CpioArchiveEntry();
      
            entry.setName("testfile");
            entry.setFileSize(10);
            entry.setMode(CpioConstants.C_ISREG);
      
            cpioArchiveOutputStream.putArchiveEntry(entry);
            cpioArchiveOutputStream.write(new byte[]{1, 2, 3, 4, 5}, 0, 5);
            cpioArchiveOutputStream.closeArchiveEntry();
        } catch (IOException e) {
            fail("Exception caught during write and closeArchiveEntry test.");
        }
    }

    @Test
    public void testFinish() {
        try {
            cpioArchiveOutputStream.finish();
        } catch (IOException e) {
            fail("Exception caught during finish test.");
        }
    }

    @Test
    public void testClose() {
        try {
            cpioArchiveOutputStream.close();
        } catch (IOException e) {
            fail("Exception caught during close test.");
        }
    }

    @Test
    public void testCreateArchiveEntry() {
        try {
            CpioArchiveEntry entry = (CpioArchiveEntry)cpioArchiveOutputStream.createArchiveEntry(new File("testfile"), "testfile");
            assertNotNull(entry);
        } catch (IOException e) {
            fail("Exception caught during createArchiveEntry test.");
        }
    }

    @Test
    public void testInvalidFormat() {
        try {
            cpioArchiveOutputStream.putArchiveEntry(new CpioArchiveEntry());
            fail("Should have thrown IllegalArgumentException for invalid format.");
        } catch (IOException e) {
            assertEquals("Unknown format: 10", e.getMessage());
        }
    }
}