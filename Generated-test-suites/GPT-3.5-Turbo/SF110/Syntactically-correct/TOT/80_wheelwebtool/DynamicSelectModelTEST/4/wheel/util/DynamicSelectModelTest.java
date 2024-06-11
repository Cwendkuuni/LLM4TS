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
    public void testConstructor_Default() {
        assertNotNull(dynamicSelectModel);
    }
    
    @Test
    public void testConstructor_WithExpressions() {
        ElExpression collection = new ElExpression("testCollection");
        ElExpression label = new ElExpression("testLabel");
        ElExpression value = new ElExpression("testValue");

        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel(collection, label, value);
        
        assertNotNull(dynamicSelectModel);
        assertEquals(collection, dynamicSelectModel.collection);
        assertEquals(label, dynamicSelectModel.label);
        assertEquals(value, dynamicSelectModel.value);
    }
    
    @Test
    public void testCollection_String() {
        String collection = "testCollection";
        
        DynamicSelectModel result = dynamicSelectModel.collection(collection);
        
        assertNotNull(result);
        assertNotNull(result.collection);
        assertEquals(collection, result.collection.getExpression());
    }
    
    @Test
    public void testLabel_String() {
        String label = "testLabel";
        
        DynamicSelectModel result = dynamicSelectModel.label(label);
        
        assertNotNull(result);
        assertNotNull(result.label);
        assertEquals(label, result.label.getExpression());
    }
    
    @Test
    public void testValue_String() {
        String value = "testValue";
        
        DynamicSelectModel result = dynamicSelectModel.value(value);
        
        assertNotNull(result);
        assertNotNull(result.value);
        assertEquals(value, result.value.getExpression());
    }
    
    @Test
    public void testEnumeration_String() {
        String enumeration = "testEnumeration";
        
        DynamicSelectModel result = dynamicSelectModel.enumeration(enumeration);
        
        assertNotNull(result);
        assertNotNull(result.collection);
        assertNotNull(result.label);
        assertNotNull(result.value);
        assertEquals(enumeration + ".values()", result.collection.getExpression());
        assertEquals("toString()", result.label.getExpression());
        assertEquals("ordinal()", result.value.getExpression());
    }
    
    @Test
    public void testTranslator_String() {
        String translator = "testTranslator";
        
        DynamicSelectModel result = dynamicSelectModel.translator(translator);
        
        assertNotNull(result);
        assertNotNull(result.translator);
        assertEquals(translator, result.translator.getExpression());
    }
    
    @Test
    public void testEmpty_Object() {
        Object emptyObject = new Object();
        
        DynamicSelectModel result = dynamicSelectModel.empty(emptyObject);
        
        assertNotNull(result);
        assertNotNull(result.emptyObject);
        assertEquals(emptyObject, result.emptyObject);
    }
    
    @Test
    public void testGetOptionCount() {
        dynamicSelectModel.setTopLevelComponent(new StandaloneComponent());
        dynamicSelectModel.setComponent(new Component());
        dynamicSelectModel.collection("testCollection");
        dynamicSelectModel.empty("emptyObject");
        dynamicSelectModel.getLabel(0);
        dynamicSelectModel.getValue(0);
        dynamicSelectModel.translateValue("testValue");
        
        int result = dynamicSelectModel.getOptionCount();
        
        assertNotEquals(0, result);
    }
    
    @Test
    public void testGetLabel_ValidIndex() {
        dynamicSelectModel.setComponent(new Component());
        dynamicSelectModel.collection("testCollection");
        dynamicSelectModel.label("testLabel");
        dynamicSelectModel.setTopLevelComponent(new StandaloneComponent());
        
        String result = dynamicSelectModel.getLabel(0);
        
        assertNotNull(result);
    }
    
    @Test(expected = WheelException.class)
    public void testGetLabel_InvalidIndex() {
        dynamicSelectModel.setComponent(new Component());
        dynamicSelectModel.collection("testCollection");
        dynamicSelectModel.label("testLabel");
        dynamicSelectModel.setTopLevelComponent(new StandaloneComponent());
        
        dynamicSelectModel.getLabel(10);
    }
    
    @Test
    public void testGetValue_ValidIndex() {
        dynamicSelectModel.setComponent(new Component());
        dynamicSelectModel.collection("testCollection");
        dynamicSelectModel.value("testValue");
        dynamicSelectModel.setTopLevelComponent(new StandaloneComponent());
        
        String result = dynamicSelectModel.getValue(0);
        
        assertNotNull(result);
    }
    
    @Test(expected = WheelException.class)
    public void testGetValue_InvalidIndex() {
        dynamicSelectModel.setComponent(new Component());
        dynamicSelectModel.collection("testCollection");
        dynamicSelectModel.value("testValue");
        dynamicSelectModel.setTopLevelComponent(new StandaloneComponent());
        
        dynamicSelectModel.getValue(10);
    }
    
    @Test
    public void testTranslateValue_Translated() {
        dynamicSelectModel.setComponent(new Component());
        dynamicSelectModel.collection("testCollection");
        dynamicSelectModel.value("testValue");
        dynamicSelectModel.setTopLevelComponent(new StandaloneComponent());
        dynamicSelectModel.enumeration("testEnumeration");
        dynamicSelectModel.translator("testTranslator");
        
        Object result = dynamicSelectModel.translateValue("testValue");
        
        assertNotNull(result);
    }
    
    @Test
    public void testTranslateValue_NotTranslated() {
        dynamicSelectModel.setComponent(new Component());
        dynamicSelectModel.collection("testCollection");
        dynamicSelectModel.value("testValue");
        dynamicSelectModel.setTopLevelComponent(new StandaloneComponent());
        dynamicSelectModel.enumeration("testEnumeration");
        
        Object result = dynamicSelectModel.translateValue("testValue");
        
        assertNotNull(result);
    }
    
    @Test
    public void testGetObjects_Collection() {
        dynamicSelectModel.setComponent(new Component());
        dynamicSelectModel.collection("testCollection");
        dynamicSelectModel.value("testValue");
        dynamicSelectModel.setTopLevelComponent(new StandaloneComponent());
        
        Collection result = dynamicSelectModel.getObjects();
        
        assertNotNull(result);
    }
    
    @Test(expected = WheelException.class)
    public void testGetObjects_NullCollection() {
        dynamicSelectModel.setComponent(new Component());
        dynamicSelectModel.value("testValue");
        dynamicSelectModel.setTopLevelComponent(new StandaloneComponent());
        
        dynamicSelectModel.getObjects();
    }
    
    @Test(expected = WheelException.class)
    public void testGetObjects_InvalidObjectType() {
        dynamicSelectModel.setComponent(new Component());
        dynamicSelectModel.collection("testCollection");
        dynamicSelectModel.setTopLevelComponent(new StandaloneComponent());
        
        dynamicSelectModel.getObjects();
    }
    
    @Test
    public void testReset() {
        dynamicSelectModel.setComponent(new Component());
        dynamicSelectModel.collection("testCollection");
        dynamicSelectModel.setTopLevelComponent(new StandaloneComponent());
        
        dynamicSelectModel.reset();
        
        assertNull(dynamicSelectModel.getObjects());
    }
    
    @Test
    public void testGetComponent() {
        Component component = new Component();
        
        dynamicSelectModel.setComponent(component);
        
        assertNotNull(dynamicSelectModel.getComponent());
        assertEquals(component, dynamicSelectModel.getComponent());
    }
    
    @Test
    public void testSetComponent() {
        Component component = new Component();
        
        dynamicSelectModel.setComponent(component);
        
        assertNotNull(dynamicSelectModel.getComponent());
        assertEquals(component, dynamicSelectModel.getComponent());
    }
    
    @Test
    public void testGetTopLevelComponent() {
        StandaloneComponent standaloneComponent = new StandaloneComponent();
        
        dynamicSelectModel.setTopLevelComponent(standaloneComponent);
        
        assertNotNull(dynamicSelectModel.getTopLevelComponent());
        assertEquals(standaloneComponent, dynamicSelectModel.getTopLevelComponent());
    }
    
    @Test
    public void testSetTopLevelComponent() {
        StandaloneComponent standaloneComponent = new StandaloneComponent();
        
        dynamicSelectModel.setTopLevelComponent(standaloneComponent);
        
        assertNotNull(dynamicSelectModel.getTopLevelComponent());
        assertEquals(standaloneComponent, dynamicSelectModel.getTopLevelComponent());
    }
    
    @Test(expected = WheelException.class)
    public void testValidate_CollectionNull() {
        dynamicSelectModel.setComponent(new Component());
        
        dynamicSelectModel.validate();
    }
    
    @Test(expected = WheelException.class)
    public void testValidate_LabelNull() {
        dynamicSelectModel.setComponent(new Component());
        dynamicSelectModel.collection("testCollection");
        
        dynamicSelectModel.validate();
    }
    
    @Test(expected = WheelException.class)
    public void testValidate_ValueNull() {
        dynamicSelectModel.setComponent(new Component());
        dynamicSelectModel.collection("testCollection");
        dynamicSelectModel.label("testLabel");
        
        dynamicSelectModel.validate();
    }
    
}