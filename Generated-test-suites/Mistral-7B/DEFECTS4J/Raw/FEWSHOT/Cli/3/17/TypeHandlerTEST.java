package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.ClassNotFoundException;
import java.lang.IllegalAccessException;
import java.lang.InstantiationException;
import java.lang.reflect.Constructor;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.jar.JarFile;

import org.junit.Test;

public class TypeHandlerTest {

    @Test
    public void testCreateValue_StringObject() {
        Object strObj = "test";
        Object stringValue = TypeHandler.createValue((String) strObj, String.class);
        assertEquals(strObj, stringValue);

        Object integerValue = TypeHandler.createValue((String) strObj, int.class);
        assertNull(integerValue);

        Object doubleValue = TypeHandler.createValue((String) strObj, double.class);
        assertNull(doubleValue);

        Object dateValue = TypeHandler.createValue((String) strObj, Date.class);
        assertNull(dateValue);

        Object fileValue = TypeHandler.createValue((String) strObj, File.class);
        assertNull(fileValue);

        Object urlValue = TypeHandler.createValue((String) strObj, URL.class);
        assertNull(urlValue);
    }

    @Test
    public void testCreateValue_StringClass() {
        String str = "test";

        Object stringValue = TypeHandler.createValue(str, String.class);
        assertEquals(str, stringValue);

        Object integerValue = TypeHandler.createValue(str, int.class);
        try {
            int intValue = (int) integerValue;
            assertEquals(Integer.parseInt(str), intValue);
        } catch (NumberFormatException e) {
            fail("Expected integer value but got: " + integerValue);
        }

        Object doubleValue = TypeHandler.createValue(str, double.class);
        try {
            double doubleValue = (double) doubleValue;
            assertEquals(Double.parseDouble(str), doubleValue, 0.0);
        } catch (NumberFormatException e) {
            fail("Expected double value but got: " + doubleValue);
        }

        Object dateValue = TypeHandler.createValue(str, Date.class);
        try {
            Date dateValue = (Date) dateValue;
            assertEquals(new Date(Long.parseLong(str) * 1000), dateValue);
        } catch (NumberFormatException | IllegalArgumentException e) {
            fail("Expected date value but got: " + dateValue);
        }

        Object fileValue = TypeHandler.createValue(str, File.class);
        assertNull(fileValue);

        Object urlValue = TypeHandler.createValue(str, URL.class);
        try {
            URL urlValue = (URL) urlValue;
            assertEquals(new URL("file:" + str), urlValue);
        } catch (MalformedURLException e) {
            fail("Expected URL value but got: " + urlValue);
        }

        Object clazzValue = TypeHandler.createValue(str, String.class);
        try {
            Class<?> clazzValue = (Class<?>) clazzValue;
            assertEquals(Class.forName(str), clazzValue);
        } catch (ClassNotFoundException e) {
            fail("Expected class value but got: " + clazzValue);
        }
    }

    @Test
    public void testCreateObject() {
        String className = "java.lang.String";
        Object stringObject = TypeHandler.createObject(className);
        assertTrue(stringObject instanceof String);

        className = "java.util.Date";
        Object dateObject = TypeHandler.createObject(className);
        assertTrue(dateObject instanceof Date);

        className = "org.apache.commons.cli.TypeHandlerTest$TestClass";
        Object testClassObject = TypeHandler.createObject(className);
        assertTrue(testClassObject instanceof TestClass);

        String invalidClassName = "java.lang.StringTest";
        Object invalidObject = TypeHandler.createObject(invalidClassName);
        assertNull(invalidObject);
    }

    @Test
    public void testCreateNumber() {
        String longValue = "123";
        Number longNumber = TypeHandler.createNumber(longValue);
        assertEquals(Long.parseLong(longValue), longNumber.longValue());

        String doubleValue = "123.45";
        Number doubleNumber = TypeHandler.createNumber(doubleValue);
        assertEquals(Double.parseDouble(doubleValue), doubleNumber.doubleValue(), 0.0);

        String invalidValue = "foo";
        Number invalidNumber = TypeHandler.createNumber(invalidValue);
        assertNull(invalidNumber);
    }

    @Test
    public void testCreateClass() {
        String className = "java.lang.String";
        Class<?> stringClass = TypeHandler.createClass(className);
        assertEquals(String.class, stringClass);

        className = "java.util.Date";
        Class<?> dateClass = TypeHandler.createClass(className);
        assertEquals(Date.class, dateClass);

        className = "org.apache.commons.cli.TypeHandlerTest$TestClass";
        Class<?> testClass = TypeHandler.createClass(className);
        assertEquals(TestClass.class, testClass);

        String invalidClassName = "java.lang.StringTest";
        Class<?> invalidClass = TypeHandler.createClass(invalidClassName);
        assertNull(invalidClass);
    }

    @Test
    public void testCreateDate() {
        String dateString = "2023-03-01";
        Date date = TypeHandler.createDate(dateString);
        assertEquals(new Date(2023, 2, 1), date);

        String invalidDateString = "foo";
        Date invalidDate = TypeHandler.createDate(invalidDateString);
        assertNull(invalidDate);
    }

    @Test
    public void testCreateURL() {
        String urlString = "http://example.com";
        URL url = TypeHandler.createURL(urlString);
        assertEquals(new URL(urlString), url);

        String invalidUrlString = "foo://example.com";
        URL invalidUrl = TypeHandler.createURL(invalidUrlString);
        assertNull(invalidUrl);
    }

    @Test
    public void testCreateFile() {
        String filePath = "test.txt";
        File file = TypeHandler.createFile(filePath);
        assertTrue(new File(filePath).exists());
        assertEquals(filePath, file.getAbsolutePath());

        String invalidFilePath = "foo:/test.txt";
        File invalidFile = TypeHandler.createFile(invalidFilePath);
        assertNull(invalidFile);
    }

    @Test
    public void testCreateFiles() {
        String filePath = "test_folder/test1.txt,test_folder/test2.txt";
        File[] files = TypeHandler.createFiles(filePath);
        File testFile1 = new File("test_folder/test1.txt");
        File testFile2 = new File("test_folder/test2.txt");
        assertArrayEquals(new File[] { testFile1, testFile2 }, files);

        String invalidFilePath = "foo:/test_folder/test1.txt,test_folder/test2.txt";
        File[] invalidFiles = TypeHandler.createFiles(invalidFilePath);
        assertNull(invalidFiles);
    }

    private static class TestClass {
        private TestClass() {}
    }

    @Test
    public void testCreateClassFromJar() throws IOException {
        String jarFilePath = getClass().getResource("/test-class.jar").getFile();
        String className = "TestClassInJar";
        Class<?> clazz = TypeHandler.createClass(className);
        assertNotNull(clazz);
        Constructor<?> constructor = clazz.getDeclaredConstructor();
        constructor.setAccessible(true);
        Object instance = constructor.newInstance();
        assertEquals(className, instance.getClass().getName());

        JarFile jarFile = new JarFile(jarFilePath);
        Enumeration<JarEntry> entries = jarFile.entries();
        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            if (entry.getName().endsWith(className.class.getName().replaceAll("\\.", "/") + ".class")) {
                jarFile.close();
                return;
            }
        }
        fail("Class file not found in the JAR.");
    }

    private static File getTestDataDirectory() {
        File testDataDirectory = new File(System.getProperty("user.dir"), "target/test-classes/test-data");
        if (!testDataDirectory.exists()) {
            testDataDirectory.mkdirs();
        }
        return testDataDirectory;
    }

    @Test
    public void testCreateFileWithTestDataDirectory() throws IOException {
        String filePath = getTestDataDirectory().getAbsolutePath() + File.separator + "test.txt";
        File file = TypeHandler.createFile(filePath);
        assertTrue(file.exists());
        assertTrue(file.delete());
    }

    @Test
    public void testCreateFilesWithTestDataDirectory() throws IOException {
        String filePath1 = getTestDataDirectory().getAbsolutePath() + File.separator + "test1.txt";
        String filePath2 = getTestDataDirectory().getAbsolutePath() + File.separator + "test2.txt";
        File[] files = TypeHandler.createFiles(filePath1 + File.pathSeparator + filePath2);
        File testFile1 = new File(filePath1);
        File testFile2 = new File(filePath2);
        assertArrayEquals(new File[] { testFile1, testFile2 }, files);

        assertTrue(testFile1.delete());
        assertTrue(testFile2.delete());
    }

    @Test
    public void testCreateURLWithBaseURL() {
        String baseURL = "http://example.com/api/";
        String urlString = "users/123";
        URL url = TypeHandler.createURL(baseURL, urlString);
        assertEquals(new URL(baseURL + urlString), url);
    }
}