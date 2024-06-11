import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class DefaultInjectionEngineTest {

    private DefaultInjectionEngine injectionEngine;
    private Set<Field> injectMocksFields;
    private Set<Object> mocks;
    private Object testClassInstance;
    
    @Mock
    private Field field1;
    
    @Mock
    private Field field2;
    
    @Mock
    private Field field3;
    
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        
        injectionEngine = new DefaultInjectionEngine();
        injectMocksFields = new HashSet<>();
        injectMocksFields.add(field1);
        injectMocksFields.add(field2);
        injectMocksFields.add(field3);
        
        mocks = new HashSet<>();
        mocks.add(mock(Object.class));
        mocks.add(mock(Object.class));
        mocks.add(mock(Object.class));
        
        testClassInstance = new Object();
    }

    @Test
    public void testInjectMocksOnFields() throws Exception {
        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);
        
        verify(field1, times(1)).getType();
        verify(field2, times(1)).getType();
        verify(field3, times(1)).getType();
    }

    @Test
    public void testInjectMocksOnFields_exceptionThrown() throws Exception {
        // Set up a mockito exception
        when(field2.getType()).thenThrow(MockitoException.class);
        
        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);
        
        verify(field1, times(1)).getType();
        verify(field2, times(1)).getType();
        verify(field3, times(1)).getType();
    }
}