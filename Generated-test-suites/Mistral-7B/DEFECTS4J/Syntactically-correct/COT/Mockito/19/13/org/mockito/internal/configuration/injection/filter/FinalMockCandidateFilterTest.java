import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.OngoingInjecter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.exceptions.Reporter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.assertNotNull;
import static org.mockito.Mockito.assertNull;

public class FinalMockCandidateFilterTest {

    private Reporter reporter;
    private Collection<Object> mocks;
    private Field field;
    private Object fieldInstance;

    @Before
    public void init() {
        reporter = mock(Reporter.class);
        mocks = new ArrayList<>();
        field = mock(Field.class);
        fieldInstance = mock(Object.class);
    }

    @Test
    public void testFilterCandidateWithOneMock() {
        Object mock = mock(MockCandidateFilter.class);
        mocks.add(mock);

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(injecter);
        verify(mock).then(invocation -> invocation.getMockitoContext().injector().getInstance(field.getType()));
    }

    @Test
    public void testFilterCandidateWithMultipleMocks() {
        Object mock1 = mock(MockCandidateFilter.class);
        Object mock2 = mock(MockCandidateFilter.class);
        mocks.add(mock1);
        mocks.add(mock2);

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertNull(injecter);
        verifyNoInteractions(mock1, mock2);
    }

    @Test(expected = RuntimeException.class)
    public void testFilterCandidateWithException() {
        Object mock = mock(MockCandidateFilter.class);
        mocks.add(mock);

        FieldSetter fieldSetter = spy(new FieldSetter(fieldInstance, field));
        BeanPropertySetter propertySetter = spy(new BeanPropertySetter(fieldInstance, field));
        when(propertySetter.set(any(), any())).thenThrow(new RuntimeException("Test Exception"));
        when(fieldSetter.set(any(), any())).thenCallRealMethod();

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        injecter.thenInject();
        verify(reporter).cannotInjectDependency(field, mock, isA(RuntimeException.class));
    }
}