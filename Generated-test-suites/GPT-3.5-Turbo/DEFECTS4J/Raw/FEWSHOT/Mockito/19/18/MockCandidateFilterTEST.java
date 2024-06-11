package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import java.lang.reflect.Field;
import java.util.Collection;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class MockCandidateFilterTest {
    @InjectMocks
    private MockCandidateFilter filter;

    @Mock
    private Collection<Object> mocks;

    @Test
    public void testFilterCandidate() {
        // Test 1: fieldToBeInjected == null
        OngoingInjecter result = filter.filterCandidate(mocks, null, null);
        assertNull(result);

        // Test 2: fieldInstance == null
        Field fieldToBeInjected = mock(Field.class);
        result = filter.filterCandidate(mocks, fieldToBeInjected, null);
        assertNull(result);

        // Test 3: mocks collection is empty
        result = filter.filterCandidate(new ArrayList<>(), fieldToBeInjected, "fieldInstance");
        assertNull(result);

        // Test 4: mocks collection contains mock objects for different fields
        Collection<Object> mocksWithOtherFields = new ArrayList<>();
        mocksWithOtherFields.add(mock(Field.class));
        mocksWithOtherFields.add(mock(Field.class));
        result = filter.filterCandidate(mocksWithOtherFields, fieldToBeInjected, "fieldInstance");
        assertNull(result);

        // Test 5: mocks collection contains mock object for the same field
        mocksWithOtherFields = new ArrayList<>();
        mocksWithOtherFields.add(mock(Field.class));
        mocksWithOtherFields.add(fieldToBeInjected);
        result = filter.filterCandidate(mocksWithOtherFields, fieldToBeInjected, "fieldInstance");
        // Assert that the returned OngoingInjecter contains the correct mocks and fieldInstance
        assertEquals(mocksWithOtherFields, result.getMocks());
        assertEquals("fieldInstance", result.getFieldInstance());
    }
}