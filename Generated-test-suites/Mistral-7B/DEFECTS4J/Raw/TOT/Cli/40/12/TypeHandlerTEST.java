import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class TypeHandlerTest {

    @Test
    public void testCreateValueStringObject() {
        Object actual = TypeHandler.createValue("test", String.class);
        Assert.assertEquals("test", actual);
    }

    @Test
    public void testCreateValueStringClass() {
        Object actual = null;
        try {
            actual = TypeHandler.createValue("test", String[].class);
        } catch (ParseException e) {
            // Expected
        }
        Assert.assertNull(actual);
    }

    @Test
    public void testCreateValueStringClassObject() {
        Object actual = null;
        try {
            String[] expected = {"test1", "test2"};
            actual = (Object) TypeHandler.createValue("test1,test2", String[].class);
            Assert.assertArrayEquals(expected, (String[]) actual);
        } catch (ParseException e) {
            // Expected
        }
    }

    @Test
    public void testCreateValueStringNumber() {
        Number actual = null;
        try {
            actual = TypeHandler.createValue("123", int.class);
            Assert.assertEquals(123, actual.intValue());
        } catch (ParseException e) {
            // Expected
        }
    }

    @Test
    public void testCreateValueStringNumberDouble() {
        Number actual = null;
        try {
            actual = TypeHandler.createValue("123.45", double.class);
            Assert.assertEquals(123.45, actual.doubleValue(), 0.001);
        } catch (ParseException e) {
            // Expected
        }
    }

    @Test(expected = ParseException.class)
    public void testCreateValueStringNumberInvalid() {
        TypeHandler.createValue("123abcd", int.class);
    }

    @Test
    public void testCreateValueStringClassWithConstructorThrowException() {
        Class<?> clazz = null;
        try {
            clazz = Class.forName("java.lang.StringBuilder");
        } catch (ClassNotFoundException e) {
            // Expected
        }
        try {
            Object actual = TypeHandler.createValue("test", clazz);
            Assert.fail();
        } catch (ParseException e) {
            // Expected
        }
    }

    @Test
    public void testCreateValueStringClassWithPublicNoArgConstructor() {
        Class<?> clazz = String[].class;
        Object actual = TypeHandler.createValue("[test]", clazz);
        String[] expected = {"test"};
        Assert.assertArrayEquals(expected, (String[]) actual);
    }

    @Test
    public void testCreateValueStringClassWithPrivateNoArgConstructor() {
        Class<?> clazz = AccessibleObject.class;
        try {
            Object actual = TypeHandler.createValue("test", clazz);
            Assert.fail();
        } catch (IllegalAccessException e) {
            // Expected
        } catch (InstantiationException e) {
            // Expected
        } catch (ParseException e) {
            // Expected
        }
    }

    @Test
    public void testCreateValueStringClassWithConstructorArgument() {
        Class<?> clazz = File.class;
        Object actual = null;
        try {
            actual = TypeHandler.createValue("/test/test.txt", clazz);
            Assert.assertInstanceOf(File.class, actual);
            File expected = new File("/test/test.txt");
            Assert.assertEquals(expected.getAbsolutePath(), ((File) actual).getAbsolutePath());
        } catch (ParseException e) {
            // Expected
        } catch (SecurityException e) {
            // Expected, since File constructor with a file as argument has security manager check
        }
    }

    @Test
    public void testCreateValueStringClassWithConstructorArgumentThrowException() {
        Class<?> clazz = FileInputStream.class;
        try {
            Object actual = TypeHandler.createValue("/test/test.txt", clazz);
            Assert.fail();
        } catch (FileNotFoundException e) {
            // Expected
        } catch (ParseException e) {
            // Expected
        }
    }

    @Test
    public void testCreateValueStringURL() {
        URL actual = null;
        try {
            actual = TypeHandler.createValue("http://test.com", URL.class);
            Assert.assertEquals("http://test.com", actual.toString());
        } catch (MalformedURLException e) {
            // Expected
        }
    }

    @Test(expected = MalformedURLException.class)
    public void testCreateValueStringURLInvalid() {
        TypeHandler.createValue("http://test.com/test", URL.class);
    }

    @Test
    public void testCreateFile() {
        String path = "test.txt";
        File actual = TypeHandler.createFile(path);
        File expected = new File(path);
        Assert.assertEquals(expected.getAbsolutePath(), actual.getAbsolutePath());
    }

    @Test(expected = ParseException.class)
    public void testCreateFileInvalidPath() {
        TypeHandler.createFile("test//test.txt");
    }

    @Test
    public void testOpenFile() throws IOException {
        String path = "test.txt";
        File file = new File(path);
        FileInputStream actual = TypeHandler.openFile(path);
        FileInputStream expected = new FileInputStream(file);
        Assert.assertEquals(expected.available(), actual.available());
        actual.close();
        expected.close();
    }

    @Test(expected = FileNotFoundException.class)
    public void testOpenFileNonExistingFile() {
        TypeHandler.openFile("nonExistingFile.txt");
    }

    @Test(expected = ParseException.class)
    public void testOpenFileInvalidPath() {
        TypeHandler.openFile("test//test.txt");
    }

    @Test
    public void testCreateFiles() {
        String path = "test";
        String[] files = {"test1.txt", "test2.txt"};
        File[] actual = TypeHandler.createFiles(path);
        File[] expected = new File[files.length];
        for (int i = 0; i < files.length; i++) {
            expected[i] = new File(path, files[i]);
        }
        Assert.assertArrayEquals(expected, actual);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateDate() {
        TypeHandler.createDate("2022-01-01");
    }
}