import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;
import java.io.FileInputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class TypeHandlerTest {

    private static final String STR = "test string";
    private static final String CLASSNAME = "TestClass";
    private static final String NUM_STR = "123";
    private static final String DOUBLE_STR = "123.45";
    private static final String DATE_STR = "2021-08-01";
    private static final String URL_STR = "https://example.com";
    private static final String FILE_STR = "test.txt";
    private static final String NON_EXISTING_FILE_STR = "non_existing.txt";
    private static final String DIRECTORY_STR = "directory";
    private static final String NON_EXISTING_DIRECTORY_STR = "non_existing_directory";

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateValueWithStringValue() {
        Object value = TypeHandler.createValue(STR, String.class);
        assertEquals(STR, value);
    }

    @Test
    public void testCreateValueWithObjectValue() throws ParseException {
        Object value = TypeHandler.createValue(STR, Object.class);
        assertNotNull(value);
        assertEquals(STR, value.toString());
    }

    @Test
    public void testCreateValueWithNumberValueLong() throws ParseException {
        Number value = TypeHandler.createValue(NUM_STR, Long.class);
        assertNotNull(value);
        assertTrue(value instanceof Long);
        assertEquals(Long.valueOf(NUM_STR), value);
    }

    @Test
    public void testCreateValueWithNumberValueDouble() throws ParseException {
        Number value = TypeHandler.createValue(DOUBLE_STR, Double.class);
        assertNotNull(value);
        assertTrue(value instanceof Double);
        assertEquals(Double.valueOf(DOUBLE_STR), value);
    }

    @Test
    public void testCreateValueWithDateValue() throws ParseException {
        Date value = TypeHandler.createValue(DATE_STR, Date.class);
        assertNull(value); // Not Implemented yet
    }

    @Test
    public void testCreateValueWithClassValue() throws ParseException {
        Class<?> value = TypeHandler.createValue(CLASSNAME, Class.class);
        assertNotNull(value);
        assertEquals(CLASSNAME, value.getSimpleName());
    }

    @Test
    public void testCreateValueWithFileValue() throws ParseException {
        File value = TypeHandler.createValue(FILE_STR, File.class);
        assertNotNull(value);
        assertEquals(FILE_STR, value.getName());
    }

    @Test(expected = ParseException.class)
    public void testCreateValueWithNonExistingFileValue() throws ParseException {
        TypeHandler.createValue(NON_EXISTING_FILE_STR, File.class);
    }

    @Test
    public void testCreateValueWithFilesValue() throws ParseException {
        File[] value = TypeHandler.createValue(DIRECTORY_STR, File[].class);
        assertNull(value); // Not Implemented yet
    }

    @Test
    public void testCreateValueWithURLValue() throws ParseException, MalformedURLException {
        URL value = TypeHandler.createValue(URL_STR, URL.class);
        assertNotNull(value);
        assertEquals(URL_STR, value.toString());
    }

    @Test(expected = ParseException.class)
    public void testCreateValueWithMalformedURLValue() throws ParseException {
        TypeHandler.createValue(STR, URL.class);
    }

    @Test
    public void testCreateObject() throws ParseException {
        Object object = TypeHandler.createObject(CLASSNAME);
        assertNotNull(object);
        assertEquals(CLASSNAME, object.getClass().getSimpleName());
    }

    @Test(expected = ParseException.class)
    public void testCreateObjectWithNonExistingClass() throws ParseException {
        TypeHandler.createObject("NonExistingClass");
    }

    @Test
    public void testCreateNumberWithLongValue() throws ParseException {
        Number number = TypeHandler.createNumber(NUM_STR);
        assertNotNull(number);
        assertTrue(number instanceof Long);
        assertEquals(Long.valueOf(NUM_STR), number);
    }

    @Test
    public void testCreateNumberWithDoubleValue() throws ParseException {
        Number number = TypeHandler.createNumber(DOUBLE_STR);
        assertNotNull(number);
        assertTrue(number instanceof Double);
        assertEquals(Double.valueOf(DOUBLE_STR), number);
    }

    @Test(expected = ParseException.class)
    public void testCreateNumberWithInvalidValue() throws ParseException {
        TypeHandler.createNumber(STR);
    }

    @Test
    public void testCreateClass() throws ParseException {
        Class<?> clazz = TypeHandler.createClass(CLASSNAME);
        assertNotNull(clazz);
        assertEquals(CLASSNAME, clazz.getSimpleName());
    }

    @Test(expected = ParseException.class)
    public void testCreateClassWithNonExistingClass() throws ParseException {
        TypeHandler.createClass("NonExistingClass");
    }

    @Test(expected = ParseException.class)
    public void testCreateDate() {
        TypeHandler.createDate(DATE_STR);
    }

    @Test
    public void testCreateURL() throws ParseException, MalformedURLException {
        URL url = TypeHandler.createURL(URL_STR);
        assertNotNull(url);
        assertEquals(URL_STR, url.toString());
    }

    @Test(expected = ParseException.class)
    public void testCreateURLWithMalformedURL() throws ParseException {
        TypeHandler.createURL(STR);
    }

    @Test
    public void testCreateFile() {
        File file = TypeHandler.createFile(FILE_STR);
        assertNotNull(file);
        assertEquals(FILE_STR, file.getName());
    }

    @Test
    public void testOpenFile() throws ParseException {
        FileInputStream fileInputStream = TypeHandler.openFile(FILE_STR);
        assertNotNull(fileInputStream);
        assertEquals(FILE_STR, fileInputStream.toString());
    }

    @Test(expected = ParseException.class)
    public void testOpenNonExistingFile() throws ParseException {
        TypeHandler.openFile(NON_EXISTING_FILE_STR);
    }

    @Test
    public void testCreateFiles() {
        File[] files = TypeHandler.createFiles(DIRECTORY_STR);
        assertNull(files); // Not Implemented yet
    }

    @Test
    public void testCompleteJunitFile() {
        //TO DO: complete this test case
    }
}