import org.junit.Test;
import static org.junit.Assert.*;

public class TypeDeserializerBaseTest {

    @Test
    public void testBaseTypeName() {
        TypeDeserializerBase deserializer = new TypeDeserializerBase() {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }
            
            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };
        String expected = "java.lang.Object";
        String actual = deserializer.baseTypeName();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetPropertyName() {
        TypeDeserializerBase deserializer = new TypeDeserializerBase() {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }
            
            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };
        String expected = "";
        String actual = deserializer.getPropertyName();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetTypeIdResolver() {
        TypeDeserializerBase deserializer = new TypeDeserializerBase() {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }
            
            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };
        assertNull(deserializer.getTypeIdResolver());
    }

    @Test
    public void testGetDefaultImpl() {
        TypeDeserializerBase deserializer = new TypeDeserializerBase() {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }
            
            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };
        assertNull(deserializer.getDefaultImpl());
    }
}