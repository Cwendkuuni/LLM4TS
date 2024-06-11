import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.util.BufferRecycler;
import com.fasterxml.jackson.core.util.TextBuffer;

public class IOContextTest {
	
	private IOContext ioContext;
	private BufferRecycler bufferRecycler;
	private Object sourceRef;
	private boolean managedResource;
	
	@Before
	public void setup() {
		bufferRecycler = new BufferRecycler();
		sourceRef = new Object();
		managedResource = true;
		ioContext = new IOContext(bufferRecycler, sourceRef, managedResource);
	}
	
	@Test
	public void testSetEncoding() {
		JsonEncoding encoding = JsonEncoding.UTF8;
		ioContext.setEncoding(encoding);
		assertEquals(encoding, ioContext.getEncoding());
	}
	
	@Test
	public void testWithEncoding() {
		JsonEncoding encoding = JsonEncoding.UTF16_BE;
		IOContext newIOContext = ioContext.withEncoding(encoding);
		assertEquals(encoding, newIOContext.getEncoding());
	}
	
	@Test
	public void testGetSourceReference() {
		assertEquals(sourceRef, ioContext.getSourceReference());
	}
	
	@Test
	public void testIsResourceManaged() {
		assertEquals(managedResource, ioContext.isResourceManaged());
	}
	
	@Test
	public void testConstructTextBuffer() {
		TextBuffer textBuffer = ioContext.constructTextBuffer();
		assertNotNull(textBuffer);
		
		// Check if the constructed text buffer is recycled and reusable
		TextBuffer newBuffer1 = ioContext.constructTextBuffer();
		TextBuffer newBuffer2 = ioContext.constructTextBuffer();
		assertSame(textBuffer, newBuffer1);
		assertSame(textBuffer, newBuffer2);
	}
	
	@Test
	public void testAllocReadIOBuffer() {
		byte[] readIOBuffer = ioContext.allocReadIOBuffer();
		assertNotNull(readIOBuffer);
		
		// Check if the allocated read IO buffer is recycled and reusable
		byte[] newBuffer1 = ioContext.allocReadIOBuffer();
		byte[] newBuffer2 = ioContext.allocReadIOBuffer();
		assertSame(readIOBuffer, newBuffer1);
		assertSame(readIOBuffer, newBuffer2);
	}
	
	@Test
	public void testAllocReadIOBufferWithMinSize() {
		int minSize = 512;
		byte[] readIOBuffer = ioContext.allocReadIOBuffer(minSize);
		assertNotNull(readIOBuffer);
		assertEquals(minSize, readIOBuffer.length);
		
		// Check if the allocated read IO buffer is recycled and reusable
		byte[] newBuffer1 = ioContext.allocReadIOBuffer(minSize);
		byte[] newBuffer2 = ioContext.allocReadIOBuffer(minSize);
		assertSame(readIOBuffer, newBuffer1);
		assertSame(readIOBuffer, newBuffer2);
	}
	
	@Test
	public void testAllocWriteEncodingBuffer() {
		byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
		assertNotNull(writeEncodingBuffer);
		
		// Check if the allocated write encoding buffer is recycled and reusable
		byte[] newBuffer1 = ioContext.allocWriteEncodingBuffer();
		byte[] newBuffer2 = ioContext.allocWriteEncodingBuffer();
		assertSame(writeEncodingBuffer, newBuffer1);
		assertSame(writeEncodingBuffer, newBuffer2);
	}
	
	@Test
	public void testAllocWriteEncodingBufferWithMinSize() {
		int minSize = 512;
		byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer(minSize);
		assertNotNull(writeEncodingBuffer);
		assertEquals(minSize, writeEncodingBuffer.length);
		
		// Check if the allocated write encoding buffer is recycled and reusable
		byte[] newBuffer1 = ioContext.allocWriteEncodingBuffer(minSize);
		byte[] newBuffer2 = ioContext.allocWriteEncodingBuffer(minSize);
		assertSame(writeEncodingBuffer, newBuffer1);
		assertSame(writeEncodingBuffer, newBuffer2);
	}
	
	@Test
	public void testAllocBase64Buffer() {
		byte[] base64Buffer = ioContext.allocBase64Buffer();
		assertNotNull(base64Buffer);
		
		// Check if the allocated base64 buffer is recycled and reusable
		byte[] newBuffer1 = ioContext.allocBase64Buffer();
		byte[] newBuffer2 = ioContext.allocBase64Buffer();
		assertSame(base64Buffer, newBuffer1);
		assertSame(base64Buffer, newBuffer2);
	}
	
	@Test
	public void testAllocTokenBuffer() {
		char[] tokenBuffer = ioContext.allocTokenBuffer();
		assertNotNull(tokenBuffer);
		
		// Check if the allocated token buffer is recycled and reusable
		char[] newBuffer1 = ioContext.allocTokenBuffer();
		char[] newBuffer2 = ioContext.allocTokenBuffer();
		assertSame(tokenBuffer, newBuffer1);
		assertSame(tokenBuffer, newBuffer2);
	}
	
	@Test
	public void testAllocTokenBufferWithMinSize() {
		int minSize = 512;
		char[] tokenBuffer = ioContext.allocTokenBuffer(minSize);
		assertNotNull(tokenBuffer);
		assertEquals(minSize, tokenBuffer.length);
		
		// Check if the allocated token buffer is recycled and reusable
		char[] newBuffer1 = ioContext.allocTokenBuffer(minSize);
		char[] newBuffer2 = ioContext.allocTokenBuffer(minSize);
		assertSame(tokenBuffer, newBuffer1);
		assertSame(tokenBuffer, newBuffer2);
	}
	
	@Test
	public void testAllocConcatBuffer() {
		char[] concatBuffer = ioContext.allocConcatBuffer();
		assertNotNull(concatBuffer);
		
		// Check if the allocated concat buffer is recycled and reusable
		char[] newBuffer1 = ioContext.allocConcatBuffer();
		char[] newBuffer2 = ioContext.allocConcatBuffer();
		assertSame(concatBuffer, newBuffer1);
		assertSame(concatBuffer, newBuffer2);
	}
	
	@Test
	public void testAllocNameCopyBuffer() {
		int minSize = 512;
		char[] nameCopyBuffer = ioContext.allocNameCopyBuffer(minSize);
		assertNotNull(nameCopyBuffer);
		assertEquals(minSize, nameCopyBuffer.length);
		
		// Check if the allocated name copy buffer is recycled and reusable
		char[] newBuffer1 = ioContext.allocNameCopyBuffer(minSize);
		char[] newBuffer2 = ioContext.allocNameCopyBuffer(minSize);
		assertSame(nameCopyBuffer, newBuffer1);
		assertSame(nameCopyBuffer, newBuffer2);
	}
	
	@Test
	public void testReleaseReadIOBuffer() {
		byte[] buffer = ioContext.allocReadIOBuffer();
		ioContext.releaseReadIOBuffer(buffer);
		assertNull(ioContext._readIOBuffer);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testReleaseReadIOBufferWithWrongBuffer() {
		byte[] buffer = new byte[512];
		ioContext.releaseReadIOBuffer(buffer);
	}
	
	@Test
	public void testReleaseWriteEncodingBuffer() {
		byte[] buffer = ioContext.allocWriteEncodingBuffer();
		ioContext.releaseWriteEncodingBuffer(buffer);
		assertNull(ioContext._writeEncodingBuffer);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testReleaseWriteEncodingBufferWithWrongBuffer() {
		byte[] buffer = new byte[512];
		ioContext.releaseWriteEncodingBuffer(buffer);
	}
	
	@Test
	public void testReleaseBase64Buffer() {
		byte[] buffer = ioContext.allocBase64Buffer();
		ioContext.releaseBase64Buffer(buffer);
		assertNull(ioContext._base64Buffer);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testReleaseBase64BufferWithWrongBuffer() {
		byte[] buffer = new byte[512];
		ioContext.releaseBase64Buffer(buffer);
	}
	
	@Test
	public void testReleaseTokenBuffer() {
		char[] buffer = ioContext.allocTokenBuffer();
		ioContext.releaseTokenBuffer(buffer);
		assertNull(ioContext._tokenCBuffer);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testReleaseTokenBufferWithWrongBuffer() {
		char[] buffer = new char[512];
		ioContext.releaseTokenBuffer(buffer);
	}
	
	@Test
	public void testReleaseConcatBuffer() {
		char[] buffer = ioContext.allocConcatBuffer();
		ioContext.releaseConcatBuffer(buffer);
		assertNull(ioContext._concatCBuffer);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testReleaseConcatBufferWithWrongBuffer() {
		char[] buffer = new char[512];
		ioContext.releaseConcatBuffer(buffer);
	}
	
	@Test
	public void testReleaseNameCopyBuffer() {
		int minSize = 512;
		char[] buffer = ioContext.allocNameCopyBuffer(minSize);
		ioContext.releaseNameCopyBuffer(buffer);
		assertNull(ioContext._nameCopyBuffer);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testReleaseNameCopyBufferWithWrongBuffer() {
		char[] buffer = new char[512];
		ioContext.releaseNameCopyBuffer(buffer);
	}
}