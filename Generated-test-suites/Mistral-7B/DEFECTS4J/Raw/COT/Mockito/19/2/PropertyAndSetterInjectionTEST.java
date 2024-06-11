import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.configuration.injection.PropertyAndSetterInjection;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.reflection.Whitebox;
import org.mockito.mock.MockitoMock;
import org.mockito.stub.Answer;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class PropertyAndSetterInjectionTest {

    @Test
    public void testProcessInjection_InjectMocksAnnotatedFieldIsInitialized() throws Exception {
        PropertyAndSetterInjection propertyAndSetterInjection = new PropertyAndSetterInjection();
        Object injectMocksFieldOwner = mock(Object.class);
        Field injectMocksField = Whitebox.getInternalState(injectMocksFieldOwner, "injectMocksField");
        injectMocksField.setAccessible(true);
        injectMocksField.set(injectMocksFieldOwner, null);
        Set<Object> mockCandidates = new HashSet<>();
        Object mock = mock(MockCandidateFilter.class);
        mockCandidates.add(mock);

        FieldInitializationReport report = propertyAndSetterInjection.initializeInjectMocksField(injectMocksField, injectMocksFieldOwner);
        Assert.assertNotNull(report);

        boolean injectionOccurred = propertyAndSetterInjection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);
        Assert.assertTrue(injectionOccurred);
        Assert.assertNull(injectMocksField.get(injectMocksFieldOwner));
    }

    @Test(expected = MockitoException.class)
    public void testProcessInjection_InjectMocksAnnotatedFieldThrowsExceptionDuringInitialization() throws Exception {
        PropertyAndSetterInjection propertyAndSetterInjection = new PropertyAndSetterInjection();
        Object injectMocksFieldOwner = mock(Object.class);
        Field injectMocksField = Whitebox.getInternalState(injectMocksFieldOwner, "injectMocksField");
        injectMocksField.setAccessible(true);
        injectMocksField.set(injectMocksFieldOwner, null);
        Set<Object> mockCandidates = new HashSet<>();
        Object mock = mock(MockCandidateFilter.class);
        mockCandidates.add(mock);

        FieldInitializationReport report = propertyAndSetterInjection.initializeInjectMocksField(injectMocksField, injectMocksFieldOwner);
        Assert.assertNotNull(report);

        Whitebox.setInternalState(report, "exception", new MockitoException("Exception during initialization"));

        boolean injectionOccurred = propertyAndSetterInjection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);
        Assert.assertFalse(injectionOccurred);
    }

    @Test
    public void testProcessInjection_NoMockCandidatesFound() throws Exception {
        PropertyAndSetterInjection propertyAndSetterInjection = new PropertyAndSetterInjection();
        Object injectMocksFieldOwner = mock(Object.class);
        Field injectMocksField = Whitebox.getInternalState(injectMocksFieldOwner, "injectMocksField");
        injectMocksField.setAccessible(true);
        injectMocksField.set(injectMocksFieldOwner, null);
        Set<Object> mockCandidates = new HashSet<>();

        boolean injectionOccurred = propertyAndSetterInjection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);
        Assert.assertFalse(injectionOccurred);
    }

    @Test
    public void testProcessInjection_MockCandidateFoundByType() throws Exception {
        PropertyAndSetterInjection propertyAndSetterInjection = new PropertyAndSetterInjection();
        Object injectMocksFieldOwner = mock(Object.class);
        Field injectMocksField = Whitebox.getInternalState(injectMocksFieldOwner, "injectMocksField");
        injectMocksField.setAccessible(true);
        injectMocksField.set(injectMocksFieldOwner, null);

        Object mock = mock(MockCandidateFilter.class);
        Set<Object> mockCandidates = new HashSet<>();
        mockCandidates.add(mock);

        Class<?> awaitingInjectionClazz = PropertyAndSetterInjection.class;
        Object instance = mock(awaitingInjectionClazz);

        boolean injectionOccurred = propertyAndSetterInjection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);
        Assert.assertTrue(injectionOccurred);
        verify(mock).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Object mockInstance = injectMocksFieldOwner;
                Whitebox.setInternalState(mockInstance, "injectMocksField", instance);
                return null;
            }
        });
    }

    @Test
    public void testProcessInjection_MockCandidateFoundByType_MultipleCandidates() throws Exception {
        PropertyAndSetterInjection propertyAndSetterInjection = new PropertyAndSetterInjection();
        Object injectMocksFieldOwner = mock(Object.class);
        Field injectMocksField = Whitebox.getInternalState(injectMocksFieldOwner, "injectMocksField");
        injectMocksField.setAccessible(true);
        injectMocksField.set(injectMocksFieldOwner, null);

        Object mock1 = mock(MockCandidateFilter.class);
        Object mock2 = mock(MockCandidateFilter.class);
        Set<Object> mockCandidates = new HashSet<>();
        mockCandidates.addAll(Arrays.asList(mock1, mock2));

        Class<?> awaitingInjectionClazz = PropertyAndSetterInjection.class;
        Object instance1 = mock(awaitingInjectionClazz);
        Object instance2 = mock(awaitingInjectionClazz);

        boolean injectionOccurred = propertyAndSetterInjection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);
        Assert.assertTrue(injectionOccurred);
        verify(mock1).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Object mockInstance = injectMocksFieldOwner;
                Whitebox.setInternalState(mockInstance, "injectMocksField", instance1);
                return null;
            }
        });
        verify(mock2).never().thenAnswer(any());
    }

    @Test(expected = MockitoException.class)
    public void testProcessInjection_MockCandidateFoundByName_ExceptionDuringSetterInjection() throws Exception {
        PropertyAndSetterInjection propertyAndSetterInjection = new PropertyAndSetterInjection();
        Object injectMocksFieldOwner = mock(Object.class);
        Field injectMocksField = Whitebox.getInternalState(injectMocksFieldOwner, "injectMocksField");
        injectMocksField.setAccessible(true);
        injectMocksField.set(injectMocksFieldOwner, null);

        Object mock = mock(MockCandidateFilter.class);
        Set<Object> mockCandidates = new HashSet<>();
        mockCandidates.add(mock);

        Class<?> awaitingInjectionClazz = PropertyAndSetterInjection.class;
        Object instance = mock(awaitingInjectionClazz);

        Field field = Whitebox.getInternalState(mock, "fieldToInject");
        field.setAccessible(true);
        field.set(mock, null);

        Whitebox.setInternalState(mock, "exceptionDuringSetterInjection", new MockitoException("Exception during setter injection"));

        boolean injectionOccurred = propertyAndSetterInjection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);
        Assert.assertFalse(injectionOccurred);
    }

    @Test
    public void testProcessInjection_MockCandidateFoundByName() throws Exception {
        PropertyAndSetterInjection propertyAndSetterInjection = new PropertyAndSetterInjection();
        Object injectMocksFieldOwner = mock(Object.class);
        Field injectMocksField = Whitebox.getInternalState(injectMocksFieldOwner, "injectMocksField");
        injectMocksField.setAccessible(true);
        injectMocksField.set(injectMocksFieldOwner, null);

        Object mock = spy(new Object() {
            @Override
            public String toString() {
                return "MockCandidate";
            }
        });
        Set<Object> mockCandidates = new HashSet<>();
        mockCandidates.add(mock);

        Class<?> awaitingInjectionClazz = PropertyAndSetterInjection.class;
        Object instance = mock(awaitingInjectionClazz);

        Field field = Whitebox.getInternalState(mock, "fieldToInject");
        field.setAccessible(true);
        field.set(mock, null);

        boolean injectionOccurred = propertyAndSetterInjection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);
        Assert.assertTrue(injectionOccurred);
        verify(mock).setFieldToInject(instance);
    }

    @Test
    public void testProcessInjection_NoMockCandidatesFoundByName() throws Exception {
        PropertyAndSetterInjection propertyAndSetterInjection = new PropertyAndSetterInjection();
        Object injectMocksFieldOwner = mock(Object.class);
        Field injectMocksField = Whitebox.getInternalState(injectMocksFieldOwner, "injectMocksField");
        injectMocksField.setAccessible(true);
        injectMocksField.set(injectMocksFieldOwner, null);

        Object mock = mock(MockCandidateFilter.class);
        Set<Object> mockCandidates = new HashSet<>();
        mockCandidates.add(mock);

        Class<?> awaitingInjectionClazz = PropertyAndSetterInjection.class;
        Object instance = mock(awaitingInjectionClazz);

        boolean injectionOccurred = propertyAndSetterInjection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);
        Assert.assertFalse(injectionOccurred);
    }

    @Test
    public void testInjectMockCandidates_InjectCandidatesOnFields() throws Exception {
        PropertyAndSetterInjection propertyAndSetterInjection = new PropertyAndSetterInjection();

        Object awaitingInjectionInstance = mock(Object.class);
        Set<Object> mocks = new HashSet<>();
        mocks.add(mock(Object.class));
        mocks.add(mock(Object.class));

        List<Field> orderedInstanceFields = Arrays.asList(new Field[] {
                new Field() {
                    @Override
                    public AccessibleObject get(Object obj) throws IllegalAccessException {
                        return null;
                    }

                    @Override
                    public void set(Object obj, Object value) throws IllegalAccessException {

                    }

                    @Override
                    public Class<?> getType() {
                        return Object.class;
                    }

                    @Override
                    public String getName() {
                        return "field1";
                    }
                },
                new Field() {
                    @Override
                    public AccessibleObject get(Object obj) throws IllegalAccessException {
                        return null;
                    }

                    @Override
                    public void set(Object obj, Object value) throws IllegalAccessException {

                    }

                    @Override
                    public Class<?> getType() {
                        return Object.class;
                    }

                    @Override
                    public String getName() {
                        return "field2";
                    }
                }
        });

        boolean injectionOccurred = propertyAndSetterInjection.injectMockCandidates(awaitingInjectionInstance.getClass(), mocks, awaitingInjectionInstance);
        Assert.assertTrue(injectionOccurred);
    }

    @Test
    public void testInjectMockCandidates_InjectCandidatesOnFields_MultipleCandidates() throws Exception {
        PropertyAndSetterInjection propertyAndSetterInjection = new PropertyAndSetterInjection();

        Object awaitingInjectionInstance = mock(Object.class);
        Set<Object> mocks = new HashSet<>();
        mocks.add(mock(Object.class));
        mocks.add(mock(Object.class));

        List<Field> orderedInstanceFields = Arrays.asList(new Field[] {
                new Field() {
                    @Override
                    public AccessibleObject get(Object obj) throws IllegalAccessException {
                        return null;
                    }

                    @Override
                    public void set(Object obj, Object value) throws IllegalAccessException {

                    }

                    @Override
                    public Class<?> getType() {
                        return Object.class;
                    }

                    @Override
                    public String getName() {
                        return "field1";
                    }
                },
                new Field() {
                    @Override
                    public AccessibleObject get(Object obj) throws IllegalAccessException {
                        return null;
                    }

                    @Override
                    public void set(Object obj, Object value) throws IllegalAccessException {

                    }

                    @Override
                    public Class<?> getType() {
                        return Object.class;
                    }

                    @Override
                    public String getName() {
                        return "field2";
                    }
                },
                new Field() {
                    @Override
                    public AccessibleObject get(Object obj) throws IllegalAccessException {
                        return null;
                    }

                    @Override
                    public void set(Object obj, Object value) throws IllegalAccessException {

                    }

                    @Override
                    public Class<?> getType() {
                        return Object.class;
                    }

                    @Override
                    public String getName() {
                        return "field3";
                    }
                }
        });

        boolean injectionOccurred = propertyAndSetterInjection.injectMockCandidates(awaitingInjectionInstance.getClass(), mocks, awaitingInjectionInstance);
        Assert.assertTrue(injectionOccurred);
    }

    @Test
    public void testInjectMockCandidates_NoCandidatesFound() throws Exception {
        PropertyAndSetterInjection propertyAndSetterInjection = new PropertyAndSetterInjection();

        Object awaitingInjectionInstance = mock(Object.class);
        Set<Object> mocks = new HashSet<>();
        mocks.add(mock(Object.class));
        mocks.add(mock(Object.class));

        List<Field> orderedInstanceFields = Arrays.asList(new Field[] {
                new Field() {
                    @Override
                    public AccessibleObject get(Object obj) throws IllegalAccessException {
                        return null;
                    }

                    @Override
                    public void set(Object obj, Object value) throws IllegalAccessException {

                    }

                    @Override
                    public Class<?> getType() {
                        return Object.class;
                    }

                    @Override
                    public String getName() {
                        return "field1";
                    }
                },
                new Field() {
                    @Override
                    public AccessibleObject get(Object obj) throws IllegalAccessException {
                        return null;
                    }

                    @Override
                    public void set(Object obj, Object value) throws IllegalAccessException {

                    }

                    @Override
                    public Class<?> getType() {
                        return Object.class;
                    }

                    @Override
                    public String getName() {
                        return "field2";
                    }
                }
        });

        boolean injectionOccurred = propertyAndSetterInjection.injectMockCandidates(awaitingInjectionInstance.getClass(), mocks, awaitingInjectionInstance);
        Assert.assertFalse(injectionOccurred);
    }
}