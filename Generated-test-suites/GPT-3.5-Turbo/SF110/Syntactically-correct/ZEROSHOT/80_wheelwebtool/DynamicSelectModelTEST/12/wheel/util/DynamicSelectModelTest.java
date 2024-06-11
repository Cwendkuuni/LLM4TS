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
    public void testCollection() {
        String collection = "testCollection";
        assertEquals(dynamicSelectModel, dynamicSelectModel.collection(collection));
        assertNull(dynamicSelectModel.getObjects());
    }

    @Test
    public void testLabel() {
        String label = "testLabel";
        assertEquals(dynamicSelectModel, dynamicSelectModel.label(label));
        assertNull(dynamicSelectModel.getObjects());
    }

    @Test
    public void testValue() {
        String value = "testValue";
        assertEquals(dynamicSelectModel, dynamicSelectModel.value(value));
        assertNull(dynamicSelectModel.getObjects());
    }

    @Test
    public void testEnumeration() {
        String enumeration = "testEnumeration";
        assertEquals(dynamicSelectModel, dynamicSelectModel.enumeration(enumeration));
        assertNull(dynamicSelectModel.getObjects());
    }

    @Test
    public void testTranslator() {
        String translator = "testTranslator";
        assertEquals(dynamicSelectModel, dynamicSelectModel.translator(translator));
        assertNull(dynamicSelectModel.getObjects());
    }

    @Test
    public void testEmpty() {
        Object o = new Object();
        assertEquals(dynamicSelectModel, dynamicSelectModel.empty(o));
        assertNull(dynamicSelectModel.getObjects());
    }

    @Test
    public void testGetOptionCount() {
        dynamicSelectModel.setTopLevelComponent(new StandaloneComponent());
        dynamicSelectModel.setComponent(new Component());
        dynamicSelectModel.collection("testCollection");
        dynamicSelectModel.reset();
        assertEquals(0, dynamicSelectModel.getOptionCount());
    }

    @Test
    public void testGetLabel() {
        dynamicSelectModel.setComponent(new Component());
        dynamicSelectModel.label("testLabel");
        dynamicSelectModel.collection("testCollection");
        dynamicSelectModel.reset();
        assertNull(dynamicSelectModel.getLabel(0));
    }

    @Test
    public void testGetValue() {
        dynamicSelectModel.setComponent(new Component());
        dynamicSelectModel.value("testValue");
        dynamicSelectModel.collection("testCollection");
        dynamicSelectModel.reset();
        assertNull(dynamicSelectModel.getValue(0));
    }

    @Test
    public void testTranslateValue() {
        dynamicSelectModel.setComponent(new Component());
        dynamicSelectModel.collection("testCollection");
        dynamicSelectModel.reset();
        assertNull(dynamicSelectModel.translateValue("testValue"));
    }

    @Test
    public void testGetObjects() {
        assertNull(dynamicSelectModel.getObjects());
    }

    @Test
    public void testSetComponent() {
        Component component = new Component();
        dynamicSelectModel.setComponent(component);
        assertSame(component, dynamicSelectModel.getComponent());
    }

    @Test
    public void testSetTopLevelComponent() {
        StandaloneComponent topLevelComponent = new StandaloneComponent();
        dynamicSelectModel.setTopLevelComponent(topLevelComponent);
        assertSame(topLevelComponent, dynamicSelectModel.getTopLevelComponent());
    }

    @Test
    public void testValidate() {
        try {
            dynamicSelectModel.validate();
            fail("Expected WheelException to be thrown");
        } catch (WheelException e) {
            assertNull(e.getComponent());
            assertEquals("Could not create DynamicSelection model. Collection attribute was null or not an el-expression.", e.getMessage());
        }

        dynamicSelectModel.collection("testCollection");

        try {
            dynamicSelectModel.validate();
            fail("Expected WheelException to be thrown");
        } catch (WheelException e) {
            assertNull(e.getComponent());
            assertEquals("Could not create DynamicSelection model. Label attribute was null or not an el-expression.", e.getMessage());
        }

        dynamicSelectModel.label("testLabel");

        try {
            dynamicSelectModel.validate();
            fail("Expected WheelException to be thrown");
        } catch (WheelException e) {
            assertNull(e.getComponent());
            assertEquals("Could not create DynamicSelection model. Value attribute was null or not an el-expression.", e.getMessage());
        }

        dynamicSelectModel.value("testValue");

        dynamicSelectModel.validate();
    }

    @Test
    public void testReset() {
        dynamicSelectModel.setObjects(new ArrayList<>());
        dynamicSelectModel.reset();
        assertNull(dynamicSelectModel.getObjects());
    }
}