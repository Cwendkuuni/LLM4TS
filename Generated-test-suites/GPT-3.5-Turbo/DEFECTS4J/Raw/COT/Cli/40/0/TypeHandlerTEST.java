import org.junit.Test;
import org.apache.commons.cli.TypeHandler;
import org.apache.commons.cli.ParseException;

public class TypeHandlerTest {

    @Test
    public void testCreateValueWithString() throws ParseException {
        String str = "test";
        Object obj = TypeHandler.createValue(str, String.class);
        assertEqual(str, obj);
    }

    @Test
    public void testCreateValueWithObject() throws ParseException {
        String str = "test";
        Object obj = TypeHandler.createValue(str, Object.class);
        assertEqual(str, obj);
    }
    
    @Test
    public void testCreateValueWithNumberInteger() throws ParseException {
        String str = "10";
        Integer obj = TypeHandler.createValue(str, Integer.class);
        assertEqual(10, obj.intValue());
    }

    @Test
    public void testCreateValueWithNumberDouble() throws ParseException {
        String str = "10.5";
        Double obj = TypeHandler.createValue(str, Double.class);
        assertEqual(10.5, obj.doubleValue());
    }

    @Test
    public void testCreateValueWithDate() throws ParseException {
        String str = "2022-01-01";
        Date obj = TypeHandler.createValue(str, Date.class);
        assertEqual(str, obj.toString());
    }

    @Test
    public void testCreateValueWithClass() throws ParseException {
        String str = "java.lang.String";
        Class<?> obj = TypeHandler.createValue(str, Class.class);
        assertEqual(String.class, obj);
    }

    @Test
    public void testCreateValueWithFile() throws ParseException {
        String str = "/path/to/file.txt";
        File obj = TypeHandler.createValue(str, File.class);
        assertEqual(str, obj.getPath());
    }

    @Test
    public void testOpenFile() throws ParseException {
        String str = "/path/to/file.txt";
        FileInputStream obj = TypeHandler.openFile(str);
        assertNotNull(obj);
    }

    @Test
    public void testCreateFiles() throws ParseException {
        String str = "/path/to/*.txt";
        File[] obj = TypeHandler.createFiles(str);
        assertNotNull(obj);
    }

    @Test
    public void testCreateURL() throws ParseException {
        String str = "http://example.com";
        URL obj = TypeHandler.createURL(str);
        assertEqual(str, obj.toString());
    }
}