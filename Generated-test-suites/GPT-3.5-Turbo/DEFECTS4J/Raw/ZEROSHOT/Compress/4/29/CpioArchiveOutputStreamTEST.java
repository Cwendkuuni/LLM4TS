import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CpioArchiveOutputStreamTest {
    private CpioArchiveOutputStream cpioArchiveOutputStream;
    private ByteArrayOutputStream outputStream;

    @Before
    public void setUp() throws Exception {
        outputStream = new ByteArrayOutputStream();
        cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream);
    }

    @After
    public void tearDown() throws Exception {
        cpioArchiveOutputStream.close();
        outputStream.close();
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("Test File");
        entry.setFormat(1);
        entry.setMode(493);
        entry.setTime(1528948800000L);
        entry.setSize(10);

        cpioArchiveOutputStream.putArchiveEntry(entry);
        cpioArchiveOutputStream.closeArchiveEntry();

        byte[] expectedBytes = { 'i', '-', 'n', 'e', 'w', '-', 'h', 'd', 'r', '\0', '\0', '\0', '\0', '\0', '\0', '\0', '\0', '\0', '\0' };
        byte[] actualBytes = outputStream.toByteArray();

        for (int i = 0; i < expectedBytes.length; i++) {
            assertEquals(expectedBytes[i], actualBytes[i]);
        }
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("Test File");
        entry.setFormat(1);
        entry.setMode(493);
        entry.setTime(1528948800000L);
        entry.setSize(10);

        cpioArchiveOutputStream.putArchiveEntry(entry);
        cpioArchiveOutputStream.closeArchiveEntry();

        assertTrue(cpioArchiveOutputStream.isClosed());
    }

    @Test
    public void testWrite() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("Test File");
        entry.setFormat(1);
        entry.setMode(493);
        entry.setTime(1528948800000L);
        entry.setSize(10);

        cpioArchiveOutputStream.putArchiveEntry(entry);
        cpioArchiveOutputStream.write("Test Data".getBytes());
        cpioArchiveOutputStream.closeArchiveEntry();

        byte[] expectedBytes = { 'i', '-', 'n', 'e', 'w', '-', 'h', 'd', 'r', '\0', '\0', '\0', '\0', '\0', '\0', '\0', '\0', '\0',
                'T', 'e', 's', 't', ' ', 'D', 'a', 't', 'a' };
        byte[] actualBytes = outputStream.toByteArray();

        for (int i = 0; i < expectedBytes.length; i++) {
            assertEquals(expectedBytes[i], actualBytes[i]);
        }
    }

    @Test
    public void testFinish() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("Test File");
        entry.setFormat(1);
        entry.setMode(493);
        entry.setTime(1528948800000L);
        entry.setSize(10);

        cpioArchiveOutputStream.putArchiveEntry(entry);
        cpioArchiveOutputStream.finish();

        byte[] expectedBytes = { 'i', '-', 'n', 'e', 'w', '-', 'h', 'd', 'r', '\0', '\0', '\0', '\0', '\0', '\0', '\0', '\0', '\0',
                'T', 'e', 's', 't', ' ', 'F', 'i', 'l', 'e', '\0', '\0', '\0', '\0', '\0', '\0', '\0', '\0', '\0' };
        byte[] actualBytes = outputStream.toByteArray();

        for (int i = 0; i < expectedBytes.length; i++) {
            assertEquals(expectedBytes[i], actualBytes[i]);
        }
    }

    @Test
    public void testClose() throws IOException {
        cpioArchiveOutputStream.close();
        assertTrue(cpioArchiveOutputStream.isClosed());
    }

}