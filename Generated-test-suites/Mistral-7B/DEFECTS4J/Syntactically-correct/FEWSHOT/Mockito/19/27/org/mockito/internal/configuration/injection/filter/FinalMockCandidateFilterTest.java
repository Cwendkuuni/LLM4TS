package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.OngoingInjecter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.internal.util.reflection.FieldTestUtil;
import org.mockito.stubs.AnonymousClass1;
import org.mockito.stubs.AnonymousClass2;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class FinalMockCandidateFilterTest {

    private Collection<Object> mocks;
    private Field field;
    private Object fieldInstance;
    private Reporter reporter;

    @Before
    public void setup() {
        mocks = new ArrayList<>();
        field = FieldTestUtil.getField(AnonymousClass1.class, "field");
        fieldInstance = new AnonymousClass1();
        reporter = mock(Reporter.class);
    }

    @Test
    public void testFilterCandidateWithOneMockAndSuccessfulPropertySetter() {
        Object mock = new AnonymousClass2();
        mocks.add(mock);

        OngoingInjecter filterCandidate = new FinalMockCandidateFilter().filterCandidate(mocks, field, fieldInstance);

        OngoingInjecter expectedInjecter = new OngoingInjecter() {
            @Override
            public Object thenInject() {
                BeanPropertySetter.setField(fieldInstance, field, mock);
                return mock;
            }
        };

        assertNotNull(filterCandidate);
        assertEquals(expectedInjecter, filterCandidate);
    }

    @Test
    public void testFilterCandidateWithOneMockAndSuccessfulFieldSetter() {
        Object mock = new AnonymousClass2();
        mocks.add(mock);

        when(new BeanPropertySetter(fieldInstance, field).set(mock)).thenThrow(new RuntimeException());

        OngoingInjecter filterCandidate = new FinalMockCandidateFilter().filterCandidate(mocks, field, fieldInstance);

        OngoingInjecter expectedInjecter = new OngoingInjecter() {
            @Override
            public Object thenInject() {
                new FieldSetter(fieldInstance, field).set(mock);
                return mock;
            }
        };

        assertNotNull(filterCandidate);
        assertEquals(expectedInjecter, filterCandidate);
    }

    @Test
    public void testFilterCandidateWithOneMockAndFailedPropertyAndFieldSetter() {
        Object mock = new AnonymousClass2();
        mocks.add(mock);

        when(new BeanPropertySetter(fieldInstance, field).set(mock)).thenThrow(new RuntimeException());
        when(new FieldSetter(fieldInstance, field).set(mock)).thenThrow(new RuntimeException());

        OngoingInjecter filterCandidate = new FinalMockCandidateFilter().filterCandidate(mocks, field, fieldInstance);

        OngoingInjecter expectedInjecter = new OngoingInjecter() {
            @Override
            public Object thenInject() {
                throw new RuntimeException();
            }
        };

        assertNotNull(filterCandidate);
        assertEquals(expectedInjecter, filterCandidate);
    }

    @Test
    public void testFilterCandidateWithMultipleMocksAndNullCandidate() {
        Object mock1 = new AnonymousClass2();
        Object mock2 = new AnonymousClass2();
        mocks.add(mock1);
        mocks.add(mock2);

        OngoingInjecter filterCandidate = new FinalMockCandidateFilter().filterCandidate(mocks, field, fieldInstance);

        OngoingInjecter expectedInjecter = new OngoingInjecter() {
            @Override
            public Object thenInject() {
                return null;
            }
        };

        assertNotNull(filterCandidate);
        assertEquals(expectedInjecter, filterCandidate);
    }
}