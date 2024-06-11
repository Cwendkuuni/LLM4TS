import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectIdInfoTest {

    @Test
    public void testDefaultConstructor() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo();
        assertNull(objectIdInfo.getPropertyName());
        assertNull(objectIdInfo.getScope());
        assertNull(objectIdInfo.getGeneratorType());
        assertNull(objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    // Continue testing other methods following the same pattern
}