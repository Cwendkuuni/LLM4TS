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
        dynamicSelectModel.collection("test");
        assertEquals("test", dynamicSelectModel.getCollection().getExpression());
    }
    
    @Test
    public void testLabel() {
        dynamicSelectModel.label("test");
        assertEquals("test", dynamicSelectModel.getLabel().getExpression());
    }
    
    @Test
    public void testValue() {
        dynamicSelectModel.value("test");
        assertEquals("test", dynamicSelectModel.getValue().getExpression());
    }
    
    @Test
    public void testEnumeration() {
        dynamicSelectModel.enumeration("test");
        assertEquals("test.values()", dynamicSelectModel.getCollection().getExpression());
        assertEquals("toString()", dynamicSelectModel.getLabel().getExpression());
        assertEquals("ordinal()", dynamicSelectModel.getValue().getExpression());
    }
    
    @Test
    public void testTranslator() {
        dynamicSelectModel.translator("test");
        assertEquals("test", dynamicSelectModel.getTranslator().getExpression());
    }
    
    @Test
    public void testEmpty() {
        Object obj = new Object();
        dynamicSelectModel.empty(obj);
        assertEquals(obj, dynamicSelectModel.getEmptyObject());
    }
    
    @Test
    public void testGetOptionCount() {
        dynamicSelectModel.setObjects(new ArrayList());
        assertEquals(0, dynamicSelectModel.getOptionCount());
        
        ArrayList objects = new ArrayList();
        objects.add(new Object());
        objects.add(new Object());
        dynamicSelectModel.setObjects(objects);
        assertEquals(2, dynamicSelectModel.getOptionCount());
    }
    
    @Test
    public void testGetLabel() {
        dynamicSelectModel.setObjects(new ArrayList());
        dynamicSelectModel.getLabel(0);
        
        ArrayList objects = new ArrayList();
        objects.add(new Object());
        objects.add(new Object());
        dynamicSelectModel.setObjects(objects);
        dynamicSelectModel.getLabel(1);
    }
    
    @Test
    public void testGetValue() {
        dynamicSelectModel.setObjects(new ArrayList());
        dynamicSelectModel.getValue(0);
        
        ArrayList objects = new ArrayList();
        objects.add(new Object());
        objects.add(new Object());
        dynamicSelectModel.setObjects(objects);
        dynamicSelectModel.getValue(1);
    }
    
    @Test
    public void testTranslateValue() {
        dynamicSelectModel.setObjects(new ArrayList());
        dynamicSelectModel.translateValue("test");
        
        ArrayList objects = new ArrayList();
        objects.add(new Object());
        objects.add(new Object());
        dynamicSelectModel.setObjects(objects);
        dynamicSelectModel.translateValue("test");
    }
    
    @Test
    public void testGetObjects() {
        dynamicSelectModel.setObjects(new ArrayList());
        assertEquals(new ArrayList(), dynamicSelectModel.getObjects());
        
        ArrayList objects = new ArrayList();
        objects.add(new Object());
        objects.add(new Object());
        dynamicSelectModel.setObjects(objects);
        assertEquals(objects, dynamicSelectModel.getObjects());
    }
    
    @Test
    public void testSetComponent() {
        Component component = new Component();
        dynamicSelectModel.setComponent(component);
        assertEquals(component, dynamicSelectModel.getComponent());
    }
    
    @Test
    public void testSetTopLevelComponent() {
        StandaloneComponent topLevelComponent = new StandaloneComponent();
        dynamicSelectModel.setTopLevelComponent(topLevelComponent);
        assertEquals(topLevelComponent, dynamicSelectModel.getTopLevelComponent());
    }
    
    @Test
    public void testValidate() {
        try {
            dynamicSelectModel.validate();
            fail("Expected WheelException to be thrown");
        } catch(WheelException e) {
            assertEquals("Could not create DynamicSelection model. Collection attribute was null or not an el-expression.", e.getMessage());
            assertNull(e.getComponent());
        }
        
        dynamicSelectModel.collection("test");
        try {
            dynamicSelectModel.validate();
            fail("Expected WheelException to be thrown");
        } catch(WheelException e) {
            assertEquals("Could not create DynamicSelection model. Label attribute was null or not an el-expression.", e.getMessage());
            assertNull(e.getComponent());
        }
        
        dynamicSelectModel.label("test");
        try {
            dynamicSelectModel.validate();
            fail("Expected WheelException to be thrown");
        } catch(WheelException e) {
            assertEquals("Could not create DynamicSelection model. Value attribute was null or not an el-expression.", e.getMessage());
            assertNull(e.getComponent());
        }
        
        dynamicSelectModel.value("test");
        dynamicSelectModel.validate();
    }
    
    @Test
    public void testReset() {
        ArrayList objects = new ArrayList();
        objects.add(new Object());
        dynamicSelectModel.setObjects(objects);
        dynamicSelectModel.reset();
        assertNull(dynamicSelectModel.getObjects());
    }
}