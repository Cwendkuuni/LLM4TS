package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;

import java.lang.reflect.Field;
import java.util.Collection;

public class FinalMockCandidateFilterTest {
    @Test
    public void testFilterCandidate_singleMock() {
        Collection<Object> mocks = new ArrayList<>();
        DummyMock mock = new DummyMock();
        mocks.add(mock);

        DummyFieldInstance fieldInstance = new DummyFieldInstance();
        Field field = new Field();

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(injecter);
        assertEquals(mock, injecter.thenInject());

        assertEquals(mock, fieldInstance.getMockSetBySetter());
        assertNull(fieldInstance.getMockSetByField());
    }

    @Test
    public void testFilterCandidate_multipleMocks() {
        Collection<Object> mocks = new ArrayList<>();
        DummyMock mock1 = new DummyMock();
        DummyMock mock2 = new DummyMock();
        mocks.add(mock1);
        mocks.add(mock2);

        DummyFieldInstance fieldInstance = new DummyFieldInstance();
        Field field = new Field();

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(injecter);
        assertNull(injecter.thenInject());

        assertNull(fieldInstance.getMockSetBySetter());
        assertNull(fieldInstance.getMockSetByField());
    }

    private class DummyMock {}

    private class DummyFieldInstance {
        private DummyMock mockSetBySetter;
        private DummyMock mockSetByField;

        public boolean setMockBySetter(DummyMock mock) {
            this.mockSetBySetter = mock;
            return true;
        }

        public void setMockByField(DummyMock mock) {
            this.mockSetByField = mock;
        }

        public DummyMock getMockSetBySetter() {
            return mockSetBySetter;
        }

        public DummyMock getMockSetByField() {
            return mockSetByField;
        }
    }

    private class MockCandidateFilter {
        public OngoingInjecter filterCandidate(Collection<Object> mocks, Field field, Object fieldInstance) {
            if(mocks.size() == 1) {
                final Object matchingMock = mocks.iterator().next();

                return new OngoingInjecter() {
                    public Object thenInject() {
                        try {
                            if (!new BeanPropertySetter(fieldInstance, field).set(matchingMock)) {
                                new FieldSetter(fieldInstance, field).set(matchingMock);
                            }
                        } catch (RuntimeException e) {
                            new Reporter().cannotInjectDependency(field, matchingMock, e);
                        }
                        return matchingMock;
                    }
                };
            }

            return new OngoingInjecter() {
                public Object thenInject() {
                    return null;
                }
            };
        }
    }
}