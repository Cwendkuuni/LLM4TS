import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.cli.ParseException;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

public class TypeHandlerTest {

  @Test
  public void testCreateValue_stringValue() throws ParseException {
    String str = "test";
    Object obj = PatternOptionBuilder.STRING_VALUE;
    Object result = TypeHandler.createValue(str, obj);
    assertEquals(str, result);
  }

  @Test
  public void testCreateValue_objectValue() throws ParseException {
    String str = "test";
    Object obj = PatternOptionBuilder.OBJECT_VALUE;
    Object result = TypeHandler.createValue(str, obj);
    assertEquals(null, result);
  }

  @Test
  public void testCreateValue_numberValue() throws ParseException {
    String str = "10";
    Object obj = PatternOptionBuilder.NUMBER_VALUE;
    Object result = TypeHandler.createValue(str, obj);
    assertEquals(10L, result);
  }

  @Test
  public void testCreateValue_dateValue() throws ParseException {
    String str = "2021-01-01";
    Object obj = PatternOptionBuilder.DATE_VALUE;
    Object result = TypeHandler.createValue(str, obj);
    assertEquals(null, result);
  }

  @Test
  public void testCreateValue_classValue() throws ParseException {
    String str = "java.lang.String";
    Object obj = PatternOptionBuilder.CLASS_VALUE;
    Object result = TypeHandler.createValue(str, obj);
    assertEquals(String.class, result);
  }

  @Test
  public void testCreateValue_fileValue() throws ParseException {
    String str = "test.txt";
    Object obj = PatternOptionBuilder.FILE_VALUE;
    Object result = TypeHandler.createValue(str, obj);
    assertEquals(new File(str), result);
  }

  @Test
  public void testCreateValue_existingFileValue() throws ParseException {
    String str = "test.txt";
    Object obj = PatternOptionBuilder.EXISTING_FILE_VALUE;
    Object result = TypeHandler.createValue(str, obj);
    assertEquals(new File(str), result);
  }

  @Test
  public void testCreateValue_filesValue() throws ParseException {
    String str = "test1.txt,test2.txt";
    Object obj = PatternOptionBuilder.FILES_VALUE;
    Object result = TypeHandler.createValue(str, obj);
    assertEquals(null, result);
  }

  @Test
  public void testCreateValue_urlValue() throws ParseException {
    String str = "http://www.example.com";
    Object obj = PatternOptionBuilder.URL_VALUE;
    Object result = TypeHandler.createValue(str, obj);
    assertEquals(new URL(str), result);
  }

  @Test
  public void testCreateValue_unknownValue() throws ParseException {
    String str = "test";
    Object obj = new Object();
    Object result = TypeHandler.createValue(str, obj);
    assertEquals(null, result);
  }

  @Test
  public void testCreateObject_validClassname() throws ParseException {
    String classname = "java.lang.String";
    Object result = TypeHandler.createObject(classname);
    assertEquals(String.class, result);
  }

  @Test(expected = ParseException.class)
  public void testCreateObject_invalidClassname() throws ParseException {
    String classname = "InvalidClass";
    TypeHandler.createObject(classname);
  }

  @Test
  public void testCreateNumber_integer() throws ParseException {
    String str = "10";
    Number result = TypeHandler.createNumber(str);
    assertEquals(10L, result);
  }

  @Test
  public void testCreateNumber_decimal() throws ParseException {
    String str = "10.5";
    Number result = TypeHandler.createNumber(str);
    assertEquals(10.5, result);
  }

  @Test(expected = ParseException.class)
  public void testCreateNumber_invalidNumber() throws ParseException {
    String str = "abc";
    TypeHandler.createNumber(str);
  }

  @Test
  public void testCreateClass_validClassname() throws ParseException {
    String classname = "java.lang.String";
    Class<?> result = TypeHandler.createClass(classname);
    assertEquals(String.class, result);
  }

  @Test(expected = ParseException.class)
  public void testCreateClass_invalidClassname() throws ParseException {
    String classname = "InvalidClass";
    TypeHandler.createClass(classname);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testCreateDate_notImplemented() {
    String str = "2021-01-01";
    TypeHandler.createDate(str);
  }

  @Test
  public void testCreateURL_validURL() throws ParseException {
    String str = "http://www.example.com";
    URL result = TypeHandler.createURL(str);
    assertEquals(new URL(str), result);
  }

  @Test(expected = ParseException.class)
  public void testCreateURL_invalidURL() throws ParseException {
    String str = "InvalidURL";
    TypeHandler.createURL(str);
  }

  @Test
  public void testCreateFile() {
    String str = "test.txt";
    File result = TypeHandler.createFile(str);
    assertEquals(new File(str), result);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testCreateFiles_notImplemented() {
    String str = "test1.txt,test2.txt";
    TypeHandler.createFiles(str);
  }
  
}