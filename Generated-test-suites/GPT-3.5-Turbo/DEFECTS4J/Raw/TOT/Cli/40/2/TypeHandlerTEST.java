import org.junit.Assert;
import org.junit.Test;

public class TypeHandlerTest {

  @Test
  public void testCreateValue_StringValue_ReturnsString() throws ParseException {
    String input = "test";
    Object expected = "test";
    
    Object result = TypeHandler.createValue(input, PatternOptionBuilder.STRING_VALUE);
    
    Assert.assertEquals(expected, result);
  }

  @Test
  public void testCreateValue_ObjectValue_ReturnsObject() throws ParseException {
    String input = "java.lang.String";
    Object expected = TypeHandler.createObject(input);
    
    Object result = TypeHandler.createValue(input, PatternOptionBuilder.OBJECT_VALUE);
    
    Assert.assertEquals(expected, result);
  }

  @Test
  public void testCreateValue_NumberValue_ReturnsNumber() throws ParseException {
    String input = "10";
    Number expected = 10L;
    
    Number result = TypeHandler.createValue(input, PatternOptionBuilder.NUMBER_VALUE);
    
    Assert.assertEquals(expected, result);
  }

  @Test
  public void testCreateValue_DateValue_ThrowsUnsupportedOperationException() {
    String input = "2021-01-01";
    
    try {
      TypeHandler.createValue(input, PatternOptionBuilder.DATE_VALUE);
      Assert.fail("Expected ParseException");
    } catch (ParseException e) {
      // Test passed
    }
  }

  @Test
  public void testCreateValue_ClassValue_ReturnsClass() throws ParseException {
    String input = "java.lang.String";
    Class<?> expected = String.class;
    
    Class<?> result = TypeHandler.createValue(input, PatternOptionBuilder.CLASS_VALUE);
    
    Assert.assertEquals(expected, result);
  }

  @Test
  public void testCreateValue_FileValue_ReturnsFile() throws ParseException {
    String input = "/path/to/file.txt";
    File expected = new File(input);
    
    File result = TypeHandler.createValue(input, PatternOptionBuilder.FILE_VALUE);
    
    Assert.assertEquals(expected, result);
  }

  @Test
  public void testCreateValue_ExistingFileValue_ReturnsFileInputStream() throws ParseException {
    String input = "/path/to/file.txt";
    
    FileInputStream result = TypeHandler.createValue(input, PatternOptionBuilder.EXISTING_FILE_VALUE);
    
    Assert.assertNotNull(result);
  }

  @Test
  public void testCreateValue_FilesValue_ThrowsUnsupportedOperationException() {
    String input = "/path/to/file1.txt,/path/to/file2.txt";
    
    try {
      TypeHandler.createValue(input, PatternOptionBuilder.FILES_VALUE);
      Assert.fail("Expected UnsupportedOperationException");
    } catch (UnsupportedOperationException e) {
      // Test passed
    }
  }

  @Test
  public void testCreateValue_URLValue_ReturnsURL() throws ParseException {
    String input = "https://www.example.com";
    URL expected = new URL(input);
    
    URL result = TypeHandler.createValue(input, PatternOptionBuilder.URL_VALUE);
    
    Assert.assertEquals(expected, result);
  }

  @Test
  public void testCreateValue_UnknownValue_ReturnsNull() throws ParseException {
    String input = "unknown";
    Object expected = null;
    
    Object result = TypeHandler.createValue(input, Object.class);
    
    Assert.assertEquals(expected, result);
  }

  @Test
  public void testCreateObject_ValidClass_ReturnsObject() throws ParseException {
    String input = "java.lang.String";
    
    Object result = TypeHandler.createObject(input);
    
    Assert.assertNotNull(result);
    Assert.assertEquals(input, result.getClass().getName());
  }

  @Test
  public void testCreateObject_InvalidClass_ThrowsParseException() {
    String input = "invalid.class.Name";
    
    try {
      TypeHandler.createObject(input);
      Assert.fail("Expected ParseException");
    } catch (ParseException e) {
      // Test passed
    }
  }

  @Test
  public void testCreateNumber_IntegerString_ReturnsLong() throws ParseException {
    String input = "10";
    Number expected = 10L;
    
    Number result = TypeHandler.createNumber(input);
    
    Assert.assertEquals(expected, result);
  }

  @Test
  public void testCreateNumber_DecimalString_ReturnsDouble() throws ParseException {
    String input = "3.14";
    Number expected = 3.14;
    
    Number result = TypeHandler.createNumber(input);
    
    Assert.assertEquals(expected, result);
  }

  @Test
  public void testCreateNumber_InvalidNumberString_ThrowsParseException() {
    String input = "invalid";
    
    try {
      TypeHandler.createNumber(input);
      Assert.fail("Expected ParseException");
    } catch (ParseException e) {
      // Test passed
    }
  }

  @Test
  public void testCreateClass_ExistentClass_ReturnsClass() throws ParseException {
    String input = "java.lang.String";
    Class<?> expected = String.class;
    
    Class<?> result = TypeHandler.createClass(input);
    
    Assert.assertEquals(expected, result);
  }

  @Test
  public void testCreateClass_NonexistentClass_ThrowsParseException() {
    String input = "non.existent.Class";
    
    try {
      TypeHandler.createClass(input);
      Assert.fail("Expected ParseException");
    } catch (ParseException e) {
      // Test passed
    }
  }

  @Test
  public void testCreateURL_ValidURL_ReturnsURL() throws ParseException {
    String input = "https://www.example.com";
    URL expected = new URL(input);
    
    URL result = TypeHandler.createURL(input);
    
    Assert.assertEquals(expected, result);
  }

  @Test
  public void testCreateURL_InvalidURL_ThrowsParseException() {
    String input = "invalid.url";
    
    try {
      TypeHandler.createURL(input);
      Assert.fail("Expected ParseException");
    } catch (ParseException e) {
      // Test passed
    }
  }

  @Test
  public void testCreateFile_ValidFilePath_ReturnsFile() throws ParseException {
    String input = "/path/to/file.txt";
    File expected = new File(input);
    
    File result = TypeHandler.createFile(input);
    
    Assert.assertEquals(expected, result);
  }

  @Test
  public void testCreateFiles_ValidFilePath_ReturnsFileArray() throws ParseException {
    String input = "/path/to/file1.txt,/path/to/file2.txt";
    File[] expected = new File[] { new File("/path/to/file1.txt"), new File("/path/to/file2.txt") };
    
    File[] result = TypeHandler.createFiles(input);
    
    Assert.assertArrayEquals(expected, result);
  }

  @Test
  public void testCreateFiles_NotImplemented_ThrowsUnsupportedOperationException() {
    String input = "/path/to/file.txt";
    
    try {
      TypeHandler.createFiles(input);
      Assert.fail("Expected UnsupportedOperationException");
    } catch (UnsupportedOperationException e) {
      // Test passed
    }
  }

  @Test
  public void testOpenFile_ValidFilePath_ReturnsFileInputStream() throws ParseException {
    String input = "/path/to/file.txt";
    
    try (FileInputStream result = TypeHandler.openFile(input)) {
      Assert.assertNotNull(result);
    } catch (FileNotFoundException e) {
      Assert.fail("Expected ParseException");
    }
  }

  @Test
  public void testOpenFile_InvalidFilePath_ThrowsParseException() {
    String input = "/invalid/path/to/file.txt";
    
    try {
      TypeHandler.openFile(input);
      Assert.fail("Expected ParseException");
    } catch (ParseException e) {
      // Test passed
    }
  }

  @Test
  public void testCreateDate_ValidDateString_ThrowsUnsupportedOperationException() {
    String input = "2021-01-01";
    
    try {
      TypeHandler.createDate(input);
      Assert.fail("Expected UnsupportedOperationException");
    } catch (UnsupportedOperationException e) {
      // Test passed
    }
  }
}