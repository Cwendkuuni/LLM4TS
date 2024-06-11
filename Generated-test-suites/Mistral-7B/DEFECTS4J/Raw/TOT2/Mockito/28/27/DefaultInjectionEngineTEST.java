import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.DefaultInjectionEngine;
import org.mockito.internal.configuration.injection.MockCandidateFilter;
import org.mockito.internal.configuration.injection.TypeBasedCandidateFilter;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;
import org.junit.Assert;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class DefaultInjectionEngineTest {

    private DefaultInjectionEngine defaultInjectionEngine;
    private Set<Object> mocks;
    private Object testClassInstance;
    private Set<Field> injectMocksFields;

    @Before
    public void setup() {
        defaultInjectionEngine = new DefaultInjectionEngine();
        mocks = new HashSet<>();
        testClassInstance = Mockito.mock(Object.class);
        injectMocksFields = Arrays.asList(new Field[]{new Field(){}, new Field(){}}).stream().collect(Collectors.toCollection(HashSet::new));
    }

    @Test
    public void testInjectMocksOnFields() {
        // Given
        Object mock1 = Mockito.mock(Class1.class);
        Object mock2 = Mockito.mock(Class2.class);
        mocks.add(mock1);
        mocks.add(mock2);

        // When
        defaultInjectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        // Then
        for (Field field : injectMocksFields) {
            assertNotNull(field.get(testClassInstance));
        }
    }

    @Test(expected = MockitoException.class)
    public void testExceptionWhenCannotInitializeInjectMocksField() {
        // Given
        Set<Field> injectMocksFields = Arrays.asList(new Field(){
            @InjectMocks
            public Object injectMocksField;
        }).stream().collect(Collectors.toCollection(HashSet::new));

        // When and Then
        defaultInjectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);
    }

    @Test
    public void testNoMockCandidateFound() {
        // Given
        Set<Field> injectMocksFields = Arrays.asList(new Field[]{new Field(){
            @InjectMocks
            public Object injectMocksField;
        }, new Field(){
            @InjectMocks
            public Object anotherInjectMocksField;
        }}).stream().collect(Collectors.toCollection(HashSet::new));
        Class<?> awaitingInjectionClazz = Object.class;

        // When
        defaultInjectionEngine.injectMockCandidate(awaitingInjectionClazz, mocks, testClassInstance);

        // Then
        assertFalse(mocks.isEmpty());
    }

    @Test
    public void testMultipleMockCandidates() {
        // Given
        Set<Field> injectMocksFields = Arrays.asList(new Field[]{
                new Field(){
                    @InjectMocks
                    public Object injectMocksField;
                },
                new Field(){
                    @InjectMocks
                    public Object anotherInjectMocksField;
                }
        }).stream().collect(Collectors.toCollection(HashSet::new));
        Class<?> awaitingInjectionClazz = Class1.class;
        Object fieldInstance = Mockito.mock(Class3.class);

        // When
        defaultInjectionEngine.injectMockCandidate(awaitingInjectionClazz, mocks, fieldInstance);

        // Then
        // Here we would check that the mock with the name closest to the injected field name is injected
    }

    @Test
    public void testNoMockCandidateAndUserProvidesDependencies() {
        // Given
        Set<Field> injectMocksFields = Arrays.asList(new Field(){
            @InjectMocks
            public Object injectMocksField;
        }).stream().collect(Collectors.toCollection(HashSet::new));
        Class<?> awaitingInjectionClazz = Class1.class;
        Object fieldInstance = Mockito.mock(Class1.class);

        // When
        doAnswer(invocation -> {
            Field field = invocation.getArgument(1);
            Object mock = Mockito.mock(field.getType());
            mocks.add(mock);
            field.set(fieldInstance, mock);
            return null;
        }).when(defaultInjectionEngine).injectMockByField(awaitingInjectionClazz, mocks, fieldInstance);

        defaultInjectionEngine.injectMockCandidate(awaitingInjectionClazz, mocks, fieldInstance);

        // Then
        // Here we would check that the field is set with the mocked instance
    }
}