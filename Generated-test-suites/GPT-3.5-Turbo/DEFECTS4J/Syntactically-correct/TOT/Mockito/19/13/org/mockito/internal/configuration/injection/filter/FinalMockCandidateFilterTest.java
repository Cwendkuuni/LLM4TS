import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class FinalMockCandidateFilterTest {

    private FinalMockCandidateFilter mockCandidateFilter;
    private Field mockField;
    private Object mockFieldInstance;

    @Before
    public void setUp() {
        mockCandidateFilter = new FinalMockCandidateFilter();
        mockField = mock(Field.class);
        mockFieldInstance = mock(Object.class);
    }

    @Test
    public void shouldReturnNullForEmptyMocks() {
        Collection<Object> emptyMocks = Collections.emptyList();

        OngoingInjecter ongoingInjecter = mockCandidateFilter.filterCandidate(emptyMocks, mockField, mockFieldInstance);
        
        assertThat(ongoingInjecter.thenInject()).isNull();
    }

    @Test
    public void shouldReturnMatchingMockAndInjectWithPropertySetter() {
        Object matchingMock = mock(Object.class);
        Collection<Object> mocks = Collections.singleton(matchingMock);
        when(mockFieldInstance.getClass()).thenReturn(Object.class);
        
        OngoingInjecter ongoingInjecter = mockCandidateFilter.filterCandidate(mocks, mockField, mockFieldInstance);
        
        assertThat(ongoingInjecter.thenInject()).isEqualTo(matchingMock);
        verify(mockFieldInstance, times(1)).getClass();
        verify(mockField, times(1)).getType();
        verify(mockFieldInstance, times(1)).getClass();
        verify(mockFieldInstance, times(1)).getClass();
    }

    @Test
    public void shouldReturnMatchingMockAndInjectWithFieldAccessIfPropertySetterFails() {
        Object matchingMock = mock(Object.class);
        Collection<Object> mocks = Collections.singleton(matchingMock);
        when(mockFieldInstance.getClass()).thenReturn(Object.class);
        when(mockFieldInstance.getClass()).thenReturn(Object.class);
        
        OngoingInjecter ongoingInjecter = mockCandidateFilter.filterCandidate(mocks, mockField, mockFieldInstance);

        assertThat(ongoingInjecter.thenInject()).isEqualTo(matchingMock);
        verify(mockFieldInstance, times(1)).getClass();
        verify(mockField, times(1)).getType();
        verify(mockFieldInstance, times(1)).getClass();
        verify(mockFieldInstance, times(1)).getClass();
        verify(mockFieldInstance, times(1)).getClass();
    }

    @Test
    public void shouldCatchRuntimeExceptionAndCallReporter() {
        Object matchingMock = mock(Object.class);
        Collection<Object> mocks = Collections.singleton(matchingMock);
        when(mockFieldInstance.getClass()).thenReturn(Object.class);
        doThrow(new RuntimeException()).when(mockFieldInstance).getClass();

        Reporter reporter = mock(Reporter.class);

        OngoingInjecter ongoingInjecter = mockCandidateFilter.filterCandidate(mocks, mockField, mockFieldInstance);

        assertThat(ongoingInjecter.thenInject()).isEqualTo(matchingMock);
        verify(mockFieldInstance, times(1)).getClass();
        verify(mockField, times(1)).getType();
        verify(mockFieldInstance, times(1)).getClass();
        verify(mockFieldInstance, times(1)).getClass();
        verify(reporter, times(1)).cannotInjectDependency(eq(mockField), eq(matchingMock), any(RuntimeException.class));
    }
}