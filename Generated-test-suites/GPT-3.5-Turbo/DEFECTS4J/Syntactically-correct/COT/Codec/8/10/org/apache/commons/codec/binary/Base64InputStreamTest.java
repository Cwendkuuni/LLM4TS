import org.junit.Test;
import static org.junit.Assert.*;

public class Base64InputStreamTest {
    
    // Test zero-length input stream
    @Test
    public void testRead_EmptyInputStream() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        
        int result = base64InputStream.read();
        
        assertEquals(-1, result);
        
        base64InputStream.close();
    }
    
    // Test null input stream
    @Test(expected = NullPointerException.class) 
    public void testConstructor_NullInputStream() {
        new Base64InputStream(null);
    }
    
    // Test null byte array
    @Test(expected = NullPointerException.class) 
    public void testRead_NullByteArray() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[10]);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        
        base64InputStream.read(null, 0, 10);
        
        base64InputStream.close();
    }
    
    // Test negative offset
    @Test(expected = IndexOutOfBoundsException.class) 
    public void testRead_NegativeOffset() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[10]);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        
        byte[] byteArray = new byte[10];
        base64InputStream.read(byteArray, -1, 10);
        
        base64InputStream.close();
    }
    
    // Test negative length
    @Test(expected = IndexOutOfBoundsException.class) 
    public void testRead_NegativeLength() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[10]);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        
        byte[] byteArray = new byte[10];
        base64InputStream.read(byteArray, 0, -1);
        
        base64InputStream.close();
    }
    
    // Test offset greater than byte array length
    @Test(expected = IndexOutOfBoundsException.class) 
    public void testRead_OffsetGreaterThanByteArrayLength() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[10]);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        
        byte[] byteArray = new byte[10];
        base64InputStream.read(byteArray, 20, 5);
        
        base64InputStream.close();
    }
    
    // Test offset plus length greater than byte array length
    @Test(expected = IndexOutOfBoundsException.class) 
    public void testRead_OffsetPlusLengthGreaterThanByteArrayLength() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[10]);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        
        byte[] byteArray = new byte[10];
        base64InputStream.read(byteArray, 5, 10);
        
        base64InputStream.close();
    }
    
    // Test line length of 0
    @Test
    public void testConstructor_LineLengthZero() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[10]);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false, 0, null);
        
        assertEquals(0, base64InputStream.base64.getLineLength());
        
        base64InputStream.close();
    }
    
    // Test line separator larger than line length
    @Test
    public void testConstructor_LineSeparatorLargerThanLineLength() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[10]);
        byte[] lineSeparator = {13, 10, 13, 10}; // "\r\n\r\n"
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true, 10, lineSeparator);
        
        assertEquals(0, base64InputStream.base64.getLineLength());
        
        base64InputStream.close();
    }
    
    // Test read() method with single byte
    @Test
    public void testRead_SingleByte() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[] {65});
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true);
        
        int result = base64InputStream.read();
        
        assertEquals(65, result);
        
        base64InputStream.close();
    }
    
    // Test read(byte b[], int offset, int len) with different combinations of offset and length values
    @Test
    public void testRead_OffsetAndLength() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[] {65, 66, 67, 68, 69, 70});
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true);
        
        byte[] byteArray1 = new byte[10];
        int result1 = base64InputStream.read(byteArray1, 2, 3);
        assertArrayEquals(new byte[] {0, 0, 65, 66, 67, 0, 0, 0, 0, 0}, byteArray1);
        assertEquals(3, result1);
        
        byte[] byteArray2 = new byte[10];
        int result2 = base64InputStream.read(byteArray2, 0, 10);
        assertArrayEquals(new byte[] {68, 69, 70, 0, 0, 0, 0, 0, 0, 0}, byteArray2);
        assertEquals(3, result2);
        
        base64InputStream.close();
    }
}