import org.junit.Test;
import org.mockito.exceptions.MockitoException;
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

    @Test
    public void testInjectMocksOnFields() {
        // Setup
        DefaultInjectionEngine engine = new DefaultInjectionEngine();
        Set<Field> injectMocksFields = new HashSet<>();
        Set<Object> mocks = new HashSet<>();
        Object testClassInstance = new Object();

        // Execute
        engine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        // Verify
        // TODO: Add verification code here
    }

    @Test
    public void testInjectMockCandidate() {
        // Setup
        DefaultInjectionEngine engine = new DefaultInjectionEngine();
        Class<?> awaitingInjectionClazz = Object.class;
        Set<Object> mocks = new HashSet<>();
        Object fieldInstance = new Object();

        // Execute
        engine.injectMockCandidate(awaitingInjectionClazz, mocks, fieldInstance);

        // Verify
        // TODO: Add verification code here
    }

    @Test
    public void testOrderedInstanceFieldsFrom() {
        // Setup
        DefaultInjectionEngine engine = new DefaultInjectionEngine();
        Class<?> awaitingInjectionClazz = Object.class;

        // Execute
        Field[] fields = engine.orderedInstanceFieldsFrom(awaitingInjectionClazz);

        // Verify
        // TODO: Add verification code here
    }
}