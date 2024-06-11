import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.OngoingInjecter;
import org.mockito.internal.util.reflection.FieldSetter;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class FinalMockCandidateFilterTest {

    private FinalMockCandidateFilter filter;
    private Field field;
    private Object fieldInstance;
    private Collection<Object> mocks;

    @Before
    public void setup() throws IllegalAccessException {
        filter = new FinalMockCandidateFilter();
        field = new Field("testField", Object.class);
        fieldInstance = new Object();
        mocks = new ArrayList<>();
    }

    @Test
    public void testFilterCandidateWithSingleMock() {
        Object mock = new Object();
        mocks.add(mock);

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertTrue(injecter.thenInject());
    }

    @Test
    public void testFilterCandidateWithMultipleMocks() {
        mocks.clear();
        mocks.add(new Object());
        mocks.add(new Object());

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertFalse(injecter.thenInject());
    }

    @Test(expected = MockitoException.class)
    public void testFilterCandidateWithExceptionInFieldSetter() throws IllegalAccessException, InvocationTargetException {
        Object mock = new Object();
        mocks.add(mock);

        FieldSetter fieldSetter = new FieldSetter(fieldInstance, field);
        fieldSetter.set(mock, new Object());

        filter.filterCandidate(mocks, field, fieldInstance);
    }

    @Test(expected = MockitoException.class)
    public void testFilterCandidateWithExceptionInFieldAccess() throws IllegalAccessException, InvocationTargetException {
        Object mock = new Object();
        mocks.add(mock);

        filter.filterCandidate(mocks, field, new Object() {
            private Object field;

            public Object getField() {
                throw new RuntimeException("Test exception");
            }
        });
    }

    @Test
    public void testFilterCandidateWithFieldAccessIfPropertySetterFails() throws IllegalAccessException {
        Object mock = new Object();
        mocks.add(mock);

        FieldSetter fieldSetter = new FieldSetter(fieldInstance, field);
        fieldSetter.set(mock, new Object());

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertFalse(injecter.thenInject());

        Object fieldValue = field.get(fieldInstance);
        assertNotSame(mock, fieldValue);

        injecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertTrue(injecter.thenInject());
    }
}