import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;



public class NameBasedCandidateFilterTest {

    @Test
    public void testFilterCandidate() throws Exception {
        MockCandidateFilter nextMock = mock(MockCandidateFilter.class);
        List<Object> mocks = new ArrayList<Object>();
        Field field = mock(Field.class);
        Object fieldInstance = new Object();

        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(nextMock);

        filter.filterCandidate(mocks, field, fieldInstance);

        verify(nextMock).filterCandidate(mocks, field, fieldInstance);
    }

    @Test
    public void testFilterCandidateWithMockNameMatches() throws Exception {
        MockCandidateFilter nextMock = mock(MockCandidateFilter.class);
        List<Object> mocks = new ArrayList<Object>();
        Object mock = mock(Object.class);
        mocks.add(mock);
        when(mockUtil.getMockName(mock)).thenReturn("MockObject");
        Field field = mock(Field.class);
        when(field.getName()).thenReturn("MockObject");
        Object fieldInstance = new Object();

        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(nextMock);

        filter.filterCandidate(mocks, field, fieldInstance);

        List<Object> expectedMockNameMatches = new ArrayList<Object>();
        expectedMockNameMatches.add(mock);
        verify(nextMock).filterCandidate(expectedMockNameMatches, field, fieldInstance);
    }

    @Test
    public void testFilterCandidateWithMultipleMocks() throws Exception {
        MockCandidateFilter nextMock = mock(MockCandidateFilter.class);
        List<Object> mocks = new ArrayList<Object>();
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        mocks.add(mock1);
        mocks.add(mock2);
        when(mockUtil.getMockName(mock1)).thenReturn("MockObject1");
        when(mockUtil.getMockName(mock2)).thenReturn("MockObject2");
        Field field = mock(Field.class);
        when(field.getName()).thenReturn("MockObject1");
        Object fieldInstance = new Object();

        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(nextMock);

        filter.filterCandidate(mocks, field, fieldInstance);

        List<Object> expectedMockNameMatches = new ArrayList<Object>();
        expectedMockNameMatches.add(mock1);
        verify(nextMock).filterCandidate(expectedMockNameMatches, field, fieldInstance);
    }

}

