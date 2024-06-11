import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;

import static org.mockito.Mockito.*;

public class FinalMockCandidateFilterTest {

    @Test
    public void testFilterCandidateWithOneMock() {
        Collection<Object> mocks = Collections.singletonList(mock(Object.class));
        Field field = getField();
        Object fieldInstance = getFieldInstance();

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        filter.filterCandidate(mocks, field, fieldInstance).thenInject();

        verify(new BeanPropertySetter(fieldInstance, field)).set(mocks.get(0));
        verifyNoMoreInteractions(new FieldSetter(fieldInstance, field));
    }

    @Test
    public void testFilterCandidateWithMultipleMocks() {
        Collection<Object> mocks = Arrays.asList(mock(Object.class), mock(Object.class));
        Field field = getField();
        Object fieldInstance = getFieldInstance();

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        filter.filterCandidate(mocks, field, fieldInstance).thenInject();

        verifyZeroInteractions(new BeanPropertySetter(fieldInstance, field));
        verifyZeroInteractions(new FieldSetter(fieldInstance, field));
    }

    @Test
    public void testFilterCandidateWithFailedPropertySetter() {
        Collection<Object> mocks = Collections.singletonList(mock(Object.class));
        Field field = getField();
        Object fieldInstance = getFieldInstance();

        BeanPropertySetter propertySetter = mock(BeanPropertySetter.class);
        when(propertySetter.set(any())).thenReturn(false);

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        filter.filterCandidate(mocks, field, fieldInstance).thenInject();

        verify(propertySetter).set(mocks.get(0));
        verify(new FieldSetter(fieldInstance, field)).set(mocks.get(0));
    }

    @Test
    public void testFilterCandidateWithException() {
        Collection<Object> mocks = Collections.singletonList(mock(Object.class));
        Field field = getField();
        Object fieldInstance = getFieldInstance();

        BeanPropertySetter propertySetter = mock(BeanPropertySetter.class);
        when(propertySetter.set(any())).thenThrow(new RuntimeException());

        Reporter reporter = mock(Reporter.class);
        doNothing().when(reporter).cannotInjectDependency(any(), any(), any());

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter() {
            protected BeanPropertySetter createBeanPropertySetter(Object obj, Field field) {
                return propertySetter;
            }
        };
        filter.filterCandidate(mocks, field, fieldInstance).thenInject();

        verify(propertySetter).set(mocks.get(0));
        verify(reporter).cannotInjectDependency(field, mocks.get(0), any(RuntimeException.class));
    }

    private Field getField() {
        try {
            return TestClass.class.getDeclaredField("field");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Object getFieldInstance() {
        return new TestClass();
    }

    private static class TestClass {
        public Object field;
    }
}