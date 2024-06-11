package org.mockito.internal.configuration.injection.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.internal.util.reflection.Whitebox;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verifyThrowable;
import static org.mockito.internal.util.reflection.Whitebox.getInternalState;

@RunWith(org.mockito.runners.JUnit4.class)
public class FinalMockCandidateFilterTest {

    private static final String FIELD_NAME = "testField";

    @Test
    public void testFilterCandidate_SingleMock_Success() {
        // Given
        Collection<Object> mocks = Arrays.asList(mock(Object.class));
        Object fieldInstance = spy(new Object());
        Field field = Whitebox.getInternalState(fieldInstance, "testField");

        // When
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter actual = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        assert(actual instanceof OngoingInjecter);
        OngoingInjecter ongoingInjecter = (OngoingInjecter) actual;
        assertTrue(ongoingInjecter.thenInject());
        assertEquals(fieldInstance, getInternalState(getInternalState(ongoingInjecter, "fieldInstance"), "injected"));
    }

    @Test
    public void testFilterCandidate_SingleMock_ExceptionThrown() {
        // Given
        Collection<Object> mocks = Arrays.asList(mock(Object.class));
        Object fieldInstance = spy(new Object());
        Field field = Whitebox.getInternalState(fieldInstance, "testField");

        // When & Then
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        assertFalse(filter.filterCandidate(mocks, field, fieldInstance).thenInject());
    }

    @Test
    public void testFilterCandidate_MultipleMocks_NoMatch_ReturnFalse() {
        // Given
        Collection<Object> mocks = Arrays.asList(mock(Object1.class), mock(Object2.class));
        Object fieldInstance = new Object();
        Field field = Whitebox.getInternalState(fieldInstance, "testField");

        // When
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter actual = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        assert(actual instanceof OngoingInjecter);
        assertFalse(actual.thenInject());
    }

    @Test(expected = MockitoException.class)
    public void testFilterCandidate_SingleMock_ExceptionThrownDuringSetterInjection() {
        // Given
        Collection<Object> mocks = Arrays.asList(mock(Object.class));
        Object fieldInstance = spy(new Object());
        Field field = Whitebox.getInternalState(fieldInstance, "testField");

        // When
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        filter.filterCandidate(mocks, field, fieldInstance);

        // Then (exception should be thrown here)
        verifyThrowable(MockitoException.class, () -> new FieldSetter(fieldInstance, field).set(mocks.iterator().next()));
    }

    @Test
    public void testFilterCandidate_SingleMock_ExceptionThrownDuringFieldAccess() {
        // Given
        Collection<Object> mocks = Arrays.asList(mock(Object.class));
        Object fieldInstance = new Object();

        try {
            Field privateField = FinalMockCandidateFilter.class.getDeclaredField("FIELD_THAT_SHOULD_NOT_EXIST");
            privateField.setAccessible(true);
            FinalMockCandidateFilter filter = (FinalMockCandidateFilter) new FinalMockCandidateFilter();
            filter.filterCandidate(mocks, mock(Field.class), fieldInstance);
            fail();
        } catch (Exception e) {
            // Expected
        }

        // Then
        verifyThrowable(NullPointerException.class, () -> getInternalState(getInternalState(mocks.iterator().next(), "mocks"), "mocks"));
    }

    @Test
    public void testFilterCandidate_SingleMock_EmptyCollection() {
        // Given
        Collection<Object> mocks = Collections.emptyList();
        Object fieldInstance = spy(new Object());
        Field field = Whitebox.getInternalState(fieldInstance, "testField");

        // When
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter actual = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        assert(actual instanceof OngoingInjecter);
        assertFalse(actual.thenInject());
    }
}