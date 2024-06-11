import org.junit.Test;
import static org.junit.Assert.*;

public class FSPathResultTest {

  @Test
  public void testGetFile() {
    File file = new File("testFile.txt");
    FSPathResult result = new FSPathResult(file);
    assertEquals(file, result.getFile());
  }

  @Test
  public void testGetString() {
    String string = "testString";
    FSPathResult result = new FSPathResult(string);
    assertEquals(string, result.getString());
  }

  @Test
  public void testGetDouble() {
    Double doub = 3.14;
    FSPathResult result = new FSPathResult(doub);
    assertEquals(doub, result.getDouble());
  }

  @Test
  public void testGetDate() {
    Date date = new Date();
    FSPathResult result = new FSPathResult(date);
    assertEquals(date, result.getDate());
  }

  @Test
  public void testGetURI() {
    URI uri = URI.create("http://www.example.com");
    FSPathResult result = new FSPathResult(uri);
    assertEquals(uri, result.getURI());
  }

  @Test
  public void testGetBoolean() {
    Boolean bool = true;
    FSPathResult result = new FSPathResult(bool);
    assertEquals(bool, result.getBoolean());
  }

  @Test
  public void testToString() {
    File file = new File("testFile.txt");
    FSPathResult result = new FSPathResult(file);
    assertEquals(file.getAbsolutePath(), result.toString());
  }
}