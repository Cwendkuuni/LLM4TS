package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Date;

import org.apache.commons.cli.TypeHandler;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

/**
 * {@link TypeHandler} Test class.
 */
public class TypeHandlerTest {

    /**
     * Test createValue method for STRING_VALUE.
     */
    @Test
    public void testCreateValueStringValue() {
        String str = "test string";
        String expected = "test string";

        Object actual = TypeHandler.createValue(str, String.class);

        assertEquals(expected, actual);
    }

    /**
     * Test createValue method for OBJECT_VALUE.
     */
    @Test
    public void testCreateValueObjectValue() {
        String className = "java.lang.String";
        String str = "test string";

        Object expected = "test string";

        Class<?> clazz = ClassUtils.forName(className);
        Object actual = TypeHandler.createValue(str, clazz);

        assertEquals(expected.getClass(), actual.getClass());
        assertEquals(expected, actual);
    }

    /**
     * Test createValue method for NUMBER_VALUE.
     */
    @Test
    public void testCreateValueNumberValue() {
        String numberString = "123";
        int expectedInt = 123;
        long expectedLong = 123;
        double expectedDouble = 123.0;

        Object actualInt = TypeHandler.createValue(numberString, int.class);
        Object actualLong = TypeHandler.createValue(numberString, long.class);
        Object actualDouble = TypeHandler.createValue(numberString, double.class);

        assertEquals(expectedInt, actualInt);
        assertEquals(expectedLong, actualLong);
        assertEquals(expectedDouble, actualDouble);
    }

    /**
     * Test createValue method for DATE_VALUE.
     */
    @Test
    public void testCreateValueDateValue() {
        String dateString = "2023-02-25";
        Date expected = new Date(2023 - 1900, 1, 25);

        Object actual = TypeHandler.createValue(dateString, Date.class);

        assertEquals(expected, actual);
    }

    /**
     * Test createValue method for CLASS_VALUE.
     */
    @Test
    public void testCreateValueClassValue() {
        String className = "java.lang.String";
        String expectedClassName = "java.lang.String";

        Class<?> expectedClass = ClassUtils.forName(className);

        Object actual = TypeHandler.createValue(className, Class.class);

        assertEquals(expectedClassName, actual.getClass().getName());
        assertSame(expectedClass, (Class<?>) actual);
    }

    /**
     * Test createValue method for FILE_VALUE.
     */
    @Test
    public void testCreateValueFileValue() {
        String filePath = "testFile.txt";
        File expected = new File(filePath);

        Object actual = TypeHandler.createValue(filePath, File.class);

        assertEquals(expected, actual);
    }

    /**
     * Test createValue method for EXISTING_FILE_VALUE.
     */
    @Test
    public void testCreateValueExistingFileValue() throws IOException {
        String filePath = "testFile.txt";
        File expected = new File(filePath);

        Object actual = TypeHandler.createValue(filePath, File.class);

        assertEquals(expected, actual);
    }

    /**
     * Test createValue method for FILES_VALUE.
     */
    @Test
    public void testCreateValueFilesValue() {
        String filePaths = "src/test/resources/*.txt";
        File[] expected = new File[] { new File("src/test/resources/TestHandlerTest.java.txt"),
                new File("src/test/resources/TypeHandler.java.txt") };

        Object actual = TypeHandler.createValue(filePaths, File[].class);

        assertEquals(expected.length, ((File[]) actual).length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i].getAbsolutePath(),
                    ((File) ((File[]) actual)[i]).getAbsolutePath());
        }
    }

    /**
     * Test createValue method for URL_VALUE.
     */
    @Test
    public void testCreateValueUrlValue() {
        String urlString = "http://www.example.com";
        URL expected = new URL("http", "www.example.com", -1, "/", null);

        Object actual = TypeHandler.createValue(urlString, URL.class);

        assertEquals(expected, actual);
    }

    /**
     * Test createObject method.
     */
    @Test
    public void testCreateObject() {
        String className = "java.lang.String";

        Object expected = "test string";

        Object actual = TypeHandler.createObject(className);

        assertEquals(expected.getClass(), actual.getClass());
        assertEquals(expected, actual);
    }

    /**
     * Test createNumber method.
     */
    @Test
    public void testCreateNumber() {
        String numberString = "123";

        Number expectedInt = 123;
        Number expectedLong = 123L;
        Number expectedDouble = 123.0;

        Number actualInt = TypeHandler.createNumber(numberString);
        Number actualLong = TypeHandler.createNumber(numberString + L);
        Number actualDouble = TypeHandler.createNumber(numberString + "." + "0");

        assertEquals(expectedInt.getClass(), actualInt.getClass());
        assertEquals(expectedInt, actualInt);

        assertEquals(expectedLong.getClass(), actualLong.getClass());
        assertEquals(expectedLong, actualLong);

        assertEquals(expectedDouble.getClass(), actualDouble.getClass());
        assertEquals(expectedDouble, actualDouble);
    }

    /**
     * Test createDate method.
     */
    @Test
    public void testCreateDate() {
        String dateString = "2023-02-25";

        Date expected = new Date(2023 - 1900, 1, 25);

        Object actual = TypeHandler.createDate(dateString);

        assertEquals(expected, actual);
    }

    /**
     * Test createFile method.
     */
    @Test
    public void testCreateFile() {
        String filePath = "testFile.txt";
        File expected = new File(filePath);

        Object actual = TypeHandler.createFile(filePath);

        assertEquals(expected, actual);
    }

    /**
     * Test createFiles method.
     */
    @Test
    public void testCreateFiles() throws IOException {
        String filePaths = "src/test/resources/*.txt";
        File[] expected = new File[] { new File("src/test/resources/TestHandlerTest.java.txt"),
                new File("src/test/resources/TypeHandler.java.txt") };

        Object actual = TypeHandler.createFiles(filePaths);

        assertEquals(expected.length, ((File[]) actual).length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i].getAbsolutePath(),
                    ((File) ((File[]) actual)[i]).getAbsolutePath());
        }
    }

    /**
     * Test createURL method.
     */
    @Test
    public void testCreateURL() {
        String urlString = "http://www.example.com";

        Object actual = TypeHandler.createURL(urlString);

        assertTrue(actual instanceof URL);

        URL url = (URL) actual;
        assertEquals("http", url.getProtocol());
        assertEquals("www.example.com", url.getHost());
        assertEquals(-1, url.getPort());
        assertEquals("/", url.getPath());
        assertNull(url.getRef());
        assertNull(url.getQuery());
    }

    /**
     * Test createValue method with invalid argument types.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateValueInvalidArgumentTypes() {
        String str = "test";
        int obj = 1;

        Object actual = TypeHandler.createValue(str, obj.getClass());
    }
}