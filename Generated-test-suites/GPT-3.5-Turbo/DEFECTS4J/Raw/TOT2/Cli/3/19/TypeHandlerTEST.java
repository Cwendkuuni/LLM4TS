import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.cli.TypeHandler;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

public class TypeHandlerTest {

  @Test
  public void testCreateValue_Object() {
    Object obj = new Object();
    Object value = TypeHandler.createValue("test", obj);
    assertEquals(obj, value);
  }

  @Test
  public void testCreateValue_Class() {
    Class clazz = Object.class;
    Object value = TypeHandler.createValue("test", clazz);
    assertTrue(clazz.isInstance(value));
  }

  @Test
  public void testCreateNumber() {
    Number number = TypeHandler.createNumber("10");
    assertNotNull(number);
    assertEquals(10, number);
  }

  @Test
  public void testCreateClass() {
    Class clazz = TypeHandler.createClass("java.lang.Object");
    assertNotNull(clazz);
    assertEquals(Object.class, clazz);
  }

  @Test
  public void testCreateDate() {
    Date date = TypeHandler.createDate("2021-09-15");
    assertNotNull(date);
  }

  @Test
  public void testCreateURL() {
    try {
      URL url = TypeHandler.createURL("http://www.example.com");
      assertNotNull(url);
    } catch (MalformedURLException e) {
      fail("MalformedURLException should not be thrown");
    }
  }

  @Test
  public void testCreateFile() {
    File file = TypeHandler.createFile("test.txt");
    assertNotNull(file);
    assertTrue(file.exists());
  }

  @Test
  public void testCreateFiles() {
    File[] files = TypeHandler.createFiles("c:\\dir\\file1.txt, c:\\dir\\file2.txt");
    assertNotNull(files);
    assertEquals(2, files.length);
  }
}