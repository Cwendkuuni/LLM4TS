import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class AttributesTest {
    
    private Attributes attributes;
    
    @Before
    public void setup() {
        attributes = new Attributes();
    }
    
    @Test
    public void testGet() {
        attributes.put("key1", "value1");
        String value = attributes.get("key1");
        assertThat(value, is("value1"));
    }
    
    @Test
    public void testGetIgnoreCase() {
        attributes.put("key1", "value1");
        String value = attributes.getIgnoreCase("KEY1");
        assertThat(value, is("value1"));
    }
    
    @Test
    public void testPut() {
        attributes.put("key1", "value1");
        String value = attributes.get("key1");
        assertThat(value, is("value1"));
    }
    
    @Test
    public void testPutBoolean() {
        attributes.put("key1", true);
        boolean hasKey = attributes.hasKey("key1");
        assertThat(hasKey, is(true));
    }
    
    @Test
    public void testPutAttribute() {
        Attribute attribute = new Attribute("key1", "value1");
        attributes.put(attribute);
        String value = attributes.get("key1");
        assertThat(value, is("value1"));
    }
    
    @Test
    public void testRemove() {
        attributes.put("key1", "value1");
        attributes.remove("key1");
        boolean hasKey = attributes.hasKey("key1");
        assertThat(hasKey, is(false));
    }
    
    @Test
    public void testRemoveIgnoreCase() {
        attributes.put("key1", "value1");
        attributes.removeIgnoreCase("KEY1");
        boolean hasKey = attributes.hasKey("key1");
        assertThat(hasKey, is(false));
    }
    
    @Test
    public void testSize() {
        attributes.put("key1", "value1");
        int size = attributes.size();
        assertThat(size, is(1));
    }
    
    @Test
    public void testAddAll() {
        Attributes other = new Attributes();
        other.put("key2", "value2");
        other.put("key3", "value3");
        attributes.addAll(other);
        int size = attributes.size();
        assertThat(size, is(2));
    }
    
    @Test
    public void testClone() {
        attributes.put("key1", "value1");
        Attributes cloned = attributes.clone();
        assertThat(cloned, is(not(sameInstance(attributes))));
    }
    
    @Test
    public void testDataset() {
        attributes.put("data-key1", "value1");
        Map<String, String> dataset = attributes.dataset();
        String value = dataset.get("key1");
        assertThat(value, is("value1"));
    }
    
    @Test
    public void testHtml() {
        attributes.put("key1", "value1");
        String html = attributes.html();
        assertThat(html, containsString("key1=\"value1\""));
    }
}