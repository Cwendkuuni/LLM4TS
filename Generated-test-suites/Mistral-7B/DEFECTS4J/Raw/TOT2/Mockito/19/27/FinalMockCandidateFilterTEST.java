package org.mockito.internal.configuration.injection.filter;

import org.junit.*;
import org.junit.Test;
import org.mockito.*;
import org.mockito.Assert;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.OngoingInjecter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.internal.util.reflection.bean.BeanProperty;
import org.mockito.mockitosis.MockitoTestBase;

import java.lang.reflect.*;
import java.util.*;

public class FinalMockCandidateFilterTest extends MockitoTestBase {

    @Test
    public void testFilterCandidate() throws Exception {
        final Collection<Object> mocks = Collections.singletonList(new Object());
        final Field field = new Field("testField") {
            @Override
            public Class<?> getType() {
                return String.class;
            }
        };
        final Object fieldInstance = new Object();

        MockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        Assert.assertNotNull(injecter);
    }

    @Test
    public void testFilterCandidate_TwoMocks() throws Exception {
        final Collection<Object> mocks = Arrays.asList(new Object(), new Object());
        final Field field = new Field("testField") {
            @Override
            public Class<?> getType() {
                return String.class;
            }
        };
        final Object fieldInstance = new Object();

        MockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        Assert.assertNotNull(injecter);
    }

    @Test(expected = AssertionError.class)
    public void testFilterCandidate_ExceptionHandling() throws Exception {
        final Field field = new Field("testField") {
            @Override
            public Class<?> getType() {
                return String.class;
            }
        };
        final Object fieldInstance = new Object();

        MockCandidateFilter filter = new FinalMockCandidateFilter();
        filter.filterCandidate(Collections.singletonList(new Object()), field, fieldInstance); // No return statement
    }

    @Test
    public void testFilterCandidate_NullMocksCollection() {
        final Field field = new Field("testField") {
            @Override
            public Class<?> getType() {
                return String.class;
            }
        };
        final Object fieldInstance = new Object();

        MockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(null, field, fieldInstance);

        Assert.assertNotNull(injecter);
    }
}