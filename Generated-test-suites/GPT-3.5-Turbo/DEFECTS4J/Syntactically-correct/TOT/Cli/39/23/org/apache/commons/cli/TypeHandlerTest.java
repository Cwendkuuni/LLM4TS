import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TypeHandlerTest {

    private TypeHandler typeHandler;

    @Before
    public void setUp() {
        typeHandler = new TypeHandler();
    }

    @Test
    public void testCreateValue_stringValue() {
        String str = "testString";
        Object obj = PatternOptionBuilder.STRING_VALUE;
        try {
            Object result = typeHandler.createValue(str, obj);
            assertEquals(str, result);
        } catch (ParseException e) {
            fail("Unexpected ParseException thrown");
        }
    }

    @Test
    public void testCreateValue_objectValue() {
        String str = "java.lang.String";
        Object obj = PatternOptionBuilder.OBJECT_VALUE;
        try {
            Object result = typeHandler.createValue(str, obj);
            assertEquals(String.class, result.getClass());
        } catch (ParseException e) {
            fail("Unexpected ParseException thrown");
        }
    }

    @Test
    public void testCreateValue_numberValue_integer() {
        String str = "10";
        Object obj = PatternOptionBuilder.NUMBER_VALUE;
        try {
            Object result = typeHandler.createValue(str, obj);
            assertEquals(Integer.class, result.getClass());
            assertEquals(10, ((Integer) result).intValue());
        } catch (ParseException e) {
            fail("Unexpected ParseException thrown");
        }
    }

    @Test
    public void testCreateValue_numberValue_double() {
        String str = "10.5";
        Object obj = PatternOptionBuilder.NUMBER_VALUE;
        try {
            Object result = typeHandler.createValue(str, obj);
            assertEquals(Double.class, result.getClass());
            assertEquals(10.5, ((Double) result).doubleValue(), 0.001);
        } catch (ParseException e) {
            fail("Unexpected ParseException thrown");
        }
    }

    @Test
    public void testCreateValue_dateValue() {
        String str = "2019-01-01";
        Object obj = PatternOptionBuilder.DATE_VALUE;
        try {
            Object result = typeHandler.createValue(str, obj);
            // Check if the result is of type java.util.Date
            assertEquals("java.util.Date", result.getClass().getName());
        } catch (ParseException e) {
            fail("Unexpected ParseException thrown");
        }
    }

    @Test
    public void testCreateValue_classValue() {
        String str = "java.lang.String";
        Object obj = PatternOptionBuilder.CLASS_VALUE;
        try {
            Object result = typeHandler.createValue(str, obj);
            assertEquals(String.class, result.getClass());
        } catch (ParseException e) {
            fail("Unexpected ParseException thrown");
        }
    }

    @Test
    public void testCreateValue_fileValue() {
        String str = "/path/to/file.txt";
        Object obj = PatternOptionBuilder.FILE_VALUE;
        try {
            Object result = typeHandler.createValue(str, obj);
            assertEquals("/path/to/file.txt", ((File) result).getPath());
        } catch (ParseException e) {
            fail("Unexpected ParseException thrown");
        }
    }

    @Test
    public void testCreateValue_existingFileValue() {
        String str = "/path/to/existing-file.txt";
        Object obj = PatternOptionBuilder.EXISTING_FILE_VALUE;
        try {
            Object result = typeHandler.createValue(str, obj);
            assertEquals("/path/to/existing-file.txt", ((File) result).getPath());
        } catch (ParseException e) {
            fail("Unexpected ParseException thrown");
        }
    }

    @Test
    public void testCreateValue_filesValue() {
        String str = "/path/to/file1.txt,/path/to/file2.txt";
        Object obj = PatternOptionBuilder.FILES_VALUE;
        try {
            Object result = typeHandler.createValue(str, obj);
            File[] filesResult = (File[]) result;
            assertEquals(2, filesResult.length);
            assertEquals("/path/to/file1.txt", filesResult[0].getPath());
            assertEquals("/path/to/file2.txt", filesResult[1].getPath());
        } catch (ParseException e) {
            fail("Unexpected ParseException thrown");
        }
    }
    
    @Test
    public void testCreateValue_urlValue() {
        String str = "http://www.example.com";
        Object obj = PatternOptionBuilder.URL_VALUE;
        try {
            Object result = typeHandler.createValue(str, obj);
            assertEquals(URL.class, result.getClass());
            assertEquals(str, ((URL) result).toString());
        } catch (ParseException e) {
            fail("Unexpected ParseException thrown");
        }
    }

    @Test
    public void testCreateValue_invalidClassValue() {
        String str = "invalidClass";
        Object obj = PatternOptionBuilder.CLASS_VALUE;
        try {
            typeHandler.createValue(str, obj);
            fail("Expected ParseException not thrown");
        } catch (ParseException e) {
            assertEquals("Unable to find the class: invalidClass", e.getMessage());
        }
    }

    @Test
    public void testCreateValue_invalidNumberValue() {
        String str = "non-numeric";
        Object obj = PatternOptionBuilder.NUMBER_VALUE;
        try {
            typeHandler.createValue(str, obj);
            fail("Expected ParseException not thrown");
        } catch (ParseException e) {
            assertEquals("For input string: \"non-numeric\"", e.getMessage());
        }
    }

    @Test
    public void testCreateDate_notImplemented() {
        String str = "2019-01-01";
        try {
            typeHandler.createDate(str);
            fail("Expected UnsupportedOperationException not thrown");
        } catch (UnsupportedOperationException e) {
            assertEquals("Not yet implemented", e.getMessage());
        }
    }

    @Test
    public void testCreateURL_validURL() {
        String str = "http://www.example.com";
        try {
            Object result = typeHandler.createURL(str);
            assertEquals(URL.class, result.getClass());
            assertEquals(str, ((URL) result).toString());
        } catch (ParseException e) {
            fail("Unexpected ParseException thrown");
        }
    }

    @Test
    public void testCreateURL_invalidURL() {
        String str = "invalidURL";
        try {
            typeHandler.createURL(str);
            fail("Expected ParseException not thrown");
        } catch (ParseException e) {
            assertEquals("Unable to parse the URL: invalidURL", e.getMessage());
        }
    }

    @Test
    public void testCreateFile_validFilePath() {
        String str = "/path/to/file.txt";
        Object result = typeHandler.createFile(str);
        assertEquals("/path/to/file.txt", ((File) result).getPath());
    }

    @Test
    public void testCreateFiles_notImplemented() {
        String str = "/path/to/file1.txt,/path/to/file2.txt";
        try {
            typeHandler.createFiles(str);
            fail("Expected UnsupportedOperationException not thrown");
        } catch (UnsupportedOperationException e) {
            assertEquals("Not yet implemented", e.getMessage());
        }
    }
}