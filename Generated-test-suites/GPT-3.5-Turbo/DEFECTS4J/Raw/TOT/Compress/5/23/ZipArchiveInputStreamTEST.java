import org.junit.Assert;
import org.junit.Test;

public class ZipArchiveInputStreamTest {
  
  @Test
  public void testGetNextZipEntry() {
    ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream();
    
    // typical use case
    ZipArchiveEntry entry = zipInputStream.getNextZipEntry();
    Assert.assertNotNull(entry);
    
    // edge case: when there are no more entries in the zip file
    entry = zipInputStream.getNextZipEntry();
    Assert.assertNull(entry);
  }
  
  @Test
  public void testGetNextEntry() {
    ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream();
    
    // typical use case
    ArchiveEntry entry = zipInputStream.getNextEntry();
    Assert.assertNotNull(entry);
    
    // edge case: when there are no more entries in the zip file
    entry = zipInputStream.getNextEntry();
    Assert.assertNull(entry);
  }
  
  @Test
  public void testRead() {
    ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream();
    
    byte[] buffer = new byte[10];
    
    // typical use case
    int bytesRead = zipInputStream.read(buffer, 0, buffer.length);
    Assert.assertEquals(10, bytesRead);
    
    // edge case: when trying to read more bytes than available in the stream
    bytesRead = zipInputStream.read(buffer, 0, buffer.length);
    Assert.assertEquals(5, bytesRead);
    
    // edge case: when the stream is closed
    zipInputStream.close();
    try {
      bytesRead = zipInputStream.read(buffer, 0, buffer.length);
      Assert.fail("Expected an IOException to be thrown");
    } catch (IOException e) {
      // expected exception
    }
  }
  
  @Test
  public void testClose() {
    ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream();
    
    // typical use case
    try {
      zipInputStream.close();
    } catch (IOException e) {
      Assert.fail("Unexpected IOException occurred");
    }
    
    // edge case: when trying to close the stream multiple times
    try {
      zipInputStream.close();
      Assert.fail("Expected an IOException to be thrown");
    } catch (IOException e) {
      // expected exception
    }
  }
  
  @Test
  public void testSkip() {
    ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream();
    
    // typical use case
    try {
      long skipped = zipInputStream.skip(10);
      Assert.assertEquals(10, skipped);
    } catch (IOException e) {
      Assert.fail("Unexpected IOException occurred");
    }
    
    // edge case: when trying to skip negative bytes
    try {
      long skipped = zipInputStream.skip(-10);
      Assert.fail("Expected an IllegalArgumentException to be thrown");
    } catch (IllegalArgumentException e) {
      // expected exception
    }
    
    // edge case: when the stream is closed
    zipInputStream.close();
    try {
      long skipped = zipInputStream.skip(10);
      Assert.fail("Expected an IOException to be thrown");
    } catch (IOException e) {
      // expected exception
    }
  }
  
  @Test
  public void testMatches() {
    byte[] signature = new byte[4];
    
    // typical use case: when the signature matches
    signature = ZipArchiveOutputStream.LFH_SIG;
    Assert.assertTrue(ZipArchiveInputStream.matches(signature, signature.length));
    
    // edge case: when the length of the signature is less than the expected length
    signature = new byte[2];
    Assert.assertFalse(ZipArchiveInputStream.matches(signature, signature.length));
    
    // edge case: when the signature does not match
    signature[0] = 1;
    signature[1] = 2;
    signature[2] = 3;
    signature[3] = 4;
    Assert.assertFalse(ZipArchiveInputStream.matches(signature, signature.length));
    
    // edge case: when the signature matches the empty zip signature
    signature = ZipArchiveOutputStream.EOCD_SIG;
    Assert.assertTrue(ZipArchiveInputStream.matches(signature, signature.length));
  }
  
  @Test
  public void testCloseEntry() {
    ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream();
    
    // typical use case
    try {
      zipInputStream.closeEntry();
    } catch (IOException e) {
      Assert.fail("Unexpected IOException occurred");
    }
    
    // edge case: when trying to close the entry multiple times
    try {
      zipInputStream.closeEntry();
      Assert.fail("Expected an IOException to be thrown");
    } catch (IOException e) {
      // expected exception
    }
  }
  
  @Test
  public void testFill() {
    ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream();
    
    // typical use case
    try {
      zipInputStream.fill();
    } catch (IOException e) {
      Assert.fail("Unexpected IOException occurred");
    }
    
    // edge case: when the stream is closed
    zipInputStream.close();
    try {
      zipInputStream.fill();
      Assert.fail("Expected an IOException to be thrown");
    } catch (IOException e) {
      // expected exception
    }
  }
  
  @Test
  public void testReadFully() {
    ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream();
    
    byte[] b = new byte[10];
    
    // typical use case
    try {
      zipInputStream.readFully(b);
    } catch (IOException e) {
      Assert.fail("Unexpected IOException occurred");
    }
    
    // edge case: when the stream is closed
    zipInputStream.close();
    try {
      zipInputStream.readFully(b);
      Assert.fail("Expected an IOException to be thrown");
    } catch (IOException e) {
      // expected exception
    }
    
    // edge case: when the number of bytes read is less than the expected length
    b = new byte[20];
    try {
      zipInputStream.readFully(b);
      Assert.fail("Expected an EOFException to be thrown");
    } catch (EOFException e) {
      // expected exception
    } catch (IOException e) {
      Assert.fail("Expected an EOFException to be thrown");
    }
  }
  
}