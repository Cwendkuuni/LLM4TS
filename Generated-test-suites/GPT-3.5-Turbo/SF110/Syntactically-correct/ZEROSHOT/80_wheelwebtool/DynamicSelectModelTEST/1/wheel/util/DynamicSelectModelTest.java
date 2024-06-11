import org.junit.Test;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import static org.junit.Assert.*;

public class DynamicSelectModelTest {
    
    @Test
    public void testGetOptionCount() {
        DynamicSelectModel model = new DynamicSelectModel();
        model.setTopLevelComponent(new StandaloneComponent());
        model.setComponent(new Component());
        model.collection("collection.expression");
        Collection<Object> objects = new ArrayList<>();
        objects.add(new Object());
        objects.add(new Object());
        objects.add(new Object());
        model.getObjects().addAll(objects);
        
        int optionCount = model.getOptionCount();
        
        assertEquals(3, optionCount);
    }
    
    @Test
    public void testGetLabel() {
        DynamicSelectModel model = new DynamicSelectModel();
        model.setTopLevelComponent(new StandaloneComponent());
        model.setComponent(new Component());
        model.label("label.expression");
        Collection<Object> objects = new ArrayList<>();
        objects.add("Label 1");
        objects.add("Label 2");
        objects.add("Label 3");
        model.getObjects().addAll(objects);
        
        String label = model.getLabel(1);
        
        assertEquals("Label 2", label);
    }
    
    @Test
    public void testGetValue() {
        DynamicSelectModel model = new DynamicSelectModel();
        model.setTopLevelComponent(new StandaloneComponent());
        model.setComponent(new Component());
        model.value("value.expression");
        Collection<Object> objects = new ArrayList<>();
        objects.add("Value 1");
        objects.add("Value 2");
        objects.add("Value 3");
        model.getObjects().addAll(objects);
        
        String value = model.getValue(2);
        
        assertEquals("Value 3", value);
    }
    
    @Test
    public void testTranslateValue() {
        DynamicSelectModel model = new DynamicSelectModel();
        model.setTopLevelComponent(new StandaloneComponent());
        model.setComponent(new Component());
        Collection<Object> objects = new ArrayList<>();
        objects.add("Value 1");
        objects.add("Value 2");
        objects.add("Value 3");
        model.getObjects().addAll(objects);
        
        Object translatedValue = model.translateValue("Value 2");
        
        assertEquals("Value 2", translatedValue);
    }
    
    @Test
    public void testGetObjects() {
        DynamicSelectModel model = new DynamicSelectModel();
        model.setTopLevelComponent(new StandaloneComponent());
        model.setComponent(new Component());
        Collection<Object> objects = new ArrayList<>();
        objects.add("Object 1");
        objects.add("Object 2");
        objects.add("Object 3");
        model.getObjects().addAll(objects);
        
        Collection<Object> actualObjects = model.getObjects();
        
        assertEquals(objects, actualObjects);
    }
    
    @Test
    public void testGetObjectsFromExpression() {
        DynamicSelectModel model = new DynamicSelectModel();
        model.setTopLevelComponent(new StandaloneComponent());
        model.setComponent(new Component());
        model.collection("collection.expression");
        
        Collection<Object> objects = new ArrayList<>();
        objects.add("Object 1");
        objects.add("Object 2");
        objects.add("Object 3");
        model.getObjects().addAll(objects);
        
        Collection<Object> actualObjects = model.getObjects();
        
        assertEquals(objects, actualObjects);
    }
    
    @Test
    public void testGetObjectsFromArrayExpression() {
        DynamicSelectModel model = new DynamicSelectModel();
        model.setTopLevelComponent(new StandaloneComponent());
        model.setComponent(new Component());
        
        String[] objectsArray = new String[] {"Object 1", "Object 2", "Object 3"};
        Collection<Object> objects = Arrays.asList(objectsArray);
        
        model.getObjects().addAll(objects);
        
        Collection<Object> actualObjects = model.getObjects();
        
        assertEquals(objects, actualObjects);
    }
    
    @Test
    public void testGetObjectsFromArray() {
        DynamicSelectModel model = new DynamicSelectModel();
        model.setTopLevelComponent(new StandaloneComponent());
        model.setComponent(new Component());
        
        Object[] objectsArray = new Object[] {new Object(), new Object(), new Object()};
        Collection<Object> objects = new ArrayList<>();
        objects.addAll(Arrays.asList(objectsArray));
        
        model.getObjects().addAll(objects);
        
        Collection<Object> actualObjects = model.getObjects();
        
        assertEquals(objects, actualObjects);
    }
    
    @Test
    public void testGetObjectsFromEmptyExpression() {
        DynamicSelectModel model = new DynamicSelectModel();
        model.setTopLevelComponent(new StandaloneComponent());
        model.setComponent(new Component());
        
        Collection<Object> actualObjects = model.getObjects();
        
        assertNull(actualObjects);
    }
    
    @Test
    public void testGetObjectsFromNullExpression() {
        DynamicSelectModel model = new DynamicSelectModel();
        model.setTopLevelComponent(new StandaloneComponent());
        model.setComponent(new Component());
        model.collection(null);
        
        try {
            model.getObjects();
            fail("Expected WheelException to be thrown");
        } catch (WheelException e) {
            assertEquals("Could not evaluate expression null to create a dynamic ISelectModel.", e.getMessage());
        }
    }
    
    @Test
    public void testEmptyCollection() {
        DynamicSelectModel model = new DynamicSelectModel();
        model.setTopLevelComponent(new StandaloneComponent());
        model.setComponent(new Component());
        Object emptyObject = new Object();
        model.empty(emptyObject);
        
        Collection<Object> objects = new ArrayList<>();
        objects.add("Object 1");
        objects.add("Object 2");
        objects.add("Object 3");
        model.getObjects().addAll(objects);
        
        Collection<Object> expectedObjects = new LinkedList<>();
        expectedObjects.add(emptyObject);
        expectedObjects.addAll(objects);
        
        Collection<Object> actualObjects = model.getObjects();
        
        assertEquals(expectedObjects, actualObjects);
    }
    
    @Test
    public void testGetComponent() {
        Component component = new Component();
        DynamicSelectModel model = new DynamicSelectModel();
        model.setComponent(component);
        
        Component actualComponent = model.getComponent();
        
        assertEquals(component, actualComponent);
    }
    
    @Test
    public void testGetComponent() {
        StandaloneComponent topLevelComponent = new StandaloneComponent();
        DynamicSelectModel model = new DynamicSelectModel();
        model.setTopLevelComponent(topLevelComponent);
        
        StandaloneComponent actualTopLevelComponent = model.getTopLevelComponent();
        
        assertEquals(topLevelComponent, actualTopLevelComponent);
    }
    
    @Test
    public void testValidateCollectionNull() {
        DynamicSelectModel model = new DynamicSelectModel();
        model.setTopLevelComponent(new StandaloneComponent());
        model.setComponent(new Component());
        model.label("label.expression");
        model.value("value.expression");
        
        try {
            model.validate();
            fail("Expected WheelException to be thrown");
        } catch (WheelException e) {
            assertEquals("Could not create DynamicSelection model. Collection attribute was null or not an el-expression.", e.getMessage());
        }
    }
    
    @Test
    public void testValidateLabelNull() {
        DynamicSelectModel model = new DynamicSelectModel();
        model.setTopLevelComponent(new StandaloneComponent());
        model.setComponent(new Component());
        model.collection("collection.expression");
        model.value("value.expression");
        
        try {
            model.validate();
            fail("Expected WheelException to be thrown");
        } catch (WheelException e) {
            assertEquals("Could not create DynamicSelection model. Label attribute was null or not an el-expression.", e.getMessage());
        }
    }
    
    @Test
    public void testValidateValueNull() {
        DynamicSelectModel model = new DynamicSelectModel();
        model.setTopLevelComponent(new StandaloneComponent());
        model.setComponent(new Component());
        model.collection("collection.expression");
        model.label("label.expression");
        
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
        model.setTopLevelComponent(new StandaloneComponent());
        model.setComponent(new Component());
        Collection<Object> objects = new ArrayList<>();
        objects.add(new Object());
        objects.add(new Object());
        objects.add(new Object());
        model.getObjects().addAll(objects);
        
        model.reset();
        
        assertNull(model.getObjects());
    }
    
}