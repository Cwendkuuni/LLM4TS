package org.apache.commons.codec.binary;

import org.junit.Test;
import static org.junit.Assert.*;

public class BaseNCodecInputStreamTest {

    @Test
    public void testRead() {
        byte[] byteArray = {65, 66, 67, 68};
        ByteArrayInputStream in = new ByteArrayInputStream(byteArray);
        BaseNCodec codec = new BaseNCodec();
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(in, codec, true);
        
        try {
            assertEquals(65, baseNCodecInputStream.read());
            assertEquals(66, baseNCodecInputStream.read());
            assertEquals(67, baseNCodecInputStream.read());
            assertEquals(68, baseNCodecInputStream.read());
            assertEquals(-1, baseNCodecInputStream.read());
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testRead_withEmptyStream() {
        byte[] byteArray = {};
        ByteArrayInputStream in = new ByteArrayInputStream(byteArray);
        BaseNCodec codec = new BaseNCodec();
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(in, codec, true);
        
        try {
            assertEquals(-1, baseNCodecInputStream.read());
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testRead_withSingleByteInput() {
        byte[] byteArray = {65};
        ByteArrayInputStream in = new ByteArrayInputStream(byteArray);
        BaseNCodec codec = new BaseNCodec();
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(in, codec, true);
        
        try {
            assertEquals(65, baseNCodecInputStream.read());
            assertEquals(-1, baseNCodecInputStream.read());
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testRead_withMultiBytesInput() {
        byte[] byteArray = {65, 66, 67, 68, 69, 70};
        ByteArrayInputStream in = new ByteArrayInputStream(byteArray);
        BaseNCodec codec = new BaseNCodec();
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(in, codec, true);
        
        try {
            byte[] outputArray = new byte[4];
            assertEquals(4, baseNCodecInputStream.read(outputArray, 0, 4));
            assertArrayEquals(new byte[] {65, 66, 67, 68}, outputArray);
            assertEquals(2, baseNCodecInputStream.read(outputArray, 0, 4));
            assertArrayEquals(new byte[] {69, 70, 67, 68}, outputArray);
            assertEquals(-1, baseNCodecInputStream.read());
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testRead_withNullInputArray() {
        byte[] byteArray = {65, 66, 67, 68};
        ByteArrayInputStream in = new ByteArrayInputStream(byteArray);
        BaseNCodec codec = new BaseNCodec();
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(in, codec, true);
        byte[] outputArray = null;
        
        try {
            baseNCodecInputStream.read(outputArray, 0, 4);
            fail("NullPointerException not thrown");
        } catch (NullPointerException e) {
            // Expected exception
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testRead_withNegativeOffset() {
        byte[] byteArray = {65, 66, 67, 68};
        ByteArrayInputStream in = new ByteArrayInputStream(byteArray);
        BaseNCodec codec = new BaseNCodec();
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(in, codec, true);
        byte[] outputArray = new byte[4];
        
        try {
            baseNCodecInputStream.read(outputArray, -1, 4);
            fail("IndexOutOfBoundsException not thrown");
        } catch (IndexOutOfBoundsException e) {
            // Expected exception
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testRead_withInvalidOffset() {
        byte[] byteArray = {65, 66, 67, 68};
        ByteArrayInputStream in = new ByteArrayInputStream(byteArray);
        BaseNCodec codec = new BaseNCodec();
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(in, codec, true);
        byte[] outputArray = new byte[4];
        
        try {
            baseNCodecInputStream.read(outputArray, 5, 4);
            fail("IndexOutOfBoundsException not thrown");
        } catch (IndexOutOfBoundsException e) {
            // Expected exception
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testRead_withZeroLength() {
        byte[] byteArray = {65, 66, 67, 68};
        ByteArrayInputStream in = new ByteArrayInputStream(byteArray);
        BaseNCodec codec = new BaseNCodec();
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(in, codec, true);
        byte[] outputArray = new byte[4];
        
        try {
            assertEquals(0, baseNCodecInputStream.read(outputArray, 0, 0));
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testRead_withOutOfRangeOffsetLength() {
        byte[] byteArray = {65, 66, 67, 68};
        ByteArrayInputStream in = new ByteArrayInputStream(byteArray);
        BaseNCodec codec = new BaseNCodec();
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(in, codec, true);
        byte[] outputArray = new byte[4];
        
        try {
            baseNCodecInputStream.read(outputArray, 0, 5);
            fail("IndexOutOfBoundsException not thrown");
        } catch (IndexOutOfBoundsException e) {
            // Expected exception
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testMarkSupported() {
        byte[] byteArray = {65, 66, 67, 68};
        ByteArrayInputStream in = new ByteArrayInputStream(byteArray);
        BaseNCodec codec = new BaseNCodec();
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(in, codec, true);
        
        assertFalse(baseNCodecInputStream.markSupported());
    }

    @Test
    public void testRead_withEOF() {
        byte[] byteArray = {65, 66, 67, -1};
        ByteArrayInputStream in = new ByteArrayInputStream(byteArray);
        BaseNCodec codec = new BaseNCodec();
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(in, codec, true);
        
        try {
            assertEquals(65, baseNCodecInputStream.read());
            assertEquals(66, baseNCodecInputStream.read());
            assertEquals(67, baseNCodecInputStream.read());
            assertEquals(256, baseNCodecInputStream.read());
            assertEquals(-1, baseNCodecInputStream.read());
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }
}