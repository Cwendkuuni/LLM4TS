import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.exceptions.BaseMockitoException;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.util.collections.ListUtil;
import org.mockito.internal.util.reflection.FieldInitializationReport;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.internal.util.reflection.SuperTypesLastSorter;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class PropertyAndSetterInjectionTest {

    private PropertyAndSetterInjection injection;
    private Object mockCandidate1;
    private Object mockCandidate2;
    private Object fieldOwner;

    @Before
    public void setup() {
        injection = new PropertyAndSetterInjection();
        mockCandidate1 = Mockito.mock(String.class);
        mockCandidate2 = Mockito.mock(String.class);
        fieldOwner = Mockito.mock(Object.class);
    }

    @Test
    public void testProcessInjection_noMockFound_shouldReturnFalse() {
        Set<Object> mockCandidates = newHashSet(mockCandidate1, mockCandidate2);
        Field injectMocksField = Mockito.spy(new Field() {
            @Override
            public Class<?> getType() {
                return String.class;
            }
        });

        assertFalse(injection.processInjection(injectMocksField, fieldOwner, mockCandidates));
    }

    @Test
    public void testProcessInjection_mockFoundByType_shouldReturnTrueAndInject() {
        Set<Object> mockCandidates = newHashSet(mockCandidate1, mockCandidate2);
        Field injectMocksField = Mockito.spy(new Field() {
            @Override
            public Class<?> getType() {
                return String.class;
            }
        });
        Whitebox.setInternalState(injectMocksField, "name", "testField");

        when(mockCandidate1.getType()).thenReturn(String.class);
        when(mockCandidate1.getName()).thenReturn("mockCandidate1");

        when(fieldOwner.getClass()).thenReturn(ClassWithStringField.class);

        assertTrue(injection.processInjection(injectMocksField, fieldOwner, mockCandidates));
        verify(mockCandidate1).mockConstruction();
        assertEquals(mockCandidate1, Whitebox.getInternalState(fieldOwner, "testField"));
    }

    @Test(expected = BaseMockitoException.class)
    public void testProcessInjection_mockFoundByName_shouldReturnTrueAndInject() {
        Set<Object> mockCandidates = newHashSet(mockCandidate1, mockCandidate2);
        Field injectMocksField = Mockito.spy(new Field() {
            @Override
            public Class<?> getType() {
                return Object.class;
            }
        });
        Whitebox.setInternalState(injectMocksField, "name", "testField");

        when(mockCandidate1.getName()).thenReturn("testField");
        when(mockCandidate2.getName()).thenReturn("mockCandidate2");
        when(fieldOwner.getClass()).thenReturn(ClassWithStringField.class);

        assertTrue(injection.processInjection(injectMocksField, fieldOwner, mockCandidates));
        verify(mockCandidate1).mockConstruction();
        assertEquals(mockCandidate1, Whitebox.getInternalState(fieldOwner, "testField"));
    }

    @Test(expected = BaseMockitoException.class)
    public void testProcessInjection_moreThanOneMockCandidateByName_shouldFail() {
        Set<Object> mockCandidates = newHashSet(mockCandidate1, mockCandidate2);
        Field injectMocksField = Mockito.spy(new Field() {
            @Override
            public Class<?> getType() {
                return Object.class;
            }
        });
        Whitebox.setInternalState(injectMocksField, "name", "testField");

        when(mockCandidate1.getName()).thenReturn("testField");
        when(mockCandidate2.getName()).thenReturn("testField");
        when(fieldOwner.getClass()).thenReturn(ClassWithTwoStringFields.class);

        assertFalse(injection.processInjection(injectMocksField, fieldOwner, mockCandidates));
    }

    @Test(expected = BaseMockitoException.class)
    public void testProcessInjection_mockNotFound_shouldFail() {
        Set<Object> mockCandidates = newHashSet(mockCandidate1, mockCandidate2);
        Field injectMocksField = Mockito.spy(new Field() {
            @Override
            public Class<?> getType() {
                return String.class;
            }
        });
        Whitebox.setInternalState(injectMocksField, "name", "testField");

        when(mockCandidate1.getType()).thenReturn(Integer.class);
        when(mockCandidate2.getName()).thenReturn("mockCandidate2");
        when(fieldOwner.getClass()).thenReturn(ClassWithStringField.class);

        assertFalse(injection.processInjection(injectMocksField, fieldOwner, mockCandidates));
    }

    @Test
    public void testProcessInjection_noMockToInject_shouldReturnFalse() {
        Set<Object> mockCandidates = newHashSet(mockCandidate1, mockCandidate2);
        Field injectMocksField = Mockito.spy(new Field() {
            @Override
            public Class<?> getType() {
                return Object.class;
            }
        });
        Whitebox.setInternalState(injectMocksField, "name", "testField");

        when(mockCandidate1.getName()).thenReturn("mockCandidate1");
        when(mockCandidate2.getName()).thenReturn("mockCandidate2");
        when(fieldOwner.getClass()).thenReturn(NoInjectableClass.class);

        assertFalse(injection.processInjection(injectMocksField, fieldOwner, mockCandidates));
    }

    @Test
    public void testInitializeInjectMocksField_initializesField() {
        Field injectMocksField = Mockito.spy(new Field() {
            @Override
            public Class<?> getType() {
                return String.class;
            }
        });
        Whitebox.setInternalState(injectMocksField, "name", "testField");

        when(fieldOwner.getClass()).thenReturn(ClassWithStringField.class);

        FieldInitializationReport report = injection.initializeInjectMocksField(injectMocksField, fieldOwner);
        assertNotNull(report);
        assertEquals(String.class, report.fieldClass());
        assertNotNull(report.fieldInstance());
    }

    @Test(expected = MockitoException.class)
    public void testInitializeInjectMocksField_initializationFailed() {
        Field injectMocksField = Mockito.spy(new Field() {
            @Override
            public Class<?> getType() {
                return String.class;
            }
        });
        Whitebox.setInternalState(injectMocksField, "name", "testField");

        when(fieldOwner.getClass()).thenReturn(ClassWithStringField.class);

        try {
            injection.initializeInjectMocksField(injectMocksField, fieldOwner);
            fail();
        } catch (MockitoException e) {
            //expected
        }
    }

    @Test(expected = InvocationTargetException.class)
    public void testInitializeInjectMocksField_constructorInitializationFailed() {
        Field injectMocksField = Mockito.spy(new Field() {
            @Override
            public Class<?> getType() {
                return ExceptionThrowingClass.class;
            }
        });
        Whitebox.setInternalState(injectMocksField, "name", "testField");

        when(fieldOwner.getClass()).thenReturn(ExceptionThrowingClass.class);

        try {
            injection.initializeInjectMocksField(injectMocksField, fieldOwner);
            fail();
        } catch (InvocationTargetException e) {
            //expected
        }
    }

    @Test
    public void testInjectMockCandidates_noMockToInject_shouldReturnFalse() {
        Class<?> awaitingInjectionClazz = ClassWithNoInjectableField.class;
        Set<Object> mocks = newHashSet(mockCandidate1, mockCandidate2);
        Object instance = Mockito.mock(ClassWithNoInjectableField.class);

        assertFalse(injection.injectMockCandidates(awaitingInjectionClazz, mocks, instance));
    }

    @Test
    public void testInjectMockCandidates_mockFoundByType_shouldReturnTrueAndInject() {
        Class<ClassWithStringField> awaitingInjectionClazz = ClassWithStringField.class;
        Set<Object> mocks = newHashSet(mockCandidate1, mockCandidate2);
        Object instance = Mockito.mock(ClassWithStringField.class);

        when(mockCandidate1.getType()).thenReturn(String.class);

        assertTrue(injection.injectMockCandidates(awaitingInjectionClazz, mocks, instance));
        verify(mockCandidate1).mockConstruction();
        assertEquals(mockCandidate1, instance.getClass().getDeclaredField("testField").get(instance));
    }

    @Test
    public void testInjectMockCandidates_mockFoundByName_shouldReturnTrueAndInject() {
        Class<ClassWithTwoStringFields> awaitingInjectionClazz = ClassWithTwoStringFields.class;
        Set<Object> mocks = newHashSet(mockCandidate1, mockCandidate2);
        Object instance = Mockito.mock(ClassWithTwoStringFields.class);

        when(mockCandidate1.getName()).thenReturn("testField1");

        assertTrue(injection.injectMockCandidates(awaitingInjectionClazz, mocks, instance));
        verify(mockCandidate1).mockConstruction();
        assertEquals(mockCandidate1, instance.getClass().getDeclaredField("testField1").get(instance));
    }

    @Test
    public void testInjectMockCandidates_noMockFound_shouldReturnFalse() {
        Class<ClassWithNoInjectableField> awaitingInjectionClazz = ClassWithNoInjectableField.class;
        Set<Object> mocks = newHashSet(mockCandidate1, mockCandidate2);
        Object instance = Mockito.mock(ClassWithNoInjectableField.class);

        assertFalse(injection.injectMockCandidates(awaitingInjectionClazz, mocks, instance));
    }

    @Test
    public void testInjectMockCandidatesOnFields_noMockToInject_shouldReturnFalse() {
        List<Field> orderedInstanceFields = Arrays.asList(new Field() {}, new Field() {});

        assertFalse(injection.injectMockCandidatesOnFields(Mockito.mock(Set.class), Mockito.mock(Object.class), false, orderedInstanceFields));
    }

    @Test
    public void testInjectMockCandidatesOnFields_mockFoundByType_shouldReturnTrueAndInject() {
        List<Field> orderedInstanceFields = Arrays.asList(new FieldWithStringMock() {}.getDeclaredField("mockField"), new FieldWithStringMock() {}.getDeclaredField("mockField2"));
        Set<Object> mocks = newHashSet(mockCandidate1, mockCandidate2);
        Object instance = Mockito.mock(ClassWithTwoStringFields.class);

        when(mockCandidate1.getType()).thenReturn(String.class);

        assertTrue(injection.injectMockCandidatesOnFields(mocks, instance, false, orderedInstanceFields));
        verify(mockCandidate1).mockConstruction();
        assertEquals(mockCandidate1, instance.getClass().getDeclaredField("testField1").get(instance));
    }

    @Test
    public void testInjectMockCandidatesOnFields_mockFoundByName_shouldReturnTrueAndInject() {
        List<Field> orderedInstanceFields = Arrays.asList(new FieldWithStringMock() {}.getDeclaredField("mockField1"), new FieldWithStringMock() {}.getDeclaredField("mockField2"));
        Set<Object> mocks = newHashSet(mockCandidate1, mockCandidate2);
        Object instance = Mockito.mock(ClassWithTwoStringFields.class);

        when(mockCandidate1.getName()).thenReturn("testField1");

        assertTrue(injection.injectMockCandidatesOnFields(mocks, instance, false, orderedInstanceFields));
        verify(mockCandidate1).mockConstruction();
        assertEquals(mockCandidate1, instance.getClass().getDeclaredField("testField1").get(instance));
    }

    @Test
    public void testInjectMockCandidatesOnFields_noMockFound_shouldReturnFalse() {
        List<Field> orderedInstanceFields = Arrays.asList(new Field() {}, new Field() {});
        Set<Object> mocks = newHashSet(mockCandidate1, mockCandidate2);
        Object instance = Mockito.mock(ClassWithNoInjectableField.class);

        assertFalse(injection.injectMockCandidatesOnFields(mocks, instance, false, orderedInstanceFields));
    }

    private static class ClassWithStringField {
        @InjectMocks
        private String testField;
    }

    private static class ClassWithTwoStringFields {
        @InjectMocks
        private String testField1;
        @InjectMocks
        private String testField2;
    }

    private static class ClassWithNoInjectableField {
        // No @InjectMocks annotations
    }

    private static class FieldWithStringMock {
        @InjectMocks
        private String mockField;

        private Field getDeclaredField(String fieldName) {
            return this.getClass().getDeclaredField(fieldName);
        }
    }

    private static Set<Object> newHashSet(Object... objects) {
        return new LinkedHashSet<>(Arrays.asList(objects));
    }
}