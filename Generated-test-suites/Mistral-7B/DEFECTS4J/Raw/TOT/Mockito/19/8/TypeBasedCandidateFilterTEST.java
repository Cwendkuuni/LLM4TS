package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class TypeBasedCandidateFilterTest {

    private TypeBasedCandidateFilter filter;
    private MockCandidateFilter nextMock;
    private Object fieldInstance;
    private Field field;
    private Collection<Object> mocks;

    @Before
    public void setUp() {
        filter = new TypeBasedCandidateFilter(nextMock = Mockito.mock(MockCandidateFilter.class));
        mocks = new ArrayList<>();
        fieldInstance = Mockito.mock(Object.class);
        field = Mockito.spy(Field.class);
        MockitoAnnotations.initMocks(this);
    }

    // Test cases for method filterCandidate(Collection<Object> mocks, Field field, Object fieldInstance)

    @Test
    public void testFilterCandidate_whenFieldTypeIsAssignableFromAllMocks_thenReturnFilteredMock() {
        // Test case 1
        Object mock1 = mock(Class1.class);
        Object mock2 = mock(Class1.class);
        mocks.add(mock1);
        mocks.add(mock2);

        // When
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        assertEquals(mock1, result.getMock());
    }

    @Test
    public void testFilterCandidate_whenFieldTypeIsNotAssignableFromAnyMocks_thenReturnNull() {
        // Test case 2
        Object mock1 = mock(Class1.class);
        Object mock2 = mock(Class2.class);
        Object mock3 = mock(Class3.class);
        mocks.add(mock1);
        mocks.add(mock2);
        mocks.add(mock3);

        // When
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        assertEquals(null, result);
    }

    @Test
    public void testFilterCandidate_whenFieldTypeIsAssignableFromSomeMocks_thenReturnFilteredMock() {
        // Test case 3
        Object mock1 = mock(Class1.class);
        Object mock2 = mock(Class2.class);
        Object mock3 = mock(Class3.class);
        mocks.add(mock1);
        mocks.add(mock2);
        mocks.add(mock3);

        // When
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        assertEquals(mock2, result.getMock());
    }
}