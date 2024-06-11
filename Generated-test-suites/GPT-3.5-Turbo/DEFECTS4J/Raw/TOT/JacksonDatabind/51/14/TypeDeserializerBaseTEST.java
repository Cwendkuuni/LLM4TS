import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TypeDeserializerBaseTest {

    private TypeDeserializerBase typeDeserializer;

    @Before
    public void setUp() {
        typeDeserializer = new TypeDeserializerBase() {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };
    }

    @Test
    public void test_baseTypeName() {
        assertEquals("com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase", typeDeserializer.baseTypeName());
    }

    @Test
    public void test_getPropertyName() {
        assertEquals("", typeDeserializer.getPropertyName());
    }

    @Test
    public void test_getTypeIdResolver() {
        assertNull(typeDeserializer.getTypeIdResolver());
    }

    @Test
    public void test_getDefaultImpl() {
        assertNull(typeDeserializer.getDefaultImpl());
    }

    @Test
    public void test_toString() {
        String expected = "[com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase; base-type:null; id-resolver: null]";
        assertEquals(expected, typeDeserializer.toString());
    }
}