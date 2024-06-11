package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.assertNotNull;
import static org.mockito.internal.util.collections.Iterables.getOnlyElement;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.mockito.MockCandidateFilter;
import org.mockito.internal.configuration.MockInjector;
import org.mockito.internal.configuration.injection.InjectableField;
import org.mockito.internal.configuration.injection.MockFactory;
import org.mockito.internal.util.collections.Iterables;

public class TypeBasedCandidateFilterTest {

    @Test
    public void testFilterCandidateWithEmptyCollection() {
        MockCandidateFilter filter = new TypeBasedCandidateFilter(null);
        Collection<Object> mocks = new ArrayList<>();
        Field field = new FieldStub();
        Object fieldInstance = new Object();

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(result);
    }

    @Test
    public void testFilterCandidateWithNoTypeMatch() {
        MockCandidateFilter filter = new TypeBasedCandidateFilter(null);
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(new String("mock1"));
        Field field = new FieldStub();
        Object fieldInstance = new Object();

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(result);
    }

    @Test
    public void testFilterCandidateWithOneTypeMatch() {
        MockCandidateFilter filter = new TypeBasedCandidateFilter(null);
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(new MockitoStub1());
        Field field = new FieldStub();
        Object fieldInstance = new Object();

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(result);
        MockitoStub1 mock = (MockitoStub1) result.getMock();
        assertNotNull(mock);
    }

    @Test
    public void testFilterCandidateWithMultipleTypeMatches() {
        MockCandidateFilter filter = new TypeBasedCandidateFilter(null);
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(new MockitoStub1());
        mocks.add(new MockitoStub2());
        Field field = new FieldStub();
        Object fieldInstance = new Object();

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        MockitoStub1 mock1 = (MockitoStub1) result.getMock();
        MockitoStub2 mock2 = (MockitoStub2) filter.next.filterCandidate(Iterables.getOnlyElements(mocks), field, fieldInstance).getMock();

        assertNotNull(result);
        assertNotNull(mock1);
        assertNotNull(mock2);
    }

    @Test
    public void testFilterCandidateWithNullNext() {
        MockCandidateFilter filter = new TypeBasedCandidateFilter(null);
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(new MockitoStub1());
        Field field = new FieldStub();
        Object fieldInstance = new Object();

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(result);
    }

    @Test
    public void testFilterCandidateWithNullCollection() {
        MockCandidateFilter filter = new TypeBasedCandidateFilter(null);
        Field field = new FieldStub();
        Object fieldInstance = new Object();

        OngoingInjecter result = filter.filterCandidate(null, field, fieldInstance);

        assertNotNull(result);
    }

    @Test
    public void testFilterCandidateWithNullField() {
        MockCandidateFilter filter = new TypeBasedCandidateFilter(null);
        Collection<Object> mocks = new ArrayList<>();
        Object fieldInstance = new Object();

        OngoingInjecter result = filter.filterCandidate(mocks, null, fieldInstance);

        assertNotNull(result);
    }

    @Test
    public void testFilterCandidateWithNullFieldInstance() {
        MockCandidateFilter filter = new TypeBasedCandidateFilter(null);
        Collection<Object> mocks = new ArrayList<>();
        Field field = new FieldStub();

        OngoingInjecter result = filter.filterCandidate(mocks, field, null);

        assertNotNull(result);
    }

    private static class FieldStub implements Field {
        public Class<?> getType() {
            return MockInjector.class;
        }

        public String getName() {
            return "field";
        }

        public int getModifiers() {
            return 0;
        }

        public Field getDeclaringClass() {
            return null;
        }

        public Object get(Object obj) throws IllegalAccessException {
            return null;
        }

        public void set(Object obj, Object value) throws IllegalAccessException {
        }
    }

    private static class MockitoStub1 implements MockFactory {
        public Object createMock(Class<?> type) {
            return new Object();
        }
    }

    private static class MockitoStub2 implements MockFactory {
        public Object createMock(Class<?> type) {
            return new Object();
        }
    }
}