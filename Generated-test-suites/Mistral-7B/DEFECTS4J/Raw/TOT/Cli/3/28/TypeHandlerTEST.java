import org.apache.commons.cli.TypeHandler;
import org.apache.commons.lang.ClassUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.net.URL;
import java.util.Date;

public class TypeHandlerTest {

    @Test
    public void testCreateValue_String_String() {
        String str = "testString";
        String clazzName = String.class.getName();
        Object expected = str;

        Object actual = TypeHandler.createValue(str, clazzName);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testCreateValue_String_Class_StringType() {
        String str = "testString";
        Class clazz = String.class;
        String expected = str;

        Object actual = TypeHandler.createValue(str, clazz);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testCreateValue_String_Class_ObjectType() {
        String str = "com.example.MyClass";
        Class clazz = Object.class;

        Class actualClazz = null;
        Object actualObject = null;

        Object actual = TypeHandler.createValue(str, clazz);

        if (actual instanceof Class) {
            actualClazz = (Class) actual;
        } else if (actual instanceof Object) {
            actualObject = (Object) actual;
        }

        String expectedClassName = "com.example.MyClass";
        if (actualClazz != null) {
            Assert.assertEquals(expectedClassName, actualClazz.getName());
        } else {
            Assert.assertNotNull(actualObject);
            Assert.assertEquals(expectedClassName, ClassUtils.getClassDescriptor(actualObject.getClass()).getName());
        }
    }

    @Test(expected = ClassNotFoundException.class)
    public void testCreateValue_String_Class_NonExistentClass() {
        String str = "non.existent.Class";
        Class clazz = Class.class;

        Object actual = TypeHandler.createValue(str, clazz);

        Assert.fail("Expected ClassNotFoundException, but received: " + actual);
    }

    @Test(expected = InstantiationException.class)
    public void testCreateObject_InvalidConstructor() {
        String str = "java.util.concurrent.ConcurrentModificationException";

        Object actual = TypeHandler.createObject(str);

        Assert.fail("Expected InstantiationException, but received: " + actual);
    }

    @Test
    public void testCreateNumber() {
        String str = "123";
        Number expected = 123;

        Number actual = TypeHandler.createNumber(str);

        Assert.assertEquals(expected.longValue(), actual.longValue());
    }

    @Test
    public void testCreateNumber_Decimal() {
        String str = "3.14";
        Double expected = 3.14;

        Number actual = TypeHandler.createNumber(str);

        Assert.assertEquals(expected, actual);
    }

    @Test(expected = NumberFormatException.class)
    public void testCreateNumber_InvalidNumber() {
        String str = "invalidNumber";

        Number actual = TypeHandler.createNumber(str);

        Assert.fail("Expected NumberFormatException, but received: " + actual);
    }

    @Test
    public void testCreateClass_ValidClass() {
        String str = "java.lang.String";
        Class expected = String.class;

        Class actual = TypeHandler.createClass(str);

        Assert.assertEquals(expected, actual);
    }

    @Test(expected = ClassNotFoundException.class)
    public void testCreateClass_InvalidClass() {
        String str = "non.existent.Class";

        Class actual = TypeHandler.createClass(str);

        Assert.fail("Expected ClassNotFoundException, but received: " + actual);
    }

    @Test
    public void testCreateDate_ValidDate() {
        String str = "2022-01-01";
        Date expected = new Date(0);
        expected.setYear(2022);
        expected.setMonth(0);
        expected.setDate(1);

        Date actual = TypeHandler.createDate(str);

        Assert.assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateDate_InvalidDateFormat() {
        String str = "invalidDateFormat";

        Date actual = TypeHandler.createDate(str);

        Assert.fail("Expected IllegalArgumentException, but received: " + actual);
    }

    @Test
    public void testCreateURL() {
        String str = "http://example.com";
        URL expected = new URL("http", "example.com", -1, "", null);

        URL actual = TypeHandler.createURL(str);

        Assert.assertEquals(expected, actual);
    }

    @Test(expected = MalformedURLException.class)
    public void testCreateURL_InvalidURL() {
        String str = "invalidURL";

        URL actual = TypeHandler.createURL(str);

        Assert.fail("Expected MalformedURLException, but received: " + actual);
    }

    @Test
    public void testCreateFile_ValidFile() {
        String str = "/path/to/file.txt";
        File expected = new File(str);

        File actual = TypeHandler.createFile(str);

        Assert.assertEquals(expected, actual);
    }

    @Test(expected = FileNotFoundException.class)
    public void testCreateFile_InvalidFile() {
        String str = "/path/to/non-existent-file.txt";

        File actual = TypeHandler.createFile(str);

        Assert.fail("Expected FileNotFoundException, but received: " + actual);
    }

    @Test
    public void testCreateFiles_ValidFiles() {
        String str = "/path/to/file1.txt,/path/to/file2.txt";
        File[] expected = new File[] {new File("/path/to/file1.txt"), new File("/path/to/file2.txt")};

        File[] actual = TypeHandler.createFiles(str);

        Assert.assertArrayEquals(expected, actual);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testCreateFiles_InvalidDelimiter() {
        String str = "/path/to/file1.txt,/path/to/file2.txt,";

        File[] actual = TypeHandler.createFiles(str);

        Assert.fail("Expected ArrayIndexOutOfBoundsException, but received: " + actual);
    }
}