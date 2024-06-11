import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.util.BufferRecycler;
import com.fasterxml.jackson.core.util.TextBuffer;

public class IOContextTest {
	
	@Test
	public void testGetSourceReference() {
		IOContext context = new IOContext(null, "source", true);
		assertEquals("source", context.getSourceReference());
	}

	@Test
	public void testGetEncoding() {
		IOContext context = new IOContext(null, null, true);
		context.setEncoding(JsonEncoding.UTF8);
		assertEquals(JsonEncoding.UTF8, context.getEncoding());
	}

	@Test
	public void testIsResourceManaged() {
		IOContext context = new IOContext(null, null, true);
		assertTrue(context.isResourceManaged());
	}

	@Test
	public void testConstructTextBuffer() {
		IOContext context = new IOContext(null, null, true);
		TextBuffer buffer = context.constructTextBuffer();
		assertNotNull(buffer);
	}

	@Test
	public void testAllocReadIOBuffer() {
		IOContext context = new IOContext(new BufferRecycler(), null, true);
		byte[] buffer = context.allocReadIOBuffer();
		assertNotNull(buffer);
	}

	@Test
	public void testAllocReadIOBufferWithMinSize() {
		IOContext context = new IOContext(new BufferRecycler(), null, true);
		byte[] buffer = context.allocReadIOBuffer(1024);
		assertNotNull(buffer);
		assertEquals(1024, buffer.length);
	}

	@Test
	public void testAllocWriteEncodingBuffer() {
		IOContext context = new IOContext(new BufferRecycler(), null, true);
		byte[] buffer = context.allocWriteEncodingBuffer();
		assertNotNull(buffer);
	}

	@Test
	public void testAllocWriteEncodingBufferWithMinSize() {
		IOContext context = new IOContext(new BufferRecycler(), null, true);
		byte[] buffer = context.allocWriteEncodingBuffer(1024);
		assertNotNull(buffer);
		assertEquals(1024, buffer.length);
	}

	@Test
	public void testAllocBase64Buffer() {
		IOContext context = new IOContext(new BufferRecycler(), null, true);
		byte[] buffer = context.allocBase64Buffer();
		assertNotNull(buffer);
	}

	@Test
	public void testAllocTokenBuffer() {
		IOContext context = new IOContext(new BufferRecycler(), null, true);
		char[] buffer = context.allocTokenBuffer();
		assertNotNull(buffer);
	}

	@Test
	public void testAllocTokenBufferWithMinSize() {
		IOContext context = new IOContext(new BufferRecycler(), null, true);
		char[] buffer = context.allocTokenBuffer(1024);
		assertNotNull(buffer);
		assertEquals(1024, buffer.length);
	}

	@Test
	public void testAllocConcatBuffer() {
		IOContext context = new IOContext(new BufferRecycler(), null, true);
		char[] buffer = context.allocConcatBuffer();
		assertNotNull(buffer);
	}

	@Test
	public void testAllocNameCopyBuffer() {
		IOContext context = new IOContext(new BufferRecycler(), null, true);
		char[] buffer = context.allocNameCopyBuffer(1024);
		assertNotNull(buffer);
		assertEquals(1024, buffer.length);
	}

	@Test
	public void testReleaseReadIOBuffer() {
		IOContext context = new IOContext(new BufferRecycler(), null, true);
		byte[] buffer = context.allocReadIOBuffer();
		context.releaseReadIOBuffer(buffer);
		assertNull(context._readIOBuffer);
	}

	@Test
	public void testReleaseWriteEncodingBuffer() {
		IOContext context = new IOContext(new BufferRecycler(), null, true);
		byte[] buffer = context.allocWriteEncodingBuffer();
		context.releaseWriteEncodingBuffer(buffer);
		assertNull(context._writeEncodingBuffer);
	}

	@Test
	public void testReleaseBase64Buffer() {
		IOContext context = new IOContext(new BufferRecycler(), null, true);
		byte[] buffer = context.allocBase64Buffer();
		context.releaseBase64Buffer(buffer);
		assertNull(context._base64Buffer);
	}

	@Test
	public void testReleaseTokenBuffer() {
		IOContext context = new IOContext(new BufferRecycler(), null, true);
		char[] buffer = context.allocTokenBuffer();
		context.releaseTokenBuffer(buffer);
		assertNull(context._tokenCBuffer);
	}

	@Test
	public void testReleaseConcatBuffer() {
		IOContext context = new IOContext(new BufferRecycler(), null, true);
		char[] buffer = context.allocConcatBuffer();
		context.releaseConcatBuffer(buffer);
		assertNull(context._concatCBuffer);
	}

	@Test
	public void testReleaseNameCopyBuffer() {
		IOContext context = new IOContext(new BufferRecycler(), null, true);
		char[] buffer = context.allocNameCopyBuffer(1024);
		context.releaseNameCopyBuffer(buffer);
		assertNull(context._nameCopyBuffer);
	}

}