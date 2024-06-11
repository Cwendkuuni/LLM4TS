import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import org.apache.commons.cli.*;

public class TypeHandlerTest {

    private TypeHandler typeHandler;

    @Before
    public void setUp() {
        typeHandler = new TypeHandler();
    }

    @Test
    public void testCreateValueWithString() throws ParseException {
        String str = "test";
        Object result = typeHandler.createValue(str, PatternOptionBuilder.STRING_VALUE);
        Assert.assertEquals(str, result);
    }

    @Test
    public void testCreateValueWithObject() throws ParseException {
        String str = "java.lang.String";
        Object result = typeHandler.createValue(str, PatternOptionBuilder.OBJECT_VALUE);
        Assert.assertEquals(String.class, result);
    }

    @Test
    public void testCreateValueWithNumber() throws ParseException {
        String str = "10";
        Number result = typeHandler.createValue(str, PatternOptionBuilder.NUMBER_VALUE);
        Assert.assertEquals(10L, result);
    }

    @Test
    public void testCreateValueWithDate() throws ParseException {
        String str = "2021-10-01";
        Date result = typeHandler.createValue(str, PatternOptionBuilder.DATE_VALUE);
        Assert.assertNull(result);
    }

    @Test(expected = ParseException.class)
    public void testCreateValueWithInvalidClass() throws ParseException {
        String str = "invalid.class";
        typeHandler.createValue(str, PatternOptionBuilder.CLASS_VALUE);
    }

    @Test
    public void testCreateValueWithFile() throws ParseException {
        String str = "test.txt";
        File result = typeHandler.createValue(str, PatternOptionBuilder.FILE_VALUE);
        Assert.assertEquals(new File(str), result);
    }

    @Test(expected = ParseException.class)
    public void testOpenFileWithNonExistingFile() throws ParseException {
        String str = "non-existing-file.txt";
        typeHandler.openFile(str);
    }

    @Test
    public void testCreateFiles() {
        String str = "file1,file2,file3";
        File[] result = typeHandler.createFiles(str);
        Assert.assertNull(result);
    }

    @Test
    public void testCreateURL() throws ParseException {
        String str = "http://www.example.com";
        URL result = typeHandler.createURL(str);
        Assert.assertEquals(new URL(str), result);
    }
}