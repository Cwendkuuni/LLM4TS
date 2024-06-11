package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.assertEquals;
import static org.mockito.internal.util.MockUtil.getMockName;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.naming.SimpleName;

@RunWith(MockitoJUnitRunner.class)
public class NameBasedCandidateFilterTest {

    @Test
    public void testFilterCandidate_oneMock_noConflictingFields() {
        MockCandidateFilter next = new MockCandidateFilter() {
            @Override
            public OngoingInjecter filterCandidate(Collection<Object> mocks, Field field, Object fieldInstance) {
                throw new UnsupportedOperationException();
            }
        };
        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(next);
        Object mock = new Object() {};
        Field field = MockUtil.getField(mock.getClass(), "fieldName");
        Object fieldInstance = new Object();
        OngoingInjecter result = filter.filterCandidate(new ArrayList<>(), field, fieldInstance);
        assertEquals(mock, result.getMock());
    }

    @Test
    public void testFilterCandidate_oneMock_conflictingFields() {
        MockCandidateFilter next = new MockCandidateFilter() {
            @Override
            public OngoingInjecter filterCandidate(Collection<Object> mocks, Field field, Object fieldInstance) {
                throw new UnsupportedOperationException();
            }
        };
        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(next);
        Object mock1 = new Object() {};
        Object mock2 = new Object() {};
        Field field1 = MockUtil.getField(mock1.getClass(), "fieldName");
        Object fieldInstance1 = new Object();
        Field field2 = MockUtil.getField(mock2.getClass(), "fieldName");
        Object fieldInstance2 = new Object();
        List<Object> mocks = new ArrayList<>();
        mocks.add(mock1);
        mocks.add(mock2);
        OngoingInjecter result = filter.filterCandidate(mocks, field1, fieldInstance1);
        assertEquals(mock1, result.getMock());
    }

    @Test
    public void testFilterCandidate_multipleMocks_noConflictingFields() {
        MockCandidateFilter next = new MockCandidateFilter() {
            @Override
            public OngoingInjecter filterCandidate(Collection<Object> mocks, Field field, Object fieldInstance) {
                throw new UnsupportedOperationException();
            }
        };
        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(next);
        Object mock1 = new Object() {};
        Object mock2 = new Object() {};
        Field field = MockUtil.getField(mock1.getClass(), "fieldName");
        Object fieldInstance = new Object();
        List<Object> mocks = new ArrayList<>();
        mocks.add(mock1);
        mocks.add(mock2);
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        assertEquals(mock1, result.getMock());
    }

    @Test
    public void testFilterCandidate_multipleMocks_conflictingFields() {
        MockCandidateFilter next = new MockCandidateFilter() {
            @Override
            public OngoingInjecter filterCandidate(Collection<Object> mocks, Field field, Object fieldInstance) {
                throw new UnsupportedOperationException();
            }
        };
        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(next);
        Object mock1 = new Object() {};
        Object mock2 = new Object() {};
        Field field1 = MockUtil.getField(mock1.getClass(), "fieldName");
        Object fieldInstance1 = new Object();
        Field field2 = MockUtil.getField(mock2.getClass(), "fieldName");
        Object fieldInstance2 = new Object();
        List<Object> mocks = new ArrayList<>();
        mocks.add(mock1);
        mocks.add(mock2);
        SimpleName name1 = new SimpleName("fieldName");
        SimpleName name2 = new SimpleName("anotherFieldName");
        assertEquals(mock1, filter.filterCandidate(mocks, field1, fieldInstance1).getMock());
        assertEquals(mock2, filter.filterCandidate(mocks, field2, fieldInstance2).getMock());
    }
}