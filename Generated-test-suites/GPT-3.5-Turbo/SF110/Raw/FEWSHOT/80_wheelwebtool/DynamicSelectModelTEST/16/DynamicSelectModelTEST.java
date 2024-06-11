package wheel.util.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.BeforeClass;
import org.junit.Test;
import wheel.ElExpression;
import wheel.StandaloneComponent;


public class DynamicSelectModelTest {
    
    private static DynamicSelectModel dynamicSelectModel;
    
    @BeforeClass
    public static void setUp() {
        dynamicSelectModel = new DynamicSelectModel();
    }
    
    @Test
    public void testGetOptionCount() {
        assertEquals(0, dynamicSelectModel.getOptionCount());
    }
    
    @Test
    public void testGetLabel() {
        assertNull(dynamicSelectModel.getLabel(0));
    }
    
    @Test
    public void testGetValue() {
        assertNull(dynamicSelectModel.getValue(0));
    }
    
    @Test
    public void testTranslateValue() {
        assertNull(dynamicSelectModel.translateValue(""));
    }
    
    @Test
    public void testGetObjects() {
        assertNull(dynamicSelectModel.getObjects());
    }
    
    @Test
    public void testSetComponent() {
        Component component = new Component();
        dynamicSelectModel.setComponent(component);
        assertEquals(component, dynamicSelectModel.getComponent());
    }
    
    @Test
    public void testSetTopLevelComponent() {
        StandaloneComponent standaloneComponent = new StandaloneComponent();
        dynamicSelectModel.setTopLevelComponent(standaloneComponent);
        assertEquals(standaloneComponent, dynamicSelectModel.getTopLevelComponent());
    }
    
    @Test(expected = WheelException.class)
    public void testValidateNullCollection() {
        dynamicSelectModel.validate();
    }
    
    @Test(expected = WheelException.class)
    public void testValidateNullLabel() {
        dynamicSelectModel.collection("collection").validate();
    }
    
    @Test(expected = WheelException.class)
    public void testValidateNullValue() {
        dynamicSelectModel.collection("collection").label("label").validate();
    }

    @Test
    public void testReset() {
        dynamicSelectModel.reset();
        assertNull(dynamicSelectModel.getObjects());
    }
}