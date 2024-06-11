package org.mockito.internal.configuration.injection;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.MockitoConfiguration;
import org.mockito.internal.configuration.injection.MockCandidateFilter;
import org.mockito.internal.configuration.injection.OngoingInjecter;
import org.mockito.internal.reflection.FieldSetter;
import org.mockito.internal.util.reflection.Field;
import org.mockito.internal.util.reflection.Fields;
import org.mockito.mockitis.Mockito;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class FinalMockCandidateFilterTest {

    private static class TestClass {
        private int field;

        public void setField(int value) {
            this.field = value;
        }

        public int getField() {
            return this.field;
        }

        public void methodWithField(int arg) {
            this.field += arg;
        }
    }

    @Test
    public void testFilterCandidate_singleMock_fieldSetterSuccess() {
        TestClass testClass = mock(TestClass.class);
        Field field = Fields.privateField(TestClass.class, "field");
        Object fieldInstance = Fields.getPrivate(testClass, field);

        Collection<Object> mocks = new ArrayList<>();
        mocks.add(spy(TestClass.class));

        MockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        Assert.assertNotNull(injecter);
        Assert.assertFalse(injecter.thenInject());

        TestClass mock = (TestClass) mocks.get(0);
        verify(mock).setField(Fields.getPrivate(testClass, field.getName()));
    }

    @Test
    public void testFilterCandidate_singleMock_fieldSetterFailure() {
        TestClass testClass = mock(TestClass.class);
        Field field = Fields.privateField(TestClass.class, "field");
        Object fieldInstance = Fields.getPrivate(testClass, field);

        Collection<Object> mocks = new ArrayList<>();
        mocks.add(spy(TestClass.class));

        MockCandidateFilter filter = new FinalMockCandidateFilter();
        try {
            filter.filterCandidate(mocks, field, fieldInstance);
            Assert.fail("Expected MockitoException");
        } catch (MockitoException e) {
            // expected
        }
    }

    @Test
    public void testFilterCandidate_singleMock_fieldAccessSuccess() throws IllegalAccessException {
        TestClass testClass = mock(TestClass.class);
        Field field = Fields.privateField(TestClass.class, "field");
        Object fieldInstance = Fields.getPrivate(testClass, field);

        Collection<Object> mocks = new ArrayList<>();
        mocks.add(testClass);

        MockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        Assert.assertNotNull(injecter);
        Assert.assertFalse(injecter.thenInject());

        FieldSetter fieldSetter = new FieldSetter(testClass, field);
        Assert.assertEquals(fieldInstance, fieldSetter.getRaw());
        fieldSetter.set(testClass, fieldInstance);
    }

    @Test
    public void testFilterCandidate_singleMock_fieldAccessFailure() {
        TestClass testClass = mock(TestClass.class);
        Field field = Fields.privateField(TestClass.class, "field");
        Object fieldInstance = Fields.getPrivate(testClass, field);

        Collection<Object> mocks = new ArrayList<>();
        mocks.add(testClass);

        MockCandidateFilter filter = new FinalMockCandidateFilter();
        try {
            filter.filterCandidate(mocks, field, fieldInstance);
            Assert.fail("Expected MockitoException");
        } catch (MockitoException e) {
            // expected
        }
    }

    @Test
    public void testFilterCandidate_multipleMocks_fieldSetterFailure() {
        TestClass testClass = mock(TestClass.class);
        Field field = Fields.privateField(TestClass.class, "field");
        Object fieldInstance = Fields.getPrivate(testClass, field);

        Collection<Object> mocks = new ArrayList<>();
        mocks.add(spy(TestClass.class));
        mocks.add(spy(TestClass.class));

        MockCandidateFilter filter = new FinalMockCandidateFilter();
        try {
            filter.filterCandidate(mocks, field, fieldInstance);
            Assert.fail("Expected MockitoException");
        } catch (MockitoException e) {
            // expected
        }
    }

    @Test
    public void testFilterCandidate_multipleMocks_fieldAccessSuccess() throws IllegalAccessException {
        TestClass testClass1 = mock(TestClass.class);
        TestClass testClass2 = mock(TestClass.class);
        Field field = Fields.privateField(TestClass.class, "field");
        Object fieldInstance = Fields.getPrivate(testClass1, field);

        Collection<Object> mocks = new ArrayList<>();
        mocks.add(testClass1);
        mocks.add(testClass2);

        MockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        Assert.assertNotNull(injecter);
        Assert.assertFalse(injecter.thenInject());

        FieldSetter fieldSetter1 = new FieldSetter(testClass1, field);
        Assert.assertEquals(fieldInstance, fieldSetter1.getRaw());
        fieldSetter1.set(testClass1, fieldInstance);

        FieldSetter fieldSetter2 = new FieldSetter(testClass2, field);
        Assert.assertNotSame(fieldInstance, fieldSetter2.getRaw());
        fieldSetter2.set(testClass2, fieldInstance);
    }

    @Test
    public void testFilterCandidate_multipleMocks_fieldAccessFailure() {
        TestClass testClass1 = mock(TestClass.class);
        TestClass testClass2 = mock(TestClass.class);
        Field field = Fields.privateField(TestClass.class, "field");
        Object fieldInstance = Fields.getPrivate(testClass1, field);

        Collection<Object> mocks = new ArrayList<>();
        mocks.add(testClass1);
        mocks.add(testClass2);

        MockCandidateFilter filter = new FinalMockCandidateFilter();
        try {
            filter.filterCandidate(mocks, field, fieldInstance);
            Assert.fail("Expected MockitoException");
        } catch (MockitoException e) {
            // expected
        }
    }

    @Test
    public void testFilterCandidate_multipleMocks_noMatchingField() {
        TestClass testClass1 = mock(TestClass.class);
        TestClass testClass2 = mock(TestClass.class);
        Field field = Fields.privateField(TestClass.class, "notExistingField");
        Object fieldInstance = Fields.getPrivate(testClass1, field);

        Collection<Object> mocks = new ArrayList<>();
        mocks.add(testClass1);
        mocks.add(testClass2);

        MockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        Assert.assertNotNull(injecter);
        Assert.assertFalse(injecter.thenInject());
    }

    @Test
    public void testFilterCandidate_propertySetterSuccess() {
        TestClass testClass = mock(TestClass.class);
        Field field = Fields.privateField(TestClass.class, "field");
        Object fieldInstance = Fields.getPrivate(testClass, field);

        Method method = Mockito.method(TestClass.class, "setField", int.class);
        Object mockMethod = Mockito.mock(Method.class);
        Mockito.when(testClass.getClass().getMethod("setField", int.class)).thenReturn(mockMethod);

        Collection<Object> mocks = new ArrayList<>();
        mocks.add(testClass);

        MockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        Assert.assertNotNull(injecter);
        Assert.assertFalse(injecter.thenInject());

        Object mockMethodResult = Mockito.mock(TestClass.class);
        Mockito.when(mockMethod.invoke(testClass, fieldInstance)).thenReturn(mockMethodResult);

        Method setterMethod = Mockito.method(TestClass.class, "setField", int.class);
        Object setter = Mockito.mock(Method.class);
        Mockito.when(testClass.getClass().getMethod("setField", int.class)).thenReturn(setter);

        Mockito.when(setter.invoke(testClass, fieldInstance)).thenReturn(testClass);

        Assert.assertEquals(testClass, injecter.thenInject());
    }

    @Test
    public void testFilterCandidate_propertySetterFailure() {
        TestClass testClass = mock(TestClass.class);
        Field field = Fields.privateField(TestClass.class, "field");
        Object fieldInstance = Fields.getPrivate(testClass, field);

        Method method = Mockito.method(TestClass.class, "setField", int.class);
        Object mockMethod = Mockito.mock(Method.class);
        Mockito.when(testClass.getClass().getMethod("setField", int.class)).thenReturn(mockMethod);

        Collection<Object> mocks = new ArrayList<>();
        mocks.add(testClass);

        MockCandidateFilter filter = new FinalMockCandidateFilter();
        try {
            filter.filterCandidate(mocks, field, fieldInstance);
            Assert.fail("Expected MockitoException");
        } catch (MockitoException e) {
            // expected
        }
    }
}