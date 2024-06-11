package wheel.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

public class DynamicSelectModelTest {

    private DynamicSelectModel selectModel;
    private Component component;
    private StandaloneComponent topLevelComponent;
    private ElExpression label;
    private ElExpression value;
    private ElExpression collection;
    private ElExpression translator;
    private Collection objects;
    private Object emptyObject;

    @Before
    public void setUp() {
        component = mock(Component.class);
        topLevelComponent = mock(StandaloneComponent.class);
        label = new ElExpression("label");
        value = new ElExpression("value");
        collection = new ElExpression("collection");
        translator = new ElExpression("translator");
        selectModel = new DynamicSelectModel(collection, label, value);
        selectModel.setComponent(component);
        selectModel.setTopLevelComponent(topLevelComponent);
        objects = new ArrayList<>();
        emptyObject = new Object();
        selectModel.reset();
    }

    @Test
    public void testConstructorWithArguments() {
        assertEquals(collection, selectModel.collection);
        assertEquals(label, selectModel.label);
        assertEquals(value, selectModel.value);
    }

    @Test
    public void testDefaultConstructor() {
        DynamicSelectModel model = new DynamicSelectModel();
        assertNull(model.collection);
        assertNull(model.label);
        assertNull(model.value);
    }

    @Test
    public void testCollection() {
        String collectionString = "collection";
        DynamicSelectModel model = selectModel.collection(collectionString);
        assertEquals(collectionString, model.collection.getExpression());
        assertEquals(label, model.label);
        assertEquals(value, model.value);
    }

    @Test
    public void testLabel() {
        String labelString = "label";
        DynamicSelectModel model = selectModel.label(labelString);
        assertEquals(collection, model.collection);
        assertEquals(labelString, model.label.getExpression());
        assertEquals(value, model.value);
    }

    @Test
    public void testValue() {
        String valueString = "value";
        DynamicSelectModel model = selectModel.value(valueString);
        assertEquals(collection, model.collection);
        assertEquals(label, model.label);
        assertEquals(valueString, model.value.getExpression());
    }

    @Test
    public void testEnumeration() {
        String enumerationString = "enumeration";
        DynamicSelectModel model = selectModel.enumeration(enumerationString);
        assertEquals(collectionString + ".values()", model.collection.getExpression());
        assertEquals("toString()", model.label.getExpression());
        assertEquals("ordinal()", model.value.getExpression());
    }

    @Test
    public void testTranslator() {
        String translatorString = "translator";
        DynamicSelectModel model = selectModel.translator(translatorString);
        assertEquals(collection, model.collection);
        assertEquals(label, model.label);
        assertEquals(value, model.value);
        assertEquals(translatorString, model.translator.getExpression());
    }

    @Test
    public void testEmpty() {
        Object empty = new Object();
        DynamicSelectModel model = selectModel.empty(empty);
        assertEquals(collection, model.collection);
        assertEquals(label, model.label);
        assertEquals(value, model.value);
        assertEquals(empty, model.emptyObject);
    }

    @Test
    public void testGetOptionCount() {
        Collection<Object> objects = new ArrayList<>();
        objects.add(new Object());
        objects.add(new Object());
        selectModel.objects = objects;
        assertEquals(objects.size(), selectModel.getOptionCount());
    }

    @Test
    public void testGetLabel() {
        Object object = new Object();
        Collection<Object> objects = new ArrayList<>();
        objects.add(object);
        selectModel.objects = objects;
        assertEquals(object.toString(), selectModel.getLabel(0));
    }

    @Test
    public void testGetValue() {
        Object object = new Object();
        Collection<Object> objects = new ArrayList<>();
        objects.add(object);
        selectModel.objects = objects;
        assertEquals(object.toString(), selectModel.getValue(0));
    }
    
    @Test
    public void testTranslateValue() {
        Object object1 = new Object();
        Object object2 = new Object();
        Collection<Object> objects = new ArrayList<>();
        objects.add(object1);
        objects.add(object2);
        selectModel.objects = objects;
        selectModel.translator = translator;
        assertEquals(object1, selectModel.translateValue(object1.toString()));
    }

    @Test
    public void testGetObjects() {
        selectModel.objects = objects;
        assertEquals(objects, selectModel.getObjects());
    }

    @Test
    public void testGetComponent() {
        assertEquals(component, selectModel.getComponent());
    }

    @Test
    public void testSetComponent() {
        Component newComponent = mock(Component.class);
        selectModel.setComponent(newComponent);
        assertEquals(newComponent, selectModel.getComponent());
    }

    @Test
    public void testGetTopLevelComponent() {
        assertEquals(topLevelComponent, selectModel.getTopLevelComponent());
    }

    @Test
    public void testSetTopLevelComponent() {
        StandaloneComponent newTopLevelComponent = mock(StandaloneComponent.class);
        selectModel.setTopLevelComponent(newTopLevelComponent);
        assertEquals(newTopLevelComponent, selectModel.getTopLevelComponent());
    }

    @Test(expected = WheelException.class)
    public void testValidateWithoutCollection() {
        selectModel.collection = null;
        selectModel.validate();
    }

    @Test(expected = WheelException.class)
    public void testValidateWithoutLabel() {
        selectModel.label = null;
        selectModel.validate();
    }

    @Test(expected = WheelException.class)
    public void testValidateWithoutValue() {
        selectModel.value = null;
        selectModel.validate();
    }

    @Test
    public void testReset() {
        selectModel.objects = objects;
        selectModel.reset();
        assertNull(selectModel.objects);
    }
}