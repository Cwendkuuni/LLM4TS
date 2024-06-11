package com.hf.sfm.crypt;

import org.junit.Test;
import static org.junit.Assert.*;

public class Base64Test {

  @Test
  public void testAltBase64ToByteArray() {
    byte[] expectedOutput = {/*insert expected output here*/};
    assertArrayEquals(expectedOutput, Base64.altBase64ToByteArray(""));
  }
  
  @Test
  public void testBase64ToByteArray() {
    byte[] expectedOutput = {/*insert expected output here*/};
    assertArrayEquals(expectedOutput, Base64.base64ToByteArray(""));
  }
  
  @Test
  public void testByteArrayToAltBase64() {
    String expectedOutput = "/*insert expected output here*/";
    assertEquals(expectedOutput, Base64.byteArrayToAltBase64(new byte[]{}));
  }
  
  @Test
  public void testByteArrayToBase64() {
    String expectedOutput = "/*insert expected output here*/";
    assertEquals(expectedOutput, Base64.byteArrayToBase64(new byte[]{}));
  }
}