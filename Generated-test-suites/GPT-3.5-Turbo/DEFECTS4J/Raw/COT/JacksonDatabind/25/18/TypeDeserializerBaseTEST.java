import static org.junit.Assert.*;
import org.junit.Test;

public class TypeDeserializerBaseTest {
    
    private TypeDeserializerBase deserializer = new TypeDeserializerBase() {
        @Override
        public JsonTypeInfo.As getTypeInclusion() {
            return null;
        }

        @Override
        public TypeDeserializer forProperty(BeanProperty prop) {
            return null;
        }
    };
    
    @Test
    public void testGetTypeInclusion() {
        assertNull(deserializer.getTypeInclusion());
    }

    @Test
    public void testBaseTypeName() {
        assertEquals("java.lang.Object", deserializer.baseTypeName());
    }

    @Test
    public void testGetPropertyName() {
        assertNull(deserializer.getPropertyName());
    }

    @Test
    public void testGetTypeIdResolver() {
        assertNull(deserializer.getTypeIdResolver());
    }

    @Test
    public void testGetDefaultImpl() {
        assertNull(deserializer.getDefaultImpl());
    }

    @Test
    public void testToString() {
        assertEquals("[com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase; base-type:null; id-resolver: null]", deserializer.toString());
    }

    @Test
    public void testForProperty() {
        assertNull(deserializer.forProperty(null));
    }
}