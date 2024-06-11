import static org.junit.Assert.*;
import org.junit.Test;

public class TypeHandlerTest {

    @Test
    public void testCreateValueWithPatternOptionBuilderStringValue() {
        String str = "test";
        Object obj = PatternOptionBuilder.STRING_VALUE;
        try {
            Object result = TypeHandler.createValue(str, obj);
            assertEquals(str, result);
        } catch (ParseException e) {
            fail("Should not throw ParseException");
        }
    }

    @Test
    public void testCreateValueWithPatternOptionBuilderObjectValue() {
        String str = "test";
        Object obj = PatternOptionBuilder.OBJECT_VALUE;
        try {
            Object result = TypeHandler.createValue(str, obj);
            assertEquals(str, result);
        } catch (ParseException e) {
            fail("Should not throw ParseException");
        }
    }

    @Test
    public void testCreateValueWithPatternOptionBuilderNumberValue() {
        String str = "123";
        Object obj = PatternOptionBuilder.NUMBER_VALUE;
        try {
            Object result = TypeHandler.createValue(str, obj);
            assertEquals(Long.valueOf(str), result);
        } catch (ParseException e) {
            fail("Should not throw ParseException");
        }
    }

    @Test
    public void testCreateValueWithPatternOptionBuilderDateValue() {
        String str = "2020-01-01";
        Object obj = PatternOptionBuilder.DATE_VALUE;
        try {
            Object result = TypeHandler.createValue(str, obj);
            // Test whether result is a Date object or not
            assertTrue(result instanceof Date);
        } catch (ParseException e) {
            fail("Should not throw ParseException");
        }
    }

    @Test
    public void testCreateValueWithPatternOptionBuilderClassValue() {
        String str = "java.lang.String";
        Object obj = PatternOptionBuilder.CLASS_VALUE;
        try {
            Object result = TypeHandler.createValue(str, obj);
            assertEquals(String.class, result);
        } catch (ParseException e) {
            fail("Should not throw ParseException");
        }
    }

    @Test
    public void testCreateValueWithPatternOptionBuilderFileValue() {
        String str = "test.txt";
        Object obj = PatternOptionBuilder.FILE_VALUE;
        try {
            Object result = TypeHandler.createValue(str, obj);
            // Test whether result is a File object or not
            assertTrue(result instanceof File);
            assertEquals(new File(str), result);
        } catch (ParseException e) {
            fail("Should not throw ParseException");
        }
    }

    @Test
    public void testCreateValueWithPatternOptionBuilderExistingFileValue() {
        String str = "test.txt";
        Object obj = PatternOptionBuilder.EXISTING_FILE_VALUE;
        try {
            Object result = TypeHandler.createValue(str, obj);
            // Test whether result is a File object or not
            assertTrue(result instanceof File);
            assertEquals(new File(str), result);
        } catch (ParseException e) {
            fail("Should not throw ParseException");
        }
    }

    @Test
    public void testCreateValueWithPatternOptionBuilderFilesValue() {
        String str = "test.txt,test2.txt";
        Object obj = PatternOptionBuilder.FILES_VALUE;
        try {
            Object result = TypeHandler.createValue(str, obj);
            // Test whether result is a File[] object or not
            assertTrue(result instanceof File[]);
            assertArrayEquals(new File[]{new File("test.txt"), new File("test2.txt")}, (File[]) result);
        } catch (ParseException e) {
            fail("Should not throw ParseException");
        }
    }

    @Test
    public void testCreateValueWithPatternOptionBuilderURLValue() {
        String str = "http://www.example.com";
        Object obj = PatternOptionBuilder.URL_VALUE;
        try {
            Object result = TypeHandler.createValue(str, obj);
            // Test whether result is a URL object or not
            assertTrue(result instanceof URL);
            assertEquals(new URL(str), result);
        } catch (ParseException e) {
            fail("Should not throw ParseException");
        }
    }

    @Test
    public void testCreateValueWithInvalidClass() {
        String str = "invalid.class";
        Object obj = PatternOptionBuilder.CLASS_VALUE;
        try {
            Object result = TypeHandler.createValue(str, obj);
            fail("Should throw ParseException");
        } catch (ParseException e) {
            // Test whether ParseException is thrown with expected message
            assertEquals("Unable to find the class: invalid.class", e.getMessage());
        }
    }

    @Test
    public void testCreateObjectWithValidClass() {
        String classname = "java.lang.String";
        try {
            Object result = TypeHandler.createObject(classname);
            assertEquals(String.class, result);
        } catch (ParseException e) {
            fail("Should not throw ParseException");
        }
    }

    @Test
    public void testCreateObjectWithInvalidClass() {
        String classname = "invalid.class";
        try {
            Object result = TypeHandler.createObject(classname);
            fail("Should throw ParseException");
        } catch (ParseException e) {
            // Test whether ParseException is thrown with expected message
            assertEquals("Unable to find the class: invalid.class", e.getMessage());
        }
    }

    @Test
    public void testCreateNumberWithIntegerValue() {
        String str = "123";
        try {
            Object result = TypeHandler.createNumber(str);
            assertTrue(result instanceof Long);
            assertEquals(Long.valueOf(str), result);
        } catch (ParseException e) {
            fail("Should not throw ParseException");
        }
    }

    @Test
    public void testCreateNumberWithDecimalValue() {
        String str = "123.45";
        try {
            Object result = TypeHandler.createNumber(str);
            assertTrue(result instanceof Double);
            assertEquals(Double.valueOf(str), result);
        } catch (ParseException e) {
            fail("Should not throw ParseException");
        }
    }

    @Test
    public void testCreateNumberWithInvalidValue() {
        String str = "invalid";
        try {
            Object result = TypeHandler.createNumber(str);
            fail("Should throw ParseException");
        } catch (ParseException e) {
            // Test whether ParseException is thrown with expected message
            assertEquals("For input string: \"invalid\"", e.getMessage());
        }
    }

    @Test
    public void testCreateClassWithValidClass() {
        String classname = "java.lang.String";
        try {
            Object result = TypeHandler.createClass(classname);
            assertEquals(String.class, result);
        } catch (ParseException e) {
            fail("Should not throw ParseException");
        }
    }

    @Test
    public void testCreateClassWithInvalidClass() {
        String classname = "invalid.class";
        try {
            Object result = TypeHandler.createClass(classname);
            fail("Should throw ParseException");
        } catch (ParseException e) {
            // Test whether ParseException is thrown with expected message
            assertEquals("Unable to find the class: invalid.class", e.getMessage());
        }
    }

    @Test
    public void testCreateURLWithValidURL() {
        String str = "http://www.example.com";
        try {
            Object result = TypeHandler.createURL(str);
            assertTrue(result instanceof URL);
            assertEquals(new URL(str), result);
        } catch (ParseException e) {
            fail("Should not throw ParseException");
        }
    }

    @Test
    public void testCreateURLWithInvalidURL() {
        String str = "invalid url";
        try {
            Object result = TypeHandler.createURL(str);
            fail("Should throw ParseException");
        } catch (ParseException e) {
            // Test whether ParseException is thrown with expected message
            assertEquals("Unable to parse the URL: invalid url", e.getMessage());
        }
    }

    @Test
    public void testCreateFileWithValidFile() {
        String str = "test.txt";
        try {
            Object result = TypeHandler.createFile(str);
            assertTrue(result instanceof File);
            assertEquals(new File(str), result);
        } catch (Exception e) {
            fail("Should not throw exception");
        }
    }

    @Test
    public void testCreateFilesWithValidFiles() {
        String str = "test.txt,test2.txt";
        try {
            Object result = TypeHandler.createFiles(str);
            assertTrue(result instanceof File[]);
            assertArrayEquals(new File[]{new File("test.txt"), new File("test2.txt")}, (File[]) result);
        } catch (UnsupportedOperationException e) {
            fail("Should not throw UnsupportedOperationException");
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateDateWithDateString() {
        String str = "2020-01-01";
        TypeHandler.createDate(str);
    }
}