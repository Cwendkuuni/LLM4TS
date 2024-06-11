package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;
import static org.junit.Assert.assertThrows;

public class TypeHandlerTest {
    @Test
    public void testCreateValueStringObject() throws ParseException {
        String str = "test";
        Object obj = new Object() {
            @Override
            public String toString() {
                return "testObject";
            }
        };
        Object actual = TypeHandler.createValue(str, obj);
        assertEquals("test", actual.toString());
    }

    @Test
    public void testCreateValueEmptyString() throws ParseException {
        String str = "";
        Object obj = new Object() {
            @Override
            public String toString() {
                return "testObject";
            }
        };
        assertThrows(ParseException.class, () -> TypeHandler.createValue(str, obj));
    }

    @Test
    public void testCreateValueNullString() throws ParseException {
        Object obj = new Object() {
            @Override
            public String toString() {
                return "testObject";
            }
        };
        assertThrows(ParseException.class, () -> TypeHandler.createValue(null, obj));
    }

    @Test
    public void testCreateValueNullObject() throws ParseException {
        String str = "test";
        assertThrows(ParseException.class, () -> TypeHandler.createValue(str, null));
    }

    @Test
    public void testCreateValueInvalidClassType() throws ParseException {
        String str = "123";
        assertThrows(ParseException.class, () -> TypeHandler.createValue(str, int.class));
    }

    @Test
    public void testCreateValueInvalidClassName() throws ParseException {
        String str = "InvalidClass";
        assertThrows(ParseException.class, () -> TypeHandler.createValue(str, String.class));
    }

    @Test
    public void testCreateValueInvalidURL() throws ParseException {
        String str = "http://invalidurl";
        assertThrows(ParseException.class, () -> TypeHandler.createValue(str, URL.class));
    }

    @Test
    public void testCreateValueInvalidFilepath() throws ParseException {
        String str = "/invalid/filepath";
        assertThrows(ParseException.class, () -> TypeHandler.createValue(str, File.class));
    }
}