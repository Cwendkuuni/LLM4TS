package org.mockito.internal.configuration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.spy;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.exceptions.Reporter;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.configuration.SpyAnnotationEngine;

@SuppressWarnings({"unchecked"})
public class SpyAnnotationEngineTest {
    private SpyAnnotationEngine spyAnnotationEngine;
    private Object testClass;
    
    @Before
    public void setup() {
        spyAnnotationEngine = new SpyAnnotationEngine();
        testClass = new Object();
    }
    
    @After
    public void tearDown() {
        spyAnnotationEngine = null;
        testClass = null;
    }

    @Test
    public void testCreateMockFor() {
        Annotation annotation = mock(Annotation.class);
        Field field = mock(Field.class);
        
        assertEquals(null, spyAnnotationEngine.createMockFor(annotation, field));
    }

    @Test
    public void testProcess() throws IllegalAccessException {
        Field field = mock(Field.class);
        Object instance = new Object();
        MockUtil mockUtil = mock(MockUtil.class);
        
        Mockito.when(mockUtil.isMock(instance)).thenReturn(false);
        Mockito.when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
        Mockito.when(field.get(testClass)).thenReturn(instance);
        
        spyAnnotationEngine.process(testClass.getClass(), testClass);
        
        Mockito.verify(mockUtil).isMock(instance);
        Mockito.verify(field, Mockito.atLeastOnce()).isAnnotationPresent(Spy.class);
        Mockito.verify(field).get(testClass);
        Mockito.verify(field).set(testClass, spy(instance));
    }
    
    @Test
    public void testProcess_withNullInstance() throws IllegalAccessException {
        Field field = mock(Field.class);
        MockUtil mockUtil = mock(MockUtil.class);
        
        Mockito.when(mockUtil.isMock(null)).thenReturn(false);
        Mockito.when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
        Mockito.when(field.get(testClass)).thenReturn(null);
        
        try {
            spyAnnotationEngine.process(testClass.getClass(), testClass);
        } catch (MockitoException e) {
            assertEquals("Cannot create a @Spy for 'field' field because the *instance* is missing\n" +
                    	 "The instance must be created *before* initMocks();\n" +
                          "Example of correct usage of @Spy:\n" +
                    	  "   @Spy List mock = new LinkedList();\n" +
                    	  "   //also, don't forget about MockitoAnnotations.initMocks();", e.getMessage());
        }
        
        Mockito.verify(mockUtil).isMock(null);
        Mockito.verify(field, Mockito.atLeastOnce()).isAnnotationPresent(Spy.class);
        Mockito.verify(field).get(testClass);
        Mockito.verify(field, Mockito.never()).set(testClass, Mockito.any());
    }
    
    @Test
    public void testProcess_withSpiedInstance() throws IllegalAccessException {
        Field field = mock(Field.class);
        Object instance = mock(Object.class);
        MockUtil mockUtil = mock(MockUtil.class);
        
        Mockito.when(mockUtil.isMock(instance)).thenReturn(true);
        Mockito.when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
        Mockito.when(field.get(testClass)).thenReturn(instance);
        
        spyAnnotationEngine.process(testClass.getClass(), testClass);
        
        Mockito.verify(mockUtil).isMock(instance);
        Mockito.verify(field, Mockito.atLeastOnce()).isAnnotationPresent(Spy.class);
        Mockito.verify(field).get(testClass);
        Mockito.verify(mockUtil).resetMock(instance);
    }
    
    @Test
    public void testProcess_withIllegalAccessException() throws IllegalAccessException {
        Field field = mock(Field.class);
        Object instance = new Object();
        MockUtil mockUtil = mock(MockUtil.class);
        
        Mockito.when(mockUtil.isMock(instance)).thenReturn(false);
        Mockito.when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
        Mockito.when(field.get(testClass)).thenThrow(new IllegalAccessException());
        
        try {
            spyAnnotationEngine.process(testClass.getClass(), testClass);
        } catch (MockitoException e) {
            assertEquals("Problems initiating spied field field", e.getMessage());
        }
        
        Mockito.verify(mockUtil).isMock(instance);
        Mockito.verify(field, Mockito.atLeastOnce()).isAnnotationPresent(Spy.class);
        Mockito.verify(field).get(testClass);
        Mockito.verify(field, Mockito.never()).set(testClass, Mockito.any());
    }
    
    @Test
    public void testProcess_assertNoAnnotations() throws IllegalAccessException {
        Field field = mock(Field.class);
        Reporter reporter = mock(Reporter.class);
        
        Mockito.when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
        
        try {
            Method method = spyAnnotationEngine.getClass().getDeclaredMethod("assertNoAnnotations", Class.class, Field.class, Class[].class);
            method.setAccessible(true);
            method.invoke(spyAnnotationEngine, Spy.class, field, new Class[]{Mock.class, org.mockito.MockitoAnnotations.Mock.class, Captor.class});
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        Mockito.verify(field).isAnnotationPresent(Spy.class);
        Mockito.verify(reporter).unsupportedCombinationOfAnnotations(Spy.class.getSimpleName(), Spy.class.getClass().getSimpleName());
    }
}