import static org.junit.Assert.*;
import org.junit.Test;

public class CodersTest {
  
  @Test
  public void testAddDecoder_Copy() {
    InputStream is = null;
    Coder coder = null;
    byte[] password = null;

    try {
      is = new InputStream();
      coder = new Coder();
      password = new byte[]{};
    
      InputStream result = Coders.addDecoder(is, coder, password);
      
      //Assert the returned InputStream is the same as the input object
      assertSame(result, is);
    
    } catch (IOException e) {
      fail("Exception thrown: " + e);
    }
    
    //Assert that the expected Exception is thrown when an unsupported compression method is used
    try {
      is = new InputStream();
      coder = new Coder();
      password = new byte[]{};

      Coders.addDecoder(is, coder, password);
      fail("IOException not thrown");
      
    } catch (IOException e) {
      assertEquals("Unsupported compression method []", e.getMessage());
    }
  }
  
  @Test
  public void testAddDecoder_LZMA() {
    //Test with minimum dictSize
    //...
    
    //Test with maximum dictSize
    //...
    
    //Test with dictSize over maximum
    //...
    
    //Test with valid properties and valid InputStream
    //...
  }
  
  @Test
  public void testAddDecoder_Deflate() {
    //Test with valid properties and valid InputStream
    //...
  }

  @Test
  public void testAddDecoder_BZIP2() {
    //Test with valid InputStream
    //...

    //Test with invalid InputStream
    //...
  }

  @Test
  public void testAddDecoder_AES256SHA256() {
    //Test with valid properties, valid passwordBytes and valid InputStream
    //...

    //Test with valid properties, valid passwordBytes but invalid InputStream
    //...

    //Test with valid properties but null passwordBytes
    //...

    //Test with invalid properties
    //...
  }

  @Test
  public void testAddEncoder() {
    OutputStream out = null;
    SevenZMethod method = null;
    byte[] password = null;

    try {
      out = new OutputStream();
      method = new SevenZMethod();
      password = new byte[]{};

      OutputStream result = Coders.addEncoder(out, method, password);

      //Assert the returned OutputStream is the same as the input object
      assertSame(result, out);

    } catch (IOException e) {
      fail("Exception thrown: " + e);
    }

    //Assert that the expected Exception is thrown when an unsupported compression method is used
    try {
      out = new OutputStream();
      method = new SevenZMethod();
      password = new byte[]{};

      Coders.addEncoder(out, method, password);
      fail("IOException not thrown");

    } catch (IOException e) {
      assertEquals("Unsupported compression method " + method, e.getMessage());
    }
  }

}