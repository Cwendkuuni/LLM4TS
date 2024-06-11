import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import org.junit.Before;
import org.junit.Test;

public class TypeDeserializerBaseTest {

    private TypeDeserializerBase typeDeserializerBase;

    @Before
    public void setup() {
        JavaType baseType = mock(JavaType.class);
        TypeIdResolver idResolver = mock(TypeIdResolver.class);
        String typePropertyName = "testType";
        boolean typeIdVisible = true;
        JavaType defaultImpl = mock(JavaType.class);

        typeDeserializerBase = new TypeDeserializerBaseImpl(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
    }

    @Test
    public void testForProperty() {
        BeanProperty prop = mock(BeanProperty.class);
        TypeDeserializer deserializer = typeDeserializerBase.forProperty(prop);

        assertEquals(TypeDeserializerBaseImpl.class, deserializer.getClass());
    }

    @Test
    public void testGetTypeInclusion() {
        TypeDeserializer.As inclusion = typeDeserializerBase.getTypeInclusion();

        assertEquals(TypeDeserializer.As.PROPERTY, inclusion);
    }

    @Test
    public void testBaseTypeName() {
        when(typeDeserializerBase._baseType.getRawClass()).thenReturn(String.class);

        String baseTypeName = typeDeserializerBase.baseTypeName();

        assertEquals("java.lang.String", baseTypeName);
    }

    @Test
    public void testGetPropertyName() {
        String propertyName = typeDeserializerBase.getPropertyName();

        assertEquals("testType", propertyName);
    }

    @Test
    public void testGetTypeIdResolver() {
        TypeIdResolver typeIdResolver = typeDeserializerBase.getTypeIdResolver();

        assertEquals(TypeIdResolver.class, typeIdResolver.getClass());
    }

    @Test
    public void testGetDefaultImpl() {
        when(typeDeserializerBase._defaultImpl.getRawClass()).thenReturn(String.class);

        Class<?> defaultImpl = typeDeserializerBase.getDefaultImpl();

        assertEquals(String.class, defaultImpl);
    }

    @Test
    public void testBaseType() {
        JavaType baseType = typeDeserializerBase.baseType();

        assertEquals(JavaType.class, baseType.getClass());
    }

    @Test
    public void testToString() {
        String string = typeDeserializerBase.toString();

        assertEquals("[TypeDeserializerBaseImpl; base-type:JavaType; id-resolver: TypeIdResolver;]", string);
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws Exception {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object typeId = "testTypeId";

        Object deserializedObject = typeDeserializerBase._deserializeWithNativeTypeId(jp, ctxt, typeId);

        assertNull(deserializedObject);
        verify(jp).getTypeId();
        verify(typeDeserializerBase).getTypeIdResolver();
        verify(typeDeserializerBase).baseType();
        verify(typeDeserializerBase).baseTypeName();
        verify(typeDeserializerBase).getPropertyName();
        verify(typeDeserializerBase).getDefaultImpl();
        verify(typeDeserializerBase).getTypeInclusion();
    }

    @Test(expected = IOException.class)
    public void testHandleUnknownTypeId() throws Exception {
        DeserializationContext ctxt = mock(DeserializationContext.class);
        String typeId = "testTypeId";

        typeDeserializerBase._handleUnknownTypeId(ctxt, typeId);

        verify(ctxt).handleUnknownTypeId(typeDeserializerBase.baseType(), typeId, typeDeserializerBase.getTypeIdResolver(), "type ids are not statically known");
    }

    @Test
    public void testHandleMissingTypeId() throws Exception {
        DeserializationContext ctxt = mock(DeserializationContext.class);
        String extraDesc = "testDesc";

        JavaType missingTypeId = typeDeserializerBase._handleMissingTypeId(ctxt, extraDesc);

        assertNull(missingTypeId);
        verify(ctxt).handleMissingTypeId(typeDeserializerBase.baseType(), typeDeserializerBase.getTypeIdResolver(), extraDesc);
    }

    private class TypeDeserializerBaseImpl extends TypeDeserializerBase {

        protected TypeDeserializerBaseImpl(JavaType baseType, TypeIdResolver idRes, String typePropertyName, boolean typeIdVisible, JavaType defaultImpl) {
            super(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl);
        }

        @Override
        public TypeDeserializer forProperty(BeanProperty prop) {
            return this;
        }

        @Override
        public JsonTypeInfo.As getTypeInclusion() {
            return JsonTypeInfo.As.PROPERTY;
        }
    }
}