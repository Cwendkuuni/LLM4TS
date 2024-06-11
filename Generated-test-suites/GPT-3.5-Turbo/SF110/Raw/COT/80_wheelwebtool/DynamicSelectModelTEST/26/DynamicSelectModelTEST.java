
import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;
import wheel.*;
import wheel.components.*;
import wheel.util.*;

public class DynamicSelectModelTest {
    private DynamicSelectModel model;
    
    @Before
    public void setUp() {
        model = new DynamicSelectModel();
    }
    
    @After
    public void tearDown() {
        model = null;
    }
    
    @Test
    public void testCollection() {
        String collection = "testCollection";
        model.collection(collection);
        assertEquals(collection, model.getCollection().getExpression());
    }
    
    @Test
    public void testLabel() {
        String label = "testLabel";
        model.label(label);
        assertEquals(label, model.getLabel().getExpression());
    }
    
    @Test
    public void testValue() {
        String value = "testValue";
        model.value(value);
        assertEquals(value, model.getValue().getExpression());
    }
    
    @Test
    public void testEnumeration() {
        String enumeration = "testEnumeration";
        model.enumeration(enumeration);
        assertEquals(enumeration + ".values()", model.getCollection().getExpression());
        assertEquals("toString()", model.getLabel().getExpression());
        assertEquals("ordinal()", model.getValue().getExpression());
    }
    
    @Test
    public void testTranslator() {
        String translator = "testTranslator";
        model.translator(translator);
        assertEquals(translator, model.getTranslator().getExpression());
    }
    
    @Test
    public void testEmpty() {
        Object o = new Object();
        model.empty(o);
        assertEquals(o, model.getEmptyObject());
    }
    
    @Test
    public void testGetOptionCount() {
        model.setObjects(Arrays.asList(1, 2, 3, 4, 5));
        assertEquals(5, model.getOptionCount());
    }
    
    @Test
    public void testGetLabel() {
        model.setObjects(Arrays.asList("A", "B", "C", "D", "E"));
        model.setComponent(new Component());
        assertEquals("A", model.getLabel(0));
        assertEquals("C", model.getLabel(2));
        assertEquals("E", model.getLabel(4));
    }
    
    @Test
    public void testGetValue() {
        model.setObjects(Arrays.asList(1, 2, 3, 4, 5));
        model.setComponent(new Component());
        assertEquals("1", model.getValue(0));
        assertEquals("3", model.getValue(2));
        assertEquals("5", model.getValue(4));
    }
    
    @Test
    public void testTranslateValue() {
        model.setObjects(Arrays.asList("A", "B", "C", "D", "E"));
        model.setComponent(new Component());
        model.setValue("1");
        assertEquals("A", model.translateValue("1"));
        model.setTranslator("translate");
        assertEquals("A", model.translateValue("1"));
    }
    
    @Test
    public void testGetObjects() {
        List<Object> objects = Arrays.asList(1, 2, 3, 4, 5);
        model.setCollection("testCollection");
        model.setTopLevelComponent(new StandaloneComponent());
        model.setComponent(new Component());
        model.setObjects(objects);
        assertEquals(objects, model.getObjects());
    }
    
    @Test
    public void testSetComponent() {
        Component component = new Component();
        model.setComponent(component);
        assertEquals(component, model.getComponent());
    }
    
    @Test
    public void testValidate() {
        try {
            model.validate();
            fail("Expected WheelException to be thrown");
        } catch (WheelException e) {
            assertEquals("Could not create DynamicSelection model. Collection attribute was null or not an el-expression.", e.getMessage());
        }
        
        try {
            model.setCollection("testCollection");
            model.validate();
            fail("Expected WheelException to be thrown");
        } catch (WheelException e) {
            assertEquals("Could not create DynamicSelection model. Label attribute was null or not an el-expression.", e.getMessage());
        }
        
        try {
            model.setLabel("testLabel");
            model.validate();
            fail("Expected WheelException to be thrown");
        } catch (WheelException e) {
            assertEquals("Could not create DynamicSelection model. Value attribute was null or not an el-expression.", e.getMessage());
        }
    }
    
    @Test
    public void testReset() {
        List<Object> objects = Arrays.asList(1, 2, 3, 4, 5);
        model.setObjects(objects);
        model.reset();
        assertNull(model.getObjects());
    }
}
