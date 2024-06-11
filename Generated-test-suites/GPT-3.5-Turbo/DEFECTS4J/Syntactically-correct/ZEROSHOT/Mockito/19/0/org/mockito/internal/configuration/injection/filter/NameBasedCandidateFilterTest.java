import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.*;

public class NameBasedCandidateFilterTest {

    private MockCandidateFilter next;
    private NameBasedCandidateFilter nameBasedCandidateFilter;

    @Before
    public void setUp() {
        next = mock(MockCandidateFilter.class);
        nameBasedCandidateFilter = new NameBasedCandidateFilter(next);
    }

    @Test
    public void testFilterCandidate() throws NoSuchFieldException {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(Object.class));
        Field field = Object.class.getDeclaredField("fieldName");
        Object fieldInstance = mock(Object.class);

        OngoingInjecter result = nameBasedCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        verify(next).filterCandidate(mocks, field, fieldInstance);
        Assert.assertEquals(next, result);
    }

    @Test
    public void testFilterCandidateWithMultipleMocksAndMatchingName() throws NoSuchFieldException {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(Object.class));
        mocks.add(mock(Object.class));
        Field field = Object.class.getDeclaredField("fieldName");
        Object fieldInstance = mock(Object.class);

        OngoingInjecter result = nameBasedCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        verify(next).filterCandidate(anyCollection(), eq(field), eq(fieldInstance));
        Assert.assertTrue(result instanceof OngoingInjecter);
    }

    @Test
    public void testFilterCandidateWithNoMatchingName() throws NoSuchFieldException {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(Object.class));
        Field field = Object.class.getDeclaredField("otherName");
        Object fieldInstance = mock(Object.class);

        OngoingInjecter result = nameBasedCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        verify(next).filterCandidate(mocks, field, fieldInstance);
        Assert.assertEquals(next, result);
    }

    @Test
    public void testFilterCandidateWithNoMultipleMocksAndMatchingName() throws NoSuchFieldException {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(Object.class));
        mocks.add(mock(Object.class));
        Field field = Object.class.getDeclaredField("fieldName");
        Object fieldInstance = mock(Object.class);

        OngoingInjecter result = nameBasedCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        verify(next).filterCandidate(anyCollection(), eq(field), eq(fieldInstance));
        Assert.assertTrue(result instanceof OngoingInjecter);
    }
}