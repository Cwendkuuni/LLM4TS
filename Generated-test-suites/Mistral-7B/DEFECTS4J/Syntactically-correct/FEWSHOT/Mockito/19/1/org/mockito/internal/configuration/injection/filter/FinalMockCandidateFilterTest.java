package org.mockito.internal.configuration.injection.filter;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.mocktype.MockType;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.internal.util.reflection.MockUtil;
import org.mockito.internal.util.reflection.Whitebox;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.reflect.whitebox.WhiteboxInternal;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class FinalMockCandidateFilterTest {

    private static final String FIELD_NAME = "fieldName";
    private static final String MOCKED_FIELD_VALUE = "mockedFieldValue";
    private static final Object MOCKED_FIELD_INSTANCE = mock(String.class);
    private static final String MOCKED_MOCK_VALUE = "mockedMockValue";
    private static final MockType MOCKED_MOCK = MockType.MOCK;
    private static final Object MOCKED_MATCHING_MOCK = mock(Object.class);
    private static final Object MOCKED_FIELD_SETTER = mock(BeanPropertySetter.class);
    private static final Object MOCKED_FIELD_SETTER_SPY = spy(mock(BeanPropertySetter.class));
    private static final Object MOCKED_FIELD = mockField(String.class, FIELD_NAME);
    private static final Object MOCKED_FIELD_INSTANCE_SPY = spy(MOCKED_FIELD_INSTANCE);
    private static final Object MOCKED_REPORTER = mock(Reporter.class);

    @Test
    public void testFilterCandidateWithOneMock() {
        // Given
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(MOCKED_MATCHING_MOCK);

        // When
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter filterResult = filter.filterCandidate(mocks, MOCKED_FIELD, MOCKED_FIELD_INSTANCE);

        // Then
        Assert.assertNotNull(filterResult);
        Assert.assertEquals(MOCKED_MATCHING_MOCK, filterResult.thenInject());
    }

    @Test
    public void testFilterCandidateWithOnePropertySetterSuccess() {
        // Given
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(MOCKED_MATCHING_MOCK);

        WhiteboxInternal.setInternalState(MOCKED_FIELD_SETTER, "beanPropertySetter", MOCKED_FIELD_SETTER_SPY);

        // When
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter filterResult = filter.filterCandidate(mocks, MOCKED_FIELD, MOCKED_FIELD_INSTANCE);

        // Then
        Assert.assertNotNull(filterResult);
        Assert.assertEquals(MOCKED_MATCHING_MOCK, filterResult.thenInject());
        PowerMockito.verifyStatic(BeanPropertySetter.class);
        PowerMockito.verifyStatic().set(MOCKED_FIELD_INSTANCE, MOCKED_FIELD, MOCKED_MATCHING_MOCK);
    }

    @Test
    public void testFilterCandidateWithOnePropertySetterFailure() {
        // Given
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(MOCKED_MATCHING_MOCK);

        WhiteboxInternal.setInternalState(MOCKED_FIELD_SETTER, "beanPropertySetter", MOCKED_FIELD_SETTER_SPY);
        when(MOCKED_FIELD_SETTER_SPY.set(MOCKED_FIELD_INSTANCE, MOCKED_FIELD, MOCKED_MOCK_VALUE)).thenThrow(new RuntimeException("Test error"));

        // When
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter filterResult = filter.filterCandidate(mocks, MOCKED_FIELD, MOCKED_FIELD_INSTANCE);

        // Then
        Reporter reporter = PowerMockito.mockStatic(Reporter.class);
        PowerMockito.verifyStatic(Reporter.class).cannotInjectDependency(MOCKED_FIELD, MOCKED_MATCHING_MOCK, PowerMockito.isInstanceOf(RuntimeException.class));
        Assert.assertNull(filterResult);
    }

    @Test
    public void testFilterCandidateWithOneFieldAccessSuccess() {
        // Given
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(MOCKED_MATCHING_MOCK);

        // When
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter filterResult = filter.filterCandidate(mocks, MOCKED_FIELD, MOCKED_FIELD_INSTANCE);

        // Then
        Assert.assertNotNull(filterResult);
        Assert.assertEquals(MOCKED_MATCHING_MOCK, filterResult.thenInject());
        PowerMockito.verifyStatic(FieldSetter.class);
        PowerMockito.verifyStatic().setFieldValue(MOCKED_FIELD_INSTANCE, MOCKED_FIELD, MOCKED_MATCHING_MOCK);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testFilterCandidateWithMultipleMocks() {
        // Given
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(MOCKED_MATCHING_MOCK);
        mocks.add(mock(Object.class));

        // When
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter filterResult = filter.filterCandidate(mocks, MOCKED_FIELD, MOCKED_FIELD_INSTANCE);

        // Then - exception is expected
    }

    @Test
    public void testFilterCandidateWithNullCollection() {
        // Given
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        // When
        OngoingInjecter filterResult = filter.filterCandidate(null, MOCKED_FIELD, MOCKED_FIELD_INSTANCE);

        // Then
        Assert.assertNull(filterResult);
    }

    @Test
    public void testFilterCandidateWithNullField() {
        // Given
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(MOCKED_MATCHING_MOCK);
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        // When
        OngoingInjecter filterResult = filter.filterCandidate(mocks, null, MOCKED_FIELD_INSTANCE);

        // Then
        Assert.assertNull(filterResult);
    }

    @Test
    public void testFilterCandidateWithNullFieldInstance() {
        // Given
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(MOCKED_MATCHING_MOCK);
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        // When
        OngoingInjecter filterResult = filter.filterCandidate(mocks, MOCKED_FIELD, null);

        // Then
        Assert.assertNull(filterResult);
    }

    private Object mockField(Class<?> clazz, String name) {
        try {
            Field field = clazz.getDeclaredField(name);
            field.setAccessible(true);
            return field;
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    private void when(Object mock, Method method, Object... args) {
        try {
            method.invoke(mock, args);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}