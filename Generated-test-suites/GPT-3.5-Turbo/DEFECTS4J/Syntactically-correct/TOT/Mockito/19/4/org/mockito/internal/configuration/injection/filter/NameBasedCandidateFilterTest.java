
import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.util.MockUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class NameBasedCandidateFilterTest {

    @Test
    public void testFilterCandidate_withMatchingName_shouldReturnFilteredCandidates() throws NoSuchFieldException {
        // Arrange
        List<Object> mocks = new ArrayList<>();
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        Object mock3 = mock(Object.class);
        mocks.add(mock1);
        mocks.add(mock2);
        mocks.add(mock3);
        Field field = NameBasedCandidateFilter.class.getDeclaredField("next");
        Object fieldInstance = mock(Object.class);

        // Act
        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(null);
        NameBasedCandidateFilter.OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        Assert.assertEquals(3, result.getMocks().size());
        Assert.assertTrue(result.getMocks().contains(mock1));
        Assert.assertTrue(result.getMocks().contains(mock2));
        Assert.assertTrue(result.getMocks().contains(mock3));
        Assert.assertEquals(field, result.getField());
        Assert.assertEquals(fieldInstance, result.getFieldInstance());
    }

    @Test
    public void testFilterCandidate_withNoMatchingName_shouldReturnOriginalCandidates() throws NoSuchFieldException {
        // Arrange
        List<Object> mocks = new ArrayList<>();
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        Object mock3 = mock(Object.class);
        mocks.add(mock1);
        mocks.add(mock2);
        mocks.add(mock3);
        Field field = NameBasedCandidateFilter.class.getDeclaredField("next");
        Object fieldInstance = mock(Object.class);

        // Act
        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(null);
        NameBasedCandidateFilter.OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        Assert.assertEquals(3, result.getMocks().size());
        Assert.assertTrue(result.getMocks().contains(mock1));
        Assert.assertTrue(result.getMocks().contains(mock2));
        Assert.assertTrue(result.getMocks().contains(mock3));
        Assert.assertEquals(field, result.getField());
        Assert.assertEquals(fieldInstance, result.getFieldInstance());
    }

    @Test
    public void testFilterCandidate_withConflictingNamingFields_shouldReturnFilteredCandidates() throws NoSuchFieldException {
        // Arrange
        List<Object> mocks = new ArrayList<>();
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        Object mock3 = mock(Object.class);
        mocks.add(mock1);
        mocks.add(mock2);
        mocks.add(mock3);
        Field field = NameBasedCandidateFilter.class.getDeclaredField("next");
        Object fieldInstance = mock(Object.class);

        when(mockUtil.getMockName(mock1)).thenReturn("mock1");
        when(mockUtil.getMockName(mock2)).thenReturn("mock2");
        when(mockUtil.getMockName(mock3)).thenReturn("mock3");

        // Act
        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(null);
        NameBasedCandidateFilter.OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        Assert.assertEquals(0, result.getMocks().size());
        Assert.assertEquals(field, result.getField());
        Assert.assertEquals(fieldInstance, result.getFieldInstance());
    }
}
