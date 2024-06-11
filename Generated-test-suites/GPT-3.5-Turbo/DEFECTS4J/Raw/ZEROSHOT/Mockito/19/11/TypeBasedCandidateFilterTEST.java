import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;

public class TypeBasedCandidateFilterTest {

    private TypeBasedCandidateFilter filter;
    private MockCandidateFilter nextMock;
    private Collection<Object> mocks;
    private Field field;
    private Object fieldInstance;

    @Before
    public void setUp() {
        nextMock = Mockito.mock(MockCandidateFilter.class);
        filter = new TypeBasedCandidateFilter(nextMock);
        mocks = new ArrayList<Object>();
        field = TestClass.class.getField("testField");
        fieldInstance = new TestClass();
    }

    @Test
    public void filterCandidate_shouldCallNextFilterWithMatchingMocks() {
        // Arrange
        Object mock1 = new MockClass1();
        Object mock2 = new MockClass2();
        Object mock3 = new MockClass3();
        mocks.add(mock1);
        mocks.add(mock2);
        mocks.add(mock3);

        // Act
        filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        Mockito.verify(nextMock).filterCandidate(Mockito.argThat(argument -> argument.contains(mock1) && argument.contains(mock2) && argument.contains(mock3)), Mockito.eq(field), Mockito.eq(fieldInstance));
    }

    @Test
    public void filterCandidate_shouldNotCallNextFilterWithNonMatchingMocks() {
        // Arrange
        Object mock1 = new NonMockClass1();
        Object mock2 = new NonMockClass2();
        Object mock3 = new NonMockClass3();
        mocks.add(mock1);
        mocks.add(mock2);
        mocks.add(mock3);

        // Act
        filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        Mockito.verify(nextMock, Mockito.never()).filterCandidate(Mockito.anyCollection(), Mockito.eq(field), Mockito.eq(fieldInstance));
    }
    
    public class TestClass {
        public String testField;
    }

    public class MockClass1 {
        public String mockField1;
    }
    
    public class MockClass2 {
        public String mockField2;
    }
    
    public class MockClass3 {
        public String mockField3;
    }
    
    public class NonMockClass1 {
        public String nonMockField1;
    }
    
    public class NonMockClass2 {
        public String nonMockField2;
    }
    
    public class NonMockClass3 {
        public String nonMockField3;
    }
}