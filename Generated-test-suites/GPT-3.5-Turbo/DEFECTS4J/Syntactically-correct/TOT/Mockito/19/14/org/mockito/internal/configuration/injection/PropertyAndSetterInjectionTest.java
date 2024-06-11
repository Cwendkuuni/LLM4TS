import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

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

public class PropertyAndSetterInjectionTest {

    private PropertyAndSetterInjection injectionStrategy;
    private MockCandidateFilter mockCandidateFilter;
    private ListUtil.Filter<Field> notFinalOrStatic;

    @Before
    public void setUp() {
        injectionStrategy = new PropertyAndSetterInjection();
        mockCandidateFilter = new TypeBasedCandidateFilter(new NameBasedCandidateFilter(new FinalMockCandidateFilter()));
        notFinalOrStatic = new ListUtil.Filter<Field>() {
            public boolean isOut(Field object) {
                return Modifier.isFinal(object.getModifiers()) || Modifier.isStatic(object.getModifiers());
            }
        };
    }

    @Test
    public void testProcessInjection_SuccessfulInjection() {
        // Arrange
        class ClassA {
            private ClassB classB;
        }
        
        class ClassB {
            // empty
        }
        
        ClassA classA = mock(ClassA.class);
        Set<Object> mockCandidates = new HashSet<>();
        mockCandidates.add(classA);
        ClassB classB = new ClassB();
        mockCandidates.add(classB);
        
        Field injectMocksField = ClassA.class.getDeclaredField("classB");
        Object injectMocksFieldOwner = classA;
        
        // Act
        boolean result = injectionStrategy.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);
        
        // Assert
        assertTrue(result);
        verify(classA).setClassB(classB);
    }

    @Test
    public void testProcessInjection_FailedInjection() {
        // Arrange
        class ClassA {
            private ClassB classB;
        }
        
        class ClassB {
            // empty
        }
        
        ClassA classA = mock(ClassA.class);
        Set<Object> mockCandidates = new HashSet<>();
        mockCandidates.add(classA);
        
        Field injectMocksField = ClassA.class.getDeclaredField("classB");
        Object injectMocksFieldOwner = classA;
        
        // Act
        boolean result = injectionStrategy.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);
        
        // Assert
        assertFalse(result);
        verify(classA, never()).setClassB(any(ClassB.class));
    }

    @Test
    public void testInitializeInjectMocksField_SuccessfulInitialization() throws NoSuchFieldException {
        // Arrange
        class ClassA {
            private ClassB classB;
        }
        
        class ClassB {
            // empty
        }
        
        ClassA classA = new ClassA();
        Field field = ClassA.class.getDeclaredField("classB");
        
        // Act
        FieldInitializationReport report = injectionStrategy.initializeInjectMocksField(field, classA);
        
        // Assert
        assertNotNull(report);
        assertEquals(ClassB.class, report.fieldClass());
        assertNull(report.fieldInstance());
    }

    @Test(expected = MockitoException.class)
    public void testInitializeInjectMocksField_ThrowException() throws NoSuchFieldException {
        // Arrange
        class ClassA {
            private ClassB classB;
        }
        
        class ClassB {
            // empty
        }
        
        ClassA classA = new ClassA();
        Field field = ClassA.class.getDeclaredField("classB");
        
        // Act
        injectionStrategy.initializeInjectMocksField(field, classA);
    }

    @Test
    public void testInjectMockCandidates_SuccessfulInjection() {
        // Arrange
        class ClassA {
            private ClassB classB;
        }
        
        class ClassB {
            private ClassC classC;
        }
        
        class ClassC {
            // empty
        }
        
        ClassA classA = mock(ClassA.class);
        Set<Object> mocks = new HashSet<>();
        ClassB classB = new ClassB();
        mocks.add(classA);
        mocks.add(classB);
        ClassC classC = new ClassC();
        mocks.add(classC);
        
        List<Field> orderedInstanceFields = new ArrayList<>();
        orderedInstanceFields.add(ClassA.class.getDeclaredField("classB"));
        
        // Act
        boolean result = injectionStrategy.injectMockCandidatesOnFields(mocks, classA, false, orderedInstanceFields);
        
        // Assert
        assertTrue(result);
        verify(classA).setClassB(classB);
        assertFalse(mocks.contains(classB));
        assertFalse(orderedInstanceFields.contains(ClassA.class.getDeclaredField("classB")));
    }

    @Test
    public void testInjectMockCandidates_FailedInjection() {
        // Arrange
        class ClassA {
            private ClassB classB;
        }
        
        class ClassB {
            // empty
        }
        
        ClassA classA = mock(ClassA.class);
        Set<Object> mocks = new HashSet<>();
        ClassB classB = new ClassB();
        mocks.add(classA);
        mocks.add(classB);
        
        List<Field> orderedInstanceFields = new ArrayList<>();
        orderedInstanceFields.add(ClassA.class.getDeclaredField("classB"));
        
        // Act
        boolean result = injectionStrategy.injectMockCandidatesOnFields(mocks, classA, false, orderedInstanceFields);
        
        // Assert
        assertFalse(result);
        verify(classA, never()).setClassB(any(ClassB.class));
        assertTrue(mocks.contains(classB));
        assertTrue(orderedInstanceFields.contains(ClassA.class.getDeclaredField("classB")));
    }

    @Test
    public void testOrderedInstanceFieldsFrom_SuccessfulSorting() throws NoSuchFieldException {
        // Arrange
        class ClassA {
            private ClassB classB;
        }
        
        class ClassB {
            private ClassC classC;
        }
        
        class ClassC {
            // empty
        }
        
        ClassA classA = new ClassA();
        
        // Act
        List<Field> result = injectionStrategy.orderedInstanceFieldsFrom(classA.getClass());
        
        // Assert
        assertEquals(2, result.size());
        assertEquals(ClassC.class, result.get(0).getType());
        assertEquals(ClassB.class, result.get(1).getType());
    }

    @Test
    public void testOrderedInstanceFieldsFrom_EmptyList() throws NoSuchFieldException {
        // Arrange
        class ClassA {
            // empty
        }
        
        ClassA classA = new ClassA();
        
        // Act
        List<Field> result = injectionStrategy.orderedInstanceFieldsFrom(classA.getClass());
        
        // Assert
        assertEquals(0, result.size());
    }

}