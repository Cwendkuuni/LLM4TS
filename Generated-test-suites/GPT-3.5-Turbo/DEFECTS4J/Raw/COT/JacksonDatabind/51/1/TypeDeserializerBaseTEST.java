
package com.fasterxml.jackson.databind.jsontype.impl;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
public class TypeDeserializerBaseTest {

    private TypeDeserializerBase typeDeserializerBase;
    private JsonDeserializer<Object> deserializer;
    private DeserializationContext deserializationContext;
    private BeanProperty beanProperty;

    @Before
    public void setUp() throws Exception {
        TypeIdResolver typeIdResolver = mock(TypeIdResolver.class);
        JavaType baseType = mock(JavaType.class);
        beanProperty = mock(BeanProperty.class);
        deserializationContext = mock(DeserializationContext.class);
        deserializer = mock(JsonDeserializer.class);
        typeDeserializerBase = new TypeDeserializerBaseMock(baseType, typeIdResolver,
            "typePropertyName", true, baseType);
    }

    @Test
    public void forProperty() throws Exception {
        typeDeserializerBase.forProperty(beanProperty);
        verify(beanProperty).getType();
    }

    @Test
    public void getTypeInclusion() throws Exception {
        typeDeserializerBase.getTypeInclusion();
    }

    @Test
    public void getPropertyName() throws Exception {
        typeDeserializerBase.getPropertyName();
    }

    @Test
    public void getTypeIdResolver() throws Exception {
        typeDeserializerBase.getTypeIdResolver();
    }

    @Test
    public void getDefaultImpl() throws Exception {
        typeDeserializerBase.getDefaultImpl();
    }

    @Test
    public void testToString() throws Exception {
        typeDeserializerBase.toString();
    }

    @Test
    public void _deserializeWithNativeTypeId() throws Exception {
        mockStatic(ClassUtil.class);
        given(ClassUtil.isBogusJavaType(new TypeDeserializerBaseMock())).willReturn(true);
        typeDeserializerBase._deserializeWithNativeTypeId(mock(JsonParser.class), deserializationContext);
        verify(deserializationContext).isEnabled(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE);
    }

    @Test
    public void _deserializeWithNativeTypeId1() throws Exception {
        typeDeserializerBase._deserializeWithNativeTypeId(mock(JsonParser.class), deserializationContext, "typeId");
    }

    @Test
    public void _findDefaultImplDeserializer() throws Exception {
        typeDeserializerBase._findDefaultImplDeserializer(deserializationContext);
        verify(deserializationContext).findContextualValueDeserializer(typeDeserializerBase._defaultImpl, beanProperty);
    }

    @Test
    public void _handleUnknownTypeId() throws Exception {
        typeDeserializerBase._handleUnknownTypeId(deserializationContext, "typeId",
                typeDeserializerBase._idResolver, typeDeserializerBase._baseType);
        verify(deserializationContext).handleUnknownTypeId(typeDeserializerBase._baseType, "typeId",
                typeDeserializerBase._idResolver, null);
    }

    private final class TypeDeserializerBaseMock extends TypeDeserializerBase {

        private TypeDeserializerBaseMock() {
            super(null, null, null, false, null);
        }

        private TypeDeserializerBaseMock(JavaType baseType, TypeIdResolver idRes,
                String typePropertyName, boolean typeIdVisible, JavaType defaultImpl) {
            super(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl);
        }

        @Override
        public TypeDeserializer forProperty(BeanProperty prop) {
            return null;
        }

        @Override
        public JsonTypeInfo.As getTypeInclusion() {
            return null;
        }

        private JsonDeserializer<Object> _findDeserializer(DeserializationContext ctxt,
                String typeId) throws IOException {
            return null;
        }

    }
}
