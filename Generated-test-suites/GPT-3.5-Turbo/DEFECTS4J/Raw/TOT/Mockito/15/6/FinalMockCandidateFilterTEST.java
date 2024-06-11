
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.reflection.FieldSetter;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import static org.junit.Assert.*;

public class FinalMockCandidateFilterTest {

    @Test
    public void testFilterCandidate_withOneMock_shouldReturnOngoingInjecter() throws Exception {
        // Arrange
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = Collections.singletonList("Mock1");
        Field field = FinalMockCandidateFilter.class.getDeclaredField("mocks");
        Object fieldInstance = new Object();

      
        // Act
        FinalMockCandidateFilter.OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
      
        // Assert
        assertNotNull(injecter);
        assertTrue(injecter.thenInject());
    }

    @Test
    public void testFilterCandidate_withMultipleMocks_shouldReturnOngoingInjecter() throws Exception {
        // Arrange
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = Arrays.asList("Mock1", "Mock2");
        Field field = FinalMockCandidateFilter.class.getDeclaredField("mocks");
        Object fieldInstance = new Object();
      
        // Act
        FinalMockCandidateFilter.OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
      
        // Assert
        assertNotNull(injecter);
        assertFalse(injecter.thenInject());
    }

    @Test(expected = MockitoException.class)
    public void testFilterCandidate_withNoMocks_shouldThrowMockitoException() throws Exception {
        // Arrange
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = Collections.emptyList();
        Field field = FinalMockCandidateFilter.class.getDeclaredField("mocks");
        Object fieldInstance = new Object();
      
        // Act
        FinalMockCandidateFilter.OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
    }

    @Test
    public void testFilterCandidate_withFieldSetterException_shouldThrowMockitoException() throws Exception {
        // Arrange
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = Collections.singletonList("Mock1");
        Field field = FinalMockCandidateFilter.class.getDeclaredField("mocks");
        Object fieldInstance = new Object();
        FieldSetter mockSetter = new FieldSetter(fieldInstance, field) {
            @Override
            public void set(Object target, Object value) {
                throw new RuntimeException("FieldSetterException");
            }
        };
      
        // Act
        FinalMockCandidateFilter.OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
      
        // Assert
        assertNotNull(injecter);
        try {
            injecter.thenInject();
            fail("Expected MockitoException to be thrown");
        } catch (MockitoException e) {
            assertEquals("Problems injecting dependency in " + field.getName(), e.getMessage());
            assertTrue(e.getCause() instanceof RuntimeException);
            assertEquals("FieldSetterException", e.getCause().getMessage());
        }
    }
}

