package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class TypeHandlerTest {
    @Test
    public void testCreateValue() throws ParseException {
        assertEquals("test", TypeHandler.createValue("test", String.class));
        assertEquals(new Object(), TypeHandler.createValue("java.lang.Object", Object.class));
        assertEquals(10, TypeHandler.createValue("10", Number.class));
        assertNull(TypeHandler.createValue("2021-01-01", Date.class));
        assertEquals(test.class, TypeHandler.createValue("test", Class.class));
        assertEquals(new File("test.txt"), TypeHandler.createValue("test.txt", File.class));
        assertEquals(new FileInputStream("test.txt"), TypeHandler.createValue("test.txt", FileInputStream.class));
        assertNull(TypeHandler.createValue("test.txt", Files.class));
        assertEquals(new URL("http://www.example.com"), TypeHandler.createValue("http://www.example.com", URL.class));
    }

    @Test
    public void testCreateObject() throws ParseException {
        assertEquals(new Object(), TypeHandler.createObject("java.lang.Object"));
        assertNull(TypeHandler.createObject("test"));
    }

    @Test
    public void testCreateNumber() throws ParseException {
        assertEquals(10, TypeHandler.createNumber("10"));
        assertEquals(10.5, TypeHandler.createNumber("10.5"));
        assertEquals(0, TypeHandler.createNumber("test"));
    }

    @Test
    public void testCreateClass() throws ParseException {
        assertEquals(test.class, TypeHandler.createClass("test"));
        try {
            TypeHandler.createClass("java.lang.Test");
        } catch (ParseException e) {
            assertEquals("Unable to find the class: java.lang.Test", e.getMessage());
        }
    }

    @Test
    public void testCreateDate() {
        try {
            TypeHandler.createDate("2021-01-01");
        } catch (UnsupportedOperationException e) {
            assertEquals("Not yet implemented", e.getMessage());
        }
    }

    @Test
    public void testCreateURL() throws ParseException {
        assertEquals(new URL("http://www.example.com"), TypeHandler.createURL("http://www.example.com"));
        try {
            TypeHandler.createURL("test");
        } catch (ParseException e) {
            assertEquals("Unable to parse the URL: test", e.getMessage());
        }
    }

    @Test
    public void testCreateFile() {
        assertEquals(new File("test.txt"), TypeHandler.createFile("test.txt"));
    }

    @Test
    public void testOpenFile() throws ParseException {
        try {
            TypeHandler.openFile("test.txt");
        } catch (ParseException e) {
            assertEquals("Unable to find file: test.txt", e.getMessage());
        }
    }

    @Test
    public void testCreateFiles() {
        try {
            TypeHandler.createFiles("test");
        } catch (UnsupportedOperationException e) {
            assertEquals("Not yet implemented", e.getMessage());
        }
    }
}