import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class TypeBasedCandidateFilterTest {

    private TypeBasedCandidateFilter filter;
    private MockCandidateFilter nextFilter;

    @Before
    public void setup() {
        nextFilter = mock(MockCandidateFilter.class);
        filter = new TypeBasedCandidateFilter(nextFilter);
    }

    @Test
    public void testFilterCandidateWithTypeMatch() {
        // given
        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = mock(String.class);
        Object mock2 = mock(Number.class);
        mocks.add(mock1);
        mocks.add(mock2);

        Field field = new Field("testField", String.class, null, null);
        Object fieldInstance = spy(new Object() {
            public String testField;
        });

        // when
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // then
        assertEquals(mock1, result.getMock());
        // check if next filter was called
        Mockito.verify(nextFilter).filterCandidate(Mockito.eq(new ArrayList<>()), Mockito.eq(field), Mockito.eq(fieldInstance));
    }

    @Test
    public void testFilterCandidateWithNoTypeMatch() {
        // given
        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = mock(String.class);
        Object mock2 = mock(String.class);
        mocks.add(mock1);
        mocks.add(mock2);

        Field field = new Field("testField", Number.class, null, null);
        Object fieldInstance = spy(new Object() {
            public int testField;
        });

        // when
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // then
        assertEquals(null, result);
        // check if next filter was not called
        Mockito.verifyNoInteractions(nextFilter);
    }

    @Test
    public void testFilterCandidateWithEmptyList() {
        // given
        Collection<Object> mocks = new ArrayList<>();

        Field field = new Field("testField", String.class, null, null);
        Object fieldInstance = spy(new Object() {
            public String testField;
        });

        // when
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // then
        assertEquals(null, result);
        // check if next filter was not called
        Mockito.verifyNoInteractions(nextFilter);
    }

    @Test
    public void testFilterCandidateWithNullField() {
        // given
        Collection<Object> mocks = new ArrayList<>();
        Object mock = mock(String.class);
        mocks.add(mock);

        Field field = null;
        Object fieldInstance = spy(new Object() {
            public String testField;
        });

        // when
        try {
            OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        } catch (IllegalArgumentException e) {
            // then
            assertEquals("Field can't be null", e.getMessage());
            // check if next filter was not called
            Mockito.verifyNoInteractions(nextFilter);
            return;
        }

        // fail
        fail("Expected IllegalArgumentException");
    }

    @Test
    public void testFilterCandidateWithNullMockCollection() {
        // given
        Field field = new Field("testField", String.class, null, null);
        Object fieldInstance = spy(new Object() {
            public String testField;
        });

        // when
        try {
            OngoingInjecter result = filter.filterCandidate(null, field, fieldInstance);
        } catch (IllegalArgumentException e) {
            // then
            assertEquals("Collection of mocks can't be null", e.getMessage());
            // check if next filter was not called
            Mockito.verifyNoInteractions(nextFilter);
            return;
        }

        // fail
        fail("Expected IllegalArgumentException");
    }

    @Test
    public void testFilterCandidateWithNullFieldInstance() {
        // given
        Collection<Object> mocks = new ArrayList<>();
        Object mock = mock(String.class);
        mocks.add(mock);

        Field field = new Field("testField", String.class, null, null);

        // when
        try {
            OngoingInjecter result = filter.filterCandidate(mocks, field, null);
        } catch (IllegalArgumentException e) {
            // then
            assertEquals("Field instance can't be null", e.getMessage());
            // check if next filter was not called
            Mockito.verifyNoInteractions(nextFilter);
            return;
        }

        // fail
        fail("Expected IllegalArgumentException");
    }

    @Test
    public void testFilterCandidateWithFieldTypeMismatch() {
        // given
        Collection<Object> mocks = new ArrayList<>();
        Object mock = mock(String.class);
        mocks.add(mock);

        Field field = new Field("testField", Integer.TYPE, null, null);
        Object fieldInstance = spy(new Object() {
            public String testField;
        });

        // when
        try {
            OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        } catch (ClassCastException e) {
            // then
            assertEquals("Type mismatch: incompatible types: java.lang.String cannot be converted to java.lang.Integer", e.getMessage());
            // check if next filter was not called
            Mockito.verifyNoInteractions(nextFilter);
            return;
        }

        // fail
        fail("Expected ClassCastException");
    }
}