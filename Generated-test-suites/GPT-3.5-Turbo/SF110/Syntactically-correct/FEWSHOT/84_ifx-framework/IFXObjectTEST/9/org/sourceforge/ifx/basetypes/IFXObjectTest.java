package org.sourceforge.ifx.basetypes;

import org.junit.Test;
import static org.junit.Assert.*;

public class IFXObjectTest {

    @Test
    public void testEquals() {
        // Test when obj is not an instance of IFXObject
        boolean result = new IFXObject().equals(new Object());
        assertFalse(result);

        // Test when obj is not null and is an instance of IFXObject
        result = new IFXObject().equals(new IFXObject());
        assertTrue(result);
    }

    @Test
    public void testToString() {
        // Create an instance of IFXObject and call toString method
        String result = new IFXObject().toString();

        // Check if the returned string starts with the class name
        assertTrue(result.startsWith("org.sourceforge.ifx.basetypes.IFXObject = {"));
        // Check if the returned string ends with a closing brace
        assertTrue(result.endsWith("}"));
    }
    
    @Test
    public void testStringify() {
        // Create an instance of IFXObject and call private stringify method
        String result = new IFXObject().stringify(new IFXObject(), 0);

        // Check if the returned string starts with the class name
        assertTrue(result.startsWith("org.sourceforge.ifx.basetypes.IFXObject = {"));
        // Check if the returned string ends with a closing brace
        assertTrue(result.endsWith("}"));
    }

    @Test
    public void testDoCheckEquals() {
        // Create two instances of IFXObject
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();

        // Call the private doCheckEquals method and check the returned value
        boolean result = new IFXObject().doCheckEquals(obj1, obj2);
        assertTrue(result);

        // Test when obj1 is null and obj2 is not null
        result = new IFXObject().doCheckEquals(null, obj2);
        assertFalse(result);

        // Test when obj1 is not null and obj2 is null
        result = new IFXObject().doCheckEquals(obj1, null);
        assertFalse(result);

        // Test when obj1 and obj2 have different class names
        obj1 = new IFXObject();
        obj2 = new IFXObjectSubclass();
        result = new IFXObject().doCheckEquals(obj1, obj2);
        assertFalse(result);
    }

    @Test
    public void testTab() {
        // Call the private tab method and check the returned value
        String result = new IFXObject().tab(2);
        assertEquals("    ", result);

        result = new IFXObject().tab(3);
        assertEquals("        ", result);

        result = new IFXObject().tab(0);
        assertEquals("", result);
    }
}