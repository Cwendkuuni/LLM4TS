import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import wheel.*;
import wheel.components.Component;
import wheel.components.ElExpression;
import wheel.components.StandaloneComponent;
import wheel.util.DynamicSelectModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

@RunWith(PowerMockRunner.class)
@PrepareForTest({DynamicSelectModel.class, Component.class, StandaloneComponent.class})
public class DynamicSelectModelTest {

    @Mock
    private Component componentMock;
    @Mock
    private StandaloneComponent topLevelComponentMock;

    private DynamicSelectModel dynamicSelectModel;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.setComponent(componentMock);
        dynamicSelectModel.setTopLevelComponent(topLevelComponentMock);
    }

    @Test
    public void testGetOptionCount() {
        dynamicSelectModel.objects = new ArrayList<>();
        dynamicSelectModel.objects.add(new Object());
        dynamicSelectModel.objects.add(new Object());

        int optionCount = dynamicSelectModel.getOptionCount();

        Assert.assertEquals(2, optionCount);
    }

    @Test
    public void testGetLabel() {
        dynamicSelectModel.objects = new ArrayList<>();
        dynamicSelectModel.objects.add("Label 1");
        dynamicSelectModel.objects.add("Label 2");

        String label = dynamicSelectModel.getLabel(0);

        Assert.assertEquals("Label 1", label);
    }

    @Test
    public void testGetValue() {
        dynamicSelectModel.objects = new ArrayList<>();
        dynamicSelectModel.objects.add("Value 1");
        dynamicSelectModel.objects.add("Value 2");

        String value = dynamicSelectModel.getValue(0);

        Assert.assertEquals("Value 1", value);
    }

    @Test
    public void testTranslateValue() {
        dynamicSelectModel.objects = new ArrayList<>();
        dynamicSelectModel.objects.add(new Object());
        dynamicSelectModel.objects.add("Value");
        dynamicSelectModel.translator = new ElExpression("translator");

        Object translatedValue = dynamicSelectModel.translateValue("Value");

        Assert.assertEquals("Translated Value", translatedValue);
    }

    @Test
    public void testGetObjects() {
        dynamicSelectModel.collection = new ElExpression("collection");
        dynamicSelectModel.topLevelComponent = topLevelComponentMock;

        Collection objects = dynamicSelectModel.getObjects();

        Assert.assertNotNull(objects);
        Assert.assertEquals(1, objects.size());
        Assert.assertTrue(objects.contains("Object 1"));
    }

    @Test
    public void testCollection() {
        DynamicSelectModel result = dynamicSelectModel.collection("collection");

        Assert.assertNotNull(result);
        Assert.assertEquals("collection", dynamicSelectModel.collection.getExpression());
    }

    @Test
    public void testLabel() {
        DynamicSelectModel result = dynamicSelectModel.label("label");

        Assert.assertNotNull(result);
        Assert.assertEquals("label", dynamicSelectModel.label.getExpression());
    }

    @Test
    public void testValue() {
        DynamicSelectModel result = dynamicSelectModel.value("value");

        Assert.assertNotNull(result);
        Assert.assertEquals("value", dynamicSelectModel.value.getExpression());
    }

    @Test
    public void testEnumeration() {
        DynamicSelectModel result = dynamicSelectModel.enumeration("enumeration");

        Assert.assertNotNull(result);
        Assert.assertEquals("enumeration.values()", dynamicSelectModel.collection.getExpression());
        Assert.assertEquals("toString()", dynamicSelectModel.label.getExpression());
        Assert.assertEquals("ordinal()", dynamicSelectModel.value.getExpression());
    }

    @Test
    public void testTranslator() {
        DynamicSelectModel result = dynamicSelectModel.translator("translator");

        Assert.assertNotNull(result);
        Assert.assertEquals("translator", dynamicSelectModel.translator.getExpression());
    }

    @Test
    public void testEmpty() {
        DynamicSelectModel result = dynamicSelectModel.empty("empty");

        Assert.assertNotNull(result);
        Assert.assertEquals("empty", dynamicSelectModel.emptyObject);
    }

    @Test(expected = WheelException.class)
    public void testGetOptionCount_NullObjects() {
        dynamicSelectModel.getOptionCount();
    }

    @Test(expected = WheelException.class)
    public void testGetLabel_NullObjects() {
        dynamicSelectModel.getLabel(0);
    }

    @Test(expected = WheelException.class)
    public void testGetValue_NullObjects() {
        dynamicSelectModel.getValue(0);
    }

    @Test(expected = WheelException.class)
    public void testTranslateValue_NullObjects() {
        dynamicSelectModel.translateValue("Value");
    }

    @Test(expected = WheelException.class)
    public void testGetObjects_EvalNull() {
        dynamicSelectModel.collection = new ElExpression("collection");
        dynamicSelectModel.topLevelComponent = topLevelComponentMock;

        dynamicSelectModel.getObjects();
    }

    @Test(expected = WheelException.class)
    public void testGetObjects_InvalidType() {
        dynamicSelectModel.collection = new ElExpression("collection");
        dynamicSelectModel.topLevelComponent = topLevelComponentMock;

        dynamicSelectModel.getObjects();
    }

    @Test(expected = WheelException.class)
    public void testGetObjects_NullEvalResult() {
        dynamicSelectModel.collection = new ElExpression("collection");
        dynamicSelectModel.topLevelComponent = topLevelComponentMock;

        dynamicSelectModel.getObjects();
    }

    @Test(expected = WheelException.class)
    public void testValidate_NullCollection() {
        dynamicSelectModel.validate();
    }

    @Test(expected = WheelException.class)
    public void testValidate_NullLabel() {
        dynamicSelectModel.collection = new ElExpression("collection");

        dynamicSelectModel.validate();
    }

    @Test(expected = WheelException.class)
    public void testValidate_NullValue() {
        dynamicSelectModel.collection = new ElExpression("collection");
        dynamicSelectModel.label = new ElExpression("label");

        dynamicSelectModel.validate();
    }

    @Test
    public void testReset() {
        dynamicSelectModel.objects = new ArrayList<>();

        dynamicSelectModel.reset();

        Assert.assertNull(dynamicSelectModel.objects);
    }

}