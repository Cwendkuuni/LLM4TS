package org.apache.commons.codec.binary;
import org.junit.Test;
import org.apache.commons.codec.binary.Base64InputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class Base64InputStreamTest {

    @Test
    public void testRead() throws IOException {
        byte[] testData = {'T','e','s','t',' ','D','a','t','a'};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        int actualData;
        StringBuilder decodedData = new StringBuilder();

        while((actualData = base64InputStream.read()) != -1) {
            decodedData.append((char)actualData);
        }
        
        String expectedData = "Test Data";
        assert(decodedData.toString().equals(expectedData));
    }

    @Test
    public void testReadWithEmptyInputStream() throws IOException {
        ByteArrayInputStream emptyStream = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStream = new Base64InputStream(emptyStream);
        
        int actualData = base64InputStream.read();
        
        assert(actualData == -1);
    }

    @Test(expected = NullPointerException.class)
    public void testReadWithNullByteArray() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        
        base64InputStream.read(null, 0, 10);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidOffset() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        
        base64InputStream.read(new byte[10], -1, 10);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidLength() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        
        base64InputStream.read(new byte[10], 0, -1);
    }

    // Add more test cases as needed for other methods in Base64InputStream class

}