package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.lang3.math.NumberUtils;
import org.junit.Test;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.Date;

public class TypeHandlerTest {

    @Test
    public void testCreateValue() {
        assertEquals("string", TypeHandler.createValue("string", PatternOptionBuilder.STRING_VALUE));
        assertEquals("True", TypeHandler.createValue("True", PatternOptionBuilder.BOOLEAN_VALUE));
        assertEquals("1", TypeHandler.createValue("1", PatternOptionBuilder.INTEGER_VALUE));
        assertNull(TypeHandler.createValue(null, PatternOptionBuilder.STRING_VALUE));
    }

    @Test
    public void testCreateObject() {
        assertEquals(null, TypeHandler.createObject("nonexistent.class"));
        assertEquals(this, TypeHandler.createObject(this.getClass().getName()));
        assertNull(TypeHandler.createObject(null));
    }

    @Test
    public void testCreateNumber() {
        assertEquals(NumberUtils.createNumber("3.14"), TypeHandler.createNumber("3.14"));
        assertEquals(null, TypeHandler.createNumber("not a number"));
    }

    @Test
    public void testCreateClass() {
        try {
            assertEquals(Class.forName("java.lang.String"), TypeHandler.createClass("java.lang.String"));
            assertEquals(null, TypeHandler.createClass("nonexistent.class"));
        } catch (ClassNotFoundException e) {
            // Do nothing
        }
    }

    @Test
    public void testCreateDate() {
        // Test valid date string
        try {
            Date date = TypeHandler.createDate("2021-01-01");
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String expectedDate = dateFormat.format(date);
            assertEquals("2021-01-01", expectedDate);
        } catch (ParseException e) {
            // Do nothing
        }
        // Test invalid date string
        assertNull(TypeHandler.createDate("not a date"));
    }

    @Test
    public void testCreateURL() {
        // Test valid URL string
        try {
            URL url = TypeHandler.createURL("http://www.example.com");
            assertEquals("http://www.example.com", url.toString());
        } catch (MalformedURLException e) {
            // Do nothing
        }
        // Test invalid URL string
        assertNull(TypeHandler.createURL("not a URL"));
    }

    @Test
    public void testCreateFile() {
        assertEquals(new File("path/to/file"), TypeHandler.createFile("path/to/file"));
    }

    @Test
    public void testCreateFiles() {
        assertNull(TypeHandler.createFiles("file1,file2,file3"));
    }
}