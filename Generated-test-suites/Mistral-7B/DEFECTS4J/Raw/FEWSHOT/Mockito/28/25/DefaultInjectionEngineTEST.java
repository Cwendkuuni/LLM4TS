package org.mockito.internal.configuration;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.MockCandidateFilter;
import org.mockito.internal.configuration.injection.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.TypeBasedCandidateFilter;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class DefaultInjectionEngineTest {

    @Rule
    public Reporter reporter = new Reporter();

    private DefaultInjectionEngine defaultInjectionEngine;
    private Set<Object> mocks;
    private Set<Field> injectMocksFields;
    private Object testClassInstance;

    @Before
    public void setup() {
        defaultInjectionEngine = new DefaultInjectionEngine();
        mocks = new HashSet<>();
        injectMocksFields = new HashSet<>();
        testClassInstance = mock(TestClass.class);
    }

    @Test
    public void testInjectMocksOnFields() {
        Field mockField = new FieldTestClass().getMockField();
        injectMocksFields.add(mockField);

        Object mock = Mockito.spy(new MockClass());
        mocks.add(mock);

        defaultInjectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        verify(testClassInstance).setMockField(mock);

        assertEquals(0, mocks.size());
    }

    @Test(expected = MockitoException.class)
    public void testCannotInitializeForInjectMocksAnnotation() {
        Field mockField = new FieldTestClass().getMockField();
        injectMocksFields.add(mockField);

        Object mock = Mockito.spy(new MockClass());
        mocks.add(mock);

        Field injectMocksField = new FieldTestClass().getInjectMocksField();
        Object injectMocksFieldInstance = new Object();

        Whitebox.setInternalState(injectMocksField, "value", injectMocksFieldInstance);

        defaultInjectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        new Reporter().cannotInitializeForInjectMocksAnnotation(injectMocksField.getName(), new MockitoException(""));
    }

    @Test
    public void testFindMockCandidateByType() {
        Set<Object> mocks = new HashSet<>();
        mocks.add(mock(MockType1.class));
        mocks.add(mock(MockType2.class));
        mocks.add(mock(MockType3.class));

        Class<?> awaitingInjectionClazz = MockType2.class;
        Field mockField = new FieldClass2().getMockField();

        defaultInjectionEngine.injectMockCandidate(awaitingInjectionClazz, mocks, mockField.get(new FieldClass2()));

        VerifyMockIsInjected(mockField.get(new FieldClass2()), mock(MockType2.class).getClass());
    }

    @Test
    public void testFindMockCandidateByName() {
        Set<Object> mocks = new HashSet<>();
        mocks.add(mock(MockType1.class));
        mocks.add(mock(MockType2.class));
        mocks.add(mock(MockType3.class));

        Class<?> awaitingInjectionClazz = MockType2.class;
        Field mockField1 = new FieldClass2().getMockField1();
        Field mockField2 = new FieldClass2().getMockField2();

        defaultInjectionEngine.injectMockCandidate(awaitingInjectionClazz, mocks, mockField1.get(new FieldClass2()));

        assertNull(mockField1.get(new FieldClass2()));

        defaultInjectionEngine.injectMockCandidate(awaitingInjectionClazz, mocks, mockField2.get(new FieldClass2()));

        VerifyMockIsInjected(mockField2.get(new FieldClass2()), mock(MockType2.class).getClass());
    }

    @Test
    public void testSetMockByPropertySetter() {
        Set<Object> mocks = new HashSet<>();
        mocks.add(mock(MockType1.class));
        mocks.add(mock(MockType2.class));

        Class<?> awaitingInjectionClazz = MockType2.class;
        Field mockField = new FieldClass2().getMockField();

        Field field = new FieldClass2().getSetterField();

        defaultInjectionEngine.injectMockCandidate(awaitingInjectionClazz, mocks, mockField.get(new FieldClass2()));

        Object testClassInstance = new Object();
        Whitebox.setInternalState(field, "value", mock(MockType2.class));

        defaultInjectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        VerifyMockIsInjected(mockField.get(new FieldClass2()), mock(MockType2.class).getClass());
    }

    @Test
    public void testSetMockByFieldInjection() {
        Set<Object> mocks = new HashSet<>();
        mocks.add(mock(MockType1.class));
        mocks.add(mock(MockType2.class));

        Class<?> awaitingInjectionClazz = MockType2.class;
        Field mockField = new FieldClass2().getMockField();

        Field field = new FieldClass2().getFieldToInject();

        Whitebox.setInternalState(field, "value", mock(MockType2.class));

        defaultInjectionEngine.injectMockCandidate(awaitingInjectionClazz, mocks, mockField.get(new FieldClass2()));

        VerifyMockIsInjected(mockField.get(new FieldClass2()), mock(MockType2.class).getClass());
    }

    @Test
    public void testDontFailWhenMultipleMockCandidatesFound() {
        Set<Object> mocks = new HashSet<>();
        mocks.add(mock(MockType1.class));
        mocks.add(mock(MockType2.class));
        mocks.add(mock(MockType3.class));

        Class<?> awaitingInjectionClazz = MockType2.class;
        Field mockField = new FieldClass2().getMockField();

        defaultInjectionEngine.injectMockCandidate(awaitingInjectionClazz, mocks, mockField.get(new FieldClass2()));

        Field field = new FieldClass2().getFieldToInject();

        Whitebox.setInternalState(field, "value", mock(MockType3.class));

        defaultInjectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        assertNull(mockField.get(new FieldClass2()));
    }

    private void VerifyMockIsInjected(Object expectedMockInstance, Class<?> expectedMockType) {
        try {
            Object actualMockInstance = Whitebox.getInternalState(expectedMockInstance, "mockitoMockSet$0$0");
            assertEquals(expectedMockType, actualMockInstance.getClass());
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Failed to access private Mockito mocks", e);
        }
    }

    private static class TestClass {
        @InjectMocks
        private Field field;

        private Field mockField;

        public Field getMockField() {
            return mockField;
        }

        public void setMockField(Field mockField) {
            this.mockField = mockField;
        }
    }

    private static class FieldClass2 {
        @InjectMocks
        private Field mockField;
        private Field setterField;
        private Field fieldToInject;

        public Field getMockField() {
            return mockField;
        }

        public Field getMockField1() {
            return mockField;
        }

        public Field getMockField2() {
            return new Field() {
                public String getName() {
                    return "mockField2";
                }

                public Class<?> getType() {
                    return MockType2.class;
                }
            };
        }

        public Field getSetterField() {
            if (setterField == null) {
                setterField = new Field() {
                    public String getName() {
                        return "setterField";
                    }

                    public Class<?> getType() {
                        return MockType2.class;
                    }

                    public Object get(Object obj) throws IllegalAccessException {
                        return mock(MockType2.class);
                    }

                    public void set(Object obj, Object value) throws IllegalAccessException {
                        Whitebox.setInternalState(obj, "mockField", value);
                    }
                };
            }
            return setterField;
        }

        public Field getFieldToInject() {
            if (fieldToInject == null) {
                fieldToInject = new Field() {
                    public String getName() {
                        return "fieldToInject";
                    }

                    public Class<?> getType() {
                        return MockType2.class;
                    }

                    public Object get(Object obj) throws IllegalAccessException {
                        return null;
                    }

                    public void set(Object obj, Object value) throws IllegalAccessException {
                        Whitebox.setInternalState(obj, "fieldToInject", value);
                    }
                };
            }
            return fieldToInject;
        }
    }

    private static class MockType1 {}

    private static class MockType2 {}

    private static class MockType3 {}
}