package org.apache.commons.codec.binary;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

public class BaseNCodecInputStreamTest {
    @Test
    public void testRead() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream("BaseNTest".getBytes());
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        byte[] singleByte = new byte[1];
        int read;

        // read single bytes
        read = baseNCodecInputStream.read();
        assertEquals((int) 'B', read);
        read = baseNCodecInputStream.read();
        assertEquals((int) 'a', read);
        read = baseNCodecInputStream.read();
        assertEquals((int) 's', read);
        read = baseNCodecInputStream.read();
        assertEquals((int) 'e', read);
        read = baseNCodecInputStream.read();
        assertEquals((int) ' ', read);
        read = baseNCodecInputStream.read();
        assertEquals((int) 'N', read);
        read = baseNCodecInputStream.read();
        assertEquals((int) 'T', read);
        read = baseNCodecInputStream.read();
        assertEquals((int) ' ', read);
        read = baseNCodecInputStream.read();
        assertEquals((int) 'Test'[0], read);
        read = baseNCodecInputStream.read();
        assertEquals((int) '.', read);
        read = baseNCodecInputStream.read();
        assertEquals(BaseNCodecInputStream.EOF, read);

        // test negative case
        baseNCodecInputStream.mark(0);
        baseNCodecInputStream.read(singleByte, 0, 1);
        baseNCodecInputStream.reset();
        read = baseNCodecInputStream.read(singleByte, 0, 1);
        assertEquals(BaseNCodecInputStream.EOF, read);

        // test zero length case
        read = baseNCodecInputStream.read(singleByte, 0, 0);
        assertEquals(BaseNCodecInputStream.EOF, read);

        // test null input
        byte[] nullByteArray = null;
        try {
            baseNCodecInputStream.read(nullByteArray, 0, 1);
            fail("Should have thrown a NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testReadWithEncode() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream("ZXhhbXBsZS5jb20=".getBytes());
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, true);

        byte[] singleByte = new byte[1];
        int read;

        read = baseNCodecInputStream.read();
        assertEquals((int) 'Z', read);
        read = baseNCodecInputStream.read();
        assertEquals((int) 'X', read);
        read = baseNCodecInputStream.read();
        assertEquals((int) 'h', read);
        read = baseNCodecInputStream.read();
        assertEquals((int) 'h', read);
        read = baseNCodecInputStream.read();
        assertEquals((int) 'b', read);
        read = baseNCodecInputStream.read();
        assertEquals((int) 'X', read);
        read = baseNCodecInputStream.read();
        assertEquals((int) 'B', read);
        read = baseNCodecInputStream.read();
        assertEquals((int) 's', read);
        read = baseNCodecInputStream.read();
        assertEquals((int) 'Z', read);
        read = baseNCodecInputStream.read();
        assertEquals((int) 'S', read);
        read = baseNCodecInputStream.read();
        assertEquals((int) '5', read);
        read = baseNCodecInputStream.read();
        assertEquals((int) 'j', read);
        read = baseNCodecInputStream.read();
        assertEquals((int) 'b', read);
        read = baseNCodecInputStream.read();
        assertEquals((int) '2', read);
        read = baseNCodecInputStream.read();
        assertEquals((int) '0', read);
        read = baseNCodecInputStream.read();
        assertEquals((int) EOF, read);

        // test negative case
        baseNCodecInputStream.mark(0);
        baseNCodecInputStream.read(singleByte, 0, 1);
        baseNCodecInputStream.reset();
        read = baseNCodecInputStream.read(singleByte, 0, 1);
        assertEquals(BaseNCodecInputStream.EOF, read);

        // test zero length case
        read = baseNCodecInputStream.read(singleByte, 0, 0);
        assertEquals(BaseNCodecInputStream.EOF, read);

        // test null input
        byte[] nullByteArray = null;
        try {
            baseNCodecInputStream.read(nullByteArray, 0, 1);
            fail("Should have thrown a NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testReadArray() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream("BaseNTest".getBytes());
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        byte[] buffer = new byte[5];
        int read;

        read = baseNCodecInputStream.read(buffer);
        assertArrayEquals(new byte[]{'B', 'a', 's', 'e', ' '}, buffer);
        assertEquals(5, read);

        read = baseNCodecInputStream.read(buffer, 3, 2);
        assertArrayEquals(new byte[]{'e', ' '}, buffer, 3);
        assertEquals(2, read);

        // test zero length case
        read = baseNCodecInputStream.read(buffer, 0, 0);
        assertEquals(0, read);

        // test null input
        byte[] nullByteArray = null;
        try {
            baseNCodecInputStream.read(nullByteArray, 0, 1);
            fail("Should have thrown a NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testReadWithEncodeArray() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream("ZXhhbXBsZS5jb20=".getBytes());
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, true);

        byte[] buffer = new byte[9];
        int read;

        read = baseNCodecInputStream.read(buffer);
        assertArrayEquals(new byte[]{'Z', 'X', 'h', 'h', 'b', 'X', 'B', 's', 'Z'}, buffer);
        assertEquals(9, read);

        read = baseNCodecInputStream.read(buffer, 5, 4);
        assertArrayEquals(new byte[]{'Z', 'S', '5', 'j'}, buffer, 5);
        assertEquals(4, read);

        // test zero length case
        read = baseNCodecInputStream.read(buffer, 0, 0);
        assertEquals(0, read);

        // test null input
        byte[] nullByteArray = null;
        try {
            baseNCodecInputStream.read(nullByteArray, 0, 1);
            fail("Should have thrown a NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testHasData() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream("BaseNTest".getBytes());
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        assertTrue(baseNCodecInputStream.hasData());

        byte[] singleByte = new byte[1];
        baseNCodecInputStream.read(singleByte, 0, 1);

        assertFalse(baseNCodecInputStream.hasData());

        byte[] buffer = new byte[5];
        baseNCodecInputStream.read(buffer);

        assertFalse(baseNCodecInputStream.hasData());

        byte[] nullByteArray = null;
        try {
            baseNCodecInputStream.hasData();
            fail("Should have thrown a NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }
}