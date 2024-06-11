import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.doReturn;

@RunWith(MockitoJUnitRunner.class)
public class FinalMockCandidateFilterTest {

    @Test
    public void testFilterCandidateWithSingleMock() {
        Collection<Object> mocks = new ArrayList<>();
        Object mock = mock(Object.class);
        Object fieldInstance = spy(new Object());
        Field field = Mockito.spy(Field.class);

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        result.thenInject();

        Mockito.verify(fieldInstance, Mockito.times(1)).set(mock);
    }

    @Test
    public void testFilterCandidateWithEmptyMockCollection() {
        Collection<Object> mocks = new ArrayList<>();
        Object fieldInstance = spy(new Object());
        Field field = Mockito.spy(Field.class);

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        Mockito.verifyNoInteractions(fieldInstance);
        Mockito.verify(result, Mockito.times(1)).thenInject();
    }

    @Test
    public void testFilterCandidateWithMultipleMocks() {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(Object.class));
        mocks.add(mock(Object.class));
        Object fieldInstance = spy(new Object());
        Field field = Mockito.spy(Field.class);

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        result.thenInject();

        Mockito.verify(fieldInstance, Mockito.times(1)).set(mocks.get(0));
    }

    @Test(expected = RuntimeException.class)
    public void testFilterCandidateWithFieldNull() {
        Collection<Object> mocks = new ArrayList<>();
        Object fieldInstance = spy(new Object());
        Field field = null;

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        result.thenInject();
    }

    @Test(expected = RuntimeException.class)
    public void testFilterCandidateWithFieldInstanceNull() {
        Collection<Object> mocks = new ArrayList<>();
        Object fieldInstance = null;
        Field field = Mockito.spy(Field.class);

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        result.thenInject();
    }

    @Test
    public void testFilterCandidateWithPropertySetterException() {
        Collection<Object> mocks = new ArrayList<>();
        Object fieldInstance = spy(new Object());
        Field field = Mockito.spy(Field.class);
        Object exception = new RuntimeException();

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        doThrow(exception).when(new BeanPropertySetter(fieldInstance, field)).set(mocks.iterator().next());

        result.thenInject();

        new Reporter().cannotInjectDependency(field, mocks.iterator().next(), exception);
    }

    @Test
    public void testFilterCandidateWithFieldAccessException() {
        Collection<Object> mocks = new ArrayList<>();
        Object fieldInstance = spy(new Object());
        Field field = Mockito.spy(Field.class);
        Object exception = new RuntimeException();

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        doThrow(exception).when(new FieldSetter(fieldInstance, field)).set(mocks.iterator().next());

        result.thenInject();

        new Reporter().cannotInjectDependency(field, mocks.iterator().next(), exception);
    }
}