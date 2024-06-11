
import org.apache.commons.cli.PatternOptionBuilder;
import org.apache.commons.lang.math.NumberUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

public class TypeHandlerTest {

    @Test
    public void testCreateValueWithStringObj() {
        String str = "value";
        Object obj = "Object";
        Object result = TypeHandler.createValue(str, obj);
        Assert.assertEquals(str, result);
    }

    @Test
    public void testCreateValueWithStringClass() {
        String str = "value";
        Class clazz = String.class;
        Object result = TypeHandler.createValue(str, clazz);
        Assert.assertEquals(str, result);
    }

    @Test
    public void testCreateValueWithObject() {
        String str = "ClassName";
        Object result = TypeHandler.createObject(str);
        Assert.assertNull(result);
    }

    @Test
    public void testCreateValueWithNumber() {
        String str = "10";
        Number result = TypeHandler.createNumber(str);
        Assert.assertEquals(10, result.intValue());
    }

    @Test
    public void testCreateValueWithClass() {
        String str = "java.lang.String";
        Class result = TypeHandler.createClass(str);
        Assert.assertEquals(String.class, result);
    }

    @Test
    public void testCreateValueWithDate() {
        String str = "2020-01-01";
        Date result = TypeHandler.createDate(str);
        Assert.assertNull(result);
    }

    @Test
    public void testCreateValueWithURL() {
        String str = "http://www.example.com";
        URL result = TypeHandler.createURL(str);
        Assert.assertNotNull(result);
    }

    @Test
    public void testCreateValueWithFile() {
        String str = "path/to/file";
        File result = TypeHandler.createFile(str);
        Assert.assertEquals(new File(str), result);
    }

    @Test
    public void testCreateValueWithFiles() {
        String str = "path/to/file1,path/to/file2";
        File[] result = TypeHandler.createFiles(str);
        Assert.assertNull(result);
    }
}
