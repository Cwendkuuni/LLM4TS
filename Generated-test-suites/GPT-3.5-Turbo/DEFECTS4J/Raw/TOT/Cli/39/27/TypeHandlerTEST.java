import org.junit.Test;
import org.junit.Assert;
import org.junit.BeforeClass;

import java.io.File;
import java.net.URL;
import java.text.ParseException;
import java.util.Date;

public class TypeHandlerTest {
  
    @Test
    public void testCreateValue_stringType_validString_returnString() throws ParseException {
        String str = "Hello";
        Object obj = PatternOptionBuilder.STRING_VALUE;
        Object result = TypeHandler.createValue(str, obj);
        
        Assert.assertEquals(String.class, result.getClass());
        Assert.assertEquals(str, result);
    }

    @Test
    public void testCreateValue_objectType_validString_returnObject() throws ParseException {
        String str = "java.lang.Object";
        Object obj = PatternOptionBuilder.OBJECT_VALUE;
        Object result = TypeHandler.createValue(str, obj);

        Assert.assertEquals(Class.class, result.getClass());
        Assert.assertEquals(Object.class, result);
    }

    @Test
    public void testCreateValue_numberType_validString_returnNumber() throws ParseException {
        String str = "10";
        Object obj = PatternOptionBuilder.NUMBER_VALUE;
        Object result = TypeHandler.createValue(str, obj);

        Assert.assertEquals(Long.class, result.getClass());
        Assert.assertEquals(10L, result);
    }

    @Test
    public void testCreateValue_dateType_validString_returnDate() throws ParseException {
        String str = "2021-10-05";
        Object obj = PatternOptionBuilder.DATE_VALUE;
        Object result = TypeHandler.createValue(str, obj);

        Assert.assertEquals(Date.class, result.getClass());
        // Assert other date comparison logic
    }

    @Test
    public void testCreateValue_classType_validString_returnClass() throws ParseException {
        String str = "java.lang.String";
        Object obj = PatternOptionBuilder.CLASS_VALUE;
        Object result = TypeHandler.createValue(str, obj);

        Assert.assertEquals(Class.class, result.getClass());
        Assert.assertEquals(String.class, result);
    }

    @Test
    public void testCreateValue_fileType_validString_returnFile() throws ParseException {
        String str = "test.txt";
        Object obj = PatternOptionBuilder.FILE_VALUE;
        Object result = TypeHandler.createValue(str, obj);

        Assert.assertEquals(File.class, result.getClass());
        Assert.assertEquals(str, ((File) result).getName());
    }

    @Test
    public void testCreateValue_existingFileType_validString_returnFile() throws ParseException {
        String str = "test.txt";
        Object obj = PatternOptionBuilder.EXISTING_FILE_VALUE;
        Object result = TypeHandler.createValue(str, obj);

        Assert.assertEquals(File.class, result.getClass());
        Assert.assertEquals(str, ((File) result).getName());
    }

    @Test
    public void testCreateValue_filesType_validString_returnFiles() throws ParseException {
        String str = "test1.txt,test2.txt";
        Object obj = PatternOptionBuilder.FILES_VALUE;
        Object result = TypeHandler.createValue(str, obj);

        Assert.assertEquals(File[].class, result.getClass());
        // Assert other file comparison logic
    }

    @Test
    public void testCreateValue_urlType_validString_returnURL() throws ParseException {
        String str = "https://www.example.com";
        Object obj = PatternOptionBuilder.URL_VALUE;
        Object result = TypeHandler.createValue(str, obj);

        Assert.assertEquals(URL.class, result.getClass());
        Assert.assertEquals(str, result.toString());
    }

    @Test
    public void testCreateValue_invalidType_validString_returnNull() throws ParseException {
        String str = "Hello";
        Object obj = "InvalidType";
        Object result = TypeHandler.createValue(str, obj);

        Assert.assertNull(result);
    }

    @Test(expected = ParseException.class)
    public void testCreateValue_classNotFound_throwParseException() throws ParseException {
        String str = "InvalidClass";
        Object obj = PatternOptionBuilder.CLASS_VALUE;
        TypeHandler.createValue(str, obj);
    }

    @Test(expected = ParseException.class)
    public void testCreateValue_createObjectFailed_throwParseException() throws ParseException {
        String str = "java.io.IOException";
        Object obj = PatternOptionBuilder.OBJECT_VALUE;
        TypeHandler.createValue(str, obj);
    }

    @Test(expected = ParseException.class)
    public void testCreateValue_invalidNumberString_throwParseException() throws ParseException {
        String str = "ABC";
        Object obj = PatternOptionBuilder.NUMBER_VALUE;
        TypeHandler.createValue(str, obj);
    }

    @Test(expected = ParseException.class)
    public void testCreateValue_createURLMalformed_throwParseException() throws ParseException {
        String str = "invalidurl";
        Object obj = PatternOptionBuilder.URL_VALUE;
        TypeHandler.createValue(str, obj);
    }

    @Test
    public void testCreateObject_validClassname_returnObject() throws ParseException {
        String classname = "java.lang.String";
        Object result = TypeHandler.createObject(classname);

        Assert.assertEquals(Class.class, result.getClass());
        Assert.assertEquals(String.class, result);
    }

    @Test(expected = ParseException.class)
    public void testCreateObject_classNotFound_throwParseException() throws ParseException {
        String classname = "InvalidClass";
        TypeHandler.createObject(classname);
    }

    @Test(expected = ParseException.class)
    public void testCreateObject_createInstanceFailed_throwParseException() throws ParseException {
        String classname = "java.io.IOException";
        TypeHandler.createObject(classname);
    }

    @Test
    public void testCreateNumber_validLongString_returnLong() throws ParseException {
        String str = "10";
        Number result = TypeHandler.createNumber(str);

        Assert.assertEquals(Long.class, result.getClass());
        Assert.assertEquals(10L, result);
    }

    @Test
    public void testCreateNumber_validDoubleString_returnDouble() throws ParseException {
        String str = "10.5";
        Number result = TypeHandler.createNumber(str);

        Assert.assertEquals(Double.class, result.getClass());
        Assert.assertEquals(10.5, result);
    }

    @Test(expected = ParseException.class)
    public void testCreateNumber_invalidString_throwParseException() throws ParseException {
        String str = "ABC";
        TypeHandler.createNumber(str);
    }

    @Test
    public void testCreateClass_validClassname_returnClass() throws ParseException {
        String classname = "java.lang.String";
        Class<?> result = TypeHandler.createClass(classname);

        Assert.assertEquals(Class.class, result.getClass());
        Assert.assertEquals(String.class, result);
    }

    @Test(expected = ParseException.class)
    public void testCreateClass_classNotFound_throwParseException() throws ParseException {
        String classname = "InvalidClass";
        TypeHandler.createClass(classname);
    }

    @Test
    public void testCreateURL_validString_returnURL() throws ParseException {
        String str = "https://www.example.com";
        URL result = TypeHandler.createURL(str);

        Assert.assertEquals(URL.class, result.getClass());
        Assert.assertEquals(str, result.toString());
    }

    @Test(expected = ParseException.class)
    public void testCreateURL_malformedString_throwParseException() throws ParseException {
        String str = "invalidurl";
        TypeHandler.createURL(str);
    }

    @Test
    public void testCreateFile_validString_returnFile() throws ParseException {
        String str = "test.txt";
        File result = TypeHandler.createFile(str);

        Assert.assertEquals(File.class, result.getClass());
        Assert.assertEquals(str, result.getName());
    }

    @Test
    public void testCreateFiles_validString_returnFilesArray() throws ParseException {
        String str = "test1.txt,test2.txt";
        File[] result = TypeHandler.createFiles(str);

        Assert.assertEquals(File[].class, result.getClass());
        // Assert other file comparison logic
    }
  
    // Add more test cases for createDate() method

}