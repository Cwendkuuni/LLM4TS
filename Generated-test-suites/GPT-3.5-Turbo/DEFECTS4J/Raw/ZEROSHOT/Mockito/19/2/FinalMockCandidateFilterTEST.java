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

    private FinalMockCandidateFilter filter;
    private Collection<Object> mocks;
    private Field field;
    private Object fieldInstance;

    @Before
    public void setUp() {
        filter = new FinalMockCandidateFilter();
        mocks = new ArrayList<Object>();
        field = mock(Field.class);
        fieldInstance = new Object();
    }

    @Test
    public void testFilterCandidateWithOneMock() {
        Object mock1 = mock(Object.class);
        mocks.add(mock1);

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(injecter);

        Object result = injecter.thenInject();

        assertSame(mock1, result);
        verify(field, times(0)).setAccessible(true);
    }

    @Test
    public void testFilterCandidateWithMultipleMocks() {
        mocks.add(mock(Object.class));
        mocks.add(mock(Object.class));

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(injecter);

        Object result = injecter.thenInject();

        assertNull(result);
        verify(field, times(0)).setAccessible(true);
    }
    
    @Test
    public void testFilterCandidateWithMatchingMockAndBeanPropertySetterFails() {
        Object mock1 = mock(Object.class);
        mocks.add(mock1);

        BeanPropertySetter beanPropertySetter = mock(BeanPropertySetter.class);
        when(beanPropertySetter.set(mock1)).thenReturn(false);
        whenNew(BeanPropertySetter.class).withArguments(fieldInstance, field).thenReturn(beanPropertySetter);

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(injecter);

        Object result = injecter.thenInject();

        assertSame(mock1, result);
        verify(beanPropertySetter).set(mock1);
        verify(field, times(1)).setAccessible(true);
    }

    @Test
    public void testFilterCandidateWithMatchingMockAndBeanPropertySetterSucceeds() {
        Object mock1 = mock(Object.class);
        mocks.add(mock1);

        BeanPropertySetter beanPropertySetter = mock(BeanPropertySetter.class);
        when(beanPropertySetter.set(mock1)).thenReturn(true);
        whenNew(BeanPropertySetter.class).withArguments(fieldInstance, field).thenReturn(beanPropertySetter);

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(injecter);

        Object result = injecter.thenInject();

        assertSame(mock1, result);
        verify(beanPropertySetter).set(mock1);
        verify(field, times(0)).setAccessible(true);
    }

    @Test
    public void testFilterCandidateWithMatchingMockAndFieldSetter() {
        Object mock1 = mock(Object.class);
        mocks.add(mock1);

        BeanPropertySetter beanPropertySetter = mock(BeanPropertySetter.class);
        when(beanPropertySetter.set(mock1)).thenReturn(false);
        whenNew(BeanPropertySetter.class).withArguments(fieldInstance, field).thenReturn(beanPropertySetter);

        FieldSetter fieldSetter = mock(FieldSetter.class);
        whenNew(FieldSetter.class).withArguments(fieldInstance, field).thenReturn(fieldSetter);

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(injecter);

        Object result = injecter.thenInject();

        assertSame(mock1, result);
        verify(beanPropertySetter).set(mock1);
        verify(fieldSetter).set(mock1);
    }

    @Test
    public void testFilterCandidateWithNoMatch() {
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(injecter);

        Object result = injecter.thenInject();

        assertNull(result);
        verify(field, times(0)).setAccessible(true);
    }
}