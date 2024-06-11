package wheel.util;

import wheel.components.*;
import wheel.*;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class DynamicSelectModelTest {
    
    @Test
    public void testConstructor() {
        ElExpression collection = new ElExpression("collection");
        ElExpression label = new ElExpression("label");
        ElExpression value = new ElExpression("value");
        
        DynamicSelectModel model = new DynamicSelectModel(collection, label, value);
        
        assertNotNull(model);
        assertEquals(collection, model.collection);
        assertEquals(label, model.label);
        assertEquals(value, model.value);
    }
    
    @Test
    public void testDefaultConstructor() {
        DynamicSelectModel model = new DynamicSelectModel();
        
        assertNotNull(model);
        assertNull(model.collection);
        assertNull(model.label);
        assertNull(model.value);
    }
    
    @Test
    public void testCollection() {
        String collection = "collection";
        DynamicSelectModel model = new DynamicSelectModel();
        
        model.collection(collection);
        
        assertEquals(collection, model.collection.getExpression());
    }
    
    @Test
    public void testLabel() {
        String label = "label";
        DynamicSelectModel model = new DynamicSelectModel();
        
        model.label(label);
        
        assertEquals(label, model.label.getExpression());
    }
    
    @Test
    public void testValue() {
        String value = "value";
        DynamicSelectModel model = new DynamicSelectModel();
        
        model.value(value);
        
        assertEquals(value, model.value.getExpression());
    }
    
    @Test
    public void testEnumeration() {
        String enumeration = "enumeration";
        DynamicSelectModel model = new DynamicSelectModel();
        
        model.enumeration(enumeration);
        
        assertEquals(enumeration + ".values()", model.collection.getExpression());
        assertEquals("toString()", model.label.getExpression());
        assertEquals("ordinal()", model.value.getExpression());
    }
    
    @Test
    public void testTranslator() {
        String translator = "translator";
        DynamicSelectModel model = new DynamicSelectModel();
        
        model.translator(translator);
        
        assertEquals(translator, model.translator.getExpression());
    }
    
    @Test
    public void testEmpty() {
        Object o = new Object();
        DynamicSelectModel model = new DynamicSelectModel();
        
        model.empty(o);
        
        assertEquals(o, model.emptyObject);
    }
    
    @Test
    public void testGetOptionCount() {
        DynamicSelectModel model = new DynamicSelectModel();
        model.objects = new ArrayList<>();
        model.objects.add(new Object());
        model.objects.add(new Object());
        
        int optionCount = model.getOptionCount();
        
        assertEquals(2, optionCount);
    }
    
    @Test
    public void testGetLabel() {
        DynamicSelectModel model = new DynamicSelectModel();
        model.label = new ElExpression("label");
        model.component = new Component();
        model.objects = new ArrayList<>();
        model.objects.add(new Object());
        
        String label = model.getLabel(0);
        
        assertNotNull(label);
    }
    
    @Test
    public void testGetValue() {
        DynamicSelectModel model = new DynamicSelectModel();
        model.value = new ElExpression("value");
        model.component = new Component();
        model.objects = new ArrayList<>();
        model.objects.add(new Object());
        
        String value = model.getValue(0);
        
        assertNotNull(value);
    }
    
    @Test
    public void testTranslateValue() {
        DynamicSelectModel model = new DynamicSelectModel();
        model.translator = new ElExpression("translator");
        model.component = new Component();
        model.objects = new ArrayList<>();
        model.objects.add(new Object());
        
        Object translatedValue = model.translateValue("value");
        
        assertNotNull(translatedValue);
    }
    
    @Test
    public void testGetObjects() {
        DynamicSelectModel model = new DynamicSelectModel();
        model.collection = new ElExpression("collection");
        model.topLevelComponent = new StandaloneComponent();
        model.component = new Component();
        
        Collection objects = model.getObjects();
        
        assertNotNull(objects);
    }
    
    @Test
    public void testGetObjectsNullCollection() {
        DynamicSelectModel model = new DynamicSelectModel();
        model.topLevelComponent = new StandaloneComponent();
        model.component = new Component();
        
        try {
            model.getObjects();
            fail("Expected WheelException to be thrown");
        } catch (WheelException e) {
            assertEquals("Could not evaluate expression null to create a dynamic ISelectModel.", e.getMessage());
        }
    }
    
    @Test
    public void testGetObjectsNullObject() {
        DynamicSelectModel model = new DynamicSelectModel();
        model.collection = new ElExpression("null");
        model.topLevelComponent = new StandaloneComponent();
        model.component = new Component();
        
        try {
            model.getObjects();
            fail("Expected WheelException to be thrown");
        } catch (WheelException e) {
            assertEquals("Expression null evaluated to null. Can't build a dynamic ISelectModel", e.getMessage());
        }
    }
    
    @Test
    public void testGetObjectsNotCollection() {
        DynamicSelectModel model = new DynamicSelectModel();
        model.collection = new ElExpression("notCollection");
        model.topLevelComponent = new StandaloneComponent();
        model.component = new Component();
        
        try {
            model.getObjects();
            fail("Expected WheelException to be thrown");
        } catch (WheelException e) {
            assertEquals("Expression notCollection evaluated to an object that is not an instance of Collection or Object[]. Can't build a dynamic ISelectModel", e.getMessage());
        }
    }
    
    @Test
    public void testGetObjectsEmptyObject() {
        DynamicSelectModel model = new DynamicSelectModel();
        model.collection = new ElExpression("collection");
        model.emptyObject = new Object();
        model.topLevelComponent = new StandaloneComponent();
        model.component = new Component();
        
        Collection objects = model.getObjects();
        
        assertNotNull(objects);
    }
    
    @Test
    public void testGetComponent() {
        DynamicSelectModel model = new DynamicSelectModel();
        Component component = new Component();
        model.setComponent(component);
        
        Component result = model.getComponent();
        
        assertEquals(component, result);
    }
    
    @Test
    public void testSetComponent() {
        DynamicSelectModel model = new DynamicSelectModel();
        Component component = new Component();
        
        model.setComponent(component);
        
        assertEquals(component, model.component);
    }
    
    @Test
    public void testGetTopLevelComponent() {
        DynamicSelectModel model = new DynamicSelectModel();
        StandaloneComponent topLevelComponent = new StandaloneComponent();
        model.setTopLevelComponent(topLevelComponent);
        
        StandaloneComponent result = model.getTopLevelComponent();
        
        assertEquals(topLevelComponent, result);
    }
    
    @Test
    public void testSetTopLevelComponent() {
        DynamicSelectModel model = new DynamicSelectModel();
        StandaloneComponent topLevelComponent = new StandaloneComponent();
        
        model.setTopLevelComponent(topLevelComponent);
        
        assertEquals(topLevelComponent, model.topLevelComponent);
    }
    
    @Test
    public void testValidate() {
        DynamicSelectModel model = new DynamicSelectModel();
        
        try {
            model.validate();
            fail("Expected WheelException to be thrown");
        } catch (WheelException e) {
            assertEquals("Could not create DynamicSelection model. Collection attribute was null or not an el-expression.", e.getMessage());
        }
        
        model.collection = new ElExpression("collection");
        
        try {
            model.validate();
            fail("Expected WheelException to be thrown");
        } catch (WheelException e) {
            assertEquals("Could not create DynamicSelection model. Label attribute was null or not an el-expression.", e.getMessage());
        }
        
        model.label = new ElExpression("label");
        
        try {
            model.validate();
            fail("Expected WheelException to be thrown");
        } catch (WheelException e) {
            assertEquals("Could not create DynamicSelection model. Value attribute was null or not an el-expression.", e.getMessage());
        }
    }
    
    @Test
    public void testReset() {
        DynamicSelectModel model = new DynamicSelectModel();
        model.objects = new ArrayList<>();
        
        model.reset();
        
        assertNull(model.objects);
    }
}