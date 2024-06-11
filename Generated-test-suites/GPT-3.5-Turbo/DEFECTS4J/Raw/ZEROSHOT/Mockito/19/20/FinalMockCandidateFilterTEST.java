import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class FinalMockCandidateFilterTest {

    private FinalMockCandidateFilter mockCandidateFilter;

    @Before
    public void setUp() {
        mockCandidateFilter = new FinalMockCandidateFilter();
    }

    @Test
    public void testFilterCandidate_singleMock() throws Exception {
        Collection<Object> mocks = new ArrayList<>();
        Field field = FinalMockCandidateFilter.class.getDeclaredField("mocks");
        Object fieldInstance = new Object();

        Object mock = mock(Object.class);
        mocks.add(mock);

        FinalMockCandidateFilter.OngoingInjecter ongoingInjecter = mockCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(ongoingInjecter);

        Object result = ongoingInjecter.thenInject();

        assertNotNull(result);
        assertSame(mock, result);

        verify(mock, times(1)).getClass(); // Verifying only one interaction with the mock
    }
    
    @Test
    public void testFilterCandidate_multipleMocks() throws Exception {
        Collection<Object> mocks = new ArrayList<>();
        Field field = FinalMockCandidateFilter.class.getDeclaredField("mocks");
        Object fieldInstance = new Object();

        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        mocks.add(mock1);
        mocks.add(mock2);

        FinalMockCandidateFilter.OngoingInjecter ongoingInjecter = mockCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(ongoingInjecter);

        Object result = ongoingInjecter.thenInject();

        assertNull(result); // Since there are multiple mocks, no injection should be performed

        verifyZeroInteractions(mock1); // Verifying no interaction with the mocks
        verifyZeroInteractions(mock2);
    }
    
    @Test
    public void testFilterCandidate_noMock() throws Exception {
        Collection<Object> mocks = new ArrayList<>();
        Field field = FinalMockCandidateFilter.class.getDeclaredField("mocks");
        Object fieldInstance = new Object();

        FinalMockCandidateFilter.OngoingInjecter ongoingInjecter = mockCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(ongoingInjecter);

        Object result = ongoingInjecter.thenInject();

        assertNull(result); // Since there are no mocks, no injection should be performed

        verifyZeroInteractions(mocks); // Verifying no interaction with the mocks
    }
    
    @Test
    public void testFilterCandidate_validSetterInjection() throws Exception {
        Collection<Object> mocks = new ArrayList<>();
        Field field = FinalMockCandidateFilter.class.getDeclaredField("mocks");
        Object fieldInstance = new Object();

        Object mock = mock(Object.class);
        mocks.add(mock);

        BeanPropertySetter mockSetter = mock(BeanPropertySetter.class);
        when(mockSetter.set(eq(fieldInstance), eq(field))).thenReturn(true);

        FinalMockCandidateFilter.OngoingInjecter ongoingInjecter = mockCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(ongoingInjecter);

        Object result = ongoingInjecter.thenInject();

        assertNotNull(result);
        assertSame(mock, result);

        verify(mockSetter, times(1)).set(eq(fieldInstance), eq(field));
        verifyZeroInteractions(mock); // Verifying no interaction with the mocks
    }
    
    @Test
    public void testFilterCandidate_invalidSetterInjection() throws Exception {
        Collection<Object> mocks = new ArrayList<>();
        Field field = FinalMockCandidateFilter.class.getDeclaredField("mocks");
        Object fieldInstance = new Object();

        Object mock = mock(Object.class);
        mocks.add(mock);

        BeanPropertySetter mockSetter = mock(BeanPropertySetter.class);
        when(mockSetter.set(eq(fieldInstance), eq(field))).thenReturn(false);

        FieldSetter mockFieldSetter = mock(FieldSetter.class);
        doNothing().when(mockFieldSetter).set(eq(fieldInstance), eq(field));

        FinalMockCandidateFilter.OngoingInjecter ongoingInjecter = mockCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(ongoingInjecter);

        Object result = ongoingInjecter.thenInject();

        assertNotNull(result);
        assertSame(mock, result);

        verify(mockSetter, times(1)).set(eq(fieldInstance), eq(field));
        verify(mockFieldSetter, times(1)).set(eq(fieldInstance), eq(field));
        verifyZeroInteractions(mock); // Verifying no interaction with the mocks
    }
    
    @Test
    public void testFilterCandidate_exceptionDuringInjection() throws Exception {
        Collection<Object> mocks = new ArrayList<>();
        Field field = FinalMockCandidateFilter.class.getDeclaredField("mocks");
        Object fieldInstance = new Object();

        Object mock = mock(Object.class);
        mocks.add(mock);

        BeanPropertySetter mockSetter = mock(BeanPropertySetter.class);
        when(mockSetter.set(eq(fieldInstance), eq(field))).thenThrow(new RuntimeException());

        Reporter mockReporter = mock(Reporter.class);
        doNothing().when(mockReporter).cannotInjectDependency(eq(field), eq(mock), any(RuntimeException.class));

        FinalMockCandidateFilter.OngoingInjecter ongoingInjecter = mockCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(ongoingInjecter);

        Object result = ongoingInjecter.thenInject();

        assertNotNull(result);
        assertSame(mock, result);

        verify(mockSetter, times(1)).set(eq(fieldInstance), eq(field));
        verify(mockReporter, times(1)).cannotInjectDependency(eq(field), eq(mock), any(RuntimeException.class));
        verifyZeroInteractions(mock); // Verifying no interaction with the mocks
    }
}