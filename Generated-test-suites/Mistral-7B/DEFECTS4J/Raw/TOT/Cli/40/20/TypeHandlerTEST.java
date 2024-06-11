package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.TypeHandler;
import org.junit.Test;

/**
 * Test for TypeHandler class.
 */
public class TypeHandlerTest
{
    @Test
    public void testCreateValue_String_String()
    {
        String str = "test";
        Object obj = new Object();

        Object result = TypeHandler.createValue(str, obj.getClass());
        assertSame(obj, result);

        String stringResult = (String) result;
        assertEquals(str, stringResult);
    }

    @Test(expected = ParseException.class)
    public void testCreateValue_String_String_InvalidType()
    {
        String str = "test";
        Class<?> invalidType = int.class;

        TypeHandler.createValue(str, invalidType);
    }

    @Test
    public void testCreateValue_String_Class_StringType()
    {
        String str = "test";
        Class<String> stringType = String.class;

        String result = (String) TypeHandler.createValue(str, stringType);
        assertEquals(str, result);
    }

    @Test(expected = ParseException.class)
    public void testCreateValue_String_Class_InvalidType()
    {
        String str = "test";
        Class<Integer> invalidType = int.class;

        TypeHandler.createValue(str, invalidType);
    }

    @Test
    public void testCreateValue_String_Class_ObjectType()
    {
        String str = "test.TestClass";
        Class<TestClass> objectType = TestClass.class;

        TestClass result = (TestClass) TypeHandler.createValue(str, objectType);
        assertNotNull(result);

        TestClass expectedResult = new TestClass();
        assertEquals(expectedResult, result);
    }

    @Test(expected = ParseException.class)
    public void testCreateValue_String_Class_InvalidObjectType()
    {
        String str = "test.TestClass";
        Class<Integer> invalidType = int.class;

        TypeHandler.createValue(str, invalidType);
    }

    @Test
    public void testCreateValue_String_Class_NumberType()
    {
        String str = "123";
        Class<Long> numberType = long.class;

        Long result = (Long) TypeHandler.createValue(str, numberType);
        assertEquals(123L, result.longValue());

        String numberStringResult = result.toString();
        assertEquals("123", numberStringResult);
    }

    @Test(expected = ParseException.class)
    public void testCreateValue_String_Class_InvalidNumberType()
    {
        String str = "123.45";
        Class<Integer> invalidType = int.class;

        TypeHandler.createValue(str, invalidType);
    }

    @Test
    public void testCreateValue_String_Class_DateType()
    {
        String str = "2022-01-01";
        Class<Date> dateType = Date.class;

        Date result = (Date) TypeHandler.createValue(str, dateType);
        assertNotNull(result);

        Date expectedResult = new Date(System.currentTimeMillis());
        assertEquals(expectedResult.getTime(), result.getTime());

        String dateStringResult = new SimpleDateFormat("yyyy-MM-dd").format(result);
        assertEquals("2022-01-01", dateStringResult);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateValue_String_Class_DateType_NotImplemented()
    {
        String str = "2022-01-01T00:00:00.000Z";
        Class<Date> dateType = Date.class;

        TypeHandler.createValue(str, dateType);
    }

    @Test
    public void testCreateValue_String_Class_URLType()
    {
        String str = "https://www.example.com";
        Class<URL> urlType = URL.class;

        URL result = (URL) TypeHandler.createValue(str, urlType);
        assertNotNull(result);

        URL expectedResult = new URL("https://www.example.com");
        assertEquals(expectedResult.toString(), result.toString());
    }

    @Test(expected = ParseException.class)
    public void testCreateValue_String_Class_URLType_InvalidURL()
    {
        String str = "http://example.com/test/";
        Class<URL> urlType = URL.class;

        TypeHandler.createValue(str, urlType);
    }

    @Test
    public void testCreateValue_String_Class_FileType()
    {
        String str = "test.txt";
        Class<File> fileType = File.class;

        File result = (File) TypeHandler.createValue(str, fileType);
        assertNotNull(result);

        File expectedResult = new File(str);
        assertEquals(expectedResult.getAbsolutePath(), result.getAbsolutePath());
    }

    @Test(expected = ParseException.class)
    public void testCreateValue_String_Class_FileType_InvalidFile()
    {
        String str = "test.docx.txt";
        Class<File> fileType = File.class;

        TypeHandler.createValue(str, fileType);
    }

    @Test
    public void testCreateValue_String_Class_FileInputStreamType()
    {
        String str = "test.txt";
        Class<FileInputStream> fileInputStreamType = FileInputStream.class;

        File file = new File(str);
        FileInputStream result = (FileInputStream) TypeHandler.createValue(str, fileInputStreamType);
        assertNotNull(result);

        FileInputStream expectedResult = new FileInputStream(file);
        assertEquals(expectedResult, result);
    }

    @Test(expected = ParseException.class)
    public void testCreateValue_String_Class_FileInputStreamType_InvalidFile()
    {
        String str = "test.docx.txt";
        Class<FileInputStream> fileInputStreamType = FileInputStream.class;

        TypeHandler.createValue(str, fileInputStreamType);
    }

    @Test
    public void testCreateValue_String_Class_FileArrayType()
    {
        String str = "test1.txt, test2.txt, test3.txt";
        Class<File[]> fileArrayType = File[].class;

        File[] result = (File[]) TypeHandler.createValue(str, fileArrayType);
        assertNotNull(result);

        File[] expectedResult = new File[] { new File("test1.txt"), new File("test2.txt"), new File("test3.txt") };
        assertArrayEquals(expectedResult, result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateValue_String_Class_FileArrayType_NotImplemented()
    {
        String str = "test1.txt, test2.txt, test3.txt, test4.txt";
        Class<File[]> fileArrayType = File[].class;

        TypeHandler.createValue(str, fileArrayType);
    }
}

class TestClass
{
    // Empty class for testing
}