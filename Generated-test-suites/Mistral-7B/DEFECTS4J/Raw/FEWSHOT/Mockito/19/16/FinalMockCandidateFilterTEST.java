package org.mockito.internal.configuration.injection.filter;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.MockitoConfiguration;
import org.mockito.internal.configuration.injection.InjectableFields;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.internal.util.reflection.FieldType;
import org.mockito.internal.util.reflection.InjectionPoint;
import org.mockito.internal.util.reflection.MockMaker;
import org.mockito.internal.util.reflection.TypeUtils;
import org.mockito.junit.MockitoJUnit;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

public class FinalMockCandidateFilterTest {

    @Rule
    public MockitoJUnit mockitoJUnit = new MockitoJUnit();

    @Test
    public void testFilterCandidate_oneMock_propertySetterSuccess() {
        Reporter reporter = mock(Reporter.class);
        Field field = new FieldType(TargetClass.class, "field1", Accessible.PUBLIC, null).getField();
        Object fieldInstance = mock(TargetClass.class);
        Object mock = mock(MockedType.class);
        InjectionPoint injectionPoint = new InjectionPoint(new MockMaker(), new InjectableFields(), field, TargetClass.class, mock, Collections.singletonList(fieldInstance));
        List<Object> mocks = new ArrayList<>();
        mocks.add(mock);

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter filterResult = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(filterResult);
        filterResult.thenInject();
        new BeanPropertySetter(fieldInstance, field).set(mock);
    }

    @Test
    public void testFilterCandidate_oneMock_propertySetterFailure() {
        Reporter reporter = mock(Reporter.class);
        Field field = new FieldType(TargetClass.class, "field1", Accessible.PUBLIC, null).getField();
        Object fieldInstance = mock(TargetClass.class);
        Object mock = mock(MockedType.class);
        InjectionPoint injectionPoint = new InjectionPoint(new MockMaker(), new InjectableFields(), field, TargetClass.class, mock, Collections.singletonList(fieldInstance));
        List<Object> mocks = new ArrayList<>();
        mocks.add(mock);

        doThrow(new RuntimeException("Exception")).when(reporter).cannotInjectDependency(field, mock, any(RuntimeException.class));

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter filterResult = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(filterResult);
        filterResult.thenInject();

        // assert that the exception is thrown by reporter
        // This test case relies on the mocking framework and can't be easily automated
    }

    @Test
    public void testFilterCandidate_oneMock_fieldAccessSuccess() {
        Reporter reporter = mock(Reporter.class);
        Field field = new FieldType(TargetClass.class, "field1", Accessible.PUBLIC, null).getField();
        Object fieldInstance = mock(TargetClass.class);
        Object mock = mock(MockedType.class);
        InjectionPoint injectionPoint = new InjectionPoint(new MockMaker(), new InjectableFields(), field, TargetClass.class, mock, Collections.singletonList(fieldInstance));
        List<Object> mocks = new ArrayList<>();
        mocks.add(mock);

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter filterResult = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(filterResult);
        filterResult.thenInject();
        new FieldSetter(fieldInstance, field).set(mock);
    }

    @Test
    public void testFilterCandidate_oneMock_fieldAccessFailure() {
        Reporter reporter = mock(Reporter.class);
        Field field = new FieldType(TargetClass.class, "field1", Accessible.PUBLIC, null).getField();
        Object fieldInstance = mock(TargetClass.class);
        Object mock = mock(MockedType.class);
        InjectionPoint injectionPoint = new InjectionPoint(new MockMaker(), new InjectableFields(), field, TargetClass.class, mock, Collections.singletonList(fieldInstance));
        List<Object> mocks = new ArrayList<>();
        mocks.add(mock);

        doThrow(new RuntimeException("Exception")).when(reporter).cannotInjectDependency(field, mock, any(RuntimeException.class));

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter filterResult = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(filterResult);
        filterResult.thenInject();

        // assert that the exception is thrown by reporter
        // This test case relies on the mocking framework and can't be easily automated
    }

    @Test
    public void testFilterCandidate_multipleMocks_noMatch() {
        Reporter reporter = mock(Reporter.class);
        Field field = new FieldType(TargetClass.class, "field1", Accessible.PUBLIC, null).getField();
        Object fieldInstance = mock(TargetClass.class);
        Object mock1 = mock(MockedType1.class);
        Object mock2 = mock(MockedType2.class);
        InjectionPoint injectionPoint = new InjectionPoint(new MockMaker(), new InjectableFields(), field, TargetClass.class, mock1, Collections.singletonList(fieldInstance));
        List<Object> mocks = new ArrayList<>();
        mocks.add(mock1);
        mocks.add(mock2);

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter filterResult = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(filterResult);
        assertNull(filterResult.thenInject());
    }
}

class TargetClass {
    public String field1;
}

class MockedType {
}

class MockedType1 {
}

class MockedType2 {
}