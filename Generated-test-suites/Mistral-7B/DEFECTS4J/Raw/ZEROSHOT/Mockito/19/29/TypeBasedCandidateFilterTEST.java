package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.util.collections.Collections3;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.internal.util.collections.ArrayUtils.newArrayList;

public class TypeBasedCandidateFilterTest {

    private TypeBasedCandidateFilter filter;
    private MockCandidateFilter nextMock;

    @Before
    public void setUp() {
        nextMock = Mockito.mock(MockCandidateFilter.class);
        filter = new TypeBasedCandidateFilter(nextMock);
    }

    @Test
    public void testFilterCandidate_whenFieldTypeIsAssignableFromMockedType_thenReturnFilteredMock() {
        // given
        Class<String> fieldType = String.class;
        Class<Integer> mockType = Integer.class;
        Collection<Object> mocks = newArrayList(Mockito.mock(String.class), Mockito.mock(Integer.class), Mockito.mock(List.class));
        Object fieldInstance = "test";
        Field field = Mockito.spy(new FieldStub());
        field.setType(fieldType);

        // when
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // then
        assertNotNull(result);
        assertEquals(mockType, result.getType());
        assertEquals(Mockito.mock(String.class), result.getMock());
    }

    @Test
    public void testFilterCandidate_whenFieldTypeIsNotAssignableFromMockedType_thenCallNextFilter() {
        // given
        Class<String> fieldType = String.class;
        Class<Integer> mockType = Integer.class;
        Collection<Object> mocks = newArrayList(Mockito.mock(String.class), Mockito.mock(Integer.class), Mockito.mock(List.class));
        Object fieldInstance = "test";
        Field field = Mockito.spy(new FieldStub());
        field.setType(fieldType);

        Mockito.when(nextMock.filterCandidate(Mockito.any(Collection.class), Mockito.any(Field.class), Mockito.any(Object.class)))
                .thenReturn(Mockito.mock(OngoingInjecter.class));

        // when
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // then
        Mockito.verify(nextMock).filterCandidate(Collections3.arrayToList(mocks), field, fieldInstance);
        assertNull(result);
    }

    @Test(expected = MockitoException.class)
    public void testFilterCandidate_whenNoMockMatches_thenThrowException() {
        // given
        Class<String> fieldType = String.class;
        Class<Integer> mockType = Integer.class;
        Collection<Object> mocks = newArrayList(Mockito.mock(List.class));
        Object fieldInstance = "test";
        Field field = Mockito.spy(new FieldStub());
        field.setType(fieldType);

        // when
        filter.filterCandidate(mocks, field, fieldInstance);

        // then
        // exception is thrown because of next filter not being able to find a candidate
    }
}

class FieldStub {
}