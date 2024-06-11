import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class NameBasedCandidateFilterTest {

    @Test
    public void filterCandidate_WithMultipleMocksAndMatchingName_ShouldReturnFilteredMocksList() throws Exception {
        // Arrange
        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(null);
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(new MockObject("mock1"));
        mocks.add(new MockObject("mock2"));
        Field field = MockObject.class.getDeclaredField("name");
        Object fieldInstance = new MockObject("mock1");
        // Act
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        // Assert
        Assert.assertNotNull(result);
    }

    @Test
    public void filterCandidate_WithMultipleMocksAndNoMatchingName_ShouldReturnUnfilteredMocksList() throws Exception {
        // Arrange
        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(null);
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(new MockObject("mock1"));
        mocks.add(new MockObject("mock2"));
        Field field = MockObject.class.getDeclaredField("name");
        Object fieldInstance = new MockObject("mock3");
        // Act
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        // Assert
        Assert.assertNull(result);
    }

    @Test
    public void filterCandidate_WithSingleMock_ShouldReturnUnfilteredMocksList() throws Exception {
        // Arrange
        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(null);
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(new MockObject("mock1"));
        Field field = MockObject.class.getDeclaredField("name");
        Object fieldInstance = new MockObject("mock1");
        // Act
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        // Assert
        Assert.assertNull(result);
    }

    @Test
    public void filterCandidate_WithEmptyMocksList_ShouldReturnUnfilteredMocksList() throws Exception {
        // Arrange
        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(null);
        Collection<Object> mocks = new ArrayList<>();
        Field field = MockObject.class.getDeclaredField("name");
        Object fieldInstance = new MockObject("mock1");
        // Act
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        // Assert
        Assert.assertNull(result);
    }

    @Test
    public void filterCandidate_WithNullMocksList_ShouldReturnUnfilteredMocksList() throws Exception {
        // Arrange
        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(null);
        Collection<Object> mocks = null;
        Field field = MockObject.class.getDeclaredField("name");
        Object fieldInstance = new MockObject("mock1");
        // Act
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        // Assert
        Assert.assertNull(result);
    }

    @Test
    public void filterCandidate_WithNullField_ShouldReturnUnfilteredMocksList() throws Exception {
        // Arrange
        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(null);
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(new MockObject("mock1"));
        Field field = null;
        Object fieldInstance = new MockObject("mock1");
        // Act
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        // Assert
        Assert.assertNull(result);
    }

    @Test
    public void filterCandidate_WithNullFieldInstance_ShouldReturnUnfilteredMocksList() throws Exception {
        // Arrange
        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(null);
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(new MockObject("mock1"));
        Field field = MockObject.class.getDeclaredField("name");
        Object fieldInstance = null;
        // Act
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        // Assert
        Assert.assertNull(result);
    }

    private class MockObject {
        private String name;

        public MockObject(String name) {
            this.name = name;
        }
    }
}