import org.apache.commons.cli.TypeHandler;
import org.junit.Test;
import static org.junit.Assert.*;

public class TypeHandlerTest {

    @Test
    public void testCreateValue_StringObject() {
        Object result = TypeHandler.createValue("test", String.class);
        assertEquals("test", result);
    }

    @Test
    public void testCreateValue_StringClass() {
        Class clazz = Integer.class;
        Object result = TypeHandler.createValue("123", clazz);
        assertEquals(123, result);

        clazz = Double.class;
        result = TypeHandler.createValue("3.14", clazz);
        assertEquals(3.14, result);

        clazz = Date.class;
        result = TypeHandler.createValue("2022-01-01", clazz);
        assertEquals(new Date(2022, 0, 1), result);

        clazz = File.class;
        result = TypeHandler.createValue("/path/to/file", clazz);
        assertNotNull(result);

        clazz = URL.class;
        result = TypeHandler.createValue("http://example.com", clazz);
        assertNotNull(result);

        clazz = PatternOptionBuilder.STRING_VALUE.getClass();
        result = TypeHandler.createValue("test", clazz);
        assertEquals(PatternOptionBuilder.STRING_VALUE, result);

        clazz = PatternOptionBuilder.OBJECT_VALUE.getClass();
        result = TypeHandler.createValue("java.lang.String", clazz);
        assertNotNull(result);

        clazz = PatternOptionBuilder.NUMBER_VALUE.getClass();
        result = TypeHandler.createValue("123", clazz);
        assertEquals(PatternOptionBuilder.NUMBER_VALUE, result);

        clazz = PatternOptionBuilder.DATE_VALUE.getClass();
        result = TypeHandler.createValue("2022-01-01", clazz);
        assertEquals(PatternOptionBuilder.DATE_VALUE, result);

        clazz = PatternOptionBuilder.CLASS_VALUE.getClass();
        result = TypeHandler.createValue("java.lang.String", clazz);
        assertNotNull(result);

        clazz = PatternOptionBuilder.FILE_VALUE.getClass();
        result = TypeHandler.createValue("/path/to/file", clazz);
        assertNotNull(result);

        clazz = PatternOptionBuilder.EXISTING_FILE_VALUE.getClass();
        result = TypeHandler.createValue("/path/to/existing/file", clazz);
        assertNotNull(result);

        clazz = PatternOptionBuilder.FILES_VALUE.getClass();
        String[] paths = new String[]{"/path/to/file1", "/path/to/file2"};
        result = TypeHandler.createValue(String.join(":", paths), clazz);
        assertArrayEquals(paths, ((File[]) result).clone());

        clazz = PatternOptionBuilder.URL_VALUE.getClass();
        String[] urls = new String[]{"http://example1.com", "http://example2.com"};
        result = TypeHandler.createValue(String.join(":", urls), clazz);
        assertArrayEquals(urls, ((URL[]) result).clone());
    }

    @Test(expected = NullPointerException.class)
    public void testCreateValue_NullStr() {
        TypeHandler.createValue(null, String.class);
    }

    @Test(expected = NullPointerException.class)
    public void testCreateValue_NullClass() {
        TypeHandler.createValue("test", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateValue_InvalidClass() {
        TypeHandler.createValue("test", Integer[].class);
    }

    @Test(expected = ClassNotFoundException.class)
    public void testCreateObject_InvalidClassName() {
        TypeHandler.createObject("java.lang.InvalidClass");
    }

    @Test(expected = InstantiationException.class)
    public void testCreateObject_ClassHasNoConstructor() {
        Class clazz = new Class() {};
        TypeHandler.createObject(clazz.getName());
    }

    @Test(expected = IllegalAccessException.class)
    public void testCreateObject_ClassAccessDenied() {
        AccessController.doPrivileged((PrivilegedAction<Object>) () -> {
            Class clazz = AccessController.doPrivileged((PrivilegedExceptionAction<Class<Object>>) () -> {
                Class<Object> c = Class.forName("java.lang.reflect.AnnotatedElement");
                c.getDeclaredConstructor(new Class[0]).setAccessible(true);
                return c;
            });
            TypeHandler.createObject(clazz.getName());
        });
    }
}