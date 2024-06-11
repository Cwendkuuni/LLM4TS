import org.junit.Test;
import java.util.Collection;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class DynamicSelectModelTest {

    @Test
    public void testGetOptionCount() {
        DynamicSelectModel model = new DynamicSelectModel();
        Collection<Object> objects = new ArrayList<>();
        objects.add("Option 1");
        objects.add("Option 2");
        objects.add("Option 3");
        model.setObjects(objects);
        
        assertEquals(3, model.getOptionCount());
    }

    @Test
    public void testGetLabel() {
        DynamicSelectModel model = new DynamicSelectModel();
        Collection<Object> objects = new ArrayList<>();
        objects.add("Option 1");
        objects.add("Option 2");
        objects.add("Option 3");
        model.setObjects(objects);
        model.setComponent(new Component());
        
        assertEquals("Option 1", model.getLabel(0));
        assertEquals("Option 2", model.getLabel(1));
        assertEquals("Option 3", model.getLabel(2));
    }

    @Test
    public void testGetValue() {
        DynamicSelectModel model = new DynamicSelectModel();
        Collection<Object> objects = new ArrayList<>();
        objects.add("Option 1");
        objects.add("Option 2");
        objects.add("Option 3");
        model.setObjects(objects);
        model.setComponent(new Component());
        
        assertEquals("Option 1", model.getValue(0));
        assertEquals("Option 2", model.getValue(1));
        assertEquals("Option 3", model.getValue(2));
    }
    
    @Test
    public void testTranslateValue() {
        DynamicSelectModel model = new DynamicSelectModel();
        Collection<Object> objects = new ArrayList<>();
        objects.add("Option 1");
        objects.add("Option 2");
        objects.add("Option 3");
        model.setObjects(objects);
        model.setComponent(new Component());
        
        assertEquals("Option 1", model.translateValue("Option 1"));
        assertEquals("Option 2", model.translateValue("Option 2"));
        assertEquals("Option 3", model.translateValue("Option 3"));
        assertNull(model.translateValue("Option 4"));
    }
    
    @Test
    public void testGetObjects() {
        DynamicSelectModel model = new DynamicSelectModel();
        Collection<Object> objects = new ArrayList<>();
        objects.add("Option 1");
        objects.add("Option 2");
        objects.add("Option 3");
        model.setObjects(objects);
        
        assertEquals(objects, model.getObjects());
    }
}