package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.MethodInterceptor;
import org.mockito.invocation.ProceedWithInterceptor;
import org.mockito.internal.configuration.MockCandidateFilter;
import org.mockito.internal.configuration.injection.InjectableField;
import org.mockito.internal.configuration.injection.MockCandidate;
import org.mockito.internal.util.collections.CollectionUtils;
import org.mockito.internal.util.reflection.FieldSetter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TypeBasedCandidateFilterTest {

    private TypeBasedCandidateFilter filter;
    private MockCandidateFilter nextFilter;

    @Before
    public void setup() {
        nextFilter = mock(MockCandidateFilter.class);
        filter = new TypeBasedCandidateFilter(nextFilter);
    }

    @Test
    public void testFilterCandidate_whenFieldTypeIsAssignableFromMockType_thenReturnNextFilterResult() {
        // given
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(Object.class));
        mocks.add(mock(SomeInterface.class));
        Object fieldInstance = mock(SomeClass.class);
        Field field = new Field("fieldName", SomeClass.class, SomeClass.class);

        Object mockToReturn = mock(SomeInterface.class);
        when(nextFilter.filterCandidate(CollectionUtils.list(mockToReturn), field, fieldInstance)).thenReturn(mockToReturn);

        // when
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // then
        assertEquals(mockToReturn, result);
        verify(nextFilter).filterCandidate(CollectionUtils.list(mockToReturn), field, fieldInstance);
    }

    @Test
    public void testFilterCandidate_whenFieldTypeIsNotAssignableFromMockType_thenDoNothing() {
        // given
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(Object.class));
        mocks.add(mock(AnotherClass.class));
        Object fieldInstance = mock(SomeClass.class);
        Field field = new Field("fieldName", SomeClass.class, SomeClass.class);

        // when
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // then
        assertEquals(null, result);
    }

    @Test
    public void testFilterCandidate_whenNextFilterDoNothing_thenReturnNull() {
        // given
        Collection<Object> mocks = new ArrayList<>();
        Object fieldInstance = mock(SomeClass.class);
        Field field = new Field("fieldName", SomeClass.class, SomeClass.class);

        when(nextFilter.filterCandidate(CollectionUtils.emptyList(), field, fieldInstance)).thenReturn(null);

        // when
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // then
        assertEquals(null, result);
    }

    @Test
    public void testFilterCandidate_whenNextFilterThrowException_thenPropagateException() {
        // given
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(Object.class));
        Object fieldInstance = mock(SomeClass.class);
        Field field = new Field("fieldName", SomeClass.class, SomeClass.class);

        when(nextFilter.filterCandidate(CollectionUtils.emptyList(), field, fieldInstance))
                .thenThrow(new RuntimeException("Next filter failed"));

        // when & then
        expectException(RuntimeException.class);
        expectMessage("Next filter failed");

        filter.filterCandidate(mocks, field, fieldInstance);
    }

    @Test
    public void testFilterCandidate_whenFilterCandidateReturnNull_thenReturnNull() {
        // given
        Collection<Object> mocks = new ArrayList<>();
        Object fieldInstance = mock(SomeClass.class);
        Field field = new Field("fieldName", SomeClass.class, SomeClass.class);

        // when
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // then
        assertEquals(null, result);
    }

    @Test
    public void testFilterCandidate_whenFilterCandidateReturnSameMock_thenReturnSameMock() {
        // given
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(SomeInterface.class));
        Object fieldInstance = mock(SomeClass.class);
        Field field = new Field("fieldName", SomeClass.class, SomeClass.class);

        // when
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // then
        assertEquals(mocks.get(0), result);
    }

    @Test
    public void testFilterCandidate_whenFilterCandidateReturnDifferentMock_thenReturnDifferentMock() {
        // given
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(SomeInterface.class));
        mocks.add(mock(AnotherInterface.class));
        Object fieldInstance = mock(SomeClass.class);
        Field field = new Field("fieldName", SomeClass.class, SomeClass.class);

        OngoingInjecter mockToReturn = mock(AnotherInterface.class);

        // when
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // then
        assertEquals(mockToReturn, result);
        verify(nextFilter, never()).filterCandidate(CollectionUtils.list(mockToReturn), field, fieldInstance);
    }

    @Test
    public void testFilterCandidate_whenNextFilterReturnNull_thenReturnSameMock() {
        // given
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(SomeInterface.class));
        Object fieldInstance = mock(SomeClass.class);
        Field field = new Field("fieldName", SomeClass.class, SomeClass.class);

        when(nextFilter.filterCandidate(CollectionUtils.emptyList(), field, fieldInstance)).thenReturn(null);

        // when
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // then
        assertEquals(mocks.get(0), result);
    }

    private static class SomeClass {
        @SuppressWarnings("unused")
        private static final Field field;

        static {
            try {
                field = new Field("fieldName", SomeClass.class, SomeClass.class);
            } catch (NoSuchFieldException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static class SomeInterface {}

    private static class AnotherClass {}

    private static class AnotherInterface {}

    private static final class Field {
        private final Class type;
        private final String name;

        Field(String name, Class type, Class clazz) {
            this.name = name;
            this.type = type;
        }
    }

    private static class InjectableField {
        private final Class fieldType;
        private final Object fieldValue;

        InjectableField(Class fieldType, Object fieldValue) {
            this.fieldType = fieldType;
            this.fieldValue = fieldValue;
        }

        public Class getFieldType() {
            return fieldType;
        }

        public Object getFieldValue() {
            return fieldValue;
        }
    }

    private static class OngoingInjecter {
        private final Object target;
        private final Field field;
        private final ProceedWithInterceptor proceed;

        OngoingInjecter(Object target, Field field, ProceedWithInterceptor proceed) {
            this.target = target;
            this.field = field;
            this.proceed = proceed;
        }

        public Object getTarget() {
            return target;
        }

        public Field getField() {
            return field;
        }

        public MethodInterceptor getProceed() {
            return proceed;
        }
    }
}