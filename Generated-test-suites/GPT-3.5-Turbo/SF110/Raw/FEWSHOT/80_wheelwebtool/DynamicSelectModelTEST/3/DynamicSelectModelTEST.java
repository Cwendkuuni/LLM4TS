
package wheel.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import wheel.components.Component;
import wheel.components.StandaloneComponent;
import wheel.util.DynamicSelectModel;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Arrays;
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
        label = new ElExpression("");
        value = new ElExpression("");
        collection = new ElExpression("");
        translator = new ElExpression("");
        objects = new ArrayList();
        emptyObject = null;
        
        dynamicSelectModel.setComponent(component);
        dynamicSelectModel.setTopLevelComponent(topLevelComponent);
        dynamicSelectModel.label(label);
        dynamicSelectModel.value(value);
        dynamicSelectModel.collection(collection);
        dynamicSelectModel.translator(translator);
        dynamicSelectModel.empty(emptyObject);
    }
    
    @Test
    public void testGetOptionCount() {
        objects.add("option1");
        objects.add("option2");
        objects.add("option3");
        dynamicSelectModel.objects = objects;
        
        assertEquals(3, dynamicSelectModel.getOptionCount());
    }
    
    @Test
    public void testGetLabel() {
        objects.add("option1");
        objects.add("option2");
        objects.add("option3");
        dynamicSelectModel.objects = objects;
        
        assertEquals("option1", dynamicSelectModel.getLabel(0));
        assertEquals("option2", dynamicSelectModel.getLabel(1));
        assertEquals("option3", dynamicSelectModel.getLabel(2));
    }
    
    @Test
    public void testGetValue() {
        objects.add("value1");
        objects.add("value2");
        objects.add("value3");
        dynamicSelectModel.objects = objects;
        
        assertEquals("value1", dynamicSelectModel.getValue(0));
        assertEquals("value2", dynamicSelectModel.getValue(1));
        assertEquals("value3", dynamicSelectModel.getValue(2));
    }
    
    @Test
    public void testTranslateValue() {
        objects.add("value1");
        objects.add("value2");
        objects.add("value3");
        dynamicSelectModel.objects = objects;
        
        assertEquals("value1", dynamicSelectModel.translateValue("value1"));
        assertEquals("value2", dynamicSelectModel.translateValue("value2"));
        assertEquals("value3", dynamicSelectModel.translateValue("value3"));
    }
    
    @Test
    public void testGetObjects() {
        objects.add("option1");
        objects.add("option2");
        objects.add("option3");
        dynamicSelectModel.objects = objects;
        
        assertEquals(objects, dynamicSelectModel.getObjects());
    }
    
    @Test
    public void testGetObjectsWithNullCollection() {
        dynamicSelectModel.collection = null;
        
        assertNull(dynamicSelectModel.getObjects());
    }
    
    @Test(expected = WheelException.class)
    public void testGetObjectsWithNullExpression() {
        dynamicSelectModel.collection(null);
        
        dynamicSelectModel.getObjects();
    }
    
    @Test
    public void testSetComponent() {
        Component newComponent = new Component();
        dynamicSelectModel.setComponent(newComponent);
        
        assertEquals(newComponent, dynamicSelectModel.getComponent());
    }
    
    @Test
    public void testSetTopLevelComponent() {
        StandaloneComponent newTopLevelComponent = new StandaloneComponent();
        dynamicSelectModel.setTopLevelComponent(newTopLevelComponent);
        
        assertEquals(newTopLevelComponent, dynamicSelectModel.getTopLevelComponent());
    }
    
    @Test
    public void testValidate() {
        dynamicSelectModel.validate();
    }
    
    @Test(expected = WheelException.class)
    public void testValidateWithNullCollection() {
        dynamicSelectModel.collection = null;
        dynamicSelectModel.validate();
    }
    
    @Test(expected = WheelException.class)
    public void testValidateWithNullLabel() {
        dynamicSelectModel.label = null;
        dynamicSelectModel.validate();
    }
    
    @Test(expected = WheelException.class)
    public void testValidateWithNullValue() {
        dynamicSelectModel.value = null;
        dynamicSelectModel.validate();
    }
    
    @Test
    public void testReset() {
        dynamicSelectModel.objects = objects;
        dynamicSelectModel.reset();
        
        assertNull(dynamicSelectModel.objects);
    }
}
