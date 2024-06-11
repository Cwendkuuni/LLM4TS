
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class DefaultInjectionEngineTest {

    private DefaultInjectionEngine defaultInjectionEngine;
    private MockCandidateFilter mockCandidateFilter;

    @Before
    public void setUp() {
        defaultInjectionEngine = new DefaultInjectionEngine();
        mockCandidateFilter = mock(MockCandidateFilter.class);
        defaultInjectionEngine.setMockCandidateFilter(mockCandidateFilter);
    }

    @Test
    public void testInjectMocksOnFields() throws Exception {
        // Arrange
        Set<Field> injectMocksFields = new HashSet<>();
        Set<Object> mocks = new HashSet<>();
        Object testClassInstance = new Object();

        Field field1 = TestClass.class.getDeclaredField("mock1");
        Field field2 = TestClass.class.getDeclaredField("mock2");
        injectMocksFields.add(field1);
        injectMocksFields.add(field2);
        
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        mocks.add(mock1);
        mocks.add(mock2);
        
        when(mockCandidateFilter.filterCandidate(eq(mocks), eq(field1), eq(null))).thenReturn(mock1);
        when(mockCandidateFilter.filterCandidate(eq(mocks), eq(field2), eq(null))).thenReturn(mock2);
        
        // Act
        defaultInjectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);
        
        // Assert
        verify(mockCandidateFilter).filterCandidate(eq(mocks), eq(field1), eq(null));
        verify(mockCandidateFilter).filterCandidate(eq(mocks), eq(field2), eq(null));
        
        assertEquals(mock1, field1.get(testClassInstance));
        assertEquals(mock2, field2.get(testClassInstance));
    }
    
    private class TestClass {
        private Object mock1;
        private Object mock2;
    }
}
