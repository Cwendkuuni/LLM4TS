import static org.junit.Assert.*;
import org.junit.Test;

public class Base64InputStreamTest {
	
	@Test
	public void testRead() throws Exception {
		InputStream in = new ByteArrayInputStream("Hello, World!".getBytes());
		Base64InputStream base64In = new Base64InputStream(in);
		int readResult = base64In.read();
		assertEquals('H', readResult);
	}
	
	@Test
	public void testReadWithOffsetAndLen() throws Exception {
		InputStream in = new ByteArrayInputStream("Hello, World!".getBytes());
		Base64InputStream base64In = new Base64InputStream(in);
		byte[] buffer = new byte[5];
		int readResult = base64In.read(buffer, 1, 3);
		assertEquals(3, readResult);
		assertEquals(0, buffer[0]);
		assertEquals('H', buffer[1]);
		assertEquals('e', buffer[2]);
		assertEquals('l', buffer[3]);
	}
	
	@Test
	public void testReadWithNullBuffer() throws Exception {
		InputStream in = new ByteArrayInputStream("Hello, World!".getBytes());
		Base64InputStream base64In = new Base64InputStream(in);
		try {
			base64In.read(null, 0, 5);
			fail("Expected NullPointerException");
		} catch (NullPointerException e) {
			assertEquals("byte array parameter is null", e.getMessage());
		}
	}
	
	@Test
	public void testReadWithNegativeOffsetAndLen() throws Exception {
		InputStream in = new ByteArrayInputStream("Hello, World!".getBytes());
		Base64InputStream base64In = new Base64InputStream(in);
		byte[] buffer = new byte[5];
		try {
			base64In.read(buffer, -1, -3);
			fail("Expected IndexOutOfBoundsException");
		} catch (IndexOutOfBoundsException e) {
			assertEquals("offset, len or buffer size are invalid", e.getMessage());
		}
	}
	
	@Test
	public void testReadWithInvalidOffsetAndLen() throws Exception {
		InputStream in = new ByteArrayInputStream("Hello, World!".getBytes());
		Base64InputStream base64In = new Base64InputStream(in);
		byte[] buffer = new byte[5];
		try {
			base64In.read(buffer, 6, 3);
			fail("Expected IndexOutOfBoundsException");
		} catch (IndexOutOfBoundsException e) {
			assertEquals("offset, len or buffer size are invalid", e.getMessage());
		}
	}
	
	@Test
	public void testReadWithEmptyBuffer() throws Exception {
		InputStream in = new ByteArrayInputStream("Hello, World!".getBytes());
		Base64InputStream base64In = new Base64InputStream(in);
		byte[] buffer = new byte[0];
		int readResult = base64In.read(buffer, 0, 0);
		assertEquals(0, readResult);
	}
	
	@Test
	public void testMarkSupported() {
		InputStream in = new ByteArrayInputStream("Hello, World!".getBytes());
		Base64InputStream base64In = new Base64InputStream(in);
		assertFalse(base64In.markSupported());
	}
}