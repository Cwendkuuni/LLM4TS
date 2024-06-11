import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.*;

public class FinalMockCandidateFilterTest {

    private FinalMockCandidateFilter filter;
    private Collection<Object> mocks;
    private Field field;
    private Object fieldInstance;
    private Object matchingMock;
    private BeanPropertySetter propertySetter;
    private FieldSetter fieldSetter;
    private Reporter reporter;

    @Before
    public void setUp() {
        filter = new FinalMockCandidateFilter();
        mocks = new ArrayList<>();
        field = mock(Field.class);
        fieldInstance = new Object();
        matchingMock = new Object();
        propertySetter = mock(BeanPropertySetter.class);
        fieldSetter = mock(FieldSetter.class);
        reporter = mock(Reporter.class);
    }

    @Test
    public void testFilterCandidate_singleMock() {
        mocks.add(matchingMock);

        // Stubbing
        when(propertySetter.set(fieldInstance, field)).thenReturn(true);

        // Execution
        filter.filterCandidate(mocks, field, fieldInstance).thenInject();

        // Verification
        verify(propertySetter, times(1)).set(fieldInstance, field);
        verify(fieldSetter, never()).set(fieldInstance, field);
        verify(reporter, never()).cannotInjectDependency(field, matchingMock, any(RuntimeException.class));
    }

    @Test
    public void testFilterCandidate_multipleMocks() {
        // Execution
        filter.filterCandidate(mocks, field, fieldInstance).thenInject();

        // Verification
        verify(propertySetter, never()).set(fieldInstance, field);
        verify(fieldSetter, never()).set(fieldInstance, field);
        verify(reporter, never()).cannotInjectDependency(field, matchingMock, any(RuntimeException.class));
    }

    @Test
    public void testFilterCandidate_singleMock_propertySetterThrowsException() {
        mocks.add(matchingMock);

        // Stubbing
        when(propertySetter.set(fieldInstance, field)).thenThrow(new RuntimeException());

        // Execution
        filter.filterCandidate(mocks, field, fieldInstance).thenInject();

        // Verification
        verify(propertySetter, times(1)).set(fieldInstance, field);
        verify(fieldSetter, times(1)).set(fieldInstance, field);
        verify(reporter, times(1)).cannotInjectDependency(field, matchingMock, any(RuntimeException.class));
    }
}