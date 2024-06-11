package org.mockito.internal.configuration.injection;

import org.junit.Rule;
import org.junit.rules.TestRule;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.utils.Fields;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.mockitoSession;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class FinalMockCandidateFilterTest {

    @Rule
    public TestRule mockitoRule = new mockitoSession();

    @Test
    public void testFilterCandidate_SingleMock_Success() {
        Collection<Object> mocks = Arrays.asList(mock(TestClass.class));
        Field field = Fields.privateField(TestClass.class, "fieldToInject");
        Object fieldInstance = new TestClass();

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(injecter);
        assertNotNull(injecter.inject());
    }

    @Test
    public void testFilterCandidate_SingleMock_Failure() {
        Collection<Object> mocks = Arrays.asList(mock(TestClass.class));
        Field field = Fields.privateField(TestClass.class, "fieldToInject");
        Object fieldInstance = new TestClass();

        doThrow(new RuntimeException("Test Exception")).when(mocks.get(0)).setField(field.getName(), any());

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(injecter);
        assertNull(injecter.inject());
    }

    @Test
    public void testFilterCandidate_MultipleMocks_Success() {
        Collection<Object> mocks = Arrays.asList(mock(TestClass1.class), mock(TestClass2.class));
        Field field = Fields.privateField(TestClass.class, "fieldToInject");
        Object fieldInstance = new TestClass();

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(injecter);
        assertTrue(injecter.inject());
    }

    @Test
    public void testFilterCandidate_MultipleMocks_Failure_PropertySetter() {
        Collection<Object> mocks = Arrays.asList(mock(TestClass1.class), mock(TestClass2.class));
        Field field = Fields.privateField(TestClass.class, "fieldToInject");
        Object fieldInstance = spy(new TestClass());

        doThrow(new RuntimeException("Test Exception")).when(mocks.get(0)).setField(field.getName(), any());

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(injecter);
        assertNull(injecter.inject());
    }

    @Test
    public void testFilterCandidate_MultipleMocks_Failure_FieldAccess() {
        Collection<Object> mocks = Arrays.asList(mock(TestClass1.class), mock(TestClass2.class));
        Field field = Fields.privateField(TestClass.class, "fieldToInject");
        Object fieldInstance = new TestClass();

        doThrow(new RuntimeException("Test Exception")).when(mocks.get(0)).getField(field.getName());

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(injecter);
        assertNull(injecter.inject());
    }

    @Test(expected = MockitoException.class)
    public void testFilterCandidate_NullCollection() {
        Field field = Fields.privateField(TestClass.class, "fieldToInject");
        Object fieldInstance = new TestClass();

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        filter.filterCandidate(null, field, fieldInstance);
    }

    @Test(expected = MockitoException.class)
    public void testFilterCandidate_NullField() {
        Collection<Object> mocks = Arrays.asList(mock(TestClass.class));
        Object fieldInstance = new TestClass();

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        filter.filterCandidate(mocks, null, fieldInstance);
    }

    @Test(expected = MockitoException.class)
    public void testFilterCandidate_NullFieldInstance() {
        Collection<Object> mocks = Arrays.asList(mock(TestClass.class));
        Field field = Fields.privateField(TestClass.class, "fieldToInject");

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        filter.filterCandidate(mocks, field, null);
    }

    static class TestClass {
        private Field fieldToInject;
    }

    static class TestClass1 {}

    static class TestClass2 {}
}