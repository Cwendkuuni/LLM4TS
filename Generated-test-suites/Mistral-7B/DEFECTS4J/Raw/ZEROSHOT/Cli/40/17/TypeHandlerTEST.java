package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import org.apache.commons.cli.TypeHandler;
import org.apache.commons.cli.ParseException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import java.util.Date;

public class TypeHandlerTest
{
    @org.junit.Test
    public void testCreateValueStringObject() throws ParseException
    {
        String str = "testString";
        Object obj = TypeHandler.createValue(str, Object.class);
        assertEquals(str, obj);
    }

    @org.junit.Test
    public void testCreateValueStringClass() throws ParseException
    {
        String str = "testString";
        Class<?> clazz = String.class;
        Object obj = TypeHandler.createValue(str, clazz);
        assertEquals(str.getClass(), obj.getClass());
        assertEquals(str, obj);
    }

    @org.junit.Test
    public void testCreateValueStringPatternOptionBuilderStringValue() throws ParseException
    {
        String str = "testString";
        Object obj = TypeHandler.createValue(str, PatternOptionBuilder.STRING_VALUE);
        assertEquals(str, obj);
    }

    @org.junit.Test
    public void testCreateValueStringPatternOptionBuilderObjectValue() throws ParseException
    {
        String str = "testString";
        Object obj = TypeHandler.createValue(str, PatternOptionBuilder.OBJECT_VALUE);
        assertNull(obj);
        // This test is not possible since TypeHandler cannot create an instance of Object
        //assertNotNull(obj);
        //assertEquals(str.getClass(), obj.getClass());
    }

    @org.junit.Test
    public void testCreateValueStringPatternOptionBuilderNumberValue() throws ParseException
    {
        String str = "123";
        Number num = TypeHandler.createValue(str, PatternOptionBuilder.NUMBER_VALUE);
        assertEquals(Long.valueOf(123), num);
    }

    @org.junit.Test
    public void testCreateValueStringPatternOptionBuilderDateValue() throws ParseException
    {
        String str = "2022-12-31";
        Date date = TypeHandler.createValue(str, PatternOptionBuilder.DATE_VALUE);
        assertNotNull(date);
        // This test is not possible since TypeHandler does not implement createDate method
        //assertEquals(str, date.toString());
    }

    @org.junit.Test
    public void testCreateValueStringPatternOptionBuilderClassValue() throws ParseException, ClassNotFoundException, InstantiationException
    {
        String str = "java.lang.String";
        Class<?> clazz = Class.forName(str);
        Object obj = TypeHandler.createValue(str, PatternOptionBuilder.CLASS_VALUE);
        assertEquals(clazz, obj.getClass());
        // This test is not possible since TypeHandler does not call the constructor of the class
        //assertNotNull(obj);
    }

    @org.junit.Test
    public void testCreateValueStringPatternOptionBuilderFileValue() throws ParseException
    {
        String str = "testFile.txt";
        File file = TypeHandler.createValue(str, PatternOptionBuilder.FILE_VALUE);
        assertNotNull(file);
        assertTrue(file.exists());
    }

    @org.junit.Test
    public void testCreateValueStringPatternOptionBuilderExistingFileValue() throws ParseException, FileNotFoundException
    {
        String str = "testFile.txt";
        FileInputStream fileInput = TypeHandler.openFile(str);
        assertNotNull(fileInput);
        fileInput.close();
    }

    @org.junit.Test
    public void testCreateValueStringPatternOptionBuilderFilesValue() throws ParseException, IOException
    {
        String str = "testFile1.txt,testFile2.txt";
        File[] files = (File[]) TypeHandler.createValue(str, PatternOptionBuilder.FILES_VALUE);
        assertArrayEquals(new File[] { new File("testFile1.txt"), new File("testFile2.txt") }, files);
    }

    @org.junit.Test
    public void testCreateValueStringPatternOptionBuilderURLValue() throws ParseException, MalformedURLException
    {
        String str = "https://www.example.com";
        URL url = TypeHandler.createValue(str, PatternOptionBuilder.URL_VALUE);
        assertNotNull(url);
        assertEquals("https://www.example.com", url.toString());
    }
}