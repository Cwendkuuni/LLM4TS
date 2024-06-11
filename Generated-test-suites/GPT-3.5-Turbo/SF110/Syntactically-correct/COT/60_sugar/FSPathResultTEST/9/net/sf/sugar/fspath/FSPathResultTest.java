
import static org.junit.Assert.*;
import org.junit.Test;
import java.io.File;
import java.net.URI;
import java.util.Date;

public class FSPathResultTest {
  
  @Test
  public void testGetFile() {
    File file = new File("test.txt");
    FSPathResult result = new FSPathResult(file);
    
    assertEquals(file, result.getFile());
  }
  
  @Test
  public void testGetString() {
    String str = "test";
    FSPathResult result = new FSPathResult(str);
    
    assertEquals(str, result.getString());
  }
  
  @Test
  public void testGetDouble() {
    Double dou = 10.5;
    FSPathResult result = new FSPathResult(dou);

    assertEquals(dou, result.getDouble());
  }
  
  @Test
  public void testGetDate() {
    Date date = new Date();
    FSPathResult result = new FSPathResult(date);
    
    assertEquals(date, result.getDate());
  }
  
  @Test
  public void testGetURI() {
    URI uri = new URI("https://www.example.com");
    FSPathResult result = new FSPathResult(uri);
    
    assertEquals(uri, result.getURI());
  }
  
  @Test
  public void testGetBoolean() {
    Boolean bool = false;
    FSPathResult result = new FSPathResult(bool);
    
    assertEquals(bool, result.getBoolean());
  }
  
  @Test
  public void testToString() {
    File file = new File("test.txt");
    FSPathResult result = new FSPathResult(file);
    
    assertEquals(file.getAbsolutePath(), result.toString());
  }
  
  @Test
  public void testGetFile_NullFile() {
    FSPathResult result = new FSPathResult(null);
    
    assertNull(result.getFile());
  }

  @Test
  public void testGetDouble_NullDouble() {
    FSPathResult result = new FSPathResult((Double) null);
    
    assertNull(result.getDouble());
  }

  @Test
  public void testGetDate_NullDate() {
    FSPathResult result = new FSPathResult((Date) null);
    
    assertNull(result.getDate());
  }

  @Test
  public void testGetBoolean_NullBoolean() {
    FSPathResult result = new FSPathResult((Boolean) null);
    
    assertNull(result.getBoolean());
  }

  @Test
  public void testToString_NullValues() {
    FSPathResult result = new FSPathResult((File) null);
    
    assertEquals("FSPathResult : empty", result.toString());
  }
}
