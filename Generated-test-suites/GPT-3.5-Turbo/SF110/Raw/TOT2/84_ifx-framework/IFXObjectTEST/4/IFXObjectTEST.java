
package org.sourceforge.ifx.basetypes;

import org.junit.Test;
import static org.junit.Assert.*;

public class IFXObjectTest {

    @Test
    public void testEquals() {
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();

        boolean result = obj1.equals(obj2);

        assertTrue(result);
    }

    @Test
    public void testToString() {
        IFXObject obj = new IFXObject();

        String result = obj.toString();

        assertNotNull(result);
    }

}