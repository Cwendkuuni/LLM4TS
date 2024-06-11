import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class NameBasedCandidateFilterTest {

    private MockCandidateFilter mockFilter;
    private NameBasedCandidateFilter filter;

    @Before
    public void setUp() {
        mockFilter = Mockito.mock(MockCandidateFilter.class);
        filter = new NameBasedCandidateFilter(mockFilter);
    }

    @Test
    public void testNameBasedCandidateFilterConstructor() {
        Mockito.verify(mockFilter, Mockito.times(1)).<MockCandidateFilter>constructor(ArgumentMatchers.any());
    }

    @Test
    public void testFilterCandidateNoConflict() {
        Field mockField = Mockito.mock(Field.class);
        Object fieldInstance = Mockito.mock(Object.class);
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(Mockito.mock(Object.class));

        Mockito.when(mockFilter.filterCandidate(mocks, mockField, fieldInstance)).thenCallRealMethod();
        Object result = filter.filterCandidate(mocks, mockField, fieldInstance);

        Mockito.verify(mockFilter, Mockito.times(1)).filterCandidate(mocks, mockField, fieldInstance);
        Assert.assertSame(mocks.get(0), result);
    }

    @Test
    public void testFilterCandidateEmptyCollection() {
        Field mockField = mock(Field.class);
        Object fieldInstance = mock(Object.class);
        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(mockFilter);
        assertNull(filter.filterCandidate(new ArrayList<>(), mockField, fieldInstance));
    }

    @Test
    public void testFilterCandidateMultipleMocksWithSameName() {
        Field mockField = mock(Field.class);
        Object fieldInstance = mock(Object.class);
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(Object.class, "mock1"));
        mocks.add(mock(Object.class, "mock1"));

        Mockito.when(mockFilter.filterCandidate(mocks, mockField, fieldInstance)).thenCallRealMethod();
        Object result = filter.filterCandidate(mocks, mockField, fieldInstance);

        Mockito.verify(mockFilter, Mockito.times(1)).filterCandidate(mocks, mockField, fieldInstance);
        Assert.assertSame(mocks.get(0), result);
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidateNullArguments() {
        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(mockFilter);
        filter.filterCandidate(null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFilterCandidateDifferentTypes() {
        Field mockField = mock(Field.class);
        Object fieldInstance1 = mock(Object.class);
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(String.class, "mockString"));

        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(mockFilter);
        filter.filterCandidate(mocks, mockField, fieldInstance1);
    }

}