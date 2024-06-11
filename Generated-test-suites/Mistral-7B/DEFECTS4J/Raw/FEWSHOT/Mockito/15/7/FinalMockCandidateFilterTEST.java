package org.mockito.internal.configuration.injection;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.exceptions.base.MockitoException.classToString;
import static org.mockito.internal.util.reflection.ReflectionUtils.getFieldValue;
import static org.mockito.mockitis.Mockito.mock;
import static org.mockito.mockitis.Mockito.spy;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.OngoingInjecter;
import org.mockito.internal.util.reflection.FieldSetter;

@RunWith(MockitoJUnitRunner.class)
public class FinalMockCandidateFilterTest {

    @Mock
    private Object mock1;

    @Mock
    private Object mock2;

    @Mock
    private Object mock3;

    @InjectMocks
    private FinalMockCandidateFilter filter;

    @Test
    public void testEmptyCollection() {
        Collection<Object> mocks = new ArrayList<>();
        Field field = MockitoTestUtils.getTestField();
        Object fieldInstance = new Object();
        assertNull(filter.filterCandidate(mocks, field, fieldInstance));
    }

    @Test
    public void testOneMock() {
        Collection<Object> mocks = new ArrayList<>(Arrays.asList(mock1));
        Field field = MockitoTestUtils.getTestField();
        Object fieldInstance = new Object();

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        assertSame(mock1, injecter.getMock());
        assertTrue(injecter.inject());
    }

    @Test(expected = MockitoException.class)
    public void testOneMock_injectException() throws IllegalAccessException, InvocationTargetException {
        Collection<Object> mocks = new ArrayList<>(Arrays.asList(mock1));
        Field field = MockitoTestUtils.getTestField();
        Object fieldInstance = new Object();

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        new FieldSetter(fieldInstance, field).set(mock1);
        throw new MockitoException("Test exception");
    }

    @Test
    public void testOneMock_noMatch() {
        Collection<Object> mocks = new ArrayList<>(Arrays.asList(mock1, mock2));
        Field field = MockitoTestUtils.getTestField();
        Object fieldInstance = new Object();

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        assertFalse(injecter instanceof OngoingInjecter);
    }

    @Test
    public void testMultipleMocks_propertySetter() throws IllegalAccessException {
        Collection<Object> mocks = new ArrayList<>(Arrays.asList(mock1, mock2));
        Field field = MockitoTestUtils.getTestField();
        Object fieldInstance = new Object();

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        assertSame(mock1, injecter.getMock());
        assertTrue(injecter.inject());
    }

    @Test
    public void testMultipleMocks_fieldAccess() throws IllegalAccessException {
        Collection<Object> mocks = new ArrayList<>(Arrays.asList(mock1, mock2));
        Field field = MockitoTestUtils.getTestField();
        Object fieldInstance = spy(new Object());

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        assertSame(mock2, injecter.getMock());
        assertTrue(injecter.inject());
        assertSame(mock2, getFieldValue(fieldInstance, field));
    }

    @Test
    public void testMultipleMocks_noMatch() {
        Collection<Object> mocks = new ArrayList<>(Arrays.asList(mock1, mock2));
        Field field = MockitoTestUtils.getTestField();
        Object fieldInstance = new Object();

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        assertFalse(injecter instanceof OngoingInjecter);
    }

    private static Field getTestField() throws NoSuchFieldException {
        return FinalMockCandidateFilter.class.getDeclaredField("testField");
    }

    private static class MockitoTestUtils {
        @SuppressWarnings("unchecked")
        private static <T> Field getTestField() throws NoSuchFieldException {
            return FinalMockCandidateFilter.class.getDeclaredField("testField");
        }
    }
}