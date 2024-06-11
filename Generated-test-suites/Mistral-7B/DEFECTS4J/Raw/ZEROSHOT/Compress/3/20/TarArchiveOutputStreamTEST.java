import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TarArchiveOutputStreamTest {

    private ByteArrayOutputStream outputStream;
    private TarArchiveOutputStream tarArchiveOutputStream;

    @Before
    public void setUp() {
        outputStream = new ByteArrayOutputStream();
        tarArchiveOutputStream = new TarArchiveOutputStream(outputStream, 512, 512);
    }

    @After
    public void tearDown() throws IOException {
        if (tarArchiveOutputStream != null) {
            tarArchiveOutputStream.finish();
            tarArchiveOutputStream.close();
        }
    }

    @Test
    public void testConstructor() {
        assertNotNull(tarArchiveOutputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullOutputStream() {
        new TarArchiveOutputStream(null, 512, 512);
    }

    @Test
    public void testSetLongFileMode() {
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarArchiveOutputStream.getLongFileMode());
    }

    @Test
    public void testFinish() throws IOException {
        File testFile = new File("test.txt");
        TarArchiveEntry entry = new TarArchiveEntry(testFile, "testFile.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.write("Hello World!".getBytes(StandardCharsets.UTF_8));
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.finish();

        byte[] expectedContent = "Hello World!\0".getBytes(StandardCharsets.UTF_8);

        byte[] resultContent = outputStream.toByteArray();

        byte[] header = new byte[512];
        entry.writeHeader(header);

        assertEquals(expectedContent.length + header.length * 2, resultContent.length);
        assertEquals(expectedContent, new String(resultContent, StandardCharsets.UTF_8).substring(0, expectedContent.length));
    }

    @Test
    public void testClose() throws IOException {
        File testFile = new File("test.txt");
        TarArchiveEntry entry = new TarArchiveEntry(testFile, "testFile.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.write("Hello World!".getBytes(StandardCharsets.UTF_8));
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.close();

        byte[] expectedContent = "Hello World!\0".getBytes(StandardCharsets.UTF_8);

        byte[] resultContent = outputStream.toByteArray();

        byte[] header = new byte[512];
        entry.writeHeader(header);

        assertEquals(expectedContent.length + header.length * 2, resultContent.length);
        assertEquals(expectedContent, new String(resultContent, StandardCharsets.UTF_8).substring(0, expectedContent.length));

        assertNull(tarArchiveOutputStream);
    }

    @Test(expected = IOException.class)
    public void testWriteWithClosedStream() throws IOException {
        File testFile = new File("test.txt");
        TarArchiveEntry entry = new TarArchiveEntry(testFile, "testFile.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.write("Hello World!".getBytes(StandardCharsets.UTF_8));
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.close();

        byte[] contentToWrite = "Hello Again!".getBytes(StandardCharsets.UTF_8);

        tarArchiveOutputStream.write(contentToWrite);
    }
}