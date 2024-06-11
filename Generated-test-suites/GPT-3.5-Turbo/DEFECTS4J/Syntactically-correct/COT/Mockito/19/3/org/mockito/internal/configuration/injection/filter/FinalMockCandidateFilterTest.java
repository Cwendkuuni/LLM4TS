import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.*;

public class FinalMockCandidateFilterTest {

    private FinalMockCandidateFilter finalMockCandidateFilter;
    private Collection<Object> mocks;
    private Field field;
    private Object fieldInstance;

    @Before
    public void setUp() {
        finalMockCandidateFilter = new FinalMockCandidateFilter();
        mocks = new ArrayList<>();
        field = mock(Field.class);
        fieldInstance = new Object();
    }

    @Test
    public void testFilterCandidate_withSingleMock() {
        Object mock = mock(Object.class);
        mocks.add(mock);

        OngoingInjecter injecter = finalMockCandidateFilter.filterCandidate(mocks, field, fieldInstance);
        Object result = injecter.thenInject();

        verify(field, never()).setAccessible(true);
        verifyZeroInteractions(new BeanPropertySetter(anyObject(), anyObject()), new FieldSetter(anyObject(), anyObject()));
        verifyZeroInteractions(new Reporter());

        assertSame(mock, result);
    }

    @Test
    public void testFilterCandidate_withMultipleMocks() {
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        mocks.addAll(Arrays.asList(mock1, mock2));

        OngoingInjecter injecter = finalMockCandidateFilter.filterCandidate(mocks, field, fieldInstance);
        Object result = injecter.thenInject();

        verifyZeroInteractions(field, new BeanPropertySetter(anyObject(), anyObject()), new FieldSetter(anyObject(), anyObject()));
        verifyZeroInteractions(new Reporter());

        assertNull(result);
    }

}