package wheel.util;

import wheel.components.*;
import wheel.*;
import java.util.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DynamicSelectModelTest {
    
    private DynamicSelectModel model;
    
    @Before
    public void setUp() {
        model = new DynamicSelectModel();
    }
    
    @Test
    public void testCollection() {
        model.collection("testCollection");
        assertNotNull(model.getObjects());
    }
  
    @Test
    public void testLabel() {
        model.label("testLabel");
        assertEquals("testLabel", model.getLabel(0));
    }
    
    @Test
    public void testValue() {
        model.value("testValue");
        assertEquals("testValue", model.getValue(0));
    }
    
    @Test
    public void testEnumeration() {
        model.enumeration("testEnumeration");
        assertNotNull(model.getObjects());
        assertEquals("toString()", model.getLabel(0));
        assertEquals("ordinal()", model.getValue(0));
    }
    
    @Test
    public void testTranslator() {
        model.translator("testTranslator");
        assertEquals("testTranslator", model.getTranslator().getExpression());
    }
    
    @Test
    public void testEmpty() {
        Object emptyObject = new Object();
        model.empty(emptyObject);
        assertEquals(emptyObject, model.getEmptyObject());
    }
    
    @Test
    public void testGetOptionCount() {
        Collection objects = new ArrayList();
        objects.add(new Object());
        objects.add(new Object());
        objects.add(new Object());
        model.setObjects(objects);
        assertEquals(3, model.getOptionCount());
    }
    
    @Test
    public void testTranslateValue() {
        Collection objects = new ArrayList();
        objects.add(new Object());
        objects.add(new Object());
        objects.add(new Object());
        model.setObjects(objects);
        assertEquals(objects.get(0), model.translateValue("0"));
    }
    
    @Test(expected = WheelException.class)
    public void testNullCollection() {
        model.validate();
    }
    
    @Test(expected = WheelException.class)
    public void testNullLabel() {
        model.collection("testCollection");
        model.validate();
    }
    
    @Test(expected = WheelException.class)
    public void testNullValue() {
        model.collection("testCollection");
        model.label("testLabel");
        model.validate();
    }
    
    @Test
    public void testReset() {
        model.setObjects(new ArrayList());
        model.reset();
        assertNull(model.getObjects());
    }
  
}