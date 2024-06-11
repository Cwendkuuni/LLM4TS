package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswer() throws Exception {
        Class<?> clazz = MockitoTestUtils.getMockitoTestClass();
        Object mock = mock(clazz, new ReturnsDeepStubs());

        Type returnType = mock.getClass().getGenericInterfaces()[0];
        assertNotNull(returnType);
        assertTrue(returnType instanceof ParameterizedType);

        ParameterizedType parameterizedType = (ParameterizedType) returnType;
        Type[] types = parameterizedType.getActualTypeArguments();
        assertEquals(1, types.length);

        Type type = types[0];
        assertEquals(Map.class, type);

        Map<Object, Collection<Number>> map = (Map<Object, Collection<Number>>) mock;
        Collection<Number> collection = map.entrySet().iterator().next().getValue();
        Number number = collection.iterator().next();

        assertSame(mock, getMock(mock, getGenericMetadataSupport(clazz, Map.class, Map.Entry.class, Number[].class)));
        assertSame(number, getMock(mock, getGenericMetadataSupport(clazz, Map.class, Map.Entry.class, Number[].class, Number.class)));
    }

    @SuppressWarnings("unchecked")
    private Object getMock(Object mock, GenericMetadataSupport returnTypeGenericMetadata) throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = (ReturnsDeepStubs) ((Answer<Object>) mock.answer(stub(InvocationOnMock.class))).getInstance();
        return returnsDeepStubs.getMock(mock, returnTypeGenericMetadata);
    }

    private GenericMetadataSupport getGenericMetadataSupport(Class<?> clazz, Class<?>... types) throws Exception {
        return getGenericMetadataSupport(clazz, types[0], types);
    }

    private GenericMetadataSupport getGenericMetadataSupport(Class<?> clazz, Class<?> rawType, Class<?>... types) throws Exception {
        return getGenericMetadataSupport(clazz, rawType, new Type[0], types);
    }

    private GenericMetadataSupport getGenericMetadataSupport(Class<?> clazz, Class<?> rawType, Type[] newTypes, Class<?>... types) throws Exception {
        CreationSettings creationSettings = mock(CreationSettings.class);
        when(creationSettings.getTypeToMock()).thenReturn(clazz);
        when(creationSettings.getRawExtraInterfaces()).thenReturn(types);

        GenericMetadataSupport genericMetadataSupport = spy(new GenericMetadataSupportAdapter(rawType, newTypes));
        doReturn(creationSettings).when(genericMetadataSupport).getMockSettings();

        return genericMetadataSupport;
    }

    @Test
    public void testReturnsEmptyValuesForNonMockableTypes() throws Exception {
        Class<?> clazz = MockitoTestUtils.getMockitoTestClass();
        Object mock = mock(clazz, new ReturnsEmptyValues());

        Type returnType = mock.getClass().getGenericInterfaces()[0];
        assertNotNull(returnType);
        assertTrue(returnType instanceof ParameterizedType);

        ParameterizedType parameterizedType = (ParameterizedType) returnType;
        Type[] types = parameterizedType.getActualTypeArguments();
        assertEquals(1, types.length);

        Type type = types[0];
        assertFalse(MockitoCore.isTypeMockable(type));

        Object value = mock.answer(any(InvocationOnMock.class));
        assertEquals(Collections.emptyList(), value);
    }

    private static class GenericMetadataSupportAdapter extends GenericMetadataSupport {
        private final Class<?> rawType;
        private final Type[] newTypes;

        public GenericMetadataSupportAdapter(Class<?> rawType, Type[] newTypes) {
            this.rawType = rawType;
            this.newTypes = newTypes;
        }

        @Override
        public Type[] getRawExtraInterfaces() {
            return newTypes;
        }

        @Override
        public Class<?> getRawType() {
            return rawType;
        }
    }
}