import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import wheel.components.Component;
import wheel.components.StandaloneComponent;
import wheel.exceptions.WheelException;
import wheel.util.DynamicSelectModel;

import java.util.*;

public class DynamicSelectModelTest {

    private DynamicSelectModel dynamicSelectModel;
    private Component component;
    private StandaloneComponent standaloneComponent;
    private String collection;
    private String label;
    private String value;
    private String translator;
    private Object emptyObject;

    @Before
    public void setUp() {
        collection = "collection";
        label = "label";
        value = "value";
        translator = "translator";
        emptyObject = new Object();

        dynamicSelectModel = new DynamicSelectModel();
        component = new Component();
        standaloneComponent = new StandaloneComponent();
    }

    // Test cases for constructor with parameters
    @Test
    public void constructor_withParameters_shouldSetCollectionLabelAndValue() {
        dynamicSelectModel = new DynamicSelectModel(new ElExpression(collection), new ElExpression(label), new ElExpression(value));

        Assert.assertEquals(collection, dynamicSelectModel.collection.getExpression());
        Assert.assertEquals(label, dynamicSelectModel.label.getExpression());
        Assert.assertEquals(value, dynamicSelectModel.value.getExpression());
    }

    // Test cases for collection()
    @Test
    public void collection_shouldSetCollectionExpression() {
        dynamicSelectModel.collection(collection);

        Assert.assertEquals(collection, dynamicSelectModel.collection.getExpression());
    }

    // Test cases for label()
    @Test
    public void label_shouldSetLabelExpression() {
        dynamicSelectModel.label(label);

        Assert.assertEquals(label, dynamicSelectModel.label.getExpression());
    }

    // Test cases for value()
    @Test
    public void value_shouldSetValueExpression() {
        dynamicSelectModel.value(value);

        Assert.assertEquals(value, dynamicSelectModel.value.getExpression());
    }

    // Test cases for enumeration()
    @Test
    public void enumeration_shouldSetCollectionLabelAndValueFromEnumeration() {
        dynamicSelectModel.enumeration("Enumeration");

        Assert.assertEquals("Enumeration.values()", dynamicSelectModel.collection.getExpression());
        Assert.assertEquals("toString()", dynamicSelectModel.label.getExpression());
        Assert.assertEquals("ordinal()", dynamicSelectModel.value.getExpression());
    }

    // Test cases for translator()
    @Test
    public void translator_shouldSetTranslatorExpression() {
        dynamicSelectModel.translator(translator);

        Assert.assertEquals(translator, dynamicSelectModel.translator.getExpression());
    }

    // Test cases for empty()
    @Test
    public void empty_shouldSetEmptyObject() {
        dynamicSelectModel.empty(emptyObject);

        Assert.assertEquals(emptyObject, dynamicSelectModel.emptyObject);
    }

    // Test cases for getOptionCount()
    @Test
    public void getOptionCount_shouldReturnObjectsSize() {
        dynamicSelectModel.setComponent(component);
        dynamicSelectModel.setTopLevelComponent(standaloneComponent);
        dynamicSelectModel.collection = new ElExpression(collection + ".size()");
        Collection<Integer> objects = Arrays.asList(1, 2, 3);
        dynamicSelectModel.objects = objects;

        Assert.assertEquals(objects.size(), dynamicSelectModel.getOptionCount());
    }

    // Test cases for getLabel()
    @Test
    public void getLabel_shouldReturnLabelFromExpression() {
        dynamicSelectModel.setComponent(component);
        dynamicSelectModel.collection = new ElExpression(collection + ".toArray()[0]");
        dynamicSelectModel.label = new ElExpression(label);

        Assert.assertEquals("Label", dynamicSelectModel.getLabel(0));
    }

    @Test(expected = WheelException.class)
    public void getLabel_withInvalidExpression_shouldThrowWheelException() {
        dynamicSelectModel.setComponent(component);
        dynamicSelectModel.collection = new ElExpression(collection + ".toArray()[0]");
        dynamicSelectModel.label = new ElExpression(label + ".invalidMethod()");

        dynamicSelectModel.getLabel(0);
    }

    // Test cases for getValue()
    @Test
    public void getValue_shouldReturnValueFromExpression() {
        dynamicSelectModel.setComponent(component);
        dynamicSelectModel.collection = new ElExpression(collection + ".toArray()[0]");
        dynamicSelectModel.value = new ElExpression(value);

        Assert.assertEquals("Value", dynamicSelectModel.getValue(0));
    }

    @Test(expected = WheelException.class)
    public void getValue_withInvalidExpression_shouldThrowWheelException() {
        dynamicSelectModel.setComponent(component);
        dynamicSelectModel.collection = new ElExpression(collection + ".toArray()[0]");
        dynamicSelectModel.value = new ElExpression(value + ".invalidMethod()");

        dynamicSelectModel.getValue(0);
    }

    // Test cases for translateValue()
    @Test
    public void translateValue_withMatchingValue_shouldReturnTranslatedValue() {
        dynamicSelectModel.setComponent(component);
        dynamicSelectModel.setTopLevelComponent(standaloneComponent);
        dynamicSelectModel.collection = new ElExpression(collection + ".toArray()");
        dynamicSelectModel.value = new ElExpression(value);
        dynamicSelectModel.translator = new ElExpression(translator);

        Assert.assertEquals("TranslatedValue", dynamicSelectModel.translateValue("Value"));
    }

    @Test
    public void translateValue_withNoMatchingValueAndNoTranslator_shouldReturnNull() {
        dynamicSelectModel.setComponent(component);
        dynamicSelectModel.setTopLevelComponent(standaloneComponent);
        dynamicSelectModel.collection = new ElExpression(collection + ".toArray()");
        dynamicSelectModel.value = new ElExpression(value);

        Assert.assertNull(dynamicSelectModel.translateValue("InvalidValue"));
    }

    @Test(expected = WheelException.class)
    public void translateValue_withNoMatchingValueAndInvalidTranslatorExpression_shouldThrowWheelException() {
        dynamicSelectModel.setComponent(component);
        dynamicSelectModel.setTopLevelComponent(standaloneComponent);
        dynamicSelectModel.collection = new ElExpression(collection + ".toArray()");
        dynamicSelectModel.value = new ElExpression(value);
        dynamicSelectModel.translator = new ElExpression(translator + ".invalidMethod()");

        dynamicSelectModel.translateValue("InvalidValue");
    }

    // Test cases for getObjects()
    @Test
    public void getObjects_withCachedObjects_shouldReturnCachedObjects() {
        Collection<Object> objects = new ArrayList<>();
        dynamicSelectModel.objects = objects;

        Assert.assertEquals(objects, dynamicSelectModel.getObjects());
    }

    @Test(expected = WheelException.class)
    public void getObjects_withInvalidCollectionExpression_shouldThrowWheelException() {
        dynamicSelectModel.collection = new ElExpression(collection + ".invalidMethod()");

        dynamicSelectModel.getObjects();
    }

    @Test(expected = WheelException.class)
    public void getObjects_withNullCollectionExpression_shouldThrowWheelException() {
        dynamicSelectModel.collection = null;

        dynamicSelectModel.getObjects();
    }

    @Test(expected = WheelException.class)
    public void getObjects_withNullEvaluatedCollection_shouldThrowWheelException() {
        dynamicSelectModel.collection = new ElExpression(collection);
        dynamicSelectModel.topLevelComponent = standaloneComponent;

        dynamicSelectModel.getObjects();
    }

    @Test(expected = WheelException.class)
    public void getObjects_withNonCollectionAndNonArrayEvaluatedCollection_shouldThrowWheelException() {
        dynamicSelectModel.collection = new ElExpression(collection);
        dynamicSelectModel.topLevelComponent = standaloneComponent;
        dynamicSelectModel.objects = new ArrayList<>();

        dynamicSelectModel.getObjects();
    }

    @Test
    public void getObjects_withArrayEvaluatedCollection_shouldReturnCollectionOfArrayElements() {
        dynamicSelectModel.collection = new ElExpression("new Integer[]{1, 2, 3}");

        Collection<Object> expectedObjects = new ArrayList<>();
        expectedObjects.add(1);
        expectedObjects.add(2);
        expectedObjects.add(3);

        Assert.assertEquals(expectedObjects, dynamicSelectModel.getObjects());
    }

    @Test
    public void getObjects_withValidCollectionExpression_shouldReturnCollection() {
        dynamicSelectModel.collection = new ElExpression(collection + ".toArray()");
        dynamicSelectModel.topLevelComponent = standaloneComponent;

        Collection<Object> objects = new ArrayList<>();
        dynamicSelectModel.objects = objects;

        Assert.assertEquals(objects, dynamicSelectModel.getObjects());
    }

    // Test cases for validate()
    @Test(expected = WheelException.class)
    public void validate_withNullCollectionExpression_shouldThrowWheelException() {
        dynamicSelectModel.validate();
    }

    @Test(expected = WheelException.class)
    public void validate_withNullLabelExpression_shouldThrowWheelException() {
        dynamicSelectModel.collection = new ElExpression(collection);

        dynamicSelectModel.validate();
    }

    @Test(expected = WheelException.class)
    public void validate_withNullValueExpression_shouldThrowWheelException() {
        dynamicSelectModel.collection = new ElExpression(collection);
        dynamicSelectModel.label = new ElExpression(label);

        dynamicSelectModel.validate();
    }

    // Test cases for reset()
    @Test
    public void reset_shouldSetObjectsToNull() {
        dynamicSelectModel.objects = new ArrayList<>();

        dynamicSelectModel.reset();

        Assert.assertNull(dynamicSelectModel.objects);
    }

}