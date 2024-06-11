import org.junit.Test;
import static org.junit.Assert.*;

public class AttributesTest {

    private Attributes attributes;

    @Before
    public void setUp() {
        attributes = new Attributes();
    }

    @Test
    public void testGet() {
        attributes.put("key1", "value1");
        assertEquals("value1", attributes.get("key1"));
    }

    @Test
    public void testGetIgnoreCase() {
        attributes.put("Key2", "value2");
        assertEquals("value2", attributes.getIgnoreCase("key2"));
    }

    @Test
    public void testPut() {
        attributes.put("key3", "value3");
        assertEquals("value3", attributes.get("key3"));
    }

    @Test
    public void testRemove() {
        attributes.put("key4", "value4");
        attributes.remove("key4");
        assertFalse(attributes.hasKey("key4"));
    }

    @Test
    public void testHasKey() {
        attributes.put("key5", "value5");
        assertTrue(attributes.hasKey("key5"));
    }

    @Test
    public void testSize() {
        attributes.put("key6", "value6");
        attributes.put("key7", "value7");
        assertEquals(2, attributes.size());
    }

    @After
    public void tearDown() {
        attributes = null;
    }
}