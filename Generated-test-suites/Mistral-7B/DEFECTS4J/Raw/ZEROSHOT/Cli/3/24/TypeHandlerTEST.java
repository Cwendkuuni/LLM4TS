import org.apache.commons.cli.TypeHandler;
import org.junit.Test;
import static org.junit.Assert.*;

public class TypeHandlerTest {

 @Test
 public void testCreateValueStringObject() {
 String str = "test";
 Object obj = new Object();
 Object result = TypeHandler.createValue(str, obj.getClass());
 assertEquals(str, result);
 }

 @Test
 public void testCreateValueStringClassString() {
 String str = "test";
 Class clazz = String.class;
 Object result = TypeHandler.createValue(str, clazz);
 assertEquals(str, result);
 }

 @Test
 public void testCreateValueStringClassObject() {
 String str = "test";
 Class clazz = Object.class;
 Object obj = new Object();
 Object result = TypeHandler.createValue(str, clazz, obj);
 assertNotSame(obj, result);
 assertEquals(str.getClass(), result.getClass());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateValueNullString() {
 String str = null;
 Object obj = new Object();
 try {
 TypeHandler.createValue(str, obj.getClass());
 fail();
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testCreateObject() {
 String className = "java.lang.String";
 Object result = TypeHandler.createObject(className);
 assertTrue(result instanceof String);
 }

 @Test(expected = ClassNotFoundException.class)
 public void testCreateObjectUnknownClass() {
 String className = "java.lang.UnKnownClass";
 TypeHandler.createObject(className);
 fail();
 }

 @Test
 public void testCreateNumber() {
 String numberString = "123";
 Number number = TypeHandler.createNumber(numberString);
 assertNotNull(number);
 assertEquals(123, number.longValue());

 String numberStringWithDecimal = "123.45";
 Number numberWithDecimal = TypeHandler.createNumber(numberStringWithDecimal);
 assertNotNull(numberWithDecimal);
 assertEquals(123.45, numberWithDecimal.doubleValue(), 0.001);
 }

 @Test(expected = NumberFormatException.class)
 public void testCreateNumberInvalidNumber() {
 String numberString = "invalidNumber";
 TypeHandler.createNumber(numberString);
 fail();
 }

 @Test
 public void testCreateClass() {
 String className = "java.lang.String";
 Class clazz = TypeHandler.createClass(className);
 assertEquals(String.class, clazz);
 }

 @Test(expected = ClassNotFoundException.class)
 public void testCreateClassUnknownClass() {
 String className = "java.lang.UnKnownClass";
 TypeHandler.createClass(className);
 fail();
 }

 @Test
 public void testCreateDate() {
 String dateString = "2022-01-01";
 Date date = TypeHandler.createDate(dateString);
 assertNotNull(date);
 assertEquals("Jan 1, 2022", date.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateDateInvalidDate() {
 String dateString = "invalidDate";
 TypeHandler.createDate(dateString);
 fail();
 }

 @Test
 public void testCreateURL() {
 String urlString = "http://example.com";
 URL url = TypeHandler.createURL(urlString);
 assertNotNull(url);
 assertEquals("http", url.getProtocol());
 assertEquals("example.com", url.getHost());
 }

 @Test(expected = MalformedURLException.class)
 public void testCreateURLInvalidURL() {
 String urlString = "invalidURL";
 TypeHandler.createURL(urlString);
 fail();
 }

 @Test
 public void testCreateFile() {
 String filePath = "/path/to/file.txt";
 File file = TypeHandler.createFile(filePath);
 assertNotNull(file);
 assertEquals(filePath, file.getAbsolutePath());
 }

 @Test(expected = FileNotFoundException.class)
 public void testCreateFileNonExistentFile() {
 String filePath = "/path/to/nonExistentFile.txt";
 TypeHandler.createFile(filePath);
 fail();
 }
}