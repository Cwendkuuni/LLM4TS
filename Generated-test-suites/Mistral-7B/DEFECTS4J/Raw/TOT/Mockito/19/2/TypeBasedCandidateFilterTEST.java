import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.stubbing.OngoingStubbing;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class TypeBasedCandidateFilterTest {

    private MockCandidateFilter filter;
    private Object fieldInstance;
    private Field testField;
    private Collection<Object> mocks;

    @Before
    public void setup() {
        this.filter = new TypeBasedCandidateFilter(null);
        this.fieldInstance = mock(Object.class);
        this.testField = Whitebox.getInternalState(this, "testField");
        this.mocks = new ArrayList<>();
        mocks.add(mock(Type1.class));
        mocks.add(mock(Type2.class));
        mocks.add(mock(Type3.class));
    }

    @Test
    public void testFilterCandidate_WhenFieldTypeIsAssignableFromMocks_ThenCorrectMockIsReturned() {
        Whitebox.setInternalState(this, "testField", new Field("testField", Type1.class, this));
        OngoingStubbing<Object> correctMock = this.filter.filterCandidate(mocks, testField, fieldInstance);
        verify(testField).set(fieldInstance, correctMock.getMock());
    }

    @Test
    public void testFilterCandidate_WhenFieldTypeIsNotAssignableFromMocks_ThenNextFilterIsCalled() {
        Whitebox.setInternalState(this, "testField", new Field("testField", Type2.class, this));
        OngoingStubbing<Object> returnedMock = this.filter.filterCandidate(mocks, testField, fieldInstance);
        MockCandidateFilter nextFilter = spy(new TypeBasedCandidateFilter(null));
        OngoingStubbing<Object> nextReturnedMock = nextFilter.filterCandidate(mocks, testField, fieldInstance);
        verify(nextFilter).filterCandidate(mocks, testField, fieldInstance);
        verify(nextReturnedMock).getMock();
        verify(returnedMock).getMock();
    }
}