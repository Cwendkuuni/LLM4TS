package org.sourceforge.ifx.basetypes;

import org.junit.Test;
import static org.junit.Assert.*;

public class IFXObjectTest {
  
  @Test
  public void testEquals_true() {
    IFXObject obj1 = new IFXObject();
    IFXObject obj2 = new IFXObject();
    
    assertTrue(obj1.equals(obj2));
  }
  
  @Test
  public void testEquals_false() {
    IFXObject obj1 = new IFXObject();
    String obj2 = "Not an IFXObject";
    
    assertFalse(obj1.equals(obj2));
  }
  
  @Test
  public void testToString() {
    IFXObject obj = new IFXObject();
    
    String expected = "org.sourceforge.ifx.basetypes.IFXObject = {\n}";
    assertEquals(expected, obj.toString());
  }
  
  @Test
  public void testStringify() {
    IFXObject obj = new IFXObject();
    int level = 0;
    
    String expected = "org.sourceforge.ifx.basetypes.IFXObject = {\n}";
    assertEquals(expected, obj.stringify(obj, level));
  }
  
  @Test
  public void testDoCheckEquals_true() {
    IFXObject thisObj = new IFXObject();
    IFXObject thatObj = new IFXObject();
    
    assertTrue(thisObj.doCheckEquals(thisObj, thatObj));
  }
  
  @Test
  public void testDoCheckEquals_false() {
    IFXObject thisObj = new IFXObject();
    String thatObj = "Not an IFXObject";
    
    assertFalse(thisObj.doCheckEquals(thisObj, thatObj));
  }
  
  @Test
  public void testTab() {
    IFXObject obj = new IFXObject();
    int level = 2;
    
    String expected = "    ";
    assertEquals(expected, obj.tab(level));
  }
}