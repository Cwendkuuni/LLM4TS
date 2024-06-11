package wheel.util;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Collection;

public class DynamicSelectModelTest {

    @Test
    public void testCollection() {
        DynamicSelectModel model = new DynamicSelectModel();
        String collection = "testCollection";
        model.collection(collection);
        assertEquals(collection, model.getCollection());
    }

    @Test
    public void testLabel() {
        DynamicSelectModel model = new DynamicSelectModel();
        String label = "testLabel";
        model.label(label);
        assertEquals(label, model.getLabel());
    }

    @Test
    public void testValue() {
        DynamicSelectModel model = new DynamicSelectModel();
        String value = "testValue";
        model.value(value);
        assertEquals(value, model.getValue());
    }

    @Test
    public void testEnumeration() {
        DynamicSelectModel model = new DynamicSelectModel();
        String enumeration = "testEnumeration";
        model.enumeration(enumeration);
        assertEquals(enumeration + ".values()", model.getCollection());
        assertEquals("toString()", model.getLabel());
        assertEquals("ordinal()", model.getValue());
    }

    @Test
    public void testTranslator() {
        DynamicSelectModel model = new DynamicSelectModel();
        String translator = "testTranslator";
        model.translator(translator);
        assertEquals(translator, model.getTranslator());
    }

    @Test
    public void testEmpty() {
        DynamicSelectModel model = new DynamicSelectModel();
        Object emptyObject = new Object();
        model.empty(emptyObject);
        assertEquals(emptyObject, model.getEmptyObject());
    }

    @Test
    public void testGetOptionCount() {
        DynamicSelectModel model = new DynamicSelectModel();
        Collection<Object> objects = new ArrayList<>();
        model.setObjects(objects);
        assertEquals(objects.size(), model.getOptionCount());
    }

    @Test
    public void testGetLabel() {
        DynamicSelectModel model = new DynamicSelectModel();
        String label = "testLabel";
        model.setLabel(label);
        int index = 0;
        assertEquals(label, model.getLabel(index));
    }

    @Test
    public void testGetValue() {
        DynamicSelectModel model = new DynamicSelectModel();
        String value = "testValue";
        model.setValue(value);
        int index = 0;
        assertEquals(value, model.getValue(index));
    }

    @Test
    public void testTranslateValue() {
        DynamicSelectModel model = new DynamicSelectModel();
        String value = "testValue";
        String translatedValue = "translatedValue";
        model.setValue(value);
        model.setTranslator(translatedValue);
        assertEquals(translatedValue, model.translateValue(value));
    }

    @Test
    public void testTranslateValueNoTranslator() {
        DynamicSelectModel model = new DynamicSelectModel();
        String value = "testValue";
        model.setValue(value);
        assertEquals(value, model.translateValue(value));
    }

    @Test
    public void testGetObjects() {
        DynamicSelectModel model = new DynamicSelectModel();
        Collection<Object> objects = new ArrayList<>();
        model.setObjects(objects);
        assertSame(objects, model.getObjects());
    }

    @Test
    public void testSetComponent() {
        DynamicSelectModel model = new DynamicSelectModel();
        Component component = new Component();
        model.setComponent(component);
        assertSame(component, model.getComponent());
    }

    @Test
    public void testSetTopLevelComponent() {
        DynamicSelectModel model = new DynamicSelectModel();
        StandaloneComponent topLevelComponent = new StandaloneComponent();
        model.setTopLevelComponent(topLevelComponent);
        assertSame(topLevelComponent, model.getTopLevelComponent());
    }

    @Test(expected = WheelException.class)
    public void testValidateCollectionNull() {
        DynamicSelectModel model = new DynamicSelectModel();
        model.validate();
    }

    @Test(expected = WheelException.class)
    public void testValidateLabelNull() {
        DynamicSelectModel model = new DynamicSelectModel();
        model.collection("testCollection");
        model.validate();
    }

    @Test(expected = WheelException.class)
    public void testValidateValueNull() {
        DynamicSelectModel model = new DynamicSelectModel();
        model.collection("testCollection");
        model.label("testLabel");
        model.validate();
    }

    @Test
    public void testReset() {
        DynamicSelectModel model = new DynamicSelectModel();
        model.reset();
        assertNull(model.getObjects());
    }

}