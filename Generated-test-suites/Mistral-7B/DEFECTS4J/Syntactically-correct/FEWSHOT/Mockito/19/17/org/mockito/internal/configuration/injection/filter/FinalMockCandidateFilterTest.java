package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.OngoingInjecter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;
import org.mockito.stub.Stubber;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class FinalMockCandidateFilterTest {

    private Collection<Object> mocks;
    private Field field;
    private Object fieldInstance;
    private Reporter reporter;
    private MockCandidateFilter filter;

    @Rule
    public final Reporter reporterMock = mock(Reporter.class);

    @Before
    public void setup() {
        mocks = new ArrayList<>();
        field = mock(Field.class);
        fieldInstance = mock(Object.class);
        reporter = spy(reporterMock);
        filter = new FinalMockCandidateFilter();
    }

    @Test
    public void testFilterCandidate_OneMock_PropertySetterSuccess() {
        Object mock = mock(Object.class);

        mocks.add(mock);

        when(field.getName()).thenReturn("testField");
        when(field.getType()).thenReturn(String.class);
        when(fieldInstance.getClass()).thenReturn(String.class);

        Stubber stubber = mock(Stubber.class);
        BeanPropertySetter propertySetter = mock(BeanPropertySetter.class);
        FieldSetter fieldSetter = mock(FieldSetter.class);

        when(propertySetter.set(fieldInstance, mock, "testValue")).thenReturn(true);
        when(fieldSetter.set(fieldInstance, field, mock)).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) {
                return null;
            }
        });

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        assertNotNull(injecter);

        injecter.thenInject();
    }

    @Test
    public void testFilterCandidate_OneMock_PropertySetterFailure() {
        Object mock = mock(Object.class);

        mocks.add(mock);

        when(field.getName()).thenReturn("testField");
        when(field.getType()).thenReturn(String.class);
        when(fieldInstance.getClass()).thenReturn(String.class);

        Stubber stubber = mock(Stubber.class);
        BeanPropertySetter propertySetter = mock(BeanPropertySetter.class);
        FieldSetter fieldSetter = mock(FieldSetter.class);

        when(propertySetter.set(fieldInstance, mock, "testValue")).thenThrow(new RuntimeException("Test Exception"));
        when(fieldSetter.set(fieldInstance, field, mock)).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) {
                return null;
            }
        });

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        assertNotNull(injecter);

        try {
            injecter.thenInject();
            assert(false); // Should not reach this point
        } catch (RuntimeException e) {
            reporter.cannotInjectDependency(field, mock, e);
        }
    }

    @Test
    public void testFilterCandidate_OneMock_FieldAccessSuccess() {
        Object mock = mock(Object.class);

        mocks.add(mock);

        when(field.getName()).thenReturn("testField");
        when(field.getType()).thenReturn(String.class);
        when(fieldInstance.getClass()).thenReturn(String.class);

        Stubber stubber = mock(Stubber.class);
        BeanPropertySetter propertySetter = mock(BeanPropertySetter.class);
        FieldSetter fieldSetter = mock(FieldSetter.class);

        when(propertySetter.set(fieldInstance, mock, "testValue")).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) {
                return null;
            }
        });
        when(fieldSetter.set(fieldInstance, field, mock)).thenReturn("testValue");

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        assertNotNull(injecter);

        injecter.thenInject();
    }

    @Test
    public void testFilterCandidate_OneMock_FieldAccessFailure() {
        Object mock = mock(Object.class);

        mocks.add(mock);

        when(field.getName()).thenReturn("testField");
        when(field.getType()).thenReturn(String.class);
        when(fieldInstance.getClass()).thenReturn(String.class);

        Stubber stubber = mock(Stubber.class);
        BeanPropertySetter propertySetter = mock(BeanPropertySetter.class);
        FieldSetter fieldSetter = mock(FieldSetter.class);

        when(propertySetter.set(fieldInstance, mock, "testValue")).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) {
                return null;
            }
        });
        when(fieldSetter.set(fieldInstance, field, mock)).thenThrow(new RuntimeException("Test Exception"));

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        assertNotNull(injecter);

        try {
            injecter.thenInject();
            assert(false); // Should not reach this point
        } catch (RuntimeException e) {
            reporter.cannotInjectDependency(field, mock, e);
        }
    }

    @Test
    public void testFilterCandidate_MultipleMocks_PropertySetterSuccess() {
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);

        mocks.add(mock1);
        mocks.add(mock2);

        when(field.getName()).thenReturn("testField");
        when(field.getType()).thenReturn(String.class);
        when(fieldInstance.getClass()).thenReturn(String.class);

        Stubber stubber = mock(Stubber.class);
        BeanPropertySetter propertySetter1 = mock(BeanPropertySetter.class);
        BeanPropertySetter propertySetter2 = mock(BeanPropertySetter.class);
        FieldSetter fieldSetter = mock(FieldSetter.class);

        when(propertySetter1.set(fieldInstance, mock1, "testValue1")).thenReturn(true);
        when(propertySetter2.set(fieldInstance, mock2, "testValue2")).thenReturn(true);
        when(fieldSetter.set(fieldInstance, field, mock1)).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) {
                return null;
            }
        });

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        assertNotNull(injecter);

        injecter.thenInject();
    }

    @Test
    public void testFilterCandidate_MultipleMocks_PropertySetterFailure() {
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);

        mocks.add(mock1);
        mocks.add(mock2);

        when(field.getName()).thenReturn("testField");
        when(field.getType()).thenReturn(String.class);
        when(fieldInstance.getClass()).thenReturn(String.class);

        Stubber stubber = mock(Stubber.class);
        BeanPropertySetter propertySetter1 = mock(BeanPropertySetter.class);
        BeanPropertySetter propertySetter2 = mock(BeanPropertySetter.class);
        FieldSetter fieldSetter = mock(FieldSetter.class);

        when(propertySetter1.set(fieldInstance, mock1, "testValue1")).thenThrow(new RuntimeException("Test Exception 1"));
        when(propertySetter2.set(fieldInstance, mock2, "testValue2")).thenReturn(true);
        when(fieldSetter.set(fieldInstance, field, mock2)).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) {
                return null;
            }
        });

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        assertNotNull(injecter);

        try {
            injecter.thenInject();
            assert(mock2.equals(fieldInstance)); // The second mock is the closest match
        } catch (RuntimeException e) {
            reporter.cannotInjectDependency(field, mock1, e);
        }
    }

    @Test
    public void testFilterCandidate_MultipleMocks_FieldAccessSuccess() {
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);

        mocks.add(mock1);
        mocks.add(mock2);

        when(field.getName()).thenReturn("testField");
        when(field.getType()).thenReturn(String.class);
        when(fieldInstance.getClass()).thenReturn(String.class);

        Stubber stubber = mock(Stubber.class);
        BeanPropertySetter propertySetter1 = mock(BeanPropertySetter.class);
        BeanPropertySetter propertySetter2 = mock(BeanPropertySetter.class);
        FieldSetter fieldSetter = mock(FieldSetter.class);

        when(propertySetter1.set(fieldInstance, mock1, "testValue1")).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) {
                return null;
            }
        });
        when(propertySetter2.set(fieldInstance, mock2, "testValue2")).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) {
                return null;
            }
        });
        when(fieldSetter.set(fieldInstance, field, mock1)).thenReturn("testValue1");

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        assertNotNull(injecter);

        injecter.thenInject();
    }

    @Test
    public void testFilterCandidate_MultipleMocks_FieldAccessFailure() {
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);

        mocks.add(mock1);
        mocks.add(mock2);

        when(field.getName()).thenReturn("testField");
        when(field.getType()).thenReturn(String.class);
        when(fieldInstance.getClass()).thenReturn(String.class);

        Stubber stubber = mock(Stubber.class);
        BeanPropertySetter propertySetter1 = mock(BeanPropertySetter.class);
        BeanPropertySetter propertySetter2 = mock(BeanPropertySetter.class);
        FieldSetter fieldSetter = mock(FieldSetter.class);

        when(propertySetter1.set(fieldInstance, mock1, "testValue1")).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) {
                return null;
            }
        });
        when(propertySetter2.set(fieldInstance, mock2, "testValue2")).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) {
                return null;
            }
        });
        when(fieldSetter.set(fieldInstance, field, mock2)).thenThrow(new RuntimeException("Test Exception"));

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        assertNotNull(injecter);

        try {
            injecter.thenInject();
            assert(mock2.equals(fieldInstance)); // The second mock is the closest match
        } catch (RuntimeException e) {
            reporter.cannotInjectDependency(field, mock1, e);
        }
    }
}