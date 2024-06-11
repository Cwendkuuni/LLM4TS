import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.injection.filter.*;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import static org.mockito.Mockito.*;

public class FinalMockCandidateFilterTest {

    private FinalMockCandidateFilter filter;
    private Collection<Object> mocks;
    private Field field;
    private Object fieldInstance;
    private Reporter reporter;

    @Before
    public void setUp() {
        filter = new FinalMockCandidateFilter();
        mocks = new HashSet<>();
        field = getField();
        fieldInstance = getFieldInstance();
        reporter = mock(Reporter.class);
    }

    @Test
    public void testFilterCandidateWithSingleMock() {
        Object mock1 = mock(Object.class);
        mocks.add(mock1);

        Object result = filter.filterCandidate(mocks, field, fieldInstance).thenInject();

        verify(mock1, times(1)).equals(eq(fieldInstance));
        verifyNoMoreInteractions(mock1);
        verifyZeroInteractions(field);
        verifyZeroInteractions(reporter);
        assertNull(result);
    }

    @Test
    public void testFilterCandidateWithMultipleMocks() {
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        mocks.add(mock1);
        mocks.add(mock2);

        Object result = filter.filterCandidate(mocks, field, fieldInstance).thenInject();

        verifyZeroInteractions(mock1);
        verifyZeroInteractions(mock2);
        verifyZeroInteractions(field);
        verifyZeroInteractions(reporter);
        assertNull(result);
    }

    private Field getField() {
        try {
            return FinalMockCandidateFilter.class.getDeclaredField("field");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Object getFieldInstance() {
        try {
            return FinalMockCandidateFilter.class.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}