package org.mockito.internal.configuration.injection.filter;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.MockitoConfiguration;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.internal.util.reflection.InjectionPoint;
import org.mockito.internal.util.reflection.MockFactory;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.runners.MockitoJUnitRunner$LazyMock;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class FinalMockCandidateFilterTest {

    @Test
    public void testFilterCandidate_OneMock_PropertySetterSuccess() {
        // Given
        Reporter reporter = mock(Reporter.class);
        Field field = getTestField();
        Object fieldInstance = new Object() {
            public Field testField;
        };
        Object mock = mock(Object.class);
        Collection<Object> mocks = Arrays.asList(mock);

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        // When
        OngoingInjecter filterResult = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        Assert.assertNotNull(filterResult);
        OngoingInjecter expectedFilterResult = new OngoingInjecter() {
            @Override
            public Object thenInject() {
                BeanPropertySetter propertySetter = new BeanPropertySetter(fieldInstance, field);
                propertySetter.set(mock);
                return mock;
            }
        };
        Assert.assertEquals(expectedFilterResult, filterResult);
    }

    @Test
    public void testFilterCandidate_OneMock_PropertySetterFailure() {
        // Given
        Reporter reporter = mock(Reporter.class);
        Field field = getTestField();
        Object fieldInstance = new Object() {
            public Field testField;
        };
        Object mock = mock(Object.class);
        Collection<Object> mocks = Arrays.asList(mock);

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        // When
        Whitebox.setInternalState(field, "notMockitoField");
        OngoingInjecter filterResult = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        Assert.assertNull(filterResult);
        verify(reporter).cannotInjectDependency(field, mock, new AssertionError("notMockitoField"));
    }

    @Test
    public void testFilterCandidate_OneMock_FieldSetterSuccess() {
        // Given
        Reporter reporter = mock(Reporter.class);
        Field field = getTestField();
        Object fieldInstance = new Object() {
            public Field testField;
        };
        Object mock = mock(Object.class);
        Collection<Object> mocks = Arrays.asList(mock);

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        // When
        Whitebox.setInternalState(field, "notMockitoField");
        OngoingInjecter filterResult = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        Assert.assertNull(filterResult);
        verify(reporter).cannotInjectDependency(field, mock, new AssertionError("notMockitoField"));

        // When
        FieldSetter fieldSetter = new FieldSetter(fieldInstance, field);
        fieldSetter.set(mock);
        filterResult = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        Assert.assertNotNull(filterResult);
        OngoingInjecter expectedFilterResult = new OngoingInjecter() {
            @Override
            public Object thenInject() {
                fieldSetter.set(mock);
                return mock;
            }
        };
        Assert.assertEquals(expectedFilterResult, filterResult);
    }

    @Test
    public void testFilterCandidate_MultipleMocks_PropertySetterSuccess() {
        // Given
        Reporter reporter = mock(Reporter.class);
        Field field = getTestField();
        Object fieldInstance = new Object() {
            public Field testField;
        };
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        Collection<Object> mocks = Arrays.asList(mock1, mock2);

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        // When
        OngoingInjecter filterResult = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        Assert.assertNull(filterResult);

        // When
        Arrays.stream(mocks).forEach(m -> {
            BeanPropertySetter beanPropertySetter = new BeanPropertySetter(fieldInstance, field);
            beanPropertySetter.set(m);
            filterResult = filter.filterCandidate(mocks, field, fieldInstance);
        });

        // Then
        Assert.assertNotNull(filterResult);
        OngoingInjecter expectedFilterResult = new OngoingInjecter() {
            @Override
            public Object thenInject() {
                return mock1;
            }
        };
        Assert.assertEquals(expectedFilterResult, filterResult);
    }

    @Test
    public void testFilterCandidate_MultipleMocks_PropertySetterFailure() {
        // Given
        Reporter reporter = mock(Reporter.class);
        Field field = getTestField();
        Object fieldInstance = new Object() {
            public Field testField;
        };
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        Collection<Object> mocks = Arrays.asList(mock1, mock2);

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        // When
        Arrays.stream(mocks).forEach(m -> {
            BeanPropertySetter beanPropertySetter = new BeanPropertySetter(fieldInstance, field);
            beanPropertySetter.set(m);
        });

        // Then
        OngoingInjecter expectedFilterResult = new OngoingInjecter() {
            @Override
            public Object thenInject() {
                return null;
            }
        };
        OngoingInjecter filterResult = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        Assert.assertNotNull(filterResult);
        Assert.assertEquals(expectedFilterResult, filterResult);
    }

    @Test
    public void testFilterCandidate_MultipleMocks_FieldSetterSuccess() {
        // Given
        Reporter reporter = mock(Reporter.class);
        Field field = getTestField();
        Object fieldInstance = new Object() {
            public Field testField;
        };
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        Collection<Object> mocks = Arrays.asList(mock1, mock2);

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        // When
        Arrays.stream(mocks).forEach(m -> {
            BeanPropertySetter beanPropertySetter = new BeanPropertySetter(fieldInstance, field);
            beanPropertySetter.set(m);
            Whitebox.setInternalState(field, "notMockitoField");
        });

        // Then
        OngoingInjecter expectedFilterResult = new OngoingInjecter() {
            @Override
            public Object thenInject() {
                return mock1;
            }
        };
        OngoingInjecter filterResult = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        Assert.assertNotNull(filterResult);
        Assert.assertEquals(expectedFilterResult, filterResult);

        // When
        Arrays.stream(mocks).forEach(m -> {
            FieldSetter fieldSetter = new FieldSetter(fieldInstance, field);
            fieldSetter.set(m);
        });

        // Then
        filterResult = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        Assert.assertNotNull(filterResult);
        expectedFilterResult = new OngoingInjecter() {
            @Override
            public Object thenInject() {
                return mock1;
            }
        };
        Assert.assertEquals(expectedFilterResult, filterResult);
    }

    @Test
    public void testFilterCandidate_MultipleMocks_FieldSetterFailure() {
        // Given
        Reporter reporter = mock(Reporter.class);
        Field field = getTestField();
        Object fieldInstance = new Object() {
            public Field testField;
        };
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        Collection<Object> mocks = Arrays.asList(mock1, mock2);

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        // When
        Arrays.stream(mocks).forEach(m -> {
            FieldSetter fieldSetter = new FieldSetter(fieldInstance, field);
            fieldSetter.set(m);
            Whitebox.setInternalState(field, "notMockitoField");
        });

        // Then
        OngoingInjecter expectedFilterResult = new OngoingInjecter() {
            @Override
            public Object thenInject() {
                return null;
            }
        };
        OngoingInjecter filterResult = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        Assert.assertNotNull(filterResult);
        Assert.assertEquals(expectedFilterResult, filterResult);

        // When
        List<Field> fields = Arrays.asList(field);
        MockitoConfiguration configuration = mock(MockitoConfiguration.class);
        Object testObject = mock(Object.class);
        InjectionPoint injectionPoint = new InjectionPoint(testObject.getClass(), fields);
        MockFactory mockFactory = mock(MockFactory.class);
        Method constructor = Arrays.stream(testObject.getClass().getConstructors())
                .filter(c -> c.getParameterTypes().length == 0)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException());

        LazyMock lazyMock = new LazyMock(testObject, configuration, mockFactory, injectionPoint, reporter, constructor);
        lazyMock.injectMocks();

        Arrays.stream(mocks).forEach(m -> {
            FieldSetter fieldSetter = new FieldSetter(fieldInstance, field);
            fieldSetter.set(m);
        });

        // Then
        filterResult = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        Assert.assertNotNull(filterResult);
        expectedFilterResult = new OngoingInjecter() {
            @Override
            public Object thenInject() {
                return mock1;
            }
        };
        Assert.assertEquals(expectedFilterResult, filterResult);
    }

    private Field getTestField() {
        try {
            return ReflectionTestUtils.findField(FinalMockCandidateFilter.class, "filterCandidate");
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }
}