
import org.junit.*;
import static org.junit.Assert.*;
import wheel.components.*;
import wheel.*;

public class DynamicSelectModelTest {
    private DynamicSelectModel dynamicSelectModel;
    
    @Before
    public void setUp() {
        dynamicSelectModel = new DynamicSelectModel();
    }
    
    @Test
    public void testGetOptionCount() {
        dynamicSelectModel.collection("testCollection");
        dynamicSelectModel.setComponent(new Component());
        dynamicSelectModel.setTopLevelComponent(new StandaloneComponent());
        
        assertEquals(0, dynamicSelectModel.getOptionCount());
        
        dynamicSelectModel.collection("testCollection2");
        
        assertEquals(0, dynamicSelectModel.getOptionCount());
    }
    
    @Test
    public void testGetLabel() {
        dynamicSelectModel.collection("testCollection");
        dynamicSelectModel.setComponent(new Component());
        dynamicSelectModel.setTopLevelComponent(new StandaloneComponent());
        dynamicSelectModel.label("testLabel");
        
        assertEquals("testLabel0", dynamicSelectModel.getLabel(0));
        
        dynamicSelectModel.collection("testCollection2");
        
        assertNull(dynamicSelectModel.getLabel(0));
    }
    
    @Test
    public void testGetValue() {
        dynamicSelectModel.collection("testCollection");
        dynamicSelectModel.setComponent(new Component());
        dynamicSelectModel.setTopLevelComponent(new StandaloneComponent());
        dynamicSelectModel.value("testValue");
        
        assertEquals("testValue0", dynamicSelectModel.getValue(0));
        
        dynamicSelectModel.collection("testCollection2");
        
        assertNull(dynamicSelectModel.getValue(0));
    }
    
    @Test
    public void testTranslateValue() {
        dynamicSelectModel.collection("testCollection");
        dynamicSelectModel.setComponent(new Component());
        dynamicSelectModel.setTopLevelComponent(new StandaloneComponent());
        dynamicSelectModel.translator("testTranslator");
        
        assertNull(dynamicSelectModel.translateValue("testValue"));
        
        dynamicSelectModel.collection("testCollection2");
        dynamicSelectModel.translator("testTranslator2");
        
        assertNull(dynamicSelectModel.translateValue(null));
    }
    
    @Test
    public void testGetObjects() {
        dynamicSelectModel.collection("testCollection");
        dynamicSelectModel.setComponent(new Component());
        dynamicSelectModel.setTopLevelComponent(new StandaloneComponent());
        
        assertNotNull(dynamicSelectModel.getObjects());
        
        dynamicSelectModel.collection("testCollection2");
        
        assertNull(dynamicSelectModel.getObjects());
    }
    
    @Test(expected=Wheelexception.class)
    public void testValidateWithNullOrEmptyCollection() {
        dynamicSelectModel.validate();
    }
    
    @Test(expected=Wheelexception.class)
    public void testValidateWithNullOrEmptyLabel() {
        dynamicSelectModel.collection("testCollection");
        dynamicSelectModel.validate();
    }
    
    @Test(expected=Wheelexception.class)
    public void testValidateWithNullOrEmptyValue() {
        dynamicSelectModel.collection("testCollection");
        dynamicSelectModel.label("testLabel");
        dynamicSelectModel.validate();
    }
    
    @Test
    public void testReset() {
        dynamicSelectModel.collection("testCollection");
        dynamicSelectModel.setComponent(new Component());
        dynamicSelectModel.setTopLevelComponent(new StandaloneComponent());
        
        assertNotNull(dynamicSelectModel.getObjects());
        
        dynamicSelectModel.reset();
        
        assertNull(dynamicSelectModel.getObjects());
    }
}

