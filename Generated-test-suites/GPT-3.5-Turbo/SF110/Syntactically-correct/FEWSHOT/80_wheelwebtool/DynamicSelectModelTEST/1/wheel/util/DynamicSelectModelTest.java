package wheel.util;

import static org.junit.Assert.*;
import org.junit.Test;


public class DynamicSelectModelTest {
    
    @Test
    public void testGetOptionCount() {
        // Create DynamicSelectModel object
        DynamicSelectModel model = new DynamicSelectModel();
        
        // Test when objects is null
        assertEquals(0, model.getOptionCount());
        
        // Test when objects is not null
        model.setObjects(new ArrayList<String>());
        assertEquals(0, model.getOptionCount());
        
        model.getObjects().add("Option 1");
        assertEquals(1, model.getOptionCount());
        
        model.getObjects().add("Option 2");
        assertEquals(2, model.getOptionCount());
        
        model.getObjects().add("Option 3");
        assertEquals(3, model.getOptionCount());
    }
    
    @Test
    public void testGetLabel() {
        // Create DynamicSelectModel object
        DynamicSelectModel model = new DynamicSelectModel();
        
        // Set objects
        model.setObjects(new ArrayList<String>());
        model.getObjects().add("Option 1");
        model.getObjects().add("Option 2");
        
        // Test when index is out of bounds
        try {
            String label = model.getLabel(-1);
            fail("Exception not thrown");
        } catch (IndexOutOfBoundsException e) {
            // Exception expected
        }
        
        try {
            String label = model.getLabel(2);
            fail("Exception not thrown");
        } catch (IndexOutOfBoundsException e) {
            // Exception expected
        }
        
        // Test when index is within bounds
        String label = model.getLabel(0);
        assertEquals("Option 1", label);
        
        label = model.getLabel(1);
        assertEquals("Option 2", label);
    }
    
    @Test
    public void testGetValue() {
        // Create DynamicSelectModel object
        DynamicSelectModel model = new DynamicSelectModel();
        
        // Set objects
        model.setObjects(new ArrayList<String>());
        model.getObjects().add("Option 1");
        model.getObjects().add("Option 2");
        
        // Test when index is out of bounds
        try {
            String value = model.getValue(-1);
            fail("Exception not thrown");
        } catch (IndexOutOfBoundsException e) {
            // Exception expected
        }
        
        try {
            String value = model.getValue(2);
            fail("Exception not thrown");
        } catch (IndexOutOfBoundsException e) {
            // Exception expected
        }
        
        // Test when index is within bounds
        String value = model.getValue(0);
        assertEquals("Option 1", value);
        
        value = model.getValue(1);
        assertEquals("Option 2", value);
    }
    
    @Test
    public void testTranslateValue() {
        // Create DynamicSelectModel object
        DynamicSelectModel model = new DynamicSelectModel();
        
        // Set objects
        model.setObjects(new ArrayList<String>());
        model.getObjects().add("Option 1");
        model.getObjects().add("Option 2");
        
        // Test when value is null
        Object translated = model.translateValue(null);
        assertNull(translated);
        
        // Test when value is not found
        translated = model.translateValue("Option 3");
        assertNull(translated);
        
        // Test when value is found
        translated = model.translateValue("Option 2");
        assertEquals("Option 2", translated);
    }
    
    @Test
    public void testGetObjects() {
        // Create DynamicSelectModel object
        DynamicSelectModel model = new DynamicSelectModel();
        
        // Test when objects is null
        assertNull(model.getObjects());
        
        // Test when objects is not null
        model.setObjects(new ArrayList<String>());
        assertNotNull(model.getObjects());
        assertTrue(model.getObjects().isEmpty());
        
        model.getObjects().add("Option 1");
        assertEquals(1, model.getObjects().size());
        
        model.getObjects().add("Option 2");
        assertEquals(2, model.getObjects().size());
    }
    
    @Test
    public void testGetComponent() {
        // Create DynamicSelectModel object
        DynamicSelectModel model = new DynamicSelectModel();
        
        // Test when component is null
        assertNull(model.getComponent());
        
        // Test when component is not null
        Component component = new Component();
        model.setComponent(component);
        assertEquals(component, model.getComponent());
    }
    
    @Test
    public void testSetComponent() {
        // Create DynamicSelectModel object
        DynamicSelectModel model = new DynamicSelectModel();
        
        // Test when component is null
        assertNull(model.getComponent());
        
        // Test when component is not null
        Component component = new Component();
        model.setComponent(component);
        assertEquals(component, model.getComponent());
    }
    
    @Test
    public void testGetTopLevelComponent() {
        // Create DynamicSelectModel object
        DynamicSelectModel model = new DynamicSelectModel();
        
        // Test when topLevelComponent is null
        assertNull(model.getTopLevelComponent());
        
        // Test when topLevelComponent is not null
        StandaloneComponent component = new StandaloneComponent();
        model.setTopLevelComponent(component);
        assertEquals(component, model.getTopLevelComponent());
    }
    
    @Test
    public void testSetTopLevelComponent() {
        // Create DynamicSelectModel object
        DynamicSelectModel model = new DynamicSelectModel();
        
        // Test when topLevelComponent is null
        assertNull(model.getTopLevelComponent());
        
        // Test when topLevelComponent is not null
        StandaloneComponent component = new StandaloneComponent();
        model.setTopLevelComponent(component);
        assertEquals(component, model.getTopLevelComponent());
    }
    
    @Test
    public void testValidate() {
        // Create DynamicSelectModel object
        DynamicSelectModel model = new DynamicSelectModel();
        
        // Test when collection is null
        try {
            model.validate();
            fail("Exception not thrown");
        } catch (WheelException e) {
            // Exception expected
        }
        
        // Test when collection is not null
        model.setCollection(new ElExpression("collection"));
        
        // Test when label is null
        try {
            model.validate();
            fail("Exception not thrown");
        } catch (WheelException e) {
            // Exception expected
        }
        
        // Test when label is not null
        model.setLabel(new ElExpression("label"));
        
        // Test when value is null
        try {
            model.validate();
            fail("Exception not thrown");
        } catch (WheelException e) {
            // Exception expected
        }
        
        // Test when value is not null
        model.setValue(new ElExpression("value"));
        
        // Test when all attributes are not null
        model.validate();
    }
    
    @Test
    public void testReset() {
        // Create DynamicSelectModel object
        DynamicSelectModel model = new DynamicSelectModel();
        
        // Set objects
        model.setObjects(new ArrayList<String>());
        model.getObjects().add("Option 1");
        
        // Test reset
        model.reset();
        assertNull(model.getObjects());
    }
}