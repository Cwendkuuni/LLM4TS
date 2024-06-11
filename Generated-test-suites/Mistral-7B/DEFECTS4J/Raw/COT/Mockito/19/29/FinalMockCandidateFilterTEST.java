import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InvocationHandler;
import org.mockito.Mockito;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.OngoingInjecter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.junit.RunnerNullableSources;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(RunnerNullableSources.class)
public class FinalMockCandidateFilterTest {

    @Test
    public void testFilterCandidate_SingleMock_BeanPropertySetterSuccess() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Object mock = Mockito.spy(new Object());
        Object fieldInstance = new Object();
        Field field = Mockito.mock(Field.class);

        MockCandidateFilter result = filter.filterCandidate(Collections.singletonList(mock), field, fieldInstance);

        assertNotNull(result);
        assertTrue(result instanceof OngoingInjecter);
        OngoingInjecter ongoingInjecter = (OngoingInjecter) result;
        Object injectedObject = ongoingInjecter.thenInject();
        assertSame(mock, injectedObject);
    }

    @Test
    public void testFilterCandidate_SingleMock_BeanPropertySetterFailure() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Object mock = Mockito.spy(new Object());
        Object fieldInstance = new Object();
        Field field = Mockito.mock(Field.class);

        when(field.canSet(fieldInstance, Mockito.anyObject())).thenReturn(false);

        MockCandidateFilter result = filter.filterCandidate(Collections.singletonList(mock), field, fieldInstance);

        assertNotNull(result);
        assertTrue(result instanceof OngoingInjecter);
        OngoingInjecter ongoingInjecter = (OngoingInjecter) result;
        Object injectedObject = ongoingInjecter.thenInject();
        assertNull(injectedObject);
    }

    @Test
    public void testFilterCandidate_SingleMock_FieldSetterSuccess() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Object mock = Mockito.spy(new Object());
        Object fieldInstance = new Object();
        Field field = Mockito.mock(Field.class);

        when(field.canSet(Mockito.eq(fieldInstance), Mockito.anyObject())).thenReturn(true);

        MockCandidateFilter result = filter.filterCandidate(Collections.singletonList(mock), field, fieldInstance);

        assertNotNull(result);
        assertTrue(result instanceof OngoingInjecter);
        OngoingInjecter ongoingInjecter = (OngoingInjecter) result;
        Object injectedObject = ongoingInjecter.thenInject();
        assertSame(mock, injectedObject);
    }

    @Test
    public void testFilterCandidate_MultipleMocks_BeanPropertySetterFailure_ForAll() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Object mock1 = Mockito.spy(new Object());
        Object mock2 = Mockito.spy(new Object());
        Object fieldInstance = new Object();
        Field field = Mockito.mock(Field.class);

        when(field.canSet(fieldInstance, Mockito.anyObject())).thenReturn(false);

        List<Object> mocks = new ArrayList<>();
        mocks.add(mock1);
        mocks.add(mock2);

        MockCandidateFilter result = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(result);
        assertTrue(result instanceof OngoingInjecter);
        OngoingInjecter ongoingInjecter = (OngoingInjecter) result;
        assertNull(ongoingInjecter.thenInject());
    }

    @Test
    public void testFilterCandidate_MultipleMocks_BeanPropertySetterFailure_ForOne() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Object mock1 = Mockito.spy(new Object());
        Object mock2 = Mockito.spy(new Object());
        Object fieldInstance = new Object();
        Field field = Mockito.mock(Field.class);

        when(field.canSet(fieldInstance, Mockito.eq(mock1))).thenReturn(false);
        when(field.canSet(fieldInstance, Mockito.eq(mock2))).thenReturn(true);

        List<Object> mocks = new ArrayList<>();
        mocks.add(mock1);
        mocks.add(mock2);

        MockCandidateFilter result = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(result);
        assertTrue(result instanceof OngoingInjecter);
        OngoingInjecter ongoingInjecter = (OngoingInjecter) result;
        Object injectedObject = ongoingInjecter.thenInject();
        assertSame(mock2, injectedObject);
    }

    @Test(expected = RuntimeException.class)
    public void testFilterCandidate_ExceptionThrown() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Object mock = Mockito.spy(new Object());
        Object fieldInstance = new Object();
        Field field = Mockito.mock(Field.class);

        when(field.canSet(fieldInstance, Mockito.anyObject())).thenThrow(RuntimeException.class);

        MockCandidateFilter result = filter.filterCandidate(Collections.singletonList(mock), field, fieldInstance);

        assertNotNull(result);
        assertTrue(result instanceof OngoingInjecter);
        OngoingInjecter ongoingInjecter = (OngoingInjecter) result;
        ongoingInjecter.thenInject();
    }

    @Test
    public void testFilterCandidate_MultipleMocks_NoMatchingMock() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Object mock1 = Mockito.spy(new Object());
        Object mock2 = Mockito.spy(new Object());
        Object fieldInstance = new Object();
        Field field = Mockito.mock(Field.class);

        List<Object> mocks = new ArrayList<>();
        mocks.add(mock1);
        mocks.add(mock2);

        MockCandidateFilter result = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(result);
        assertTrue(result instanceof OngoingInjecter);
        OngoingInjecter ongoingInjecter = (OngoingInjecter) result;
        assertNull(ongoingInjecter.thenInject());
    }

    @Test
    public void testFilterCandidate_NullMockCollection() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Object fieldInstance = new Object();
        Field field = Mockito.mock(Field.class);

        MockCandidateFilter result = filter.filterCandidate(null, field, fieldInstance);

        assertNotNull(result);
        assertTrue(result instanceof OngoingInjecter);
        OngoingInjecter ongoingInjecter = (OngoingInjecter) result;
        assertNull(ongoingInjecter.thenInject());
    }

    @Test
    public void testFilterCandidate_NullFieldInstance() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = new ArrayList<>();
        Object mock = Mockito.spy(new Object());
        mocks.add(mock);
        Field field = Mockito.mock(Field.class);

        MockCandidateFilter result = filter.filterCandidate(mocks, field, null);

        assertNotNull(result);
        assertTrue(result instanceof OngoingInjecter);
        OngoingInjecter ongoingInjecter = (OngoingInjecter) result;
        assertNull(ongoingInjecter.thenInject());
    }

    @Test
    public void testFilterCandidate_NullField() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = new ArrayList<>();
        Object mock = Mockito.spy(new Object());
        mocks.add(mock);
        Object fieldInstance = new Object();

        MockCandidateFilter result = filter.filterCandidate(mocks, null, fieldInstance);

        assertNotNull(result);
        assertTrue(result instanceof OngoingInjecter);
        OngoingInjecter ongoingInjecter = (OngoingInjecter) result;
        assertNull(ongoingInjecter.thenInject());
    }
}