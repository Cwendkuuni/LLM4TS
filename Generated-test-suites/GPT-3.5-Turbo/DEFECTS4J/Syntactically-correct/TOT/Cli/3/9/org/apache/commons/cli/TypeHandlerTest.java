
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.net.URL;
import java.util.Date;

public class TypeHandlerTest {

    @Test
    public void testCreateValue_String() {
        String str = "testString";
        Object obj = PatternOptionBuilder.STRING_VALUE;
        Object result = TypeHandler.createValue(str, obj);

        Assert.assertEquals(str, result);
    }

    @Test
    public void testCreateValue_Object() {
        String str = "org.apache.commons.cli.Option";
        Object result = TypeHandler.createValue(str, PatternOptionBuilder.OBJECT_VALUE);

        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof Option);
    }

    @Test
    public void testCreateValue_Number() {
        String str = "123";
        Object result = TypeHandler.createValue(str, PatternOptionBuilder.NUMBER_VALUE);

        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof Number);
        Assert.assertEquals(123, ((Number) result).intValue());
    }

    @Test
    public void testCreateValue_Date() {
        String str = "2022-01-01";
        Object result = TypeHandler.createValue(str, PatternOptionBuilder.DATE_VALUE);

        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof Date);
    }

    @Test
    public void testCreateValue_Class() {
        String str = "java.lang.String";
        Object result = TypeHandler.createValue(str, PatternOptionBuilder.CLASS_VALUE);

        Assert.assertNotNull(result);
        Assert.assertEquals(String.class, result);
    }

    @Test
    public void testCreateValue_File() {
        String str = "test.txt";
        Object result = TypeHandler.createValue(str, PatternOptionBuilder.FILE_VALUE);

        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof File);
    }

    @Test
    public void testCreateValue_ExistingFile() {
        String str = "existingFile.txt";
        Object result = TypeHandler.createValue(str, PatternOptionBuilder.EXISTING_FILE_VALUE);

        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof File);
        Assert.assertTrue(((File) result).exists());
    }

    @Test
    public void testCreateValue_Files() {
        String str = "file1.txt,file2.txt";
        Object result = TypeHandler.createValue(str, PatternOptionBuilder.FILES_VALUE);

        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof File[]);
        File[] files = (File[]) result;
        Assert.assertEquals(2, files.length);
        Assert.assertEquals("file1.txt", files[0].getName());
        Assert.assertEquals("file2.txt", files[1].getName());
    }

    @Test
    public void testCreateValue_URL() {
        String str = "https://www.example.com";
        Object result = TypeHandler.createValue(str, PatternOptionBuilder.URL_VALUE);

        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof URL);
    }

    @Test
    public void testCreateValue_Invalid() {
        String str = "invalid";
        Object result = TypeHandler.createValue(str, Object.class);

        Assert.assertNull(result);
    }
}
