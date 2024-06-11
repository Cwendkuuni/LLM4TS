
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import wheel.components.Component;
import wheel.components.StandaloneComponent;
import wheel.util.DynamicSelectModel;

public class DynamicSelectModelTest {
    private DynamicSelectModel dynamicSelectModel;
    private Component component;
    private StandaloneComponent topLevelComponent;
    private String collection;
    private String label;
    private String value;

    @Before
    public void setUp() {
        collection = "testCollection";
        label = "testLabel";
        value = "testValue";
        dynamicSelectModel = new DynamicSelectModel(collection, label, value);
        dynamicSelectModel.setComponent(component);
        dynamicSelectModel.setTopLevelComponent(topLevelComponent);
    }

    @Test
    public void testConstructor() {
        assertEquals(collection, dynamicSelectModel.collection);
        assertEquals(label, dynamicSelectModel.label);
        assertEquals(value, dynamicSelectModel.value);
    }

    @Test
    public void testCollection() {
        String newCollection = "newCollection";
        DynamicSelectModel result = dynamicSelectModel.collection(newCollection);
        assertEquals(newCollection, dynamicSelectModel.collection);
        assertSame(dynamicSelectModel, result);
    }

    @Test
    public void testLabel() {
        String newLabel = "newLabel";
        DynamicSelectModel result = dynamicSelectModel.label(newLabel);
        assertEquals(newLabel, dynamicSelectModel.label);
        assertSame(dynamicSelectModel, result);
    }

    @Test
    public void testValue() {
        String newValue = "newValue";
        DynamicSelectModel result = dynamicSelectModel.value(newValue);
        assertEquals(newValue, dynamicSelectModel.value);
        assertSame(dynamicSelectModel, result);
    }

    @Test
    public void testEnumeration() {
        String enumeration = "testEnumeration";
        DynamicSelectModel result = dynamicSelectModel.enumeration(enumeration);
        assertEquals(enumeration + ".values()", dynamicSelectModel.collection);
        assertEquals("toString()", dynamicSelectModel.label);
        assertEquals("ordinal()", dynamicSelectModel.value);
        assertSame(dynamicSelectModel, result);
    }

    @Test
    public void testTranslator() {
        String translator = "testTranslator";
        DynamicSelectModel result = dynamicSelectModel.translator(translator);
        assertEquals(translator, dynamicSelectModel.translator);
        assertSame(dynamicSelectModel, result);
    }

    @Test
    public void testEmpty() {
        Object emptyObject = new Object();
        DynamicSelectModel result = dynamicSelectModel.empty(emptyObject);
        assertEquals(emptyObject, dynamicSelectModel.emptyObject);
        assertSame(dynamicSelectModel, result);
    }

    @Test
    public void testGetOptionCount() {
        int expected = 0;
        assertEquals(expected, dynamicSelectModel.getOptionCount());
    }

    @Test
    public void testGetLabel() {
        int index = 0;
        String expected = "";
        assertEquals(expected, dynamicSelectModel.getLabel(index));
    }

    @Test
    public void testGetValue() {
        int index = 0;
        String expected = "";
        assertEquals(expected, dynamicSelectModel.getValue(index));
    }

    @Test
    public void testTranslateValue() {
        String value = "";
        assertNull(dynamicSelectModel.translateValue(value));
    }

    @Test
    public void testGetObjects() {
        assertNull(dynamicSelectModel.objects);
        // test with null expression
        dynamicSelectModel.getObjects();
        assertNotNull(dynamicSelectModel.objects);
        // test with empty collection
        Collection emptyCollection = new ArrayList<>();
        dynamicSelectModel.objects = emptyCollection;
        assertEquals(emptyCollection, dynamicSelectModel.getObjects());
        // test with non-empty collection
        Collection nonEmptyCollection = new ArrayList<>();
        nonEmptyCollection.add(new Object());
        dynamicSelectModel.objects = nonEmptyCollection;
        assertEquals(nonEmptyCollection, dynamicSelectModel.getObjects());
        // test with emptyObject set
        dynamicSelectModel.emptyObject = new Object();
        Collection newCollection = new LinkedList<>();
        newCollection.add(dynamicSelectModel.emptyObject);
        newCollection.addAll(nonEmptyCollection);
        dynamicSelectModel.objects = null;
        assertEquals(newCollection, dynamicSelectModel.getObjects());
    }

    @Test
    public void testGetComponent() {
        assertSame(component, dynamicSelectModel.getComponent());
    }

    @Test
    public void testSetComponent() {
        Component newComponent = new Component();
        dynamicSelectModel.setComponent(newComponent);
        assertSame(newComponent, dynamicSelectModel.getComponent());
    }

    @Test
    public void testGetTopLevelComponent() {
        assertSame(topLevelComponent, dynamicSelectModel.getTopLevelComponent());
    }

    @Test
    public void testSetTopLevelComponent() {
        StandaloneComponent newTopLevelComponent = new StandaloneComponent();
        dynamicSelectModel.setTopLevelComponent(newTopLevelComponent);
        assertSame(newTopLevelComponent, dynamicSelectModel.getTopLevelComponent());
    }

    @Test(expected = WheelException.class)
    public void testValidateCollectionNull() {
        DynamicSelectModel invalidModel = new DynamicSelectModel();
        invalidModel.validate();
    }

    @Test(expected = WheelException.class)
    public void testValidateLabelNull() {
        DynamicSelectModel invalidModel = new DynamicSelectModel(collection, null, value);
        invalidModel.validate();
    }

    @Test(expected = WheelException.class)
    public void testValidateValueNull() {
        DynamicSelectModel invalidModel = new DynamicSelectModel(collection, label, null);
        invalidModel.validate();
    }

    @Test
    public void testReset() {
        dynamicSelectModel.objects = new ArrayList<>();
        dynamicSelectModel.reset();
        assertNull(dynamicSelectModel.objects);
    }
}
