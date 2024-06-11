import static org.junit.Assert.*;
import org.junit.Test;

public class IOContextTest {
  
  @Test
  public void testSetEncoding() {
    IOContext context = new IOContext(new BufferRecycler(), new Object(), true);
    
    context.setEncoding(JsonEncoding.UTF8);
    
    assertEquals(JsonEncoding.UTF8, context.getEncoding());
  }
  
  @Test
  public void testWithEncoding() {
    IOContext context = new IOContext(new BufferRecycler(), new Object(), true);
    
    IOContext newContext = context.withEncoding(JsonEncoding.UTF8);
    
    assertEquals(JsonEncoding.UTF8, newContext.getEncoding());
    assertNotSame(context, newContext);
  }
  
  @Test
  public void testConstructTextBuffer() {
    IOContext context = new IOContext(new BufferRecycler(), new Object(), true);
    
    TextBuffer buffer = context.constructTextBuffer();
    
    assertNotNull(buffer);
  }
  
  @Test
  public void testAllocReadIOBuffer() {
    IOContext context = new IOContext(new BufferRecycler(), new Object(), true);
    
    byte[] buffer = context.allocReadIOBuffer();
    
    assertNotNull(buffer);
  }
  
  @Test
  public void testAllocReadIOBufferWithMinSize() {
    IOContext context = new IOContext(new BufferRecycler(), new Object(), true);
    
    byte[] buffer = context.allocReadIOBuffer(100);
    
    assertNotNull(buffer);
    assertTrue(buffer.length >= 100);
  }
  
  @Test
  public void testAllocWriteEncodingBuffer() {
    IOContext context = new IOContext(new BufferRecycler(), new Object(), true);
    
    byte[] buffer = context.allocWriteEncodingBuffer();
    
    assertNotNull(buffer);
  }
  
  @Test
  public void testAllocWriteEncodingBufferWithMinSize() {
    IOContext context = new IOContext(new BufferRecycler(), new Object(), true);
    
    byte[] buffer = context.allocWriteEncodingBuffer(100);
    
    assertNotNull(buffer);
    assertTrue(buffer.length >= 100);
  }
  
  @Test
  public void testAllocBase64Buffer() {
    IOContext context = new IOContext(new BufferRecycler(), new Object(), true);
    
    byte[] buffer = context.allocBase64Buffer();
    
    assertNotNull(buffer);
  }
  
  @Test
  public void testAllocTokenBuffer() {
    IOContext context = new IOContext(new BufferRecycler(), new Object(), true);
    
    char[] buffer = context.allocTokenBuffer();
    
    assertNotNull(buffer);
  }
  
  @Test
  public void testAllocTokenBufferWithMinSize() {
    IOContext context = new IOContext(new BufferRecycler(), new Object(), true);
    
    char[] buffer = context.allocTokenBuffer(100);
    
    assertNotNull(buffer);
    assertTrue(buffer.length >= 100);
  }
  
  @Test
  public void testAllocConcatBuffer() {
    IOContext context = new IOContext(new BufferRecycler(), new Object(), true);
    
    char[] buffer = context.allocConcatBuffer();
    
    assertNotNull(buffer);
  }
  
  @Test
  public void testAllocNameCopyBufferWithMinSize() {
    IOContext context = new IOContext(new BufferRecycler(), new Object(), true);
    
    char[] buffer = context.allocNameCopyBuffer(100);
    
    assertNotNull(buffer);
    assertTrue(buffer.length >= 100);
  }
  
  @Test
  public void testReleaseReadIOBuffer() {
    IOContext context = new IOContext(new BufferRecycler(), new Object(), true);
    
    byte[] buffer = context.allocReadIOBuffer();
    
    context.releaseReadIOBuffer(buffer);
    
    assertNull(context._readIOBuffer);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testReleaseReadIOBufferWithWrongBuffer() {
    IOContext context = new IOContext(new BufferRecycler(), new Object(), true);
    
    byte[] buffer1 = new byte[100];
    byte[] buffer2 = new byte[200];
    
    context.releaseReadIOBuffer(buffer2);
  }
  
  @Test
  public void testReleaseWriteEncodingBuffer() {
    IOContext context = new IOContext(new BufferRecycler(), new Object(), true);
    
    byte[] buffer = context.allocWriteEncodingBuffer();
    
    context.releaseWriteEncodingBuffer(buffer);
    
    assertNull(context._writeEncodingBuffer);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testReleaseWriteEncodingBufferWithWrongBuffer() {
    IOContext context = new IOContext(new BufferRecycler(), new Object(), true);
    
    byte[] buffer1 = new byte[100];
    byte[] buffer2 = new byte[200];
    
    context.releaseWriteEncodingBuffer(buffer2);
  }
  
  @Test
  public void testReleaseBase64Buffer() {
    IOContext context = new IOContext(new BufferRecycler(), new Object(), true);
    
    byte[] buffer = context.allocBase64Buffer();
    
    context.releaseBase64Buffer(buffer);
    
    assertNull(context._base64Buffer);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testReleaseBase64BufferWithWrongBuffer() {
    IOContext context = new IOContext(new BufferRecycler(), new Object(), true);
    
    byte[] buffer1 = new byte[100];
    byte[] buffer2 = new byte[200];
    
    context.releaseBase64Buffer(buffer2);
  }
  
  @Test
  public void testReleaseTokenBuffer() {
    IOContext context = new IOContext(new BufferRecycler(), new Object(), true);
    
    char[] buffer = context.allocTokenBuffer();
    
    context.releaseTokenBuffer(buffer);
    
    assertNull(context._tokenCBuffer);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testReleaseTokenBufferWithWrongBuffer() {
    IOContext context = new IOContext(new BufferRecycler(), new Object(), true);
    
    char[] buffer1 = new char[100];
    char[] buffer2 = new char[200];
    
    context.releaseTokenBuffer(buffer2);
  }
  
  @Test
  public void testReleaseConcatBuffer() {
    IOContext context = new IOContext(new BufferRecycler(), new Object(), true);
    
    char[] buffer = context.allocConcatBuffer();
    
    context.releaseConcatBuffer(buffer);
    
    assertNull(context._concatCBuffer);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testReleaseConcatBufferWithWrongBuffer() {
    IOContext context = new IOContext(new BufferRecycler(), new Object(), true);
    
    char[] buffer1 = new char[100];
    char[] buffer2 = new char[200];
    
    context.releaseConcatBuffer(buffer2);
  }
  
  @Test
  public void testReleaseNameCopyBuffer() {
    IOContext context = new IOContext(new BufferRecycler(), new Object(), true);
    
    char[] buffer = context.allocNameCopyBuffer(100);
    
    context.releaseNameCopyBuffer(buffer);
    
    assertNull(context._nameCopyBuffer);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testReleaseNameCopyBufferWithWrongBuffer() {
    IOContext context = new IOContext(new BufferRecycler(), new Object(), true);
    
    char[] buffer1 = new char[100];
    char[] buffer2 = new char[200];
    
    context.releaseNameCopyBuffer(buffer2);
  }
}