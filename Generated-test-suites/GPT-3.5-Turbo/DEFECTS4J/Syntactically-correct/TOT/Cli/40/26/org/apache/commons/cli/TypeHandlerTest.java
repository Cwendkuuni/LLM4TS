import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.cli.TypeHandler;
import org.apache.commons.cli.ParseException;
import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.Date;


public class TypeHandlerTest {

    @Test
    public void testCreateValue_String_Object() throws ParseException {
        String str = "test";
        Object obj = new Object();
        Object result = TypeHandler.createValue(str, obj);
        Assert.assertEquals(obj, result);
    }
    
    @Test
    public void testCreateValue_Number() throws ParseException {
        String str = "123";
        Number result = TypeHandler.createValue(str, Number.class);
        Assert.assertEquals(Long.valueOf(str), result);
    }
    
    @Test
    public void testCreateValue_String() throws ParseException {
        String str = "test";
        String result = TypeHandler.createValue(str, String.class);
        Assert.assertEquals(str, result);
    }
    
    @Test
    public void testCreateValue_Object() throws ParseException {
        String str = "java.lang.Object";
        Object result = TypeHandler.createValue(str, Object.class);
        Assert.assertEquals(Object.class, result.getClass());
    }
    
    @Test
    public void testCreateValue_Date() {
        String str = "2021-01-01";
        Date result = TypeHandler.createValue(str, Date.class);
        Assert.assertNull(result);
    }
    
    @Test
    public void testCreateValue_Class() throws ParseException {
        String str = "java.lang.String";
        Class<?> result = TypeHandler.createValue(str, Class.class);
        Assert.assertEquals(String.class, result);
    }
    
    @Test
    public void testCreateValue_File() {
        String str = "test.txt";
        File result = TypeHandler.createValue(str, File.class);
        Assert.assertEquals(new File(str), result);
    }
    
    @Test
    public void testCreateValue_ExistingFile() throws ParseException {
        String str = "test.txt";
        FileInputStream result = TypeHandler.createValue(str, FileInputStream.class);
        Assert.assertNotNull(result);
    }
    
    @Test
    public void testCreateValue_Files() {
        String str = "file1.txt, file2.txt";
        File[] result = TypeHandler.createValue(str, File[].class);
        Assert.assertNull(result);
    }
    
    @Test
    public void testCreateValue_URL() throws ParseException {
        String str = "http://www.example.com";
        URL result = TypeHandler.createValue(str, URL.class);
        Assert.assertEquals(new URL(str), result);
    }
    
    @Test(expected = ParseException.class)
    public void testCreateValue_InvalidClass() throws ParseException {
        String str = "InvalidClass";
        TypeHandler.createValue(str, Class.class);
    }
    
    @Test(expected = ParseException.class)
    public void testCreateValue_InvalidURL() throws ParseException {
        String str = "Invalid@URL";
        TypeHandler.createValue(str, URL.class);
    }
    
    @Test(expected = ParseException.class)
    public void testOpenFile_NotExist() throws ParseException {
        String str = "nonexistent.txt";
        TypeHandler.openFile(str);
    }
    
    @Test(expected = ParseException.class)
    public void testOpenFile_NotReadable() throws ParseException {
        String str = "unreadable.txt";
        TypeHandler.openFile(str);
    }

}