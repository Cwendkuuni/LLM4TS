import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.MockCandidateFilter;
import org.mockito.internal.configuration.injection.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.TypeBasedCandidateFilter;
import org.mockito.internal.util.reflection.FieldInitializer;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.*;

public class DefaultInjectionEngineTest {

    private DefaultInjectionEngine defaultInjectionEngine;

    @Before
    public void setUp() {
        defaultInjectionEngine = new DefaultInjectionEngine();
    }

    @Test
    public void testInjectMocksOnFields() throws Exception {
        Field field1 = mock(Field.class);
        Field field2 = mock(Field.class);
        Field field3 = mock(Field.class);

        Set<Field> injectMocksFields = new HashSet<>();
        injectMocksFields.add(field1);
        injectMocksFields.add(field2);
        injectMocksFields.add(field3);

        Set<Object> mocks = new HashSet<>();
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        Object mock3 = mock(Object.class);
        mocks.add(mock1);
        mocks.add(mock2);
        mocks.add(mock3);

        Object testClassInstance = mock(Object.class);

        defaultInjectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        verify(field1, times(1)).getType();
        verify(field2, times(1)).getType();
        verify(field3, times(1)).getType();
        verify(field1, times(1)).isAssignableFrom(any(Class.class));
        verify(field2, times(1)).isAssignableFrom(any(Class.class));
        verify(field3, times(1)).isAssignableFrom(any(Class.class));
        verify(mocks, times(3)).remove(any(Object.class));
        verify(mock1, times(1)).setMockBySetter(any(Object.class));
        verify(mock2, times(1)).setMockBySetter(any(Object.class));
        verify(mock3, times(1)).setMockBySetter(any(Object.class));
        verify(mock1, never()).setMockByFieldInjection(any(Object.class));
        verify(mock2, never()).setMockByFieldInjection(any(Object.class));
        verify(mock3, never()).setMockByFieldInjection(any(Object.class));
    }

    @Test
    public void testInjectMocksOnFields_failure() throws Exception {
        Field field = mock(Field.class);

        Set<Field> injectMocksFields = new HashSet<>();
        injectMocksFields.add(field);

        Set<Object> mocks = new HashSet<>();
        Object mock = mock(Object.class);
        mocks.add(mock);

        Object testClassInstance = mock(Object.class);

        defaultInjectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        verify(field, times(1)).getType();
        verify(field, times(1)).isAssignableFrom(any(Class.class));
        verify(mocks, never()).remove(any(Object.class));
        verify(mock, never()).setMockBySetter(any(Object.class));
        verify(mock, never()).setMockByFieldInjection(any(Object.class));
    }

    @Test(expected = MockitoException.class)
    public void testInjectMocksOnFields_exception() throws Exception {
        Field field = mock(Field.class);
        when(field.getName()).thenReturn("fieldName");

        Set<Field> injectMocksFields = new HashSet<>();
        injectMocksFields.add(field);

        Set<Object> mocks = new HashSet<>();
        Object mock = mock(Object.class);
        mocks.add(mock);

        Object testClassInstance = mock(Object.class);

        defaultInjectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        verify(field, times(1)).getType();
        verify(field, times(1)).isAssignableFrom(any(Class.class));
        verify(mocks, never()).remove(any(Object.class));
        verify(mock, never()).setMockBySetter(any(Object.class));
        verify(mock, never()).setMockByFieldInjection(any(Object.class));
    }

    @Test
    public void testInjectMockCandidate() throws Exception {
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        Set<Object> mocks = new HashSet<>();
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        Object mock3 = mock(Object.class);
        mocks.add(mock1);
        mocks.add(mock2);
        mocks.add(mock3);

        defaultInjectionEngine.injectMockCandidate(Object.class, mocks, fieldInstance);

        verify(field, times(1)).getType();
        verify(mocks, times(1)).remove(any(Object.class));
        verify(mock1, times(1)).setMockBySetter(any(Object.class));
        verify(mock2, times(1)).setMockBySetter(any(Object.class));
        verify(mock3, times(1)).setMockBySetter(any(Object.class));
        verify(mock1, never()).setMockByFieldInjection(any(Object.class));
        verify(mock2, never()).setMockByFieldInjection(any(Object.class));
        verify(mock3, never()).setMockByFieldInjection(any(Object.class));
    }

    @Test
    public void testOrderedInstanceFieldsFrom() throws Exception {
        Class<?> awaitingInjectionClazz = Object.class;

        Field[] result = defaultInjectionEngine.orderedInstanceFieldsFrom(awaitingInjectionClazz);

        assertNotNull(result);
        assertEquals(0, result.length);
    }
}