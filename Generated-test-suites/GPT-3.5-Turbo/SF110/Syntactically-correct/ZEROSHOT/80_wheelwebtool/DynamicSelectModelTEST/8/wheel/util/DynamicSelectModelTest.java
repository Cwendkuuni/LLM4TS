package wheel.util;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import wheel.components.*;
import java.util.*;

public class DynamicSelectModelTest {

    private DynamicSelectModel dynamicSelectModel;
    private Component component;
    private StandaloneComponent topLevelComponent;
    private ElExpression collection;
    private ElExpression label;
    private ElExpression value;
    private ElExpression translator;
    private Collection objects;
    private Object emptyObject;
    
    @Before
    public void setUp() {
        dynamicSelectModel = new DynamicSelectModel();
        component = new Component();
        topLevelComponent = new StandaloneComponent();
        collection = new ElExpression("collection");
        label = new ElExpression("label");
        value = new ElExpression("value");
        translator = new ElExpression("translator");
        objects = new ArrayList();
        emptyObject = new Object();
        
        dynamicSelectModel.setComponent(component);
        dynamicSelectModel.setTopLevelComponent(topLevelComponent);
    }
    
    @Test
    public void testCollection() {
        DynamicSelectModel result = dynamicSelectModel.collection("collection");
        assertEquals(collection, result.collection);
    }
    
    @Test
    public void testLabel() {
        DynamicSelectModel result = dynamicSelectModel.label("label");
        assertEquals(label, result.label);
    }
    
    @Test
    public void testValue() {
        DynamicSelectModel result = dynamicSelectModel.value("value");
        assertEquals(value, result.value);
    }
    
    @Test
    public void testEnumeration() {
        DynamicSelectModel result = dynamicSelectModel.enumeration("enumeration");
        ElExpression expectedCollection = new ElExpression("enumeration.values()");
        ElExpression expectedLabel = new ElExpression("toString()");
        ElExpression expectedValue = new ElExpression("ordinal()");
        assertEquals(expectedCollection, result.collection);
        assertEquals(expectedLabel, result.label);
        assertEquals(expectedValue, result.value);
    }
    
    @Test
    public void testTranslator() {
        DynamicSelectModel result = dynamicSelectModel.translator("translator");
        assertEquals(translator, result.translator);
    }
    
    @Test
    public void testEmpty() {
        DynamicSelectModel result = dynamicSelectModel.empty(emptyObject);
        assertEquals(emptyObject, result.emptyObject);
    }
    
    @Test
    public void testGetOptionCount() {
        objects.add("option1");
        objects.add("option2");
        dynamicSelectModel.objects = objects;
        
        int expected = 2;
        int result = dynamicSelectModel.getOptionCount();
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testGetLabel() {
        objects.add("option1");
        dynamicSelectModel.objects = objects;
        dynamicSelectModel.label = label;
        dynamicSelectModel.component = component;
        
        String expected = "option1";
        String result = dynamicSelectModel.getLabel(0);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testGetValue() {
        objects.add("option1");
        dynamicSelectModel.objects = objects;
        dynamicSelectModel.value = value;
        dynamicSelectModel.component = component;
        
        String expected = "option1";
        String result = dynamicSelectModel.getValue(0);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testTranslateValue() {
        objects.add("option1");
        dynamicSelectModel.objects = objects;
        dynamicSelectModel.value = value;
        dynamicSelectModel.translator = translator;
        dynamicSelectModel.component = component;
        
        String input = "option1";
        Object expected = "translatedValue";
        
        // Mock translator's eval method to return expected value
        translator.eval = (obj, cmp) -> {
            if (obj.equals(input) && cmp.equals(component)) {
                return expected;
            } else {
                return null;
            }
        };
        
        Object result = dynamicSelectModel.translateValue(input);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testGetObjects() {
        Collection expected = new ArrayList();
        
        dynamicSelectModel.collection = collection;
        dynamicSelectModel.topLevelComponent = topLevelComponent;
        dynamicSelectModel.component = component;
        
        // Mock collection's eval method to return expected collection
        collection.eval = (cmp1, cmp2) -> {
            if (cmp1.equals(topLevelComponent) && cmp2.equals(component)) {
                return expected;
            } else {
                return null;
            }
        };
        
        Collection result = dynamicSelectModel.getObjects();
        
        assertEquals(expected, result);
    }
    
    @Test(expected = WheelException.class)
    public void testGetObjects_NullCollection() {
        dynamicSelectModel.collection = null;
        dynamicSelectModel.getObjects();
    }
    
    @Test(expected = WheelException.class)
    public void testGetObjects_NullEvalResult() {
        dynamicSelectModel.collection = collection;
        dynamicSelectModel.topLevelComponent = topLevelComponent;
        dynamicSelectModel.component = component;
        
        collection.eval = (cmp1, cmp2) -> null;
        
        dynamicSelectModel.getObjects();
    }
    
    @Test(expected = WheelException.class)
    public void testGetObjects_InvalidEvalResult() {
        dynamicSelectModel.collection = collection;
        dynamicSelectModel.topLevelComponent = topLevelComponent;
        dynamicSelectModel.component = component;
        
        collection.eval = (cmp1, cmp2) -> new Object();
        
        dynamicSelectModel.getObjects();
    }
    
    @Test
    public void testReset() {
        dynamicSelectModel.objects = objects;
        
        dynamicSelectModel.reset();
        
        assertNull(dynamicSelectModel.objects);
    }
    
    @Test
    public void testGetComponent() {
        Component result = dynamicSelectModel.getComponent();
        
        assertEquals(component, result);
    }
    
    @Test
    public void testSetComponent() {
        Component newComponent = new Component();
        
        dynamicSelectModel.setComponent(newComponent);
        
        assertEquals(newComponent, dynamicSelectModel.getComponent());
    }
    
    @Test
    public void testGetTopLevelComponent() {
        StandaloneComponent result = dynamicSelectModel.getTopLevelComponent();
        
        assertEquals(topLevelComponent, result);
    }
    
    @Test
    public void testSetTopLevelComponent() {
        StandaloneComponent newTopLevelComponent = new StandaloneComponent();
        
        dynamicSelectModel.setTopLevelComponent(newTopLevelComponent);
        
        assertEquals(newTopLevelComponent, dynamicSelectModel.getTopLevelComponent());
    }
    
    @Test(expected = WheelException.class)
    public void testValidate_NullCollection() {
        dynamicSelectModel.validate();
    }
    
    @Test(expected = WheelException.class)
    public void testValidate_NullLabel() {
        dynamicSelectModel.collection = collection;
        
        dynamicSelectModel.validate();
    }
    
    @Test(expected = WheelException.class)
    public void testValidate_NullValue() {
        dynamicSelectModel.collection = collection;
        dynamicSelectModel.label = label;
        
        dynamicSelectModel.validate();
    }
}