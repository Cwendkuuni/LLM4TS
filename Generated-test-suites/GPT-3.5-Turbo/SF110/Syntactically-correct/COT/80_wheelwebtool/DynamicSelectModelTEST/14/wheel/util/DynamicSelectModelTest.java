import org.junit.Before;
import org.junit.Test;
import wheel.components.Component;
import wheel.components.StandaloneComponent;
import wheel.util.DynamicSelectModel;
import wheel.util.ElExpression;

import static org.junit.Assert.*;

public class DynamicSelectModelTest {

    private DynamicSelectModel dynamicSelectModel;

    @Before
    public void setUp() {
        dynamicSelectModel = new DynamicSelectModel();
    }

    @Test
    public void testCollection() {
        dynamicSelectModel.collection("collection");
        assertEquals(new ElExpression("collection"), dynamicSelectModel.getCollection());
    }

    @Test
    public void testLabel() {
        dynamicSelectModel.label("label");
        assertEquals(new ElExpression("label"), dynamicSelectModel.getLabel());
    }

    @Test
    public void testValue() {
        dynamicSelectModel.value("value");
        assertEquals(new ElExpression("value"), dynamicSelectModel.getValue());
    }

    @Test
    public void testEnumeration() {
        dynamicSelectModel.enumeration("enumeration");
        assertEquals(new ElExpression("enumeration + \".values()\""), dynamicSelectModel.getCollection());
        assertEquals(new ElExpression("toString()"), dynamicSelectModel.getLabel());
        assertEquals(new ElExpression("ordinal()"), dynamicSelectModel.getValue());
    }

    @Test
    public void testTranslator() {
        dynamicSelectModel.translator("translator");
        assertEquals(new ElExpression("translator"), dynamicSelectModel.getTranslator());
    }

    @Test
    public void testEmpty() {
        Object emptyObject = new Object();
        dynamicSelectModel.empty(emptyObject);
        assertEquals(emptyObject, dynamicSelectModel.getEmptyObject());
    }

    @Test
    public void testGetOptionCount() {
        dynamicSelectModel.setObjects(new Object[]{1, 2, 3});
        assertEquals(3, dynamicSelectModel.getOptionCount());
    }

    @Test
    public void testGetLabel() {
        dynamicSelectModel.setObjects(new Object[]{1, 2, 3});
        dynamicSelectModel.setComponent(new Component());
        assertEquals("1", dynamicSelectModel.getLabel(0));
    }

    @Test
    public void testGetValue() {
        dynamicSelectModel.setObjects(new Object[]{1, 2, 3});
        dynamicSelectModel.setComponent(new Component());
        assertEquals("1", dynamicSelectModel.getValue(0));
    }

    @Test
    public void testTranslateValue() {
        dynamicSelectModel.setObjects(new Object[]{1, 2, 3});
        dynamicSelectModel.setValue(new ElExpression("value"));
        dynamicSelectModel.setComponent(new Component());
        assertEquals(1, dynamicSelectModel.translateValue("1"));
    }

    @Test
    public void testGetObjects() {
        Object[] objects = new Object[]{1, 2, 3};
        dynamicSelectModel.setTopLevelComponent(new StandaloneComponent());
        dynamicSelectModel.setCollection(new ElExpression("collection"));
        dynamicSelectModel.setComponent(new Component());
        dynamicSelectModel.setObjects(objects);
        assertArrayEquals(objects, dynamicSelectModel.getObjects().toArray());
    }

    @Test(expected = WheelException.class)
    public void testGetObjectsWithNullCollection() {
        dynamicSelectModel.getObjects();
    }

    @Test(expected = WheelException.class)
    public void testGetObjectsWithNonCollection() {
        dynamicSelectModel.setTopLevelComponent(new StandaloneComponent());
        dynamicSelectModel.setCollection(new ElExpression("not a collection"));
        dynamicSelectModel.getObjects();
    }

    @Test
    public void testGetObjectsWithEmptyObject() {
        Object[] objects = new Object[]{1, 2, 3};
        dynamicSelectModel.setTopLevelComponent(new StandaloneComponent());
        dynamicSelectModel.setCollection(new ElExpression("collection"));
        dynamicSelectModel.setComponent(new Component());
        dynamicSelectModel.setObjects(objects);
        dynamicSelectModel.empty(new Object());
        assertEquals(4, dynamicSelectModel.getObjects().size());
    }

    @Test
    public void testGetObjectsWithNullAndEmptyObject() {
        Object[] objects = new Object[]{1, 2, 3};
        dynamicSelectModel.setTopLevelComponent(new StandaloneComponent());
        dynamicSelectModel.setCollection(new ElExpression("collection"));
        dynamicSelectModel.setComponent(new Component());
        dynamicSelectModel.setObjects(objects);
        dynamicSelectModel.empty(null);
        assertEquals(3, dynamicSelectModel.getObjects().size());
    }

    @Test
    public void testGetObjectsWithNullObjects() {
        dynamicSelectModel.setTopLevelComponent(new StandaloneComponent());
        dynamicSelectModel.setCollection(new ElExpression("collection"));
        dynamicSelectModel.setComponent(new Component());
        dynamicSelectModel.setObjects(null);
        assertNull(dynamicSelectModel.getObjects());
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
        dynamicSelectModel.validate();
    }

    @Test(expected = WheelException.class)
    public void testValidateWithNullLabel() {
        dynamicSelectModel.setCollection(new ElExpression("collection"));
        dynamicSelectModel.validate();
    }

    @Test(expected = WheelException.class)
    public void testValidateWithNullValue() {
        dynamicSelectModel.setCollection(new ElExpression("collection"));
        dynamicSelectModel.setLabel(new ElExpression("label"));
        dynamicSelectModel.validate();
    }

    @Test
    public void testReset() {
        dynamicSelectModel.setObjects(new Object[]{1, 2, 3});
        dynamicSelectModel.reset();
        assertNull(dynamicSelectModel.getObjects());
    }

    
}