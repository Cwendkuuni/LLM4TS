package org.apache.commons.cli;
import org.apache.commons.cli.TypeHandler;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;

public class TypeHandlerTest {

    @Test
    public void testCreateValueStringObject() {
        String str = "test";
        Object obj = String.class;
        Object value = TypeHandler.createValue(str, obj);
        assertEquals("test", value);
    }

    @Test
    public void testCreateValueNullString() {
        Object obj = String.class;
        assertNull(TypeHandler.createValue(null, obj));
    }

    @Test
    public void testCreateValueNullObject() {
        String str = "test";
        assertNull(TypeHandler.createValue(str, null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateValueInvalidClass() {
        String str = "test";
        Class clazz = Integer.TYPE;
        TypeHandler.createValue(str, clazz);
    }

    @Test
    public void testCreateValueEmptyString() {
        Object obj = String.class;
        Object value = TypeHandler.createValue("", obj);
        assertEquals("", value);
    }

    @Test
    public void testCreateValueSpecialChars() {
        Object obj = String.class;
        Object value = TypeHandler.createValue("!@#$%^&*()_+-=[]{}|;:,.<>?", obj);
        assertEquals("!@#$%^&*()_+-=[]{}|;:,.<>?", value);
    }
}