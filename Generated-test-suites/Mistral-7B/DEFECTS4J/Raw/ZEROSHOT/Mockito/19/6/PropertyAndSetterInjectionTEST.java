package org.mockito.internal.configuration.injection;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.util.collections.ListUtil;
import org.mockito.internal.util.reflection.FieldInitializationReport;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.internal.util.reflection.SuperTypesLastSorter;
import org.mockito.internal.util.reflection.TestFields;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.internal.util.reflection.TestFields.getPrivateField;
import static org.mockito.internal.util.reflection.TestFields.setPrivateField;

/**
 * Test class for PropertyAndSetterInjection
 */
public class PropertyAndSetterInjectionTest {

    private PropertyAndSetterInjection propertyAndSetterInjection;
    private Reporter reporter;
    private Class<?> testClass;
    private Object testInstance;

    @Before
    public void setup() {
        propertyAndSetterInjection = new PropertyAndSetterInjection();
        reporter = new Reporter();
        testClass = TestClassUnderTest.class;
        try {
            testInstance = testClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testProcessInjection() throws Exception {
        // given
        Field injectMocksField = TestClassUnderTest.class.getDeclaredField("injectMocksField");
        injectMocksField.setAccessible(true);
        Object mockCandidates = Arrays.asList(new ClassA(), new ClassB(), new ClassC()).toArray();

        // when
        boolean injectionOccurred = propertyAndSetterInjection.processInjection(injectMocksField, testInstance, (Set<Object>) Arrays.asList(mockCandidates));

        // then
        assertThat(injectionOccurred, is(true));
        // check that all mocks were injected exactly once
        for (Object mock : mockCandidates) {
            Object injectedMock = getPrivateField(testInstance, "injectedMocks").get(0);
            assertThat(injectedMock, is(sameInstance(mock)));
            assertThat(getPrivateField(testInstance, "injectedFields").get(injectedMock), is(notNullValue()));
        }
    }

    private static class TestClassUnderTest {
        @InjectMocks
        private static Field injectMocksField;
        private static List<Object> injectedMocks = new ArrayList<>();
        private static Map<Object, Field> injectedFields = new HashMap<>();

        static {
            try {
                injectMocksField = TestClassUnderTest.class.getDeclaredField("injectedFields");
                injectMocksField.setAccessible(true);
            } catch (NoSuchFieldException e) {
                throw new RuntimeException(e);
            }
            try {
                Field injectedFieldsField = TestClassUnderTest.class.getDeclaredField("injectedFields");
                injectedFieldsField.setAccessible(true);
                injectedFieldsField.set(null, new HashMap<>());
            } catch (NoSuchFieldException e) {
                throw new RuntimeException(e);
            }
        }

        public static void setInjectedFields(Map<Object, Field> injectedFields) {
            setPrivateField(TestClassUnderTest.class, "injectedFields", injectedFields);
        }
    }

    private static class ClassA {}
    private static class ClassB {}
    private static class ClassC {}
}