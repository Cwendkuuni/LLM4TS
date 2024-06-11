import static org.junit.Assert.*;
import org.junit.Test;

public class ArArchiveInputStreamTest {
  
  @Test
  public void testGetNextArEntry() {
    // Test when there is no entry available
    ArArchiveInputStream arInput = new ArArchiveInputStream(null);
    try {
      ArArchiveEntry entry = arInput.getNextArEntry();
      assertNull(entry);
    } catch (IOException e) {
      fail("Exception thrown: " + e.getMessage());
    }
    
    // Test when the header bytes do not match
    InputStream input1 = new InputStream() {
      public int read() {
        return 0;
      }
    };
    ArArchiveInputStream arInput1 = new ArArchiveInputStream(input1);
    try {
      ArArchiveEntry entry1 = arInput1.getNextArEntry();
      fail("Invalid header exception not thrown");
    } catch (IOException e) {
      assertEquals("invalid header ", e.getMessage());
    }
    
    // Test when there is incomplete entry
    InputStream input2 = new InputStream() {
      private int count = 0;
      private byte[] bytes = { 
        0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a,
        0x74, 0x65, 0x73, 0x74, 0x49, 0x6e, 0x70, 0x75,
        0x74, 0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e,
        0x0a, 0x74, 0x65, 0x73, 0x74 };
      public int read() {
        if (count < bytes.length) {
          return bytes[count++];
        } else {
          return -1;
        }
      }
    };
    ArArchiveInputStream arInput2 = new ArArchiveInputStream(input2);
    try {
      ArArchiveEntry entry2 = arInput2.getNextArEntry();
      fail("Entry not read completely exception not thrown");
    } catch (IOException e) {
      assertEquals("failed to read entry header", e.getMessage());
    }
    
    // Test when there is a complete entry
    InputStream input3 = new InputStream() {
      private int count = 0;
      private byte[] bytes = { 
        0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a,
        0x74, 0x65, 0x73, 0x74, 0x49, 0x6e, 0x70, 0x75,
        0x74, 0x74, 0x65, 0x73, 0x74, 0x0a, 0x21, 0x3c,
        0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a };
      public int read() {
        if (count < bytes.length) {
          return bytes[count++];
        } else {
          return -1;
        }
      }
    };
    ArArchiveInputStream arInput3 = new ArArchiveInputStream(input3);
    try {
      ArArchiveEntry entry3 = arInput3.getNextArEntry();
      assertEquals("testInput", entry3.getName());
      assertEquals(12, entry3.getLength());
    } catch (IOException e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }
  
  @Test
  public void testGetNextEntry() {
    // Test when there is no entry available
    ArArchiveInputStream arInput = new ArArchiveInputStream(null);
    try {
      ArchiveEntry entry = arInput.getNextEntry();
      assertNull(entry);
    } catch (IOException e) {
      fail("Exception thrown: " + e.getMessage());
    }  
  }
  
  @Test
  public void testClose() {
    // Test when input stream is not closed
    InputStream input = new InputStream() {
      public int read() {
        return 0;
      }
      public void close() {
        fail("Close method called on input stream");
      }
    };
    ArArchiveInputStream arInput = new ArArchiveInputStream(input);
    try {
      arInput.close();
    } catch (IOException e) {
      fail("Exception thrown: " + e.getMessage());
    }
    
    // Test when input stream is closed
    InputStream input1 = new InputStream() {
      public int read() {
        return 0;
      }
      public void close() {
        // do nothing
      }
    };
    ArArchiveInputStream arInput1 = new ArArchiveInputStream(input1);
    try {
      arInput1.close();
    } catch (IOException e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }
  
  @Test
  public void testRead() {
    InputStream input = new InputStream() {
      private int count = 0;
      private byte[] bytes = { 0x00, 0x01, 0x02 };
      public int read() {
        if (count < bytes.length) {
          return bytes[count++];
        } else {
          return -1;
        }
      }
    };
    ArArchiveInputStream arInput = new ArArchiveInputStream(input);
    try {
      assertEquals(0, arInput.read());
      assertEquals(1, arInput.read());
      assertEquals(2, arInput.read());
      assertEquals(-1, arInput.read());
    } catch (IOException e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }
  
  @Test
  public void testReadWithBytes() {
    byte[] bytes = { 0x00, 0x01, 0x02 };
    InputStream input = new InputStream() {
      private int count = 0;
      public int read() {
        return -1;
      }
      public int read(byte[] b, int off, int len) {
        for (int i = off; i < len; i++) {
          b[i] = bytes[count++];
        }
        return len - off;
      }
    };
    ArArchiveInputStream arInput = new ArArchiveInputStream(input);
    try {
      byte[] buffer = new byte[2];
      assertEquals(2, arInput.read(buffer));
      assertArrayEquals(new byte[] { 0x00, 0x01 }, buffer);
      assertEquals(1, arInput.read(buffer));
      assertArrayEquals(new byte[] { 0x02, 0x01 }, buffer);
      assertEquals(0, arInput.read(buffer));
      assertArrayEquals(new byte[] { 0x02, 0x01 }, buffer);
    } catch (IOException e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }
  
  @Test
  public void testMatches() {
    byte[] signature = { 0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a };
    assertTrue(ArArchiveInputStream.matches(signature, 8));
    
    byte[] signature1 = { 0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e };
    assertFalse(ArArchiveInputStream.matches(signature1, 7));
    
    byte[] signature2 = { 0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a, 0x00 };
    assertTrue(ArArchiveInputStream.matches(signature2, 9));
    
    byte[] signature3 = { 0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0b };
    assertFalse(ArArchiveInputStream.matches(signature3, 8));
    
    byte[] signature4 = { 0x20, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a };
    assertFalse(ArArchiveInputStream.matches(signature4, 8));
  }
}