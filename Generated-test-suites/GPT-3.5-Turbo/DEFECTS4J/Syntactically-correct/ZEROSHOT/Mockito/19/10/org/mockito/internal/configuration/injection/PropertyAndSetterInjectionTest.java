import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

public class PropertyAndSetterInjectionTest {

    @Test
    public void testProcessInjection() throws NoSuchFieldException {
        PropertyAndSetterInjection injection = new PropertyAndSetterInjection();

        Field injectMocksField = PropertyAndSetterInjection.class.getDeclaredField("mockCandidateFilter");
        Object injectMocksFieldOwner = injection;
        Set<Object> mockCandidates = new HashSet<>();

        boolean result = injection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);

        Assert.assertTrue(result);
    }

    @Test
    public void testInitializeInjectMocksField() throws NoSuchFieldException {
        PropertyAndSetterInjection injection = new PropertyAndSetterInjection();

        Field field = PropertyAndSetterInjection.class.getDeclaredField("mockCandidateFilter");
        Object fieldOwner = injection;

        FieldInitializationReport report = injection.initializeInjectMocksField(field, fieldOwner);

        Assert.assertNotNull(report);
    }

    @Test
    public void testInjectMockCandidates() {
        PropertyAndSetterInjection injection = new PropertyAndSetterInjection();

        Class<?> awaitingInjectionClazz = PropertyAndSetterInjection.class;
        Set<Object> mocks = new HashSet<>();
        Object instance = new PropertyAndSetterInjection();

        boolean result = injection.injectMockCandidates(awaitingInjectionClazz, mocks, instance);

        Assert.assertFalse(result);
    }

    @Test
    public void testInjectMockCandidatesOnFields() throws NoSuchFieldException {
        PropertyAndSetterInjection injection = new PropertyAndSetterInjection();

        Set<Object> mocks = new HashSet<>();
        Object instance = new PropertyAndSetterInjection();
        Field field = PropertyAndSetterInjection.class.getDeclaredField("mockCandidateFilter");

        boolean result = injection.injectMockCandidatesOnFields(mocks, instance, false, new ArrayList<>());

        Assert.assertFalse(result);
    }

    @Test
    public void testOrderedInstanceFieldsFrom() {
        PropertyAndSetterInjection injection = new PropertyAndSetterInjection();

        Class<?> awaitingInjectionClazz = PropertyAndSetterInjection.class;

        List<Field> result = injection.orderedInstanceFieldsFrom(awaitingInjectionClazz);

        Assert.assertNotNull(result);
    }
}