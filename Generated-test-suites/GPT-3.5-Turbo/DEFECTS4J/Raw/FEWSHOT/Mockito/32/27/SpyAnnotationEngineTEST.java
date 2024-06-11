package org.mockito.internal.configuration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import java.lang.reflect.Field;
import java.util.List;
import java.util.LinkedList;

import org.junit.Test;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.configuration.AnnotationEngine;
import org.mockito.exceptions.Reporter;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.MockUtil;

public class SpyAnnotationEngineTest {
	
	private SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
	
	@Spy
	private List<String> spyList;
	
	@Spy
	private LinkedList<String> spyLinkedList;
	
	@Mock
	private List<String> mockList;
	
	@Mock
	private LinkedList<String> mockLinkedList;
	
	@Captor
	private ArgumentCaptor<String> captor;

	@Test
    public void testCreateMockFor() {
        Annotation annotation = mock(Annotation.class);
        Field field = mock(Field.class);
        assertNull(spyAnnotationEngine.createMockFor(annotation, field));
    }

    @Test
    public void testProcess() {
        Class<?> context = null;
        Object testClass = null;
        spyAnnotationEngine.process(context, testClass);
        
        assertEquals(mockList, spyAnnotationEngine.createMockFor(context, mockList));
        assertEquals(mockLinkedList, spyAnnotationEngine.createMockFor(context, mockLinkedList));
        
        spyAnnotationEngine.process(context, testClass);
        
        Mockito.verify(mockList).clear();
        Mockito.verify(mockLinkedList).clear();
        
        spyAnnotationEngine.process(context, testClass);
        
        Mockito.verify(mockList, Mockito.times(2)).clear();
        Mockito.verify(mockLinkedList, Mockito.times(2)).clear();
        
        Mockito.verify(mockList).size();
        Mockito.verify(mockLinkedList).size();
    }
    
    @Test(expected = MockitoException.class)
    public void testProcessWithNullInstance() {
    	Field field = mock(Field.class);
    	Object testClass = new Object();
    	spyAnnotationEngine.process(testClass.getClass(), testClass);
    }
    
    @Test
    public void testAssertNoAnnotations() {
    	Field field = mock(Field.class);
    	spyAnnotationEngine.assertNoAnnotations(Spy.class, field, Mock.class, org.mockito.MockitoAnnotations.Mock.class, Captor.class);
    	
    	field = mock(Field.class);
    	spyAnnotationEngine.assertNoAnnotations(Spy.class, field);
    }
}