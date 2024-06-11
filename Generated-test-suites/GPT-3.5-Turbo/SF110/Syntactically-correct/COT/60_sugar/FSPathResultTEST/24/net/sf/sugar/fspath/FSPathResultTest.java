import org.junit.Assert;
import org.junit.Test;

public class FSPathResultTest {
  
  @Test
  public void testConstructorWithFile() {
    File file = new File("example.txt");
    FSPathResult result = new FSPathResult(file);
    
    Assert.assertEquals(file, result.getFile());
  }
  
  @Test
  public void testConstructorWithString() {
    String string = "example";
    FSPathResult result = new FSPathResult(string);
    
    Assert.assertEquals(string, result.getString());
  }
  
  // similar test cases for other constructors
  
  @Test
  public void testGetFile() {
    File file = new File("example.txt");
    FSPathResult result = new FSPathResult(file);
    
    Assert.assertEquals(file, result.getFile());
  }
  
  @Test
  public void testGetString() {
    String string = "example";
    FSPathResult result = new FSPathResult(string);
    
    Assert.assertEquals(string, result.getString());
  }
  
  // similar test cases for other getter methods
  
  @Test
  public void testToString() {
    File file = new File("example.txt");
    FSPathResult result = new FSPathResult(file);
    
    Assert.assertEquals(file.getAbsolutePath(), result.toString());
  }
  
  // additional test cases for other methods
  
  // edge cases and exception handling scenarios
  
  @Test(expected = NullPointerException.class)
  public void testConstructorWithNullFile() {
    File file = null;
    FSPathResult result = new FSPathResult(file);
  }
  
  @Test(expected = NullPointerException.class)
  public void testConstructorWithNullString() {
    String string = null;
    FSPathResult result = new FSPathResult(string);
  }
  
  // Similar test cases for other constructors with null parameters
  
  @Test(expected = NullPointerException.class)
  public void testGetFileWithNullResultFile() {
    FSPathResult result = new FSPathResult((File) null);
    result.getFile();
  }
  
  // Similar test cases for other getter methods with null result objects
  
  @Test
  public void testToStringWithNullFile() {
    File file = null;
    FSPathResult result = new FSPathResult(file);
  
    Assert.assertEquals("FSPathResult : empty", result.toString());
  }
  
  // Additional test cases for edge cases and exception handling scenarios
  
}