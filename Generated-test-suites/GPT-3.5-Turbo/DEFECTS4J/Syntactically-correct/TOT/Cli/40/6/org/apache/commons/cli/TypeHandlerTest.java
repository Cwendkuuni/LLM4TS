import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.cli.TypeHandler;

public class TypeHandlerTest {

    @Test
    public void testCreateValueString() throws Exception {
        String str = "testString";
        Object result = TypeHandler.createValue(str, String.class);
        Assert.assertEquals(result, str);
    }

    @Test
    public void testCreateValueObject() throws Exception {
        String str = "org.apache.commons.cli.TypeHandler";
        Object result = TypeHandler.createValue(str, Object.class);
        Assert.assertEquals(result.getClass().getName(), str);
    }

    @Test
    public void testCreateValueNumber() throws Exception {
        String str = "123";
        Object result = TypeHandler.createValue(str, Number.class);
        Assert.assertEquals(result, Long.valueOf(str));
    }

    @Test
    public void testCreateValueDate() throws Exception {
        String str = "2021-01-01";
        Object result = TypeHandler.createValue(str, Date.class);
        Assert.assertNull(result);
    }

    @Test
    public void testCreateValueClass() throws Exception {
        String str = "org.apache.commons.cli.TypeHandler";
        Object result = TypeHandler.createValue(str, Class.class);
        Assert.assertEquals(result.getClass().getName(), str);
    }

    @Test
    public void testCreateValueFile() throws Exception {
        String str = "test.txt";
        Object result = TypeHandler.createValue(str, File.class);
        Assert.assertEquals(result.getClass(), File.class);
    }

    @Test
    public void testCreateValueExistingFile() throws Exception {
        String str = "test.txt";
        Object result = TypeHandler.createValue(str, FileInputStream.class);
        Assert.assertNotNull(result);
    }

    @Test
    public void testCreateValueFiles() throws Exception {
        String str = "file1.txt,file2.txt,file3.txt";
        Object result = TypeHandler.createValue(str, File[].class);
        Assert.assertNull(result);
    }

    @Test
    public void testCreateValueURL() throws Exception {
        String str = "https://www.example.com/";
        Object result = TypeHandler.createValue(str, URL.class);
        Assert.assertNotNull(result);
    }
}