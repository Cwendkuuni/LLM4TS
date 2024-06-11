package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;

public class TypeHandlerTest {

    @Test
    public void testCreateValueStringString() {
        String str = "foo";
        Class<?> clazz = String.class;
        Object obj = TypeHandler.createValue(str, clazz);
        assertEquals(str, obj);
    }

    @Test
    public void testCreateValueStringClassString() throws Exception {
        String str = "foo";
        Class<String> clazz = String.class;
        Object obj = TypeHandler.createValue(str, clazz);
        assertEquals(str, obj);

        String classname = "java.lang.Integer";
        Class<?> clazz2 = Class.forName(classname);
        Object obj2 = TypeHandler.createValue(str, clazz2);
        Assert.assertNotSame(str, obj2);
        Assert.assertEquals(Integer.valueOf(1), obj2);
    }

    @Test(expected = ParseException.class)
    public void testCreateValueStringClassInvalid() throws Exception {
        String str = "foo";
        Class<?> clazz = Integer.class;
        TypeHandler.createValue(str, clazz);
    }

    @Test
    public void testCreateValueStringClassArray() throws Exception {
        String str = "foo,bar,baz";
        Class<String[]> clazz = String[].class;
        Object obj = TypeHandler.createValue(str, clazz);
        String[] expected = new String[]{"foo", "bar", "baz"};
        assertArrayEquals(expected, obj);
    }

    @Test
    public void testCreateValueStringObject() throws Exception {
        String str = "foo";
        Object obj = new Object();
        Class<?> clazz = Object.class;
        Object result = TypeHandler.createValue(str, clazz);
        assertEquals(obj.getClass(), result.getClass());
    }

    @Test(expected = ParseException.class)
    public void testCreateValueStringObjectTypeMismatch() throws Exception {
        String str = "123";
        Object obj = new Object();
        Class<Integer> clazz = Integer.class;
        TypeHandler.createValue(str, clazz, obj);
    }

    @Test
    public void testCreateValueStringNumber() throws Exception {
        String str = "123";
        Class<Number> clazz = Number.class;
        Number result = (Number) TypeHandler.createValue(str, clazz);
        assertEquals(123, result.intValue());
    }

    @Test
    public void testCreateValueStringNumberDouble() throws Exception {
        String str = "3.14";
        Class<Number> clazz = Number.class;
        Number result = (Number) TypeHandler.createValue(str, clazz);
        assertEquals(3.14, result.doubleValue(), 0.0);
    }

    @Test(expected = ParseException.class)
    public void testCreateValueStringNumberInvalid() throws Exception {
        String str = "foo";
        Class<Number> clazz = Number.class;
        TypeHandler.createValue(str, clazz);
    }

    @Test
    public void testCreateValueStringDate() {
        String str = "2023-03-24";
        Class<Date> clazz = Date.class;
        Date result = (Date) TypeHandler.createValue(str, clazz);
        // Use Assert.assertSame() if Date objects are equal based on logic
        // Use Assert.assertEquals() if Date objects are equal based on time
        assertEquals(str, result.toString());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateValueStringDateUnsupported() {
        String str = "2023-03-24";
        Class<Date> clazz = Date.class;
        TypeHandler.createDate(str);
    }

    @Test
    public void testCreateValueStringURL() throws Exception {
        String str = "http://example.com";
        Class<URL> clazz = URL.class;
        URL result = (URL) TypeHandler.createValue(str, clazz);
        assertEquals(new URL("http://example.com"), result);
    }

    @Test(expected = ParseException.class)
    public void testCreateValueStringURLInvalid() throws Exception {
        String str = "foo://example.com";
        Class<URL> clazz = URL.class;
        TypeHandler.createValue(str, clazz);
    }

    @Test
    public void testCreateValueStringFile() throws Exception {
        String str = "path/to/file.txt";
        Class<File> clazz = File.class;
        File result = (File) TypeHandler.createValue(str, clazz);
        File expected = new File(str);
        assertEquals(expected.getAbsolutePath(), result.getAbsolutePath());
    }

    @Test(expected = ParseException.class)
    public void testCreateValueStringFileInvalid() throws Exception {
        String str = "foo:/path/to/file.txt";
        Class<File> clazz = File.class;
        TypeHandler.createValue(str, clazz);
    }

    @Test
    public void testCreateValueStringFileInputStream() throws Exception {
        String str = "path/to/file.txt";
        Class<FileInputStream> clazz = FileInputStream.class;
        File file = new File(str);
        FileInputStream result = (FileInputStream) TypeHandler.createValue(str, clazz);
        FileInputStream expected = new FileInputStream(file);
        assertEquals(expected.getFileDescriptor(), result.getFileDescriptor());
    }

    @Test(expected = ParseException.class)
    public void testCreateValueStringFileInputStreamInvalid() throws Exception {
        String str = "foo:/path/to/file.txt";
        Class<FileInputStream> clazz = FileInputStream.class;
        TypeHandler.createValue(str, clazz);
    }

    @Test
    public void testCreateValueStringFiles() {
        String str = "path/to/file1.txt,path/to/file2.txt";
        Class<File[]> clazz = File[].class;
        File[] result = (File[]) TypeHandler.createValue(str, clazz);
        File[] expected = {new File("path/to/file1.txt"), new File("path/to/file2.txt")};
        assertArrayEquals(expected, result);
    }
}