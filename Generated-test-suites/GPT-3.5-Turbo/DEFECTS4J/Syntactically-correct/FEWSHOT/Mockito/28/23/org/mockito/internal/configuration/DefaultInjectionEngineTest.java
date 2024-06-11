import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.MockCandidateFilter;
import org.mockito.internal.configuration.injection.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.TypeBasedCandidateFilter;
import org.mockito.internal.util.reflection.FieldInitializer;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public class DefaultInjectionEngineTest {

    private DefaultInjectionEngine engine;
    
    @Before
    public void setUp() {
        engine = new DefaultInjectionEngine();
    }
    
    @Test
    public void testInjectMocksOnFields() {
        Set<Object> mocks = new HashSet<>();
        mocks.add(mock(Object.class));
        mocks.add(mock(Object.class));
        Object testClassInstance = new Object();
        
        Field field1 = mock(Field.class);
        Field field2 = mock(Field.class);
        Field[] fields = {field1, field2};
        when(field1.getType()).thenReturn(Object.class);
        when(field2.getType()).thenReturn(Object.class);
        
        Set<Field> injectMocksFields = new HashSet<>();
        injectMocksFields.add(field1);
        injectMocksFields.add(field2);
        
        engine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);
        
        verify(field1, times(1)).getType();
        verify(field2, times(1)).getType();
        verify(field1, times(1)).getSuperclass();
        verify(field2, times(1)).getSuperclass();
        verify(field1, times(1)).getDeclaredFields();
        verify(field2, times(1)).getDeclaredFields();
        
        verify(mock(Object.class), times(1)).thenInject();
        verify(mock(Object.class), times(1)).thenInject();
    }

    @Test
    public void testInjectMocksOnFieldsWithException() {
        Set<Object> mocks = new HashSet<>();
        mocks.add(mock(Object.class));
        mocks.add(mock(Object.class));
        Object testClassInstance = new Object();
        
        Field field1 = mock(Field.class);
        Field[] fields = {field1};
        when(field1.getType()).thenReturn(Object.class);
        
        Set<Field> injectMocksFields = new HashSet<>();
        injectMocksFields.add(field1);
        
        doThrow(new MockitoException()).when(mock(FieldInitializer.class)).initialize();
        
        engine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);
        
        verify(field1, times(1)).getType();
        verify(field1, never()).getSuperclass();
        verify(field1, never()).getDeclaredFields();
        
        verify(new Reporter(), times(1)).cannotInitializeForInjectMocksAnnotation(anyString(), any(MockitoException.class));
        verify(mock(Object.class), never()).thenInject();
    }

    @Test
    public void testInjectMockCandidate() {
        Set<Object> mocks = new HashSet<>();
        Object fieldInstance = new Object();
        
        Field field1 = mock(Field.class);
        Field field2 = mock(Field.class);
        Field[] fields = {field1, field2};
        
        mocks.add(mock(Object.class));
        when(field1.getType()).thenReturn(Object.class);
        
        engine.injectMockCandidate(Object.class, mocks, fieldInstance);
        
        verify(field1, times(1)).getType();
        verify(field2, never()).getType();
        verify(mock(MockCandidateFilter.class), times(1)).filterCandidate(eq(mocks), eq(field1), eq(fieldInstance));
        verify(mock(MockCandidateFilter.class), never()).filterCandidate(eq(mocks), eq(field2), eq(fieldInstance));
        verify(mock(Object.class), times(1)).thenInject();
    }

    @Test
    public void testOrderedInstanceFieldsFrom() {
        Field field1 = mock(Field.class);
        Field field2 = mock(Field.class);
        Field[] fields = {field1, field2};
        
        when(field1.getType()).thenReturn(Object.class);
        when(field2.getType()).thenReturn(Object.class);
        when(field1.getSuperclass()).thenReturn(TestSuperclass.class);
        when(field2.getSuperclass()).thenReturn(TestSuperclass.class);
        
        Field[] orderedFields = engine.orderedInstanceFieldsFrom(TestClass.class);
        
        assertEquals(field1, orderedFields[0]);
        assertEquals(field2, orderedFields[1]);
    }
    
    private class TestClass extends TestSuperclass {
        private Object field1;
        private Object field2;
    }
    
    private class TestSuperclass {
        
    }
}