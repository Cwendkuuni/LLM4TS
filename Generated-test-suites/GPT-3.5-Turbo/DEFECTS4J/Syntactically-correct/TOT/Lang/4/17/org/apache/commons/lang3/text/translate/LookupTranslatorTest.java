import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringWriter;
import org.apache.commons.lang3.text.translate.*;
import org.junit.Before;
import org.junit.Test;

public class LookupTranslatorTest {

  private LookupTranslator lookupTranslator;
  
  @Before
  public void setUp() {
    // Set up test data
    CharSequence[][] lookup = new CharSequence[][] {
        {"one", "ONE"},
        {"two", "TWO"},
        {"three", "THREE"}
    };
    lookupTranslator = new LookupTranslator(lookup);
  }
  
  @Test
  public void testTranslate_WhenValidInput_ShouldTranslateSuccessfully() throws IOException {
    // Arrange
    StringWriter writer = new StringWriter();
    CharSequence input = "onetwothree";
    
    // Act
    int result = lookupTranslator.translate(input, 0, writer);
    
    // Assert
    assertEquals(11, result);
    assertEquals("ONETWOTHREE", writer.toString());
  }

  @Test
  public void testTranslate_WhenInputContainsSubSequenceNotPresentInLookup_ShouldNotTranslate() throws IOException {
    // Arrange
    StringWriter writer = new StringWriter();
    CharSequence input = "four";
    
    // Act
    int result = lookupTranslator.translate(input, 0, writer);
    
    // Assert
    assertEquals(0, result);
    assertEquals("", writer.toString());
  }

  @Test
  public void testTranslate_WhenInputIsEmpty_ShouldNotTranslate() throws IOException {
    // Arrange
    StringWriter writer = new StringWriter();
    CharSequence input = "";
    
    // Act
    int result = lookupTranslator.translate(input, 0, writer);
    
    // Assert
    assertEquals(0, result);
    assertEquals("", writer.toString());
  }
  
  @Test
  public void testTranslate_WhenIndexIsGreaterThanInputLength_ShouldNotTranslate() throws IOException {
    // Arrange
    StringWriter writer = new StringWriter();
    CharSequence input = "onetwothree";
    
    // Act
    int result = lookupTranslator.translate(input, 20, writer);
    
    // Assert
    assertEquals(0, result);
    assertEquals("", writer.toString());
  }
  
  @Test
  public void testTranslate_WhenLookupIsNull_ShouldNotTranslate() throws IOException {
    // Arrange
    LookupTranslator lookupTranslator = new LookupTranslator((CharSequence[][])null);
    StringWriter writer = new StringWriter();
    CharSequence input = "onetwothree";
    
    // Act
    int result = lookupTranslator.translate(input, 0, writer);
    
    // Assert
    assertEquals(0, result);
    assertEquals("", writer.toString());
  }
  
  @Test
  public void testTranslate_WhenInputIsNull_ShouldNotTranslate() throws IOException {
    // Arrange
    StringWriter writer = new StringWriter();
    CharSequence input = null;
    
    // Act
    int result = lookupTranslator.translate(input, 0, writer);
    
    // Assert
    assertEquals(0, result);
    assertEquals("", writer.toString());
  }
  
  @Test
  public void testTranslate_WhenWriterThrowsIOException_ShouldThrowIOException() throws IOException {
    // Arrange
    StringWriter writer = new StringWriter() {
      @Override
      public void write(String str) throws IOException {
        throw new IOException();
      }
    };
    CharSequence input = "onetwothree";
    
    // Act & Assert
    try {
      lookupTranslator.translate(input, 0, writer);
      fail("IOException was not thrown.");
    } catch (IOException e) {
      // Exception caught successfully
    }
  }
}