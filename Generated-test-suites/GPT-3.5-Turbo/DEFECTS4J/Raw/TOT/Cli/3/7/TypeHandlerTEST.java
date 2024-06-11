
import org.junit.*;
import static org.junit.Assert.*;

public class TypeHandlerTest {
  
  @Test
  public void createValue_stringType_returnsStringObject() {
    String value = "test";
    Object result = TypeHandler.createValue(value, PatternOptionBuilder.STRING_VALUE);
    assertEquals(String.class, result.getClass());
    assertEquals(value, result);
  }
  
  @Test
  public void createValue_objectType_returnsObjectOfType() {
    String value = "java.lang.String";
    Object result = TypeHandler.createValue(value, PatternOptionBuilder.OBJECT_VALUE);
    assertEquals(Class.class, result.getClass());
    assertEquals(value, ((Class)result).getName());
  }
  
  @Test
  public void createValue_numberType_withIntegerValue_returnsLongObject() {
    String value = "123";
    Object result = TypeHandler.createValue(value, PatternOptionBuilder.NUMBER_VALUE);
    assertEquals(Long.class, result.getClass());
    assertEquals(Long.valueOf(value), result);
  }
  
  @Test
  public void createValue_numberType_withDecimalValue_returnsDoubleObject() {
    String value = "12.34";
    Object result = TypeHandler.createValue(value, PatternOptionBuilder.NUMBER_VALUE);
    assertEquals(Double.class, result.getClass());
    assertEquals(Double.valueOf(value), result);
  }
  
  @Test
  public void createValue_dateType_withValidDate_returnsDateObject() {
    String value = "2021-01-01";
    Object result = TypeHandler.createValue(value, PatternOptionBuilder.DATE_VALUE);
    assertEquals(Date.class, result.getClass());
    // assert remaining assertions for Date
  }
  
  @Test
  public void createValue_dateType_withInvalidDate_returnsNull() {
    String value = "invalid date";
    Object result = TypeHandler.createValue(value, PatternOptionBuilder.DATE_VALUE);
    assertNull(result);
  }
  
  @Test
  public void createValue_classType_returnsClassObject() {
    String value = "java.lang.String";
    Object result = TypeHandler.createValue(value, PatternOptionBuilder.CLASS_VALUE);
    assertEquals(Class.class, result.getClass());
    assertEquals(value, ((Class)result).getName());
  }
  
  @Test
  public void createValue_fileType_returnsFileObject() {
    String value = "/path/to/file.txt";
    Object result = TypeHandler.createValue(value, PatternOptionBuilder.FILE_VALUE);
    assertEquals(File.class, result.getClass());
    assertEquals(value, ((File)result).getPath());
  }
  
  @Test
  public void createValue_existingFileType_returnsFileObject() {
    String value = "/path/to/file.txt";
    Object result = TypeHandler.createValue(value, PatternOptionBuilder.EXISTING_FILE_VALUE);
    assertEquals(File.class, result.getClass());
    assertEquals(value, ((File)result).getPath());
  }
  
  @Test
  public void createValue_filesType_returnsArrayOfFileObjects() {
    String value = "/path/to/file1.txt,/path/to/file2.txt";
    Object result = TypeHandler.createValue(value, PatternOptionBuilder.FILES_VALUE);
    assertEquals(File[].class, result.getClass());
    File[] files = (File[]) result;
    assertEquals(2, files.length);
    assertEquals("/path/to/file1.txt", files[0].getPath());
    assertEquals("/path/to/file2.txt", files[1].getPath());
  }
  
  @Test
  public void createValue_urlType_withValidURL_returnsURLObject() {
    String value = "http://example.com";
    Object result = TypeHandler.createValue(value, PatternOptionBuilder.URL_VALUE);
    assertEquals(URL.class, result.getClass());
    // assert remaining assertions for URL
  }
  
  @Test
  public void createValue_urlType_withInvalidURL_returnsNull() {
    String value = "invalid url";
    Object result = TypeHandler.createValue(value, PatternOptionBuilder.URL_VALUE);
    assertNull(result);
  }
}

