import org.junit.Test;
import org.junit.Assert;

public class TypeHandlerTest {

    // Test createValue method with String argument
    @Test
    public void testCreateValueWithString() {
        try {
            Object result = TypeHandler.createValue("testString", PatternOptionBuilder.STRING_VALUE);
            Assert.assertEquals("testString", result);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    // Test createValue method with Object argument
    @Test
    public void testCreateValueWithObject() {
        try {
            Object result = TypeHandler.createValue("java.lang.Object", PatternOptionBuilder.OBJECT_VALUE);
            Assert.assertEquals(Object.class, result.getClass());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
  
    // Test createValue method with Number argument
    @Test
    public void testCreateValueWithNumber() {
        try {
            Object result = TypeHandler.createValue("10.5", PatternOptionBuilder.NUMBER_VALUE);
            Assert.assertEquals(Double.class, result.getClass());

            result = TypeHandler.createValue("10", PatternOptionBuilder.NUMBER_VALUE);
            Assert.assertEquals(Long.class, result.getClass());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    
    // Test createValue method with Date argument
    @Test
    public void testCreateValueWithDate() {
        try {
            Object result = TypeHandler.createValue("2022-01-01", PatternOptionBuilder.DATE_VALUE);
            Assert.assertNull(result);
        } catch (ParseException e) {
            Assert.fail("Unexpected exception occurred: " + e.getMessage());
        }
    }
  
    // Test createValue method with Class argument
    @Test
    public void testCreateValueWithClass() {
        try {
            Object result = TypeHandler.createValue("java.lang.String", PatternOptionBuilder.CLASS_VALUE);
            Assert.assertEquals(String.class, result.getClass());
        } catch (ParseException e) {
            Assert.fail("Unexpected exception occurred: " + e.getMessage());
        }
    }
  
    // Test createValue method with File argument
    @Test
    public void testCreateValueWithFile() {
        try {
            Object result = TypeHandler.createValue("test.txt", PatternOptionBuilder.FILE_VALUE);
            Assert.assertEquals(File.class, result.getClass());
        } catch (ParseException e) {
            Assert.fail("Unexpected exception occurred: " + e.getMessage());
        }
    }
  
    // Test createValue method with Existing File argument
    @Test
    public void testCreateValueWithExistingFile() {
        try {
            Object result = TypeHandler.createValue("existingFile.txt", PatternOptionBuilder.EXISTING_FILE_VALUE);
            Assert.assertEquals(File.class, result.getClass());
        } catch (ParseException e) {
            Assert.fail("Unexpected exception occurred: " + e.getMessage());
        }
    }
  
    // Test createValue method with Files argument
    @Test
    public void testCreateValueWithFiles() {
        try {
            Object result = TypeHandler.createValue("file1.txt,file2.txt", PatternOptionBuilder.FILES_VALUE);
            Assert.assertNull(result);
        } catch (ParseException e) {
            Assert.fail("Unexpected exception occurred: " + e.getMessage());
        }
    }
  
    // Test createValue method with URL argument
    @Test
    public void testCreateValueWithURL() {
        try {
            Object result = TypeHandler.createValue("https://www.example.com", PatternOptionBuilder.URL_VALUE);
            Assert.assertEquals(URL.class, result.getClass());
        } catch (ParseException e) {
            Assert.fail("Unexpected exception occurred: " + e.getMessage());
        }
    }
  
    // Test createObject method with valid classname
    @Test
    public void testCreateObjectWithValidClassName() {
        try {
            Object result = TypeHandler.createObject("java.lang.String");
            Assert.assertEquals(String.class, result.getClass());
        } catch (ParseException e) {
            Assert.fail("Unexpected exception occurred: " + e.getMessage());
        }
    }
  
    // Test createObject method with invalid classname
    @Test
    public void testCreateObjectWithInvalidClassName() {
        try {
            Object result = TypeHandler.createObject("invalid.class.name");
            Assert.fail("Expected ParseException not thrown");
        } catch (ParseException e) {
            // Expected exception occurred
        }
    }
  
    // Test createNumber method with valid number string
    @Test
    public void testCreateNumberWithValidNumberString() {
        try {
            Number result = TypeHandler.createNumber("10");
            Assert.assertEquals(Long.class, result.getClass());
  
            result = TypeHandler.createNumber("10.5");
            Assert.assertEquals(Double.class, result.getClass());
        } catch (ParseException e) {
            Assert.fail("Unexpected exception occurred: " + e.getMessage());
        }
    }
  
    // Test createNumber method with invalid number string
    @Test
    public void testCreateNumberWithInvalidNumberString() {
        try {
            Number result = TypeHandler.createNumber("invalidNumber");
            Assert.fail("Expected ParseException not thrown");
        } catch (ParseException e) {
            // Expected exception occurred
        }
    }
  
    // Test createClass method with valid classname
    @Test
    public void testCreateClassWithValidClassName() {
        try {
            Class<?> result = TypeHandler.createClass("java.lang.String");
            Assert.assertEquals(String.class, result);
        } catch (ParseException e) {
            Assert.fail("Unexpected exception occurred: " + e.getMessage());
        }
    }
  
    // Test createClass method with invalid classname
    @Test
    public void testCreateClassWithInvalidClassName() {
        try {
            Class<?> result = TypeHandler.createClass("invalid.class.name");
            Assert.fail("Expected ParseException not thrown");
        } catch (ParseException e) {
            // Expected exception occurred
        }
    }
  
    // Test createURL method with valid URL string
    @Test
    public void testCreateURLWithValidURLString() {
        try {
            URL result = TypeHandler.createURL("https://www.example.com");
            Assert.assertEquals(URL.class, result.getClass());
        } catch (ParseException e) {
            Assert.fail("Unexpected exception occurred: " + e.getMessage());
        }
    }
  
    // Test createURL method with invalid URL string
    @Test
    public void testCreateURLWithInvalidURLString() {
        try {
            URL result = TypeHandler.createURL("invalidURL");
            Assert.fail("Expected ParseException not thrown");
        } catch (ParseException e) {
            // Expected exception occurred
        }
    }
  
    // Test createFile method with valid file location
    @Test
    public void testCreateFileWithValidLocation() {
        File result = TypeHandler.createFile("test.txt");
        Assert.assertEquals(File.class, result.getClass());
    }
  
    // Test createFiles method with valid files string
    @Test
    public void testCreateFilesWithValidFilesString() {
        try {
            File[] result = TypeHandler.createFiles("file1.txt,file2.txt");
            Assert.assertNull(result);
        } catch (UnsupportedOperationException e) {
            Assert.fail("Unexpected exception occurred: " + e.getMessage());
        }
    }
  
    // Test createFiles method with invalid files string
    @Test
    public void testCreateFilesWithInvalidFilesString() {
        try {
            File[] result = TypeHandler.createFiles("invalidFilesString");
            Assert.fail("Expected UnsupportedOperationException not thrown");
        } catch (UnsupportedOperationException e) {
            // Expected exception occurred
        }
    }
}