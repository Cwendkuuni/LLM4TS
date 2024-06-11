
package org.apache.commons.codec.binary;

import org.junit.Test;
import org.junit.Assert;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Base64InputStreamTest {
    
    @Test
    public void testConstructor1() {
        InputStream input = new ByteArrayInputStream(new byte[] {65, 66, 67});
        Base64InputStream base64InputStream = new Base64InputStream(input);
        Assert.assertNotNull(base64InputStream);
    }
    
    @Test
    public void testConstructor2() {
        InputStream input = new ByteArrayInputStream(new byte[] {65, 66, 67});
        Base64InputStream base64InputStream = new Base64InputStream(input, false);
        Assert.assertNotNull(base64InputStream);
    }
    
    @Test
    public void testConstructor3() {
        InputStream input = new ByteArrayInputStream(new byte[] {65, 66, 67});
        Base64InputStream base64InputStream = new Base64InputStream(input, false, 76, new byte[] {13, 10});
        Assert.assertNotNull(base64InputStream);
    }
    
    @Test
    public void testRead() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] {65, 66, 67});
        Base64InputStream base64InputStream = new Base64InputStream(input);
        int result = base64InputStream.read();
        Assert.assertEquals(65, result);
    }
    
    @Test
    public void testReadWithByteArray() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] {65, 66, 67});
        Base64InputStream base64InputStream = new Base64InputStream(input);
        byte[] buffer = new byte[3];
        int result = base64InputStream.read(buffer, 0, 3);
        Assert.assertEquals(3, result);
        Assert.assertEquals(65, buffer[0]);
        Assert.assertEquals(66, buffer[1]);
        Assert.assertEquals(67, buffer[2]);
    }
    
    @Test(expected = NullPointerException.class)
    public void testReadNullByteArray() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] {65, 66, 67});
        Base64InputStream base64InputStream = new Base64InputStream(input);
        base64InputStream.read(null, 0, 3);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadInvalidOffset() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] {65, 66, 67});
        Base64InputStream base64InputStream = new Base64InputStream(input);
        byte[] buffer = new byte[3];
        base64InputStream.read(buffer, -1, 3);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadInvalidLength() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] {65, 66, 67});
        Base64InputStream base64InputStream = new Base64InputStream(input);
        byte[] buffer = new byte[3];
        base64InputStream.read(buffer, 0, -1);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadInvalidBufferLength() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] {65, 66, 67});
        Base64InputStream base64InputStream = new Base64InputStream(input);
        byte[] buffer = new byte[3];
        base64InputStream.read(buffer, 0, 4);
    }
    
    @Test
    public void testMarkSupported() {
        InputStream input = new ByteArrayInputStream(new byte[] {65, 66, 67});
        Base64InputStream base64InputStream = new Base64InputStream(input);
        boolean result = base64InputStream.markSupported();
        Assert.assertFalse(result);
    }
}
