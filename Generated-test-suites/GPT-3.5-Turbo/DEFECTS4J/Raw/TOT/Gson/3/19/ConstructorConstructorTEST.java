import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ConstructorConstructorTest {
    private ConstructorConstructor constructorConstructor;

    @Mock
    private TypeToken typeToken;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
        constructorConstructor = new ConstructorConstructor(instanceCreators);
    }

    @Test
    public void testGetWithInstanceCreator() {
        InstanceCreator instanceCreator = mock(InstanceCreator.class);
        when(typeToken.getType()).thenReturn("InstanceCreatorTestType");
        Map<Type, InstanceCreator<?>> instanceCreators = new HashMap<>();
        instanceCreators.put("InstanceCreatorTestType", instanceCreator);
        constructorConstructor = new ConstructorConstructor(instanceCreators);

        ObjectConstructor objectConstructor = constructorConstructor.get(typeToken);

        assertNotNull(objectConstructor);
        verify(instanceCreator, times(1)).createInstance(typeToken.getType());
    }

    @Test
    public void testGetWithRawTypeCreator() {
        InstanceCreator instanceCreator = mock(InstanceCreator.class);
        when(typeToken.getType()).thenReturn("RawTypeCreatorTestType");
        when(typeToken.getRawType()).thenReturn("RawTypeCreatorTestTypeRawType");
        Map<Type, InstanceCreator<?>> instanceCreators = new HashMap<>();
        instanceCreators.put("RawTypeCreatorTestTypeRawType", instanceCreator);
        constructorConstructor = new ConstructorConstructor(instanceCreators);

        ObjectConstructor objectConstructor = constructorConstructor.get(typeToken);

        assertNotNull(objectConstructor);
        verify(instanceCreator, times(1)).createInstance(typeToken.getType());
    }

    @Test
    public void testGetWithDefaultConstructor() throws NoSuchMethodException {
        Constructor defaultConstructor = mock(Constructor.class);
        when(defaultConstructor.isAccessible()).thenReturn(false);
        when(defaultConstructor.getDeclaredConstructor()).thenReturn(defaultConstructor);
        when(defaultConstructor.newInstance()).thenReturn("DefaultConstructorTestResult");

        Class defaultConstructorTestClass = mock(Class.class);
        when(defaultConstructorTestClass.getDeclaredConstructor()).thenReturn(defaultConstructor);

        when(typeToken.getType()).thenReturn("DefaultConstructorTestType");
        when(typeToken.getRawType()).thenReturn(defaultConstructorTestClass);
        Map<Type, InstanceCreator<?>> instanceCreators = new HashMap<>();

        constructorConstructor = new ConstructorConstructor(instanceCreators);

        ObjectConstructor objectConstructor = constructorConstructor.get(typeToken);

        assertNotNull(objectConstructor);
        assertEquals("DefaultConstructorTestResult", objectConstructor.construct());
        verify(defaultConstructor, times(1)).setAccessible(true);
        verify(defaultConstructor, times(1)).newInstance();
    }

    @Test
    public void testGetWithDefaultImplementationConstructor() {
        when(typeToken.getType()).thenReturn("DefaultImplementationConstructorTestType");

        ObjectConstructor objectConstructor = constructorConstructor.get(typeToken);

        assertNotNull(objectConstructor);
        assertNotNull(objectConstructor.construct());
    }

    @Test
    public void testGetWithUnsafeAllocator() {
        when(typeToken.getType()).thenReturn("UnsafeAllocatorTestType");

        ObjectConstructor objectConstructor = constructorConstructor.get(typeToken);

        assertNotNull(objectConstructor);
        assertNotNull(objectConstructor.construct());
    }

    @Test
    public void testGetWithNullTypeToken() {
        when(typeToken.getType()).thenReturn(null);

        ObjectConstructor objectConstructor = constructorConstructor.get(typeToken);

        assertNull(objectConstructor);
    }

    @Test
    public void testGetWithNullTypeFromTypeToken() {
        when(typeToken.getType()).thenReturn(null);
        when(typeToken.getRawType()).thenReturn("RawTypeTest");
        Map<Type, InstanceCreator<?>> instanceCreators = new HashMap<>();

        constructorConstructor = new ConstructorConstructor(instanceCreators);

        ObjectConstructor objectConstructor = constructorConstructor.get(typeToken);

        assertNull(objectConstructor);
    }

    @Test
    public void testGetWithNullInstanceCreators() {
        when(typeToken.getType()).thenReturn("TypeTest");
        when(typeToken.getRawType()).thenReturn("RawTypeTest");
        constructorConstructor = new ConstructorConstructor(null);

        ObjectConstructor objectConstructor = constructorConstructor.get(typeToken);

        assertNull(objectConstructor);
    }

    @Test
    public void testToString() {
        String expected = "{}";

        assertEquals(expected, constructorConstructor.toString());
    }
}