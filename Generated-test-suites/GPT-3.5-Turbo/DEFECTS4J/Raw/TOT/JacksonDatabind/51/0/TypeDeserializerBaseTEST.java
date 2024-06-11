
package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase;

public class TypeDeserializerBaseTest {

    private static class DummyIdResolver implements TypeIdResolver {
        @Override
        public void init(JavaType baseType) {
        }

        @Override
        public String idFromValue(Object value) {
            return null;
        }

        @Override
        public String idFromValueAndType(Object value, Class<?> suggestedType) {
            return null;
        }

        @Override
        public String idFromBaseType() {
            return null;
        }

        @Override
        public String idFromValueAndType(Object value, Class<?> suggestedType, TypeSerializer typeSer) {
            return null;
        }

        @Override
        public JavaType typeFromId(DatabindContext context, String id) {
            return null;
        }

        @Override
        public JsonTypeInfo.Id getMechanism() {
            return null;
        }
    }

    private static class DummyDeserializer extends JsonDeserializer<Object> {
        @Override
        public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return null;
        }
    }

    private static class DummyBeanProperty implements BeanProperty {
        @Override
        public String getName() {
            return null;
        }

        @Override
        public <A extends Annotation> A getAnnotation(Class<A> clazz) {
            return null;
        }

        @Override
        public <A extends Annotation> A getContextAnnotation(Class<A> clazz) {
            return null;
        }

        @Override
        public AnnotatedMember getMember() {
            return null;
        }

        @Override
        public TypeDeserializer findTypeDeserializer(DeserializationConfig config) throws JsonMappingException {
            return null;
        }

        @Override
        public String getFullName() {
            return null;
        }

        @Override
        public JavaType getType() {
            return null;
        }

        @Override
        public PropertyName getWrapperName() {
            return null;
        }

        @Override
        public boolean isRequired() {
            return false;
        }
    }

    private TypeDeserializerBase typeDeserializer;

    @Before
    public void setup() {
        JavaType baseType = JsonMapper.constructType(String.class);
        TypeIdResolver idResolver = new DummyIdResolver();
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        JavaType defaultImpl = JsonMapper.constructType(Object.class);
        typeDeserializer = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl) {
            private static final long serialVersionUID = 1L;

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
    public void testBaseTypeName() {
        assertEquals("java.lang.String", typeDeserializer.baseTypeName());
    }

    @Test
    public void testGetPropertyName() {
        assertEquals("type", typeDeserializer.getPropertyName());
    }

    @Test
    public void testGetTypeIdResolver() {
        assertNotNull(typeDeserializer.getTypeIdResolver());
        assertEquals(DummyIdResolver.class, typeDeserializer.getTypeIdResolver().getClass());
    }

    @Test
    public void testGetDefaultImpl() {
        assertNull(typeDeserializer.getDefaultImpl());
    }

    @Test
    public void testToString() {
        assertEquals("[com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase; base-type:java.lang.String; id-resolver: com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBaseTest$DummyIdResolver]", typeDeserializer.toString());
    }

    @Test
    public void testFindDeserializer() throws IOException {
        DeserializationContext ctxt = new DeserializationContext(null, null, null);

        assertNull(typeDeserializer._findDeserializer(ctxt, "dummy"));
    }

    @Test(expected = JsonParseException.class)
    public void testFindDefaultImplDeserializer() throws IOException {
        DeserializationContext ctxt = new DeserializationContext(null, null, null);

        typeDeserializer._findDefaultImplDeserializer(ctxt).deserialize(null, null);
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws IOException {
        JsonParser jp = new JsonParser(null, null, null);
        DeserializationContext ctxt = new DeserializationContext(null, null, null);

        assertNull(typeDeserializer._deserializeWithNativeTypeId(jp, ctxt));

        jp.setTypeId("dummy");
        assertNull(typeDeserializer._deserializeWithNativeTypeId(jp, ctxt, jp.getTypeId()));
    }

    @Test
    public void testHandleUnknownTypeId() throws IOException {
        DeserializationContext ctxt = new DeserializationContext(null, null, null);

        assertNull(typeDeserializer._handleUnknownTypeId(ctxt, "dummy", new DummyIdResolver(), null));
    }

}
