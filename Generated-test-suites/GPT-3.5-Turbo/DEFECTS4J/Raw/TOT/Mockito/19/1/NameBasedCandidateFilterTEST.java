import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.util.MockUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class NameBasedCandidateFilterTest {

    private NameBasedCandidateFilter nameBasedCandidateFilter;
    private MockUtil mockUtil;

    @Before
    public void setUp() {
        MockCandidateFilter mockCandidateFilter = new MockCandidateFilter() {
            @Override
            public Object filterCandidate(Collection<Object> mocks, Field field, Object fieldInstance) {
                return null;
            }
        };
        nameBasedCandidateFilter = new NameBasedCandidateFilter(mockCandidateFilter);
        mockUtil = new MockUtil();
    }

    @Test
    public void testFilterCandidate_OneMatchingMock_ReturnsListWithMatchingMock() {
        // Arrange
        List<Object> mocks = new ArrayList<>();
        Object mock = new Object();
        mocks.add(mock);
        Field field = getMockFieldWithMatchingName("mock");
        Object fieldInstance = null;

        // Act
        Object result = nameBasedCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        Assert.assertEquals(1, ((List<Object>) result).size());
        Assert.assertTrue(((List<Object>) result).contains(mock));
    }

    @Test
    public void testFilterCandidate_MultipleMatchingMocks_ReturnsListWithMatchingMocks() {
        // Arrange
        List<Object> mocks = new ArrayList<>();
        Object mock1 = new Object();
        Object mock2 = new Object();
        mocks.add(mock1);
        mocks.add(mock2);
        Field field = getMockFieldWithMatchingName("mock");
        Object fieldInstance = null;

        // Act
        Object result = nameBasedCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        Assert.assertEquals(2, ((List<Object>) result).size());
        Assert.assertTrue(((List<Object>) result).contains(mock1));
        Assert.assertTrue(((List<Object>) result).contains(mock2));
    }

    @Test
    public void testFilterCandidate_NoMatchingMocks_ReturnsEmptyList() {
        // Arrange
        List<Object> mocks = new ArrayList<>();
        Object mock = new Object();
        mocks.add(mock);
        Field field = getMockFieldWithMatchingName("nonexistentMock");
        Object fieldInstance = null;

        // Act
        Object result = nameBasedCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        Assert.assertTrue(((List<Object>) result).isEmpty());
    }

    private Field getMockFieldWithMatchingName(String fieldName) {
        try {
            return MockCandidateFilter.class.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }

}