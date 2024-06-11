package org.apache.commons.cli.test;

import org.apache.commons.cli.TypeHandler;
import org.junit.Test;
import static org.junit.Assert.*;

public class TypeHandlerTest {

    @Test
    public void testCreateValueStringObject() {
        String str = "test";
        Object obj = String.class;
        Object result = TypeHandler.createValue(str, obj);
        assertEquals(str, result);
    }

    @Test
    public void testCreateObject() {
        String className = "java.lang.String";
        Object result = TypeHandler.createObject(className);
        assertNotNull(result);
    }

    @Test
    public void testCreateNumber() {
        String str = "123";
        Number result = TypeHandler.createNumber(str);
        assertEquals(123L, result.longValue());
    }

    @Test
    public void testCreateNumberWithDecimal() {
        String str = "123.45";
        Number result = TypeHandler.createNumber(str);
        assertEquals(123.45, result.doubleValue(), 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateValueWithNullStr() {
        Object obj = Integer.class;
        TypeHandler.createValue(null, obj);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateValueWithNullObj() {
        String str = "test";
        TypeHandler.createValue(str, null);
    }

    @Test(expected = IllegaArgmentException.class)
    public void testCreateValueWithMismatchedType() {
        String str = "test";
        Object obj = Double.class;
        TypeHandler.createValue(str, obj.getClass());
    }

    @Test(expected = NumberFormatException.class)
    public void testCreateNumberWithInvalidNumber() {
        String str = "abc123";
        TypeHandler.createNumber(str);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateObjectWithNonExistentClass() {
        String className = "java.lang.Xyz";
        TypeHandler.createObject(className);
    }

    @Test(expected = InstantiationException.class)
    public void testCreateObjectWithNoArgsConstructorMissing() {
        String className = "java.lang.MyClassWithoutNoArgsConstructor";
        TypeHandler.createObject(className);
    }

    @Test(expected = IllegalAccessException.class)
    public void testCreateObjectWithNoAccessToConstructor() {
        String className = "java.lang.MyClassWithPrivateConstructor";
        TypeHandler.createObject(className);
    }

    @Test(expected = NumberFormatException.class)
    public void testCreateNumberWithLargeNumber() {
        String str = Long.MAX_VALUE + "";
        TypeHandler.createNumber(str);
    }

    @Test
    public void testCreateDate() {
        String dateStr = "2022-12-31";
        Date result = TypeHandler.createDate(dateStr);
        assertNotNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateDateWithInvalidFormat() {
        String dateStr = "2022-12-32";
        TypeHandler.createDate(dateStr);
    }

    @Test(expected = MalformedURLException.class)
    public void testCreateURLWithInvalidFormat() {
        String urlStr = "http:/example.com";
        TypeHandler.createURL(urlStr);
    }

    @Test
    public void testCreateFile() {
        String filePath = "test.txt";
        File result = TypeHandler.createFile(filePath);
        assertNotNull(result);
    }

    @Test(expected = FileNotFoundException.class)
    public void testCreateFileWithNonExistentFilePath() {
        String filePath = "nonexistent.txt";
        TypeHandler.createFile(filePath);
    }

    @Test(expected = IOException.class)
    public void testCreateFilesWithInvalidFilePaths() {
        String filePaths = "test.txt, nonexistent.txt";
        TypeHandler.createFiles(filePaths);
    }
}