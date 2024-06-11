import org.junit.Assert;
import org.junit.Test;

public class TypeHandlerTest {

    @Test
    public void testCreateValue_String() throws ParseException {
        String str = "Test";
        Object obj = PatternOptionBuilder.STRING_VALUE;
        Object result = TypeHandler.createValue(str, obj);
        Assert.assertEquals(str, result);
    }

    @Test
    public void testCreateValue_Object() throws ParseException {
        String str = "java.lang.String";
        Object obj = PatternOptionBuilder.OBJECT_VALUE;
        Object result = TypeHandler.createValue(str, obj);
        Assert.assertEquals(str, result.getClass().getName());
    }

    @Test
    public void testCreateValue_Number() throws ParseException {
        String str = "10";
        Object obj = PatternOptionBuilder.NUMBER_VALUE;
        Object result = TypeHandler.createValue(str, obj);
        Assert.assertEquals(Long.valueOf(10), result);
    }

    @Test
    public void testCreateValue_Date() throws ParseException {
        String str = "2022-01-01";
        Object obj = PatternOptionBuilder.DATE_VALUE;
        Object result = TypeHandler.createValue(str, obj);
        Assert.assertNull(result);
    }

    @Test
    public void testCreateValue_Class() throws ParseException {
        String str = "java.lang.String";
        Object obj = PatternOptionBuilder.CLASS_VALUE;
        Object result = TypeHandler.createValue(str, obj);
        Assert.assertEquals(str, result.getClass().getName());
    }

    @Test
    public void testCreateValue_File() throws ParseException {
        String str = "test.txt";
        Object obj = PatternOptionBuilder.FILE_VALUE;
        Object result = TypeHandler.createValue(str, obj);
        Assert.assertEquals(str, ((File) result).getName());
    }

    @Test
    public void testCreateValue_ExistingFile() throws ParseException {
        String str = "test.txt";
        Object obj = PatternOptionBuilder.EXISTING_FILE_VALUE;
        Object result = TypeHandler.createValue(str, obj);
        Assert.assertEquals(str, ((File) result).getName());
    }

    @Test
    public void testCreateValue_Files() throws ParseException {
        String str = "test1.txt,test2.txt";
        Object obj = PatternOptionBuilder.FILES_VALUE;
        Object result = TypeHandler.createValue(str, obj);
        Assert.assertNull(result);
    }

    @Test
    public void testCreateValue_URL() throws ParseException {
        String str = "https://www.example.com";
        Object obj = PatternOptionBuilder.URL_VALUE;
        Object result = TypeHandler.createValue(str, obj);
        Assert.assertEquals(str, result.toString());
    }

    @Test
    public void testCreateValue_Null() throws ParseException {
        String str = "invalid";
        Object obj = null;
        Object result = TypeHandler.createValue(str, obj);
        Assert.assertNull(result);
    }

    @Test
    public void testCreateObject() throws ParseException {
        String classname = "java.lang.String";
        Object result = TypeHandler.createObject(classname);
        Assert.assertEquals(classname, result.getClass().getName());
    }

    @Test(expected = ParseException.class)
    public void testCreateObject_ClassNotFound() throws ParseException {
        String classname = "invalid.class";
        TypeHandler.createObject(classname);
    }

    @Test(expected = ParseException.class)
    public void testCreateObject_InstanceCreationFailed() throws ParseException {
        String classname = "java.util.AbstractList";
        TypeHandler.createObject(classname);
    }

    @Test
    public void testCreateNumber_Long() throws ParseException {
        String str = "10";
        Number result = TypeHandler.createNumber(str);
        Assert.assertEquals(Long.valueOf(10), result);
    }

    @Test
    public void testCreateNumber_Double() throws ParseException {
        String str = "10.5";
        Number result = TypeHandler.createNumber(str);
        Assert.assertEquals(Double.valueOf(10.5), result);
    }

    @Test(expected = ParseException.class)
    public void testCreateNumber_InvalidNumber() throws ParseException {
        String str = "invalid";
        TypeHandler.createNumber(str);
    }

    @Test
    public void testCreateClass() throws ParseException {
        String classname = "java.lang.String";
        Class<?> result = TypeHandler.createClass(classname);
        Assert.assertEquals(classname, result.getName());
    }

    @Test(expected = ParseException.class)
    public void testCreateClass_ClassNotFound() throws ParseException {
        String classname = "invalid.class";
        TypeHandler.createClass(classname);
    }

    @Test
    public void testCreateDate() {
        String str = "2022-01-01";
        Date result = TypeHandler.createDate(str);
        Assert.assertNull(result);
    }

    @Test
    public void testCreateURL() throws ParseException {
        String str = "https://www.example.com";
        URL result = TypeHandler.createURL(str);
        Assert.assertEquals(str, result.toString());
    }

    @Test(expected = ParseException.class)
    public void testCreateURL_MalformedURL() throws ParseException {
        String str = "invalid";
        TypeHandler.createURL(str);
    }

    @Test
    public void testCreateFile() {
        String str = "test.txt";
        File result = TypeHandler.createFile(str);
        Assert.assertEquals(str, result.getName());
    }

    @Test
    public void testCreateFiles() {
        String str = "test1.txt,test2.txt";
        File[] result = TypeHandler.createFiles(str);
        Assert.assertNull(result);
    }
}