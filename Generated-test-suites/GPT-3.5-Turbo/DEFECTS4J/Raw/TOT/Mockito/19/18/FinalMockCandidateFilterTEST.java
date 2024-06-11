import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import static org.mockito.Mockito.*;

public class FinalMockCandidateFilterTest {

    private FinalMockCandidateFilter filter;

    @Before
    public void setUp() {
        filter = new FinalMockCandidateFilter();
    }

    @Test
    public void testFilterCandidate_withOneMock_shouldInjectUsingPropertySetter() {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(Object.class));
        Field field = getField("fieldName");
        Object fieldInstance = mock(Object.class);

        filter.filterCandidate(mocks, field, fieldInstance).thenInject();

        verify(new BeanPropertySetter(fieldInstance, field), times(1)).set(matches(mocks.iterator().next()));
        verifyNoMoreInteractions(new BeanPropertySetter(fieldInstance, field));
        verifyNoMoreInteractions(new FieldSetter(fieldInstance, field));
        verifyNoMoreInteractions(new Reporter());
    }

    @Test
    public void testFilterCandidate_withOneMock_shouldInjectUsingFieldAccess() {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(Object.class));
        Field field = getField("fieldName");
        Object fieldInstance = mock(Object.class);

        when(new BeanPropertySetter(fieldInstance, field).set(matches(mocks.iterator().next()))).thenReturn(false);

        filter.filterCandidate(mocks, field, fieldInstance).thenInject();

        verify(new BeanPropertySetter(fieldInstance, field), times(1)).set(matches(mocks.iterator().next()));
        verify(new FieldSetter(fieldInstance, field), times(1)).set(matches(mocks.iterator().next()));
        verifyNoMoreInteractions(new BeanPropertySetter(fieldInstance, field));
        verifyNoMoreInteractions(new FieldSetter(fieldInstance, field));
        verifyNoMoreInteractions(new Reporter());
    }

    @Test
    public void testFilterCandidate_withMultipleMocks_shouldNotInject() {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(Object.class));
        mocks.add(mock(Object.class));
        Field field = getField("fieldName");
        Object fieldInstance = mock(Object.class);

        filter.filterCandidate(mocks, field, fieldInstance).thenInject();

        verifyNoMoreInteractions(new BeanPropertySetter(fieldInstance, field));
        verifyNoMoreInteractions(new FieldSetter(fieldInstance, field));
        verifyNoMoreInteractions(new Reporter());
    }

    private Field getField(String name) {
        try {
            return FinalMockCandidateFilter.class.getDeclaredField(name);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }
}