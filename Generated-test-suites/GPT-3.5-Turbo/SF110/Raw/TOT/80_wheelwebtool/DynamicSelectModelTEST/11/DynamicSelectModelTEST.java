import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import wheel.components.Component;
import wheel.components.StandaloneComponent;
import wheel.ElExpression;
import wheel.util.DynamicSelectModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class DynamicSelectModelTest {

    private DynamicSelectModel dynamicSelectModel;

    @Before
    public void setup() {
        dynamicSelectModel = new DynamicSelectModel();
    }

    @Test
    public void testConstructor() {
        // typical use case
        ElExpression collection = new ElExpression("collection");
        ElExpression label = new ElExpression("label");
        ElExpression value = new ElExpression("value");

        dynamicSelectModel = new DynamicSelectModel(collection, label, value);

        Assert.assertEquals(collection, dynamicSelectModel.getCollection());
        Assert.assertEquals(label, dynamicSelectModel.getLabel());
        Assert.assertEquals(value, dynamicSelectModel.getValue());
    }

    @Test
    public void testCollection() {
        // typical use case
        String collectionExpression = "collectionExpression";

        dynamicSelectModel.collection(collectionExpression);

        Assert.assertEquals(collectionExpression, dynamicSelectModel.getCollectionExpression());
    }

    @Test
    public void testLabel() {
        // typical use case
        String labelExpression = "labelExpression";

        dynamicSelectModel.label(labelExpression);

        Assert.assertEquals(labelExpression, dynamicSelectModel.getLabelExpression());
    }

    @Test
    public void testValue() {
        // typical use case
        String valueExpression = "valueExpression";

        dynamicSelectModel.value(valueExpression);

        Assert.assertEquals(valueExpression, dynamicSelectModel.getValueExpression());
    }

    @Test
    public void testEnumeration() {
        // typical use case
        final String enumeration = "enumeration";

        dynamicSelectModel.enumeration(enumeration);

        Assert.assertEquals(enumeration + ".values()", dynamicSelectModel.getCollectionExpression());
        Assert.assertEquals("toString()", dynamicSelectModel.getLabelExpression());
        Assert.assertEquals("ordinal()", dynamicSelectModel.getValueExpression());
    }

    @Test
    public void testTranslator() {
        // typical use case
        final String translator = "translator";

        dynamicSelectModel.translator(translator);

        Assert.assertEquals(translator, dynamicSelectModel.getTranslatorExpression());
    }

    @Test
    public void testEmpty() {
        // typical use case
        final Object emptyObject = new Object();

        dynamicSelectModel.empty(emptyObject);

        Assert.assertEquals(emptyObject, dynamicSelectModel.getEmptyObject());
    }

    @Test
    public void testGetOptionCount() {
        // typical use case
        Collection<Object> objects = new ArrayList<>();
        dynamicSelectModel.setObjects(objects);

        int optionCount = dynamicSelectModel.getOptionCount();

        Assert.assertEquals(objects.size(), optionCount);
    }

    @Test
    public void testGetLabel() {
        // typical use case
        Collection<Object> objects = new ArrayList<>();
        Object object = new Object();
        objects.add(object);
        dynamicSelectModel.setObjects(objects);

        String label = dynamicSelectModel.getLabel(0);

        Assert.assertNotNull(label);
    }

    @Test
    public void testGetLabel_ExpressionError() {
        // error scenario
        Collection<Object> objects = new ArrayList<>();
        Object object = new Object();
        objects.add(object);
        dynamicSelectModel.setObjects(objects);
        dynamicSelectModel.setLabelExpression(null);

        String label = dynamicSelectModel.getLabel(0);

        Assert.assertNull(label);
    }

    @Test
    public void testGetValue() {
        // typical use case
        Collection<Object> objects = new ArrayList<>();
        Object object = new Object();
        objects.add(object);
        dynamicSelectModel.setObjects(objects);

        String value = dynamicSelectModel.getValue(0);

        Assert.assertNotNull(value);
    }

    @Test
    public void testGetValue_ExpressionError() {
        // error scenario
        Collection<Object> objects = new ArrayList<>();
        Object object = new Object();
        objects.add(object);
        dynamicSelectModel.setObjects(objects);
        dynamicSelectModel.setValueExpression(null);

        String value = dynamicSelectModel.getValue(0);

        Assert.assertNull(value);
    }

    @Test
    public void testTranslateValue() {
        // typical use case
        Collection<Object> objects = new ArrayList<>();
        Object object = new Object();
        objects.add(object);
        dynamicSelectModel.setObjects(objects);

        String value = "value";

        Object translatedValue = dynamicSelectModel.translateValue(value);

        Assert.assertNotNull(translatedValue);
    }

    @Test
    public void testTranslateValue_TranslatorExpressionError() {
        // error scenario
        Collection<Object> objects = new ArrayList<>();
        Object object = new Object();
        objects.add(object);
        dynamicSelectModel.setObjects(objects);
        dynamicSelectModel.setTranslatorExpression(null);

        String value = "value";

        Object translatedValue = dynamicSelectModel.translateValue(value);

        Assert.assertNull(translatedValue);
    }

    @Test
    public void testGetObjects() {
        // typical use case
        ElExpression collection = new ElExpression("collection");
        Component component = new Component();
        StandaloneComponent topLevelComponent = new StandaloneComponent();
        dynamicSelectModel.setCollectionExpression(collection);
        dynamicSelectModel.setComponent(component);
        dynamicSelectModel.setTopLevelComponent(topLevelComponent);

        Collection<Object> objects = dynamicSelectModel.getObjects();

        Assert.assertNotNull(objects);
    }

    @Test(expected = WheelException.class)
    public void testGetObjects_CollectionNull() {
        // error scenario
        Component component = new Component();
        StandaloneComponent topLevelComponent = new StandaloneComponent();
        dynamicSelectModel.setCollectionExpression(null);
        dynamicSelectModel.setComponent(component);
        dynamicSelectModel.setTopLevelComponent(topLevelComponent);

        dynamicSelectModel.getObjects();
    }

    @Test(expected = WheelException.class)
    public void testGetObjects_CollectionEvaluatedToNull() {
        // error scenario
        ElExpression collection = new ElExpression("collection");
        Component component = new Component();
        StandaloneComponent topLevelComponent = new StandaloneComponent();
        dynamicSelectModel.setCollectionExpression(collection);
        dynamicSelectModel.setComponent(component);
        dynamicSelectModel.setTopLevelComponent(topLevelComponent);

        dynamicSelectModel.getObjects();
    }

    @Test(expected = WheelException.class)
    public void testGetObjects_CollectionEvaluatedToNonCollectionOrArrayOfObjects() {
        // error scenario
        ElExpression collection = new ElExpression("collection");
        Component component = new Component();
        StandaloneComponent topLevelComponent = new StandaloneComponent();
        dynamicSelectModel.setCollectionExpression(collection);
        dynamicSelectModel.setComponent(component);
        dynamicSelectModel.setTopLevelComponent(topLevelComponent);

        Object obj = new Object();
        Object[] array = new Object[]{obj};
        dynamicSelectModel.setCollectionResult(array);

        dynamicSelectModel.getObjects();
    }

    @Test
    public void testGetObjects_EmptyObjectNotNull() {
        // typical use case
        ElExpression collection = new ElExpression("collection");
        Component component = new Component();
        StandaloneComponent topLevelComponent = new StandaloneComponent();
        dynamicSelectModel.setCollectionExpression(collection);
        dynamicSelectModel.setComponent(component);
        dynamicSelectModel.setTopLevelComponent(topLevelComponent);
        List<Object> objectsList = new ArrayList<>();
        dynamicSelectModel.setCollectionResult(objectsList);
        Object emptyObject = new Object();
        dynamicSelectModel.setEmptyObject(emptyObject);

        Collection<Object> objects = dynamicSelectModel.getObjects();

        Assert.assertNotNull(objects);
        Assert.assertEquals(objectsList.size() + 1, objects.size());
        Assert.assertTrue(objects.contains(emptyObject));
        Assert.assertTrue(objects.containsAll(objectsList));
    }
    
    @Test
    public void testGetComponent() {
        // typical use case
        Component component = new Component();
        dynamicSelectModel.setComponent(component);

        Component result = dynamicSelectModel.getComponent();

        Assert.assertEquals(component, result);
    }

    @Test
    public void testSetComponent() {
        // typical use case
        Component component = new Component();

        dynamicSelectModel.setComponent(component);

        Assert.assertEquals(component, dynamicSelectModel.getComponent());
    }

    @Test
    public void testGetTopLevelComponent() {
        // typical use case
        StandaloneComponent topLevelComponent = new StandaloneComponent();
        dynamicSelectModel.setTopLevelComponent(topLevelComponent);

        StandaloneComponent result = dynamicSelectModel.getTopLevelComponent();

        Assert.assertEquals(topLevelComponent, result);
    }

    @Test
    public void testSetTopLevelComponent() {
        // typical use case
        StandaloneComponent topLevelComponent = new StandaloneComponent();

        dynamicSelectModel.setTopLevelComponent(topLevelComponent);

        Assert.assertEquals(topLevelComponent, dynamicSelectModel.getTopLevelComponent());
    }

    @Test(expected = WheelException.class)
    public void testValidate_CollectionNull() {
        // error scenario
        dynamicSelectModel.setCollectionExpression(null);

        dynamicSelectModel.validate();
    }

    @Test(expected = WheelException.class)
    public void testValidate_LabelNull() {
        // error scenario
        dynamicSelectModel.setLabelExpression(null);

        dynamicSelectModel.validate();
    }

    @Test(expected = WheelException.class)
    public void testValidate_ValueNull() {
        // error scenario
        dynamicSelectModel.setValueExpression(null);

        dynamicSelectModel.validate();
    }

    @Test
    public void testReset() {
        // typical use case
        dynamicSelectModel.reset();

        Assert.assertNull(dynamicSelectModel.getObjects());
    }

}