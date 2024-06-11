package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;

public class MockCandidateFilterTest implements MockCandidateFilter {

    @Override
    public OngoingInjecter filterCandidate(Collection<Object> mocks, Field fieldToBeInjected, Object fieldInstance) {
        return new OngoingMockInjecter(mocks, fieldToBeInjected, fieldInstance);
    }

    @Test
    public void testFilterCandidateNullCollection() {
        Collection<Object> mocks = null;
        Field field = new Field("testField", String.class, new Class[0]);
        Object fieldInstance = "testInstance";

        assertNull(filterCandidate(mocks, field, fieldInstance));
    }

    @Test
    public void testFilterCandidateEmptyCollection() {
        Collection<Object> mocks = new ArrayList<>();
        Field field = new Field("testField", String.class, new Class[0]);
        Object fieldInstance = "testInstance";

        assertNull(filterCandidate(mocks, field, fieldInstance));
    }

    @Test
    public void testFilterCandidateSingleMock() {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(String.class));
        Field field = new Field("testField", String.class, new Class[0]);
        Object fieldInstance = "testInstance";

        OngoingMockInjecter result = (OngoingMockInjecter) filterCandidate(mocks, field, fieldInstance);
        assertSame(mocks.get(0), result.getMock());
    }

    @Test
    public void testFilterCandidateMultipleMocks() {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(String.class));
        mocks.add(mock(Integer.class));
        Field field = new Field("testField", String.class, new Class[0]);
        Object fieldInstance = "testInstance";

        OngoingMockInjecter result = (OngoingMockInjecter) filterCandidate(mocks, field, fieldInstance);
        assertSame(mocks.get(0), result.getMock());
    }

    @Test
    public void testFilterCandidateSpyObject() {
        Collection<Object> mocks = new ArrayList<>();
        Object spy = spy(String.class);
        Field field = new Field("testField", String.class, new Class[0]);
        Object fieldInstance = "testInstance";

        OngoingMockInjecter result = (OngoingMockInjecter) filterCandidate(mocks, field, fieldInstance);
        assertSame(spy, result.getMock());
    }
}

class Field {
    String name;
    Class<?> type;
    Class<?> clazz;

    Field(String name, Class<?> type, Class<?> clazz) {
        this.name = name;
        this.type = type;
        this.clazz = clazz;
    }
}

class OngoingMockInjecter implements OngoingInjecter {
    Collection<Object> mocks;
    Field field;
    Object fieldInstance;
    Object mock;

    OngoingMockInjecter(Collection<Object> mocks, Field field, Object fieldInstance) {
        this.mocks = mocks;
        this.field = field;
        this.fieldInstance = fieldInstance;
    }

    Object getMock() {
        return mock;
    }
}