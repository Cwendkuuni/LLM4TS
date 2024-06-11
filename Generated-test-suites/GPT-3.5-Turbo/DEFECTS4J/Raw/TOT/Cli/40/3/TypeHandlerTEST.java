import org.junit.Test;
import org.junit.Before;
import org.junit.Assert;

import java.text.ParseException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import java.net.MalformedURLException;
import java.net.URL;

import java.util.Date;

public class TypeHandlerTest {

  @Test
  public void testCreateValue_String_StringValue() throws ParseException {
    String str = "Hello";
    Class<String> clazz = String.class;

    String result = TypeHandler.createValue(str, clazz);

    Assert.assertEquals(str, result);
  }

  @Test
  public void testCreateValue_String_ObjectValue() throws ParseException {
    String str = "TestClass";
    Class<Object> clazz = Object.class;

    Object result = TypeHandler.createValue(str, clazz);

    Assert.assertEquals(TestClass.class, result.getClass());
  }

  @Test
  public void testCreateValue_String_NumberValue_Integer() throws ParseException {
    String str = "100";
    Class<Number> clazz = Number.class;

    Number result = TypeHandler.createValue(str, clazz);

    Assert.assertEquals(Integer.valueOf(100), result);
  }

  @Test
  public void testCreateValue_String_NumberValue_Double() throws ParseException {
    String str = "3.14";
    Class<Number> clazz = Number.class;

    Number result = TypeHandler.createValue(str, clazz);

    Assert.assertEquals(Double.valueOf(3.14), result);
  }

  @Test
  public void testCreateValue_String_DateValue() {
    String str = "2022-01-01";
    Class<Date> clazz = Date.class;

    try {
      Date result = TypeHandler.createValue(str, clazz);
      Assert.fail("Should throw UnsupportedOperationException");
    } catch (UnsupportedOperationException e) {
      Assert.assertEquals("Not yet implemented", e.getMessage());
    }
  }

  @Test
  public void testCreateValue_String_ClassValue() throws ParseException {
    String str = "java.util.Date";
    Class<Class> clazz = Class.class;

    Class result = TypeHandler.createValue(str, clazz);

    Assert.assertEquals(Date.class, result);
  }

  @Test
  public void testCreateValue_String_FileValue() {
    String str = "path/to/file.txt";
    Class<File> clazz = File.class;

    File result = TypeHandler.createValue(str, clazz);

    Assert.assertEquals(new File(str), result);
  }

  @Test
  public void testCreateValue_String_ExistingFileValue() throws ParseException {
    String str = "path/to/existing/file.txt";
    Class<FileInputStream> clazz = FileInputStream.class;

    FileInputStream result = TypeHandler.createValue(str, clazz);

    Assert.assertNotNull(result);
  }

  @Test
  public void testCreateValue_String_FilesValue() {
    String str = "path/to/files";
    Class<File[]> clazz = File[].class;

    try {
      TypeHandler.createValue(str, clazz);
      Assert.fail("Should throw UnsupportedOperationException");
    } catch (UnsupportedOperationException e) {
      Assert.assertEquals("Not yet implemented", e.getMessage());
    }
  }

  @Test
  public void testCreateValue_String_URLValue() throws ParseException {
    String str = "https://www.example.com";
    Class<URL> clazz = URL.class;

    URL result = TypeHandler.createValue(str, clazz);

    Assert.assertEquals(new URL(str), result);
  }

  @Test
  public void testCreateValue_InvalidClass() throws ParseException {
    String str = "InvalidClass";
    Class<InvalidClass> clazz = InvalidClass.class;

    InvalidClass result = TypeHandler.createValue(str, clazz);

    Assert.assertNull(result);
  }

  @Test
  public void testCreateObject_ValidClass() throws ParseException {
    String str = "java.util.Date";

    Object result = TypeHandler.createObject(str);

    Assert.assertEquals(Date.class, result.getClass());
  }

  @Test
  public void testCreateObject_InvalidClass() {
    String str = "InvalidClass";

    try {
      TypeHandler.createObject(str);
      Assert.fail("Should throw ParseException");
    } catch (ParseException e) {
      Assert.assertEquals("Unable to find the class: InvalidClass", e.getMessage());
    }
  }

  @Test
  public void testCreateNumber_Integer() throws ParseException {
    String str = "100";

    Number result = TypeHandler.createNumber(str);

    Assert.assertEquals(Integer.valueOf(100), result);
  }

  @Test
  public void testCreateNumber_Double() throws ParseException {
    String str = "3.14";

    Number result = TypeHandler.createNumber(str);

    Assert.assertEquals(Double.valueOf(3.14), result);
  }

  @Test
  public void testCreateNumber_InvalidNumber() {
    String str = "InvalidNumber";

    try {
      TypeHandler.createNumber(str);
      Assert.fail("Should throw ParseException");
    } catch (ParseException e) {
      Assert.assertEquals("For input string: \"InvalidNumber\"", e.getMessage());
    }
  }

  @Test
  public void testCreateClass_ValidClass() throws ParseException {
    String str = "java.util.Date";

    Class result = TypeHandler.createClass(str);

    Assert.assertEquals(Date.class, result);
  }

  @Test
  public void testCreateClass_InvalidClass() {
    String str = "InvalidClass";

    try {
      TypeHandler.createClass(str);
      Assert.fail("Should throw ParseException");
    } catch (ParseException e) {
      Assert.assertEquals("Unable to find the class: InvalidClass", e.getMessage());
    }
  }

  @Test
  public void testCreateDate_NotImplemented() {
    String str = "2022-01-01";

    try {
      TypeHandler.createDate(str);
      Assert.fail("Should throw UnsupportedOperationException");
    } catch (UnsupportedOperationException e) {
      Assert.assertEquals("Not yet implemented", e.getMessage());
    }
  }

  @Test
  public void testCreateURL_ValidURL() throws ParseException {
    String str = "https://www.example.com";

    URL result = TypeHandler.createURL(str);

    Assert.assertEquals(new URL(str), result);
  }

  @Test
  public void testCreateURL_InvalidURL() {
    String str = "InvalidURL";

    try {
      TypeHandler.createURL(str);
      Assert.fail("Should throw ParseException");
    } catch (ParseException e) {
      Assert.assertEquals("Unable to parse the URL: InvalidURL", e.getMessage());
    }
  }

  @Test
  public void testCreateFile_ValidPath() {
    String str = "path/to/file.txt";

    File result = TypeHandler.createFile(str);

    Assert.assertEquals(new File(str), result);
  }

  @Test
  public void testOpenFile_ExistingFile() throws ParseException {
    String str = "path/to/existing/file.txt";

    FileInputStream result = TypeHandler.openFile(str);

    Assert.assertNotNull(result);
  }

  @Test
  public void testOpenFile_NotExistingFile() {
    String str = "path/to/not/existing/file.txt";

    try {
      TypeHandler.openFile(str);
      Assert.fail("Should throw ParseException");
    } catch (ParseException e) {
      Assert.assertEquals("Unable to find file: " + str, e.getMessage());
    }
  }

  @Test
  public void testCreateFiles_NotImplemented() {
    String str = "path/to/files";

    try {
      TypeHandler.createFiles(str);
      Assert.fail("Should throw UnsupportedOperationException");
    } catch (UnsupportedOperationException e) {
      Assert.assertEquals("Not yet implemented", e.getMessage());
    }
  }
}