import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import org.apache.commons.cli.ParseException;

public class TypeHandlerTest {
  
  private TypeHandler typeHandler;

  @Before
  public void setup() {
    typeHandler = new TypeHandler();
  }

  @Test
  public void testCreateStringValue() throws ParseException {
    String str = "Hello";
    Object expected = "Hello";
    Object result = typeHandler.createValue(str, String.class);
    assertEquals(expected, result);
  }

  @Test
  public void testCreateObjectValue() throws ParseException {
    String str = "java.lang.String";
    Object expected = new String();
    Object result = typeHandler.createValue(str, Object.class);
    assertEquals(expected.getClass(), result.getClass());
  }

  @Test
  public void testCreateNumberValue() throws ParseException {
    String str = "10";
    Number expected = 10L;
    Number result = typeHandler.createValue(str, Number.class);
    assertEquals(expected, result);
  }

  @Test
  public void testCreateDateValue() {
    String str = "2021-01-01";
    assertThrows(UnsupportedOperationException.class, () -> {
      typeHandler.createValue(str, Date.class);
    });
  }

  @Test
  public void testCreateClassValue() throws ParseException {
    String str = "java.lang.String";
    Class<?> expected = String.class;
    Class<?> result = typeHandler.createValue(str, Class.class);
    assertEquals(expected, result);
  }

  @Test
  public void testCreateFileValue() {
    String str = "path/to/file.txt";
    File expected = new File(str);
    File result = typeHandler.createValue(str, File.class);
    assertEquals(expected, result);
  }

  @Test
  public void testOpenFileValue() throws ParseException {
    String str = "path/to/file.txt";
    FileInputStream expected = null;
    try {
      expected = new FileInputStream(str);
    } catch (FileNotFoundException e) {
      // do nothing
    }
    FileInputStream result = typeHandler.createValue(str, FileInputStream.class);
    assertNotNull(result);
  }

  @Test
  public void testCreateFilesValue() {
    String str = "path/to/files/*.txt";
    assertThrows(UnsupportedOperationException.class, () -> {
      typeHandler.createValue(str, File[].class);
    });
  }

  @Test
  public void testCreateURLValue() throws ParseException {
    String str = "http://www.example.com";
    URL expected = null;
    try {
      expected = new URL(str);
    } catch (MalformedURLException e) {
      // do nothing
    }
    URL result = typeHandler.createValue(str, URL.class);
    assertEquals(expected, result);
  }
  
  @Test
  public void testInvalidClassValue() {
    String str = "invalid.class.Name";
    assertThrows(ParseException.class, () -> {
      typeHandler.createValue(str, Class.class);
    });
  }

  @Test
  public void testInvalidFileValue() {
    String str = "invalid/file/path.txt";
    assertThrows(ParseException.class, () -> {
      typeHandler.openFile(str);
    });
  }
}