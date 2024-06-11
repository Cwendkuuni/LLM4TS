import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.util.MockUtil;
import org.junit.Test;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Collection;
import static org.junit.Assert.*;

public class NameBasedCandidateFilterTest {
    private NameBasedCandidateFilter filter;
    private MockCandidateFilter next;
    private MockUtil mockUtil;

    public NameBasedCandidateFilterTest() {
        next = mock(MockCandidateFilter.class);
        filter = new NameBasedCandidateFilter(next);
        mockUtil = new MockUtil();
    }

    @Test
    public void testFilterCandidate_OneMock() throws Exception {
        Field field = getClass().getDeclaredField("next");
        Object fieldInstance = next;
        
        Collection<Object> mocks = new ArrayList<Object>();
        Object mock = mock(getClass());
        mocks.add(mock);
        
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        
        verify(next, never()).filterCandidate(anyCollection(), any(Field.class), any());
        assertEquals(mocks, result.getMocks());
        assertEquals(field, result.getField());
        assertEquals(fieldInstance, result.getFieldInstance());
    }

    @Test
    public void testFilterCandidate_MultipleMocks_WithMatchingName() throws Exception {
        Field field = getClass().getDeclaredField("next");
        Object fieldInstance = next;
        
        Collection<Object> mocks = new ArrayList<Object>();
        Object mock1 = mock(getClass());
        Object mock2 = mock(getClass());
        mocks.add(mock1);
        mocks.add(mock2);
        
        String fieldName = field.getName();
        when(mockUtil.getMockName(mock1)).thenReturn(fieldName);
        when(mockUtil.getMockName(mock2)).thenReturn(fieldName);
        
        List<Object> expectedMocks = new ArrayList<Object>();
        expectedMocks.add(mock1);
        expectedMocks.add(mock2);
        
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        
        verify(next).filterCandidate(expectedMocks, field, fieldInstance);
        assertEquals(expectedMocks, result.getMocks());
        assertEquals(field, result.getField());
        assertEquals(fieldInstance, result.getFieldInstance());
    }

    // Add additional test cases for other methods in the class

}