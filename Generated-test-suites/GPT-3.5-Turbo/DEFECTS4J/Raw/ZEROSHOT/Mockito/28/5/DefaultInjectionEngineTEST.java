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
    private MockCandidateFilter mockCandidateFilter;
    private Comparator<Field> supertypesLast;

    @Before
    public void setUp() {
        mockCandidateFilter = mock(MockCandidateFilter.class);
        supertypesLast = mock(Comparator.class);
        defaultInjectionEngine = new DefaultInjectionEngine();
        defaultInjectionEngine.mockCandidateFilter = mockCandidateFilter;
        defaultInjectionEngine.supertypesLast = supertypesLast;
    }

    @Test
    public void testInjectMocksOnFields() {
        Set<Field> injectMocksFields = new HashSet<>();
        Field field = mock(Field.class);
        injectMocksFields.add(field);

        Set<Object> mocks = new HashSet<>();
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        mocks.add(mock1);
        mocks.add(mock2);

        Object testClassInstance = new Object();

        FieldInitializer fieldInitializer = mock(FieldInitializer.class);
        when(fieldInitializer.initialize()).thenReturn(fieldInstance);
        whenNew(FieldInitializer.class).withArguments(testClassInstance, field).thenReturn(fieldInitializer);

        Class<?> fieldClass1 = mock(Class.class);
        Class<?> fieldClass2 = mock(Class.class);
        Class<?> fieldClass3 = mock(Class.class);

        when(fieldInstance.getClass()).thenReturn(fieldClass1, fieldClass2, fieldClass3, Object.class);
        when(fieldClass1.getSuperclass()).thenReturn(fieldClass2);
        when(fieldClass2.getSuperclass()).thenReturn(fieldClass3);
        when(fieldClass3.getSuperclass()).thenReturn(Object.class);

        Field[] orderedFields = new Field[]{mock(Field.class), mock(Field.class), mock(Field.class)};
        when(defaultInjectionEngine.orderedInstanceFieldsFrom(fieldClass1)).thenReturn(orderedFields);
        when(mockCandidateFilter.filterCandidate(mocks, orderedFields[0], fieldInstance)).thenReturn(mockCandidateFilter);
        when(mockCandidateFilter.filterCandidate(mocks, orderedFields[1], fieldInstance)).thenReturn(mockCandidateFilter);
        when(mockCandidateFilter.filterCandidate(mocks, orderedFields[2], fieldInstance)).thenReturn(mockCandidateFilter);
        when(mockCandidateFilter.thenInject()).thenReturn(mockCandidateFilter);

        defaultInjectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        verify(fieldInitializer).initialize();
        verify(mockCandidateFilter, times(3)).filterCandidate(mocks, field, fieldInstance);
        verify(mockCandidateFilter, times(3)).thenInject();
    }

    @Test
    public void testInjectMockCandidate() {
        Class<?> awaitingInjectionClazz = mock(Class.class);
        Set<Object> mocks = new HashSet<>();
        Object fieldInstance = mock(Object.class);

        Field field1 = mock(Field.class);
        Field field2 = mock(Field.class);
        Field[] orderedFields = new Field[]{field1, field2};

        when(defaultInjectionEngine.orderedInstanceFieldsFrom(awaitingInjectionClazz)).thenReturn(orderedFields);
        when(mockCandidateFilter.filterCandidate(mocks, field1, fieldInstance)).thenReturn(mockCandidateFilter);
        when(mockCandidateFilter.filterCandidate(mocks, field2, fieldInstance)).thenReturn(mockCandidateFilter);
        when(mockCandidateFilter.thenInject()).thenReturn(mockCandidateFilter);

        defaultInjectionEngine.injectMockCandidate(awaitingInjectionClazz, mocks, fieldInstance);

        verify(mockCandidateFilter).filterCandidate(mocks, field1, fieldInstance);
        verify(mockCandidateFilter).filterCandidate(mocks, field2, fieldInstance);
        verify(mockCandidateFilter, times(2)).thenInject();
    }

    @Test
    public void testOrderedInstanceFieldsFrom() {
        Class<?> awaitingInjectionClazz = mock(Class.class);

        Field field1 = mock(Field.class);
        Field field2 = mock(Field.class);
        Field[] declaredFields = new Field[]{field1, field2};

        when(awaitingInjectionClazz.getDeclaredFields()).thenReturn(declaredFields);
        when(supertypesLast.compare(field1, field2)).thenReturn(-1);

        Field[] orderedFields = defaultInjectionEngine.orderedInstanceFieldsFrom(awaitingInjectionClazz);

        assertEquals(2, orderedFields.length);
        assertEquals(field2, orderedFields[0]);
        assertEquals(field1, orderedFields[1]);
    }
}