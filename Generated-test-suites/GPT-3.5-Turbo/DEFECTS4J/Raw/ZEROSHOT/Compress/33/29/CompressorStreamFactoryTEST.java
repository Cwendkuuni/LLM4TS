import static org.junit.Assert.*;
import org.junit.Test;

public class CompressorStreamFactoryTest {
  
  @Test
  public void testCreateCompressorInputStream() {
    CompressorStreamFactory factory = new CompressorStreamFactory();
    
    InputStream in = new InputStream();
    CompressorInputStream compressorInputStream = factory.createCompressorInputStream(in);
    
    assertNotNull(compressorInputStream);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testCreateCompressorInputStreamNullStream() {
    CompressorStreamFactory factory = new CompressorStreamFactory();
    
    InputStream in = null;
    CompressorInputStream compressorInputStream = factory.createCompressorInputStream(in);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testCreateCompressorInputStreamNoMarkSupport() {
    CompressorStreamFactory factory = new CompressorStreamFactory();
    
    InputStream in = new ByteArrayInputStream(new byte[10]);
    CompressorInputStream compressorInputStream = factory.createCompressorInputStream(in);
  }
  
  @Test
  public void testCreateCompressorOutputStream() {
    CompressorStreamFactory factory = new CompressorStreamFactory();
    
    OutputStream out = new OutputStream();
    CompressorOutputStream compressorOutputStream = factory.createCompressorOutputStream("gzip", out);
    
    assertNotNull(compressorOutputStream);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testCreateCompressorOutputStreamNullStream() {
    CompressorStreamFactory factory = new CompressorStreamFactory();
    
    OutputStream out = null;
    CompressorOutputStream compressorOutputStream = factory.createCompressorOutputStream("gzip", out);
  }
  
  @Test
  public void testCreateCompressorOutputStreamUnknownCompressor() {
    CompressorStreamFactory factory = new CompressorStreamFactory();
    
    OutputStream out = new OutputStream();
    CompressorOutputStream compressorOutputStream = factory.createCompressorOutputStream("zip", out);
    
    assertNull(compressorOutputStream);
  }
}