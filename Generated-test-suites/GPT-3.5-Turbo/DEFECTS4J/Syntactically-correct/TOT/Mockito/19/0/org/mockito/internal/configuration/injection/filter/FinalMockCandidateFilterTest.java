import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import static org.mockito.Mockito.*;

public class FinalMockCandidateFilterTest {

    private FinalMockCandidateFilter filter;
    private Field field;
    private Object fieldInstance;

    @Before
    public void setup() {
        filter = new FinalMockCandidateFilter();
        field = mock(Field.class);
        fieldInstance = new Object();
    }

    @Test
    public void testFilterCandidate_OneMock() {
        Collection<Object> mocks = new ArrayList<>();
        Object mock = mock(Object.class);
        mocks.add(mock);

        FinalMockCandidateFilter.OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        Object result = injecter.thenInject();

        verify(mock, never()).setField(any());
        verify(field, never()).set(any(), any());
        assertSame(mock, result);
    }

    @Test
    public void testFilterCandidate_MultipleMocks() {
        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        mocks.add(mock1);
        mocks.add(mock2);

        FinalMockCandidateFilter.OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        Object result = injecter.thenInject();

        verifyNoInteractions(mock1);
        verifyNoInteractions(mock2);
        assertNull(result);
    }

    @Test
    public void testFilterCandidate_ExceptionThrownOnBeanPropertySetter() {
        Collection<Object> mocks = new ArrayList<>();
        Object mock = mock(Object.class);
        mocks.add(mock);

        BeanPropertySetter setter = mock(BeanPropertySetter.class);
        when(setter.set(any())).thenReturn(false);
        doThrow(RuntimeException.class).when(setter).set(any());

        FinalMockCandidateFilter.OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        Object result = injecter.thenInject();

        verify(setter).set(fieldInstance, field);
        verify(field, never()).set(any(), any());
        assertNull(result);
    }

    @Test
    public void testFilterCandidate_ExceptionThrownOnFieldSetter() {
        Collection<Object> mocks = new ArrayList<>();
        Object mock = mock(Object.class);
        mocks.add(mock);

        BeanPropertySetter propertySetter = mock(BeanPropertySetter.class);
        when(propertySetter.set(any())).thenReturn(false);
        when(propertySetter.set(any())).thenReturn(true);

        FieldSetter fieldSetter = mock(FieldSetter.class);
        doThrow(RuntimeException.class).when(fieldSetter).set(any(), any());

        FinalMockCandidateFilter.OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        Object result = injecter.thenInject();

        verify(propertySetter).set(fieldInstance, field);
        verify(fieldSetter).set(fieldInstance, field);
        assertNull(result);
    }

}