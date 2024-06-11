package org.sourceforge.ifx.basetypes;

import static org.junit.Assert.*;
import org.junit.Test;
import java.lang.reflect.*;

public class IFXObjectTest {
    
    IFXObject obj = new IFXObject();
    
    @Test
    public void testEquals() {
        IFXObject obj2 = new IFXObject();
        assertTrue(obj.equals(obj2));
        
        Object obj3 = new Object();
        assertFalse(obj.equals(obj3));
    }
    
    @Test
    public void testToString() {
        String expectedEmptyString = "org.sourceforge.ifx.basetypes.IFXObject = {}";
        assertEquals(expectedEmptyString, obj.toString());
        
        IFXObject obj2 = new IFXObject();
        obj2.setMemberName("memberName");
        obj2.setRetObj("retObj");
        obj2.setRetVal(null);
        obj2.setThatRetVal(null);
        
        String expectedString = "org.sourceforge.ifx.basetypes.IFXObject = {\n" +
                                "  memberName = null,\n" +
                                "  retObj = null,\n" +
                                "  retVal = null,\n" +
                                "  thatRetVal = null\n" +
                                "}";
        assertEquals(expectedString, obj2.toString());
    }
    
    @Test
    public void testStringify() {
        IFXObject obj2 = new IFXObject();
        obj2.setMemberName("memberName");
        obj2.setRetObj("retObj");
        obj2.setRetVal(null);
        obj2.setThatRetVal(null);
        
        String expectedString = "org.sourceforge.ifx.basetypes.IFXObject = {\n" +
                                "  memberName = null,\n" +
                                "  retObj = null,\n" +
                                "  retVal = null,\n" +
                                "  thatRetVal = null\n" +
                                "}";
        assertEquals(expectedString, obj2.stringify(obj2, 0));
    }
    
    @Test
    public void testDoCheckEquals() {
        IFXObject obj2 = new IFXObject();
        obj2.setMemberName("memberName");
        obj2.setRetObj("retObj");
        obj2.setRetVal(null);
        obj2.setThatRetVal(null);
        
        assertTrue(obj.doCheckEquals(obj, obj2));
        
        IFXObject obj3 = null;
        assertFalse(obj.doCheckEquals(obj3, obj));
    }
    
    @Test
    public void testTab() {
        int level = 2;
        String expectedString = "    ";
        assertEquals(expectedString, obj.tab(level));
    }
}