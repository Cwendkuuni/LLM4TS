import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.PropertyAndSetterInjection;
import org.mockito.internal.util.collections.Sets;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.internal.util.reflection.SuperTypesLastSorter;
import org.mockito.mockitosis.annotation.InjectMocks;
import org.mockito.mockitosis.internal.util.collections.ListUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.Arrays;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

/**
 * Test class for PropertyAndSetterInjection.
 */
public class PropertyAndSetterInjectionTest {

    private PropertyAndSetterInjection injection;
    private Class<?> testClass;
    private Object testObject;
    private Set<Object> mockCandidates;

    @Before
    public void setUp() {
        injection = new PropertyAndSetterInjection();
        testClass = mock(Class.class);
        testObject = mock(Object.class);
        mockCandidates = Sets.newMockSafeHashSet();
    }

    @Test
    public void testProcessInjection_Success() throws MockitoException {
        Field injectMocksField = Mockito.spy(new Field("injectMocksField", String.class, Modifier.PRIVATE, testClass));
        Object injectMocksFieldOwner = mock(Object.class);

        // Prepare mock candidates
        Object mock1 = mock(String.class);
        Object mock2 = mock(String.class);
        mockCandidates.addAll(Arrays.asList(mock1, mock2));

        // Call method under test
        boolean result = injection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);

        // Verify that exactly one mock was injected
        assertEquals(1, mockCandidates.size());

        // Verify that the injected mock was removed from the list
        assertFalse(mockCandidates.contains(mock1));
        assertFalse(mockCandidates.contains(mock2));

        // Verify that the field was injected
        verify(injectMocksFieldOwner).setField(injectMocksField.getName(), mock1);
        verify(mock1).thenAnswer((invocation) -> invocation.getArgument(0));
        verify(injectMocksField).set(injectMocksFieldOwner, mock1);

        // Verify that the injected field was removed from the list of class fields
        Field injectedField = FieldInitializer.getField(injectMocksFieldOwner, injectMocksField.getName());
        List<Field> orderedInstanceFields = injection.orderedInstanceFieldsFrom(testClass);
        assertFalse(orderedInstanceFields.contains(injectedField));
    }

    @Test
    public void testProcessInjection_NoMockCandidates() {
        Field injectMocksField = Mockito.spy(new Field("injectMocksField", String.class, Modifier.PRIVATE, testClass));
        Object injectMocksFieldOwner = mock(Object.class);

        // Call method under test
        boolean result = injection.processInjection(injectMocksField, injectMocksFieldOwner, Collections.emptySet());

        // Verify that no mock was injected
        assertTrue(mockCandidates.isEmpty());

        // Verify that no field was injected
        verifyZeroInteractions(injectMocksFieldOwner);
    }

    @Test(expected = MockitoException.class)
    public void testProcessInjection_InitializationFailed() {
        Field injectMocksField = Mockito.spy(new Field("injectMocksField", String.class, Modifier.PRIVATE, testClass));
        Object injectMocksFieldOwner = mock(Object.class);

        // Call method under test with a field that cannot be initialized
        injection.initializeInjectMocksField(injectMocksField, injectMocksFieldOwner);
    }

    @Test(expected = MockitoException.class)
    public void testProcessInjection_MockCandidateNotFound() {
        Field injectMocksField = Mockito.spy(new Field("injectMocksField", String.class, Modifier.PRIVATE, testClass));
        Object injectMocksFieldOwner = mock(Object.class);

        // Prepare mock candidates with only one mock
        Object mock1 = mock(String.class);
        mockCandidates.add(mock1);

        // Call method under test
        injection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);

        // Verify that no exception was thrown
        // (since there was only one mock candidate, it was injected)

        // Verify that the field was injected
        verify(injectMocksFieldOwner).setField(injectMocksField.getName(), mock1);
        verify(mock1).thenAnswer((invocation) -> invocation.getArgument(0));
        verify(injectMocksField).set(injectMocksFieldOwner, mock1);

        // Verify that the injected mock was removed from the list
        assertEquals(0, mockCandidates.size());

        // Test for the case where no mock candidate was found
        mockCandidates.clear();

        // Call method under test
        injection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);
    }
}