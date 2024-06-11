import org.apache.commons.cli.TypeHandler;
import org.apache.commons.cli.PatternOptionBuilder;
import org.apache.commons.lang.math.NumberUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class TypeHandlerTest {

 @Test
 public void testCreateValueStringObject() {
 String str = "abc";
 Object obj = new String("def");
 Object value = TypeHandler.createValue(str, obj.getClass());
 assertEquals("Value should be the same as original object", obj, value);
 }

 @Test
 public void testCreateValueStringString() {
 String str = "abc";
 Class clazz = String.class;
 Object value = TypeHandler.createValue(str, clazz);
 assertEquals("Value should be the same as original string", str, value);
 }

 @Test
 public void testCreateValueStringClassWithInvalidPatternOptionBuilder() {
 String str = "abc";
 Class clazz = PatternOptionBuilder.INVALID_VALUE_TYPE.getClass();
 Object value = TypeHandler.createValue(str, clazz);
 assertNull("Value should be null for invalid PatternOptionBuilder", value);
 }

 @Test
 public void testCreateValueStringPatternOptionBuilderStringValue() {
 String str = "abc";
 Class clazz = PatternOptionBuilder.STRING_VALUE.getClass();
 Object value = TypeHandler.createValue(str, clazz);
 assertEquals("Value should be the same as original string", str, value);
 }

 @Test
 public void testCreateValueStringPatternOptionBuilderObjectValue() {
 String str = "org.apache.commons.cli.TypeHandler";
 Class clazz = PatternOptionBuilder.OBJECT_VALUE.getClass();
 Object value = TypeHandler.createValue(str, clazz);
 assertNotNull("Value should not be null for Object_VALUE", value);
 }

 @Test(expected = NumberFormatException.class)
 public void testCreateValueStringNumberValueInvalidNumber() {
 String str = "abc";
 Class clazz = PatternOptionBuilder.NUMBER_VALUE.getClass();
 Object value = TypeHandler.createValue(str, clazz);
 fail("NumberFormatException should be thrown for invalid number");
 }

 @Test
 public void testCreateValueStringNumberValueLong() {
 String str = "123";
 Class clazz = PatternOptionBuilder.NUMBER_VALUE.getClass();
 Long value = (Long) TypeHandler.createValue(str, clazz);
 assertEquals("Value should be the same as original long number", 123L, value.longValue());
 }

 @Test
 public void testCreateValueStringNumberValueDouble() {
 String str = "123.45";
 Class clazz = PatternOptionBuilder.NUMBER_VALUE.getClass();
 Double value = (Double) TypeHandler.createValue(str, clazz);
 assertEquals("Value should be the same as original double number", 123.45, value.doubleValue(), 0.001);
 }

 @Test
 public void testCreateValueStringDateValue() {
 String str = "2023-03-01";
 Class clazz = PatternOptionBuilder.DATE_VALUE.getClass();
 Date value = (Date) TypeHandler.createValue(str, clazz);
 assertEquals("Value should be the same as original date", new Date(2023, 2, 1), value);
 }

 @Test(expected = ClassNotFoundException.class)
 public void testCreateValueStringClassValueInvalidClassName() {
 String str = "InvalidClassName";
 Class clazz = PatternOptionBuilder.CLASS_VALUE.getClass();
 Object value = TypeHandler.createValue(str, clazz);
 fail("ClassNotFoundException should be thrown for invalid class name");
 }

 @Test
 public void testCreateValueStringClassValue() {
 String str = "java.util.Date";
 Class clazz = PatternOptionBuilder.CLASS_VALUE.getClass();
 Object value = TypeHandler.createValue(str, clazz);
 assertInstanceof("Value should be an instance of the given class", Date.class, value);
 }

 @Test(expected = InstantiationException.class)
 public void testCreateValueStringFileValue() {
 String str = "InvalidFileLocation";
 Class clazz = PatternOptionBuilder.FILE_VALUE.getClass();
 Object value = TypeHandler.createValue(str, clazz);
 fail("InstantiationException should be thrown for invalid file location");
 }

 @Test
 public void testCreateValueStringFileValueValidFile() {
 String str = "src/test/resources/testFile.txt";
 Class clazz = PatternOptionBuilder.FILE_VALUE.getClass();
 File value = (File) TypeHandler.createValue(str, clazz);
 assertTrue("Value should be a File", value.exists());
 }

 @Test
 public void testCreateValueStringURLValue() {
 String str = "https://www.example.com";
 Class clazz = PatternOptionBuilder.URL_VALUE.getClass();
 URL value = (URL) TypeHandler.createValue(str, clazz);
 assertNotNull("Value should not be null for URL_VALUE", value);
 }

 @Test(expected = MalformedURLException.class)
 public void testCreateValueStringURLValueInvalidURL() {
 String str = "InvalidURL";
 Class clazz = PatternOptionBuilder.URL_VALUE.getClass();
 Object value = TypeHandler.createValue(str, clazz);
 fail("MalformedURLException should be thrown for invalid URL");
 }

 @Test
 public void testCreateValueStringFilesValue() {
 String str = "src/test/resources/testFile1.txt src/test/resources/testFile2.txt";
 Class clazz = PatternOptionBuilder.FILES_VALUE.getClass();
 File[] values = (File[]) TypeHandler.createValue(str, clazz);
 assertEquals("Number of files should match the number of tokens in the string", 2, values.length);
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testCreateValueStringFilesValueInvalidNumberOfFiles() {
 String str = "src/test/resources/testFile.txt src/test/resources/testFile.txt src/test/resources/testFile.txt src/test/resources/testFile.txt src/test/resources/testFile.txt src/test/resources/testFile.txt src/test/resources/testFile.txt src/test/resources/testFile.txt src/test/resources/testFile.txt src/test/resources/testFile.txt src/test/resources/testFile.txt src/test/resources/testFile.txt";
 Class clazz = PatternOptionBuilder.FILES_VALUE.getClass();
 Object value = TypeHandler.createValue(str, clazz);
 fail("ArrayIndexOutOfBoundsException should be thrown for invalid number of files");
 }
}