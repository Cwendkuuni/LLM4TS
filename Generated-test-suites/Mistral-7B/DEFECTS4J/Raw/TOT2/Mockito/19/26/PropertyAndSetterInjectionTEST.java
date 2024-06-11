import static org.junit.Assert.*;
import org.mockito.Mockito;
import org.mockito.internal.configuration.injection.PropertyAndSetterInjection;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.util.collections.ListUtil;
import org.mockito.internal.util.reflection.FieldInitializationReport;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.internal.util.reflection.SuperTypesLastSorter;

public class PropertyAndSetterInjectionTest {

    @Test
    public void testProcessInjection() {
        PropertyAndSetterInjection injection = new PropertyAndSetterInjection();
        Field injectMocksField = Mockito.mock(Field.class);
        Object injectMocksFieldOwner = Mockito.mock(Object.class);
        Set<Object> mockCandidates = new MockSafeHashSet<>();
        mockCandidates.add(Mockito.mock(Class.class));

        assertTrue(injection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates));
    }
}