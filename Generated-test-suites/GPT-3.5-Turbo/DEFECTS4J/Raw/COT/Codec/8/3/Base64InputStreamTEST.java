import org.junit.Assert;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class Base64InputStreamTest {

    @Test
    public void testConstructor1() {
        try {
            Base64InputStream stream = new Base64InputStream(new ByteArrayInputStream("test".getBytes()));
        } catch (Exception e) {
            Assert.fail("Exception should not be thrown.");
        }
    }

    @Test
    public void testConstructor2() {
        try {
            Base64InputStream stream = new Base64InputStream(new ByteArrayInputStream("test".getBytes()), true);
        } catch (Exception e) {
            Assert.fail("Exception should not be thrown.");
        }
    }
    
    @Test
    public void testConstructor3() {
        try {
            byte[] lineSeparator = {'\r', '\n'};
            Base64InputStream stream = new Base64InputStream(new ByteArrayInputStream("test".getBytes()),
                    true, 76, lineSeparator);
        } catch (Exception e) {
            Assert.fail("Exception should not be thrown.");
        }
    }
    
    @Test
    public void testRead() {
        try {
            Base64InputStream stream = new Base64InputStream(new ByteArrayInputStream("test".getBytes()));
            int read = stream.read();
            Assert.assertEquals('t', read);
        } catch (Exception e) {
            Assert.fail("Exception should not be thrown.");
        }
    }
    
    @Test
    public void testReadArray() {
        try {
            byte[] bytes = "test".getBytes();
            Base64InputStream stream = new Base64InputStream(new ByteArrayInputStream(bytes));
            byte[] buffer = new byte[4];
            int read = stream.read(buffer, 0, 4);
            Assert.assertEquals(4, read);
            Assert.assertArrayEquals(bytes, buffer);
        } catch (Exception e) {
            Assert.fail("Exception should not be thrown.");
        }
    }
    
    @Test
    public void testMarkSupported() {
        Base64InputStream stream = new Base64InputStream(new ByteArrayInputStream("test".getBytes()));
        Assert.assertFalse(stream.markSupported());
    }
    
    @Test
    public void testEmptyInputStream() {
        try {
            Base64InputStream stream = new Base64InputStream(new ByteArrayInputStream(new byte[0]));
        } catch (Exception e) {
            Assert.fail("Exception should not be thrown.");
        }
    }
    
    @Test(expected = NullPointerException.class)
    public void testNullInputStream() {
        Base64InputStream stream = new Base64InputStream(null);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadNegativeOffset() throws IOException {
        Base64InputStream stream = new Base64InputStream(new ByteArrayInputStream("test".getBytes()));
        byte[] buffer = new byte[4];
        int read = stream.read(buffer, -1, 4);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadNegativeLength() throws IOException {
        Base64InputStream stream = new Base64InputStream(new ByteArrayInputStream("test".getBytes()));
        byte[] buffer = new byte[4];
        int read = stream.read(buffer, 0, -1);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadInvalidOffset() throws IOException {
        Base64InputStream stream = new Base64InputStream(new ByteArrayInputStream("test".getBytes()));
        byte[] buffer = new byte[4];
        int read = stream.read(buffer, 5, 4);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadInvalidLength() throws IOException {
        Base64InputStream stream = new Base64InputStream(new ByteArrayInputStream("test".getBytes()));
        byte[] buffer = new byte[4];
        int read = stream.read(buffer, 0, 5);
    }
}