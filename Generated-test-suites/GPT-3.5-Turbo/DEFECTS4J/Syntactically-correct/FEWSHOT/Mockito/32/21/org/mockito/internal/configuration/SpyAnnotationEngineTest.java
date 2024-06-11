
package org.mockito.internal.configuration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.MockUtil;

public class SpyAnnotationEngineTest {
    
    @Spy
    private MyClass myClass;
    
    @Mock
    private AnotherClass anotherClass;
    
    @Captor
    private MyCaptor myCaptor;
    
    private SpyAnnotationEngine spyAnnotationEngine;
    
    @Before
    public void setup() {
        spyAnnotationEngine = new SpyAnnotationEngine();
    }
    
    @Test
    public void testProcess_withSpyAnnotation_shouldSpyField() {
        spyAnnotationEngine.process(MyClass.class, this);
        assertNotNull(myClass);
        Mockito.verifyZeroInteractions(myClass);
    }
    
    @Test(expected = MockitoException.class)
    public void testProcess_withNullInstance_shouldThrowException() {
        spyAnnotationEngine.process(AnotherClass.class, this);
    }
    
    @Test(expected = MockitoException.class)
    public void testProcess_withMockInstance_shouldThrowException() {
        spyAnnotationEngine.process(AnotherClass.class, this);
    }
    
    @Test
    public void testAssertNoAnnotations_withUndesiredAnnotations_shouldThrowException() throws NoSuchFieldException {
        NoSuchFieldException ex = null;
        try {
            spyAnnotationEngine.assertNoAnnotations(Spy.class, getClass().getDeclaredField("myClass"), Mock.class, MockitoAnnotations.Mock.class, Captor.class);
        } catch (NoSuchFieldException e) {
            ex = e;
        }
        assertNull(ex);
    }
    
}
