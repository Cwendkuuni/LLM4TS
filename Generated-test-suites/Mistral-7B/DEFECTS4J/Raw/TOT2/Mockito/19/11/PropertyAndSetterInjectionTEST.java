import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.internal.configuration.injection.PropertyAndSetterInjection;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.util.collections.Sets;
import org.mockito.internal.util.reflection.FieldInitializationReport;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.internal.util.reflection.SuperTypesLastSorter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class PropertyAndSetterInjectionTest {

    @Test
    public void testProcessInjection() throws Exception {
        PropertyAndSetterInjection injection = new PropertyAndSetterInjection();
        Object owner = mock(Object.class);
        Field injectMocksField = Mockito.fieldTypes().getField(Owner.class, "injectMocksField");
        Set<Object> mockCandidates = Sets.newMockSafeHashSet(mock(MockCandidate.class), mock(MockCandidate.class));
        FieldInitializationReport report = new FieldInitializationReport();

        // Test successful injection using setter
        Mockito.doReturn(new Object()).when(owner).newInstance();
        Mockito.doReturn(mockCandidates).when(owner).getMockCandidates();
        Mockito.doReturn(report).when(owner).initializeInjectMocksField(injectMocksField, owner);
        Mockito.doReturn(null).when(owner).injectMockCandidates(Mockito.anyClass(), mock(Set.class), owner);
        injection.processInjection(injectMocksField, owner, mockCandidates);
        verify(owner).setInjectMocksField(mockCandidates.iterator().next());

        // Test successful injection using field
        Mockito.doReturn(null).when(owner).setInjectMocksField(Mockito.any());
        Mockito.doReturn(report).when(owner).initializeInjectMocksField(injectMocksField, owner);
        Mockito.doReturn(null).when(owner).injectMockCandidates(Mockito.anyClass(), mock(Set.class), owner);
        Mockito.doReturn(mockCandidates.iterator().next()).when(injectMocksField).get(owner);
        injection.processInjection(injectMocksField, owner, mockCandidates);
        verify(owner).injectMockCandidates(Mockito.anyClass(), mock(Set.class), owner);
        verify(injectMocksField).set(owner, Mockito.eq(mockCandidates.iterator().next()));

        // Test no injection
        Mockito.doReturn(null).when(owner).getMockCandidates();
        injection.processInjection(injectMocksField, owner, mockCandidates);
        verify(owner, Mockito.never()).setInjectMocksField(Mockito.any());
        verify(owner, Mockito.never()).injectMockCandidates(Mockito.anyClass(), mock(Set.class), owner);
    }

    @Test(expected = IllegalAccessException.class)
    public void testInitializeInjectMocksFieldWithPrivateField() throws Exception {
        PropertyAndSetterInjection injection = new PropertyAndSetterInjection();
        Object owner = mock(Owner.class);
        Field injectMocksField = Mockito.fieldTypes().getField(Owner.class, "injectMocksField");
        injectMocksField.setAccessible(true);
        Set<Object> mockCandidates = Sets.newMockSafeHashSet(mock(MockCandidate.class), mock(MockCandidate.class));
        FieldInitializationReport report = new FieldInitializationReport();

        Mockito.doThrow(new IllegalAccessException()).when(owner).setInjectMocksField(Mockito.any());
        injection.initializeInjectMocksField(injectMocksField, owner);
    }

    @Test(expected = InvocationTargetException.class)
    public void testInitializeInjectMocksFieldWithException() throws Exception {
        PropertyAndSetterInjection injection = new PropertyAndSetterInjection();
        Object owner = mock(Owner.class);
        Field injectMocksField = Mockito.fieldTypes().getField(Owner.class, "injectMocksField");
        Set<Object> mockCandidates = Sets.newMockSafeHashSet(mock(MockCandidate.class), mock(MockCandidate.class));
        FieldInitializationReport report = new FieldInitializationReport();

        Mockito.doThrow(new InvocationTargetException(new RuntimeException())).when(new FieldInitializer(owner, injectMocksField)).initialize();
        injection.initializeInjectMocksField(injectMocksField, owner);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitializeInjectMocksFieldWithNullField() throws Exception {
        PropertyAndSetterInjection injection = new PropertyAndSetterInjection();
        Object owner = mock(Owner.class);
        Set<Object> mockCandidates = Sets.newMockSafeHashSet(mock(MockCandidate.class), mock(MockCandidate.class));
        FieldInitializationReport report = new FieldInitializationReport();

        injection.initializeInjectMocksField(null, owner);
    }

    @Test(expected = IllegalAccessException.class)
    public void testInjectMockCandidatesWithPrivateField() throws Exception {
        PropertyAndSetterInjection injection = new PropertyAndSetterInjection();
        Object owner = mock(Owner.class);
        Class<?> awaitingInjectionClazz = Owner.class;
        Set<Object> mocks = Sets.newMockSafeHashSet(mock(MockCandidate.class), mock(MockCandidate.class));
        Object instance = new Object();

        Field injectMocksField = Mockito.fieldTypes().getField(Owner.class, "injectMocksField");
        injectMocksField.setAccessible(true);
        Field initialInstanceField = Mockito.fieldTypes().getField(Owner.class, "instance");
        initialInstanceField.setAccessible(true);
        initialInstanceField.set(owner, instance);

        Mockito.doThrow(new IllegalAccessException()).when(owner).setInjectMocksField(Mockito.any());
        injection.injectMockCandidates(awaitingInjectionClazz, mocks, instance);
    }

    @Test(expected = InvocationTargetException.class)
    public void testInjectMockCandidatesWithException() throws Exception {
        PropertyAndSetterInjection injection = new PropertyAndSetterInjection();
        Object owner = mock(Owner.class);
        Class<?> awaitingInjectionClazz = Owner.class;
        Set<Object> mocks = Sets.newMockSafeHashSet(mock(MockCandidate.class), mock(MockCandidate.class));
        Object instance = new Object();

        Field injectMocksField = Mockito.fieldTypes().getField(Owner.class, "injectMocksField");
        Field initialInstanceField = Mockito.fieldTypes().getField(Owner.class, "instance");
        initialInstanceField.setAccessible(true);
        initialInstanceField.set(owner, instance);

        Mockito.doThrow(new InvocationTargetException(new RuntimeException())).when(new FieldInitializer(owner, injectMocksField)).initialize();
        injection.injectMockCandidates(awaitingInjectionClazz, mocks, instance);
    }

    @Test
    public void testOrderedInstanceFields() throws Exception {
        PropertyAndSetterInjection injection = new PropertyAndSetterInjection();
        Class<?> awaitingInjectionClazz = Owner.class;

        List<Field> orderedInstanceFields = injection.orderedInstanceFieldsFrom(awaitingInjectionClazz);
        List<String> orderedNames = new ArrayList<>();
        for (Field field : orderedInstanceFields) {
            orderedNames.add(field.getName());
        }

        List<String> expectedNames = Arrays.asList("field1", "field2", "field3");
        Collections.sort(orderedNames);
        assertEquals(expectedNames, orderedNames);
    }
}