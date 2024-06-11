import org.junit.Test;
import org.mockito.exceptions.Reporter;

import java.util.HashSet;
import java.util.Set;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;

public class DefaultInjectionEngineTest {

    @Test
    public void testInjectMocksOnFields() {
        DefaultInjectionEngine injectionEngine = new DefaultInjectionEngine();
        Set<Field> injectMocksFields = new HashSet<>();
        Set<Object> mocks = new HashSet<>();
        Object testClassInstance = new Object();

        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);
    }

    @Test
    public void testInjectMockCandidate() {
        DefaultInjectionEngine injectionEngine = new DefaultInjectionEngine();
        Class<?> awaitingInjectionClazz = Object.class;
        Set<Object> mocks = new HashSet<>();
        Object fieldInstance = new Object();

        injectionEngine.injectMockCandidate(awaitingInjectionClazz, mocks, fieldInstance);
    }

    @Test
    public void testOrderedInstanceFieldsFrom() {
        DefaultInjectionEngine injectionEngine = new DefaultInjectionEngine();
        Class<?> awaitingInjectionClazz = Object.class;

        injectionEngine.orderedInstanceFieldsFrom(awaitingInjectionClazz);
    }
}