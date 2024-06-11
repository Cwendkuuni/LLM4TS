
import static org.junit.Assert.*;
import org.junit.Test;

public class TypeDeserializerBaseTest {

    @Test
    public void testForProperty() {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, null, true, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JrsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };

        assertNull(typeDeserializerBase.forProperty(null));
    }
    
    @Test
    public void testGetPropertyName() {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, "typePropertyName", true, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JrsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };
        
        assertEquals("typePropertyName", typeDeserializerBase.getPropertyName());
    }
    
    @Test
    public void testGetTypeIdResolver() {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, null, true, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JrsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };
        
        assertNull(typeDeserializerBase.getTypeIdResolver());
    }
    
    @Test
    public void testGetDefaultImpl() {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, null, true, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JrsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };
        
        assertNull(typeDeserializerBase.getDefaultImpl());
    }
    
    @Test
    public void testBaseType() {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, null, true, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JrsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };
        
        assertNull(typeDeserializerBase.baseType());
    }
    
    @Test
    public void testToString() {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, null, true, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JrsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };
        
        assertEquals("[com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase; base-type:null; id-resolver: null]", typeDeserializerBase.toString());
    }
    
}
