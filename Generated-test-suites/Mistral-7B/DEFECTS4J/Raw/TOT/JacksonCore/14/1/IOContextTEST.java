import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.BufferRecycler;
import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.util.TextBuffer;

public class IOContextTest {

 private IOContext ioContext;
 private BufferRecycler bufferRecycler;

 @Before
 public void setUp() {
 bufferRecycler = new BufferRecycler();
 ioContext = new IOContext(bufferRecycler, new Object(), false);
 }

 @Test
 public void testConstructor() {
 // Test constructor with valid arguments
 assertNotNull(ioContext);
 assertEquals(bufferRecycler, ioContext.getBufferRecycler());
 assertNull(ioContext.getEncoding());
 assertFalse(ioContext.isResourceManaged());
 }

 @Test
 public void testSetEncoding() {
 // Test setting encoding
 JsonEncoding encoding = JsonEncoding.UTF8;
 ioContext.setEncoding(encoding);
 assertEquals(encoding, ioContext.getEncoding());
 }

 @Test
 public void testWithEncoding() {
 // Test withEncoding method
 JsonEncoding encoding = JsonEncoding.UTF8;
 IOContext ioContextWithEncoding = ioContext.withEncoding(encoding);
 assertEquals(encoding, ioContextWithEncoding.getEncoding());
 assertEquals(ioContext, ioContextWithEncoding); // Test if it returns the same instance
 }

 @Test(expected = IllegalStateException.class)
 public void testSetEncodingTwice() {
 // Test setting encoding twice
 JsonEncoding encoding = JsonEncoding.UTF8;
 ioContext.setEncoding(encoding);
 ioContext.setEncoding(encoding);
 }

 @Test
 public void testGetSourceReference() {
 // Test getting source reference
 Object sourceRef = new Object();
 ioContext = new IOContext(bufferRecycler, sourceRef, false);
 assertEquals(sourceRef, ioContext.getSourceReference());
 }

 @Test
 public void testGetEncoding() {
 // Test getting encoding
 JsonEncoding encoding = JsonEncoding.UTF8;
 ioContext.setEncoding(encoding);
 assertEquals(encoding, ioContext.getEncoding());
 }

 @Test
 public void testIsResourceManaged() {
 // Test isResourceManaged
 ioContext = new IOContext(bufferRecycler, new Object(), true);
 assertTrue(ioContext.isResourceManaged());
 ioContext = new IOContext(bufferRecycler, new Object(), false);
 assertFalse(ioContext.isResourceManaged());
 }

 @Test
 public void testConstructTextBuffer() {
 // Test constructTextBuffer method
 TextBuffer textBuffer = ioContext.constructTextBuffer();
 assertNotNull(textBuffer);
 }

 @Test
 public void testAllocReadIOBuffer() {
 // Test allocReadIOBuffer method
 byte[] readIOBuffer = ioContext.allocReadIOBuffer();
 assertNotNull(readIOBuffer);
 ioContext.releaseReadIOBuffer(readIOBuffer);
 }

 @Test(expected = IllegalStateException.class)
 public void testAllocReadIOBufferTwice() {
 // Test allocReadIOBuffer method twice
 byte[] readIOBuffer = ioContext.allocReadIOBuffer();
 byte[] readIOBuffer2 = ioContext.allocReadIOBuffer();
 }

 @Test
 public void testAllocWriteEncodingBuffer() {
 // Test allocWriteEncodingBuffer method
 byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
 assertNotNull(writeEncodingBuffer);
 ioContext.releaseWriteEncodingBuffer(writeEncodingBuffer);
 }

 @Test(expected = IllegalStateException.class)
 public void testAllocWriteEncodingBufferTwice() {
 // Test allocWriteEncodingBuffer method twice
 byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
 byte[] writeEncodingBuffer2 = ioContext.allocWriteEncodingBuffer();
 }

 @Test
 public void testAllocBase64Buffer() {
 // Test allocBase64Buffer method
 byte[] base64Buffer = ioContext.allocBase64Buffer();
 assertNotNull(base64Buffer);
 ioContext.releaseBase64Buffer(base64Buffer);
 }

 @Test
 public void testAllocTokenBuffer() {
 // Test allocTokenBuffer method
 char[] tokenBuffer = ioContext.allocTokenBuffer();
 assertNotNull(tokenBuffer);
 ioContext.releaseTokenBuffer(tokenBuffer);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAllocTokenBufferWithNegativeSize() {
 // Test allocTokenBuffer method with negative size
 ioContext.allocTokenBuffer(-1);
 }

 @Test
 public void testAllocConcatBuffer() {
 // Test allocConcatBuffer method
 char[] concatBuffer = ioContext.allocConcatBuffer();
 assertNotNull(concatBuffer);
 ioContext.releaseConcatBuffer(concatBuffer);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAllocConcatBufferWithNegativeSize() {
 // Test allocConcatBuffer method with negative size
 ioContext.allocConcatBuffer(-1);
 }

 @Test
 public void testAllocNameCopyBuffer() {
 // Test allocNameCopyBuffer method
 char[] nameCopyBuffer = ioContext.allocNameCopyBuffer(10);
 assertNotNull(nameCopyBuffer);
 ioContext.releaseNameCopyBuffer(nameCopyBuffer);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAllocNameCopyBufferWithNegativeSize() {
 // Test allocNameCopyBuffer method with negative size
 ioContext.allocNameCopyBuffer(-1);
 }

 @Test
 public void testReleaseReadIOBuffer() {
 // Test releaseReadIOBuffer method
 byte[] readIOBuffer = ioContext.allocReadIOBuffer();
 ioContext.releaseReadIOBuffer(readIOBuffer);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReleaseReadIOBufferWithNullBuffer() {
 // Test releaseReadIOBuffer method with null buffer
 ioContext.releaseReadIOBuffer(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReleaseWriteEncodingBuffer() {
 // Test releaseWriteEncodingBuffer method with null buffer
 ioContext.releaseWriteEncodingBuffer(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReleaseBase64Buffer() {
 // Test releaseBase64Buffer method with null buffer
 ioContext.releaseBase64Buffer(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReleaseTokenBuffer() {
 // Test releaseTokenBuffer method with null buffer
 ioContext.releaseTokenBuffer(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReleaseConcatBuffer() {
 // Test releaseConcatBuffer method with null buffer
 ioContext.releaseConcatBuffer(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReleaseNameCopyBuffer() {
 // Test releaseNameCopyBuffer method with null buffer
 ioContext.releaseNameCopyBuffer(null);
 }
}