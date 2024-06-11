import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;

public class ArArchiveInputStreamTest {

    @Test
    public void testConstructor() {
        // Create input stream and assert it is not null
        ArArchiveInputStream arInputStream = new ArArchiveInputStream(input);
        Assert.assertNotNull(arInputStream);
    }

    @Test
    public void testGetNextArEntry() {
        // Create input stream
        ArArchiveInputStream arInputStream = new ArArchiveInputStream(input);

        try {
            // Call getNextArEntry and assert it is not null
            ArArchiveEntry entry = arInputStream.getNextArEntry();
            Assert.assertNotNull(entry);
        } catch (Exception e) {
            Assert.fail("IOException thrown");
        }
    }

    @Test
    public void testGetNextEntry() {
        // Create input stream
        ArArchiveInputStream arInputStream = new ArArchiveInputStream(input);

        try {
            // Call getNextEntry and assert it is not null
            ArchiveEntry entry = arInputStream.getNextEntry();
            Assert.assertNotNull(entry);
        } catch (Exception e) {
            Assert.fail("IOException thrown");
        }
    }

    @Test
    public void testClose() {
        // Create input stream
        ArArchiveInputStream arInputStream = new ArArchiveInputStream(input);

        try {
            // Call close and assert no exception is thrown
            arInputStream.close();
        } catch (Exception e) {
            Assert.fail("IOException thrown");
        }
    }

    @Test
    public void testRead() {
        // Create input stream
        ArArchiveInputStream arInputStream = new ArArchiveInputStream(input);

        try {
            // Call read and assert the returned value is valid
            int value = arInputStream.read();
            Assert.assertTrue(value >= 0 && value <= 255);
        } catch (Exception e) {
            Assert.fail("IOException thrown");
        }
    }

    @Test
    public void testReadByteArray() {
        // Create input stream
        ArArchiveInputStream arInputStream = new ArArchiveInputStream(input);

        try {
            // Create byte array to store read data
            byte[] data = new byte[10];

            // Call read(byte[]) and assert the returned value is valid
            int bytesRead = arInputStream.read(data);
            Assert.assertTrue(bytesRead >= 0 && bytesRead <= 10);
        } catch (Exception e) {
            Assert.fail("IOException thrown");
        }
    }

    @Test
    public void testReadByteArrayWithOffsetAndLength() {
        // Create input stream
        ArArchiveInputStream arInputStream = new ArArchiveInputStream(input);

        try {
            // Create byte array to store read data
            byte[] data = new byte[10];

            // Call read(byte[], int, int) and assert the returned value is valid
            int bytesRead = arInputStream.read(data, 2, 6);
            Assert.assertTrue(bytesRead >= 0 && bytesRead <= 6);
        } catch (Exception e) {
            Assert.fail("IOException thrown");
        }
    }

    @Test
    public void testMatches() {
        // Create byte array with valid signature
        byte[] signature = new byte[]{0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a};

        // Call matches and assert the returned value is true
        boolean result = ArArchiveInputStream.matches(signature, 8);
        Assert.assertTrue(result);
    }

}