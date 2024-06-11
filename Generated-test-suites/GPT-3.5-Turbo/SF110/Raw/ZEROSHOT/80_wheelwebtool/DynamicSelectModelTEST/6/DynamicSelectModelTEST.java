import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

public class DynamicSelectModelTest {
    private DynamicSelectModel dynamicSelectModel;

    @Before
    public void setUp() {
        dynamicSelectModel = new DynamicSelectModel();
    }

    @Test
    public void testCollection() {
        String collection = "test";
        DynamicSelectModel result = dynamicSelectModel.collection(collection);
        assertNotNull(result);
        assertEquals(collection, dynamicSelectModel.getCollection().getExpression());
    }

    @Test
    public void testLabel() {
        String label = "test";
        DynamicSelectModel result = dynamicSelectModel.label(label);
        assertNotNull(result);
        assertEquals(label, dynamicSelectModel.getLabel().getExpression());
    }

    @Test
    public void testValue() {
        String value = "test";
        DynamicSelectModel result = dynamicSelectModel.value(value);
        assertNotNull(result);
        assertEquals(value, dynamicSelectModel.getValue().getExpression());
    }

    @Test
    public void testEnumeration() {
        String enumeration = "test";
        DynamicSelectModel result = dynamicSelectModel.enumeration(enumeration);
        assertNotNull(result);
        assertEquals(enumeration + ".values()", dynamicSelectModel.getCollection().getExpression());
        assertEquals("toString()", dynamicSelectModel.getLabel().getExpression());
        assertEquals("ordinal()", dynamicSelectModel.getValue().getExpression());
    }

    @Test
    public void testTranslator() {
        String translator = "test";
        DynamicSelectModel result = dynamicSelectModel.translator(translator);
        assertNotNull(result);
        assertEquals(translator, dynamicSelectModel.getTranslator().getExpression());
    }

    @Test
    public void testEmpty() {
        Object emptyObject = new Object();
        DynamicSelectModel result = dynamicSelectModel.empty(emptyObject);
        assertNotNull(result);
        assertEquals(emptyObject, dynamicSelectModel.getEmptyObject());
    }

    @Test
    public void testGetOptionCount() {
        Collection<Object> objects = new ArrayList<>();
        objects.add(new Object());
        objects.add(new Object());
        objects.add(new Object());
        dynamicSelectModel.setObjects(objects);

        int result = dynamicSelectModel.getOptionCount();

        assertEquals(objects.size(), result);
    }

    @Test
    public void testGetLabel() {
        dynamicSelectModel.setLabel(new ElExpression("test"));
        dynamicSelectModel.setObjects(new ArrayList<>());
        dynamicSelectModel.setComponent(new Component());

        String result = dynamicSelectModel.getLabel(0);

        assertEquals("", result);
    }

    @Test
    public void testGetValue() {
        dynamicSelectModel.setValue(new ElExpression("test"));
        dynamicSelectModel.setObjects(new ArrayList<>());
        dynamicSelectModel.setComponent(new Component());

        String result = dynamicSelectModel.getValue(0);

        assertEquals("", result);
    }

    @Test
    public void testTranslateValue() {
        dynamicSelectModel.setObjects(new ArrayList<>());
        dynamicSelectModel.setComponent(new Component());

        Object result = dynamicSelectModel.translateValue("");

        assertNull(result);
    }

    @Test
    public void testGetObjects() {
        dynamicSelectModel.setCollection(new ElExpression("test"));
        dynamicSelectModel.setObjects(new ArrayList<>());
        dynamicSelectModel.setTopLevelComponent(new StandaloneComponent());

        Collection result = dynamicSelectModel.getObjects();

        assertNotNull(result);
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

        Component result = dynamicSelectModel.getComponent();

        assertEquals(component, result);
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

        StandaloneComponent result = dynamicSelectModel.getTopLevelComponent();

        assertEquals(topLevelComponent, result);
    }

    @Test(expected = WheelException.class)
    public void testValidateWithNullCollection() {
        dynamicSelectModel.setCollection(null);
        dynamicSelectModel.validate();
    }

    @Test(expected = WheelException.class)
    public void testValidateWithNullLabel() {
        dynamicSelectModel.setCollection(new ElExpression("test"));
        dynamicSelectModel.setLabel(null);
        dynamicSelectModel.validate();
    }

    @Test(expected = WheelException.class)
    public void testValidateWithNullValue() {
        dynamicSelectModel.setCollection(new ElExpression("test"));
        dynamicSelectModel.setLabel(new ElExpression("test"));
        dynamicSelectModel.setValue(null);
        dynamicSelectModel.validate();
    }

    @Test
    public void testReset() {
        Collection<Object> objects = new ArrayList<>();
        objects.add(new Object());
        objects.add(new Object());
        objects.add(new Object());
        dynamicSelectModel.setObjects(objects);

        dynamicSelectModel.reset();

        assertNull(dynamicSelectModel.getObjects());
    }
}