import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.PropertyAndSetterInjection;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.util.collections.ListUtil;
import org.mockito.internal.util.reflection.FieldInitializationReport;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.internal.util.reflection.SuperTypesLastSorter;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.internal.util.collections.Sets.newMockSafeHashSet;

public class PropertyAndSetterInjectionTest {

    private PropertyAndSetterInjection injectionStrategy;
    private MockCandidateFilter mockCandidateFilter;

    @Before
    public void setup() {
        mockCandidateFilter = new TypeBasedCandidateFilter(new NameBasedCandidateFilter(new FinalMockCandidateFilter()));
        injectionStrategy = new PropertyAndSetterInjection();
    }

    @Test
    public void processInjection_WithInjectMocksField_InjectsMocksAndReturnsTrue() {
        // Arrange
        TestClass testClass = new TestClass();
        Set<Object> mockCandidates = new HashSet<>();
        mockCandidates.add(new Dependency1());
        mockCandidates.add(new Dependency2());

        try {
            Field injectMocksField = TestClass.class.getDeclaredField("dependency");
            FieldInitializationReport report = new FieldInitializationReport(TestClass.class, testClass.dependency);
            FieldInitializer initializer = new FieldInitializer(testClass, injectMocksField);
            initializer.initialize();

            // Act
            boolean result = injectionStrategy.processInjection(injectMocksField, testClass, mockCandidates);

            // Assert
            assertThat(result).isTrue();
            assertThat(testClass.getDependency()).isNotNull();
            assertThat(testClass.getDependency()).isInstanceOf(Dependency1.class);

        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void processInjection_WithNoInjectMocksField_DoesNotInjectMocksAndReturnsFalse() {
        // Arrange
        TestClass testClass = new TestClass();
        Set<Object> mockCandidates = new HashSet<>();
        mockCandidates.add(new Dependency1());
        mockCandidates.add(new Dependency2());

        try {
            Field injectMocksField = TestClass.class.getDeclaredField("noDependency");
            FieldInitializationReport report = new FieldInitializationReport(TestClass.class, testClass.dependency);
            FieldInitializer initializer = new FieldInitializer(testClass, injectMocksField);
            initializer.initialize();

            // Act
            boolean result = injectionStrategy.processInjection(injectMocksField, testClass, mockCandidates);

            // Assert
            assertThat(result).isFalse();
            assertThat(testClass.getNoDependency()).isNull();

        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void processInjection_WithInvalidInjectMocksField_ThrowsMockitoException() {
        // Arrange
        TestClass testClass = new TestClass();
        Set<Object> mockCandidates = new HashSet<>();
        mockCandidates.add(new Dependency1());
        mockCandidates.add(new Dependency2());

        try {
            Field injectMocksField = TestClass.class.getDeclaredField("invalidDependency");
            FieldInitializationReport report = new FieldInitializationReport(TestClass.class, testClass.dependency);
            FieldInitializer initializer = new FieldInitializer(testClass, injectMocksField);
            initializer.initialize();

            // Act
            Throwable thrown = catchThrowable(() -> {
                injectionStrategy.processInjection(injectMocksField, testClass, mockCandidates);
            });

            // Assert
            assertThat(thrown).isInstanceOf(MockitoException.class);
            assertThat(thrown.getCause()).isInstanceOf(IllegalArgumentException.class);

        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }

    }

    @Test
    public void injectMockCandidates_WithMockCandidates_InjectsMocksAndReturnsTrue() {
        // Arrange
        TestClass testClass = new TestClass();
        Set<Object> mockCandidates = new HashSet<>();
        mockCandidates.add(new Dependency1());
        mockCandidates.add(new Dependency2());

        try {
            Class<?> awaitingInjectionClazz = TestClass.class;
            Object instance = testClass;

            // Act
            boolean result = injectionStrategy.injectMockCandidates(awaitingInjectionClazz, mockCandidates, instance);

            // Assert
            assertThat(result).isTrue();
            assertThat(testClass.getDependency()).isNotNull();
            assertThat(testClass.getDependency()).isInstanceOf(Dependency1.class);

        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void initializeInjectMocksField_WithValidField_ReturnsFieldInitializationReport() {
        // Arrange
        TestClass testClass = new TestClass();
        Field field = null;

        try {
            field = TestClass.class.getDeclaredField("dependency");

            // Act
            FieldInitializationReport report = injectionStrategy.initializeInjectMocksField(field, testClass);

            // Assert
            assertThat(report).isNotNull();
            assertThat(report.fieldClass()).isEqualTo(TestClass.class);
            assertThat(report.fieldInstance()).isEqualTo(testClass.dependency);

        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void initializeInjectMocksField_WithInvalidField_ThrowsMockitoException() {
        // Arrange
        TestClass testClass = new TestClass();
        Field field = null;

        try {
            field = TestClass.class.getDeclaredField("invalidDependency");

            // Act & Assert
            Throwable thrown = catchThrowable(() -> {
                injectionStrategy.initializeInjectMocksField(field, testClass);
            });

            // Assert
            assertThat(thrown).isInstanceOf(MockitoException.class);
            assertThat(thrown.getCause()).isInstanceOf(IllegalArgumentException.class);

        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void injectMockCandidatesOnFields_WithMockCandidates_InjectsMocksAndReturnsTrue() {
        // Arrange
        TestClass testClass = new TestClass();
        Set<Object> mockCandidates = new HashSet<>();
        mockCandidates.add(new Dependency1());
        mockCandidates.add(new Dependency2());

        try {
            Object instance = testClass;
            boolean injectionOccurred = false;
            List<Field> orderedInstanceFields = new ArrayList<>();
            orderedInstanceFields.add(TestClass.class.getDeclaredField("dependency"));

            // Act
            boolean result = injectionStrategy.injectMockCandidatesOnFields(mockCandidates, instance, injectionOccurred, orderedInstanceFields);

            // Assert
            assertThat(result).isTrue();
            assertThat(testClass.getDependency()).isNotNull();
            assertThat(testClass.getDependency()).isInstanceOf(Dependency1.class);

        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void orderedInstanceFieldsFrom_WithTestClass_ReturnsListWithInjectedField() {
        // Arrange
        Class<?> awaitingInjectionClass = TestClass.class;

        // Act
        List<Field> result = injectionStrategy.orderedInstanceFieldsFrom(awaitingInjectionClass);

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("dependency");
    }

    @Test
    public void orderedInstanceFieldsFrom_WithTestClassWithoutInjectedField_ReturnsEmptyList() {
        // Arrange
        Class<?> awaitingInjectionClass = TestClassWithoutInjectedField.class;

        // Act
        List<Field> result = injectionStrategy.orderedInstanceFieldsFrom(awaitingInjectionClass);

        // Assert
        assertThat(result).isEmpty();
    }

}

class TestClass {
    @InjectMocks
    public Dependency1 dependency;
    public Dependency2 noDependency;
    public String invalidDependency;
    
    public Dependency1 getDependency() {
      return dependency;
    }

    public Dependency2 getNoDependency() {
      return noDependency;
    }
    
    public String getInvalidDependency() {
      return invalidDependency;
    }
}

class TestClassWithoutInjectedField {
    public Dependency1 dependency;
    public Dependency2 noDependency;
    public String invalidDependency;
    
    public Dependency1 getDependency() {
      return dependency;
    }

    public Dependency2 getNoDependency() {
      return noDependency;
    }
    
    public String getInvalidDependency() {
      return invalidDependency;
    }
}

class Dependency1 {}

class Dependency2 {}