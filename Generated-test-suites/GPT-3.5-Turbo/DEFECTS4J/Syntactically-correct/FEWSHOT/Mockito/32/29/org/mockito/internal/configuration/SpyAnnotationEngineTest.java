package org.mockito.internal.configuration;

import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.configuration.AnnotationEngine;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.MockUtil;
import org.mockito.Mockito;
import org.mockito.exceptions.Reporter;
import org.junit.Test;
import java.lang.reflect.Field;
import java.lang.annotation.Annotation;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SpyAnnotationEngineTest {

    @Test
    public void testCreateMockFor() {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Annotation annotation = mock(Annotation.class);
        Field field = mock(Field.class);
        assertNull(spyAnnotationEngine.createMockFor(annotation, field));
    }

    @Test
    public void testProcess() throws Exception {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Class<?> context = TestClass.class;
        Object testClass = new TestClass();
        spyAnnotationEngine.process(context, testClass);
        TestClass testObject = (TestClass) testClass;
        assertNotNull(testObject.mock);
    }

    public class TestClass {
        @Mock
        private Object mock;
    }
}