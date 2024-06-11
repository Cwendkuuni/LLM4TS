import static org.junit.Assert.*;
import org.junit.Test;
import wheel.util.DynamicSelectModel;

public class DynamicSelectModelTest {

    @Test
    public void testConstructor() {
        DynamicSelectModel model = new DynamicSelectModel();
        assertNotNull(model);
    }
    
    @Test
    public void testCollection() {
        DynamicSelectModel model = new DynamicSelectModel();
        model.collection("col");
        assertNotNull(model.getObjects());
    }
    
    @Test
    public void testLabel() {
        DynamicSelectModel model = new DynamicSelectModel();
        model.label("label");
        assertNotNull(model.getLabel(0));
    }
    
    @Test
    public void testValue() {
        DynamicSelectModel model = new DynamicSelectModel();
        model.value("value");
        assertNotNull(model.getValue(0));
    }
    
    @Test
    public void testEnumeration() {
        DynamicSelectModel model = new DynamicSelectModel();
        model.enumeration("enum");
        assertNotNull(model.getObjects());
    }
    
    @Test
    public void testTranslator() {
        DynamicSelectModel model = new DynamicSelectModel();
        model.translator("translator");
        assertNotNull(model.translateValue("value"));
    }
    
    @Test
    public void testEmpty() {
        DynamicSelectModel model = new DynamicSelectModel();
        model.empty("empty");
        assertNotNull(model.getObjects());
    }
    
    @Test
    public void testGetOptionCount() {
        DynamicSelectModel model = new DynamicSelectModel();
        assertNotNull(model.getOptionCount());
    }
    
    @Test
    public void testGetLabel() {
        DynamicSelectModel model = new DynamicSelectModel();
        model.label("label");
        assertNotNull(model.getLabel(0));
    }
    
    @Test
    public void testGetValue() {
        DynamicSelectModel model = new DynamicSelectModel();
        model.value("value");
        assertNotNull(model.getValue(0));
    }
    
    @Test
    public void testTranslateValue() {
        DynamicSelectModel model = new DynamicSelectModel();
        model.translator("translator");
        assertNotNull(model.translateValue("value"));
    }
    
    @Test
    public void testGetObjects() {
        DynamicSelectModel model = new DynamicSelectModel();
        assertNotNull(model.getObjects());
    }
    
    @Test
    public void testSetComponent() {
        DynamicSelectModel model = new DynamicSelectModel();
        model.setComponent(null);
        assertNull(model.getComponent());
    }
    
    @Test
    public void testSetTopLevelComponent() {
        DynamicSelectModel model = new DynamicSelectModel();
        model.setTopLevelComponent(null);
        assertNull(model.getTopLevelComponent());
    }

    @Test
    public void testValidate() {
        DynamicSelectModel model = new DynamicSelectModel();
        assertThrows(WheelException.class, () -> {
            model.validate();
        });
    }
    
    @Test
    public void testReset() {
        DynamicSelectModel model = new DynamicSelectModel();
        model.reset();
        assertNull(model.getObjects());
    }

}