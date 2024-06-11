import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import wheel.components.Component;
import wheel.components.StandaloneComponent;
import wheel.ElExpression;
import wheel.WheelException;

import java.util.Collection;
import java.util.LinkedList;
import java.util.ArrayList;

public class DynamicSelectModelTest {
    
    private DynamicSelectModel dynamicSelectModel;
    
    @Before
    public void setUp() {
        dynamicSelectModel = new DynamicSelectModel();
    }
    
    @Test
    public void testConstructorWithElExpressions() {
        ElExpression collection = new ElExpression("myCollection");
        ElExpression label = new ElExpression("myLabel");
        ElExpression value = new ElExpression("myValue");
        
        dynamicSelectModel = new DynamicSelectModel(collection, label, value);
        
        assertEquals(collection, dynamicSelectModel.collection);
        assertEquals(label, dynamicSelectModel.label);
        assertEquals(value, dynamicSelectModel.value);
    }
    
    @Test
    public void testCollection() {
        String collection = "myCollection";
        
        dynamicSelectModel.collection(collection);
        
        assertEquals(new ElExpression(collection), dynamicSelectModel.collection);
    }
    
    @Test
    public void testLabel() {
        String label = "myLabel";
        
        dynamicSelectModel.label(label);
        
        assertEquals(new ElExpression(label), dynamicSelectModel.label);
    }
    
    @Test
    public void testValue() {
        String value = "myValue";
        
        dynamicSelectModel.value(value);
        
        assertEquals(new ElExpression(value), dynamicSelectModel.value);
    }
    
    @Test
    public void testEnumeration() {
        String enumeration = "myEnumeration";
        
        dynamicSelectModel.enumeration(enumeration);
        
        assertEquals(new ElExpression(enumeration + ".values()"), dynamicSelectModel.collection);
        assertEquals(new ElExpression("toString()"), dynamicSelectModel.label);
        assertEquals(new ElExpression("ordinal()"), dynamicSelectModel.value);
    }
    
    @Test
    public void testTranslator() {
        String translator = "myTranslator";
        
        dynamicSelectModel.translator(translator);
        
        assertEquals(new ElExpression(translator), dynamicSelectModel.translator);
    }
    
    @Test
    public void testEmpty() {
        Object emptyObject = new Object();
        
        dynamicSelectModel.empty(emptyObject);
        
        assertEquals(emptyObject, dynamicSelectModel.emptyObject);
    }
    
    @Test
    public void testGetOptionCount() {
        Collection objects = new ArrayList();
        objects.add(new Object());
        objects.add(new Object());
        objects.add(new Object());
        
        dynamicSelectModel.objects = objects;
        
        assertEquals(3, dynamicSelectModel.getOptionCount());
    }
    
    @Test
    public void testGetLabel() {
        ElExpression label = new ElExpression("myLabel");
        Component component = new Component();
        
        dynamicSelectModel.label = label;
        dynamicSelectModel.component = component;
        
        assertEquals("label1", dynamicSelectModel.getLabel(0));
    }
    
    @Test
    public void testGetValue() {
        ElExpression value = new ElExpression("myValue");
        Component component = new Component();
        
        dynamicSelectModel.value = value;
        dynamicSelectModel.component = component;
        
        assertEquals("value1", dynamicSelectModel.getValue(0));
    }
    
    @Test
    public void testTranslateValue() {
        dynamicSelectModel.collection = new ElExpression("myCollection");
        dynamicSelectModel.label = new ElExpression("myLabel");
        dynamicSelectModel.value = new ElExpression("myValue");
        dynamicSelectModel.translator = new ElExpression("myTranslator");
        
        String value = "myValue";
        String expectedResult = "translatedValue";
        
        Object[] array = new Object[1];
        array[0] = new Object();
        dynamicSelectModel.objects = new ArrayList();
        dynamicSelectModel.objects.add(array[0]);
        
        assertEquals(expectedResult, dynamicSelectModel.translateValue(value));
    }
    
    @Test
    public void testGetObjectsWithCollection() {
        dynamicSelectModel.collection = new ElExpression("myCollection");
        dynamicSelectModel.topLevelComponent = new StandaloneComponent();
        dynamicSelectModel.component = new Component();
        
        Collection objects = new ArrayList();
        objects.add(new Object());
        dynamicSelectModel.objects = objects;
        
        assertEquals(objects, dynamicSelectModel.getObjects());
    }
    
    @Test
    public void testGetObjectsWithNullCollection() {
        dynamicSelectModel.topLevelComponent = new StandaloneComponent();
        dynamicSelectModel.component = new Component();
        
        dynamicSelectModel.collection = new ElExpression("nullCollection");
        
        try {
            dynamicSelectModel.getObjects();
            fail("Expected WheelException");
        } catch (WheelException e) {
            assertEquals("Could not evaluate expression nullCollection to create a dynamic ISelectModel.", e.getMessage());
        }
    }
    
    @Test
    public void testGetObjectsWithNullObject() {
        dynamicSelectModel.topLevelComponent = new StandaloneComponent();
        dynamicSelectModel.component = new Component();
        
        dynamicSelectModel.collection = new ElExpression("myCollection");
        
        try {
            dynamicSelectModel.getObjects();
            fail("Expected WheelException");
        } catch (WheelException e) {
            assertEquals("Expression myCollection evaluated to null. Can't build a dynamic ISelectModel", e.getMessage());
        }
    }
    
    @Test
    public void testGetObjectsWithInvalidObject() {
        dynamicSelectModel.topLevelComponent = new StandaloneComponent();
        dynamicSelectModel.component = new Component();
        
        dynamicSelectModel.collection = new ElExpression("myInvalidCollection");
        
        try {
            dynamicSelectModel.getObjects();
            fail("Expected WheelException");
        } catch (WheelException e) {
            assertEquals("Expression myInvalidCollection evaluated to an object that is not an instance of Collection or Object[]. Can't build a dynamic ISelectModel", e.getMessage());
        }
    }
    
    @Test
    public void testGetObjectsWithValidObject() {
        Object[] array = new Object[1];
        array[0] = new Object();
        dynamicSelectModel.collection = new ElExpression("myCollection");
        dynamicSelectModel.topLevelComponent = new StandaloneComponent();
        dynamicSelectModel.component = new Component();
        
        dynamicSelectModel.objects = new ArrayList();
        dynamicSelectModel.objects.add(array[0]);
        
        Collection expectedObjects = new LinkedList();
        expectedObjects.add(array[0]);
        
        assertEquals(expectedObjects, dynamicSelectModel.getObjects());
    }
    
    @Test
    public void testGetObjectsWithEmptyObject() {
        Object emptyObject = new Object();
        dynamicSelectModel.collection = new ElExpression("myCollection");
        dynamicSelectModel.topLevelComponent = new StandaloneComponent();
        dynamicSelectModel.component = new Component();
        dynamicSelectModel.emptyObject = emptyObject;
        
        Object[] array = new Object[1];
        array[0] = new Object();
        dynamicSelectModel.objects = new ArrayList();
        dynamicSelectModel.objects.add(array[0]);
        
        Collection expectedObjects = new LinkedList();
        expectedObjects.add(emptyObject);
        expectedObjects.add(array[0]);
        
        assertEquals(expectedObjects, dynamicSelectModel.getObjects());
    }
    
    @Test
    public void testGetComponent() {
        Component component = new Component();
        
        dynamicSelectModel.component = component;
        
        assertEquals(component, dynamicSelectModel.getComponent());
    }
    
    @Test
    public void testSetComponent() {
        Component component = new Component();
        
        dynamicSelectModel.setComponent(component);
        
        assertEquals(component, dynamicSelectModel.component);
    }
    
    @Test
    public void testGetTopLevelComponent() {
        StandaloneComponent topLevelComponent = new StandaloneComponent();
        
        dynamicSelectModel.topLevelComponent = topLevelComponent;
        
        assertEquals(topLevelComponent, dynamicSelectModel.getTopLevelComponent());
    }
    
    @Test
    public void testSetTopLevelComponent() {
        StandaloneComponent topLevelComponent = new StandaloneComponent();
        
        dynamicSelectModel.setTopLevelComponent(topLevelComponent);
        
        assertEquals(topLevelComponent, dynamicSelectModel.topLevelComponent);
    }
    
    @Test
    public void testValidateWithNullCollection() {
        dynamicSelectModel.label = new ElExpression("myLabel");
        dynamicSelectModel.value = new ElExpression("myValue");
        
        try {
            dynamicSelectModel.validate();
            fail("Expected WheelException");
        } catch (WheelException e) {
            assertEquals("Could not create DynamicSelection model. Collection attribute was null or not an el-expression.", e.getMessage());
        }
    }
    
    @Test
    public void testValidateWithNullLabel() {
        dynamicSelectModel.collection = new ElExpression("myCollection");
        dynamicSelectModel.value = new ElExpression("myValue");
        
        try {
            dynamicSelectModel.validate();
            fail("Expected WheelException");
        } catch (WheelException e) {
            assertEquals("Could not create DynamicSelection model. Label attribute was null or not an el-expression.", e.getMessage());
        }
    }
    
    @Test
    public void testValidateWithNullValue() {
        dynamicSelectModel.collection = new ElExpression("myCollection");
        dynamicSelectModel.label = new ElExpression("myLabel");
        
        try {
            dynamicSelectModel.validate();
            fail("Expected WheelException");
        } catch (WheelException e) {
            assertEquals("Could not create DynamicSelection model. Value attribute was null or not an el-expression.", e.getMessage());
        }
    }
    
    @Test
    public void testResetObjects() {
        dynamicSelectModel.objects = new ArrayList();
        
        dynamicSelectModel.reset();
        
        assertNull(dynamicSelectModel.objects);
    }
}