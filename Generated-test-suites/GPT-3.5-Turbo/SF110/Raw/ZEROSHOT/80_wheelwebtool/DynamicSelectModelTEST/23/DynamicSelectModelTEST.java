import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import wheel.components.Component;
import wheel.components.StandaloneComponent;
import wheel.ElExpression;
import wheel.WheelException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

public class DynamicSelectModelTest {
    private DynamicSelectModel dynamicSelectModel;
    private Component component;
    private StandaloneComponent topLevelComponent;
    private ElExpression label;
    private ElExpression value;
    private ElExpression collection;
    private ElExpression translator;
    private Collection objects;
    private Object emptyObject;
    
    @Before
    public void setUp() {
        dynamicSelectModel = new DynamicSelectModel();
        component = new Component();
        topLevelComponent = new StandaloneComponent();
        label = new ElExpression();
        value = new ElExpression();
        collection = new ElExpression();
        translator = new ElExpression();
        objects = new ArrayList();
        emptyObject = new Object();
    }
    
    @Test
    public void testConstructor() {
        dynamicSelectModel = new DynamicSelectModel(collection, label, value);
        assertNotNull(dynamicSelectModel);
        assertEquals(collection, dynamicSelectModel.collection);
        assertEquals(label, dynamicSelectModel.label);
        assertEquals(value, dynamicSelectModel.value);
    }
    
    @Test
    public void testDefaultConstructor() {
        dynamicSelectModel = new DynamicSelectModel();
        assertNotNull(dynamicSelectModel);
    }
    
    @Test
    public void testCollection() {
        dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.collection("collection");
        assertEquals("collection", dynamicSelectModel.collection.getExpression());
    }
    
    @Test
    public void testLabel() {
        dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.label("label");
        assertEquals("label", dynamicSelectModel.label.getExpression());
    }
    
    @Test
    public void testValue() {
        dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.value("value");
        assertEquals("value", dynamicSelectModel.value.getExpression());
    }
    
    @Test
    public void testEnumeration() {
        dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.enumeration("enumeration");
        assertEquals("enumeration.values()", dynamicSelectModel.collection.getExpression());
        assertEquals("toString()", dynamicSelectModel.label.getExpression());
        assertEquals("ordinal()", dynamicSelectModel.value.getExpression());
    }
    
    @Test
    public void testTranslator() {
        dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.translator("translator");
        assertEquals("translator", dynamicSelectModel.translator.getExpression());
    }
    
    @Test
    public void testEmpty() {
        dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.empty(emptyObject);
        assertEquals(emptyObject, dynamicSelectModel.emptyObject);
    }
    
    @Test
    public void testGetOptionCount() {
        dynamicSelectModel.objects = new ArrayList();
        int optionCount = dynamicSelectModel.getOptionCount();
        assertEquals(0, optionCount);
        
        dynamicSelectModel.objects.add(new Object());
        optionCount = dynamicSelectModel.getOptionCount();
        assertEquals(1, optionCount);
    }
    
    @Test
    public void testGetLabel() {
        Object object1 = new Object();
        Object object2 = new Object();
        dynamicSelectModel.objects = new ArrayList();
        dynamicSelectModel.objects.add(object1);
        dynamicSelectModel.objects.add(object2);
        dynamicSelectModel.component = component;
        
        dynamicSelectModel.label = label;
        String label1 = dynamicSelectModel.getLabel(0);
        assertEquals(object1.toString(), label1);
        
        dynamicSelectModel.label = null;
        try {
            dynamicSelectModel.getLabel(0);
            fail("Expected WheelException to be thrown");
        } catch (WheelException e) {
            assertEquals("Could not evaluate expression null to create a label for dynamic ISelectModel. Index was 0", e.getMessage());
        }
    }
    
    @Test
    public void testGetValue() {
        Object object1 = new Object();
        Object object2 = new Object();
        dynamicSelectModel.objects = new ArrayList();
        dynamicSelectModel.objects.add(object1);
        dynamicSelectModel.objects.add(object2);
        dynamicSelectModel.component = component;
        
        dynamicSelectModel.value = value;
        String value1 = dynamicSelectModel.getValue(0);
        assertEquals(object1.toString(), value1);
        
        dynamicSelectModel.value = null;
        try {
            dynamicSelectModel.getValue(0);
            fail("Expected WheelException to be thrown");
        } catch (WheelException e) {
            assertEquals("Could not evaluate expression null to create a value for dynamic ISelectModel. Index was 0", e.getMessage());
        }
    }
    
    @Test
    public void testTranslateValue() {
        Object object1 = new Object();
        Object object2 = new Object();
        dynamicSelectModel.objects = new ArrayList();
        dynamicSelectModel.objects.add(object1);
        dynamicSelectModel.objects.add(object2);
        dynamicSelectModel.translator = translator;
        dynamicSelectModel.component = component;
        
        dynamicSelectModel.value = value;
        dynamicSelectModel.value = null;
        Object translatedValue = dynamicSelectModel.translateValue("value1");
        assertNull(translatedValue);
        
        dynamicSelectModel.translator = null;
        translatedValue = dynamicSelectModel.translateValue("value1");
        assertEquals(object1, translatedValue);
    }
    
    @Test
    public void testGetObjects() {
        dynamicSelectModel.objects = null;
        dynamicSelectModel.collection = collection;
        dynamicSelectModel.topLevelComponent = topLevelComponent;
        dynamicSelectModel.component = component;
        
        try {
            dynamicSelectModel.getObjects();
            fail("Expected WheelException to be thrown");
        } catch (WheelException e) {
            assertEquals("Could not evaluate expression null to create a dynamic ISelectModel.", e.getMessage());
        }
        
        dynamicSelectModel.collection = new ElExpression("collection1");
        dynamicSelectModel.collection.eval(topLevelComponent, component);
        assertNull(dynamicSelectModel.objects);
        
        dynamicSelectModel.collection = new ElExpression("collection2");
        dynamicSelectModel.collection.eval(topLevelComponent, component);
        assertNull(dynamicSelectModel.objects);
        
        dynamicSelectModel.collection = new ElExpression("collection3");
        dynamicSelectModel.collection.eval(topLevelComponent, component);
        assertEquals(objects, dynamicSelectModel.objects);
        
        dynamicSelectModel.collection = new ElExpression("collection4");
        dynamicSelectModel.collection.eval(topLevelComponent, component);
        assertEquals(objects, dynamicSelectModel.objects);
        
        dynamicSelectModel.objects = new LinkedList();
        dynamicSelectModel.emptyObject = emptyObject;
        dynamicSelectModel.objects.add(new Object());
        dynamicSelectModel.getObjects();
        assertTrue(dynamicSelectModel.objects.contains(emptyObject));
        assertEquals(dynamicSelectModel.objects.size(), 2);
    }
    
    @Test
    public void testGetComponent() {
        dynamicSelectModel.component = component;
        Component result = dynamicSelectModel.getComponent();
        assertEquals(component, result);
    }
    
    @Test
    public void testSetComponent() {
        dynamicSelectModel.setComponent(component);
        assertEquals(component, dynamicSelectModel.component);
    }
    
    @Test
    public void testGetTopLevelComponent() {
        dynamicSelectModel.topLevelComponent = topLevelComponent;
        StandaloneComponent result = dynamicSelectModel.getTopLevelComponent();
        assertEquals(topLevelComponent, result);
    }
    
    @Test
    public void testSetTopLevelComponent() {
        dynamicSelectModel.setTopLevelComponent(topLevelComponent);
        assertEquals(topLevelComponent, dynamicSelectModel.topLevelComponent);
    }
    
    @Test
    public void testValidate() {
        dynamicSelectModel.collection = null;
        try {
            dynamicSelectModel.validate();
            fail("Expected WheelException to be thrown");
        } catch (WheelException e) {
            assertEquals("Could not create DynamicSelection model. Collection attribute was null or not an el-expression.", e.getMessage());
        }
        
        dynamicSelectModel.collection = collection;
        dynamicSelectModel.label = null;
        try {
            dynamicSelectModel.validate();
            fail("Expected WheelException to be thrown");
        } catch (WheelException e) {
            assertEquals("Could not create DynamicSelection model. Label attribute was null or not an el-expression.", e.getMessage());
        }
        
        dynamicSelectModel.label = label;
        dynamicSelectModel.value = null;
        try {
            dynamicSelectModel.validate();
            fail("Expected WheelException to be thrown");
        } catch (WheelException e) {
            assertEquals("Could not create DynamicSelection model. Value attribute was null or not an el-expression.", e.getMessage());
        }
    }
    
    @Test
    public void testReset() {
        dynamicSelectModel.objects = null;
        dynamicSelectModel.reset();
        assertNull(dynamicSelectModel.objects);
    }
}