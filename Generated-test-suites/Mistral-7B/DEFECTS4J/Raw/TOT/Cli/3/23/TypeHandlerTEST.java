import org.apache.commons.cli.TypeHandler;
import org.apache.commons.cli.PatternOptionBuilder;
import org.junit.Test;
import static org.junit.Assert.*;

public class TypeHandlerTest {

 @Test
 public void testStringValue() {
 String str = "test";
 Object obj = TypeHandler.createValue(str, String.class);
 assertEquals(str, obj);
 }

 @Test
 public void testObjectValue_Valid() {
 String className = "java.lang.String";
 Object obj = TypeHandler.createObject(className);
 String str = "test";
 Object expected = new StringBuilder(str).reverse().toString();
 Object result = TypeHandler.createValue(str, (Class) obj.getClass());
 assertEquals(expected, result);
 }

 @Test
 public void testObjectValue_Invalid() {
 String className = "java.lang.Integer";
 Object obj = TypeHandler.createObject(className.substring(0, className.lastIndexOf(".")));
 String str = "test";
 assertNull(TypeHandler.createValue(str, (Class) obj.getClass()));
 }

 @Test
 public void testNumberValue_Long() {
 String str = "123";
 Number number = TypeHandler.createNumber(str);
 Long expected = 123L;
 assertEquals(expected.longValue(), number.longValue());
 }

 @Test
 public void testNumberValue_Double() {
 String str = "123.45";
 Number number = TypeHandler.createNumber(str);
 Double expected = 123.45;
 assertEquals(expected.doubleValue(), number.doubleValue(), 0.001);
 }

 @Test(expected = NumberFormatException.class)
 public void testNumberValue_Invalid() {
 String str = "test";
 TypeHandler.createNumber(str);
 }

 @Test
 public void testDateValue_Valid() {
 String str = "2022-12-31";
 Date date = TypeHandler.createDate(str);
 Date expected = new Date(Long.parseLong("1670470399000"));
 assertEquals(expected, date);
 }

 @Test
 public void testDateValue_Invalid() {
 String str = "test";
 Date date = TypeHandler.createDate(str);
 assertNull(date);
 }

 @Test
 public void testClassValue_Valid() {
 String className = "java.lang.String";
 Object obj = TypeHandler.createObject(className);
 Class clazz = String.class;
 Object expected = new StringBuilder("test").reverse().toString();
 Object result = TypeHandler.createValue("test", clazz);
 assertEquals(expected, result);
 }

 @Test
 public void testClassValue_Invalid() {
 String className = "java.lang.Integer";
 Object obj = TypeHandler.createObject(className.substring(0, className.lastIndexOf(".")));
 Class clazz = String.class;
 String str = "test";
 assertNull(TypeHandler.createValue(str, clazz));
 }

 @Test
 public void testFileValue() {
 String filePath = "test.txt";
 File file = new File(filePath);
 Object obj = TypeHandler.createValue(filePath, File.class);
 assertEquals(file, obj);
 }

 @Test(expected = FileNotFoundException.class)
 public void testFileValue_Invalid() {
 String filePath = "test/non-existent.txt";
 Object obj = TypeHandler.createValue(filePath, File.class);
 fail("Expected FileNotFoundException, but got: " + obj);
 }

 @Test
 public void testUrlValue() {
 String urlString = "http://www.example.com";
 URL url = new URL(urlString);
 Object obj = TypeHandler.createValue(urlString, URL.class);
 assertEquals(url, obj);
 }

 @Test(expected = MalformedURLException.class)
 public void testUrlValue_Invalid() {
 String urlString = "test:test";
 Object obj = TypeHandler.createValue(urlString, URL.class);
 fail("Expected MalformedURLException, but got: " + obj);
 }
}