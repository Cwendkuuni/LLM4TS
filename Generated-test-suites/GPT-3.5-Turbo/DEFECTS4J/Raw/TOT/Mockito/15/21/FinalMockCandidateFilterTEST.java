import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.util.reflection.FieldSetter;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import static org.junit.Assert.*;

public class FinalMockCandidateFilterTest {

    private FinalMockCandidateFilter mockCandidateFilter;

    @Before
    public void setUp() {
        mockCandidateFilter = new FinalMockCandidateFilter();
    }

    @Test
    public void testFilterCandidate_withSingleCandidate_shouldReturnInjecterWithMatchingMock() throws Exception {
        // Arrange
        Collection<Object> mocks = Collections.singletonList("mock");
        Field field = FinalMockCandidateFilter.class.getDeclaredField("mocks");
        Object fieldInstance = new Object();

        // Act
        FinalMockCandidateFilter.OngoingInjecter injecter = mockCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        assertTrue(injecter.thenInject());
        assertSame("mock", new FieldSetter(fieldInstance, field).get());
    }

    @Test
    public void testFilterCandidate_withMultipleCandidates_shouldReturnInjecterWithoutInjecting() throws Exception {
        // Arrange
        Collection<Object> mocks = Arrays.asList("mock1", "mock2");
        Field field = FinalMockCandidateFilter.class.getDeclaredField("mocks");
        Object fieldInstance = new Object();

        // Act
        FinalMockCandidateFilter.OngoingInjecter injecter = mockCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        assertFalse(injecter.thenInject());
    }

    @Test(expected = MockitoException.class)
    public void testFilterCandidate_withExceptionDuringInjection_shouldThrowMockitoException() throws Exception {
        // Arrange
        Collection<Object> mocks = Collections.singletonList("mock");
        Field field = FinalMockCandidateFilter.class.getDeclaredField("mocks");
        Object fieldInstance = new Object();

        // Act
        FinalMockCandidateFilter.OngoingInjecter injecter = mockCandidateFilter.filterCandidate(mocks, field, fieldInstance);
        new FieldSetter(fieldInstance, field).set(null);

        // Assert (expecting MockitoException)
        injecter.thenInject();
    }
}