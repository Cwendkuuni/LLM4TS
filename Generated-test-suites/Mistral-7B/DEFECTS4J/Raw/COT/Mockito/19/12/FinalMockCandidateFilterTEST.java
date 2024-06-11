import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.internal.util.reflection.Whitebox;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

public class FinalMockCandidateFilterTest {

    @Test
    public void testFilterCandidateWithSingleMock() {
        MockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = new ArrayList<>();
        Object mock = Mockito.mock(Object.class);
        mocks.add(mock);
        Field field = Mockito.mock(Field.class);
        Object fieldInstance = Mockito.mock(Object.class);

        OngoingInjecter actual = filter.filterCandidate(mocks, field, fieldInstance);

        assertEquals(mock, actual.thenInject());
    }

    @Test
    public void testFilterCandidateWithMultipleMocks() {
        MockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(Mockito.mock(Object.class));
        mocks.add(Mockito.mock(Object.class));
        Field field = Mockito.mock(Field.class);
        Object fieldInstance = Mockito.mock(Object.class);

        OngoingInjecter actual = filter.filterCandidate(mocks, field, fieldInstance);

        assertNull(actual.thenInject());
    }

    @Test(expected = MockitoException.class)
    public void testExceptionWhenInjectingMockFail() throws Exception {
        MockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = new ArrayList<>();
        Object mock = Mockito.mock(Object.class);
        mocks.add(mock);
        Field field = Mockito.mock(Field.class);
        Object fieldInstance = Mockito.mock(Object.class);
        Whitebox.setInternalState(fieldInstance, "throwExceptionOnSet", true);

        filter.filterCandidate(mocks, field, fieldInstance);

        // This test should result in a MockitoException
    }

    @Test
    public void testPropertySetterSuccess() {
        MockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = new ArrayList<>();
        Object mock = Mockito.mock(Object.class);
        mocks.add(mock);
        Field field = Mockito.mock(Field.class);
        Object fieldInstance = Mockito.mock(Object.class);
        Object expected = new Object() {
            // Some value
        };

        BeanPropertySetter propertySetter = new BeanPropertySetter(fieldInstance, field);
        propertySetter.set(mock);

        assertEquals(expected, Whitebox.getInternalState(fieldInstance, "injectedMock"));
    }

    @Test
    public void testFieldSetterSuccess() {
        MockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = new ArrayList<>();
        Object mock = Mockito.mock(Object.class);
        mocks.add(mock);
        Field field = Mockito.mock(Field.class);
        Object fieldInstance = Mockito.mock(Object.class);
        Object expected = new Object() {
            // Some value
        };

        FieldSetter fieldSetter = new FieldSetter(fieldInstance, field);
        fieldSetter.set(mock);

        assertEquals(expected, Whitebox.getInternalState(fieldInstance, "injectedMock"));
    }

    @Test
    public void testFilterCandidateWithNullCollection() {
        MockCandidateFilter filter = new FinalMockCandidateFilter();
        Field field = Mockito.mock(Field.class);
        Object fieldInstance = Mockito.mock(Object.class);

        assertNull(filter.filterCandidate(null, field, fieldInstance).thenInject());
    }

    @Test
    public void testFilterCandidateWithNullField() {
        MockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = new ArrayList<>();
        Object mock = Mockito.mock(Object.class);
        mocks.add(mock);
        Object fieldInstance = Mockito.mock(Object.class);

        assertNull(filter.filterCandidate(mocks, null, fieldInstance).thenInject());
    }

    @Test
    public void testFilterCandidateWithNullFieldInstance() {
        MockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = new ArrayList<>();
        Object mock = Mockito.mock(Object.class);
        mocks.add(mock);
        Field field = Mockito.mock(Field.class);

        assertNull(filter.filterCandidate(mocks, field, null).thenInject());
    }
}