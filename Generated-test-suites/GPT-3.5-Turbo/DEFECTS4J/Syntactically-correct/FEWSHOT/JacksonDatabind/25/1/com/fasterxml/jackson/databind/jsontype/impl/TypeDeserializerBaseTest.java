import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

public class TypeDeserializerBaseTest {
    
    private TypeDeserializerBase deserializer;
    private JsonDeserializer<Object> defaultImplDeserializer;
    
    @Before
    public void setup() {
        JavaType baseType = mock(JavaType.class);
        TypeIdResolver idResolver = mock(TypeIdResolver.class);
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        deserializer = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, Object.class) {
            
            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
            
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }
        };
        
        defaultImplDeserializer = new JsonDeserializer<Object>() {
            
            @Override
            public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return null;
            }
        };
    }

    @Test
    public void testBaseTypeName() {
        assertEquals("java.lang.Object", deserializer.baseTypeName());
    }
    
    @Test
    public void testGetPropertyName() {
        assertEquals("type", deserializer.getPropertyName());
    }
    
    @Test
    public void testGetTypeIdResolver() {
        assertNotNull(deserializer.getTypeIdResolver());
    }
    
    @Test
    public void testGetDefaultImpl() {
        assertNull(deserializer.getDefaultImpl());
    }
    
    @Test
    public void testToString() {
        assertEquals("[class com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase; base-type:null; id-resolver: null]", deserializer.toString());
    }
    
    @Test
    public void testFindDeserializer() throws IOException {
        DeserializationContext ctxt = mock(DeserializationContext.class);
        JsonDeserializer<Object> deser = deserializer._findDeserializer(ctxt, "type");
        assertNull(deser);
    }
    
    @Test
    public void testFindDefaultImplDeserializer() throws IOException {
        DeserializationContext ctxt = mock(DeserializationContext.class);
        JsonDeserializer<Object> deser = deserializer._findDefaultImplDeserializer(ctxt);
        assertNull(deser);
    }
    
    @Test
    public void testHandleUnknownTypeId() throws IOException {
        DeserializationContext ctxt = mock(DeserializationContext.class);
        String typeId = "type";
        TypeIdResolver idResolver = mock(TypeIdResolver.class);
        JavaType baseType = mock(JavaType.class);
        try {
            deserializer._handleUnknownTypeId(ctxt, typeId, idResolver, baseType);
        } catch (Exception e) {
            assertEquals("com.fasterxml.jackson.databind.JsonMappingException", e.getClass().getName());
        }
    }
    
    @Test
    public void testDeserializeWithNativeTypeId() throws IOException {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object result = deserializer._deserializeWithNativeTypeId(jp, ctxt);
        assertNull(result);
    }
    
    @Test
    public void testDeserializeWithNativeTypeIdWithType() throws IOException {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object typeId = "type";
        Object result = deserializer._deserializeWithNativeTypeId(jp, ctxt, typeId);
        assertNull(result);
    }
}