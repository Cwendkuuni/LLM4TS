import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.cli.PatternOptionBuilder;
import org.junit.Test;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import org.apache.commons.lang.math.NumberUtils;

public class TypeHandlerTest {

    @Test
    public void testCreateValue_StringObject() {
        String str = "test";
        Object obj = mock(Object.class);
        Class clazz = mock(Class.class);

        when((Class) obj).thenReturn(clazz);
        when(createValue(str, (Class) obj)).thenReturn(obj);

        assertEquals(obj, TypeHandler.createValue(str, obj));
    }

    @Test
    public void testCreateValue_StringClass() {
        String str = "test";
        Class clazz = mock(Class.class);

        when(createValue(str, clazz)).thenReturn(null);

        assertNull(TypeHandler.createValue(str, clazz));
    }

    @Test
    public void testCreateObject() {
        String str = "test";
        Class cl = mock(Class.class);
        Object instance = mock(Object.class);

        when(cl.newInstance()).thenReturn(instance);
        when(Class.forName(str)).thenReturn(cl);
        when(createObject(str)).thenReturn(instance);

        assertEquals(instance, TypeHandler.createObject(str));
    }

    @Test
    public void testCreateNumber() {
        String str = "test";
        Number number = mock(Number.class);

        when(NumberUtils.createNumber(str)).thenReturn(number);
        when(createNumber(str)).thenReturn(number);

        assertEquals(number, TypeHandler.createNumber(str));
    }

    @Test
    public void testCreateClass() {
        String str = "test";
        Class clazz = mock(Class.class);

        when(Class.forName(str)).thenReturn(clazz);
        when(createClass(str)).thenReturn(clazz);

        assertEquals(clazz, TypeHandler.createClass(str));
    }

    @Test
    public void testCreateDate() {
        String str = "test";
        Date date = mock(Date.class);

        when(createDate(str)).thenReturn(date);

        assertEquals(date, TypeHandler.createDate(str));
    }

    @Test
    public void testCreateURL() throws MalformedURLException {
        String str = "test";
        URL url = mock(URL.class);

        when(new URL(str)).thenReturn(url);
        when(createURL(str)).thenReturn(url);

        assertEquals(url, TypeHandler.createURL(str));
    }

    @Test
    public void testCreateFile() {
        String str = "test";
        File file = mock(File.class);

        when(new File(str)).thenReturn(file);
        when(createFile(str)).thenReturn(file);

        assertEquals(file, TypeHandler.createFile(str));
    }

    @Test
    public void testCreateFiles() {
        String str = "test";
        File[] files = mock(File[].class);

        when(createFiles(str)).thenReturn(files);

        assertEquals(files, TypeHandler.createFiles(str));
    }
}