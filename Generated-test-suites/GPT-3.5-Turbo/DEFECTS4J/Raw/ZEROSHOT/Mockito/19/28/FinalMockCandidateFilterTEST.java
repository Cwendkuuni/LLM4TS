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
    public void testFilterCandidate_singleMatchingMock() {
        Object matchingMock = new Object();
        mocks.add(matchingMock);

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(injecter);
        assertEquals(matchingMock, injecter.thenInject());
        verify(field, times(1)).setAccessible(true);
        verifyNoMoreInteractions(field);
    }

    @Test
    public void testFilterCandidate_multipleMocks() {
        mocks.add(new Object());
        mocks.add(new Object());

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(injecter);
        assertNull(injecter.thenInject());
        verifyNoMoreInteractions(field);
    }

    @Test
    public void testFilterCandidate_setterInjectionSuccess() {
        Object matchingMock = new Object();
        mocks.add(matchingMock);

        BeanPropertySetter beanPropertySetter = mock(BeanPropertySetter.class);
        when(beanPropertySetter.set(matchingMock)).thenReturn(true);
        whenNew(BeanPropertySetter.class).withArguments(fieldInstance, field).thenReturn(beanPropertySetter);

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(injecter);
        assertEquals(matchingMock, injecter.thenInject());
        verify(beanPropertySetter, times(1)).set(matchingMock);
        verifyZeroInteractions(new Reporter());
        verifyNoMoreInteractions(field);
    }

    @Test
    public void testFilterCandidate_setterInjectionFailure() {
        Object matchingMock = new Object();
        mocks.add(matchingMock);

        BeanPropertySetter beanPropertySetter = mock(BeanPropertySetter.class);
        when(beanPropertySetter.set(matchingMock)).thenReturn(false);
        whenNew(BeanPropertySetter.class).withArguments(fieldInstance, field).thenReturn(beanPropertySetter);

        FieldSetter fieldSetter = mock(FieldSetter.class);
        whenNew(FieldSetter.class).withArguments(fieldInstance, field).thenReturn(fieldSetter);

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(injecter);
        assertEquals(matchingMock, injecter.thenInject());
        verify(beanPropertySetter, times(1)).set(matchingMock);
        verify(fieldSetter, times(1)).set(matchingMock);
        verifyZeroInteractions(new Reporter());
    }

    @Test
    public void testFilterCandidate_injectionError() {
        Object matchingMock = new Object();
        mocks.add(matchingMock);

        BeanPropertySetter beanPropertySetter = mock(BeanPropertySetter.class);
        when(beanPropertySetter.set(matchingMock)).thenReturn(false);
        whenNew(BeanPropertySetter.class).withArguments(fieldInstance, field).thenReturn(beanPropertySetter);

        FieldSetter fieldSetter = mock(FieldSetter.class);
        when(fieldSetter.set(matchingMock)).thenThrow(new RuntimeException());
        whenNew(FieldSetter.class).withArguments(fieldInstance, field).thenReturn(fieldSetter);

        Reporter reporter = mock(Reporter.class);
        whenNew(Reporter.class).thenReturn(reporter);

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(injecter);
        assertEquals(matchingMock, injecter.thenInject());
        verify(beanPropertySetter, times(1)).set(matchingMock);
        verify(fieldSetter, times(1)).set(matchingMock);
        verify(reporter, times(1)).cannotInjectDependency(field, matchingMock, new RuntimeException());
    }
}