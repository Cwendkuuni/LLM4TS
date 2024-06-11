import static org.junit.Assert.*;
import org.junit.Test;

public class TypeHandlerTest {
  
  @Test
  public void testCreateValueWithString() throws ParseException {
    String str = "test";
    Object obj = PatternOptionBuilder.STRING_VALUE;
    Object result = TypeHandler.createValue(str, obj);
    assertEquals(str, result);
  }
  
  @Test
  public void testCreateValueWithObject() throws ParseException {
    String classname = "java.lang.Object";
    Object result = TypeHandler.createObject(classname);
    assertEquals(Object.class, result.getClass());
  }
  
  @Test
  public void testCreateValueWithNumber() throws ParseException {
    String str = "123";
    Object result = TypeHandler.createNumber(str);
    assertEquals(Long.class, result.getClass());
    assertEquals(123L, result);
    
    str = "123.45";
    result = TypeHandler.createNumber(str);
    assertEquals(Double.class, result.getClass());
    assertEquals(123.45, result);
  }
  
  @Test(expected = ParseException.class)
  public void testCreateValueWithInvalidNumber() throws ParseException {
    String str = "abc";
    TypeHandler.createNumber(str);
  }
  
  @Test
  public void testCreateValueWithDate() {
    String str = "2020-01-01";
    Date result = TypeHandler.createDate(str);
    assertNull(result);
  }
  
  @Test
  public void testCreateValueWithClass() throws ParseException {
    String classname = "java.lang.String";
    Object result = TypeHandler.createClass(classname);
    assertEquals(String.class, result);
  }
  
  @Test(expected = ParseException.class)
  public void testCreateValueWithInvalidClass() throws ParseException {
    String classname = "invalid.class";
    TypeHandler.createClass(classname);
  }
  
  @Test
  public void testCreateValueWithURL() throws ParseException {
    String str = "http://example.com";
    URL result = TypeHandler.createURL(str);
    assertEquals(str, result.toString());
  }
  
  @Test(expected = ParseException.class)
  public void testCreateValueWithInvalidURL() throws ParseException {
    String str = "invalid_url";
    TypeHandler.createURL(str);
  }
  
  @Test
  public void testCreateValueWithFile() {
    String str = "test/file.txt";
    File result = TypeHandler.createFile(str);
    assertEquals(str, result.getPath());
  }
  
  @Test
  public void testCreateValueWithFiles() {
    String str = "test/*.txt";
    File[] result = TypeHandler.createFiles(str);
    // Verify the result here
    // ...
  }
  
  @Test(expected = ParseException.class)
  public void testCreateValueWithInvalidFiles() throws ParseException {
    String str = "invalid/files";
    TypeHandler.createFiles(str);
  }
}