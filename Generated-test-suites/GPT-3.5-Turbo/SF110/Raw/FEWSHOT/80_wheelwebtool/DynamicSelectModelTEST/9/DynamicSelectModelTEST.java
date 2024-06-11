package wheel.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Before;
import org.junit.Test;

import wheel.components.Component;
import wheel.components.StandaloneComponent;
import wheel.util.DynamicSelectModel;

public class DynamicSelectModelTest {
    
    private DynamicSelectModel selectModel;
    private Component component;
    private StandaloneComponent topLevelComponent;
    private ElExpression label;
    private ElExpression value;
    private ElExpression collection;
    private ElExpression translator;
    private Collection objects;
    private Object emptyObject;
    
    @Before
    public void setup() {
        component = new Component();
        topLevelComponent = new StandaloneComponent();
        label = new ElExpression("label");
        value = new ElExpression("value");
        collection = new ElExpression("collection");
        translator = new ElExpression("translator");
        objects = new ArrayList();
        emptyObject = new Object();
        
        selectModel = new DynamicSelectModel(collection, label, value);
        selectModel.setComponent(component);
        selectModel.setTopLevelComponent(topLevelComponent);
        selectModel.objects = objects;
        selectModel.emptyObject = emptyObject;
    }
    
    @Test
    public void testConstructor() {
        assertSame(collection, selectModel.collection);
        assertSame(label, selectModel.label);
        assertSame(value, selectModel.value);
        assertNull(selectModel.translator);
        assertNull(selectModel.objects);
        assertNull(selectModel.emptyObject);
    }
    
    @Test
    public void testCollection() {
        selectModel.collection("new collection");
        
        assertSame(collection, selectModel.collection);
    }
    
    @Test
    public void testLabel() {
        selectModel.label("new label");
        
        assertSame(label, selectModel.label);
    }
    
    @Test
    public void testValue() {
        selectModel.value("new value");
        
        assertSame(value, selectModel.value);
    }
    
    @Test
    public void testEnumeration() {
        selectModel.enumeration("enum");
        
        assertSame(collection, selectModel.collection);
        assertEquals("enum.values().toString()", label.getExpression());
        assertEquals("enum.values().ordinal()", value.getExpression());
    }
    
    @Test
    public void testTranslator() {
        selectModel.translator("new translator");
        
        assertSame(translator, selectModel.translator);
    }
    
    @Test
    public void testEmpty() {
        selectModel.empty(emptyObject);
        
        assertSame(emptyObject, selectModel.emptyObject);
    }
    
    @Test
    public void testGetOptionCount() {
        objects.add(new Object());
        objects.add(new Object());
        
        assertEquals(2, selectModel.getOptionCount());
    }
    
    @Test
    public void testGetLabel() {
        Object obj = new Object();
        objects.add(obj);
        
        assertEquals(obj.toString(), selectModel.getLabel(0));
    }
    
    @Test
    public void testGetValue() {
        Object obj = new Object();
        objects.add(obj);
        
        assertEquals(obj.toString(), selectModel.getValue(0));
    }
    
    @Test
    public void testTranslateValue() {
        Object obj = new Object();
        objects.add(obj);
        
        assertEquals(obj, selectModel.translateValue(obj.toString()));
    }
    
    @Test
    public void testGetObjects() {
        Collection expected = new ArrayList();
        expected.add(new Object());
        expected.add(new Object());
        
        selectModel.objects = expected;
        
        assertEquals(expected, selectModel.getObjects());
    }
    
    @Test(expected = WheelException.class)
    public void testGetObjectsNullExpression() {
        selectModel.getObjects();
    }
    
    @Test(expected = WheelException.class)
    public void testGetObjectsNullResult() {
        selectModel.collection.eval(topLevelComponent, component);
        selectModel.getObjects();
    }
    
    @Test(expected = WheelException.class)
    public void testGetObjectsInvalidResult() {
        selectModel.collection.eval(topLevelComponent, component);
        selectModel.objects = new Object[2];
        selectModel.getObjects();
    }
    
    @Test
    public void testGetComponent() {
        assertSame(component, selectModel.getComponent());
    }
    
    @Test
    public void testSetComponent() {
        Component newComponent = new Component();
        selectModel.setComponent(newComponent);
        
        assertSame(newComponent, selectModel.getComponent());
    }
    
    @Test
    public void testGetTopLevelComponent() {
        assertSame(topLevelComponent, selectModel.getTopLevelComponent());
    }
    
    @Test
    public void testSetTopLevelComponent() {
        StandaloneComponent newTopLevelComponent = new StandaloneComponent();
        selectModel.setTopLevelComponent(newTopLevelComponent);
        
        assertSame(newTopLevelComponent, selectModel.getTopLevelComponent());
    }
    
    @Test(expected = WheelException.class)
    public void testValidateNullCollection() {
        selectModel.collection = null;
        selectModel.validate();
    }
    
    @Test(expected = WheelException.class)
    public void testValidateNullLabel() {
        selectModel.label = null;
        selectModel.validate();
    }
    
    @Test(expected = WheelException.class)
    public void testValidateNullValue() {
        selectModel.value = null;
        selectModel.validate();
    }
    
    @Test
    public void testReset() {
        selectModel.reset();
        
        assertNull(selectModel.objects);
    }
}