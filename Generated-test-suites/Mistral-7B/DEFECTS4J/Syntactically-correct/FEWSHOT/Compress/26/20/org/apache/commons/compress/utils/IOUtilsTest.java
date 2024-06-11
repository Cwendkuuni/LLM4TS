package org.apache.commons.compress.utils;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import org.junit.Test;

public class IOUtilsTest {

 @Test
 public void testCopy() throws IOException {
 InputStream input = mock(InputStream.class);
 OutputStream output = mock(OutputStream.class);
 when(input.read()).thenReturn(42);
 when(input.read(new byte[1])).thenReturn(-1);
 when(output.write(new byte[1])).thenAnswer(invocation -> invocation.getArguments()[0][0]);

 long actual = IOUtils.copy(input, output);
 byte[] expected = new byte[] { 42, 42 };
 byte[] actualArray = new byte[2];
 IOUtils.toByteArray(input, actualArray);

 assertEquals(2L, actual);
 assertArrayEquals(expected, actualArray);
 }

 @Test
 public void testCopyWithBuffersize() throws IOException {
 InputStream input = new ByteArrayInputStream("abcdefghijklmnopqrstuvwxyz".getBytes());
 OutputStream output = new ByteArrayOutputStream();
 long actual = IOUtils.copy(input, output, 5);
 byte[] expected = "abcde".getBytes();
 byte[] actualArray = output.toByteArray();

 assertEquals(5L, actual);
 assertArrayEquals(expected, actualArray);
 }

 @Test(expected = NullPointerException.class)
 public void testCopyNullInput() {
 OutputStream output = mock(OutputStream.class);
 InputStream input = null;
 IOUtils.copy(input, output);
 }

 @Test(expected = NullPointerException.class)
 public void testCopyNullOutput() {
 InputStream input = mock(InputStream.class);
 OutputStream output = null;
 IOUtils.copy(input, output);
 }

 @Test
 public void testSkip() throws IOException {
 InputStream input = mock(InputStream.class);
 when(input.skip(2147483647L)).thenThrow(new IOException());
 when(input.skip(2147483646L)).thenReturn(2147483645L);
 when(input.skip(10L)).thenReturn(10L);
 when(input.skip(0L)).thenReturn(0L);

 long actual = IOUtils.skip(input, 4294967295L);

 assertEquals(4294967285L, actual);
 }

 @Test
 public void testReadFully() throws IOException {
 InputStream input = new ByteArrayInputStream("abcdefghijklmnopqrstuvwxyz".getBytes());
 byte[] b = new byte[22];
 byte[] expected = new byte[] { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v' };
 byte[] actual = new byte[22];
 int actualLength = IOUtils.readFully(input, actual);

 assertEquals(22, actualLength);
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testReadFullyPartialFill() throws IOException {
 InputStream input = new ByteArrayInputStream("abcdefghijklmnopqrstuvwxyz".getBytes());
 byte[] b = new byte[12];
 byte[] actual = new byte[12];
 int actualLength = IOUtils.readFully(input, b, 0, b.length);

 assertEquals(11, actualLength);
 byte[] expected = new byte[] { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k' };
 assertArrayEquals(expected, actual);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testReadFullyNegativeOffset() throws IOException {
 InputStream input = mock(InputStream.class);
 byte[] b = new byte[5];
 byte[] actual = new byte[5];
 IOUtils.readFully(input, actual, -1, 5);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testReadFullyNegativeLength() throws IOException {
 InputStream input = mock(InputStream.class);
 byte[] b = new byte[5];
 byte[] actual = new byte[5];
 IOUtils.readFully(input, actual, 0, -5);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testReadFullyTooLongArray() throws IOException {
 InputStream input = mock(InputStream.class);
 byte[] b = new byte[5];
 byte[] actual = new byte[6];
 IOUtils.readFully(input, actual, 0, b.length);
 }

 @Test
 public void testToByteArray() throws IOException {
 InputStream input = new ByteArrayInputStream("abcdefghijklmnopqrstuvwxyz".getBytes());
 byte[] expected = "abcdefghijklmnopqrstuvwxyz".getBytes();
 byte[] actual = IOUtils.toByteArray(input);

 assertArrayEquals(expected, actual);
 }

 @Test
 public void testCloseQuietly() throws IOException {
 InputStream input = mock(InputStream.class);
 Closeable closeable = mock(Closeable.class);
 when(input.close()).thenThrow(new IOException());
 when(closeable.close()).thenThrow(new IOException());

 IOUtils.closeQuietly(input);
 IOUtils.closeQuietly(closeable);
 }
}