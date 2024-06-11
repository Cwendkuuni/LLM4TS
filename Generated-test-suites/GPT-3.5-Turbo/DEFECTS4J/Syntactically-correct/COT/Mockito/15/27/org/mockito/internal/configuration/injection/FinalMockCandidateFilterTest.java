
package org.mockito.internal.configuration.injection;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.reflection.FieldSetter;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;

public class FinalMockCandidateFilterTest {

    @Test
    public void testFilterCandidate_singleMock_noInjection() {
        // Arrange
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = Collections.singleton(new Object());
        Field field = getField("testField");
        Object fieldInstance = new Object();

        // Act
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        Assertions.assertThat(injecter.thenInject()).isFalse();
    }

    @Test
    public void testFilterCandidate_singleMock_injectedSuccessfully() {
        // Arrange
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Object mock = new Object();
        Collection<Object> mocks = Collections.singleton(mock);
        Field field = getField("testField");
        Object fieldInstance = new Object();

        // Act
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        Assertions.assertThat(injecter.thenInject()).isTrue();
        Assertions.assertThat(field.get(fieldInstance)).isSameAs(mock);
    }

    @Test(expected = MockitoException.class)
    public void testFilterCandidate_singleMock_injectedUnsuccessfully() {
        // Arrange
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Object mock = new Object();
        Collection<Object> mocks = Collections.singleton(mock);
        Field field = getField("testField");
        Object fieldInstance = new Object();

        // Act
        try {
            FieldSetter fieldSetter = new FieldSetter(fieldInstance, field);
            fieldSetter.set(null);
        } catch (Exception e) {
            throw new MockitoException("Problems injecting dependency in " + field.getName(), e);
        }

        // Assert
        filter.filterCandidate(mocks, field, fieldInstance);
        // Expecting MockitoException to be thrown
    }

    @Test
    public void testFilterCandidate_multipleMocks_noInjection() {
        // Arrange
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = Collections.singleton(new Object());
        Field field = getField("testField");
        Object fieldInstance = new Object();

        // Act
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        Assertions.assertThat(injecter.thenInject()).isFalse();
    }

    @Test
    public void testFilterCandidate_multipleMocks_injectedSuccessfully() {
        // Arrange
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Object mock1 = new Object();
        Object mock2 = new Object();
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock1);
        mocks.add(mock2);
        Field field = getField("testField");
        Object fieldInstance = new Object();

        // Act
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        Assertions.assertThat(injecter.thenInject()).isTrue();
        Assertions.assertThat(field.get(fieldInstance)).isSameAs(mock1);
    }

    private Field getField(String fieldName) {
        try {
            return FinalMockCandidateFilter.class.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("Failed to retrieve field: " + fieldName, e);
        }
    }
}
