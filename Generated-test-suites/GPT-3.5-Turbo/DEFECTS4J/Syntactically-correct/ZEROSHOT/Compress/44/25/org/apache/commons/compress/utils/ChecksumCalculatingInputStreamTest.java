package org.apache.commons.compress.utils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.CRC32;
import java.util.zip.Checksum;


public class ChecksumCalculatingInputStreamTest {

    ChecksumCalculatingInputStream checksumCalculatingInputStream;
    byte[] byteArray;
    Checksum checksum;
    ByteArrayInputStream byteArrayInputStream;

    @Before
    public void setUp() {
        byteArrayInputStream = new ByteArrayInputStream(new byte[10]);
        checksum = new CRC32();
        checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, byteArrayInputStream);
    }

    @Test(expected = IOException.class)
    public void readThrowsIOException() throws IOException {
        checksumCalculatingInputStream.read();
    }

    @Test
    public void readValidData() throws IOException{
      Assert.assertEquals(-1,checksumCalculatingInputStream.read());
    }

    @Test
    public void readByteCountBasedFromStream() throws IOException{
        byteArray = new byte[10];
        Assert.assertEquals(10,  checksumCalculatingInputStream.read(byteArray));
    }

    @Test
    public void skipValidData() throws IOException{
        Assert.assertEquals(0, checksumCalculatingInputStream.skip(1));
    }
    
    @Test
    public void getValueTest() {
        Assert.assertEquals(0, checksumCalculatingInputStream.getValue());
    }
}