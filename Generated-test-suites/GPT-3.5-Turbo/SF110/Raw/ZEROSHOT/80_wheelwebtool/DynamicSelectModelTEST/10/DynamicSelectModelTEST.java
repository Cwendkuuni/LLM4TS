package wheel.util;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import wheel.components.Component;
import wheel.StandaloneComponent;
import wheel.ElExpression;
import wheel.WheelException;
import java.util.Collection;
import java.util.ArrayList;
import java.util.LinkedList;

public class DynamicSelectModelTest {
    
    private DynamicSelectModel dynamicSelectModel;
    private Component component;
    private StandaloneComponent topLevelComponent;
    
    @Before
    public void setup() {
        dynamicSelectModel = new DynamicSelectModel();
        component = new Component();
        topLevelComponent = new StandaloneComponent();
        dynamicSelectModel.setComponent(component);
        dynamicSelectModel.setTopLevelComponent(topLevelComponent);
    }
    
    @Test
    public void testGetOptionCount() {
        dynamicSelectModel.getObjects().add("Option 1");
        dynamicSelectModel.getObjects().add("Option 2");
        
        int expectedOptionCount = 2;
        int actualOptionCount = dynamicSelectModel.getOptionCount();
        
        Assert.assertEquals(expectedOptionCount, actualOptionCount);
    }
    
    @Test
    public void testGetLabel() {
        dynamicSelectModel.getObjects().add("Option 1");
        dynamicSelectModel.getObjects().add("Option 2");
        
        String expectedLabel = "Option 1";
        String actualLabel = dynamicSelectModel.getLabel(0);
        
        Assert.assertEquals(expectedLabel, actualLabel);
    }
    
    @Test
    public void testGetValue() {
        dynamicSelectModel.getObjects().add("Option 1");
        dynamicSelectModel.getObjects().add("Option 2");
        
        String expectedValue = "Option 1";
        String actualValue = dynamicSelectModel.getValue(0);
        
        Assert.assertEquals(expectedValue, actualValue);
    }
    
    @Test
    public void testTranslateValue() {
        dynamicSelectModel.getObjects().add("Option 1");
        dynamicSelectModel.getObjects().add("Option 2");
        
        Object expectedTranslatedValue = "Option 1";
        Object actualTranslatedValue = dynamicSelectModel.translateValue("Option 1");
        
        Assert.assertEquals(expectedTranslatedValue, actualTranslatedValue);
    }
    
    @Test
    public void testGetObjects() {
        dynamicSelectModel.getObjects().add("Option 1");
        dynamicSelectModel.getObjects().add("Option 2");
        
        Collection<Object> expectedObjects = new ArrayList<>();
        expectedObjects.add("Option 1");
        expectedObjects.add("Option 2");
        
        Collection<Object> actualObjects = dynamicSelectModel.getObjects();
        
        Assert.assertEquals(expectedObjects, actualObjects);
    }
    
    @Test(expected = WheelException.class)
    public void testValidate_NullCollection() {
        dynamicSelectModel.validate();
    }
    
    @Test(expected = WheelException.class)
    public void testValidate_NullLabel() {
        dynamicSelectModel.collection("collection");
        dynamicSelectModel.validate();
    }
    
    @Test(expected = WheelException.class)
    public void testValidate_NullValue() {
        dynamicSelectModel.collection("collection");
        dynamicSelectModel.label("label");
        dynamicSelectModel.validate();
    }
    
    @Test
    public void testReset() {
        dynamicSelectModel.getObjects().add("Option 1");
        dynamicSelectModel.getObjects().add("Option 2");
        
        dynamicSelectModel.reset();
        
        Assert.assertNull(dynamicSelectModel.getObjects());
    }
}