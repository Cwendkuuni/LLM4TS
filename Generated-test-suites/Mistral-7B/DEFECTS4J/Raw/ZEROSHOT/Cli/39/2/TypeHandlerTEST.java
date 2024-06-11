package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.Date;
import java.time.format.DateTimeFormatter;

import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.TypeHandler;
import org.junit.Test;

/**
 * Test class for the TypeHandler class.
 */
public class TypeHandlerTest
{
    /**
     * Test method for {@link TypeHandler#createValue(String, Object)}.
     */
    @Test
    public void testCreateValueStringObject()
    {
        // Test String type
        Object value = TypeHandler.createValue("test", String.class);
        assertEquals("test", value);

        // Test null object
        assertNull(TypeHandler.createValue(null, Object.class));

        // Test other types
        assertNull(TypeHandler.createValue("123", Integer.class));
        assertNull(TypeHandler.createValue("123.45", Double.class));
        assertNull(TypeHandler.createValue("2022-03-01", Date.class));
        assertNull(TypeHandler.createValue("com.test.TestClass", Class.class));
        assertNull(TypeHandler.createValue("file:/path/to/file.txt", File.class));
        assertNull(TypeHandler.createValue("http://example.com", URL.class));
    }

    /**
     * Test method for {@link TypeHandler#createObject(String)}.
     */
    @Test
    public void testCreateObject()
    {
        try
        {
            Object value = TypeHandler.createObject("java.lang.String");
            assertEquals(String.class, value.getClass());
        }
        catch (final ParseException e)
        {
            fail("Unexpected exception: " + e.getMessage());
        }

        try
        {
            Object value = TypeHandler.createObject("org.apache.commons.cli.TypeHandlerTest");
            assertEquals(TypeHandlerTest.class, value.getClass());
        }
        catch (final ParseException e)
        {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test method for {@link TypeHandler#createNumber(String)}.
     */
    @Test
    public void testCreateNumber()
    {
        Object valueLong = TypeHandler.createValue("123", Long.TYPE);
        assertEquals(123L, valueLong);

        Object valueDouble = TypeHandler.createValue("123.45", Double.TYPE);
        assertEquals(123.45, valueDouble);

        assertThrows(ParseException.class, () -> TypeHandler.createValue("abcd", Double.TYPE));
    }

    /**
     * Test method for {@link TypeHandler#createDate(String)}.
     */
    @Test
    public void testCreateDate()
    {
        String dateString = "2022-03-01";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        Date date = Date.from(java.time.LocalDate.parse(dateString, formatter).atStartOfDay().atZone(java.util.ZoneId.systemDefault()).toInstant());

        Object valueDate = TypeHandler.createValue(dateString, Date.class);
        assertEquals(date, valueDate);

        assertThrows(UnsupportedOperationException.class, TypeHandler::createDate);
    }

    /**
     * Test method for {@link TypeHandler#createURL(String)}.
     */
    @Test
    public void testCreateURL()
    {
        Object valueURL = TypeHandler.createValue("http://example.com", URL.class);
        assertEquals("http://example.com", valueURL.toString());

        assertThrows(ParseException.class, () -> TypeHandler.createValue("invalid url", URL.class));
    }

    /**
     * Test method for {@link TypeHandler#createFile(String)}.
     */
    @Test
    public void testCreateFile()
    {
        String filePath = "path/to/file.txt";
        File file = new File(filePath);

        Object valueFile = TypeHandler.createValue(filePath, File.class);
        assertEquals(file, valueFile);

        assertThrows(FileNotFoundException.class, () -> new FileInputStream(TypeHandler.createValue("non-existing-file.txt", File.class)));
    }
}