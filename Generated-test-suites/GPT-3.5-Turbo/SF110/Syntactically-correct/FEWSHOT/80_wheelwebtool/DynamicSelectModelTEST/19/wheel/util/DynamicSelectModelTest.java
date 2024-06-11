package wheel.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import wheel.ElExpression;
import wheel.WheelException;
import wheel.components.Component;
import wheel.components.StandaloneComponent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

public class DynamicSelectModelTest {
    private DynamicSelectModel dynamicSelectModel;

    @Before
    public void setUp() {
        dynamicSelectModel = new DynamicSelectModel();
    }

    @After
    public void tearDown() {
        dynamicSelectModel = null;
    }

    @Test
    public void testConstructorWithCollection() {
        ElExpression collection = new ElExpression("someCollection");
        ElExpression label = new ElExpression("someLabel");
        ElExpression value = new ElExpression("someValue");
        dynamicSelectModel = new DynamicSelectModel(collection, label, value);

        assertNotNull(dynamicSelectModel.getCollection());
        assertNotNull(dynamicSelectModel.getLabel());
        assertNotNull(dynamicSelectModel.getValue());
        assertNull(dynamicSelectModel.getTranslator());
        assertNull(dynamicSelectModel.getEmptyObject());
        assertNull(dynamicSelectModel.getObjects());
    }

    @Test
    public void testDefaultConstructor() {
        dynamicSelectModel = new DynamicSelectModel();

        assertNull(dynamicSelectModel.getCollection());
        assertNull(dynamicSelectModel.getLabel());
        assertNull(dynamicSelectModel.getValue());
        assertNull(dynamicSelectModel.getTranslator());
        assertNull(dynamicSelectModel.getEmptyObject());
        assertNull(dynamicSelectModel.getObjects());
    }

    @Test
    public void testCollection() {
        String collection = "someCollection";
        DynamicSelectModel result = dynamicSelectModel.collection(collection);

        assertEquals(collection, result.getCollection().getExpression());
    }

    @Test
    public void testLabel() {
        String label = "someLabel";
        DynamicSelectModel result = dynamicSelectModel.label(label);

        assertEquals(label, result.getLabel().getExpression());
    }

    @Test
    public void testValue() {
        String value = "someValue";
        DynamicSelectModel result = dynamicSelectModel.value(value);

        assertEquals(value, result.getValue().getExpression());
    }

    @Test
    public void testEnumeration() {
        String enumeration = "someEnumeration";
        dynamicSelectModel.enumeration(enumeration);

        assertEquals(enumeration + ".values()", dynamicSelectModel.getCollection().getExpression());
        assertEquals("toString()", dynamicSelectModel.getLabel().getExpression());
        assertEquals("ordinal()", dynamicSelectModel.getValue().getExpression());
    }

    @Test
    public void testTranslator() {
        String translator = "someTranslator";
        DynamicSelectModel result = dynamicSelectModel.translator(translator);

        assertEquals(translator, result.getTranslator().getExpression());
    }

    @Test
    public void testEmpty() {
        Object emptyObject = new Object();
        DynamicSelectModel result = dynamicSelectModel.empty(emptyObject);

        assertEquals(emptyObject, result.getEmptyObject());
    }

    @Test
    public void testGetOptionCount() {
        Collection<Object> objects = new ArrayList<>();
        objects.add("option1");
        objects.add("option2");
        objects.add("option3");
        dynamicSelectModel.setObjects(objects);

        int count = dynamicSelectModel.getOptionCount();

        assertEquals(3, count);
    }

    @Test
    public void testGetLabel() {
        Collection<Object> objects = new ArrayList<>();
        objects.add("option1");
        objects.add("option2");
        objects.add("option3");
        dynamicSelectModel.setObjects(objects);
        dynamicSelectModel.setComponent(new Component());
        dynamicSelectModel.setTopLevelComponent(new StandaloneComponent());

        String label1 = dynamicSelectModel.getLabel(0);
        String label2 = dynamicSelectModel.getLabel(1);
        String label3 = dynamicSelectModel.getLabel(2);

        assertEquals("option1", label1);
        assertEquals("option2", label2);
        assertEquals("option3", label3);
    }

    @Test
    public void testGetLabelWithException() {
        Collection<Object> objects = new ArrayList<>();
        objects.add("option1");
        objects.add("option2");
        objects.add("option3");
        dynamicSelectModel.setObjects(objects);
        dynamicSelectModel.setComponent(new Component());
        dynamicSelectModel.setTopLevelComponent(new StandaloneComponent());
        dynamicSelectModel.setLabel(null);

        try {
            dynamicSelectModel.getLabel(0);
        } catch (WheelException e) {
            assertEquals("Could not create DynamicSelection model. Label attribute was null or not an el-expression.", e.getMessage());
            assertNotNull(e.getComponent());
        }
    }

    @Test
    public void testGetValue() {
        Collection<Object> objects = new ArrayList<>();
        objects.add("value1");
        objects.add("value2");
        objects.add("value3");
        dynamicSelectModel.setObjects(objects);
        dynamicSelectModel.setComponent(new Component());
        dynamicSelectModel.setTopLevelComponent(new StandaloneComponent());

        String value1 = dynamicSelectModel.getValue(0);
        String value2 = dynamicSelectModel.getValue(1);
        String value3 = dynamicSelectModel.getValue(2);

        assertEquals("value1", value1);
        assertEquals("value2", value2);
        assertEquals("value3", value3);
    }

    @Test
    public void testGetValueWithException() {
        Collection<Object> objects = new ArrayList<>();
        objects.add("value1");
        objects.add("value2");
        objects.add("value3");
        dynamicSelectModel.setObjects(objects);
        dynamicSelectModel.setComponent(new Component());
        dynamicSelectModel.setTopLevelComponent(new StandaloneComponent());
        dynamicSelectModel.setValue(null);

        try {
            dynamicSelectModel.getValue(0);
        } catch (WheelException e) {
            assertEquals("Could not create DynamicSelection model. Value attribute was null or not an el-expression.", e.getMessage());
            assertNotNull(e.getComponent());
        }
    }

    @Test
    public void testTranslateValue() {
        Collection<Object> objects = new ArrayList<>();
        objects.add("option1");
        objects.add("option2");
        objects.add("option3");
        dynamicSelectModel.setObjects(objects);
        dynamicSelectModel.setValue("value2");

        Object result = dynamicSelectModel.translateValue("value2");

        assertEquals("option2", result);
    }

    @Test
    public void testTranslateValueWithTranslator() {
        Collection<Object> objects = new ArrayList<>();
        objects.add("option1");
        objects.add("option2");
        objects.add("option3");
        dynamicSelectModel.setObjects(objects);
        dynamicSelectModel.setValue("value2");
        dynamicSelectModel.setTranslator(new ElExpression("someTranslator"));

        Object result = dynamicSelectModel.translateValue("value2");

        assertEquals("translatedOption2", result);
    }

    @Test
    public void testTranslateValueReturnsNull() {
        Collection<Object> objects = new ArrayList<>();
        objects.add("option1");
        objects.add("option2");
        objects.add("option3");
        dynamicSelectModel.setObjects(objects);
        dynamicSelectModel.setValue("value4");

        Object result = dynamicSelectModel.translateValue("value4");

        assertNull(result);
    }

    @Test
    public void testGetObjectsWhenObjectsIsNotNull() {
        Collection<Object> objects = new ArrayList<>();
        objects.add("object1");
        objects.add("object2");
        objects.add("object3");
        dynamicSelectModel.setObjects(objects);

        Collection<Object> result = dynamicSelectModel.getObjects();

        assertEquals(objects, result);
    }

    @Test
    public void testGetObjectsWhenObjectsIsNull() {
        dynamicSelectModel.setCollection(new ElExpression("someCollection"));
        dynamicSelectModel.setTopLevelComponent(new StandaloneComponent());
        dynamicSelectModel.setComponent(new Component());

        Collection<Object> result = dynamicSelectModel.getObjects();

        assertTrue(result instanceof LinkedList);
        assertEquals(0, result.size());
    }

    @Test
    public void testGetObjectsWithException() {
        dynamicSelectModel.setCollection(null);

        try {
            dynamicSelectModel.getObjects();
        } catch (WheelException e) {
            assertEquals("Could not create DynamicSelection model. Collection attribute was null or not an el-expression.", e.getMessage());
            assertNull(e.getComponent());
        }
    }

    @Test
    public void testSetAndGetComponent() {
        Component component = new Component();
        dynamicSelectModel.setComponent(component);

        Component result = dynamicSelectModel.getComponent();

        assertEquals(component, result);
    }

    @Test
    public void testSetAndGetTopLevelComponent() {
        StandaloneComponent topLevelComponent = new StandaloneComponent();
        dynamicSelectModel.setTopLevelComponent(topLevelComponent);

        StandaloneComponent result = dynamicSelectModel.getTopLevelComponent();

        assertEquals(topLevelComponent, result);
    }

    @Test
    public void testValidateWithNullCollection() {
        dynamicSelectModel.setLabel(new ElExpression("someLabel"));
        dynamicSelectModel.setValue(new ElExpression("someValue"));

        try {
            dynamicSelectModel.validate();
        } catch (WheelException e) {
            assertEquals("Could not create DynamicSelection model. Collection attribute was null or not an el-expression.", e.getMessage());
            assertNull(e.getComponent());
        }
    }

    @Test
    public void testValidateWithNullLabel() {
        dynamicSelectModel.setCollection(new ElExpression("someCollection"));
        dynamicSelectModel.setValue(new ElExpression("someValue"));

        try {
            dynamicSelectModel.validate();
        } catch (WheelException e) {
            assertEquals("Could not create DynamicSelection model. Label attribute was null or not an el-expression.", e.getMessage());
            assertNull(e.getComponent());
        }
    }

    @Test
    public void testValidateWithNullValue() {
        dynamicSelectModel.setCollection(new ElExpression("someCollection"));
        dynamicSelectModel.setLabel(new ElExpression("someLabel"));

        try {
            dynamicSelectModel.validate();
        } catch (WheelException e) {
            assertEquals("Could not create DynamicSelection model. Value attribute was null or not an el-expression.", e.getMessage());
            assertNull(e.getComponent());
        }
    }

    @Test
    public void testReset() {
        dynamicSelectModel.setObjects(new ArrayList<>());

        dynamicSelectModel.reset();

        assertNull(dynamicSelectModel.getObjects());
    }
}