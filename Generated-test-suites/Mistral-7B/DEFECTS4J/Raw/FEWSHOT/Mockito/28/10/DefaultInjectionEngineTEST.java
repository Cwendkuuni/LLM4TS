package org.mockito.internal.configuration;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.DefaultInjectionEngine;
import org.mockito.internal.configuration.injection.MockCandidateFilter;
import org.mockito.internal.configuration.injection.TypeBasedCandidateFilter;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.mockitostub.MockitoStubs;
import org.mockito.verification.VerificationMode;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

public class DefaultInjectionEngineTest {

    private DefaultInjectionEngine injectionEngine;
    private Field fieldA;
    private Field fieldB;
    private Field fieldC;
    private Object testInstance;

    @Before
    public void setUp() throws IllegalAccessException {
        injectionEngine = new DefaultInjectionEngine();
        Class<?> testClass = this.getClass();
        testInstance = spy(testClass);

        fieldA = testClass.getDeclaredField("fieldA");
        fieldA.setAccessible(true);
        fieldA.set(testInstance, mock( ClassA.class, "fieldAMock" ));

        fieldB = testClass.getDeclaredField("fieldB");
        fieldB.setAccessible(true);

        fieldC = testClass.getDeclaredField("fieldC");
        fieldC.setAccessible(true);
        fieldC.set(testInstance, mock( ClassC.class, "fieldCMock" ));
    }

    @Test
    public void testInjectMocksOnFields() {
        Set<Field> injectMocksFields = new HashSet<Field>() {{
            add(fieldA);
            add(fieldB);
        }};
        Set<Object> mocks = new HashSet<Object>() {{
            add(fieldAMock);
            add(fieldCMock);
        }};

        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testInstance);

        // verifications
        verify(fieldAMock).someMethod();
        verifyZeroInteractions(fieldCMock);
    }

    @Test
    public void testInjectMocksOnFieldsWithMultipleCandidates() {
        Set<Field> injectMocksFields = new HashSet<Field>() {{
            add(fieldA);
            add(fieldB);
            add(new FieldTestClassB().getDeclaredField("fieldD"));
        }};
        Set<Object> mocks = new HashSet<Object>() {{
            add(fieldAMock);
            add(fieldCMock);
            add(mock(ClassB.class, "fieldBMock"));
            add(mock(ClassD.class, "fieldDMock"));
        }};

        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testInstance);

        // verifications
        verify(fieldAMock).someMethod();
        verify(fieldBMock).someMethod();
        verifyNoMoreInteractions(fieldCMock);
        verifyZeroInteractions(fieldDMock);
    }

    @Test(expected = MockitoException.class)
    public void testInjectMocksOnFieldsWithMultipleCandidatesAndNoNameCriteria() {
        Set<Field> injectMocksFields = new HashSet<Field>() {{
            add(fieldA);
            add(new FieldTestClassB().getDeclaredField("fieldD"));
        }};
        Set<Object> mocks = new HashSet<Object>() {{
            add(fieldAMock);
            add(mock(ClassC.class, "fieldCMock"));
            add(mock(ClassD.class, "fieldDMock"));
        }};

        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testInstance);
    }

    @Test
    public void testInjectMocksOnFieldsWithTypeBasedCandidate() {
        Set<Field> injectMocksFields = new HashSet<Field>() {{
            add(fieldC);
        }};
        Set<Object> mocks = new HashSet<Object>() {{
            add(mock(ClassB.class, "fieldEMock"));
            add(fieldCMock);
        }};

        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testInstance);

        // verifications
        verifyZeroInteractions(fieldEMock);
        verify(fieldCMock).someMethod();
    }

    @Test
    public void testInjectMocksOnFieldsWithFinalMockCandidate() {
        Set<Field> injectMocksFields = new HashSet<Field>() {{
            add(new FieldTestClassB().getDeclaredField("fieldE"));
        }};
        Set<Object> mocks = new HashSet<Object>() {{
            add(MockitoStubs.spy(ClassE.class, "fieldEMock"));
        }};

        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testInstance);

        // verifications
        verify(fieldEMock).someMethod();
    }
}

class FieldTestClassB {
    private Field fieldD;

    public Field getDeclaredField(String fieldName) {
        Class<?> fieldTestClassB = this.getClass();
        try {
            field = fieldTestClassB.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field;
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }
}

class ClassA {
    public void someMethod() {
        // some implementation
    }
}

class ClassB {
    public void someMethod() {
        // some implementation
    }
}

class ClassC {
    public void someMethod() {
        // some implementation
    }
}

class ClassD {
    public void someMethod() {
        // some implementation
    }
}

class ClassE {
    public void someMethod() {
        // some implementation
    }
}