import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DynamicSelectModelTest {

    private DynamicSelectModel dynamicSelectModel;

    @Before
    public void setUp() {
        dynamicSelectModel = new DynamicSelectModel();
    }

    @Test
    public void testConstructorWithParameters() {
        ElExpression collection = new ElExpression("collection");
        ElExpression label = new ElExpression("label");
        ElExpression value = new ElExpression("value");

        dynamicSelectModel = new DynamicSelectModel(collection, label, value);
        
        assertEquals(collection, dynamicSelectModel.collection);
        assertEquals(label, dynamicSelectModel.label);
        assertEquals(value, dynamicSelectModel.value);
    }

    @Test
    public void testCollectionMethod() {
        String collection = "collection";

        DynamicSelectModel result = dynamicSelectModel.collection(collection);

        assertEquals(new ElExpression(collection), result.collection);
    }

    @Test
    public void testLabelMethod() {
        String label = "label";

        DynamicSelectModel result = dynamicSelectModel.label(label);

        assertEquals(new ElExpression(label), result.label);
    }

    @Test
    public void testValueMethod() {
        String value = "value";

        DynamicSelectModel result = dynamicSelectModel.value(value);

        assertEquals(new ElExpression(value), result.value);
    }

    @Test
    public void testEnumerationMethod() {
        String enumeration = "enumeration";

        DynamicSelectModel result = dynamicSelectModel.enumeration(enumeration);

        assertEquals(new ElExpression(enumeration + ".values()"), result.collection);
        assertEquals(new ElExpression("toString()"), result.label);
        assertEquals(new ElExpression("ordinal()"), result.value);
    }

    @Test
    public void testTranslatorMethod() {
        String translator = "translator";

        DynamicSelectModel result = dynamicSelectModel.translator(translator);

        assertEquals(new ElExpression(translator), result.translator);
    }

    @Test
    public void testEmptyMethod() {
        Object o = new Object();

        DynamicSelectModel result = dynamicSelectModel.empty(o);

        assertEquals(o, result.emptyObject);
    }

    @Test
    public void testGetOptionCount() {
        dynamicSelectModel.objects = Arrays.asList("Option1", "Option2", "Option3");

        int result = dynamicSelectModel.getOptionCount();

        assertEquals(3, result);
    }

    @Test
    public void testGetLabel() {
        dynamicSelectModel.label = new ElExpression("SomeLabel");
        dynamicSelectModel.component = new Component();

        String result = dynamicSelectModel.getLabel(0);

        assertEquals("SomeLabel", result);
    }

    @Test(expected = RuntimeException.class)
    public void testGetLabelWithInvalidIndex() {
        dynamicSelectModel.label = new ElExpression("SomeLabel");
        dynamicSelectModel.component = new Component();

        dynamicSelectModel.getLabel(10);
    }

    @Test
    public void testGetValue() {
        dynamicSelectModel.value = new ElExpression("SomeValue");
        dynamicSelectModel.component = new Component();

        String result = dynamicSelectModel.getValue(0);
        
        assertEquals("SomeValue", result);
    }

    @Test(expected = RuntimeException.class)
    public void testGetValueWithInvalidIndex() {
        dynamicSelectModel.value = new ElExpression("SomeValue");
        dynamicSelectModel.component = new Component();

        dynamicSelectModel.getValue(10);
    }

    @Test
    public void testTranslateValue() {
        dynamicSelectModel.translator = new ElExpression("SomeTranslator");
        dynamicSelectModel.collection = new ElExpression("collection");
        dynamicSelectModel.component = new Component();

        dynamicSelectModel.objects = Arrays.asList("Option1", "Option2", "Option3");

        Object result = dynamicSelectModel.translateValue("Option2");

        assertEquals("SomeTranslator", result);
    }

    @Test
    public void testTranslateValueWithNullTranslator() {
        dynamicSelectModel.collection = new ElExpression("collection");
        dynamicSelectModel.component = new Component();

        dynamicSelectModel.objects = Arrays.asList("Option1", "Option2", "Option3");

        Object result = dynamicSelectModel.translateValue("Option2");

        assertEquals("Option2", result);
    }

    @Test
    public void testTranslateValueWithNotFoundValue() {
        dynamicSelectModel.collection = new ElExpression("collection");
        dynamicSelectModel.component = new Component();

        dynamicSelectModel.objects = Arrays.asList("Option1", "Option2", "Option3");

        Object result = dynamicSelectModel.translateValue("Option5");

        assertNull(result);
    }

    @Test
    public void testGetObjects() {
        dynamicSelectModel.collection = new ElExpression("collection");
        dynamicSelectModel.topLevelComponent = new StandaloneComponent();
        dynamicSelectModel.component = new Component();

        dynamicSelectModel.objects = Arrays.asList("Option1", "Option2", "Option3");

        Collection result = dynamicSelectModel.getObjects();

        assertEquals(3, result.size());
        assertTrue(result.contains("Option1"));
        assertTrue(result.contains("Option2"));
        assertTrue(result.contains("Option3"));
    }

    @Test(expected = RuntimeException.class)
    public void testGetObjectsWithNullResult() {
        dynamicSelectModel.collection = new ElExpression("collection");
        dynamicSelectModel.topLevelComponent = new StandaloneComponent();
        dynamicSelectModel.component = new Component();

        dynamicSelectModel.objects = null;

        dynamicSelectModel.getObjects();
    }

    @Test(expected = WheelException.class)
    public void testGetObjectsWithNullEvaluatedResult() {
        dynamicSelectModel.collection = new ElExpression("nonExistentCollection");
        dynamicSelectModel.topLevelComponent = new StandaloneComponent();
        dynamicSelectModel.component = new Component();

        dynamicSelectModel.getObjects();
    }

    @Test(expected = WheelException.class)
    public void testGetObjectsWithNonCollectionResult() {
        dynamicSelectModel.collection = new ElExpression("nonCollection");
        dynamicSelectModel.topLevelComponent = new StandaloneComponent();
        dynamicSelectModel.component = new Component();

        dynamicSelectModel.getObjects();
    }

    @Test
    public void testGetObjectsWithEmptyObject() {
        dynamicSelectModel.collection = new ElExpression("collection");
        dynamicSelectModel.topLevelComponent = new StandaloneComponent();
        dynamicSelectModel.component = new Component();

        dynamicSelectModel.objects = Arrays.asList("Option1", "Option2", "Option3");
        dynamicSelectModel.emptyObject = "EmptyOption";

        Collection result = dynamicSelectModel.getObjects();

        assertEquals(4, result.size());
        assertTrue(result.contains("EmptyOption"));
        assertTrue(result.contains("Option1"));
        assertTrue(result.contains("Option2"));
        assertTrue(result.contains("Option3"));
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

        assertEquals(component, dynamicSelectModel.component);
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

        assertEquals(topLevelComponent, dynamicSelectModel.topLevelComponent);
    }

    @Test(expected = WheelException.class)
    public void testValidateWithNullCollection() {
        dynamicSelectModel.label = new ElExpression("SomeLabel");
        dynamicSelectModel.value = new ElExpression("SomeValue");

        dynamicSelectModel.validate();
    }

    @Test(expected = WheelException.class)
    public void testValidateWithNullLabel() {
        dynamicSelectModel.collection = new ElExpression("SomeCollection");
        dynamicSelectModel.value = new ElExpression("SomeValue");

        dynamicSelectModel.validate();
    }

    @Test(expected = WheelException.class)
    public void testValidateWithNullValue() {
        dynamicSelectModel.collection = new ElExpression("SomeCollection");
        dynamicSelectModel.label = new ElExpression("SomeLabel");

        dynamicSelectModel.validate();
    }

    @Test
    public void testReset() {
        dynamicSelectModel.objects = Arrays.asList("Option1", "Option2", "Option3");

        dynamicSelectModel.reset();

        assertNull(dynamicSelectModel.objects);
    }
}