import org.junit.Before;
import org.junit.Test;
import wheel.util.DynamicSelectModel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class DynamicSelectModelTest {
    private DynamicSelectModel dynamicSelectModel;

    @Before
    public void setup() {
        dynamicSelectModel = new DynamicSelectModel();
    }

    @Test
    public void testConstructorWithCollectionLabelAndValue() {
        String collection = "collection";
        String label = "label";
        String value = "value";

        dynamicSelectModel = new DynamicSelectModel(collection, label, value);

        assertEquals(collection, dynamicSelectModel.getCollection().getExpression());
        assertEquals(label, dynamicSelectModel.getLabel().getExpression());
        assertEquals(value, dynamicSelectModel.getValue().getExpression());
    }

    @Test
    public void testConstructorWithNoParameters() {
        assertNotNull(dynamicSelectModel.getCollection());
        assertNotNull(dynamicSelectModel.getLabel());
        assertNotNull(dynamicSelectModel.getValue());
    }

    @Test
    public void testCollection() {
        String collection = "collection";
        dynamicSelectModel.collection(collection);

        assertEquals(collection, dynamicSelectModel.getCollection().getExpression());
    }

    @Test
    public void testLabel() {
        String label = "label";
        dynamicSelectModel.label(label);

        assertEquals(label, dynamicSelectModel.getLabel().getExpression());
    }

    @Test
    public void testValue() {
        String value = "value";
        dynamicSelectModel.value(value);

        assertEquals(value, dynamicSelectModel.getValue().getExpression());
    }

    @Test
    public void testEnumeration() {
        String enumeration = "enumeration";
        dynamicSelectModel.enumeration(enumeration);

        assertEquals(enumeration + ".values()", dynamicSelectModel.getCollection().getExpression());
        assertEquals("toString()", dynamicSelectModel.getLabel().getExpression());
        assertEquals("ordinal()", dynamicSelectModel.getValue().getExpression());
    }

    @Test
    public void testTranslator() {
        String translator = "translator";
        dynamicSelectModel.translator(translator);

        assertEquals(translator, dynamicSelectModel.getTranslator().getExpression());
    }

    @Test
    public void testEmpty() {
        Object o = new Object();
        dynamicSelectModel.empty(o);

        assertEquals(o, dynamicSelectModel.getEmptyObject());
    }

    @Test
    public void testGetOptionCount() {
        // Test typical scenario
        dynamicSelectModel.collection("collection");
        dynamicSelectModel.setObjects(new ArrayList<>());
        dynamicSelectModel.getObjects().add("Option1");
        dynamicSelectModel.getObjects().add("Option2");

        assertEquals(2, dynamicSelectModel.getOptionCount());

        // Test empty scenario
        dynamicSelectModel.setObjects(null);

        assertEquals(0, dynamicSelectModel.getOptionCount());
    }

    @Test
    public void testGetLabel() {
        dynamicSelectModel.setLabel(new ElExpression("label"));
        // Test typical scenario
        dynamicSelectModel.setObjects(new ArrayList<>());
        dynamicSelectModel.getObjects().add("Option1");
        dynamicSelectModel.getObjects().add("Option2");

        assertEquals("Option1", dynamicSelectModel.getLabel(0));
        assertEquals("Option2", dynamicSelectModel.getLabel(1));

        // Test error scenario
        try {
            dynamicSelectModel.getLabel(2);
            fail("Expected WheelException to be thrown");
        } catch (Exception e) {
            assertEquals("Could not evaluate expression label to create a label for dynamic ISelectModel. Index was 2", e.getMessage());
        }
    }

    @Test
    public void testGetValue() {
        dynamicSelectModel.setValue(new ElExpression("value"));
        // Test typical scenario
        dynamicSelectModel.setObjects(new ArrayList<>());
        dynamicSelectModel.getObjects().add(100);
        dynamicSelectModel.getObjects().add(200);

        assertEquals("100", dynamicSelectModel.getValue(0));
        assertEquals("200", dynamicSelectModel.getValue(1));

        // Test error scenario
        try {
            dynamicSelectModel.getValue(2);
            fail("Expected WheelException to be thrown");
        } catch (Exception e) {
            assertEquals("Could not evaluate expression value to create a value for dynamic ISelectModel. Index was 2", e.getMessage());
        }
    }

    @Test
    public void testTranslateValue() {
        // Test typical scenario
        dynamicSelectModel.setTranslator(new ElExpression("translator"));
        dynamicSelectModel.setObjects(new ArrayList<>());
        dynamicSelectModel.getObjects().add("Option1");
        dynamicSelectModel.getObjects().add("Option2");

        Object translation = dynamicSelectModel.translateValue("Option1");

        assertEquals("Option1", translation);

        // Test scenario with translator
        dynamicSelectModel.setTranslator(new ElExpression("translator.toUpperCase()"));
        translation = dynamicSelectModel.translateValue("Option1");

        assertEquals("OPTION1", translation);

        // Test scenario with null translation
        translation = dynamicSelectModel.translateValue("InvalidOption");

        assertNull(translation);
    }

    @Test
    public void testGetObjects() {
        // Test typical scenario with Collection
        Collection<Object> objects = new ArrayList<>();
        objects.add("Option1");
        objects.add("Option2");
        dynamicSelectModel.setObjects(objects);

        Collection<Object> result = dynamicSelectModel.getObjects();

        assertNotNull(result);
        assertEquals(objects, result);

        // Test typical scenario with Object[]
        Object[] objectArray = {1, 2, 3};
        dynamicSelectModel.setObjects(Arrays.asList(objectArray));

        result = dynamicSelectModel.getObjects();

        assertNotNull(result);
        assertTrue(result.containsAll(Arrays.asList(objectArray)));

        // Test scenario with invalid object type
        Object invalidObject = new Object();
        try {
            dynamicSelectModel.setObjects(invalidObject);
            fail("Expected WheelException to be thrown");
        } catch (Exception e) {
            assertEquals("Expression " + invalidObject + " evaluated to an object that is not an instance of Collection or Object[]. Can't build a dynamic ISelectModel", e.getMessage());
        }

        // Test scenario with null object
        try {
            dynamicSelectModel.setObjects(null);
            fail("Expected WheelException to be thrown");
        } catch (Exception e) {
            assertEquals("Expression " + null + " evaluated to null. Can't build a dynamic ISelectModel", e.getMessage());
        }
    }

    @Test
    public void testValidate() {
        // Test scenario with null collection
        try {
            dynamicSelectModel.validate();
            fail("Expected WheelException to be thrown");
        } catch (Exception e) {
            assertEquals("Could not create DynamicSelection model. Collection attribute was null or not an el-expression.", e.getMessage());
        }

        // Test scenario with null label
        dynamicSelectModel.setCollection(new ElExpression("collection"));

        try {
            dynamicSelectModel.validate();
            fail("Expected WheelException to be thrown");
        } catch (Exception e) {
            assertEquals("Could not create DynamicSelection model. Label attribute was null or not an el-expression.", e.getMessage());
        }

        // Test scenario with null value
        dynamicSelectModel.setLabel(new ElExpression("label"));

        try {
            dynamicSelectModel.validate();
            fail("Expected WheelException to be thrown");
        } catch (Exception e) {
            assertEquals("Could not create DynamicSelection model. Value attribute was null or not an el-expression.", e.getMessage());
        }
    }

    @Test
    public void testReset() {
        dynamicSelectModel.setObjects(new ArrayList<>());

        dynamicSelectModel.reset();

        assertNull(dynamicSelectModel.getObjects());
    }

    @Test
    public void testComponent() {
        Component component = new Component();
        dynamicSelectModel.setComponent(component);

        assertEquals(component, dynamicSelectModel.getComponent());
    }

    @Test
    public void testTopLevelComponent() {
        StandaloneComponent topLevelComponent = new StandaloneComponent();
        dynamicSelectModel.setTopLevelComponent(topLevelComponent);

        assertEquals(topLevelComponent, dynamicSelectModel.getTopLevelComponent());
    }
}