package org.apache.commons.compress.utils;

import org.apache.commons.compress.archivers.test.TestArchiveEntry;
import org.apache.commons.compress.archivers.test.TestInputStream;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ArchiveUtilsTest {

    @Test
    public void testToString() throws IOException {
        final ArchiveEntry entry = new TestArchiveEntry("test.txt", "test content".getBytes(StandardCharsets.UTF_8));
        assertEquals("- 10 test.txt\n", ArchiveUtils.toString(entry));
        final ArchiveEntry dirEntry = new TestArchiveEntry("testdir/", true);
        assertEquals("d 0 testdir/\n", ArchiveUtils.toString(dirEntry));
    }

    @Test
    public void testMatchAsciiBuffer() {
        final byte[] buffer1 = "test".getBytes(StandardCharsets.UTF_8);
        final byte[] buffer2 = new byte[]{'t', 'e', 's', 't'};
        assertTrue(ArchiveUtils.matchAsciiBuffer("test", buffer1));
        assertTrue(ArchiveUtils.matchAsciiBuffer("test", buffer2));
        assertFalse(ArchiveUtils.matchAsciiBuffer("test2", buffer1));
        assertFalse(ArchiveUtils.matchAsciiBuffer("test2", buffer2));
    }

    @Test
    public void testToAsciiBytes() {
        final String inputString = "test";
        byte[] expected = inputString.getBytes(StandardCharsets.UTF_8);
        byte[] result = ArchiveUtils.toAsciiBytes(inputString);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testToAsciiString() {
        final byte[] inputBytes = "test".getBytes(StandardCharsets.UTF_8);
        assertEquals("test", ArchiveUtils.toAsciiString(inputBytes));
        final byte[] inputBytes2 = new byte[]{'t', 'e', 's', 't', 0};
        assertEquals("test", ArchiveUtils.toAsciiString(inputBytes2, 0, inputBytes2.length - 1));
        final byte[] inputBytes3 = new byte[]{'t', 'e', 's', 't'};
        assertEquals("test", ArchiveUtils.toAsciiString(inputBytes3));
    }

    @Test
    public void testIsEqual() {
        final byte[] buffer1 = "test".getBytes(StandardCharsets.UTF_8);
        final byte[] buffer2 = new byte[]{'t', 'e', 's', 't'};
        final byte[] buffer3 = "test2".getBytes(StandardCharsets.UTF_8);
        final byte[] buffer4 = new byte[]{'t', 'e', 's', 't'};
        final byte[] buffer5 = new byte[]{'t', 'e', 's', 't'};
        final byte[] buffer6 = new byte[]{'t', 'e', 's', 't', 0};
        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2));
        assertTrue(ArchiveUtils.isEqual(buffer1, buffer4));
        assertFalse(ArchiveUtils.isEqual(buffer1, buffer3));
        assertFalse(ArchiveUtils.isEqual(buffer1, buffer5));
        assertFalse(ArchiveUtils.isEqual(buffer1, buffer6));
        assertTrue(ArchiveUtils.isEqual(buffer1, buffer1));
        assertTrue(ArchiveUtils.isEqualWithNull(buffer1, buffer1, buffer1.length));
        assertTrue(ArchiveUtils.isEqualWithNull(buffer1, buffer1, buffer1.length - 1));
    }

    @Test
    public void testIsEqualWithNull() {
        final byte[] buffer1 = new byte[]{'t', 'e', 's', 't'};
        final byte[] buffer2 = new byte[]{'t', 'e', 's', 't', 0};
        final byte[] buffer3 = new byte[]{'t', 'e', 's', 't'};
        assertTrue(ArchiveUtils.isEqualWithNull(buffer1, buffer1, buffer1.length));
        assertTrue(ArchiveUtils.isEqualWithNull(buffer1, buffer1, buffer1.length - 1));
        assertTrue(ArchiveUtils.isEqualWithNull(buffer1, buffer2, buffer2.length));
        assertFalse(ArchiveUtils.isEqualWithNull(buffer1, buffer3, buffer3.length));
        assertFalse(ArchiveUtils.isEqualWithNull(buffer1, buffer3, buffer3.length + 1));
    }

    @Test
    public void testIsArrayZero() {
        final byte[] a = new byte[0];
        final byte[] a1 = new byte[]{0, 0, 0, 0, 0};
        final byte[] a2 = new byte[]{0, 1, 0, 1, 0};
        assertTrue(ArchiveUtils.isArrayZero(a, 0));
        assertTrue(ArchiveUtils.isArrayZero(a, a.length));
        assertTrue(ArchiveUtils.isArrayZero(a1, a1.length));
        assertFalse(ArchiveUtils.isArrayZero(a2, a2.length));
    }

    @Test
    public void testSanitize() {
        final String s1 = "test";
        final String s2 = "test\u0001";
        final String s3 = "test\u0001\u0002";
        final String s4 = "test\u0001\u0002\u0003";
        assertEquals("test", ArchiveUtils.sanitize(s1));
        assertEquals("test??", ArchiveUtils.sanitize(s2));
        assertEquals("test????", ArchiveUtils.sanitize(s3));
        assertEquals("test?????", ArchiveUtils.sanitize(s4));
    }

    private static void assertArrayEquals(byte[] expected, byte[] actual) {
        assertEquals(expected.length, actual.length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], actual[i]);
        }
    }

    @Test
    public void testIsEqualStream() throws IOException {
        final String data = "test data";
        final byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);

        final ByteArrayOutputStream outputStream1 = new ByteArrayOutputStream();
        outputStream1.write(dataBytes, 0, dataBytes.length);
        final InputStream inputStream1 = new ByteArrayInputStream(outputStream1.toByteArray());

        final ByteArrayOutputStream outputStream2 = new ByteArrayOutputStream();
        outputStream2.write(dataBytes, 0, dataBytes.length);
        final InputStream inputStream2 = new ByteArrayInputStream(outputStream2.toByteArray());

        final TestInputStream testStream1 = new TestInputStream(inputStream1);
        final TestInputStream testStream2 = new TestInputStream(inputStream2);

        assertTrue(ArchiveUtils.isEqual(dataBytes, 0, dataBytes.length, testStream1.getBuffer(), 0, testStream1.size(), false));
        assertTrue(ArchiveUtils.isEqual(dataBytes, 0, dataBytes.length, testStream2.getBuffer(), 0, testStream2.size(), false));
        assertTrue(ArchiveUtils.isEqual(testStream1, testStream2));
    }

    @Test
    public void testIsEqualStreamDifferentLength() throws IOException {
        final String data = "test data";
        final byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);

        final ByteArrayOutputStream outputStream1 = new ByteArrayOutputStream();
        outputStream1.write(dataBytes, 0, dataBytes.length);
        final InputStream inputStream1 = new ByteArrayInputStream(outputStream1.toByteArray());

        final ByteArrayOutputStream outputStream2 = new ByteArrayOutputStream();
        outputStream2.write(Arrays.copyOfRange(dataBytes, 0, dataBytes.length - 1));
        final InputStream inputStream2 = new ByteArrayInputStream(outputStream2.toByteArray());

        final TestInputStream testStream1 = new TestInputStream(inputStream1);
        final TestInputStream testStream2 = new TestInputStream(inputStream2);

        assertFalse(ArchiveUtils.isEqual(dataBytes, 0, dataBytes.length, testStream1.getBuffer(), 0, testStream2.size(), false));
        assertFalse(ArchiveUtils.isEqual(testStream1, testStream2));
    }
}