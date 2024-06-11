
package com.fasterxml.jackson.core.io;

import static org.junit.Assert.*;
import org.junit.Test;

public class IOContextTest {
	
	@Test
	public void testConstructor() {
		BufferRecycler br = new BufferRecycler();
		Object sourceRef = new Object();
		boolean managedResource = true;
		IOContext ioContext = new IOContext(br, sourceRef, managedResource);
		
		assertEquals(br, ioContext._bufferRecycler);
		assertEquals(sourceRef, ioContext._sourceRef);
		assertTrue(ioContext._managedResource);
	}
	
	@Test
	public void testSetEncoding() {
		IOContext ioContext = new IOContext(new BufferRecycler(), new Object(), true);
		JsonEncoding enc = JsonEncoding.UTF8;
		ioContext.setEncoding(enc);
		
		assertEquals(enc, ioContext._encoding);
	}
	
	@Test
	public void testWithEncoding() {
		IOContext ioContext = new IOContext(new BufferRecycler(), new Object(), true);
		JsonEncoding enc = JsonEncoding.UTF16_BE;
		IOContext newContext = ioContext.withEncoding(enc);
		
		assertEquals(enc, newContext._encoding);
	}
	
	@Test
	public void testGetSourceReference() {
		Object sourceRef = new Object();
		IOContext ioContext = new IOContext(new BufferRecycler(), sourceRef, true);
		
		assertEquals(sourceRef, ioContext.getSourceReference());
	}
	
	@Test
	public void testGetEncoding() {
		IOContext ioContext = new IOContext(new BufferRecycler(), new Object(), true);
		JsonEncoding enc = JsonEncoding.UTF8;
		ioContext.setEncoding(enc);
		
		assertEquals(enc, ioContext.getEncoding());
	}
	
	@Test
	public void testIsResourceManaged() {
		IOContext ioContext = new IOContext(new BufferRecycler(), new Object(), true);
		
		assertTrue(ioContext.isResourceManaged());
	}
	
	@Test
	public void testConstructTextBuffer() {
		IOContext ioContext = new IOContext(new BufferRecycler(), new Object(), true);
		TextBuffer textBuffer = ioContext.constructTextBuffer();
		
		assertNotNull(textBuffer);
	}
}
