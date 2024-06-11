package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import java.lang.ClassNotFoundException;
import java.lang.ClassSyntaxError;
import java.lang.IllegalAccessException;
import java.lang.InstantiationException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.UndeclaredThrowableException;

import java.text.ParseException;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import org.mockito.Mockito;

@RunWith(Parameterized.class)
public class TypeHandlerTest {

    private String input;
    private Class<?> expectedType;
    private Object expectedOutput;

    public TypeHandlerTest(String input, Class<?> expectedType, Object expectedOutput) {
        this.input = input;
        this.expectedType = expectedType;
        this.expectedOutput = expectedOutput;
    }

    @Parameterized.Parameters(name = "{index}: TypeHandler.createValue(\"{0}\", {1})")
    public static Object[] testData() {
        return new Object[][]{
                {"", String.class, ""},
                {"foo", String.class, "foo"},
                {"[Ljava.lang.String;", Object[].class, new String[]{"foo"}},
                {"123", Long.TYPE, 123L},
                {"123.5", Double.TYPE, 123.5},
                {"123", Integer.TYPE, 123},
                {"true", Boolean.TYPE, true},
                {"com.example.MyClass", MyClass.class, new MyClass()},
                {"file.txt", File.class, new File("file.txt")},
                {"file:/path/to/file.txt", File.class, new File("file:/path/to/file.txt")},
                {"http://example.com", URL.class, new URL("http://example.com")},
                {"123.5,45.6", Double[].class, new Double[]{123.5, 45.6}}};
    }

    @Test
    public void testCreateValue() throws Exception {
        Object output;

        if (expectedOutput == null) {
            output = null;
        } else if (expectedOutput instanceof File || expectedOutput instanceof FileInputStream) {
            File file = Mockito.mock(File.class);
            Mockito.when(file.exists()).thenReturn(true);
            if (expectedOutput instanceof File) {
                output = file;
            } else {
                FileInputStream inputStream = Mockito.mock(FileInputStream.class);
                Mockito.when(file.length()).thenReturn(1L);
                Mockito.when(inputStream.available()).thenReturn((int) file.length());
                Mockito.when(inputStream.readAllBytes()).thenReturn(new byte[0]);
                output = inputStream;
            }
        } else {
            output = TypeHandler.createValue(input, expectedType);
        }

        if (expectedOutput != null) {
            assertEquals(expectedOutput.getClass(), output.getClass());
            if (expectedOutput instanceof Date) {
                Date date = (Date) expectedOutput;
                Date outputDate = (Date) output;
                assertEquals(date.getTime(), outputDate.getTime());
            } else {
                assertEquals(expectedOutput, output);
            }
        } else {
            assertNull(output);
        }
    }

    @Test
    public void testCreateObject() throws Exception {
        Class<?> clazz = Class.forName("com.example.MyClass");

        Object obj = TypeHandler.createObject(clazz.getName());
        assertInstanceOf(clazz, obj);

        Class<MyClass> myClass = MyClass.class;
        MyClass myClassInstance = new MyClass();
        MyClass object = (MyClass) TypeHandler.createObject(myClass.getName());
        assertEquals(myClassInstance, object);
    }

    @Test
    public void testCreateNumber() throws Exception {
        assertEquals(123, TypeHandler.createNumber("123"));
        assertEquals(123.5, TypeHandler.createNumber("123.5"));

        assertThrows(NumberFormatException.class, () -> TypeHandler.createNumber("foo"));
    }

    @Test
    public void testCreateClass() throws Exception {
        assertThrows(ClassNotFoundException.class, () -> TypeHandler.createClass("com.example.NonExistentClass"));

        Class<MyClass> myClass = MyClass.class;
        assertEquals(myClass, TypeHandler.createClass(myClass.getName()));
    }

    @Test
    public void testCreateDate() {
        assertThrows(UnsupportedOperationException.class, () -> TypeHandler.createDate("2022-01-01"));
    }

    @Test
    public void testCreateURL() throws Exception {
        assertThrows(MalformedURLException.class, () -> TypeHandler.createURL(""));
        assertThrows(MalformedURLException.class, () -> TypeHandler.createURL("foo:"));

        URL url = new URL("http://example.com");
        assertEquals(url, TypeHandler.createURL("http://example.com"));
    }

    @Test
    public void testCreateFile() throws Exception {
        assertEquals(new File("file.txt"), TypeHandler.createFile("file.txt"));
    }

    @Test
    public void testOpenFile() throws Exception {
        File file = new File("file.txt");
        FileInputStream expected = new FileInputStream(file);

        FileInputStream result = TypeHandler.openFile("file.txt");
        assertInstanceOf(FileInputStream.class, result);
        assertEquals(expected.available(), result.available());
        assertEquals(expected.read(), result.read());
        assertEquals(expected.close(), result.close());
    }

    @Test
    public void testCreateFiles() {
        assertThrows(UnsupportedOperationException.class, () -> TypeHandler.createFiles(""));
    }

    @Test
    public void testCreateValueWithClassNotFound() throws Exception {
        assertThrows(ClassNotFoundException.class, () -> TypeHandler.createValue("com.example.NonExistentClass", Object.class));
    }

    @Test
    public void testCreateValueWithClassSyntaxError() throws Exception {
        assertThrows(ClassSyntaxError.class, () -> TypeHandler.createValue("com.example.MyClass$InnerClass", Object.class));
    }

    @Test
    public void testCreateValueWithIllegalAccess() throws Exception {
        Class<?> clazz = Class.forName("com.example.MyClass");
        Class<?> innerClass = clazz.getDeclaredMember("innerClass").getType();

        Object output;

        try {
            output = TypeHandler.createValue("new " + innerClass.getName() + "()", clazz);
        } catch (IllegalAccessException e) {
            throw new ParseException("Unexpected error", e);
        }

        assertInstanceOf(innerClass, output);
    }

    @Test
    public void testCreateValueWithInstantiationError() throws Exception {
        Class<?> clazz = Class.forName("com.example.MyClass");

        assertThrows(InstantiationException.class, () -> TypeHandler.createValue(clazz.getName(), clazz));
    }

    @Test
    public void testCreateValueWithReflectionError() throws Exception {
        Class<?> clazz = Class.forName("com.example.MyClass");

        Method method = clazz.getDeclaredMethod("createInstance");
        method.setAccessible(true);

        Object output;

        try {
            output = TypeHandler.createValue(clazz.getName(), clazz);
        } catch (InvocationTargetException e) {
            throw new ParseException("Unexpected error", e.getCause());
        } catch (IllegalAccessException e) {
            throw new ParseException("Unexpected error", e);
        }

        assertInstanceOf(clazz, output);
    }

    @Test
    public void testCreateValueWithUndeclaredThrowableException() throws Exception {
        Class<?> clazz = Class.forName("com.example.MyClass");

        Method method = clazz.getDeclaredMethod("createInstance");
        method.setAccessible(true);

        try {
            method.invoke(null);
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            if (cause instanceof RuntimeException) {
                throw (RuntimeException) cause;
            } else {
                throw new RuntimeException("Unexpected error", cause);
            }
        }

        assertThrows(RuntimeException.class, () -> TypeHandler.createValue(clazz.getName(), clazz));
    }
}

class MyClass {
    public static MyClass createInstance() {
        throw new RuntimeException("This method should not be called");
    }
}