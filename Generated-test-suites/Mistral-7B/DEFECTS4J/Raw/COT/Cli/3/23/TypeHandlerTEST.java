import org.apache.commons.cli.TypeHandler;
import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.Date;

/**
 * TypeHandlerTest.java
 */
public class TypeHandlerTest {

    @Test
    public void testCreateValueStringObject() {
        String strValue = "sampleStr";
        Object obj = new Object() {
        };
        Object result = TypeHandler.createValue(strValue, obj.getClass());
        Assert.assertEquals(strValue, result);
    }

    @Test(expected = NullPointerException.class)
    public void testCreateValueNullString() {
        Object obj = new Object() {
        };
        TypeHandler.createValue(null, obj.getClass());
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderStringValue() {
        String strValue = "sampleStr";
        Object result = TypeHandler.createValue(strValue, PatternOptionBuilder.STRING_VALUE);
        Assert.assertEquals(strValue, result);
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderObjectValue() {
        String strValue = "com.example.MyClass";
        Object result = TypeHandler.createValue(strValue, PatternOptionBuilder.OBJECT_VALUE);
        Assert.assertNotNull(result);
        Class<?> clazz = result.getClass();
        Assert.assertEquals(ClassUtils.getClassLoader(clazz).loadClass(strValue), clazz);
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderNumberValue() {
        String strValue = "123";
        Number result = (Number) TypeHandler.createValue(strValue, PatternOptionBuilder.NUMBER_VALUE);
        Assert.assertEquals(Long.parseLong(strValue), result.longValue());
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderNumberValueWithDecimalPart() {
        String strValue = "123.45";
        Number result = (Number) TypeHandler.createValue(strValue, PatternOptionBuilder.NUMBER_VALUE);
        Assert.assertEquals(Double.parseDouble(strValue), result.doubleValue(), 0.0);
    }

    @Test(expected = NumberFormatException.class)
    public void testCreateValueStringPatternOptionBuilderNumberValueInvalid() {
        String strValue = "invalidNumber";
        TypeHandler.createValue(strValue, PatternOptionBuilder.NUMBER_VALUE);
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderDateValue() {
        String strValue = "2022-12-12";
        Date result = (Date) TypeHandler.createValue(strValue, PatternOptionBuilder.DATE_VALUE);
        Assert.assertNotNull(result);
        Assert.assertEquals("Mon Dec 12 00:00:00 GMT 2022", result.toString());
    }

    @Test(expected = ParseException.class)
    public void testCreateValueStringPatternOptionBuilderDateValueInvalid() {
        String strValue = "invalidDate";
        TypeHandler.createValue(strValue, PatternOptionBuilder.DATE_VALUE);
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderClassValue() {
        String strValue = "java.lang.String";
        Class result = (Class) TypeHandler.createValue(strValue, PatternOptionBuilder.CLASS_VALUE);
        Assert.assertEquals(String.class, result);
    }

    @Test(expected = ClassNotFoundException.class)
    public void testCreateValueStringPatternOptionBuilderClassValueInvalidClass() {
        String strValue = "com.example.NonExistentClass";
        TypeHandler.createValue(strValue, PatternOptionBuilder.CLASS_VALUE);
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderFileValue() {
        String strValue = "/path/to/file.txt";
        File result = (File) TypeHandler.createValue(strValue, PatternOptionBuilder.FILE_VALUE);
        Assert.assertEquals(new File("/path/to/file.txt"), result);
    }

    @Test(expected = FileNotFoundException.class)
    public void testCreateValueStringPatternOptionBuilderFileValueInvalidFile() {
        String strValue = "/path/to/invalidFile";
        TypeHandler.createValue(strValue, PatternOptionBuilder.FILE_VALUE);
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderExistingFileValue() {
        String strValue = "/path/to/existingFile.txt";
        File result = (File) TypeHandler.createValue(strValue, PatternOptionBuilder.EXISTING_FILE_VALUE);
        Assert.assertEquals(new File("/path/to/existingFile.txt"), result);
    }

    @Test(expected = FileNotFoundException.class)
    public void testCreateValueStringPatternOptionBuilderExistingFileValueInvalidFile() {
        String strValue = "/path/to/invalidFile.txt";
        TypeHandler.createValue(strValue, PatternOptionBuilder.EXISTING_FILE_VALUE);
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderURLValue() {
        String strValue = "http://example.com";
        URL result = (URL) TypeHandler.createValue(strValue, PatternOptionBuilder.URL_VALUE);
        Assert.assertEquals(new URL("http://example.com"), result);
    }

    @Test(expected = MalformedURLException.class)
    public void testCreateValueStringPatternOptionBuilderURLValueInvalid() {
        String strValue = "invalidURL";
        TypeHandler.createValue(strValue, PatternOptionBuilder.URL_VALUE);
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderFilesValue() {
        String strValue = "/path/to/files/*.txt";
        File[] result = (File[]) TypeHandler.createValue(strValue, PatternOptionBuilder.FILES_VALUE);
        Assert.assertArrayNotFound(File[].class, result);
        Assert.assertEquals(2, result.length);
        Assert.assertTrue(StringUtils.endsWith(result[0].getPath(), ".txt"));
        Assert.assertTrue(StringUtils.endsWith(result[1].getPath(), ".txt"));
    }

    @Test(expected = IOException.class)
    public void testCreateValueStringPatternOptionBuilderFilesValueInvalid() {
        String strValue = "/path/to/nonExistentDirectory/*.txt";
        TypeHandler.createValue(strValue, PatternOptionBuilder.FILES_VALUE);
    }
}