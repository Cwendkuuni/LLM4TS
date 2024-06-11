
import org.junit.Test;

import static org.junit.Assert.*;

public class CreatorPropertyTest {

    @Test
    public void testWithNullSetter() {
        CreatorProperty property = new CreatorProperty(null, null, null, null, null, null, 0, null, null);
        try {
            property.deserializeAndSet(null, null, null);
            fail("Expected an IOException to be thrown");
        } catch (IOException e) {
            assertEquals("No fallback setter/field defined for creator property 'null'", e.getMessage());
        }
    }

    @Test
    public void testFindInjectableValueNull() {
        CreatorProperty property = new CreatorProperty(null, null, null, null, null, null, 0, null, null);
        try {
            property.findInjectableValue(null, null);
            fail("Expected a JsonMappingException to be thrown");
        } catch (JsonMappingException e) {
            assertEquals("Property 'null' (type null) has no injectable value id configured", e.getMessage());
        }
    }
}
