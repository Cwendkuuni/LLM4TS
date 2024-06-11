
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DefaultInjectionEngineTest {

    private DefaultInjectionEngine injectionEngine;

    @Before
    public void setup() {
        injectionEngine = new DefaultInjectionEngine();
    }

    @Test
    public void testInjectMocksOnFields_WithValidFields_SetsMocksAndInjects() throws NoSuchFieldException, IllegalAccessException {
        // Arrange
        Set<Object> mocks = new HashSet<>();
        Field field = DefaultInjectionEngine.class.getDeclaredField("mockCandidateFilter");
        Set<Field> injectMocksFields = new HashSet<>();
        injectMocksFields.add(field);
        Object testClassInstance = this;

        // Act
        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        // Assert
        assertEquals(1, mocks.size());
        assertTrue(mocks.contains(injectionEngine.mockCandidateFilter));
        verify(field).setAccessible(true);
        verify(field).set(testClassInstance, injectionEngine.mockCandidateFilter);
    }

    @Test(expected = Reporter.class)
    public void testInjectMocksOnFields_WithInvalidFields_ThrowsReporterException() throws NoSuchFieldException, IllegalAccessException {
        // Arrange
        Set<Object> mocks = new HashSet<>();
        Field field = DefaultInjectionEngine.class.getDeclaredField("mockCandidateFilter");
        field.setAccessible(false); // Make the field inaccessible
        Set<Field> injectMocksFields = new HashSet<>();
        injectMocksFields.add(field);
        Object testClassInstance = this;

        // Act
        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        // Assert
        // Expecting Reporter exception to be thrown
    }

    @Test
    public void testInjectMocksOnFields_WithEmptyMocks_SkipInjection() throws NoSuchFieldException, IllegalAccessException {
        // Arrange
        Field field = DefaultInjectionEngine.class.getDeclaredField("mockCandidateFilter");
        Set<Field> injectMocksFields = new HashSet<>();
        injectMocksFields.add(field);
        Object testClassInstance = this;

        // Act
        injectionEngine.injectMocksOnFields(injectMocksFields, new HashSet<>(), testClassInstance);

        // Assert
        verify(field).setAccessible(true);
    }

    @Test
    public void testInjectMocksOnFields_WithMultipleFields_InjectsInCorrectOrderAndRemovesMocks() throws NoSuchFieldException, IllegalAccessException {
        // Arrange
        Set<Object> mocks = new HashSet<>();
        Field field1 = DefaultInjectionEngine.class.getDeclaredField("mockCandidateFilter");
        Field field2 = DefaultInjectionEngine.class.getDeclaredField("supertypesLast");
        Set<Field> injectMocksFields = new HashSet<>();
        injectMocksFields.add(field1);
        injectMocksFields.add(field2);
        Object testClassInstance = this;

        // Act
        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        // if field2 is a subtype of field1
        // then field2 should be injected first and removed from mocks
        verify(field2).setAccessible(true);
        verify(field2).set(testClassInstance, injectionEngine.supertypesLast);
        assertEquals(1, mocks.size());
        assertTrue(mocks.contains(injectionEngine.mockCandidateFilter));

        // if field1 is a supertype of field2
        // then field1 should be injected next and removed from mocks
        verify(field1).setAccessible(true);
        verify(field1).set(testClassInstance, injectionEngine.mockCandidateFilter);
        assertEquals(0, mocks.size());
    }
}

