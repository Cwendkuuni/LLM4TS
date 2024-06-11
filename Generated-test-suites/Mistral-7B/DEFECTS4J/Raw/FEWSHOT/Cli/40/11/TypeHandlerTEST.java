package org.apache.commons.cli.test;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.TypeHandler;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import java.util.Date;

/**
 * Test cases for {@link TypeHandler} class
 */
public class TypeHandlerTest {

    @Test
    public void testCreateValue_StringBoolean() {
        String str = "true";
        Boolean expected = true;
        Boolean actual = null;
        try {
            actual = (Boolean) TypeHandler.createValue(str, Boolean.class);
        } catch (ParseException e) {
            // Ignore
        }
        assertEquals(expected, actual);
    }

    @Test
    public void testCreateValue_StringClassWithEmptyConstructor() throws Exception {
        String className = "java.util.Date";
        Class<?> expectedClass = Date.class;
        Object actual = null;

        try {
            actual = TypeHandler.createValue(className, Class.class);
        } catch (ParseException e) {
            throw new AssertionError("Unexpected ParseException", e);
        }

        assertEquals(expectedClass, actual.getClass());
    }

    @Test
    public void testCreateValue_StringNumber() {
        String str = "123";
        Number expected = 123;
        Number actual = null;

        try {
            actual = (Number) TypeHandler.createValue(str, Number.class);
        } catch (ParseException e) {
            // Ignore
        }

        assertEquals(expected, actual);
    }

    @Test
    public void testCreateValue_StringNumberWithDecimalPoint() {
        String str = "123.45";
        Double expected = 123.45;
        Double actual = null;

        try {
            actual = (Double) TypeHandler.createValue(str, Double.class);
        } catch (ParseException e) {
            // Ignore
        }

        assertEquals(expected, actual, 0.0);
    }

    @Test(expected = ParseException.class)
    public void testCreateValue_InvalidStringNumber() {
        String str = "a1b2c3";
        TypeHandler.createValue(str, Number.class);
    }

    @Test
    public void testCreateValue_StringClassWithPublicNoArgConstructor_ThrowsParseException() {
        String className = "org.apache.commons.cli.test.TestClassWithPublicNoArgConstructor";

        try {
            TypeHandler.createValue(className, Class.class);
            fail("Expected ParseException");
        } catch (ParseException e) {
            // Expected
        }
    }

    @Test
    public void testCreateValue_StringClassWithPrivateNoArgConstructor() throws Exception {
        String className = "java.lang.String";
        Class<?> expectedClass = String.class;
        Object actual = null;

        try {
            Class<?> clazz = Class.forName(className);
            clazz.setAccessible(true);
            Constructor<?> ctor = clazz.getDeclaredConstructor();
            ctor.setAccessible(true);
            actual = TypeHandler.createValue(className, Class.class);
        } catch (Exception e) {
            throw new AssertionError("Unexpected exception", e);
        }

        assertEquals(expectedClass, actual.getClass());
    }

    @Test(expected = ParseException.class)
    public void testCreateValue_StringClassWithPrivateNoArgConstructor_ThrowsParseException() throws Exception {
        String className = "java.lang.String";

        try {
            TypeHandler.createValue(className, Class.class);
            fail("Expected ParseException");
        } catch (ParseException e) {
            // Expected
        }
    }

    @Test
    public void testCreateValue_StringClassWithPublicArgConstructor() throws Exception {
        String className = "org.apache.commons.cli.test.TestClassWithPublicArgConstructor";
        String arg = "test";
        Object expected = new TestClassWithPublicArgConstructor(arg);
        Object actual = null;

        try {
            actual = TypeHandler.createValue(className, Class.class);
            assertEquals(expected.getClass(), actual.getClass());
            TestClassWithPublicArgConstructor casted = (TestClassWithPublicArgConstructor) actual;
            assertEquals(arg, casted.arg);
        } catch (ParseException e) {
            throw new AssertionError("Unexpected ParseException", e);
        }
    }

    @Test(expected = ParseException.class)
    public void testCreateValue_StringClassWithPrivateArgConstructor() throws Exception {
        String className = "org.apache.commons.cli.test.TestClassWithPrivateArgConstructor";
        String arg = "test";

        try {
            TypeHandler.createValue(className, arg + ".class");
            fail("Expected ParseException");
        } catch (ParseException e) {
            // Expected
        }

        try {
            TestClassWithPrivateArgConstructor instance = new TestClassWithPrivateArgConstructor();
            Class<?> clazz = instance.getClass();
            Constructor<?> constructor = clazz.getDeclaredConstructor(String.class);
            constructor.setAccessible(true);
            Object actual = constructor.newInstance(arg);
            assertEquals(arg, actual);
        } catch (Exception e) {
            throw new AssertionError("Unexpected exception", e);
        }
    }

    @Test
    public void testCreateValue_StringURL() {
        String urlStr = "http://example.com";
        URL expected = new URL("http://example.com");
        URL actual = null;

        try {
            actual = (URL) TypeHandler.createValue(urlStr, URL.class);
        } catch (ParseException e) {
            // Ignore
        }

        assertEquals(expected, actual);
    }

    @Test(expected = ParseException.class)
    public void testCreateValue_StringInvalidURL() {
        String urlStr = "http://example.com/foo%61x";

        try {
            TypeHandler.createValue(urlStr, URL.class);
            fail("Expected ParseException");
        } catch (ParseException e) {
            // Expected
        }
    }

    @Test
    public void testCreateValue_StringFile() {
        String filePath = "/tmp/test.txt";
        File expected = new File(filePath);
        File actual = null;

        try {
            actual = (File) TypeHandler.createValue(filePath, File.class);
        } catch (ParseException e) {
            // Ignore
        }

        assertEquals(expected, actual);
    }

    @Test(expected = ParseException.class)
    public void testCreateValue_StringInvalidFile() {
        String filePath = "/tmp/test.txt/foo%61x";

        try {
            TypeHandler.createValue(filePath, File.class);
            fail("Expected ParseException");
        } catch (ParseException e) {
            // Expected
        }
    }

    @Test
    public void testCreateValue_StringFileInputStream() throws IOException {
        String filePath = "/tmp/test.txt";
        String expectedFileName = "test.txt";

        try (File file = new File(filePath);
             FileInputStream inputStream = new FileInputStream(file)) {
            Object actual = TypeHandler.createValue(filePath, FileInputStream.class);
            assertEquals(expectedFileName, ((FileInputStream) actual).getFile().getName());
        }
    }

    @Test(expected = ParseException.class)
    public void testCreateValue_StringInvalidFileInputStream() {
        String filePath = "/tmp/test.txt/foo%61x";

        try {
            TypeHandler.createValue(filePath, FileInputStream.class);
            fail("Expected ParseException");
        } catch (ParseException e) {
            // Expected
        }
    }
}

class TestClassWithPublicNoArgConstructor {
    // Empty constructor
}

class TestClassWithPublicArgConstructor {
    private final String arg;

    public TestClassWithPublicArgConstructor(String arg) {
        this.arg = arg;
    }
}

class TestClassWithPrivateArgConstructor {
    private final String arg;

    private TestClassWithPrivateArgConstructor() {}

    public TestClassWithPrivateArgConstructor(String arg) {
        this.arg = arg;
    }
}