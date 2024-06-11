package wheel.util;

import static org.junit.Assert.*;
import org.junit.*;

public class DynamicSelectModelTest {
    
    @Test
    public void testConstructor1() {
        DynamicSelectModel model = new DynamicSelectModel(new ElExpression("collection"), new ElExpression("label"), new ElExpression("value"));
        
        assertNotNull(model);
        assertEquals("collection", model.getCollection().getExpression());
        assertEquals("label", model.getLabel().getExpression());
        assertEquals("value", model.getValue().getExpression());
    }
    
    @Test
    public void testConstructor2() {
        DynamicSelectModel model = new DynamicSelectModel();
        
        assertNotNull(model);
        assertNull(model.getCollection());
        assertNull(model.getLabel());
        assertNull(model.getValue());
    }
    
    @Test
    public void testCollection() {
        DynamicSelectModel model = new DynamicSelectModel();
        model.collection("collection");
        
        assertNotNull(model.getCollection());
        assertEquals("collection", model.getCollection().getExpression());
    }
    
    @Test
    public void testLabel() {
        DynamicSelectModel model = new DynamicSelectModel();
        model.label("label");
        
        assertNotNull(model.getLabel());
        assertEquals("label", model.getLabel().getExpression());
    }
    
    @Test
    public void testValue() {
        DynamicSelectModel model = new DynamicSelectModel();
        model.value("value");
        
        assertNotNull(model.getValue());
        assertEquals("value", model.getValue().getExpression());
    }
    
    @Test
    public void testEnumeration() {
        DynamicSelectModel model = new DynamicSelectModel();
        model.enumeration("enumeration");
        
        assertNotNull(model.getCollection());
        assertEquals("enumeration.values()", model.getCollection().getExpression());
        assertNotNull(model.getLabel());
        assertEquals("toString()", model.getLabel().getExpression());
        assertNotNull(model.getValue());
        assertEquals("ordinal()", model.getValue().getExpression());
    }
    
    @Test
    public void testTranslator() {
        DynamicSelectModel model = new DynamicSelectModel();
        model.translator("translator");
        
        assertNotNull(model.getTranslator());
        assertEquals("translator", model.getTranslator().getExpression());
    }
    
    @Test
    public void testEmpty() {
        DynamicSelectModel model = new DynamicSelectModel();
        model.empty("empty");
        
        assertNotNull(model.getEmptyObject());
        assertEquals("empty", model.getEmptyObject());
    }
    
    @Test
    public void testGetOptionCount() {
        DynamicSelectModel model = new DynamicSelectModel();
        
        model.collection("collection");
        model.setObjects(new ArrayList<Object>());
        
        assertEquals(0, model.getOptionCount());
        
        model.getObjects().add("option1");
        model.getObjects().add("option2");
        
        assertEquals(2, model.getOptionCount());
    }
    
    @Test
    public void testGetLabel() {
        DynamicSelectModel model = new DynamicSelectModel();
        
        model.label("label");
        model.setObjects(new ArrayList<Object>());
        
        model.getObjects().add("option1");
        model.getObjects().add("option2");
        
        assertEquals("option1", model.getLabel(0));
        assertEquals("option2", model.getLabel(1));
    }
    
    @Test
    public void testGetValue() {
        DynamicSelectModel model = new DynamicSelectModel();
        
        model.value("value");
        model.setObjects(new ArrayList<Object>());
        
        model.getObjects().add("option1");
        model.getObjects().add("option2");
        
        assertEquals("option1", model.getValue(0));
        assertEquals("option2", model.getValue(1));
    }
    
    @Test
    public void testTranslateValue() {
        DynamicSelectModel model = new DynamicSelectModel();
        
        model.setObjects(new ArrayList<Object>());
        
        model.getObjects().add("option1");
        model.getObjects().add("option2");
        
        assertNull(model.translateValue("value"));
        
        model.value("value");
        
        assertNull(model.translateValue("value"));
        assertNull(model.translateValue("option1"));
        
        model.getObjects().add("value");
        
        assertEquals("value", model.translateValue("value"));
    }
    
    @Test
    public void testGetObjects() {
        DynamicSelectModel model = new DynamicSelectModel();
        
        model.collection("collection");
        model.setObjects(new ArrayList<Object>());
        
        assertNotNull(model.getObjects());
        assertTrue(model.getObjects().isEmpty());
        
        model.getObjects().add("option1");
        model.getObjects().add("option2");
        
        assertEquals(2, model.getObjects().size());
    }

    @Test
    public void testGetSetComponent() {
        DynamicSelectModel model = new DynamicSelectModel();
        Component component = new Component();
        
        assertNull(model.getComponent());
        
        model.setComponent(component);
        
        assertEquals(component, model.getComponent());
    }
    
    @Test
    public void testGetSetTopLevelComponent() {
        DynamicSelectModel model = new DynamicSelectModel();
        StandaloneComponent topLevelComponent = new StandaloneComponent();
        
        assertNull(model.getTopLevelComponent());
        
        model.setTopLevelComponent(topLevelComponent);
        
        assertEquals(topLevelComponent, model.getTopLevelComponent());
    }
    
    @Test
    public void testValidate() {
        DynamicSelectModel model = new DynamicSelectModel();
        
        try {
            model.validate();
            fail("WheelException not thrown");
        } catch (WheelException e) {
            assertEquals("Could not create DynamicSelection model. Collection attribute was null or not an el-expression.", e.getMessage());
            assertNull(e.getComponent());
        }
        
        model.collection("collection");
        
        try {
            model.validate();
            fail("WheelException not thrown");
        } catch (WheelException e) {
            assertEquals("Could not create DynamicSelection model. Label attribute was null or not an el-expression.", e.getMessage());
            assertNull(e.getComponent());
        }
        
        model.label("label");
        
        try {
            model.validate();
            fail("WheelException not thrown");
        } catch (WheelException e) {
            assertEquals("Could not create DynamicSelection model. Value attribute was null or not an el-expression.", e.getMessage());
            assertNull(e.getComponent());
        }
        
        model.value("value");
        
        try {
            model.validate();
            assertNull(e);
        } catch (WheelException e) {
            assertNull(e);
        }
    }
    
    @Test
    public void testReset() {
        DynamicSelectModel model = new DynamicSelectModel();
        
        model.setObjects(new ArrayList<Object>());
        
        assertNotNull(model.getObjects());
        
        model.reset();
        
        assertNull(model.getObjects());
    }
    
}