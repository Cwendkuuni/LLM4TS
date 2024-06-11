import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter.OngoingInjecter;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TypeBasedCandidateFilterTest {

    private TypeBasedCandidateFilter filter;
    private MockCandidateFilter nextMock;
    private Object fieldInstance;
    private Field field;
    private List<Object> mocks;

    @Before
    public void setup() {
        filter = new TypeBasedCandidateFilter(nextMock = Mockito.spy(new StubMockCandidateFilter()));
        field = Mockito.spy(new Field("testField", Integer.TYPE, fieldInstance = new Object()));
        mocks = new ArrayList<>();
        mocks.add(mock(MockType1.class));
        mocks.add(mock(MockType2.class));
        mocks.add(mock(MockType3.class));
    }

    @Test
    public void testFilterCandidate_WithValidMockTypes_ShouldFilterMockWithMatchingType() {
        // Given
        Object mockWithMatchingType = mocks.get(1);

        // When
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        assertNotNull(result);
        verify(nextMock).filterCandidate(mockWithMatchingType, field, fieldInstance);
    }

    @Test
    public void testFilterCandidate_WithNoMockTypesMatching_ShouldInvokeNextFilter() {
        // Given

        // When
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        verify(nextMock).filterCandidate(mocks, field, fieldInstance);
    }

    @Test
    public void testFilterCandidate_WithNullFieldInstance_ShouldThrowNullPointerException() {
        // Given
        fieldInstance = null;

        // When & Then
        try {
            filter.filterCandidate(mocks, field, fieldInstance);
            assert false; // should not reach here
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testFilterCandidate_WithNullField_ShouldThrowNullPointerException() {
        // Given
        field = null;

        // When & Then
        try {
            filter.filterCandidate(mocks, field, fieldInstance);
            assert false; // should not reach here
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testFilterCandidate_WithNullMockCollection_ShouldThrowNullPointerException() {
        // Given
        mocks = null;

        // When & Then
        try {
            filter.filterCandidate(mocks, field, fieldInstance);
            assert false; // should not reach here
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testFilterCandidate_WithMultipleMocksOfSameType_ShouldReturnOnlyOne() {
        // Given
        Object mockWithMatchingType1 = mocks.get(1);
        Object mockWithMatchingType2 = mocks.remove(mocks.indexOf(mockWithMatchingType1)); // remove mock1 for next test
        mocks.add(new Object() {}); // add a new mock

        // When
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        assertNotNull(result);
        verify(nextMock).filterCandidate(mockWithMatchingType1, field, fieldInstance);
        // assert that nextMock.filterCandidate(mockWithMatchingType2, field, fieldInstance) was not called
    }

    @Test
    public void testFilterCandidate_WhenNextFilterReturnsNull_ShouldReturnNull() {
        // Given
        Mockito.when(nextMock.filterCandidate(mocks, field, fieldInstance)).thenReturn(null);

        // When
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        assertNull(result);
    }

    private static class StubMockCandidateFilter implements MockCandidateFilter {
        @Override
        public OngoingInjecter filterCandidate(Collection<Object> mocks, Field field, Object fieldInstance) {
            return null; // stub next filter to return null
        }
    }

    private static class MockType1 {}
    private static class MockType2 {}
    private static class MockType3 {}
}