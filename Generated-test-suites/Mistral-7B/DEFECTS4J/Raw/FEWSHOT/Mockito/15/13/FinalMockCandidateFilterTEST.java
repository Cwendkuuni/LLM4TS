package org.mockito.internal.configuration.injection;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.MockitoConfiguration;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.OngoingInjecter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.internal.util.reflection.fields.Fields;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Stubber;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.stub;

public class FinalMockCandidateFilterTest {

    @Rule
    public MockitoConfiguration configuration = new MockitoConfiguration();

    private FinalMockCandidateFilter filter;
    private Collection<Object> mocks;
    private Object fieldInstance;
    private Field field;
    private Stubber stubber;

    @Before
    public void setUp() {
        filter = new FinalMockCandidateFilter();
        mocks = Arrays.asList(mock(Object.class), mock(Object.class));
        fieldInstance = mock(Object.class);
        field = Fields.privateField(Object.class, "field");
        stubber = stub(InvocationOnMock.class);
    }

    @Test
    public void testFilterCandidateWithOneMock() {
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        assertNotNull(injecter);
        assertTrue(injecter instanceof OngoingInjecter);
        assertTrue(injecter.thenInject());
    }

    @Test
    public void testFilterCandidateWithTwoMocks() {
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        assertFalse(injecter.thenInject());
    }

    @Test
    public void testInjectPropertyWhenOneMock() throws Exception {
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        Object mock = mocks.iterator().next();
        stubber.answer(mock);

        injecter.thenInject();

        FieldSetter fieldSetter = new FieldSetter(fieldInstance, field);
        fieldSetter.set(mock);

        InvocationOnMock invocation = stubber.verify(mock);
        assertEquals(fieldInstance, invocation.getArgument(0));
    }

    @Test
    public void testInjectPropertyWhenTwoMocks() throws Exception {
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertFalse(injecter.thenInject());
    }

    @Test
    public void testInjectFieldWhenOneMock() throws Exception {
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        Object mock = mocks.iterator().next();
        stubber.answer(mock);

        injecter.thenInject();

        FieldSetter fieldSetter = new FieldSetter(mock, field);
        fieldSetter.set(fieldInstance);

        InvocationOnMock invocation = stubber.verify(mock);
        assertEquals(fieldInstance, invocation.getArgument(0));
    }

    @Test
    public void testInjectFieldWhenTwoMocks() throws Exception {
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertFalse(injecter.thenInject());
    }

    @Test
    public void testThrowExceptionWhenOneMock() throws Exception {
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        Object mock = mocks.iterator().next();
        stubber.answer(mock);

        injecter.thenInject();

        FieldSetter fieldSetter = new FieldSetter(fieldInstance, field);
        stubber.doThrow(new MockitoException("Exception message")).when(mock).setField(field, fieldInstance);

        assertThrows(MockitoException.class, () -> filter.filterCandidate(mocks, field, fieldInstance).thenInject());
    }

    @Test
    public void testThrowExceptionWhenTwoMocks() {
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertFalse(injecter.thenInject());
    }

    @Test
    public void testNullCollection() {
        assertThrows(NullPointerException.class, () -> filter.filterCandidate(null, field, fieldInstance));
    }

    @Test
    public void testNullField() {
        assertThrows(NullPointerException.class, () -> filter.filterCandidate(mocks, null, fieldInstance));
    }

    @Test
    public void testNullFieldInstance() {
        assertThrows(NullPointerException.class, () -> filter.filterCandidate(mocks, field, null));
    }
}