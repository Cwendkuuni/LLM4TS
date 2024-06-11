import org.junit.Test;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.OngoingInjecter;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class FinalMockCandidateFilterTest {

    @Test
    public void testFilterCandidate_OneMock_Success() throws Exception {
        // Given
        Collection<Object> mocks = Collections.singletonList(mock(Object.class));
        Field field = Field.class.getDeclaredField("someField");
        Object fieldInstance = spy(new Object() {});

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        // When
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        assertNotNull(result);
        assertTrue(result.thenInject());
    }
}