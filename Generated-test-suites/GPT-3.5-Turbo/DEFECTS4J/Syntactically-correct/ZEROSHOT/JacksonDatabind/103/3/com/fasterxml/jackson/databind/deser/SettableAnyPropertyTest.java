import org.junit.Test;
import static org.junit.Assert.*;

public class SettableAnyPropertyTest {

    @Test
    public void testDeserializeAndSet() throws Exception {
        SettableAnyProperty property = new SettableAnyProperty(null, null, null, null, null, null);
        property.deserializeAndSet(null, null, null, "");
    }

    @Test
    public void testDeserialize() throws Exception {
        SettableAnyProperty property = new SettableAnyProperty(null, null, null, null, null, null);
        property.deserialize(null, null);
    }

    @Test
    public void testSet() throws Exception {
        SettableAnyProperty property = new SettableAnyProperty(null, null, null, null, null, null);
        property.set(null, null, null);
    }

    @Test
    public void testGetValueDeserializer() throws Exception {
        SettableAnyProperty property = new SettableAnyProperty(null, null, null, null, null, null);
        assertFalse(property.hasValueDeserializer());
    }

    @Test
    public void testGetType() throws Exception {
        SettableAnyProperty property = new SettableAnyProperty(null, null, null, null, null, null);
        assertNull(property.getType());
    }
}