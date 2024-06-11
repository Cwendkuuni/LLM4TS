import org.junit.Before;
import org.junit.Test;
import org.apache.commons.codec.language.Caverphone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class CaverphoneTest {
  
  private Caverphone caverphone;
  
  @Before
  public void setUp() {
    caverphone = new Caverphone();
  }
  
  // Typical use case
  @Test
  public void testCaverphone_typical() {
    String input = "example";
    String expectedOutput = "eksamp11";
    
    String result = caverphone.caverphone(input);
    
    assertEquals(expectedOutput, result);
  }
  
  // Edge case: empty string input
  @Test
  public void testCaverphone_emptyString() {
    String input = "";
    String expectedOutput = "1111111111";
    
    String result = caverphone.caverphone(input);
    
    assertEquals(expectedOutput, result);
  }
  
  // Edge case: input containing non-alphabet characters
  @Test
  public void testCaverphone_nonAlphabetCharacters() {
    String input = "123abc!@#";
    String expectedOutput = "abc";
    
    String result = caverphone.caverphone(input);
    
    assertEquals(expectedOutput, result);
  }
  
  // Error scenario: null input
  @Test
  public void testCaverphone_nullInput() {
    String input = null;
    String expectedOutput = "1111111111";
    
    String result = caverphone.caverphone(input);
    
    assertEquals(expectedOutput, result);
  }
  
  // Typical use case
  @Test
  public void testEncode_typical() {
    String input = "example";
    String expectedOutput = "eksamp11";
    
    String result = caverphone.encode(input);
    
    assertEquals(expectedOutput, result);
  }
  
  // Error scenario: input of type other than string
  @Test(expected = EncoderException.class)
  public void testEncode_invalidInputType() throws EncoderException {
    Object input = new Object();
    
    caverphone.encode(input);
  }
  
  // Typical use case: caverphones of two strings are equal
  @Test
  public void testIsCaverphoneEqual_equal() {
    String str1 = "example";
    String str2 = "exampler";
    
    boolean result = caverphone.isCaverphoneEqual(str1, str2);
    
    assertEquals(true, result);
  }
  
  // Typical use case: caverphones of two strings are not equal
  @Test
  public void testIsCaverphoneEqual_notEqual() {
    String str1 = "example";
    String str2 = "different";
    
    boolean result = caverphone.isCaverphoneEqual(str1, str2);
    
    assertEquals(false, result);
  }
  
  // Edge case: null input for str1
  @Test
  public void testIsCaverphoneEqual_nullInput1() {
    String str1 = null;
    String str2 = "example";
    
    boolean result = caverphone.isCaverphoneEqual(str1, str2);
    
    assertEquals(false, result);
  }
  
  // Edge case: null input for str2
  @Test
  public void testIsCaverphoneEqual_nullInput2() {
    String str1 = "example";
    String str2 = null;
    
    boolean result = caverphone.isCaverphoneEqual(str1, str2);
    
    assertEquals(false, result);
  }
  
  // Edge case: null inputs for both str1 and str2
  @Test
  public void testIsCaverphoneEqual_nullInputs() {
    String str1 = null;
    String str2 = null;
    
    boolean result = caverphone.isCaverphoneEqual(str1, str2);
    
    assertEquals(true, result);
  }
  
  // Error scenario: encoder returning null for str1
  @Test
  public void testIsCaverphoneEqual_nullOutput1() {
    String str1 = "example";
    String str2 = "example";
    Caverphone faultyCaverphone = new Caverphone() {
      @Override
      public String caverphone(String txt) {
        return null;
      }
    };
    
    boolean result = faultyCaverphone.isCaverphoneEqual(str1, str2);
    
    assertEquals(false, result);
  }
  
  // Error scenario: encoder returning null for str2
  @Test
  public void testIsCaverphoneEqual_nullOutput2() {
    String str1 = "example";
    String str2 = "example";
    Caverphone faultyCaverphone = new Caverphone() {
      @Override
      public String caverphone(String txt) {
        if(txt.equals(str1)) {
          return caverphone.caverphone(txt);
        } else {
          return null;
        }
      }
    };
    
    boolean result = faultyCaverphone.isCaverphoneEqual(str1, str2);
    
    assertEquals(false, result);
  }
  
  // Error scenario: null inputs for both str1 and str2
  @Test
  public void testIsCaverphoneEqual_nullInputsOutput() {
    String str1 = null;
    String str2 = null;
    Caverphone faultyCaverphone = new Caverphone() {
      @Override
      public String caverphone(String txt) {
        return null;
      }
    };
    
    boolean result = faultyCaverphone.isCaverphoneEqual(str1, str2);
    
    assertEquals(true, result);
  }
  
  // Error scenario: encoder returning different caverphones for str1 and str2
  @Test
  public void testIsCaverphoneEqual_differentOutput() {
    String str1 = "example";
    String str2 = "different";
    Caverphone faultyCaverphone = new Caverphone() {
      @Override
      public String caverphone(String txt) {
        if(txt.equals(str1)) {
          return caverphone.caverphone(txt);
        } else {
          return "differentcode";
        }
      }
    };
    
    boolean result = faultyCaverphone.isCaverphoneEqual(str1, str2);
    
    assertEquals(false, result);
  }
  
}