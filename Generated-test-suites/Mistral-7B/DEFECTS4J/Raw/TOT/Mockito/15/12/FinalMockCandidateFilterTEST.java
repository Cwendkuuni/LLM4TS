import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.OngoingInjecter;
import org.mockito.internal.util.reflection.FieldSetter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class FinalMockCandidateFilterTest {

    private FinalMockCandidateFilter filter;
    private Collection<Object> mocks;
    private Object fieldInstance;
    private Field field;

    @Before
    public void setup() {
        filter = new FinalMockCandidateFilter();
        mocks = new ArrayList<>();
        fieldInstance = Mockito.mock(Object.class);
        field = Mockito.mock(Field.class);
    }

    @Test
    public void testFilterCandidateSingleMock() {
        Object mock = Mockito.mock(Object.class);
        mocks.add(mock);

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertTrue(injecter instanceof OngoingInjecter);

        assertFalse(injecter.thenInject()); // should not inject, as we don't want to test the injection itself
    }

    @Test
    public void testFilterCandidateMultipleMocks() {
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertFalse(injecter.thenInject()); // should not inject, as we don't have a single matching mock
    }

    @Test
    public void testFilterCandidateWithMatchingMock() {
        Object mock = Mockito.mock(Object.class);
        mocks.add(mock);

        when(field.getName()).thenReturn("testField");

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertTrue(injecter instanceof OngoingInjecter);

        assertTrue(injecter.thenInject()); // should successfully inject the dependency

        verify(mock).setTestField(fieldInstance); // verify that the field was set
    }

    @Test(expected = MockitoException.class)
    public void testFilterCandidateException() {
        when(field.getName()).thenReturn("nonExistentField");

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertFalse(injecter.thenInject()); // should not inject, as we don't have a matching mock

        assertThrows(MockitoException.class, () -> new FieldSetter(fieldInstance, field).set(mock)); // test that an exception is thrown when trying to inject into a non-existent field
    }
}