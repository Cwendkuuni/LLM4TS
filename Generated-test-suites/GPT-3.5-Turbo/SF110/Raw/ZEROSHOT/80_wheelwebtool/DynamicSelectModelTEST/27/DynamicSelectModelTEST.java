package wheel.util;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import wheel.components.Component;
import wheel.components.StandaloneComponent;
import wheel.ElExpression;
import wheel.WheelException;
import java.util.Collection;
import java.util.ArrayList;
import java.util.LinkedList;

public class DynamicSelectModelTest {

    private DynamicSelectModel dynamicSelectModel;

    @Before
    public void setup() {
        dynamicSelectModel = new DynamicSelectModel();
    }

    @Test
    public void testCollection() {
        String collection = "testCollection";
        DynamicSelectModel result = dynamicSelectModel.collection(collection);
        assertEquals(collection, dynamicSelectModel.getCollection().getExpression());
        assertEquals(dynamicSelectModel, result);
    }

    @Test
    public void testLabel() {
        String label = "testLabel";
        DynamicSelectModel result = dynamicSelectModel.label(label);
        assertEquals(label, dynamicSelectModel.getLabel().getExpression());
        assertEquals(dynamicSelectModel, result);
    }

    @Test
    public void testValue() {
        String value = "testValue";
        DynamicSelectModel result = dynamicSelectModel.value(value);
        assertEquals(value, dynamicSelectModel.getValue().getExpression());
        assertEquals(dynamicSelectModel, result);
    }

    @Test
    public void testEnumeration() {
        String enumeration = "testEnumeration";
        DynamicSelectModel result = dynamicSelectModel.enumeration(enumeration);
        assertEquals(enumeration + ".values()", dynamicSelectModel.getCollection().getExpression());
        assertEquals("toString()", dynamicSelectModel.getLabel().getExpression());
        assertEquals("ordinal()", dynamicSelectModel.getValue().getExpression());
        assertEquals(dynamicSelectModel, result);
    }

    @Test
    public void testTranslator() {
        String translator = "testTranslator";
        DynamicSelectModel result = dynamicSelectModel.translator(translator);
        assertEquals(translator, dynamicSelectModel.getTranslator().getExpression());
        assertEquals(dynamicSelectModel, result);
    }

    @Test
    public void testEmpty() {
        Object o = new Object();
        DynamicSelectModel result = dynamicSelectModel.empty(o);
        assertEquals(o, dynamicSelectModel.getEmptyObject());
        assertEquals(dynamicSelectModel, result);
    }

    @Test
    public void testGetOptionCount() {
        Collection objects = new ArrayList();
        dynamicSelectModel.setObjects(objects);
        int result = dynamicSelectModel.getOptionCount();
        assertEquals(objects.size(), result);
    }

    @Test
    public void testGetLabel() {
        int index = 0;
        ElExpression label = new ElExpression("testLabel");
        dynamicSelectModel.setLabel(label);
        Component component = new Component();
        Object obj = new Object();
        ArrayList objects = new ArrayList();
        objects.add(obj);
        dynamicSelectModel.setObjects(objects);
        dynamicSelectModel.setComponent(component);
        String result = dynamicSelectModel.getLabel(index);
        assertEquals(obj.toString(), result);
    }

    @Test
    public void testGetValue() {
        int index = 0;
        ElExpression value = new ElExpression("testValue");
        dynamicSelectModel.setValue(value);
        Component component = new Component();
        Object obj = new Object();
        ArrayList objects = new ArrayList();
        objects.add(obj);
        dynamicSelectModel.setObjects(objects);
        dynamicSelectModel.setComponent(component);
        String result = dynamicSelectModel.getValue(index);
        assertEquals(obj.toString(), result);
    }

    @Test
    public void testTranslateValue() {
        String value = "testValue";
        Object obj = new Object();
        ElExpression translator = new ElExpression("testTranslator");
        dynamicSelectModel.setTranslator(translator);
        ArrayList objects = new ArrayList();
        objects.add(obj);
        dynamicSelectModel.setObjects(objects);
        Component component = new Component();
        dynamicSelectModel.setComponent(component);
        Object result = dynamicSelectModel.translateValue(value);
        assertEquals(obj, result);
    }

    @Test
    public void testGetObjects() {
        String collection = "testCollection";
        Object[] array = {1, 2, 3};
        ElExpression collectionEl = new ElExpression(collection);
        dynamicSelectModel.setCollection(collectionEl);
        Component component = new Component();
        StandaloneComponent topLevelComponent = new StandaloneComponent();
        dynamicSelectModel.setComponent(component);
        dynamicSelectModel.setTopLevelComponent(topLevelComponent);
        ArrayList objects = new ArrayList();
        objects.add(array[0]);
        objects.add(array[1]);
        objects.add(array[2]);
        dynamicSelectModel.setObjects(objects);
        Collection result = dynamicSelectModel.getObjects();
        assertEquals(objects, result);
    }

    @Test
    public void testGetObjectsWithNull() {
        String collection = "testCollection";
        ElExpression collectionEl = new ElExpression(collection);
        dynamicSelectModel.setCollection(collectionEl);
        Component component = new Component();
        StandaloneComponent topLevelComponent = new StandaloneComponent();
        dynamicSelectModel.setComponent(component);
        dynamicSelectModel.setTopLevelComponent(topLevelComponent);
        ArrayList objects = new ArrayList();
        dynamicSelectModel.setObjects(objects);
        WheelException exception = assertThrows(WheelException.class, () -> dynamicSelectModel.getObjects());
        assertEquals("Expression " + collectionEl + " evaluated to null. Can't build a dynamic ISelectModel", exception.getMessage());
        assertEquals(component, exception.getComponent());
    }

    @Test
    public void testGetObjectsWithInvalidObject() {
        String collection = "testCollection";
        Object obj = new Object();
        ElExpression collectionEl = new ElExpression(collection);
        dynamicSelectModel.setCollection(collectionEl);
        Component component = new Component();
        StandaloneComponent topLevelComponent = new StandaloneComponent();
        dynamicSelectModel.setComponent(component);
        dynamicSelectModel.setTopLevelComponent(topLevelComponent);
        ArrayList objects = new ArrayList();
        objects.add(obj);
        dynamicSelectModel.setObjects(objects);
        WheelException exception = assertThrows(WheelException.class, () -> dynamicSelectModel.getObjects());
        assertEquals("Expression " + collectionEl + " evaluated to an object that is not an instance of Collection or Object[]. Can't build a dynamic ISelectModel", exception.getMessage());
        assertEquals(component, exception.getComponent());
    }

    @Test
    public void testGetObjectsWithEmptyObject() {
        String collection = "testCollection";
        ElExpression collectionEl = new ElExpression(collection);
        dynamicSelectModel.setCollection(collectionEl);
        Component component = new Component();
        StandaloneComponent topLevelComponent = new StandaloneComponent();
        dynamicSelectModel.setComponent(component);
        dynamicSelectModel.setTopLevelComponent(topLevelComponent);
        ArrayList objects = new ArrayList();
        dynamicSelectModel.setObjects(objects);
        Object emptyObject = new Object();
        dynamicSelectModel.setEmptyObject(emptyObject);
        Collection result = dynamicSelectModel.getObjects();
        LinkedList newCollection = new LinkedList();
        newCollection.add(emptyObject);
        newCollection.addAll(objects);
        assertEquals(newCollection, result);
    }

    @Test
    public void testGetComponent() {
        Component component = new Component();
        dynamicSelectModel.setComponent(component);
        Component result = dynamicSelectModel.getComponent();
        assertEquals(component, result);
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
        StandaloneComponent result = dynamicSelectModel.getTopLevelComponent();
        assertEquals(topLevelComponent, result);
    }

    @Test
    public void testSetTopLevelComponent() {
        StandaloneComponent topLevelComponent = new StandaloneComponent();
        dynamicSelectModel.setTopLevelComponent(topLevelComponent);
        assertEquals(topLevelComponent, dynamicSelectModel.getTopLevelComponent());
    }

    @Test
    public void testValidateWithNullCollection() {
        dynamicSelectModel.setCollection(null);
        Component component = new Component();
        WheelException exception = assertThrows(WheelException.class, () -> dynamicSelectModel.validate());
        assertEquals("Could not create DynamicSelection model. Collection attribute was null or not an el-expression.", exception.getMessage());
        assertNull(exception.getComponent());
    }

    @Test
    public void testValidateWithNullLabel() {
        dynamicSelectModel.setLabel(null);
        Component component = new Component();
        WheelException exception = assertThrows(WheelException.class, () -> dynamicSelectModel.validate());
        assertEquals("Could not create DynamicSelection model. Label attribute was null or not an el-expression.", exception.getMessage());
        assertNull(exception.getComponent());
    }

    @Test
    public void testValidateWithNullValue() {
        dynamicSelectModel.setValue(null);
        Component component = new Component();
        WheelException exception = assertThrows(WheelException.class, () -> dynamicSelectModel.validate());
        assertEquals("Could not create DynamicSelection model. Value attribute was null or not an el-expression.", exception.getMessage());
        assertNull(exception.getComponent());
    }

    @Test
    public void testReset() {
        dynamicSelectModel.setObjects(null);
        dynamicSelectModel.reset();
        assertNull(dynamicSelectModel.getObjects());
    }
}