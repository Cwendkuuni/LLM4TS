
import org.junit.Test;
import org.junit.Assert;
import java.text.ParseException;

public class TypeHandlerTest {

    @Test
    public void testCreateValueWithStringObj() {
      String str = "test";
      Object obj = "String";
      try {
        Object result = TypeHandler.createValue(str, obj);
        Assert.assertEquals("Result should be String", "test", result);
      } catch (ParseException e) {
        Assert.fail("Should not throw an exception");
      }
    }

    @Test
    public void testCreateValueWithStringClass() {
      String str = "test";
      Class<?> clazz = String.class;
      try {
        Object result = TypeHandler.createValue(str, clazz);
        Assert.assertEquals("Result should be String", "test", result);
      } catch (ParseException e) {
        Assert.fail("Should not throw an exception");
      }
    }

    @Test
    public void testCreateValueWithObject() {
      String str = "java.lang.String";
      try {
        Object result = TypeHandler.createObject(str);
        Assert.assertEquals("Result should be instance of String", String.class, result.getClass());
      } catch (ParseException e) {
        Assert.fail("Should not throw an exception");
      }
    }

    @Test
    public void testCreateValueWithNumberLong() {
      String str = "10";
      try {
        Number result = TypeHandler.createNumber(str);
        Assert.assertEquals("Result should be Long", Long.valueOf(10), result);
      } catch (ParseException e) {
        Assert.fail("Should not throw an exception");
      }
    }

    @Test
    public void testCreateValueWithNumberDouble() {
      String str = "10.5";
      try {
        Number result = TypeHandler.createNumber(str);
        Assert.assertEquals("Result should be Double", Double.valueOf(10.5), result);
      } catch (ParseException e) {
        Assert.fail("Should not throw an exception");
      }
    }

    @Test
    public void testCreateValueWithClass() {
      String str = "java.lang.String";
      try {
        Class<?> result = TypeHandler.createClass(str);
        Assert.assertEquals("Result should be String.class", String.class, result);
      } catch (ParseException e) {
        Assert.fail("Should not throw an exception");
      }
    }

    @Test
    public void testCreateURL() {
      String str = "http://www.example.com";
      try {
        URL result = TypeHandler.createURL(str);
        Assert.assertEquals("Result should be URL", new URL(str), result);
      } catch (ParseException | MalformedURLException e) {
        Assert.fail("Should not throw an exception");
      }
    }

    @Test
    public void testCreateFile() {
      String str = "test.txt";
      File result = TypeHandler.createFile(str);
      Assert.assertEquals("Result should be File with name test.txt", str, result.getName());
    }

    @Test
    public void testCreateFiles() {
      String str = "test.txt,example.txt";
      File[] result = TypeHandler.createFiles(str);
      Assert.assertEquals("Result should be File array with two files", 2, result.length);
      Assert.assertEquals("Result should be File with name test.txt", "test.txt", result[0].getName());
      Assert.assertEquals("Result should be File with name example.txt", "example.txt", result[1].getName());
    }
}