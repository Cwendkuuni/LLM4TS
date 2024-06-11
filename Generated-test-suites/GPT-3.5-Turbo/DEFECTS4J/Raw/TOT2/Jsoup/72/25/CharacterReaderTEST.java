import org.junit.Test;
import static org.junit.Assert.*;
import java.io.StringReader;

public class CharacterReaderTest {
  
  @Test
  public void testConstructorWithReaderAndSize() {
    String str = "TestString";
    StringReader reader = new StringReader(str);
    CharacterReader charReader = new CharacterReader(reader, str.length());
    
    assertEquals(0, charReader.pos());
    assertFalse(charReader.isEmpty());
    assertEquals('T', charReader.current());
  }
  
  @Test
  public void testConsumeMethod() {
    String str = "TestString";
    CharacterReader charReader = new CharacterReader(str);
    
    char firstChar = charReader.consume();
    assertEquals('T', firstChar);
    
    charReader.consume();
    assertEquals('s', charReader.current());
  }
  
  @Test
  public void testConsumeToMethod() {
    String str = "This is a test string to test consumeTo method.";
    CharacterReader charReader = new CharacterReader(str);
    
    String consumed = charReader.consumeTo('t');
    assertEquals("This is a ", consumed);
    
    charReader.consume(); // skipping white space
    
    consumed = charReader.consumeTo("test");
    assertEquals("string", consumed);
  }
  
  // Add more test cases for other methods
  
}