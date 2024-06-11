package org.apache.commons.cli.test;

import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.TypeHandler;
import org.junit.Test;
import org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import static org.mockito.Mockito.*;

public class TypeHandlerTest
{
    @Test
    public void testCreateValueWithStringClass() throws ParseException
    {
        String str = "test";
        String expected = "test";
        Object actual = TypeHandler.createValue(str, String.class);
        assertEquals(expected, actual);
    }

    @Test
    public void testCreateValueWithObjectClass() throws ParseException, ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        String className = "java.lang.String";
        String expected = "Test String";
        Object actual = TypeHandler.createValue(className, String.class);
        assertEquals(expected, actual);

        Class<?> clazz = Class.forName(className);
        Object mockObject = mock(clazz);
        when(mockObject.toString()).thenReturn("Test String");
        Object actualWithMock = TypeHandler.createValue(className, mockObject.getClass());
        assertEquals(expected, actualWithMock.toString());
        verify(mockObject).toString();
    }

    @Test(expected = ParseException.class)
    public void testCreateValueWithInvalidClass() throws ParseException
    {
        TypeHandler.createValue("test", Integer.class);
    }

    @Test
    public void testCreateNumber() throws ParseException
    {
        String str = "123";
        Number expected = 123;
        Number actual = TypeHandler.createNumber(str);
        assertEquals(expected, actual);
    }

    @Test(expected = ParseException.class)
    public void testCreateNumberWithInvalidString() throws ParseException
    {
        String str = "abcd";
        TypeHandler.createNumber(str);
    }

    @Test
    public void testCreateDate() throws ParseException
    {
        String str = "2022-12-31";
        Date expected = new Date(2022, 11, 31);
        Date actual = TypeHandler.createDate(str);
        assertEquals(expected, actual);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateDateWithInvalidString() throws ParseException
    {
        String str = "test";
        TypeHandler.createDate(str);
    }

    @Test
    public void testCreateURL() throws ParseException, MalformedURLException
    {
        String str = "http://test.com";
        URL expected = new URL("http://test.com");
        URL actual = TypeHandler.createURL(str);
        assertEquals(expected, actual);
    }

    @Test(expected = ParseException.class)
    public void testCreateURLWithInvalidString() throws ParseException
    {
        String str = "test";
        TypeHandler.createURL(str);
    }

    @Test
    public void testCreateFile()
    {
        String str = "path/to/file.txt";
        File expected = new File("path/to/file.txt");
        File actual = TypeHandler.createFile(str);
        assertEquals(expected, actual);
    }

    @Test(expected = FileNotFoundException.class)
    public void testOpenFileWithInvalidFilePath() throws ParseException, FileNotFoundException
    {
        String path = "test";
        TypeHandler.openFile(path);
    }

    @Test
    public void testOpenFileWithValidFilePath() throws ParseException, IOException
    {
        String path = "path/to/file.txt";
        FileInputStream expected = new FileInputStream(new File(path));
        FileInputStream actual = TypeHandler.openFile(path);
        assertEquals(expected, actual);
    }

    @Test(expected = ParseException.class)
    public void testCreateFilesWithInvalidString() throws ParseException
    {
        String str = "test";
        TypeHandler.createFiles(str);
    }
}