import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.*;
import org.mockito.internal.util.MockUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class NameBasedCandidateFilterTest {
    private MockCandidateFilter nextMockCandidateFilter;
    private NameBasedCandidateFilter nameBasedCandidateFilter;
    private MockUtil mockUtil;

    @Before
    public void setUp() {
        nextMockCandidateFilter = mock(MockCandidateFilter.class);
        nameBasedCandidateFilter = new NameBasedCandidateFilter(nextMockCandidateFilter);
        mockUtil = new MockUtil();
    }

    @Test
    public void testFilterCandidate_multipleMocks_shouldReturnMatchingMocksWithSameName() {
        // Arrange
        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        Object mock3 = mock(Object.class);
        mocks.add(mock1);
        mocks.add(mock2);
        mocks.add(mock3);
        Field field = getField("mock1");
        Object fieldInstance = getFieldInstance(mock1);

        // Act
        OngoingInjecter result = nameBasedCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        List<Object> expectedMockNameMatches = new ArrayList<>();
        expectedMockNameMatches.add(mock1);
        assertEquals(expectedMockNameMatches, result.getMockNameMatches());
        assertEquals(field, result.getField());
        assertEquals(fieldInstance, result.getFieldInstance());
        verify(nextMockCandidateFilter).filterCandidate(expectedMockNameMatches, field, fieldInstance);
    }

    @Test
    public void testFilterCandidate_singleMock_shouldCallNextFilterCandidate() {
        // Arrange
        Collection<Object> mocks = new ArrayList<>();
        Object mock = mock(Object.class);
        mocks.add(mock);
        Field field = getField("mock");
        Object fieldInstance = getFieldInstance(mock);

        // Act
        OngoingInjecter result = nameBasedCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        verify(nextMockCandidateFilter).filterCandidate(mocks, field, fieldInstance);
        assertEquals(result, nextMockCandidateFilter.filterCandidate(mocks, field, fieldInstance));
    }

    // Helper methods to get field and field instance names
    private Field getField(String fieldName) {
        Field[] declaredFields = NameBasedCandidateFilter.class.getDeclaredFields();
        for (Field field : declaredFields) {
            if (fieldName.equals(field.getName())) {
                return field;
            }
        }
        return null;
    }

    private Object getFieldInstance(Object mock) {
        return mockUtil.getMockName(mock).toString();
    }
}