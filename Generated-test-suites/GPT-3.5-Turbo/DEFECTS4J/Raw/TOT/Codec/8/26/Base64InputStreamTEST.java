import org.junit.*;
import static org.junit.Assert.*;

public class Base64InputStreamTest {

  @Test
  public void testRead() {
    // create input stream with sample data
    byte[] sampleData = {72, 101, 108, 108, 111, 33}; // "Hello!" in ASCII
    ByteArrayInputStream inputStream = new ByteArrayInputStream(sampleData);
    Base64InputStream base64InputStream = new Base64InputStream(inputStream);
    
    try {
      // read one byte at a time
      assertEquals(72, base64InputStream.read());
      assertEquals(101, base64InputStream.read());
      assertEquals(108, base64InputStream.read());
      assertEquals(108, base64InputStream.read());
      assertEquals(111, base64InputStream.read());
      assertEquals(33, base64InputStream.read());
      
      // test when EOF is reached
      assertEquals(-1, base64InputStream.read());
    } catch (IOException e) {
      fail("Exception should not be thrown");
    }
  }
  
  @Test
  public void testReadWithOffsetAndLength() {
    // create input stream with sample data
    byte[] sampleData = {72, 101, 108, 108, 111, 33}; // "Hello!" in ASCII
    ByteArrayInputStream inputStream = new ByteArrayInputStream(sampleData);
    Base64InputStream base64InputStream = new Base64InputStream(inputStream);
    
    try {
      byte[] buffer = new byte[3];
      
      // read three bytes into the buffer starting at offset 0
      assertEquals(3, base64InputStream.read(buffer, 0, 3));
      assertArrayEquals(new byte[]{72, 101, 108}, buffer);
      
      // read three bytes into the buffer starting at offset 1
      assertEquals(3, base64InputStream.read(buffer, 1, 3));
      assertArrayEquals(new byte[]{72, 111, 33}, buffer);
      
      // read remaining byte into the buffer starting at offset 2
      assertEquals(1, base64InputStream.read(buffer, 2, 3));
      assertArrayEquals(new byte[]{72, 111, 33}, buffer);
      
      // test when EOF is reached
      assertEquals(-1, base64InputStream.read(buffer, 0, 3));
    } catch (IOException e) {
      fail("Exception should not be thrown");
    }
  }
  
  @Test
  public void testReadWithZeroLength() {
    // create input stream with sample data
    byte[] sampleData = {72, 101, 108, 108, 111, 33}; // "Hello!" in ASCII
    ByteArrayInputStream inputStream = new ByteArrayInputStream(sampleData);
    Base64InputStream base64InputStream = new Base64InputStream(inputStream);
    
    try {
      byte[] buffer = new byte[3];
      
      // read zero bytes into the buffer starting at offset 0
      assertEquals(0, base64InputStream.read(buffer, 0, 0));
      
      // read one byte into the buffer starting at offset 0
      assertEquals(1, base64InputStream.read(buffer, 0, 0));
    } catch (IOException e) {
      fail("Exception should not be thrown");
    }
  }
  
  @Test
  public void testReadWithNullArray() {
    // create input stream with sample data
    byte[] sampleData = {72, 101, 108, 108, 111, 33}; // "Hello!" in ASCII
    ByteArrayInputStream inputStream = new ByteArrayInputStream(sampleData);
    Base64InputStream base64InputStream = new Base64InputStream(inputStream);
    
    try {
      // read into null byte array
      base64InputStream.read(null, 0, 3);
      fail("NullPointerException should be thrown");
    } catch (IOException e) {
      fail("IOException should not be thrown");
    } catch (NullPointerException e) {
      // expected exception
    }
  }
  
  @Test
  public void testReadWithNegativeOffsetAndLength() {
    // create input stream with sample data
    byte[] sampleData = {72, 101, 108, 108, 111, 33}; // "Hello!" in ASCII
    ByteArrayInputStream inputStream = new ByteArrayInputStream(sampleData);
    Base64InputStream base64InputStream = new Base64InputStream(inputStream);
    
    try {
      byte[] buffer = new byte[3];
      
      // read into the buffer with negative offset
      base64InputStream.read(buffer, -1, 3);
      fail("IndexOutOfBoundsException should be thrown");
    } catch (IOException e) {
      fail("IOException should not be thrown");
    } catch (IndexOutOfBoundsException e) {
      // expected exception
    }
    
    try {
      byte[] buffer = new byte[3];
      
      // read into the buffer with negative length
      base64InputStream.read(buffer, 0, -3);
      fail("IndexOutOfBoundsException should be thrown");
    } catch (IOException e) {
      fail("IOException should not be thrown");
    } catch (IndexOutOfBoundsException e) {
      // expected exception
    }
  }
  
  @Test
  public void testReadWithInvalidOffsetAndLength() {
    // create input stream with sample data
    byte[] sampleData = {72, 101, 108, 108, 111, 33}; // "Hello!" in ASCII
    ByteArrayInputStream inputStream = new ByteArrayInputStream(sampleData);
    Base64InputStream base64InputStream = new Base64InputStream(inputStream);
    
    try {
      byte[] buffer = new byte[3];
      
      // read into the buffer with offset greater than array length
      base64InputStream.read(buffer, 4, 3);
      fail("IndexOutOfBoundsException should be thrown");
    } catch (IOException e) {
      fail("IOException should not be thrown");
    } catch (IndexOutOfBoundsException e) {
      // expected exception
    }
    
    try {
      byte[] buffer = new byte[3];
      
      // read into the buffer with offset + length larger than array length
      base64InputStream.read(buffer, 2, 3);
      fail("IndexOutOfBoundsException should be thrown");
    } catch (IOException e) {
      fail("IOException should not be thrown");
    } catch (IndexOutOfBoundsException e) {
      // expected exception
    }
  }
  
  @Test
  public void testMarkNotSupported() {
    // create input stream with sample data
    byte[] sampleData = {72, 101, 108, 108, 111, 33}; // "Hello!" in ASCII
    ByteArrayInputStream inputStream = new ByteArrayInputStream(sampleData);
    Base64InputStream base64InputStream = new Base64InputStream(inputStream);
    
    assertFalse(base64InputStream.markSupported());
  }

  
}