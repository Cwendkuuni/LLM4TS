package org.mockito.internal.configuration.injection;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.MockitoConfiguration;
import org.mockito.internal.configuration.MockitoConfigurationBuilder;
import org.mockito.internal.configuration.injection.MockCandidateFilter;
import org.mockito.internal.configuration.injection.OngoingInjecter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.internal.util.reflection.FieldUtils;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;
import org.mockito.stub.Stubber;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FinalMockCandidateFilterTest {

    private static class TestClass {
        private String field;

        public void testMethod(String arg) {
            System.out.println(field);
            System.out.println(arg);
        }
    }

    @Test
    public void testFilterCandidate_oneMock_oneField_success() {
        // Given
        List<Object> mocks = Arrays.asList(mock(TestClass.class));
        Field field = FieldUtils.getField(TestClass.class, "field", false);
        Object fieldInstance = mock(TestClass.class);

        MockitoConfiguration config = new MockitoConfigurationBuilder().build();
        MockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        // When
        Object mock = injecter.getMockInstance();
        Stubber stubber = mockito3Stubber();
        when(mock).then(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                stubber.stub(invocation.getMethod()).toReturn("mocked value");
                return null;
            }
        });

        // Then
        injecter.inject();
        verify(mock).testMethod("argValue");
        Assert.assertEquals("mocked value", FieldUtils.getFieldValue(mock, "field"));
    }

    @Test
    public void testFilterCandidate_oneMock_oneField_fail() {
        // Given
        List<Object> mocks = Arrays.asList(mock(TestClass.class));
        Field field = FieldUtils.getField(TestClass.class, "field", false);
        Object fieldInstance = mock(TestClass.class);

        MockitoConfiguration config = new MockitoConfigurationBuilder().build();
        MockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        try {
            injecter.inject();
            Assert.fail("Expected MockitoException");
        } catch (MockitoException e) {
            // Then
            Assert.assertEquals("Problems injecting dependency in field", e.getMessage());
        }

        verify(fieldInstance, never()).setField(any(TestClass.class));
    }

    @Test
    public void testFilterCandidate_multipleMocks_oneField_success() {
        // Given
        List<Object> mocks = Arrays.asList(mock(TestClass.class), mock(TestClass.class));
        Field field = FieldUtils.getField(TestClass.class, "field", false);
        Object fieldInstance = mock(TestClass.class);

        MockitoConfiguration config = new MockitoConfigurationBuilder().build();
        MockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        // When
        Object mock1 = injecter.getMockInstance(0);
        Object mock2 = injecter.getMockInstance(1);

        Stubber stubber = mockito3Stubber();
        when(mock1).then(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                stubber.stub(invocation.getMethod()).toReturn("mock1 value");
                return null;
            }
        });

        when(mock2).then(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                stubber.stub(invocation.getMethod()).toReturn("mock2 value");
                return null;
            }
        });

        // Then
        injecter.inject();
        verify(mock1).testMethod("argValue");
        Assert.assertEquals("mock1 value", FieldUtils.getFieldValue(mock1, "field"));
        verify(mock2).testMethod("argValue");
        Assert.assertEquals("mock2 value", FieldUtils.getFieldValue(mock2, "field"));
    }

    @Test
    public void testFilterCandidate_multipleMocks_oneField_failure_onFirstMock() {
        // Given
        List<Object> mocks = Arrays.asList(mock(TestClass.class), mock(TestClass.class));
        Field field = FieldUtils.getField(TestClass.class, "field", false);
        Object fieldInstance = mock(TestClass.class);

        MockitoConfiguration config = new MockitoConfigurationBuilder().build();
        MockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        try {
            injecter.getMockInstance(0).testMethod("argValue");
            Assert.fail("Expected MockitoException");
        } catch (MockitoException e) {
            // When
            injecter.inject(); // continue with the second mock

            Object mock2 = injecter.getMockInstance(1);
            Stubber stubber = mockito3Stubber();
            when(mock2).then(new Answer() {
                @Override
                public Object answer(InvocationOnMock invocation) throws Throwable {
                    stubber.stub(invocation.getMethod()).toReturn("mock2 value");
                    return null;
                }
            });

            // Then
            verify(mock2).testMethod("argValue");
            Assert.assertEquals("mock2 value", FieldUtils.getFieldValue(mock2, "field"));
        }
    }

    @Test
    public void testFilterCandidate_multipleMocks_oneField_failure_onSecondMock() {
        // Given
        List<Object> mocks = Arrays.asList(mock(TestClass.class), mock(TestClass.class));
        Field field = FieldUtils.getField(TestClass.class, "field", false);
        Object fieldInstance = mock(TestClass.class);

        MockitoConfiguration config = new MockitoConfigurationBuilder().build();
        MockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        try {
            injecter.getMockInstance(1).testMethod("argValue");
            Assert.fail("Expected MockitoException");
        } catch (MockitoException e) {
            // When
            injecter.inject(); // continue with the first mock

            Object mock1 = injecter.getMockInstance(0);
            Stubber stubber = mockito3Stubber();
            when(mock1).then(new Answer() {
                @Override
                public Object answer(InvocationOnMock invocation) throws Throwable {
                    stubber.stub(invocation.getMethod()).toReturn("mock1 value");
                    return null;
                }
            });

            // Then
            verify(mock1).testMethod("argValue");
            Assert.assertEquals("mock1 value", FieldUtils.getFieldValue(mock1, "field"));
        }
    }

    @Test
    public void testFilterCandidate_multipleMocks_multipleFields_success() {
        // Given
        List<Object> mocks = Arrays.asList(mock(TestClass1.class), mock(TestClass2.class));
        TestClass1 testClass1 = mock(TestClass1.class);
        TestClass2 testClass2 = mock(TestClass2.class);

        Field field1 = FieldUtils.getField(TestClass1.class, "field1", false);
        Object fieldInstance1 = testClass1;

        Field field2 = FieldUtils.getField(TestClass2.class, "field2", false);
        Object fieldInstance2 = testClass2;

        MockitoConfiguration config = new MockitoConfigurationBuilder().build();
        MockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter1 = filter.filterCandidate(mocks, field1, fieldInstance1);
        OngoingInjecter injecter2 = filter.filterCandidate(mocks, field2, fieldInstance2);

        // When
        Object mock1 = injecter1.getMockInstance();
        Object mock2 = injecter2.getMockInstance();

        Stubber stubber = mockito3Stubber();
        when(mock1).then(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                stubber.stub(invocation.getMethod()).toReturn("mock1 value");
                return null;
            }
        });

        when(mock2).then(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                stubber.stub(invocation.getMethod()).toReturn("mock2 value");
                return null;
            }
        });

        // Then
        injecter1.inject();
        injecter2.inject();

        verify(mock1).testMethod1();
        Assert.assertEquals("mock1 value", FieldUtils.getFieldValue(mock1, "field1"));

        verify(mock2).testMethod2();
        Assert.assertEquals("mock2 value", FieldUtils.getFieldValue(mock2, "field2"));
    }

    @Test
    public void testFilterCandidate_multipleMocks_multipleFields_failure() {
        // Given
        List<Object> mocks = Arrays.asList(mock(TestClass1.class), mock(TestClass2.class));
        TestClass1 testClass1 = mock(TestClass1.class);
        TestClass2 testClass2 = mock(TestClass2.class);

        Field field1 = FieldUtils.getField(TestClass1.class, "field1", false);
        Object fieldInstance1 = testClass1;

        Field field2 = FieldUtils.getField(TestClass2.class, "field2", false);
        Object fieldInstance2 = testClass2;

        MockitoConfiguration config = new MockitoConfigurationBuilder().build();
        MockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter1 = filter.filterCandidate(mocks, field1, fieldInstance1);
        OngoingInjecter injecter2 = filter.filterCandidate(mocks, field2, fieldInstance2);

        try {
            injecter1.inject(); // should succeed
            injecter2.inject(); // should throw exception

            Assert.fail("Expected MockitoException");
        } catch (MockitoException e) {
            // Then
            Assert.assertEquals("Problems injecting dependency in field", e.getMessage());
        }

        verify(fieldInstance1, never()).setField(any(TestClass1.class));
        verify(fieldInstance2, never()).setField(any(TestClass2.class));
    }

    private Stubber mockito3Stubber() {
        return spy(stub(InvocationOnMock.class));
    }

    private static class TestClass1 {
        private String field1;

        public void testMethod1() {
            System.out.println(field1);
        }
    }

    private static class TestClass2 {
        private String field2;

        public void testMethod2() {
            System.out.println(field2);
        }
    }
}