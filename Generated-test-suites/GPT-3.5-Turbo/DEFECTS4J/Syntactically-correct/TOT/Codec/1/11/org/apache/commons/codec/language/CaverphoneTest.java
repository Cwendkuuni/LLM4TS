import org.junit.Test;
import org.junit.Assert;

public class CaverphoneTest {

  @Test
  public void testCaverphone_NullInput_Returns1111111111() {
    // Arrange
    Caverphone caverphone = new Caverphone();
    
    // Act
    String result = caverphone.caverphone(null);
    
    // Assert
    Assert.assertEquals("1111111111", result);
  }

  @Test
  public void testCaverphone_EmptyInput_Returns1111111111() {
    // Arrange
    Caverphone caverphone = new Caverphone();
    
    // Act
    String result = caverphone.caverphone("");
    
    // Assert
    Assert.assertEquals("1111111111", result);
  }
  
  @Test
  public void testCaverphone_OnlySpecialCharacters_Returns1111111111() {
    // Arrange
    Caverphone caverphone = new Caverphone();
    
    // Act
    String result = caverphone.caverphone("@#$%^&*()");
    
    // Assert
    Assert.assertEquals("1111111111", result);
  }
  
  @Test
  public void testCaverphone_LowercaseInput_ReturnsCaverphoneCode() {
    // Arrange
    Caverphone caverphone = new Caverphone();
    
    // Act
    String result = caverphone.caverphone("example");
    
    // Assert
    Assert.assertEquals("XAMPL1111", result);
  }
  
  @Test
  public void testCaverphone_UppercaseInput_ReturnsCaverphoneCode() {
    // Arrange
    Caverphone caverphone = new Caverphone();
    
    // Act
    String result = caverphone.caverphone("EXAMPLE");
    
    // Assert
    Assert.assertEquals("XAMPL1111", result);
  }
  
  @Test
  public void testEncode_ObjectOfNonStringType_ThrowsEncoderException() {
    // Arrange
    Caverphone caverphone = new Caverphone();
    Object object = new Object();
    
    // Act & Assert
    try {
      caverphone.encode(object);
      Assert.fail("Expected EncoderException to be thrown");
    } catch (EncoderException e) {
      // Pass
    }
  }
  
  @Test
  public void testEncode_ValidStringInput_ReturnsCaverphoneCode() {
    // Arrange
    Caverphone caverphone = new Caverphone();
    String input = "example";
    
    // Act
    String result = caverphone.encode(input);
    
    // Assert
    Assert.assertEquals("XAMPL1111", result);
  }
  
  @Test
  public void testIsCaverphoneEqual_TwoIdenticalStrings_ReturnsTrue() {
    // Arrange
    Caverphone caverphone = new Caverphone();
    String str1 = "example";
    String str2 = "example";
    
    // Act
    boolean result = caverphone.isCaverphoneEqual(str1, str2);
    
    // Assert
    Assert.assertTrue(result);
  }
  
  @Test
  public void testIsCaverphoneEqual_TwoDifferentStrings_ReturnsFalse() {
    // Arrange
    Caverphone caverphone = new Caverphone();
    String str1 = "example1";
    String str2 = "example2";
    
    // Act
    boolean result = caverphone.isCaverphoneEqual(str1, str2);
    
    // Assert
    Assert.assertFalse(result);
  }
  
  @Test
  public void testIsCaverphoneEqual_NullString_ReturnsFalse() {
    // Arrange
    Caverphone caverphone = new Caverphone();
    String str1 = "example1";
    String str2 = null;
    
    // Act
    boolean result = caverphone.isCaverphoneEqual(str1, str2);
    
    // Assert
    Assert.assertFalse(result);
  }

}