package org.mockito.internal.configuration.injection.filter;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.util.MockUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class NameBasedCandidateFilterTest {
    private MockCandidateFilter nextMock;
    private NameBasedCandidateFilter filter;

    @Test
    public void testFilterCandidate_OneMock_NoConflictingFields() {
        Collection<Object> mocks = new ArrayList<>();
        Object mock = new Object();
        mocks.add(mock);

        Field field = new Field() {
            @Override
            public String getName() {
                return "testField";
            }
        };

        Object fieldInstance = new Object();

        nextMock = new StubMockCandidateFilter();
        filter = new NameBasedCandidateFilter(nextMock);

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        Assert.assertEquals(mock, result.getMock());
        Assert.assertNull(nextMock.getLastFilteredMock());
    }

    @Test
    public void testFilterCandidate_OneMock_ConflictingField() {
        Collection<Object> mocks = new ArrayList<>();
        Object mock = new Object();
        mocks.add(mock);

        Field field = new Field() {
            @Override
            public String getName() {
                return "testField";
            }
        };

        Object fieldInstance = new Object();

        Object conflictingMock = new Object();
        Field conflictingField = new Field() {
            @Override
            public String getName() {
                return "testField";
            }
        };

        mocks.add(conflictingMock);

        nextMock = new StubMockCandidateFilter();
        filter = new NameBasedCandidateFilter(nextMock);

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        Assert.assertEquals(conflictingMock, result.getMock());
        Assert.assertNull(nextMock.getLastFilteredMock());
    }

    @Test
    public void testFilterCandidate_MultipleMocks_NoConflictingFields() {
        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = new Object();
        Object mock2 = new Object();
        mocks.add(mock1);
        mocks.add(mock2);

        Field field = new Field() {
            @Override
            public String getName() {
                return "testField";
            }
        };

        Object fieldInstance = new Object();

        nextMock = new StubMockCandidateFilter();
        filter = new NameBasedCandidateFilter(nextMock);

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        if (mock1.getClass().getName().equals(result.getMock().getClass().getName())) {
            Assert.assertEquals(mock1, result.getMock());
        } else {
            Assert.assertEquals(mock2, result.getMock());
        }

        Assert.assertNull(nextMock.getLastFilteredMock());
    }

    @Test
    public void testFilterCandidate_MultipleMocks_ConflictingFields() {
        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = new Object();
        Object mock2 = new Object();
        mocks.add(mock1);
        mocks.add(mock2);

        Field field = new Field() {
            @Override
            public String getName() {
                return "testField";
            }
        };

        Object fieldInstance = new Object();

        Object conflictingMock1 = new Object();
        Field conflictingField1 = new Field() {
            @Override
            public String getName() {
                return "testField";
            }
        };

        Object conflictingMock2 = new Object();
        Field conflictingField2 = new Field() {
            @Override
            public String getName() {
                return "testField";
            }
        };

        mocks.add(conflictingMock1);
        mocks.add(conflictingMock2);

        nextMock = new StubMockCandidateFilter();
        filter = new NameBasedCandidateFilter(nextMock);

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        if (conflictingMock1.getClass().getName().equals(result.getMock().getClass().getName())) {
            Assert.assertEquals(conflictingMock1, result.getMock());
        } else {
            Assert.assertEquals(conflictingMock2, result.getMock());
        }

        Assert.assertEquals(conflictingMock1, nextMock.getLastFilteredMock());
    }

    @Test
    public void testFilterCandidate_MultipleMocks_ConflictingFields_NextFilterPasses() {
        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = new Object();
        Object mock2 = new Object();
        mocks.add(mock1);
        mocks.add(mock2);

        Field field = new Field() {
            @Override
            public String getName() {
                return "testField";
            }
        };

        Object fieldInstance = new Object();

        Object conflictingMock1 = new Object();
        Field conflictingField1 = new Field() {
            @Override
            public String getName() {
                return "testField";
            }
        };

        Object conflictingMock2 = new Object();
        Field conflictingField2 = new Field() {
            @Override
            public String getName() {
                return "testField";
            }
        };

        mocks.add(conflictingMock1);
        mocks.add(conflictingMock2);

        StubMockCandidateFilter nextStubFilter = new StubMockCandidateFilter() {
            @Override
            public OngoingInjecter filterCandidate(Collection<Object> mocks, Field field, Object fieldInstance) {
                if (field.getName().equals("conflictingField")) {
                    return new OngoingInjecter(mock2, field, fieldInstance);
                }
                return nextMock.filterCandidate(mocks, field, fieldInstance);
            }
        };

        nextMock = nextStubFilter;
        filter = new NameBasedCandidateFilter(nextMock);

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        Assert.assertEquals(mock2, result.getMock());
        Assert.assertEquals(conflictingMock1, nextMock.getLastFilteredMock());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFilterCandidate_NullNextFilter() {
        Collection<Object> mocks = new ArrayList<>();
        Object mock = new Object();
        mocks.add(mock);

        Field field = new Field() {
            @Override
            public String getName() {
                return "testField";
            }
        };

        Object fieldInstance = new Object();

        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(null);
        filter.filterCandidate(mocks, field, fieldInstance);
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidate_NullMocks() {
        Field field = new Field() {
            @Override
            public String getName() {
                return "testField";
            }
        };

        Object fieldInstance = new Object();

        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(nextMock);
        filter.filterCandidate(null, field, fieldInstance);
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidate_NullField() {
        Collection<Object> mocks = new ArrayList<>();
        Object mock = new Object();
        mocks.add(mock);

        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(nextMock);
        filter.filterCandidate(mocks, null, new Object());
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidate_NullFieldInstance() {
        Collection<Object> mocks = new ArrayList<>();
        Object mock = new Object();
        mocks.add(mock);

        Field field = new Field() {
            @Override
            public String getName() {
                return "testField";
            }
        };

        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(nextMock);
        filter.filterCandidate(mocks, field, null);
    }

    private static class StubMockCandidateFilter implements MockCandidateFilter {
        @Override
        public OngoingInjecter filterCandidate(Collection<Object> mocks, Field field, Object fieldInstance) {
            if (nextMock != null) {
                return nextMock.filterCandidate(mocks, field, fieldInstance);
            }
            throw new IllegalStateException("nextMock should not be null");
        }
    }
}