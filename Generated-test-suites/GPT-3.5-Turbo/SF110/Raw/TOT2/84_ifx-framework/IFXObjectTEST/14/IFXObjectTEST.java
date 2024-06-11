package org.sourceforge.ifx.basetypes;

import org.junit.Test;
import static org.junit.Assert.*;

public class IFXObjectTest {

    IFXObject object1 = new IFXObject();
    IFXObject object2 = new IFXObject();

    @Test
    public void testEquals() {
        assertTrue(object1.equals(object1));
        assertFalse(object1.equals(null));
        assertFalse(object1.equals("Not an IFXObject"));
    }

    @Test
    public void testToString() {
        assertNotNull(object1.toString());
    }

    @Test
    public void testStringify() {
        String result = object1.stringify(object1, 0);
        assertNotNull(result);
    }

    @Test
    public void testDoCheckEquals() {
        assertTrue(object1.doCheckEquals(object1, object1));
        assertFalse(object1.doCheckEquals(null, object1));
    }

    @Test
    public void testTab() {
        String tab = object1.tab(2);
        assertEquals("    ", tab);
    }
}