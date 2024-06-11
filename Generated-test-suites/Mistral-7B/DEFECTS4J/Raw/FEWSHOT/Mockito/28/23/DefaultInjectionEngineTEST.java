package org.mockito.internal.configuration;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.DefaultInjectionEngine;
import org.mockito.internal.configuration.injection.FieldInitializer;
import org.mockito.internal.configuration.injection.MockCandidateFilter;
import org.mockito.internal.configuration.injection.TypeBasedCandidateFilter;
import org.mockito.internal.util.reflection.FieldAccessUtil;
import org.mockito.internal.util.reflection.FieldUtil;
import org.mockito.internal.util.reflection.ReflectUtil;
import org.mockito.internal.util.reflection.Whitebox;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

public class DefaultInjectionEngineTest {

    private DefaultInjectionEngine injectionEngine;
    private Object testClassInstance;

    @Before
    public void setup() throws Exception {
        testClassInstance = Mockito.spy(new TestClass());
        injectionEngine = new DefaultInjectionEngine();
    }

    @Test
    public void testInjectMocksOnFields() throws Exception {
        Set<Field> injectMocksFields = Arrays.asList(
                FieldUtil.getDeclaredFields(TestClass.class, Mockito.mock(MockCandidateFilter.class), Mockito.mock(FieldInitializer.class)),
                FieldUtil.getDeclaredFields(TestClass.class, Mockito.mock(TypeBasedCandidateFilter.class), Mockito.mock(NameBasedCandidateFilter.class), Mockito.mock(FinalMockCandidateFilter.class))
        ).stream().collect(Collectors.toCollection(HashSet::new));

        Set<Object> mocks = new HashSet<>();
        mocks.add(Mockito.mock(TypeA.class));
        mocks.add(Mockito.mock(TypeB.class));
        mocks.add(Mockito.mock(TypeC.class));

        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        // verify mocks are injected in the correct order
        Field fieldA = ReflectUtil.getDeclaredField(TestClass.class, "fieldA");
        assertNotNull(fieldA.get(testClassInstance));
        assertSame(mocks.iterator().next(), fieldA.get(testClassInstance));

        Field fieldB = ReflectUtil.getDeclaredField(TestClass.class, "fieldB");
        assertNotNull(fieldB.get(testClassInstance));
        assertSame(mocks.iterator().remove(), fieldB.get(testClassInstance));

        Field fieldC = ReflectUtil.getDeclaredField(TestClass.class, "fieldC");
        assertNotNull(fieldC.get(testClassInstance));
        assertSame(mocks.iterator().remove(), fieldC.get(testClassInstance));

        // verify no more mocks are injected
        assertNull(FieldUtil.getDeclaredFieldValue(testClassInstance, "fieldD"));
    }

    @Test(expected = MockitoException.class)
    public void testInjectMocksOnFieldsWithDuplicateMocks() throws Exception {
        Set<Field> injectMocksFields = Arrays.asList(
                FieldUtil.getDeclaredFields(TestClass.class, Mockito.mock(MockCandidateFilter.class), Mockito.mock(FieldInitializer.class)),
                FieldUtil.getDeclaredFields(TestClass.class, Mockito.mock(MockCandidateFilter.class), Mockito.mock(FieldInitializer.class))
        ).stream().collect(Collectors.toCollection(HashSet::new));

        Set<Object> mocks = new HashSet<>();
        mocks.add(Mockito.mock(TypeA.class));

        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        // should throw exception
        throw new AssertionError("Exception expected but not thrown");
    }

    @Test
    public void testInjectMockCandidate() throws Exception {
        Class<?> awaitingInjectionClazz = TypeD.class;

        Set<Object> mocks = new HashSet<>();
        mocks.add(Mockito.mock(TypeA.class));
        mocks.add(Mockito.mock(TypeB.class));
        mocks.add(Mockito.mock(TypeC.class));

        injectionEngine.injectMockCandidate(awaitingInjectionClazz, mocks, testClassInstance);

        // verify mocks are injected in the correct order
        Field fieldA = ReflectUtil.getDeclaredField(awaitingInjectionClazz, "fieldA");
        assertNotNull(fieldA.get(testClassInstance));
        assertSame(mocks.iterator().next(), fieldA.get(testClassInstance));

        Field fieldB = ReflectUtil.getDeclaredField(awaitingInjectionClazz, "fieldB");
        assertNotNull(fieldB.get(testClassInstance));
        assertSame(mocks.iterator().remove(), fieldB.get(testClassInstance));
    }

    @Test
    public void testInjectMockCandidateWithNoCandidate() throws Exception {
        Class<?> awaitingInjectionClazz = NonMockableClass.class;

        Set<Object> mocks = new HashSet<>();
        mocks.add(Mockito.mock(TypeA.class));
        mocks.add(Mockito.mock(TypeB.class));
        mocks.add(Mockito.mock(TypeC.class));

        injectionEngine.injectMockCandidate(awaitingInjectionClazz, mocks, testClassInstance);

        // no mock is injected
        assertNull(ReflectUtil.getDeclaredFieldValue(awaitingInjectionClazz, "fieldA"));
    }

    @Test
    public void testOrderedInstanceFieldsFrom() throws Exception {
        Class<?> awaitingInjectionClazz = TypeD.class;

        Field[] orderedInstanceFields = injectionEngine.orderedInstanceFieldsFrom(awaitingInjectionClazz);

        // verify fields are ordered from sub-type to super-type
        Field fieldC = Arrays.stream(orderedInstanceFields).filter(f -> f.getName().equals("fieldC")).findFirst().orElse(null);
        Field fieldB = Arrays.stream(orderedInstanceFields).filter(f -> f.getName().equals("fieldB")).findFirst().orElse(null);
        Field fieldA = Arrays.stream(orderedInstanceFields).filter(f -> f.getName().equals("fieldA")).findFirst().orElse(null);

        assertNotNull(fieldA);
        assertNotNull(fieldB);
        assertNotNull(fieldC);

        assertSame(fieldA.getDeclaringClass(), awaitingInjectionClazz.getSuperclass());
        assertSame(fieldB.getDeclaringClass(), awaitingInjectionClazz.getSuperclass());
        assertSame(fieldC.getDeclaringClass(), awaitingInjectionClazz);

        assertEquals(0, fieldA.compareTo(fieldB));
        assertEquals(-1, fieldB.compareTo(fieldC));
    }
}

class TestClass {
    @InjectMocks
    private TypeA fieldA;

    @InjectMocks
    private TypeB fieldB;

    @InjectMocks
    private TypeC fieldC;

    private TypeD fieldD;
}

class TypeA {}

class TypeB extends TypeA {}

class TypeC extends TypeB {}

class TypeD extends TypeC {}

class NonMockableClass {}