package wheel.util;

import org.junit.Test;
import static org.junit.Assert.*;
import wheel.components.*;
import wheel.*;
import java.util.*;

public class DynamicSelectModelTest {

    @Test
    public void testConstructor() {
        // Test case 1
        DynamicSelectModel model = new DynamicSelectModel(new ElExpression("collection"), new ElExpression("label"), new ElExpression("value"));
        assertNotNull(model);
        assertNotNull(model.label);
        assertNotNull(model.value);
        assertNotNull(model.collection);

        // Test case 2
        model = new DynamicSelectModel();
        assertNotNull(model);
    }

    @Test
    public void testCollectionMethod() {
        DynamicSelectModel model = new DynamicSelectModel();
        model.collection("collection");
        assertNotNull(model.collection);
    }

    @Test
    public void testLabelMethod() {
        DynamicSelectModel model = new DynamicSelectModel();
        model.label("label");
        assertNotNull(model.label);
    }

    @Test
    public void testValueMethod() {
        DynamicSelectModel model = new DynamicSelectModel();
        model.value("value");
        assertNotNull(model.value);
    }

    @Test
    public void testEnumerationMethod() {
        DynamicSelectModel model = new DynamicSelectModel();
        model.enumeration("enumeration");
        assertNotNull(model.collection);
        assertNotNull(model.label);
        assertNotNull(model.value);
    }

    @Test
    public void testTranslatorMethod() {
        DynamicSelectModel model = new DynamicSelectModel();
        model.translator("translator");
        assertNotNull(model.translator);
    }

    @Test
    public void testEmptyMethod() {
        DynamicSelectModel model = new DynamicSelectModel();
        model.empty(new Object());
        assertNotNull(model.emptyObject);
    }

    @Test
    public void testGetOptionCount() {
        // Test case 1
        DynamicSelectModel model = new DynamicSelectModel();
        model.objects = new ArrayList<>(Arrays.asList("option1", "option2", "option3"));
        assertEquals(3, model.getOptionCount());

        // Test case 2
        model.objects = new ArrayList<>();
        assertEquals(0, model.getOptionCount());
    }

    @Test
    public void testGetLabel() {
        DynamicSelectModel model = new DynamicSelectModel();
        model.component = new Component();
        model.objects = new ArrayList<>(Arrays.asList("option1", "option2", "option3"));
        model.label = new ElExpression("label");

        // Test case 1
        String label = model.getLabel(0);
        assertEquals("option1", label);

        // Test case 2
        label = model.getLabel(2);
        assertEquals("option3", label);
    }

    @Test
    public void testGetValue() {
        DynamicSelectModel model = new DynamicSelectModel();
        model.component = new Component();
        model.objects = new ArrayList<>(Arrays.asList("value1", "value2", "value3"));
        model.value = new ElExpression("value");

        // Test case 1
        String value = model.getValue(0);
        assertEquals("value1", value);

        // Test case 2
        value = model.getValue(2);
        assertEquals("value3", value);
    }

    @Test
    public void testTranslateValue() {
        DynamicSelectModel model = new DynamicSelectModel();
        model.component = new Component();
        model.objects = new ArrayList<>(Arrays.asList("value1", "value2", "value3"));
        model.value = new ElExpression("value");
        model.translator = new ElExpression("translator");

        // Test case 1
        Object translatedValue = model.translateValue("value1");
        assertEquals("value1", translatedValue);

        // Test case 2
        translatedValue = model.translateValue("value3");
        assertEquals("value3", translatedValue);
    }

    @Test
    public void testGetObjects() {
        DynamicSelectModel model = new DynamicSelectModel();
        model.component = new Component();
        model.topLevelComponent = new StandaloneComponent();
        model.collection = new ElExpression("collection");

        // Test case 1
        model.objects = new ArrayList<>(Arrays.asList("option1", "option2", "option3"));
        Collection objects = model.getObjects();
        assertNotNull(objects);
        assertEquals(3, objects.size());

        // Test case 2
        model.objects = null;
        try {
            objects = model.getObjects();
        } catch (WheelException e) {
            assertEquals("Could not evaluate expression collection to creat a dynamic ISelectModel.", e.getMessage());
        }
    }

    @Test
    public void testGetComponent() {
        DynamicSelectModel model = new DynamicSelectModel();
        model.component = new Component();
        Component component = model.getComponent();
        assertNotNull(component);
    }

    @Test
    public void testSetComponent() {
        DynamicSelectModel model = new DynamicSelectModel();
        Component component = new Component();
        model.setComponent(component);
        assertEquals(component, model.getComponent());
    }

    @Test
    public void testGetTopLevelComponent() {
        DynamicSelectModel model = new DynamicSelectModel();
        model.topLevelComponent = new StandaloneComponent();
        StandaloneComponent topLevelComponent = model.getTopLevelComponent();
        assertNotNull(topLevelComponent);
    }

    @Test
    public void testSetTopLevelComponent() {
        DynamicSelectModel model = new DynamicSelectModel();
        StandaloneComponent topLevelComponent = new StandaloneComponent();
        model.setTopLevelComponent(topLevelComponent);
        assertEquals(topLevelComponent, model.getTopLevelComponent());
    }

    @Test(expected = WheelException.class)
    public void testValidateCollectionIsNull() throws WheelException {
        DynamicSelectModel model = new DynamicSelectModel();
        model.label = new ElExpression("label");
        model.value = new ElExpression("value");

        model.validate();
    }

    @Test(expected = WheelException.class)
    public void testValidateLabelIsNull() throws WheelException {
        DynamicSelectModel model = new DynamicSelectModel();
        model.collection = new ElExpression("collection");
        model.value = new ElExpression("value");

        model.validate();
    }

    @Test(expected = WheelException.class)
    public void testValidateValueIsNull() throws WheelException {
        DynamicSelectModel model = new DynamicSelectModel();
        model.collection = new ElExpression("collection");
        model.label = new ElExpression("label");

        model.validate();
    }

    @Test
    public void testReset() {
        DynamicSelectModel model = new DynamicSelectModel();
        model.objects = new ArrayList<>(Arrays.asList("option1", "option2", "option3"));
        model.reset();
        assertNull(model.objects);
    }
}