import org.junit.Test;
import static org.junit.Assert.*;

public class FinalMockCandidateFilterTest {

    @Test
    public void testFilterCandidate_singleMock_success() {
        // Arrange
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(new MockObject());
        Field field = MockObject.class.getDeclaredField("mockField");
        Object fieldInstance = new MockObject();

        // Act
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        assertTrue(injecter.thenInject());
    }

    @Test
    public void testFilterCandidate_multipleMocks_failure() {
        // Arrange
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(new MockObject());
        mocks.add(new MockObject());
        Field field = MockObject.class.getDeclaredField("mockField");
        Object fieldInstance = new MockObject();

        // Act
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        assertFalse(injecter.thenInject());
    }

    @Test(expected = MockitoException.class)
    public void testFilterCandidate_exceptionHandling() {
        // Arrange
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(new MockObject());
        Field field = MockObject.class.getDeclaredField("mockField");
        Object fieldInstance = new MockObject();

        // Act
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        // Trying to inject dependency without setter method, expect MockitoException
        injecter.thenInject();
    }
}