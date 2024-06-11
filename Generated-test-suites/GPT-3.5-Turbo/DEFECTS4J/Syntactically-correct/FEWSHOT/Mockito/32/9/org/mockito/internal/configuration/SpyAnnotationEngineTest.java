
package org.mockito.internal.configuration;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.Spy;

public class SpyAnnotationEngineTest {
    
    private SpyAnnotationEngine spyAnnotationEngine;
    private Class<?> context;
    private Object testClass;
    private Field spyField;
    
    @Before
    public void setUp() throws NoSuchFieldException, SecurityException {
        spyAnnotationEngine = new SpyAnnotationEngine();
        context = SpyAnnotationEngine.class;
        testClass = new SpyTestClass();
        spyField = testClass.getClass().getDeclaredField("spyField");
    }
    
    @Test
    public void testCreateMockFor() {
        assertNull(spyAnnotationEngine.createMockFor(null, null));
    }
    
    @Test
    public void testProcess() throws IllegalArgumentException, IllegalAccessException {
        spyAnnotationEngine.process(context, testClass);
        SpyTestClass spyTestClass = (SpyTestClass) testClass;
        assertNull(spyTestClass.getSpyField());
        
        List<String> list = new LinkedList<>();
        spyTestClass.setSpyField(list);
        MockitoAnnotations.initMocks(testClass);
        spyAnnotationEngine.process(context, testClass);
        assertSame(list, spyTestClass.getSpyField());
    }
    
    @Test
    public void testProcess_nullInstance() {
        SpyTestClass spyTestClass = (SpyTestClass) testClass;
        spyTestClass.setSpyField(null);
        try {
            spyAnnotationEngine.process(context, testClass);
            fail("Expected MockitoException to be thrown");
        } catch (MockitoException e) {
            assertEquals("Cannot create a @Spy for 'spyField' field because the *instance* is missing\n" +
                         "The instance must be created *before* initMocks();\n" +
                         "Example of correct usage of @Spy:\n" +
                         "   @Spy List mock = new LinkedList();\n" +
                         "   //also, don't forget about MockitoAnnotations.initMocks();", e.getMessage());
        }
    }
    
    @Test
    public void testProcess_existingSpy() throws IllegalArgumentException, IllegalAccessException {
        List<String> list = Mockito.spy(new LinkedList<>());
        SpyTestClass spyTestClass = (SpyTestClass) testClass;
        spyTestClass.setSpyField(list);
        MockitoAnnotations.initMocks(testClass);
        spyAnnotationEngine.process(context, testClass);
        verify(list).clear();
    }
    
    @Test
    public void testProcess_unsupportedAnnotationCombination() {
        spyField.setAccessible(true);
        spyField.setAnnotation(Mock.class);
        try {
            spyAnnotationEngine.process(context, testClass);
            fail("Expected MockitoException to be thrown");
        } catch (MockitoException e) {
            assertEquals("Unsupported combination of annotations: Spy and Mock", e.getMessage());
        }
    }
    
    private class SpyTestClass {
        @Spy
        private List<String> spyField;
        
        public List<String> getSpyField() {
            return spyField;
        }
        
        public void setSpyField(List<String> spyField) {
            this.spyField = spyField;
        }
    }
}
