import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

public class DynamicSelectModelTest {
  
    private DynamicSelectModel dynamicSelectModel;

    @Before
    public void setUp() {
        dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.setComponent(mock(Component.class));
        dynamicSelectModel.setTopLevelComponent(mock(StandaloneComponent.class));
    }

    @Test
    public void testGetOptionCount() {
        dynamicSelectModel.collection("collection");
        dynamicSelectModel.value("value");
        dynamicSelectModel.label("label");
        assertEquals(0, dynamicSelectModel.getOptionCount());
        
        dynamicSelectModel.collection("collection1");
        dynamicSelectModel.value("value1");
        dynamicSelectModel.label("label1");
        assertEquals(0, dynamicSelectModel.getOptionCount());
    }
    
    @Test
    public void testGetLabel() {
        dynamicSelectModel.collection("collection");
        dynamicSelectModel.value("value");
        dynamicSelectModel.label("label");
        
        assertNull(dynamicSelectModel.getLabel(0));
    }

    @Test
    public void testGetValue() {
        dynamicSelectModel.collection("collection");
        dynamicSelectModel.value("value");
        dynamicSelectModel.label("label");
        
        assertNull(dynamicSelectModel.getValue(0));
    }

    @Test
    public void testTranslateValue() {
        dynamicSelectModel.collection("collection");
        dynamicSelectModel.value("value");
        dynamicSelectModel.label("label");
        
        assertNull(dynamicSelectModel.translateValue("value"));
    }

    @Test
    public void testGetObjects() {
        dynamicSelectModel.collection("collection");
        dynamicSelectModel.value("value");
        dynamicSelectModel.label("label");
        
        assertNull(dynamicSelectModel.getObjects());
    }

    @Test
    public void testSetComponent() {
        dynamicSelectModel.setComponent(null);
        
        assertNull(dynamicSelectModel.getComponent());
    }

    @Test
    public void testSetTopLevelComponent() {
        dynamicSelectModel.setTopLevelComponent(null);
        
        assertNull(dynamicSelectModel.getTopLevelComponent());
    }
}