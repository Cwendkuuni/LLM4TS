import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class Base64InputStreamTest {

    @Test
    public void testRead_singleByte() throws IOException {
        byte[] input = {65, 66, 67}; // 'A','B','C'
        ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64.encodeBase64(input));

        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);
        
        Assert.assertEquals(65, base64InputStream.read()); // 'A'
        Assert.assertEquals(66, base64InputStream.read()); // 'B'
        Assert.assertEquals(67, base64InputStream.read()); // 'C'
        Assert.assertEquals(-1, base64InputStream.read()); // End of Stream
        
        base64InputStream.close();
    }
    
    @Test
    public void testRead_intoByteArray() throws IOException {
        byte[] input = {65, 66, 67}; // 'A','B','C'
        ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64.encodeBase64(input));
        byte[] output = new byte[3];
        
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);
        
        Assert.assertEquals(3, base64InputStream.read(output, 0, 3));
        Assert.assertArrayEquals(input, output);
        
        Assert.assertEquals(-1, base64InputStream.read()); // End of Stream
        
        base64InputStream.close();
    }
    
    @Test
    public void testRead_moreThanBufferSize() throws IOException {
        byte[] input = new byte[10000];
        for (int i = 0; i < 10000; i++) {
            input[i] = (byte) (i % 256);
        }
        
        ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64.encodeBase64(input));
        byte[] output = new byte[10000];
        
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);
        
        Assert.assertEquals(10000, base64InputStream.read(output, 0, 10000));
        Assert.assertArrayEquals(input, output);
        
        Assert.assertEquals(-1, base64InputStream.read()); // End of Stream
        
        base64InputStream.close();
    }
    
    @Test (expected = IOException.class)
    public void testRead_nullByteArray() throws IOException {
        byte[] input = {65, 66, 67}; // 'A','B','C'
        ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64.encodeBase64(input));
        byte[] output = null;
        
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);
        
        base64InputStream.read(output, 0, 3);
    }
    
    @Test (expected = IndexOutOfBoundsException.class)
    public void testRead_invalidOffset() throws IOException {
        byte[] input = {65, 66, 67}; // 'A','B','C'
        ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64.encodeBase64(input));
        byte[] output = new byte[3];
        
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);
        
        base64InputStream.read(output, 4, 3);
    }
    
    @Test (expected = IndexOutOfBoundsException.class)
    public void testRead_invalidLength() throws IOException {
        byte[] input = {65, 66, 67}; // 'A','B','C'
        ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64.encodeBase64(input));
        byte[] output = new byte[3];
        
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);
        
        base64InputStream.read(output, 0, 4);
    }
    
    @Test (expected = IndexOutOfBoundsException.class)
    public void testRead_invalidOffsetAndLength() throws IOException {
        byte[] input = {65, 66, 67}; // 'A','B','C'
        ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64.encodeBase64(input));
        byte[] output = new byte[3];
        
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);
        
        base64InputStream.read(output, 1, 3);
    }
    
    @Test
    public void testRead_emptyStream() throws IOException {
        byte[] input = {};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64.encodeBase64(input));
        byte[] output = new byte[3];
        
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);
        
        Assert.assertEquals(-1, base64InputStream.read(output, 0, 3)); // End of Stream
        
        base64InputStream.close();
    }
    
    @Test
    public void testRead_emptyAndThenNonEmptyStream() throws IOException {
        byte[] input1 = {};
        ByteArrayInputStream inputStream1 = new ByteArrayInputStream(Base64.encodeBase64(input1));
        byte[] output = new byte[3];
        
        Base64InputStream base64InputStream1 = new Base64InputStream(inputStream1, false);
        Assert.assertEquals(-1, base64InputStream1.read(output, 0, 3)); // End of Stream
        base64InputStream1.close();
        
        byte[] input2 = {65, 66, 67}; // 'A','B','C'
        ByteArrayInputStream inputStream2 = new ByteArrayInputStream(Base64.encodeBase64(input2));
        
        Base64InputStream base64InputStream2 = new Base64InputStream(inputStream2, false);
        Assert.assertEquals(3, base64InputStream2.read(output, 0, 3));
        Assert.assertArrayEquals(input2, output);
        
        Assert.assertEquals(-1, base64InputStream2.read()); // End of Stream
        
        base64InputStream2.close();
    }
    
    @Test
    public void testMarkSupported() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);
        
        Assert.assertFalse(base64InputStream.markSupported());
        
        base64InputStream.close();
    }
    
    @Test
    public void testRead_lineLength() throws IOException {
        byte[] input = {65, 66, 67}; // 'A','B','C'
        ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64.encodeBase64(input));
        byte[] output = new byte[4];
        
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true, 4, null);
        
        Assert.assertEquals(4, base64InputStream.read(output, 0, 4)); // 'QUJD' + line separator
        Assert.assertArrayEquals("QUJD".getBytes(), output);
        
        Assert.assertEquals(0, base64InputStream.read(output, 0, 4)); // End of Stream
        
        base64InputStream.close();
    }
    
    @Test
    public void testRead_lineLength_zero() throws IOException {
        byte[] input = {65, 66, 67}; // 'A','B','C'
        ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64.encodeBase64(input));
        byte[] output = new byte[4];
        
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true, 0, null);
        
        Assert.assertEquals(3, base64InputStream.read(output, 0, 4)); // 'A', 'B', 'C'
        
        Assert.assertEquals(0, base64InputStream.read(output, 0, 4)); // End of Stream
        
        base64InputStream.close();
    }
    
    @Test
    public void testRead_lineSeparator() throws IOException {
        byte[] input = {65, 66, 67}; // 'A','B','C'
        byte[] lineSeparator = {13, 10}; // Carriage Return + Line Feed
        
        ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64.encodeBase64(input));
        byte[] output = new byte[6];
        
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true, 4, lineSeparator);
        
        Assert.assertEquals(6, base64InputStream.read(output, 0, 6)); // 'QUJD' + CR + LF
        Assert.assertArrayEquals("QUJD\r\n".getBytes(), output);
        
        Assert.assertEquals(0, base64InputStream.read(output, 0, 6)); // End of Stream
        
        base64InputStream.close();
    }
    
    @Test
    public void testEncodeStream() throws IOException {
        byte[] input = {65, 66, 67}; // 'A','B','C'
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input);
        byte[] output = Base64.encodeBase64(input);
        
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true);
        
        byte[] result = new byte[4]; // 'QUJD'
        
        Assert.assertEquals(4, base64InputStream.read(result, 0, 4));
        Assert.assertArrayEquals(output, result);
        
        Assert.assertEquals(-1, base64InputStream.read()); // End of Stream
        
        base64InputStream.close();
    }

}