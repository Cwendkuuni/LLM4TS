import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.DefaultInjectionEngine;
import org.mockito.internal.configuration.DefaultInjectionEngine.TestClassInstance;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.TypeBasedCandidateFilter;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.internal.configuration.injection.MockCandidateFilter;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.*;
import static org.junit.Assert.*;

public class DefaultInjectionEngineTest {
    private DefaultInjectionEngine defaultInjectionEngine;
    private TestClassInstance testClassInstance;
    private Set<Object> mocks;
    private Set<Field> injectMocksFields;
    private Field fieldToMock;

    @Before
    public void setup() {
        defaultInjectionEngine = new DefaultInjectionEngine();
        mocks = new HashSet<>();
        testClassInstance = new TestClassInstance();
        injectMocksFields = new HashSet<Field>() {{
            add(testClassInstance.getClass().getField("fieldToMock"));
        }};
        fieldToMock = testClassInstance.getClass().getField("fieldToMock");
        initMocks(testClassInstance);
    }

    @Test
    public void testInjectMocksOnFields() {
        defaultInjectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);
        verify(mocks).sizeIsAtLeast(1);
    }

    @Test
    public void testInjectMockCandidate() {
        Class<?> awaitingInjectionClazz = FieldUtils.getClass(fieldToMock.getType());
        defaultInjectionEngine.injectMockCandidate(awaitingInjectionClazz, mocks, testClassInstance);
        verify(mocks).contains(any());
    }

    @Test
    public void testOrderedInstanceFieldsFrom() {
        Class<?> targetClass = testClassInstance.getClass();
        Field[] fields = defaultInjectionEngine.orderedInstanceFieldsFrom(targetClass);
        assertEquals(1, fields.length);
        assertEquals(fieldToMock, fields[0]);
    }
}