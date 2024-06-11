import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import org.jsoup.nodes.Element;
import org.jsoup.select.CombiningEvaluator;
import org.jsoup.select.Evaluator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class CombiningEvaluatorTest {
    
    private CombiningEvaluator.And andEvaluator;
    private CombiningEvaluator.Or orEvaluator;
    private Element rootElement;
    private Element nodeElement;
    
    @Before
    public void setup() {
        Evaluator evaluator1 = Mockito.mock(Evaluator.class);
        Evaluator evaluator2 = Mockito.mock(Evaluator.class);
        Evaluator evaluator3 = Mockito.mock(Evaluator.class);
        List<Evaluator> evaluators1 = new ArrayList<>();
        evaluators1.add(evaluator1);
        evaluators1.add(evaluator2);
        andEvaluator = new CombiningEvaluator.And(evaluators1);
        
        List<Evaluator> evaluators2 = new ArrayList<>();
        evaluators2.add(evaluator1);
        evaluators2.add(evaluator2);
        evaluators2.add(evaluator3);
        orEvaluator = new CombiningEvaluator.Or(evaluators2);
        
        rootElement = Mockito.mock(Element.class);
        nodeElement = Mockito.mock(Element.class);
    }
    
    @Test
    public void testAndMatches() {
        Evaluator evaluator1 = Mockito.mock(Evaluator.class);
        Evaluator evaluator2 = Mockito.mock(Evaluator.class);
        
        Mockito.when(evaluator1.matches(rootElement, nodeElement)).thenReturn(true);
        Mockito.when(evaluator2.matches(rootElement, nodeElement)).thenReturn(true);
        
        boolean result = andEvaluator.matches(rootElement, nodeElement);
        assertEquals(result, true);
    }
    
    @Test
    public void testAndDoesNotMatch() {
        Evaluator evaluator1 = Mockito.mock(Evaluator.class);
        Evaluator evaluator2 = Mockito.mock(Evaluator.class);
        
        Mockito.when(evaluator1.matches(rootElement, nodeElement)).thenReturn(true);
        Mockito.when(evaluator2.matches(rootElement, nodeElement)).thenReturn(false);
        
        boolean result = andEvaluator.matches(rootElement, nodeElement);
        assertEquals(result, false);
    }
    
    @Test
    public void testOrMatches() {
        Evaluator evaluator1 = Mockito.mock(Evaluator.class);
        Evaluator evaluator2 = Mockito.mock(Evaluator.class);
        Evaluator evaluator3 = Mockito.mock(Evaluator.class);
        
        Mockito.when(evaluator1.matches(rootElement, nodeElement)).thenReturn(false);
        Mockito.when(evaluator2.matches(rootElement, nodeElement)).thenReturn(false);
        Mockito.when(evaluator3.matches(rootElement, nodeElement)).thenReturn(true);
        
        boolean result = orEvaluator.matches(rootElement, nodeElement);
        assertEquals(result, true);
    }
    
    @Test
    public void testOrDoesNotMatch() {
        Evaluator evaluator1 = Mockito.mock(Evaluator.class);
        Evaluator evaluator2 = Mockito.mock(Evaluator.class);
        Evaluator evaluator3 = Mockito.mock(Evaluator.class);
        
        Mockito.when(evaluator1.matches(rootElement, nodeElement)).thenReturn(false);
        Mockito.when(evaluator2.matches(rootElement, nodeElement)).thenReturn(false);
        Mockito.when(evaluator3.matches(rootElement, nodeElement)).thenReturn(false);
        
        boolean result = orEvaluator.matches(rootElement, nodeElement);
        assertEquals(result, false);
    }
    
    @Test
    public void testOrSingleEvaluatorMatches() {
        Evaluator evaluator1 = Mockito.mock(Evaluator.class);
        
        Mockito.when(evaluator1.matches(rootElement, nodeElement)).thenReturn(true);
        
        boolean result = orEvaluator.matches(rootElement, nodeElement);
        assertEquals(result, true);
    }
    
    @Test
    public void testOrSingleEvaluatorDoesNotMatch() {
        Evaluator evaluator1 = Mockito.mock(Evaluator.class);
        
        Mockito.when(evaluator1.matches(rootElement, nodeElement)).thenReturn(false);
        
        boolean result = orEvaluator.matches(rootElement, nodeElement);
        assertEquals(result, false);
    }
    
    @Test
    public void testAndToString() {
        String result = andEvaluator.toString();
        assertEquals(result, "");
    }
    
    @Test
    public void testOrToString() {
        String result = orEvaluator.toString();
        assertEquals(result, "");
    }
}