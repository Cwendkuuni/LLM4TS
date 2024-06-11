import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import java.net.MalformedURLException;
import java.net.URL;

import java.util.Date;
import java.text.ParseException;

public class TypeHandlerTest {
    @Test
    public void testCreateValueWithStringType() throws ParseException {
        String value = "test";
        Object obj = PatternOptionBuilder.STRING_VALUE;

        assertEquals(value, TypeHandler.createValue(value, obj));
    }

    @Test
    public void testCreateValueWithObjectType() throws ParseException {
        String value = "org.apache.commons.cli.Util";
        Object obj = PatternOptionBuilder.OBJECT_VALUE;

        assertEquals(Util.class, TypeHandler.createValue(value, obj));
    }

    @Test
    public void testCreateValueWithNumberType() throws ParseException {
        String value = "10";
        Object obj = PatternOptionBuilder.NUMBER_VALUE;

        assertEquals(10L, TypeHandler.createValue(value, obj));
    }

    @Test
    public void testCreateValueWithDateType() throws ParseException {
        String value = "2021-07-01";
        Object obj = PatternOptionBuilder.DATE_VALUE;

        assertNull(TypeHandler.createValue(value, obj));
    }

    @Test
    public void testCreateValueWithClassType() throws ParseException {
        String value = "java.lang.String";
        Object obj = PatternOptionBuilder.CLASS_VALUE;

        assertEquals(String.class, TypeHandler.createValue(value, obj));
    }

    @Test
    public void testCreateValueWithFileType() throws ParseException {
        String value = "test.txt";
        Object obj = PatternOptionBuilder.FILE_VALUE;

        assertEquals(new File(value), TypeHandler.createValue(value, obj));
    }

    @Test
    public void testCreateValueWithExistingFileType() throws ParseException {
        String value = "test.txt";
        Object obj = PatternOptionBuilder.EXISTING_FILE_VALUE;

        try {
            TypeHandler.createValue(value, obj);
        } catch (ParseException ex) {
            assertEquals("Unable to find file: " + value, ex.getMessage());
        }
    }

    @Test
    public void testCreateValueWithFilesType() throws ParseException {
        String value = "file1.txt, file2.txt";
        Object obj = PatternOptionBuilder.FILES_VALUE;

        try {
            TypeHandler.createValue(value, obj);
        } catch (UnsupportedOperationException ex) {
            assertEquals("Not yet implemented", ex.getMessage());
        }
    }

    @Test
    public void testCreateValueWithURLType() throws ParseException {
        String value = "http://www.example.com";
        Object obj = PatternOptionBuilder.URL_VALUE;

        try {
            TypeHandler.createValue(value, obj);
        } catch (MalformedURLException ex) {
            assertEquals("Unable to parse the URL: " + value, ex.getMessage());
        }
    }

    @Test
    public void testCreateObject() throws ParseException {
        String classname = "java.lang.String";

        assertEquals(String.class, TypeHandler.createObject(classname));
    }

    @Test
    public void testCreateNumberWithDecimal() throws ParseException {
        String str = "3.14";

        assertEquals(3.14, TypeHandler.createNumber(str));
    }

    @Test
    public void testCreateNumberWithoutDecimal() throws ParseException {
        String str = "10";

        assertEquals(10L, TypeHandler.createNumber(str));
    }

    @Test
    public void testCreateNumberWithInvalidFormat() {
        String str = "not_a_number";

        try {
            TypeHandler.createNumber(str);
        } catch (ParseException ex) {
            assertEquals(ex.getMessage(), "not_a_number");
        }
    }

    @Test
    public void testCreateClass() throws ParseException {
        String classname = "java.lang.String";

        assertEquals(String.class, TypeHandler.createClass(classname));
    }

    @Test
    public void testCreateDate() {
        String str = "2021-07-01";

        try {
            TypeHandler.createDate(str);
        } catch (UnsupportedOperationException ex) {
            assertEquals("Not yet implemented", ex.getMessage());
        }
    }

    @Test
    public void testCreateURLWithValidURL() throws ParseException {
        String str = "http://www.example.com";

        assertEquals(new URL(str), TypeHandler.createURL(str));
    }

    @Test
    public void testCreateURLWithInvalidURL() {
        String str = "not_a_url";

        try {
            TypeHandler.createURL(str);
        } catch (ParseException ex) {
            assertEquals("Unable to parse the URL: " + str, ex.getMessage());
        }
    }

    @Test
    public void testCreateFile() {
        String str = "test.txt";

        assertEquals(new File(str), TypeHandler.createFile(str));
    }

    @Test
    public void testOpenFileWithExistingFile() throws ParseException {
        String str = "test.txt";

        try {
            TypeHandler.openFile(str);
        } catch (FileNotFoundException ex) {
            assertEquals("Unable to find file: " + str, ex.getMessage());
        }
    }

    @Test
    public void testCreateFiles() {
        String str = "file1.txt, file2.txt";

        try {
            TypeHandler.createFiles(str);
        } catch (UnsupportedOperationException ex) {
            assertEquals("Not yet implemented", ex.getMessage());
        }
    }
}