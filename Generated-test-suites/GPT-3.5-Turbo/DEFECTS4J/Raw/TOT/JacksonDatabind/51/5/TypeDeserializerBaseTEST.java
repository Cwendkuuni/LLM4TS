import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.type.SimpleType;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class TypeDeserializerBaseTest {

    @Mock
    private TypeIdResolver typeIdResolver;
    @Mock
    private DeserializationContext deserializationContext;
    @Mock
    private JsonParser jsonParser;
    @Mock
    private BeanProperty beanProperty;
    @Mock
    private JsonDeserializer<Object> jsonDeserializer;

    private TypeDeserializerBase typeDeserializerBase;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        typeDeserializerBase = new TypeDeserializerBase(SimpleType.construct(Object.class), typeIdResolver,
                "typePropertyName", true, SimpleType.construct(Object.class));

        typeDeserializerBase._deserializers.put("test", jsonDeserializer);
        typeDeserializerBase._defaultImplDeserializer = jsonDeserializer;
    }

    @Test
    public void testBaseTypeName_ShouldReturnCorrectName() {
        String expected = Object.class.getName();
        String actual = typeDeserializerBase.baseTypeName();

        assertEquals(expected, actual);
    }

    @Test
    public void testGetPropertyName_ShouldReturnCorrectName() {
        String expected = "typePropertyName";
        String actual = typeDeserializerBase.getPropertyName();

        assertEquals(expected, actual);
    }

    @Test
    public void testGetTypeIdResolver_ShouldReturnCorrectResolver() {
        TypeIdResolver expected = typeIdResolver;
        TypeIdResolver actual = typeDeserializerBase.getTypeIdResolver();

        assertEquals(expected, actual);
    }

    @Test
    public void testGetTypeInclusion_ShouldReturnCorrectInclusion() {
        JsonTypeInfo.As expected = JsonTypeInfo.As.WRAPPER_OBJECT;
        JsonTypeInfo.As actual = typeDeserializerBase.getTypeInclusion();

        assertEquals(expected, actual);
    }

    @Test
    public void testGetDefaultImpl_ShouldReturnCorrectDefaultImpl() {
        Class<?> expected = Object.class;
        Class<?> actual = typeDeserializerBase.getDefaultImpl();

        assertEquals(expected, actual);
    }

    @Test
    public void testToString_ShouldReturnCorrectString() {
        String expected = "[" + typeDeserializerBase.getClass().getName() + "; base-type:"
                + SimpleType.construct(Object.class) + "; id-resolver: " + typeIdResolver + "]";
        String actual = typeDeserializerBase.toString();

        assertEquals(expected, actual);
    }

    @Test
    public void testForProperty_ShouldReturnInstanceOfSameClass() {
        BeanProperty expected = beanProperty;
        BeanProperty actual = typeDeserializerBase.forProperty(beanProperty);

        assertNotNull(actual);
        assertEquals(expected.getClass(), actual.getClass());
    }

    @Test
    public void testFindDefaultImplDeserializer_WithNonNullDefaultImpl_ShouldReturnDefaultImplDeserializer()
            throws IOException {
        JsonDeserializer<Object> expected = jsonDeserializer;
        JsonDeserializer<Object> actual = typeDeserializerBase._findDefaultImplDeserializer(deserializationContext);

        assertEquals(expected, actual);
    }

    @Test
    public void testFindDefaultImplDeserializer_WithNullDefaultImpl_ShouldReturnNullifyingDeserializer()
            throws IOException {
        typeDeserializerBase._defaultImpl = null;

        JsonDeserializer<Object> actual = typeDeserializerBase._findDefaultImplDeserializer(deserializationContext);

        assertEquals(NullifyingDeserializer.instance, actual);
    }

    @Test
    public void testFindDefaultImplDeserializer_WithBogusClassDefaultImpl_ShouldReturnNullifyingDeserializer()
            throws IOException {
        typeDeserializerBase._defaultImpl = SimpleType.construct(ClassUtil.class);

        JsonDeserializer<Object> actual = typeDeserializerBase._findDefaultImplDeserializer(deserializationContext);

        assertEquals(NullifyingDeserializer.instance, actual);
    }

    @Test
    public void testDeserializeWithNativeTypeId_WithNullTypeId_ShouldReturnDeserializedObject() throws IOException {
        when(typeDeserializerBase._findDefaultImplDeserializer(deserializationContext)).thenReturn(jsonDeserializer);

        Object actual = typeDeserializerBase._deserializeWithNativeTypeId(jsonParser, deserializationContext, null);

        verify(jsonDeserializer, times(1)).deserialize(jsonParser, deserializationContext);
        assertNotNull(actual);
    }

    @Test
    public void testDeserializeWithNativeTypeId_WithNonNullTypeId_ShouldReturnDeserializedObject() throws IOException {
        String typeId = "test";
        when(typeDeserializerBase._findDeserializer(deserializationContext, typeId)).thenReturn(jsonDeserializer);

        Object actual = typeDeserializerBase._deserializeWithNativeTypeId(jsonParser, deserializationContext, typeId);

        verify(jsonDeserializer, times(1)).deserialize(jsonParser, deserializationContext);
        assertNotNull(actual);
    }

    @Test(expected = IOException.class)
    public void testDeserializeWithNativeTypeId_WithNonexistentTypeIdAndEnabledFailOnInvalidSubtype_ShouldThrowIOException()
            throws IOException {
        String typeId = "nonexistent";

        typeDeserializerBase._deserializeWithNativeTypeId(jsonParser, deserializationContext, typeId);
    }

    @Test
    public void testDeserializeWithNativeTypeId_WithNonexistentTypeIdAndDisabledFailOnInvalidSubtype_ShouldReturnNull()
            throws IOException {
        String typeId = "nonexistent";
        when(deserializationContext.isEnabled(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE)).thenReturn(false);
        when(typeDeserializerBase._findDefaultImplDeserializer(deserializationContext)).thenReturn(jsonDeserializer);

        Object actual = typeDeserializerBase._deserializeWithNativeTypeId(jsonParser, deserializationContext, typeId);

        verify(jsonDeserializer, never()).deserialize(jsonParser, deserializationContext);
        assertNull(actual);
    }

    @Test
    public void testDeserializeWithNativeTypeId_WithInvalidClassDefaultImpl_ShouldReturnNullifyingDeserializer()
            throws IOException {
        Class<?> defaultImplClass = BogusClass.class;
        typeDeserializerBase._defaultImpl = SimpleType.construct(defaultImplClass);

        Object actual = typeDeserializerBase._deserializeWithNativeTypeId(jsonParser, deserializationContext, null);

        assertEquals(NullifyingDeserializer.instance, actual);
    }

    @Test
    public void testHandleUnknownTypeId_WithUnknownTypeId_ShouldCallHandleUnknownTypeIdOnDeserializationContext()
            throws IOException {
        String typeId = "unknown";
        JavaType baseType = SimpleType.construct(Object.class);

        typeDeserializerBase._handleUnknownTypeId(deserializationContext, typeId, typeIdResolver, baseType);

        verify(deserializationContext, times(1)).handleUnknownTypeId(baseType, typeId, typeIdResolver,
                "known type ids are not statically known");
    }

    static class BogusClass {
        // Bogus class for testing
    }
}