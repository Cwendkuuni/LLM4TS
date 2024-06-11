package org.apache.commons.cli.test;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.TypeHandler;
import org.apache.commons.cli.commons.PatternOptionBuilder;
import org.apache.commons.cli.commons.OptionType;
import org.apache.commons.cli.commons.ConvertUtils;
import org.apache.commons.cli.commons.ClassType;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Test class for TypeHandler.
 */
public class TypeHandlerTest
{
    @Test
    public void testCreateValueStringObject() throws ParseException
    {
        String str = "Test Value";
        Object obj = new Object();

        Object result = TypeHandler.createValue(str, obj.getClass());

        assertInstanceOf(Object.class, result);
        assertEquals(str, result.toString());
    }

    @Test
    public void testCreateValueStringString() throws ParseException
    {
        String str = "Test Value";

        Class<?> clazz = String.class;

        Object result = TypeHandler.createValue(str, clazz);

        assertInstanceOf(String.class, result);
        assertEquals(str, (String) result);
    }

    @Test
    public void testCreateValueStringClassWithStringConstructor() throws ParseException
    {
        String str = "Test Value";
        String className = "java.lang.String";

        Class<?> clazz = Class.forName(className);

        Object result = TypeHandler.createValue(str, clazz);

        assertInstanceOf(String.class, result);
        assertEquals(str, (String) result);
    }

    @Test
    public void testCreateValueStringClassWithNoArgConstructor() throws ParseException
    {
        String str = "Test Value";
        String className = "java.util.Date";

        Class<?> clazz = Class.forName(className);

        Object result = TypeHandler.createValue(str, clazz);

        assertInstanceOf(Date.class, result);

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        assertEquals("Test Value", df.format((Date) result));
    }

    @Test(expected = ParseException.class)
    public void testCreateValueStringInvalidClass() throws ParseException
    {
        String str = "Test Value";
        Class<?> clazz = int.class;

        TypeHandler.createValue(str, clazz);
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderStringValue() throws ParseException
    {
        String str = "Test Value";
        OptionType optionType = OptionType.STRING_VALUE;

        Class<?> clazz = PatternOptionBuilder.getStringClass(optionType);

        Object result = TypeHandler.createValue(str, clazz);

        assertInstanceOf(String.class, result);
        assertEquals(str, (String) result);
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderObjectValue() throws ParseException
    {
        String str = "Test Value";
        OptionType optionType = OptionType.OBJECT_VALUE;

        Class<?> clazz = PatternOptionBuilder.getObjectClass(optionType);

        Object result = TypeHandler.createValue(str, clazz);

        assertInstanceOf(String.class, result.getClass());
        assertEquals(str, result.toString());
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderNumberValue() throws ParseException
    {
        String str = "123";
        OptionType optionType = OptionType.NUMBER_VALUE;

        Class<?> clazz = PatternOptionBuilder.getNumberClass(optionType);

        Number result = (Number) TypeHandler.createValue(str, clazz);

        assertInstanceOf(Long.class, result.getClass());
        assertEquals(123L, result.longValue());
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderDateValue() throws ParseException
    {
        String str = "2022-10-01 10:10:10";
        OptionType optionType = OptionType.DATE_VALUE;

        Class<?> clazz = PatternOptionBuilder.getDateClass(optionType);

        Date result = (Date) TypeHandler.createValue(str, clazz);

        assertInstanceOf(Date.class, result);

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        assertEquals("2022-10-01 10:10:10", df.format(result));
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderClassValue() throws ParseException
    {
        String str = "com.example.TestClass";
        OptionType optionType = OptionType.CLASS_VALUE;

        Class<?> clazz = PatternOptionBuilder.getClassForClassValue(optionType);

        Object result = TypeHandler.createValue(str, clazz);

        assertInstanceOf(Class.class, result.getClass());
        assertNull(result); // Class.forName should return null as test class doesn't exist.
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderFileValue() throws ParseException, IOException
    {
        String str = "/path/to/testfile.txt";
        OptionType optionType = OptionType.FILE_VALUE;

        Class<?> clazz = File.class;

        Object result = TypeHandler.createValue(str, clazz);

        assertInstanceOf(File.class, result);
        assertEquals(new File(str), (File) result);
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderExistingFileValue() throws ParseException, IOException
    {
        String str = "/path/to/testfile.txt";
        OptionType optionType = OptionType.EXISTING_FILE_VALUE;

        Class<?> clazz = FileInputStream.class;

        Object result = TypeHandler.createValue(str, clazz);

        assertInstanceOf(FileInputStream.class, result);
        assertEquals(new FileInputStream(new File(str)), (FileInputStream) result);
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderFilesValue() throws ParseException, IOException
    {
        String str = "/path/to/testfile1.txt,/path/to/testfile2.txt";
        OptionType optionType = OptionType.FILES_VALUE;

        Class<?> clazz = File[].class;

        Object result = TypeHandler.createValue(str, clazz);

        assertInstanceOf(File[].class, result.getClass());
        assertEquals(new File[] {new File("/path/to/testfile1.txt"), new File("/path/to/testfile2.txt")}, (File[]) result);
    }

    @Test(expected = ParseException.class)
    public void testCreateValueStringPatternOptionBuilderURLValueInvalidURL() throws ParseException
    {
        String str = "invalid:url";
        OptionType optionType = OptionType.URL_VALUE;

        Class<?> clazz = URL.class;

        TypeHandler.createValue(str, clazz);
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderURLValue() throws ParseException
    {
        String str = "http://test.com";
        OptionType optionType = OptionType.URL_VALUE;

        Class<?> clazz = URL.class;

        Object result = TypeHandler.createValue(str, clazz);

        assertInstanceOf(URL.class, result);
        assertEquals(new URL(str), (URL) result);
    }
}