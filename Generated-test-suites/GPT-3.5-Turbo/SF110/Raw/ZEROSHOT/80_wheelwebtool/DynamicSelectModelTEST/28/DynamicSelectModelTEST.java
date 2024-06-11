package wheel.util;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import wheel.components.*;
import java.util.*;

public class DynamicSelectModelTest {

    private DynamicSelectModel dynamicSelectModel;

    @Before
    public void setUp() {
        dynamicSelectModel = new DynamicSelectModel();
    }

    @Test
    public void testGetOptionCount() {
        dynamicSelectModel.setComponent(new Component());
        dynamicSelectModel.setTopLevelComponent(new StandaloneComponent());
        dynamicSelectModel.collection("collection");
        dynamicSelectModel.value("value");

        int optionCount = dynamicSelectModel.getOptionCount();

        assertEquals(0, optionCount);
    }

    @Test
    public void testGetLabel() {
        dynamicSelectModel.setComponent(new Component());
        dynamicSelectModel.setTopLevelComponent(new StandaloneComponent());
        dynamicSelectModel.label("label");

        String label = dynamicSelectModel.getLabel(0);

        assertNull(label);
    }

    @Test
    public void testGetValue() {
        dynamicSelectModel.setComponent(new Component());
        dynamicSelectModel.setTopLevelComponent(new StandaloneComponent());
        dynamicSelectModel.value("value");

        String value = dynamicSelectModel.getValue(0);

        assertNull(value);
    }

    @Test
    public void testTranslateValue() {
        dynamicSelectModel.setComponent(new Component());
        dynamicSelectModel.setTopLevelComponent(new StandaloneComponent());
        dynamicSelectModel.translator("translator");

        Object translatedValue = dynamicSelectModel.translateValue("value");

        assertNull(translatedValue);
    }

    @Test
    public void testGetObjects() {
        dynamicSelectModel.setComponent(new Component());
        dynamicSelectModel.setTopLevelComponent(new StandaloneComponent());
        dynamicSelectModel.collection("collection");

        try {
            Collection objects = dynamicSelectModel.getObjects();
        } catch (WheelException e) {
            assertEquals("Could not evaluate expression null to create a dynamic ISelectModel.", e.getMessage());
        }
    }

    @Test
    public void testSetComponent() {
        dynamicSelectModel.setComponent(new Component());
        
        assertNotNull(dynamicSelectModel.getComponent());
    }

    @Test
    public void testSetTopLevelComponent() {
        dynamicSelectModel.setTopLevelComponent(new StandaloneComponent());
        
        assertNotNull(dynamicSelectModel.getTopLevelComponent());
    }

    @Test(expected = WheelException.class)
    public void testValidate_NullCollection() {
        dynamicSelectModel.validate();
    }

    @Test(expected = WheelException.class)
    public void testValidate_NullLabel() {
        dynamicSelectModel.collection("collection");
        dynamicSelectModel.validate();
    }

    @Test(expected = WheelException.class)
    public void testValidate_NullValue() {
        dynamicSelectModel.collection("collection");
        dynamicSelectModel.label("label");
        dynamicSelectModel.validate();
    }

    @Test
    public void testReset() {
        dynamicSelectModel.setComponent(new Component());
        dynamicSelectModel.setTopLevelComponent(new StandaloneComponent());
        dynamicSelectModel.collection("collection");

        dynamicSelectModel.reset();

        assertNull(dynamicSelectModel.getObjects());
    }
}