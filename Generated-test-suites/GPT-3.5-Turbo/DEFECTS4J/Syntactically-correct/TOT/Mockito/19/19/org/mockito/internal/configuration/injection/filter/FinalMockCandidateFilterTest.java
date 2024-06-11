import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FinalMockCandidateFilterTest {

    private FinalMockCandidateFilter filter;
    private Reporter reporterMock;

    @Before
    public void setup() {
        filter = new FinalMockCandidateFilter();
        reporterMock = mock(Reporter.class);
    }

    @Test
    public void filterCandidate_WhenOneMockAvailable_ShouldReturnOngoingInjecterWithMatchingMock() {
        Object mock = mock(Object.class);
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock);
        Field fieldMock = mock(Field.class);
        Object fieldInstanceMock = mock(Object.class);

        OngoingInjecter injecter = filter.filterCandidate(mocks, fieldMock, fieldInstanceMock);

        assertNotNull(injecter);
        Object result = injecter.thenInject();
        assertEquals(mock, result);
        verifyZeroInteractions(reporterMock);
    }

    @Test
    public void filterCandidate_WhenEmptyMocksList_ShouldReturnOngoingInjecterWithNullResult() {
        Collection<Object> mocks = new ArrayList<>();
        Field fieldMock = mock(Field.class);
        Object fieldInstanceMock = mock(Object.class);

        OngoingInjecter injecter = filter.filterCandidate(mocks, fieldMock, fieldInstanceMock);

        assertNotNull(injecter);
        Object result = injecter.thenInject();
        assertNull(result);
        verifyZeroInteractions(reporterMock);
    }

    @Test
    public void filterCandidate_WhenMultipleMocksAvailable_ShouldReturnOngoingInjecterWithNullResult() {
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock1);
        mocks.add(mock2);
        Field fieldMock = mock(Field.class);
        Object fieldInstanceMock = mock(Object.class);

        OngoingInjecter injecter = filter.filterCandidate(mocks, fieldMock, fieldInstanceMock);

        assertNotNull(injecter);
        Object result = injecter.thenInject();
        assertNull(result);
        verifyZeroInteractions(reporterMock);
    }

    @Test
    public void filterCandidate_WhenPropertySetterSucceeds_ShouldSetMatchingMockAndReturnMatchingMock() {
        Object mock = mock(Object.class);
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock);
        Field fieldMock = mock(Field.class);
        Object fieldInstanceMock = spy(Object.class);
        BeanPropertySetter propertySetterMock = mock(BeanPropertySetter.class);
        whenNew(BeanPropertySetter.class).withArguments(fieldInstanceMock, fieldMock).thenReturn(propertySetterMock);
        when(propertySetterMock.set(eq(mock))).thenReturn(true);

        OngoingInjecter injecter = filter.filterCandidate(mocks, fieldMock, fieldInstanceMock);

        assertNotNull(injecter);
        Object result = injecter.thenInject();
        assertEquals(mock, result);
        verifyZeroInteractions(reporterMock);
        verify(propertySetterMock).set(eq(mock));
        verifyNoMoreInteractions(propertySetterMock);
    }

    @Test
    public void filterCandidate_WhenPropertySetterFailsAndFieldSetterSucceeds_ShouldSetMatchingMockAndReturnMatchingMock() {
        Object mock = mock(Object.class);
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock);
        Field fieldMock = mock(Field.class);
        Object fieldInstanceMock = spy(Object.class);
        BeanPropertySetter propertySetterMock = mock(BeanPropertySetter.class);
        whenNew(BeanPropertySetter.class).withArguments(fieldInstanceMock, fieldMock).thenReturn(propertySetterMock);
        FieldSetter fieldSetterMock = mock(FieldSetter.class);
        whenNew(FieldSetter.class).withArguments(fieldInstanceMock, fieldMock).thenReturn(fieldSetterMock);
        when(propertySetterMock.set(eq(mock))).thenReturn(false);

        OngoingInjecter injecter = filter.filterCandidate(mocks, fieldMock, fieldInstanceMock);

        assertNotNull(injecter);
        Object result = injecter.thenInject();
        assertEquals(mock, result);
        verifyZeroInteractions(reporterMock);
        verify(propertySetterMock).set(eq(mock));
        verify(fieldSetterMock).set(eq(mock));
        verifyNoMoreInteractions(propertySetterMock, fieldSetterMock);
    }

    @Test
    public void filterCandidate_WhenBothSettersFail_ShouldCallReporterAndReturnNull() {
        Object mock = mock(Object.class);
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock);
        Field fieldMock = mock(Field.class);
        Object fieldInstanceMock = spy(Object.class);
        BeanPropertySetter propertySetterMock = mock(BeanPropertySetter.class);
        whenNew(BeanPropertySetter.class).withArguments(fieldInstanceMock, fieldMock).thenReturn(propertySetterMock);
        when(propertySetterMock.set(eq(mock))).thenReturn(false);
        FieldSetter fieldSetterMock = mock(FieldSetter.class);
        whenNew(FieldSetter.class).withArguments(fieldInstanceMock, fieldMock).thenReturn(fieldSetterMock);
        doThrow(new RuntimeException()).when(reporterMock).cannotInjectDependency(eq(fieldMock), eq(mock), any(RuntimeException.class));
        Whitebox.setInternalState(filter, "reporter", reporterMock);

        OngoingInjecter injecter = filter.filterCandidate(mocks, fieldMock, fieldInstanceMock);

        assertNotNull(injecter);
        Object result = injecter.thenInject();
        assertNull(result);
        verify(reporterMock).cannotInjectDependency(eq(fieldMock), eq(mock), any(RuntimeException.class));
        verify(propertySetterMock).set(eq(mock));
        verify(fieldSetterMock).set(eq(mock));
        verifyNoMoreInteractions(propertySetterMock, fieldSetterMock);
    }
}