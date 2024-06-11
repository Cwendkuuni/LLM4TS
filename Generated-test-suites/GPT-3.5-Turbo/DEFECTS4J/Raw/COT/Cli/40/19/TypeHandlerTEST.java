
import org.junit.Test;
import org.apache.commons.cli.PatternOptionBuilder;

public class TypeHandlerTest {

  @Test
  public void testCreateValue_String_Object() {
    try {
      Object result = TypeHandler.createValue("test", PatternOptionBuilder.STRING_VALUE);
      Assert.assertEquals("test", result);
    } catch (ParseException e) {
      Assert.fail("Unexpected exception: " + e.getMessage());
    }
  }

  @Test
  public void testCreateValue_String_Class() {
    try {
      String result = TypeHandler.createValue("10", String.class);
      Assert.assertEquals("10", result);
    } catch (ParseException e) {
      Assert.fail("Unexpected exception: " + e.getMessage());
    }
  }

  @Test
  public void testCreateValue_Object() {
    try {
      Object result = TypeHandler.createObject("java.lang.Integer");
      Assert.assertNotNull(result);
      Assert.assertEquals(Integer.class, result.getClass());
    } catch (ParseException e) {
      Assert.fail("Unexpected exception: " + e.getMessage());
    }
  }

  @Test
  public void testCreateNumber_Long() {
    try {
      Number result = TypeHandler.createNumber("10");
      Assert.assertTrue(result instanceof Long);
      Assert.assertEquals(10L, result.longValue());
    } catch (ParseException e) {
      Assert.fail("Unexpected exception: " + e.getMessage());
    }
  }

  @Test
  public void testCreateNumber_Double() {
    try {
      Number result = TypeHandler.createNumber("10.5");
      Assert.assertTrue(result instanceof Double);
      Assert.assertEquals(10.5, result.doubleValue(), 0.01);
    } catch (ParseException e) {
      Assert.fail("Unexpected exception: " + e.getMessage());
    }
  }

  @Test
  public void testCreateClass() {
    try {
      Class<?> result = TypeHandler.createClass("java.lang.String");
      Assert.assertEquals(String.class, result);
    } catch (ParseException e) {
      Assert.fail("Unexpected exception: " + e.getMessage());
    }
  }

  @Test
  public void testCreateURL() {
    try {
      URL result = TypeHandler.createURL("http://www.example.com");
      Assert.assertNotNull(result);
      Assert.assertEquals(new URL("http://www.example.com"), result);
    } catch (ParseException e) {
      Assert.fail("Unexpected exception: " + e.getMessage());
    } catch (MalformedURLException e) {
      Assert.fail("Unexpected exception: " + e.getMessage());
    }
  }

  @Test
  public void testCreateFile() {
    File result = TypeHandler.createFile("test.txt");
    Assert.assertNotNull(result);
    Assert.assertTrue(result instanceof File);
    Assert.assertEquals("test.txt", result.getPath());
  }

  @Test
  public void testOpenFile() {
    try {
      FileInputStream result = TypeHandler.openFile("test.txt");
      Assert.assertNotNull(result);
    } catch (ParseException e) {
      Assert.fail("Unexpected exception: " + e.getMessage());
    }
  }

  @Test
  public void testCreateFiles() {
    try {
      File[] result = TypeHandler.createFiles("test1.txt,test2.txt");
      Assert.fail("UnsupportedOperationException should be thrown");
    } catch (UnsupportedOperationException e) {
      // expected exception
    }
  }
}

