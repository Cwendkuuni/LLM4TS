import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DumpArchiveInputStreamTest {

    @Test
    public void testConstructorWithDefaultEncoding() throws IOException {
        ByteArrayInputStream is = new ByteArrayInputStream(new byte[0]);
        DumpArchiveInputStream inputStream = new DumpArchiveInputStream(is);

        assertNotNull(inputStream.getSummary());
    }

    @Test
    public void testConstructorWithCustomEncoding() throws IOException {
        ByteArrayInputStream is = new ByteArrayInputStream(new byte[0]);
        DumpArchiveInputStream inputStream = new DumpArchiveInputStream(is, "UTF-8");

        assertNotNull(inputStream.getSummary());
    }

    @Test
    public void testGetNextDumpEntry() throws IOException {
        ByteArrayInputStream is = new ByteArrayInputStream(new byte[0]);
        DumpArchiveInputStream inputStream = new DumpArchiveInputStream(is);

        assertNotNull(inputStream.getNextDumpEntry());
    }

    @Test
    public void testRead() throws IOException {
        ByteArrayInputStream is = new ByteArrayInputStream(new byte[0]);
        DumpArchiveInputStream inputStream = new DumpArchiveInputStream(is);

        byte[] buffer = new byte[10];
        int bytesRead = inputStream.read(buffer, 0, 10);

        assertEquals(-1, bytesRead);
    }

    @Test(expected = IllegalStateException.class)
    public void testReadWithoutCurrentEntry() throws IOException {
        ByteArrayInputStream is = new ByteArrayInputStream(new byte[0]);
        DumpArchiveInputStream inputStream = new DumpArchiveInputStream(is);

        inputStream.read(new byte[10], 0, 10);
    }

}