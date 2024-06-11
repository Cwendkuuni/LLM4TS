import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;

@RunWith(MockitoJUnitRunner.class)
public class TypeDeserializerBaseTest {

    private TypeDeserializerBase typeDeserializerBase;

    @Mock
    private TypeIdResolver typeIdResolver;
    
    @Mock
    private DeserializationContext deserializationContext;
    
    @Mock
    private JsonParser jsonParser;
    
    @Mock
    private BeanProperty beanProperty;
    
    @Captor
    private ArgumentCaptor<JavaType> javaTypeCaptor;
    
    @Before
    public void setUp() {
        typeDeserializerBase = new TypeDeserializerBaseTestClass(JavaTypeFactory.defaultInstance().constructType(String.class), typeIdResolver, "type", true, String.class);
    }

    @Test
    public void testForProperty() {
        TypeDeserializerBase result = typeDeserializerBase.forProperty(beanProperty);
        assertNotNull(result);
        assertEquals(typeDeserializerBase.getClass(), result.getClass());
    }

    @Test
    public void testGetTypeInclusion() {
        assertEquals(JsonTypeInfo.As.WRAPPER_ARRAY, TypeDeserializerBaseTestClass.INSTANCE.getTypeInclusion());
    }

    @Test
    public void testBaseTypeName() {
        assertEquals(String.class.getName(), typeDeserializerBase.baseTypeName());
    }

    @Test
    public void testGetPropertyName() {
        assertEquals("type", typeDeserializerBase.getPropertyName());
    }

    @Test
    public void testGetTypeIdResolver() {
        assertEquals(typeIdResolver, typeDeserializerBase.getTypeIdResolver());
    }

    @Test
    public void testGetDefaultImpl() {
        assertEquals(String.class, typeDeserializerBase.getDefaultImpl());
    }

    @Test
    public void testFindContextualValueDeserializer() throws IOException {
        JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);
        JavaType javaType = JavaTypeFactory.defaultInstance().constructType(String.class);
        when(deserializationContext.findContextualValueDeserializer(javaTypeCaptor.capture(), any(BeanProperty.class))).thenReturn(deserializer);

        JsonDeserializer<Object> result = typeDeserializerBase.findContextualValueDeserializer(javaType, beanProperty);
        
        assertEquals(deserializer, result);
        assertEquals(javaType.getRawClass(), javaTypeCaptor.getValue().getRawClass());
    }

    @Test
    public void testToString() {
        String expected = "[" + TypeDeserializerBaseTestClass.class.getName()
                + "; base-type:" + JavaTypeFactory.defaultInstance().constructType(String.class)
                + "; id-resolver: " + typeIdResolver
                + "]";
        assertEquals(expected, typeDeserializerBase.toString());
    }

    @Test
    public void testFindDeserializer_TypeIdFound() throws IOException {
        String typeId = "String";
        JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);
        when(typeIdResolver.typeFromId(deserializationContext, typeId)).thenReturn(JavaTypeFactory.defaultInstance().constructType(String.class));
        when(deserializationContext.findContextualValueDeserializer(javaTypeCaptor.capture(), any(BeanProperty.class))).thenReturn(deserializer);

        JsonDeserializer<Object> result = typeDeserializerBase._findDeserializer(deserializationContext, typeId);
        
        assertEquals(deserializer, result);
        assertEquals(JavaTypeFactory.defaultInstance().constructType(String.class).getRawClass(), javaTypeCaptor.getValue().getRawClass());
    }

    @Test
    public void testFindDeserializer_TypeIdNotFound() throws IOException {
        String typeId = "InvalidType";
        JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);
        when(typeIdResolver.typeFromId(deserializationContext, typeId)).thenReturn(null);
        when(typeDeserializerBase._findDefaultImplDeserializer(deserializationContext)).thenReturn(deserializer);

        JsonDeserializer<Object> result = typeDeserializerBase._findDeserializer(deserializationContext, typeId);
        
        assertEquals(deserializer, result);
    }
    
    @Test(expected=IOException.class)
    public void testFindDeserializer_DefaultImplNotFoundFailOnInvalidSubtype() throws IOException {
        String typeId = "InvalidType";
        when(typeIdResolver.typeFromId(deserializationContext, typeId)).thenReturn(null);
        when(typeDeserializerBase._findDefaultImplDeserializer(deserializationContext)).thenReturn(null);
        when(deserializationContext.isEnabled(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE)).thenReturn(true);

        typeDeserializerBase._findDeserializer(deserializationContext, typeId);
    }
    
    @Test
    public void testDeserializeWithNativeTypeId_TypeIdNull() throws IOException {
        Object result = typeDeserializerBase._deserializeWithNativeTypeId(jsonParser, deserializationContext, null);
        
        verify(typeDeserializerBase)._deserializeWithNativeTypeId(jsonParser, deserializationContext);
        assertSame(mock(JsonDeserializer.class), result);
    }
    
    @Test
    public void testDeserializeWithNativeTypeId_TypeIdNotNull() throws IOException {
        Object result = typeDeserializerBase._deserializeWithNativeTypeId(jsonParser, deserializationContext, "String");
        
        verify(typeDeserializerBase)._findDeserializer(deserializationContext, "String");
        verify((JsonDeserializer<Object>) deserializer).deserialize(jsonParser, deserializationContext);
        assertSame(mock(JsonDeserializer.class), result);
    }

    @Test(expected=IOException.class)
    public void testHandleUnknownTypeId() throws IOException {
        String typeId = "UnknownType";
        when(typeIdResolver instanceof TypeIdResolverBase).thenReturn(true);
        when(((TypeIdResolverBase) typeIdResolver).getDescForKnownTypeIds()).thenReturn("KnownType1, KnownType2");

        typeDeserializerBase._handleUnknownTypeId(deserializationContext, typeId, typeIdResolver, JavaTypeFactory.defaultInstance().constructType(String.class));
    }

    private static class TypeDeserializerBaseTestClass extends TypeDeserializerBase {
        public static final TypeDeserializerBaseTestClass INSTANCE = new TypeDeserializerBaseTestClass(JavaTypeFactory.defaultInstance().constructType(String.class), null, "type", true, String.class);

        public TypeDeserializerBaseTestClass(JavaType baseType, TypeIdResolver idRes, String typePropertyName, boolean typeIdVisible, Class<?> defaultImpl) {
            super(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl);
        }

        @Override
        public TypeDeserializer forProperty(BeanProperty prop) {
            return this;
        }

        @Override
        public JsonTypeInfo.As getTypeInclusion() {
            return JsonTypeInfo.As.WRAPPER_ARRAY;
        }

        @Override
        protected JsonDeserializer<Object> _handleUnknownTypeId(DeserializationContext ctxt, String typeId, TypeIdResolver idResolver, JavaType baseType) throws IOException {
            return null;
        }
    }
}