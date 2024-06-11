import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.MockCandidateFilter;
import org.mockito.internal.configuration.injection.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.TypeBasedCandidateFilter;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.exceptions.Reporter;
import org.mockito.exceptions.base.MockitoException;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.*;

public class DefaultInjectionEngineTest {

    private DefaultInjectionEngine defaultInjectionEngine;

    @Before
    public void setup() {
        defaultInjectionEngine = new DefaultInjectionEngine();
    }

    @Test
    public void testInjectMocksOnFields() throws Exception {
        Set<Field> injectMocksFields = new HashSet<Field>();
        Set<Object> mocks = new HashSet<Object>();
        Object testClassInstance = new Object();

        Field mockField = DefaultInjectionEngine.class.getDeclaredField("mockCandidateFilter");
        injectMocksFields.add(mockField);

        FieldInitializer fieldInitializer = mock(FieldInitializer.class);
        whenNew(FieldInitializer.class).withArguments(testClassInstance, mockField).thenReturn(fieldInitializer);
        when(fieldInitializer.initialize()).thenReturn(null);

        defaultInjectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        verify(fieldInitializer).initialize();
    }

    @Test
    public void testInjectMockCandidate() throws Exception {
        Class<?> awaitingInjectionClazz = Object.class;
        Set<Object> mocks = new HashSet<Object>();
        Object fieldInstance = new Object();

        Field field1 = awaitingInjectionClazz.getDeclaredField("field1");
        Field field2 = awaitingInjectionClazz.getDeclaredField("field2");

        MockCandidateFilter mockCandidateFilter = mock(MockCandidateFilter.class);
        whenNew(MockCandidateFilter.class).withNoArguments().thenReturn(mockCandidateFilter);
        when(mockCandidateFilter.filterCandidate(mocks, field1, fieldInstance)).thenReturn(mockCandidateFilter);
        when(mockCandidateFilter.filterCandidate(mocks, field2, fieldInstance)).thenReturn(mockCandidateFilter);
        when(mockCandidateFilter.thenInject()).thenReturn(true);

        defaultInjectionEngine.injectMockCandidate(awaitingInjectionClazz, mocks, fieldInstance);

        verify(mockCandidateFilter, times(2)).filterCandidate(mocks, field1, fieldInstance);
        verify(mockCandidateFilter).filterCandidate(mocks, field2, fieldInstance);
        verify(mockCandidateFilter, times(2)).thenInject();
    }

    @Test
    public void testOrderedInstanceFieldsFrom() throws Exception {
        Class<?> awaitingInjectionClazz = Object.class;

        Field[] declaredFields = new Field[2];
        declaredFields[0] = awaitingInjectionClazz.getDeclaredField("field1");
        declaredFields[1] = awaitingInjectionClazz.getDeclaredField("field2");

        Comparator<Field> supertypesLast = mock(Comparator.class);
        whenNew(Comparator.class).withNoArguments().thenReturn(supertypesLast);
        when(awaitingInjectionClazz.getDeclaredFields()).thenReturn(declaredFields);
        when(supertypesLast.compare(declaredFields[1], declaredFields[0])).thenReturn(1);

        Field[] expectedFields = new Field[2];
        expectedFields[0] = declaredFields[1];
        expectedFields[1] = declaredFields[0];

        Field[] orderedFields = defaultInjectionEngine.orderedInstanceFieldsFrom(awaitingInjectionClazz);

        assertArrayEquals(expectedFields, orderedFields);
    }

}