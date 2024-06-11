
import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.apache.commons.codec.binary.BaseNCodecInputStream;

public class BaseNCodecInputStreamTest {

	@Test
	public void testRead() throws IOException {
		// Initialize the BaseNCodecInputStream with an input stream and a BaseNCodec
		BaseNCodecInputStream input = new BaseNCodecInputStream(new ByteArrayInputStream(new byte[] { 65, 66, 67 }), new BaseNCodec(), false);
		
		// Read the first byte, should return 65
		assertEquals(65, input.read());
		
		// Read the second byte, should return 66
		assertEquals(66, input.read());
		
		// Read the third byte, should return 67
		assertEquals(67, input.read());
		
		// Read the fourth byte, should return -1 indicating EOF
		assertEquals(-1, input.read());
		
		// Close the input stream
		input.close();
	}

	@Test
	public void testReadByteArrayIntInt() throws IOException {
		// Initialize the BaseNCodecInputStream with an input stream and a BaseNCodec
		BaseNCodecInputStream input = new BaseNCodecInputStream(new ByteArrayInputStream(new byte[] { 65, 66, 67, 68, 69 }), new BaseNCodec(), false);
		
		// Create a byte array to store the read bytes
		byte[] buffer = new byte[3];
		
		// Attempt to read 3 bytes into the buffer, should return 3
		assertEquals(3, input.read(buffer, 0, 3));
		
		// The buffer should contain the bytes 65, 66, 67
		assertArrayEquals(new byte[] { 65, 66, 67 }, buffer);
		
		// Attempt to read 2 bytes into the buffer starting from index 1, should return 2
		assertEquals(2, input.read(buffer, 1, 2));
		
		// The buffer should contain the bytes 65, 68, 69
		assertArrayEquals(new byte[] { 65, 68, 69 }, buffer);
		
		// Close the input stream
		input.close();
	}

	@Test(expected = NullPointerException.class)
	public void testReadByteArrayIntIntNullBuffer() throws IOException {
		// Initialize the BaseNCodecInputStream with an input stream and a BaseNCodec
		BaseNCodecInputStream input = new BaseNCodecInputStream(new ByteArrayInputStream(new byte[] { 65, 66, 67 }), new BaseNCodec(), false);
		
		// Attempt to read bytes into a null buffer, should throw a NullPointerException
		input.read(null, 0, 3);
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testReadByteArrayIntIntInvalidOffset() throws IOException {
		// Initialize the BaseNCodecInputStream with an input stream and a BaseNCodec
		BaseNCodecInputStream input = new BaseNCodecInputStream(new ByteArrayInputStream(new byte[] { 65, 66, 67 }), new BaseNCodec(), false);
		
		// Create a byte array to store the read bytes
		byte[] buffer = new byte[3];
		
		// Attempt to read bytes into the buffer with an invalid offset, should throw an IndexOutOfBoundsException
		input.read(buffer, -1, 3);
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testReadByteArrayIntIntInvalidLength() throws IOException {
		// Initialize the BaseNCodecInputStream with an input stream and a BaseNCodec
		BaseNCodecInputStream input = new BaseNCodecInputStream(new ByteArrayInputStream(new byte[] { 65, 66, 67 }), new BaseNCodec(), false);
		
		// Create a byte array to store the read bytes
		byte[] buffer = new byte[3];
		
		// Attempt to read bytes into the buffer with an invalid length, should throw an IndexOutOfBoundsException
		input.read(buffer, 0, -1);
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testReadByteArrayIntIntInvalidBufferSize() throws IOException {
		// Initialize the BaseNCodecInputStream with an input stream and a BaseNCodec
		BaseNCodecInputStream input = new BaseNCodecInputStream(new ByteArrayInputStream(new byte[] { 65, 66, 67 }), new BaseNCodec(), false);
		
		// Create a byte array to store the read bytes
		byte[] buffer = new byte[3];
		
		// Attempt to read bytes into the buffer with an invalid buffer size, should throw an IndexOutOfBoundsException
		input.read(buffer, 0, 4);
	}

	@Test
	public void testMarkSupported() {
		// Initialize the BaseNCodecInputStream with an input stream and a BaseNCodec
		BaseNCodecInputStream input = new BaseNCodecInputStream(new ByteArrayInputStream(new byte[] { 65, 66, 67 }), new BaseNCodec(), false);
		
		// Check if mark is supported
		assertFalse(input.markSupported());
	}

	@Test
	public void testAvailable() throws IOException {
		// Initialize the BaseNCodecInputStream with an input stream and a BaseNCodec
		BaseNCodecInputStream input = new BaseNCodecInputStream(new ByteArrayInputStream(new byte[] { 65, 66, 67 }), new BaseNCodec(), false);
		
		// The input stream is always available, should return 1
		assertEquals(1, input.available());
		
		// Close the input stream
		input.close();
	}

}
