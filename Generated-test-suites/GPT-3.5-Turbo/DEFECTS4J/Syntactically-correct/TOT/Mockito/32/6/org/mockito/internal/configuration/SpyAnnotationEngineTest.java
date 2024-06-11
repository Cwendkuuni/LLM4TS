import static org.mockito.Mockito.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.configuration.AnnotationEngine;
import org.mockito.exceptions.MockitoException;
import org.mockito.internal.configuration.SpyAnnotationEngine;
import org.mockito.internal.util.MockUtil;

public class SpyAnnotationEngineTest {
    
    private SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
    
    @Spy
    private List<String> spyList = new LinkedList<String>();
    
    @Mock
    private List<String> mockList;
    
    @Captor
    private ArgumentCaptor<List<String>> captorList;
    
    @Spy
    @Mock
    private List<String> multipleAnnotationList;
    
    @Spy
    private List<String> nullList;
    
    @Test
    public void testCreateMockFor() {
        Annotation annotation = Mockito.mock(Annotation.class);
        Field field = Mockito.mock(Field.class);
        Object result = spyAnnotationEngine.createMockFor(annotation, field);
        Assert.assertNull(result);
    }
    
    @Test
    public void testProcess_SpyField() throws IllegalAccessException {
        Class<?> context = SpyAnnotationEngineTest.class;
        Object testClass = this;
        
        spyAnnotationEngine.process(context, testClass);
        
        Assert.assertTrue(new MockUtil().isMock(spyList));
        Assert.assertTrue(new MockUtil().isSpy(spyList));
    }
    
    @Test
    public void testProcess_MockField() throws IllegalAccessException {
        Class<?> context = SpyAnnotationEngineTest.class;
        Object testClass = this;
        
        spyAnnotationEngine.process(context, testClass);
        
        Assert.assertTrue(new MockUtil().isMock(mockList));
        Assert.assertFalse(new MockUtil().isSpy(mockList));
        Mockito.verifyZeroInteractions(mockList);
    }
    
    @Test(expected = MockitoException.class)
    public void testProcess_CaptorField() {
        Class<?> context = SpyAnnotationEngineTest.class;
        Object testClass = this;
        
        spyAnnotationEngine.process(context, testClass);
    }
    
    @Test
    public void testProcess_InstanceMissing() {
        Class<?> context = SpyAnnotationEngineTest.class;
        Object testClass = this;
        
        try {
            spyAnnotationEngine.process(context, testClass);
        } catch (MockitoException e) {
            Assert.assertEquals("Cannot create a @Spy for 'nullList' field because the *instance* is missing\n" +
                    "The instance must be created *before* initMocks();\n" +
                    "Example of correct usage of @Spy:\n" +
                    "   @Spy List mock = new LinkedList();\n" +
                    "   //also, don't forget about MockitoAnnotations.initMocks();", e.getMessage());
        }
    }
    
    @Test
    public void testProcess_InstanceSpiedEarlier() throws IllegalAccessException {
        Class<?> context = SpyAnnotationEngineTest.class;
        Object testClass = this;
        
        spyAnnotationEngine.process(context, testClass);
        
        Mockito.reset(spyList);
        Assert.assertTrue(new MockUtil().isSpy(spyList));
        Mockito.verifyNoMoreInteractions(spyList);
    }
    
    @Test
    public void testProcess_InstanceNotSpiedEarlier() throws IllegalAccessException {
        Class<?> context = SpyAnnotationEngineTest.class;
        Object testClass = this;
        
        spyAnnotationEngine.process(context, testClass);
        spyAnnotationEngine.process(context, testClass);
        
        Assert.assertTrue(new MockUtil().isSpy(spyList));
        Mockito.verifyNoMoreInteractions(spyList);
    }
    
    @Test
    public void testProcess_Field_NotAccessible() throws IllegalAccessException {
        Class<?> context = SpyAnnotationEngineTest.class;
        Object testClass = this;
        
        Field field = Mockito.mock(Field.class);
        when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
        when(field.getName()).thenReturn("spyList");
        when(field.isAccessible()).thenReturn(false);
        when(field.get(testClass)).thenReturn(spyList);
        
        spyAnnotationEngine.process(context, testClass);
        
        Assert.assertTrue(new MockUtil().isSpy(spyList));
        
        Mockito.verify(field, times(2)).isAnnotationPresent(Spy.class);
        Mockito.verify(field, times(1)).getName();
        Mockito.verify(field, times(1)).isAccessible();
        Mockito.verify(field, times(1)).setAccessible(true);
        Mockito.verify(field, times(1)).get(testClass);
        Mockito.verify(field, times(1)).set(testClass, spyList);
        
        Mockito.verifyNoMoreInteractions(field);
    }
    
    @Test(expected = MockitoException.class)
    public void testProcess_Field_IllegalAccessException() throws IllegalAccessException {
        Class<?> context = SpyAnnotationEngineTest.class;
        Object testClass = this;
        
        Field field = Mockito.mock(Field.class);
        when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
        when(field.getName()).thenReturn("spyList");
        when(field.isAccessible()).thenReturn(true);
        when(field.get(testClass)).thenReturn(spyList);
        doThrow(new IllegalAccessException()).when(field).setAccessible(false);
        
        spyAnnotationEngine.process(context, testClass);
    }
    
    @Test
    public void testAssertNoAnnotations_NoUndesiredAnnotations() throws NoSuchFieldException {
        Field field = getClass().getDeclaredField("spyList");
        spyAnnotationEngine.assertNoAnnotations(Spy.class, field, Mock.class, org.mockito.MockitoAnnotations.Mock.class, Captor.class);
    }
    
    @Test(expected = MockitoException.class)
    public void testAssertNoAnnotations_UndesiredAnnotationPresent() throws NoSuchFieldException {
        Field field = getClass().getDeclaredField("spyList");
        spyAnnotationEngine.assertNoAnnotations(Spy.class, field, Spy.class, Mock.class, org.mockito.MockitoAnnotations.Mock.class, Captor.class);
    }
}