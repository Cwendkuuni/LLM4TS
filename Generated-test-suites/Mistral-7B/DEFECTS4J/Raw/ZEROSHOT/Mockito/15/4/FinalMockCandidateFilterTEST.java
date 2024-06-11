import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestRule;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.OngoingInjecter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

public class FinalMockCandidateFilterTest {

    @Rule
    public TestRule mockitoRule = MockitoJUnit.rule();
    @InjectMocks
    private FinalMockCandidateFilter filter;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFilterCandidateWithOneMockAndOneField() {
        Object mock = mock(Object.class);
        Field field = new Field("testField", Object.class);
        Object fieldInstance = new Object();

        OngoingInjecter result = filter.filterCandidate(Collections.singletonList(mock), field, fieldInstance);

        assertNotNull(result);
        assertFalse(result.thenInject());

        try {
            new FieldSetter(fieldInstance, field).set(mock);
        } catch (Exception e) {
            throw new MockitoException("Unexpected exception during test", e);
        }

        // Calling thenInject() again should succeed
        result.thenInject();
    }

    @Test
    public void testFilterCandidateWithOneMockAndMultipleFields() {
        Object mock = mock(Object.class);
        Object fieldInstance1 = new Object();
        Object fieldInstance2 = new Object();
        Field field1 = new Field("testField1", Object.class);
        Field field2 = new Field("testField2", Object.class);

        OngoingInjecter result = filter.filterCandidate(Collections.singletonList(mock), field1, fieldInstance1);
        assertFalse(result.thenInject());

        result = filter.filterCandidate(Collections.singletonList(mock), field2, fieldInstance2);
        assertFalse(result.thenInject());
    }

    @Test
    public void testFilterCandidateWithMultipleMocksAndOneField() {
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        Object fieldInstance = new Object();
        Field field = new Field("testField", Object.class);

        assertThrows(MockitoException.class, () -> filter.filterCandidate(List.of(mock1, mock2), field, fieldInstance));
    }

    @Test
    public void testFilterCandidateWithMultipleMocksAndMultipleFields() {
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        Object fieldInstance1 = new Object();
        Object fieldInstance2 = new Object();
        Field field1 = new Field("testField1", Object.class);
        Field field2 = new Field("testField2", Object.class);

        assertThrows(MockitoException.class, () -> filter.filterCandidate(List.of(mock1, mock2), field1, fieldInstance1));
        assertThrows(MockitoException.class, () -> filter.filterCandidate(List.of(mock1, mock2), field2, fieldInstance2));
    }

    @Test
    public void testFilterCandidateWithNoMocksAndOneField() {
        Object fieldInstance = new Object();
        Field field = new Field("testField", Object.class);

        assertThrows(MockitoException.class, () -> filter.filterCandidate(Collections.emptyList(), field, fieldInstance));
    }

    @Test
    public void testFilterCandidateWithNoMocksAndMultipleFields() {
        Object fieldInstance1 = new Object();
        Object fieldInstance2 = new Object();
        Field field1 = new Field("testField1", Object.class);
        Field field2 = new Field("testField2", Object.class);

        assertThrows(MockitoException.class, () -> filter.filterCandidate(Collections.emptyList(), field1, fieldInstance1));
        assertThrows(MockitoException.class, () -> filter.filterCandidate(Collections.emptyList(), field2, fieldInstance2));
    }
}