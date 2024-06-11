
package wheel.util;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;
import wheel.components.*;
import wheel.*;

public class DynamicSelectModelTest {
    
    @Test
    public void testConstructor1() {
        ElExpression collection = new ElExpression("collection");
        ElExpression label = new ElExpression("label");
        ElExpression value = new ElExpression("value");
        DynamicSelectModel dsm = new DynamicSelectModel(collection, label, value);
        
        assertNotNull(dsm);
        assertNull(dsm.getObjects());
        assertNull(dsm.getLabel(0));
        assertNull(dsm.getValue(0));
        assertNull(dsm.translateValue("value"));
        assertEquals(0, dsm.getOptionCount());
    }
    
    @Test
    public void testConstructor2() {
        DynamicSelectModel dsm = new DynamicSelectModel();
        
        assertNotNull(dsm);
        assertNull(dsm.getObjects());
        assertNull(dsm.getLabel(0));
        assertNull(dsm.getValue(0));
        assertNull(dsm.translateValue("value"));
        assertEquals(0, dsm.getOptionCount());
    }
    
    @Test
    public void testCollection() {
        String collection = "collection";
        DynamicSelectModel dsm = new DynamicSelectModel().collection(collection);
        
        assertNotNull(dsm);
        assertNull(dsm.getObjects());
        assertNull(dsm.getLabel(0));
        assertNull(dsm.getValue(0));
        assertNull(dsm.translateValue("value"));
        assertEquals(0, dsm.getOptionCount());
    }
    
    @Test
    public void testLabel() {
        String label = "label";
        DynamicSelectModel dsm = new DynamicSelectModel().label(label);
        
        assertNotNull(dsm);
        assertNull(dsm.getObjects());
        assertNull(dsm.getLabel(0));
        assertNull(dsm.getValue(0));
        assertNull(dsm.translateValue("value"));
        assertEquals(0, dsm.getOptionCount());
    }
    
    @Test
    public void testValue() {
        String value = "value";
        DynamicSelectModel dsm = new DynamicSelectModel().value(value);
        
        assertNotNull(dsm);
        assertNull(dsm.getObjects());
        assertNull(dsm.getLabel(0));
        assertNull(dsm.getValue(0));
        assertNull(dsm.translateValue("value"));
        assertEquals(0, dsm.getOptionCount());
    }
    
    @Test
    public void testEnumeration() {
        String enumeration = "enumeration";
        DynamicSelectModel dsm = new DynamicSelectModel().enumeration(enumeration);
        
        assertNotNull(dsm);
        assertNull(dsm.getObjects());
        assertNull(dsm.getLabel(0));
        assertNull(dsm.getValue(0));
        assertNull(dsm.translateValue("value"));
        assertEquals(0, dsm.getOptionCount());
    }
    
    @Test
    public void testTranslator() {
        String translator = "translator";
        DynamicSelectModel dsm = new DynamicSelectModel().translator(translator);
        
        assertNotNull(dsm);
        assertNull(dsm.getObjects());
        assertNull(dsm.getLabel(0));
        assertNull(dsm.getValue(0));
        assertNull(dsm.translateValue("value"));
        assertEquals(0, dsm.getOptionCount());
    }
    
    @Test
    public void testEmpty() {
        Object o = new Object();
        DynamicSelectModel dsm = new DynamicSelectModel().empty(o);
        
        assertNotNull(dsm);
        assertNull(dsm.getObjects());
        assertNull(dsm.getLabel(0));
        assertNull(dsm.getValue(0));
        assertNull(dsm.translateValue("value"));
        assertEquals(0, dsm.getOptionCount());
    }
    
    @Test
    public void testGetOptionCount() {
        ArrayList<Object> objects = new ArrayList<>();
        objects.add(new Object());
        objects.add(new Object());
        DynamicSelectModel dsm = new DynamicSelectModel();
        dsm.objects = objects;
        
        assertEquals(2, dsm.getOptionCount());
    }
    
    @Test
    public void testGetLabel() {
        ArrayList<Object> objects = new ArrayList<>();
        objects.add(new Object());
        DynamicSelectModel dsm = new DynamicSelectModel();
        dsm.objects = objects;
        dsm.label = new ElExpression("label");
        dsm.component = new Component();
        
        assertEquals("Object", dsm.getLabel(0));
    }
    
    @Test
    public void testGetValue() {
        ArrayList<Object> objects = new ArrayList<>();
        objects.add(new Object());
        DynamicSelectModel dsm = new DynamicSelectModel();
        dsm.objects = objects;
        dsm.value = new ElExpression("value");
        dsm.component = new Component();
        
        assertEquals("Object", dsm.getValue(0));
    }
    
    @Test
    public void testTranslateValue() {
        ArrayList<Object> objects = new ArrayList<>();
        Object o = new Object();
        objects.add(o);
        DynamicSelectModel dsm = new DynamicSelectModel();
        dsm.objects = objects;
        dsm.translator = new ElExpression("translator");
        dsm.component = new Component();
        
        assertEquals(o, dsm.translateValue("value"));
    }
    
    @Test
    public void testGetObjects() {
        Collection<Object> objects = new ArrayList<>();
        DynamicSelectModel dsm = new DynamicSelectModel();
        dsm.objects = objects;
        dsm.collection = new ElExpression("collection");
        dsm.topLevelComponent = new StandaloneComponent();
        dsm.component = new Component();
        
        assertSame(objects, dsm.getObjects());
    }
    
    @Test
    public void testGetObjects_Null() {
        DynamicSelectModel dsm = new DynamicSelectModel();
        dsm.collection = new ElExpression("collection");
        dsm.topLevelComponent = new StandaloneComponent();
        dsm.component = new Component();
        
        try {
            dsm.getObjects();
            fail("Expected WheelException to be thrown");
        } catch (WheelException e) {
            assertEquals("Expression collection evaluated to null. Can't build a dynamic ISelectModel", e.getMessage());
        }
    }
    
    @Test
    public void testGetObjects_NotCollection() {
        Object[] array = {new Object()};
        DynamicSelectModel dsm = new DynamicSelectModel();
        dsm.collection = new ElExpression("collection");
        dsm.topLevelComponent = new StandaloneComponent();
        dsm.component = new Component();
        
        try {
            dsm.getObjects();
            fail("Expected WheelException to be thrown");
        } catch (WheelException e) {
            assertEquals("Expression collection evaluated to an object that is not an instance of Collection or Object[]. Can't build a dynamic ISelectModel", e.getMessage());
        }
    }
    
    @Test
    public void testGetObjects_WithEmptyObject() {
        Collection<Object> objects = new ArrayList<>();
        Object o = new Object();
        objects.add(new Object());
        DynamicSelectModel dsm = new DynamicSelectModel();
        dsm.objects = objects;
        dsm.collection = new ElExpression("collection");
        dsm.topLevelComponent = new StandaloneComponent();
        dsm.component = new Component();
        dsm.emptyObject = o;
        
        Collection<Object> newCollection = new LinkedList<>();
        newCollection.add(o);
        newCollection.addAll(objects);
        
        assertSame(newCollection, dsm.getObjects());
    }
    
    @Test
    public void testGetObjects_WithoutEmptyObject() {
        Collection<Object> objects = new ArrayList<>();
        objects.add(new Object());
        DynamicSelectModel dsm = new DynamicSelectModel();
        dsm.objects = objects;
        dsm.collection = new ElExpression("collection");
        dsm.topLevelComponent = new StandaloneComponent();
        dsm.component = new Component();
        
        assertSame(objects, dsm.getObjects());
    }
    
    @Test
    public void testGetObjects_NullCollection() {
        DynamicSelectModel dsm = new DynamicSelectModel();
        dsm.topLevelComponent = new StandaloneComponent();
        dsm.component = new Component();
        
        try {
            dsm.getObjects();
            fail("Expected WheelException to be thrown");
        } catch (WheelException e) {
            assertEquals("Could not evaluate expression null to create a dynamic ISelectModel.", e.getMessage());
        }
    }
    
    @Test
    public void testGetComponent() {
        Component component = new Component();
        DynamicSelectModel dsm = new DynamicSelectModel();
        dsm.component = component;
        
        assertSame(component, dsm.getComponent());
    }
    
    @Test
    public void testSetComponent() {
        Component component = new Component();
        DynamicSelectModel dsm = new DynamicSelectModel();
        dsm.setComponent(component);
        
        assertSame(component, dsm.component);
    }
    
    @Test
    public void testGetTopLevelComponent() {
        StandaloneComponent topLevelComponent = new StandaloneComponent();
        DynamicSelectModel dsm = new DynamicSelectModel();
        dsm.topLevelComponent = topLevelComponent;
        
        assertSame(topLevelComponent, dsm.getTopLevelComponent());
    }
    
    @Test
    public void testSetTopLevelComponent() {
        StandaloneComponent topLevelComponent = new StandaloneComponent();
        DynamicSelectModel dsm = new DynamicSelectModel();
        dsm.setTopLevelComponent(topLevelComponent);
        
        assertSame(topLevelComponent, dsm.topLevelComponent);
    }
    
    @Test
    public void testValidate_CollectionNull() {
        DynamicSelectModel dsm = new DynamicSelectModel();
        dsm.label = new ElExpression("label");
        dsm.value = new ElExpression("value");
        
        try {
            dsm.validate();
            fail("Expected WheelException to be thrown");
        } catch (WheelException e) {
            assertEquals("Could not create DynamicSelection model. Collection attribute was null or not an el-expression.", e.getMessage());
        }
    }
    
    @Test
    public void testValidate_LabelNull() {
        DynamicSelectModel dsm = new DynamicSelectModel();
        dsm.collection = new ElExpression("collection");
        dsm.value = new ElExpression("value");
        
        try {
            dsm.validate();
            fail("Expected WheelException to be thrown");
        } catch (WheelException e) {
            assertEquals("Could not create DynamicSelection model. Label attribute was null or not an el-expression.", e.getMessage());
        }
    }
    
    @Test
    public void testValidate_ValueNull() {
        DynamicSelectModel dsm = new DynamicSelectModel();
        dsm.collection = new ElExpression("collection");
        dsm.label = new ElExpression("label");
        
        try {
            dsm.validate();
            fail("Expected WheelException to be thrown");
        } catch (WheelException e) {
            assertEquals("Could not create DynamicSelection model. Value attribute was null or not an el-expression.", e.getMessage());
        }
    }
    
    @Test
    public void testReset() {
        Collection<Object> objects = new ArrayList<>();
        DynamicSelectModel dsm = new DynamicSelectModel();
        dsm.objects = objects;
        
        dsm.reset();
        
        assertNull(dsm.objects);
    }
}
