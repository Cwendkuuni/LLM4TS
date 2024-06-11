package org.apache.commons.codec.binary;

import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Base64InputStreamTest
 */
public class Base64InputStreamTest {

    @Test
    public void testBase64InputStreamConstructor() {
        InputStream inputStream = new ByteArrayInputStream("Base64Data".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        Assert.assertFalse(base64InputStream.doEncode);
        Assert.assertNotNull(base64InputStream.base64);
    }

    @Test
    public void testBase64InputStreamConstructorWithEncode() {
        InputStream inputStream = new ByteArrayInputStream("Base64Data".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true);
        Assert.assertTrue(base64InputStream.doEncode);
        Assert.assertNotNull(base64InputStream.base64);
    }

    @Test
    public void testBase64InputStreamConstructorWithEncodeLineLengthAndLineSeparator() {
        InputStream inputStream = new ByteArrayInputStream("Base64Data".getBytes());
        byte[] lineSeparator = new byte[]{0x0D, 0x0A};
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true, 76, lineSeparator);
        Assert.assertTrue(base64InputStream.doEncode);
        Assert.assertNotNull(base64InputStream.base64);
        Assert.assertEquals(76, base64InputStream.base64.getLineLength());
        Assert.assertArrayEquals(lineSeparator, base64InputStream.base64.getLineSeparator());
    }

    @Test
    public void testBase64InputStreamRead() throws IOException {
        String base64Data = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLS0tLS0tCiAgICAtLS0tLS0tCiAgICAtLS0tLS0tCiAgICAtLS0tLS0tCiAgICAtLS0tLS0tCiAgICAtLS0tLS0tCiAgICAtLS0tLS0tCiAgICAtLS0tLS0tCiAgICAtLS0tLS0tCiAgICAtLS0tLS0tCiAgICAtLS0tLS0tCiAgICAtLS0tLS0tCiAgICAtLS0tLS0tCiAgICAtLS0tLS0t";
        InputStream inputStream = new ByteArrayInputStream(base64Data.getBytes());

        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] byteArray = new byte[10];
        int readLength = 0;

        // Test reading with default decode
        readLength = base64InputStream.read(byteArray);
        Assert.assertEquals(readLength, 3);
        Assert.assertArrayEquals(new byte[]{"A", "B", "="}, byteArray);

        // Test reading with decode and read all bytes
        byteArray = new byte[base64Data.length()];
        readLength = base64InputStream.read(byteArray);
        Assert.assertEquals(readLength, base64Data.length());
        Assert.assertEquals(new String(byteArray), "Hello World!");

        // Test reading with encode
        base64InputStream = new Base64InputStream(inputStream, true);
        byteArray = new byte[4];
        readLength = base64InputStream.read(byteArray);
        Assert.assertEquals(readLength, 4);
        Assert.assertArrayEquals(new byte[]{48, 65, 61, 6D}, byteArray);

        // Test reading with encode, read all bytes
        byteArray = new byte[base64Data.length()];
        readLength = base64InputStream.read(byteArray);
        Assert.assertEquals(readLength, base64Data.length());
        Assert.assertEquals(new String(byteArray), base64Data);

        // Test reading with encode, line length and line separator
        base64InputStream = new Base64InputStream(inputStream, true, 76, new byte[]{0x0D, 0x0A});
        byteArray = new byte[10];
        readLength = base64InputStream.read(byteArray);
        Assert.assertEquals(readLength, 3);
        Assert.assertArrayEquals(new byte[]{"A", "B", "="}, byteArray);

        byteArray = new byte[base64Data.length()];
        readLength = base64InputStream.read(byteArray);
        Assert.assertEquals(readLength, base64Data.length());
        Assert.assertEquals(new String(byteArray), "Hello World!");
    }

    @Test(expected = IOException.class)
    public void testBase64InputStreamMarkSupported() throws IOException {
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream("Base64Data".getBytes()));
        base64InputStream.mark(0);
    }

    @Test(expected = NullPointerException.class)
    public void testBase64InputStreamNullInput() {
        Base64InputStream base64InputStream = new Base64InputStream(null);
    }
}