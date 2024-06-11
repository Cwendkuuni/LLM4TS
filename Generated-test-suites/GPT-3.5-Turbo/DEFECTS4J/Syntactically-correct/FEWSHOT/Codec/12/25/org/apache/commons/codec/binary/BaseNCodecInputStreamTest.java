package org.apache.commons.codec.binary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class BaseNCodecInputStreamTest {
    
    @Test
    public void testRead_singleByte() {
        byte[] data = { 65, 66, 67 }; // ABC
        BaseNCodecInputStream stream = new BaseNCodecInputStream(data, BaseNCodec.BASE64, true);
        
        try {
            assertEquals(65, stream.read());
            assertEquals(66, stream.read());
            assertEquals(67, stream.read());
            assertEquals(-1, stream.read());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testRead_byteArrayOffsetLength() {
        byte[] data1 = { 65, 66, 67 }; // ABC
        byte[] data2 = { 68, 69, 70 }; // DEF
        byte[] data3 = { 71, 72 }; // GH
        
        BaseNCodecInputStream stream1 = new BaseNCodecInputStream(data1, BaseNCodec.BASE64, true);
        BaseNCodecInputStream stream2 = new BaseNCodecInputStream(data2, BaseNCodec.BASE64, true);
        BaseNCodecInputStream stream3 = new BaseNCodecInputStream(data3, BaseNCodec.BASE64, true);
        
        byte[] result1 = new byte[3];
        byte[] result2 = new byte[3];
        byte[] result3 = new byte[3];
        
        try {
            assertEquals(3, stream1.read(result1, 0, 3));
            assertTrue(result1[0] == 65 && result1[1] == 66 && result1[2] == 67);
            
            assertEquals(3, stream2.read(result2, 0, 3));
            assertTrue(result1[0] == 68 && result1[1] == 69 && result1[2] == 70);
            
            assertEquals(2, stream3.read(result3, 0, 3));
            assertTrue(result1[0] == 71 && result1[1] == 72);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testMarkSupported() {
        byte[] data = { 65, 66, 67 }; // ABC
        BaseNCodecInputStream stream = new BaseNCodecInputStream(data, BaseNCodec.BASE64, true);
        assertEquals(false, stream.markSupported());
    }
    
    @Test
    public void testAvailable() {
        byte[] data = { 65, 66, 67 }; // ABC
        BaseNCodecInputStream stream = new BaseNCodecInputStream(data, BaseNCodec.BASE64, true);
        try {
            assertEquals(3, stream.available());
            stream.read();
            assertEquals(2, stream.available());
            stream.read();
            assertEquals(1, stream.available());
            stream.read();
            assertEquals(0, stream.available());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}