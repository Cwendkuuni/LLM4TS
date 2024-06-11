import org.junit.Test;
import static org.junit.Assert.*;

public class TypeDeserializerBaseTest {
    @Test
    public void testGetPropertyName() {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, null, false, null);
        String propertyName = typeDeserializerBase.getPropertyName();
        assertEquals("", propertyName);
    }
}