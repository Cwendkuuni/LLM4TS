import org.junit.Assert;
import org.junit.Test;

public class Utf8Test {
  
  @Test
  public void testEncodedLength() {
    // Test cases with input sequence of length 0
    Assert.assertEquals(0, Utf8.encodedLength(""));
    
    // Test cases with input sequence of length 1
    Assert.assertEquals(1, Utf8.encodedLength("a"));
    Assert.assertEquals(1, Utf8.encodedLength("A"));
    Assert.assertEquals(1, Utf8.encodedLength("\u0080"));
    
    // Test cases with input sequence of length greater than 1
    Assert.assertEquals(2, Utf8.encodedLength("aa"));
    Assert.assertEquals(3, Utf8.encodedLength("abc"));
    Assert.assertEquals(3, Utf8.encodedLength("\u0080\u0080"));
    Assert.assertEquals(4, Utf8.encodedLength("\u0800"));
    Assert.assertEquals(4, Utf8.encodedLength("\ud800\udc00"));
    Assert.assertEquals(5, Utf8.encodedLength("\ud800\udc00\u0800"));
    
    // Test case with invalid input
    try {
      Utf8.encodedLength("\ud800");
      Assert.fail("Expected IllegalArgumentException was not thrown");
    } catch (IllegalArgumentException e) {
      // Expected
    }
  }

  @Test
  public void testEncodedLengthGeneral() {
    // Test cases with input sequence of length 0
    Assert.assertEquals(0, Utf8.encodedLengthGeneral("", 0));
    
    // Test cases with input sequence of length 1
    Assert.assertEquals(1, Utf8.encodedLengthGeneral("a", 0));
    Assert.assertEquals(1, Utf8.encodedLengthGeneral("A", 0));
    Assert.assertEquals(1, Utf8.encodedLengthGeneral("\u0080", 0));
    
    // Test cases with input sequence of length greater than 1
    Assert.assertEquals(2, Utf8.encodedLengthGeneral("aa", 0));
    Assert.assertEquals(3, Utf8.encodedLengthGeneral("abc", 0));
    Assert.assertEquals(3, Utf8.encodedLengthGeneral("\u0080\u0080", 0));
    Assert.assertEquals(4, Utf8.encodedLengthGeneral("\u0800", 0));
    Assert.assertEquals(5, Utf8.encodedLengthGeneral("\ud800\udc00", 0));
    Assert.assertEquals(6, Utf8.encodedLengthGeneral("\ud800\udc00\u0800", 0));
    
    // Test case with invalid input
    try {
      Utf8.encodedLengthGeneral("\ud800", 0);
      Assert.fail("Expected IllegalArgumentException was not thrown");
    } catch (IllegalArgumentException e) {
      // Expected
    }
  }
  
  @Test
  public void testIsWellFormed() {
    // Test case with empty byte array
    Assert.assertTrue(Utf8.isWellFormed(new byte[0]));
    
    // Test case with valid byte array
    Assert.assertTrue(Utf8.isWellFormed(new byte[] {0b01111111}));
    Assert.assertTrue(Utf8.isWellFormed(new byte[] {(byte) 0xC2, (byte) 0x80}));
    Assert.assertTrue(Utf8.isWellFormed(new byte[] {(byte) 0xE0, (byte) 0xA0, (byte) 0x80}));
    Assert.assertTrue(Utf8.isWellFormed(new byte[] {(byte) 0xF0, (byte) 0x90, (byte) 0x80, (byte) 0x80}));
    
    // Test case with invalid byte array
    Assert.assertFalse(Utf8.isWellFormed(new byte[] {(byte) 0x80}));
    Assert.assertFalse(Utf8.isWellFormed(new byte[] {(byte) 0xC0}));
    Assert.assertFalse(Utf8.isWellFormed(new byte[] {(byte) 0xC0, (byte) 0x80}));
  }
  
  @Test
  public void testIsWellFormedWithOffsetAndLength() {
    // Test case with empty byte array
    Assert.assertTrue(Utf8.isWellFormed(new byte[0], 0, 0));
    
    // Test case with valid byte array
    Assert.assertTrue(Utf8.isWellFormed(new byte[] {0b01111111}, 0, 1));
    Assert.assertTrue(Utf8.isWellFormed(new byte[] {(byte) 0xC2, (byte) 0x80}, 0, 2));
    Assert.assertTrue(Utf8.isWellFormed(new byte[] {(byte) 0xE0, (byte) 0xA0, (byte) 0x80}, 0, 3));
    Assert.assertTrue(Utf8.isWellFormed(new byte[] {(byte) 0xF0, (byte) 0x90, (byte) 0x80, (byte) 0x80}, 0, 4));
    
    // Test case with invalid byte array
    Assert.assertFalse(Utf8.isWellFormed(new byte[] {(byte) 0x80}, 0, 1));
    Assert.assertFalse(Utf8.isWellFormed(new byte[] {(byte) 0xC0}, 0, 1));
    Assert.assertFalse(Utf8.isWellFormed(new byte[] {(byte) 0xC0, (byte) 0x80}, 0, 2));
  }
  
  private boolean isWellFormedSlowPath(byte[] bytes, int off, int end) {
    try {
      Class<?> cls = Utf8.class.getDeclaredClasses()[0];
      java.lang.reflect.Method method = cls.getDeclaredMethod("isWellFormedSlowPath", byte[].class, int.class, int.class);
      method.setAccessible(true);
      return (boolean) method.invoke(null, bytes, off, end);
    } catch (Exception e) {
      return false;
    }
  }
  
  @Test
  public void testIsWellFormedSlowPath() {
    // Test case with valid byte array
    Assert.assertFalse(isWellFormedSlowPath(new byte[] {(byte) 0x80}, 0, 1));
    Assert.assertFalse(isWellFormedSlowPath(new byte[] {(byte) 0xC0}, 0, 1));
    Assert.assertTrue(isWellFormedSlowPath(new byte[] {(byte) 0xC0, (byte) 0x80}, 0, 2));
  }
  
}