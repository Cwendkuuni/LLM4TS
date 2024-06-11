package org.sourceforge.ifx.basetypes;

import org.junit.Assert;
import org.junit.Test;
import java.lang.reflect.*;

public class IFXObjectTest {

    @Test
    public void testEquals() {
        
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();

        boolean result = obj1.equals(obj2);

        Assert.assertTrue(result);
    }

    @Test
    public void testToString() {
        
        IFXObject obj = new IFXObject();

        String result = obj.toString();
        
        String expected = "org.sourceforge.ifx.basetypes.IFXObject = {\n}";

        Assert.assertEquals(expected, result);
    }

    @Test
    public void testStringify() {
        
        IFXObject obj = new IFXObject();
        int level = 0;

        String result = obj.stringify(obj, level);
        
        String expected = "org.sourceforge.ifx.basetypes.IFXObject = {\n}";

        Assert.assertEquals(expected, result);
    }

    @Test
    public void testDoCheckEquals() {
        
        IFXObject thisObj = new IFXObject();
        IFXObject thatObj = new IFXObject();

        boolean result = thisObj.doCheckEquals(thisObj, thatObj);

        Assert.assertTrue(result);
    }

    @Test
    public void testTab() {
        
        IFXObject obj = new IFXObject();
        int level = 0;

        String result = obj.tab(level);
        
        String expected = "";

        Assert.assertEquals(expected, result);
    }
}