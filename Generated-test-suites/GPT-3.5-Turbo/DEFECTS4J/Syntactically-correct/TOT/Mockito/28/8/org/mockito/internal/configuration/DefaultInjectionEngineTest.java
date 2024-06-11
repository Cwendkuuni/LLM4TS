import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.MockCandidateFilter;
import org.mockito.internal.configuration.injection.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.TypeBasedCandidateFilter;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.junit.Test;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;
import static org.mockito.Mockito.*;

public class DefaultInjectionEngineTest {

    private DefaultInjectionEngine defaultInjectionEngine = new DefaultInjectionEngine();
    private Set<Field> injectMocksFields = new HashSet<Field>();
    private Set<Object> mocks = new HashSet<Object>();

    @Test
    public void testInjectMocksOnFields() {
        // Arrange
        Field field = mock(Field.class);
        injectMocksFields.add(field);
        Object testClassInstance = mock(Object.class);
        DefaultInjectionEngine spyEngine = spy(defaultInjectionEngine);
        doReturn(null).when(spyEngine).orderedInstanceFieldsFrom(any(Class.class));

        // Act
        spyEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        // Assert
        verify(spyEngine, times(1)).injectMocksOnFields(injectMocksFields, mocks, testClassInstance);
    }

    @Test
    public void testInjectMocksOnFields_withInjectMocksAnnotation() {
        // Arrange
        Field field = mock(Field.class);
        injectMocksFields.add(field);
        Object testClassInstance = mock(Object.class);
        DefaultInjectionEngine spyEngine = spy(defaultInjectionEngine);
        FieldInitializer fieldInitializerMock = mock(FieldInitializer.class);
        when(spyEngine.injectMockCandidate(any(Class.class), any(Set.class), any(Object.class))).thenReturn(null);
        when(spyEngine.getInjectMocksFieldInstance(any(Object.class), any(Field.class))).thenReturn(testClassInstance);
        when(spyEngine.getFieldInitializer(testClassInstance, field)).thenReturn(fieldInitializerMock);
        when(fieldInitializerMock.initialize()).thenThrow(MockitoException.class);

        // Act
        spyEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        // Assert
        verify(spyEngine, times(1)).injectMocksOnFields(injectMocksFields, mocks, testClassInstance);
        verify(fieldInitializerMock, times(1)).initialize();
    }

    @Test
    public void testInjectMocksOnFields_withSuperclasses() {
        // Arrange
        Field field = mock(Field.class);
        injectMocksFields.add(field);
        Object testClassInstance = mock(Object.class);
        DefaultInjectionEngine spyEngine = spy(defaultInjectionEngine);
        Class<?> awaitingInjectionClazz = mock(Class.class);
        Set<Object> mockSet = new HashSet<Object>();
        Object fieldInstance = mock(Object.class);
        when(awaitingInjectionClazz.getSuperclass()).thenReturn(Object.class);
        when(spyEngine.getOrderedInstanceFieldsFrom(any(Class.class))).thenReturn(new Field[] {field});
        when(spyEngine.getMockCandidateFilter()).thenReturn(mock(MockCandidateFilter.class));
        when(spyEngine.getMockCandidateFilter().filterCandidate(mockSet, field, fieldInstance)).thenReturn(spyEngine.getMockCandidateFilter());
      
        // Act
        spyEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        // Assert
        verify(spyEngine, times(1)).injectMocksOnFields(injectMocksFields, mocks, testClassInstance);
        verify(spyEngine, times(1)).injectMockCandidate(awaitingInjectionClazz, mockSet, fieldInstance);
        verify(spyEngine.getMockCandidateFilter(), times(1)).filterCandidate(mockSet, field, fieldInstance);
        verify(spyEngine.getMockCandidateFilter(), times(1)).thenInject();
    }
    

    @Test
    public void testInjectMockCandidate() {
        // Arrange
        Field field = mock(Field.class);
        Set<Object> mocks = new HashSet<Object>();
        Object fieldInstance = mock(Object.class);
        DefaultInjectionEngine spyEngine = spy(defaultInjectionEngine);
        when(spyEngine.getOrderedInstanceFieldsFrom(any(Class.class))).thenReturn(new Field[] {field});
        when(spyEngine.getMockCandidateFilter()).thenReturn(mock(MockCandidateFilter.class));
        when(spyEngine.getMockCandidateFilter().filterCandidate(mocks, field, fieldInstance)).thenReturn(spyEngine.getMockCandidateFilter());

        // Act
        spyEngine.injectMockCandidate(Field.class, mocks, fieldInstance);

        // Assert
        verify(spyEngine, times(1)).getOrderedInstanceFieldsFrom(Field.class);
        verify(spyEngine, times(1)).getMockCandidateFilter();
        verify(spyEngine.getMockCandidateFilter(), times(1)).filterCandidate(mocks, field, fieldInstance);
        verify(spyEngine.getMockCandidateFilter(), times(1)).thenInject();
    }
    

    @Test
    public void testOrderedInstanceFieldsFrom() {
        // Arrange
        Class<?> awaitingInjectionClazz = mock(Class.class);
        DefaultInjectionEngine spyEngine = spy(defaultInjectionEngine);
        when(awaitingInjectionClazz.getDeclaredFields()).thenReturn(new Field[] {mock(Field.class)});
        when(spyEngine.getSupertypesLastComparator()).thenReturn(mock(Comparator.class));

        // Act
        Field[] result = spyEngine.orderedInstanceFieldsFrom(awaitingInjectionClazz);

        // Assert
        verify(awaitingInjectionClazz, times(1)).getDeclaredFields();
        verify(spyEngine, times(1)).getSupertypesLastComparator();
    }
    
    @Test
    public void testGetSupertypesLastComparator() {
        // Arrange
        DefaultInjectionEngine spyEngine = spy(defaultInjectionEngine);

        // Act
        Comparator<Field> result = spyEngine.getSupertypesLastComparator();

        // Assert
        assertNotEquals(null, result);
    }
    
    @Test
    public void testGetOrderedInstanceFieldsFrom() {
        // Arrange
        Class<?> awaitingInjectionClazz = mock(Class.class);
        DefaultInjectionEngine spyEngine = spy(defaultInjectionEngine);
        Field[] arr = new Field[] {mock(Field.class)};
        when(awaitingInjectionClazz.getDeclaredFields()).thenReturn(arr);
        when(spyEngine.getSupertypesLastComparator()).thenReturn(mock(Comparator.class));

        // Act
        Field[] result = spyEngine.getOrderedInstanceFieldsFrom(awaitingInjectionClazz);

        // Assert
        assertArrayEquals(arr, result);
    }
    

    @Test
    public void testGetInjectMocksFieldInstance() {
        // Arrange
        Field field = mock(Field.class);
        Object testClassInstance = mock(Object.class);
        DefaultInjectionEngine spyEngine = spy(defaultInjectionEngine);
        when(field.getType()).thenReturn(testClassInstance.getClass());

        // Act
        Object result = spyEngine.getInjectMocksFieldInstance(testClassInstance, field);

        // Assert
        assertEquals(testClassInstance, result);
    }
    

    @Test
    public void testGetFieldInitializer() {
        // Arrange
        Field field = mock(Field.class);
        Object testClassInstance = mock(Object.class);
        DefaultInjectionEngine spyEngine = spy(defaultInjectionEngine);
        Constructor<DefaultInjectionEngine> constructor = DefaultInjectionEngine.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        // Act
        FieldInitializer result = spyEngine.getFieldInitializer(testClassInstance, field);

        // Assert
        assertNotEquals(null, result);
    }
    
    @Test
    public void testGetMockCandidateFilter() {
        // Arrange
        DefaultInjectionEngine spyEngine = spy(defaultInjectionEngine);

        // Act
        MockCandidateFilter result = spyEngine.getMockCandidateFilter();

        // Assert
        assertNotEquals(null, result);
    }
    
    @Test
    public void testGetSupertypesLastComparator_compare() {
        // Arrange
        DefaultInjectionEngine.SupertypesLastComparator comparator = new DefaultInjectionEngine.SupertypesLastComparator();
        Field field1 = mock(Field.class);
        Field field2 = mock(Field.class);
        when(field1.getType()).thenReturn(Object.class);
        when(field2.getType()).thenReturn(Object.class);
        
        // Act
        int result = comparator.compare(field1, field2);

        // Assert
        assertEquals(0, result);
    }

}