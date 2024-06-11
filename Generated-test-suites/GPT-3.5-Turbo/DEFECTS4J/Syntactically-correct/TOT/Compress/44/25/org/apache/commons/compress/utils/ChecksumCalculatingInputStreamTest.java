import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;
import org.junit.Test;

public class ChecksumCalculatingInputStreamTest {

	@Test
	public void testRead() throws IOException {
		// Create a mock InputStream and Checksum
		InputStream in = mock(InputStream.class);
		Checksum checksum = mock(Checksum.class);
		
		// Configure the InputStream to return a specific byte value
		when(in.read()).thenReturn(42);
		
		// Create the instance of ChecksumCalculatingInputStream with the mock objects
		ChecksumCalculatingInputStream testStream = new ChecksumCalculatingInputStream(checksum, in);
		
		// Call the read() method of the testStream
		int result = testStream.read();
		
		// Verify that the read method of the InputStream was called once
		verify(in, times(1)).read();
		
		// Verify that the update method of the Checksum was called once with the expected value
		verify(checksum, times(1)).update(42);
		
		// Assert that the result is the expected byte value
		assertEquals(42, result);
	}

	@Test
	public void testReadWithByteArray() throws IOException {
		// Create a mock InputStream and Checksum
		InputStream in = mock(InputStream.class);
		Checksum checksum = mock(Checksum.class);
		
		// Create a byte array to be read
		byte[] byteArray = {1, 2, 3, 4, 5};
		
		// Configure the InputStream to return the byte array
		when(in.read(byteArray)).thenReturn(byteArray.length);
		
		// Create the instance of ChecksumCalculatingInputStream with the mock objects
		ChecksumCalculatingInputStream testStream = new ChecksumCalculatingInputStream(checksum, in);
		
		// Call the read(byte[] b) method of the testStream
		int result = testStream.read(byteArray);
		
		// Verify that the read(byte[] b) method of the InputStream was called once with the same byte array
		verify(in, times(1)).read(byteArray);
		
		// Verify that the update method of the Checksum was called once with the byte array
		verify(checksum, times(1)).update(byteArray, 0, byteArray.length);
		
		// Assert that the result is the number of bytes read
		assertEquals(byteArray.length, result);
	}

	@Test
	public void testReadWithByteArrayAndOffset() throws IOException {
		// Create a mock InputStream and Checksum
		InputStream in = mock(InputStream.class);
		Checksum checksum = mock(Checksum.class);
		
		// Create a byte array to be read and an offset value
		byte[] byteArray = {1, 2, 3, 4, 5};
		int offset = 1;
		
		// Configure the InputStream to return the byte array
		when(in.read(byteArray, offset, byteArray.length - offset)).thenReturn(byteArray.length - offset);
		
		// Create the instance of ChecksumCalculatingInputStream with the mock objects
		ChecksumCalculatingInputStream testStream = new ChecksumCalculatingInputStream(checksum, in);
		
		// Call the read(byte[] b, int off, int len) method of the testStream
		int result = testStream.read(byteArray, offset, byteArray.length - offset);
		
		// Verify that the read(byte[] b, int off, int len) method of the InputStream was called once with the same byte array and offset
		verify(in, times(1)).read(byteArray, offset, byteArray.length - offset);
		
		// Verify that the update method of the Checksum was called once with the byte array and offset
		verify(checksum, times(1)).update(byteArray, offset, byteArray.length - offset);
		
		// Assert that the result is the number of bytes read
		assertEquals(byteArray.length - offset, result);
	}

	@Test
	public void testSkip() throws IOException {
		// Create a mock InputStream and Checksum
		InputStream in = mock(InputStream.class);
		Checksum checksum = mock(Checksum.class);
		
		// Configure the InputStream to return a byte value larger than or equal to 0
		when(in.read()).thenReturn(42);
		
		// Create the instance of ChecksumCalculatingInputStream with the mock objects
		ChecksumCalculatingInputStream testStream = new ChecksumCalculatingInputStream(checksum, in);
		
		// Call the skip() method of the testStream
		long result = testStream.skip(1);
		
		// Verify that the read() method of the InputStream was called once
		verify(in, times(1)).read();
		
		// Verify that the update method of the Checksum was called once with the expected byte value
		verify(checksum, times(1)).update(42);
		
		// Assert that the result is 1
		assertEquals(1, result);
	}

	@Test
	public void testGetValue() {
		// Create a mock Checksum
		Checksum checksum = mock(Checksum.class);
		
		// Configure the Checksum to return a specific value
		when(checksum.getValue()).thenReturn(12345L);
		
		// Create an instance of ChecksumCalculatingInputStream with the mock Checksum and a ByteArrayInputStream
		ChecksumCalculatingInputStream testStream = new ChecksumCalculatingInputStream(checksum,
				new ByteArrayInputStream(new byte[0]));
		
		// Call the getValue() method of the testStream
		long result = testStream.getValue();
		
		// Verify that the getValue() method of the Checksum was called once
		verify(checksum, times(1)).getValue();
		
		// Assert that the result is the expected value
		assertEquals(12345L, result);
	}
}