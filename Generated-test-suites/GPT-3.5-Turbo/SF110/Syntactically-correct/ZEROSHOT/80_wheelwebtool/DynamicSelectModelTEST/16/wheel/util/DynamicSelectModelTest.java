import org.junit.Before;
import org.junit.Test;
import wheel.components.Component;
import wheel.StandaloneComponent;
import wheel.util.ElExpression;
import wheel.WheelException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import static org.junit.Assert.*;

public class DynamicSelectModelTest {

    private DynamicSelectModel dynamicSelectModel;

    @Before
    public void setUp() {
        dynamicSelectModel = new DynamicSelectModel();
    }

    @Test
    public void testCollection() {
        String collection = "test collection";
        dynamicSelectModel.collection(collection);
        assertEquals(collection, dynamicSelectModel.getCollection().getExpression());
    }

    @Test
    public void testLabel() {
        String label = "test label";
        dynamicSelectModel.label(label);
        assertEquals(label, dynamicSelectModel.getLabel().getExpression());
    }

    @Test
    public void testValue() {
        String value = "test value";
        dynamicSelectModel.value(value);
        assertEquals(value, dynamicSelectModel.getValue().getExpression());
    }

    @Test
    public void testEnumeration() {
        String enumeration = "test enumeration";
        dynamicSelectModel.enumeration(enumeration);
        assertEquals(enumeration + ".values()", dynamicSelectModel.getCollection().getExpression());
        assertEquals("toString()", dynamicSelectModel.getLabel().getExpression());
        assertEquals("ordinal()", dynamicSelectModel.getValue().getExpression());
    }

    @Test
    public void testTranslator() {
        String translator = "test translator";
        dynamicSelectModel.translator(translator);
        assertEquals(translator, dynamicSelectModel.getTranslator().getExpression());
    }

    @Test
    public void testEmpty() {
        Object o = new Object();
        dynamicSelectModel.empty(o);
        assertEquals(o, dynamicSelectModel.getEmptyObject());
    }

    @Test
    public void testGetOptionCount() {
        Collection<Object> objects = new ArrayList<>();
        objects.add(new Object());
        objects.add(new Object());
        objects.add(new Object());
        dynamicSelectModel.setObjects(objects);
        assertEquals(objects.size(), dynamicSelectModel.getOptionCount());
    }

    @Test
    public void testGetLabel() {
        Collection<Object> objects = new ArrayList<>();
        objects.add(new Object());
        objects.add(new Object());
        objects.add(new Object());
        dynamicSelectModel.setObjects(objects);
        dynamicSelectModel.setComponent(new Component());
        String label = "test label";
        dynamicSelectModel.label(label);
        for (int i = 0; i < objects.size(); i++) {
            assertEquals(label, dynamicSelectModel.getLabel(i));
        }
    }

    @Test
    public void testGetValue() {
        Collection<Object> objects = new ArrayList<>();
        objects.add(new Object());
        objects.add(new Object());
        objects.add(new Object());
        dynamicSelectModel.setObjects(objects);
        dynamicSelectModel.setComponent(new Component());
        String value = "test value";
        dynamicSelectModel.value(value);
        for (int i = 0; i < objects.size(); i++) {
            assertEquals(value, dynamicSelectModel.getValue(i));
        }
    }

    @Test
    public void testTranslateValue() {
        Collection<Object> objects = new ArrayList<>();
        objects.add(new Object());
        objects.add(new Object());
        objects.add(new Object());
        dynamicSelectModel.setObjects(objects);
        dynamicSelectModel.setComponent(new Component());
        dynamicSelectModel.setValue("value1");
        String value = "value1";
        assertEquals(objects.toArray()[0], dynamicSelectModel.translateValue(value));
        dynamicSelectModel.setValue("value2");
        value = "value2";
        assertEquals(objects.toArray()[1], dynamicSelectModel.translateValue(value));
    }

    @Test(expected = WheelException.class)
    public void testGetObjectsWithNullCollection() {
        dynamicSelectModel.getObjects();
    }

    @Test(expected = WheelException.class)
    public void testGetObjectsWithNullObject() {
        dynamicSelectModel.collection("test collection");
        dynamicSelectModel.setComponent(new Component());
        dynamicSelectModel.getObjects();
    }

    @Test(expected = WheelException.class)
    public void testGetObjectsWithInvalidObject() {
        dynamicSelectModel.collection("test collection");
        dynamicSelectModel.setComponent(new Component());
        dynamicSelectModel.getObjects();
    }

    @Test
    public void testGetObjectsWithEmptyObject() {
        Object emptyObject = new Object();
        Collection<Object> objects = new ArrayList<>();
        objects.add(new Object());
        objects.add(new Object());
        objects.add(new Object());
        dynamicSelectModel.setEmptyObject(emptyObject);
        dynamicSelectModel.setObjects(objects);
        Collection<Object> expectedObjects = new LinkedList<>();
        expectedObjects.add(emptyObject);
        expectedObjects.addAll(objects);
        assertEquals(expectedObjects, dynamicSelectModel.getObjects());
    }
}