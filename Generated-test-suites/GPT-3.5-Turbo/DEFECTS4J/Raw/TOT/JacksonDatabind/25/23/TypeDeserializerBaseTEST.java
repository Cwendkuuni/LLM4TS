import static org.junit.Assert.*;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class TypeDeserializerBaseTest {

    private TypeDeserializerBase typeDeserializerBase;
    private static final String TYPE_NAME = "com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase";
    private static final String TYPE_PROPERTY_NAME = "_typePropertyName";
    private static final boolean TYPE_ID_VISIBLE = true;
    private static final ConcurrentHashMap<String, JsonDeserializer<Object>> DESERIALIZERS = new ConcurrentHashMap<String, JsonDeserializer<Object>>(16, 0.75f, 4);
    private BeanProperty property;

    @Before
    public void setUp() {
        JavaType baseType = new JavaType(TYPE_NAME);
        TypeIdResolver idResolver = null;
        Class<?> defaultImpl = null;
        typeDeserializerBase = new TypeDeserializerBaseSubClass(baseType, idResolver, TYPE_PROPERTY_NAME, TYPE_ID_VISIBLE, defaultImpl);
    }
    
    @Test
    public void testForProperty() {
        BeanProperty prop = null;
        TypeDeserializer deserializer = typeDeserializerBase.forProperty(prop);
        assertNotNull(deserializer);
    }
    
    @Test
    public void testGetTypeInclusion() {
        JsonTypeInfo.As typeInfoAs = typeDeserializerBase.getTypeInclusion();
        assertNotNull(typeInfoAs);
    }
    
    @Test
    public void testBaseTypeName() {
        String name = typeDeserializerBase.baseTypeName();
        assertEquals(TYPE_NAME, name);
    }
    
    @Test
    public void testGetPropertyName() {
        String propertyName = typeDeserializerBase.getPropertyName();
        assertEquals(TYPE_PROPERTY_NAME, propertyName);
    }
    
    @Test
    public void testGetTypeIdResolver() {
        TypeIdResolver idResolver = typeDeserializerBase.getTypeIdResolver();
        assertNull(idResolver);
    }
    
    @Test
    public void testGetDefaultImpl() {
        Class<?> defaultImpl = typeDeserializerBase.getDefaultImpl();
        assertNull(defaultImpl);
    }
    
    @Test
    public void testToString() {
        String string = typeDeserializerBase.toString();
        assertNotNull(string);
        assertTrue(string.contains("com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase"));
        assertTrue(string.contains("base-type:com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase"));
        assertTrue(string.contains("id-resolver: null"));
    }
    
    @Test
    public void test_findDeserializerWhenDeserExistsInMap() throws IOException {
        DeserializationContext ctxt = null;
        String typeId = "1";
        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDeserializer(ctxt, typeId);
        assertNotNull(deserializer);
    }
    
    @Test
    public void test_findDeserializerWhenDeserDoesNotExistInMap() throws IOException {
        DeserializationContext ctxt = null;
        String typeId = "2";
        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDeserializer(ctxt, typeId);
        assertNull(deserializer);
    }
    
    @Test
    public void test_findDefaultImplDeserializerWhenDefaultImplIsNull() throws IOException {
        DeserializationContext ctxt = null;
        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDefaultImplDeserializer(ctxt);
        assertNull(deserializer);
    }
    
    @Test
    public void test_findDefaultImplDeserializerWhenDefaultImplIsVoid() throws IOException {
        DeserializationContext ctxt = null;
        typeDeserializerBase = new TypeDeserializerBaseSubClass(Void.class);
        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDefaultImplDeserializer(ctxt);
        assertNotNull(deserializer);
        assertEquals(NullifyingDeserializer.instance, deserializer);
    }
    
    @Test
    public void test_findDefaultImplDeserializerWhenDefaultImplIsBogusClass() throws IOException {
        DeserializationContext ctxt = null;
        typeDeserializerBase = new TypeDeserializerBaseSubClass(Void.class);
        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDefaultImplDeserializer(ctxt);
        assertNotNull(deserializer);
        assertEquals(NullifyingDeserializer.instance, deserializer);
    }
    
    @Test
    public void test_findDefaultImplDeserializer() throws IOException {
        DeserializationContext ctxt = null;
        typeDeserializerBase = new TypeDeserializerBaseSubClass(String.class);
        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDefaultImplDeserializer(ctxt);
        assertNotNull(deserializer);
    }
    
    @Test
    public void test_deserializeWithNativeTypeIdWhenTypeIdIsNull() throws IOException {
        DeserializationContext ctxt = null;
        Object object = typeDeserializerBase._deserializeWithNativeTypeId(null, ctxt);
        assertNotNull(object);
    }
    
    @Test(expected = JsonParseException.class)
    public void test_deserializeWithNativeTypeIdWhenTypeIdNotNullButDeserIsNull() throws IOException {
        DeserializationContext ctxt = null;
        Object typeId = "unknown";
        typeDeserializerBase._deserializeWithNativeTypeId(null, ctxt, typeId);
    }
    
    @Test
    public void test_handleUnknownTypeId() throws IOException {
        DeserializationContext ctxt = null;
        TypeIdResolver idResolver = null;
        JavaType baseType = null;
        String unknownTypeId = "unknown";
        JsonDeserializer<Object> deserializer = typeDeserializerBase._handleUnknownTypeId(ctxt, unknownTypeId, idResolver, baseType);
        assertNull(deserializer);
    }
    
    private class TypeDeserializerBaseSubClass extends TypeDeserializerBase {
        
        private static final long serialVersionUID = 1L;

        public TypeDeserializerBaseSubClass(JavaType baseType, TypeIdResolver idRes,
                String typePropertyName, boolean typeIdVisible, Class<?> defaultImpl) {
            super(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl);
        }
        
        public TypeDeserializerBaseSubClass(Class<?> defaultImpl) {
            super(null, null, null, false, defaultImpl);
        }

        @Override
        public TypeDeserializer forProperty(BeanProperty prop) {
            return null;
        }

        @Override
        public JsonTypeInfo.As getTypeInclusion() {
            return null;
        }
        
        @Override
        protected JsonDeserializer<Object> _handleUnknownTypeId(DeserializationContext ctxt, String typeId,
                TypeIdResolver idResolver, JavaType baseType) throws IOException {
            return null;
        }
        
        @Override
        protected Object _deserializeWithNativeTypeId(
                com.fasterxml.jackson.core.JsonParser jp,
                com.fasterxml.jackson.databind.DeserializationContext ctxt,
                Object typeId) throws IOException {
            return null;
        }
    }

}