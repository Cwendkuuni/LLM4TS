import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.*;

public class TypeBasedCandidateFilterTest {

    private TypeBasedCandidateFilter filter;
    private Collection<Object> mocks;
    private Object fieldInstance;
    private Object mockA;
    private Object mockB;
    private Object mockC;

    @Before
    public void setup() {
        mockA = Mockito.mock(String.class);
        mockB = Mockito.mock(Number.class);
        mockC = Mockito.mock(List.class);

        mocks = new ArrayList<>();
        mocks.add(mockA);
        mocks.add(mockB);
        mocks.add(mockC);

        filter = new TypeBasedCandidateFilter(null);
        fieldInstance = new Object();
    }

    @Test
    public void testFilterCandidate_MockMatchesType_ReturnsMock() {
        // Given
        when(mockA.getClass()).thenReturn(String.class);
        when(field.getType()).thenReturn(String.class);

        // When
        OngoingInjecter result = filter.filterCandidate(mocks, new Field("fieldName", fieldInstance), fieldInstance);

        // Then
        verify(mockA).eq(result.getMock());
    }

    @Test
    public void testFilterCandidate_MockDoesNotMatchType_PassesToNext() {
        // Given
        when(mockB.getClass()).thenReturn(Number.class);
        when(field.getType()).thenReturn(String.class);

        // When
        OngoingInjecter result = filter.filterCandidate(mocks, new Field("fieldName", fieldInstance), fieldInstance);

        // Then
        verify(next, never()).filterCandidate(argThat(isA(Collection.class)), eq(field), eq(fieldInstance));
    }

    @Test
    public void testFilterCandidate_NextFilterIsCalled_ReturnsInjectedValue() {
        // Given
        Object expectedValue = new Object();
        MockCandidateFilter nextFilter = mock(MockCandidateFilter.class);
        when(nextFilter.filterCandidate(argThat(isA(Collection.class)), eq(field), eq(fieldInstance))).thenAnswer(new Answer<OngoingInjecter>() {
            @Override
            public OngoingInjecter answer(InvocationOnMock invocation) throws Throwable {
                return new OngoingInjecter(expectedValue);
            }
        });

        filter = new TypeBasedCandidateFilter(nextFilter);

        // When
        OngoingInjecter result = filter.filterCandidate(mocks, new Field("fieldName", fieldInstance), fieldInstance);

        // Then
        verify(nextFilter).filterCandidate(argThat(isA(Collection.class)), eq(field), eq(fieldInstance));
        assertEquals(expectedValue, result.getInjectedValue());
    }

    @Test
    public void testFilterCandidate_FieldInstanceIsNull_NoMockIsReturned() {
        // Given
        when(field.getType()).thenReturn(Integer.class);

        // When
        OngoingInjecter result = filter.filterCandidate(mocks, new Field("fieldName", null), null);

        // Then
        assertNull(result);
    }

    @Test
    public void testFilterCandidate_FieldTypeIsAssignableFromObjectClass_NoMockIsReturned() {
        // Given
        when(field.getType()).thenReturn(Object.class);

        // When
        OngoingInjecter result = filter.filterCandidate(mocks, new Field("fieldName", fieldInstance), fieldInstance);

        // Then
        assertNull(result);
    }
}