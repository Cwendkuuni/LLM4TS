
package org.apache.commons.codec.binary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class BaseNCodecInputStreamTest {

    private BaseNCodec baseNCodec;
    private BaseNCodecInputStream baseNCodecInputStream;

    @Before
    public void setUp() {
        baseNCodec = new BaseNCodec() {
            @Override
            void encode(byte[] p0, int p1, int p2, Context p3) {
            }

            @Override
            void decode(byte[] p0, int p1, int p2, Context p3) {
            }

            @Override
            void checkOverflow(int p0, byte[] p1, Context p2) throws IOException {
            }

            @Override
            void end(Context p0) {
            }
        };
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, true);
    }

    @Test
    public void testRead() throws IOException {
        assertEquals(-1, baseNCodecInputStream.read());

        byte[] testArray = {65, 66, 67};
        InputStream inputStream = new ByteArrayInputStream(testArray);
        baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        assertEquals(65, baseNCodecInputStream.read());
        assertEquals(66, baseNCodecInputStream.read());
        assertEquals(67, baseNCodecInputStream.read());
        assertEquals(-1, baseNCodecInputStream.read());
    }

    @Test(expected = NullPointerException.class)
    public void testRead_NullArray() throws IOException {
        baseNCodecInputStream.read(null, 0, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRead_InvalidOffset() throws IOException {
        byte[] testArray = new byte[1];
        baseNCodecInputStream.read(testArray, -1, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRead_InvalidLength1() throws IOException {
        byte[] testArray = new byte[1];
        baseNCodecInputStream.read(testArray, 0, -1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRead_InvalidLength2() throws IOException {
        byte[] testArray = new byte[1];
        baseNCodecInputStream.read(testArray, 0, 2);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRead_InvalidBufferLength() throws IOException {
        byte[] testArray = new byte[1];
        baseNCodecInputStream.read(testArray, 2, 1);
    }

    @Test
    public void testRead_EmptyArray() throws IOException {
        byte[] testArray = new byte[0];
        assertEquals(0, baseNCodecInputStream.read(testArray, 0, 0));
        assertEquals(0, baseNCodecInputStream.read(testArray, 0, 1));
    }

    @Test
    public void testRead_WithContent() throws IOException {
        byte[] testArray = new byte[3];
        assertEquals(3, baseNCodecInputStream.read(testArray, 0, 3));
        assertEquals(0, testArray[0]);
        assertEquals(0, testArray[1]);
        assertEquals(0, testArray[2]);
    }

    @Test
    public void testMarkSupported() {
        assertFalse(baseNCodecInputStream.markSupported());
    }

    @Test
    public void testAvailable() {
        assertEquals(0, baseNCodecInputStream.available());
    }

    @Test
    public void testSkip() {
        assertEquals(0, baseNCodecInputStream.skip(10));
    }

    @Test
    public void testMark() {
        baseNCodecInputStream.mark(10);
    }

    @Test
    public void testReset() throws IOException {
        baseNCodecInputStream.reset();
    }

    @Test
    public void testClose() throws IOException {
        baseNCodecInputStream.close();
    }

    @Test
    public void testHasData() {
        assertFalse(baseNCodec.hasData());
    }

    @Test
    public void testReadResults() {
        byte[] testArray = new byte[3];
        assertEquals(0, baseNCodec.readResults(testArray, 0, 3));
    }
}
