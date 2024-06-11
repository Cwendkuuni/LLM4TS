package wheel.util;

import org.junit.Before;
import org.junit.Test;
import wheel.components.*;
import java.util.*;

import static org.junit.Assert.*;

public class DynamicSelectModelTest {
    private DynamicSelectModel dynamicSelectModel;

    @Before
    public void setUp() {
        dynamicSelectModel = new DynamicSelectModel();
    }

    @Test
    public void testCollection() {
        DynamicSelectModel actual = dynamicSelectModel.collection("collection");
        assertSame(dynamicSelectModel, actual);
    }

    @Test
    public void testLabel() {
        DynamicSelectModel actual = dynamicSelectModel.label("label");
        assertSame(dynamicSelectModel, actual);
    }

    @Test
    public void testValue() {
        DynamicSelectModel actual = dynamicSelectModel.value("value");
        assertSame(dynamicSelectModel, actual);
    }

    @Test
    public void testEnumeration() {
        DynamicSelectModel actual = dynamicSelectModel.enumeration("enumeration");
        assertSame(dynamicSelectModel, actual);
    }

    @Test
    public void testTranslator() {
        DynamicSelectModel actual = dynamicSelectModel.translator("translator");
        assertSame(dynamicSelectModel, actual);
    }

    @Test
    public void testEmpty() {
        DynamicSelectModel actual = dynamicSelectModel.empty("empty");
        assertSame(dynamicSelectModel, actual);
    }

    @Test
    public void testGetOptionCount() {
        dynamicSelectModel.setObjects(Arrays.asList("option1", "option2", "option3"));
        int result = dynamicSelectModel.getOptionCount();
        assertEquals(3, result);
    }

    @Test
    public void testGetLabel() {
        dynamicSelectModel.setObjects(Arrays.asList("option1", "option2", "option3"));
        dynamicSelectModel.setComponent(new Component());
        String result = dynamicSelectModel.getLabel(0);
        assertEquals("option1", result);
    }

    @Test(expected = NullPointerException.class)
    public void testGetLabelThrowsNullPointerException() {
        dynamicSelectModel.setObjects(Arrays.asList("option1", "option2", "option3"));
        String result = dynamicSelectModel.getLabel(4);
    }

    @Test
    public void testGetValue() {
        dynamicSelectModel.setObjects(Arrays.asList("option1", "option2", "option3"));
        dynamicSelectModel.setComponent(new Component());
        String result = dynamicSelectModel.getValue(0);
        assertEquals("option1", result);
    }

    @Test(expected = NullPointerException.class)
    public void testGetValueThrowsNullPointerException() {
        dynamicSelectModel.setObjects(Arrays.asList("option1", "option2", "option3"));
        String result = dynamicSelectModel.getValue(4);
    }

    @Test
    public void testTranslateValue() {
        dynamicSelectModel.setObjects(Arrays.asList("option1", "option2", "option3"));
        dynamicSelectModel.setValue("value");
        dynamicSelectModel.setTranslator("translator");
        Object result = dynamicSelectModel.translateValue("value");
        assertEquals("translator", result);
    }

    @Test
    public void testTranslateValueWhenTranslatorIsNull() {
        dynamicSelectModel.setObjects(Arrays.asList("option1", "option2", "option3"));
        dynamicSelectModel.setComponent(new Component());
        Object result = dynamicSelectModel.translateValue("value");
        assertNull(result);
    }

    @Test
    public void testGetObjectsWhenObjectsIsNotNull() {
        dynamicSelectModel.setObjects(Arrays.asList("option1", "option2", "option3"));
        Collection result = dynamicSelectModel.getObjects();
        assertSame(dynamicSelectModel.getObjects(), result);
    }

    @Test(expected = WheelException.class)
    public void testGetObjectsThrowsWheelExceptionWhenEvalReturnsNull() {
        dynamicSelectModel.setCollection("collection");
        dynamicSelectModel.setTopLevelComponent(new StandaloneComponent());
        dynamicSelectModel.getObjects();
    }

    @Test(expected = WheelException.class)
    public void testGetObjectsThrowsWheelExceptionWhenEvalReturnsNotCollection() {
        dynamicSelectModel.setCollection("collection");
        dynamicSelectModel.setTopLevelComponent(new StandaloneComponent());
        dynamicSelectModel.getObjects();
    }

    @Test
    public void testGetComponent() {
        Component component = new Component();
        dynamicSelectModel.setComponent(component);
        Component result = dynamicSelectModel.getComponent();
        assertSame(component, result);
    }

    @Test
    public void testSetComponent() {
        Component component = new Component();
        dynamicSelectModel.setComponent(component);
        assertSame(component, dynamicSelectModel.getComponent());
    }

    @Test
    public void testGetTopLevelComponent() {
        StandaloneComponent standaloneComponent = new StandaloneComponent();
        dynamicSelectModel.setTopLevelComponent(standaloneComponent);
        StandaloneComponent result = dynamicSelectModel.getTopLevelComponent();
        assertSame(standaloneComponent, result);
    }

    @Test
    public void testSetTopLevelComponent() {
        StandaloneComponent standaloneComponent = new StandaloneComponent();
        dynamicSelectModel.setTopLevelComponent(standaloneComponent);
        assertSame(standaloneComponent, dynamicSelectModel.getTopLevelComponent());
    }
    
    @Test(expected = WheelException.class)
    public void testValidateThrowsWheelExceptionWhenCollectionIsNull() {
        dynamicSelectModel.validate();
    }
    
    @Test(expected = WheelException.class)
    public void testValidateThrowsWheelExceptionWhenLabelIsNull() {
        dynamicSelectModel.collection("collection");
        dynamicSelectModel.validate();
    }
    
    @Test(expected = WheelException.class)
    public void testValidateThrowsWheelExceptionWhenValueIsNull() {
        dynamicSelectModel.collection("collection");
        dynamicSelectModel.label("label");
        dynamicSelectModel.validate();
    }
    
    @Test
    public void testReset() {
        dynamicSelectModel.setObjects(Arrays.asList("option1", "option2", "option3"));
        dynamicSelectModel.reset();
        assertNull(dynamicSelectModel.getObjects());
    }
}