import org.junit.*;
import static org.mockito.Mockito.*;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.MockCandidateFilter;
import org.mockito.internal.configuration.injection.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.TypeBasedCandidateFilter;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.exceptions.Reporter;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public class DefaultInjectionEngineTest {

    private DefaultInjectionEngine engine;

    @Before
    public void setUp() {
        engine = new DefaultInjectionEngine();
    }

    @Test
    public void testInjectMocksOnFields_typicalUseCase() {
        // Arrange
        Set<Field> injectMocksFields = new HashSet<>();
        Set<Object> mocks = new HashSet<>();
        Object testClassInstance = new Object();

        Field mockField1 = mock(Field.class);
        Field mockField2 = mock(Field.class);
        Field mockField3 = mock(Field.class);
        injectMocksFields.add(mockField1);
        injectMocksFields.add(mockField2);
        injectMocksFields.add(mockField3);

        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        Object mock3 = mock(Object.class);
        mocks.add(mock1);
        mocks.add(mock2);
        mocks.add(mock3);

        // Act
        engine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        // Assert
        // Verify that all mock candidates are injected correctly
        verify(mockField1).set(mock1, mock1);
        verify(mockField2).set(mock2, mock2);
        verify(mockField3).set(mock3, mock3);
    }

    @Test
    public void testInjectMocksOnFields_edgeCase() {
        // Arrange
        Set<Field> injectMocksFields = new HashSet<>();
        Set<Object> mocks = new HashSet<>();
        Object testClassInstance = new Object();

        Field mockField = mock(Field.class);
        injectMocksFields.add(mockField);

        Object mock = mock(Object.class);
        mocks.add(mock);

        // Act
        engine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        // Assert
        // Verify that the mock candidate is injected correctly
        verify(mockField).set(mock, mock);
    }

    @Test(expected = MockitoException.class)
    public void testInjectMocksOnFields_errorScenario() {
        // Arrange
        Set<Field> injectMocksFields = new HashSet<>();
        Set<Object> mocks = new HashSet<>();
        Object testClassInstance = new Object();

        Field mockField = mock(Field.class);
        injectMocksFields.add(mockField);

        Object mock = mock(Object.class);
        mocks.add(mock);

        doThrow(MockitoException.class).when(mockField).set(mock, mock);

        // Act
        engine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);
    }

    @Test
    public void testInjectMockCandidate_typicalUseCase() {
        // Arrange
        Class<?> awaitingInjectionClazz = Object.class;
        Set<Object> mocks = new HashSet<>();
        Object fieldInstance = new Object();

        Field mockField1 = mock(Field.class);
        Field mockField2 = mock(Field.class);
        Field mockField3 = mock(Field.class);

        mocks.add(mockField1);
        mocks.add(mockField2);
        mocks.add(mockField3);

        MockCandidateFilter filter = new TypeBasedCandidateFilter(new NameBasedCandidateFilter(new FinalMockCandidateFilter()));
        doNothing().when(filter).filterCandidate(mocks, mockField1, fieldInstance);
        doNothing().when(filter).filterCandidate(mocks, mockField2, fieldInstance);
        doNothing().when(filter).filterCandidate(mocks, mockField3, fieldInstance);

        engine.mockCandidateFilter = filter;

        // Act
        engine.injectMockCandidate(awaitingInjectionClazz, mocks, fieldInstance);

        // Assert
        // Verify that all mock candidates are injected correctly
        verify(filter).filterCandidate(mocks, mockField1, fieldInstance);
        verify(filter).filterCandidate(mocks, mockField2, fieldInstance);
        verify(filter).filterCandidate(mocks, mockField3, fieldInstance);
    }

    @Test
    public void testOrderedInstanceFieldsFrom_typicalUseCase() {
        // Arrange
        Class<?> awaitingInjectionClazz = Object.class;

        Field field1 = mock(Field.class);
        Field field2 = mock(Field.class);
        Field field3 = mock(Field.class);

        awaitingInjectionClazz.declaredFields = new Field[] { field1, field2, field3 };

        Comparator<Field> supertypesLast = new Comparator<Field>() {
            public int compare(Field field1, Field field2) {
                Class<?> field1Type = field1.getType();
                Class<?> field2Type = field2.getType();

                if(field1Type.isAssignableFrom(field2Type)) {
                    return 1;
                }
                if(field2Type.isAssignableFrom(field1Type)) {
                    return -1;
                }
                return 0;
            }
        };

        engine.supertypesLast = supertypesLast;

        // Act
        Field[] orderedFields = engine.orderedInstanceFieldsFrom(awaitingInjectionClazz);

        // Assert
        // Verify that the fields are ordered correctly based on supertypes last
        Assert.assertArrayEquals(new Field[] { field3, field2, field1 }, orderedFields);
    }

}