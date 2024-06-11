import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class FinalMockCandidateFilterTest {

    private FinalMockCandidateFilter filter;
    private Collection<Object> mocks;
    private Field field;
    private Object fieldInstance;

    @Before
    public void setUp() {
        filter = new FinalMockCandidateFilter();
        mocks = new ArrayList<>();
        field = mock(Field.class);
        fieldInstance = new Object();
    }

    @Test
    public void filterCandidate_WithSingleMock_ShouldReturnInjecterWithMatchingMock() {
        Object mock = new Object();
        mocks.add(mock);

        FinalMockCandidateFilter.OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(injecter);
        Object result = injecter.thenInject();
        assertEquals(mock, result);
        verifyNoMoreInteractions(field);
    }

    @Test
    public void filterCandidate_WithMultipleMocks_ShouldReturnEmptyInjecter() {
        mocks.add(new Object());
        mocks.add(new Object());

        FinalMockCandidateFilter.OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(injecter);
        Object result = injecter.thenInject();
        assertNull(result);
        verifyNoMoreInteractions(field);
    }

    @Test
    public void filterCandidate_WithSingleMockAndSuccessfulBeanPropertySetter_ShouldReturnInjecterWithMatchingMock() {
        Object mock = new Object();
        mocks.add(mock);
        BeanPropertySetter propertySetterMock = mock(BeanPropertySetter.class);
        when(propertySetterMock.set(mock)).thenReturn(true);
        whenNew(BeanPropertySetter.class).withArguments(fieldInstance, field).thenReturn(propertySetterMock);

        FinalMockCandidateFilter.OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(injecter);
        Object result = injecter.thenInject();
        assertEquals(mock, result);
        verify(propertySetterMock).set(mock);
        verifyNoMoreInteractions(field, propertySetterMock);
    }

    @Test
    public void filterCandidate_WithSingleMockAndFailedBeanPropertySetter_ShouldReturnInjecterWithMatchingMock_UsingFieldSetter() {
        Object mock = new Object();
        mocks.add(mock);
        BeanPropertySetter propertySetterMock = mock(BeanPropertySetter.class);
        FieldSetter fieldSetterMock = mock(FieldSetter.class);
        when(propertySetterMock.set(mock)).thenReturn(false);
        whenNew(BeanPropertySetter.class).withArguments(fieldInstance, field).thenReturn(propertySetterMock);
        whenNew(FieldSetter.class).withArguments(fieldInstance, field).thenReturn(fieldSetterMock);

        FinalMockCandidateFilter.OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(injecter);
        Object result = injecter.thenInject();
        assertEquals(mock, result);
        verify(propertySetterMock).set(mock);
        verify(fieldSetterMock).set(mock);
        verifyNoMoreInteractions(field, propertySetterMock, fieldSetterMock);
    }

    @Test
    public void filterCandidate_WithSingleMockAndExceptionInBeanPropertySetter_ShouldThrowReporterException() {
        Object mock = new Object();
        mocks.add(mock);
        BeanPropertySetter propertySetterMock = mock(BeanPropertySetter.class);
        when(propertySetterMock.set(mock)).thenThrow(new RuntimeException());
        whenNew(BeanPropertySetter.class).withArguments(fieldInstance, field).thenReturn(propertySetterMock);
        Reporter reporterMock = mock(Reporter.class);
        whenNew(Reporter.class).thenReturn(reporterMock);

        FinalMockCandidateFilter.OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(injecter);
        try {
            injecter.thenInject();
            fail("Expected Reporter exception to be thrown");
        } catch (Reporter.ReportedInvocationException e) {
            verify(reporterMock).cannotInjectDependency(field, mock, e.getCause());
        }
        verifyNoMoreInteractions(field, propertySetterMock, reporterMock);
    }

    @Test
    public void filterCandidate_WithMultipleMocks_ShouldReturnEmptyInjecter_UsingThenInject() {
        mocks.add(new Object());
        mocks.add(new Object());

        FinalMockCandidateFilter.OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(injecter);
        Object result = injecter.thenInject();
        assertNull(result);
        verifyNoMoreInteractions(field);
    }

}