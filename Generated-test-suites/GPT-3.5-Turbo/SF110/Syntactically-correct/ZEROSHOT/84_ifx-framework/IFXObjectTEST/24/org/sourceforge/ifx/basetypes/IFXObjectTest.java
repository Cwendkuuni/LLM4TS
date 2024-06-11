package org.sourceforge.ifx.basetypes;

import org.junit.Test;
import static org.junit.Assert.*;

public class IFXObjectTest {

    @Test
    public void testEquals() {
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();

        // Test when both objects are null
        assertFalse(obj1.equals(null));

        // Test when obj1 is null
        assertFalse(obj1.equals(obj2));

        // Test when obj2 is null
        assertFalse(obj2.equals(obj1));

        // Test when obj2 and obj1 are not null
        IFXObject obj3 = new IFXObject();
        assertTrue(obj1.equals(obj3));
    }

    @Test
    public void testToString() {
        IFXObject obj = new IFXObject();

        // Test that the toString method returns a string
        assertNotNull(obj.toString());
    }

    @Test
    public void testStringify() {
        IFXObject obj = new IFXObject();

        // Test that the stringify method returns a string
        assertNotNull(obj.stringify(obj, 0));
    }

    @Test
    public void testDoCheckEquals() {
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();

        // Test that the doCheckEquals method returns a boolean
        assertEquals(obj1.doCheckEquals(obj1, obj2), true);
    }

    @Test
    public void testTab() {
        IFXObject obj = new IFXObject();

        // Test that the tab method returns a string
        assertNotNull(obj.tab(0));
    }
}