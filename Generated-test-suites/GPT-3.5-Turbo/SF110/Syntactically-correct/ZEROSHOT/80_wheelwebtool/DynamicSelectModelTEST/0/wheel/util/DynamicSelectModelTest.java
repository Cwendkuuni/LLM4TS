import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DynamicSelectModelTest {

    DynamicSelectModel dynamicSelectModel;

    @Before
    public void setUp() {
        dynamicSelectModel = new DynamicSelectModel();
    }

    @Test
    public void testCollection() {
        dynamicSelectModel.collection("collection");
        assertEquals("collection", dynamicSelectModel.collection.getExpression());
    }

    @Test
    public void testLabel() {
        dynamicSelectModel.label("label");
        assertEquals("label", dynamicSelectModel.label.getExpression());
    }

    @Test
    public void testValue() {
        dynamicSelectModel.value("value");
        assertEquals("value", dynamicSelectModel.value.getExpression());
    }

    @Test
    public void testEnumeration() {
        dynamicSelectModel.enumeration("enumeration");
        assertEquals("enumeration.values()", dynamicSelectModel.collection.getExpression());
        assertEquals("toString()", dynamicSelectModel.label.getExpression());
        assertEquals("ordinal()", dynamicSelectModel.value.getExpression());
    }

    @Test
    public void testTranslator() {
        dynamicSelectModel.translator("translator");
        assertEquals("translator", dynamicSelectModel.translator.getExpression());
    }

    @Test
    public void testEmpty() {
        Object emptyObject = new Object();
        dynamicSelectModel.empty(emptyObject);
        assertEquals(emptyObject, dynamicSelectModel.emptyObject);
    }

    @Test
    public void testGetOptionCount() {
        assertEquals(0, dynamicSelectModel.getOptionCount());
    }

    @Test
    public void testGetLabel() {
        dynamicSelectModel.label = new ElExpression("label");
        Component component = new Component();
        dynamicSelectModel.setComponent(component);

        Object object1 = new Object();
        Object object2 = new Object();
        dynamicSelectModel.objects = new ArrayList<>();
        dynamicSelectModel.objects.add(object1);
        dynamicSelectModel.objects.add(object2);

        assertEquals(object1.toString(), dynamicSelectModel.getLabel(0));
        assertEquals(object2.toString(), dynamicSelectModel.getLabel(1));
    }

    @Test
    public void testGetValue() {
        dynamicSelectModel.value = new ElExpression("value");
        Component component = new Component();
        dynamicSelectModel.setComponent(component);

        Object object1 = new Object();
        Object object2 = new Object();
        dynamicSelectModel.objects = new ArrayList<>();
        dynamicSelectModel.objects.add(object1);
        dynamicSelectModel.objects.add(object2);

        assertEquals(object1.toString(), dynamicSelectModel.getValue(0));
        assertEquals(object2.toString(), dynamicSelectModel.getValue(1));
    }

    @Test
    public void testTranslateValue() {
        dynamicSelectModel.label = new ElExpression("label");
        dynamicSelectModel.value = new ElExpression("value");
        Component component = new Component();
        dynamicSelectModel.setComponent(component);

        Object object1 = new Object();
        Object object2 = new Object();
        dynamicSelectModel.objects = new ArrayList<>();
        dynamicSelectModel.objects.add(object1);
        dynamicSelectModel.objects.add(object2);

        assertEquals(object1, dynamicSelectModel.translateValue(object1.toString()));
        assertEquals(null, dynamicSelectModel.translateValue("nonexistent"));
    }

    @Test
    public void testGetObjects() {
        Component topLevelComponent = new StandaloneComponent();
        dynamicSelectModel.setTopLevelComponent(topLevelComponent);

        dynamicSelectModel.collection = new ElExpression("collection");
        dynamicSelectModel.collection.errorMessage("Could not evaluate expression " + dynamicSelectModel.collection.getExpression() + " to create a dynamic ISelectModel.");
        topLevelComponent.customErrorMessage = "Could not evaluate expression " + dynamicSelectModel.collection.getExpression() + " to create a dynamic ISelectModel.";

        try {
            dynamicSelectModel.getObjects();
            fail("Expected WheelException to be thrown");
        } catch (WheelException e) {
            assertEquals("Could not create DynamicSelection model. Collection attribute was null or not an el-expression.", e.getMessage());
            assertEquals(null, e.component);
            assertEquals("Could not evaluate expression collection to create a dynamic ISelectModel.", topLevelComponent.customErrorMessage);
        }

        dynamicSelectModel.collection = new ElExpression("object");

        try {
            dynamicSelectModel.getObjects();
            fail("Expected WheelException to be thrown");
        } catch (WheelException e) {
            assertEquals("Could not create DynamicSelection model. Collection attribute was null or not an el-expression.", e.getMessage());
            assertEquals(null, e.component);
            assertEquals("Could not evaluate expression object to create a dynamic ISelectModel.", topLevelComponent.customErrorMessage);
        }

        dynamicSelectModel.collection = new ElExpression("collection");

        Object collectionObject = new Object();
        dynamicSelectModel.collection.eval(topLevelComponent, dynamicSelectModel.component, collectionObject);

        assertEquals(collectionObject, dynamicSelectModel.getObjects());

        dynamicSelectModel.collection = new ElExpression("object");

        try {
            dynamicSelectModel.getObjects();
            fail("Expected WheelException to be thrown");
        } catch (WheelException e) {
            assertEquals("Could not create DynamicSelection model. Collection attribute was null or not an el-expression.", e.getMessage());
            assertEquals(null, e.component);
            assertEquals("Could not evaluate expression object to create a dynamic ISelectModel.", topLevelComponent.customErrorMessage);
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
        dynamicSelectModel.label = null;
        try {
            dynamicSelectModel.validate();
            fail("Expected WheelException to be thrown");
        } catch (WheelException e) {
            assertEquals("Could not create DynamicSelection model. Label attribute was null or not an el-expression.", e.getMessage());
            assertEquals(null, e.component);
        }

        dynamicSelectModel.label = new ElExpression("label");

        dynamicSelectModel.value = null;
        try {
            dynamicSelectModel.validate();
            fail("Expected WheelException to be thrown");
        } catch (WheelException e) {
            assertEquals("Could not create DynamicSelection model. Value attribute was null or not an el-expression.", e.getMessage());
            assertEquals(null, e.component);
        }

        dynamicSelectModel.value = new ElExpression("value");

        try {
            dynamicSelectModel.validate();
        } catch (WheelException e) {
            fail("Did not expect WheelException to be thrown");
        }
    }

    @Test
    public void testReset() {
        dynamicSelectModel.objects = new ArrayList<>();
        dynamicSelectModel.reset();
        assertEquals(null, dynamicSelectModel.objects);
    }

}