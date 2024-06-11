import org.junit.Test;
import org.junit.Assert;

public class TypeHandlerTest {

  @Test
  public void testCreateValueStringObject() throws ParseException {
    // Test CASE 1: STRING_VALUE
    Object result = TypeHandler.createValue("test", PatternOptionBuilder.STRING_VALUE);
    String expected = "test";
    Assert.assertEquals(expected, result);

    // Test CASE 2: OBJECT_VALUE
    result = TypeHandler.createValue("java.lang.Object", PatternOptionBuilder.OBJECT_VALUE);
    expected = "java.lang.Object";
    Assert.assertEquals(expected, result);

    // Test CASE 3: NUMBER_VALUE
    result = TypeHandler.createValue("10", PatternOptionBuilder.NUMBER_VALUE);
    expected = 10;
    Assert.assertEquals(expected, result);

    // Test CASE 4: DATE_VALUE
    result = TypeHandler.createValue("2021-01-01", PatternOptionBuilder.DATE_VALUE);
    expected = "2021-01-01";
    Assert.assertEquals(expected, result);

    // Test CASE 5: CLASS_VALUE
    result = TypeHandler.createValue("java.lang.String", PatternOptionBuilder.CLASS_VALUE);
    expected = "java.lang.String";
    Assert.assertEquals(expected, result);

    // Test CASE 6: FILE_VALUE
    result = TypeHandler.createValue("test.txt", PatternOptionBuilder.FILE_VALUE);
    expected = new File("test.txt");
    Assert.assertEquals(expected, result);

    // Test CASE 7: EXISTING_FILE_VALUE
    result = TypeHandler.createValue("test.txt", PatternOptionBuilder.EXISTING_FILE_VALUE);
    expected = new File("test.txt");
    Assert.assertEquals(expected, result);

    // Test CASE 8: FILES_VALUE
    result = TypeHandler.createValue("file1.txt,file2.txt,file3.txt", PatternOptionBuilder.FILES_VALUE);
    expected = new File[] { new File("file1.txt"), new File("file2.txt"), new File("file3.txt") };
    Assert.assertEquals(expected, result);

    // Test CASE 9: URL_VALUE
    result = TypeHandler.createValue("http://www.example.com", PatternOptionBuilder.URL_VALUE);
    expected = new URL("http://www.example.com");
    Assert.assertEquals(expected, result);
  }

  @Test
  public void testCreateValueStringClass() throws ParseException {
    // Test CASE 1: STRING_VALUE
    Object result = TypeHandler.createValue("test", String.class);
    String expected = "test";
    Assert.assertEquals(expected, result);

    // Test CASE 2: OBJECT_VALUE
    result = TypeHandler.createValue("java.lang.Object", Object.class);
    expected = "java.lang.Object";
    Assert.assertEquals(expected, result);

    // Test CASE 3: NUMBER_VALUE
    result = TypeHandler.createValue("10", Number.class);
    expected = 10;
    Assert.assertEquals(expected, result);

    // Test CASE 4: DATE_VALUE
    result = TypeHandler.createValue("2021-01-01", Date.class);
    expected = "2021-01-01";
    Assert.assertEquals(expected, result);

    // Test CASE 5: CLASS_VALUE
    result = TypeHandler.createValue("java.lang.String", Class.class);
    expected = "java.lang.String";
    Assert.assertEquals(expected, result);

    // Test CASE 6: FILE_VALUE
    result = TypeHandler.createValue("test.txt", File.class);
    expected = new File("test.txt");
    Assert.assertEquals(expected, result);

    // Test CASE 7: EXISTING_FILE_VALUE
    result = TypeHandler.createValue("test.txt", File.class);
    expected = new File("test.txt");
    Assert.assertEquals(expected, result);

    // Test CASE 8: FILES_VALUE
    result = TypeHandler.createValue("file1.txt,file2.txt,file3.txt", File[].class);
    expected = new File[] { new File("file1.txt"), new File("file2.txt"), new File("file3.txt") };
    Assert.assertEquals(expected, result);

    // Test CASE 9: URL_VALUE
    result = TypeHandler.createValue("http://www.example.com", URL.class);
    expected = new URL("http://www.example.com");
    Assert.assertEquals(expected, result);
  }

  @Test(expected = ParseException.class)
  public void testCreateValueInvalidClass() throws ParseException {
    // Test CASE: Invalid class
    TypeHandler.createValue("InvalidClass", String.class);
  }

  @Test
  public void testCreateObject() throws ParseException {
    Object result = TypeHandler.createObject("java.lang.String");
    String expected = "java.lang.String";
    Assert.assertEquals(expected, result);
  }

  @Test(expected = ParseException.class)
  public void testCreateObjectInvalidClass() throws ParseException {
    TypeHandler.createObject("InvalidClass");
  }

  @Test
  public void testCreateNumber() throws ParseException {
    Object result = TypeHandler.createNumber("10");
    Number expected = 10;
    Assert.assertEquals(expected, result);

    result = TypeHandler.createNumber("10.5");
    expected = 10.5;
    Assert.assertEquals(expected, result);
  }

  @Test(expected = ParseException.class)
  public void testCreateNumberInvalid() throws ParseException {
    TypeHandler.createNumber("10.5a");
  }

  @Test
  public void testCreateClass() throws ParseException {
    Class<?> result = TypeHandler.createClass("java.lang.String");
    Class<?> expected = String.class;
    Assert.assertEquals(expected, result);
  }

  @Test(expected = ParseException.class)
  public void testCreateClassInvalid() throws ParseException {
    TypeHandler.createClass("InvalidClass");
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testCreateDate() {
    TypeHandler.createDate("2021-01-01");
  }

  @Test
  public void testCreateURL() throws ParseException, MalformedURLException {
    URL result = TypeHandler.createURL("http://www.example.com");
    URL expected = new URL("http://www.example.com");
    Assert.assertEquals(expected, result);
  }

  @Test(expected = ParseException.class)
  public void testCreateURLInvalid() throws ParseException {
    TypeHandler.createURL("InvalidURL");
  }

  @Test
  public void testCreateFile() {
    File result = TypeHandler.createFile("test.txt");
    File expected = new File("test.txt");
    Assert.assertEquals(expected, result);
  }

  @Test
  public void testCreateFiles() {
    // test not implemented yet
    Assert.fail("Test not implemented yet");
  }
}