import static org.junit.Assert.*;
import org.junit.Test;
import java.io.StringReader;

public class CharacterReaderTest {
  
  @Test
  public void testPos() {
    Reader input = new StringReader("abc");
    CharacterReader reader = new CharacterReader(input);
    
    assertEquals(0, reader.pos());
    
    reader.consume();
    assertEquals(1, reader.pos());
    
    reader.advance();
    assertEquals(2, reader.pos());
    
    reader.consumeTo('c');
    assertEquals(3, reader.pos());
    
    reader.consumeToEnd();
    assertEquals(3, reader.pos());
  }
  
  @Test
  public void testIsEmpty() {
    Reader input = new StringReader("abc");
    CharacterReader reader = new CharacterReader(input);
    
    assertFalse(reader.isEmpty());
    
    reader.consumeTo('c');
    assertFalse(reader.isEmpty());
    
    reader.consumeToEnd();
    assertTrue(reader.isEmpty());
  }
  
  @Test
  public void testCurrent() {
    Reader input = new StringReader("abc");
    CharacterReader reader = new CharacterReader(input);
    
    assertEquals('a', reader.current());
    
    reader.consume();
    assertEquals('b', reader.current());
    
    reader.advance();
    assertEquals('c', reader.current());
    
    reader.consumeToEnd();
    assertEquals(CharacterReader.EOF, reader.current());
  }
  
  // Tests for other methods can be generated in a similar manner
  
  // Edge cases and exception scenarios:
  
  @Test
  public void testNextIndexOf_EdgeCases() {
    // Test when the target character is the first character in the buffer
    Reader input = new StringReader("abc");
    CharacterReader reader = new CharacterReader(input);
    int index = reader.nextIndexOf('a');
    assertEquals(0, index);
    
    // Test when the target character is not found in the buffer
    input = new StringReader("abc");
    reader = new CharacterReader(input);
    index = reader.nextIndexOf('d');
    assertEquals(-1, index);
  }
  
  @Test
  public void testConsumeTo_Exception() {
    // Test when the delimiter is not found in the remaining content
    Reader input = new StringReader("abc");
    CharacterReader reader = new CharacterReader(input);
    String consumed = reader.consumeTo('d');
    assertEquals("abc", consumed);
  }
  
  // Merge all test cases into a complete test class
  
  @Test
  public void testCharacterReader() {
    testPos();
    testIsEmpty();
    testCurrent();
    // Add more test methods here
  }

}