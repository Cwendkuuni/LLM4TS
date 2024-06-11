package wheel.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import wheel.components.Component;
import wheel.exceptions.WheelException;
import wheel.expressions.ElExpression;
import wheel.expressions.ExpressionEvalException;
import wheel.expressions.ExpressionParseException;
import java.util.*;

public class DynamicSelectModelTest {

    private DynamicSelectModel model;

    @Before
    public void setup() {
        model = new DynamicSelectModel();
    }

    @Test
    public void testConstructorWithParams() {
        ElExpression collection = new ElExpression("collection");
        ElExpression label = new ElExpression("label");
        ElExpression value = new ElExpression("value");
        model = new DynamicSelectModel(collection, label, value);

        assertEquals(collection, model.collection);
        assertEquals(label, model.label);
        assertEquals(value, model.value);
    }

    @Test
    public void testDefaultConstructor() {
        assertNull(model.collection);
        assertNull(model.label);
        assertNull(model.value);
    }

    @Test
    public void testCollection() {
        String collection = "collection";
        model.collection(collection);

        assertEquals(new ElExpression(collection), model.collection);
    }

    @Test
    public void testLabel() {
        String label = "label";
        model.label(label);

        assertEquals(new ElExpression(label), model.label);
    }

    @Test
    public void testValue() {
        String value = "value";
        model.value(value);

        assertEquals(new ElExpression(value), model.value);
    }

    @Test
    public void testEnumeration() {
        String enumeration = "enumeration";
        model.enumeration(enumeration);

        assertEquals(new ElExpression(enumeration + ".values()"), model.collection);
        assertEquals(new ElExpression("toString()"), model.label);
        assertEquals(new ElExpression("ordinal()"), model.value);
    }

    @Test
    public void testTranslator() {
        String translator = "translator";
        model.translator(translator);

        assertEquals(new ElExpression(translator), model.translator);
    }

    @Test
    public void testEmpty() {
        Object emptyObject = new Object();
        model.empty(emptyObject);

        assertSame(emptyObject, model.emptyObject);
    }

    @Test
    public void testGetOptionCount() {
        Collection<Object> objects = new ArrayList<>();
        objects.add(new Object());
        objects.add(new Object());
        model.objects = objects;

        assertEquals(objects.size(), model.getOptionCount());
    }

    @Test
    public void testGetLabel() throws ExpressionParseException, ExpressionEvalException {
        ElExpression label = new ElExpression("label");
        model.label = label;
        Object object = new Object();
        model.objects = Arrays.asList(object);
        Component component = new Component();
        model.component = component;

        String expectedResult = object.toString();
        String actualResult = model.getLabel(0);

        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testGetValue() throws ExpressionParseException, ExpressionEvalException {
        ElExpression value = new ElExpression("value");
        model.value = value;
        Object object = new Object();
        model.objects = Arrays.asList(object);
        Component component = new Component();
        model.component = component;

        String expectedResult = object.toString();
        String actualResult = model.getValue(0);

        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testTranslateValueWithTranslator() throws ExpressionParseException, ExpressionEvalException {
        String value = "value";
        Object object = new Object();
        model.objects = Arrays.asList(object);
        model.value = new ElExpression(value);
        String translator = "translator";
        model.translator = new ElExpression(translator);

        Object expectedResult = new Object();
        Object actualResult = model.translateValue(value);

        assertSame(expectedResult, actualResult);
    }

    @Test
    public void testTranslateValueWithoutTranslator() throws ExpressionParseException, ExpressionEvalException {
        String value = "value";
        Object object = new Object();
        model.objects = Arrays.asList(object);
        model.value = new ElExpression(value);
        model.translator = null;

        Object expectedResult = object;
        Object actualResult = model.translateValue(value);

        assertSame(expectedResult, actualResult);
    }

    @Test
    public void testTranslateValueNotFound() {
        String value = "value";
        model.objects = Collections.emptyList();
        model.value = new ElExpression("otherValue");
        model.translator = null;

        assertNull(model.translateValue(value));
    }

    @Test
    public void testGetObjects() throws ExpressionParseException, ExpressionEvalException {
        ElExpression collection = new ElExpression("collection");
        model.collection = collection;
        Object[] array = new Object[]{new Object()};
        model.topLevelComponent = new StandaloneComponent();
        model.component = new Component();

        Collection<Object> expectedResult = Arrays.asList(array);
        Collection<Object> actualResult = model.getObjects();

        assertTrue(actualResult.containsAll(expectedResult));
        assertTrue(expectedResult.containsAll(actualResult));
    }

    @Test(expected = WheelException.class)
    public void testGetObjectsNullResult() throws ExpressionParseException, ExpressionEvalException {
        ElExpression collection = new ElExpression("collection");
        model.collection = collection;

        model.getObjects();
    }

    @Test(expected = WheelException.class)
    public void testGetObjectsInvalidResult() throws ExpressionParseException, ExpressionEvalException {
        ElExpression collection = new ElExpression("collection");
        model.collection = collection;
        model.topLevelComponent = new StandaloneComponent();
        model.component = new Component();

        model.getObjects();
    }

    @Test
    public void testGetComponent() {
        Component component = new Component();
        model.component = component;

        Component actualComponent = model.getComponent();

        assertSame(component, actualComponent);
    }

    @Test
    public void testSetComponent() {
        Component component = new Component();
        model.setComponent(component);

        assertSame(component, model.component);
    }

    @Test
    public void testGetTopLevelComponent() {
        StandaloneComponent topLevelComponent = new StandaloneComponent();
        model.topLevelComponent = topLevelComponent;

        StandaloneComponent actualTopLevelComponent = model.getTopLevelComponent();

        assertSame(topLevelComponent, actualTopLevelComponent);
    }

    @Test
    public void testSetTopLevelComponent() {
        StandaloneComponent topLevelComponent = new StandaloneComponent();
        model.setTopLevelComponent(topLevelComponent);

        assertSame(topLevelComponent, model.topLevelComponent);
    }

    @Test(expected = WheelException.class)
    public void testValidateNullCollection() {
        model.validate();
    }

    @Test(expected = WheelException.class)
    public void testValidateNullLabel() {
        model.collection = new ElExpression("collection");
        model.validate();
    }

    @Test(expected = WheelException.class)
    public void testValidateNullValue() {
        model.collection = new ElExpression("collection");
        model.label = new ElExpression("label");
        model.validate();
    }

    @Test
    public void testValidateValidModel() {
        model.collection = new ElExpression("collection");
        model.label = new ElExpression("label");
        model.value = new ElExpression("value");

        model.validate();
    }

    @Test
    public void testReset() {
        model.objects = new ArrayList<>();

        model.reset();

        assertNull(model.objects);
    }
}