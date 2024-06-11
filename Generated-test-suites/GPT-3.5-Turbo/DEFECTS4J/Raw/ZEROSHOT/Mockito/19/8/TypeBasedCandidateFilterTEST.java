import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.OngoingInjecter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.*;

public class TypeBasedCandidateFilterTest {

    private TypeBasedCandidateFilter typeBasedCandidateFilter;
    @Mock
    private MockCandidateFilter mockCandidateFilter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        typeBasedCandidateFilter = new TypeBasedCandidateFilter(mockCandidateFilter);
    }

    @Test
    public void testFilterCandidate() {
        // Arrange
        Collection<Object> mocks = new ArrayList<>();
        Field field = mock(Field.class);
        Object fieldInstance = new Object();
        List<Object> mockTypeMatches = new ArrayList<>();
        Object mock = mock(Object.class);
        mocks.add(mock);

        // Act
        when(field.getType().isAssignableFrom(mock.getClass())).thenReturn(true);
        when(mockCandidateFilter.filterCandidate(mockTypeMatches, field, fieldInstance)).thenReturn(mock(OngoingInjecter.class));

        OngoingInjecter result = typeBasedCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        Assert.assertNotNull(result);
        verify(mockCandidateFilter).filterCandidate(mockTypeMatches, field, fieldInstance);
    }

}