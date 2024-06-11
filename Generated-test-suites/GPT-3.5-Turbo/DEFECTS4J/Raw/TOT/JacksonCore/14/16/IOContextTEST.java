import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.util.BufferRecycler;
import com.fasterxml.jackson.core.util.TextBuffer;

public class IOContextTest {
  
  private IOContext ioContext;
  
  @Before
  public void setUp() {
    BufferRecycler bufferRecycler = new BufferRecycler();
    Object sourceRef = new Object();
    boolean managedResource = true;
    ioContext = new IOContext(bufferRecycler, sourceRef, managedResource);
  }
  
  @After
  public void tearDown() {
    ioContext = null;
  }
  
  @Test
  public void testSetEncoding() {
    JsonEncoding encoding = JsonEncoding.UTF8;
    ioContext.setEncoding(encoding);
    
    assertEquals(ioContext.getEncoding(), encoding);
  }
  
  @Test
  public void testWithEncoding() {
    JsonEncoding encoding = JsonEncoding.UTF16_BE;
    IOContext updatedIOContext = ioContext.withEncoding(encoding);
    
    assertEquals(updatedIOContext.getEncoding(), encoding);
    assertNotEquals(ioContext.getEncoding(), encoding);
  }
  
  @Test
  public void testGetSourceReference() {
    Object sourceRef = ioContext.getSourceReference();
    
    assertNotNull(sourceRef);
  }
  
  @Test
  public void testIsResourceManaged() {
    boolean isResourceManaged = ioContext.isResourceManaged();
    
    assertTrue(isResourceManaged);
  }
  
  @Test
  public void testConstructTextBuffer() {
    TextBuffer textBuffer = ioContext.constructTextBuffer();
    
    assertNotNull(textBuffer);
  }
  
  @Test
  public void testAllocReadIOBuffer() {
    byte[] readIOBuffer = ioContext.allocReadIOBuffer();
    
    assertNotNull(readIOBuffer);
  }
  
  @Test
  public void testAllocReadIOBufferWithSize() {
    int minSize = 1024;
    byte[] readIOBuffer = ioContext.allocReadIOBuffer(minSize);
    
    assertNotNull(readIOBuffer);
    assertTrue(readIOBuffer.length >= minSize);
  }
  
  @Test
  public void testAllocWriteEncodingBuffer() {
    byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
    
    assertNotNull(writeEncodingBuffer);
  }
  
  @Test
  public void testAllocWriteEncodingBufferWithSize() {
    int minSize = 1024;
    byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer(minSize);
    
    assertNotNull(writeEncodingBuffer);
    assertTrue(writeEncodingBuffer.length >= minSize);
  }
  
  @Test
  public void testAllocBase64Buffer() {
    byte[] base64Buffer = ioContext.allocBase64Buffer();
    
    assertNotNull(base64Buffer);
  }
  
  @Test
  public void testAllocTokenBuffer() {
    char[] tokenBuffer = ioContext.allocTokenBuffer();
    
    assertNotNull(tokenBuffer);
  }
  
  @Test
  public void testAllocTokenBufferWithSize() {
    int minSize = 1024;
    char[] tokenBuffer = ioContext.allocTokenBuffer(minSize);
    
    assertNotNull(tokenBuffer);
    assertTrue(tokenBuffer.length >= minSize);
  }
  
  @Test
  public void testAllocConcatBuffer() {
    char[] concatBuffer = ioContext.allocConcatBuffer();
    
    assertNotNull(concatBuffer);
  }
  
  @Test
  public void testAllocNameCopyBuffer() {
    int minSize = 1024;
    char[] nameCopyBuffer = ioContext.allocNameCopyBuffer(minSize);
    
    assertNotNull(nameCopyBuffer);
    assertTrue(nameCopyBuffer.length >= minSize);
  }
  
  @Test
  public void testReleaseReadIOBuffer() {
    byte[] readIOBuffer = ioContext.allocReadIOBuffer();
    
    ioContext.releaseReadIOBuffer(readIOBuffer);
    
    assertNull(ioContext._readIOBuffer);
  }
  
  @Test
  public void testReleaseWriteEncodingBuffer() {
    byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
    
    ioContext.releaseWriteEncodingBuffer(writeEncodingBuffer);
    
    assertNull(ioContext._writeEncodingBuffer);
  }
  
  @Test
  public void testReleaseBase64Buffer() {
    byte[] base64Buffer = ioContext.allocBase64Buffer();
    
    ioContext.releaseBase64Buffer(base64Buffer);
    
    assertNull(ioContext._base64Buffer);
  }
  
  @Test
  public void testReleaseTokenBuffer() {
    char[] tokenBuffer = ioContext.allocTokenBuffer();
    
    ioContext.releaseTokenBuffer(tokenBuffer);
    
    assertNull(ioContext._tokenCBuffer);
  }
  
  @Test
  public void testReleaseConcatBuffer() {
    char[] concatBuffer = ioContext.allocConcatBuffer();
    
    ioContext.releaseConcatBuffer(concatBuffer);
    
    assertNull(ioContext._concatCBuffer);
  }
  
  @Test
  public void testReleaseNameCopyBuffer() {
    int minSize = 1024;
    char[] nameCopyBuffer = ioContext.allocNameCopyBuffer(minSize);
    
    ioContext.releaseNameCopyBuffer(nameCopyBuffer);
    
    assertNull(ioContext._nameCopyBuffer);
  }
}