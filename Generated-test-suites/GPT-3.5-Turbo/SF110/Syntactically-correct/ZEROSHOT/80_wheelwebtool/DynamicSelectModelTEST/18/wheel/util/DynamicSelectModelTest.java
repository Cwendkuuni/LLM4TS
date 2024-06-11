package wheel.util;

import org.junit.Test;
import static org.junit.Assert.*;

public class DynamicSelectModelTest {

    @Test
    public void testGetOptionCount() {
        DynamicSelectModel model = new DynamicSelectModel();
        assertEquals(0, model.getOptionCount());
        
        model.collection("collection");
        model.setComponent(new Component());
        model.setTopLevelComponent(new StandaloneComponent());
        assertEquals(0, model.getOptionCount());
        
        model.getObjects().add(new Object());
        assertEquals(1, model.getOptionCount());
    }
    
    @Test
    public void testGetLabel() {
        DynamicSelectModel model = new DynamicSelectModel();
        model.setComponent(new Component());
        model.setTopLevelComponent(new StandaloneComponent());
        
        String label = model.getLabel(0);
        assertNotNull(label);
    }
    
    @Test
    public void testGetValue() {
        DynamicSelectModel model = new DynamicSelectModel();
        model.setComponent(new Component());
        model.setTopLevelComponent(new StandaloneComponent());
        
        String value = model.getValue(0);
        assertNotNull(value);
    }
    
    @Test
    public void testTranslateValue() {
        DynamicSelectModel model = new DynamicSelectModel();
        model.setComponent(new Component());
        model.setTopLevelComponent(new StandaloneComponent());
        
        Object translatedValue = model.translateValue("value");
        assertNull(translatedValue);
    }
    
    @Test
    public void testGetObjects() {
        DynamicSelectModel model = new DynamicSelectModel();
        model.setComponent(new Component());
        model.setTopLevelComponent(new StandaloneComponent());
        
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
    
    @Test
    public void testReset() {
        DynamicSelectModel model = new DynamicSelectModel();
        model.getObjects().add(new Object());
        
        model.reset();
        assertNull(model.getObjects());
    }
}