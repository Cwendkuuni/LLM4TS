package org.mockito.internal.configuration.injection;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.MockitoConfiguration;
import org.mockito.internal.configuration.injection.MockCandidateFilter;
import org.mockito.internal.configuration.injection.OngoingInjecter;
import org.mockito.internal.reflection.FieldSetter;
import org.mockito.internal.util.reflection.FieldTestUtils;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.mock.MockitoMock;
import org.mockito.stub.Answer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class FinalMockCandidateFilterTest {

    private MockCandidateFilter filter;
    private Collection<Object> mocks;
    private Field field;
    private Object fieldInstance;

    @Before
    public void setup() {
        filter = new FinalMockCandidateFilter();
        mocks = new ArrayList<>();
        field = mock(Field.class);
        fieldInstance = mock(Object.class);

        initMocks(this);
    }

    @Test
    public void testFilterCandidate_OneMock_FieldSetterSucceeds() {
        Object mock = mock(Answer.class);
        mocks.add(mock);

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(injecter);
        assertFalse(injecter.thenInject());
        verify(mock).answer(Answer.RETURN_VALUES);
        FieldTestUtils.setFieldValue(mock, field, fieldInstance);
    }

    @Test
    public void testFilterCandidate_OneMock_FieldSetterFails() {
        MockitoException exception = assertThrows(MockitoException.class, () -> {
            filter.filterCandidate(mocks, field, fieldInstance);
        });

        assertEquals("Problems injecting dependency in " + field.getName(), exception.getMessage());
    }

    @Test
    public void testFilterCandidate_MultipleMocks_NoMatchingMock() {
        Object mock1 = mock(Answer.class);
        Object mock2 = mock(Answer.class);
        mocks.add(mock1);
        mocks.add(mock2);

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertInstanceOf(OngoingInjecter.class, injecter);
        assertFalse(injecter.thenInject());
    }

    @Test
    public void testFilterCandidate_MultipleMocks_FieldSetterSucceedsOnFirstMock() {
        Object mock1 = spy(Answer.class);
        Object mock2 = mock(Answer.class);
        mocks.add(mock1);
        mocks.add(mock2);

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(injecter);
        assertFalse(injecter.thenInject());
        verify(mock1).answer(Answer.RETURN_VALUES);
        FieldTestUtils.setFieldValue(mock1, field, fieldInstance);
    }

    @Test
    public void testFilterCandidate_MultipleMocks_FieldSetterFailsOnFirstMock() {
        Object mock1 = mock(Answer.class);
        Object mock2 = mock(Answer.class);
        MockitoException exception1 = assertThrows(MockitoException.class, () -> {
            filter.filterCandidate(mocks, field, fieldInstance);
        });

        assertEquals("Problems injecting dependency in " + field.getName(), exception1.getMessage());

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertInstanceOf(OngoingInjecter.class, injecter);
        assertFalse(injecter.thenInject());
        verify(mock2).answer(Answer.RETURN_VALUES);
    }

    @Test
    public void testFilterCandidate_MultipleMocks_FieldAccessSucceedsOnSecondMock() {
        Object mock1 = mock(Answer.class);
        Object mock2 = spy(Answer.class);
        mocks.add(mock1);
        mocks.add(mock2);

        Field testField = FieldTestUtils.getField(Answer.class, "answer");

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(injecter);
        assertFalse(injecter.thenInject());
        verify(mock1).answer(Answer.RETURN_VALUES);

        FieldTestUtils.setFieldValue(mock2, testField, fieldInstance);
    }

    @Test
    public void testFilterCandidate_MultipleMocks_FieldAccessFailsOnSecondMock() {
        Object mock1 = mock(Answer.class);
        Object mock2 = mock(Answer.class);

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertInstanceOf(OngoingInjecter.class, injecter);
        assertFalse(injecter.thenInject());
    }
}