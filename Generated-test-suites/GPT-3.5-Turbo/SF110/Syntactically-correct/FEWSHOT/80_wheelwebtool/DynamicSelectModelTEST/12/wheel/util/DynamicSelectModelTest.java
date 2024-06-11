package wheel.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import wheel.components.Component;
import wheel.components.StandaloneComponent;
import wheel.util.DynamicSelectModel;

import org.junit.Before;
import org.junit.Test;

public class DynamicSelectModelTest {

    private DynamicSelectModel model;

    @Before
    public void setUp() {
        model = new DynamicSelectModel();
    }

    @Test
    public void testGetOptionCount() {
        model.setObjects(null);
        assertEquals(0, model.getOptionCount());

        model.setObjects(new ArrayList());
        assertEquals(0, model.getOptionCount());

        model.setObjects(Arrays.asList(1, 2, 3));
        assertEquals(3, model.getOptionCount());
    }

    @Test
    public void testGetLabel() {
        model.setObjects(Arrays.asList("a", "b", "c"));
        model.setComponent(new Component());
        model.setLabel(new ElExpression("label"));

        assertEquals("a", model.getLabel(0));
        assertEquals("b", model.getLabel(1));
        assertEquals("c", model.getLabel(2));
    }

    @Test
    public void testGetValue() {
        model.setObjects(Arrays.asList("a", "b", "c"));
        model.setComponent(new Component());
        model.setValue(new ElExpression("value"));

        assertEquals("a", model.getValue(0));
        assertEquals("b", model.getValue(1));
        assertEquals("c", model.getValue(2));
    }

    @Test
    public void testTranslateValue() {
        model.setObjects(Arrays.asList("a", "b", "c"));
        model.setComponent(new Component());
        model.setValue(new ElExpression("value"));

        assertEquals("a", model.translateValue("a"));
        assertEquals("b", model.translateValue("b"));
        assertEquals("c", model.translateValue("c"));
        assertNull(model.translateValue("d"));
    }

    @Test
    public void testGetObjects() {
        model.setTopLevelComponent(new StandaloneComponent());
        model.setComponent(new Component());

        model.setCollection(new ElExpression("collection"));
        assertEquals(null, model.getObjects());

        model.setCollection(new ElExpression("[]"));
        assertEquals(0, model.getObjects().size());

        model.setCollection(new ElExpression("[1, 2, 3]"));
        assertEquals(3, model.getObjects().size());
        assertEquals(true, model.getObjects().contains(1));
        assertEquals(true, model.getObjects().contains(2));
        assertEquals(true, model.getObjects().contains(3));
    }
}