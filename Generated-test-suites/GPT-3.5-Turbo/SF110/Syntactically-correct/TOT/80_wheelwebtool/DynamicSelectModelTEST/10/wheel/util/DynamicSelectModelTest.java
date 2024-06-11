import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DynamicSelectModelTest {

    private DynamicSelectModel dynamicSelectModel;

    @Before
    public void setup() {
        dynamicSelectModel = new DynamicSelectModel();
    }

    @Test
    public void testConstructor() {
        ElExpression collection = new ElExpression("collection");
        ElExpression label = new ElExpression("label");
        ElExpression value = new ElExpression("value");

        dynamicSelectModel = new DynamicSelectModel(collection, label, value);

        assertNotNull(dynamicSelectModel);
        assertEquals(collection, dynamicSelectModel.getCollection());
        assertEquals(label, dynamicSelectModel.getLabel());
        assertEquals(value, dynamicSelectModel.getValue());
    }

    @Test
    public void testCollection() {
        String collection = "collection";

        dynamicSelectModel.collection(collection);

        assertNotNull(dynamicSelectModel.getCollection());
        assertEquals(collection, dynamicSelectModel.getCollection().getExpression());
    }

    @Test
    public void testLabel() {
        String label = "label";

        dynamicSelectModel.label(label);

        assertNotNull(dynamicSelectModel.getLabel());
        assertEquals(label, dynamicSelectModel.getLabel().getExpression());
    }

    @Test
    public void testValue() {
        String value = "value";

        dynamicSelectModel.value(value);

        assertNotNull(dynamicSelectModel.getValue());
        assertEquals(value, dynamicSelectModel.getValue().getExpression());
    }

    @Test
    public void testEnumeration() {
        String enumeration = "enumeration";

        dynamicSelectModel.enumeration(enumeration);

        assertNotNull(dynamicSelectModel.getCollection());
        assertNotNull(dynamicSelectModel.getLabel());
        assertNotNull(dynamicSelectModel.getValue());
        assertEquals(enumeration + ".values()", dynamicSelectModel.getCollection().getExpression());
        assertEquals("toString()", dynamicSelectModel.getLabel().getExpression());
        assertEquals("ordinal()", dynamicSelectModel.getValue().getExpression());
    }

    @Test
    public void testTranslator() {
        String translator = "translator";

        dynamicSelectModel.translator(translator);

        assertNotNull(dynamicSelectModel.getTranslator());
        assertEquals(translator, dynamicSelectModel.getTranslator().getExpression());
    }

    @Test
    public void testEmpty() {
        Object emptyObject = new Object();

        dynamicSelectModel.empty(emptyObject);

        assertEquals(emptyObject, dynamicSelectModel.getEmptyObject());
    }

    @Test
    public void testGetOptionCount() {
        // Test normal case
        Collection<String> objects1 = new ArrayList<>();
        objects1.add("option1");
        objects1.add("option2");
        dynamicSelectModel.setObjects(objects1);

        int optionCount1 = dynamicSelectModel.getOptionCount();
        assertEquals(2, optionCount1);

        // Test empty case
        Collection<Object> objects2 = new ArrayList<>();
        dynamicSelectModel.setObjects(objects2);

        int optionCount2 = dynamicSelectModel.getOptionCount();
        assertEquals(0, optionCount2);
    }

    @Test
    public void testGetLabel() {
        // Test normal case
        Collection<String> objects1 = new ArrayList<>();
        objects1.add("option1");
        objects1.add("option2");
        dynamicSelectModel.setObjects(objects1);
        dynamicSelectModel.setComponent(new Component());

        String label1 = dynamicSelectModel.getLabel(0);
        assertEquals("option1", label1);

        // Test error case
        Collection<Object> objects2 = new ArrayList<>();
        objects2.add(new Object());
        dynamicSelectModel.setObjects(objects2);

        try {
            dynamicSelectModel.getLabel(0);
            fail("Exception not thrown");
        } catch (WheelException e) {
            assertEquals("Could not evaluate expression null to create a label for dynamic ISelectModel. Index was 0", e.getMessage());
        }
    }

    @Test
    public void testGetValue() {
        // Test normal case
        Collection<String> objects1 = new ArrayList<>();
        objects1.add("option1");
        objects1.add("option2");
        dynamicSelectModel.setObjects(objects1);
        dynamicSelectModel.setComponent(new Component());

        String value1 = dynamicSelectModel.getValue(0);
        assertEquals("option1", value1);

        // Test error case
        Collection<Object> objects2 = new ArrayList<>();
        objects2.add(new Object());
        dynamicSelectModel.setObjects(objects2);

        try {
            dynamicSelectModel.getValue(0);
            fail("Exception not thrown");
        } catch (WheelException e) {
            assertEquals("Could not evaluate expression null to create a value for dynamic ISelectModel. Index was 0", e.getMessage());
        }
    }

    @Test
    public void testTranslateValue() {
        // Test normal case
        Collection<String> objects1 = new ArrayList<>();
        objects1.add("option1");
        objects1.add("option2");
        dynamicSelectModel.setObjects(objects1);

        dynamicSelectModel.setValue("option1");

        String translatedValue1 = dynamicSelectModel.translateValue("option1");
        assertEquals("option1", translatedValue1);

        // Test error case
        Collection<Object> objects2 = new ArrayList<>();
        objects2.add(new Object());
        dynamicSelectModel.setObjects(objects2);

        try {
            dynamicSelectModel.translateValue("option1");
            fail("Exception not thrown");
        } catch (WheelException e) {
            assertEquals("Could not evaluate expression null to translate value ' option1'", e.getMessage());
        }
    }

    @Test
    public void testGetObjects() {
        // Test normal case
        Collection<String> objects1 = new ArrayList<>();
        objects1.add("option1");
        objects1.add("option2");
        dynamicSelectModel.setObjects(objects1);

        Collection<String> retrievedObjects1 = dynamicSelectModel.getObjects();
        assertEquals(objects1, retrievedObjects1);

        // Test null case
        dynamicSelectModel.setObjects(null);

        try {
            dynamicSelectModel.getObjects();
            fail("Exception not thrown");
        } catch (WheelException e) {
            assertEquals("Could not evaluate expression null to create a dynamic ISelectModel.", e.getMessage());
        }
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

        assertEquals(component, dynamicSelectModel.getComponent());
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

        assertEquals(topLevelComponent, dynamicSelectModel.getTopLevelComponent());
    }

    @Test
    public void testValidate() {
        dynamicSelectModel.collection(null);

        try {
            dynamicSelectModel.validate();
            fail("Exception not thrown");
        } catch (WheelException e) {
            assertEquals("Could not create DynamicSelection model. Collection attribute was null or not an el-expression.", e.getMessage());
        }

        dynamicSelectModel.collection("collection");
        dynamicSelectModel.label(null);

        try {
            dynamicSelectModel.validate();
            fail("Exception not thrown");
        } catch (WheelException e) {
            assertEquals("Could not create DynamicSelection model. Label attribute was null or not an el-expression.", e.getMessage());
        }

        dynamicSelectModel.label("label");
        dynamicSelectModel.value(null);

        try {
            dynamicSelectModel.validate();
            fail("Exception not thrown");
        } catch (WheelException e) {
            assertEquals("Could not create DynamicSelection model. Value attribute was null or not an el-expression.", e.getMessage());
        }
    }

    @Test
    public void testReset() {
        dynamicSelectModel.setObjects(new ArrayList<>());

        dynamicSelectModel.reset();

        assertNull(dynamicSelectModel.getObjects());
    }
}