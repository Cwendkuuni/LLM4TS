package org.apache.commons.compress.utils.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.compress.utils.IOUtils;

import org.junit.Test;

/**
 * IOUtils test case
 */
public class IOUtilsTest {

    @Test
    public void testCopy() throws IOException {
        // Test copying a byte array to a byte array
        byte[] src = { 0x01, 0x02, 0x03, 0x04 };
        byte[] dest = new byte[src.length];
        int n = IOUtils.copy(new ByteArrayInputStream(src), new ByteArrayOutputStream(dest.length), IOUtils.COPY_BUF_SIZE);
        byte[] result = ((ByteArrayOutputStream) dest).toByteArray();
        assertEquals("byte arrays are not equal", src, result);
        assertEquals("wrong number of bytes copied", src.length, n);

        // Test copying a byte array to itself
        byte[] src2 = { 0x05, 0x06, 0x07, 0x08 };
        byte[] dest2 = new byte[src2.length];
        IOUtils.copy(new ByteArrayInputStream(src2), new ByteArrayOutputStream(dest2.length), IOUtils.COPY_BUF_SIZE);
        byte[] result2 = ((ByteArrayOutputStream) dest2).toByteArray();
        assertEquals("byte arrays are not equal", src2, result2);

        // Test copying a InputStream to a OutputStream
        InputStream input = new ByteArrayInputStream(src);
        OutputStream output = new ByteArrayOutputStream();
        long n2 = IOUtils.copy(input, output);
        byte[] result3 = ((ByteArrayOutputStream) output).toByteArray();
        assertEquals("byte arrays are not equal", src, result3);
        assertEquals("wrong number of bytes copied", src.length, n2);

        // Test copying a InputStream to a OutputStream with different buffer sizes
        long n3 = IOUtils.copy(input, output, 1024);
        long n4 = IOUtils.copy(input, output, 8192);
        assertEquals("number of bytes copied should be the same", n3, n4);

        // Test copying an empty InputStream to a OutputStream
        InputStream emptyInput = new ByteArrayInputStream(new byte[0]);
        OutputStream emptyOutput = new ByteArrayOutputStream();
        long n5 = IOUtils.copy(emptyInput, emptyOutput);
        assertEquals("number of bytes copied should be 0", 0, n5);

        // Test copying a InputStream to a OutputStream with an error
        InputStream errorInput = new ByteArrayInputStream(new byte[] { 0x01 });
        OutputStream errorOutput = new ByteArrayOutputStream() {
            @Override
            public void write(int b) throws IOException {
                throw new IOException("Test exception");
            }
        };
        assertThrows("should throw an IOException", IOException.class, () -> IOUtils.copy(errorInput, errorOutput));
    }

    @Test
    public void testSkip() throws IOException {
        byte[] src = { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08 };
        ByteArrayInputStream input = new ByteArrayInputStream(src);

        long numToSkip = 0;
        long skipped = IOUtils.skip(input, numToSkip);
        assertEquals("wrong number of bytes skipped", 0, skipped);

        numToSkip = 5;
        skipped = IOUtils.skip(input, numToSkip);
        byte[] result = new byte[src.length - 5];
        System.arraycopy(src, 5, result, 0, result.length);
        byte[] expected = result;
        byte[] actual = ((ByteArrayInputStream) input).readNBytes(5);
        assertEquals("bytes skipped are not the expected bytes", expected, actual);
        assertEquals("wrong number of bytes skipped", 5, skipped);

        numToSkip = 13;
        skipped = IOUtils.skip(input, numToSkip);
        assertEquals("wrong number of bytes skipped", src.length, skipped);

        input = new ByteArrayInputStream(new byte[0]);
        numToSkip = 1;
        assertThrows("should throw an IOException when the InputStream has no data", IOException.class, () -> IOUtils.skip(input, numToSkip));
    }

    @Test
    public void testReadFully() throws IOException {
        byte[] src = { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08 };
        ByteArrayInputStream input = new ByteArrayInputStream(src);
        byte[] dest = new byte[src.length];

        int n = IOUtils.readFully(input, dest);
        assertEquals("wrong number of bytes read", src.length, n);
        byte[] result = new byte[src.length];
        System.arraycopy(src, 0, result, 0, src.length);
        assertEquals("byte arrays are not equal", result, dest);

        // Test reading less bytes than the length of the array
        byte[] dest2 = new byte[src.length + 1];
        n = IOUtils.readFully(input, dest2);
        assertEquals("wrong number of bytes read", src.length, n);
        byte[] actual = new byte[n];
        System.arraycopy(dest2, 0, actual, 0, n);
        byte[] expected = src;
        assertEquals("byte arrays are not equal", expected, actual);

        // Test reading less bytes than the length of the array and getting an error
        byte[] dest3 = new byte[src.length + 1];
        assertThrows("should throw an EOFException when not enough bytes are available", EOFException.class, () -> IOUtils.readFully(input, dest3, src.length + 1));
    }

    @Test
    public void testToByteArray() throws IOException {
        byte[] src = { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08 };
        InputStream input = new ByteArrayInputStream(src);
        byte[] dest = IOUtils.toByteArray(input);
        assertEquals("byte arrays are not equal", src, dest);

        // Test InputStream with an error
        InputStream errorInput = new ByteArrayInputStream(new byte[] { 0x01 });
        assertThrows("should throw an IOException", IOException.class, () -> IOUtils.toByteArray(errorInput));

        // Test InputStream with a length of 0
        InputStream emptyInput = new ByteArrayInputStream(new byte[0]);
        byte[] emptyResult = IOUtils.toByteArray(emptyInput);
        assertEquals("expected an empty byte array", new byte[0], emptyResult);
    }
}