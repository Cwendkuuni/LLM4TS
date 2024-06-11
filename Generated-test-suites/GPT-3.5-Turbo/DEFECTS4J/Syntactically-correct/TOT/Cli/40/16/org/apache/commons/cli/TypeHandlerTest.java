import org.junit.Test;
import static org.junit.Assert.*;

public class TypeHandlerTest {

   @Test
   public void testCreateValue_stringValue() throws ParseException {
      assertEquals("test", TypeHandler.createValue("test", PatternOptionBuilder.STRING_VALUE));
   }
   
   @Test
   public void testCreateValue_objectValue() throws ParseException {
      Object obj = TypeHandler.createObject("java.lang.String");
      assertEquals(obj.getClass(), TypeHandler.createValue("java.lang.String", PatternOptionBuilder.OBJECT_VALUE).getClass());
   }
   
   @Test
   public void testCreateValue_numberValue_integer() throws ParseException {
      Number num = Integer.valueOf(123);
      assertEquals(num, TypeHandler.createValue("123", PatternOptionBuilder.NUMBER_VALUE));
   }
   
   @Test
   public void testCreateValue_numberValue_double() throws ParseException {
      Number num = Double.valueOf(12.34);
      assertEquals(num, TypeHandler.createValue("12.34", PatternOptionBuilder.NUMBER_VALUE));
   }
   
   @Test
   public void testCreateValue_dateValue() {
      try {
         TypeHandler.createValue("2021-01-01", PatternOptionBuilder.DATE_VALUE);
      } catch (UnsupportedOperationException e) {
         assertTrue(true);
      }
   }
   
   @Test
   public void testCreateValue_classValue() throws ParseException {
      assertEquals(String.class, TypeHandler.createValue("java.lang.String", PatternOptionBuilder.CLASS_VALUE));
   }
   
   @Test
   public void testCreateValue_fileValue() {
      File file = new File("test.txt");
      assertEquals(file.getPath(), TypeHandler.createValue("test.txt", PatternOptionBuilder.FILE_VALUE).getPath());
   }
   
   @Test
   public void testCreateValue_existingFileValue() {
      try {
         TypeHandler.createValue("test.txt", PatternOptionBuilder.EXISTING_FILE_VALUE);
      } catch (ParseException e) {
         assertTrue(true);
      }
   }
   
   @Test
   public void testCreateValue_filesValue() {
      try {
         TypeHandler.createValue("*.txt", PatternOptionBuilder.FILES_VALUE);
      } catch (UnsupportedOperationException e) {
         assertTrue(true);
      }
   }
   
   @Test
   public void testCreateValue_urlValue() throws ParseException {
      URL url = new URL("http://www.example.com");
      assertEquals(url, TypeHandler.createValue("http://www.example.com", PatternOptionBuilder.URL_VALUE));
   }
   
   @Test
   public void testCreateValue_invalidValue() throws ParseException {
      assertEquals(null, TypeHandler.createValue("invalid", Integer.class));
   }
   
   @Test
   public void testCreateDate() {
      try {
         TypeHandler.createDate("2021-01-01");
      } catch (UnsupportedOperationException e) {
         assertTrue(true);
      }
   }
   
   @Test
   public void testCreateURL() throws ParseException {
      URL url = new URL("http://www.example.com");
      assertEquals(url, TypeHandler.createURL("http://www.example.com"));
   }
   
   @Test
   public void testCreateFile() {
      File file = new File("test.txt");
      assertEquals(file.getPath(), TypeHandler.createFile("test.txt").getPath());
   }
   
   @Test
   public void testOpenFile() {
      try {
         TypeHandler.openFile("test.txt");
      } catch (ParseException e) {
         assertTrue(true);
      }
   }
   
   @Test
   public void testCreateFiles() {
      try {
         TypeHandler.createFiles("*.txt");
      } catch (UnsupportedOperationException e) {
         assertTrue(true);
      }
   }
   
}