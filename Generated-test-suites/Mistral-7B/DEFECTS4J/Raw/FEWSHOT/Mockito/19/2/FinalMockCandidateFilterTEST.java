package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.OngoingInjecter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stub.Stub;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FinalMockCandidateFilterTest {
    private static final String FIELD_NAME = "testField";
    private static final String FIELD_TYPE = "java.lang.String";
    private static final Object FIELD_VALUE = "testValue";

    private BeanPropertySetter propertySetter;
    private FieldSetter fieldSetter;
    private Reporter reporter;
    private FinalMockCandidateFilter filter;

    @Before
    public void setUp() {
        propertySetter = mock(BeanPropertySetter.class);
        fieldSetter = mock(FieldSetter.class);
        reporter = mock(Reporter.class);
        filter = spy(new FinalMockCandidateFilter());
    }

    @Test
    public void testFilterCandidate_SingleMock_PropertySet() {
        Object mock = mock(Object.class);
        Field field = mock(Field.class);
        when(field.getName()).thenReturn(FIELD_NAME);
        when(field.getType()).thenReturn(FieldType.resolve(FIELD_TYPE));
        when(propertySetter.set(fieldInstance, mock, FIELD_VALUE)).thenReturn(true);

        List<Object> mocks = new ArrayList<>();
        mocks.add(mock);

        OngoingInjecter result = filter.filterCandidate(mocks, field, mock);

        verify(propertySetter).set(fieldInstance, mock, FIELD_VALUE);
        verify(fieldSetter).set(fieldInstance, mock);
        assertNull(result);
    }

    @Test
    public void testFilterCandidate_SingleMock_FieldSet() {
        Object mock = mock(Object.class);
        Field field = mock(Field.class);
        when(field.getName()).thenReturn(FIELD_NAME);
        when(field.getType()).thenReturn(FieldType.resolve(FIELD_TYPE));
        when(fieldSetter.set(fieldInstance, mock, FIELD_VALUE)).thenReturn(true);

        List<Object> mocks = new ArrayList<>();
        mocks.add(mock);

        OngoingInjecter result = filter.filterCandidate(mocks, field, mock);

        verify(propertySetter).set(fieldInstance, mock, FIELD_VALUE);
        verify(fieldSetter).set(fieldInstance, mock);
        assertNull(result);
    }

    @Test
    public void testFilterCandidate_SingleMock_PropertySetFailure() {
        Object mock = mock(Object.class);
        Field field = mock(Field.class);
        when(field.getName()).thenReturn(FIELD_NAME);
        when(field.getType()).thenReturn(FieldType.resolve(FIELD_TYPE));
        when(propertySetter.set(fieldInstance, mock, FIELD_VALUE)).thenThrow(new RuntimeException());

        List<Object> mocks = new ArrayList<>();
        mocks.add(mock);

        OngoingInjecter result = filter.filterCandidate(mocks, field, mock);

        verify(propertySetter).set(fieldInstance, mock, FIELD_VALUE);
        verify(fieldSetter).set(fieldInstance, mock);
        verify(reporter).cannotInjectDependency(field, mock, new RuntimeException());
        assertNull(result);
    }

    @Test
    public void testFilterCandidate_SingleMock_FieldSetFailure() {
        Object mock = mock(Object.class);
        Field field = mock(Field.class);
        when(field.getName()).thenReturn(FIELD_NAME);
        when(field.getType()).thenReturn(FieldType.resolve(FIELD_TYPE));
        when(fieldSetter.set(fieldInstance, mock, FIELD_VALUE)).thenThrow(new RuntimeException());

        List<Object> mocks = new ArrayList<>();
        mocks.add(mock);

        OngoingInjecter result = filter.filterCandidate(mocks, field, mock);

        verify(propertySetter).set(fieldInstance, mock, FIELD_VALUE);
        verify(fieldSetter).set(fieldInstance, mock);
        verify(reporter).cannotInjectDependency(field, mock, new RuntimeException());
        assertNull(result);
    }

    @Test
    public void testFilterCandidate_MultipleMock_NoMatch() {
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        Field field = mock(Field.class);
        when(field.getName()).thenReturn(FIELD_NAME);
        when(field.getType()).thenReturn(FieldType.resolve(FIELD_TYPE));

        List<Object> mocks = new ArrayList<>();
        mocks.add(mock1);
        mocks.add(mock2);

        OngoingInjecter result = filter.filterCandidate(mocks, field, mock2);

        assertNull(result);
    }

    @Test
    public void testFilterCandidate_MultipleMock_PropertySetMock1() {
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        Field field = mock(Field.class);
        when(field.getName()).thenReturn(FIELD_NAME);
        when(field.getType()).thenReturn(FieldType.resolve(FIELD_TYPE));
        when(propertySetter.set(fieldInstance, mock1, FIELD_VALUE)).thenReturn(true);

        List<Object> mocks = new ArrayList<>();
        mocks.add(mock1);
        mocks.add(mock2);

        OngoingInjecter result = filter.filterCandidate(mocks, field, mock2);

        verify(propertySetter).set(fieldInstance, mock1, FIELD_VALUE);
        verify(fieldSetter).set(fieldInstance, mock2);
        assertNull(result);
    }

    @Test
    public void testFilterCandidate_MultipleMock_FieldSetMock1() {
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        Field field = mock(Field.class);
        when(field.getName()).thenReturn(FIELD_NAME);
        when(field.getType()).thenReturn(FieldType.resolve(FIELD_TYPE));
        when(fieldSetter.set(fieldInstance, mock1, FIELD_VALUE)).thenReturn(true);

        List<Object> mocks = new ArrayList<>();
        mocks.add(mock1);
        mocks.add(mock2);

        OngoingInjecter result = filter.filterCandidate(mocks, field, mock1);

        verify(propertySetter).set(fieldInstance, mock1, FIELD_VALUE);
        verify(fieldSetter).set(fieldInstance, mock1);
        assertNull(result);
    }

    @Test
    public void testFilterCandidate_MultipleMock_PropertySetMock2() {
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        Field field = mock(Field.class);
        when(field.getName()).thenReturn(FIELD_NAME);
        when(field.getType()).thenReturn(FieldType.resolve(FIELD_TYPE));
        when(propertySetter.set(fieldInstance, mock2, FIELD_VALUE)).thenReturn(true);

        List<Object> mocks = new ArrayList<>();
        mocks.add(mock1);
        mocks.add(mock2);

        OngoingInjecter result = filter.filterCandidate(mocks, field, mock1);

        verify(propertySetter).set(fieldInstance, mock2, FIELD_VALUE);
        verify(fieldSetter).set(fieldInstance, mock1);
        assertNull(result);
    }

    @Test
    public void testFilterCandidate_MultipleMock_FieldSetMock2() {
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        Field field = mock(Field.class);
        when(field.getName()).thenReturn(FIELD_NAME);
        when(field.getType()).thenReturn(FieldType.resolve(FIELD_TYPE));
        when(fieldSetter.set(fieldInstance, mock1, FIELD_VALUE)).thenReturn(true);

        List<Object> mocks = new ArrayList<>();
        mocks.add(mock1);
        mocks.add(mock2);

        OngoingInjecter result = filter.filterCandidate(mocks, field, mock2);

        verify(propertySetter).set(fieldInstance, mock1, FIELD_VALUE);
        verify(fieldSetter).set(fieldInstance, mock2);
        assertNull(result);
    }

    @Test
    public void testFilterCandidate_MultipleMock_NoSet() {
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        Field field = mock(Field.class);
        when(field.getName()).thenReturn(FIELD_NAME);
        when(field.getType()).thenReturn(FieldType.resolve(FIELD_TYPE));

        List<Object> mocks = new ArrayList<>();
        mocks.add(mock1);
        mocks.add(mock2);

        OngoingInjecter result = filter.filterCandidate(mocks, field, mock1);

        verify(propertySetter).set(fieldInstance, mock1, FIELD_VALUE);
        verify(propertySetter).set(fieldInstance, mock2);
        verify(fieldSetter).set(fieldInstance, mock1);
        verify(fieldSetter).set(fieldInstance, mock2);
        assertNull(result);
    }

    @Test
    public void testFilterCandidate_MultipleMock_Null() {
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        Field field = mock(Field.class);
        when(field.getName()).thenReturn(FIELD_NAME);
        when(field.getType()).thenReturn(FieldType.resolve(FIELD_TYPE));

        List<Object> mocks = new ArrayList<>();
        mocks.add(mock1);
        mocks.add(mock2);

        OngoingInjecter result = filter.filterCandidate(mocks, field, null);

        verify(propertySetter).set(fieldInstance, mock1, FIELD_VALUE);
        verify(propertySetter).set(fieldInstance, mock2);
        verify(fieldSetter).set(fieldInstance, mock1);
        verify(fieldSetter).set(fieldInstance, mock2);
        assertNull(result);
    }
}