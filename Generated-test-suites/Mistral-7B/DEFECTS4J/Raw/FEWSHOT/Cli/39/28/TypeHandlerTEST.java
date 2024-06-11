package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import java.lang.ClassNotFoundException;
import java.lang.IllegalAccessException;
import java.lang.InstantiationException;
import java.lang.reflect.InvocationTargetException;

import org.junit.Test;

public class TypeHandlerTest {

    @Test
    public void testCreateValueStringObject() {
        assertEquals("hello", TypeHandler.createValue("hello", String.class));
    }

    @Test
    public void testCreateValueStringClass() {
        assertThrows(ParseException.class, () -> TypeHandler.createValue("hello", int.class));
        assertNull(TypeHandler.createValue("", Object.class));
        assertSame(TypeHandler.class, TypeHandler.createValue("org.apache.commons.cli.TypeHandler", TypeHandler.class));
        assertThrows(ClassNotFoundException.class, () -> TypeHandler.createValue("com.test.MyClass", Object.class));
        assertThrows(InstantiationException.class, () -> TypeHandler.createValue("com.test.MyClass", MyClass.class));
        assertThrows(IllegalAccessException.class, () -> TypeHandler.createValue("com.test.MyClass", MyClass.class));
        assertThrows(InvocationTargetException.class, () -> TypeHandler.createValue("com.test.MyClass", MyClass.class));
    }

    @Test
    public void testCreateValueStringClassWithConstructor() {
        class TestClass {
            public TestClass() {
            }

            public TestClass(String str) {
                this();
                assertEquals("hello", str);
            }
        }

        assertEquals("hello", TypeHandler.createValue("hello", TestClass.class));
    }

    @Test
    public void testCreateValueStringClassWithStaticConstructor() {
        class TestClass {
            public TestClass() {
            }

            public static TestClass createInstance(String str) {
                assertEquals("hello", str);
                return new TestClass();
            }
        }

        assertEquals(TestClass.class, TypeHandler.createValue("hello", TestClass.class).getClass());
    }

    @Test
    public void testCreateNumber() {
        assertEquals((double) 1.2, TypeHandler.createNumber("1.2"));
        assertEquals((long) 1, TypeHandler.createNumber("1"));
        assertEquals((long) 0, TypeHandler.createNumber(""));
        assertThrows(NumberFormatException.class, () -> TypeHandler.createNumber("foo"));
    }

    @Test
    public void testCreateDate() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Test
    public void testCreateClass() {
        assertEquals(String.class, TypeHandler.createClass("java.lang.String"));
        assertThrows(ClassNotFoundException.class, () -> TypeHandler.createClass("com.test.MyClass"));
    }

    @Test
    public void testCreateURL() {
        assertEquals(new URL("http://example.com"), TypeHandler.createURL("http://example.com"));
        assertThrows(MalformedURLException.class, () -> TypeHandler.createURL("http://example.com/foo%"));
    }

    @Test
    public void testCreateFile() {
        File file = new File("test.txt");
        assertEquals(file, TypeHandler.createFile("test.txt"));
    }

    @Test
    public void testCreateFileInputStream() {
        File file = new File("test.txt");
        assertThrows(FileNotFoundException.class, () -> TypeHandler.createFile(file.getPath()));
    }

    @Test
    public void testCreateFiles() {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}