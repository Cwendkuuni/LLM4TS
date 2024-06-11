import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase;
import com.fasterxml.jackson.databind.jsontype.impl.TypeIdResolverBuilder;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class TypeDeserializerBaseTest {
    
    private TypeDeserializerBase deserializer;
    
    @Before
    public void setup() {
        TypeIdResolverBuilder<?> idResolverBuilder = new TypeIdResolverBuilder<>(TypeFactory.defaultInstance().constructType(Object.class));
        deserializer = new TypeDeserializerBaseStub(new JavaTypeStub(), idResolverBuilder, "type", true, new JavaTypeStub());
    }
    
    @Test
    public void testForProperty() {
        TypeDeserializerBase derivedDeserializer = (TypeDeserializerBase) deserializer.forProperty(null);
        assertNotNull(derivedDeserializer);
        assertNull(derivedDeserializer._property);
    }
    
    @Test
    public void testGetTypeInclusion() {
        assertEquals(TypeDeserializer.As.PROPERTY, deserializer.getTypeInclusion());
    }
    
    @Test
    public void testBaseTypeName() {
        assertEquals(JavaTypeStub.class.getName(), deserializer.baseTypeName());
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
        assertEquals(JavaTypeStub.class, deserializer.getDefaultImpl());
    }
    
    @Test
    public void testToString() {
        assertEquals("[com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase; base-type:JavaTypeStub; id-resolver: type-resolver]", deserializer.toString());
    }
    
    @Test
    public void testDeserializeWithNativeTypeId_NullTypeId() throws Exception {
        deserializer._deserializeWithNativeTypeId(null, null);
    }
    
    @Test(expected = com.fasterxml.jackson.databind.JsonMappingException.class)
    public void testDeserializeWithNativeTypeId_UnresolvedTypeId() throws Exception {
        deserializer._deserializeWithNativeTypeId(new JsonParserStub(null), null);
    }
    
    @Test
    public void testDeserializeWithNativeTypeId_ResolvedTypeId() throws Exception {
        JsonParserStub jp = new JsonParserStub("json");
        jp.setTypeId("type-id");
        Object value = deserializer._deserializeWithNativeTypeId(jp, null);
        assertNotNull(value);
    }
    
    @Test(expected = com.fasterxml.jackson.databind.JsonMappingException.class)
    public void testHandleUnknownTypeId() throws Exception {
        deserializer._handleUnknownTypeId(null, null, null, null);
    }
    
    private static class TypeDeserializerBaseStub extends TypeDeserializerBase {
        
        private static final long serialVersionUID = 1L;

        protected TypeDeserializerBaseStub(JavaType baseType, TypeIdResolverBuilder<?> idRes,
                String typePropertyName, boolean typeIdVisible, JavaType defaultImpl) {
            super(baseType, idRes.buildTypeIdResolver(), typePropertyName, typeIdVisible, defaultImpl);
        }

        protected TypeDeserializerBaseStub(TypeDeserializerBase src, BeanProperty property) {
            super(src, property);
        }

        @Override
        public TypeIdResolver getTypeIdResolver() {
            return super.getTypeIdResolver();
        }

        @Override
        public Class<?> getDefaultImpl() {
            return super.getDefaultImpl();
        }

        @Override
        public TypeDeserializerBase forProperty(BeanProperty prop) {
            return new TypeDeserializerBaseStub(this, prop);
        }

        @Override
        public asJsonTypeInfo.As getTypeInclusion() {
            return super.getTypeInclusion();
        }

        @Override
        public String getPropertyName() {
            return super.getPropertyName();
        }

        @Override
        public JavaType _handleUnknownTypeId(DeserializationContext ctxt, String typeId,
                TypeIdResolver idResolver, JavaType baseType) throws Exception {
            return super._handleUnknownTypeId(ctxt, typeId, idResolver, baseType);
        }

        @Override
        protected JavaType _findDefaultImplDeserializer(DeserializationContext ctxt) throws Exception {
            return super._findDefaultImplDeserializer(ctxt);
        }

        @Override
        protected JsonDeserializer<Object> _findDeserializer(DeserializationContext ctxt,
                String typeId) throws Exception {
            return super._findDeserializer(ctxt, typeId);
        }

        @Override
        public String toString() {
            return super.toString();
        }
        
    }
    
    private static class JavaTypeStub extends JavaType {
        
        private static final long serialVersionUID = 1L;

        private JavaTypeStub() {
            super(TypeFactory.defaultInstance());
        }

        @Override
        public Class<?> getRawClass() {
            return this.getClass();
        }
        
    }
    
    private static class JsonParserStub extends JsonParser {
        
        private String typeId;
        
        protected JsonParserStub(String text) {
            super(null, -1, null);
        }
        
        public void setTypeId(String typeId) {
            this.typeId = typeId;
        }

        @Override
        public Object getTypeId() {
            return typeId;
        }

        @Override
        public String getText() throws RuntimeException {
            return null;
        }

        @Override
        public int getTextLength() throws RuntimeException {
            return 0;
        }

        @Override
        public int getTextOffset() throws RuntimeException {
            return 0;
        }
        
        @Override
        public boolean isExpectedStartArrayToken() {
            return false;
        }

        @Override
        public boolean isExpectedStartObjectToken() {
            return false;
        }

        @Override
        public boolean isExpectedEndArrayToken() {
            return false;
        }

        @Override
        public boolean isExpectedEndObjectToken() {
            return false;
        }

        @Override
        public String nextFieldName() {
            return null;
        }

        @Override
        public boolean nextFieldName(SerializableString sstr) {
            return false;
        }

        @Override
        public int nextIntValue(int defaultValue) throws RuntimeException {
            return 0;
        }

        @Override
        public long nextLongValue(long defaultValue) throws RuntimeException {
            return 0;
        }

        @Override
        public String nextTextValue() throws RuntimeException {
            return null;
        }
        
    }
}