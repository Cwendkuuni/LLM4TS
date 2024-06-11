import org.junit.Assert;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.*;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.ArrayList;

public class FinalMockCandidateFilterTest {
    
    @Test
    public void testFilterCandidate_singleMock() {
        // Arrange
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = new ArrayList<>();
        Object mock = new Object();
        mocks.add(mock);
        Field field = null;
        Object fieldInstance = null;
        
        // Act
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        
        // Assert
        Assert.assertNotNull(injecter);
        Assert.assertTrue(injecter.thenInject());
    }
    
    @Test
    public void testFilterCandidate_multipleMocks() {
        // Arrange
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = new Object();
        Object mock2 = new Object();
        mocks.add(mock1);
        mocks.add(mock2);
        Field field = null;
        Object fieldInstance = null;
        
        // Act
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        
        // Assert
        Assert.assertNotNull(injecter);
        Assert.assertFalse(injecter.thenInject());
    }
    
    @Test(expected = MockitoException.class)
    public void testFilterCandidate_exception() {
        // Arrange
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = new ArrayList<>();
        Field field = null;
        Object fieldInstance = null;
        
        // Act
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        injecter.thenInject();
        
        // Assert
        // Exception is expected
    }
}