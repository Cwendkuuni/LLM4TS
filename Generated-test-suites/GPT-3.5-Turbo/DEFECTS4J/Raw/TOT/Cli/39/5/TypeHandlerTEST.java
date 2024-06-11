import org.junit.Assert;
import org.junit.Test;

public class TypeHandlerTest {
  
  @Test
  public void testCreateValueWithString() throws ParseException {
    String commandLineValue = "testString";
    Object expected = "testString";
    Object result = TypeHandler.createValue(commandLineValue, String.class);
    
    Assert.assertEquals(expected, result);
  }
  
  @Test
  public void testCreateValueWithObject() throws ParseException {
    String commandLineValue = "org.apache.commons.cli.TypeHandler";
    Object expected = new TypeHandler();
    Object result = TypeHandler.createValue(commandLineValue, Object.class);
    
    Assert.assertEquals(expected.getClass(), result.getClass());
  }
  
  @Test
  public void testCreateValueWithNumber() throws ParseException {
    String commandLineValue1 = "10";
    String commandLineValue2 = "10.5";
    Number expected1 = 10L;
    Number expected2 = 10.5;
    Number result1 = TypeHandler.createValue(commandLineValue1, Number.class);
    Number result2 = TypeHandler.createValue(commandLineValue2, Number.class);
    
    Assert.assertEquals(expected1, result1);
    Assert.assertEquals(expected2, result2);
  }
  
  @Test
  public void testCreateValueWithDate() throws ParseException {
    String commandLineValue = "2021-01-01";
    Date expected = null;
    Date result = TypeHandler.createValue(commandLineValue, Date.class);
    
    Assert.assertEquals(expected, result);
  }
  
  @Test
  public void testCreateValueWithClass() throws ParseException {
    String commandLineValue = "java.lang.String";
    Class<?> expected = String.class;
    Class<?> result = TypeHandler.createValue(commandLineValue, Class.class);
    
    Assert.assertEquals(expected, result);
  }
  
  @Test
  public void testCreateValueWithFile() throws ParseException {
    String commandLineValue = "test.txt";
    File expected = new File("test.txt");
    File result = TypeHandler.createValue(commandLineValue, File.class);
    
    Assert.assertEquals(expected, result);
  }
  
  @Test
  public void testCreateValueWithExistingFile() throws ParseException {
    String commandLineValue = "existingFile.txt";
    File expected = new File("existingFile.txt");
    File result = TypeHandler.createValue(commandLineValue, File.class);
    
    Assert.assertEquals(expected, result);
  }
  
  @Test
  public void testCreateValueWithFiles() throws ParseException {
    String commandLineValue = "file1.txt,file2.txt";
    File[] expected = null;
    File[] result = TypeHandler.createValue(commandLineValue, File[].class);
    
    Assert.assertEquals(expected, result);
  }
  
  @Test
  public void testCreateValueWithURL() throws ParseException {
    String commandLineValue = "http://www.example.com";
    URL expected = new URL("http://www.example.com");
    URL result = TypeHandler.createValue(commandLineValue, URL.class);
    
    Assert.assertEquals(expected, result);
  }
  
  @Test(expected = ParseException.class)
  public void testCreateValueWithInvalidURL() throws ParseException {
    String commandLineValue = "example.com";
    TypeHandler.createValue(commandLineValue, URL.class);
  }
  
  @Test(expected = ParseException.class)
  public void testCreateValueWithInvalidClass() throws ParseException {
    String commandLineValue = "InvalidClass";
    TypeHandler.createValue(commandLineValue, Class.class);
  }
  
  @Test(expected = ParseException.class)
  public void testCreateValueWithInvalidFile() throws ParseException {
    String commandLineValue = "InvalidFile.txt";
    TypeHandler.createValue(commandLineValue, File.class);
  }
  
  @Test(expected = ParseException.class)
  public void testCreateValueWithInvalidExistingFile() throws ParseException {
    String commandLineValue = "InvalidExistingFile.txt";
    TypeHandler.createValue(commandLineValue, File.class);
  }
  
  @Test(expected = ParseException.class)
  public void testCreateValueWithInvalidFiles() throws ParseException {
    String commandLineValue = "InvalidFile1.txt,InvalidFile2.txt";
    TypeHandler.createValue(commandLineValue, File[].class);
  }
  
  @Test
  public void testCreateObjectWithValidClass() throws ParseException {
    String classname = "java.lang.String";
    Object expected = "";
    Object result = TypeHandler.createObject(classname);
    
    Assert.assertEquals(expected, result);
  }
  
  @Test(expected = ParseException.class)
  public void testCreateObjectWithInvalidClass() throws ParseException {
    String classname = "InvalidClass";
    TypeHandler.createObject(classname);
  }
  
  @Test
  public void testCreateNumberWithIntegerString() throws ParseException {
    String str = "10";
    Number expected = 10L;
    Number result = TypeHandler.createNumber(str);
    
    Assert.assertEquals(expected, result);
  }
  
  @Test
  public void testCreateNumberWithDecimalString() throws ParseException {
    String str = "10.5";
    Number expected = 10.5;
    Number result = TypeHandler.createNumber(str);
    
    Assert.assertEquals(expected, result);
  }
  
  @Test(expected = ParseException.class)
  public void testCreateNumberWithInvalidString() throws ParseException {
    String str = "invalidNumber";
    TypeHandler.createNumber(str);
  }
  
  @Test
  public void testCreateClassWithValidClass() throws ParseException {
    String classname = "java.lang.String";
    Class<?> expected = String.class;
    Class<?> result = TypeHandler.createClass(classname);
    
    Assert.assertEquals(expected, result);
  }
  
  @Test(expected = ParseException.class)
  public void testCreateClassWithInvalidClass() throws ParseException {
    String classname = "InvalidClass";
    TypeHandler.createClass(classname);
  }
  
  @Test
  public void testCreateDate() throws ParseException {
    String str = "2021-01-01";
    Date expected = null;
    Date result = TypeHandler.createDate(str);
    
    Assert.assertEquals(expected, result);
  }
  
  @Test
  public void testCreateURLWithValidURL() throws ParseException {
    String str = "http://www.example.com";
    URL expected = new URL("http://www.example.com");
    URL result = TypeHandler.createURL(str);
    
    Assert.assertEquals(expected, result);
  }
  
  @Test(expected = ParseException.class)
  public void testCreateURLWithInvalidURL() throws ParseException {
    String str = "example.com";
    TypeHandler.createURL(str);
  }
  
  @Test
  public void testCreateFile() throws ParseException {
    String str = "test.txt";
    File expected = new File("test.txt");
    File result = TypeHandler.createFile(str);
    
    Assert.assertEquals(expected, result);
  }
  
  @Test
  public void testCreateFiles() throws ParseException {
    String str = "file1.txt,file2.txt";
    File[] expected = null;
    File[] result = TypeHandler.createFiles(str);
    
    Assert.assertEquals(expected, result);
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void testCreateFilesUnsupported() throws ParseException {
    String str = "file1.txt,file2.txt";
    TypeHandler.createFiles(str);
  }
}