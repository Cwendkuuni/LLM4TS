package wheel.util;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Collection;
import java.util.ArrayList;
import java.util.LinkedList;

public class DynamicSelectModelTest {

    private DynamicSelectModel dynamicSelectModel;

    @Before
    public void setUp() {
        dynamicSelectModel = new DynamicSelectModel();
    }

    @Test
    public void testGetOptionCount() {
        // Test when objects is null
        assertEquals(0, dynamicSelectModel.getOptionCount());

        // Test when objects is not null
        Collection objects = new ArrayList();
        objects.add("Option 1");
        objects.add("Option 2");
        dynamicSelectModel.objects = objects;
        assertEquals(2, dynamicSelectModel.getOptionCount());
    }

    @Test
    public void testGetLabel() {
        // Test when objects is null
        dynamicSelectModel.objects = null;
        dynamicSelectModel.component = null;
        assertEquals("", dynamicSelectModel.getLabel(0));

        // Test when objects is not null and index is valid
        Collection objects = new ArrayList();
        objects.add("Option 1");
        objects.add("Option 2");
        dynamicSelectModel.objects = objects;
        assertEquals("Option 1", dynamicSelectModel.getLabel(0));

        // Test when objects is not null and index is invalid
        assertEquals("", dynamicSelectModel.getLabel(2));
        
    }

    @Test
    public void testGetValue() {
        // Test when objects is null
        dynamicSelectModel.objects = null;
        dynamicSelectModel.component = null;
        assertEquals("", dynamicSelectModel.getValue(0));

        // Test when objects is not null and index is valid
        Collection objects = new ArrayList();
        objects.add("Option 1");
        objects.add("Option 2");
        dynamicSelectModel.objects = objects;
        assertEquals("Option 1", dynamicSelectModel.getValue(0));

        // Test when objects is not null and index is invalid
        assertEquals("", dynamicSelectModel.getValue(2));
        
    }

    @Test
    public void testTranslateValue() {
        // Test when objects is null
        assertNull(dynamicSelectModel.translateValue("Value 1"));

        // Test when objects is not null and value does not match any object
        Collection objects = new ArrayList();
        objects.add("Option 1");
        objects.add("Option 2");
        dynamicSelectModel.objects = objects;
        assertEquals("Option 1", dynamicSelectModel.translateValue("Value 1"));

        // Test when objects is not null and value matches an object
        assertEquals("Option 2", dynamicSelectModel.translateValue("Value 2"));
    }

    @Test
    public void testGetObjects() {
        // Test when objects is not null
        Collection objects = new ArrayList();
        objects.add("Option 1");
        objects.add("Option 2");
        dynamicSelectModel.objects = objects;
        assertSame(objects, dynamicSelectModel.getObjects());

        // Test when objects is null
        dynamicSelectModel.objects = null;
        dynamicSelectModel.topLevelComponent = null;
        dynamicSelectModel.component = null;
        assertThrows(WheelException.class, () -> {
            dynamicSelectModel.getObjects();
        });
    }

    @Test
    public void testValidate() {
        // Test when collection is null
        dynamicSelectModel.collection = null;
        dynamicSelectModel.label = new ElExpression("label");
        dynamicSelectModel.value = new ElExpression("value");
        assertThrows(WheelException.class, () -> {
            dynamicSelectModel.validate();
        });

        // Test when label is null
        dynamicSelectModel.collection = new ElExpression("collection");
        dynamicSelectModel.label = null;
        dynamicSelectModel.value = new ElExpression("value");
        assertThrows(WheelException.class, () -> {
            dynamicSelectModel.validate();
        });

        // Test when value is null
        dynamicSelectModel.collection = new ElExpression("collection");
        dynamicSelectModel.label = new ElExpression("label");
        dynamicSelectModel.value = null;
        assertThrows(WheelException.class, () -> {
            dynamicSelectModel.validate();
        });
    }

    @Test
    public void testReset() {
        dynamicSelectModel.reset();
        assertNull(dynamicSelectModel.objects);
    }
}