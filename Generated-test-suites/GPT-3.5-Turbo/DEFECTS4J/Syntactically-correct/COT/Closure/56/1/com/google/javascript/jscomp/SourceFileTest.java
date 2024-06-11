import static org.junit.Assert.*;
import org.junit.Test;

public class SourceFileTest {
  
  @Test
  public void testConstructor() {
    SourceFile file = new SourceFile("test.js");
    assertEquals("test.js", file.getName());
  }
  
  @Test
  public void testGetLineOffset() {
    SourceFile file = new SourceFile("test.js");
    assertEquals(0, file.getLineOffset(1));
    
    //add additional test cases here
  }
  
  @Test
  public void testGetNumLines() {
    SourceFile file = new SourceFile("test.js");
    assertEquals(1, file.getNumLines());
    
    //add additional test cases here
  }
  
  @Test
  public void testGetLine() {
    SourceFile file = new SourceFile("test.js");
    assertNull(file.getLine(1));
    
    //add additional test cases here
  }
  
  @Test
  public void testGetRegion() {
    SourceFile file = new SourceFile("test.js");
    assertNull(file.getRegion(1));
    
    //add additional test cases here
  }
  
  @Test
  public void testToString() {
    SourceFile file = new SourceFile("test.js");
    assertEquals("test.js", file.toString());
  }
  
  //add additional test cases for the other methods
  
}