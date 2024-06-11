import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.TypeHandler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

@RunWith(PowerMockRunner.class)
public class TypeHandlerTest
{
    @Test
    public void testCreateValueWithStringObject() throws Exception
    {
        String str = "test";
        Object obj = new Object();
        Object value = TypeHandler.createValue(str, obj.getClass());

        assertEquals(str, value);
    }

    @Test
    public void testCreateValueWithStringClass() throws Exception
    {
        String str = "test";
        Class<String> clazz = String.class;
        String value = (String) TypeHandler.createValue(str, clazz);

        assertEquals(str, value);
    }

    @Test(expected = ParseException.class)
    public void testCreateValueWithInvalidClass() throws Exception
    {
        String str = "test";
        Class<Integer> clazz = Integer.class;

        TypeHandler.createValue(str, clazz);
    }

    @Test
    public void testCreateObject() throws Exception
    {
        String classname = "java.lang.String";
        Object value = TypeHandler.createObject(classname);

        assertTrue(value instanceof String);
    }

    @Test(expected = ParseException.class)
    public void testCreateObjectWithInvalidClassName() throws Exception
    {
        String classname = "TestClassThatDoesNotExist";

        TypeHandler.createObject(classname);
    }

    @Test
    public void testCreateNumber() throws Exception
    {
        String str = "123";
        Number value = TypeHandler.createNumber(str);

        assertEquals(123, value.longValue());
    }

    @Test(expected = ParseException.class)
    public void testCreateNumberWithInvalidString() throws Exception
    {
        String str = "test";

        TypeHandler.createNumber(str);
    }

    @Test
    public void testCreateDate() throws Exception
    {
        String str = "2022-12-31";
        Date value = TypeHandler.createDate(str);

        assertTrue(value instanceof Date);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateDateWithInvalidString() throws Exception
    {
        String str = "test";

        TypeHandler.createDate(str);
    }

    @Test
    public void testCreateURL() throws Exception
    {
        String str = "http://test.com";
        URL value = TypeHandler.createURL(str);

        assertSame(new URL("http://test.com"), value);
    }

    @Test(expected = ParseException.class)
    public void testCreateURLWithInvalidString() throws Exception
    {
        String str = "test";

        TypeHandler.createURL(str);
    }

    @Test
    public void testCreateFile() throws Exception
    {
        String str = "test.txt";
        File value = TypeHandler.createFile(str);

        assertTrue(value.exists());
        assertTrue(value.delete());
    }

    @Test(expected = ParseException.class)
    public void testCreateFileWithInvalidString() throws Exception
    {
        String str = "test";

        TypeHandler.createFile(str);
    }

    @Test
    public void testOpenFile() throws Exception
    {
        String str = "test.txt";
        File file = new File(str);
        PowerMockito.mockStatic(File.class);
        PowerMockito.when(File.exists(file.getAbsolutePath())).thenReturn(true);

        FileInputStream value = TypeHandler.openFile(str);

        assertNotNull(value);
        assertTrue(value.isClosed());
    }

    @Test(expected = ParseException.class)
    public void testOpenFileWithInvalidString() throws Exception
    {
        String str = "test";

        TypeHandler.openFile(str);
    }
}