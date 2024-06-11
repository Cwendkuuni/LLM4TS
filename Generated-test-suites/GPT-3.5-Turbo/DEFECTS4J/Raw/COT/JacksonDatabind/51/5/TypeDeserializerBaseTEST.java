import org.junit.Test;
import static org.junit.Assert.*;

public class TypeDeserializerBaseTest {

    @Test
    public void testForProperty() {
        BeanProperty prop = null;
        TypeDeserializerBase base = new TypeDeserializerBase(null, null, "", false, null) {
            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
            
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }
        };
        
        TypeDeserializer result = base.forProperty(prop);
        assertNull(result);
    }
    
    @Test
    public void testGetPropertyTypeInclusion() {
        TypeDeserializerBase base = new TypeDeserializerBase(null, null, "", false, null) {
            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return JsonTypeInfo.As.EXTERNAL_PROPERTY;
            }
            
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }
        };
        
        JsonTypeInfo.As result = base.getPropertyTypeInclusion();
        assertEquals(JsonTypeInfo.As.EXTERNAL_PROPERTY, result);
    }
    
    @Test
    public void testBaseTypeName() {
        TypeDeserializerBase base = new TypeDeserializerBase(null, null, "", false, null) {
            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
            
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }
        };
        
        String result = base.baseTypeName();
        assertEquals("", result);
    }
    
    @Test
    public void testGetPropertyTypePropertyName() {
        TypeDeserializerBase base = new TypeDeserializerBase(null, null, "type", false, null) {
            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
            
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }
        };
        
        String result = base.getPropertyTypeName();
        assertEquals("type", result);
    }
    
    @Test
    public void testGetTypeIdResolver() {
        TypeDeserializerBase base = new TypeDeserializerBase(null, null, "", false, null) {
            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
            
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }
        };
        
        TypeIdResolver result = base.getTypeIdResolver();
        assertNull(result);
    }
    
    @Test
    public void testGetDefaultImpl() {
        TypeDeserializerBase base = new TypeDeserializerBase(null, null, "", false, null) {
            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
            
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }
        };
        
        Class<?> result = base.getDefaultImpl();
        assertNull(result);
    }
    
    @Test
    public void testToString() {
        TypeDeserializerBase base = new TypeDeserializerBase(null, null, "", false, null) {
            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
            
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }
        };
        
        String result = base.toString();
        assertEquals("[com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase; base-type:null; id-resolver: null]", result);
    }
    
    @Test
    public void testDeserializeWithNativeTypeId() throws Exception{
        TypeDeserializerBase base = new TypeDeserializerBase(null, null, "", false, null) {
            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
            
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }
        };
        
        Object result = base._deserializeWithNativeTypeId(null, null);
        assertNull(result);
    }
    
    @Test
    public void testDeserializeWithNativeTypeId2() throws Exception{
        TypeDeserializerBase base = new TypeDeserializerBase(null, null, "", false, null) {
            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
            
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }
        };
        
        Object result = base._deserializeWithNativeTypeId(null, null, "type");
        assertNull(result);
    }

    @Test
    public void testHandleUnknownTypeId() throws Exception{
        TypeDeserializerBase base = new TypeDeserializerBase(null, null, "", false, null) {
            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
            
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }
        };
        
        JavaType result = base._handleUnknownTypeId(null, "type", null, null);
        assertNull(result);
    }
}