import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

@RunWith(MockitoJUnitRunner.class)
public class FinalMockCandidateFilterTest {

    @Mock
    private Field field;
    @Mock
    private Object fieldInstance;

    @Test
    public void testFilterCandidate_WhenMocksHasOneElement_ThenReturnMatchingMock() {
        Collection<Object> mocks = new ArrayList<>();
        Object mock = Mockito.mock(Object.class);
        mocks.add(mock);

        FinalMockCandidateFilter finalMockCandidateFilter = new FinalMockCandidateFilter();
        FinalMockCandidateFilter.OngoingInjecter ongoingInjecter = finalMockCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        Object result = ongoingInjecter.thenInject();

        Mockito.verify(new BeanPropertySetter(fieldInstance, field)).set(mock);
        Mockito.verifyZeroInteractions(new FieldSetter(fieldInstance, field));
        Mockito.verifyZeroInteractions(new Reporter());

        Mockito.verifyNoMoreInteractions(fieldInstance, field, mock);

        Assert.assertEquals(mock, result);
    }

    @Test
    public void testFilterCandidate_WhenMocksIsEmpty_ThenReturnNull() {
        Collection<Object> mocks = new ArrayList<>();

        FinalMockCandidateFilter finalMockCandidateFilter = new FinalMockCandidateFilter();
        FinalMockCandidateFilter.OngoingInjecter ongoingInjecter = finalMockCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        Object result = ongoingInjecter.thenInject();

        Mockito.verifyZeroInteractions(new BeanPropertySetter(fieldInstance, field));
        Mockito.verifyZeroInteractions(new FieldSetter(fieldInstance, field));
        Mockito.verifyZeroInteractions(new Reporter());

        Assert.assertNull(result);
    }

    @Test
    public void testFilterCandidate_WhenMocksHasMultipleElements_ThenReturnNull() {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(Mockito.mock(Object.class));
        mocks.add(Mockito.mock(Object.class));

        FinalMockCandidateFilter finalMockCandidateFilter = new FinalMockCandidateFilter();
        FinalMockCandidateFilter.OngoingInjecter ongoingInjecter = finalMockCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        Object result = ongoingInjecter.thenInject();

        Mockito.verifyZeroInteractions(new BeanPropertySetter(fieldInstance, field));
        Mockito.verifyZeroInteractions(new FieldSetter(fieldInstance, field));
        Mockito.verifyZeroInteractions(new Reporter());

        Assert.assertNull(result);
    }
}