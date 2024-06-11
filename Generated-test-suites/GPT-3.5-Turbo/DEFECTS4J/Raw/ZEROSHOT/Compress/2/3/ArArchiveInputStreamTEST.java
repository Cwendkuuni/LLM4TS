import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;

public class ArArchiveInputStreamTest {

    private MockInputStream input;

    @Before
    public void setup() {
        byte[] bytes = new byte[] {0x3c, 0x21, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a,
                                   0x38, 0x39, 0x37, 0x36, 0x35, 0x34, 0x33, 0x32};
        input = new MockInputStream(bytes);
    }

    @Test
    public void testGetNextArEntry() throws IOException {
        ArArchiveInputStream stream = new ArArchiveInputStream(input);
        ArArchiveEntry entry = stream.getNextArEntry();
        
        Assert.assertNotNull("Entry should not be null", entry);
        Assert.assertEquals("Entry name should match", "", entry.getName());
        Assert.assertEquals("Entry length should match", 87654321L, entry.getLength());
    }

    @Test
    public void testGetNextEntry() throws IOException {
        ArArchiveInputStream stream = new ArArchiveInputStream(input);
        ArchiveEntry entry = stream.getNextEntry();

        Assert.assertNotNull("Entry should not be null", entry);
        Assert.assertTrue("Entry should be instance of ArArchiveEntry", entry instanceof ArArchiveEntry);
        Assert.assertEquals("Entry name should match", "", entry.getName());
        Assert.assertEquals("Entry length should match", 87654321L, entry.getLength());
    }

    @Test
    public void testClose() throws IOException {
        ArArchiveInputStream stream = new ArArchiveInputStream(input);
        stream.close();

        Assert.assertTrue("Input stream should be closed", input.isClosed());
    }

    @Test
    public void testRead() throws IOException {
        byte[] expected = new byte[] {0x3c, 0x21, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a};
        ArArchiveInputStream stream = new ArArchiveInputStream(input);

        byte[] actual = new byte[8];
        int bytesRead = stream.read(actual);

        Assert.assertEquals("Number of bytes read should match", expected.length, bytesRead);
        Assert.assertTrue("Read bytes should match", Arrays.equals(expected, actual));
    }

    @Test
    public void testMatches() {
        byte[] signature = new byte[] {0x3c, 0x21, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a};
        boolean result = ArArchiveInputStream.matches(signature, 8);

        Assert.assertTrue("Matches should return true", result);
    }

    private class MockInputStream extends ByteArrayInputStream {

        private boolean closed;

        public MockInputStream(byte[] bytes) {
            super(bytes);
            closed = false;
        }

        @Override
        public void close() throws IOException {
            super.close();
            closed = true;
        }

        public boolean isClosed() {
            return closed;
        }
    }
}