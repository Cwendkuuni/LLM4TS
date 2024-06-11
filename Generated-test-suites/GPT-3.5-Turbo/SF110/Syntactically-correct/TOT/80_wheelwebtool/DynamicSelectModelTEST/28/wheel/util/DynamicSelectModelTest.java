
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

import wheel.components.*;
import wheel.*;

import java.util.*;

public class DynamicSelectModelTest {

    private DynamicSelectModel dynamicSelectModel;

    @Before
    public void setUp() {
        dynamicSelectModel = new DynamicSelectModel();
    }

    @Test
    public void testConstructorWithParameters() {
        ElExpression collection = new ElExpression("users");
        ElExpression label = new ElExpression("username");
        ElExpression value = new ElExpression("id");

        dynamicSelectModel = new DynamicSelectModel(collection, label, value);

        assertEquals(collection, dynamicSelectModel.collection);
        assertEquals(label, dynamicSelectModel.label);
        assertEquals(value, dynamicSelectModel.value);
    }

    @Test
    public void testCollection() {
        String collection = "users";

        dynamicSelectModel.collection(collection);

        assertEquals(new ElExpression(collection), dynamicSelectModel.collection);
    }

    @Test
    public void testLabel() {
        String label = "username";

        dynamicSelectModel.label(label);

        assertEquals(new ElExpression(label), dynamicSelectModel.label);
    }

    @Test
    public void testValue() {
        String value = "id";

        dynamicSelectModel.value(value);

        assertEquals(new ElExpression(value), dynamicSelectModel.value);
    }

    @Test
    public void testEnumeration() {
        String enumeration = "OrderStatus";

        dynamicSelectModel.enumeration(enumeration);

        assertEquals(new ElExpression(enumeration + ".values()"), dynamicSelectModel.collection);
        assertEquals(new ElExpression("toString()"), dynamicSelectModel.label);
        assertEquals(new ElExpression("ordinal()"), dynamicSelectModel.value);
    }

    @Test
    public void testTranslator() {
        String translator = "translator";

        dynamicSelectModel.translator(translator);

        assertEquals(new ElExpression(translator), dynamicSelectModel.translator);
    }

    @Test
    public void testEmpty() {
        Integer emptyObject = 0;

        dynamicSelectModel.empty(emptyObject);

        assertEquals(emptyObject, dynamicSelectModel.emptyObject);
    }

    @Test
    public void testGetOptionCount() {
        Collection<String> objects = new ArrayList<>();
        objects.add("Option 1");
        objects.add("Option 2");
        objects.add("Option 3");

        dynamicSelectModel.objects = objects;

        int expected = objects.size();
        int actual = dynamicSelectModel.getOptionCount();

        assertEquals(expected, actual);
    }

    @Test
    public void testGetLabel() {
        Collection<String> objects = new ArrayList<>();
        objects.add("Option 1");
        objects.add("Option 2");
        objects.add("Option 3");

        dynamicSelectModel.objects = objects;
        dynamicSelectModel.component = new StandaloneComponent();

        int index = 1;

        String expected = "Option 2";
        String actual = dynamicSelectModel.getLabel(index);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetValue() {
        Collection<Integer> objects = new ArrayList<>();
        objects.add(1);
        objects.add(2);
        objects.add(3);

        dynamicSelectModel.objects = objects;
        dynamicSelectModel.component = new StandaloneComponent();

        int index = 2;

        String expected = "3";
        String actual = dynamicSelectModel.getValue(index);

        assertEquals(expected, actual);
    }

    @Test
    public void testTranslateValueWithoutTranslator() {
        Collection<Integer> objects = new ArrayList<>();
        objects.add(1);
        objects.add(2);
        objects.add(3);

        dynamicSelectModel.objects = objects;
        dynamicSelectModel.component = new StandaloneComponent();
        dynamicSelectModel.value = new ElExpression("ordinal()");

        String value = "3";

        Object expected = 3;
        Object actual = dynamicSelectModel.translateValue(value);

        assertEquals(expected, actual);
    }

    @Test
    public void testTranslateValueWithTranslator() {
        Collection<String> objects = new ArrayList<>();
        objects.add("One");
        objects.add("Two");
        objects.add("Three");

        dynamicSelectModel.objects = objects;
        dynamicSelectModel.component = new StandaloneComponent();
        dynamicSelectModel.value = new ElExpression("toString()");
        dynamicSelectModel.translator = new ElExpression("translator");

        String value = "Two";

        Object expected = "Translated Value";
        Object actual = dynamicSelectModel.translateValue(value);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetObjectsWithCollection() {
        String collection = "users";
        StandaloneComponent topLevelComponent = new StandaloneComponent();

        dynamicSelectModel.collection(collection);
        dynamicSelectModel.topLevelComponent = topLevelComponent;

        Collection<String> expected = new ArrayList<>();
        expected.add("Option 1");
        expected.add("Option 2");
        expected.add("Option 3");

        dynamicSelectModel.objects = expected;

        Collection<?> actual = dynamicSelectModel.getObjects();

        assertEquals(expected, actual);
    }

    @Test
    public void testGetObjectsWithArray() {
        String collection = "users";
        StandaloneComponent topLevelComponent = new StandaloneComponent();

        dynamicSelectModel.collection(collection);
        dynamicSelectModel.topLevelComponent = topLevelComponent;

        Object[] objectsArray = {1, 2, 3};

        Collection<?> expected = new ArrayList<>();
        expected.add(1);
        expected.add(2);
        expected.add(3);

        dynamicSelectModel.objects = expected;

        Collection<?> actual = dynamicSelectModel.getObjects();

        assertEquals(expected, actual);
    }

    @Test(expected = WheelException.class)
    public void testGetObjectsWithNullResult() {
        String collection = "users";
        StandaloneComponent topLevelComponent = new StandaloneComponent();

        dynamicSelectModel.collection(collection);
        dynamicSelectModel.topLevelComponent = topLevelComponent;

        dynamicSelectModel.objects = null;

        dynamicSelectModel.getObjects();
    }

    @Test(expected = WheelException.class)
    public void testGetObjectsWithNullEvaluatedValue() {
        String collection = "users";
        StandaloneComponent topLevelComponent = new StandaloneComponent();

        dynamicSelectModel.collection(collection);
        dynamicSelectModel.topLevelComponent = topLevelComponent;

        dynamicSelectModel.objects = null;

        dynamicSelectModel.getObjects();
    }

    @Test
    public void testGetComponent() {
        Component expected = new Component();

        dynamicSelectModel.component = expected;

        Component actual = dynamicSelectModel.getComponent();

        assertEquals(expected, actual);
    }

    @Test
    public void testSetComponent() {
        Component expected = new Component();

        dynamicSelectModel.setComponent(expected);

        Component actual = dynamicSelectModel.component;

        assertEquals(expected, actual);
    }

    @Test
    public void testGetTopLevelComponent() {
        StandaloneComponent expected = new StandaloneComponent();

        dynamicSelectModel.topLevelComponent = expected;

        StandaloneComponent actual = dynamicSelectModel.getTopLevelComponent();

        assertEquals(expected, actual);
    }

    @Test
    public void testSetTopLevelComponent() {
        StandaloneComponent expected = new StandaloneComponent();

        dynamicSelectModel.setTopLevelComponent(expected);

        StandaloneComponent actual = dynamicSelectModel.topLevelComponent;

        assertEquals(expected, actual);
    }

    @Test(expected = WheelException.class)
    public void testValidateWithNullCollection() {
        dynamicSelectModel.validate();
    }

    @Test(expected = WheelException.class)
    public void testValidateWithNullLabel() {
        dynamicSelectModel.collection("users");

        dynamicSelectModel.validate();
    }

    @Test(expected = WheelException.class)
    public void testValidateWithNullValue() {
        dynamicSelectModel.collection("users");
        dynamicSelectModel.label("username");

        dynamicSelectModel.validate();
    }

    @Test
    public void testReset() {
        dynamicSelectModel.objects = new ArrayList<>();

        dynamicSelectModel.reset();

        assertNull(dynamicSelectModel.objects);
    }
}

