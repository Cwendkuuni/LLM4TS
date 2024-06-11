package org.mockito.internal.configuration.injection;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.MockitoConfiguration;
import org.mockito.internal.configuration.injector.OngoingInjecter;
import org.mockito.internal.util.reflection.FieldSetter;

public class FinalMockCandidateFilterTest {

    @Test
    public void testFilterCandidateWhenSingleMock_FieldSetterSuccess() {
        final Object mock = mock(String.class);
        final Field field = String.class.getDeclaredField("value");
        final Object fieldInstance = spy(mock);

        final Collection<Object> mocks = Collections.singletonList(mock);

        final FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        final OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(injecter);
        assertFalse(injecter.thenInject());
        new FieldSetter(fieldInstance, field).set(mock); // this call should not throw an exception
    }

    @Test
    public void testFilterCandidateWhenSingleMock_FieldSetterFailure() {
        final Object mock = mock(String.class);
        final Field field = String.class.getDeclaredField("value");
        final Object fieldInstance = spy(mock);

        final Collection<Object> mocks = Collections.singletonList(mock);

        final FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        try {
            final OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
            assertNotNull(injecter);
            assertFalse(injecter.thenInject());
            new FieldSetter(fieldInstance, field).set(mock); // this call should throw an exception
            assertThrows(MockitoException.class, () -> injecter.thenInject());
        } catch (Exception e) {
            // expected exception
        }
    }

    @Test
    public void testFilterCandidateWhenMultipleMocks_NoMatchFound() {
        final Object mock1 = mock(String.class);
        final Object mock2 = mock(String.class);
        final Field field = String.class.getDeclaredField("value");
        final Object fieldInstance = mock(String.class);

        final Collection<Object> mocks = Arrays.asList(mock1, mock2);

        final FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        final OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(injecter);
        assertFalse(injecter.thenInject());
    }

    @Test
    public void testFilterCandidateWhenOneMockMatches_FieldSetterSuccess() {
        final Object mock = mock(String.class);
        final Object mockMatched = mock(String.class);
        final Field field = String.class.getDeclaredField("value");
        final Object fieldInstance = spy(mock);

        final Collection<Object> mocks = Arrays.asList(mock, mockMatched);

        final FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        final OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(injecter);
        assertFalse(injecter.thenInject());

        try {
            new FieldSetter(fieldInstance, field).set(mockMatched);
        } catch (Exception e) {
            // expected exception
        }

        new FieldSetter(fieldInstance, field).set(mock);
        assertTrue(injecter.thenInject());
    }

    @Test
    public void testFilterCandidateWhenOneMockMatches_FieldAccessSuccess() {
        final Object mock = mock(String.class);
        final Object mockMatched = mock(String.class);
        final Field field = String.class.getDeclaredField("value");
        final Object fieldInstance = spy(mock);

        final Collection<Object> mocks = Arrays.asList(mock, mockMatched);

        final FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        final OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(injecter);
        assertFalse(injecter.thenInject());

        try {
            final Field fieldAccess = mockMatched.getClass().getDeclaredField("value");
            fieldAccess.setAccessible(true);
            fieldAccess.set(fieldInstance, mockMatched);
        } catch (Exception e) {
            // expected exception
        }

        try {
            fieldAccess.setAccessible(true);
            fieldAccess.set(fieldInstance, mock);
            assertTrue(injecter.thenInject());
        } catch (Exception e) {
            throw new RuntimeException("Unexpected exception", e);
        }
    }

    @Test
    public void testFilterCandidateWhenOneMockMatches_PropertySetterSuccess() {
        final Object mock = mock(String.class);
        final Object mockMatched = mock(String.class);
        final Field field = String.class.getDeclaredField("value");
        final Object fieldInstance = spy(mock);

        final Collection<Object> mocks = Arrays.asList(mock, mockMatched);

        final FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        final OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(injecter);
        assertFalse(injecter.thenInject());

        try {
            final Field propertyField = mockMatched.getClass().getDeclaredField("value");
            propertyField.setAccessible(true);
            propertyField.set(fieldInstance, mockMatched);
        } catch (Exception e) {
            // expected exception
        }

        try {
            propertyField.setAccessible(true);
            propertyField.set(fieldInstance, mock);
            assertTrue(injecter.thenInject());
        } catch (Exception e) {
            throw new RuntimeException("Unexpected exception", e);
        }
    }

    @Test
    public void testFilterCandidateWhenOneMockMatches_PropertySetterFailure() {
        final Object mock = mock(String.class);
        final Object mockMatched = mock(String.class);
        final Field field = String.class.getDeclaredField("value");
        final Object fieldInstance = spy(mock);

        final Collection<Object> mocks = Arrays.asList(mock, mockMatched);

        final FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        final OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(injecter);
        assertFalse(injecter.thenInject());

        try {
            final Field propertyField = mockMatched.getClass().getDeclaredField("value");
            propertyField.setAccessible(true);
            propertyField.set(fieldInstance, mockMatched);
        } catch (Exception e) {
            // expected exception
        }

        try {
            propertyField.setAccessible(true);
            assertThrows(MockitoException.class, () -> propertyField.set(fieldInstance, mock));
            assertFalse(injecter.thenInject());
        } catch (Exception e) {
            throw new RuntimeException("Unexpected exception", e);
        }
    }
}