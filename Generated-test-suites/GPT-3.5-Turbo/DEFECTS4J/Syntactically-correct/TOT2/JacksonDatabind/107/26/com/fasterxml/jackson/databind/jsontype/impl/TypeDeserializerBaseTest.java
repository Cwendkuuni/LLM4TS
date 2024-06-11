import static org.junit.Assert.*;
import org.junit.Test;

public class TypeDeserializerBaseTest {

    @Test
    public void testBaseTypeName() {
        TypeDeserializerBase base = new TypeDeserializerBaseConcrete();
        assertEquals("com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase", base.baseTypeName());
    }

    @Test
    public void testGetPropertyName() {
        TypeDeserializerBase base = new TypeDeserializerBaseConcrete();
        assertEquals("_typePropertyName", base.getPropertyName());
    }

    @Test
    public void testGetTypeIdResolver() {
        TypeDeserializerBase base = new TypeDeserializerBaseConcrete();
        assertNotNull(base.getTypeIdResolver());
    }

    @Test
    public void testGetDefaultImpl() {
        TypeDeserializerBase base = new TypeDeserializerBaseConcrete();
        assertNull(base.getDefaultImpl());
    }
    
    @Test
    public void testToString() {
        TypeDeserializerBase base = new TypeDeserializerBaseConcrete();
        assertEquals("[com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase; base-type:null; id-resolver: null]", base.toString());
    }

    private static class TypeDeserializerBaseConcrete extends TypeDeserializerBase {
        public TypeDeserializerBaseConcrete() {
            super(null, null, "_typePropertyName", true, null);
        }
        
        @Override
        public TypeDeserializer forProperty(BeanProperty prop) {
            return null;
        }

        @Override
        public JsonTypeInfo.As getTypeInclusion() {
            return JsonTypeInfo.As.EXISTING_PROPERTY;
        }
    }

}