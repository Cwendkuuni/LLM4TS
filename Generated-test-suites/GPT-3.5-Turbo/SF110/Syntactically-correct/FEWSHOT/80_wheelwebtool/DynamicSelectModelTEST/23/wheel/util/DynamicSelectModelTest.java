
package wheel.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class DynamicSelectModelTest {
    
    private DynamicSelectModel dynamicSelectModel;
    
    @Before
    public void setUp() {
        dynamicSelectModel = new DynamicSelectModel();
    }
    
    @Test
    public void testGetOptionCountNoObjects() {
        dynamicSelectModel.setObjects(null);
        assertEquals(0, dynamicSelectModel.getOptionCount());
    }
    
    @Test
    public void testGetOptionCountWithObjects() {
        dynamicSelectModel.setObjects(new ArrayList());
        assertEquals(0, dynamicSelectModel.getOptionCount());
        
        List<Object> objects = new ArrayList<>();
        objects.add(new Object());
        objects.add(new Object());
        dynamicSelectModel.setObjects(objects);
        assertEquals(2, dynamicSelectModel.getOptionCount());
    }
    
    @Test
    public void testGetLabel() {
        dynamicSelectModel.setObjects(Arrays.asList("Label1", "Label2", "Label3"));
        dynamicSelectModel.setComponent(new Component());
        
        assertEquals("Label1", dynamicSelectModel.getLabel(0));
        assertEquals("Label2", dynamicSelectModel.getLabel(1));
        assertEquals("Label3", dynamicSelectModel.getLabel(2));
    }
    
    @Test(expected = NullPointerException.class)
    public void testGetLabelNullComponent() {
        dynamicSelectModel.setObjects(Arrays.asList("Label1", "Label2", "Label3"));
        dynamicSelectModel.setComponent(null);
        
        dynamicSelectModel.getLabel(0);
    }
    
    @Test
    public void testGetValue() {
        dynamicSelectModel.setObjects(Arrays.asList("Value1", "Value2", "Value3"));
        dynamicSelectModel.setComponent(new Component());
        
        assertEquals("Value1", dynamicSelectModel.getValue(0));
        assertEquals("Value2", dynamicSelectModel.getValue(1));
        assertEquals("Value3", dynamicSelectModel.getValue(2));
    }
    
    @Test(expected = NullPointerException.class)
    public void testGetValueNullComponent() {
        dynamicSelectModel.setObjects(Arrays.asList("Value1", "Value2", "Value3"));
        dynamicSelectModel.setComponent(null);
        
        dynamicSelectModel.getValue(0);
    }
    
    @Test
    public void testTranslateValue() {
        dynamicSelectModel.setObjects(Arrays.asList("Value1", "Value2", "Value3"));
        dynamicSelectModel.setValue(new ElExpression("value"));
        dynamicSelectModel.setTranslator(new ElExpression("translator"));
        dynamicSelectModel.setComponent(new Component());
        
        assertEquals("TranslatedValue", dynamicSelectModel.translateValue("Value2"));
    }
    
    @Test
    public void testTranslateValueNoTranslator() {
        dynamicSelectModel.setObjects(Arrays.asList("Value1", "Value2", "Value3"));
        dynamicSelectModel.setValue(new ElExpression("value"));
        dynamicSelectModel.setTranslator(null);
        dynamicSelectModel.setComponent(new Component());
        
        assertEquals("Value2", dynamicSelectModel.translateValue("Value2"));
    }
    
    @Test
    public void testTranslateValueNoMatch() {
        dynamicSelectModel.setObjects(Arrays.asList("Value1", "Value2", "Value3"));
        dynamicSelectModel.setValue(new ElExpression("value"));
        dynamicSelectModel.setTranslator(new ElExpression("translator"));
        dynamicSelectModel.setComponent(new Component());
        
        assertNull(dynamicSelectModel.translateValue("Value4"));
    }
    
    @Test
    public void testGetObjects() {
        Collection objects = Arrays.asList("Object1", "Object2", "Object3");
        dynamicSelectModel.setCollection(new ElExpression("collection"));
        dynamicSelectModel.setComponent(new Component());
        dynamicSelectModel.setTopLevelComponent(new StandaloneComponent());
        
        dynamicSelectModel.setObjects(objects);
        
        assertEquals(objects, dynamicSelectModel.getObjects());
    }
    
    @Test(expected = WheelException.class)
    public void testGetObjectsNullCollection() {
        dynamicSelectModel.setComponent(new Component());
        dynamicSelectModel.setTopLevelComponent(new StandaloneComponent());
        
        dynamicSelectModel.getObjects();
    }
    
    @Test(expected = WheelException.class)
    public void testGetObjectsCollectionEvaluatedToNull() {
        dynamicSelectModel.setCollection(new ElExpression("null"));
        dynamicSelectModel.setComponent(new Component());
        dynamicSelectModel.setTopLevelComponent(new StandaloneComponent());
        
        dynamicSelectModel.getObjects();
    }
    
    @Test(expected = WheelException.class)
    public void testGetObjectsInvalidObjectType() {
        Object obj = new Object();
        dynamicSelectModel.setCollection(new ElExpression("collection"));
        dynamicSelectModel.setComponent(new Component());
        dynamicSelectModel.setTopLevelComponent(new StandaloneComponent());
        
        dynamicSelectModel.setObjects(obj);
    }
    
    @Test
    public void testEmpty() {
        dynamicSelectModel.empty("emptyValue");
        assertEquals("emptyValue", dynamicSelectModel.getEmptyObject());
    }
    
    @Test
    public void testSetComponent() {
        Component component = new Component();
        dynamicSelectModel.setComponent(component);
        
        assertEquals(component, dynamicSelectModel.getComponent());
    }
    
    @Test
    public void testSetTopLevelComponent() {
        StandaloneComponent topLevelComponent = new StandaloneComponent();
        dynamicSelectModel.setTopLevelComponent(topLevelComponent);
        
        assertEquals(topLevelComponent, dynamicSelectModel.getTopLevelComponent());
    }
    
    @Test(expected = WheelException.class)
    public void testValidateNullCollection() {
        DynamicSelectModel model = new DynamicSelectModel();
        model.label("Label");
        model.value("Value");
        
        model.validate();
    }
    
    @Test(expected = WheelException.class)
    public void testValidateNullLabel() {
        DynamicSelectModel model = new DynamicSelectModel();
        model.collection("Collection");
        model.value("Value");
        
        model.validate();
    }
    
    @Test(expected = WheelException.class)
    public void testValidateNullValue() {
        DynamicSelectModel model = new DynamicSelectModel();
        model.collection("Collection");
        model.label("Label");
        
        model.validate();
    }
    
    @Test
    public void testReset() {
        dynamicSelectModel.setObjects(Arrays.asList("Object1", "Object2", "Object3"));
        
        dynamicSelectModel.reset();
        
        assertNull(dynamicSelectModel.getObjects());
    }
}
