package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.ObjectMapperTestBase;

/**
 * Test class for {@link TypeDeserializerBase}.
 */
public class TypeDeserializerBaseTest extends ObjectMapperTestBase {

    private static final String TYPE_ID = "TypeId";
    private static final String TYPE_ID_STR = "typeId";
    private static final String TYPE_ID_STR_2 = "typeId2";
    private static final String TYPE_NAME_STR = "TypeName";

    private TypeDeserializerBase _typeDeserializerBase;
    private BeanProperty _beanProperty;
    private DeserializationContext _deserializationContext;
    private JsonParser _jsonParser;
    private TypeIdResolver _typeIdResolver;

    /**
     * Sets up test environment.
     *
     * @throws IOException on error
     */
    @Before
    public void setUp() throws IOException {
        _beanProperty = mock(BeanProperty.class);
        _deserializationContext = mock(DeserializationContext.class);
        _jsonParser = createParser("[]", getObjectMapper());
        _typeIdResolver = mock(TypeIdResolver.class);
        _typeDeserializerBase = new TypeDeserializerBase(
                mock(JavaType.class), _typeIdResolver, "", false, null) {

            @Override
            protected TypeDeserializer forProperty(BeanProperty prop) {
                assertEquals(_beanProperty, prop);
                return this;
            }
        };
    }

    /**
     * Tests method {@link TypeDeserializerBase#forProperty(BeanProperty)}.
     *
     * @throws IOException on error
     */
    @Test
    public void testForProperty() throws IOException {
        TypeDeserializer typeDeserializer = _typeDeserializerBase.forProperty(_beanProperty);
        assertEquals(_typeDeserializerBase, typeDeserializer);
    }

    /**
     * Tests method {@link TypeDeserializerBase#getTypeInclusion()}.
     */
    @Test
    public void testGetTypeInclusion() {
        assertEquals(JsonTypeInfo.As.EXISTING_PROPERTY, _typeDeserializerBase.getTypeInclusion());
    }

    /**
     * Tests method {@link TypeDeserializerBase#baseTypeName()}.
     */
    @Test
    public void testBaseTypeName() {
        String baseTypeName = _typeDeserializerBase.baseTypeName();
        assertNull(baseTypeName);
    }

    /**
     * Tests method {@link TypeDeserializerBase#getPropertyName()}.
     */
    @Test
    public void testGetPropertyName() {
        String propertyName = _typeDeserializerBase.getPropertyName();
        assertEquals("", propertyName);
    }

    /**
     * Tests method {@link TypeDeserializerBase#getTypeIdResolver()}.
     */
    @Test
    public void testGetTypeIdResolver() {
        TypeIdResolver typeIdResolver = _typeDeserializerBase.getTypeIdResolver();
        assertEquals(_typeIdResolver, typeIdResolver);
    }

    /**
     * Tests method {@link TypeDeserializerBase#getDefaultImpl()}.
     */
    @Test
    public void testGetDefaultImpl() {
        Class<?> defaultImpl = _typeDeserializerBase.getDefaultImpl();
        assertNull(defaultImpl);
    }

    /**
     * Tests method {@link TypeDeserializerBase#toString()}.
     */
    @Test
    public void testToString() {
        String toString = _typeDeserializerBase.toString();
        assertNotNull(toString);
    }

    /**
     * Tests method {@link TypeDeserializerBase#_findDeserializer(DeserializationContext, String)}.
     *
     * @throws IOException on error
     */
    @Test
    public void testFindDeserializer() throws IOException {
        when(_typeIdResolver.typeFromId(_deserializationContext, TYPE_ID_STR)).thenReturn(mock(JavaType.class));
        JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);
        when(_deserializationContext.findContextualValueDeserializer(mock(JavaType.class), _beanProperty)).thenReturn(deserializer);

        _typeDeserializerBase._deserializers.put(TYPE_ID_STR, null);

        JsonDeserializer<Object> jsonDeserializer = _typeDeserializerBase._findDeserializer(_deserializationContext, TYPE_ID_STR);
        assertEquals(deserializer, jsonDeserializer);
    }

    /**
     * Tests method {@link TypeDeserializerBase#_findDefaultImplDeserializer(DeserializationContext)}.
     *
     * @throws IOException on error
     */
    @Test
    public void testFindDefaultImplDeserializer() throws IOException {
        when(_typeDeserializerBase._defaultImplDeserializer).thenReturn(null);
        JsonDeserializer<Object> jsonDeserializer = _typeDeserializerBase._findDefaultImplDeserializer(_deserializationContext);
        assertNull(jsonDeserializer);

        when(_typeDeserializerBase._defaultImplDeserializer).thenReturn(mock(JsonDeserializer.class));
        JsonDeserializer<Object> deserializer = _typeDeserializerBase._findDefaultImplDeserializer(_deserializationContext);
        assertEquals(mock(JsonDeserializer.class), deserializer);
    }

    /**
     * Tests method {@link TypeDeserializerBase#_deserializeWithNativeTypeId(JsonParser, DeserializationContext, Object)}.
     *
     * @throws IOException on error
     */
    @Test
    public void testDeserializeWithNativeTypeId() throws IOException {
        when(_typeDeserializerBase._deserializeWithNativeTypeId(_jsonParser, _deserializationContext, null))
                .thenCallRealMethod();
        _typeDeserializerBase._deserializeWithNativeTypeId(_jsonParser, _deserializationContext, null);
    }

    /**
     * Tests method {@link TypeDeserializerBase#_handleUnknownTypeId(DeserializationContext, String, TypeIdResolver, JavaType)}.
     *
     * @throws IOException on error
     */
    @Test(expected = IOException.class)
    public void testHandleUnknownTypeId() throws IOException {
        when(_typeIdResolver.typeFromId(_deserializationContext, TYPE_ID_STR)).thenReturn(mock(JavaType.class));
        when(_typeIdResolver.typeFromId(_deserializationContext, TYPE_ID_STR_2)).thenReturn(null);
        when(_typeDeserializerBase._handleUnknownTypeId(_deserializationContext, TYPE_ID_STR_2, _typeIdResolver, mock(JavaType.class)))
                .thenCallRealMethod();

        _typeDeserializerBase._handleUnknownTypeId(_deserializationContext, TYPE_ID_STR_2, _typeIdResolver, mock(JavaType.class));
    }
}