package org.mockito.internal.configuration.injection;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.MockitoConfiguration;
import org.mockito.internal.configuration.injection.MockCandidateFilter;
import org.mockito.internal.configuration.injection.OngoingInjecter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.internal.util.reflection.FieldUtils;
import org.mockito.internal.util.reflection.Whitebox;
import org.powermock.reflect.Whitebox2;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.mockito.internal.util.reflection.FieldUtils.getFieldValue;
import static org.powermock.api.support.annotation.MockAnnotation.Mock;

public class FinalMockCandidateFilterTest {

    private Collection<Object> mocks;
    private Field field;
    private Object fieldInstance;

    @Before
    public void setup() {
        mocks = new ArrayList<>();
        field = new FieldUtils().field(Object.class, "field1");
        fieldInstance = new Object() {{
            this.field1 = "field value";
        }};
    }

    @Test
    public void testFilterCandidate_singleMock_fieldSetterSuccess() {
        mocks.add(new TestMock());

        MockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        TestMock mock = (TestMock) Whitebox.getInternalState(mocks.iterator().next());
        assertSame(fieldInstance, mock.field1);
        assertEquals(true, injecter.thenInject());
    }

    @Test
    public void testFilterCandidate_singleMock_fieldSetterFailure() {
        mocks.add(new TestMock());

        MockitoException exception = assertThrows(MockitoException.class, () -> {
            MockCandidateFilter filter = new FinalMockCandidateFilter();
            filter.filterCandidate(mocks, field, fieldInstance);
        });

        String expectedMessage = "Problems injecting dependency in field1";
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void testFilterCandidate_singleMock_fieldAccessSuccess() {
        FieldUtils.writeFinalStaticField(MockitoConfiguration.class, "fieldAccessible", true);
        mocks.add(new TestMock());

        MockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        TestMock mock = (TestMock) getFieldValue(mocks.iterator().next(), "mockitoMock");
        assertSame(fieldInstance, Whitebox2.getInternalValue(mock, field));
        assertEquals(true, injecter.thenInject());
    }

    @Test
    public void testFilterCandidate_singleMock_fieldAccessFailure() {
        MockitoException exception = assertThrows(MockitoException.class, () -> {
            MockCandidateFilter filter = new FinalMockCandidateFilter();
            filter.filterCandidate(mocks, field, fieldInstance);
        });

        String expectedMessage = "Problems injecting dependency in field1";
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void testFilterCandidate_multipleMocks_noMatch() {
        mocks.add(new TestMock1());
        mocks.add(new TestMock2());

        MockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertFalse(injecter.thenInject());
    }

    @Test
    public void testFilterCandidate_multipleMocks_fieldSetterSuccess() {
        mocks.add(new TestMockWithField1("expectedValue"));
        mocks.add(new TestMockWithField1("differentValue"));

        MockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        TestMockWithField1 mock = (TestMockWithField1) mocks.iterator().next();
        assertSame("expectedValue", mock.field1);
        assertEquals(true, injecter.thenInject());
    }

    @Test
    public void testFilterCandidate_multipleMocks_fieldAccessSuccess() {
        FieldUtils.writeFinalStaticField(MockitoConfiguration.class, "fieldAccessible", true);
        mocks.add(new TestMockWithField1("expectedValue"));
        mocks.add(new TestMockWithField1("differentValue"));

        MockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        TestMockWithField1 mock = (TestMockWithField1) getFieldValue(mocks.iterator().next(), "mockitoMock");
        assertSame("expectedValue", Whitebox2.getInternalValue(mock, field));
        assertEquals(true, injecter.thenInject());
    }

    @Test
    public void testFilterCandidate_multipleMocks_noField() {
        mocks.add(new TestMockNoField());
        mocks.add(new TestMockNoField());

        MockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertFalse(injecter.thenInject());
    }

    @Test
    public void testFilterCandidate_multipleMocks_fieldSetterFailure() {
        mocks.add(new TestMockWithField1("expectedValue"));
        mocks.add(new TestMockWithField1("differentValue"));

        MockitoException exception = assertThrows(MockitoException.class, () -> {
            MockCandidateFilter filter = new FinalMockCandidateFilter();
            filter.filterCandidate(mocks, field, fieldInstance);
        });

        String expectedMessage = "Problems injecting dependency in field1";
        assertEquals(expectedMessage, exception.getMessage());
    }

    static class TestMock {
        public String field1;
    }

    static class TestMock1 {
        // Different class
    }

    static class TestMock2 {
        public String field1;
    }

    static class TestMockWithField1 {
        @Mock
        public String field1;

        public TestMockWithField1(String value) {
            this.field1 = value;
        }
    }

    static class TestMockNoField {
        // No field named 'field1'
    }
}