import org.jsoup.nodes.Element;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class CombiningEvaluatorTest {
    
    private CombiningEvaluator.And andEvaluator;
    private CombiningEvaluator.Or orEvaluator;
    
    @Before
    public void setUp() {
        andEvaluator = new CombiningEvaluator.And();
        orEvaluator = new CombiningEvaluator.Or();
    }
    
    @Test
    public void testAndMatches_ShouldReturnTrue_WhenAllEvaluatorsMatch() {
        // Arrange
        Element root = new Element("root");
        Element node = new Element("node");
        Evaluator evaluator1 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        };
        Evaluator evaluator2 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        };
        andEvaluator.add(evaluator1);
        andEvaluator.add(evaluator2);
        
        // Act
        boolean result = andEvaluator.matches(root, node);
        
        // Assert
        assertTrue(result);
    }
    
    @Test
    public void testAndMatches_ShouldReturnFalse_WhenAnyEvaluatorDoesNotMatch() {
        // Arrange
        Element root = new Element("root");
        Element node = new Element("node");
        Evaluator evaluator1 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        };
        Evaluator evaluator2 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return false;
            }
        };
        andEvaluator.add(evaluator1);
        andEvaluator.add(evaluator2);
        
        // Act
        boolean result = andEvaluator.matches(root, node);
        
        // Assert
        assertFalse(result);
    }
    
    @Test
    public void testOrMatches_ShouldReturnTrue_WhenAnyEvaluatorMatches() {
        // Arrange
        Element root = new Element("root");
        Element node = new Element("node");
        Evaluator evaluator1 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return false;
            }
        };
        Evaluator evaluator2 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        };
        orEvaluator.add(evaluator1);
        orEvaluator.add(evaluator2);
        
        // Act
        boolean result = orEvaluator.matches(root, node);
        
        // Assert
        assertTrue(result);
    }
    
    @Test
    public void testOrMatches_ShouldReturnFalse_WhenNoEvaluatorMatches() {
        // Arrange
        Element root = new Element("root");
        Element node = new Element("node");
        Evaluator[] evaluators = new Evaluator[0];
        List<Evaluator> evaluatorList = Arrays.asList(evaluators);
        orEvaluator = new CombiningEvaluator.Or(evaluatorList);
        
        // Act
        boolean result = orEvaluator.matches(root, node);
        
        // Assert
        assertFalse(result);
    }

    @Test
    public void testOrAdd_ShouldAddEvaluator_WhenCalled() {
        // Arrange
        Evaluator evaluator = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        };
        
        // Act
        orEvaluator.add(evaluator);
        
        // Assert
        assertEquals(1, orEvaluator.evaluators.size());
    }
    
    @Test
    public void testToString_ShouldReturnExpectedString_WhenCalled() {
        // Arrange
        Evaluator evaluator1 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        };
        Evaluator evaluator2 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        };
        andEvaluator.add(evaluator1);
        andEvaluator.add(evaluator2);
        
        // Act
        String result = andEvaluator.toString();
        
        // Assert
        assertEquals("evaluator1 evaluator2", result);
    }
}