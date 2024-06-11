import org.junit.Assert;
import org.junit.Test;

public class FSPathResultTest {

  @Test
  public void testGetFile() {
    File file = new File("test.txt");
    FSPathResult result = new FSPathResult(file);
    Assert.assertEquals("test.txt", result.getFile().getName());
  }

  @Test
  public void testGetString() {
    FSPathResult result = new FSPathResult("Hello");
    Assert.assertEquals("Hello", result.getString());
  }

  @Test
  public void testGetDouble() {
    FSPathResult result = new FSPathResult(3.14);
    Assert.assertEquals(3.14, result.getDouble(), 0.01);
  }

  @Test
  public void testGetDate() {
    Date date = new Date();
    FSPathResult result = new FSPathResult(date);
    Assert.assertEquals(date, result.getDate());
  }

  @Test
  public void testGetURI() {
    URI uri = URI.create("http://www.example.com");
    FSPathResult result = new FSPathResult(uri);
    Assert.assertEquals(uri, result.getURI());
  }

  @Test
  public void testGetBoolean() {
    FSPathResult result = new FSPathResult(true);
    Assert.assertTrue(result.getBoolean());
  }

  @Test
  public void testToString() {
    File file = new File("test.txt");
    FSPathResult result = new FSPathResult(file);
    Assert.assertEquals("test.txt", result.toString());
  }
}