package org.mockito.internal.configuration.injection.test;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.OngoingInjecter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FinalMockCandidateFilterTest {

    @Test
    public void testFilterCandidate_oneMock_fieldSetterSuccess() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = new ArrayList<>();
        Object mock = createMockWithField("mockField");
        mocks.add(mock);
        Object fieldInstance = new Object();
        Field field = getField("mockField");

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        Assert.assertTrue(result instanceof OngoingInjecter);
        OngoingInjecter ongoingInjecter = (OngoingInjecter) result;
        Assert.assertTrue(ongoingInjecter.thenInject());
        Assert.assertEquals(mock, fieldInstance);
    }

    @Test
    public void testFilterCandidate_oneMock_fieldSetterFailure() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = new ArrayList<>();
        Object mock = createMockWithField("mockField");
        mocks.add(mock);
        Object fieldInstance = new Object();
        Field field = getField("mockField");

        try {
            new FieldSetter(fieldInstance, field).set(mock, new Object());
            Assert.fail("Field should not be set");
        } catch (Exception e) {
            // expected
        }

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        Assert.assertTrue(result instanceof OngoingInjecter);
        OngoingInjecter ongoingInjecter = (OngoingInjecter) result;
        Assert.assertFalse(ongoingInjecter.thenInject());
    }

    @Test
    public void testFilterCandidate_oneMock_fieldAccessSuccess() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = new ArrayList<>();
        Object mock = createMockWithField("mockField");
        mocks.add(mock);
        Object fieldInstance = new Object();
        Field field = getField("mockField");
        field.setAccessible(true);

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        Assert.assertTrue(result instanceof OngoingInjecter);
        OngoingInjecter ongoingInjecter = (OngoingInjecter) result;
        Assert.assertTrue(ongoingInjecter.thenInject());
        Assert.assertEquals(mock, fieldInstance);
    }

    @Test(expected = MockitoException.class)
    public void testFilterCandidate_oneMock_fieldAccessFailure() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = new ArrayList<>();
        Object mock = createMockWithField("mockField");
        mocks.add(mock);
        Object fieldInstance = new Object();
        Field field = getField("mockField");
        field.setAccessible(true);

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        Assert.assertTrue(result instanceof OngoingInjecter);
        OngoingInjecter ongoingInjecter = (OngoingInjecter) result;
        Assert.assertFalse(ongoingInjecter.thenInject());

        try {
            field.set(mock, new Object());
            Assert.fail("Field should not be set");
        } catch (Exception e) {
            throw new MockitoException("Problems injecting dependency in mockField", e);
        }
    }

    @Test
    public void testFilterCandidate_multipleMocks_noMatch() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = createMockWithField("mockField1");
        Object mock2 = createMockWithField("mockField2");
        mocks.add(mock1);
        mocks.add(mock2);
        Object fieldInstance = new Object();
        Field field = getField("mockField");

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        Assert.assertTrue(result instanceof OngoingInjecter);
        OngoingInjecter ongoingInjecter = (OngoingInjecter) result;
        Assert.assertFalse(ongoingInjecter.thenInject());
    }

    private Object createMockWithField(String fieldName) {
        Object mock = createMock(MockitoTestAnswer.MOCKITO_ANSWER);
        Field field = getField(fieldName);
        field.setAccessible(true);
        try {
            field.set(mock, new Object());
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return mock;
    }

    private Object createMock(Answer answer) {
        Object mock = org.mockito.Mockito.mock(Object.class, answer);
        return mock;
    }

    private Field getField(String fieldName) {
        try {
            return org.mockito.internal.util.reflection.Reflections.getField(Object.class, fieldName);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    private static class MockitoTestAnswer implements Answer {
        public static final Answer MOCKITO_ANSWER = new MockitoTestAnswer();

        @Override
        public Object answer(InvocationOnMock invocation) throws Throwable {
            return null;
        }
    }
}