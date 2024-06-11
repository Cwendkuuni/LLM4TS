import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.util.MockUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class NameBasedCandidateFilterTest {

    private MockCandidateFilter mockCandidateFilter;
    private NameBasedCandidateFilter nameBasedCandidateFilter;
    private MockUtil mockUtil;

    @Before
    public void setUp() {
        mockCandidateFilter = mock(MockCandidateFilter.class);
        mockUtil = mock(MockUtil.class);
        nameBasedCandidateFilter = new NameBasedCandidateFilter(mockCandidateFilter);
    }

    @Test
    public void testFilterCandidate_OneMock_FieldNameMatches() throws NoSuchFieldException {
        Field field = SomeClass.class.getDeclaredField("someField");
        Object fieldInstance = new Object();

        Collection<Object> mocks = new ArrayList<>();
        Object mock = mock(Object.class);
        mocks.add(mock);
        
        when(mockUtil.getMockName(mock)).thenReturn("someField");

        OngoingInjecter result = nameBasedCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        assertEquals(mockCandidateFilter, result);
        verify(mockCandidateFilter).filterCandidate(mocks, field, fieldInstance);
        verifyNoMoreInteractions(mockCandidateFilter);
    }

    @Test
    public void testFilterCandidate_OneMock_FieldNameDoesNotMatch() throws NoSuchFieldException {
        Field field = SomeClass.class.getDeclaredField("someField");
        Object fieldInstance = new Object();

        Collection<Object> mocks = new ArrayList<>();
        Object mock = mock(Object.class);
        mocks.add(mock);
        
        when(mockUtil.getMockName(mock)).thenReturn("otherField");

        OngoingInjecter result = nameBasedCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        assertEquals(mockCandidateFilter, result);
        verify(mockCandidateFilter).filterCandidate(mocks, field, fieldInstance);
        verifyNoMoreInteractions(mockCandidateFilter);
    }

    @Test
    public void testFilterCandidate_MultipleMocks_OneMatch() throws NoSuchFieldException {
        Field field = SomeClass.class.getDeclaredField("someField");
        Object fieldInstance = new Object();

        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        mocks.add(mock1);
        mocks.add(mock2);
        
        when(mockUtil.getMockName(mock1)).thenReturn("someField");
        when(mockUtil.getMockName(mock2)).thenReturn("otherField");

        OngoingInjecter result = nameBasedCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        assertEquals(mockCandidateFilter, result);
        verify(mockCandidateFilter).filterCandidate(anyCollection(), eq(field), eq(fieldInstance));
        verifyNoMoreInteractions(mockCandidateFilter);
    }

    @Test
    public void testFilterCandidate_MultipleMocks_NoMatch() throws NoSuchFieldException {
        Field field = SomeClass.class.getDeclaredField("someField");
        Object fieldInstance = new Object();

        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        mocks.add(mock1);
        mocks.add(mock2);
        
        when(mockUtil.getMockName(mock1)).thenReturn("otherField");
        when(mockUtil.getMockName(mock2)).thenReturn("anotherField");

        OngoingInjecter result = nameBasedCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        assertEquals(mockCandidateFilter, result);
        verify(mockCandidateFilter).filterCandidate(mocks, field, fieldInstance);
        verifyNoMoreInteractions(mockCandidateFilter);
    }

    @Test
    public void testFilterCandidate_NoMocks() throws NoSuchFieldException {
        Field field = SomeClass.class.getDeclaredField("someField");
        Object fieldInstance = new Object();

        Collection<Object> mocks = new ArrayList<>();

        OngoingInjecter result = nameBasedCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        assertEquals(mockCandidateFilter, result);
        verify(mockCandidateFilter).filterCandidate(mocks, field, fieldInstance);
        verifyNoMoreInteractions(mockCandidateFilter);
    }

    private static class SomeClass {
        private Object someField;
    }
}