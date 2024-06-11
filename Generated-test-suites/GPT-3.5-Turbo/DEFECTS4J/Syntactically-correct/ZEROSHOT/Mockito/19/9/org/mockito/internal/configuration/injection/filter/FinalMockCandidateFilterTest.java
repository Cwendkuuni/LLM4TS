import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
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

    @Before
    public void setUp() {
        filter = new FinalMockCandidateFilter();
        mocks = new ArrayList<Object>();
        field = mock(Field.class);
        fieldInstance = mock(Object.class);
    }

    @Test
    public void testFilterCandidate_singleMock_successfullyInjected() {
        Object matchingMock = mock(Object.class);
        mocks.add(matchingMock);

        BeanPropertySetter propertySetterMock = mock(BeanPropertySetter.class);
        FieldSetter fieldSetterMock = mock(FieldSetter.class);
        Reporter reporterMock = mock(Reporter.class);

        when(new BeanPropertySetter(fieldInstance, field)).thenReturn(propertySetterMock);
        when(propertySetterMock.set(matchingMock)).thenReturn(true);

        filter.filterCandidate(mocks, field, fieldInstance).thenInject();

        verify(propertySetterMock).set(matchingMock);
        verifyZeroInteractions(fieldSetterMock);
        verify(reporterMock, never()).cannotInjectDependency(any(Field.class), any(Object.class), any(RuntimeException.class));
        verifyNoMoreInteractions(propertySetterMock, fieldSetterMock, reporterMock);
    }

    @Test
    public void testFilterCandidate_singleMock_propertySetterFailed_fieldSetterSuccess() {
        Object matchingMock = mock(Object.class);
        mocks.add(matchingMock);

        BeanPropertySetter propertySetterMock = mock(BeanPropertySetter.class);
        FieldSetter fieldSetterMock = mock(FieldSetter.class);
        Reporter reporterMock = mock(Reporter.class);

        when(new BeanPropertySetter(fieldInstance, field)).thenReturn(propertySetterMock);
        when(propertySetterMock.set(matchingMock)).thenReturn(false);

        filter.filterCandidate(mocks, field, fieldInstance).thenInject();

        verify(propertySetterMock).set(matchingMock);
        verify(fieldSetterMock).set(matchingMock);
        verify(reporterMock, never()).cannotInjectDependency(any(Field.class), any(Object.class), any(RuntimeException.class));
        verifyNoMoreInteractions(propertySetterMock, fieldSetterMock, reporterMock);
    }

    @Test
    public void testFilterCandidate_singleMock_propertySetterFailed_fieldSetterFailed() {
        Object matchingMock = mock(Object.class);
        mocks.add(matchingMock);

        BeanPropertySetter propertySetterMock = mock(BeanPropertySetter.class);
        FieldSetter fieldSetterMock = mock(FieldSetter.class);
        Reporter reporterMock = mock(Reporter.class);

        when(new BeanPropertySetter(fieldInstance, field)).thenReturn(propertySetterMock);
        when(propertySetterMock.set(matchingMock)).thenReturn(false);
        doThrow(RuntimeException.class).when(new FieldSetter(fieldInstance, field)).set(matchingMock);

        filter.filterCandidate(mocks, field, fieldInstance).thenInject();

        verify(propertySetterMock).set(matchingMock);
        verify(fieldSetterMock).set(matchingMock);
        verify(reporterMock).cannotInjectDependency(field, matchingMock, any(RuntimeException.class));
        verifyNoMoreInteractions(propertySetterMock, fieldSetterMock, reporterMock);
    }

    @Test
    public void testFilterCandidate_multipleMocks_noInjection() {
        mocks.add(mock(Object.class));
        mocks.add(mock(Object.class));

        filter.filterCandidate(mocks, field, fieldInstance).thenInject();

        verifyZeroInteractions(field, fieldInstance);
    }
}