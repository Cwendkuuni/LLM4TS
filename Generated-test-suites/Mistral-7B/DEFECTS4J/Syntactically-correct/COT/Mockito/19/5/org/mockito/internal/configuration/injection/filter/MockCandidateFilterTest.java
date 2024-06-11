import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.util.collections.Arrays;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MockCandidateFilterTest {

    @Test
    public void testFilterCandidate() {
        MockCandidateFilter mockCandidateFilter = (mockCandidates, field, fieldInstance) -> mockCandidates.get(0);

        Collection<Object> mocks = Arrays.asList(new MockObject(), new MockObject(), new MockObject());
        Field fieldToBeInjected = new Field("testField", Object.class, MockObject.class);
        Object fieldInstance = new MockObject();

        OngoingInjecter result = mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        Assert.assertEquals(mockCandidates.get(0), result);
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidateWithNullMocks() {
        MockCandidateFilter mockCandidateFilter = (mockCandidates, field, fieldInstance) -> mockCandidates.get(0);

        Collection<Object> mocks = null;
        Field fieldToBeInjected = new Field("testField", Object.class, MockObject.class);
        Object fieldInstance = new MockObject();

        mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidateWithNullFieldToBeInjected() {
        MockCandidateFilter mockCandidateFilter = (mockCandidates, field, fieldInstance) -> mockCandidates.get(0);

        Collection<Object> mocks = Arrays.asList(new MockObject(), new MockObject(), new MockObject());
        Field fieldToBeInjected = null;
        Object fieldInstance = new MockObject();

        mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidateWithNullFieldInstance() {
        MockCandidateFilter mockCandidateFilter = (mockCandidates, field, fieldInstance) -> mockCandidates.get(0);

        Collection<Object> mocks = Arrays.asList(new MockObject(), new MockObject(), new MockObject());
        Field fieldToBeInjected = new Field("testField", Object.class, MockObject.class);
        Object fieldInstance = null;

        mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);
    }

    @Test
    public void testFilterCandidateWithEmptyMocks() {
        MockCandidateFilter mockCandidateFilter = (mockCandidates, field, fieldInstance) -> mockCandidates.get(0);

        Collection<Object> mocks = new ArrayList<>();
        Field fieldToBeInjected = new Field("testField", Object.class, MockObject.class);
        Object fieldInstance = new MockObject();

        OngoingInjecter result = mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        Assert.assertNull(result);
    }

    @Test
    public void testFilterCandidateWithNullMockInMocks() {
        MockCandidateFilter mockCandidateFilter = (mockCandidates, field, fieldInstance) -> mockCandidates.get(0);

        Collection<Object> mocks = Arrays.asList(null, new MockObject(), new MockObject());
        Field fieldToBeInjected = new Field("testField", Object.class, MockObject.class);
        Object fieldInstance = new MockObject();

        mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);
    }

    @Test
    public void testFilterCandidateWithMultipleMocks() {
        MockCandidateFilter mockCandidateFilter = (mockCandidates, field, fieldInstance) -> mockCandidates.get(1);

        Collection<Object> mocks = Arrays.asList(new MockObject(), new MockObject(), new MockObject());
        Field fieldToBeInjected = new Field("testField", Object.class, MockObject.class);
        Object fieldInstance = new MockObject();

        OngoingInjecter result = mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        Assert.assertEquals(mocks.get(1), result);
    }

}