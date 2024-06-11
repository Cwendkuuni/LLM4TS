import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.IOException;

public class ArArchiveInputStreamTest {

	@Test
	public void testGetNextArEntry() throws IOException {
		// Test case 1: Input stream is empty
		byte[] emptyArInputStream = new byte[0];
		InputStream emptyInput = new ByteArrayInputStream(emptyArInputStream);
		ArArchiveInputStream emptyArInputStreamObj = new ArArchiveInputStream(emptyInput);
		assertNull(emptyArInputStreamObj.getNextArEntry());

		// Test case 2: Read a valid AR entry
		byte[] arEntry = new byte[64];
		InputStream arInput = new ByteArrayInputStream(arEntry);
		ArArchiveInputStream arInputStreamObj = new ArArchiveInputStream(arInput);
		ArArchiveEntry arEntryObj = arInputStreamObj.getNextArEntry();
		assertNotNull(arEntryObj);
		assertEquals(64, arEntryObj.getLength());

		// Test case 3: Read an invalid AR entry
		byte[] invalidArEntry = new byte[10];
		InputStream invalidArInput = new ByteArrayInputStream(invalidArEntry);
		ArArchiveInputStream invalidArInputStreamObj = new ArArchiveInputStream(invalidArInput);
		try {
			invalidArInputStreamObj.getNextArEntry();
			fail("Expected IOException");
		} catch (IOException e) {
			assertEquals("failed to read header", e.getMessage());
		}
	}

	@Test
	public void testGetNextEntry() throws IOException {
		// Test case 1: Input stream is empty
		byte[] emptyArInputStream = new byte[0];
		InputStream emptyInput = new ByteArrayInputStream(emptyArInputStream);
		ArArchiveInputStream emptyArInputStreamObj = new ArArchiveInputStream(emptyInput);
		assertNull(emptyArInputStreamObj.getNextEntry());

		// Test case 2: Read a valid entry
		byte[] arEntry = new byte[64];
		InputStream arInput = new ByteArrayInputStream(arEntry);
		ArArchiveInputStream arInputStreamObj = new ArArchiveInputStream(arInput);
		ArchiveEntry arEntryObj = arInputStreamObj.getNextEntry();
		assertNotNull(arEntryObj);
		assertTrue(arEntryObj instanceof ArArchiveEntry);

		// Test case 3: Read an invalid entry
		byte[] invalidArEntry = new byte[10];
		InputStream invalidArInput = new ByteArrayInputStream(invalidArEntry);
		ArArchiveInputStream invalidArInputStreamObj = new ArArchiveInputStream(invalidArInput);
		try {
			invalidArInputStreamObj.getNextEntry();
			fail("Expected IOException");
		} catch (IOException e) {
			assertEquals("failed to read header", e.getMessage());
		}
	}

	@Test
	public void testClose() throws IOException {
		byte[] arEntry = new byte[64];
		InputStream arInput = new ByteArrayInputStream(arEntry);
		ArArchiveInputStream arInputStreamObj = new ArArchiveInputStream(arInput);
		assertFalse(arInputStreamObj.isClosed());
		arInputStreamObj.close();
		assertTrue(arInputStreamObj.isClosed());
		arInputStreamObj.close(); // Make sure close() can be called multiple times without raising exception
	}

	@Test
	public void testRead() throws IOException {
		byte[] arEntry = new byte[64];
		InputStream arInput = new ByteArrayInputStream(arEntry);
		ArArchiveInputStream arInputStreamObj = new ArArchiveInputStream(arInput);
		assertEquals(0, arInputStreamObj.read());

		byte[] arEntry2 = new byte[128];
		InputStream arInput2 = new ByteArrayInputStream(arEntry2);
		ArArchiveInputStream arInputStreamObj2 = new ArArchiveInputStream(arInput2);
		byte[] buffer = new byte[64];
		assertEquals(64, arInputStreamObj2.read(buffer));
		assertEquals(64, arInputStreamObj2.read(buffer)); // Reading again should return 64 again
		assertEquals(-1, arInputStreamObj2.read(buffer)); // Reading beyond the end of stream should return -1

		byte[] arEntry3 = new byte[0];
		InputStream arInput3 = new ByteArrayInputStream(arEntry3);
		ArArchiveInputStream arInputStreamObj3 = new ArArchiveInputStream(arInput3);
		assertEquals(-1, arInputStreamObj3.read());
	}

	@Test
	public void testReadWithOffsetAndLength() throws IOException {
		byte[] arEntry = new byte[64];
		InputStream arInput = new ByteArrayInputStream(arEntry);
		ArArchiveInputStream arInputStreamObj = new ArArchiveInputStream(arInput);
		byte[] buffer = new byte[32];
		assertEquals(32, arInputStreamObj.read(buffer, 0, 32));
		assertEquals(32, arInputStreamObj.read(buffer, 0, 32)); // Reading again should return 32 again
		assertEquals(-1, arInputStreamObj.read(buffer, 0, 32)); // Reading beyond the end of stream should return -1

		byte[] arEntry2 = new byte[0];
		InputStream arInput2 = new ByteArrayInputStream(arEntry2);
		ArArchiveInputStream arInputStreamObj2 = new ArArchiveInputStream(arInput2);
		assertEquals(-1, arInputStreamObj2.read(buffer, 0, 32));
	}

	@Test
	public void testMatches() {
		byte[] signature = new byte[8];
		assertTrue(ArArchiveInputStream.matches(signature, 8));

		byte[] signature2 = { 0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a };
		assertTrue(ArArchiveInputStream.matches(signature2, 8));

		byte[] signature3 = { 0x21, 0x3c, 0x61 };
		assertFalse(ArArchiveInputStream.matches(signature3, 3));

		byte[] signature4 = { 0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a, 0x00 };
		assertTrue(ArArchiveInputStream.matches(signature4, 9));
	}

}