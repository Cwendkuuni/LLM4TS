import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.util.BufferRecycler;
import com.fasterxml.jackson.core.util.TextBuffer;
import org.junit.Test;
import static org.junit.Assert.*;

public class IOContextTest {

	@Test
	public void testSetEncoding() {
		IOContext context = new IOContext(null, null, true);
		context.setEncoding(JsonEncoding.UTF8);
		assertEquals(JsonEncoding.UTF8, context.getEncoding());
	}

	@Test
	public void testWithEncoding() {
		IOContext context = new IOContext(null, null, true);
		IOContext contextWithEncoding = context.withEncoding(JsonEncoding.UTF8);
		assertEquals(JsonEncoding.UTF8, contextWithEncoding.getEncoding());
	}

	@Test
	public void testGetSourceReference() {
		Object sourceRef = new Object();
		IOContext context = new IOContext(null, sourceRef, true);
		assertEquals(sourceRef, context.getSourceReference());
	}

	@Test
	public void testIsResourceManaged() {
		IOContext context = new IOContext(null, null, true);
		assertTrue(context.isResourceManaged());
	}

	@Test
	public void testConstructTextBuffer() {
		BufferRecycler br = new BufferRecycler();
		IOContext context = new IOContext(br, null, true);
		TextBuffer textBuffer = context.constructTextBuffer();
		assertNotNull(textBuffer);
	}

	@Test
	public void testAllocReadIOBuffer() {
		BufferRecycler br = new BufferRecycler();
		IOContext context = new IOContext(br, null, true);
		byte[] buffer = context.allocReadIOBuffer();
		assertNotNull(buffer);
	}

	@Test
	public void testAllocReadIOBufferWithMinSize() {
		BufferRecycler br = new BufferRecycler();
		IOContext context = new IOContext(br, null, true);
		int minSize = 1024;
		byte[] buffer = context.allocReadIOBuffer(minSize);
		assertNotNull(buffer);
	}

	@Test
	public void testAllocWriteEncodingBuffer() {
		BufferRecycler br = new BufferRecycler();
		IOContext context = new IOContext(br, null, true);
		byte[] buffer = context.allocWriteEncodingBuffer();
		assertNotNull(buffer);
	}

	@Test
	public void testAllocWriteEncodingBufferWithMinSize() {
		BufferRecycler br = new BufferRecycler();
		IOContext context = new IOContext(br, null, true);
		int minSize = 1024;
		byte[] buffer = context.allocWriteEncodingBuffer(minSize);
		assertNotNull(buffer);
	}

	@Test
	public void testAllocBase64Buffer() {
		BufferRecycler br = new BufferRecycler();
		IOContext context = new IOContext(br, null, true);
		byte[] buffer = context.allocBase64Buffer();
		assertNotNull(buffer);
	}

	@Test
	public void testAllocTokenBuffer() {
		BufferRecycler br = new BufferRecycler();
		IOContext context = new IOContext(br, null, true);
		char[] buffer = context.allocTokenBuffer();
		assertNotNull(buffer);
	}

	@Test
	public void testAllocTokenBufferWithMinSize() {
		BufferRecycler br = new BufferRecycler();
		IOContext context = new IOContext(br, null, true);
		int minSize = 1024;
		char[] buffer = context.allocTokenBuffer(minSize);
		assertNotNull(buffer);
	}

	@Test
	public void testAllocConcatBuffer() {
		BufferRecycler br = new BufferRecycler();
		IOContext context = new IOContext(br, null, true);
		char[] buffer = context.allocConcatBuffer();
		assertNotNull(buffer);
	}

	@Test
	public void testAllocNameCopyBuffer() {
		BufferRecycler br = new BufferRecycler();
		IOContext context = new IOContext(br, null, true);
		int minSize = 1024;
		char[] buffer = context.allocNameCopyBuffer(minSize);
		assertNotNull(buffer);
	}

	@Test
	public void testReleaseReadIOBuffer() {
		BufferRecycler br = new BufferRecycler();
		IOContext context = new IOContext(br, null, true);
		byte[] buffer = context.allocReadIOBuffer();
		context.releaseReadIOBuffer(buffer);
	}

	@Test
	public void testReleaseWriteEncodingBuffer() {
		BufferRecycler br = new BufferRecycler();
		IOContext context = new IOContext(br, null, true);
		byte[] buffer = context.allocWriteEncodingBuffer();
		context.releaseWriteEncodingBuffer(buffer);
	}

	@Test
	public void testReleaseBase64Buffer() {
		BufferRecycler br = new BufferRecycler();
		IOContext context = new IOContext(br, null, true);
		byte[] buffer = context.allocBase64Buffer();
		context.releaseBase64Buffer(buffer);
	}

	@Test
	public void testReleaseTokenBuffer() {
		BufferRecycler br = new BufferRecycler();
		IOContext context = new IOContext(br, null, true);
		char[] buffer = context.allocTokenBuffer();
		context.releaseTokenBuffer(buffer);
	}

	@Test
	public void testReleaseConcatBuffer() {
		BufferRecycler br = new BufferRecycler();
		IOContext context = new IOContext(br, null, true);
		char[] buffer = context.allocConcatBuffer();
		context.releaseConcatBuffer(buffer);
	}

	@Test
	public void testReleaseNameCopyBuffer() {
		BufferRecycler br = new BufferRecycler();
		IOContext context = new IOContext(br, null, true);
		char[] buffer = context.allocNameCopyBuffer();
		context.releaseNameCopyBuffer(buffer);
	}

}