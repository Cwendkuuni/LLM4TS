package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TypeBasedCandidateFilterTest {

    @Test
    public void testFilterCandidate() {
        Class<?> mockClass1 = MockitoMockitoMockito.class;
        Class<?> mockClass2 = MockitoAnotherMockito.class;
        Class<?> targetClass = TargetClass.class;

        List<Object> mocks = new ArrayList<>();
        mocks.add(new MockitoMockitoMockito());
        mocks.add(new MockitoAnotherMockito());

        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(null);

        Field field = null; // Replace this with a real field for testing

        Object fieldInstance = new Object(); // Replace this with a real field instance for testing

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        assertNull(result);

        mocks = new ArrayList<>();
        mocks.add(new MockitoMockitoMockito());
        mocks.add(new MockitoAnotherMockito());

        field = new Field("testField") {
            @Override
            public Class<?> getType() {
                return targetClass;
            }
        };

        fieldInstance = new Object();

        result = filter.filterCandidate(mocks, field, fieldInstance);

        assertTrue(result instanceof MockitoMockitoMockito);

        mocks = new ArrayList<>();
        mocks.add(new MockitoMockitoMockito());
        mocks.add(new MockitoAnotherMockito());

        field = new Field("testField") {
            @Override
            public Class<?> getType() {
                return Object.class;
            }
        };

        fieldInstance = new Object();

        result = filter.filterCandidate(mocks, field, fieldInstance);

        assertNull(result);

        mocks = new ArrayList<>();
        mocks.add(new MockitoMockitoMockito());
        mocks.add(new MockitoAnotherMockito());

        field = new Field("testField") {
            @Override
            public Class<?> getType() {
                return targetClass;
            }
        };

        result = new MockCandidateFilter() {
            @Override
            public OngoingInjecter filterCandidate(Collection<Object> mocks, Field field, Object fieldInstance) {
                return new OngoingInjecter(new MockitoAnotherMockito());
            }
        }.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(result);
        assertTrue(result instanceof OngoingInjecter);
        assertTrue(result.getMock() instanceof MockitoAnotherMockito);
    }

    @Test
    public void testFilterCandidateWithNext() {
        Class<?> mockClass1 = MockitoMockitoMockito.class;
        Class<?> mockClass2 = MockitoAnotherMockito.class;
        Class<?> targetClass = TargetClass.class;

        List<Object> mocks = new ArrayList<>();
        mocks.add(new MockitoMockitoMockito());
        mocks.add(new MockitoAnotherMockito());

        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(new DummyCandidateFilter());

        Field field = new Field("testField") {
            @Override
            public Class<?> getType() {
                return targetClass;
            }
        };

        Object fieldInstance = new Object();

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        assertNull(result);

        mockClass1 = MockitoMockitoMockitoWithNext.class;
        mocks = new ArrayList<>();
        mocks.add(new MockitoMockitoMockitoWithNext());

        result = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(result);
        assertTrue(result instanceof OngoingInjecter);
        assertTrue(result.getMock() instanceof MockitoMockitoMockitoWithNext);
    }
}