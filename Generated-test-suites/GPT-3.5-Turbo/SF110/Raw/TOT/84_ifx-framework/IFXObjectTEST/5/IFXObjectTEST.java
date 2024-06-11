package org.sourceforge.ifx.basetypes;

import org.junit.Test;
import static org.junit.Assert.*;

public class IFXObjectTest {

    // Test case for equals method
    @Test
    public void testEquals() {
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();

        // Test when obj1 equals obj2
        boolean result = obj1.equals(obj2);
        assertTrue(result);

        // Test when obj1 does not equal obj2
        obj2 = new IFXObjectChild();
        result = obj1.equals(obj2);
        assertFalse(result);
    }

    // Test case for toString method
    @Test
    public void testToString() {
        IFXObject obj1 = new IFXObject();

        // Test when obj1 is empty
        String expected = "org.sourceforge.ifx.basetypes.IFXObject = {}\n";
        String result = obj1.toString();
        assertEquals(expected, result);

        // Test when obj1 has values
        IFXObjectChild child1 = new IFXObjectChild();
        IFXObjectChild child2 = new IFXObjectChild();
        child2.setIntValue(10);
        child2.setStrValue("Hello");

        obj1.setChild(child1);
        obj1.setChildArray(new IFXObjectChild[] { child2 });

        expected = "org.sourceforge.ifx.basetypes.IFXObject = {\n"
                    + "  child = org.sourceforge.ifx.basetypes.IFXObjectChild = {}\n"
                    + "  childArray = [\n"
                    + "    [0]:org.sourceforge.ifx.basetypes.IFXObjectChild = {\n"
                    + "      intValue = 10\n"
                    + "      strValue = Hello\n"
                    + "    }\n"
                    + "  ]\n"
                    + "}";
        result = obj1.toString();
        assertEquals(expected, result);
    }
}