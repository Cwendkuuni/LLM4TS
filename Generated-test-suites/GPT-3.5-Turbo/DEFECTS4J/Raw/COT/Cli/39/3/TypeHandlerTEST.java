import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.cli.ParseException;

public class TypeHandlerTest {
    
    @Test
    public void testCreateValueStringObject() {
        String str = "test";
        Object obj = "test";
        
        try {
            Object result = TypeHandler.createValue(str, obj);
            Assert.assertEquals("Test createValueStringObject", obj, result);
        } catch (ParseException e) {
            Assert.fail("Test createValueStringObject threw an exception: " + e.getMessage());
        }
    }
    
    @Test
    public void testCreateValueStringClass() {
        String str = "test";
        Class<?> clazz = String.class;
        
        try {
            Object result = TypeHandler.createValue(str, clazz);
            Assert.assertEquals("Test createValueStringClass", str, result);
        } catch (ParseException e) {
            Assert.fail("Test createValueStringClass threw an exception: " + e.getMessage());
        }
    }
    
    @Test
    public void testCreateObject() {
        String classname = "java.lang.String";
        
        try {
            Object result = TypeHandler.createObject(classname);
            Assert.assertEquals("Test createObject", "", result);
        } catch (ParseException e) {
            Assert.fail("Test createObject threw an exception: " + e.getMessage());
        }
    }
    
    @Test
    public void testCreateNumber() {
        String str = "10";
        
        try {
            Number result = TypeHandler.createNumber(str);
            Assert.assertEquals("Test createNumber", 10L, result);
        } catch (ParseException e) {
            Assert.fail("Test createNumber threw an exception: " + e.getMessage());
        }
    }
    
    @Test
    public void testCreateClass() {
        String classname = "java.lang.String";
        
        try {
            Class<?> result = TypeHandler.createClass(classname);
            Assert.assertEquals("Test createClass", String.class, result);
        } catch (ParseException e) {
            Assert.fail("Test createClass threw an exception: " + e.getMessage());
        }
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testCreateDate() {
        String str = "2020-01-01";
        TypeHandler.createDate(str);
    }
    
    @Test
    public void testCreateURL() {
        String str = "http://www.example.com";
        
        try {
            URL result = TypeHandler.createURL(str);
            Assert.assertEquals("Test createURL", new URL(str), result);
        } catch (ParseException e) {
            Assert.fail("Test createURL threw an exception: " + e.getMessage());
        } catch (MalformedURLException e) {
            Assert.fail("Test createURL threw a MalformedURLException: " + e.getMessage());
        }
    }
    
    @Test
    public void testCreateFile() {
        String str = "test.txt";
        File expected = new File(str);
        File result = TypeHandler.createFile(str);
        Assert.assertEquals("Test createFile", expected, result);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testCreateFiles() {
        String str = "file1.txt,file2.txt";
        TypeHandler.createFiles(str);
    }

}