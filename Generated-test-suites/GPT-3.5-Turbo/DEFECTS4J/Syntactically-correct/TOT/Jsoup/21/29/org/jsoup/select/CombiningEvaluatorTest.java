import org.jsoup.nodes.Element;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.jsoup.select.CombiningEvaluator.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class CombiningEvaluatorTest {

    @Test
    public void testAndMatches() {
        Element root = Mockito.mock(Element.class);
        Element child = Mockito.mock(Element.class);
        
        Evaluator evaluator1 = Mockito.mock(Evaluator.class);
        Evaluator evaluator2 = Mockito.mock(Evaluator.class);
        
        Mockito.when(evaluator1.matches(root, child)).thenReturn(true);
        Mockito.when(evaluator2.matches(root, child)).thenReturn(true);
        
        And and = new And(Arrays.asList(evaluator1, evaluator2));
        
        assertTrue(and.matches(root, child));
    }
    
    @Test
    public void testAndNoMatches() {
        Element root = Mockito.mock(Element.class);
        Element child = Mockito.mock(Element.class);
        
        Evaluator evaluator1 = Mockito.mock(Evaluator.class);
        Evaluator evaluator2 = Mockito.mock(Evaluator.class);
        
        Mockito.when(evaluator1.matches(root, child)).thenReturn(true);
        Mockito.when(evaluator2.matches(root, child)).thenReturn(false);
        
        And and = new And(Arrays.asList(evaluator1, evaluator2));
        
        assertFalse(and.matches(root, child));
    }
    
    @Test
    public void testOrMatches() {
        Element root = Mockito.mock(Element.class);
        Element child = Mockito.mock(Element.class);
        
        Evaluator evaluator1 = Mockito.mock(Evaluator.class);
        Evaluator evaluator2 = Mockito.mock(Evaluator.class);
        
        Mockito.when(evaluator1.matches(root, child)).thenReturn(false);
        Mockito.when(evaluator2.matches(root, child)).thenReturn(true);
        
        Or or = new Or(Arrays.asList(evaluator1, evaluator2));
        
        assertTrue(or.matches(root, child));
    }
    
    @Test
    public void testOrNoMatches() {
        Element root = Mockito.mock(Element.class);
        Element child = Mockito.mock(Element.class);
        
        Evaluator evaluator1 = Mockito.mock(Evaluator.class);
        Evaluator evaluator2 = Mockito.mock(Evaluator.class);
        
        Mockito.when(evaluator1.matches(root, child)).thenReturn(false);
        Mockito.when(evaluator2.matches(root, child)).thenReturn(false);
        
        Or or = new Or(Arrays.asList(evaluator1, evaluator2));
        
        assertFalse(or.matches(root, child));
    }
    
    @Test
    public void testOrMultipleMatches() {
        Element root = Mockito.mock(Element.class);
        Element child = Mockito.mock(Element.class);
        
        Evaluator evaluator1 = Mockito.mock(Evaluator.class);
        Evaluator evaluator2 = Mockito.mock(Evaluator.class);
        Evaluator evaluator3 = Mockito.mock(Evaluator.class);
        
        Mockito.when(evaluator1.matches(root, child)).thenReturn(false);
        Mockito.when(evaluator2.matches(root, child)).thenReturn(true);
        Mockito.when(evaluator3.matches(root, child)).thenReturn(true);
        
        Or or = new Or(Arrays.asList(evaluator1, evaluator2, evaluator3));
        
        assertTrue(or.matches(root, child));
    }
    
    @Test
    public void testOrEmptyEvaluators() {
        Element root = Mockito.mock(Element.class);
        Element child = Mockito.mock(Element.class);
        
        Or or = new Or(Collections.emptyList());
        
        assertFalse(or.matches(root, child));
    }
}