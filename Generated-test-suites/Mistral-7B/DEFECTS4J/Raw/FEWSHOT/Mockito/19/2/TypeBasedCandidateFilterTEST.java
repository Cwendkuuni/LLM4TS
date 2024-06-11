package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.assertEquals;
import static org.mockito.internal.util.collections.CollectionUtils.newArrayList;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.mocktype.MockType;
import org.mockito.internal.configuration.mocktype.StubbingType;
import org.mockito.internal.util.collections.CollectionUtils;

public class TypeBasedCandidateFilterTest {

    private MockCandidateFilter next;
    private TypeBasedCandidateFilter filter;
    private Collection<Object> mocks;
    private Field field;
    private Object fieldInstance;

    @Before
    public void setup() {
        next = mock(MockCandidateFilter.class);
        filter = new TypeBasedCandidateFilter(next);

        mocks = newArrayList(new MockType(StubbingType.LENIENT), new MockType(StubbingType.STRICT));
        field = mock(Field.class);
        fieldInstance = mock(Object.class);
    }

    @Test
    public void testFilterCandidate_EmptyMocks_ReturnsNull() {
        assertNull(filter.filterCandidate(CollectionUtils.emptyList(), field, fieldInstance));
    }

    @Test
    public void testFilterCandidate_FieldTypeNotAssignableToAnyMocks_ReturnsNextFilter() {
        MockType mockType = new MockType(StubbingType.LENIENT, Object.class);
        mocks = CollectionUtils.newList(mockType);

        Object result = filter.filterCandidate(mocks, field, fieldInstance);
        verify(next).filterCandidate(mocks, field, fieldInstance);
        assertEquals(result, result); // to suppress warning
    }

    @Test
    public void testFilterCandidate_FieldTypeAssignableToOneMock_ReturnsThatMock() {
        MockType mockType = new MockType(StubbingType.LENIENT, Field.class);
        mocks = newArrayList(mockType);

        Object result = filter.filterCandidate(mocks, field, fieldInstance);
        assertEquals(mockType, result);
    }

    @Test
    public void testFilterCandidate_FieldTypeAssignableToMultipleMocks_ReturnsNextFilterWithFilteredList() {
        MockType lenientMockType = new MockType(StubbingType.LENIENT, Field.class);
        MockType strictMockType = new MockType(StubbingType.STRICT, Field.class);
        mocks = newArrayList(lenientMockType, strictMockType);

        Object result = filter.filterCandidate(mocks, field, fieldInstance);
        verify(next).filterCandidate(mocks, field, fieldInstance);

        MockCandidateFilter nextFilter = verify(next).filterCandidate(CollectionUtils.newList(strictMockType), field, fieldInstance);
        assertEquals(nextFilter, result);
    }

    @Test
    public void testFilterCandidate_FieldTypeAssignableToAllMocks_ReturnsFirstMock() {
        MockType lenientMockType = new MockType(StubbingType.LENIENT, Field.class);
        MockType strictMockType = new MockType(StubbingType.STRICT, Field.class);
        mocks = newArrayList(lenientMockType, strictMockType);

        MockitoCoreTesting.setFieldValue(filter, "next", new Object() {
            @Override
            public Object filterCandidate(Collection<Object> mocks, Field field, Object fieldInstance) {
                throw new UnsupportedOperationException();
            }
        });

        Object result = filter.filterCandidate(mocks, field, fieldInstance);
        assertEquals(lenientMockType, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFilterCandidate_NullNextFilter_ThrowsIllegalArgumentException() {
        filter = new TypeBasedCandidateFilter(null);
        filter.filterCandidate(mocks, field, fieldInstance);
    }
}