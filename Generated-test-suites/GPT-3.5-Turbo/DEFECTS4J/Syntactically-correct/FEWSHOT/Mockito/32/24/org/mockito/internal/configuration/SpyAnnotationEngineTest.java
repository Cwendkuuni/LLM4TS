package org.mockito.internal.configuration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.spy;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.configuration.AnnotationEngine;
import org.mockito.exceptions.Reporter;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.MockUtil;

public class SpyAnnotationEngineTest {
    private SpyAnnotationEngine spyAnnotationEngine;
    
    @Before
    public void setUp() {
        spyAnnotationEngine = new SpyAnnotationEngine();
    }
    
    @Test
    public void testCreateMockFor() {
        Annotation annotation = mock(Annotation.class);
        Field field = mock(Field.class);
        Object result = spyAnnotationEngine.createMockFor(annotation, field);
        assertNull(result);
    }
    
    @Test
    public void testProcess() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        Class<?> context = SpyAnnotationEngine.class;
        Object testClass = new Object();
        
        Field field1 = SpyAnnotationEngine.class.getDeclaredField("fields");
        Field field2 = SpyAnnotationEngine.class.getDeclaredField("field");
        Field field3 = SpyAnnotationEngine.class.getDeclaredField("wasAccessible");
        Field field4 = SpyAnnotationEngine.class.getDeclaredField("instance");
        field1.setAccessible(true);
        field2.setAccessible(true);
        field3.setAccessible(true);
        field4.setAccessible(true);
        Field[] fields = new Field[] { field1, field2, field3, field4 };
        field1.set(spyAnnotationEngine, fields);
        field2.set(spyAnnotationEngine, field1);
        field3.set(spyAnnotationEngine, true);
        field4.set(spyAnnotationEngine, testClass);
        
        MockUtil mockUtil = mock(MockUtil.class);
        Mockito.when(mockUtil.isMock(Mockito.any())).thenReturn(true);
        
        Spy spy1 = mock(Spy.class);
        Mockito.when(field1.isAnnotationPresent(Spy.class)).thenReturn(true);
        Mock spy1Instance = mock(Mock.class);
        Mockito.when(field1.get(testClass)).thenReturn(spy1Instance);
        Mockito.when(mockUtil.isMock(spy1Instance)).thenReturn(true);
        
        Spy spy2 = mock(Spy.class);
        Mockito.when(field2.isAnnotationPresent(Spy.class)).thenReturn(true);
        Mock spy2Instance = mock(Mock.class);
        Mockito.when(field2.get(testClass)).thenReturn(spy2Instance);
        Mockito.when(mockUtil.isMock(spy2Instance)).thenReturn(false);
        
        Mock mock1 = mock(Mock.class);
        Mockito.when(new MockUtil().isMock(mock1)).thenReturn(false);
        
        MockitoAnnotations.Mock mock2 = mock(MockitoAnnotations.Mock.class);
        org.mockito.MockitoAnnotations.Mock mock3 = mock(org.mockito.MockitoAnnotations.Mock.class);
        Captor captor = mock(Captor.class);
        
        spyAnnotationEngine.process(context, testClass);
        
        Mockito.verify(field1, Mockito.times(2)).isAnnotationPresent(Spy.class);
        Mockito.verify(field1).setAccessible(true);
        Mockito.verify(field1).get(testClass);
        Mockito.verify(mockUtil).isMock(spy1Instance);
        Mockito.verify(mockUtil).isMock(spy2Instance);
        Mockito.verify(field2).isAnnotationPresent(Spy.class);
        Mockito.verify(field2).setAccessible(true);
        Mockito.verify(field2, Mockito.never()).get(testClass);
        Mockito.verify(mockUtil).isMock(mock1);
        Mockito.verifyMock(spy1Instance).reset();
        Mockito.verify(field1).set(testClass, Mockito.spy(spy1Instance));
        Mockito.verify(field2, Mockito.never()).set(testClass, Mockito.spy(spy2Instance));
        
        field1.setAccessible(false);
        field2.setAccessible(false);
        field3.setAccessible(false);
        field4.setAccessible(false);
    }
    
    @Test
    public void testProcess_noSpyAnnotation() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        Class<?> context = SpyAnnotationEngine.class;
        Object testClass = new Object();
        
        Field field1 = SpyAnnotationEngine.class.getDeclaredField("fields");
        Field field2 = SpyAnnotationEngine.class.getDeclaredField("field");
        Field field3 = SpyAnnotationEngine.class.getDeclaredField("wasAccessible");
        Field field4 = SpyAnnotationEngine.class.getDeclaredField("instance");
        field1.setAccessible(true);
        field2.setAccessible(true);
        field3.setAccessible(true);
        field4.setAccessible(true);
        Field[] fields = new Field[] { field1, field2, field3, field4 };
        field1.set(spyAnnotationEngine, fields);
        field2.set(spyAnnotationEngine, field1);
        field3.set(spyAnnotationEngine, true);
        field4.set(spyAnnotationEngine, testClass);
        
        spyAnnotationEngine.process(context, testClass);
        
        Mockito.verify(field1, Mockito.times(2)).isAnnotationPresent(Spy.class);
        Mockito.verify(field1).setAccessible(true);
        Mockito.verify(field1, Mockito.never()).get(testClass);
        Mockito.verify(field2, Mockito.never()).isAnnotationPresent(Spy.class);
        Mockito.verify(field2, Mockito.never()).setAccessible(true);
        Mockito.verify(field2, Mockito.never()).get(testClass);
        Mockito.verify(Mockito.never()).reset(Mockito.any());
        Mockito.verify(Mockito.never()).spy(Mockito.any());
        
        field1.setAccessible(false);
        field2.setAccessible(false);
        field3.setAccessible(false);
        field4.setAccessible(false);
    }
    
    @Test
    public void testProcess_instanceNull() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        Class<?> context = SpyAnnotationEngine.class;
        Object testClass = new Object();
        
        Field field1 = SpyAnnotationEngine.class.getDeclaredField("fields");
        Field field2 = SpyAnnotationEngine.class.getDeclaredField("field");
        Field field3 = SpyAnnotationEngine.class.getDeclaredField("wasAccessible");
        Field field4 = SpyAnnotationEngine.class.getDeclaredField("instance");
        field1.setAccessible(true);
        field2.setAccessible(true);
        field3.setAccessible(true);
        field4.setAccessible(true);
        Field[] fields = new Field[] { field1, field2, field3, field4 };
        field1.set(spyAnnotationEngine, fields);
        field2.set(spyAnnotationEngine, field1);
        field3.set(spyAnnotationEngine, true);
        field4.set(spyAnnotationEngine, null);
        
        spyAnnotationEngine.process(context, testClass);
        
        Mockito.verify(field1, Mockito.times(2)).isAnnotationPresent(Spy.class);
        Mockito.verify(field1).setAccessible(true);
        Mockito.verify(field1, Mockito.never()).get(testClass);
        Mockito.verify(field2, Mockito.never()).isAnnotationPresent(Spy.class);
        Mockito.verify(field2, Mockito.never()).setAccessible(true);
        Mockito.verify(field2, Mockito.never()).get(testClass);
        Mockito.verify(Mockito.never()).reset(Mockito.any());
        Mockito.verify(Mockito.never()).spy(Mockito.any());
        
        field1.setAccessible(false);
        field2.setAccessible(false);
        field3.setAccessible(false);
        field4.setAccessible(false);
    }
    
    @Test
    public void testProcess_notAMock() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        Class<?> context = SpyAnnotationEngine.class;
        Object testClass = new Object();
        
        Field field1 = SpyAnnotationEngine.class.getDeclaredField("fields");
        Field field2 = SpyAnnotationEngine.class.getDeclaredField("field");
        Field field3 = SpyAnnotationEngine.class.getDeclaredField("wasAccessible");
        Field field4 = SpyAnnotationEngine.class.getDeclaredField("instance");
        field1.setAccessible(true);
        field2.setAccessible(true);
        field3.setAccessible(true);
        field4.setAccessible(true);
        Field[] fields = new Field[] { field1, field2, field3, field4 };
        field1.set(spyAnnotationEngine, fields);
        field2.set(spyAnnotationEngine, field1);
        field3.set(spyAnnotationEngine, true);
        field4.set(spyAnnotationEngine, testClass);
        
        MockUtil mockUtil = mock(MockUtil.class);
        Mockito.when(mockUtil.isMock(Mockito.any())).thenReturn(false);
        
        Spy spy1 = mock(Spy.class);
        Mockito.when(field1.isAnnotationPresent(Spy.class)).thenReturn(true);
        Mockito.when(field1.get(testClass)).thenReturn(mock(Mock.class));
        Mockito.when(new MockUtil().isMock(Mockito.any())).thenReturn(false);
        
        spyAnnotationEngine.process(context, testClass);
        
        Mockito.verify(field1, Mockito.times(2)).isAnnotationPresent(Spy.class);
        Mockito.verify(field1).setAccessible(true);
        Mockito.verify(field1).get(testClass);
        Mockito.verify(mockUtil).isMock(Mockito.any());
        Mockito.verify(field2, Mockito.never()).isAnnotationPresent(Spy.class);
        Mockito.verify(field2, Mockito.never()).setAccessible(true);
        Mockito.verify(field2, Mockito.never()).get(testClass);
        Mockito.verify(Mockito.never()).reset(Mockito.any());
        Mockito.verify(Mockito.never()).spy(Mockito.any());
        
        field1.setAccessible(false);
        field2.setAccessible(false);
        field3.setAccessible(false);
        field4.setAccessible(false);
    }
    
    @Test
    public void testAssertNoAnnotations() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        Field field = mock(Field.class);
        
        Annotation annotation1 = mock(org.mockito.Spy.class);
        Mockito.when(field.isAnnotationPresent(org.mockito.Spy.class)).thenReturn(true);
        Class[] undesiredAnnotations1 = { Mock.class, MockitoAnnotations.Mock.class, Captor.class };
        
        Annotation annotation2 = mock(MockitoAnnotations.Mock.class);
        Mockito.when(field.isAnnotationPresent(MockitoAnnotations.Mock.class)).thenReturn(true);
        Class[] undesiredAnnotations2 = { Spy.class, Mock.class, Captor.class };
        
        Annotation annotation3 = mock(Captor.class);
        Mockito.when(field.isAnnotationPresent(Captor.class)).thenReturn(true);
        Class[] undesiredAnnotations3 = { Spy.class, Mock.class, MockitoAnnotations.Mock.class };
        
        Reporter reporter = mock(Reporter.class);
        Mockito.whenNew(Reporter.class).withNoArguments().thenReturn(reporter);
        
        spyAnnotationEngine.assertNoAnnotations(org.mockito.Spy.class, field, undesiredAnnotations1);
        spyAnnotationEngine.assertNoAnnotations(MockitoAnnotations.Mock.class, field, undesiredAnnotations2);
        spyAnnotationEngine.assertNoAnnotations(Captor.class, field, undesiredAnnotations3);
        
        Mockito.verify(field).isAnnotationPresent(org.mockito.Spy.class);
        Mockito.verify(field).isAnnotationPresent(MockitoAnnotations.Mock.class);
        Mockito.verify(field).isAnnotationPresent(Captor.class);
        Mockito.verifyNew(Reporter.class).withNoArguments();
        Mockito.verify(reporter).unsupportedCombinationOfAnnotations(org.mockito.Spy.class.getSimpleName(), org.mockito.Spy.class.getClass().getSimpleName());
        Mockito.verify(reporter).unsupportedCombinationOfAnnotations(MockitoAnnotations.Mock.class.getSimpleName(), MockitoAnnotations.Mock.class.getClass().getSimpleName());
        Mockito.verify(reporter).unsupportedCombinationOfAnnotations(Captor.class.getSimpleName(), Captor.class.getClass().getSimpleName());
    }
}