package org.mockito.internal.configuration.injection.filter;

import java.lang.reflect.Field;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.MockitoConfiguration;
import org.mockito.internal.configuration.injection.Injector;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.OngoingInjecter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.stubs.MockitoStubs;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.internal.util.reflection.ReflectionUtils.getAccessibleField;
import static org.mockito.internal.util.reflection.ReflectionUtils.setAccessible;

public class FinalMockCandidateFilterTest {

    private Collection<Object> mocks;
    private Field field;
    private Object fieldInstance;

    @Before
    public void setup() {
        mocks = MockitoConfiguration.getMocks();
        field = MockitoStubs.class.getDeclaredField("fieldToMock");
        fieldInstance = new MockitoStubs();
        setAccessible(field);
    }

    @Test
    public void testFilterCandidate_OneMock_BeanPropertySetterSuccess() {
        // Given
        Object mock = new Object() {};
        Object expectedMock = mock;

        // When
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        injecter.thenInject();

        // Then
        Object actualMock = field.get(fieldInstance);
        assertThat(actualMock).isSameAs(expectedMock);
    }

    @Test
    public void testFilterCandidate_OneMock_BeanPropertySetterFailure() {
        // Given
        Object mock = new Object() {};
        Object expectedMock = new Object() {};
        field.set(fieldInstance, mock);

        // When
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        injecter.thenInject();

        // Then
        Object actualMock = field.get(fieldInstance);
        Object newMock = mocks.iterator().next();
        assertThat(actualMock).isSameAs(newMock);
    }

    @Test
    public void testFilterCandidate_OneMock_FieldSetterSuccess() {
        // Given
        Object mock = new Object() {};
        Object expectedMock = mock;

        // When
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        injecter.thenInject();

        // Then
        Object actualMock = getAccessibleField(fieldInstance, "actualMock");
        assertThat(actualMock).isSameAs(expectedMock);
    }

    @Test
    public void testFilterCandidate_OneMock_FieldSetterFailure() {
        // Given
        Object mock = new Object() {};
        Object expectedMock = new Object() {};
        setAccessibleField(fieldInstance, "actualMock", mock);

        // When
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        injecter.thenInject();

        // Then
        Object actualMock = getAccessibleField(fieldInstance, "actualMock");
        Object newMock = mocks.iterator().next();
        assertThat(actualMock).isSameAs(newMock);
    }

    @Test
    public void testFilterCandidate_MultipleMocks_BeanPropertySetterSuccess() {
        // Given
        Object mock1 = new Object() {};
        Object mock2 = new Object() {};

        // When
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        injecter.thenInject();

        // Then
        Object actualMock = field.get(fieldInstance);
        Object expectedMock = mock1;
        assertThat(actualMock).isSameAs(expectedMock);
    }

    @Test
    public void testFilterCandidate_MultipleMocks_BeanPropertySetterFailure() {
        // Given
        Object mock1 = new Object() {};
        Object mock2 = new Object() {};
        field.set(fieldInstance, mock1);

        // When
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        injecter.thenInject();

        // Then
        Object actualMock = field.get(fieldInstance);
        Object newMock = mocks.iterator().next();
        Object expectedMock = mock2;
        assertThat(actualMock).isSameAs(expectedMock);
    }

    @Test
    public void testFilterCandidate_MultipleMocks_FieldSetterSuccess() {
        // Given
        Object mock1 = new Object() {};
        Object mock2 = new Object() {};

        // When
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        injecter.thenInject();

        // Then
        Object actualMock = getAccessibleField(fieldInstance, "actualMock");
        Object expectedMock = mock1;
        assertThat(actualMock).isSameAs(expectedMock);
    }

    @Test
    public void testFilterCandidate_MultipleMocks_FieldSetterFailure() {
        // Given
        Object mock1 = new Object() {};
        Object mock2 = new Object() {};
        setAccessibleField(fieldInstance, "actualMock", mock1);

        // When
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        injecter.thenInject();

        // Then
        Object actualMock = getAccessibleField(fieldInstance, "actualMock");
        Object newMock = mocks.iterator().next();
        Object expectedMock = mock2;
        assertThat(actualMock).isSameAs(expectedMock);
    }

    @Test(expected = RuntimeException.class)
    public void testFilterCandidate_RuntimeException() {
        // Given
        Object mock = new Object() {};
        field.set(fieldInstance, mock);

        // When
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        injecter.thenInject();

        // Then
        // This test case should throw a RuntimeException
    }

    private void setAccessibleField(Object instance, String fieldName, Object value) {
        try {
            Field field = instance.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(instance, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            new Reporter().cannotInjectDependency(field, fieldInstance, e);
        }
    }

    private Object getAccessibleField(Object instance, String fieldName) {
        try {
            Field field = instance.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(instance);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}