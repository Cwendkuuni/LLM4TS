import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoMock;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.injection.PropertyAndSetterInjection;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.util.collections.ListUtil;
import org.mockito.internal.util.collections.Sets;
import org.mockito.internal.util.reflection.Field;
import org.mockito.internal.util.reflection.SuperTypesLastSorter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.internal.util.reflection.Reflections.setFieldValue;
import static org.mockito.internal.util.reflection.Reflections.getFieldValue;

public class PropertyAndSetterInjectionTest {

    @MockitoMock
    static class MockObject {
        @InjectMocks
        private Object mockObjectField;

        public Object getMockObjectField() {
            return mockObjectField;
        }
    }

    @Test
    public void testProcessInjection() {
        PropertyAndSetterInjection propertyAndSetterInjection = new PropertyAndSetterInjection();
        Field injectMocksField = new Field(PropertyAndSetterInjection.class, "injectMocksField");
        Object injectMocksFieldOwner = new Object();
        Set<Object> mockCandidates = new HashSet<Object>();
        mockCandidates.add(new MockObject());

        assertTrue(propertyAndSetterInjection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates));
    }

    @Test
    public void testProcessInjectionNoMockCandidates() {
        PropertyAndSetterInjection propertyAndSetterInjection = new PropertyAndSetterInjection();
        Field injectMocksField = new Field(PropertyAndSetterInjection.class, "injectMocksField");
        Object injectMocksFieldOwner = new Object();
        Set<Object> emptyMockCandidates = new HashSet<Object>();

        assertFalse(propertyAndSetterInjection.processInjection(injectMocksField, injectMocksFieldOwner, emptyMockCandidates));
    }

    @Test(expected = MockitoException.class)
    public void testProcessInjectionThrowsException() {
        PropertyAndSetterInjection propertyAndSetterInjection = new PropertyAndSetterInjection();
        Field injectMocksField = new Field(PropertyAndSetterInjection.class, "injectMocksField");
        Object injectMocksFieldOwner = new Object();
        Set<Object> mockCandidates = new HashSet<Object>();
        mockCandidates.add(new MockObject());

        MockitoException exception = assertThrows(MockitoException.class, () -> propertyAndSetterInjection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates));
        MockitoException cause = exception.getCause();
        InvocationTargetException invocationTargetException = (InvocationTargetException) cause.getCause();
        Throwable realCause = invocationTargetException.getCause();

        new Reporter().fieldInitialisationThrewException(injectMocksField, realCause);
    }

    @Test
    public void testProcessInjectionSetsMockByPropertySetter() {
        PropertyAndSetterInjection propertyAndSetterInjection = new PropertyAndSetterInjection();
        Field injectMocksField = new Field(PropertyAndSetterInjection.class, "injectMocksField");
        Object injectMocksFieldOwner = new Object();
        Set<Object> mockCandidates = new HashSet<Object>();
        mockCandidates.add(new MockObject());

        MockObject mockObject = new MockObject();
        Field mockObjectField = new Field(MockObject.class, "mockObjectField");
        injectMocksFieldOwner.setField(injectMocksField, mockObject);

        assertTrue(mockObjectField.getFieldValue(mockObject) instanceof MockitoMock);
    }

    @Test
    public void testProcessInjectionSetsMockByFieldInjection() {
        PropertyAndSetterInjection propertyAndSetterInjection = new PropertyAndSetterInjection();
        Field injectMocksField = new Field(PropertyAndSetterInjection.class, "injectMocksField");
        Object injectMocksFieldOwner = new Object();
        Set<Object> mockCandidates = new HashSet<Object>();
        mockCandidates.add(new MockObject());

        MockObject mockObject = new MockObject();
        Field mockObjectField = new Field(MockObject.class, "mockObjectField");
        mockObjectField.setAccessible(true);
        mockObjectField.set(mockObject, mock(MockitoMock.class));

        injectMocksFieldOwner.setField(injectMocksField, mockObject);
        assertTrue(injectMocksField.getFieldValue(injectMocksFieldOwner) == mockObject);
    }

    @Test
    public void testProcessInjectionSetsMockByPropertySetterWhenNoField() {
        PropertyAndSetterInjection propertyAndSetterInjection = new PropertyAndSetterInjection();
        Field injectMocksField = new Field(PropertyAndSetterInjection.class, "injectMocksField");
        Object injectMocksFieldOwner = new Object();
        Set<Object> mockCandidates = new HashSet<Object>();
        mockCandidates.add(new MockObject());

        MockObject mockObject = new MockObject();
        injectMocksFieldOwner.setField(injectMocksField, mockObject);

        assertTrue(mockObjectField.getFieldValue(mockObject) instanceof MockitoMock);
    }

    @Test
    public void testProcessInjectionSetsMockByFieldInjectionWhenNoSetter() {
        PropertyAndSetterInjection propertyAndSetterInjection = new PropertyAndSetterInjection();
        Field injectMocksField = new Field(PropertyAndSetterInjection.class, "injectMocksField");
        Object injectMocksFieldOwner = new Object();
        Set<Object> mockCandidates = new HashSet<Object>();
        mockCandidates.add(new MockObject());

        MockObject mockObject = new MockObject();
        Field mockObjectField = new Field(MockObject.class, "mockObjectField");
        mockObjectField.setAccessible(true);

        injectMocksFieldOwner.setField(injectMocksField, mockObject);
        assertTrue(getFieldValue(injectMocksField, injectMocksFieldOwner) == mockObject);
    }

    @Test
    public void testProcessInjectionSetsMockByFieldInjectionWhenNoFieldOrSetter() {
        PropertyAndSetterInjection propertyAndSetterInjection = new PropertyAndSetterInjection();
        Field injectMocksField = new Field(PropertyAndSetterInjection.class, "injectMocksField");
        Object injectMocksFieldOwner = new Object();
        Set<Object> mockCandidates = new HashSet<Object>();
        mockCandidates.add(new MockObject());

        MockObject mockObject = new MockObject();
        MockitoMock mock = mock(MockitoMock.class);
        setFieldValue(injectMocksFieldOwner, "injectMocksField", mock);

        assertTrue(injectMocksField.getFieldValue(injectMocksFieldOwner) == mock);
    }

    @Test
    public void testProcessInjectionSetsMockByPropertySetterWithNoArgumentConstructor() {
        PropertyAndSetterInjection propertyAndSetterInjection = new PropertyAndSetterInjection();
        Field injectMocksField = new Field(PropertyAndSetterInjection.class, "injectMocksField");
        Object injectMocksFieldOwner = new Object();
        Set<Object> mockCandidates = new HashSet<Object>();
        mockCandidates.add(new MockObject());

        MockObject mockObject = new MockObject();
        injectMocksFieldOwner.setField(injectMocksField, mockObject);

        Object newMockObject = new MockObject();
        Field newMockObjectField = new Field(MockObject.class, "mockObjectField");
        newMockObjectField.setAccessible(true);
        newMockObjectField.set(newMockObject, mock(MockitoMock.class));
        mockCandidates.add(newMockObject);

        PropertyAndSetterInjection spyPropertyAndSetterInjection = spy(propertyAndSetterInjection);
        when(spyPropertyAndSetterInjection.initializeInjectMocksField(injectMocksField, injectMocksFieldOwner)).thenReturn(new FieldInitializationReport());
        when(spyPropertyAndSetterInjection.injectMockCandidates(MockObject.class, mockCandidates, mockObject)).thenReturn(true);

        assertTrue(propertyAndSetterInjection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates));
    }
}