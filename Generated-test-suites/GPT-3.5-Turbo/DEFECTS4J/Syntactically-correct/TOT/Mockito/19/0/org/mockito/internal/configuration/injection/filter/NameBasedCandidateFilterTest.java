import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.util.MockUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class NameBasedCandidateFilterTest {
    private NameBasedCandidateFilter filter;
    private MockCandidateFilter nextMock;
    private MockUtil mockUtil;

    @Before
    public void setup() {
        nextMock = mock(MockCandidateFilter.class);
        filter = new NameBasedCandidateFilter(nextMock);
        mockUtil = new MockUtil();
    }

    @Test
    public void filterCandidate_singleMatchingName_returnsFilteredCandidate() {
        // Arrange
        Object mock = mock(Object.class);
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock);
        Field field = getField("mockName");
        Object fieldInstance = new Object();

        // Act
        filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        assertEquals(mock, nextMock.filterCandidate(mocks, field, fieldInstance));
    }

    @Test
    public void filterCandidate_multipleMatchingNames_returnsFilteredCandidates() {
        // Arrange
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock1);
        mocks.add(mock2);
        Field field = getField("mockName");
        Object fieldInstance = new Object();

        // Act
        filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        List<Object> expectedFilteredCandidates = new ArrayList<>();
        expectedFilteredCandidates.add(mock1);
        assertEquals(expectedFilteredCandidates, nextMock.filterCandidate(expectedFilteredCandidates, field, fieldInstance));
    }

    @Test
    public void filterCandidate_noMatchingNames_returnsOriginalCandidates() {
        // Arrange
        Object mock = mock(Object.class);
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock);
        Field field = getField("field");
        Object fieldInstance = new Object();

        // Act
        filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        assertEquals(mocks, nextMock.filterCandidate(mocks, field, fieldInstance));
    }

    private Field getField(String fieldName) {
        try {
            return getClass().getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }
}