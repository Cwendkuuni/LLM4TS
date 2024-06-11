import org.junit.Test;
import static org.junit.Assert.*;

public class DynamicSelectModelTest {

    @Test
    public void testConstructor() {
        ElExpression collection = new ElExpression("collection");
        ElExpression label = new ElExpression("label");
        ElExpression value = new ElExpression("value");
        
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel(collection, label, value);
        
        assertEquals(collection, dynamicSelectModel.collection);
        assertEquals(label, dynamicSelectModel.label);
        assertEquals(value, dynamicSelectModel.value);
    }
    
    @Test
    public void testDefaultConstructor() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        
        assertNull(dynamicSelectModel.collection);
        assertNull(dynamicSelectModel.label);
        assertNull(dynamicSelectModel.value);
    }
    
    @Test
    public void testCollection() {
        String collection = "collection";
        
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.collection(collection);
        
        assertEquals(new ElExpression(collection), dynamicSelectModel.collection);
    }
    
    @Test
    public void testLabel() {
        String label = "label";
        
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.label(label);
        
        assertEquals(new ElExpression(label), dynamicSelectModel.label);
    }
    
    @Test
    public void testValue() {
        String value = "value";
        
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.value(value);
        
        assertEquals(new ElExpression(value), dynamicSelectModel.value);
    }
    
    @Test
    public void testEnumeration() {
        String enumeration = "enumeration";
        
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.enumeration(enumeration);
        
        assertEquals(new ElExpression(enumeration + ".values()"), dynamicSelectModel.collection);
        assertEquals(new ElExpression("toString()"), dynamicSelectModel.label);
        assertEquals(new ElExpression("ordinal()"), dynamicSelectModel.value);
    }
    
    @Test
    public void testTranslator() {
        String translator = "translator";
        
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.translator(translator);
        
        assertEquals(new ElExpression(translator), dynamicSelectModel.translator);
    }
    
    @Test
    public void testEmpty() {
        Object o = new Object();
        
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.empty(o);
        
        assertEquals(o, dynamicSelectModel.emptyObject);
    }
    
    @Test
    public void testGetOptionCount() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.objects = new ArrayList();
        dynamicSelectModel.objects.add(new Object());
        dynamicSelectModel.objects.add(new Object());
        
        assertEquals(2, dynamicSelectModel.getOptionCount());
    }
    
    @Test
    public void testGetLabel() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.label = new ElExpression("label");
        dynamicSelectModel.component = new Component();
        dynamicSelectModel.objects = new ArrayList();
        dynamicSelectModel.objects.add(new Object() {
            public String toString() {
                return "Test Label";
            }
        });
        
        assertEquals("Test Label", dynamicSelectModel.getLabel(0));
    }
    
    @Test
    public void testGetValue() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.value = new ElExpression("value");
        dynamicSelectModel.component = new Component();
        dynamicSelectModel.objects = new ArrayList();
        dynamicSelectModel.objects.add(new Object() {
            public String toString() {
                return "Test Value";
            }
        });
        
        assertEquals("Test Value", dynamicSelectModel.getValue(0));
    }
    
    @Test
    public void testTranslateValue() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.objects = new ArrayList();
        dynamicSelectModel.objects.add(new Object() {
            public String toString() {
                return "Test Value";
            }
        });
        
        assertEquals(dynamicSelectModel.objects.get(0), dynamicSelectModel.translateValue("Test Value"));
        assertNull(dynamicSelectModel.translateValue("Invalid Value"));
    }
    
    @Test
    public void testGetObjects() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.collection = new ElExpression("collection");
        dynamicSelectModel.topLevelComponent = new StandaloneComponent();
        dynamicSelectModel.component = new Component();
        dynamicSelectModel.objects = new ArrayList();
        dynamicSelectModel.objects.add(new Object());
        
        assertEquals(dynamicSelectModel.objects, dynamicSelectModel.getObjects());
    }
    
    @Test
    public void testGetObjectsWithEmptyObject() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.collection = new ElExpression("collection");
        dynamicSelectModel.topLevelComponent = new StandaloneComponent();
        dynamicSelectModel.component = new Component();
        dynamicSelectModel.objects = new ArrayList();
        dynamicSelectModel.objects.add(new Object());
        dynamicSelectModel.emptyObject = new Object();
        dynamicSelectModel.objects = new LinkedList();
        dynamicSelectModel.objects.add(dynamicSelectModel.emptyObject);
        dynamicSelectModel.objects.addAll(dynamicSelectModel.objects);
        
        assertEquals(dynamicSelectModel.objects, dynamicSelectModel.getObjects());
    }
    
    @Test
    public void testGetComponent() {
        Component component = new Component();
        
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.component = component;
        
        assertEquals(component, dynamicSelectModel.getComponent());
    }
    
    @Test
    public void testSetComponent() {
        Component component = new Component();
        
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.setComponent(component);
        
        assertEquals(component, dynamicSelectModel.component);
    }
    
    @Test
    public void testGetTopLevelComponent() {
        StandaloneComponent topLevelComponent = new StandaloneComponent();
        
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.topLevelComponent = topLevelComponent;
        
        assertEquals(topLevelComponent, dynamicSelectModel.getTopLevelComponent());
    }
    
    @Test
    public void testSetTopLevelComponent() {
        StandaloneComponent topLevelComponent = new StandaloneComponent();
        
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.setTopLevelComponent(topLevelComponent);
        
        assertEquals(topLevelComponent, dynamicSelectModel.topLevelComponent);
    }
    
    @Test(expected = WheelException.class)
    public void testValidateWithNullCollection() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.label = new ElExpression("label");
        dynamicSelectModel.value = new ElExpression("value");
        
        dynamicSelectModel.validate();
    }
    
    @Test(expected = WheelException.class)
    public void testValidateWithNullLabel() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.collection = new ElExpression("collection");
        dynamicSelectModel.value = new ElExpression("value");
        
        dynamicSelectModel.validate();
    }
    
    @Test(expected = WheelException.class)
    public void testValidateWithNullValue() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.collection = new ElExpression("collection");
        dynamicSelectModel.label = new ElExpression("label");
        
        dynamicSelectModel.validate();
    }
    
    @Test
    public void testReset() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.objects = new ArrayList();
        
        dynamicSelectModel.reset();
        
        assertNull(dynamicSelectModel.objects);
    }
}