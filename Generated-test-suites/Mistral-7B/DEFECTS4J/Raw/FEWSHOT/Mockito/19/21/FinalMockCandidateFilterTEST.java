package org.mockito.internal.configuration.injection.filter;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.MockitoConfiguration;
import org.mockito.internal.configuration.injection.MockInjector;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.stubbing.Answer;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

public class FinalMockCandidateFilterTest {

    @Rule
    public MockitoConfiguration mockitoConfiguration = new MockitoConfiguration();

    @Test
    public void testFilterCandidate_oneMock_propertySuccess() {
        // Given
        Object mock = createMock(String.class);
        Field field = getField("field");

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        // When
        Object actualInjecter = filter.filterCandidate(Arrays.asList(mock), field, mock);

        // Then
        OngoingInjecter injecter = (OngoingInjecter) actualInjecter;
        Object injectedMock = injecter.thenInject();
        assertThat(injectedMock, is(mock));
    }

    @Test
    public void testFilterCandidate_oneMock_propertyFailure() {
        // Given
        Object mock = createSpy(String.class);
        Field field = getField("field");

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Reporter reporter = mock(Reporter.class);

        doThrow(RuntimeException.class).when(mock).someMethod();

        // When
        Object actualInjecter = filter.filterCandidate(Collections.singletonList(mock), field, mock);

        // Then
        OngoingInjecter injecter = (OngoingInjecter) actualInjecter;
        assertNull(injecter.thenInject());
        verify(reporter).cannotInjectDependency(any(Field.class), eq(mock), any(RuntimeException.class));
    }

    @Test
    public void testFilterCandidate_oneMock_fieldSuccess() {
        // Given
        Object mock = createSpy(String.class);
        Field field = getField("field");

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        // When
        Object actualInjecter = filter.filterCandidate(Collections.singletonList(mock), field, mock);

        // Then
        OngoingInjecter injecter = (OngoingInjecter) actualInjecter;
        Object injectedMock = injecter.thenInject();
        assertThat(injectedMock, is(mock));

        // Verify that field was set
        Object fieldValue = Whitebox.getInternalState(mock, field.getName());
        assertThat(fieldValue, is(injectedMock));
    }

    @Test
    public void testFilterCandidate_oneMock_fieldFailure() {
        // Given
        Object mock = createSpy(String.class);
        Field field = getField("field");

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Reporter reporter = mock(Reporter.class);

        doThrow(RuntimeException.class)
                .when(mock)
                .setField(field, any());

        // When
        Object actualInjecter = filter.filterCandidate(Collections.singletonList(mock), field, mock);

        // Then
        OngoingInjecter injecter = (OngoingInjecter) actualInjecter;
        assertNull(injecter.thenInject());
        verify(reporter).cannotInjectDependency(any(Field.class), eq(mock), any(RuntimeException.class));
    }

    @Test
    public void testFilterCandidate_multipleMocks_propertySuccess() {
        // Given
        Object mock1 = createMock(String.class);
        Object mock2 = createMock(String.class);
        Field field = getField("field");

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        // When
        Object actualInjecter = filter.filterCandidate(Arrays.asList(mock1, mock2), field, mock1);

        // Then
        OngoingInjecter injecter = (OngoingInjecter) actualInjecter;
        Object injectedMock = injecter.thenInject();
        assertThat(injectedMock, is(mock1));
    }

    @Test
    public void testFilterCandidate_multipleMocks_propertyFailure() {
        // Given
        Object mock1 = createSpy(String.class);
        Object mock2 = createSpy(String.class);
        Field field = getField("field");

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Reporter reporter = mock(Reporter.class);

        doThrow(RuntimeException.class).when(mock1).someMethod();

        // When
        Object actualInjecter = filter.filterCandidate(Arrays.asList(mock1, mock2), field, mock1);

        // Then
        OngoingInjecter injecter = (OngoingInjecter) actualInjecter;
        assertNull(injecter.thenInject());
        verify(reporter).cannotInjectDependency(any(Field.class), eq(mock1), any(RuntimeException.class));
    }

    @Test
    public void testFilterCandidate_multipleMocks_fieldSuccess() {
        // Given
        Object mock1 = createSpy(String.class);
        Object mock2 = createSpy(String.class);
        Field field = getField("field");

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        // When
        Object actualInjecter = filter.filterCandidate(Arrays.asList(mock1, mock2), field, mock1);

        // Then
        OngoingInjecter injecter = (OngoingInjecter) actualInjecter;
        Object injectedMock = injecter.thenInject();
        assertThat(injectedMock, is(mock1));

        // Verify that field was set
        Object fieldValue1 = Whitebox.getInternalState(mock1, field.getName());
        Object fieldValue2 = Whitebox.getInternalState(mock2, field.getName());
        assertThat(fieldValue1, is(injectedMock));
        assertThat(fieldValue2, is(mock2));
    }

    @Test
    public void testFilterCandidate_multipleMocks_fieldFailure() {
        // Given
        Object mock1 = createSpy(String.class);
        Object mock2 = createSpy(String.class);
        Field field = getField("field");

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Reporter reporter = mock(Reporter.class);

        doThrow(RuntimeException.class)
                .when(mock1)
                .setField(field, any());

        // When
        Object actualInjecter = filter.filterCandidate(Arrays.asList(mock1, mock2), field, mock1);

        // Then
        OngoingInjecter injecter = (OngoingInjecter) actualInjecter;
        assertNull(injecter.thenInject());
        verify(reporter).cannotInjectDependency(any(Field.class), eq(mock1), any(RuntimeException.class));
    }

    @Test
    public void testFilterCandidate_noMatchingMock_propertySuccess() {
        // Given
        Object mock1 = createMock(String.class);
        Object mock2 = createMock(String.class);
        Field field = getField("field");

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        // When
        Object actualInjecter = filter.filterCandidate(Arrays.asList(mock1, mock2), field, mock2);

        // Then
        OngoingInjecter injecter = (OngoingInjecter) actualInjecter;
        assertNull(injecter.thenInject());
    }

    @Test
    public void testFilterCandidate_noMatchingMock_propertyFailure() {
        // Given
        Object mock1 = createSpy(String.class);
        Object mock2 = createSpy(String.class);
        Field field = getField("field");

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Reporter reporter = mock(Reporter.class);

        doThrow(RuntimeException.class)
                .when(mock2)
                .someMethod();

        // When
        Object actualInjecter = filter.filterCandidate(Arrays.asList(mock1, mock2), field, mock2);

        // Then
        OngoingInjecter injecter = (OngoingInjecter) actualInjecter;
        assertNull(injecter.thenInject());
        verify(reporter).cannotInjectDependency(any(Field.class), eq(mock2), any(RuntimeException.class));
    }

    @Test
    public void testFilterCandidate_noMatchingMock_fieldSuccess() {
        // Given
        Object mock1 = createSpy(String.class);
        Object mock2 = createSpy(String.class);
        Field field = getField("field");

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        // When
        Object actualInjecter = filter.filterCandidate(Arrays.asList(mock1, mock2), field, mock2);

        // Then
        OngoingInjecter injecter = (OngoingInjecter) actualInjecter;
        assertNull(injecter.thenInject());
    }

    @Test
    public void testFilterCandidate_noMatchingMock_fieldFailure() {
        // Given
        Object mock1 = createSpy(String.class);
        Object mock2 = createSpy(String.class);
        Field field = getField("field");

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Reporter reporter = mock(Reporter.class);

        doThrow(RuntimeException.class)
                .when(mock2)
                .setField(field, any());

        // When
        Object actualInjecter = filter.filterCandidate(Arrays.asList(mock1, mock2), field, mock2);

        // Then
        OngoingInjecter injecter = (OngoingInjecter) actualInjecter;
        assertNull(injecter.thenInject());
        verify(reporter).cannotInjectDependency(any(Field.class), eq(mock2), any(RuntimeException.class));
    }

    private Field getField(String name) {
        try {
            return Class.forName("org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter")
                    .getField(name);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private Object createMock(Class<?> type) {
        return mock(type);
    }

    private Object createSpy(Class<?> type) {
        return spy(type);
    }

    private static final class OngoingInjecter implements InvocationHandler {
        private Object target;

        public Object thenInject() {
            InvocationOnMock invocation = createMock(InvocationOnMock.class);
            invocation.setAnswer(new Answer<Object>() {
                @Override
                public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                    Object[] args = invocationOnMock.getArguments();
                    target = args[0];
                    return null;
                }
            });
            return Proxy.newProxyInstance(
                    OngoingInjecter.class.getClassLoader(),
                    new Class[]{type},
                    this);
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            return null;
        }
    }
}