import static org.junit.Assert.*;
import org.junit.Test;
import java.io.File;
import java.net.URI;
import java.util.Date;

public class FSPathResultTest {

  // File constructor test cases

  @Test
  public void testFileConstructor_NormalCase() {
    File file = new File("test.txt");
    FSPathResult result = new FSPathResult(file);
    assertEquals(file, result.getFile());
  }

  @Test
  public void testFileConstructor_NullFile() {
    FSPathResult result = new FSPathResult(null);
    assertNull(result.getFile());
  }

  // String constructor test cases

  @Test
  public void testStringConstructor_NormalCase() {
    String str = "String";
    FSPathResult result = new FSPathResult(str);
    assertEquals(str, result.getString());
  }

  @Test
  public void testStringConstructor_NullString() {
    FSPathResult result = new FSPathResult(null);
    assertNull(result.getString());
  }

  // Double constructor test cases

  @Test
  public void testDoubleConstructor_NormalCase() {
    Double d = 3.14;
    FSPathResult result = new FSPathResult(d);
    assertEquals(d, result.getDouble());
  }

  @Test
  public void testDoubleConstructor_NullDouble() {
    FSPathResult result = new FSPathResult(null);
    assertNull(result.getDouble());
  }

  // Date constructor test cases

  @Test
  public void testDateConstructor_NormalCase() {
    Date date = new Date();
    FSPathResult result = new FSPathResult(date);
    assertEquals(date, result.getDate());
  }

  @Test
  public void testDateConstructor_NullDate() {
    FSPathResult result = new FSPathResult(null);
    assertNull(result.getDate());
  }

  // URI constructor test cases

  @Test
  public void testURIConstructor_NormalCase() {
    URI uri = URI.create("http://www.example.com");
    FSPathResult result = new FSPathResult(uri);
    assertEquals(uri, result.getURI());
  }

  @Test
  public void testURIConstructor_NullURI() {
    FSPathResult result = new FSPathResult(null);
    assertNull(result.getURI());
  }

  // Boolean constructor test cases

  @Test
  public void testBooleanConstructor_NormalCase() {
    Boolean bool = true;
    FSPathResult result = new FSPathResult(bool);
    assertEquals(bool, result.getBoolean());
  }

  @Test
  public void testBooleanConstructor_NullBoolean() {
    FSPathResult result = new FSPathResult(null);
    assertNull(result.getBoolean());
  }

  // toString() method test cases

  @Test
  public void testToString_File() {
    File file = new File("test.txt");
    FSPathResult result = new FSPathResult(file);
    assertEquals(file.getAbsolutePath(), result.toString());
  }

  @Test
  public void testToString_String() {
    String str = "String";
    FSPathResult result = new FSPathResult(str);
    assertEquals(str, result.toString());
  }

  @Test
  public void testToString_Double() {
    Double d = 3.14;
    FSPathResult result = new FSPathResult(d);
    assertEquals(d.toString(), result.toString());
  }

  @Test
  public void testToString_Boolean() {
    Boolean bool = true;
    FSPathResult result = new FSPathResult(bool);
    assertEquals(bool.toString(), result.toString());
  }

  @Test
  public void testToString_Date() {
    Date date = new Date();
    FSPathResult result = new FSPathResult(date);
    assertEquals(date.toString(), result.toString());
  }

  @Test
  public void testToString_URI() {
    URI uri = URI.create("http://www.example.com");
    FSPathResult result = new FSPathResult(uri);
    assertEquals(uri.toString(), result.toString());
  }

  @Test
  public void testToString_EmptyResult() {
    FSPathResult result = new FSPathResult(null);
    assertEquals("FSPathResult : empty", result.toString());
  }
}