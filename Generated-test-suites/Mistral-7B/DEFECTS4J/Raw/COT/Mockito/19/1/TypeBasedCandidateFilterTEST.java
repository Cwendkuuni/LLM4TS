import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.utils.collections.Arrays;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.internal.utils.reflection.Whitebox.*;

public class TypeBasedCandidateFilterTest {

    private MockCandidateFilter nextMock;
    private TypeBasedCandidateFilter filterUnderTest;

    @Before
    public void setup() {
        nextMock = mock(MockCandidateFilter.class);
        filterUnderTest = new TypeBasedCandidateFilter(nextMock);
    }

    @Test
    public void testFilterCandidate_WhenMocksAreTypeMatches_ThenNextFilterIsCalledWithTypeMatches() {
        // Given
        Collection<Object> mocks = Arrays.asList(new MockType1(), new MockType2());
        Field field = getField("field");
        Object fieldInstance = new Object();

        // When
        OngoingInjecter result = filterUnderTest.filterCandidate(mocks, field, fieldInstance);

        // Then
        verify(nextMock).filterCandidate(argThat(c -> c.containsAll(mocks)), field, fieldInstance);
    }

    @Test
    public void testFilterCandidate_WhenMocksAreNotTypeMatches_ThenNextFilterIsNotCalled() {
        // Given
        Collection<Object> mocks = Arrays.asList(new MockType1(), new MockType3());
        Field field = getField("field");
        Object fieldInstance = new Object();

        // When
        OngoingInjecter result = filterUnderTest.filterCandidate(mocks, field, fieldInstance);

        // Then
        verifyNoMoreInteractions(nextMock);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFilterCandidate_WhenFieldIsNull_ThenThrowIllegalArgumentException() {
        // Given
        Collection<Object> mocks = Arrays.asList(new MockType1(), new MockType2());
        Object fieldInstance = new Object();

        // When & Then
        filterUnderTest.filterCandidate(mocks, null, fieldInstance);
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidate_WhenMocksIsNull_ThenThrowNullPointerException() {
        // Given
        Field field = getField("field");
        Object fieldInstance = new Object();

        // When & Then
        filterUnderTest.filterCandidate(null, field, fieldInstance);
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidate_WhenFieldInstanceIsNull_ThenThrowNullPointerException() {
        // Given
        Collection<Object> mocks = Arrays.asList(new MockType1(), new MockType2());
        Field field = getField("field");

        // When & Then
        filterUnderTest.filterCandidate(mocks, field, null);
    }

    private static Field getField(String fieldName) {
        try {
            return TypeBasedCandidateFilter.class.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }
}

class MockType1 {
}

class MockType2 {
}

class MockType3 {
}