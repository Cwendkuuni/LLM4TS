package org.mockito.internal.configuration;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.MockitoAnnotations.initMocks;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import org.junit.Test;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.util.MockUtil;
import org.mockito.Mockito;

public class SpyAnnotationEngineTest {

    @Test
    public void testProcess_noSpyAnnotation_doesNotSpy() {
        Class context = Mockito.class;
        Object testClass = new Object() {};
        SpyAnnotationEngine engine = new SpyAnnotationEngine();
        engine.process(context, testClass);
        Field[] fields = context.getDeclaredFields();
        assertFalse(fields[0].isAnnotationPresent(Spy.class));
    }

    @Test
    public void testProcess_spyAnnotationPresent_spiesField() {
        Class context = Mockito.class;
        Object testClass = new Object() {
            @Spy
            private List list = new ArrayList();
        };
        SpyAnnotationEngine engine = new SpyAnnotationEngine();
        engine.process(context, testClass);
        Field field = context.getDeclaredField("list");
        assertTrue(field.isAnnotationPresent(Spy.class));
        Object instance = field.get(testClass);
        assertSame(instance, spy(new ArrayList()));
        verifyNoMoreInteractions(instance);
    }

    @Test
    public void testProcess_spyAnnotationPresent_resetsMockedField() {
        Class context = Mockito.class;
        Object testClass = new Object() {
            @Spy
            @Mock
            private List list = new ArrayList();
        };
        SpyAnnotationEngine engine = new SpyAnnotationEngine();
        engine.process(context, testClass);
        Field field = context.getDeclaredField("list");
        assertTrue(field.isAnnotationPresent(Spy.class));
        assertTrue(Mockito.isMock(field.get(testClass)));
        Mockito.reset(field.get(testClass));
        assertFalse(Mockito.isMock(field.get(testClass)));
    }

    @Test(expected = MockitoException.class)
    public void testProcess_spyAnnotationPresent_instanceMissing() {
        Class context = Mockito.class;
        Object testClass = new Object() {};
        SpyAnnotationEngine engine = new SpyAnnotationEngine();
        engine.process(context, testClass);
        Field field = context.getDeclaredField("list");
        field.setAccessible(true);
        assertNull(field.get(testClass));
    }

    @Test(expected = IllegalAccessException.class)
    public void testProcess_spyAnnotationPresent_accessViolation() {
        Class context = Mockito.class;
        Object testClass = new Object() {};
        SpyAnnotationEngine engine = new SpyAnnotationEngine();
        engine.process(context, testClass);
        Field field = context.getDeclaredField("list");
        field.setAccessible(true);
        field.set(testClass, new ArrayList());
    }

    @Test
    public void testAssertNoAnnotations_noUndesiredAnnotation_doesNotThrowException() {
        Class annotation = Spy.class;
        Field field = new Field() {};
        new SpyAnnotationEngine().assertNoAnnotations(annotation, field, Captor.class, Mockito.Mock.class);
    }

    @Test(expected = org.mockito.exceptions.base.MockitoException.class)
    public void testAssertNoAnnotations_spyAnnotationAndMockAnnotation_throwsException() {
        Class annotation = Spy.class;
        Field field = new Field() {
            @Mock
            private int value = 42;
        };
        new SpyAnnotationEngine().assertNoAnnotations(annotation, field, Mockito.Mock.class);
    }

    @Test
    public void testAssertNoAnnotations_mockAnnotationAndSpyAnnotation_doesNotThrowException() {
        Class annotation = Mockito.Mock.class;
        Field field = new Field() {
            @Spy
            private int value = 42;
        };
        new SpyAnnotationEngine().assertNoAnnotations(annotation, field, Spy.class);
    }

    @Test
    public void testAssertNoAnnotations_sameAnnotation_doesNotThrowException() {
        Class annotation = Spy.class;
        Field field = new Field() {
            @Spy
            private Spy spy = new Spy();
        };
        new SpyAnnotationEngine().assertNoAnnotations(annotation, field, Spy.class);
    }

    @Test
    public void testAssertNoAnnotations_noAnnotation_doesNotThrowException() {
        Class annotation = Spy.class;
        Field field = new Field() {};
        new SpyAnnotationEngine().assertNoAnnotations(annotation, field);
    }
}