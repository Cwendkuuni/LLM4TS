import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;

import static org.junit.Assert.*;

import java.lang.reflect.*;
import java.util.*;

public class FinalMockCandidateFilterTest {
    
    private FinalMockCandidateFilter finalMockCandidateFilter;
    
    @Before
    public void setUp() {
        finalMockCandidateFilter = new FinalMockCandidateFilter();
    }
    
    @Test
    public void testFilterCandidate_OneMock_InjectWithPropertySetter() throws Exception {
        // Arrange
        Collection<Object> mocks = new ArrayList<Object>();
        Field field = MyClass.class.getDeclaredField("myField");
        MyClass fieldInstance = new MyClass();
        MyMock matchingMock = Mockito.mock(MyMock.class);
        mocks.add(matchingMock);
        
        // Act
        MockCandidateFilter.OngoingInjecter ongoingInjecter = finalMockCandidateFilter.filterCandidate(mocks, field, fieldInstance);
        Object result = ongoingInjecter.thenInject();
        
        // Assert
        assertNotNull(result);
        verify(matchingMock).setMyField(any());
    }
    
    @Test
    public void testFilterCandidate_OneMock_InjectWithField() throws Exception {
        // Arrange
        Collection<Object> mocks = new ArrayList<Object>();
        Field field = MyClass.class.getDeclaredField("myField");
        MyClass fieldInstance = new MyClass();
        MyMock matchingMock = Mockito.mock(MyMock.class);
        mocks.add(matchingMock);
        doReturn(true).when(matchingMock).setMyField(any());
        
        // Act
        MockCandidateFilter.OngoingInjecter ongoingInjecter = finalMockCandidateFilter.filterCandidate(mocks, field, fieldInstance);
        Object result = ongoingInjecter.thenInject();
        
        // Assert
        assertNotNull(result);
        verify(matchingMock).setMyField(any());
    }
    
    @Test
    public void testFilterCandidate_MultipleMocks() throws Exception {
        // Arrange
        Collection<Object> mocks = new ArrayList<Object>();
        Field field = MyClass.class.getDeclaredField("myField");
        MyClass fieldInstance = new MyClass();
        MyMock matchingMock1 = Mockito.mock(MyMock.class);
        MyMock matchingMock2 = Mockito.mock(MyMock.class);
        mocks.add(matchingMock1);
        mocks.add(matchingMock2);
        
        // Act
        MockCandidateFilter.OngoingInjecter ongoingInjecter = finalMockCandidateFilter.filterCandidate(mocks, field, fieldInstance);
        Object result = ongoingInjecter.thenInject();
        
        // Assert
        assertNull(result);
    }
    
    @Test
    public void testFilterCandidate_RuntimeExceptionThrown() throws Exception {
        // Arrange
        Collection<Object> mocks = new ArrayList<Object>();
        Field field = MyClass.class.getDeclaredField("myField");
        MyClass fieldInstance = new MyClass();
        MyMock matchingMock = Mockito.mock(MyMock.class);
        mocks.add(matchingMock);
        doThrow(new RuntimeException()).when(matchingMock).setMyField(any());
        
        // Act
        MockCandidateFilter.OngoingInjecter ongoingInjecter = finalMockCandidateFilter.filterCandidate(mocks, field, fieldInstance);
        Object result = ongoingInjecter.thenInject();
        
        // Assert
        assertNotNull(result);
        verify(matchingMock).setMyField(any());
        verify(matchingMock).handleDependencyInjectionException(any(), any(), any());
    }
    
    class MyClass {
        MyMock myField;
    }
    
    interface MyMock {
        boolean setMyField(Object value);
        void handleDependencyInjectionException(Field field, Object object, Exception exception);
    }
    
}