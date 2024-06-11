import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DynamicSelectModelTest {

    private DynamicSelectModel dynamicSelectModel;

    @Before
    public void setUp() {
        dynamicSelectModel = new DynamicSelectModel();
    }

    @Test
    public void testConstructor() {
        DynamicSelectModel model = new DynamicSelectModel(new ElExpression("collection"), new ElExpression("label"), new ElExpression("value"));
        
        assertNotNull(model);
        assertNotNull(model.getCollection());
        assertNotNull(model.getLabel());
        assertNotNull(model.getValue());
    }

    @Test
    public void testCollection() {
        DynamicSelectModel model = dynamicSelectModel.collection("collection");
        
        assertNotNull(model);
        assertNotNull(model.getCollection());
    }

    @Test
    public void testLabel() {
        DynamicSelectModel model = dynamicSelectModel.label("label");
        
        assertNotNull(model);
        assertNotNull(model.getLabel());
    }

    @Test
    public void testValue() {
        DynamicSelectModel model = dynamicSelectModel.value("value");
        
        assertNotNull(model);
        assertNotNull(model.getValue());
    }

    @Test
    public void testEnumeration() {
        DynamicSelectModel model = dynamicSelectModel.enumeration("enumeration");
        
        assertNotNull(model);
        assertNotNull(model.getCollection());
        assertNotNull(model.getLabel());
        assertNotNull(model.getValue());
    }

    @Test
    public void testTranslator() {
        DynamicSelectModel model = dynamicSelectModel.translator("translator");
        
        assertNotNull(model);
        assertNotNull(model.getTranslator());
    }

    @Test
    public void testEmpty() {
        Object emptyObject = new Object();
        DynamicSelectModel model = dynamicSelectModel.empty(emptyObject);
        
        assertNotNull(model);
        assertEquals(emptyObject, model.getEmptyObject());
    }

    @Test
    public void testGetOptionCount() {
        DynamicSelectModel model = new DynamicSelectModel(new ElExpression("collection"), new ElExpression("label"), new ElExpression("value"));
        model.setComponent(new Component());
        model.getObjects().add(new Object());
        model.getObjects().add(new Object());
        model.getObjects().add(new Object());
        int expectedCount = 3;
        
        int actualCount = model.getOptionCount();
        
        assertEquals(expectedCount, actualCount);
    }

    @Test
    public void testGetLabelSuccess() {
        DynamicSelectModel model = new DynamicSelectModel(new ElExpression("collection"), new ElExpression("label"), new ElExpression("value"));
        model.setComponent(new Component());
        model.getObjects().add("Label");
        int index = 0;
        String expectedLabel = "Label";
        
        String actualLabel = model.getLabel(index);
        
        assertEquals(expectedLabel, actualLabel);
    }

    @Test(expected = RuntimeException.class)
    public void testGetLabelFailure() {
        DynamicSelectModel model = new DynamicSelectModel(new ElExpression("collection"), new ElExpression("label"), new ElExpression("value"));
        model.setComponent(new Component());
        model.getObjects().add(new Object());
        int index = 1;
        
        model.getLabel(index);
    }

    @Test
    public void testGetValueSuccess() {
        DynamicSelectModel model = new DynamicSelectModel(new ElExpression("collection"), new ElExpression("label"), new ElExpression("value"));
        model.setComponent(new Component());
        model.getObjects().add("Value");
        int index = 0;
        String expectedValue = "Value";
        
        String actualValue = model.getValue(index);
        
        assertEquals(expectedValue, actualValue);
    }

    @Test(expected = RuntimeException.class)
    public void testGetValueFailure() {
        DynamicSelectModel model = new DynamicSelectModel(new ElExpression("collection"), new ElExpression("label"), new ElExpression("value"));
        model.setComponent(new Component());
        model.getObjects().add(new Object());
        int index = 1;
        
        model.getValue(index);
    }

    @Test
    public void testTranslateValueWithTranslatorSuccess() {
        DynamicSelectModel model = new DynamicSelectModel(new ElExpression("collection"), new ElExpression("label"), new ElExpression("value"));
        model.setTranslator(new ElExpression("translator"));
        model.setComponent(new Component());
        model.getObjects().add("Value");
        String value = "Value";
        String expectedTranslatedValue = "Translated Value";
        
        Object actualTranslatedValue = model.translateValue(value);
        
        assertEquals(expectedTranslatedValue, actualTranslatedValue);
    }

    @Test
    public void testTranslateValueWithoutTranslatorSuccess() {
        DynamicSelectModel model = new DynamicSelectModel(new ElExpression("collection"), new ElExpression("label"), new ElExpression("value"));
        model.setComponent(new Component());
        model.getObjects().add("Value");
        String value = "Value";
        String expectedTranslatedValue = "Value";
        
        Object actualTranslatedValue = model.translateValue(value);
        
        assertEquals(expectedTranslatedValue, actualTranslatedValue);
    }

    @Test
    public void testTranslateValueNotFound() {
        DynamicSelectModel model = new DynamicSelectModel(new ElExpression("collection"), new ElExpression("label"), new ElExpression("value"));
        model.setComponent(new Component());
        model.getObjects().add("Value");
        String value = "Other Value";
        
        Object actualTranslatedValue = model.translateValue(value);
        
        assertNull(actualTranslatedValue);
    }

    @Test
    public void testGetObjectsEmptyCollection() {
        DynamicSelectModel model = new DynamicSelectModel(new ElExpression("collection"), new ElExpression("label"), new ElExpression("value"));
        model.setComponent(new Component());
        model.getObjects().clear();
        
        assertTrue(model.getObjects().isEmpty());
    }

    @Test
    public void testGetObjectsWithCollection() {
        DynamicSelectModel model = new DynamicSelectModel(new ElExpression("collection"), new ElExpression("label"), new ElExpression("value"));
        model.setComponent(new Component());
        model.getObjects().add(new Object());
        model.getObjects().add(new Object());
        model.getObjects().add(new Object());
        
        assertFalse(model.getObjects().isEmpty());
        assertEquals(3, model.getObjects().size());
    }

    @Test(expected = WheelException.class)
    public void testGetObjectsWithNullCollection() {
        DynamicSelectModel model = new DynamicSelectModel(new ElExpression(null), new ElExpression("label"), new ElExpression("value"));
        model.setComponent(new Component());
        
        model.getObjects();
    }

    @Test(expected = WheelException.class)
    public void testGetObjectsWithInvalidCollection() {
        DynamicSelectModel model = new DynamicSelectModel(new ElExpression("Invalid collection"), new ElExpression("label"), new ElExpression("value"));
        model.setComponent(new Component());
        
        model.getObjects();
    }

    @Test(expected = WheelException.class)
    public void testGetObjectsWithNullExpression() {
        DynamicSelectModel model = new DynamicSelectModel(new ElExpression(null), new ElExpression("label"), new ElExpression("value"));
        model.setComponent(new Component());
        
        model.getObjects();
    }

    @Test(expected = WheelException.class)
    public void testGetObjectsWithInvalidExpression() {
        DynamicSelectModel model = new DynamicSelectModel(new ElExpression("Invalid expression"), new ElExpression("label"), new ElExpression("value"));
        model.setComponent(new Component());
        
        model.getObjects();
    }

    @Test
    public void testGetObjectsWithArray() {
        Object[] array = new Object[3];
        DynamicSelectModel model = new DynamicSelectModel(new ElExpression(array), new ElExpression("label"), new ElExpression("value"));
        model.setComponent(new Component());
        
        assertFalse(model.getObjects().isEmpty());
        assertEquals(3, model.getObjects().size());
    }

    @Test
    public void testGetObjectsWithEmptyObject() {
        DynamicSelectModel model = new DynamicSelectModel(new ElExpression("collection"), new ElExpression("label"), new ElExpression("value"));
        model.setComponent(new Component());
        model.empty(new Object());
        
        assertFalse(model.getObjects().isEmpty());
        assertEquals(1, model.getObjects().size());
    }

    @Test
    public void testGetObjectsWithEmptyObjectAndCollection() {
        DynamicSelectModel model = new DynamicSelectModel(new ElExpression("collection"), new ElExpression("label"), new ElExpression("value"));
        model.setComponent(new Component());
        model.getObjects().add(new Object());
        model.empty(new Object());
        
        assertFalse(model.getObjects().isEmpty());
        assertEquals(2, model.getObjects().size());
    }

    @Test
    public void testGetObjectsWithEmptyObjectAndEmptyCollection() {
        DynamicSelectModel model = new DynamicSelectModel(new ElExpression("collection"), new ElExpression("label"), new ElExpression("value"));
        model.setComponent(new Component());
        model.getObjects().clear();
        model.empty(new Object());
        
        assertFalse(model.getObjects().isEmpty());
        assertEquals(1, model.getObjects().size());
    }

    @Test
    public void testGetObjectsWithNullEmptyObject() {
        DynamicSelectModel model = new DynamicSelectModel(new ElExpression("collection"), new ElExpression("label"), new ElExpression("value"));
        model.setComponent(new Component());
        model.empty(null);
        
        assertTrue(model.getObjects().isEmpty());
    }

    @Test
    public void testGetObjectsWithNullEmptyObjectAndCollection() {
        DynamicSelectModel model = new DynamicSelectModel(new ElExpression("collection"), new ElExpression("label"), new ElExpression("value"));
        model.setComponent(new Component());
        model.getObjects().add(new Object());
        model.empty(null);
        
        assertFalse(model.getObjects().isEmpty());
        assertEquals(1, model.getObjects().size());
    }

    @Test
    public void testGetObjectsWithNullEmptyObjectAndEmptyCollection() {
        DynamicSelectModel model = new DynamicSelectModel(new ElExpression("collection"), new ElExpression("label"), new ElExpression("value"));
        model.setComponent(new Component());
        model.getObjects().clear();
        model.empty(null);
        
        assertFalse(model.getObjects().isEmpty());
        assertEquals(1, model.getObjects().size());
    }

    @Test
    public void testGetObjectsWithNullEmptyObjectAndExistingCollection() {
        DynamicSelectModel model = new DynamicSelectModel(new ElExpression("collection"), new ElExpression("label"), new ElExpression("value"));
        model.setComponent(new Component());
        model.getObjects().add(new Object());
        int existingCollectionSize = model.getObjects().size();
        model.empty(null);
        
        assertFalse(model.getObjects().isEmpty());
        assertEquals(existingCollectionSize, model.getObjects().size());
    }

    @Test
    public void testGetObjectsWithExistingEmptyObjectAndEmptyCollection() {
        DynamicSelectModel model = new DynamicSelectModel(new ElExpression("collection"), new ElExpression("label"), new ElExpression("value"));
        model.setComponent(new Component());
        model.getObjects().clear();
        model.empty(new Object());
        model.empty(new Object());
        
        assertFalse(model.getObjects().isEmpty());
        assertEquals(2, model.getObjects().size());
    }

    @Test
    public void testGetComponent() {
        Component component = new Component();
        
        dynamicSelectModel.setComponent(component);
        
        assertEquals(component, dynamicSelectModel.getComponent());
    }

    @Test
    public void testSetComponent() {
        Component component = new Component();
        
        dynamicSelectModel.setComponent(component);
        
        assertEquals(component, dynamicSelectModel.getComponent());
    }

    @Test
    public void testGetTopLevelComponent() {
        StandaloneComponent topLevelComponent = new StandaloneComponent();
        
        dynamicSelectModel.setTopLevelComponent(topLevelComponent);
        
        assertEquals(topLevelComponent, dynamicSelectModel.getTopLevelComponent());
    }

    @Test
    public void testSetTopLevelComponent() {
        StandaloneComponent topLevelComponent = new StandaloneComponent();
        
        dynamicSelectModel.setTopLevelComponent(topLevelComponent);
        
        assertEquals(topLevelComponent, dynamicSelectModel.getTopLevelComponent());
    }

    @Test(expected = WheelException.class)
    public void testValidateWithNullCollection() {
        DynamicSelectModel model = new DynamicSelectModel();
        model.setCollection(null);
        
        model.validate();
    }

    @Test(expected = WheelException.class)
    public void testValidateWithNullLabel() {
        DynamicSelectModel model = new DynamicSelectModel();
        model.setLabel(null);
        
        model.validate();
    }

    @Test(expected = WheelException.class)
    public void testValidateWithNullValue() {
        DynamicSelectModel model = new DynamicSelectModel();
        model.setValue(null);
        
        model.validate();
    }

    @Test
    public void testReset() {
        DynamicSelectModel model = new DynamicSelectModel();
        model.getObjects().add(new Object());
        model.getObjects().add(new Object());
        model.getObjects().add(new Object());
        
        model.reset();
        
        assertTrue(model.getObjects().isEmpty());
    }
}