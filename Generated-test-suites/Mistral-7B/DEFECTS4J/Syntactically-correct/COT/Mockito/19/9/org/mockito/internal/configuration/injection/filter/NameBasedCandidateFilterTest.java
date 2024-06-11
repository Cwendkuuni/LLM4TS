import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.naming.Name;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class NameBasedCandidateFilterTest {

    private NameBasedCandidateFilter filter;
    private MockCandidateFilter nextMock;
    private MockUtil mockUtilMock;

    @Before
    public void setUp() {
        nextMock = mock(MockCandidateFilter.class);
        mockUtilMock = spy(new MockUtil());
        filter = new NameBasedCandidateFilter(nextMock);
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFilterCandidateWithOneMockAndMatchingFieldName() {
        Object mock = mock(Object.class);
        Name mockName = new Name("mockName");
        mockUtilMock.setMockName(mock, mockName);
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock);
        Field field = mock.getClass().getDeclaredField("fieldName");
        Object fieldInstance = new Object();

        Object result = filter.filterCandidate(mocks, field, fieldInstance);

        verify(nextMock).filterCandidate(mocks, field, fieldInstance);
        assertEquals(mock, result);
    }

    @Test
    public void testFilterCandidateWithOneMockAndNonMatchingFieldName() {
        Object mock = mock(Object.class);
        Name mockName = new Name("mockName2");
        mockUtilMock.setMockName(mock, mockName);
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock);
        Field field = mock.getClass().getDeclaredField("anotherFieldName");
        Object fieldInstance = new Object();

        Object result = filter.filterCandidate(mocks, field, fieldInstance);

        verify(nextMock, Mockito.never()).filterCandidate(mocks, field, fieldInstance);
        assertEquals(null, result);
    }

    @Test
    public void testFilterCandidateWithMultipleMocksAndMatchingFieldNames() {
        Object mock1 = mock(Object.class);
        Name mockName1 = new Name("mockName1");
        mockUtilMock.setMockName(mock1, mockName1);
        Object mock2 = mock(Object.class);
        Name mockName2 = new Name("mockName1");
        mockUtilMock.setMockName(mock2, mockName2);
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock1);
        mocks.add(mock2);
        Field field = mock1.getClass().getDeclaredField("fieldName");
        Object fieldInstance = new Object();

        Object result = filter.filterCandidate(mocks, field, fieldInstance);

        verify(nextMock).filterCandidate(Collections.singletonList(mock2), field, fieldInstance);
        assertEquals(mock2, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFilterCandidateWithNullMocks() {
        Field field = mock(Field.class);
        Object fieldInstance = new Object();

        filter.filterCandidate(null, field, fieldInstance);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFilterCandidateWithNullField() {
        Collection<Object> mocks = new ArrayList<>();
        Object fieldInstance = new Object();

        filter.filterCandidate(mocks, null, fieldInstance);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFilterCandidateWithNullFieldInstance() {
        Collection<Object> mocks = new ArrayList<>();
        Field field = mock(Field.class);

        filter.filterCandidate(mocks, field, null);
    }
}