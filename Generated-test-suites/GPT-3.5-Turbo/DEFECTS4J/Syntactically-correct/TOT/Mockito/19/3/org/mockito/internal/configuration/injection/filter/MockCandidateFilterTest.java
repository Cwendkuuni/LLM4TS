import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.ArrayList;

public class MockCandidateFilterTest {

    private MockCandidateFilter filter;
    private Collection<Object> mocks;
    private Field field;
    private Object instance;

    @Before
    public void setUp() {
        mocks = new ArrayList<Object>();
        field = null;
        instance = null;
        filter = new MockCandidateFilterImpl();
    }

    @Test
    public void testFilterCandidate_typicalUseCase() {
        // Arrange
        mocks.add(new Object());
        field = getField();
        instance = new Object();

        // Act
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, instance);

        // Assert
        assertNotNull(injecter);
        assertEquals(mocks, injecter.getMocks());
        assertEquals(field, injecter.getFieldToBeInjected());
        assertEquals(instance, injecter.getFieldInstance());
    }

    @Test
    public void testFilterCandidate_edgeCaseNoMocks() {
        // Arrange
        field = getField();
        instance = new Object();

        // Act
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, instance);

        // Assert
        assertNotNull(injecter);
        assertEquals(mocks, injecter.getMocks());
        assertEquals(field, injecter.getFieldToBeInjected());
        assertEquals(instance, injecter.getFieldInstance());
    }

    @Test
    public void testFilterCandidate_errorScenarioNullField() {
        // Arrange
        mocks.add(new Object());
        instance = new Object();

        // Act
        try {
            filter.filterCandidate(mocks, null, instance);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Assert
            assertEquals("Field to be injected cannot be null", e.getMessage());
        }
    }

    @Test
    public void testFilterCandidate_errorScenarioNullMocks() {
        // Arrange
        field = getField();
        instance = new Object();

        // Act
        try {
            filter.filterCandidate(null, field, instance);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Assert
            assertEquals("Mocks collection cannot be null", e.getMessage());
        }
    }

    @Test
    public void testFilterCandidate_errorScenarioNullInstance() {
        // Arrange
        mocks.add(new Object());
        field = getField();

        // Act
        try {
            filter.filterCandidate(mocks, field, null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Assert
            assertEquals("Field instance cannot be null", e.getMessage());
        }
    }

    private Field getField() {
        try {
            return MockCandidateFilter.class.getDeclaredField("fieldToBeInjected");
        } catch (NoSuchFieldException e) {
            return null;
        }
    }
}