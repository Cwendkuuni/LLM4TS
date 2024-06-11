import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Element;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@RunWith(JUnit4.class)
public class CombiningEvaluatorTest {
    
    @Test
    public void andEvaluator_Matches_ReturnsTrue() {
        // Arrange
        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And();
        MockEvaluator mockEvaluator1 = new MockEvaluator(true);
        MockEvaluator mockEvaluator2 = new MockEvaluator(true);
        
        // Act
        andEvaluator.add(mockEvaluator1);
        andEvaluator.add(mockEvaluator2);
        
        // Assert
        assert(andEvaluator.matches(null, null));
    }
    
    @Test
    public void andEvaluator_OneEvaluatorFails_ReturnsFalse() {
        // Arrange
        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And();
        MockEvaluator mockEvaluator1 = new MockEvaluator(true);
        MockEvaluator mockEvaluator2 = new MockEvaluator(false);
        
        // Act
        andEvaluator.add(mockEvaluator1);
        andEvaluator.add(mockEvaluator2);
        
        // Assert
        assert(!andEvaluator.matches(null, null));
    }
    
    @Test
    public void andEvaluator_NoEvaluators_ReturnsTrue() {
        // Arrange
        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And();
        
        // Assert
        assert(andEvaluator.matches(null, null));
    }
    
    @Test
    public void orEvaluator_Matches_ReturnsTrue() {
        // Arrange
        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or();
        MockEvaluator mockEvaluator1 = new MockEvaluator(true);
        MockEvaluator mockEvaluator2 = new MockEvaluator(false);
        MockEvaluator mockEvaluator3 = new MockEvaluator(false);
        
        // Act
        orEvaluator.add(mockEvaluator1);
        orEvaluator.add(mockEvaluator2);
        orEvaluator.add(mockEvaluator3);
        
        // Assert
        assert(orEvaluator.matches(null, null));
    }
    
    @Test
    public void orEvaluator_NoEvaluators_ReturnsFalse() {
        // Arrange
        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or();
        
        // Assert
        assert(!orEvaluator.matches(null, null));
    }
    
    @Test
    public void orEvaluator_AllEvaluatorsFail_ReturnsFalse() {
        // Arrange
        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or();
        MockEvaluator mockEvaluator1 = new MockEvaluator(false);
        MockEvaluator mockEvaluator2 = new MockEvaluator(false);
        
        // Act
        orEvaluator.add(mockEvaluator1);
        orEvaluator.add(mockEvaluator2);
        
        // Assert
        assert(!orEvaluator.matches(null, null));        
    }
    
    private static class MockEvaluator extends Evaluator {
        private boolean result;
        
        public MockEvaluator(boolean result) {
            this.result = result;
        }
        
        @Override
        public boolean matches(Element root, Element node) {
            return result;
        }
    }
}