package org.mockito.internal.configuration.injection;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.MockCandidateFilter;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.internal.util.reflection.FieldType;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

public class FinalMockCandidateFilterTest {

    @Test
    public void testFilterCandidate_OneMock_Success() {
        Collection<Object> mocks = new ArrayList<>();
        Object mock = new Object() {};
        Object fieldInstance = new Object() {};
        Field field = FieldType.getDeclaredField(FinalMockCandidateFilter.class, "mocks");
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        Assert.assertEquals(new OngoingInjecter() {
            @Override
            public boolean thenInject() {
                new FieldSetter(fieldInstance, field).set(mock);
                return true;
            }
        }, filter.filterCandidate(mocks, field, fieldInstance));
    }

    @Test
    public void testFilterCandidate_OneMock_Failure() {
        Collection<Object> mocks = new ArrayList<>();
        Object mock = new Object() {};
        Object fieldInstance = new Object() {};
        Field field = FieldType.getDeclaredField(FinalMockCandidateFilter.class, "mocks");
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        try {
            mocks.add(null);
            filter.filterCandidate(mocks, field, fieldInstance);
            Assert.fail();
        } catch (MockitoException e) {
            // Expected
        }
    }

    @Test
    public void testFilterCandidate_MultipleMocks_NoMatch() {
        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = new Object() {};
        Object mock2 = new Object() {};
        Object fieldInstance = new Object() {};
        Field field = FieldType.getDeclaredField(FinalMockCandidateFilter.class, "mocks");
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        Assert.assertEquals(new OngoingInjecter() {
            @Override
            public boolean thenInject() {
                return false;
            }
        }, filter.filterCandidate(mocks, field, fieldInstance));
    }

    @Test
    public void testFilterCandidate_MultipleMocks_FirstMatch() {
        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = new Object() {};
        Object mock2 = new Object() {};
        Object fieldInstance = new Object() {};
        Field field = FieldType.getDeclaredField(FinalMockCandidateFilter.class, "mocks");
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        mocks.add(mock1);
        mocks.add(mock2);

        Object matchingMock = mock1;

        Assert.assertEquals(new OngoingInjecter() {
            @Override
            public boolean thenInject() {
                try {
                    new FieldSetter(fieldInstance, field).set(matchingMock);
                } catch (Exception e) {
                    throw new MockitoException("Problems injecting dependency in " + field.getName(), e);
                }
                return true;
            }
        }, filter.filterCandidate(mocks, field, fieldInstance));
    }

    @Test
    public void testFilterCandidate_MultipleMocks_SecondMatch() {
        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = new Object() {};
        Object mock2 = new Object() {};
        Object fieldInstance = new Object() {};
        Field field = FieldType.getDeclaredField(FinalMockCandidateFilter.class, "mocks");
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        mocks.add(mock1);
        mocks.add(mock2);

        Object matchingMock = mock2;

        Assert.assertEquals(new OngoingInjecter() {
            @Override
            public boolean thenInject() {
                try {
                    new FieldSetter(fieldInstance, field).set(matchingMock);
                } catch (Exception e) {
                    throw new MockitoException("Problems injecting dependency in " + field.getName(), e);
                }
                return true;
            }
        }, filter.filterCandidate(mocks, field, fieldInstance));
    }

    @Test
    public void testFilterCandidate_NullCollection() {
        Field field = FieldType.getDeclaredField(FinalMockCandidateFilter.class, "mocks");
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        Assert.assertEquals(new OngoingInjecter() {
            @Override
            public boolean thenInject() {
                return false;
            }
        }, filter.filterCandidate(null, field, new Object() {}));
    }

    @Test
    public void testFilterCandidate_NullField() {
        Collection<Object> mocks = new ArrayList<>();
        Object fieldInstance = new Object() {};
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        Assert.assertEquals(new OngoingInjecter() {
            @Override
            public boolean thenInject() {
                return false;
            }
        }, filter.filterCandidate(mocks, null, fieldInstance));
    }

    @Test
    public void testFilterCandidate_NullFieldInstance() {
        Collection<Object> mocks = new ArrayList<>();
        Field field = FieldType.getDeclaredField(FinalMockCandidateFilter.class, "mocks");
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        Assert.assertEquals(new OngoingInjecter() {
            @Override
            public boolean thenInject() {
                return false;
            }
        }, filter.filterCandidate(mocks, field, null));
    }
}