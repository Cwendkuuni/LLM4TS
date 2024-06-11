package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TypeBasedCandidateFilterTest {

    private TypeBasedCandidateFilter filter;
    private MockCandidateFilter next;

    @Before
    public void setUp() {
        next = new MockCandidateFilterImpl();
        filter = new TypeBasedCandidateFilter(next);
    }

    @Test
    public void testFilterCandidate() throws Exception {
        // Test mocks collection is empty
        Collection<Object> mocks = new ArrayList<Object>();
        Field field = getClass().getDeclaredField("mocks");
        Object fieldInstance = new Object();
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        assertNotNull(result);
        // MockCandidateFilter is called with an empty list
        assertEquals(mocks, result.getMocks());
        assertEquals(field, result.getField());
        assertEquals(fieldInstance, result.getFieldInstance());

        // Test with one match mock
        MockCandidateFilterImpl nextMock = new MockCandidateFilterImpl();
        Collection<Object> mocksOneMatch = new ArrayList<Object>();
        MockOne mockOne = new MockOne();
        mocksOneMatch.add(mockOne);
        nextMock.setExpectedMocks(mocksOneMatch);
        filter.setNext(nextMock);
        result = filter.filterCandidate(mocksOneMatch, field, fieldInstance);
        assertEquals(mocksOneMatch, result.getMocks());
        assertEquals(field, result.getField());
        assertEquals(fieldInstance, result.getFieldInstance());
        assertNotNull(result.getMocks().contains(mockOne));

        // Test with multiple match mocks
        MockCandidateFilterImpl nextMockMultiple = new MockCandidateFilterImpl();
        Collection<Object> mocksMultipleMatch = new ArrayList<Object>();
        MockOne mockOne1 = new MockOne();
        MockOne mockOne2 = new MockOne();
        mocksMultipleMatch.add(mockOne1);
        mocksMultipleMatch.add(mockOne2);
        nextMockMultiple.setExpectedMocks(mocksMultipleMatch);
        filter.setNext(nextMockMultiple);
        result = filter.filterCandidate(mocksMultipleMatch, field, fieldInstance);
        assertEquals(mocksMultipleMatch, result.getMocks());
        assertEquals(field, result.getField());
        assertEquals(fieldInstance, result.getFieldInstance());
        assertTrue(result.getMocks().contains(mockOne1));
        assertTrue(result.getMocks().contains(mockOne2));

        // Test with no match mocks
        MockCandidateFilterImpl nextMockNoMatch = new MockCandidateFilterImpl();
        Collection<Object> mocksNoMatch = new ArrayList<Object>();
        MockTwo mockTwo = new MockTwo();
        mocksNoMatch.add(mockTwo);
        nextMockNoMatch.setExpectedMocks(new ArrayList<Object>());
        filter.setNext(nextMockNoMatch);
        result = filter.filterCandidate(mocksNoMatch, field, fieldInstance);
        assertEquals(0, result.getMocks().size());
        assertEquals(field, result.getField());
        assertEquals(fieldInstance, result.getFieldInstance());
    }
}

class MockCandidateFilterImpl implements MockCandidateFilter {
    private Collection<Object> expectedMocks;

    public void setExpectedMocks(Collection<Object> mocks) {
        this.expectedMocks = mocks;
    }

    @Override
    public OngoingInjecter filterCandidate(Collection<Object> mocks, Field field, Object fieldInstance) {
        assertEquals(expectedMocks, mocks);
        OngoingInjecter ongoingInjecter = new OngoingInjecter();
        ongoingInjecter.setMocks(mocks);
        ongoingInjecter.setField(field);
        ongoingInjecter.setFieldInstance(fieldInstance);
        return ongoingInjecter;
    }
}

class OngoingInjecter {
    private Collection<Object> mocks;
    private Field field;
    private Object fieldInstance;

    public Collection<Object> getMocks() {
        return mocks;
    }

    public void setMocks(Collection<Object> mocks) {
        this.mocks = mocks;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public Object getFieldInstance() {
        return fieldInstance;
    }

    public void setFieldInstance(Object fieldInstance) {
        this.fieldInstance = fieldInstance;
    }
}

class MockOne {}
class MockTwo {}