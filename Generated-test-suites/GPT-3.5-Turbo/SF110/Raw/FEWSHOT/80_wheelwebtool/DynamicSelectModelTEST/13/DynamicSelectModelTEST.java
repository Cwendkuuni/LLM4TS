package wheel.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Before;
import org.junit.Test;

public class DynamicSelectModelTest {

    private DynamicSelectModel dynamicSelectModel;

    @Before
    public void setup() {
        dynamicSelectModel = new DynamicSelectModel();
    }

    @Test
    public void testConstructorWithArguments() {
        dynamicSelectModel = new DynamicSelectModel(new ElExpression("collection"), new ElExpression("label"), new ElExpression("value"));
        
        assertSame("collection", dynamicSelectModel.collection);
        assertSame("label", dynamicSelectModel.label);
        assertSame("value", dynamicSelectModel.value);
    }

    @Test
    public void testCollectionMethod() {
        dynamicSelectModel.collection("collection");
        assertEquals("collection", dynamicSelectModel.collection.getExpression());
    }

    @Test
    public void testLabelMethod() {
        dynamicSelectModel.label("label");
        assertEquals("label", dynamicSelectModel.label.getExpression());
    }

    @Test
    public void testValueCollectionMethod() {
        dynamicSelectModel.value("value");
        assertEquals("value", dynamicSelectModel.value.getExpression());
    }

    @Test
    public void testEnumerationMethod() {
        dynamicSelectModel.enumeration("enumeration");
        assertEquals("enumeration.values()", dynamicSelectModel.collection.getExpression());
        assertEquals("toString()", dynamicSelectModel.label.getExpression());
        assertEquals("ordinal()", dynamicSelectModel.value.getExpression());
    }

    @Test
    public void testTranslatorMethod() {
        dynamicSelectModel.translator("translator");
        assertEquals("translator", dynamicSelectModel.translator.getExpression());
    }

    @Test
    public void testEmptyMethod() {
        Object o = new Object();
        dynamicSelectModel.empty(o);
        assertSame(o, dynamicSelectModel.emptyObject);
    }

    @Test
    public void testGetOptionCount() {
        dynamicSelectModel.objects = Arrays.asList("option1", "option2", "option3");
        assertEquals(3, dynamicSelectModel.getOptionCount());
    }

    @Test
    public void testGetLabel() {
        dynamicSelectModel.objects = Arrays.asList("option1", "option2", "option3");
        dynamicSelectModel.label = new ElExpression("toString()");
        assertEquals("option1", dynamicSelectModel.getLabel(0));
        assertEquals("option2", dynamicSelectModel.getLabel(1));
        assertEquals("option3", dynamicSelectModel.getLabel(2));
    }

    @Test
    public void testGetValue() {
        dynamicSelectModel.objects = Arrays.asList("option1", "option2", "option3");
        dynamicSelectModel.value = new ElExpression("toString()");
        assertEquals("option1", dynamicSelectModel.getValue(0));
        assertEquals("option2", dynamicSelectModel.getValue(1));
        assertEquals("option3", dynamicSelectModel.getValue(2));
    }

    @Test
    public void testTranslateValue() {
        dynamicSelectModel.objects = Arrays.asList("option1", "option2", "option3");
        dynamicSelectModel.value = new ElExpression("toString()");
        dynamicSelectModel.translator = new ElExpression("translator");
        
        assertEquals("option1", dynamicSelectModel.translateValue("option1"));
        assertEquals("option2", dynamicSelectModel.translateValue("option2"));
        assertEquals("option3", dynamicSelectModel.translateValue("option3"));
    }

    @Test
    public void testGetObjects() {
        dynamicSelectModel.collection = new ElExpression("collection");

        dynamicSelectModel.topLevelComponent = new StandaloneComponent();
        dynamicSelectModel.component = new Component();

        dynamicSelectModel.collection = new ElExpression("collection");
        dynamicSelectModel.objects = Arrays.asList("option1", "option2", "option3");

        assertEquals(Arrays.asList("option1", "option2", "option3"), dynamicSelectModel.getObjects());
    }

    @Test(expected = WheelException.class)
    public void testGetObjectsWithNullExpression() {
        dynamicSelectModel.getObjects();
    }

    @Test(expected = WheelException.class)
    public void testGetObjectsWithNullResult() {
        dynamicSelectModel.collection = new ElExpression("collection");
        dynamicSelectModel.topLevelComponent = new StandaloneComponent();
        dynamicSelectModel.component = new Component();

        dynamicSelectModel.collection = new ElExpression("nonExistentCollection");

        dynamicSelectModel.getObjects();
    }

    @Test(expected = WheelException.class)
    public void testGetObjectsWithInvalidResult() {
        dynamicSelectModel.collection = new ElExpression("collection");

        dynamicSelectModel.topLevelComponent = new StandaloneComponent();
        dynamicSelectModel.component = new Component();

        dynamicSelectModel.collection = new ElExpression("invalidCollection");

        dynamicSelectModel.getObjects();
    }

    @Test
    public void testGetComponent() {
        Component component = new Component();
        dynamicSelectModel.setComponent(component);
        assertSame(component, dynamicSelectModel.getComponent());
    }

    @Test
    public void testSetComponent() {
        Component component = new Component();
        dynamicSelectModel.setComponent(component);
        assertSame(component, dynamicSelectModel.getComponent());
    }

    @Test
    public void testGetTopLevelComponent() {
        StandaloneComponent topLevelComponent = new StandaloneComponent();
        dynamicSelectModel.setTopLevelComponent(topLevelComponent);
        assertSame(topLevelComponent, dynamicSelectModel.getTopLevelComponent());
    }

    @Test
    public void testSetTopLevelComponent() {
        StandaloneComponent topLevelComponent = new StandaloneComponent();
        dynamicSelectModel.setTopLevelComponent(topLevelComponent);
        assertSame(topLevelComponent, dynamicSelectModel.getTopLevelComponent());
    }

    @Test(expected = WheelException.class)
    public void testValidateWithNullCollection() {
        dynamicSelectModel.validate();
    }

    @Test(expected = WheelException.class)
    public void testValidateWithNullLabel() {
        dynamicSelectModel.collection = new ElExpression("collection");
        dynamicSelectModel.validate();
    }

    @Test(expected = WheelException.class)
    public void testValidateWithNullValue() {
        dynamicSelectModel.collection = new ElExpression("collection");
        dynamicSelectModel.label = new ElExpression("label");
        dynamicSelectModel.validate();
    }

    @Test
    public void testReset() {
        dynamicSelectModel.objects = Arrays.asList("option1", "option2", "option3");
        dynamicSelectModel.reset();
        assertNull(dynamicSelectModel.objects);
    }
}