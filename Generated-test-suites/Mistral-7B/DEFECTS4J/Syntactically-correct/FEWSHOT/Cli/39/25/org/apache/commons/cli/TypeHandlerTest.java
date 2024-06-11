package org.apache.commons.cli;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;

import org.junit.Test;

public class TypeHandlerTest {

    @Test
    public void testCreateValueWithStringType() {
        String str = "test";
        assertEquals(str, TypeHandler.createValue(str, String.class));
    }

    @Test
    public void testCreateValueWithObjectType() {
        Class<?> clazz = String.class;
        assertNull(TypeHandler.createValue("", clazz));
        String className = "java.lang.String";
        String str = "test";
        Object obj = TypeHandler.createValue(className, clazz);
        assertInstanceOf(String.class, obj);
        assertEquals(str, obj.toString());
    }

    @Test
    public void testCreateValueWithNumberType() {
        String numberStr1 = "123";
        Long number1 = 123L;
        Long number2 = TypeHandler.createValue(numberStr1, Long.class);
        assertEquals(number1, number2);

        String numberStr2 = "4.5";
        Double number3 = 4.5;
        Double number4 = TypeHandler.createValue(numberStr2, Double.class);
        assertEquals(number3, number4);
    }

    @Test
    public void testCreateValueWithDateType() {
        String dateStr = "2022-12-31";
        Date date = new Date(0L);
        try {
            Date date1 = TypeHandler.createDate(dateStr);
            assertEquals(date, date1);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testCreateValueWithClassType() {
        String className = "java.lang.String";
        Class<?> clazz = String.class;
        Object obj = TypeHandler.createValue(className, clazz);
        assertInstanceOf(String.class, obj);
    }

    @Test
    public void testCreateValueWithFileType() {
        String filePath = "test.txt";
        File file = new File(filePath);
        File file1 = TypeHandler.createFile(filePath);
        assertEquals(file, file1);
    }

    @Test
    public void testCreateValueWithExistingFileType() {
        String filePath = "test.txt";
        File file = new File(filePath);
        assertThrows(FileNotFoundException.class, () -> TypeHandler.createFileInputStream(filePath));
    }

    @Test
    public void testCreateValueWithURLType() {
        String urlStr = "http://test.com";
        URL url = new URL(urlStr);
        URL url1 = TypeHandler.createURL(urlStr);
        assertEquals(url, url1);
    }

    @Test
    public void testCreateValueWithUnknownType() {
        String unknownTypeStr = "unknownType";
        assertNull(TypeHandler.createValue(unknownTypeStr, Object.class));
    }

    @Test
    public void testCreateObject() throws ClassNotFoundException, InstantiationException {
        String classname = "java.lang.String";
        Class<?> clazz = Class.forName(classname);
        Object obj = TypeHandler.createObject(classname);
        assertInstanceOf(String.class, obj);
        assertEquals(obj.toString(), "");
    }

    @Test
    public void testCreateNumber() {
        String numberStr1 = "123";
        Long number1 = 123L;
        Long number2 = TypeHandler.createNumber(numberStr1).longValue();
        assertEquals(number1, number2);

        String numberStr2 = "4.5";
        Double number3 = 4.5;
        Double number4 = TypeHandler.createNumber(numberStr2);
        assertEquals(number3, number4);
    }

    @Test
    public void testCreateClass() throws ClassNotFoundException {
        String classname = "java.lang.String";
        Class<?> clazz = TypeHandler.createClass(classname);
        assertEquals(String.class, clazz);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateDate() {
        String dateStr = "2022-12-31";
        TypeHandler.createDate(dateStr);
    }

    @Test
    public void testCreateFileInputStream() throws FileNotFoundException {
        String filePath = "test.txt";
        File file = new File(filePath);
        FileInputStream fileInputStream = TypeHandler.createFileInputStream(filePath);
        try {
            fileInputStream.close();
        } catch (IOException e) {
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateFiles() {
        String filePaths = "test1.txt, test2.txt";
        TypeHandler.createFiles(filePaths);
    }
}