import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.util.MockUtil;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;
import org.powermock.reflect.Whitebox;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class NameBasedCandidateFilterTest {

    private MockUtil mockUtil = new MockUtil();

    @Test
    public void testFilterCandidate_MultipleMocksWithSameName_ReturnsCorrectMock() {
        // Given
        String mockName = "mockName";
        MockCandidateFilter filter = new NameBasedCandidateFilter(null);
        Object fieldInstance = new Object();
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(createMockWithName(mockName));
        mocks.add(createMockWithName(mockName));
        Field field = createField();

        // When
        Object result = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        Assert.assertEquals(mocks.get(0), result);
    }

    @Test
    public void testFilterCandidate_MultipleMocksWithDifferentNames_ReturnsNextFilter() {
        // Given
        String mockName1 = "mockName1";
        String mockName2 = "mockName2";
        MockCandidateFilter filter = new NameBasedCandidateFilter(new NoopMockCandidateFilter());
        Object fieldInstance = new Object();
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(createMockWithName(mockName1));
        mocks.add(createMockWithName(mockName2));
        Field field = createField();

        // When
        Object result = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        Assert.assertNotSame(mocks.get(0), result);
        Assert.assertNotSame(mocks.get(1), result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFilterCandidate_NullCollection_ThrowsIllegalArgumentException() {
        // Given
        MockCandidateFilter filter = new NameBasedCandidateFilter(null);
        Collection<Object> mocks = null;
        Field field = createField();
        Object fieldInstance = new Object();

        // When
        filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        // exception should be thrown
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFilterCandidate_NullField_ThrowsIllegalArgumentException() {
        // Given
        MockCandidateFilter filter = new NameBasedCandidateFilter(null);
        Collection<Object> mocks = new ArrayList<>();
        Object fieldInstance = new Object();
        Field field = null;

        // When
        filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        // exception should be thrown
    }

    @Test
    public void testFilterCandidate_EmptyCollection_ReturnsNextFilter() {
        // Given
        MockCandidateFilter filter = new NameBasedCandidateFilter(null);
        Collection<Object> mocks = new ArrayList<>();
        Field field = createField();
        Object fieldInstance = new Object();

        // When
        Object result = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        Assert.assertNull(result);
    }

    @Test
    public void testFilterCandidate_NoConflictingMocks_ReturnsNextFilter() {
        // Given
        MockCandidateFilter filter = new NameBasedCandidateFilter(null);
        String mockName = "mockName";
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(createMockWithName("mockName1"));
        Field field = createField();
        Object fieldInstance = new Object();

        // When
        Object result = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        Assert.assertNull(result);
    }

    private Object createMockWithName(String name) {
        Object mock = MockUtil.createMock(new Object() {});
        Whitebox.setInternalState(mock, "name", name);
        return mock;
    }

    private Field createField() {
        try {
            return Whitebox.getInternalState(this, "field");
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    private static class NoopMockCandidateFilter implements MockCandidateFilter {
        @Override
        public OngoingInjecter filterCandidate(Collection<Object> mocks, Field field, Object fieldInstance) {
            return null;
        }
    }
}