import org.junit.Test;
import static org.mockito.Mockito.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import static org.junit.Assert.*;

public class FinalMockCandidateFilterTest {

    @Test
    public void testFilterCandidate_WhenSingleMock_ShouldReturnInjecterWithMatchingMock() {
        // Arrange
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = new ArrayList<>();
        Object mock = mock(Object.class);
        mocks.add(mock);
        Field field = null; // We don't need an actual field for this test
        Object fieldInstance = new Object(); // We don't need an actual object for this test

        // Act
        FinalMockCandidateFilter.OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        assertNotNull(injecter);
        Object result = injecter.thenInject();
        assertEquals(mock, result);
    }

    @Test
    public void testFilterCandidate_WhenMultipleMocks_ShouldReturnNullInjecter() {
        // Arrange
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(Object.class));
        mocks.add(mock(Object.class));
        Field field = null; // We don't need an actual field for this test
        Object fieldInstance = new Object(); // We don't need an actual object for this test

        // Act
        FinalMockCandidateFilter.OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        assertNotNull(injecter);
        Object result = injecter.thenInject();
        assertNull(result);
    }

    @Test
    public void testFilterCandidate_WhenMatchingMockAndPropertySetterFails_ShouldUseFieldSetter() {
        // Arrange
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = new ArrayList<>();
        Object mock = mock(Object.class);
        mocks.add(mock);
        Field field = null; // We don't need an actual field for this test
        Object fieldInstance = new Object(); // We don't need an actual object for this test
        BeanPropertySetter propertySetter = mock(BeanPropertySetter.class);
        when(propertySetter.set(any())).thenReturn(false);
        FieldSetter fieldSetter = mock(FieldSetter.class);
        when(fieldSetter.set(any())).thenReturn(true); // FieldSetter succeeds

        // Act
        FinalMockCandidateFilter.OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        injecter.thenInject();

        // Assert
        verify(propertySetter).set(mock);
        verify(fieldSetter).set(mock);
    }

    @Test
    public void testFilterCandidate_WhenMatchingMockAndPropertyAndFieldSettersFail_ShouldThrowException() {
        // Arrange
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = new ArrayList<>();
        Object mock = mock(Object.class);
        mocks.add(mock);
        Field field = null; // We don't need an actual field for this test
        Object fieldInstance = new Object(); // We don't need an actual object for this test
        BeanPropertySetter propertySetter = mock(BeanPropertySetter.class);
        when(propertySetter.set(any())).thenReturn(false); // PropertySetter fails
        FieldSetter fieldSetter = mock(FieldSetter.class);
        when(fieldSetter.set(any())).thenReturn(false); // FieldSetter fails

        // Act
        try{
            FinalMockCandidateFilter.OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
            injecter.thenInject();
            fail("Expected exception not thrown.");
        } catch (RuntimeException e) {
            // Assert
            assertNotNull(e);
        }
    }
}