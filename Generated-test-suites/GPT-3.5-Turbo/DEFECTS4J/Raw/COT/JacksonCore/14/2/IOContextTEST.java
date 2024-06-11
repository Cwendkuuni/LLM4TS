
package com.fasterxml.jackson.core.io;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.util.BufferRecycler;
import com.fasterxml.jackson.core.util.TextBuffer;

public class IOContextTest {
	@Test
	public void testSetEncoding() {
		IOContext context = new IOContext(null, null, false);
		JsonEncoding encoding = JsonEncoding.UTF16_BE;
		context.setEncoding(encoding);
		
		assertSame(encoding, context.getEncoding());
	}
	
	@Test
	public void testWithEncoding() {
		IOContext context = new IOContext(null, null, false);
		JsonEncoding encoding = JsonEncoding.UTF16_BE;
		IOContext newContext = context.withEncoding(encoding);
		
		assertSame(encoding, newContext.getEncoding());
	}
	
	@Test
	public void testConstructTextBuffer() {
		IOContext context = new IOContext(null, null, false);
		TextBuffer buffer = context.constructTextBuffer();
		
		assertNotNull(buffer);
	}
	
	@Test
	public void testAllocReadIOBuffer() {
		IOContext context = new IOContext(new BufferRecycler(), null, false);
		byte[] buffer = context.allocReadIOBuffer();
		
		assertNotNull(buffer);
	}
	
	@Test
	public void testAllocReadIOBufferWithMinSize() {
		IOContext context = new IOContext(new BufferRecycler(), null, false);
		int minSize = 1024;
		byte[] buffer = context.allocReadIOBuffer(minSize);
		
		assertNotNull(buffer);
		assertTrue(buffer.length >= minSize);
	}
	
	@Test
	public void testAllocWriteEncodingBuffer() {
		IOContext context = new IOContext(new BufferRecycler(), null, false);
		byte[] buffer = context.allocWriteEncodingBuffer();
		
		assertNotNull(buffer);
	}
	
	@Test
	public void testAllocWriteEncodingBufferWithMinSize() {
		IOContext context = new IOContext(new BufferRecycler(), null, false);
		int minSize = 1024;
		byte[] buffer = context.allocWriteEncodingBuffer(minSize);
		
		assertNotNull(buffer);
		assertTrue(buffer.length >= minSize);
	}

	@Test
	public void testAllocBase64Buffer() {
		IOContext context = new IOContext(new BufferRecycler(), null, false);
		byte[] buffer = context.allocBase64Buffer();

		assertNotNull(buffer);
	}
	
	@Test
	public void testAllocTokenBuffer() {
		IOContext context = new IOContext(new BufferRecycler(), null, false);
		char[] buffer = context.allocTokenBuffer();
		
		assertNotNull(buffer);
	}
	
	@Test
	public void testAllocTokenBufferWithMinSize() {
		IOContext context = new IOContext(new BufferRecycler(), null, false);
		int minSize = 1024;
		char[] buffer = context.allocTokenBuffer(minSize);
		
		assertNotNull(buffer);
		assertTrue(buffer.length >= minSize);
	}
	
	@Test
	public void testAllocConcatBuffer() {
		IOContext context = new IOContext(new BufferRecycler(), null, false);
		char[] buffer = context.allocConcatBuffer();
		
		assertNotNull(buffer);
	}
	
	@Test
	public void testAllocNameCopyBuffer() {
		IOContext context = new IOContext(new BufferRecycler(), null, false);
		int minSize = 1024;
		char[] buffer = context.allocNameCopyBuffer(minSize);
		
		assertNotNull(buffer);
		assertTrue(buffer.length >= minSize);
	}
	
	@Test
	public void testReleaseReadIOBuffer() {
		IOContext context = new IOContext(new BufferRecycler(), null, false);
		byte[] buffer = context.allocReadIOBuffer();
		
		context.releaseReadIOBuffer(buffer);
		
		assertNull(context._readIOBuffer);
	}
	
	@Test
	public void testReleaseWriteEncodingBuffer() {
		IOContext context = new IOContext(new BufferRecycler(), null, false);
		byte[] buffer = context.allocWriteEncodingBuffer();
		
		context.releaseWriteEncodingBuffer(buffer);
		
		assertNull(context._writeEncodingBuffer);
	}
	
	@Test
	public void testReleaseBase64Buffer() {
		IOContext context = new IOContext(new BufferRecycler(), null, false);
		byte[] buffer = context.allocBase64Buffer();
		
		context.releaseBase64Buffer(buffer);
		
		assertNull(context._base64Buffer);
	}
	
	@Test
	public void testReleaseTokenBuffer() {
		IOContext context = new IOContext(new BufferRecycler(), null, false);
		char[] buffer = context.allocTokenBuffer();
		
		context.releaseTokenBuffer(buffer);
		
		assertNull(context._tokenCBuffer);
	}
	
	@Test
	public void testReleaseConcatBuffer() {
		IOContext context = new IOContext(new BufferRecycler(), null, false);
		char[] buffer = context.allocConcatBuffer();
		
		context.releaseConcatBuffer(buffer);
		
		assertNull(context._concatCBuffer);
	}
	
	@Test
	public void testReleaseNameCopyBuffer() {
		IOContext context = new IOContext(new BufferRecycler(), null, false);
		char[] buffer = context.allocNameCopyBuffer(1024);
		
		context.releaseNameCopyBuffer(buffer);
		
		assertNull(context._nameCopyBuffer);
	}
}
