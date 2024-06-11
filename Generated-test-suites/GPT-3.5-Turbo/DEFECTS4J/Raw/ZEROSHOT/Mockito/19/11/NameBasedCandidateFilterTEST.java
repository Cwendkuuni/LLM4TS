import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class NameBasedCandidateFilterTest {

    private NameBasedCandidateFilter filter;
    private MockCandidateFilter nextFilter;
    private MockUtil mockUtil;

    @Before
    public void setup() {
        nextFilter = mock(MockCandidateFilter.class);
        filter = new NameBasedCandidateFilter(nextFilter);
        mockUtil = spy(new MockUtil());
    }

    @Test
    public void testFilterCandidate_singleMock_match() {
        List<Object> mocks = new ArrayList<>();
        Object mock = mock(Object.class);
        mocks.add(mock);

        Field field = getField("mock");
        Object fieldInstance = getFieldInstance(mock);

        when(mockUtil.getMockName(mock)).thenReturn("mock");

        MockCandidateFilter.OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        verify(nextFilter).filterCandidate(mocks, field, fieldInstance);
        assertNotNull(result);
    }

    @Test
    public void testFilterCandidate_singleMock_noMatch() {
        List<Object> mocks = new ArrayList<>();
        Object mock = mock(Object.class);
        mocks.add(mock);

        Field field = getField("nonMatchingField");
        Object fieldInstance = getFieldInstance(mock);

        when(mockUtil.getMockName(mock)).thenReturn("mock");

        MockCandidateFilter.OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        verify(nextFilter).filterCandidate(mocks, field, fieldInstance);
        assertNotNull(result);
    }

    @Test
    public void testFilterCandidate_multipleMock_match() {
        List<Object> mocks = new ArrayList<>();
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        mocks.add(mock1);
        mocks.add(mock2);

        Field field = getField("mock1");
        Object fieldInstance = getFieldInstance(mock1);

        when(mockUtil.getMockName(mock1)).thenReturn("mock1");
        when(mockUtil.getMockName(mock2)).thenReturn("mock2");

        MockCandidateFilter.OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        verify(nextFilter).filterCandidate(argThat(argument -> argument.size() == 1 && argument.contains(mock1)), field, fieldInstance);
        assertNotNull(result);
    }

    @Test
    public void testFilterCandidate_multipleMock_noMatch() {
        List<Object> mocks = new ArrayList<>();
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        mocks.add(mock1);
        mocks.add(mock2);

        Field field = getField("nonMatchingField");
        Object fieldInstance = getFieldInstance(mock1);

        when(mockUtil.getMockName(mock1)).thenReturn("mock1");
        when(mockUtil.getMockName(mock2)).thenReturn("mock2");

        MockCandidateFilter.OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        verify(nextFilter).filterCandidate(mocks, field, fieldInstance);
        assertNotNull(result);
    }

    private Field getField(String fieldName) {
        try {
            return NameBasedCandidateFilter.class.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            return null;
        }
    }

    private Object getFieldInstance(Object mock) {
        try {
            Field instanceField = MockUtil.class.getDeclaredField("mockInstance");
            instanceField.setAccessible(true);
            return instanceField.get(mock);
        } catch (Exception e) {
            return null;
        }
    }
}