import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.OngoingInjecter;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.reflection.Whitebox;
import org.mockito.stubbing.Answer;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

public class FinalMockCandidateFilterTest {

    private FinalMockCandidateFilter filter;
    private List<Object> mocks;
    private Field field;
    private Object fieldInstance;

    @Before
    public void setup() {
        filter = new FinalMockCandidateFilter();
        mocks = Arrays.asList(mock(Object.class), mock(Object.class));
        field = Whitebox.getInternalState(Object.class, "field");
        fieldInstance = mock(Object.class);
    }

    @Test
    public void testFilterCandidate_singleMock() {
        // Given
        Object mock = mocks.get(0);
        doAnswer(invocation -> {
            fieldInstance.getClass().getDeclaredField(invocation.getArgument(1).getName()).set(mock, invocation.getArgument(2));
            return null;
        }).when(fieldInstance).setField(any(Field.class), any());

        // When
        OngoingInjecter candidateFilter = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        assertEquals(mock, candidateFilter.thenInject());
        verify(fieldInstance).setField(eq(field), eq(mock));
    }

    @Test
    public void testFilterCandidate_multipleMocks() {
        // Given

        // When
        OngoingInjecter candidateFilter = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        assertNull(candidateFilter.thenInject());
    }

    @Test(expected = RuntimeException.class)
    public void testFilterCandidate_throwException() {
        // Given
        when(field).thenThrow(new RuntimeException("Exception message"));

        // When
        try {
            filter.filterCandidate(mocks, field, fieldInstance);
            fail("Exception should be thrown");
        } catch (RuntimeException e) {
            // Then
            assertEquals("Could not inject dependency: <field name>, <field instance>, java.lang.RuntimeException: Exception message", e.getMessage());
        }
    }
}