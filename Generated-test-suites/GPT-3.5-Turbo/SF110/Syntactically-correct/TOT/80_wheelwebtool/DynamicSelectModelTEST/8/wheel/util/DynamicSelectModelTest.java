import org.junit.*;
import static org.junit.Assert.*;

public class DynamicSelectModelTest {
    
    @Test
    public void testConstructorWithCollectionLabelValue() {
        // Given
        ElExpression collection = new ElExpression("collection");
        ElExpression label = new ElExpression("label");
        ElExpression value = new ElExpression("value");
        
        // When
        DynamicSelectModel model = new DynamicSelectModel(collection, label, value);
        
        // Then
        assertNotNull(model);
        assertEquals(collection, model.getCollection());
        assertEquals(label, model.getLabel());
        assertEquals(value, model.getValue());
    }
    
    @Test
    public void testEmptyConstructor() {
        // Given
        
        // When
        DynamicSelectModel model = new DynamicSelectModel();
        
        // Then
        assertNotNull(model);
    }
    
    @Test
    public void testCollection() {
        // Given
        String collection = "collection";
        DynamicSelectModel model = new DynamicSelectModel();
        
        // When
        model.collection(collection);
        
        // Then
        assertEquals(collection, model.getCollection().getExpression());
    }
    
    @Test
    public void testLabel() {
        // Given
        String label = "label";
        DynamicSelectModel model = new DynamicSelectModel();
        
        // When
        model.label(label);
        
        // Then
        assertEquals(label, model.getLabel().getExpression());
    }
    
    @Test
    public void testValue() {
        // Given
        String value = "value";
        DynamicSelectModel model = new DynamicSelectModel();
        
        // When
        model.value(value);
        
        // Then
        assertEquals(value, model.getValue().getExpression());
    }
    
    @Test
    public void testEnumeration() {
        // Given
        String enumeration = "enumeration";
        DynamicSelectModel model = new DynamicSelectModel();
        
        // When
        model.enumeration(enumeration);
        
        // Then
        assertEquals(enumeration + ".values()", model.getCollection().getExpression());
        assertEquals("toString()", model.getLabel().getExpression());
        assertEquals("ordinal()", model.getValue().getExpression());
    }
    
    @Test
    public void testTranslator() {
        // Given
        String translator = "translator";
        DynamicSelectModel model = new DynamicSelectModel();
        
        // When
        model.translator(translator);
        
        // Then
        assertEquals(translator, model.getTranslator().getExpression());
    }
    
    @Test
    public void testEmpty() {
        // Given
        Object emptyObject = new Object();
        DynamicSelectModel model = new DynamicSelectModel();
        
        // When
        model.empty(emptyObject);
        
        // Then
        assertEquals(emptyObject, model.getEmptyObject());
    }
    
    @Test
    public void testGetOptionCount() {
        // Given
        DynamicSelectModel model = new DynamicSelectModel();
        model.setObjects(new ArrayList());
        model.getObjects().add(new Object());
        model.getObjects().add(new Object());
        
        // When
        int optionCount = model.getOptionCount();
        
        // Then
        assertEquals(2, optionCount);
    }
    
    @Test
    public void testGetLabel() {
        // Given
        DynamicSelectModel model = new DynamicSelectModel();
        ElExpression label = new ElExpression("label");
        model.setLabel(label);
        
        // When
        String actualLabel = model.getLabel(0);
        
        // Then
        assertEquals("label", actualLabel);
    }
    
    @Test(expected = WheelException.class)
    public void testGetLabelWithInvalidExpression() {
        // Given
        DynamicSelectModel model = new DynamicSelectModel();
        ElExpression label = new ElExpression("invalidExpression");
        model.setLabel(label);
        
        // When
        model.getLabel(0);
        
        // Then
        // WheelException is expected to be thrown
    }
    
    @Test
    public void testGetValue() {
        // Given
        DynamicSelectModel model = new DynamicSelectModel();
        ElExpression value = new ElExpression("value");
        model.setValue(value);
        
        // When
        String actualValue = model.getValue(0);
        
        // Then
        assertEquals("value", actualValue);
    }
    
    @Test(expected = WheelException.class)
    public void testGetValueWithInvalidExpression() {
        // Given
        DynamicSelectModel model = new DynamicSelectModel();
        ElExpression value = new ElExpression("invalidExpression");
        model.setValue(value);
        
        // When
        model.getValue(0);
        
        // Then
        // WheelException is expected to be thrown
    }
    
    @Test
    public void testTranslateValue() {
        // Given
        DynamicSelectModel model = new DynamicSelectModel();
        Object[] objects = new Object[2];
        objects[0] = "value1";
        objects[1] = "value2";
        model.setObjects(Arrays.asList(objects));
        ElExpression translator = new ElExpression("translator");
        model.setTranslator(translator);
        
        // When
        Object translatedValue = model.translateValue("value1");
        
        // Then
        assertEquals("translator", translatedValue);
    }
    
    @Test(expected = WheelException.class)
    public void testTranslateValueWithInvalidTranslatorExpression() {
        // Given
        DynamicSelectModel model = new DynamicSelectModel();
        Object[] objects = new Object[2];
        objects[0] = "value1";
        objects[1] = "value2";
        model.setObjects(Arrays.asList(objects));
        ElExpression translator = new ElExpression("invalidExpression");
        model.setTranslator(translator);
        
        // When
        model.translateValue("value1");
        
        // Then
        // WheelException is expected to be thrown
    }
    
    @Test
    public void testGetObjects() {
        // Given
        DynamicSelectModel model = new DynamicSelectModel();
        Object[] objects = new Object[2];
        objects[0] = "value1";
        objects[1] = "value2";
        model.setObjects(Arrays.asList(objects));
        
        // When
        Collection actualObjects = model.getObjects();
        
        // Then
        assertEquals(2, actualObjects.size());
    }
    
    @Test(expected = WheelException.class)
    public void testGetObjectsWithInvalidCollectionExpression() {
        // Given
        DynamicSelectModel model = new DynamicSelectModel();
        ElExpression collection = new ElExpression("invalidExpression");
        model.setCollection(collection);
        
        // When
        model.getObjects();
        
        // Then
        // WheelException is expected to be thrown
    }
    
    @Test(expected = WheelException.class)
    public void testGetObjectsWithNullCollection() {
        // Given
        DynamicSelectModel model = new DynamicSelectModel();
        ElExpression collection = null;
        model.setCollection(collection);
        
        // When
        model.getObjects();
        
        // Then
        // WheelException is expected to be thrown
    }
    
    @Test(expected = WheelException.class)
    public void testGetObjectsWithNullEvaluatedObject() {
        // Given
        DynamicSelectModel model = new DynamicSelectModel();
        ElExpression collection = new ElExpression("validExpression");
        model.setCollection(collection);
        
        // When
        model.getObjects();
        
        // Then
        // WheelException is expected to be thrown
    }
    
    @Test
    public void testGetComponent() {
        // Given
        DynamicSelectModel model = new DynamicSelectModel();
        Component component = new Component();
        model.setComponent(component);
        
        // When
        Component actualComponent = model.getComponent();
        
        // Then
        assertEquals(component, actualComponent);
    }
    
    @Test
    public void testSetComponent() {
        // Given
        DynamicSelectModel model = new DynamicSelectModel();
        Component component = new Component();
        
        // When
        model.setComponent(component);
        
        // Then
        assertEquals(component, model.getComponent());
    }
    
    @Test
    public void testGetTopLevelComponent() {
        // Given
        DynamicSelectModel model = new DynamicSelectModel();
        StandaloneComponent topLevelComponent = new StandaloneComponent();
        model.setTopLevelComponent(topLevelComponent);
        
        // When
        StandaloneComponent actualTopLevelComponent = model.getTopLevelComponent();
        
        // Then
        assertEquals(topLevelComponent, actualTopLevelComponent);
    }
    
    @Test
    public void testSetTopLevelComponent() {
        // Given
        DynamicSelectModel model = new DynamicSelectModel();
        StandaloneComponent topLevelComponent = new StandaloneComponent();
        
        // When
        model.setTopLevelComponent(topLevelComponent);
        
        // Then
        assertEquals(topLevelComponent, model.getTopLevelComponent());
    }
    
    @Test
    public void testValidate() {
        // Given
        DynamicSelectModel model = new DynamicSelectModel();
        model.setCollection(null);
        
        // When
        try {
            model.validate();
            fail("Expected WheelException");
        } catch(WheelException e) {
            // Then
            assertEquals("Could not create DynamicSelection model. Collection attribute was null or not an el-expression.", e.getMessage());
        }
        
    }
    
    @Test
    public void testReset() {
        // Given
        DynamicSelectModel model = new DynamicSelectModel();
        model.setObjects(new ArrayList());
        model.getObjects().add(new Object());
        
        // When
        model.reset();
        
        // Then
        assertNull(model.getObjects());
    }
}