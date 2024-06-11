import org.junit.Test;
import static org.junit.Assert.*;

public class DynamicSelectModelTest {

    @Test
    public void testGetOptionCount() {
        DynamicSelectModel model = new DynamicSelectModel();
        assertEquals(0, model.getOptionCount());
    }

    @Test
    public void testGetLabel() {
        DynamicSelectModel model = new DynamicSelectModel();
        model.label("label");
        model.setComponent(new Component());
        model.setTopLevelComponent(new StandaloneComponent());
        assertEquals("test", model.getLabel(0));
    }

    @Test
    public void testGetValue() {
        DynamicSelectModel model = new DynamicSelectModel();
        model.value("value");
        model.setComponent(new Component());
        model.setTopLevelComponent(new StandaloneComponent());
        assertEquals("test", model.getValue(0));
    }

    @Test
    public void testTranslateValue() {
        DynamicSelectModel model = new DynamicSelectModel();
        model.setComponent(new Component());
        model.setTopLevelComponent(new StandaloneComponent());
        assertEquals(null, model.translateValue("test"));
    }

    @Test
    public void testGetObjects() {
        DynamicSelectModel model = new DynamicSelectModel();
        model.setComponent(new Component());
        model.setTopLevelComponent(new StandaloneComponent());
        assertNotNull(model.getObjects());
    }

    @Test
    public void testCollection() {
        DynamicSelectModel model = new DynamicSelectModel();
        model.collection("collection");
        assertNotNull(model.getObjects());
    }

    @Test
    public void testLabel() {
        DynamicSelectModel model = new DynamicSelectModel();
        model.label("label");
        model.setComponent(new Component());
        model.setTopLevelComponent(new StandaloneComponent());
        assertNotNull(model.getLabel(0));
    }

    @Test
    public void testValue() {
        DynamicSelectModel model = new DynamicSelectModel();
        model.value("value");
        model.setComponent(new Component());
        model.setTopLevelComponent(new StandaloneComponent());
        assertNotNull(model.getValue(0));
    }

    @Test
    public void testEnumeration() {
        DynamicSelectModel model = new DynamicSelectModel();
        assertNotNull(model.getObjects());
    }

    @Test
    public void testTranslator() {
        DynamicSelectModel model = new DynamicSelectModel();
        model.translator("translator");
        assertEquals(null, model.translateValue("test"));
    }

    @Test
    public void testEmpty() {
        DynamicSelectModel model = new DynamicSelectModel();
        model.empty("empty");
        assertNotNull(model.getObjects());
    }

    @Test
    public void testSetComponent() {
        DynamicSelectModel model = new DynamicSelectModel();
        Component component = new Component();
        model.setComponent(component);
        assertEquals(component, model.getComponent());
    }

    @Test
    public void testSetTopLevelComponent() {
        DynamicSelectModel model = new DynamicSelectModel();
        StandaloneComponent topLevelComponent = new StandaloneComponent();
        model.setTopLevelComponent(topLevelComponent);
        assertEquals(topLevelComponent, model.getTopLevelComponent());
    }

    @Test(expected = WheelException.class)
    public void testValidateNullCollection() {
        DynamicSelectModel model = new DynamicSelectModel();
        model.validate();
    }

    @Test(expected = WheelException.class)
    public void testValidateNullLabel() {
        DynamicSelectModel model = new DynamicSelectModel();
        model.collection("collection");
        model.validate();
    }

    @Test(expected = WheelException.class)
    public void testValidateNullValue() {
        DynamicSelectModel model = new DynamicSelectModel();
        model.collection("collection");
        model.label("label");
        model.validate();
    }
}