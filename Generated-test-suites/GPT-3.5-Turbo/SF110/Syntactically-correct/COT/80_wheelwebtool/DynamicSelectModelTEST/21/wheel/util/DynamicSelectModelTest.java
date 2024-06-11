
import org.junit.Before;
import org.junit.Test;
import wheel.components.Component;
import wheel.components.StandaloneComponent;
import wheel.exceptions.WheelException;
import wheel.util.DynamicSelectModel;
import wheel.util.ElExpression;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import static org.junit.Assert.*;

public class DynamicSelectModelTest {
    private DynamicSelectModel dynamicSelectModel;
    private ElExpression collection;
    private ElExpression label;
    private ElExpression value;
    private Component component;
    private StandaloneComponent topLevelComponent;

    @Before
    public void setUp() {
        dynamicSelectModel = new DynamicSelectModel();
        collection = new ElExpression("collection");
        label = new ElExpression("label");
        value = new ElExpression("value");
        component = new Component();
        topLevelComponent = new StandaloneComponent();
    }

    @Test
    public void testConstructorWithParams() {
        dynamicSelectModel = new DynamicSelectModel(collection, label, value);
        assertEquals(collection, dynamicSelectModel.getCollection());
        assertEquals(label, dynamicSelectModel.getLabel());
        assertEquals(value, dynamicSelectModel.getValue());
    }

    @Test
    public void testDefaultConstructor() {
        dynamicSelectModel = new DynamicSelectModel();
        assertNull(dynamicSelectModel.getCollection());
        assertNull(dynamicSelectModel.getLabel());
        assertNull(dynamicSelectModel.getValue());
    }

    @Test
    public void testCollection() {
        String collectionValue = "TestCollection";
        dynamicSelectModel.collection(collectionValue);
        assertEquals(collectionValue, dynamicSelectModel.getCollection().getExpression());
    }

    @Test
    public void testLabel() {
        String labelValue = "TestLabel";
        dynamicSelectModel.label(labelValue);
        assertEquals(labelValue, dynamicSelectModel.getLabel().getExpression());
    }

    @Test
    public void testValue() {
        String valueValue = "TestValue";
        dynamicSelectModel.value(valueValue);
        assertEquals(valueValue, dynamicSelectModel.getValue().getExpression());
    }

    @Test
    public void testEnumeration() {
        String enumerationValue = "TestEnumeration";
        dynamicSelectModel.enumeration(enumerationValue);
        assertEquals(enumerationValue + ".values()", dynamicSelectModel.getCollection().getExpression());
        assertEquals("toString()", dynamicSelectModel.getLabel().getExpression());
        assertEquals("ordinal()", dynamicSelectModel.getValue().getExpression());
    }

    @Test
    public void testTranslator() {
        String translatorValue = "TestTranslator";
        dynamicSelectModel.translator(translatorValue);
        assertEquals(translatorValue, dynamicSelectModel.getTranslator().getExpression());
    }

    @Test
    public void testEmpty() {
        Object emptyObject = new Object();
        dynamicSelectModel.empty(emptyObject);
        assertEquals(emptyObject, dynamicSelectModel.getEmptyObject());
    }

    @Test
    public void testGetOptionCount() {
        Collection<Object> objects = new ArrayList<>();
        objects.add("Option 1");
        objects.add("Option 2");
        dynamicSelectModel.setObjects(objects);
        assertEquals(2, dynamicSelectModel.getOptionCount());
    }

    @Test
    public void testGetLabel() {
        Collection<Object> objects = new ArrayList<>();
        objects.add("Option 1");
        objects.add("Option 2");
        dynamicSelectModel.setObjects(objects);
        dynamicSelectModel.setComponent(component);
        assertEquals("Option 1", dynamicSelectModel.getLabel(0));
    }

    @Test(expected = WheelException.class)
    public void testGetLabelWithInvalidIndex() {
        Collection<Object> objects = new ArrayList<>();
        objects.add("Option 1");
        objects.add("Option 2");
        dynamicSelectModel.setObjects(objects);
        dynamicSelectModel.setComponent(component);
        dynamicSelectModel.getLabel(5);
    }

    @Test
    public void testGetValue() {
        Collection<Object> objects = new ArrayList<>();
        objects.add("Option 1");
        objects.add("Option 2");
        dynamicSelectModel.setObjects(objects);
        dynamicSelectModel.setComponent(component);
        assertEquals("Option 1", dynamicSelectModel.getValue(0));
    }

    @Test(expected = WheelException.class)
    public void testGetValueWithInvalidIndex() {
        Collection<Object> objects = new ArrayList<>();
        objects.add("Option 1");
        objects.add("Option 2");
        dynamicSelectModel.setObjects(objects);
        dynamicSelectModel.setComponent(component);
        dynamicSelectModel.getValue(5);
    }

    @Test
    public void testTranslateValue() {
        Collection<Object> objects = new ArrayList<>();
        objects.add("Option 1");
        objects.add("Option 2");
        dynamicSelectModel.setObjects(objects);
        dynamicSelectModel.setComponent(component);
        assertEquals("Option 1", dynamicSelectModel.translateValue("Option 1"));
    }

    @Test
    public void testTranslateValueWithTranslator() {
        Collection<Object> objects = new ArrayList<>();
        objects.add("Option 1");
        objects.add("Option 2");
        dynamicSelectModel.setObjects(objects);
        dynamicSelectModel.setComponent(component);
        String translatorValue = "TestTranslator";
        dynamicSelectModel.translator(translatorValue);
        assertEquals(translatorValue, dynamicSelectModel.translateValue("Option 1"));
    }

    @Test(expected = WheelException.class)
    public void testTranslateValueWithInvalidValue() {
        Collection<Object> objects = new ArrayList<>();
        objects.add("Option 1");
        objects.add("Option 2");
        dynamicSelectModel.setObjects(objects);
        dynamicSelectModel.setComponent(component);
        dynamicSelectModel.translateValue("Option 3");
    }

    @Test
    public void testGetObjects() {
        Collection<Object> objects = new ArrayList<>();
        objects.add("Option 1");
        objects.add("Option 2");
        dynamicSelectModel.setObjects(objects);
        assertEquals(objects, dynamicSelectModel.getObjects());
    }

    @Test(expected = WheelException.class)
    public void testGetObjectsWithNullCollection() {
        dynamicSelectModel.setTopLevelComponent(topLevelComponent);
        dynamicSelectModel.getObjects();
    }
    
    @Test(expected = WheelException.class)
    public void testGetObjectsWithNullCollectionAndNullTopLevelComponent() {
        dynamicSelectModel.getObjects();
    }

    @Test(expected = WheelException.class)
    public void testGetObjectsWithNullExpression() {
        dynamicSelectModel.setTopLevelComponent(topLevelComponent);
        dynamicSelectModel.collection(null);
        dynamicSelectModel.getObjects();
    }

    @Test(expected = WheelException.class)
    public void testGetObjectsWithNullExpressionAndNullTopLevelComponent() {
        dynamicSelectModel.collection(null);
        dynamicSelectModel.getObjects();
    }

    @Test(expected = WheelException.class)
    public void testGetObjectsWithNullEvaluationResult() {
        dynamicSelectModel.setTopLevelComponent(topLevelComponent);
        dynamicSelectModel.collection("nonExistingCollection");
        dynamicSelectModel.getObjects();
    }

    @Test(expected = WheelException.class)
    public void testGetObjectsWithNullEvaluationResultAndNullTopLevelComponent() {
        dynamicSelectModel.collection("nonExistingCollection");
        dynamicSelectModel.getObjects();
    }

    @Test(expected = WheelException.class)
    public void testGetObjectsWithInvalidEvaluationResult() {
        dynamicSelectModel.setTopLevelComponent(topLevelComponent);
        dynamicSelectModel.collection("invalidCollection");
        dynamicSelectModel.getObjects();
    }

    @Test(expected = WheelException.class)
    public void testGetObjectsWithInvalidEvaluationResultAndNullTopLevelComponent() {
        dynamicSelectModel.collection("invalidCollection");
        dynamicSelectModel.getObjects();
    }

    @Test
    public void testGetComponent() {
        dynamicSelectModel.setComponent(component);
        assertEquals(component, dynamicSelectModel.getComponent());
    }

    @Test
    public void testSetComponent() {
        dynamicSelectModel.setComponent(component);
        assertEquals(component, dynamicSelectModel.getComponent());
    }

    @Test
    public void testGetTopLevelComponent() {
        dynamicSelectModel.setTopLevelComponent(topLevelComponent);
        assertEquals(topLevelComponent, dynamicSelectModel.getTopLevelComponent());
    }

    @Test
    public void testSetTopLevelComponent() {
        dynamicSelectModel.setTopLevelComponent(topLevelComponent);
        assertEquals(topLevelComponent, dynamicSelectModel.getTopLevelComponent());
    }

    @Test(expected = WheelException.class)
    public void testValidateWithNullCollection() {
        dynamicSelectModel.validate();
    }

    @Test(expected = WheelException.class)
    public void testValidateWithNullLabel() {
        dynamicSelectModel.collection("collection");
        dynamicSelectModel.validate();
    }

    @Test(expected = WheelException.class)
    public void testValidateWithNullValue() {
        dynamicSelectModel.collection("collection");
        dynamicSelectModel.label("label");
        dynamicSelectModel.validate();
    }

    @Test
    public void testReset() {
        Collection<Object> objects = new LinkedList<>();
        objects.add("Option 1");
        objects.add("Option 2");
        dynamicSelectModel.setObjects(objects);
        dynamicSelectModel.reset();
        assertNull(dynamicSelectModel.getObjects());
    }
}

